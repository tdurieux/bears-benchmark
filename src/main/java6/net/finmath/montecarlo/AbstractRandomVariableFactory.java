/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 09.02.2004
 */
package net.finmath.montecarlo;

import net.finmath.stochastic.RandomVariableInterface;

/**
 *
 * @author Christian Fries
 * @version 1.0
 */
public abstract class AbstractRandomVariableFactory {

	public RandomVariableInterface createRandomVariable(double value) {
		return createRandomVariable(-Double.MAX_VALUE, value);
	}

	public abstract RandomVariableInterface createRandomVariable(double time, double value);

	public abstract RandomVariableInterface createRandomVariable(double time, double[] values);
}
