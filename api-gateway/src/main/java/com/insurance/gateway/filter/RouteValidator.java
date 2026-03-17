package com.insurance.gateway.filter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );

    public Predicate<String> isSecured =
            uri -> PUBLIC_ENDPOINTS.stream().noneMatch(uri::startsWith);
}