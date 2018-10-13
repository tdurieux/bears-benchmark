/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 22.11.2009
 */
package net.finmath.montecarlo.interestrate.products.components;

import java.util.ArrayList;
import java.util.Set;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.MonteCarloSimulationInterface;
import net.finmath.montecarlo.conditionalexpectation.MonteCarloConditionalExpectationRegression;
import net.finmath.montecarlo.conditionalexpectation.RegressionBasisFunctionsProvider;
import net.finmath.montecarlo.interestrate.LIBORModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.interestrate.products.AbstractLIBORMonteCarloProduct;
import net.finmath.stochastic.ConditionalExpectationEstimatorInterface;
import net.finmath.stochastic.RandomVariableInterface;

/**
 * An option.
 *
 * Implements the function <code>max(underlying(t)-strike,0)</code> for any <code>underlying</code> object
 * implementing an {@link net.finmath.montecarlo.interestrate.products.AbstractLIBORMonteCarloProduct}.
 *
 * The strike may be a fixed constant value or an object implementing
 * {@link net.finmath.montecarlo.interestrate.products.AbstractLIBORMonteCarloProduct}
 * (resulting in a stochastic strike or exchange option).
 *
 * More precise, the <code>getVaue</code> method returns the value
 * \[
 * 	\left\{
 * 		\begin{array}{ll}
 * 			U(t)-S(t) &amp; \text{if E(t) &gt; 0} \\
 * 			U(t)-S(t) &amp; \text{else.}
 * 		\end{array}
 * 	\right.
 * \]
 * where \( E \) is an estimator for the expectation of \( U(t)-S(t) \) and \( U \) is the value
 * returned by the call to <code>getValue</code> of the underlying product, which may return a
 * sum on discounted futures cash-flows / values (i.e. not yet performing the expectation) and
 * \( S \) is the strike (which may be a fixed value or another underlying product).
 *
 * @author Christian Fries
 * @version 1.2
 * @see net.finmath.montecarlo.interestrate.products.AbstractLIBORMonteCarloProduct
 */
public class Option extends AbstractProductComponent implements RegressionBasisFunctionsProvider {

	private static final long serialVersionUID = 2987369289230532162L;

	private final double							exerciseDate;
	private final double							strikePrice;
	private final AbstractLIBORMonteCarloProduct	underlying;
	private final AbstractLIBORMonteCarloProduct	strikeProduct;
	private final boolean							isCall;

	private final RegressionBasisFunctionsProvider	regressionBasisFunctionsProvider;

	/**
	 * Creates the function underlying(exerciseDate) &ge; strikePrice ? underlying : strikePrice
	 *
	 * @param exerciseDate The exercise date of the option (given as a double).
	 * @param strikePrice The strike price.
	 * @param isCall If true, the function implements is underlying(exerciseDate) &ge; strikePrice ? underlying : strikePrice. Otherwise it is underlying(exerciseDate) &lt; strikePrice ? underlying : strikePrice.
	 * @param underlying The underlying.
	 * @param regressionBasisFunctionsProvider Used to determine the regresssion basis functions for the conditional expectation operator.
	 */
	public Option(double exerciseDate, double strikePrice, boolean isCall, AbstractLIBORMonteCarloProduct underlying, RegressionBasisFunctionsProvider	regressionBasisFunctionsProvider) {
		super();
		this.exerciseDate	= exerciseDate;
		this.strikePrice	= strikePrice;
		this.underlying		= underlying;
		this.isCall			= isCall;
		this.strikeProduct	= null;
		this.regressionBasisFunctionsProvider = regressionBasisFunctionsProvider;
	}

	/**
	 * Creates the function underlying(exerciseDate) &ge; strikeProduct ? underlying : strikeProduct
	 *
	 * @param exerciseDate The exercise date of the option (given as a double).
	 * @param isCall If true, the function implements is underlying(exerciseDate) &ge; strikePrice ? underlying : strikePrice. Otherwise it is underlying(exerciseDate) &lt; strikePrice ? underlying : strikePrice.
	 * @param strikeProduct The strike (can be a general AbstractLIBORMonteCarloProduct).
	 * @param underlying The underlying.
	 * @param regressionBasisFunctionsProvider Used to determine the regresssion basis functions for the conditional expectation operator.
	 */
	public Option(double exerciseDate, boolean isCall,  AbstractLIBORMonteCarloProduct strikeProduct, AbstractLIBORMonteCarloProduct underlying, RegressionBasisFunctionsProvider	regressionBasisFunctionsProvider) {
		super();
		this.exerciseDate	= exerciseDate;
		this.strikePrice	= Double.NaN;
		this.strikeProduct	= strikeProduct;
		this.underlying		= underlying;
		this.isCall			= isCall;
		this.regressionBasisFunctionsProvider = regressionBasisFunctionsProvider;
	}

	/**
	 * Creates the function underlying(exerciseDate) &ge; strikeProduct ? underlying : strikeProduct
	 *
	 * @param exerciseDate The exercise date of the option (given as a double).
	 * @param isCall If true, the function implements is underlying(exerciseDate) &ge; strikePrice ? underlying : strikePrice. Otherwise it is underlying(exerciseDate) &lt; strikePrice ? underlying : strikePrice.
	 * @param strikeProduct The strike (can be a general AbstractLIBORMonteCarloProduct).
	 * @param underlying The underlying.
	 */
	public Option(double exerciseDate, boolean isCall,  AbstractLIBORMonteCarloProduct strikeProduct, AbstractLIBORMonteCarloProduct underlying) {
		this(exerciseDate, isCall, strikeProduct, underlying, null);
	}

	/**
	 * Creates the function underlying(exerciseDate) &ge; strikePrice ? underlying : strikePrice
	 *
	 * @param exerciseDate The exercise date of the option (given as a double).
	 * @param strikePrice The strike price.
	 * @param isCall If true, the function implements is underlying(exerciseDate) &ge; strikePrice ? underlying : strikePrice. Otherwise it is underlying(exerciseDate) &lt; strikePrice ? underlying : strikePrice.
	 * @param underlying The underlying.
	 */
	public Option(double exerciseDate, double strikePrice, boolean isCall, AbstractLIBORMonteCarloProduct underlying) {
		this(exerciseDate, strikePrice, isCall, underlying, null);
	}

	/**
	 * Creates the function underlying(exerciseDate) &ge; strikePrice ? underlying : strikePrice
	 *
	 * @param exerciseDate The exercise date of the option (given as a double).
	 * @param strikePrice The strike price.
	 * @param underlying The underlying.
	 */
	public Option(double exerciseDate, double strikePrice, AbstractLIBORMonteCarloProduct underlying) {
		this(exerciseDate, strikePrice, true, underlying);
	}

	/**
	 * Creates the function underlying(exerciseDate) &ge; 0 ? underlying : 0
	 *
	 * @param exerciseDate The exercise date of the option (given as a double).
	 * @param underlying The underlying.
	 */
	public Option(double exerciseDate, AbstractLIBORMonteCarloProduct underlying) {
		this(exerciseDate, 0.0, underlying);
	}

	@Override
	public String getCurrency() {
		return underlying.getCurrency();
	}

	@Override
	public Set<String> queryUnderlyings() {
		Set<String> underlyingNames = null;
		for(AbstractLIBORMonteCarloProduct product : new AbstractLIBORMonteCarloProduct[] {underlying, strikeProduct}) {
			if(product instanceof AbstractProductComponent) {
				Set<String> productUnderlyingNames = ((AbstractProductComponent)product).queryUnderlyings();
				if(productUnderlyingNames != null) {
					if(underlyingNames == null) {
						underlyingNames = productUnderlyingNames;
					} else {
						underlyingNames.addAll(productUnderlyingNames);
					}
				}
				else {
					throw new IllegalArgumentException("Underlying cannot be queried for underlyings.");
				}
			}
		}
		return underlyingNames;
	}

	/**
	 * This method returns the value random variable of the product within the specified model, evaluated at a given evalutationTime.
	 * Note: For a lattice this is often the value conditional to evalutationTime, for a Monte-Carlo simulation this is the (sum of) value discounted to evaluation time.
	 * Cashflows prior evaluationTime are not considered.
	 *
	 * @param evaluationTime The time on which this products value should be observed.
	 * @param model The model used to price the product.
	 * @return The random variable representing the value of the product discounted to evaluation time
	 * @throws net.finmath.exception.CalculationException Thrown if the valuation fails, specific cause may be available via the <code>cause()</code> method.
	 */
	@Override
	public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {

		final RandomVariableInterface one	= model.getRandomVariableForConstant(1.0);
		final RandomVariableInterface zero	= model.getRandomVariableForConstant(0.0);

		// TODO >=? -
		if(evaluationTime > exerciseDate) {
			return zero;
		}

		RandomVariableInterface values = underlying.getValue(exerciseDate, model);
		RandomVariableInterface strike;
		if(strikeProduct != null) {
			strike = strikeProduct.getValue(exerciseDate, model);
		} else {
			strike = model.getRandomVariableForConstant(strikePrice);
		}

		RandomVariableInterface exerciseTrigger = values.sub(strike).mult(isCall ? 1.0 : -1.0);

		if(exerciseTrigger.getFiltrationTime() > exerciseDate) {
			RandomVariableInterface filterNaN = exerciseTrigger.isNaN().sub(1.0).mult(-1.0);
			RandomVariableInterface exerciseTriggerFiltered = exerciseTrigger.mult(filterNaN);

			/*
			 * Cut off two standard deviations from regression
			 */
			double exerciseTriggerMean		= exerciseTriggerFiltered.getAverage();
			double exerciseTriggerStdDev	= exerciseTriggerFiltered.getStandardDeviation();
			double exerciseTriggerFloor		= exerciseTriggerMean*(1.0-Math.signum(exerciseTriggerMean)*1E-5)-3.0*exerciseTriggerStdDev;
			double exerciseTriggerCap		= exerciseTriggerMean*(1.0+Math.signum(exerciseTriggerMean)*1E-5)+3.0*exerciseTriggerStdDev;
			RandomVariableInterface filter = exerciseTrigger
					.barrier(exerciseTrigger.sub(exerciseTriggerFloor), one, zero)
					.mult(exerciseTrigger.barrier(exerciseTrigger.sub(exerciseTriggerCap).mult(-1.0), one, zero));
			filter = filter.mult(filterNaN);
			// Filter exerciseTrigger and regressionBasisFunctions
			exerciseTrigger = exerciseTrigger.mult(filter);

			RandomVariableInterface[] regressionBasisFunctions			= regressionBasisFunctionsProvider != null ? regressionBasisFunctionsProvider.getBasisFunctions(evaluationTime, model) : getBasisFunctions(exerciseDate, model);
			RandomVariableInterface[] filteredRegressionBasisFunctions	= new RandomVariableInterface[regressionBasisFunctions.length];
			for(int i=0; i<regressionBasisFunctions.length; i++) {
				filteredRegressionBasisFunctions[i] = regressionBasisFunctions[i].mult(filter);
			}

			// Remove foresight through conditional expectation
			ConditionalExpectationEstimatorInterface conditionalExpectationOperator = new MonteCarloConditionalExpectationRegression(filteredRegressionBasisFunctions, regressionBasisFunctions);

			// Calculate cond. expectation. Note that no discounting (numeraire division) is required!
			exerciseTrigger         = exerciseTrigger.getConditionalExpectation(conditionalExpectationOperator);
		}

		// Apply exercise criteria
		if(strikeProduct != null) {
			values = values.barrier(exerciseTrigger, values, strikeProduct.getValue(exerciseDate, model));
		} else {
			values = values.barrier(exerciseTrigger, values, strikePrice);
		}

		// Discount to evaluation time
		if(evaluationTime != exerciseDate) {
			RandomVariableInterface	numeraireAtEval			= model.getNumeraire(evaluationTime);
			RandomVariableInterface	numeraire				= model.getNumeraire(exerciseDate);
			values = values.div(numeraire).mult(numeraireAtEval);
		}

		// Return values
		return values;
	}

	@Override
	public RandomVariableInterface[] getBasisFunctions(double evaluationTime, MonteCarloSimulationInterface model) throws CalculationException {
		if(model instanceof LIBORModelMonteCarloSimulationInterface) {
			return getBasisFunctions(evaluationTime, (LIBORModelMonteCarloSimulationInterface)model);
		}
		else {
			throw new IllegalArgumentException("getBasisFunctions requires an model of type LIBORModelMonteCarloSimulationInterface.");
		}
	}

	/**
	 * Return the regression basis functions.
	 *
	 * @param exerciseDate The date w.r.t. which the basis functions should be measurable.
	 * @param model The model.
	 * @return Array of random variables.
	 * @throws net.finmath.exception.CalculationException Thrown if the valuation fails, specific cause may be available via the <code>cause()</code> method.
	 */
	public RandomVariableInterface[] getBasisFunctions(double exerciseDate, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {

		ArrayList<RandomVariableInterface> basisFunctions = new ArrayList<RandomVariableInterface>();

		RandomVariableInterface basisFunction;

		// Constant
		basisFunction = model.getRandomVariableForConstant(1.0);
		basisFunctions.add(basisFunction);

		// LIBORs
		int liborPeriodIndex, liborPeriodIndexEnd;
		RandomVariableInterface rate;

		// 1 Period
		basisFunction = model.getRandomVariableForConstant(1.0);
		liborPeriodIndex = model.getLiborPeriodIndex(exerciseDate);
		if(liborPeriodIndex < 0) {
			liborPeriodIndex = -liborPeriodIndex-1;
		}
		liborPeriodIndexEnd = liborPeriodIndex+1;
		double periodLength1 = model.getLiborPeriod(liborPeriodIndexEnd) - model.getLiborPeriod(liborPeriodIndex);

		rate = model.getLIBOR(exerciseDate, model.getLiborPeriod(liborPeriodIndex), model.getLiborPeriod(liborPeriodIndexEnd));
		basisFunction = basisFunction.discount(rate, periodLength1);
		basisFunctions.add(basisFunction);//.div(Math.sqrt(basisFunction.mult(basisFunction).getAverage())));

		basisFunction = basisFunction.discount(rate, periodLength1);
		basisFunctions.add(basisFunction);//.div(Math.sqrt(basisFunction.mult(basisFunction).getAverage())));

		// n/2 Period
		basisFunction = model.getRandomVariableForConstant(1.0);
		liborPeriodIndex = model.getLiborPeriodIndex(exerciseDate);
		if(liborPeriodIndex < 0) {
			liborPeriodIndex = -liborPeriodIndex-1;
		}
		liborPeriodIndexEnd = (liborPeriodIndex + model.getNumberOfLibors())/2;

		double periodLength2 = model.getLiborPeriod(liborPeriodIndexEnd) - model.getLiborPeriod(liborPeriodIndex);

		if(periodLength2 != periodLength1) {
			rate = model.getLIBOR(exerciseDate, model.getLiborPeriod(liborPeriodIndex), model.getLiborPeriod(liborPeriodIndexEnd));
			basisFunction = basisFunction.discount(rate, periodLength2);
			basisFunctions.add(basisFunction);//.div(Math.sqrt(basisFunction.mult(basisFunction).getAverage())));

			basisFunction = basisFunction.discount(rate, periodLength2);
			//			basisFunctions.add(basisFunction);//.div(Math.sqrt(basisFunction.mult(basisFunction).getAverage())));

			basisFunction = basisFunction.discount(rate, periodLength2);
			//			basisFunctions.add(basisFunction);//.div(Math.sqrt(basisFunction.mult(basisFunction).getAverage())));
		}


		// n Period
		basisFunction = model.getRandomVariableForConstant(1.0);
		liborPeriodIndex = model.getLiborPeriodIndex(exerciseDate);
		if(liborPeriodIndex < 0) {
			liborPeriodIndex = -liborPeriodIndex-1;
		}
		liborPeriodIndexEnd = model.getNumberOfLibors();
		double periodLength3 = model.getLiborPeriod(liborPeriodIndexEnd) - model.getLiborPeriod(liborPeriodIndex);

		if(periodLength3 != periodLength1 && periodLength3 != periodLength2) {
			rate = model.getLIBOR(exerciseDate, model.getLiborPeriod(liborPeriodIndex), model.getLiborPeriod(liborPeriodIndexEnd));
			basisFunction = basisFunction.discount(rate, periodLength3);
			basisFunctions.add(basisFunction);//.div(Math.sqrt(basisFunction.mult(basisFunction).getAverage())));

			basisFunction = basisFunction.discount(rate, periodLength3);
			//			basisFunctions.add(basisFunction);//.div(Math.sqrt(basisFunction.mult(basisFunction).getAverage())));
		}

		return basisFunctions.toArray(new RandomVariableInterface[0]);
	}

	@Override
	public String toString() {
		return "Option [exerciseDate=" + exerciseDate + ", strikePrice="
				+ strikePrice + ", underlying=" + underlying + ", isCall="
				+ isCall + ", toString()=" + super.toString() + "]";
	}
}
