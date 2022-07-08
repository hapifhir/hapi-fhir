package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is a wrapper for an existing {@link @IContextValidationSupport} object, intended to be
 * subclassed in order to layer functionality on top of the existing validation support object.
 *
 * @since 5.0.0
 */
public class BaseValidationSupportWrapper extends BaseValidationSupport {
	private final IValidationSupport myWrap;

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FhirContext object (must be initialized for the appropriate FHIR version)
	 * @param theWrap The validation support object to wrap
	 */
	public BaseValidationSupportWrapper(FhirContext theFhirContext, IValidationSupport theWrap) {
		super(theFhirContext);
		Validate.notNull(theWrap, "theWrap must not be null");

		myWrap = theWrap;
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		return myWrap.fetchAllConformanceResources();
	}

	@Nullable
	@Override
	public <T extends IBaseResource> List<T> fetchAllNonBaseStructureDefinitions() {
		return myWrap.fetchAllNonBaseStructureDefinitions();
	}

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return myWrap.fetchAllStructureDefinitions();
	}

	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		return myWrap.fetchResource(theClass, theUri);
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return myWrap.isCodeSystemSupported(theValidationSupportContext, theSystem);
	}

	@Override
	public CodeValidationResult validateCode(@Nonnull ValidationSupportContext theValidationSupportContext, @Nonnull ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return myWrap.validateCode(theValidationSupportContext, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
	}

	@Override
	public IValidationSupport.CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theValidationOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		return myWrap.validateCodeInValueSet(theValidationSupportContext, theValidationOptions, theCodeSystem, theCode, theDisplay, theValueSet);
	}

	@Override
	public LookupCodeResult lookupCode(ValidationSupportContext theValidationSupportContext, String theSystem, String theCode, String theDisplayLanguage) {
		return myWrap.lookupCode(theValidationSupportContext, theSystem, theCode, theDisplayLanguage);
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return myWrap.isValueSetSupported(theValidationSupportContext, theValueSetUrl);
	}

	@Override
	public IValidationSupport.ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext, ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
		return myWrap.expandValueSet(theValidationSupportContext, theExpansionOptions, theValueSetToExpand);
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		return myWrap.fetchCodeSystem(theSystem);
	}

	@Override
	public IBaseResource fetchValueSet(String theUri) {
		return myWrap.fetchValueSet(theUri);
	}


	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return myWrap.fetchStructureDefinition(theUrl);
	}

	@Override
	public IBaseResource generateSnapshot(ValidationSupportContext theValidationSupportContext, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		return myWrap.generateSnapshot(theValidationSupportContext, theInput, theUrl, theWebUrl, theProfileName);
	}

	@Override
	public TranslateConceptResults translateConcept(TranslateCodeRequest theRequest) {
		return myWrap.translateConcept(theRequest);
	}
}
