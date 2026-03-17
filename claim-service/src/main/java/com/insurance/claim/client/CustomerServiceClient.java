package com.insurance.claim.client;

import com.insurance.claim.dto.CustomerExternalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomerServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.customer.base-url}")
    private String customerServiceBaseUrl;

    public CustomerServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CustomerExternalResponse getCustomerById(Long customerId, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CustomerExternalResponse> response = restTemplate.exchange(
                customerServiceBaseUrl + "/api/customers/" + customerId,
                HttpMethod.GET,
                entity,
                CustomerExternalResponse.class
        );

        return response.getBody();
    }
}