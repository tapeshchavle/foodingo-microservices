package com.foodingo.restaurant.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "food")
public class FoodEntity {
	
	@Id
	private String id;
	
	private String name;
	
	private String description;
	
	private double price;
	
	private double discountedPrice;
	
	private String category;
	
	private String imageUrl;
	
	private String restaurantId;
	
	@Builder.Default
	private boolean isAvailable = true;
	
	@Builder.Default
	private boolean isVegetarian = false;
	
	@Builder.Default
	private boolean isVegan = false;
	
	@Builder.Default
	private boolean isGlutenFree = false;
	
	@Builder.Default
	private List<String> tags = new ArrayList<>(); // spicy, bestseller, new, etc.
	
	@Builder.Default
	private List<String> allergens = new ArrayList<>();
	
	private Integer preparationTime; // in minutes
	
	@Builder.Default
	private Integer stockQuantity = 0;
	
	@Builder.Default
	private Double averageRating = 0.0;
	
	@Builder.Default
	private Integer totalReviews = 0;
	
	@Builder.Default
	private Integer totalOrders = 0;
	
	// Nutrition information (per 100g)
	private NutritionInfo nutritionInfo;
	
	// Customization options
	@Builder.Default
	private List<CustomizationOption> customizations = new ArrayList<>();
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class NutritionInfo {
		private Double calories;
		private Double protein;
		private Double carbs;
		private Double fat;
		private Double fiber;
		private Double sugar;
		private Double sodium;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class CustomizationOption {
		private String name; // Size, Spice Level, Toppings
		private boolean isRequired;
		private Integer minSelections;
		private Integer maxSelections;
		@Builder.Default
		private List<CustomizationChoice> choices = new ArrayList<>();
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class CustomizationChoice {
		private String id;
		private String name;
		private Double additionalPrice;
		private boolean isAvailable;
	}
}

