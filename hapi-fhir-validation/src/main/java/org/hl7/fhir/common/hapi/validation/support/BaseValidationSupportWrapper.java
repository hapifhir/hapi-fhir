package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
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

	@Override
	public <T extends IBaseResource> List<T> fetchAllStructureDefinitions() {
		return myWrap.fetchAllStructureDefinitions();
	}

	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		return myWrap.fetchResource(theClass, theUri);
	}

	@Override
	public boolean isCodeSystemSupported(IValidationSupport theRootValidationSupport, String theSystem) {
		return myWrap.isCodeSystemSupported(myWrap, theSystem);
	}

	@Override
	public CodeValidationResult validateCode(IValidationSupport theRootValidationSupport, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return myWrap.validateCode(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
	}

	@Override
	public LookupCodeResult lookupCode(IValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		return myWrap.lookupCode(theRootValidationSupport, theSystem, theCode);
	}

	@Override
	public boolean isValueSetSupported(IValidationSupport theRootValidationSupport, String theValueSetUrl) {
		return myWrap.isValueSetSupported(myWrap, theValueSetUrl);
	}

	@Override
	public IValidationSupport.ValueSetExpansionOutcome expandValueSet(IValidationSupport theRootValidationSupport, ValueSetExpansionOptions theExpansionOptions, IBaseResource theValueSetToExpand) {
		return myWrap.expandValueSet(theRootValidationSupport, null, theValueSetToExpand);
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
	public IBaseResource generateSnapshot(IValidationSupport theRootValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		return myWrap.generateSnapshot(theRootValidationSupport, theInput, theUrl, theWebUrl, theProfileName);
	}

	@Override
	public IValidationSupport.CodeValidationResult validateCodeInValueSet(IValidationSupport theRootValidationSupport, ConceptValidationOptions theValidationOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		return myWrap.validateCodeInValueSet(theRootValidationSupport, theValidationOptions, theCodeSystem, theCode, theDisplay, theValueSet);
	}


}
