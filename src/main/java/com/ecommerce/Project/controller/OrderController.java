package com.ecommerce.Project.controller;

import com.ecommerce.Project.model.Order;
import com.ecommerce.Project.model.OrderItem;
import com.ecommerce.Project.payload.OrderDTO;
import com.ecommerce.Project.payload.OrderRequestDTO;
import com.ecommerce.Project.service.OrderService;
import com.ecommerce.Project.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/orders/users/paymentMethod/{paymentMethod}")
public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod,
                                           @RequestBody OrderRequestDTO orderRequestDTO){

       String emailId=authUtil.loggedInEmail();

OrderDTO orderDTO= orderService.placeOrder(emailId,orderRequestDTO.getAddressId(),
        paymentMethod,orderRequestDTO.getPgName(),
        orderRequestDTO.getPgPaymentId(),orderRequestDTO.getPgStatus(),
        orderRequestDTO.getPgResponseMessage());

return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);


    }

}
