package com.insurance.claim.dto;

import lombok.Data;

@Data
public class CustomerExternalResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String dateOfBirth;
}