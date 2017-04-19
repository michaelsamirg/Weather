package com.orange.weather.service;

import java.util.Date;
import java.util.List;

import com.orange.weather.enums.TempCategory;
import com.orange.weather.model.DailyNote;

public interface WeatherService {
	public int getCurrentTemp();
	
	public String getCurrentNote(TempCategory category);
	
	public List<?> findNoteList(Date startDate, Date endDate, TempCategory category);
	
	public void saveDailyNote(DailyNote dailyNote);
	
	public void updateDailyNote(DailyNote dailyNote);
}
