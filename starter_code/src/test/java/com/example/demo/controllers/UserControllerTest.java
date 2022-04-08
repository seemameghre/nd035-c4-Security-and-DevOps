package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class UserControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    private JacksonTester<CreateUserRequest> reqestjson;
    @Autowired
    private JacksonTester<User> userjson;

    @MockBean
    UserRepository userRepository;
    @MockBean
    CartRepository cartRepository;
    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void init(){
        User user = TestHelper.getUser();
        given(bCryptPasswordEncoder.encode("testpassword")).willReturn("hashedpwd");
        given(userRepository.findByUsername("testuser")).willReturn(user);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.save(any())).willReturn(user);
    }

/*    private User getUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        return user;
    }*/
    @Test
    public void create_user_success() throws Exception {
        CreateUserRequest req = TestHelper.getUserRequest("testuser","testpassword","testpassword");
        mvc.perform(post(new URI("/api/user/create"))
                        .content(reqestjson.write(req).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void find_by_id() throws Exception {
        User user = TestHelper.getUser();
        mvc.perform(get("/api/user/id/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(userjson.write(user).getJson()));
    }
    @Test
    public void find_by_username() throws Exception{
        User user = TestHelper.getUser();
        mvc.perform(get("/api/user/testuser")
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser")))
                .andExpect(status().isOk())
                .andExpect(content().json(userjson.write(user).getJson()));
    }
    @Test
    public void create_user_mismatch_password() throws Exception {
        CreateUserRequest req = TestHelper.getUserRequest("testuser","testpassword","password");
        mvc.perform(post(new URI("/api/user/create"))
                        .content(reqestjson.write(req).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(userRepository,never()).save(any());
    }
}
