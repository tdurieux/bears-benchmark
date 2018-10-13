/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 20.01.2004
 */
package net.finmath.montecarlo.assetderivativevaluation;

import java.util.Map;

import net.finmath.montecarlo.AbstractRandomVariableFactory;
import net.finmath.montecarlo.RandomVariableFactory;
import net.finmath.montecarlo.model.AbstractModel;
import net.finmath.stochastic.RandomVariableInterface;

/**
 * This class implements a Black Scholes Model, that is, it provides the drift and volatility specification
 * and performs the calculation of the numeraire (consistent with the dynamics, i.e. the drift).
 *
 * The model is
 * \[
 * 	dS = r S dt + \sigma S dW, \quad S(0) = S_{0},
 * \]
 * \[
 * 	dN = r N dt, \quad N(0) = N_{0},
 * \]
 *
 * The class provides the model of S to an <code>{@link net.finmath.montecarlo.process.AbstractProcessInterface}</code> via the specification of
 * \( f = exp \), \( \mu = r - \frac{1}{2} \sigma^2 \), \( \lambda_{1,1} = \sigma \), i.e.,
 * of the SDE
 * \[
 * 	dX = \mu dt + \lambda_{1,1} dW, \quad X(0) = \log(S_{0}),
 * \]
 * with \( S = f(X) \). See {@link net.finmath.montecarlo.process.AbstractProcessInterface} for the notation.
 *
 * @author Christian Fries
 * @see net.finmath.montecarlo.process.AbstractProcessInterface The interface for numerical schemes.
 * @see net.finmath.montecarlo.model.AbstractModelInterface The interface for models provinding parameters to numerical schemes.
 * @version 1.0
 */
public class BlackScholesModel extends AbstractModel {

	private final RandomVariableInterface initialValue;
	private final RandomVariableInterface riskFreeRate;
	private final RandomVariableInterface volatility;

	private final AbstractRandomVariableFactory randomVariableFactory;

	// Cache for arrays provided though AbstractModel
	private final RandomVariableInterface[]	initialState;
	private final RandomVariableInterface[]	drift;
	private final RandomVariableInterface[]	factorLoadings;

	/**
	 * Create a Black-Scholes specification implementing AbstractModel.
	 *
	 * @param initialValue Spot value.
	 * @param riskFreeRate The risk free rate.
	 * @param volatility The log volatility.
	 * @param randomVariableFactory The random variable factory used to create random variables from constants.
	 */
	public BlackScholesModel(
			RandomVariableInterface initialValue,
			RandomVariableInterface riskFreeRate,
			RandomVariableInterface volatility,
			AbstractRandomVariableFactory randomVariableFactory) {
		super();

		this.initialValue = initialValue;
		this.volatility = volatility;
		this.riskFreeRate	= riskFreeRate;
		this.randomVariableFactory = randomVariableFactory;

		// Cache
		this.initialState = new RandomVariableInterface[] { initialValue.log() };
		this.drift = new RandomVariableInterface[] { riskFreeRate.sub(volatility.squared().div(2)) };
		this.factorLoadings = new RandomVariableInterface[] { volatility };
	}

	/**
	 * Create a Monte-Carlo simulation using given time discretization.
	 *
	 * @param initialValue Spot value.
	 * @param riskFreeRate The risk free rate.
	 * @param volatility The log volatility.
	 * @param randomVariableFactory The random variable factory used to create random variables from constants.
	 */
	public BlackScholesModel(
			double initialValue,
			double riskFreeRate,
			double volatility,
			AbstractRandomVariableFactory randomVariableFactory) {
		this(randomVariableFactory.createRandomVariable(initialValue), randomVariableFactory.createRandomVariable(riskFreeRate), randomVariableFactory.createRandomVariable(volatility), randomVariableFactory);
	}

	/**
	 * Create a Black-Scholes model from given parameters.
	 *
	 * @param initialValue Spot value.
	 * @param riskFreeRate The risk free rate.
	 * @param volatility The log volatility.
	 */
	public BlackScholesModel(
			double initialValue,
			double riskFreeRate,
			double volatility) {
		this(initialValue, riskFreeRate, volatility, new RandomVariableFactory());
	}

	@Override
	public RandomVariableInterface[] getInitialState() {
		return initialState;
	}

	@Override
	public RandomVariableInterface[] getDrift(int timeIndex, RandomVariableInterface[] realizationAtTimeIndex, RandomVariableInterface[] realizationPredictor) {
		return drift;
	}

	@Override
	public RandomVariableInterface[] getFactorLoading(int timeIndex, int component, RandomVariableInterface[] realizationAtTimeIndex) {
		return factorLoadings;
	}

	@Override
	public RandomVariableInterface applyStateSpaceTransform(int componentIndex, RandomVariableInterface randomVariable) {
		return randomVariable.exp();
	}

	@Override
	public RandomVariableInterface applyStateSpaceTransformInverse(int componentIndex, RandomVariableInterface randomVariable) {
		return randomVariable.log();
	}

	@Override
	public RandomVariableInterface getNumeraire(double time) {
		return riskFreeRate.mult(time).exp();
	}

	@Override
	public int getNumberOfComponents() {
		return 1;
	}

	@Override
	public RandomVariableInterface getRandomVariableForConstant(double value) {
		return randomVariableFactory.createRandomVariable(value);
	}

	@Override
	public BlackScholesModel getCloneWithModifiedData(Map<String, Object> dataModified) {
		/*
		 * Determine the new model parameters from the provided parameter map.
		 */
		double	newInitialValue	= dataModified.get("initialValue") != null	? ((Number)dataModified.get("initialValue")).doubleValue() 	: initialValue.getAverage();
		double	newRiskFreeRate	= dataModified.get("riskFreeRate") != null	? ((Number)dataModified.get("riskFreeRate")).doubleValue()	: getRiskFreeRate().getAverage();
		double	newVolatility	= dataModified.get("volatility") != null	? ((Number)dataModified.get("volatility")).doubleValue()	: getVolatility().getAverage();

		return new BlackScholesModel(newInitialValue, newRiskFreeRate, newVolatility, randomVariableFactory);
	}

	@Override
	public String toString() {
		return super.toString() + "\n" +
				"BlackScholesModel:\n" +
				"  initial value...:" + getInitialValue() + "\n" +
				"  risk free rate..:" +  getRiskFreeRate() + "\n" +
				"  volatiliy.......:" + getVolatility();
	}

	/**
	 * Return the initial value of this model.
	 *
	 * @return the initial value of this model.
	 */
	@Override
	public RandomVariableInterface[] getInitialValue() {
		return new RandomVariableInterface[] { initialValue };
	}

	/**
	 * Returns the risk free rate parameter of this model.
	 *
	 * @return Returns the riskFreeRate.
	 */
	public RandomVariableInterface getRiskFreeRate() {
		return riskFreeRate;
	}

	/**
	 * Returns the volatility parameter of this model.
	 *
	 * @return Returns the volatility.
	 */
	public RandomVariableInterface getVolatility() {
		return factorLoadings[0];
	}
}
