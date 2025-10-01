package com.foodingo.restaurant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodingo.restaurant.dto.FoodRequest;
import com.foodingo.restaurant.dto.FoodResponse;
import com.foodingo.restaurant.service.FoodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/foods")
@Tag(name = "Food", description = "Food management APIs")
public class FoodController {
	
	@Autowired
	private FoodService foodService;
	
	@PostMapping("/add")
	@Operation(summary = "Add a new food item")
	public ResponseEntity<FoodResponse> addFood(
			@RequestPart("food") String foodString,
			@RequestPart("file") MultipartFile file) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		FoodRequest request = null;
		
		try {
			request = objectMapper.readValue(foodString, FoodRequest.class);
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON Format");
		}
		
		return new ResponseEntity<>(foodService.addFood(request, file), HttpStatus.CREATED);
	}
	
	@GetMapping
	@Operation(summary = "Get all food items")
	public ResponseEntity<List<FoodResponse>> readFoods() {
		return new ResponseEntity<>(foodService.readFoods(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get food by ID")
	public ResponseEntity<FoodResponse> getFoodById(@PathVariable String id) {
		return new ResponseEntity<>(foodService.getFoodById(id), HttpStatus.OK);
	}
	
	@GetMapping("/restaurant/{restaurantId}")
	@Operation(summary = "Get all foods for a restaurant")
	public ResponseEntity<List<FoodResponse>> getFoodsByRestaurantId(@PathVariable String restaurantId) {
		return new ResponseEntity<>(foodService.getFoodsByRestaurantId(restaurantId), HttpStatus.OK);
	}
	
	@GetMapping("/search")
	@Operation(summary = "Search food by name")
	public ResponseEntity<List<FoodResponse>> searchFood(@RequestParam String name) {
		return new ResponseEntity<>(foodService.searchFoodByName(name), HttpStatus.OK);
	}
	
	@GetMapping("/category/{category}")
	@Operation(summary = "Get foods by category")
	public ResponseEntity<List<FoodResponse>> getFoodsByCategory(@PathVariable String category) {
		return new ResponseEntity<>(foodService.getFoodsByCategory(category), HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Update food item")
	public ResponseEntity<FoodResponse> updateFood(
			@PathVariable String id,
			@RequestPart("food") String foodString,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		FoodRequest request = null;
		
		try {
			request = objectMapper.readValue(foodString, FoodRequest.class);
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON Format");
		}
		
		FoodResponse response = foodService.updateFood(id, request);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete food by ID")
	public ResponseEntity<FoodResponse> deleteFoodById(@PathVariable String id) {
		return new ResponseEntity<>(foodService.deleteFood(id), HttpStatus.OK);
	}
}

