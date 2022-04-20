package com.nagp.providerservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {
	
	@GetMapping("/providers")
	public List<Provider> getAllProviders() {
		List<Provider> providers = new ArrayList<Provider>();
		providers.add(new Provider("Provider1","Plumber","123456789"));
		providers.add(new Provider("Provider2","Electrician","123456789"));
		providers.add(new Provider("Provider3","Yoga Trainer","123456789"));
		providers.add(new Provider("Provider4","Cook","123456789"));
		providers.add(new Provider("Provider5","Cleaner","123456789"));
		providers.add(new Provider("Provider6","Gardener","123456789"));
		providers.add(new Provider("Provider7","Tutor","123456789"));
		providers.add(new Provider("Provider8","Babysitter","123456789"));
		providers.add(new Provider("Provider9","Dance teacher","123456789"));
		providers.add(new Provider("Provider10","Personal Trainer","123456789"));
		return providers;
	}
	
	@GetMapping("/provider/{providerId}")
	public Provider getAllProviders(@PathVariable String providerId) {	
		return new Provider("Provider1","Personal Trainer","123456789");
	}
	
}
