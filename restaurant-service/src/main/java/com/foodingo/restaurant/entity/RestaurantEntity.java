package com.foodingo.restaurant.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "restaurants")
public class RestaurantEntity {
	
	@Id
	private String id;
	
	private String ownerId; // User ID of restaurant owner
	
	private String name;
	
	private String description;
	
	private String logoUrl;
	
	private String bannerUrl;
	
	@Builder.Default
	private List<String> cuisineTypes = new ArrayList<>(); // Italian, Chinese, Indian, etc.
	
	private String email;
	
	private String phoneNumber;
	
	private String address;
	
	private String city;
	
	private String state;
	
	private String zipCode;
	
	private String country;
	
	@GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
	private double[] location; // [longitude, latitude]
	
	private Double latitude;
	
	private Double longitude;
	
	@Builder.Default
	private Double averageRating = 0.0;
	
	@Builder.Default
	private Integer totalReviews = 0;
	
	@Builder.Default
	private Integer totalOrders = 0;
	
	@Builder.Default
	private boolean isActive = true;
	
	@Builder.Default
	private boolean isOpen = false;
	
	@Builder.Default
	private boolean acceptingOrders = true;
	
	private Double minimumOrderAmount;
	
	private Double deliveryRadius; // in km
	
	private Double deliveryFee;
	
	private Integer averageDeliveryTime; // in minutes
	
	@Builder.Default
	private List<String> paymentMethods = new ArrayList<>();
	
	@Builder.Default
	private List<OperatingHours> operatingHours = new ArrayList<>();
	
	private String fssaiLicense;
	
	private String gstNumber;
	
	@Builder.Default
	private List<String> specialties = new ArrayList<>();
	
	@Builder.Default
	private List<String> features = new ArrayList<>(); // Free Delivery, Outdoor Seating, etc.
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OperatingHours {
		private String dayOfWeek; // MONDAY, TUESDAY, etc.
		private LocalTime openTime;
		private LocalTime closeTime;
		private boolean isClosed;
	}
}

