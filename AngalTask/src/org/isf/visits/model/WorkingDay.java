package org.isf.visits.model;
/*
 * Questa classe descrive una giornata lavorativa dell'ambulatorio,
 * l'orario di inizio visite, l'orario di fine visita, il tempo dedicato
 * ad ogni visita, le ore di pausa
 */
public class WorkingDay {
	private String Day;
	private int StartHour;
	private int StartMinute;
	private int EndHour;
	private int EndMinute;
	private int StartHourPause;
	private int StartMinutePause;
	private int EndHourPause;
	private int EndMinutePause;
	private int TimeperVisit;
	
	public String getDay()
	{
		return this.Day;
	}
	public int getStartHour ()
	{
		return this.StartHour;
	}
	public int getStartMinute ()
	{
		return this.StartMinute;
	}
	public int getEndHour ()
	{
		return this.EndHour;
	}
	public int getEndMinute ()
	{
		return this.EndMinute;
	}
	public int getStartHourPause ()
	{
		return this.StartHourPause;
	}
	public int getStartMinutePause ()
	{
		return this.StartMinutePause;
	}
	public int getEndHourPause()
	{
		return this.EndHourPause;
	}
	public int getEndMinutePause()
	{
		return this.EndMinutePause;
	}
	public int getTimeperVisit()
	{
		return this.TimeperVisit;
	}
	public void setStartHour (int start)
	{
		this.StartHour=start;
	}
	public void setStartMinute (int startm)
	{
		this.StartMinute=startm;
	}
	public void setEndHour (int endhour)
	{
		this.EndHour=endhour;
	}
	public void setEndMinute (int endmin)
	{
		this.EndMinute=endmin;
	}
	public void setStartHourPause (int starthourpause)
	{
		this.StartHourPause=starthourpause;
	}
	public void setStartMinutePause (int startminutepause)
	{
		this.StartMinutePause=startminutepause;
	}
	public void setEndHourPause(int endhourpause)
	{
		this.EndHourPause=endhourpause;
	}
	public void setEndMinutePause(int endminutepause)
	{
		this.EndMinutePause=endminutepause;
	}
	public void setTimeperVisit(int timepervisit)
	{
		this.TimeperVisit=timepervisit;
	}
	public void setDay(String day)
	{
		this.Day=day;
	}
}
