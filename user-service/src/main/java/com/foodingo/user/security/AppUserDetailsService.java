package com.foodingo.user.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.foodingo.user.entity.UserEntity;
import com.foodingo.user.entity.UserEntity.UserRole;
import com.foodingo.user.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
		
		if (!user.isActive()) {
			throw new UsernameNotFoundException("User account is not active");
		}
		
		Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
			.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
			.collect(Collectors.toSet());
		
		if (authorities.isEmpty()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + UserRole.CUSTOMER.name()));
		}
		
		return new User(
			user.getEmail(),
			user.getPassword(),
			user.isActive(),
			true,
			true,
			true,
			authorities
		);
	}
}

