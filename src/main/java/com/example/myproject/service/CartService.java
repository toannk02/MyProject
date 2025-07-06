package com.example.myproject.service;

import com.example.myproject.entity.*;
import com.example.myproject.exception.NameAlreadyExistsException;
import com.example.myproject.exception.RecordNotFoundException;
import com.example.myproject.repository.*;
import com.example.myproject.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private void addToCard(Integer userId, Integer productId, int quantity){
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RecordNotFoundException("User"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, Constants.status.ACTIVE);
        if(cart == null){
            cart = new Cart();
            cart.setUser(userRepository.findById(Long.valueOf(userId)).orElseThrow());
            cartRepository.save(cart);
        }

        boolean exists = false;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                exists = true;
                break;
            }
        }

        // 3. Nếu chưa có thì thêm mới
        if (!exists) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RecordNotFoundException("product"));
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPriceAtAddedTime(product.getPrice()); // giá tại thời điểm thêm
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);
    }

    public void checkout(Integer userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, "ACTIVE");
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("No active cart or cart is empty!");
        }

        Order order = new Order();
        order.setUser(cart.getUser());

        double total = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtOrder(cartItem.getPriceAtAddedTime());
            order.getOrderItems().add(orderItem);

            total += cartItem.getQuantity() * cartItem.getPriceAtAddedTime();
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        cart.setStatus("ORDERED");
        cartRepository.save(cart);
    }
}
 