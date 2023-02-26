package com.offliner.rust.rust_offliner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.offliner.rust.rust_offliner.interfaces.IServerDao;
import com.offliner.rust.rust_offliner.interfaces.IUserDao;
import com.offliner.rust.rust_offliner.persistence.datamodel.ServerEntity;
import com.offliner.rust.rust_offliner.persistence.datamodel.UserEntity;
import com.offliner.rust.rust_offliner.security.model.JwtRequest;
import com.offliner.rust.rust_offliner.security.model.JwtResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/application-test.properties")
public class ServerRestControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ServerRestControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IServerDao dao;

    @Autowired
    private IUserDao userDao;

    private String authorization;

    private final String username = "testfsdfds";
    private final String password = "zjeba";

    @BeforeEach
    void getCredentials() throws Exception {
        JwtRequest credentials = new JwtRequest(username, password);
        String regex = "^(?:[\\w-]*\\.){2}[\\w-]*$";
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/auth")
                        .content(asJsonString(credentials))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.jwtToken").isString())
                .andExpect(jsonPath("$.jwtToken").value(Matchers.matchesPattern(regex)))
                .andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        JwtResponse response = new ObjectMapper().readValue(responseAsString, JwtResponse.class);
        authorization = "Bearer " + response.jwtToken();
    }

    @BeforeEach
    void clearServersToUsersTable() {
        dao.clearServersToUsersTable();
    }


    //TODO test database
    @Test
    void followServerMakesServerTrackedInDatabase() throws Exception {

        long id = 9565288; //rusticated eu trio monday
        Optional<ServerEntity> serverBefore = dao.findByServerId(id);

        //TODO CHANGE
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/follow/{id}", id)
                        .header("Authorization", authorization)
        )
                .andExpect(status().isCreated())
                .andReturn();

        Optional<ServerEntity> serverAfter = dao.findByServerId(id);
        assertEquals(serverBefore.get(), serverAfter.get());

    }

    @Test
    void followAlreadyTrackedServerThrowsNotAcceptable() throws Exception {
        long id = 9565288; //rusticated eu trio monday

        // make server followed before actual test
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", authorization)
                )
//                .andExpect(status().isCreated())
//                .andExpect(header().exists("X-Rate-Limit-Remaining"))
//                .andExpect(header().exists("Location"))
//                .andExpect(header().string("Location", "http://localhost/api/" + id))
                .andReturn();


        // second call to already tracked server
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", authorization)
                )
                .andExpect(status().isNotAcceptable())
                .andReturn();
    }

    @Test
    void followServerWithoutAuthenticationThrowsException() throws Exception {
        long id = 123;
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                )
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void followServerCorrectlyUpdatesManyToManyRelationshipInDB() throws Exception {
        long id = 9565288; //rusticated eu trio monday
        UserEntity user = userDao.findByUsername(username);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", authorization)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/" + id))
                .andReturn();
        long count = dao.countAllByUsersIsAndServerIdEquals(id, user.getId());

        assertEquals(count, 1);
    }

    @Test
    void followServerMultipleTimesByOneUserThrowsException() throws Exception {
        long id = 123;
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/follow/{id}", id)
                        .header("Authorization", authorization)
        )
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/" + id))
                .andReturn();
        mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/follow/{id}", id)
                        .header("Authorization", authorization)
        )
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.status").value(406))
                .andExpect(jsonPath("$.message").value("You can't follow a server you are already following"))
                .andReturn();
    }

    @Test
    void followServerWithMultipleUsersIsOk() throws Exception {
        JwtRequest secondUserCredentials = new JwtRequest("karczuchowski", "123");
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/auth")
                                .content(asJsonString(secondUserCredentials))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.jwtToken").isString())
                .andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        JwtResponse response = new ObjectMapper().readValue(responseAsString, JwtResponse.class);
        String secondUserAuthorization = "Bearer " + response.jwtToken();
        long id = 123;
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", authorization)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/" + id))
                .andReturn();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", secondUserAuthorization)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/" + id))
                .andReturn();

    }

    @Test
    void followServerWithMultipleUsersIsOkAndThenFollowWithAlreadyUsedUserThrowsException() throws Exception {
        JwtRequest secondUserCredentials = new JwtRequest("karczuchowski", "123");
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/auth")
                                .content(asJsonString(secondUserCredentials))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").exists())
                .andExpect(jsonPath("$.jwtToken").isString())
                .andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        JwtResponse response = new ObjectMapper().readValue(responseAsString, JwtResponse.class);
        String secondUserAuthorization = "Bearer " + response.jwtToken();
        long id = 123;
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", authorization)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/" + id))
                .andReturn();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", secondUserAuthorization)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("X-Rate-Limit-Remaining"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/api/" + id))
                .andReturn();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/follow/{id}", id)
                                .header("Authorization", authorization)
                )
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.status").value(406))
                .andExpect(jsonPath("$.message").value("You can't follow a server you are already following"))
                .andReturn();
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
