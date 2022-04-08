package com.example.demo.controllers;

import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class OrderControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    private JacksonTester<UserOrder> json;
    @MockBean
    UserRepository userRepository;
    @MockBean
    OrderRepository orderRepository;

    @Before
    public void setup(){
        given(userRepository.findByUsername("testuser")).willReturn(TestHelper.getUser());
        given(userRepository.findByUsername("testusernull")).willReturn(null);
    }
    @Test
    public void submit_order_success() throws Exception{
        mvc.perform(
                post(new URI("/api/order/submit/testuser"))
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(orderRepository).save(any());
    }
    @Test
    public void submit_order_user_not_found() throws Exception{
        mvc.perform(
                        post(new URI("/api/order/submit/testusernull"))
                                .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(orderRepository,never()).save(any());
    }
    @Test
    public void get_order_history() throws Exception{
        mvc.perform(
                get("/api/order/history/testuser")
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
        )
                .andExpect(status().isOk());
        verify(orderRepository).findByUser(any());
    }
    @Test
    public void get_order_history_user_not_found() throws Exception{
        mvc.perform(
                        get("/api/order/history/testusernull")
                                .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                )
                .andExpect(status().isNotFound());
        verify(orderRepository,never()).findByUser(any());
    }
}
