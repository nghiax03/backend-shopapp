package com.project.shopapp.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Role;
import com.project.shopapp.models.User;
import com.project.shopapp.repositories.RoleRepository;
import com.project.shopapp.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;

	@Override
	public User createUser(UserDTO userDTO) throws DataNotFoundException {
		String phoneNumber = userDTO.getPhoneNumber();
		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new DataIntegrityViolationException("Phone number alredy exists");
		}
		User newUser = User.builder()
				       .fullName(userDTO.getFullName())
				       .phoneNumber(userDTO.getPhoneNumber())
				       .password(userDTO.getPassword())
				       .address(userDTO.getAddress())
				       .dateOfBirth(userDTO.getDataOfBirth())
				       .facebookAccountId(userDTO.getFacebookAccountId())
				       .googleAccountId(userDTO.getGoogleAccountId())
				       .build();
		
	     Role role = roleRepository.findById(userDTO.getRoleId())
	    		     .orElseThrow(() -> new DataNotFoundException("Role not found"));
	     
	     newUser.setRole(role);
	     
	     if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() ==0) {
	    	 String password = userDTO.getPassword();
	     }
	     
	     return userRepository.save(newUser);
		
	}

	@Override
	public String login(String numberPhone, String password) {
		return null;
	}

}
