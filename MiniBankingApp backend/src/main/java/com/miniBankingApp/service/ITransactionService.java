package com.miniBankingApp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.miniBankingApp.DTO.TransactionDTO;
import com.miniBankingApp.DTO.UserDTO;

public interface ITransactionService {

    TransactionDTO transferMoney(UUID fromAccountId, UUID toAccountId, BigDecimal amount, UserDTO userDto);

    List<TransactionDTO> getTransactionHistory(UUID accountId, UserDTO userDto);

}
