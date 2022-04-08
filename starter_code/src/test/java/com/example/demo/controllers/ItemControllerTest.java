package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.awt.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ItemControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    private JacksonTester<Item> json;
    @MockBean
    ItemRepository itemRepository;

    @Before
    public void setup(){
        given(itemRepository.findById(1L)).willReturn(Optional.of(TestHelper.getItem(1L,"pen", BigDecimal.valueOf(3),"")));
        given(itemRepository.findById(10L)).willReturn(Optional.empty());
        given(itemRepository.findByName("pen")).willReturn(Collections.singletonList(TestHelper.getItem(1L,"pen", BigDecimal.valueOf(3),"")));
    }

    @Test
    public void get_item_by_id()throws Exception{
        Item item = TestHelper.getItem(1L,"pen",BigDecimal.valueOf(3),"");
        mvc.perform(
                get("/api/item/1")
                        .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
        )
                .andExpect(status().isOk())
                .andExpect(content().json(json.write(item).getJson()));
    }
    @Test
    public void get_item_by_name()throws Exception{
        mvc.perform(
                        get("/api/item/name/pen")
                                .with(SecurityMockMvcRequestPostProcessors.user("testuser"))
                )
                .andExpect(status().isOk());
    }
}
