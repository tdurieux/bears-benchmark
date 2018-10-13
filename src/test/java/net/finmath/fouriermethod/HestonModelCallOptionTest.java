/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 24.03.2014
 */

package net.finmath.fouriermethod;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.finmath.exception.CalculationException;
import net.finmath.fouriermethod.models.BlackScholesModel;
import net.finmath.fouriermethod.models.HestonModel;
import net.finmath.fouriermethod.models.ProcessCharacteristicFunctionInterface;
import net.finmath.fouriermethod.products.AbstractProductFourierTransform;
import net.finmath.fouriermethod.products.DigitalOption;
import net.finmath.fouriermethod.products.EuropeanOption;

/**
 * Test class for the valuation of a call option under Heston
 * model using Fourier transforms / characteristic functions.
 *
 * @author Christian Fries
 */
@RunWith(Parameterized.class)
public class HestonModelCallOptionTest {

	/**
	 * The parameters for this test
	 *
	 * @return Array of parameters.
	 */
	@Parameters(name="{0}")
	public static Collection<Object[]> generateData()
	{
		return Arrays.asList(new Object[][] {
			{ new EuropeanOption(maturity, strike) },
			{ new DigitalOption(maturity, strike) },
		});
	}

	private final AbstractProductFourierTransform product;

	// Model properties
	private final double	initialValue   = 1.0;
	private final double	riskFreeRate   = 0.05;
	private final double	volatility     = 0.30;

	private final double theta = volatility*volatility;
	private final double kappa = 0.1;
	private double xi = 0.50;
	private final double rho = 0.1;

	private static final double maturity	= 1.0;
	private static final double strike		= 0.95;

	public HestonModelCallOptionTest(AbstractProductFourierTransform product) {
		super();
		this.product = product;
	}

	@Test
	public void test() throws CalculationException {

		// For xi close to zero we should recover the Black Scholes model
		double xi = 0.000001;

		ProcessCharacteristicFunctionInterface modelBS = new BlackScholesModel(initialValue, riskFreeRate, volatility);
		ProcessCharacteristicFunctionInterface modelHS = new HestonModel(initialValue, riskFreeRate, volatility, theta, kappa, xi, rho);

		long startMillis	= System.currentTimeMillis();

		double valueBS			= product.getValue(modelBS);
		double valueHS			= product.getValue(modelHS);

		long endMillis		= System.currentTimeMillis();

		double error			= valueHS-valueBS;

		System.out.println(product.getClass().getSimpleName() + "\t" + "Result: " + valueHS + ". \tError: " + error + "." + ". \tCalculation time: " + ((endMillis-startMillis)/1000.0) + " sec.");
		Assert.assertEquals("Value", valueBS, valueHS, 1E-7);
	}
}
