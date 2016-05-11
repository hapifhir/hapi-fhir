package org.hl7.fhir.instance.hapi.validation;

import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;

public interface IValidationSupport {

  /**
   * Expands the given portion of a ValueSet
   * 
   * @param theInclude
   *          The portion to include
   * @return The expansion
   */
  ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude);

  /**
   * Fetch a code system by ID
   * 
   * @param theSystem
   *          The code system
   * @return The valueset (must not be null, but can be an empty ValueSet)
   */
  ValueSet fetchCodeSystem(FhirContext theContext, String theSystem);

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
  CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay);

  public class CodeValidationResult {
    private ConceptDefinitionComponent definition;
    private String message;
    private IssueSeverity severity;

    public CodeValidationResult(ConceptDefinitionComponent definition) {
      this.definition = definition;
    }

    public CodeValidationResult(IssueSeverity severity, String message) {
      this.severity = severity;
      this.message = message;
    }

    public CodeValidationResult(IssueSeverity severity, String message, ConceptDefinitionComponent definition) {
      this.severity = severity;
      this.message = message;
      this.definition = definition;
    }

    public ConceptDefinitionComponent asConceptDefinition() {
      return definition;
    }

    public String getDisplay() {
      return definition == null ? "??" : definition.getDisplay();
    }

    public String getMessage() {
      return message;
    }

    public IssueSeverity getSeverity() {
      return severity;
    }

    public boolean isOk() {
      return definition != null;
    }

  }

}
