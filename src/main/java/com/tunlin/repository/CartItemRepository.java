package com.tunlin.repository;

import com.tunlin.model.Cart;
import com.tunlin.model.CartItem;
import com.tunlin.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);

}
