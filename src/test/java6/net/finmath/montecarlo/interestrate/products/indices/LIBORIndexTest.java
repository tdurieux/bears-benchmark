/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 07.12.2014
 */

package net.finmath.montecarlo.interestrate.products.indices;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import net.finmath.exception.CalculationException;
import net.finmath.marketdata.model.AnalyticModel;
import net.finmath.marketdata.model.AnalyticModelInterface;
import net.finmath.marketdata.model.curves.CurveInterface;
import net.finmath.marketdata.model.curves.DiscountCurve;
import net.finmath.marketdata.model.curves.DiscountCurveFromForwardCurve;
import net.finmath.marketdata.model.curves.DiscountCurveInterface;
import net.finmath.marketdata.model.curves.DiscountCurveNelsonSiegelSvensson;
import net.finmath.marketdata.model.curves.ForwardCurve;
import net.finmath.marketdata.model.curves.ForwardCurveFromDiscountCurve;
import net.finmath.marketdata.model.curves.ForwardCurveInterface;
import net.finmath.marketdata.model.curves.ForwardCurveNelsonSiegelSvensson;
import net.finmath.montecarlo.interestrate.LIBORMarketModel;
import net.finmath.montecarlo.interestrate.LIBORMarketModel.Measure;
import net.finmath.montecarlo.interestrate.LIBORMarketModelInterface;
import net.finmath.montecarlo.interestrate.LIBORModelMonteCarloSimulation;
import net.finmath.montecarlo.interestrate.LIBORModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.interestrate.modelplugins.LIBORCorrelationModelExponentialDecay;
import net.finmath.montecarlo.interestrate.modelplugins.LIBORCovarianceModelFromVolatilityAndCorrelation;
import net.finmath.montecarlo.interestrate.modelplugins.LIBORVolatilityModelFromGivenMatrix;
import net.finmath.montecarlo.interestrate.products.components.AbstractProductComponent;
import net.finmath.montecarlo.interestrate.products.components.Notional;
import net.finmath.montecarlo.interestrate.products.components.Period;
import net.finmath.montecarlo.interestrate.products.components.ProductCollection;
import net.finmath.montecarlo.process.ProcessEulerScheme;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationInterface;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarExcludingTARGETHolidays;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarInterface;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarInterface.DateRollConvention;
import net.finmath.time.daycount.DayCountConventionInterface;

/**
 * @author Christian Fries
 */
@RunWith(Parameterized.class)
public class LIBORIndexTest {

	public enum CurveSetup {
		NSS,				// Uses NSS curve
		DISCRETE_FORWARDCURVE,			// Uses a forward curve  with interpolation points and a discout curve which is derived from a discount curve.
		DISCRETE_DISCOUNTCURVE			// Uses a discount curve  with interpolation points and a forward which is derived from a discount curve.
	}

	/**
	 * The parameters for this test, that is an array consisting of
	 * { numberOfPaths, setup, isVolatility }.
	 *
	 * @return Array of parameters.
	 */
	@Parameters(name="Curve={1}-Paths={0}-IsVolatility={2}")
	public static Collection<Object[]> generateData()
	{
		List<Integer> numberOfPathsList = Arrays.asList(1000, 2000, 4000, 8000, 10000, 20000, 40000, 80000, 100000);
		List<CurveSetup> curveSetups = Arrays.asList(CurveSetup.NSS, CurveSetup.DISCRETE_FORWARDCURVE, CurveSetup.DISCRETE_DISCOUNTCURVE);
		List<Double> volatilitySclalings = Arrays.asList(1.0, 0.0);

		ArrayList<Object[]> parameters = new ArrayList<Object[]>();

		for(Integer numberOfPaths : numberOfPathsList) {
			for(CurveSetup curveSetup : curveSetups) {
				for(Double volatilitySclaling : volatilitySclalings) {
					parameters.add(new Object[] { numberOfPaths, curveSetup, volatilitySclaling });
				}
			}
		}
		return parameters;
	}

	private final int numberOfFactors = 5;
	private final double correlationDecayParam = 0.05;

	private double[] periodStarts	= { 2.00, 2.00, 2.00, 2.50, 2.50, 2.50, 2.00, 2.00, 2.05, 2.10, 2.20, 2.25, 4.00 };
	private double[] periodEnds		= { 2.50, 2.25, 3.00, 3.00, 3.25, 3.50, 4.00, 5.00, 2.50, 2.50, 2.50, 2.50, 5.00 };
	private double[] tolerance		= { 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4, 1E-4 };		// Tolerance at 100.000 path

	private LIBORModelMonteCarloSimulationInterface liborMarketModel;
	private Double volatilityScaling;
	private CurveSetup curveSetup;

	public LIBORIndexTest(Integer numberOfPaths, CurveSetup curveSetup, Double volatilityScaling) throws CalculationException {
		super();
		this.curveSetup = curveSetup;
		this.volatilityScaling = volatilityScaling;

		// Create a LIBOR market model
		liborMarketModel = createLIBORMarketModel(curveSetup, Measure.SPOT, numberOfPaths, volatilityScaling, numberOfFactors, correlationDecayParam);

		System.out.println("\n Checking model with curve " + curveSetup.name() + " and number of path " + numberOfPaths + ".");
	}

	@Test
	public void testSinglePeriods() throws CalculationException {
		System.out.println("Single Period forward startig floater.");

		NumberFormat formatDec2 = new DecimalFormat("0.00");
		NumberFormat formatDec6 = new DecimalFormat("0.000000");

		for(int iTestCase = 0; iTestCase<periodStarts.length; iTestCase++) {
			double periodStart	= periodStarts[iTestCase];
			double periodEnd	= periodEnds[iTestCase];
			double periodLength	= periodEnd-periodStart;

			AbstractIndex index = new LIBORIndex(0.0, periodLength);
			Period period = new Period(periodStart, periodEnd, periodStart, periodEnd, new Notional(1.0), index, periodLength, true, true, false);
			double value = period.getValue(liborMarketModel);

			double toleranceThisTest = tolerance[iTestCase] * volatilityScaling/Math.sqrt((liborMarketModel.getNumberOfPaths())/100000.0) + 1E-12;

			// @TODO Add correct benchmark for case CurveSetup.DISCRETE_FORWARDCURVE
			// The setup DISCRETE_FORWARDCURVE has a different behaviour for interpolated forwards, so we will see deviations on unaligned periods:
			if(curveSetup == CurveSetup.DISCRETE_FORWARDCURVE) {
				toleranceThisTest += 5.0/100.0/100.0 * (periodEnd-periodStart);
			}

			System.out.println(
					formatDec2.format(periodStart) + "\t" + formatDec2.format(periodEnd) + "\t" +
							formatDec6.format(value) + "\t < " +
							formatDec6.format(toleranceThisTest));
			Assert.assertTrue(Math.abs(value) < toleranceThisTest);
		}
		System.out.println();
	}

	@Test
	public void testMultiPeriodFloater() throws CalculationException {
		System.out.println("Multi Period forward startig floater.");

		double tolerance = 5E-4;
		ArrayList<AbstractProductComponent> periods = new ArrayList<AbstractProductComponent>();
		for(int iPeriod = 0; iPeriod<10; iPeriod++) {
			double periodStart	= 2.0 + 0.5 * iPeriod;
			double periodEnd	= 2.0 + 0.5 * (iPeriod+1);
			double periodLength	= periodEnd-periodStart;

			AbstractIndex index = new LIBORIndex(0.0, periodLength);
			Period period = new Period(periodStart, periodEnd, periodStart, periodEnd, new Notional(1.0), index, periodLength, true, true, false);
			periods.add(period);
		}
		AbstractProductComponent floater = new ProductCollection(periods);
		double value = floater.getValue(liborMarketModel);

		double toleranceThisTest = tolerance * volatilityScaling/Math.sqrt((liborMarketModel.getNumberOfPaths())/100000.0) + 1E-12;

		NumberFormat formatDec6 = new DecimalFormat("0.000000");
		System.out.println("Multi period floater: " +
				formatDec6.format(value) + "\t< " + formatDec6.format(toleranceThisTest));

		Assert.assertEquals("Deviation", 0.0, value, toleranceThisTest);
		System.out.println();
	}

	@Test
	public void testUnalignedPeriods() throws CalculationException {
		System.out.println("Unaligned periods floating rate payment.");

		NumberFormat formatDec2 = new DecimalFormat("0.00");
		NumberFormat formatDec6 = new DecimalFormat("0.000000");

		TimeDiscretizationInterface liborPeriodDiscretization = liborMarketModel.getLiborPeriodDiscretization();

		for(int iPeriodStart=liborPeriodDiscretization.getNumberOfTimeSteps()-2; iPeriodStart < liborPeriodDiscretization.getNumberOfTimeSteps()-1; iPeriodStart++) {
			double periodStart	= liborPeriodDiscretization.getTime(3);
			double periodEnd	= liborPeriodDiscretization.getTime(iPeriodStart+1);

			// Shift period by half libor period
			periodStart	+= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;
			periodEnd	+= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;

			double periodLength	= periodEnd-periodStart;

			AbstractIndex index = new LIBORIndex(0.0, periodLength);
			Period period = new Period(periodStart, periodEnd, periodStart, periodEnd, new Notional(1.0), index, periodLength, true, true, false);
			double value = period.getValue(liborMarketModel);

			final double oneBasisPoint = 1.0 / 100.0 / 100.0;
			double toleranceThisTest = oneBasisPoint * volatilityScaling/Math.sqrt((liborMarketModel.getNumberOfPaths())/100000.0) + 1E-12;

			// The setup DISCRETE_FORWARDCURVE has a different behaviour for interpolated forwards, so we will see deviations on unaligned periods:
			if(curveSetup == CurveSetup.DISCRETE_FORWARDCURVE) {
				toleranceThisTest += 1.0/100.0/100.0 * (periodEnd-periodStart);
			}

			System.out.println(
					formatDec2.format(periodStart) + "\t" + formatDec2.format(periodEnd) + "\t" +
							formatDec6.format(value) + "\t< " + formatDec6.format(toleranceThisTest) );

			Assert.assertEquals(0.0, value / periodLength, toleranceThisTest);
		}
		System.out.println();
	}

	@Test
	public void testAligedPeriod() throws CalculationException {
		System.out.println("Aligned periods floating rate payment.");

		NumberFormat formatDec2 = new DecimalFormat("0.00");
		NumberFormat formatDec6 = new DecimalFormat("0.000000");
		NumberFormat formatSci2 = new DecimalFormat("0.00E00");

		TimeDiscretizationInterface liborPeriodDiscretization = liborMarketModel.getLiborPeriodDiscretization();

		for(int iPeriodStart=liborPeriodDiscretization.getNumberOfTimeSteps()-20; iPeriodStart < liborPeriodDiscretization.getNumberOfTimeSteps()-1; iPeriodStart++) {
			double periodStart	= liborPeriodDiscretization.getTime(iPeriodStart);
			double periodEnd	= liborPeriodDiscretization.getTime(iPeriodStart+1);

			double periodLength	= periodEnd-periodStart;

			AbstractIndex index = new LIBORIndex(0.0, periodLength);
			Period period = new Period(periodStart, periodEnd, periodStart, periodEnd, new Notional(1.0), index, periodLength, true, false, false);
			double value = period.getValue(liborMarketModel);
			double valueAnalytic = liborMarketModel.getModel().getForwardRateCurve().getForward(liborMarketModel.getModel().getAnalyticModel(), periodStart, periodEnd-periodStart) * liborMarketModel.getModel().getDiscountCurve().getDiscountFactor(periodEnd) * periodLength;

			final double oneBasisPoint = 1.0 / 100.0 / 100.0;
			double toleranceThisTest = oneBasisPoint * volatilityScaling/Math.sqrt((liborMarketModel.getNumberOfPaths())/100000.0) + 1E-12;

			System.out.println(
					formatDec2.format(periodStart) + "\t" + formatDec2.format(periodEnd) + "\t" +
							formatDec6.format(value-valueAnalytic) + "\t< " + formatDec6.format(toleranceThisTest) );

			Assert.assertEquals(valueAnalytic, value, toleranceThisTest);
		}
		System.out.println();
	}

	@Test
	public void testUnalignedPeriodsOnStartAndEnd() throws CalculationException {
		System.out.println("Unaligned start and end periods floating rate payment.");

		NumberFormat formatDec2 = new DecimalFormat("0.00");
		NumberFormat formatDec6 = new DecimalFormat("0.000000");
		NumberFormat formatSci2 = new DecimalFormat("0.00E00");

		TimeDiscretizationInterface liborPeriodDiscretization = liborMarketModel.getLiborPeriodDiscretization();

		for(int iPeriodStart=liborPeriodDiscretization.getNumberOfTimeSteps()-20; iPeriodStart < liborPeriodDiscretization.getNumberOfTimeSteps()-1; iPeriodStart++) {
			double periodStart	= liborPeriodDiscretization.getTime(iPeriodStart);
			double periodEnd	= liborPeriodDiscretization.getTime(iPeriodStart+1);

			// Shift period start by a fractional libor period
			periodStart	+= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;
			periodEnd	+= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;

			double periodLength	= periodEnd-periodStart;

			AbstractIndex index = new LIBORIndex(0.0, periodLength);
			Period period = new Period(periodStart, periodEnd, periodStart, periodEnd, new Notional(1.0), index, periodLength, true, false, false);
			double value = period.getValue(liborMarketModel);
			double valueAnalytic = liborMarketModel.getModel().getForwardRateCurve().getForward(liborMarketModel.getModel().getAnalyticModel(), periodStart, periodEnd-periodStart) * liborMarketModel.getModel().getDiscountCurve().getDiscountFactor(periodEnd) * periodLength;

			final double oneBasisPoint = 1.0 / 100.0 / 100.0;
			double toleranceThisTest = oneBasisPoint * volatilityScaling/Math.sqrt((liborMarketModel.getNumberOfPaths())/100000.0) + 1E-12;

			// The setup DISCRETE_FORWARDCURVE has a different behaviour for interpolated forwards, so we will see deviations on unaligned periods:
			if(curveSetup == CurveSetup.DISCRETE_FORWARDCURVE) {
				toleranceThisTest += 1.0/100.0/100.0;
			}

			System.out.println(
					formatDec2.format(periodStart) + "\t" + formatDec2.format(periodEnd) + "\t" +
							formatDec6.format(value-valueAnalytic) + "\t< " + formatDec6.format(toleranceThisTest) );

			Assert.assertEquals(valueAnalytic, value, toleranceThisTest);
		}
		System.out.println();
	}

	@Test
	public void testUnalignedPeriodsOnStart() throws CalculationException {
		System.out.println("Unaligned start floating rate payment.");

		NumberFormat formatDec2 = new DecimalFormat("0.00");
		NumberFormat formatDec6 = new DecimalFormat("0.000000");
		NumberFormat formatSci2 = new DecimalFormat("0.00E00");

		TimeDiscretizationInterface liborPeriodDiscretization = liborMarketModel.getLiborPeriodDiscretization();

		for(int iPeriodStart=liborPeriodDiscretization.getNumberOfTimeSteps()-20; iPeriodStart < liborPeriodDiscretization.getNumberOfTimeSteps()-1; iPeriodStart++) {
			double periodStart	= liborPeriodDiscretization.getTime(iPeriodStart);
			double periodEnd	= liborPeriodDiscretization.getTime(iPeriodStart+1);

			// Shift period start by a fractional libor period
			periodStart	+= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;
			//			periodEnd	+= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;

			double periodLength	= periodEnd-periodStart;

			AbstractIndex index = new LIBORIndex(0.0, periodLength);
			Period period = new Period(periodStart, periodEnd, periodStart, periodEnd, new Notional(1.0), index, periodLength, true, false, false);
			double value = period.getValue(liborMarketModel);
			double valueAnalytic = liborMarketModel.getModel().getForwardRateCurve().getForward(liborMarketModel.getModel().getAnalyticModel(), periodStart, periodEnd-periodStart) * liborMarketModel.getModel().getDiscountCurve().getDiscountFactor(periodEnd) * periodLength;

			final double oneBasisPoint = 1.0 / 100.0 / 100.0;
			double toleranceThisTest = oneBasisPoint * volatilityScaling/Math.sqrt((liborMarketModel.getNumberOfPaths())/100000.0) + 1E-12;

			System.out.println(
					formatDec2.format(periodStart) + "\t" + formatDec2.format(periodEnd) + "\t" +
							formatDec6.format(value-valueAnalytic) + "\t< " + formatDec6.format(toleranceThisTest) );

			Assert.assertEquals(valueAnalytic, value, toleranceThisTest);

		}
		System.out.println();
	}

	@Test
	public void testUnalignedPeriodsOnEnd() throws CalculationException {
		System.out.println("Unaligned end floating rate payment.");

		NumberFormat formatDec2 = new DecimalFormat("0.00");
		NumberFormat formatDec6 = new DecimalFormat("0.000000");
		NumberFormat formatSci2 = new DecimalFormat("0.00E00");

		TimeDiscretizationInterface liborPeriodDiscretization = liborMarketModel.getLiborPeriodDiscretization();

		for(int iPeriodStart=liborPeriodDiscretization.getNumberOfTimeSteps()-20; iPeriodStart < liborPeriodDiscretization.getNumberOfTimeSteps()-1; iPeriodStart++) {
			double periodStart	= liborPeriodDiscretization.getTime(iPeriodStart);
			double periodEnd	= liborPeriodDiscretization.getTime(iPeriodStart+1);

			// Shift period end by a fractional libor period
			//			periodStart	+= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;
			periodEnd	-= (liborPeriodDiscretization.getTime(4)-liborPeriodDiscretization.getTime(3))/3;

			double periodLength	= periodEnd-periodStart;

			AbstractIndex index = new LIBORIndex(0.0, periodLength);
			Period period = new Period(periodStart, periodEnd, periodStart, periodEnd, new Notional(1.0), index, periodLength, true, false, false);
			double value = period.getValue(liborMarketModel);
			double valueAnalytic = liborMarketModel.getModel().getForwardRateCurve().getForward(liborMarketModel.getModel().getAnalyticModel(), periodStart, periodEnd-periodStart) * liborMarketModel.getModel().getDiscountCurve().getDiscountFactor(periodEnd) * periodLength;

			final double oneBasisPoint = 1.0 / 100.0 / 100.0;
			double toleranceThisTest = oneBasisPoint * volatilityScaling/Math.sqrt((liborMarketModel.getNumberOfPaths())/100000.0) + 1E-12;

			System.out.println(
					formatDec2.format(periodStart) + "\t" + formatDec2.format(periodEnd) + "\t" +
							formatDec6.format(value-valueAnalytic) + "\t< " + formatDec6.format(toleranceThisTest) );

			Assert.assertEquals(valueAnalytic, value, toleranceThisTest);
		}
		System.out.println();
	}

	public static LIBORModelMonteCarloSimulationInterface createLIBORMarketModel(
			CurveSetup curveSetup,
			Measure measure,
			int numberOfPaths,
			double volatilityScaling,
			int numberOfFactors, double correlationDecayParam) throws CalculationException {

		/*
		 * Create the libor tenor structure and the initial values
		 */
		double liborPeriodLength	= 0.5;
		double liborRateTimeHorzion	= 20.0;
		TimeDiscretization liborPeriodDiscretization = new TimeDiscretization(0.0, (int) (liborRateTimeHorzion / liborPeriodLength), liborPeriodLength);

		LocalDate referenceDate = LocalDate.of(2014, Month.SEPTEMBER, 16);

		double[] nssParameters = new double[] { 0.02 , -0.01, 0.16, -0.17, 4.5, 3.5 };

		/*
		 * Create forwardCurve and discountCurve. The two need to fit to each other for this test.
		 */
		DiscountCurveInterface discountCurve;
		ForwardCurveInterface forwardCurve;
		switch(curveSetup) {
		case NSS:
		{
			discountCurve = new DiscountCurveNelsonSiegelSvensson("EUR Curve", referenceDate, nssParameters, 1.0);

			String paymentOffsetCode = "6M";
			BusinessdayCalendarInterface paymentBusinessdayCalendar = new BusinessdayCalendarExcludingTARGETHolidays();
			BusinessdayCalendarInterface.DateRollConvention paymentDateRollConvention = DateRollConvention.MODIFIED_FOLLOWING;
			DayCountConventionInterface daycountConvention = null;//new DayCountConvention_ACT_360();

			forwardCurve = new ForwardCurveNelsonSiegelSvensson("EUR Curve", referenceDate, paymentOffsetCode, paymentBusinessdayCalendar, paymentDateRollConvention, daycountConvention, nssParameters, 1.0, 0.0);
			break;
		}
		case DISCRETE_FORWARDCURVE:
		{
			// Create the forward curve (initial value of the LIBOR market model)
			forwardCurve = ForwardCurve.createForwardCurveFromForwards(
					"forwardCurve"								/* name of the curve */,
					new double[] {0.5 , 1.0 , 2.0 , 5.0 , 40.0}	/* fixings of the forward */,
					new double[] {0.02, 0.025, 0.03, 0.035, 0.04}	/* forwards */,
					liborPeriodLength							/* tenor / period length */
					);

			// Create the discount curve
			discountCurve = new DiscountCurveFromForwardCurve(forwardCurve);
			break;
		}
		case DISCRETE_DISCOUNTCURVE:
		{
			// Create the forward curve (initial value of the LIBOR market model)
			discountCurve = DiscountCurve.createDiscountCurveFromZeroRates(
					"forwardCurve"								/* name of the curve */,
					new double[] {0.5 , 40.0}	/* fixings of the forward */,
					new double[] {0.01, 0.05}	/* zero rates */
					);

			// Create the discount curve
			forwardCurve = new ForwardCurveFromDiscountCurve(discountCurve.getName(), LocalDate.now(), "6M");
			break;
		}
		default:
			throw new IllegalArgumentException("Unknown curve setup: " + curveSetup.toString());
		}

		AnalyticModelInterface analyticModel = new AnalyticModel(new CurveInterface[] { discountCurve, forwardCurve });

		/*
		 * Create a simulation time discretization
		 */
		double lastTime	= 20.0;
		double dt		= 0.50;

		TimeDiscretization timeDiscretization = new TimeDiscretization(0.0, (int) (lastTime / dt), dt);

		/*
		 * Create a volatility structure v[i][j] = sigma_j(t_i)
		 */
		double[][] volatility = new double[timeDiscretization.getNumberOfTimeSteps()][liborPeriodDiscretization.getNumberOfTimeSteps()];
		for (int timeIndex = 0; timeIndex < volatility.length; timeIndex++) {
			for (int liborIndex = 0; liborIndex < volatility[timeIndex].length; liborIndex++) {
				// Create a very simple volatility model here
				double time = timeDiscretization.getTime(timeIndex);
				double maturity = liborPeriodDiscretization.getTime(liborIndex);
				double timeToMaturity = maturity - time;

				double instVolatility;
				if(timeToMaturity <= 0) {
					instVolatility = 0;				// This forward rate is already fixed, no volatility
				} else {
					instVolatility = volatilityScaling * (0.10 + 0.35 * Math.exp(-0.5 * timeToMaturity));
				}

				// Store
				volatility[timeIndex][liborIndex] = instVolatility;
			}
		}
		LIBORVolatilityModelFromGivenMatrix volatilityModel = new LIBORVolatilityModelFromGivenMatrix(timeDiscretization, liborPeriodDiscretization, volatility);

		/*
		 * Create a correlation model rho_{i,j} = exp(-a * abs(T_i-T_j))
		 */
		LIBORCorrelationModelExponentialDecay correlationModel = new LIBORCorrelationModelExponentialDecay(
				timeDiscretization, liborPeriodDiscretization, numberOfFactors,
				correlationDecayParam);


		/*
		 * Combine volatility model and correlation model to a covariance model
		 */
		LIBORCovarianceModelFromVolatilityAndCorrelation covarianceModel =
				new LIBORCovarianceModelFromVolatilityAndCorrelation(timeDiscretization,
						liborPeriodDiscretization, volatilityModel, correlationModel);

		// BlendedLocalVolatlityModel (future extension)
		//		AbstractLIBORCovarianceModel covarianceModel2 = new BlendedLocalVolatlityModel(covarianceModel, 0.00, false);

		// Set model properties
		Map<String, String> properties = new HashMap<String, String>();

		// Choose the simulation measure
		properties.put("measure", measure.name());

		// Choose log normal model
		properties.put("stateSpace", LIBORMarketModel.StateSpace.LOGNORMAL.name());

		// Empty array of calibration items - hence, model will use given covariance
		LIBORMarketModel.CalibrationItem[] calibrationItems = new LIBORMarketModel.CalibrationItem[0];

		/*
		 * Create corresponding LIBOR Market Model
		 */
		LIBORMarketModelInterface liborMarketModel = new LIBORMarketModel(liborPeriodDiscretization, analyticModel, forwardCurve, discountCurve, covarianceModel, calibrationItems, properties);
		//		LIBORMarketModel(liborPeriodDiscretization, forwardRateCurve, null, covarianceModel, calibrationItems, properties);

		ProcessEulerScheme process = new ProcessEulerScheme(
				new net.finmath.montecarlo.BrownianMotion(timeDiscretization,
						numberOfFactors, numberOfPaths, 8787 /* seed */));
		//		process.setScheme(ProcessEulerScheme.Scheme.PREDICTOR_CORRECTOR);

		return new LIBORModelMonteCarloSimulation(liborMarketModel, process);
	}
}
