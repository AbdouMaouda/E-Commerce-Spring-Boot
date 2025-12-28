package com.ecommerce.Project.payload;

import com.ecommerce.Project.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItems;
    private Payment payment;
    private Double totalAmount;
    private String orderStatus;
    private Long addressId;

}
