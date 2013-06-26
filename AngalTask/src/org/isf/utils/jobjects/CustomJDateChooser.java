/**
 * 
 */
package org.isf.utils.jobjects;

import java.awt.Font;
import java.util.Date;

import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

/**
 * @author Mwithi
 * 
 * JDateChooser override, needs JCalendar(r)
 * it overrides Font attribution
 *
 */
public class CustomJDateChooser extends JDateChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public CustomJDateChooser() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dateEditor
	 */
	public CustomJDateChooser(IDateEditor dateEditor) {
		super(dateEditor);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param date
	 */
	public CustomJDateChooser(Date date) {
		super(date);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param date
	 * @param dateFormatString
	 */
	public CustomJDateChooser(Date date, String dateFormatString) {
		super(date, dateFormatString);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param date
	 * @param dateFormatString
	 * @param dateEditor
	 */
	public CustomJDateChooser(Date date, String dateFormatString,
			IDateEditor dateEditor) {
		super(date, dateFormatString, dateEditor);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param datePattern
	 * @param maskPattern
	 * @param placeholder
	 */
	public CustomJDateChooser(String datePattern, String maskPattern,
			char placeholder) {
		super(datePattern, maskPattern, placeholder);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param jcal
	 * @param date
	 * @param dateFormatString
	 * @param dateEditor
	 */
	public CustomJDateChooser(JCalendar jcal, Date date,
			String dateFormatString, IDateEditor dateEditor) {
		super(jcal, date, dateFormatString, dateEditor);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @author Mwithi
	 * 
	 * Override
	 * 
	 * @param font
	 * @param calendar - if true, set Font for the popup calendar also
	 */
	public void setFont(Font font, boolean calendar) {
		if (isInitialized) {
			dateEditor.getUiComponent().setFont(font);
			if (calendar) jcalendar.setFont(font);
		}
	}

	@Override
	public void setFont(Font font) {
		setFont(font, true);
	}
}
