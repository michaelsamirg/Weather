package com.orange.weather.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.orange.weather.dao.WeatherDao;
import com.orange.weather.enums.TempCategory;
import com.orange.weather.model.DailyNote;

@Service
public class WeatherServiceImpl implements WeatherService {

	@Autowired
	private MessageSource messages;
	
	@Autowired
	private WeatherDao weatherNoteDao;
	
	final static Logger logger = Logger.getLogger(WeatherServiceImpl.class);
	
	/**
	 * get current weather status using weather API, parse the response json object to return the correct temperature 
	 */
	@Override
	public int getCurrentTemp() 
	{
		try {
			URL url = new URL(messages.getMessage("weather.api.url", null, null));
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			String result = "";
			while ((output = br.readLine()) != null) {
				result += output;
			}

			conn.disconnect();
			
			JSONObject jsonObject = new JSONObject(result);
			
			Double currentTemp = jsonObject.getJSONObject("current_observation").getDouble(("temp_c"));
			
			return currentTemp.intValue();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return 0;
	}
	
	public String getCurrentNote(TempCategory category) {
		return this.getWeatherNoteDao().getCurrentNote(category);
	}

	public List<?> findNoteList(Date startDate, Date endDate, TempCategory category) {
		return this.getWeatherNoteDao().findNoteList(startDate, endDate, category);
	}
	
	public void saveDailyNote(DailyNote dailyNote)
	{
		this.getWeatherNoteDao().saveDailyNote(dailyNote);
	}
	
	public void updateDailyNote(DailyNote dailyNote)
	{
		this.getWeatherNoteDao().updateDailyNote(dailyNote);
	}
	
	public MessageSource getMessages() {
		return messages;
	}

	public void setMessages(MessageSource messages) {
		this.messages = messages;
	}

	public WeatherDao getWeatherNoteDao() {
		return weatherNoteDao;
	}

	public void setWeatherNoteDao(WeatherDao weatherNoteDao) {
		this.weatherNoteDao = weatherNoteDao;
	}
}
