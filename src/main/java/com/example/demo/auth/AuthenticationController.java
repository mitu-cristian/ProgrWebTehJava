package com.example.demo.auth;

import com.example.demo.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @RequestMapping(value = "/auth/authenticate", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody AuthenticationRequest request)
    throws Exception{
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @RequestMapping(value = "/auth/refresh-token", method = RequestMethod.POST)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        authService.refreshToken(request, response);
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value = "/auth/delete", method = RequestMethod.DELETE)
    public ResponseEntity<MessageResponse> deleteMyAccount(HttpServletRequest request)
            throws Exception {
        return ResponseEntity.ok(authService.deleteMyAccount(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/admin/register", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse> registerAdmin(@RequestBody @Valid RegisterRequest request)
    throws Exception{
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

}
