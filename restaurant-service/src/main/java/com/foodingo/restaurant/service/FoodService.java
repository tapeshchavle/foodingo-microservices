package com.foodingo.restaurant.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.foodingo.restaurant.dto.FoodRequest;
import com.foodingo.restaurant.dto.FoodResponse;

public interface FoodService {
	String uploadFile(MultipartFile file);
	
	FoodResponse addFood(FoodRequest request, MultipartFile file);
	
	List<FoodResponse> readFoods();
	
	List<FoodResponse> getFoodsByRestaurantId(String restaurantId);
	
	FoodResponse getFoodById(String id);
	
	FoodResponse updateFood(String id, FoodRequest request);
	
	FoodResponse deleteFood(String id);
	
	boolean deleteFile(String fileName);
	
	List<FoodResponse> searchFoodByName(String name);
	
	List<FoodResponse> getFoodsByCategory(String category);
}

