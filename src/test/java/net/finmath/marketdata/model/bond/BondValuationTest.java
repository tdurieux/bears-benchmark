/*
 * (c) Copyright finmath.net, Germany. Contact: info@finmath.net.
 *
 * Created on 01.06.2018
 */
package net.finmath.marketdata.model.bond;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.junit.Assert;

import net.finmath.marketdata.calibration.ParameterObjectInterface;
import net.finmath.marketdata.calibration.Solver;
import net.finmath.marketdata.model.AnalyticModel;
import net.finmath.marketdata.model.AnalyticModelInterface;
import net.finmath.marketdata.model.curves.Curve;
import net.finmath.marketdata.model.curves.Curve.ExtrapolationMethod;
import net.finmath.marketdata.model.curves.Curve.InterpolationEntity;
import net.finmath.marketdata.model.curves.Curve.InterpolationMethod;
import net.finmath.marketdata.model.curves.CurveInterface;
import net.finmath.marketdata.model.curves.DiscountCurve;
import net.finmath.marketdata.products.AnalyticProductInterface;
import net.finmath.optimizer.SolverException;
import net.finmath.time.RegularSchedule;
import net.finmath.time.TimeDiscretization;

/**
 * This class makes some basic tests related to the setup, use and calibration of discount curves and forward curve.
 *
 * @author Christian Fries
 * @author Moritz Scherrmann
 */
public class BondValuationTest {

	static final double errorTolerance = 1E-14;

	final InterpolationMethod interpolationMethod;

	public BondValuationTest(InterpolationMethod interpolationMethod)
	{
		this.interpolationMethod = interpolationMethod;
	}

	/**
	 * The parameters for this test, that is an error consisting of
	 * { numberOfPaths, setup }.
	 *
	 * @return Array of parameters.
	 */

	public static Collection<Object[]> generateData()
	{
		return Arrays.asList(new Object[][] {
			{ InterpolationMethod.LINEAR },
			{ InterpolationMethod.CUBIC_SPLINE },
			{ InterpolationMethod.AKIMA },
			{ InterpolationMethod.AKIMA_CONTINUOUS },
			{ InterpolationMethod.HARMONIC_SPLINE },
			{ InterpolationMethod.HARMONIC_SPLINE_WITH_MONOTONIC_FILTERING },
		});
	}

	/**
	 * Run some test using discount curves and forward curves and the solver to create a calibrated model.
	 *
	 * @param args Arguments - not used.
	 * @throws SolverException Thrown if the solver cannot find a solution to the calibration problem.
	 */
	public static void main(String[] args) throws SolverException {

		BondValuationTest bondValuationTest = new BondValuationTest(InterpolationMethod.LINEAR);

		bondValuationTest.testImplementation();
	}


	public void testImplementation() throws SolverException {
		/*
		 * CREATING AND USING A DISCOUNT CURVE
		 */

		// Create a discount curve
		DiscountCurve			discountCurve					= DiscountCurve.createDiscountCurveFromDiscountFactors(
				"discountCurve"								/* name */,
				new double[] {0.0,  1.0,  2.0, 3.0,  4.0,  5.0}	/* maturities */,
				new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0}	/* discount factors */
				);


		// Create a survival probability curve which is constant 1 ( non defaultable)
		CurveInterface	survivalProbabilityCurve	= new Curve("survivalProbabilityCurve",
				LocalDate.now() ,
				InterpolationMethod.LINEAR,
				ExtrapolationMethod.CONSTANT,
				InterpolationEntity.VALUE,
				new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0},
				new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});

		// Create a basis factor curve which is constant 1 ( start value)
		Curve basisFactorCurve	= DiscountCurve.createDiscountCurveFromDiscountFactors(
				"basisFactorCurve",
				new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0},
				new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0});


		// Create a collection of objective functions (calibration products)
		Vector<AnalyticProductInterface> calibrationProducts1 = new Vector<>();
		calibrationProducts1.add(new Bond(new RegularSchedule(new TimeDiscretization(0.0, 1, 1.0)),"discountCurve",null,"survivalProbabilityCurve","basisFactorCurve", 0.1,0));
		calibrationProducts1.add(new Bond(new RegularSchedule(new TimeDiscretization(0.0, 2, 1.0)),"discountCurve",null,"survivalProbabilityCurve","basisFactorCurve", 0.1,0));
		calibrationProducts1.add(new Bond(new RegularSchedule(new TimeDiscretization(0.0, 3, 1.0)),"discountCurve",null,"survivalProbabilityCurve","basisFactorCurve", 0.1,0));
		calibrationProducts1.add(new Bond(new RegularSchedule(new TimeDiscretization(0.0, 4, 1.0)),"discountCurve",null,"survivalProbabilityCurve","basisFactorCurve", 0.1,0));
		calibrationProducts1.add(new Bond(new RegularSchedule(new TimeDiscretization(0.0, 5, 1.0)),"discountCurve",null,"survivalProbabilityCurve","basisFactorCurve", 0.1,0));

		// A model is a collection of curves (curves and products find other curves by looking up their name in the model)
		AnalyticModel model1 = new AnalyticModel(new CurveInterface[] { discountCurve , survivalProbabilityCurve, basisFactorCurve });

		for(int i=0;i<calibrationProducts1.size();i++){
			System.out.println("Implemented value at t=0 of bond with "+ (i+1) +" payments:"+" "+ calibrationProducts1.get(i).getValue(0, model1));
		}

		// Create a collection of curves to calibrate
		Set<ParameterObjectInterface> curvesToCalibrate1 = new HashSet<>();
		curvesToCalibrate1.add(basisFactorCurve);

		// Calibrate the curve
		ArrayList<Double> targetList = new ArrayList<>();
		targetList.add(1.04762);
		targetList.add(1.09297);
		targetList.add(1.136162);
		targetList.add(1.177298);
		targetList.add(1.21647);
		Solver solver1 = new Solver(model1, calibrationProducts1, targetList, 0.0, errorTolerance);
		AnalyticModelInterface calibratedModel1 = solver1.getCalibratedModel(curvesToCalibrate1);
		System.out.println("The solver required " + solver1.getIterations() + " iterations.");
		System.out.println("The best fit curve is:");
		System.out.println(calibratedModel1.getCurve("basisFactorCurve").toString());

		/*
		 * The model calibratedModel1 now contains a set af calibrated curves.
		 * The curves are clones. The model model1 still contains the original curve.
		 */

		// Calibration check
		System.out.println("Calibration check:");
		double evaluationTime = 0.0;
		double error = 0;
		for(int calibrationProductIndex = 0; calibrationProductIndex < calibrationProducts1.size(); calibrationProductIndex++) {
			AnalyticProductInterface	calibrationProduct		= calibrationProducts1.get(calibrationProductIndex);
			double						calibrationProductValue	= calibrationProduct.getValue(evaluationTime, calibratedModel1);
			System.out.println("Calibration product " + calibrationProductIndex + ":\t" + calibrationProductValue);

			error += (calibrationProductValue-targetList.get(calibrationProductIndex))*(calibrationProductValue-targetList.get(calibrationProductIndex));
		}
		error = Math.sqrt(error);
		Assert.assertTrue(error < errorTolerance);

		System.out.println("__________________________________________________________________________________________\n");
		RegularSchedule schedule=new RegularSchedule(new TimeDiscretization(0.0, 2, 1));
		Bond bond=new Bond(schedule,"discountCurve",null,"survivalProbabilityCurve","basisFactorCurve", 0.1,0);
		double bondPrice=0.9;
		double yield=bond.getYield(bondPrice, model1);
		System.out.println(schedule.getPayment(0));
		System.out.println(yield);
		System.out.println(bond.getValueWithGivenYield(0.0,yield, model1));

		double value=0.0;
		for(int i=0; i<10; i++) {
			value += i<6 ? 1:0;
		}

		System.out.println(value);




	}




}
