package org.hl7.fhir.dstu3.hapi.fluentpath;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.dstu3.fhirpath.ExpressionNode;
import org.hl7.fhir.dstu3.fhirpath.FHIRPathEngine;
import org.hl7.fhir.dstu3.fhirpath.FHIRPathUtilityClasses.FunctionDetails;
import org.hl7.fhir.dstu3.fhirpath.TypeDetails;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;

import java.util.List;
import java.util.Optional;

public class FhirPathDstu3 implements IFhirPath {

	private final FHIRPathEngine myEngine;

	public FhirPathDstu3(FhirContext theCtx) {
		IValidationSupport validationSupport = theCtx.getValidationSupport();
		myEngine = new FHIRPathEngine(new HapiWorkerContext(theCtx, validationSupport));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBase> List<T> evaluate(IBase theInput, String thePath, Class<T> theReturnType) {
		ExpressionNode parsed;
		try {
			parsed = myEngine.parse(thePath);
		} catch (FHIRException e) {
			throw new FhirPathExecutionException(Msg.code(2408) + e);
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
			throw new FhirPathExecutionException(Msg.code(607) + e);
		}

		for (IBase next : result) {
			if (!theReturnType.isAssignableFrom(next.getClass())) {
				throw new FhirPathExecutionException(Msg.code(608) + "FhirPath expression returned unexpected type "
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
			public Base resolveConstant(Object appContext, String name) throws PathEngineException {
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
			public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters)
					throws PathEngineException {
				return null;
			}

			@Override
			public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
				return null;
			}

			@Override
			public Base resolveReference(Object appContext, String theUrl) throws FHIRException {
				return (Base) theEvaluationContext.resolveReference(new IdType(theUrl), null);
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
