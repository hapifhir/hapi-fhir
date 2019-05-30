package ca.uhn.fhir.context.support;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

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
public interface IContextValidationSupport<EVS_IN, EVS_OUT, SDT, CST, CDCT, IST> {

	/**
	 * Expands the given portion of a ValueSet
	 *
	 * @param theInclude The portion to include
	 * @return The expansion
	 */
	EVS_OUT expandValueSet(FhirContext theContext, EVS_IN theInclude);

	/**
	 * Load and return all conformance resources associated with this
	 * validation support module. This method may return null if it doesn't
	 * make sense for a given module.
	 */
	List<IBaseResource> fetchAllConformanceResources(FhirContext theContext);

	/**
	 * Load and return all possible structure definitions
	 */
	List<SDT> fetchAllStructureDefinitions(FhirContext theContext);

	/**
	 * Fetch a code system by ID
	 *
	 * @param theSystem The code system
	 * @return The valueset (must not be null, but can be an empty ValueSet)
	 */
	CST fetchCodeSystem(FhirContext theContext, String theSystem);

	/**
	 * Loads a resource needed by the validation (a StructureDefinition, or a
	 * ValueSet)
	 *
	 * @param theContext The HAPI FHIR Context object current in use by the validator
	 * @param theClass   The type of the resource to load
	 * @param theUri     The resource URI
	 * @return Returns the resource, or <code>null</code> if no resource with the
	 * given URI can be found
	 */
	<T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri);

	SDT fetchStructureDefinition(FhirContext theCtx, String theUrl);

	/**
	 * Returns <code>true</code> if codes in the given code system can be expanded
	 * or validated
	 *
	 * @param theSystem The URI for the code system, e.g. <code>"http://loinc.org"</code>
	 * @return Returns <code>true</code> if codes in the given code system can be
	 * validated
	 */
	boolean isCodeSystemSupported(FhirContext theContext, String theSystem);

	/**
	 * Validates that the given code exists and if possible returns a display
	 * name. This method is called to check codes which are found in "example"
	 * binding fields (e.g. <code>Observation.code</code> in the default profile.
	 *
	 * @param theCodeSystem The code system, e.g. "<code>http://loinc.org</code>"
	 * @param theCode       The code, e.g. "<code>1234-5</code>"
	 * @param theDisplay    The display name, if it should also be validated
	 * @return Returns a validation result object
	 */
	CodeValidationResult<CDCT, IST> validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay);

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

	class CodeValidationResult<CDCT, IST> {
		private CDCT myDefinition;
		private String myMessage;
		private IST mySeverity;
		private String myCodeSystemName;
		private String myCodeSystemVersion;
		private List<BaseConceptProperty> myProperties;

		public CodeValidationResult(CDCT theNext) {
			this.myDefinition = theNext;
		}

		public CodeValidationResult(IST severity, String message) {
			this.mySeverity = severity;
			this.myMessage = message;
		}

		public CodeValidationResult(IST severity, String message, CDCT definition) {
			this.mySeverity = severity;
			this.myMessage = message;
			this.myDefinition = definition;
		}

		public CDCT asConceptDefinition() {
			return myDefinition;
		}

		public String getCodeSystemName() {
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

		public List<BaseConceptProperty> getProperties() {
			return myProperties;
		}

		public void setProperties(List<BaseConceptProperty> theProperties) {
			myProperties = theProperties;
		}

		public IST getSeverity() {
			return mySeverity;
		}

		public boolean isOk() {
			return myDefinition != null;
		}

	}

}
