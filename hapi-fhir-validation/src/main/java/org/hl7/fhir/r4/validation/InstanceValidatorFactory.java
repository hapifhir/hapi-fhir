package org.hl7.fhir.r4.validation;

import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.context.SimpleWorkerContext.IValidatorFactory;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.hl7.fhir.exceptions.FHIRException;

public class InstanceValidatorFactory implements IValidatorFactory {

  @Override
  public IResourceValidator makeValidator(IWorkerContext ctxt) throws FHIRException {
    return new InstanceValidator(ctxt, null);
  }

}
