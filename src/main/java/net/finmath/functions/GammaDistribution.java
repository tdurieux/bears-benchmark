/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 29.06.2014
 */

package net.finmath.functions;

/**
 * @author Christian Fries
 */
public class GammaDistribution {

	final org.apache.commons.math3.distribution.GammaDistribution gammaDistribution;

	public GammaDistribution(double shape, double scale) {
		super();
		this.gammaDistribution = new org.apache.commons.math3.distribution.GammaDistribution(shape, scale);
	}

	/**
	 * Return the inverse cumulative distribution function at x.
	 *
	 * @param x Argument
	 * @return Inverse cumulative distribution function at x.
	 */
	public double inverseCumulativeDistribution(double x) {
		return gammaDistribution.inverseCumulativeProbability(x);
	}
}
