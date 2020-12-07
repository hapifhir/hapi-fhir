package ca.uhn.fhir.context.support.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.conceptproperty.BaseConceptProperty;
import ca.uhn.fhir.context.support.conceptproperty.CodingConceptProperty;
import ca.uhn.fhir.context.support.conceptproperty.StringConceptProperty;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ParametersUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class LookupCodeResult {

	private String myCodeDisplay;
	private boolean myCodeIsAbstract;
	private String myCodeSystemDisplayName;
	private String myCodeSystemVersion;
	private boolean myFound;
	private String mySearchedForCode;
	private String mySearchedForSystem;
	private List<BaseConceptProperty> myProperties;
	private List<ConceptDesignation> myDesignations;

	/**
	 * Constructor
	 */
	public LookupCodeResult() {
		super();
	}

	public List<BaseConceptProperty> getProperties() {
		if (myProperties == null) {
			myProperties = new ArrayList<>();
		}
		return myProperties;
	}

	public void setProperties(List<BaseConceptProperty> theProperties) {
		myProperties = theProperties;
	}

	@Nonnull
	public List<ConceptDesignation> getDesignations() {
		if (myDesignations == null) {
			myDesignations = new ArrayList<>();
		}
		return myDesignations;
	}

	public String getCodeDisplay() {
		return myCodeDisplay;
	}

	public void setCodeDisplay(String theCodeDisplay) {
		myCodeDisplay = theCodeDisplay;
	}

	public String getCodeSystemDisplayName() {
		return myCodeSystemDisplayName;
	}

	public void setCodeSystemDisplayName(String theCodeSystemDisplayName) {
		myCodeSystemDisplayName = theCodeSystemDisplayName;
	}

	public String getCodeSystemVersion() {
		return myCodeSystemVersion;
	}

	public void setCodeSystemVersion(String theCodeSystemVersion) {
		myCodeSystemVersion = theCodeSystemVersion;
	}

	public String getSearchedForCode() {
		return mySearchedForCode;
	}

	public LookupCodeResult setSearchedForCode(String theSearchedForCode) {
		mySearchedForCode = theSearchedForCode;
		return this;
	}

	public String getSearchedForSystem() {
		return mySearchedForSystem;
	}

	public LookupCodeResult setSearchedForSystem(String theSearchedForSystem) {
		mySearchedForSystem = theSearchedForSystem;
		return this;
	}

	public boolean isCodeIsAbstract() {
		return myCodeIsAbstract;
	}

	public void setCodeIsAbstract(boolean theCodeIsAbstract) {
		myCodeIsAbstract = theCodeIsAbstract;
	}

	public boolean isFound() {
		return myFound;
	}

	public LookupCodeResult setFound(boolean theFound) {
		myFound = theFound;
		return this;
	}

	public void throwNotFoundIfAppropriate() {
		if (isFound() == false) {
			throw new ResourceNotFoundException("Unable to find code[" + getSearchedForCode() + "] in system[" + getSearchedForSystem() + "]");
		}
	}

	public IBaseParameters toParameters(FhirContext theContext, List<? extends IPrimitiveType<String>> theProperties) {

		IBaseParameters retVal = ParametersUtil.newInstance(theContext);
		if (isNotBlank(getCodeSystemDisplayName())) {
			ParametersUtil.addParameterToParametersString(theContext, retVal, "name", getCodeSystemDisplayName());
		}
		if (isNotBlank(getCodeSystemVersion())) {
			ParametersUtil.addParameterToParametersString(theContext, retVal, "version", getCodeSystemVersion());
		}
		ParametersUtil.addParameterToParametersString(theContext, retVal, "display", getCodeDisplay());
		ParametersUtil.addParameterToParametersBoolean(theContext, retVal, "abstract", isCodeIsAbstract());

		if (myProperties != null) {

			Set<String> properties = Collections.emptySet();
			if (theProperties != null) {
				properties = theProperties
					.stream()
					.map(IPrimitiveType::getValueAsString)
					.collect(Collectors.toSet());
			}

			for (BaseConceptProperty next : myProperties) {

				if (!properties.isEmpty()) {
					if (!properties.contains(next.getPropertyName())) {
						continue;
					}
				}

				IBase property = ParametersUtil.addParameterToParameters(theContext, retVal, "property");
				ParametersUtil.addPartCode(theContext, property, "code", next.getPropertyName());

				if (next instanceof StringConceptProperty) {
					StringConceptProperty prop = (StringConceptProperty) next;
					ParametersUtil.addPartString(theContext, property, "value", prop.getValue());
				} else if (next instanceof CodingConceptProperty) {
					CodingConceptProperty prop = (CodingConceptProperty) next;
					ParametersUtil.addPartCoding(theContext, property, "value", prop.getCodeSystem(), prop.getCode(), prop.getDisplay());
				} else {
					throw new IllegalStateException("Don't know how to handle " + next.getClass());
				}
			}
		}

		if (myDesignations != null) {
			for (ConceptDesignation next : myDesignations) {

				IBase property = ParametersUtil.addParameterToParameters(theContext, retVal, "designation");
				ParametersUtil.addPartCode(theContext, property, "language", next.getLanguage());
				ParametersUtil.addPartCoding(theContext, property, "use", next.getUseSystem(), next.getUseCode(), next.getUseDisplay());
				ParametersUtil.addPartString(theContext, property, "value", next.getValue());
			}
		}

		return retVal;
	}

	public static LookupCodeResult notFound(String theSearchedForSystem, String theSearchedForCode) {
		return new LookupCodeResult()
			.setFound(false)
			.setSearchedForSystem(theSearchedForSystem)
			.setSearchedForCode(theSearchedForCode);
	}
}
