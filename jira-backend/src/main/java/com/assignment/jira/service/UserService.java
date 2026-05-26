package com.assignment.jira.service;

import com.assignment.jira.dto.UserDto;
import com.assignment.jira.dto.UserRequest;

import java.util.List;

public interface UserService {
    UserDto createUser(UserRequest request);
    UserDto updateUser(Long id, UserRequest request);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
}
