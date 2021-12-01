package org.hl7.fhir.dstu3.hapi.fluentpath;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;
import java.util.Optional;

public class FhirPathDstu3 implements IFhirPath {

	private FHIRPathEngine myEngine;

	public FhirPathDstu3(FhirContext theCtx) {
     IValidationSupport validationSupport = theCtx.getValidationSupport();
		myEngine = new FHIRPathEngine(new HapiWorkerContext(theCtx, validationSupport));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType) {
		List<Base> result;
		try {
			result = myEngine.evaluate((Base)theInput, thePath);
		} catch (FHIRException e) {
			throw new FhirPathExecutionException(Msg.code(607) + e);
		}

		for (Base next : result) {
			if (!theReturnType.isAssignableFrom(next.getClass())) {
				throw new FhirPathExecutionException(Msg.code(608) + "FluentPath expression \"" + thePath + "\" returned unexpected type " + next.getClass().getSimpleName() + " - Expected " + theReturnType.getName());
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
