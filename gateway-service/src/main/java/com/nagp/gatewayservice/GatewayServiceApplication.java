package com.nagp.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class GatewayServiceApplication {
	
	@RequestMapping("/fallback")
	public Mono<String> fallback() {
	  return Mono.just("fallback");
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("provider-service",p -> p
	            .path("/provider-service/**")
	            .filters(f -> f.circuitBreaker(config -> config
	                .setName("provider-service-cb")
	                .setFallbackUri("forward:/fallback"))
            		.rewritePath("/provider-service/(?<segment>.*)", "/$\\{segment}"))
	            .uri("lb://provider-service"))
	        .route("customer-service",p -> p
	            .path("/customer-service/**")
	            .filters(f -> f.circuitBreaker(config -> config
	                .setName("customer-service-cb")
	                .setFallbackUri("forward:/fallback"))
            		.rewritePath("/customer-service/(?<segment>.*)", "/$\\{segment}"))
	            .uri("lb://customer-service"))
	        .route("service-provider-service",p -> p
	            .path("/service-provider-service/**")
	            .filters(f -> f.circuitBreaker(config -> config
	                .setName("service-provider-service-cb")
	                .setFallbackUri("forward:/fallback"))
            		.rewritePath("/service-provider-service/(?<segment>.*)", "/$\\{segment}"))
	            .uri("lb://service-provider-service"))
	        .route("customer-request-service",p -> p
	            .path("/customer-request-service/**")
	            .filters(f -> f.circuitBreaker(config -> config
	                .setName("customer-request-service-cb")
	                .setFallbackUri("forward:/fallback"))
            		.rewritePath("/customer-request-service/(?<segment>.*)", "/$\\{segment}"))
	            .uri("lb://customer-request-service"))
	        .build();
	}
	public static void main(String[] args) {
		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}
