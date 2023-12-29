package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String hi() {
        return "Hello World";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value =  "/hi-admin", method = RequestMethod.GET)
    public String hiAdmin() {return "Hello, admin!";}

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "hi-user", method = RequestMethod.GET)
    public String hiUser() {return "Hello, user!";}
}
