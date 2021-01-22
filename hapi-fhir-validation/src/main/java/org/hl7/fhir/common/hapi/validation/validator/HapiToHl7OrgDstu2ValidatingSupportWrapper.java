package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.support.BaseValidationSupportWrapper;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.stream.Collectors;

public class HapiToHl7OrgDstu2ValidatingSupportWrapper extends BaseValidationSupportWrapper implements IValidationSupport {
	private final FhirContext myHapiCtx;

	/**
	 * Constructor
	 */
	public HapiToHl7OrgDstu2ValidatingSupportWrapper(IValidationSupport theWrap) {
		super(FhirContext.forDstu2Hl7Org(), theWrap);

		Validate.isTrue(theWrap.getFhirContext().getVersion().getVersion() == FhirVersionEnum.DSTU2);
		myHapiCtx = theWrap.getFhirContext();
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources() {
		return super.fetchAllConformanceResources();
	}

	@Override
	public List<IBaseResource> fetchAllStructureDefinitions() {
		return super
			.fetchAllStructureDefinitions()
			.stream()
			.map(t -> translate(t))
			.collect(Collectors.toList());
	}

	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		Class<? extends IBaseResource> type = translateTypeToHapi(theClass);
		IBaseResource output = super.fetchResource(type, theUri);
		return theClass.cast(translate(output));
	}

	@Override
	public IBaseResource fetchCodeSystem(String theSystem) {
		IBaseResource output = super.fetchCodeSystem(theSystem);
		return translate(output);
	}

	@Override
	public IBaseResource fetchValueSet(String theUri) {
		return translate(super.fetchValueSet(theUri));
	}

	@Override
	public IBaseResource fetchStructureDefinition(String theUrl) {
		return translate(super.fetchStructureDefinition(theUrl));
	}

	private Class<? extends IBaseResource> translateTypeToHapi(Class<? extends IBaseResource> theCodeSystemType) {
		if (theCodeSystemType == null) {
			return null;
		}
		String resName = getFhirContext().getResourceType(theCodeSystemType);
		return myHapiCtx.getResourceDefinition(resName).getImplementingClass();
	}

	private IBaseResource translate(IBaseResource theInput) {
		if (theInput == null) {
			return null;
		}
		String encoded = myHapiCtx.newJsonParser().encodeResourceToString(theInput);
		return getFhirContext().newJsonParser().parseResource(encoded);
	}
}
