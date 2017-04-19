package com.orange.weather.model;

import java.util.Iterator;
import java.util.List;

import com.orange.weather.enums.TempCategory;

public class DailyNotesForm {
	private String value1To10 = "";
	private String value10To15 = "";
	private String value15To20 = "";
	private String valueGt20 = "";

	public DailyNotesForm()
	{
		super();
	}
	
	public DailyNotesForm(List<?> list) {
		if (list != null) {
			for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
				DailyNote dailyNote = (DailyNote) iterator.next();

				if (dailyNote.getCategory().equals(TempCategory.TEMP_1_10))
					value1To10 = dailyNote.getNotes();
				else if (dailyNote.getCategory().equals(TempCategory.TEMP_10_15))
					value10To15 = dailyNote.getNotes();
				else if (dailyNote.getCategory().equals(TempCategory.TEMP_15_20))
					value15To20 = dailyNote.getNotes();
				else if (dailyNote.getCategory().equals(TempCategory.TEMP_GT_20))
					valueGt20 = dailyNote.getNotes();
			}
		}
	}

	public String getValue1To10() {
		return value1To10;
	}

	public void setValue1To10(String value1To10) {
		this.value1To10 = value1To10;
	}

	public String getValue10To15() {
		return value10To15;
	}

	public void setValue10To15(String value10To15) {
		this.value10To15 = value10To15;
	}

	public String getValue15To20() {
		return value15To20;
	}

	public void setValue15To20(String value15To20) {
		this.value15To20 = value15To20;
	}

	public String getValueGt20() {
		return valueGt20;
	}

	public void setValueGt20(String valueGt20) {
		this.valueGt20 = valueGt20;
	}

	public String getNote(TempCategory category)
	{
		if(category.equals(TempCategory.TEMP_1_10))
			return getValue1To10();
		else if(category.equals(TempCategory.TEMP_10_15))
			return getValue10To15();
		else if(category.equals(TempCategory.TEMP_15_20))
			return getValue15To20();
		else
			return getValueGt20();
	}
}
