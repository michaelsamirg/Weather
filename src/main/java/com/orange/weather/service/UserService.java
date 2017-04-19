package com.orange.weather.service;

import com.orange.weather.model.User;

public interface UserService {

	public void save(User user);

	public User findByEmail(String email);
}
