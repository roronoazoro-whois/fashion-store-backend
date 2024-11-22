package com.fashionstore.fashion_store_backend.service;

import com.fashionstore.fashion_store_backend.dto.LoginResponseDto;
import com.fashionstore.fashion_store_backend.exception.InvalidLoginException;
import com.fashionstore.fashion_store_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponseDto login(String email, String password, boolean rememberMe) {
        try {
            // Thực hiện xác thực
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            throw new InvalidLoginException("Email hoặc mật khẩu không chính xác.");
        }

        // Lấy thông tin người dùng
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .orElse("USER");

        // Tạo và trả về JWT
        String token = jwtUtil.generateToken(email, role, rememberMe);

        // Lấy thời gian hết hạn của token
        long expiration = jwtUtil.extractExpiration(token) / 1000;

        return new LoginResponseDto(token, email, role, expiration);
    }


}