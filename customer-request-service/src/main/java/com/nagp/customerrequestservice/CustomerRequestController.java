package com.nagp.customerrequestservice;

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
public class CustomerRequestController {
	
	private final RabbitTemplate rabbitTemplate;
	
	@PostMapping(path = "/place-service-request/{serviceId}")
	public HttpStatus placeServiceRequest(@PathVariable String serviceId) {
		log.info("Sending service request message to rabbitMq.");
		try {
			rabbitTemplate.convertAndSend("customer-service-request-exchange", "customer.service.request.type",serviceId);
		}
		catch(AmqpException e) {
			log.error("Exception: " + e);
			return HttpStatus.SERVICE_UNAVAILABLE;
		}
		return HttpStatus.OK;
	}

}
