package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class TestHelper {
    public static CreateUserRequest getUserRequest(String username, String password, String confirmPassword){
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername(username);;
        req.setPassword(password);
        req.setConfirmPassword(confirmPassword);
        return req;
    }
    public static ModifyCartRequest getModifyCartRequest(String username, long itemId, int quantity){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;
    }
    public static User getUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setCart(getCart());
        return user;
    }
    public static User getLoginUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("somepassword");
        return user;
    }

    public static Cart getCart(){
        Cart cart = new Cart();
        cart.addItem(getItem(1L,"pen", BigDecimal.valueOf(3),""));
        cart.addItem(getItem(2L,"pencil",BigDecimal.valueOf(2),""));
        return cart;
    }
    public static Item getItem(long id, String name, BigDecimal price, String desc){
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        item.setDescription(desc);
        return item;
    }
}
