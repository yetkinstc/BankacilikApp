package com.miniBankingApp.service.Impl;

import org.springframework.stereotype.Service;

import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.entity.User;
@Service
public class UserMapping {
	public UserDTO toDto(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }


    public User userToEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
       
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        
        return user;
    }
}
