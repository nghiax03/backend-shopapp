package com.project.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.dtos.UserLoginDTO;
import com.project.shopapp.services.IUserService;
import com.project.shopapp.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
	
	private IUserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO,//
			BindingResult result) {
		try {
			if(result.hasErrors()) {
				List<String> errorMessage = result.getFieldErrors().stream()
						.map(FieldError::getDefaultMessage).toList();
				return ResponseEntity.badRequest().body(errorMessage);
			}
			if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
				return ResponseEntity.badRequest().body("Password does not match");
			}
			userService.createUser(userDTO);
			return ResponseEntity.ok("Register Successfully");
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO){
		//check thong tin dang nhap va dang ky
		//traa ve token trong response
		String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
		return ResponseEntity.ok("token");
	}
}
