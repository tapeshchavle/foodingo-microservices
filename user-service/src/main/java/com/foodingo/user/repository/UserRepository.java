package com.foodingo.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodingo.user.entity.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
	Optional<UserEntity> findByEmail(String email);
	boolean existsByEmail(String email);
	Optional<UserEntity> findByReferralCode(String referralCode);
}

