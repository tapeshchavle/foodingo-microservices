package com.foodingo.review.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class ReviewEntity {
    
    @Id
    private String id;
    
    private String userId;
    
    private String restaurantId;
    
    private String foodId; // Optional - for food-specific reviews
    
    private String orderId; // Optional - link to order
    
    private Integer rating; // 1-5 stars
    
    private String title;
    
    private String comment;
    
    @Builder.Default
    private List<String> tags = List.of(); // e.g., "delicious", "fast delivery", "good value"
    
    @Builder.Default
    private List<String> images = List.of(); // URLs of review images
    
    @Builder.Default
    private boolean isVerified = false; // Verified purchase
    
    @Builder.Default
    private boolean isHelpful = false; // Marked as helpful by others
    
    @Builder.Default
    private Integer helpfulCount = 0;
    
    @Builder.Default
    private boolean isVisible = true;
    
    private String response; // Restaurant owner response
    
    private LocalDateTime responseDate;
    
    private String responseBy; // Restaurant owner ID
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

