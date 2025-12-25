package com.ecommerce.Project.service;

import com.ecommerce.Project.exceptions.APIException;
import com.ecommerce.Project.model.Cart;
import com.ecommerce.Project.model.CartItem;
import com.ecommerce.Project.model.Product;
import com.ecommerce.Project.payload.CartDTO;
import com.ecommerce.Project.payload.ProductDTO;
import com.ecommerce.Project.repositories.CartItemRepository;
import com.ecommerce.Project.repositories.CartRepository;
import com.ecommerce.Project.repositories.ProductRepository;
import com.ecommerce.Project.util.AuthUtil;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new APIException("Product not found"));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId
                (cart.getCartId(),
                        productId);

        if (cartItem != null) {
            throw new APIException("Product" + product.getProductName() + "already exists");
        }
        if (product.getQuantity() == 0) {
            throw new APIException(product.getProductName() + "is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Please,make an order of the" + product.getProductName() + "quantity" + "less than or equal to the quantity" + product.getQuantity() + ".");
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getPrice());
        cartItemRepository.save(newCartItem);
        cart.getCartItems().add(newCartItem);

        product.setQuantity(product.getQuantity() - quantity);

        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));

        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
            map.setQuantity(item.getQuantity());
            return map;
        });
        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }


    private Cart createCart() {

        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());

        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.00);
        cart.setUser(authUtil.loggedInUser());

        Cart newCart = cartRepository.save(cart);
        return newCart;

    }
}
