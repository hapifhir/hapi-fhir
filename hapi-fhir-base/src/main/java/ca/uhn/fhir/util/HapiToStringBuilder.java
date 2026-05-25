package ca.uhn.fhir.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Added functionality to {@link ToStringBuilder}
 *
 * @since 8.12.0
 */
public class HapiToStringBuilder extends ToStringBuilder {

	public HapiToStringBuilder(Object theObject, ToStringStyle theStyle) {
		super(theObject, theStyle);
	}

	/**
	 * Performs an {@link #append(String, int)} if {@literal theValue != 0}
	 */
	public void appendIfNonZero(String theFieldName, int theValue) {
		if (theValue != 0) {
			append(theFieldName, theValue);
		}
	}
}
