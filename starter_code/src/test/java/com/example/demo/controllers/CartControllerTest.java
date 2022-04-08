package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CartControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    private JacksonTester<ModifyCartRequest> reqestjson;
    @MockBean
    UserRepository userRepository;
    @MockBean
    CartRepository cartRepository;
    @MockBean
    ItemRepository itemRepository;

    @Before
    public void setup(){
        given(userRepository.findByUsername("testuser")).willReturn(TestHelper.getUser());
        given(userRepository.findByUsername("testusernull")).willReturn(null);
        given(itemRepository.findById(1L)).willReturn(Optional.of(TestHelper.getItem(1L,"pen",BigDecimal.valueOf(3),"")));
        given(itemRepository.findById(10L)).willReturn(Optional.empty());
    }


    @Test
    public void add_to_cart_success() throws Exception{
        ModifyCartRequest modifyCartRequest = TestHelper.getModifyCartRequest("testuser",1L,3);
        mvc.perform(
                post(new URI("/api/cart/addToCart"))
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                        .content(reqestjson.write(modifyCartRequest).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(cartRepository).save(any());
    }
    @Test
    public void add_to_cart_user_not_found_fails() throws Exception{
        ModifyCartRequest modifyCartRequest = TestHelper.getModifyCartRequest("testusernull",1L,3);
        mvc.perform(
                post(new URI("/api/cart/addToCart"))
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                        .content(reqestjson.write(modifyCartRequest).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(cartRepository,never()).save(any());
    }
    @Test
    public void add_to_cart_item_not_found_fails() throws Exception{
        ModifyCartRequest modifyCartRequest = TestHelper.getModifyCartRequest("testuser",10L,3);
        mvc.perform(
                        post(new URI("/api/cart/addToCart"))
                                .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                                .content(reqestjson.write(modifyCartRequest).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(cartRepository,never()).save(any());
    }
    @Test
    public void remove_from_cart_success() throws Exception{
        ModifyCartRequest modifyCartRequest = TestHelper.getModifyCartRequest("testuser",1L,1);
        mvc.perform(
                        post(new URI("/api/cart/removeFromCart"))
                                .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                                .content(reqestjson.write(modifyCartRequest).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(cartRepository).save(any());
    }
    @Test
    public void remove_from_cart_user_not_found_fails()throws Exception{
        ModifyCartRequest modifyCartRequest = TestHelper.getModifyCartRequest("testusernull",1L,3);
        mvc.perform(
                        post(new URI("/api/cart/removeFromCart"))
                                .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                                .content(reqestjson.write(modifyCartRequest).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(cartRepository,never()).save(any());
    }
    @Test
    public void remove_from_cart_item_not_found_fails()throws Exception{
        ModifyCartRequest modifyCartRequest = TestHelper.getModifyCartRequest("testuser",10L,3);
        mvc.perform(
                        post(new URI("/api/cart/removeFromCart"))
                                .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                                .content(reqestjson.write(modifyCartRequest).getJson())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(cartRepository,never()).save(any());
    }
}
