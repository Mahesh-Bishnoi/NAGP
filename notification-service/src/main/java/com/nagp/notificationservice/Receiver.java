package com.nagp.notificationservice;

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
	
	@Value("${spring.customer-service.base.url}")
	private String customerServiceBaseUrl;
	@Value("${spring.provider-service.base.url}")
	private String providerServiceBaseUrl;
	private final RestTemplate restTemplate;
	
	public void receiveMessage(String message) {
		log.info("Received <" + message + ">");
		sendNotification(message);
	}

	private void sendNotification(String message) {	
		if(message.equalsIgnoreCase("accepted")) {
			log.info("Send notification.");
			sendNotificationToProvider("customerId");//TODO get customerId from message
			sendNotificationToCustomer("providerId");//TODO get providerId from message
		}
		else {
			sendServiceRequestNotificationToProvider(message);
		}
	}

	private void sendServiceRequestNotificationToProvider(String message) {
		log.info("Send service request notification to provider." + message);
	}

	private void sendNotificationToProvider(String customerId) {
		ResponseEntity<Customer> customer = restTemplate.getForEntity(customerServiceBaseUrl+"/customer/"+customerId, Customer.class);
		log.info("Send notification to Provider." + customer.toString());
	}

	private void sendNotificationToCustomer(String providerId) {
		ResponseEntity<Provider> provider = restTemplate.getForEntity(providerServiceBaseUrl+"/provider/"+providerId, Provider.class);
		log.info("Send notification to Customer." + provider.toString());
	}
}
