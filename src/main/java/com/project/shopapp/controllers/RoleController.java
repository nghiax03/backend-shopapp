package com.project.shopapp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopapp.models.Role;
import com.project.shopapp.services.IRoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {
	private final IRoleService iRoleService;
	@GetMapping("")
	public ResponseEntity<?> getAllRoles(){
		List<Role> roles = iRoleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}
}
