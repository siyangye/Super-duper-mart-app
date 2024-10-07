package com.bfs.hibernateprojectdemo.service;

import com.bfs.hibernateprojectdemo.dao.UserDao;
import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.domain.dto.user.UserLoginRequest;
import com.bfs.hibernateprojectdemo.exception.InvalidCredentialsException;
import com.bfs.hibernateprojectdemo.out.LoginAdminUserOut;
import com.bfs.hibernateprojectdemo.utils.JwtTokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private UserDao userDao;

    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    public void setUserDao(UserDao userDao, JwtTokenUtils jwtTokenUtils) {
        this.userDao = userDao;
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Transactional
    public User registerUser(User user) {
        if (userDao.getUserByUsername(user.getUsername()) != null || userDao.getUserByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Username or email already exists");
        }
        user.setRole(0);
        return userDao.add(user);
    }

    @Transactional
    public LoginAdminUserOut loginUser(UserLoginRequest loginRequest) {
        User user = userDao.getUserByUsername(loginRequest.getUsername());

        if (user != null && user.getPassword().equals(loginRequest.getPassword())) {
            LoginAdminUserOut loginAdminUserOut = new LoginAdminUserOut();
            BeanUtils.copyProperties(loginAdminUserOut, user);
            loginAdminUserOut.setUser(user);
            String token = jwtTokenUtils.createToken(convertToMap(user));
            loginAdminUserOut.setToken(token);
            return loginAdminUserOut;
        } else {
            throw new InvalidCredentialsException("username or password is not exist.");
        }
    }

    public Map<String, Object> convertToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getUserId());
        map.put("username", user.getUsername());
        map.put("password", user.getPassword());
        return map;
    }

    @Transactional
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Transactional
    public void addUser(User... users) {
        for (User u : users) {
            userDao.addUser(u);
        }
    }

    @Transactional
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
}