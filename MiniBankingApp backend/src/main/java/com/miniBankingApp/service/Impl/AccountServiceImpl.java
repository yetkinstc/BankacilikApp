package com.miniBankingApp.service.Impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.miniBankingApp.DTO.AccountDTO;
import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.entity.Account;
import com.miniBankingApp.entity.User;
import com.miniBankingApp.exception.NotFoundException;
import com.miniBankingApp.repository.AccountRepository;
import com.miniBankingApp.repository.UserRepository;
import com.miniBankingApp.service.IAccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapping accountMapping;
    private final UserMapping userMapping;
    private final UserRepository userRepository; 

    @Override
    public AccountDTO createAccount(AccountDTO dto, UserDTO userDto) {
        // Gerçek user'ı DB'den çek
        User userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountMapping.accountToEntity(dto, userEntity);
        account.setUser(userEntity);
        account.setId(null);

        Account saved = accountRepository.save(account);
        return accountMapping.accountToDto(saved);
    }

    @Override
    public AccountDTO updateAccount(String id, AccountDTO dto, UserDTO userDto) {
        UUID uuid = UUID.fromString(id);
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        User userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!account.getUser().getId().equals(userEntity.getId())) {
            throw new RuntimeException("Bu hesaba erişim yetkiniz yok");
        }

        account.setName(dto.getName());
        account.setNumber(dto.getNumber());
        account.setBalance(dto.getBalance());

        Account updated = accountRepository.save(account);
        return accountMapping.accountToDto(updated);
    }

    @Override
    public void deleteAccount(String id, UserDTO userDto) {
        UUID uuid = UUID.fromString(id);
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        User userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!account.getUser().getId().equals(userEntity.getId())) {
            throw new RuntimeException("Bu hesaba erişim yetkiniz yok");
        }

        accountRepository.delete(account);
    }

    @Override
    public AccountDTO getAccount(String id, UserDTO userDto) {
        UUID uuid = UUID.fromString(id);
        Account account = accountRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        User userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!account.getUser().getId().equals(userEntity.getId())) {
            throw new RuntimeException("Bu hesaba erişim yetkiniz yok");
        }
        return accountMapping.accountToDto(account);
    }

    @Override
    public List<AccountDTO> searchAccounts(UserDTO userDto, String number, String name) {
        User userEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Account> accounts = accountRepository.findByUserId(userEntity.getId());

        return accounts.stream()
                .filter(acc -> (number == null || acc.getNumber().contains(number)) &&
                               (name == null || acc.getName().toLowerCase().contains(name.toLowerCase())))
                .map(accountMapping::accountToDto)
                .collect(Collectors.toList());
    }
}