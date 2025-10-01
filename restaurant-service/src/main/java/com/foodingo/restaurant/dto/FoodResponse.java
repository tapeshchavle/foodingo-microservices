package com.foodingo.restaurant.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class FoodResponse {
	private String id;
	private String name;
	private String description;
	private String imageUrl;
	private double price;
	private double discountedPrice;
	private String category;
	private String restaurantId;
	
	private boolean isAvailable;
	private boolean isVegetarian;
	private boolean isVegan;
	private boolean isGlutenFree;
	
	private List<String> tags;
	private List<String> allergens;
	
	private Integer preparationTime;
	private Integer stockQuantity;
	
	private Double averageRating;
	private Integer totalReviews;
	private Integer totalOrders;
	
	private NutritionInfoDTO nutritionInfo;
	private List<CustomizationOptionDTO> customizations;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class NutritionInfoDTO {
		private Double calories;
		private Double protein;
		private Double carbs;
		private Double fat;
		private Double fiber;
		private Double sugar;
		private Double sodium;
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CustomizationOptionDTO {
		private String name;
		private boolean isRequired;
		private Integer minSelections;
		private Integer maxSelections;
		private List<CustomizationChoiceDTO> choices;
	}
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CustomizationChoiceDTO {
		private String id;
		private String name;
		private Double additionalPrice;
		private boolean isAvailable;
	}
}

