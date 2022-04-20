package com.nagp.serviceproviderservice;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor

public class ServiceProviderResponseController {
	
	private final RabbitTemplate rabbitTemplate;
	
	@PostMapping(path = "/accept-service-request/{serviceId}")
	public HttpStatus acceptServiceRequest(@PathVariable String serviceId) {
		log.info("Service request accepted by service provider.");
		if(sendNotification(serviceId)) {
			return HttpStatus.OK;
		}
		return HttpStatus.SERVICE_UNAVAILABLE;
	}

	@PostMapping(path = "/reject-service-request/{serviceId}")
	public HttpStatus rejectServiceRequest(@PathVariable String serviceId) {
		log.info("Service request rejected by service provider.");
		if(reassignServiceRequest(serviceId)) {
			return HttpStatus.OK;
		}
		return HttpStatus.SERVICE_UNAVAILABLE;
	}

	private boolean sendNotification(String serviceId) {
		// TODO Send notification with proper message
		try {
			rabbitTemplate.convertAndSend("notification-exchange", "notification.request.type","accepted");			
		}
		catch(AmqpException e) {
			log.error("Exception: " + e);
			return false;
		}
		return true;
	}
	
	private boolean reassignServiceRequest(String serviceId) {
		// Reassign service request
		log.info("Reassign service request.");
		try {
			rabbitTemplate.convertAndSend("customer-service-request-exchange", "customer.service.request.type",serviceId);
		}
		catch(AmqpException e) {
			log.error("Exception: " + e);
			return false;
		}
		return true;
	}

}
