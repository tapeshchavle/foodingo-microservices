package com.foodingo.user.service;

import com.foodingo.user.dto.LoginRequest;
import com.foodingo.user.dto.LoginResponse;
import com.foodingo.user.dto.UserRequest;
import com.foodingo.user.dto.UserResponse;

public interface UserService {
	UserResponse registerUser(UserRequest request);
	LoginResponse login(LoginRequest request);
	UserResponse getUserById(String userId);
	UserResponse getUserByEmail(String email);
	String getCurrentUserId();
	UserResponse updateUser(String userId, UserRequest request);
	void updateLoyaltyPoints(String userId, Integer points);
}

