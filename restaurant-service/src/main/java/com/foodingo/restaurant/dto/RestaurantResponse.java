package com.foodingo.restaurant.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse {
	private String id;
	private String ownerId;
	private String name;
	private String description;
	private String logoUrl;
	private String bannerUrl;
	private List<String> cuisineTypes;
	private String email;
	private String phoneNumber;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private Double latitude;
	private Double longitude;
	private Double averageRating;
	private Integer totalReviews;
	private Integer totalOrders;
	private boolean isActive;
	private boolean isOpen;
	private boolean acceptingOrders;
	private Double minimumOrderAmount;
	private Double deliveryRadius;
	private Double deliveryFee;
	private Integer averageDeliveryTime;
	private List<String> paymentMethods;
	private List<OperatingHoursDTO> operatingHours;
	private String fssaiLicense;
	private String gstNumber;
	private List<String> specialties;
	private List<String> features;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OperatingHoursDTO {
		private String dayOfWeek;
		private LocalTime openTime;
		private LocalTime closeTime;
		private boolean isClosed;
	}
}

