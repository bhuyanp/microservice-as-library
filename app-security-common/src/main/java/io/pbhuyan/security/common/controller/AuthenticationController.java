package io.pbhuyan.security.common.controller;

import io.pbhuyan.security.common.JWTGenerator;
import io.pbhuyan.security.common.dto.AuthenticationResponse;
import io.pbhuyan.security.common.dto.LoginDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.pbhuyan.security.common.controller.AuthenticationController.AUTH_API_BASE_URI;

@RestController
@RequestMapping(AUTH_API_BASE_URI)
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;

    public static final String AUTH_API_BASE_URI ="/api/v1/auth";

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDto loginDto) {
        log.info("Login request: {}", loginDto);
        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginDto.username(), loginDto.password()));
            // Set it in the filter
            //SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            log.info("Login successful!!");
            return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
        } catch (Exception e) {
            log.error(null, e);
            return new ResponseEntity<>(new AuthenticationResponse(""), HttpStatus.UNAUTHORIZED);
        }
    }
}
