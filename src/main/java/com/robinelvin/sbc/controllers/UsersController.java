package com.robinelvin.sbc.controllers;

import com.robinelvin.sbc.models.RegistrationRequest;
import com.robinelvin.sbc.models.RegistrationResponse;
import com.robinelvin.sbc.models.User;
import com.robinelvin.sbc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userDetailsService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse register(@RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse response;
        try {

            // Register user in local system
            User user = userDetailsService.register(registrationRequest.getLoginName(), registrationRequest.getFirstName(), registrationRequest.getLastName(), registrationRequest.getPassword());
            userDetailsService.save(user);

            response = new RegistrationResponse("OK");
        } catch (RuntimeException ex) {
            response = new RegistrationResponse(String.format("Error: %s", ex.toString()));
        }
        return response;
    }

}