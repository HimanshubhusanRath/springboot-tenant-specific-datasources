package com.hr.microservices.controller;

import com.hr.microservices.context.TenantContextHolder;
import com.hr.microservices.entity.User;
import com.hr.microservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{user-id}")
    public ResponseEntity<User> getUser(@PathVariable("user-id") final Integer userId,
                                              @RequestParam("tenant") final String tenant) {
        // Set the tenant in the context
        TenantContextHolder.set(tenant);
        final Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.isPresent() ? ResponseEntity.ok(userOptional.get()) : ResponseEntity.badRequest().build();
    }
}