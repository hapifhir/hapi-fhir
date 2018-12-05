package ca.uhn.fhir.jpa.model.entity;

import java.io.Serializable;

public abstract class BaseResourceIndex implements Serializable {

	/**
	 * Subclasses must implement
	 */
	@Override
	public abstract int hashCode();

	/**
	 * Subclasses must implement
	 */
	@Override
	public abstract boolean equals(Object obj);

	/**
	 * Subclasses must implement this method which is used to
	 * <b>completely replace</b>
	 * the contents of this index with the contents from the given index.
	 *
	 * @param theBaseResourceIndex The given index from which to take contents. Implementations
	 *                             may assume that the type will be the same as this class.
	 */
	public abstract void populateFrom(BaseResourceIndex theBaseResourceIndex);

}
