package com.miniBankingApp.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.miniBankingApp.DTO.TransactionDTO;
import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.service.ITransactionService;
import com.miniBankingApp.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final ITransactionService transactionService;
    private final IUserService userService;

    // Para transferi
    @PostMapping("/transfer")
    @Operation(summary = "Para transferi yapar")
    public ResponseEntity<TransactionDTO> transferMoney(
            @RequestParam UUID fromAccountId,
            @RequestParam UUID toAccountId,
            @RequestParam BigDecimal amount,
            Authentication auth) {

        UserDTO userDto = userService.findDtoByUsername(auth.getName());
        TransactionDTO transaction = transactionService.transferMoney(fromAccountId, toAccountId, amount, userDto);
        return ResponseEntity.ok(transaction);
    }

    // Hesap işlem geçmişi
    @Operation(summary = "Hesap geçmişini gösterir")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistory(
            @PathVariable UUID accountId,
            Authentication auth) {

        UserDTO userDto = userService.findDtoByUsername(auth.getName());
        List<TransactionDTO> transactions = transactionService.getTransactionHistory(accountId, userDto);
        return ResponseEntity.ok(transactions);
    }
}
