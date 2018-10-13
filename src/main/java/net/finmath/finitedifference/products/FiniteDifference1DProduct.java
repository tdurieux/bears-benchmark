package net.finmath.finitedifference.products;

import net.finmath.finitedifference.models.FiniteDifference1DModel;
import net.finmath.modelling.ModelInterface;
import net.finmath.modelling.ProductInterface;

/**
 * Interface one dimensional finite difference products.
 *
 * @author Christian Fries
 */
public interface FiniteDifference1DProduct extends ProductInterface {

	/**
	 * Return the value of the product under the given model.
	 *
	 * @param evaluationTime The time at which the value (valuation) is requested.
	 * @param model The model under which the valuation should be performed.
	 * @return The random variable representing the valuation result.
	 */
	double[][] getValue(double evaluationTime, FiniteDifference1DModel model);


	default Object getValue(double evaluationTime, ModelInterface model) {
		if(model instanceof FiniteDifference1DModel) {
			return getValue(evaluationTime, (FiniteDifference1DModel) model);
		}
		else {
			throw new IllegalArgumentException(
					"The product " + this.getClass()
					+ " cannot be valued against a model " + model.getClass() + "."
					+ "It requires a model of type " + FiniteDifference1DModel.class + "."
					);
		}
	}
}
