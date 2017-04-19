package com.orange.weather.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.orange.weather.model.User;
import com.orange.weather.service.UserService;
import com.orange.weather.util.Utilites;

@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UserValidator implements Validator {
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	/**
	 * validate user, email not empty and valid, name not empty, valid mobile number
	 * password > 5 and both password fields should be match
	 */
	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty");
		if (!Utilites.vaidateEmail(user.getEmail())) {
			errors.rejectValue("email", "Size.userForm.email");
		}
		if (userService.findByEmail(user.getEmail()) != null) {
			errors.rejectValue("email", "Duplicate.userForm.email");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");

		if (user.getMobile() != null && user.getMobile().length() > 0 && !Utilites.vaidateMobile(user.getMobile())) {
			errors.rejectValue("mobile", "Size.userForm.mobile");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (user.getPassword().length() < 5 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.userForm.password");
		}

		if (!user.getConfirmPassword().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
		}
	}
}
