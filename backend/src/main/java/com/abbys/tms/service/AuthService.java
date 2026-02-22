package com.abbys.tms.service;

import com.abbys.tms.data.user.dto.LoginRequest;
import com.abbys.tms.data.user.dto.RefreshTokenRequest;
import com.abbys.tms.data.user.dto.TokenPair;
import com.abbys.tms.data.user.repository.UserRepo;
import com.abbys.tms.exception.JwtValidationException;
import com.abbys.tms.exception.NotFound;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public TokenPair login(LoginRequest dto) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(),dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtService.generateTokenPair(auth);
    }

    public TokenPair refreshToken(@Valid RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new JwtValidationException("Invalid Refresh Token");
        }

        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        if (userDetails == null) {
            throw new NotFound("User not found");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

        String accessToken = jwtService.generateAccessToken(authenticationToken);
        return new TokenPair(accessToken, refreshToken);
    }
}
