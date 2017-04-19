package com.orange.weather.validator;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.orange.weather.model.User;

@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DailyNoteValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}

	/**
	 * validate daily note form, should provide notes for each range
	 */
	@Override
	public void validate(Object o, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value1To10", "NotEmpty");

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value10To15", "NotEmpty");

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "value15To20", "NotEmpty");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "valueGt20", "NotEmpty");
	}
}
