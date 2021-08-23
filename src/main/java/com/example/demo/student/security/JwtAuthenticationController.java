package com.example.demo.student.security;

import com.example.demo.student.model.User;
import com.example.demo.student.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokUtil;
    private final JwtUserDetailService userDetailsService;
    private final UserRepository userRepository;

    public JwtAuthenticationController(AuthenticationManager authenticationManager,
                                       JwtTokenUtil jwtTokUtil,
                                       JwtUserDetailService userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokUtil = jwtTokUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

        final User user = userRepository.findUserByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid"));
        final String access = jwtTokUtil.generateToken(user);
        final String refreshToken = jwtTokUtil.generateRefreshToken(user);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return ResponseEntity.ok(new JwtResponse(access, refreshToken));
    }

    @PostMapping(value = "refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader(value = "token") String refreshToken) throws Exception {
        if (!refreshToken.startsWith("Bearer ")) {
            throw new IllegalStateException("Invalid refresh token");
        }
        String token = refreshToken.substring(7).trim();
        User user = userRepository.findByRefreshToken(token)
                .orElseThrow(() -> new IllegalStateException("User with refresh token not found"));
        if (!jwtTokUtil.validateToken(token, user.getEmail())) {
            throw new IllegalStateException("Expired");
        }
        final String access = jwtTokUtil.generateToken(user);
        final String newRefreshToken = jwtTokUtil.generateRefreshToken(user);
        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);
        return ResponseEntity.ok(new JwtResponse(access, newRefreshToken));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("User has been disabled", e);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid credentials", e);
        }
    }
}