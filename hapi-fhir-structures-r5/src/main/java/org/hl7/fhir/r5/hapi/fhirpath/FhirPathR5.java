package org.hl7.fhir.r5.hapi.fhirpath;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r5.model.Base;
import org.hl7.fhir.r5.utils.FHIRPathEngine;

import java.util.List;
import java.util.Optional;

public class FhirPathR5 implements IFhirPath {

  private FHIRPathEngine myEngine;

  public FhirPathR5(FhirContext theCtx) {
	  IValidationSupport validationSupport = theCtx.getValidationSupport();
    myEngine = new FHIRPathEngine(new HapiWorkerContext(theCtx, validationSupport));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType) {
    List<Base> result;
    try {
      result = myEngine.evaluate((Base) theInput, thePath);
    } catch (FHIRException e) {
      throw new FhirPathExecutionException(Msg.code(198) + e);
	 }

	  for (Base next : result) {
      if (!theReturnType.isAssignableFrom(next.getClass())) {
        throw new FhirPathExecutionException(Msg.code(199) + "FluentPath expression \"" + thePath + "\" returned unexpected type " + next.getClass().getSimpleName() + " - Expected " + theReturnType.getName());
      }
    }

    return (List<T>) result;
  }

  @Override
  public <T extends IBase> Optional<T> evaluateFirst(IBase theInput, String thePath, Class<T> theReturnType) {
    return evaluate(theInput, thePath, theReturnType).stream().findFirst();
  }

	@Override
	public void parse(String theExpression) {
		myEngine.parse(theExpression);
	}


}
