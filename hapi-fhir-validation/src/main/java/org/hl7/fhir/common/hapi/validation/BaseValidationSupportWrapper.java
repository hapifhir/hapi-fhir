package org.hl7.fhir.common.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IContextValidationSupport;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.ValidationOptions;

import javax.annotation.Nonnull;
import java.util.List;

public class BaseValidationSupportWrapper extends BaseValidationSupport {
	private final IContextValidationSupport myWrap;

	/**
	 * Constructor
	 *
	 * @param theFhirContext
	 * @param theWrap
	 */
	public BaseValidationSupportWrapper(FhirContext theFhirContext, IContextValidationSupport theWrap) {
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
	public boolean isCodeSystemSupported(String theSystem) {
		return myWrap.isCodeSystemSupported(theSystem);
	}

	@Override
	public CodeValidationResult validateCode(IContextValidationSupport theRootValidationSupport, ValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		return myWrap.validateCode(theRootValidationSupport, theOptions, theCodeSystem, theCode, theDisplay, theValueSetUrl);
	}

	@Override
	public LookupCodeResult lookupCode(IContextValidationSupport theRootValidationSupport, String theSystem, String theCode) {
		return myWrap.lookupCode(theRootValidationSupport, theSystem, theCode);
	}

	@Override
	public boolean isValueSetSupported(String theValueSetUrl) {
		return myWrap.isValueSetSupported(theValueSetUrl);
	}

	@Override
	public IContextValidationSupport.ValueSetExpansionOutcome expandValueSet(IContextValidationSupport theRootValidationSupport, IBaseResource theValueSetToExpand) {
		return myWrap.expandValueSet(theRootValidationSupport, theValueSetToExpand);
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
	public IBaseResource generateSnapshot(IContextValidationSupport theRootValidationSupport, IBaseResource theInput, String theUrl, String theWebUrl, String theProfileName) {
		return myWrap.generateSnapshot(theRootValidationSupport, theInput, theUrl, theWebUrl, theProfileName);
	}

	@Override
	public IContextValidationSupport.CodeValidationResult validateCodeInValueSet(String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		return myWrap.validateCodeInValueSet(theCodeSystem, theCode, theDisplay, theValueSet);
	}


}
