package com.foodingo.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodingo.restaurant.entity.RestaurantEntity;

@Repository
public interface RestaurantRepository extends MongoRepository<RestaurantEntity, String> {
	
	Optional<RestaurantEntity> findByOwnerId(String ownerId);
	
	List<RestaurantEntity> findByIsActiveTrue();
	
	List<RestaurantEntity> findByIsActiveTrueAndIsOpenTrue();
	
	Page<RestaurantEntity> findByIsActiveTrue(Pageable pageable);
	
	List<RestaurantEntity> findByCity(String city);
	
	List<RestaurantEntity> findByCuisineTypesContaining(String cuisineType);
	
	List<RestaurantEntity> findByAverageRatingGreaterThanEqual(Double rating);
	
	List<RestaurantEntity> findByNameContainingIgnoreCase(String name);
}

