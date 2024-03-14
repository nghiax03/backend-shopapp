package com.project.shopapp.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.shopapp.component.JwtTokenUtil;
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
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtTokenUtil  jwtTokenUtil;

	@Override
	public User createUser(UserDTO userDTO) throws DataNotFoundException {
		//register
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
	    	 String encoderPassword = passwordEncoder.encode(password);
	    	 newUser.setPassword(encoderPassword);
	     }
	     
	     return userRepository.save(newUser);
		
	}

	@Override
	public String login(String numberPhone, String password) throws Exception{
		Optional<com.project.shopapp.models.User> optionalUser = userRepository.findByPhoneNumber(numberPhone);
		if(optionalUser.isEmpty()) {
			throw new DataNotFoundException("Invalid phonenumber / password");
		}
		User existingUser = optionalUser.get();
		//check password
		if(existingUser.getFacebookAccountId() == 0 
				&& existingUser.getGoogleAccountId() ==0) {
	    	 if(!passwordEncoder.matches(password, existingUser.getPassword())) {
	    		 throw new BadCredentialsException("Wrong Phone Number or Password");
	    	 }
	     }
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				numberPhone, password, existingUser.getAuthorities());
		//authenticate with java spring
		authenticationManager.authenticate(authenticationToken);
		return jwtTokenUtil.generateToken(optionalUser.get());
	}
}
