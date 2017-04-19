package com.orange.weather.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.orange.weather.enums.TempCategory;

@Entity
@Table(name = "daily_note")
public class DailyNote {
	private int id;
	private TempCategory category;
	private Date date;
	private String notes;
	

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "category")
	@Enumerated(EnumType.STRING)
	public TempCategory getCategory() {
		return category;
	}

	public void setCategory(TempCategory category) {
		this.category = category;
	}

	@Column(name = "date")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
