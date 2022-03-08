package com.uas.api.controller.User;


import com.uas.api.mapper.UserMapper;
import com.uas.api.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")

public class UserApi {

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ROLE_USER_CTO') or hasRole('ROLE_USER_LOGISTIC')")
    public String userAccess() {
        return "User Content.";
    }
    @GetMapping("/cto")
    @PreAuthorize("hasRole('ROLE_USER_CTO')")
    public String ctoAccess() {
        return "CTO Board.";
    }
    @GetMapping("/ceo")
    @PreAuthorize("hasRole('USER_CEO')")
    public String ceoAccess() {
        return "CEO Board.";
    }
}

