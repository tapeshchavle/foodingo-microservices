package com.foodingo.user.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserEventPublisher {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	private static final String EXCHANGE = "user.events";
	
	public void publishUserRegistered(String userId, String email, String referredBy) {
		Map<String, Object> event = new HashMap<>();
		event.put("eventType", "USER_REGISTERED");
		event.put("userId", userId);
		event.put("email", email);
		event.put("referredBy", referredBy);
		event.put("timestamp", System.currentTimeMillis());
		
		rabbitTemplate.convertAndSend(EXCHANGE, "user.registered", event);
		log.info("Published user.registered event for userId: {}", userId);
	}
}

