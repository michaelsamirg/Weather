package com.orange.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.orange.weather.dao.UserDao;
import com.orange.weather.model.User;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        
        userDao.save(user);
    }
	
	@Override
	public User findByEmail(String email) {
		return userDao.findByEmail(email);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
}
