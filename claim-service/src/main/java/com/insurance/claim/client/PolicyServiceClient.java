package com.insurance.claim.client;

import com.insurance.claim.dto.PolicyActiveResponse;
import com.insurance.claim.dto.PolicyExternalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PolicyServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.policy.base-url}")
    private String policyServiceBaseUrl;

    public PolicyServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PolicyExternalResponse getPolicyById(Long policyId, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PolicyExternalResponse> response = restTemplate.exchange(
                policyServiceBaseUrl + "/api/policies/" + policyId,
                HttpMethod.GET,
                entity,
                PolicyExternalResponse.class
        );

        return response.getBody();
    }

    public boolean isPolicyActive(Long policyId, String authHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PolicyActiveResponse> response = restTemplate.exchange(
                policyServiceBaseUrl + "/api/policies/" + policyId + "/active",
                HttpMethod.GET,
                entity,
                PolicyActiveResponse.class
        );

        return response.getBody() != null && response.getBody().isActive();
    }
}