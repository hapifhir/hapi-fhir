package org.hl7.fhir.dstu3.hapi.fluentpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fluentpath.*;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.ExpressionNode;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.Optional;

public class FluentPathDstu3 implements IFluentPath {

	private FHIRPathEngine myEngine;

	public FluentPathDstu3(FhirContext theCtx) {
		if (!(theCtx.getValidationSupport() instanceof IValidationSupport)) {
			throw new IllegalStateException("Validation support module configured on context appears to be for the wrong FHIR version- Does not extend " + IValidationSupport.class.getName());
		}
		IValidationSupport validationSupport = (IValidationSupport) theCtx.getValidationSupport();
		myEngine = new FHIRPathEngine(new HapiWorkerContext(theCtx, validationSupport));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType) {
		List<Base> result;
		try {
			result = myEngine.evaluate((Base)theInput, thePath);
		} catch (FHIRException e) {
			throw new FluentPathExecutionException(e);
		}

		for (Base next : result) {
			if (!theReturnType.isAssignableFrom(next.getClass())) {
				throw new FluentPathExecutionException("FluentPath expression \"" + thePath + "\" returned unexpected type " + next.getClass().getSimpleName() + " - Expected " + theReturnType.getName());
			}
		}
		
		return (List<T>) result;
	}

  @Override
  public <T extends IBase> Optional<T> evaluateFirst(IBase theInput, String thePath, Class<T> theReturnType) {
    return evaluate(theInput, thePath, theReturnType).stream().findFirst();
  }

  @Override
  public IExpressionNode parse(String path) {
    return myEngine.parse(path);
  }

  @Override
  public IExpressionNodeWithOffset parsePartial(String path, int offset) {
    return myEngine.parsePartial(path, offset);
  }

  @Override
  public String evaluateToString(Object theAppInfo, IBaseResource theResource, IBase theBase, IExpressionNode theCompiled) {
    return myEngine.evaluateToString(theAppInfo, (Resource)theResource, (Base)theBase, (ExpressionNode) theCompiled);
  }

  @Override
  public boolean evaluateToBoolean(Object theAppInfo, IBaseResource theResource, IBase theBase, IExpressionNode theCompiled) {
    return myEngine.evaluateToBoolean(theAppInfo, (Resource)theResource, (Base)theBase, (ExpressionNode) theCompiled);
  }

  @Override
  public List<IBase> evaluate(Object theAppInfo, IBaseResource theResource, IBase theBase, IExpressionNode theCompiled) {
    return (List<IBase>)(List<?>)myEngine.evaluate(theAppInfo, (Resource) theResource, (Base) theBase, (ExpressionNode) theCompiled);
  }

  @Override
  public void setHostServices(Object theHostServices) {
    myEngine.setHostServices((FHIRPathEngine.IEvaluationContext) theHostServices);
  }

  @Override
  public INarrativeConstantMap createLiquidIncludeMap() {
    return new NarrativeConstantMapDstu3();
  }
}
