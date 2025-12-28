package com.ecommerce.Project.service;

import com.ecommerce.Project.payload.OrderDTO;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);
}
