package com.foodingo.user.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
@Builder
public class UserEntity {
	@Id
	private String id;
	
	private String name;
	
	@Indexed(unique = true)
	private String email;
	
	private String password;
	
	private String phoneNumber;
	
	private String profileImageUrl;
	
	@Builder.Default
	private Set<UserRole> roles = new HashSet<>();
	
	@Builder.Default
	private List<Address> addresses = new ArrayList<>();
	
	private String defaultAddressId;
	
	@Builder.Default
	private boolean isActive = true;
	
	@Builder.Default
	private boolean isEmailVerified = false;
	
	@Builder.Default
	private boolean isPhoneVerified = false;
	
	private String restaurantId;
	
	@Builder.Default
	private Integer loyaltyPoints = 0;
	
	private String referralCode;
	
	private String referredBy;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
	private String provider;
	private String providerId;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Address {
		private String id;
		private String label;
		private String fullAddress;
		private String street;
		private String city;
		private String state;
		private String zipCode;
		private String country;
		private Double latitude;
		private Double longitude;
		private String landmark;
		private boolean isDefault;
	}
	
	public enum UserRole {
		CUSTOMER, RESTAURANT_OWNER, DELIVERY_PARTNER, ADMIN
	}
}

