package com.miniBankingApp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniBankingApp.entity.Account;
import com.miniBankingApp.entity.User;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
	List<Account> findByUserAndNumberAndName(User user, String number, String name);
	List<Account> findByUserId(UUID userId);
}
