package com.nagp.providerservice;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Provider {
	private String name;
	private String serviceType;
	private String phone;
}
