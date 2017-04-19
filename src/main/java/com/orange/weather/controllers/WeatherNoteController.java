package com.orange.weather.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.orange.weather.enums.TempCategory;
import com.orange.weather.model.DailyNote;
import com.orange.weather.model.DailyNotesForm;
import com.orange.weather.service.WeatherService;
import com.orange.weather.validator.DailyNoteValidator;

@Controller
@EnableAspectJAutoProxy(proxyTargetClass = true)
@RequestMapping(value = "/dailyNotes")
public class WeatherNoteController {

	@Autowired
	private WeatherService weatherService;
	
	@Autowired @Qualifier("dailyNoteValidator")
	private DailyNoteValidator dailyNoteValidator;

	final static Logger logger = Logger.getLogger(WeatherNoteController.class);
	
	/**
	 * the page to add/update today's note, if the notes are already saved it will display the current notes
	 * @param dailyNotesForm
	 * @param bindingResult
	 * @param model
	 * @return view name
	 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public ModelAndView dailyNotes(@ModelAttribute("dailyNotesForm") DailyNotesForm dailyNotesForm,
			BindingResult bindingResult, Model model) {
		ModelAndView view = new ModelAndView("dailyNotes");

		DateTime date = new DateTime();
		DateTime startDate = date.millisOfDay().setCopy(3600000);
		DateTime endDate = date.hourOfDay().setCopy(23);
		endDate = endDate.minuteOfHour().setCopy(59);
		endDate = endDate.secondOfMinute().setCopy(59);

		List<?> list = null;
		
		try {
			list = weatherService.findNoteList(startDate.toDate(), endDate.toDate(), null);
		} catch (Exception e) {
			list = null;
			logger.error(e.getMessage(), e);
		}
		
		if(list != null && list.size() > 0)
			dailyNotesForm = new DailyNotesForm(list);

		view.addObject("dailyNotesForm", dailyNotesForm);

		return view;
	}

	/**
	 * Save/update today's note, validate the data if the data is valid save or update the notes
	 * @param dailyNotesForm
	 * @param bindingResult
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String saveDailyNotes(@ModelAttribute("dailyNotesForm") DailyNotesForm dailyNotesForm, BindingResult bindingResult, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		dailyNoteValidator.validate(dailyNotesForm, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "dailyNotes";
		}
		
		DateTime date = new DateTime();
		DateTime startDate = date.millisOfDay().setCopy(3600000);
		DateTime endDate = date.hourOfDay().setCopy(23);
		endDate = endDate.minuteOfHour().setCopy(59);
		endDate = endDate.secondOfMinute().setCopy(59);
		
		DailyNote dailyNote = null;
		List<?> list = null;
		String[] categoryArray = {"TEMP_1_10", "TEMP_10_15", "TEMP_15_20", "TEMP_GT_20"};
		
		try {
			list = weatherService.findNoteList(startDate.toDate(), endDate.toDate(), null);
			for (int i = 0; i < categoryArray.length; i++) {

				boolean found = false;

				for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
					dailyNote = (DailyNote) iterator.next();

					// if data is already saved update the notes
					if (dailyNote.getCategory().name().equals(categoryArray[i])) {
						found = true;
						dailyNote.setNotes(dailyNotesForm.getNote(dailyNote.getCategory()));

						this.weatherService.updateDailyNote(dailyNote);
					}
				}

				// if data's not saved yet make new daily note
				if (!found) {
					dailyNote = new DailyNote();
					dailyNote.setDate(startDate.toDate());
					dailyNote.setCategory(TempCategory.valueOf(categoryArray[i]));
					dailyNote.setNotes(dailyNotesForm.getNote(dailyNote.getCategory()));

					this.weatherService.saveDailyNote(dailyNote);
				}
			} 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "dailyNotes";
		}
		return "redirect:/dailyNotes/saveSuccess";
	}

	/**
	 * open after save/update note is success
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "saveSuccess", method = RequestMethod.GET)
	public ModelAndView saveSuccess(Model model) {
		ModelAndView saveSuccess = new ModelAndView("saveSuccess");

		return saveSuccess;
	}
	
	/**
	 * A page to display old notes
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView dailyNotes(Model model) {
		ModelAndView listDailyNotes = new ModelAndView("listDailyNotes");

		return listDailyNotes;
	}

	/**
	 * Ajax call to load old data based on search criteria
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/search")
	@ResponseBody
	public List<?> searchDailyNote(HttpServletRequest request, HttpServletResponse response) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		String startDateString = request.getParameter("startDate");
		String endDateString = request.getParameter("endDate");
		
		DateTime startDate = null;
		DateTime endDate = null;
		
		try {
			startDate = new DateTime(dateFormat.parse(startDateString));
			startDate = startDate.millisOfDay().setCopy(3600000);
			
		} catch (ParseException e) {
			startDate = null;
			logger.error(e.getMessage(), e);
		}
		
		try {
			endDate = new DateTime(dateFormat.parse(endDateString));
			endDate = endDate.hourOfDay().setCopy(23);
			endDate = endDate.minuteOfHour().setCopy(59);
			endDate = endDate.secondOfMinute().setCopy(59);
		} catch (ParseException e) {
			endDate = null;
			
			logger.error(e.getMessage(), e);
		}
		
		List<?> list = null;
		
		try {
			list = weatherService.findNoteList(startDate != null ? startDate.toDate() : null,
					endDate != null ? endDate.toDate() : null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			list = new ArrayList<>();
		}
		return list;
		
	}
}
