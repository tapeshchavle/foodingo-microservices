package com.foodingo.restaurant.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.foodingo.restaurant.dto.RestaurantRequest;
import com.foodingo.restaurant.dto.RestaurantResponse;
import com.foodingo.restaurant.entity.RestaurantEntity;
import com.foodingo.restaurant.repository.RestaurantRepository;
import com.foodingo.restaurant.service.RestaurantService;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	@CacheEvict(value = "restaurants", allEntries = true)
	public RestaurantResponse createRestaurant(RestaurantRequest request, String ownerId) {
		RestaurantEntity restaurant = modelMapper.map(request, RestaurantEntity.class);
		restaurant.setOwnerId(ownerId);
		restaurant.setLocation(new double[]{request.getLongitude(), request.getLatitude()});
		restaurant.setActive(true);
		restaurant.setOpen(false);
		restaurant.setAcceptingOrders(true);
		
		RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);
		return modelMapper.map(savedRestaurant, RestaurantResponse.class);
	}
	
	@Override
	@CacheEvict(value = "restaurants", allEntries = true)
	public RestaurantResponse updateRestaurant(String id, RestaurantRequest request) {
		RestaurantEntity restaurant = restaurantRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id));
		
		modelMapper.map(request, restaurant);
		restaurant.setLocation(new double[]{request.getLongitude(), request.getLatitude()});
		
		RestaurantEntity updatedRestaurant = restaurantRepository.save(restaurant);
		return modelMapper.map(updatedRestaurant, RestaurantResponse.class);
	}
	
	@Override
	@Cacheable(value = "restaurants", key = "#id")
	public RestaurantResponse getRestaurantById(String id) {
		RestaurantEntity restaurant = restaurantRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id));
		return modelMapper.map(restaurant, RestaurantResponse.class);
	}
	
	@Override
	public List<RestaurantResponse> getAllRestaurants() {
		return restaurantRepository.findAll().stream()
			.map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class))
			.collect(Collectors.toList());
	}
	
	@Override
	public Page<RestaurantResponse> getAllRestaurants(Pageable pageable) {
		return restaurantRepository.findAll(pageable)
			.map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class));
	}
	
	@Override
	@Cacheable(value = "activeRestaurants")
	public List<RestaurantResponse> getActiveRestaurants() {
		return restaurantRepository.findByIsActiveTrue().stream()
			.map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class))
			.collect(Collectors.toList());
	}
	
	@Override
	public List<RestaurantResponse> searchRestaurantsByName(String name) {
		return restaurantRepository.findByNameContainingIgnoreCase(name).stream()
			.map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class))
			.collect(Collectors.toList());
	}
	
	@Override
	public List<RestaurantResponse> getRestaurantsByCity(String city) {
		return restaurantRepository.findByCity(city).stream()
			.map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class))
			.collect(Collectors.toList());
	}
	
	@Override
	public List<RestaurantResponse> getRestaurantsByCuisine(String cuisineType) {
		return restaurantRepository.findByCuisineTypesContaining(cuisineType).stream()
			.map(restaurant -> modelMapper.map(restaurant, RestaurantResponse.class))
			.collect(Collectors.toList());
	}
	
	@Override
	@CacheEvict(value = {"restaurants", "activeRestaurants"}, allEntries = true)
	public void updateRestaurantStatus(String id, boolean isActive) {
		RestaurantEntity restaurant = restaurantRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id));
		restaurant.setActive(isActive);
		restaurantRepository.save(restaurant);
	}
	
	@Override
	@CacheEvict(value = "restaurants", allEntries = true)
	public void updateRestaurantOpenStatus(String id, boolean isOpen) {
		RestaurantEntity restaurant = restaurantRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id));
		restaurant.setOpen(isOpen);
		restaurantRepository.save(restaurant);
	}
	
	@Override
	@CacheEvict(value = {"restaurants", "activeRestaurants"}, allEntries = true)
	public void deleteRestaurant(String id) {
		if (!restaurantRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id);
		}
		restaurantRepository.deleteById(id);
	}
	
	@Override
	@CacheEvict(value = "restaurants", key = "#id")
	public RestaurantResponse updateRestaurantLogo(String id, String logoUrl) {
		RestaurantEntity restaurant = restaurantRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id));
		restaurant.setLogoUrl(logoUrl);
		RestaurantEntity updated = restaurantRepository.save(restaurant);
		return modelMapper.map(updated, RestaurantResponse.class);
	}
	
	@Override
	@CacheEvict(value = "restaurants", key = "#id")
	public RestaurantResponse updateRestaurantBanner(String id, String bannerUrl) {
		RestaurantEntity restaurant = restaurantRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found with id: " + id));
		restaurant.setBannerUrl(bannerUrl);
		RestaurantEntity updated = restaurantRepository.save(restaurant);
		return modelMapper.map(updated, RestaurantResponse.class);
	}
}

