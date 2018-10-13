package net.finmath.modelling.describedproducts;

import java.time.LocalDate;

import net.finmath.modelling.DescribedProduct;
import net.finmath.modelling.descriptor.SingleAssetEuropeanOptionProductDescriptor;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.time.FloatingpointDate;

public class EuropeanOptionMonteCarlo extends EuropeanOption implements DescribedProduct<SingleAssetEuropeanOptionProductDescriptor> {

	private final SingleAssetEuropeanOptionProductDescriptor descriptor;
	
	/**
	 * Construct a product representing an European option on an asset S (where S the asset with index 0 from the model - single asset case).
	 * @param descriptor Implementation of SingleAssetEuropeanOptionProductDescriptor
	 */
	public EuropeanOptionMonteCarlo(SingleAssetEuropeanOptionProductDescriptor descriptor, LocalDate referenceDate) {
		super(descriptor.getUnderlyingName(), FloatingpointDate.getFloatingPointDateFromDate(referenceDate, descriptor.getMaturity()), descriptor.getStrike());
		this.descriptor = descriptor;
	}
	
	@Override
	public SingleAssetEuropeanOptionProductDescriptor getDescriptor() {
		return descriptor;
	}
}
