package com.miniBankingApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniBankingApp.DTO.LoginDTO;
import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.entity.User;
import com.miniBankingApp.service.IUserService;
import com.miniBankingApp.service.JwtService;
import com.miniBankingApp.service.Impl.UserMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@SecurityRequirement(name = "bearerAuth")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Kullanıcı kayıt ve login işlemleri")
public class UserController {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtService jwtService;
	
	@Autowired
    private IUserService userService;

	@Autowired
    private UserMapping userMapping;
	
    @Operation(summary = "Yeni kullanıcı kaydı yapar")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        return ResponseEntity.ok("Kayıt başarılı bir şekilde tamamlandı");
    }
    
    @Operation(summary = "Kullanıcı girişi yapar ve JWT döner")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDTO.getUsername(), loginDTO.getPassword()
                )
            );

            String jwt = jwtService.generateToken(auth);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Giriş başarısız: " + e.getMessage());
        }
    }

 
}
