package com.miniBankingApp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.miniBankingApp.DTO.AccountDTO;
import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.entity.User;


public interface IAccountService {

    AccountDTO createAccount(AccountDTO dto, UserDTO user);

    List<AccountDTO> searchAccounts(UserDTO user, String number, String name);

    AccountDTO updateAccount(String id, AccountDTO dto, UserDTO user);

    void deleteAccount(String id, UserDTO user);

    AccountDTO getAccount(String id, UserDTO user);
}
