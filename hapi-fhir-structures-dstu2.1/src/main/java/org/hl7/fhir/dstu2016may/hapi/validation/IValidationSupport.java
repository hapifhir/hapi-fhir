package org.hl7.fhir.dstu2016may.hapi.validation;

import java.util.List;

import org.hl7.fhir.dstu2016may.model.CodeSystem;
import org.hl7.fhir.dstu2016may.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2016may.model.OperationOutcome;
import org.hl7.fhir.dstu2016may.model.ValueSet;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu2016may.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;

public interface IValidationSupport
		extends ca.uhn.fhir.context.support.IContextValidationSupport<ConceptSetComponent, ValueSetExpansionComponent, StructureDefinition, CodeSystem, ConceptDefinitionComponent, OperationOutcome.IssueSeverity> {

	/**
	 * Expands the given portion of a ValueSet
	 * 
	 * @param theInclude
	 *           The portion to include
	 * @return The expansion
	 */
	@Override
	ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude);

	/**
	 * Load and return all possible structure definitions
	 */
	@Override
	List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext);

	/**
	 * Fetch a code system by Uri
	 *
	 * @param uri
	 *           Canonical Uri of the code system
	 * @return The valueset (must not be null, but can be an empty ValueSet)
	 */
	@Override
	CodeSystem fetchCodeSystem(FhirContext theContext, String uri);

	/**
	 * Fetch a valueset by Uri
	 *
	 * @param uri
	 *           Canonical Uri of the ValueSet
	 * @return The valueset (must not be null, but can be an empty ValueSet)
	 */
	ValueSet fetchValueSet(FhirContext theContext, String uri);

	/**
	 * Loads a resource needed by the validation (a StructureDefinition, or a
	 * ValueSet)
	 * 
	 * @param theContext
	 *           The HAPI FHIR Context object current in use by the validator
	 * @param theClass
	 *           The type of the resource to load
	 * @param theUri
	 *           The resource URI
	 * @return Returns the resource, or <code>null</code> if no resource with the
	 *         given URI can be found
	 */
	@Override
	<T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri);

	@Override
	StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl);

	/**
	 * Returns <code>true</code> if codes in the given code system can be expanded
	 * or validated
	 * 
	 * @param theSystem
	 *           The URI for the code system, e.g. <code>"http://loinc.org"</code>
	 * @return Returns <code>true</code> if codes in the given code system can be
	 *         validated
	 */
	@Override
	boolean isCodeSystemSupported(FhirContext theContext, String theSystem);

	/**
	 * Validates that the given code exists and if possible returns a display
	 * name. This method is called to check codes which are found in "example"
	 * binding fields (e.g. <code>Observation.code</code> in the default profile.
	 * 
	 * @param theCodeSystem
	 *           The code system, e.g. "<code>http://loinc.org</code>"
	 * @param theCode
	 *           The code, e.g. "<code>1234-5</code>"
	 * @param theDisplay
	 *           The display name, if it should also be validated
	 * @return Returns a validation result object
	 */
	@Override
	CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay);

	class CodeValidationResult extends IContextValidationSupport.CodeValidationResult<ConceptDefinitionComponent, OperationOutcome.IssueSeverity> {

		public CodeValidationResult(ConceptDefinitionComponent theNext) {
			super(theNext);
		}

		public CodeValidationResult(OperationOutcome.IssueSeverity theSeverity, String theMessage) {
			super(theSeverity, theMessage);
		}

		public CodeValidationResult(OperationOutcome.IssueSeverity severity, String message, ConceptDefinitionComponent definition) {
			super(severity, message, definition);
		}

		@Override
		protected String getDisplay() {
			String retVal = null;
			if (isOk()) {
				retVal = asConceptDefinition().getDisplay();
			}
			return retVal;
		}

	}

}
