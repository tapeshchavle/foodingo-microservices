package com.foodingo.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String foodId;
    private String name;
    private String description;
    private String imageUrl;
    private String category;
    private int quantity;
    private double price;
}

