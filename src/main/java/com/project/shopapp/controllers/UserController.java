package com.project.shopapp.controllers;

import java.util.List;
import java.util.Locale;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dtos.UpdateUserDTO;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.responses.RegisterResponse;
import com.project.shopapp.responses.UserResponse;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.services.UserService;
import com.project.shopapp.utils.MessageKeys;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
	
	private final IUserService userService;
    private final LocalizationUtils localizationUtils;
	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody UserDTO userDTO,//
			BindingResult result) {
		RegisterResponse registerResponse = new RegisterResponse();
		try {
			if(result.hasErrors()) {
				List<String> errorMessage = result.getFieldErrors().stream()
						.map(FieldError::getDefaultMessage).toList();
				registerResponse.setMessage(errorMessage.toString());
				return ResponseEntity.badRequest().body(registerResponse);
			}
			if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
				registerResponse.setMessage(localizationUtils
						.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH));
			}
			User user =  userService.createUser(userDTO);
			registerResponse.setMessage(localizationUtils.getLocalizedMessage(MessageKeys.REGISTER_SUCCESSFULLY));
			registerResponse.setUser(user);
			return ResponseEntity.ok(registerResponse);
		} catch (Exception e) {
			// TODO: handle exception
			registerResponse.setMessage(e.getMessage());
			return ResponseEntity.badRequest().body(registerResponse);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
		//check thong tin dang nhap va dang ky
		//traa ve token trong response
		try {
			String token = 
				userService.login(
						userLoginDTO.getPhoneNumber(),
						userLoginDTO.getPassword(),
						userLoginDTO.getRoleId() == null ? 1 : userLoginDTO.getRoleId());
			
			return ResponseEntity.ok(LoginResponse.builder()
					.message(localizationUtils.getLocalizedMessage(MessageKeys
							.LOGIN_SUCCESSFULLY))
					.token(token)
					.build());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(LoginResponse.builder()
					.message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED,
							e.getMessage()))
					.build());
		}
	}
	
	@PostMapping("/details")
	public ResponseEntity<UserResponse> getUserDetails(
			@RequestHeader("Authorization") String authorizationHeader){
			try {
				String extractedToken = authorizationHeader.substring(7); //Loai bo Bearer
				User user = userService.getUserDetailsFromToken(extractedToken);
				return ResponseEntity.ok(UserResponse.formUser(user));
			} catch (Exception e) {
				return ResponseEntity.badRequest().build();
				// TODO: handle exception
			}
	}
	
	@PutMapping("/details/{userId}")
	public ResponseEntity<UserResponse> updateUserDetails(
			@PathVariable Long userId,
			@RequestBody UpdateUserDTO updatedUserDTO,
			@RequestHeader("Authorization") String authorizationHeader){
		try {
			String extractedToken = authorizationHeader.substring(7);
	        User user = userService.getUserDetailsFromToken(extractedToken);
	        if(user.getId() != userId) {
	        	return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	        }
	        User updatedUser = userService.updateUser(userId, updatedUserDTO);
	        return ResponseEntity.ok(UserResponse.formUser(updatedUser));
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
			// TODO: handle exception
		}
	}
}
