package com.nagp.customerservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

	@GetMapping("/customer/{id}")
	public Customer getCustomerbyId(@PathVariable String id) {
		Customer customer = new Customer("Customer1","Some address","1234567890");
		return customer;
	}
	
}
