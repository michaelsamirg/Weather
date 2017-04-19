package com.orange.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.orange.weather.dao.UserDao;
import com.orange.weather.model.User;
import com.orange.weather.model.UserRole;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional(readOnly = true)
    /**
     * override method to return the login user and its authorities 
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByEmail(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        
        List<?> userRoles = userDao.findUserRole(user.getId());
        for (Iterator<?> iterator = userRoles.iterator(); iterator.hasNext();) {
        	UserRole userRole = (UserRole) iterator.next();
		
            grantedAuthorities.add(new SimpleGrantedAuthority(userRole.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
    }

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
    
    
}
