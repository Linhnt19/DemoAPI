package com.tunlin.service.impl;

import com.tunlin.model.CartItem;
import com.tunlin.model.User;
import com.tunlin.repository.CartItemRepository;
import com.tunlin.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws Exception {
        CartItem cartItemFind = findCartItemById(id);
        User cartItemUser = cartItemFind.getCart().getUser();
        if (cartItemUser.getId().equals(userId)){
            cartItemFind.setQuantity(cartItem.getQuantity());
            cartItemFind.setMrpPrice(cartItemFind.getQuantity()*cartItemFind.getProduct().getMrpPrice());
            cartItemFind.setSellingPrice(cartItemFind.getQuantity()*cartItemFind.getProduct().getMrpPrice());
            return cartItemRepository.save(cartItemFind);
        }
        throw new Exception("you can't update this cartItem");

    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws Exception {

        CartItem cartItem = findCartItemById(cartItemId);
        User cartItemUser = cartItem.getCart().getUser();

        if (cartItemUser.getId().equals(userId)){
            cartItemRepository.delete(cartItem);
        }else
            throw new Exception("You can't delete this item");

    }

    @Override
    public CartItem findCartItemById(Long id) throws Exception {
        return cartItemRepository.findById(id).orElseThrow(()->
            new Exception("Cart item not found with id"+ id));
    }
}
