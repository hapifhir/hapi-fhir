package ca.uhn.fhir.context.support.support;

import ca.uhn.fhir.context.support.conceptproperty.BaseConceptProperty;

import javax.annotation.Nonnull;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class CodeValidationResult {
	private String myCode;
	private String myMessage;
	private IssueSeverity mySeverity;
	private String myCodeSystemName;
	private String myCodeSystemVersion;
	private List<BaseConceptProperty> myProperties;
	private String myDisplay;

	public CodeValidationResult() {
		super();
	}

	public String getDisplay() {
		return myDisplay;
	}

	public CodeValidationResult setDisplay(String theDisplay) {
		myDisplay = theDisplay;
		return this;
	}

	public String getCode() {
		return myCode;
	}

	public CodeValidationResult setCode(String theCode) {
		myCode = theCode;
		return this;
	}

	String getCodeSystemName() {
		return myCodeSystemName;
	}

	public CodeValidationResult setCodeSystemName(String theCodeSystemName) {
		myCodeSystemName = theCodeSystemName;
		return this;
	}

	public String getCodeSystemVersion() {
		return myCodeSystemVersion;
	}

	public CodeValidationResult setCodeSystemVersion(String theCodeSystemVersion) {
		myCodeSystemVersion = theCodeSystemVersion;
		return this;
	}

	public String getMessage() {
		return myMessage;
	}

	public CodeValidationResult setMessage(String theMessage) {
		myMessage = theMessage;
		return this;
	}

	public List<BaseConceptProperty> getProperties() {
		return myProperties;
	}

	public void setProperties(List<BaseConceptProperty> theProperties) {
		myProperties = theProperties;
	}

	public IssueSeverity getSeverity() {
		return mySeverity;
	}

	public CodeValidationResult setSeverity(IssueSeverity theSeverity) {
		mySeverity = theSeverity;
		return this;
	}

	public boolean isOk() {
		return isNotBlank(myCode);
	}

	public LookupCodeResult asLookupCodeResult(String theSearchedForSystem, String theSearchedForCode) {
		LookupCodeResult retVal = new LookupCodeResult();
		retVal.setSearchedForSystem(theSearchedForSystem);
		retVal.setSearchedForCode(theSearchedForCode);
		if (isOk()) {
			retVal.setFound(true);
			retVal.setCodeDisplay(myDisplay);
			retVal.setCodeSystemDisplayName(getCodeSystemName());
			retVal.setCodeSystemVersion(getCodeSystemVersion());
		}
		return retVal;
	}

	/**
	 * Convenience method that returns {@link #getSeverity()} as an IssueSeverity code string
	 */
	public String getSeverityCode() {
		String retVal = null;
		if (getSeverity() != null) {
			retVal = getSeverity().name().toLowerCase();
		}
		return retVal;
	}

	/**
	 * Sets an issue severity as a string code. Value must be the name of
	 * one of the enum values in {@link IssueSeverity}. Value is case-insensitive.
	 */
	public CodeValidationResult setSeverityCode(@Nonnull String theIssueSeverity) {
		setSeverity(IssueSeverity.valueOf(theIssueSeverity.toUpperCase()));
		return this;
	}
}
