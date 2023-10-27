package com.safaricom.dxl.tempral_api.controllers;
import com.safaricom.dxl.tempral_api.dtos.UserDto;
import com.safaricom.dxl.tempral_api.services.UserRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "api/users")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;


    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        this.userRegistrationService.registerUser(userDto);
        return ResponseEntity.ok("Registration Started");
    }

    @PostMapping(value="/update-stage/{username}")
    public ResponseEntity<?> update(@PathVariable String username){
        this.userRegistrationService.update(username);
        return ResponseEntity.ok().build();
    }
    @PostMapping(value="/decrease-stage/{username}")
    public ResponseEntity<?> decrease(@PathVariable String username){
        this.userRegistrationService.decrease(username);
        return ResponseEntity.ok().build();
    }
}
