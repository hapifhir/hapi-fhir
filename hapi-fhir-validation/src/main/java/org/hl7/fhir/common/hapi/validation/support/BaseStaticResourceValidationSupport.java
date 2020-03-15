package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseStaticResourceValidationSupport extends BaseValidationSupport implements IValidationSupport {

	/**
	 * Constructor
	 */
	protected BaseStaticResourceValidationSupport(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	@SuppressWarnings("unchecked")
	static <T extends IBaseResource> List<T> toList(Map<String, IBaseResource> theMap) {
		ArrayList<IBaseResource> retVal = new ArrayList<>(theMap.values());
		return (List<T>) Collections.unmodifiableList(retVal);
	}

}
