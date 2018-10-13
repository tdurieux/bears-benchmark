/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 09.02.2018
 */

package net.finmath.modelling.descriptor;

import java.time.LocalDate;

import net.finmath.marketdata.model.curves.DiscountCurveInterface;

/**
 * @author Christian Fries
 *
 * @version 1.0
 */
public class HestonModelDescriptor implements AssetModelDescriptor {

	private final LocalDate referenceDate;

	private final Double initialValue;

	private final DiscountCurveInterface discountCurveForForwardRate;
	private final DiscountCurveInterface discountCurveForDiscountRate;

	private final Double volatility;

	private final Double theta;
	private final Double kappa;
	private final Double xi;
	private final Double rho;

	public HestonModelDescriptor(LocalDate referenceDate, Double initialValue,
			DiscountCurveInterface discountCurveForForwardRate, DiscountCurveInterface discountCurveForDiscountRate,
			Double volatility, Double theta, Double kappa, Double xi, Double rho) {
		super();
		this.referenceDate = referenceDate;
		this.initialValue = initialValue;
		this.discountCurveForForwardRate = discountCurveForForwardRate;
		this.discountCurveForDiscountRate = discountCurveForDiscountRate;
		this.volatility = volatility;
		this.theta = theta;
		this.kappa = kappa;
		this.xi = xi;
		this.rho = rho;
	}

	@Override
	public Integer version() {
		return 1;
	}

	@Override
	public String name() {
		return "Single asset Heston model";
	}

	/**
	 * @return the referenceDate
	 */
	public LocalDate getReferenceDate() {
		return referenceDate;
	}

	/**
	 * @return the initialValue
	 */
	public Double getInitialValue() {
		return initialValue;
	}

	/**
	 * @return the discountCurveForForwardRate
	 */
	public DiscountCurveInterface getDiscountCurveForForwardRate() {
		return discountCurveForForwardRate;
	}

	/**
	 * @return the discountCurveForDiscountRate
	 */
	public DiscountCurveInterface getDiscountCurveForDiscountRate() {
		return discountCurveForDiscountRate;
	}

	/**
	 * @return the volatility
	 */
	public Double getVolatility() {
		return volatility;
	}

	/**
	 * @return the theta
	 */
	public Double getTheta() {
		return theta;
	}

	/**
	 * @return the kappa
	 */
	public Double getKappa() {
		return kappa;
	}

	/**
	 * @return the xi
	 */
	public Double getXi() {
		return xi;
	}

	/**
	 * @return the rho
	 */
	public Double getRho() {
		return rho;
	}
}
