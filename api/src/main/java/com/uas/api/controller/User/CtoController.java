package com.uas.api.controller.User;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class CtoController {

    public String ctoAccess() {
        return "cto Dashboard";
    }
}
