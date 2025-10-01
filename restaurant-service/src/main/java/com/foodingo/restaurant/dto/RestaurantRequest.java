package com.foodingo.restaurant.dto;

import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequest {
	
	@NotBlank(message = "Restaurant name is required")
	private String name;
	
	private String description;
	
	private List<String> cuisineTypes;
	
	@Email(message = "Invalid email format")
	private String email;
	
	private String phoneNumber;
	
	@NotBlank(message = "Address is required")
	private String address;
	
	private String city;
	
	private String state;
	
	private String zipCode;
	
	private String country;
	
	@NotNull(message = "Latitude is required")
	private Double latitude;
	
	@NotNull(message = "Longitude is required")
	private Double longitude;
	
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

