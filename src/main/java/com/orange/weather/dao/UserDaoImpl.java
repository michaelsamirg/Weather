package com.orange.weather.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.orange.weather.model.User;
import com.orange.weather.model.UserRole;

public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

	final static Logger logger = Logger.getLogger(UserDaoImpl.class);
	
	@Transactional(readOnly=true)
	public User findByEmail(String email) {

		List<?> users = new ArrayList<User>();

		try {
			users = this.getHibernateTemplate().find("from User where email = '" + email + "'");
					

			if (users.size() > 0) {
				return (User)users.get(0);
			} else {
				return null;
			}
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	@Transactional(readOnly=true)
	public List<?> findUserRole(int userId)
	{
		List<?> userRoles = new ArrayList<User>();

		
		try {
			userRoles = this.getHibernateTemplate().find("from UserRole where user.id = " + userId);
			
			if (userRoles.size() > 0) {
				return userRoles;
			} else {
				return null;
			}
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * save user and its role
	 */
	@Transactional(readOnly = false)
	public void save(User user) {
		try {
			this.getHibernateTemplate().save(user);
			UserRole userRole = new UserRole();
			userRole.setUser(user);
			userRole.setRole("ROLE_USER");
			this.getHibernateTemplate().save(userRole);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}