/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 15.09.2013
 */

package net.finmath.time.businessdaycalendar;

import org.threeten.bp.LocalDate;

/**
 * A business day calendar, where every day is a business day, expect
 * the TARGET holidays.
 *
 * @author Christian Fries
 * @version 1.0
 */
public class BusinessdayCalendarExcludingTARGETHolidays extends BusinessdayCalendar {

	/**
	 *
	 */
	private static final long serialVersionUID = -607317161321548729L;
	private BusinessdayCalendarInterface baseCalendar;
	private final BusinessdayCalendarInterface weekdayCalendar = new BusinessdayCalendarExcludingWeekends();

	/**
	 * Create TARGET business day calendar.
	 */
	public BusinessdayCalendarExcludingTARGETHolidays() {
	}

	/**
	 * Create TARGET business day calendar using a given business day calendar as basis.
	 *
	 * @param baseCalendar Calendar of business days.
	 */
	public BusinessdayCalendarExcludingTARGETHolidays(BusinessdayCalendarInterface baseCalendar) {
		this.baseCalendar = baseCalendar;
	}

	@Override
	public boolean isBusinessday(LocalDate date) {
		int day = date.getDayOfMonth();
		int month = date.getMonthValue();

		return	weekdayCalendar.isBusinessday(date)
				&& (baseCalendar == null || baseCalendar.isBusinessday(date))
				&&	!(day ==  1 && month ==  1)			// date is New Year
				&&	!isEasterSunday(date.plusDays(2))	// date is Good Friday
				&&	!isEasterSunday(date.minusDays(1))	// date is Easter Monday
				&&	!(day ==  1 && month ==  5)			// date is Labour Day
				&&	!(day == 25 && month == 12)			// date is Christmas
				&&	!(day == 26 && month == 12)			// date is Boxing Day
				;
	}

	/**
	 * Test a given date for being easter sunday.
	 *
	 * The method uses the algorithms sometimes cited as Meeus,Jones, Butcher Gregorian algorithm.
	 * Taken from http://en.wikipedia.org/wiki/Computus
	 *
	 * @param date The date to check.
	 * @return True, if date is easter sunday.
	 */
	public static boolean isEasterSunday(LocalDate date) {
		int y = date.getYear();
		int a = y % 19;
		int b = y / 100;
		int c = y % 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (19 * a + b - d - g + 15) % 30;
		int i = c / 4;
		int k = c % 4;
		int l = (32 + 2 * e + 2 * i - h - k) % 7;
		int m = (a + 11 * h + 22 * l) / 451;
		int easterSundayMonth	= (h + l - 7 * m + 114) / 31;
		int easterSundayDay		= ((h + l - 7 * m + 114) % 31) + 1;

		int month = date.getMonthValue();
		int day = date.getDayOfMonth();

		return (easterSundayMonth == month) && (easterSundayDay == day);
	}

	@Override
	public String toString() {
		return "BusinessdayCalendarExcludingTARGETHolidays [baseCalendar=" + baseCalendar + "]";
	}
}
