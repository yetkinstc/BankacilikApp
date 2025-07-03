package com.miniBankingApp.service;

import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.entity.User;

public interface IUserService {
	void registerUser(UserDTO userDto);
	UserDTO findDtoByUsername(String username);
}
