/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christianfries.com.
 *
 * Created on 29.05.2015
 */

package net.finmath.optimizer;

import net.finmath.optimizer.StochasticOptimizerInterface.ObjectiveFunction;
import net.finmath.stochastic.RandomVariableInterface;

/**
 * @author Christian Fries
 * @version 1.0
 */
public class StochasticOptimizerFactoryLevenbergMarquardtAD implements StochasticOptimizerFactoryInterface {

	private final int		maxIterations;
	private final double		errorTolerance;
	private final int		maxThreads;

	public StochasticOptimizerFactoryLevenbergMarquardtAD(int maxIterations, double errorTolerance, int maxThreads) {
		super();
		this.maxIterations = maxIterations;
		this.errorTolerance = errorTolerance;
		this.maxThreads = maxThreads;
	}

	public StochasticOptimizerFactoryLevenbergMarquardtAD(int maxIterations, int maxThreads) {
		this(maxIterations, 0.0, maxThreads);
	}

	@Override
	public StochasticOptimizerInterface getOptimizer(final ObjectiveFunction objectiveFunction, RandomVariableInterface[] initialParameters, RandomVariableInterface[] lowerBound, RandomVariableInterface[]  upperBound, RandomVariableInterface[] parameterSteps, RandomVariableInterface[] targetValues) {
		return
				new StochasticPathwiseLevenbergMarquardtAD(initialParameters, targetValues, null /* weights */, parameterSteps, maxIterations, null, null)
		{
			private static final long serialVersionUID = -7050719719557572792L;

			@Override
			public void setValues(RandomVariableInterface[] parameters, RandomVariableInterface[] values) throws SolverException {
				objectiveFunction.setValues(parameters, values);
			}
		};
	}
}
