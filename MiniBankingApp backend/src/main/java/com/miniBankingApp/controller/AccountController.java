package com.miniBankingApp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Düzgün import, Apache değil!
import org.springframework.web.bind.annotation.*;

import com.miniBankingApp.DTO.AccountDTO;
import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.service.IAccountService;
import com.miniBankingApp.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final IAccountService accountService;
    private final IUserService userService;

    // 1. hesap yaratma
    @PostMapping("/create")
    @Operation(summary = "Yeni hesap kaydı yapar")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO dto, Authentication auth) {
        UserDTO userDto = userService.findDtoByUsername(auth.getName());
        AccountDTO saved = accountService.createAccount(dto, userDto);
        return ResponseEntity.ok(saved);
    }

    // 2. hesap arama. Numara ve isimle
    @GetMapping("/search")
    @Operation(summary = "hesap arar")
    public ResponseEntity<List<AccountDTO>> searchAccounts(
            @RequestParam(required = false) String number,
            @RequestParam(required = false) String name,
            Authentication auth
    ) {
        UserDTO userDto = userService.findDtoByUsername(auth.getName());
        List<AccountDTO> results = accountService.searchAccounts(userDto, number, name);
        return ResponseEntity.ok(results);
    }

    // 3. hesap güncelleme
    @PutMapping("/{id}")
    @Operation(summary = "hesap günceller")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable String id,
            @RequestBody AccountDTO dto,
            Authentication auth
    ) {
        UserDTO userDto = userService.findDtoByUsername(auth.getName());
        AccountDTO updated = accountService.updateAccount(id, dto, userDto);
        return ResponseEntity.ok(updated);
    }

    // 4. hesap silme
    @DeleteMapping("/{id}")
    @Operation(summary = "Kullanıcı siler")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id, Authentication auth) {
        UserDTO userDto = userService.findDtoByUsername(auth.getName());
        accountService.deleteAccount(id, userDto);
        return ResponseEntity.noContent().build();
    }

    // 5. hesao detayları
    @GetMapping("/{id}")
    @Operation(summary = "hesap detay getirir")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable String id, Authentication auth) {
        UserDTO userDto = userService.findDtoByUsername(auth.getName());
        AccountDTO dto = accountService.getAccount(id, userDto);
        return ResponseEntity.ok(dto);
    }
}