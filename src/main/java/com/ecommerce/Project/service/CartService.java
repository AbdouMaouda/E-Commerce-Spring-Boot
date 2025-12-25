package com.ecommerce.Project.service;

import com.ecommerce.Project.payload.CartDTO;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    public CartDTO addProductToCart(Long productId, Integer quantity);
}
