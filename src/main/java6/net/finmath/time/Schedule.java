/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 07.09.2013
 */

package net.finmath.time;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.threeten.bp.LocalDate;

import net.finmath.time.daycount.DayCountConventionInterface;

/**
 * A schedule of interest rate periods with
 * a fixing and payment.
 *
 * The periods have two representations: one a {@link net.finmath.time.Period}
 * which contains {@link org.threeten.bp.LocalDate} dates and
 * an alternative representation using doubles.
 *
 * Within a schedule, the mapping from doubles to dates is one to one.
 *
 * @author Christian Fries
 * @version 1.0
 */
public class Schedule implements ScheduleInterface {

	private			LocalDate					referenceDate;

	private List<Period>			periods;
	private DayCountConventionInterface	daycountconvention;

	private double[] fixingTimes;
	private double[] paymentTimes;
	private double[] periodStartTimes;
	private double[] periodEndTimes;
	private double[] periodLength;

	public Schedule(LocalDate referenceDate, DayCountConventionInterface daycountconvention, Period... periods) {
		this(referenceDate, Arrays.asList(periods), daycountconvention);
	}

	public Schedule(LocalDate referenceDate, List<Period> periods, DayCountConventionInterface daycountconvention) {
		super();
		this.referenceDate = referenceDate;
		this.periods = periods;
		this.daycountconvention = daycountconvention;

		// Precalculate dates to yearfrac doubles
		fixingTimes = new double[periods.size()];
		paymentTimes = new double[periods.size()];
		periodStartTimes = new double[periods.size()];
		periodEndTimes = new double[periods.size()];
		periodLength = new double[periods.size()];
		for(int periodIndex=0; periodIndex < periods.size(); periodIndex++) {
			fixingTimes[periodIndex] = FloatingpointDate.getFloatingPointDateFromDate(referenceDate, periods.get(periodIndex).getFixing());
			paymentTimes[periodIndex] = FloatingpointDate.getFloatingPointDateFromDate(referenceDate, periods.get(periodIndex).getPayment());
			periodStartTimes[periodIndex] = FloatingpointDate.getFloatingPointDateFromDate(referenceDate, periods.get(periodIndex).getPeriodStart());
			periodEndTimes[periodIndex] = FloatingpointDate.getFloatingPointDateFromDate(referenceDate, periods.get(periodIndex).getPeriodEnd());
			periodLength[periodIndex] = daycountconvention.getDaycountFraction(periods.get(periodIndex).getPeriodStart(), periods.get(periodIndex).getPeriodEnd());
		}
	}

	@Override
	public LocalDate getReferenceDate() {
		return referenceDate;
	}

	@Override
	public List<Period> getPeriods() {
		return periods;
	}

	@Override
	public DayCountConventionInterface getDaycountconvention() {
		return daycountconvention;
	}

	@Override
	public int getNumberOfPeriods() {
		return periods.size();
	}

	@Override
	public Period getPeriod(int periodIndex) {
		return periods.get(periodIndex);
	}

	@Override
	public double getFixing(int periodIndex) {
		return fixingTimes[periodIndex];
	}

	@Override
	public double getPayment(int periodIndex) {
		return paymentTimes[periodIndex];
	}

	@Override
	public double getPeriodStart(int periodIndex) {
		return periodStartTimes[periodIndex];
	}

	@Override
	public double getPeriodEnd(int periodIndex) {
		return periodEndTimes[periodIndex];
	}

	@Override
	public double getPeriodLength(int periodIndex) {
		return periodLength[periodIndex];
	}

	@Override
	public Iterator<Period> iterator() {
		return periods.iterator();
	}

	@Override
	public String toString() {
		String periodOutputString = "Periods (fixing, periodStart, periodEnd, payment):";
		for(int periodIndex=0; periodIndex<periods.size(); periodIndex++) {
			periodOutputString += "\n" + periods.get(periodIndex).getFixing() + ", " +
					periods.get(periodIndex).getPeriodStart() + ", " +
					periods.get(periodIndex).getPeriodEnd() + ", " +
					periods.get(periodIndex).getPayment();
		}
		return "Schedule [referenceDate=" + referenceDate + ", daycountconvention=" + daycountconvention + "\n" + periodOutputString + "]";
	}
}
