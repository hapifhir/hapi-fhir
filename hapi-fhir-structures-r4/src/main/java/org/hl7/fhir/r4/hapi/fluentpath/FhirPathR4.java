package org.hl7.fhir.r4.hapi.fluentpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.fhirpath.ExpressionNode;
import org.hl7.fhir.r4.fhirpath.FHIRPathEngine;
import org.hl7.fhir.r4.fhirpath.FHIRPathUtilityClasses.FunctionDetails;
import org.hl7.fhir.r4.fhirpath.TypeDetails;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Optional;

public class FhirPathR4 implements IFhirPath {

	private final FHIRPathEngine myEngine;

	public FhirPathR4(FhirContext theCtx) {
		IValidationSupport validationSupport = theCtx.getValidationSupport();
		myEngine = new FHIRPathEngine(new HapiWorkerContext(theCtx, validationSupport));
		// These changes are to make the FP evaluation non-strict
		myEngine.setDoNotEnforceAsCaseSensitive(true);
		myEngine.setDoNotEnforceAsSingletonRule(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType) {
		ExpressionNode parsed;
		try {
			parsed = myEngine.parse(thePath);
		} catch (FHIRException e) {
			throw new FhirPathExecutionException(Msg.code(2409) + e);
		}
		return (List<T>) evaluate(theInput, parsed, theReturnType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBase> List<T> evaluate(
			IBase theInput, IParsedExpression theParsedExpression, Class<T> theReturnType) {
		ExpressionNode expressionNode = ((ParsedExpression) theParsedExpression).myParsedExpression;
		return (List<T>) evaluate(theInput, expressionNode, theReturnType);
	}

	@Nonnull
	private <T extends IBase> List<Base> evaluate(
			IBase theInput, ExpressionNode expressionNode, Class<T> theReturnType) {
		List<Base> result;
		try {
			result = myEngine.evaluate((Base) theInput, expressionNode);
		} catch (FHIRException e) {
			throw new FhirPathExecutionException(Msg.code(255) + e.getMessage(), e);
		}

		for (IBase next : result) {
			if (!theReturnType.isAssignableFrom(next.getClass())) {
				throw new FhirPathExecutionException(Msg.code(256) + "FhirPath expression returned unexpected type "
						+ next.getClass().getSimpleName() + " - Expected " + theReturnType.getName());
			}
		}
		return result;
	}

	@Override
	public <T extends IBase> Optional<T> evaluateFirst(IBase theInput, String thePath, Class<T> theReturnType) {
		return evaluate(theInput, thePath, theReturnType).stream().findFirst();
	}

	@Override
	public <T extends IBase> Optional<T> evaluateFirst(
			IBase theInput, IParsedExpression theParsedExpression, Class<T> theReturnType) {
		return evaluate(theInput, theParsedExpression, theReturnType).stream().findFirst();
	}

	@Override
	public IParsedExpression parse(String theExpression) {
		return new ParsedExpression(myEngine.parse(theExpression));
	}

	@Override
	public void setEvaluationContext(@Nonnull IFhirPathEvaluationContext theEvaluationContext) {
		myEngine.setHostServices(new FHIRPathEngine.IEvaluationContext() {

			@Override
			public List<Base> resolveConstant(
					FHIRPathEngine engine,
					Object appContext,
					String name,
					boolean beforeContext,
					boolean explicitConstant)
					throws PathEngineException {
				return null;
			}

			@Override
			public TypeDetails resolveConstantType(
					FHIRPathEngine engine, Object appContext, String name, boolean explicitConstant)
					throws PathEngineException {
				return null;
			}

			@Override
			public boolean log(String argument, List<Base> focus) {
				return false;
			}

			@Override
			public FunctionDetails resolveFunction(FHIRPathEngine engine, String functionName) {
				return null;
			}

			@Override
			public TypeDetails checkFunction(
					FHIRPathEngine engine,
					Object appContext,
					String functionName,
					TypeDetails focus,
					List<TypeDetails> parameters)
					throws PathEngineException {
				return null;
			}

			@Override
			public List<Base> executeFunction(
					FHIRPathEngine engine,
					Object appContext,
					List<Base> focus,
					String functionName,
					List<List<Base>> parameters) {
				return null;
			}

			@Override
			public Base resolveReference(FHIRPathEngine engine, Object appContext, String url, Base refContext)
					throws FHIRException {
				return (Base) theEvaluationContext.resolveReference(new IdType(url), refContext);
			}

			@Override
			public boolean conformsToProfile(FHIRPathEngine engine, Object appContext, Base item, String url)
					throws FHIRException {
				return false;
			}

			@Override
			public ValueSet resolveValueSet(FHIRPathEngine engine, Object appContext, String url) {
				return null;
			}
		});
	}

	private static class ParsedExpression implements IParsedExpression {

		private final ExpressionNode myParsedExpression;

		public ParsedExpression(ExpressionNode theParsedExpression) {
			myParsedExpression = theParsedExpression;
		}
	}
}
