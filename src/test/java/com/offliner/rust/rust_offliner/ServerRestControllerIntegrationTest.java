package com.offliner.rust.rust_offliner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.offliner.rust.rust_offliner.security.model.JwtRequest;
import com.offliner.rust.rust_offliner.security.model.JwtResponse;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(locations = "/application-test.properties")
public class ServerRestControllerIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ServerRestControllerIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    private String authorization;

    @BeforeEach
     void getCredentials() throws Exception {
        JwtRequest credentials = new JwtRequest("testfsdfds", "zjeba");
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken").value(Matchers.matchesPattern(regex)))
                .andReturn();
        String responseAsString = result.getResponse().getContentAsString();
        JwtResponse response = new ObjectMapper().readValue(responseAsString, JwtResponse.class);
        authorization = response.jwtToken();
    }

    @Test
    void followServerDoesntChangeDataInDatabaseIfItsAlreadyPresent() {
        long id = 123;
//        mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/follow/{id}", id)
//                        .headers()
//        )
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
