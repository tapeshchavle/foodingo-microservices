package com.foodingo.restaurant.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.foodingo.restaurant.dto.RestaurantRequest;
import com.foodingo.restaurant.dto.RestaurantResponse;

public interface RestaurantService {
	RestaurantResponse createRestaurant(RestaurantRequest request, String ownerId);
	RestaurantResponse updateRestaurant(String id, RestaurantRequest request);
	RestaurantResponse getRestaurantById(String id);
	List<RestaurantResponse> getAllRestaurants();
	Page<RestaurantResponse> getAllRestaurants(Pageable pageable);
	List<RestaurantResponse> getActiveRestaurants();
	List<RestaurantResponse> searchRestaurantsByName(String name);
	List<RestaurantResponse> getRestaurantsByCity(String city);
	List<RestaurantResponse> getRestaurantsByCuisine(String cuisineType);
	void updateRestaurantStatus(String id, boolean isActive);
	void updateRestaurantOpenStatus(String id, boolean isOpen);
	void deleteRestaurant(String id);
	RestaurantResponse updateRestaurantLogo(String id, String logoUrl);
	RestaurantResponse updateRestaurantBanner(String id, String bannerUrl);
}

