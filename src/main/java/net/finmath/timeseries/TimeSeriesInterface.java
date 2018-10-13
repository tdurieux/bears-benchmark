package net.finmath.timeseries;

/**
 * Interface to be implemented by finite time series.
 *
 * @author Christian Fries
 */
public interface TimeSeriesInterface {

	double getTime(int index);
	double getValue(int index);

	int getNumberOfTimePoints();

	Iterable<Double> getValues();
}
