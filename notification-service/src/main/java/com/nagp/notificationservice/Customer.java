package com.nagp.notificationservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {
	private String name;
	private String address;
	private String phone;
}
