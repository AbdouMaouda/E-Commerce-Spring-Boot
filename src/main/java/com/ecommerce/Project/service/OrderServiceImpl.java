package com.ecommerce.Project.service;

import com.ecommerce.Project.exceptions.APIException;
import com.ecommerce.Project.exceptions.ResourceNotFoundException;
import com.ecommerce.Project.model.*;
import com.ecommerce.Project.payload.OrderDTO;
import com.ecommerce.Project.payload.OrderItemDTO;
import com.ecommerce.Project.repositories.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod,
                               String pgName, String pgPaymentId, String pgStatus,
                               String pgResponseMessage) {


        System.out.println("=== ORDER PLACEMENT STARTED ===");
        System.out.println("Email: " + emailId);
        System.out.println("Address ID: " + addressId);

        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }
        System.out.println("Cart found: " + cart.getCartId());

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        System.out.println("Address found: " + address.getAddressId());

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted !");
        order.setAddress(address);
        System.out.println("Order created");

        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
        payment.setOrder(order);
        payment = paymentRepository.save(payment);
        order.setPayment(payment);
        System.out.println("Payment saved");


        Order savedOrder = orderRepository.save(order);
        System.out.println("Order saved: " + savedOrder.getOrderId());


        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty");
        }

        System.out.println("Cart items: " + cartItems.size());


        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        System.out.println("Order items created");


        orderItems = orderItemRepository.saveAll(orderItems);

        System.out.println("Order items saved");


        List<CartItem> cartItemsCopy = new ArrayList<>(cart.getCartItems());

        for (CartItem cartItem : cartItemsCopy) {
            int quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();

            // Check if enough stock available
            if (product.getQuantity() < quantity) {
                throw new APIException("Insufficient stock for product: " + product.getProductName());
            }

            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            cartService.deleteProductFromCart(cart.getCartId(), cartItem.getProduct().getId());
        }


        System.out.println("Products updated and cart cleared");

        System.out.println("=== ABOUT TO MAP TO DTO ===");

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        System.out.println("Order mapped to DTO");


        // Initialize orderItems list if null
        if (orderDTO.getOrderItems() == null) {
            System.out.println("OrderItems was null, initializing...");

            orderDTO.setOrderItems(new ArrayList<>());
        }

        orderItems.forEach(item ->
                orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class))
        );

        System.out.println("Order items mapped");

        orderDTO.setAddressId(addressId);


        System.out.println("=== ORDER PLACEMENT COMPLETED ===");

        return orderDTO;
    }
}