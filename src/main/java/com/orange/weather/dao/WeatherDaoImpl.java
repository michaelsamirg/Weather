package com.orange.weather.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.orange.weather.enums.TempCategory;
import com.orange.weather.model.DailyNote;

public class WeatherDaoImpl extends HibernateDaoSupport implements WeatherDao {

	/**
	 * get current temp based on the range
	 */
	@Transactional(readOnly=true)
	public String getCurrentNote(TempCategory category) {
		
		DateTime date = new DateTime();
		DateTime startDate = date.millisOfDay().setCopy(3600000);
		DateTime endDate = date.hourOfDay().setCopy(23);
		endDate = endDate.minuteOfHour().setCopy(59);
		endDate = endDate.secondOfMinute().setCopy(59);
		
		List<?> list = findNoteListGeneral(startDate.toDate(), endDate.toDate(), category);
		
		if(list != null && list.size() > 0)
			return ((DailyNote)list.get(0)).getNotes();
		else
			return null;
	}

	/**
	 * list all daily notes based on search criteria
	 */
	@Transactional(readOnly=true)
	public List<?> findNoteList(Date startDate, Date endDate, TempCategory category) {
		
		List<?> list = findNoteListGeneral(startDate, endDate, category);
		
		return list;
	}

	@Transactional(readOnly=true)
	private List<?> findNoteListGeneral(Date startDate, Date endDate, TempCategory category)
	{
		List<?> list = new ArrayList<DailyNote>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DailyNote.class);
		
		if(startDate != null)
			detachedCriteria.add(Restrictions.ge("date", startDate));
		
		if(endDate != null)
			detachedCriteria.add(Restrictions.le("date", endDate));
		
		if(category != null)
			detachedCriteria.add(Restrictions.eq("category", category));
			
		list = this.getHibernateTemplate().findByCriteria(detachedCriteria);
		
		return list;
	}
	
	@Transactional(readOnly = false)
	public void saveDailyNote(DailyNote dailyNote)
	{
		this.getHibernateTemplate().save(dailyNote);
	}
	
	@Transactional(readOnly = false)
	public void updateDailyNote(DailyNote dailyNote)
	{
		this.getHibernateTemplate().update(dailyNote);
	}
}
