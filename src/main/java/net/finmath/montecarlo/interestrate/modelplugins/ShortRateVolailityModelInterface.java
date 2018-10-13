/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 24.01.2016
 */

package net.finmath.montecarlo.interestrate.modelplugins;

import net.finmath.time.TimeDiscretizationInterface;

/**
 * Interface for piecewise constant short rate volatility models with
 * piecewise constant instantaneous short rate volatility \( t \mapsto \sigma(t) \)
 * and piecewise constant short rate mean reversion speed \( t \mapsto a(t) \).
 *
 * @author Christian Fries
 * @version 1.0
 */
public interface ShortRateVolailityModelInterface {

	/**
	 * Returns the time discretization \( \{ t_{i} \} \) associated
	 * with the piecewise constant functions.
	 *
	 * @return the time discretization \( \{ t_{i} \} \)
	 */
	TimeDiscretizationInterface getTimeDiscretization();

	/**
	 * Returns the value of \( \sigma(t) \) for \( t_{i} \leq t &lt; t_{i+1} \).
	 *
	 * @param timeIndex The index \( i \).
	 * @return the value of \( \sigma(t) \) for \( t_{i} \leq t &lt; t_{i+1} \)
	 */
	double getVolatility(int timeIndex);

	/**
	 * Returns the value of \( a(t) \) for \( t_{i} \leq t &lt; t_{i+1} \).
	 *
	 * @param timeIndex The index \( i \).
	 * @return the value of \( a(t) \) for \( t_{i} \leq t &lt; t_{i+1} \)
	 */
	double getMeanReversion(int timeIndex);
}
