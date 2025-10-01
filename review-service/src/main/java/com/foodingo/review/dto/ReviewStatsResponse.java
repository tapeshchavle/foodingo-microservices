package com.foodingo.review.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatsResponse {
    private String restaurantId;
    private Double averageRating;
    private Integer totalReviews;
    private Map<Integer, Integer> ratingDistribution; // {1: 5, 2: 10, 3: 20, 4: 30, 5: 35}
    private Integer verifiedReviews;
    private Integer helpfulReviews;
    private Double helpfulPercentage;
}

