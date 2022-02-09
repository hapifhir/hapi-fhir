package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public class JobDefinitionParameter {

	private final String myName;
	private final String myDescription;
	private final TypeEnum myType;
	private final boolean myRequired;
	private final boolean myRepeating;

	/**
	 * Constructor
	 */
	public JobDefinitionParameter(@Nonnull String theName, @Nonnull String theDescription, @Nonnull TypeEnum theType, boolean theRequired, boolean theRepeating) {
		Validate.notBlank(theName);
		Validate.notBlank(theDescription);

		myName = theName;
		myDescription = theDescription;
		myType = theType;
		myRequired = theRequired;
		myRepeating = theRepeating;
	}

	public enum TypeEnum {

		STRING

	}
}
