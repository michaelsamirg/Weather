package com.orange.weather.dao;

import java.util.List;

import com.orange.weather.model.User;

public interface UserDao {

	public User findByEmail(String username);
	
	public List<?> findUserRole(int userId);
	
	public void save(User user);
}