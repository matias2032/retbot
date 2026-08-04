package dev258.retbotbackend.integration.oauth;

public record TokenOAuthResponse(String accessToken, String tokenType, String scope) {}