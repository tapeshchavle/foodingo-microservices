package com.foodingo.restaurant.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodingo.restaurant.entity.FoodEntity;

@Repository
public interface FoodRepository extends MongoRepository<FoodEntity, String> {
	
	List<FoodEntity> findByRestaurantId(String restaurantId);
	
	List<FoodEntity> findByCategory(String category);
	
	List<FoodEntity> findByIsAvailableTrue();
	
	List<FoodEntity> findByRestaurantIdAndIsAvailableTrue(String restaurantId);
	
	List<FoodEntity> findByIsVegetarianTrue();
	
	List<FoodEntity> findByIsVeganTrue();
	
	List<FoodEntity> findByTagsContaining(String tag);
	
	List<FoodEntity> findByNameContainingIgnoreCase(String name);
	
	List<FoodEntity> findByPriceBetween(Double minPrice, Double maxPrice);
}

