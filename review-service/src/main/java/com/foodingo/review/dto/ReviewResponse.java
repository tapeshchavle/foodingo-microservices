package com.foodingo.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String userId;
    private String restaurantId;
    private String foodId;
    private String orderId;
    private Integer rating;
    private String title;
    private String comment;
    private List<String> tags;
    private List<String> images;
    private boolean isVerified;
    private boolean isHelpful;
    private Integer helpfulCount;
    private boolean isVisible;
    private String response;
    private LocalDateTime responseDate;
    private String responseBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

