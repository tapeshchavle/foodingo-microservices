package com.foodingo.user.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.foodingo.user.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private String id;
	private String name;
	private String email;
	private String phoneNumber;
	private String profileImageUrl;
	private Set<UserEntity.UserRole> roles;
	private List<UserEntity.Address> addresses;
	private boolean isActive;
	private Integer loyaltyPoints;
	private String referralCode;
	private LocalDateTime createdAt;
}

