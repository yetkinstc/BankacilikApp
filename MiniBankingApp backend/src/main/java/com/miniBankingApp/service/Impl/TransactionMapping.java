package com.miniBankingApp.service.Impl;

import org.springframework.stereotype.Service;

import com.miniBankingApp.DTO.TransactionDTO;
import com.miniBankingApp.entity.Account;
import com.miniBankingApp.entity.Transaction;

@Service
public class TransactionMapping {

    public TransactionDTO toDto(Transaction entity) {
        if (entity == null) return null;

        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setFromAccountId(entity.getFrom().getId());
        dto.setToAccountId(entity.getTo().getId());
        dto.setAmount(entity.getAmount());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return dto;
    }

    public Transaction accountToEntity(TransactionDTO dto, Account from, Account to) {
        if (dto == null || from == null || to == null) return null;

        Transaction entity = new Transaction();
        entity.setFrom(from);
        entity.setTo(to);
        entity.setAmount(dto.getAmount());

        return entity;
    }
}
