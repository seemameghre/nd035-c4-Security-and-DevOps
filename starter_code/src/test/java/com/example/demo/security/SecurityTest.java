package com.example.demo.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class SecurityTest {
    @Autowired
    MockMvc mvc;

    private static final String json = "{\"username\": \"testuser\",\"password\": \"somepassword\"}";
    private static final String jwt = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiZXhwIjoxNjUwMDI5MTc3fQ.BQKQN-jk6T1YRyFM4y7Xjru1fvnKklf2S65kPcPGUQyTlLv7K89aeeoqn5PizKpFfEOT6C3mV_e_zwe-ZOYuHA";

    @Test
    public void user_not_logged_in() throws Exception{
        mvc.perform(
                get("/api/user/id/1")
        ).andExpect(status().isUnauthorized());
    }
    @Test
    public void user_not_created() throws Exception{

        mvc.perform(
                        post(new URI("/login"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isUnauthorized());
    }
    @Test
    public void wrong_user_token() throws Exception{
        mvc.perform(
                get("/api/user/id/1")
                        .header("Authorization",jwt)
        ).andExpect(status().isNotFound());
    }
}
