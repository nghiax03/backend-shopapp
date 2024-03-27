package com.project.shopapp.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.shopapp.component.JwtTokenUtils;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.PermissionDenyException;
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
	
	private final JwtTokenUtils  jwtTokenUtil;

	@Override
	public User createUser(UserDTO userDTO) throws Exception {
		//register
		String phoneNumber = userDTO.getPhoneNumber();
		if(userRepository.existsByPhoneNumber(phoneNumber)) {
			throw new DataIntegrityViolationException("Phone number alredy exists");
		}
		Role role = roleRepository.findById(userDTO.getRoleId())
   		     .orElseThrow(() -> new DataNotFoundException("Role not found"));
		if(role.getName().toUpperCase().equals(role.ADMIN)) {
			throw new PermissionDenyException("You cannot register an admin account");
		}
		User newUser = User.builder()
				       .fullName(userDTO.getFullName())
				       .phoneNumber(userDTO.getPhoneNumber())
				       .password(userDTO.getPassword())
				       .address(userDTO.getAddress())
				       .dateOfBirth(userDTO.getDataOfBirth())
				       .facebookAccountId(userDTO.getFacebookAccountId())
				       .googleAccountId(userDTO.getGoogleAccountId())
				       .active(true)
				       .build();     
	     newUser.setRole(role);
	     
	     if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() ==0) {
	    	 String password = userDTO.getPassword();
	    	 String encoderPassword = passwordEncoder.encode(password);
	    	 newUser.setPassword(encoderPassword);
	     }
	     
	     return userRepository.save(newUser);
		
	}

	@Override
	public String login(String numberPhone, String password, Long roleId) throws Exception{
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
		Optional<Role> optionalRole = roleRepository.findById(roleId);
		if(optionalRole.isEmpty() || !roleId.equals(existingUser.getRole().getId())) {
			throw new DataNotFoundException("Role does not exists");
		}
		if(!optionalUser.get().isActive()) {
			throw new DataNotFoundException("User is locked");
		}
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				numberPhone, password, existingUser.getAuthorities());
		//authenticate with java spring
		authenticationManager.authenticate(authenticationToken);
		return jwtTokenUtil.generateToken(optionalUser.get());
	}

	@Override
	public User getUserDetailsFromToken(String token) throws Exception {
		if(jwtTokenUtil.isTokenExpired(token)) {
			throw new Exception("Token is expired");
		}
		String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
		Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
		
		if(user.isPresent()) {
			return user.get();
		}
		else {
			throw new Exception("User not found");
		}
	}
}
