/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 21.06.2008
 */
package net.finmath.optimizer;

/**
 * Exception thrown by solvers {@link net.finmath.rootfinder} or {@link net.finmath.optimizer}.
 *
 * @author Christian Fries
 */
public class SolverException extends Exception {

	private static final long serialVersionUID = 7123998462171729835L;

	/**
	 * Create an exception with error message.
	 *
	 * @param message The error message.
	 */
	public SolverException(String message) {
		super(message);
	}

	/**
	 * Create an exception from another exception.
	 *
	 * @param cause The cause.
	 */
	public SolverException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create an exception from another exception with error message.
	 *
	 * @param message The error message.
	 * @param cause The cause.
	 */
	public SolverException(String message, Throwable cause) {
		super(message, cause);
	}

}
