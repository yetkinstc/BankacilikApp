package com.miniBankingApp.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miniBankingApp.DTO.TransactionDTO;
import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.entity.Account;
import com.miniBankingApp.entity.Transaction;
import com.miniBankingApp.entity.TransactionStatus;
import com.miniBankingApp.entity.User;
import com.miniBankingApp.exception.NotFoundException;
import com.miniBankingApp.repository.AccountRepository;
import com.miniBankingApp.repository.TransactionRepository;
import com.miniBankingApp.repository.UserRepository;
import com.miniBankingApp.service.ITransactionService;

@Service
public class TransactionServiceImpl implements ITransactionService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    private final UserRepository userRepo;
    private final TransactionMapping mapper;

    public TransactionServiceImpl(TransactionRepository transactionRepo, AccountRepository accountRepo,
                                  UserRepository userRepo, TransactionMapping mapper) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public TransactionDTO transferMoney(UUID fromAccId, UUID toAccId, BigDecimal amount, UserDTO userDto) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Para miktarı sıfırdan büyük olmalı");
        }

        User user = userRepo.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));

        Account fromAcc = accountRepo.findById(fromAccId)
                .orElseThrow(() -> new NotFoundException("Gönderen hesap bulunamadı"));

        Account toAcc = accountRepo.findById(toAccId)
                .orElseThrow(() -> new NotFoundException("Alıcı hesap bulunamadı"));

        if (!fromAcc.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu hesap senin değil");
        }

        if (fromAcc.getBalance().compareTo(amount) < 0) {
            Transaction failTx = new Transaction();
            failTx.setFrom(fromAcc);
            failTx.setTo(toAcc);
            failTx.setAmount(amount);
            failTx.setTransactionDate(LocalDateTime.now());
            failTx.setStatus(TransactionStatus.FAILED);
            transactionRepo.save(failTx);

            throw new RuntimeException("Bakiyen yetmiyor");
        }

        fromAcc.setBalance(fromAcc.getBalance().subtract(amount));
        toAcc.setBalance(toAcc.getBalance().add(amount));

        accountRepo.save(fromAcc);
        accountRepo.save(toAcc);

        Transaction tx = new Transaction();
        tx.setFrom(fromAcc);
        tx.setTo(toAcc);
        tx.setAmount(amount);
        tx.setTransactionDate(LocalDateTime.now());
        tx.setStatus(TransactionStatus.SUCCESS);

        Transaction savedTx = transactionRepo.save(tx);

        return mapper.toDto(savedTx);
    }

    @Override
    public List<TransactionDTO> getTransactionHistory(UUID accountId, UserDTO userDto) {
        User user = userRepo.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("Kullanıcı yok"));

        Account acc = accountRepo.findById(accountId).orElseThrow(() -> new NotFoundException("Hesap yok"));

        if (!acc.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu hesap sana ait değil");
        }

        List<Transaction> txList = transactionRepo.findByFromOrTo(acc, acc);

        return txList.stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
