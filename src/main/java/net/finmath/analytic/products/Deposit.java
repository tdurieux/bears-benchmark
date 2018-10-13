package net.finmath.analytic.products;

import net.finmath.analytic.model.AnalyticModelInterface;
import net.finmath.analytic.model.curves.DiscountCurveInterface;
import net.finmath.stochastic.RandomVariableInterface;
import net.finmath.time.ScheduleInterface;

/**
 * Implements the valuation of the (overnight) deposit (maturity t+1 or t+2). May be used in curve calibration.
 *
 * For definition and convention see Ametrano/Bianchetti (2013). Following the notation there,...
 * <ul>
 * 	<li>for deposit ON set spot offset = 0, start = 0D, maturity = 1D</li>
 *  <li>for deposit TN set spot offset = 1, start = 0D, maturity = 1D</li>
 *  <li>for deposit ST set spot offset = 2, start = 0D, maturity as given.</li>
 * </ul>
 * Date rolling convention: "following" for T &lt; 1M, "modified following, eom" for T &ge; 1
 *
 * The getValue method returns the value df(T) * (1.0+rate*periodLength) - df(t),
 * where t = schedule.getPeriodStart(0), T = schedule.getPayment(0) and df denotes discountCurve.getDiscountFactor.
 *
 * This corresponds to the valuation of an investment of 1 in t, paid back as (1.0+rate*periodLength) in time T.
 *
 * @author Rebecca Declara
 * @author Christian Fries
 */
public class Deposit extends AbstractAnalyticProduct implements AnalyticProductInterface{

	private ScheduleInterface	schedule;
	private double				rate;
	private String				discountCurveName;

	/**
	 * @param schedule The schedule of the deposit consisting of one period, providing start, payment and periodLength.
	 * @param rate The deposit rate.
	 * @param discountCurveName The discount curve name.
	 */
	public Deposit(ScheduleInterface schedule, double rate, String discountCurveName) {
		super();
		this.schedule =  schedule;
		this.rate = rate;
		this.discountCurveName = discountCurveName;

		// Check schedule
		if(schedule.getNumberOfPeriods() > 1) {
			throw new IllegalArgumentException("Number of periods has to be 1: Change frequency to 'tenor'!");
		}
	}

	@Override
	public RandomVariableInterface getValue(double evaluationTime, AnalyticModelInterface model) {
		if(model==null) {
			throw new IllegalArgumentException("model==null");
		}

		DiscountCurveInterface discountCurve = model.getDiscountCurve(discountCurveName);
		if(discountCurve == null) {
			throw new IllegalArgumentException("No discount curve with name '" + discountCurveName + "' was found in the model:\n" + model.toString());
		}

		double maturity = schedule.getPayment(0);

		if (evaluationTime > maturity)
		{
			model.getRandomVariableForConstant(0.0); // after maturity the contract is worth nothing
		}

		double payoutDate	= schedule.getPeriodStart(0);
		double periodLength = schedule.getPeriodLength(0);
		RandomVariableInterface discountFactor = discountCurve.getDiscountFactor(model, maturity);
		RandomVariableInterface discountFactorPayout = discountCurve.getDiscountFactor(model, payoutDate);

		RandomVariableInterface value;
		if (evaluationTime > payoutDate) {
			value = discountFactor.mult(1.0 + rate * periodLength);
		}
		else {
			value = discountFactor.mult(1.0 + rate * periodLength).sub(discountFactorPayout);
		}

		return value.div(discountCurve.getDiscountFactor(model, evaluationTime));
	}

	/**
	 * Return the deposit rate implied by the given model's curve.
	 *
	 * @param model The given model containing the curve of name <code>discountCurveName</code>.
	 * @return The value of the deposit rate implied by the given model's curve.
	 */
	public RandomVariableInterface getRate(AnalyticModelInterface model) {
		if(model==null) {
			throw new IllegalArgumentException("model==null");
		}

		DiscountCurveInterface discountCurve = model.getDiscountCurve(discountCurveName);
		if(discountCurve == null) {
			throw new IllegalArgumentException("No discount curve with name '" + discountCurveName + "' was found in the model:\n" + model.toString());
		}

		double payoutDate = schedule.getPeriodStart(0);
		double maturity = schedule.getPayment(0);
		double periodLength = schedule.getPeriodLength(0);

		RandomVariableInterface discountFactor = discountCurve.getDiscountFactor(model, maturity);
		RandomVariableInterface discountFactorPayout = discountCurve.getDiscountFactor(model, payoutDate);

		return discountFactorPayout.div(discountFactor).sub(1).div(periodLength);
	}

	public ScheduleInterface getSchedule() {
		return schedule;
	}

	public String getDiscountCurveName() {
		return discountCurveName;
	}

	public double getRate() {
		return rate;
	}

	public double getPeriodEndTime() {
		double tenorEnd = schedule.getPeriodEnd(0);
		return tenorEnd;
	}

	public double getFixingTime() {
		double fixingDate = schedule.getFixing(0);
		return fixingDate;
	}

	@Override
	public String toString() {
		return "Deposit [schedule=" + schedule + ", rate=" + rate + ", forwardCurveName=" + discountCurveName + "]";
	}
}
