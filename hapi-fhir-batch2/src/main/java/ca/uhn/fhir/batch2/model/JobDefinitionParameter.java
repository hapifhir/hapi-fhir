package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

public class JobDefinitionParameter {

	private final String myName;
	private final String myDescription;
	private final ParamTypeEnum myType;
	private final boolean myRequired;
	private final boolean myRepeating;

	/**
	 * Constructor
	 */
	public JobDefinitionParameter(@Nonnull String theName, @Nonnull String theDescription, @Nonnull ParamTypeEnum theType, boolean theRequired, boolean theRepeating) {
		Validate.isTrue(theName.length() <= ID_MAX_LENGTH, "Maximum name length is %d", ID_MAX_LENGTH);

		Validate.notBlank(theName);
		Validate.notBlank(theDescription);

		myName = theName;
		myDescription = theDescription;
		myType = theType;
		myRequired = theRequired;
		myRepeating = theRepeating;
	}

	public String getName() {
		return myName;
	}

	public String getDescription() {
		return myDescription;
	}

	public ParamTypeEnum getType() {
		return myType;
	}

	public boolean isRequired() {
		return myRequired;
	}

	public boolean isRepeating() {
		return myRepeating;
	}

	public enum ParamTypeEnum {

		STRING

	}
}
