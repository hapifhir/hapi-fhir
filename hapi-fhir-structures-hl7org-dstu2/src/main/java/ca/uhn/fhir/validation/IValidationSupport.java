package ca.uhn.fhir.validation;

import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;

public interface IValidationSupport {

  /**
   * Returns <code>true</code> if codes in the given code system can be
   * validated
   * 
   * @param theSystem
   *          The URI for the code system, e.g. <code>"http://loinc.org"</code>
   * @return Returns <code>true</code> if codes in the given code system can be
   *         validated
   */
  boolean isCodeSystemSupported(String theSystem);

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
  org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult validateCode(String theCodeSystem, String theCode,
      String theDisplay);

  /**
   * Fetch a code system by ID
   * 
   * @param theSystem
   *          The code system
   * @return The valueset (must not be null, but can be an empty ValueSet)
   */
  ValueSet fetchCodeSystem(String theSystem);

}
