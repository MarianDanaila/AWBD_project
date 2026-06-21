package com.awbd.ironplate.nutritionservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserClientFallback implements UserClient {

    @Override
    public UserDto getUserById(Long id) {
        log.warn("User service unavailable, using fallback for user id: {}", id);
        return null;
    }
}
