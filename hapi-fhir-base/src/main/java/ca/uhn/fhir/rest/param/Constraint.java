package ca.uhn.fhir.rest.param;

/**
 * Used to add an additional constraint to an existing parameter
 */
public class Constraint<T> {

	/**
	 * The constraint type.
	 */
	public enum Type {
		/**
		 * This indicates an upper bound.
		 * The constrained value should be *lower* than this value.
		 */
		UPPER,
		/**
		 * This indicates a lower bound.
		 * The constrained value should be *higher* than this value.
		 */
		LOWER;
	}

	/**
	 * The value of the constraint.
	 */
	private final T myConstraintValue;

	/**
	 * The kind of constraint.
	 */
	private final Type myType;

	public Constraint(T theValue, Type theType) {
		myConstraintValue = theValue;
		myType = theType;
	}

	public T getConstraintValue() {
		return myConstraintValue;
	}

	public Type getType() {
		return myType;
	}
}
