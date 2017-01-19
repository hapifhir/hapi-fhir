package ca.uhn.fhir.context.support;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;

public interface IContextValidationSupport<EVS_IN, EVS_OUT, SDT, CST, CDCT, IST> {

  /**
   * Expands the given portion of a ValueSet
   * 
   * @param theInclude
   *          The portion to include
   * @return The expansion
   */
  EVS_OUT expandValueSet(FhirContext theContext, EVS_IN theInclude);

  /**
   * Load and return all possible structure definitions
   */
  List<SDT> fetchAllStructureDefinitions(FhirContext theContext);

  
  /**
   * Fetch a code system by ID
   * 
   * @param theSystem
   *          The code system
   * @return The valueset (must not be null, but can be an empty ValueSet)
   */
  CST fetchCodeSystem(FhirContext theContext, String theSystem);

  /**
   * Loads a resource needed by the validation (a StructureDefinition, or a
   * ValueSet)
   * 
   * @param theContext
   *          The HAPI FHIR Context object current in use by the validator
   * @param theClass
   *          The type of the resource to load
   * @param theUri
   *          The resource URI
   * @return Returns the resource, or <code>null</code> if no resource with the
   *         given URI can be found
   */
  <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri);

  SDT fetchStructureDefinition(FhirContext theCtx, String theUrl);

  /**
   * Returns <code>true</code> if codes in the given code system can be expanded
   * or validated
   * 
   * @param theSystem
   *          The URI for the code system, e.g. <code>"http://loinc.org"</code>
   * @return Returns <code>true</code> if codes in the given code system can be
   *         validated
   */
  boolean isCodeSystemSupported(FhirContext theContext, String theSystem);

/**
   * Validates that the given code exists and if possible returns a display
   * name. This method is called to check codes which are found in "example"
   * binding fields (e.g. <code>Observation.code</code> in the default profile.
   * 
   * @param theCodeSystem
   *          The code system, e.g. "<code>http://loinc.org</code>"
   * @param theCode
   *          The code, e.g. "<code>1234-5</code>"
   * @param theDisplay
   *          The display name, if it should also be validated
   * @return Returns a validation result object
   */
  CodeValidationResult<CDCT, IST> validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay);

	public class CodeValidationResult<CDCT, IST> {
	    private CDCT definition;
	    private String message;
	    private IST severity;
	
	    public CodeValidationResult(CDCT theNext) {
	      this.definition = theNext;
	    }
	
	    public CodeValidationResult(IST severity, String message) {
	      this.severity = severity;
	      this.message = message;
	    }
	
	    public CodeValidationResult(IST severity, String message, CDCT definition) {
	      this.severity = severity;
	      this.message = message;
	      this.definition = definition;
	    }
	
	    public CDCT asConceptDefinition() {
	      return definition;
	    }
		
	    public String getMessage() {
	      return message;
	    }
	
	    public IST getSeverity() {
	      return severity;
	    }
	
	    public boolean isOk() {
	      return definition != null;
	    }
	
	  }

}
