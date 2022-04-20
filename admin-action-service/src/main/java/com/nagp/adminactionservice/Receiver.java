package com.nagp.adminactionservice;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class Receiver {

	@Value("${spring.provider-service.base.url}")
	private String providerServiceBaseUrl;
	private final RabbitTemplate rabbitTemplate;
	private final RestTemplate restTemplate;
	
	public void receiveMessage(String message) {
		log.info("Received <" + message + ">");
		assignServiceProvider(message);
	}

	private void assignServiceProvider(String message) {
		log.info("Assign service provider.");
		ResponseEntity<Provider[]> response = restTemplate.getForEntity(providerServiceBaseUrl+"/providers", Provider[].class);
		Provider[] providers = response.getBody();
		for(Provider provider : providers) {
			if(provider.getServiceType().equalsIgnoreCase(message)) {
				message = message + ", providerDetails=" + provider.toString();
			}
		}
		sendServiceRequestNotificationToServiceProvider(message);
	}

	private void sendServiceRequestNotificationToServiceProvider(String message) {
		log.info("Send service request notification to service provider.");
		try {
			rabbitTemplate.convertAndSend("notification-exchange", "notification.request.type",message);
		}
		catch(AmqpException e) {
			log.error("Exception: " + e);
		}
	}

}
