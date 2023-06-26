package org.hl7.fhir.r5.hapi.fhirpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r5.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r5.model.Base;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.TypeDetails;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.utils.FHIRPathEngine;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public class FhirPathR5 implements IFhirPath {

	private FHIRPathEngine myEngine;

	public FhirPathR5(FhirContext theCtx) {
		IValidationSupport validationSupport = theCtx.getValidationSupport();
		myEngine = new FHIRPathEngine(new HapiWorkerContext(theCtx, validationSupport));
		myEngine.setDoNotEnforceAsSingletonRule(true);
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

	@Override
	public void setEvaluationContext(@Nonnull IFhirPathEvaluationContext theEvaluationContext) {
		myEngine.setHostServices(new FHIRPathEngine.IEvaluationContext() {

			@Override
			public List<Base> resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
				return null;
			}

			@Override
			public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
				return null;
			}

			@Override
			public boolean log(String argument, List<Base> focus) {
				return false;
			}

			@Override
			public FunctionDetails resolveFunction(String functionName) {
				return null;
			}

			@Override
			public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
				return null;
			}

			@Override
			public List<Base> executeFunction(Object appContext, List<Base> focus, String functionName, List<List<Base>> parameters) {
				return null;
			}

			@Override
			public Base resolveReference(Object appContext, String theUrl, Base refContext) throws FHIRException {
				return (Base) theEvaluationContext.resolveReference(new IdType(theUrl), refContext);
			}

			@Override
			public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
				return false;
			}

			@Override
			public ValueSet resolveValueSet(Object appContext, String url) {
				return null;
			}
		});
	}
}
