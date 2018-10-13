/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 29.05.2015
 */

package net.finmath.optimizer;

import net.finmath.stochastic.RandomVariableInterface;

/**
 * @author Christian Fries
 *
 * @version 1.0
 */
public interface StochasticOptimizerInterface {

	/**
	 * The interface describing the objective function of a <code>StochasticOptimizerInterface</code>.
	 *
	 * @author Christian Fries
	 */
	interface ObjectiveFunction {
		void setValues(RandomVariableInterface[] parameters, RandomVariableInterface[] values) throws SolverException;
	}

	/**
	 * Get the best fit parameter vector.
	 *
	 * @return The best fit parameter.
	 */
	RandomVariableInterface[] getBestFitParameters();

	/**
	 * @return the the root mean square error achieved with the the best fit parameter
	 */
	double getRootMeanSquaredError();

	/**
	 * Get the number of iterations.
	 *
	 * @return The number of iterations required
	 */
	int getIterations();

	/**
	 * Runs the optimization.
	 *
	 * @throws SolverException Thrown if the valuation fails, specific cause may be available via the <code>cause()</code> method.
	 */
	void run() throws SolverException;
}
