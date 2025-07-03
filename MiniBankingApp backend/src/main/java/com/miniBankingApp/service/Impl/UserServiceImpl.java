package com.miniBankingApp.service.Impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.miniBankingApp.DTO.UserDTO;
import com.miniBankingApp.entity.User;
import com.miniBankingApp.repository.UserRepository;
import com.miniBankingApp.service.IUserService;
import com.miniBankingApp.service.Impl.UserMapping;

@Service
public class UserServiceImpl implements IUserService {
	@Autowired
    private  UserRepository userRepository;
	
	@Autowired
	private UserMapping userMapping;  // DTO 
  
	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @Override
    public void registerUser(UserDTO userDTO) {
        
        User userEntity = userMapping.userToEntity(userDTO);


        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        // TODO: Şifre hash

        userRepository.save(userEntity);
    }
    
    @Override
    public UserDTO findDtoByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return userMapping.toDto(user); 
    }
}
