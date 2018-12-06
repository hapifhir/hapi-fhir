package ca.uhn.fhir.jpa.model.entity;

import java.io.Serializable;

public abstract class BaseResourceIndex implements Serializable {

	public abstract Long getId();

	public abstract void setId(Long theId);

	public abstract void calculateHashes();

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

}
