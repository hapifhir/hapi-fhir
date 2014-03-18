package ca.uhn.fhir.model.api;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a FHIR resource path specification, e.g.
 * <code>Patient.gender.coding</code>
 * <p>
 * Note on equality: This class uses the {@link PathSpecification#setValue(String) value} 
 * as the single item used to provide {@link #hashCode()} and {@link #equals(Object)}.
 * </p>
 */
public class PathSpecification {

	private String myValue;

	public PathSpecification(String theValue) {
		myValue = theValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathSpecification other = (PathSpecification) obj;
		if (myValue == null) {
			if (other.myValue != null)
				return false;
		} else if (!myValue.equals(other.myValue))
			return false;
		return true;
	}

	public String getValue() {
		return myValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myValue == null) ? 0 : myValue.hashCode());
		return result;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("myValue", myValue);
		return builder.toString();
	}
	
}
