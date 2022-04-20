
# NAGP microservices architecture demo

A demo project with microservices architecture, zipkin distributed tracing, spring cloud gateway, spring boot administration, eureka service discovery,  resilience4j circuit breaker and rabbitMQ messaging.


## API Reference
Import to postman:
#### NAGP.postman_collection.json


## Features

- Microservices architecture
- Zipkin distributed tracing
- Spring cloud gateway
- Codecentric's Spring Boot Admin
- Netflix eureka service discovery
- Resilience4j circuit breaker
- RabbitMq messaging

## Run Locally

Clone the project

```bash
  git clone https://github.com/Mahesh-Bishnoi/NAGP.git
```

Go to the project directory

```bash
  cd NAGP
```

Install dependencies


  - [Docker](https://docs.docker.com/desktop/#download-and-install)


Start the server

```bash
  docker-compose up
```


## Usage
- RabbitMq : http://localhost:15672
- Zipkin : http://localhost:9411
- Eureka : http://localhost:8761
- Admin : http://localhost:9999
- Gateway : http://localhost:9000



## Architecture

![Architecture diagram](https://raw.githubusercontent.com/Mahesh-Bishnoi/NAGP/main/architecture-diagram.png)

### Explaination
- `zipkin-service`: Zipkin server for distributed tracing.
- `rabbitmq-service`: RabbitMQ server for asynchronous messaging queues.
- `eureka-service-discovery`: Eureka server for service discovery.
- `customer-service`: Details about customers and customer related services.
- `provider-service`: Details about service providers and service provider related services.
- `admin-action-service`: Administration team services to assign service providers to customer requests.
- `notification-service`: Notification services to send appropriate notifications to customers, service providers through various channels(Email, SMS, Push notification, etc.) based on the notification queues in the rabbitmq-service.
- `customer-request-service`: Service to handle customer requests for a service and generate a new message and add the message to the queue in rabbitmq-service for a service request.
- `service-provider-service`: Service to handle accepting or rejecting of service request by the service provider.
- `gateway-service`: Edge service for clients to access other services.
- `admin-service`: Admin panel service for monitoring the health and other metrics of the system.

A customer when places an order for a service, it is routed to `customer-request-service` through `gateway-service`, `customer-request-service` then generates a new message and publish it to `rabbitmq-service`'s service request queue. `admin-action-service` subscribes to the service request queue and based on the business logic assigns a service provider from the `provider-service` and generates a new message and publishes it to notification queue on `rabbitmq-service`, `notification-service` subscribes to notification queue and based on the message fetches the details of provider from `provider-service` and send a service request notification to the provider.

A provider can accept or reject the service request which is routed to `service-provider-service` through `gateway-service` and based on the response from provider a new message is generated and published to either notification queue(ACCEPTED) or service request queue(REJECTED).

If the service provider accepts the service request, the message generated on the notification queue is subscribed by `notification-service` and it fetches the details of customer and service provider from `customer-service` and `provider-service` respectively and appropriate notifications are sent to customer and service provider with the details of each other.

If the service provider rejects the service request, the message generated on the service request queue is subscribed by `admin-action-service` and based on the business logic it again assigns a new service provider and generates and publish a message on notification queue on `rabbitmq-service`.

Hybrid communication mode for the inter microservices communication is used, asynchronous communication between `customer-request-service` and `admin-action-service` as there may be a time lag between customer placing an order for service and admin team assigning a service provider based on availability. Similarly between `notification-service` and other services as there may be a lag due to external integration for notification channels(Email, SMS, etc.). Synchronous communication with `customer-service` and `provider-service` as these are just fetching the details of customer and service provider for enriching the message for notification.
## Authors

- [@MaheshBishnoi](https://www.github.com/Mahesh-Bishnoi)


## License

[MIT](https://choosealicense.com/licenses/mit/)

