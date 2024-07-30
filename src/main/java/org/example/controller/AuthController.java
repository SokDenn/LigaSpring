package org.example.controller;

import org.example.Jwt.JwtRequest;
import org.example.Jwt.JwtResponse;
import org.example.Jwt.JwtUtil;
import org.example.exceptions.AppError;
import org.example.security.SecurityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private SecurityValidator securityValidator;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            Authentication authentication = securityValidator.authenticate(authenticationRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            String accessToken = jwtUtil.generateToken(userDetails, true);
            String refreshToken = jwtUtil.generateToken(userDetails, false);

            ResponseCookie accessTokenCookie = jwtUtil.createAccessTokenCookie(accessToken);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .body(new JwtResponse(accessToken, refreshToken));

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(@RequestBody JwtRequest refreshRequest) throws Exception {
        String refreshToken = refreshRequest.getToken();

        String username = jwtUtil.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtUtil.validateToken(refreshToken, userDetails)) {

            String accessToken = jwtUtil.generateToken(userDetails, true);
            ResponseCookie accessTokenCookie = jwtUtil.createAccessTokenCookie(accessToken);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                    .body(new JwtResponse(accessToken, refreshToken));

        } else {
            return ResponseEntity.status(403).body("Некорректный refresh token");
        }
    }
}
