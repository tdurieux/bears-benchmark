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
 */
public class BlackScholesModelDescriptor implements AssetModelDescriptor {

	private final LocalDate referenceDate;

	private final Double initialValue;

	private final DiscountCurveInterface discountCurveForForwardRate;
	private final DiscountCurveInterface discountCurveForDiscountRate;

	private final Double volatility;

	public BlackScholesModelDescriptor(LocalDate referenceDate, Double initialValue,
			DiscountCurveInterface discountCurveForForwardRate, DiscountCurveInterface discountCurveForDiscountRate,
			Double volatility) {
		super();
		this.referenceDate = referenceDate;
		this.initialValue = initialValue;
		this.discountCurveForForwardRate = discountCurveForForwardRate;
		this.discountCurveForDiscountRate = discountCurveForDiscountRate;
		this.volatility = volatility;
	}

	@Override
	public Integer version() {
		return 1;
	}

	@Override
	public String name() {
		return "Single asset Black Scholes model";
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
}
