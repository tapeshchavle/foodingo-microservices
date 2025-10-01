package com.foodingo.restaurant.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
	
	@NotBlank(message = "Food name is required")
	private String name;
	
	private String description;
	
	@NotNull(message = "Price is required")
	@Positive(message = "Price must be positive")
	private Double price;
	
	private Double discountedPrice;
	
	@NotBlank(message = "Category is required")
	private String category;
	
	@NotBlank(message = "Restaurant ID is required")
	private String restaurantId;
	
	private boolean isAvailable;
	private boolean isVegetarian;
	private boolean isVegan;
	private boolean isGlutenFree;
	
	private List<String> tags;
	private List<String> allergens;
	
	private Integer preparationTime;
	private Integer stockQuantity;
	
	private NutritionInfoDTO nutritionInfo;
	private List<CustomizationOptionDTO> customizations;
	
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

