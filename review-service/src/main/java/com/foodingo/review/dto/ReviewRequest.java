package com.foodingo.review.dto;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    
    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    
    private String foodId; // Optional
    
    private String orderId; // Optional
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;
    
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;
    
    private List<String> tags;
    
    private List<String> images;
}

