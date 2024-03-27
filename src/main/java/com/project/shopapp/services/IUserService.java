package com.project.shopapp.services;

import com.project.shopapp.dtos.UserDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    
    String login(String numberPhone, String password, Long roleId) throws Exception;
    
    User getUserDetailsFromToken(String token) throws Exception;
}
