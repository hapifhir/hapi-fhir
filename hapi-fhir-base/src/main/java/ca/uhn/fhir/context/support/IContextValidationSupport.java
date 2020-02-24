package ca.uhn.fhir.context.support;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ParametersUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.ValidationOptions;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This interface is a version-independent representation of the
 * various functions that can be provided by validation and terminology
 * services.
 * <p>
 * Implementations are not required to implement all of the functions
 * in this interface; in fact it is expected that most won't. Any
 * methods which are not implemented may simply return <code>null</code>
 * and calling code is expected to be able to handle this.
 * </p>
 */
public interface IContextValidationSupport {
	String URL_PREFIX_VALUE_SET = "http://hl7.org/fhir/ValueSet/";


	/**
	 * Expands the given portion of a ValueSet
	 *
	 * @param theRootValidationSupport The validation support module will be passed in to this method. This is convenient in cases where the operation needs to make calls to
	 *                                 other method in the support chain.
	 * @param theValueSetToExpand      The valueset that should be expanded
	 * @return The expansion, or null
	 */
	default ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, IBaseResource theValueSetToExpand) {
		return null;
	}

	/**
	 * Load and return all conformance resources associated with this
	 * validation support module. This method may return null if it doesn't
	 * make sense for a given module.
	 */
	default List<IBaseResource> fetchAllConformanceResources() {
		return null;
	}

	/**
	 * Load and return all possible structure definitions
	 */
	default <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return null;
	}

	/**
	 * Fetch a code system by ID
	 *
	 * @param theSystem The code system
	 * @return The valueset (must not be null, but can be an empty ValueSet)
	 */
	default IBaseResource fetchCodeSystem(String theSystem) {
		return null;
	}

	/**
	 * Loads a resource needed by the validation (a StructureDefinition, or a
	 * ValueSet)
	 *
	 * @param theClass The type of the resource to load
	 * @param theUri   The resource URI
	 * @return Returns the resource, or <code>null</code> if no resource with the
	 * given URI can be found
	 */
	default <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		Validate.notNull(theClass, "theClass must not be null or blank");
		Validate.notBlank(theUri, "theUri must not be null or blank");

		switch (getFhirContext().getResourceDefinition(theClass).getName()) {
			case "StructureDefinition":
				return theClass.cast(fetchStructureDefinition(theUri));
			case "ValueSet":
				return theClass.cast(fetchValueSet(theUri));
			case "CodeSystem":
				return theClass.cast(fetchCodeSystem(theUri));
		}

		if (theUri.startsWith(URL_PREFIX_VALUE_SET)) {
			return theClass.cast(fetchValueSet(theUri));
		}

		return null;
	}

	default IBaseResource fetchStructureDefinition(String theUrl) {
		return null;
	}

	/**
	 * Returns <code>true</code> if codes in the given code system can be expanded
	 * or validated
	 *
	 * @param theSystem The URI for the code system, e.g. <code>"http://loinc.org"</code>
	 * @return Returns <code>true</code> if codes in the given code system can be
	 * validated
	 */
	default boolean isCodeSystemSupported(String theSystem) {
		return false;
	}

	/**
	 * Fetch the given ValueSet by URL
	 */
	default IBaseResource fetchValueSet(String theValueSetUrl) {
		return null;
	}

	/**
	 * Validates that the given code exists and if possible returns a display
	 * name. This method is called to check codes which are found in "example"
	 * binding fields (e.g. <code>Observation.code</code> in the default profile.
	 *
	 * @param theRootValidationSupport
	 * @param theOptions
	 * @param theCodeSystem            The code system, e.g. "<code>http://loinc.org</code>"
	 * @param theCode                  The code, e.g. "<code>1234-5</code>"
	 * @param theDisplay               The display name, if it should also be validated
	 * @return Returns a validation result object
	 */
	default CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, ValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return null;
	}

	/**
	 * Validates that the given code exists and if possible returns a display
	 * name. This method is called to check codes which are found in "example"
	 * binding fields (e.g. <code>Observation.code</code> in the default profile.
	 *
	 * @param theCodeSystem The code system, e.g. "<code>http://loinc.org</code>"
	 * @param theCode       The code, e.g. "<code>1234-5</code>"
	 * @param theDisplay    The display name, if it should also be validated
	 * @param theValueSet   The ValueSet to validate against. Must not be null, and must be a ValueSet resource.
	 * @return Returns a validation result object, or <code>null</code> if this validation support module can not handle this kind of request
	 */
	default CodeValidationResult validateCodeInValueSet(String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		return null;
	}

	/**
	 * Look up a code using the system and code value
	 *
	 * @param theRootValidationSupport
	 * @param theSystem                The CodeSystem URL
	 * @param theCode                  The code
	 */
	default LookupCodeResult lookupCode(IContextValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		return null;
	}

	/**
	 * Returns <code>true</code> if the given valueset can be validated by the given
	 * validation support module
	 *
	 * @param theValueSetUrl The URL
	 */
	default boolean isValueSetSupported(String theValueSetUrl) {
		return false;
	}

	/**
	 * Generate a snapshot from the given differential profile.
	 *
	 * @param theRootValidationSupport The root validation support object is passed in, in order to allow this operation to perform other lookups as required
	 * @return Returns null if this module does not know how to handle this request
	 */
	default IBaseResource generateSnapshot(IContextValidationSupport theRootValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		return null;
	}

	/**
	 * Returns the FHIR Context associated with this module
	 */
	FhirContext getFhirContext();


	class ConceptDesignation {
		private String myLanguage;
		private String myUseSystem;
		private String myUseCode;
		private String myUseDisplay;
		private String myValue;

		public String getLanguage() {
			return myLanguage;
		}

		public ConceptDesignation setLanguage(String theLanguage) {
			myLanguage = theLanguage;
			return this;
		}

		public String getUseSystem() {
			return myUseSystem;
		}

		public ConceptDesignation setUseSystem(String theUseSystem) {
			myUseSystem = theUseSystem;
			return this;
		}

		public String getUseCode() {
			return myUseCode;
		}

		public ConceptDesignation setUseCode(String theUseCode) {
			myUseCode = theUseCode;
			return this;
		}

		public String getUseDisplay() {
			return myUseDisplay;
		}

		public ConceptDesignation setUseDisplay(String theUseDisplay) {
			myUseDisplay = theUseDisplay;
			return this;
		}

		public String getValue() {
			return myValue;
		}

		public ConceptDesignation setValue(String theValue) {
			myValue = theValue;
			return this;
		}
	}

	abstract class BaseConceptProperty {
		private final String myPropertyName;

		/**
		 * Constructor
		 */
		protected BaseConceptProperty(String thePropertyName) {
			myPropertyName = thePropertyName;
		}

		public String getPropertyName() {
			return myPropertyName;
		}
	}

	class StringConceptProperty extends BaseConceptProperty {
		private final String myValue;

		/**
		 * Constructor
		 *
		 * @param theName The name
		 */
		public StringConceptProperty(String theName, String theValue) {
			super(theName);
			myValue = theValue;
		}

		public String getValue() {
			return myValue;
		}
	}

	class CodingConceptProperty extends BaseConceptProperty {
		private final String myCode;
		private final String myCodeSystem;
		private final String myDisplay;

		/**
		 * Constructor
		 *
		 * @param theName The name
		 */
		public CodingConceptProperty(String theName, String theCodeSystem, String theCode, String theDisplay) {
			super(theName);
			myCodeSystem = theCodeSystem;
			myCode = theCode;
			myDisplay = theDisplay;
		}

		public String getCode() {
			return myCode;
		}

		public String getCodeSystem() {
			return myCodeSystem;
		}

		public String getDisplay() {
			return myDisplay;
		}
	}

	class CodeValidationResult {
		private String myCode;
		private String myMessage;
		private String mySeverity;
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

		public void setCodeSystemName(String theCodeSystemName) {
			myCodeSystemName = theCodeSystemName;
		}

		public String getCodeSystemVersion() {
			return myCodeSystemVersion;
		}

		public void setCodeSystemVersion(String theCodeSystemVersion) {
			myCodeSystemVersion = theCodeSystemVersion;
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

		public String getSeverity() {
			return mySeverity;
		}

		public CodeValidationResult setSeverity(String theSeverity) {
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
	}

	class ValueSetExpansionOutcome {

		private final IBaseResource myValueSet;
		private final String myError;

		public ValueSetExpansionOutcome(IBaseResource theValueSet, String theError) {
			myValueSet = theValueSet;
			myError = theError;
		}

		public ValueSetExpansionOutcome(IBaseResource theValueSet) {
			myValueSet = theValueSet;
			myError = null;
		}

		public String getError() {
			return myError;
		}

		public IBaseResource getValueSet() {
			return myValueSet;
		}
	}

	class LookupCodeResult {

		private String myCodeDisplay;
		private boolean myCodeIsAbstract;
		private String myCodeSystemDisplayName;
		private String myCodeSystemVersion;
		private boolean myFound;
		private String mySearchedForCode;
		private String mySearchedForSystem;
		private List<IContextValidationSupport.BaseConceptProperty> myProperties;
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

		public void setProperties(List<IContextValidationSupport.BaseConceptProperty> theProperties) {
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

				for (IContextValidationSupport.BaseConceptProperty next : myProperties) {

					if (!properties.isEmpty()) {
						if (!properties.contains(next.getPropertyName())) {
							continue;
						}
					}

					IBase property = ParametersUtil.addParameterToParameters(theContext, retVal, "property");
					ParametersUtil.addPartCode(theContext, property, "code", next.getPropertyName());

					if (next instanceof IContextValidationSupport.StringConceptProperty) {
						IContextValidationSupport.StringConceptProperty prop = (IContextValidationSupport.StringConceptProperty) next;
						ParametersUtil.addPartString(theContext, property, "value", prop.getValue());
					} else if (next instanceof IContextValidationSupport.CodingConceptProperty) {
						IContextValidationSupport.CodingConceptProperty prop = (IContextValidationSupport.CodingConceptProperty) next;
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

}
