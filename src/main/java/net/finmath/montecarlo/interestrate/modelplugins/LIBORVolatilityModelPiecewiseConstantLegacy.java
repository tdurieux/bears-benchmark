/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 08.08.2005
 */
package net.finmath.montecarlo.interestrate.modelplugins;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.finmath.montecarlo.AbstractRandomVariableFactory;
import net.finmath.montecarlo.RandomVariableFactory;
import net.finmath.stochastic.RandomVariableInterface;
import net.finmath.time.TimeDiscretizationInterface;

/**
 * @author Christian Fries
 */
public class LIBORVolatilityModelPiecewiseConstantLegacy extends LIBORVolatilityModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -1508940332674724348L;

	private final AbstractRandomVariableFactory	randomVariableFactory;

	private final TimeDiscretizationInterface	simulationTimeDiscretization;
	private final TimeDiscretizationInterface	timeToMaturityDiscretization;

	private Map<Integer, HashMap<Integer, Integer>> 	indexMap = new HashMap<>();
	private double[] volatility;
	private final	boolean		isCalibrateable;

	private final transient RandomVariableInterface[][] volatilityRandomVariables;


	public LIBORVolatilityModelPiecewiseConstantLegacy(AbstractRandomVariableFactory randomVariableFactory, TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization, TimeDiscretizationInterface simulationTimeDiscretization, TimeDiscretizationInterface timeToMaturityDiscretization, double[][] volatility, boolean isCalibrateable) {
		super(timeDiscretization, liborPeriodDiscretization);

		this.randomVariableFactory = randomVariableFactory;

		/*
		 * Build index map
		 */
		double maxMaturity = timeToMaturityDiscretization.getTime(timeToMaturityDiscretization.getNumberOfTimes()-1);
		int volatilityIndex = 0;
		for(int simulationTime=0; simulationTime<simulationTimeDiscretization.getNumberOfTimes(); simulationTime++) {
			HashMap<Integer, Integer> timeToMaturityIndexing = new HashMap<>();
			for(int timeToMaturity=0; timeToMaturity<timeToMaturityDiscretization.getNumberOfTimes(); timeToMaturity++) {
				if(simulationTimeDiscretization.getTime(simulationTime)+timeToMaturityDiscretization.getTime(timeToMaturity) > maxMaturity) {
					continue;
				}

				timeToMaturityIndexing.put(timeToMaturity,volatilityIndex++);
			}
			indexMap.put(simulationTime, timeToMaturityIndexing);
		}

		// Flatten parameter matrix
		this.volatility = new double[volatilityIndex];
		for(Integer simulationTime : indexMap.keySet()) {
			for(Integer timeToMaturity : indexMap.get(simulationTime).keySet()) {
				this.volatility[indexMap.get(simulationTime).get(timeToMaturity)] = volatility[simulationTime][timeToMaturity];
			}
		}

		this.simulationTimeDiscretization = simulationTimeDiscretization;
		this.timeToMaturityDiscretization = timeToMaturityDiscretization;
		this.isCalibrateable = isCalibrateable;
		this.volatilityRandomVariables = new RandomVariableInterface[timeDiscretization.getNumberOfTimeSteps()][liborPeriodDiscretization.getNumberOfTimeSteps()];
	}

	public LIBORVolatilityModelPiecewiseConstantLegacy(AbstractRandomVariableFactory randomVariableFactory, TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization, TimeDiscretizationInterface simulationTimeDiscretization, TimeDiscretizationInterface timeToMaturityDiscretization, double[] volatility, boolean isCalibrateable) {
		super(timeDiscretization, liborPeriodDiscretization);

		this.randomVariableFactory = randomVariableFactory;

		/*
		 * Build index map
		 */
		double maxMaturity = timeToMaturityDiscretization.getTime(timeToMaturityDiscretization.getNumberOfTimes()-1);
		int volatilityIndex = 0;
		for(int simulationTime=0; simulationTime<simulationTimeDiscretization.getNumberOfTimes(); simulationTime++) {
			HashMap<Integer, Integer> timeToMaturityIndexing = new HashMap<>();
			for(int timeToMaturity=0; timeToMaturity<timeToMaturityDiscretization.getNumberOfTimes(); timeToMaturity++) {
				if(simulationTimeDiscretization.getTime(simulationTime)+timeToMaturityDiscretization.getTime(timeToMaturity) > maxMaturity) {
					continue;
				}

				timeToMaturityIndexing.put(timeToMaturity,volatilityIndex++);
			}
			indexMap.put(simulationTime, timeToMaturityIndexing);
		}

		if(volatility.length == 1) {
			this.volatility = new double[volatilityIndex];
			Arrays.fill(this.volatility, volatility[0]);
		}
		else {
			this.volatility = volatility;
		}

		if(volatilityIndex != this.volatility.length) {
			throw new IllegalArgumentException("volatility.length should equal simulationTimeDiscretization.getNumberOfTimes()*timeToMaturityDiscretization.getNumberOfTimes().");
		}
		this.simulationTimeDiscretization = simulationTimeDiscretization;
		this.timeToMaturityDiscretization = timeToMaturityDiscretization;
		this.isCalibrateable = isCalibrateable;
		this.volatilityRandomVariables = new RandomVariableInterface[timeDiscretization.getNumberOfTimeSteps()][liborPeriodDiscretization.getNumberOfTimeSteps()];
	}

	public LIBORVolatilityModelPiecewiseConstantLegacy(TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization, TimeDiscretizationInterface simulationTimeDiscretization, TimeDiscretizationInterface timeToMaturityDiscretization, double[] volatility, boolean isCalibrateable) {
		this(new RandomVariableFactory(), timeDiscretization, liborPeriodDiscretization, simulationTimeDiscretization, timeToMaturityDiscretization, volatility, isCalibrateable);
	}

	public LIBORVolatilityModelPiecewiseConstantLegacy(TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization, TimeDiscretizationInterface simulationTimeDiscretization, TimeDiscretizationInterface timeToMaturityDiscretization, double volatility, boolean isCalibrateable) {
		this(timeDiscretization, liborPeriodDiscretization, simulationTimeDiscretization, timeToMaturityDiscretization, new double[] { volatility }, isCalibrateable);
	}

	public LIBORVolatilityModelPiecewiseConstantLegacy(TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization, TimeDiscretizationInterface simulationTimeDiscretization, TimeDiscretizationInterface timeToMaturityDiscretization, double[] volatility) {
		this(timeDiscretization, liborPeriodDiscretization, simulationTimeDiscretization, timeToMaturityDiscretization, volatility, true);
	}

	public LIBORVolatilityModelPiecewiseConstantLegacy(TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization, TimeDiscretizationInterface simulationTimeDiscretization, TimeDiscretizationInterface timeToMaturityDiscretization, double volatility) {
		this(timeDiscretization, liborPeriodDiscretization, simulationTimeDiscretization, timeToMaturityDiscretization, new double[] { volatility });
	}

	@Override
	public double[] getParameter() {
		if(isCalibrateable) {
			return volatility;
		} else {
			return null;
		}
	}

	@Override
	public LIBORVolatilityModel getCloneWithModifiedParameter(double[] parameter) {
		return new LIBORVolatilityModelPiecewiseConstantLegacy(
				randomVariableFactory,
				super.getTimeDiscretization(),
				super.getLiborPeriodDiscretization(),
				this.simulationTimeDiscretization,
				this.timeToMaturityDiscretization,
				parameter,
				this.isCalibrateable
				);
	}

	@Override
	public RandomVariableInterface getVolatility(int timeIndex, int liborIndex) {
		// Create a very simple volatility model here
		double time             = getTimeDiscretization().getTime(timeIndex);
		double maturity         = getLiborPeriodDiscretization().getTime(liborIndex);
		double timeToMaturity   = maturity-time;

		double volatilityInstanteaneous;
		if(timeToMaturity <= 0)
		{
			volatilityInstanteaneous = 0.0;   // This forward rate is already fixed, no volatility

			return randomVariableFactory.createRandomVariable(time, volatilityInstanteaneous);
		}
		else
		{
			int timeIndexSimulationTime = simulationTimeDiscretization.getTimeIndex(time);
			if(timeIndexSimulationTime < 0) {
				timeIndexSimulationTime = -timeIndexSimulationTime-1-1;
			}
			if(timeIndexSimulationTime < 0) {
				timeIndexSimulationTime = 0;
			}
			if(timeIndexSimulationTime >= simulationTimeDiscretization.getNumberOfTimes()) {
				timeIndexSimulationTime--;
			}

			int timeIndexTimeToMaturity = timeToMaturityDiscretization.getTimeIndex(timeToMaturity);
			if(timeIndexTimeToMaturity < 0) {
				timeIndexTimeToMaturity = -timeIndexTimeToMaturity-1-1;
			}
			if(timeIndexTimeToMaturity < 0) {
				timeIndexTimeToMaturity = 0;
			}
			if(timeIndexTimeToMaturity >= timeToMaturityDiscretization.getNumberOfTimes()) {
				timeIndexTimeToMaturity--;
			}

			RandomVariableInterface volatilityRandomVariable = volatilityRandomVariables[timeIndexSimulationTime][timeIndexTimeToMaturity];

			if(volatilityRandomVariable == null) {
				volatilityInstanteaneous = volatility[indexMap.get(timeIndexSimulationTime).get(timeIndexTimeToMaturity)];
				volatilityRandomVariable = randomVariableFactory.createRandomVariable(time, volatilityInstanteaneous);
				volatilityRandomVariables[timeIndexSimulationTime][timeIndexTimeToMaturity] = volatilityRandomVariable;
			}

			return volatilityRandomVariable;
		}
	}

	@Override
	public Object clone() {
		return new LIBORVolatilityModelPiecewiseConstantLegacy(
				randomVariableFactory,
				super.getTimeDiscretization(),
				super.getLiborPeriodDiscretization(),
				this.simulationTimeDiscretization,
				this.timeToMaturityDiscretization,
				this.volatility.clone(),
				this.isCalibrateable
				);
	}
}
