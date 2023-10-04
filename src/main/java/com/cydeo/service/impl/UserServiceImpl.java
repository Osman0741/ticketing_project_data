package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        return userRepository.findAll().stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        return userMapper.convertToDto(userRepository.findByUserName(username));
    }

    @Override
    public void save(UserDTO user) {

       userRepository.save(userMapper.convertToEntity(user));


    }

    @Override
    public void deleteByUserName(String username) {

        userRepository.deleteByUserName(username);

    }

    @Override
    public void update(UserDTO user) {

        User user1 = userRepository.findByUserName(user.getUserName());

        User converted = userMapper.convertToEntity(user);

        converted.setId(user1.getId());

        userRepository.save(converted);


    }

    @Override
    public void delete(String username) {

       User user= userRepository.findByUserName(username);
       user.setDeleted(true);
       userRepository.save(user);


    }

    @Override
    public List<UserDTO> listAllByRole(String role) {

      List<User> list =  userRepository.findByRoleDescriptionIgnoreCase(role);
        return list.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }
}
