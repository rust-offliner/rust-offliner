package com.offliner.rust.rust_offliner.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.offliner.rust.rust_offliner.security.model.JwtRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void checkIfJwtTokenIsValid() throws Exception {
        JwtRequest credentials = new JwtRequest("testfsdfds", "zjeba");
        String regex = "^(?:[\\w-]*\\.){2}[\\w-]*$";
        mockMvc.perform(
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
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
