/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 07.09.2013
 */

package net.finmath.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.format.DateTimeParseException;

import net.finmath.time.businessdaycalendar.BusinessdayCalendarAny;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarInterface;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarInterface.DateRollConvention;
import net.finmath.time.daycount.DayCountConventionInterface;
import net.finmath.time.daycount.DayCountConvention_30E_360;
import net.finmath.time.daycount.DayCountConvention_30E_360_ISDA;
import net.finmath.time.daycount.DayCountConvention_30U_360;
import net.finmath.time.daycount.DayCountConvention_ACT_360;
import net.finmath.time.daycount.DayCountConvention_ACT_365;
import net.finmath.time.daycount.DayCountConvention_ACT_ACT_ISDA;

/**
 * Generates a schedule based on some meta data (frequency, maturity, date roll convention, etc.).
 * A schedule is just a collection of {@link net.finmath.time.Period}s.
 *
 * <ul>
 * 	<li>The period length is specified via {@link net.finmath.time.ScheduleGenerator.Frequency}.
 * 	<li>The schedule generation considers short periods via the specification of {@link net.finmath.time.ScheduleGenerator.DaycountConvention}.</li>
 * 	<li>The schedule may use an externally provided business day adjustment via an object implementing {@link net.finmath.time.businessdaycalendar.BusinessdayCalendarInterface}</li>
 * 	<li>You may specify fixing and payment adjustments.
 * </ul>
 *
 * @author Christian Fries
 * @date 02.03.2014
 */
public class ScheduleGenerator {

	/**
	 * Possible frequencies supported by {@link ScheduleGenerator}.
	 *
	 * @author Christian Fries
	 */
	public enum Frequency {
		/** Daily periods. **/
		DAILY,
		/** Weekly periods. **/
		WEEKLY,
		/** One months periods. **/
		MONTHLY,
		/** Three months periods. **/
		QUARTERLY,
		/** Six months periods. **/
		SEMIANNUAL,
		/** Twelve months periods. **/
		ANNUAL,
		/** A single period, i.e., the period is as long as from start to maturity. **/
		TENOR
	}

	/**
	 * Possible day count conventions supported by {@link DaycountConvention}.
	 *
	 * @author Christian Fries
	 */
	public enum DaycountConvention {
		/** See {@link net.finmath.time.daycount.DayCountConvention_30E_360_ISDA }. **/
		E30_360_ISDA,
		/** See {@link net.finmath.time.daycount.DayCountConvention_30E_360 }. **/
		E30_360,
		/** See {@link net.finmath.time.daycount.DayCountConvention_30U_360 }. **/
		U30_360,
		/** See {@link net.finmath.time.daycount.DayCountConvention_ACT_360 }. **/
		ACT_360,
		/** See {@link net.finmath.time.daycount.DayCountConvention_ACT_365 }. **/
		ACT_365,
		/** See {@link net.finmath.time.daycount.DayCountConvention_ACT_ACT_ISDA }. **/
		ACT_ACT_ISDA,
		ACT_ACT;

		public static DaycountConvention getEnum(String string) {
			if(string == null) {
				throw new IllegalArgumentException();
			}
			if(string.equalsIgnoreCase("30e/360 isda")) {
				return E30_360_ISDA;
			}
			if(string.equalsIgnoreCase("e30/360 isda")) {
				return E30_360_ISDA;
			}
			if(string.equalsIgnoreCase("30e/360")) {
				return E30_360;
			}
			if(string.equalsIgnoreCase("e30/360")) {
				return E30_360;
			}
			if(string.equalsIgnoreCase("30/360")) {
				return E30_360;
			}
			if(string.equalsIgnoreCase("30u/360")) {
				return U30_360;
			}
			if(string.equalsIgnoreCase("u30/360")) {
				return U30_360;
			}
			if(string.equalsIgnoreCase("act/360")) {
				return ACT_360;
			}
			if(string.equalsIgnoreCase("act/365")) {
				return ACT_365;
			}
			if(string.equalsIgnoreCase("act/act isda")) {
				return ACT_ACT_ISDA;
			}
			if(string.equalsIgnoreCase("act/act")) {
				return ACT_ACT;
			}

			return DaycountConvention.valueOf(string.toUpperCase());
		}
	}

	/**
	 * Possible stub period conventions supported.
	 *
	 * @author Christian Fries
	 */
	public enum ShortPeriodConvention {
		/** The first period will be shorter, if a regular period does not fit. **/
		FIRST,
		/** The last period will be shorter, if a regular period does not fit. **/
		LAST
	}

	private ScheduleGenerator() {
	}

	/**
	 * Schedule generation for given {referenceDate,startDate,maturityDate}.
	 *
	 * Generates a schedule based on some meta data.
	 * <ul>
	 * 	<li>The schedule generation considers short stub periods at beginning or at the end.</li>
	 * 	<li>Date rolling is performed using the provided businessdayCalendar.</li>
	 * </ul>
	 *
	 * The reference date is used internally to represent all dates as doubles, i.e.
	 * t = 0 corresponds to the reference date.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param startDate The start date of the first period (unadjusted - adjustments take place during schedule generation).
	 * @param maturityDate The end date of the last period (unadjusted - adjustments take place during schedule generation).
	 * @param frequency The frequency.
	 * @param daycountConvention The daycount convention.
	 * @param shortPeriodConvention If short period exists, have it first or last.
	 * @param dateRollConvention Adjustment to be applied to the all dates.
	 * @param businessdayCalendar Businessday calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @param isUseEndOfMonth If ShortPeriodConvention is LAST and startDate is an end of month date, all period will be adjusted to EOM. If ShortPeriodConvention is FIRST and maturityDate is an end of month date, all period will be adjusted to EOM.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			LocalDate startDate,
			LocalDate maturityDate,
			Frequency frequency,
			DaycountConvention daycountConvention,
			ShortPeriodConvention shortPeriodConvention,
			DateRollConvention dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays,
			boolean isUseEndOfMonth
			)
	{
		/*
		 * Generate periods - note: we do not use any date roll convention
		 */
		ArrayList<Period> periods = new ArrayList<Period>();

		DayCountConventionInterface daycountConventionObject = null;
		switch (daycountConvention) {
		case E30_360_ISDA:
			daycountConventionObject = new DayCountConvention_30E_360_ISDA();
			break;
		case E30_360:
			daycountConventionObject = new DayCountConvention_30E_360();
			break;
		case U30_360:
			daycountConventionObject = new DayCountConvention_30U_360();
			break;
		case ACT_360:
			daycountConventionObject = new DayCountConvention_ACT_360();
			break;
		case ACT_365:
			daycountConventionObject = new DayCountConvention_ACT_365();
			break;
		case ACT_ACT_ISDA:
		case ACT_ACT:
		default:
			daycountConventionObject = new DayCountConvention_ACT_ACT_ISDA();
			break;
		}

		int periodLengthDays	= 0;
		int periodLengthWeeks	= 0;
		int periodLengthMonth	= 0;
		switch(frequency) {
		case DAILY:
			periodLengthDays	= 1;
			break;
		case WEEKLY:
			periodLengthWeeks	= 1;
			break;
		case MONTHLY:
			periodLengthMonth	= 1;
			break;
		case QUARTERLY:
			periodLengthMonth	= 3;
			break;
		case SEMIANNUAL:
			periodLengthMonth	= 6;
			break;
		case ANNUAL:
		default:
			periodLengthMonth	= 12;
			break;
		case TENOR:
			periodLengthMonth	= 100000;
			break;
		}

		// This should not happen.
		if(periodLengthDays == 0 && periodLengthWeeks == 0 && periodLengthMonth == 0) {
			throw new IllegalArgumentException("Schedule generation requires positive period length.");
		}
		if(shortPeriodConvention == ShortPeriodConvention.LAST) {
			/*
			 * Going forward on periodStartDate, starting with startDate as periodStartDate
			 */
			LocalDate periodStartDateUnadjusted	= startDate;
			LocalDate periodEndDateUnadjusted	= startDate;
			LocalDate periodStartDate			= businessdayCalendar.getAdjustedDate(periodStartDateUnadjusted, dateRollConvention);

			int periodIndex = 0;
			while(periodStartDateUnadjusted.isBefore(maturityDate)) {
				periodIndex++;
				// The following code only makes calculations on periodEndXxx while the periodStartXxx is only copied and used to check if we terminate
				// Determine period end
				if(isUseEndOfMonth && startDate.getDayOfMonth() == startDate.lengthOfMonth()) {
					periodEndDateUnadjusted = startDate
							.plusDays(1)
							.plusDays(periodLengthDays*periodIndex)
							.plusWeeks(periodLengthWeeks*periodIndex)
							.plusMonths(periodLengthMonth*periodIndex)
							.minusDays(1);
				}
				else {
					periodEndDateUnadjusted = startDate
							.plusDays(periodLengthDays*periodIndex)
							.plusWeeks(periodLengthWeeks*periodIndex)
							.plusMonths(periodLengthMonth*periodIndex);
				}
				if(periodEndDateUnadjusted.isAfter(maturityDate)) {
					periodEndDateUnadjusted 	= maturityDate;
					periodStartDateUnadjusted 	= maturityDate;	// Terminate loop (next periodEndDateUnadjusted)
				}

				// Adjust period
				LocalDate periodEndDate		= businessdayCalendar.getAdjustedDate(periodEndDateUnadjusted, dateRollConvention);

				// Skip empty periods
				if(periodStartDate.compareTo(periodEndDate) == 0) {
					continue;
				}

				// Roll fixing date
				LocalDate fixingDate = businessdayCalendar.getRolledDate(periodStartDate, fixingOffsetDays);
				// TODO: There might be an additional calendar adjustment of the fixingDate, if the index has its own businessdayCalendar.

				// Roll payment date
				LocalDate paymentDate = businessdayCalendar.getRolledDate(periodEndDate, paymentOffsetDays);
				// TODO: There might be an additional calendar adjustment of the paymentDate, if the index has its own businessdayCalendar.

				// Create period
				periods.add(new Period(fixingDate, paymentDate, periodStartDate, periodEndDate));

				periodStartDate				= periodEndDate;
				periodStartDateUnadjusted	= periodEndDateUnadjusted;
			}
		} else {
			/*
			 * Going backward on periodEndDate, starting with maturity as periodEndDate
			 */
			LocalDate periodStartDateUnadjusted	= maturityDate;
			LocalDate periodEndDateUnadjusted	= maturityDate;
			LocalDate periodEndDate				= businessdayCalendar.getAdjustedDate(periodEndDateUnadjusted, dateRollConvention);

			int periodIndex = 0;
			while(periodEndDateUnadjusted.isAfter(startDate)) {
				periodIndex++;
				// The following code only makes calculations on periodStartXxx while the periodEndXxx is only copied and used to check if we terminate
				// Determine period start
				if(isUseEndOfMonth && maturityDate.getDayOfMonth() == maturityDate.lengthOfMonth()) {
					periodStartDateUnadjusted = maturityDate
							.plusDays(1)
							.minusDays(periodLengthDays*periodIndex)
							.minusWeeks(periodLengthWeeks*periodIndex)
							.minusMonths(periodLengthMonth*periodIndex)
							.minusDays(1);
				}
				else {
					periodStartDateUnadjusted = maturityDate
							.minusDays(periodLengthDays*periodIndex)
							.minusWeeks(periodLengthWeeks*periodIndex)
							.minusMonths(periodLengthMonth*periodIndex);
				}

				if(periodStartDateUnadjusted.isBefore(startDate))	{
					periodStartDateUnadjusted	= startDate;
					periodEndDateUnadjusted 	= startDate;	// Terminate loop (next periodEndDateUnadjusted)
				}

				// Adjust period
				LocalDate periodStartDate	= businessdayCalendar.getAdjustedDate(periodStartDateUnadjusted, dateRollConvention);

				// Skip empty periods
				if(periodStartDate.compareTo(periodEndDate) == 0) {
					continue;
				}

				// Roll fixing date
				LocalDate fixingDate = businessdayCalendar.getRolledDate(periodStartDate, fixingOffsetDays);
				// TODO: There might be an additional calendar adjustment of the fixingDate, if the index has its own businessdayCalendar.

				// Roll payment date
				LocalDate paymentDate = businessdayCalendar.getRolledDate(periodEndDate, paymentOffsetDays);
				// TODO: There might be an additional calendar adjustment of the paymentDate, if the index has its own businessdayCalendar.

				// Create period
				periods.add(0, new Period(fixingDate, paymentDate, periodStartDate, periodEndDate));

				periodEndDate			= periodStartDate;
				periodEndDateUnadjusted	= periodStartDateUnadjusted;
			}
		}

		return new Schedule(referenceDate, periods, daycountConventionObject);
	}

	/**
	 * Schedule generation for given {referenceDate,startDate,maturityDate}.
	 *
	 * Generates a schedule based on some meta data.
	 * <ul>
	 * 	<li>The schedule generation considers short stub periods at beginning or at the end.</li>
	 * 	<li>Date rolling is performed using the provided businessdayCalendar.</li>
	 * </ul>
	 *
	 * The reference date is used internally to represent all dates as doubles, i.e.
	 * t = 0 corresponds to the reference date.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param startDate The start date of the first period (unadjusted - adjustments take place during schedule generation).
	 * @param maturityDate The end date of the last period (unadjusted - adjustments take place during schedule generation).
	 * @param frequency The frequency.
	 * @param daycountConvention The daycount convention.
	 * @param shortPeriodConvention If short period exists, have it first or last.
	 * @param dateRollConvention Adjustment to be applied to the all dates.
	 * @param businessdayCalendar Businessday calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			LocalDate startDate,
			LocalDate maturityDate,
			Frequency frequency,
			DaycountConvention daycountConvention,
			ShortPeriodConvention shortPeriodConvention,
			DateRollConvention dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		return createScheduleFromConventions(referenceDate, startDate, maturityDate, frequency, daycountConvention, shortPeriodConvention, dateRollConvention, businessdayCalendar, fixingOffsetDays, paymentOffsetDays, false);
	}

	/**
	 * Schedule generation for given {referenceDate,startDate,maturityDate}.
	 *
	 * Generates a schedule based on some meta data.
	 * <ul>
	 * 	<li>The schedule generation considers short stub periods at beginning or at the end.</li>
	 * 	<li>Date rolling is performed using the provided businessdayCalendar.</li>
	 * </ul>
	 *
	 * The reference date is used internally to represent all dates as doubles.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param startDate The start date of the first period (unadjusted - adjustments take place during schedule generation).
	 * @param maturityDate The end date of the last period (unadjusted - adjustments take place during schedule generation).
	 * @param frequency The frequency (as String).
	 * @param daycountConvention The daycount convention (as String).
	 * @param shortPeriodConvention If short period exists, have it first or last (as String).
	 * @param dateRollConvention Adjustment to be applied to the all dates (as String).
	 * @param businessdayCalendar Businessday calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			LocalDate startDate,
			LocalDate maturityDate,
			String frequency,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		return createScheduleFromConventions(
				referenceDate,
				startDate,
				maturityDate,
				Frequency.valueOf(frequency.replace("/", "_").toUpperCase()),
				DaycountConvention.getEnum(daycountConvention),
				ShortPeriodConvention.valueOf(shortPeriodConvention.replace("/", "_").toUpperCase()),
				DateRollConvention.getEnum(dateRollConvention),
				businessdayCalendar,
				fixingOffsetDays,
				paymentOffsetDays
				);
	}

	/**
	 * Schedule generation for given {referenceDate,startDate,maturityDate}. Method using Date instead of LocalDate for backward compatibility.
	 *
	 * Generates a schedule based on some meta data.
	 * <ul>
	 * 	<li>The schedule generation considers short stub periods at beginning or at the end.</li>
	 * 	<li>Date rolling is performed using the provided businessdayCalendar.</li>
	 * </ul>
	 *
	 * The reference date is used internally to represent all dates as doubles.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param startDate The start date of the first period (unadjusted - adjustments take place during schedule generation).
	 * @param maturityDate The end date of the last period (unadjusted - adjustments take place during schedule generation).
	 * @param frequency The frequency (as String).
	 * @param daycountConvention The daycount convention (as String).
	 * @param shortPeriodConvention If short period exists, have it first or last (as String).
	 * @param dateRollConvention Adjustment to be applied to the all dates (as String).
	 * @param businessdayCalendar Businessday calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			Date referenceDate,
			Date startDate,
			Date maturityDate,
			String frequency,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		return createScheduleFromConventions(
				Instant.ofEpochMilli(referenceDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
				Instant.ofEpochMilli(startDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
				Instant.ofEpochMilli(maturityDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate(),
				frequency,
				daycountConvention,
				shortPeriodConvention,
				dateRollConvention,
				businessdayCalendar,
				fixingOffsetDays,
				paymentOffsetDays
				);
	}

	/**
	 * Simple schedule generation where startDate and maturityDate are calculated based on tradeDate, spotOffsetDays, startOffsetString and maturityString.
	 *
	 * The schedule generation considers short periods. Date rolling is ignored.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param tradeDate Base date for the schedule generation (used to build spot date).
	 * @param spotOffsetDays Number of business days to be added to the trade date to obtain the spot date.
	 * @param startOffsetString The start date as an offset from the spotDate (build from tradeDate and spotOffsetDays) entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param maturityString The end date of the last period entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param frequency The frequency (as String).
	 * @param daycountConvention The daycount convention (as String).
	 * @param shortPeriodConvention If short period exists, have it first or last (as String).
	 * @param dateRollConvention Adjustment to be applied to the all dates (as String).
	 * @param businessdayCalendar Business day calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			LocalDate tradeDate,
			int spotOffsetDays,
			String startOffsetString,
			String maturityString,
			String frequency,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		LocalDate spotDate = businessdayCalendar.getRolledDate(tradeDate, spotOffsetDays);
		LocalDate startDate = businessdayCalendar.createDateFromDateAndOffsetCode(spotDate, startOffsetString);
		LocalDate maturityDate = businessdayCalendar.createDateFromDateAndOffsetCode(startDate, maturityString);

		return createScheduleFromConventions(referenceDate, startDate, maturityDate, frequency, daycountConvention, shortPeriodConvention, dateRollConvention, businessdayCalendar, fixingOffsetDays, paymentOffsetDays);
	}

	/**
	 * Simple schedule generation where startDate and maturityDate are calculated based on referenceDate, spotOffsetDays, startOffsetString and maturityString.
	 *
	 * The schedule generation considers short periods. Date rolling is ignored.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param spotOffsetDays Number of business days to be added to the reference date to obtain the spot date.
	 * @param startOffsetString The start date as an offset from the spotDate (build from tradeDate and spotOffsetDays) entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param maturityString The end date of the last period entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param frequency The frequency (as String).
	 * @param daycountConvention The daycount convention (as String).
	 * @param shortPeriodConvention If short period exists, have it first or last (as String).
	 * @param dateRollConvention Adjustment to be applied to the all dates (as String).
	 * @param businessdayCalendar Business day calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @param isUseEndOfMonth If ShortPeriodConvention is LAST and startDate is an end of month date, all period will be adjusted to EOM. If ShortPeriodConvention is FIRST and maturityDate is an end of month date, all period will be adjusted to EOM.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			int spotOffsetDays,
			String startOffsetString,
			String maturityString,
			String frequency,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays,
			boolean isUseEndOfMonth
			)
	{
		LocalDate spotDate = businessdayCalendar.getRolledDate(referenceDate, spotOffsetDays);
		LocalDate startDate = businessdayCalendar.createDateFromDateAndOffsetCode(spotDate, startOffsetString);
		LocalDate maturityDate = businessdayCalendar.createDateFromDateAndOffsetCode(startDate, maturityString);

		return createScheduleFromConventions(
				referenceDate,
				startDate,
				maturityDate,
				Frequency.valueOf(frequency.replace("/", "_").toUpperCase()),
				DaycountConvention.getEnum(daycountConvention),
				ShortPeriodConvention.valueOf(shortPeriodConvention.replace("/", "_").toUpperCase()),
				DateRollConvention.getEnum(dateRollConvention),
				businessdayCalendar,
				fixingOffsetDays,
				paymentOffsetDays,
				isUseEndOfMonth
				);
	}

	/**
	 * Simple schedule generation where startDate and maturityDate are calculated based on referenceDate, spotOffsetDays, startOffsetString and maturityString.
	 *
	 * The schedule generation considers short periods. Date rolling is ignored.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param spotOffsetDays Number of business days to be added to the trade date to obtain the spot date.
	 * @param startOffsetString The start date as an offset from the spotDate (build from tradeDate and spotOffsetDays) entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param maturityString The end date of the last period entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param frequency The frequency (as String).
	 * @param daycountConvention The daycount convention (as String).
	 * @param shortPeriodConvention If short period exists, have it first or last (as String).
	 * @param dateRollConvention Adjustment to be applied to the all dates (as String).
	 * @param businessdayCalendar Business day calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			int spotOffsetDays,
			String startOffsetString,
			String maturityString,
			String frequency,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		// tradeDate=referenceDate
		return createScheduleFromConventions(referenceDate, referenceDate, spotOffsetDays, startOffsetString, maturityString, frequency, daycountConvention, shortPeriodConvention, dateRollConvention, businessdayCalendar, fixingOffsetDays, paymentOffsetDays);
	}

	/**
	 * Simple schedule generation where startDate and maturityDate are calculated based on referenceDate, startOffsetString and maturityString.
	 *
	 * The schedule generation considers short periods. Date rolling is ignored.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param startOffsetString The start date as an offset from the spotDate (build from tradeDate and spotOffsetDays) entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param maturityString The end date of the last period entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param frequency The frequency (as String).
	 * @param daycountConvention The daycount convention (as String).
	 * @param shortPeriodConvention If short period exists, have it first or last (as String).
	 * @param dateRollConvention Adjustment to be applied to the all dates (as String).
	 * @param businessdayCalendar Business day calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			String startOffsetString,
			String maturityString,
			String frequency,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		// spotOffsetDays=0
		return createScheduleFromConventions(referenceDate, 0, startOffsetString, maturityString, frequency, daycountConvention, shortPeriodConvention, dateRollConvention, businessdayCalendar, fixingOffsetDays, paymentOffsetDays);
	}

	/**
	 * Schedule generation with futureCodes (in the format DEC17). Futures are assumed to expire on the third wednesday in the respective month.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param futureCode Future code in the format DEC17
	 * @param startOffsetString The start date as an offset from the spotDate (build from tradeDate and spotOffsetDays) entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param maturityString The end date of the last period entered as a code like 1D, 1W, 1M, 2M, 3M, 1Y, etc.
	 * @param frequency The frequency (as String).
	 * @param daycountConvention The daycount convention (as String).
	 * @param shortPeriodConvention If short period exists, have it first or last (as String).
	 * @param dateRollConvention Adjustment to be applied to the all dates (as String).
	 * @param businessdayCalendar Business day calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 */
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			String futureCode,
			String startOffsetString,
			String maturityString,
			String frequency,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		int futureExpiryYearsShort = Integer.parseInt(futureCode.substring(futureCode.length()-2));
		String futureExpiryMonthsString = futureCode.substring(0,futureCode.length()-2);
		DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("dd/MMM/yy").toFormatter(Locale.ENGLISH);
		String futureExpiryString = "01/" + futureExpiryMonthsString + "/" + futureExpiryYearsShort;
		LocalDate futureExpiryDate;
		try{
			futureExpiryDate = LocalDate.parse(futureExpiryString, formatter);
		} catch(DateTimeParseException e) {
			throw new IllegalArgumentException("Error when parsing futureCode " + futureCode + ". Must be of format MMMYY with english month format (e.g. DEC17)");
		}
		// get third wednesday in month, adjust with following if no busday
		while(!futureExpiryDate.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
			futureExpiryDate = futureExpiryDate.plusDays(1);
		}
		futureExpiryDate = futureExpiryDate.plusWeeks(2);
		futureExpiryDate = businessdayCalendar.getAdjustedDate(futureExpiryDate, startOffsetString, DateRollConvention.FOLLOWING); // adjust to the next good busday

		LocalDate maturityDate = businessdayCalendar.createDateFromDateAndOffsetCode(futureExpiryDate, maturityString);

		return createScheduleFromConventions(
				referenceDate,
				futureExpiryDate,
				maturityDate,
				Frequency.valueOf(frequency.replace("/", "_").toUpperCase()),
				DaycountConvention.getEnum(daycountConvention),
				ShortPeriodConvention.valueOf(shortPeriodConvention.replace("/", "_").toUpperCase()),
				DateRollConvention.getEnum(dateRollConvention),
				businessdayCalendar,
				fixingOffsetDays,
				paymentOffsetDays
				);
	}

	/**
	 * Generates a schedule based on some meta data. The schedule generation
	 * considers short periods.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param startDate The start date of the first period.
	 * @param frequency The frequency.
	 * @param maturity The end date of the last period.
	 * @param daycountConvention The daycount convention.
	 * @param shortPeriodConvention If short period exists, have it first or last.
	 * @param dateRollConvention Adjustment to be applied to the all dates.
	 * @param businessdayCalendar Businessday calendar (holiday calendar) to be used for date roll adjustment.
	 * @param fixingOffsetDays Number of business days to be added to period start to get the fixing date.
	 * @param paymentOffsetDays Number of business days to be added to period end to get the payment date.
	 * @return The corresponding schedule
	 * @deprecated Will be removed in version 2.3
	 */
	@Deprecated
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			LocalDate startDate,
			String frequency,
			double maturity,
			String daycountConvention,
			String shortPeriodConvention,
			String dateRollConvention,
			BusinessdayCalendarInterface businessdayCalendar,
			int	fixingOffsetDays,
			int	paymentOffsetDays
			)
	{
		LocalDate maturityDate = createDateFromDateAndOffset(startDate, maturity);

		return createScheduleFromConventions(
				referenceDate,
				startDate,
				maturityDate,
				Frequency.valueOf(frequency.toUpperCase()),
				DaycountConvention.getEnum(daycountConvention),
				ShortPeriodConvention.valueOf(shortPeriodConvention.toUpperCase()),
				DateRollConvention.getEnum(dateRollConvention),
				businessdayCalendar,
				fixingOffsetDays,
				paymentOffsetDays
				);

	}

	/**
	 * Generates a schedule based on some meta data. The schedule generation
	 * considers short periods. Date rolling is ignored.
	 *
	 * @param referenceDate The date which is used in the schedule to internally convert dates to doubles, i.e., the date where t=0.
	 * @param startDate The start date of the first period.
	 * @param frequency The frequency.
	 * @param maturity The end date of the last period.
	 * @param daycountConvention The daycount convention.
	 * @param shortPeriodConvention If short period exists, have it first or last.
	 * @return The corresponding schedule
	 * @deprecated Will be removed in version 2.3
	 */
	@Deprecated
	public static ScheduleInterface createScheduleFromConventions(
			LocalDate referenceDate,
			LocalDate startDate,
			String frequency,
			double maturity,
			String daycountConvention,
			String shortPeriodConvention
			)
	{
		return createScheduleFromConventions(
				referenceDate,
				startDate,
				frequency,
				maturity,
				daycountConvention,
				shortPeriodConvention,
				"UNADJUSTED",
				new BusinessdayCalendarAny(),
				0, 0);
	}

	/**
	 * Create a new date by "adding" a year fraction to the start date.
	 * The year fraction is interpreted in a 30/360 way. More specifically,
	 * every integer unit advances by a year, each remaining fraction of 12
	 * advances by a month and each remaining fraction of 30 advances a day.
	 *
	 * The function may be used to ease the creation of maturities in spreadsheets.
	 *
	 * @param baseDate The start date.
	 * @param offsetYearFrac The year fraction in 30/360 to be used for adding to the start date.
	 * @return A date corresponding the maturity.
	 */
	private static LocalDate createDateFromDateAndOffset(LocalDate baseDate, double offsetYearFrac) {

		// Years
		LocalDate maturity = baseDate.plusYears((int)offsetYearFrac);

		// Months
		offsetYearFrac = (offsetYearFrac - (int)offsetYearFrac) * 12;
		maturity = maturity.plusMonths((int)offsetYearFrac);

		// Days
		offsetYearFrac = (offsetYearFrac - (int)offsetYearFrac) * 30;
		maturity = maturity.plusDays((int)Math.round(offsetYearFrac));

		return maturity;
	}
}
