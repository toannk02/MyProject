package com.example.myproject.service;

import com.example.myproject.dto.request.CartRequest;
import com.example.myproject.entity.*;
import com.example.myproject.exception.NameAlreadyExistsException;
import com.example.myproject.exception.RecordNotFoundException;
import com.example.myproject.repository.*;
import com.example.myproject.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public String addToCard(Integer userId, List<CartRequest.ProductQuantity> items){
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RecordNotFoundException("User"));

        Cart cart = cartRepository.findByUserIdAndStatus(userId, Constants.status.ACTIVE);
        if(cart == null){
            cart = new Cart();
            cart.setStatus(Constants.status.ACTIVE);
            cart.setItems(new ArrayList<>());
            cart.setUser(userRepository.findById(Long.valueOf(userId)).orElseThrow());
            cartRepository.save(cart);
        }

        // Kiểm tra xem sản phẩm đã có trong card chưa?
        for (CartRequest.ProductQuantity item : items) {
            Integer productId = item.getProductId();
            Integer quantity = item.getQuantity();

            Optional<CartItem> existingCartItem  = cart.getItems().stream()
                    .filter(cartItem -> cartItem.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingCartItem.isPresent()) {
                existingCartItem.get().setQuantity(existingCartItem.get().getQuantity() + quantity);
            }else {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RecordNotFoundException("Product"));
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(quantity);
                newItem.setPriceAtAddedTime(product.getPrice());
                cart.getItems().add(newItem);
            }
        }

        cartRepository.save(cart);
        return "Product is added in cart";
    }

    @Transactional
    public String checkout(Integer userId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Constants.status.ACTIVE);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("No active cart or cart is empty!");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderItems(new ArrayList<>());
        order.setStatus(Constants.status.ORDERED);

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

        cart.setStatus(Constants.status.ORDERED);
        cartRepository.save(cart);

        return "Checkout thành công";
    }
}
 