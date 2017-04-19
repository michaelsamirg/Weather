package com.orange.weather.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.orange.weather.enums.TempCategory;
import com.orange.weather.model.User;
import com.orange.weather.service.SecurityService;
import com.orange.weather.service.UserService;
import com.orange.weather.service.WeatherService;
import com.orange.weather.validator.UserValidator;

@Controller
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Autowired @Qualifier("userValidator")
	private UserValidator userValidator;
	
	@Autowired
	private WeatherService weatherService;

	@Autowired
	private MessageSource messages;
	
	final static Logger logger = Logger.getLogger(UserController.class);
	
	/**
	 * Login controller
	 * @param model
	 * @param error
	 * @param logout: if logout by user
	 * @return view name
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}
	
	/**
	 * registration controller
	 * @param userForm: instance bean of user
	 * @param bindingResult
	 * @param model
	 * @return view name
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
		
		return "registration";
	}

	/**
	 * Call after registration form submitted, validate the data if no error save the new user
	 * @param userForm: the user bean that holds the data
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "registration";
		}

		try {
			userService.save(userForm);
			securityService.autologin(userForm.getEmail(), userForm.getConfirmPassword());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "redirect:/welcome";
	}

	/**
	 * Welcome and default page
	 * @param model
	 * @return view name
	 */
	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public ModelAndView welcome(Model model) {
		
		int currentTemp = weatherService.getCurrentTemp();
		
	    User user = null;
	    
	    //check current user, if no user return to login
	    try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String name = auth.getName();
			user = userService.findByEmail(name);
		} catch (Exception e) {
			user = null;
			logger.error(e.getMessage(), e);
			
			ModelAndView view = new ModelAndView("login");
			return view;
		}
		TempCategory category = TempCategory.TEMP_1_10;
	    
		// get current weather status and note
	    if(currentTemp <= 10)
	    	category = TempCategory.TEMP_1_10;
	    else if(currentTemp > 10 && currentTemp <= 15)
	    	category = TempCategory.TEMP_10_15;
	    else if(currentTemp > 15 && currentTemp <= 20)
	    	category = TempCategory.TEMP_15_20;
	    else if(currentTemp > 20)
	    	category = TempCategory.TEMP_GT_20;
	    
	    String currentNote = null;
	    
	    try {
	    	currentNote = weatherService.getCurrentNote(category);
		} catch (Exception e) {
			currentNote = null;
			logger.error(e.getMessage(), e);
		}
	    
		if(currentNote == null || (currentNote != null && currentNote.length() == 0))
	    {
	    	currentNote = messages.getMessage("Temp.Note." + category, null, null);
	    }
	    
		ModelAndView view = new ModelAndView("welcome");
		view.addObject("temp", currentTemp);
		view.addObject("userName", user.getName());
		view.addObject("currentNote", currentNote);
		
		return view;
	}
}
