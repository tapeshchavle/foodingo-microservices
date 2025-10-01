package com.foodingo.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodingo.user.dto.LoginRequest;
import com.foodingo.user.dto.LoginResponse;
import com.foodingo.user.dto.UserRequest;
import com.foodingo.user.dto.UserResponse;
import com.foodingo.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
		UserResponse response = userService.registerUser(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		LoginResponse response = userService.login(request);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable String userId) {
		UserResponse response = userService.getUserById(userId);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
		UserResponse response = userService.getUserByEmail(email);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/current")
	public ResponseEntity<String> getCurrentUserId() {
		String userId = userService.getCurrentUserId();
		return ResponseEntity.ok(userId);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable String userId,
			@Valid @RequestBody UserRequest request) {
		UserResponse response = userService.updateUser(userId, request);
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{userId}/loyalty-points")
	public ResponseEntity<Void> updateLoyaltyPoints(
			@PathVariable String userId,
			@RequestParam Integer points) {
		userService.updateLoyaltyPoints(userId, points);
		return ResponseEntity.ok().build();
	}
}

