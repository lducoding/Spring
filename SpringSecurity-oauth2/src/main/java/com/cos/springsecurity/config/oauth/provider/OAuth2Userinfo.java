package com.cos.springsecurity.config.oauth.provider;

public interface OAuth2Userinfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
