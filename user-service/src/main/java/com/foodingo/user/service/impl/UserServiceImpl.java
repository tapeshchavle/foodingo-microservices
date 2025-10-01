package com.foodingo.user.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.foodingo.user.dto.LoginRequest;
import com.foodingo.user.dto.LoginResponse;
import com.foodingo.user.dto.UserRequest;
import com.foodingo.user.dto.UserResponse;
import com.foodingo.user.entity.UserEntity;
import com.foodingo.user.entity.UserEntity.UserRole;
import com.foodingo.user.messaging.UserEventPublisher;
import com.foodingo.user.repository.UserRepository;
import com.foodingo.user.security.AppUserDetailsService;
import com.foodingo.user.security.JwtUtil;
import com.foodingo.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AppUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserEventPublisher eventPublisher;
	
	@Override
	public UserResponse registerUser(UserRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new RuntimeException("Email already exists");
		}
		
		Set<UserRole> roles = new HashSet<>();
		roles.add(UserRole.CUSTOMER);
		
		UserEntity user = UserEntity.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.phoneNumber(request.getPhoneNumber())
			.roles(roles)
			.referralCode(generateReferralCode())
			.referredBy(request.getReferredBy())
			.isActive(true)
			.loyaltyPoints(0)
			.build();
		
		UserEntity savedUser = userRepository.save(user);
		
		// Publish user registered event
		eventPublisher.publishUserRegistered(savedUser.getId(), savedUser.getEmail(), savedUser.getReferredBy());
		
		return mapToResponse(savedUser);
	}
	
	@Override
	public LoginResponse login(LoginRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = jwtUtil.generateToken(userDetails);
		
		UserEntity user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new RuntimeException("User not found"));
		
		return LoginResponse.builder()
			.token(token)
			.email(user.getEmail())
			.userId(user.getId())
			.name(user.getName())
			.build();
	}
	
	@Override
	public UserResponse getUserById(String userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));
		return mapToResponse(user);
	}
	
	@Override
	public UserResponse getUserByEmail(String email) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("User not found"));
		return mapToResponse(user);
	}
	
	@Override
	public String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String email = authentication.getName();
			UserEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found"));
			return user.getId();
		}
		throw new RuntimeException("User not authenticated");
	}
	
	@Override
	public UserResponse updateUser(String userId, UserRequest request) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));
		
		user.setName(request.getName());
		user.setPhoneNumber(request.getPhoneNumber());
		
		UserEntity updatedUser = userRepository.save(user);
		return mapToResponse(updatedUser);
	}
	
	@Override
	public void updateLoyaltyPoints(String userId, Integer points) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new RuntimeException("User not found"));
		user.setLoyaltyPoints(user.getLoyaltyPoints() + points);
		userRepository.save(user);
	}
	
	private UserResponse mapToResponse(UserEntity user) {
		return UserResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.phoneNumber(user.getPhoneNumber())
			.profileImageUrl(user.getProfileImageUrl())
			.roles(user.getRoles())
			.addresses(user.getAddresses())
			.isActive(user.isActive())
			.loyaltyPoints(user.getLoyaltyPoints())
			.referralCode(user.getReferralCode())
			.createdAt(user.getCreatedAt())
			.build();
	}
	
	private String generateReferralCode() {
		return "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}
}

