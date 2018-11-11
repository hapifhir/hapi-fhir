package org.hl7.fhir.dstu3.validation;

import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.utils.IResourceValidator;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.context.SimpleWorkerContext;

public class InstanceValidatorFactory implements SimpleWorkerContext.IValidatorFactory {

	@Override
	public org.hl7.fhir.r4.utils.IResourceValidator makeValidator(org.hl7.fhir.r4.context.IWorkerContext ctxts) throws FHIRException {
		throw new UnsupportedOperationException();
	}
}
