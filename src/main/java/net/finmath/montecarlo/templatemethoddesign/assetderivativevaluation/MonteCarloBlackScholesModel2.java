/*
 * Created on 20.01.2004
 */
package net.finmath.montecarlo.templatemethoddesign.assetderivativevaluation;

import java.util.Map;

import net.finmath.montecarlo.RandomVariable;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.templatemethoddesign.LogNormalProcess;
import net.finmath.stochastic.RandomVariableInterface;
import net.finmath.time.TimeDiscretizationInterface;

/**
 * Monte Carlo simulation of a simple Black-Scholes model for a stock generated discrete process
 *
 * @author Christian Fries
 * @version 1.2
 */
public class MonteCarloBlackScholesModel2 extends LogNormalProcess implements AssetModelMonteCarloSimulationInterface {

	private double initialValue;
	private double riskFreeRate;		// Actually the same as the drift (which is not stochastic)
	private double volatility;

	private RandomVariableInterface[]	initialValueVector	= new RandomVariableInterface[1];
	private RandomVariableInterface	drift;
	private RandomVariableInterface	volatilityOnPaths;

	/**
	 * Create a Monte-Carlo simulation using given time discretization.
	 *
	 * @param timeDiscretization The time discretization
	 * @param numberOfPaths The number of Monte-Carlo path to be used
	 * @param initialValue Spot value
	 * @param riskFreeRate The risk free rate
	 * @param volatility The log volatility
	 */
	public MonteCarloBlackScholesModel2(
			TimeDiscretizationInterface timeDiscretization,
			int numberOfPaths,
			double initialValue,
			double riskFreeRate,
			double volatility) {
		super(timeDiscretization, 1 /* numberOfComponents */ , 1 /* numberOfFactors */, numberOfPaths, 3141 /* seed */);

		this.initialValue	= initialValue;
		this.riskFreeRate	= riskFreeRate;
		this.volatility		= volatility;

		/*
		 * The interface definition requires that we provide the drift and the volatility in terms of random variables.
		 * We construct the corresponding random variables here and will return (immutable) references to them.
		 */
		this.initialValueVector[0]	= new RandomVariable(0.0, initialValue);
		this.drift					= new RandomVariable(0.0, riskFreeRate);
		this.volatilityOnPaths		= new RandomVariable(0.0, volatility);
	}

	/**
	 * Create a Monte-Carlo simulation using given time discretization.
	 *
	 * @param timeDiscretization The time discretization.
	 * @param numberOfPaths The number of Monte-Carlo path to be used.
	 * @param initialValue Spot value.
	 * @param riskFreeRate The risk free rate.
	 * @param volatility The log volatility.
	 * @param seed The seed for the random number generator.
	 */
	public MonteCarloBlackScholesModel2(
			TimeDiscretizationInterface timeDiscretization,
			int numberOfPaths,
			double initialValue,
			double riskFreeRate,
			double volatility,
			int seed) {
		super(timeDiscretization, 1 /* numberOfComponents */ , 1 /* numberOfFactors */, numberOfPaths, seed);

		this.initialValue	= initialValue;
		this.riskFreeRate	= riskFreeRate;
		this.volatility		= volatility;

		/*
		 * The interface definition requires that we provide the drift and the volatility in terms of random variables.
		 * We construct the corresponding random variables here and will return (immutable) references to them.
		 */
		this.initialValueVector[0]	= new RandomVariable(0.0, initialValue);
		this.drift					= new RandomVariable(0.0, riskFreeRate);
		this.volatilityOnPaths		= new RandomVariable(0.0, volatility);
	}

	public int getNumberOfAssets() {
		return 1;
	}

	/**
	 * @return Returns the initialValue.
	 */
	@Override
	public RandomVariableInterface[] getInitialValue() {
		return initialValueVector;
	}

	@Override
	public RandomVariableInterface getDrift(int timeIndex, int componentIndex, RandomVariableInterface[] realizationAtTimeIndex, RandomVariableInterface[] realizationPredictor) {
		return drift;
	}

	@Override
	public RandomVariableInterface getFactorLoading(int timeIndex, int factor, int component, RandomVariableInterface[] realizationAtTimeIndex) {
		return volatilityOnPaths;
	}


	/* (non-Javadoc)
	 * @see net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationInterface#getAssetValue(int, int)
	 */
	public RandomVariableInterface getAssetValue(int timeIndex, int assetIndex) {
		return getProcessValue(timeIndex, assetIndex);
	}

	public RandomVariableInterface getAssetValue(double time, int assetIndex) {
		return getAssetValue(getTimeIndex(time), assetIndex);
	}

	public RandomVariableInterface getMonteCarloWeights(double time) {
		return getMonteCarloWeights(getTimeIndex(time));
	}

	public RandomVariableInterface getNumeraire(int timeIndex)
	{
		double time = getTime(timeIndex);

		return getNumeraire(time);
	}

	public RandomVariableInterface getNumeraire(double time)
	{
		double numeraireValue = Math.exp(riskFreeRate * time);

		return new RandomVariable(time, numeraireValue);
	}

	@Override
	public RandomVariableInterface getRandomVariableForConstant(double value) {
		return getBrownianMotion().getRandomVariableForConstant(value);
	}

	@Override
	public String toString() {
		return super.toString() + "\n" +
				"MonteCarloBlackScholesModelByInheritance:\n" +
				"  initial value...:" + initialValue + "\n" +
				"  risk free rate..:" + riskFreeRate + "\n" +
				"  volatiliy.......:" + volatility;
	}

	/**
	 * Returns the riskFreeRate.
	 *
	 * @return The riskFreeRate.
	 */
	public double getRiskFreeRate() {
		return riskFreeRate;
	}

	/**
	 * Returns the volatility.
	 *
	 * @return The volatility.
	 */
	public double getVolatility() {
		return volatility;
	}

	@Override
	public AssetModelMonteCarloSimulationInterface getCloneWithModifiedSeed(int seed) {
		return new MonteCarloBlackScholesModel2(this.getTimeDiscretization(), this.getNumberOfPaths(), this.getInitialValue()[0].get(0), this.getRiskFreeRate(), this.getVolatility(), seed);
	}

	@Override
	public AssetModelMonteCarloSimulationInterface getCloneWithModifiedData(Map<String, Object> dataModified) {
		throw new UnsupportedOperationException();
	}
}
