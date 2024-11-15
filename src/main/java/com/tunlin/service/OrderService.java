package com.tunlin.service;

import com.tunlin.domain.OrderStatus;
import com.tunlin.model.Address;
import com.tunlin.model.Cart;
import com.tunlin.model.Order;
import com.tunlin.model.User;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
    Order findOrderId(Long id);
    List<Order> userOrderHistory(Long userId);
    List<Order> sellerOrder(Long sellerId);
    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Order cancelOrder(Long orderId, User user);
}
