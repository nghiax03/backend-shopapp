package com.project.shopapp.controllers;

import java.util.List;
import java.util.Locale;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.project.shopapp.component.LocalizationUtils;
import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.models.User;
import com.project.shopapp.responses.LoginResponse;
import com.project.shopapp.responses.RegisterResponse;
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
						.getLocalizationUtils(MessageKeys.PASSWORD_NOT_MATCH));
			}
			User user =  userService.createUser(userDTO);
			registerResponse.setMessage(localizationUtils.getLocalizationUtils(MessageKeys.REGISTER_SUCCESSFULLY));
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
				userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
			
			return ResponseEntity.ok(LoginResponse.builder()
					.message(localizationUtils.getLocalizationUtils(MessageKeys
							.LOGIN_SUCCESSFULLY))
					.token(token)
					.build());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(LoginResponse.builder()
					.message(localizationUtils.getLocalizationUtils(MessageKeys.LOGIN_FAILED,
							e.getMessage()))
					.build());
		}
	}
}
