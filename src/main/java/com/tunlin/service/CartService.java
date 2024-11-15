package com.tunlin.service;

import com.tunlin.model.Cart;
import com.tunlin.model.CartItem;
import com.tunlin.model.Product;
import com.tunlin.model.User;

public interface CartService {

    public CartItem addCartItem(User user, Product product, String size, int quantity) throws Exception;
    public Cart findUserCart(User user) throws Exception;
}
