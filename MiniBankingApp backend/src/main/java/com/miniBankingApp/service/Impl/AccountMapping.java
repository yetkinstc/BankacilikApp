package com.miniBankingApp.service.Impl;

import org.springframework.stereotype.Service;

import com.miniBankingApp.DTO.AccountDTO;
import com.miniBankingApp.entity.Account;
import com.miniBankingApp.entity.User;
@Service
public class AccountMapping {

	public AccountDTO accountToDto(Account account) {
	    if (account == null) return null;

	    AccountDTO dto = new AccountDTO();
	    dto.setId(account.getId());  // ID ekle
	    dto.setNumber(account.getNumber());
	    dto.setName(account.getName());
	    dto.setBalance(account.getBalance());
	    dto.setUsername(account.getUser().getUsername());
	    return dto;
	}

	public Account accountToEntity(AccountDTO dto, User user) {
	    if (dto == null || user == null) return null;

	    Account account = new Account();
	    account.setId(dto.getId());  // ID ekle, varsa kullan
	    account.setNumber(dto.getNumber());
	    account.setName(dto.getName());
	    account.setBalance(dto.getBalance());
	    account.setUser(user);
	    return account;
	}
}