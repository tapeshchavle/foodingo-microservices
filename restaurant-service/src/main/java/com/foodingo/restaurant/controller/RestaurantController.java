package com.foodingo.restaurant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodingo.restaurant.dto.RestaurantRequest;
import com.foodingo.restaurant.dto.RestaurantResponse;
import com.foodingo.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurant", description = "Restaurant management APIs")
public class RestaurantController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@PostMapping
	@Operation(summary = "Create a new restaurant")
	public ResponseEntity<RestaurantResponse> createRestaurant(
			@Valid @RequestBody RestaurantRequest request,
			@RequestHeader(value = "X-User-Id", required = false) String ownerId) {
		// TODO: Get ownerId from JWT token via Feign client to User Service
		String userId = ownerId != null ? ownerId : "default-owner-id";
		RestaurantResponse response = restaurantService.createRestaurant(request, userId);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	@Operation(summary = "Update restaurant details")
	public ResponseEntity<RestaurantResponse> updateRestaurant(
			@PathVariable String id,
			@Valid @RequestBody RestaurantRequest request) {
		RestaurantResponse response = restaurantService.updateRestaurant(id, request);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Get restaurant by ID")
	public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable String id) {
		RestaurantResponse response = restaurantService.getRestaurantById(id);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping
	@Operation(summary = "Get all restaurants with pagination")
	public ResponseEntity<Page<RestaurantResponse>> getAllRestaurants(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "createdAt") String sortBy,
			@RequestParam(defaultValue = "DESC") String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase("ASC") 
			? Sort.by(sortBy).ascending() 
			: Sort.by(sortBy).descending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<RestaurantResponse> restaurants = restaurantService.getAllRestaurants(pageable);
		return ResponseEntity.ok(restaurants);
	}
	
	@GetMapping("/active")
	@Operation(summary = "Get all active restaurants")
	public ResponseEntity<List<RestaurantResponse>> getActiveRestaurants() {
		List<RestaurantResponse> restaurants = restaurantService.getActiveRestaurants();
		return ResponseEntity.ok(restaurants);
	}
	
	@GetMapping("/search")
	@Operation(summary = "Search restaurants by name")
	public ResponseEntity<List<RestaurantResponse>> searchRestaurants(@RequestParam String name) {
		List<RestaurantResponse> restaurants = restaurantService.searchRestaurantsByName(name);
		return ResponseEntity.ok(restaurants);
	}
	
	@GetMapping("/city/{city}")
	@Operation(summary = "Get restaurants by city")
	public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCity(@PathVariable String city) {
		List<RestaurantResponse> restaurants = restaurantService.getRestaurantsByCity(city);
		return ResponseEntity.ok(restaurants);
	}
	
	@GetMapping("/cuisine/{cuisineType}")
	@Operation(summary = "Get restaurants by cuisine type")
	public ResponseEntity<List<RestaurantResponse>> getRestaurantsByCuisine(@PathVariable String cuisineType) {
		List<RestaurantResponse> restaurants = restaurantService.getRestaurantsByCuisine(cuisineType);
		return ResponseEntity.ok(restaurants);
	}
	
	@PatchMapping("/{id}/status")
	@Operation(summary = "Update restaurant active status")
	public ResponseEntity<Void> updateRestaurantStatus(
			@PathVariable String id,
			@RequestParam boolean isActive) {
		restaurantService.updateRestaurantStatus(id, isActive);
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/{id}/open-status")
	@Operation(summary = "Update restaurant open/close status")
	public ResponseEntity<Void> updateRestaurantOpenStatus(
			@PathVariable String id,
			@RequestParam boolean isOpen) {
		restaurantService.updateRestaurantOpenStatus(id, isOpen);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	@Operation(summary = "Delete a restaurant")
	public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
		restaurantService.deleteRestaurant(id);
		return ResponseEntity.noContent().build();
	}
}

