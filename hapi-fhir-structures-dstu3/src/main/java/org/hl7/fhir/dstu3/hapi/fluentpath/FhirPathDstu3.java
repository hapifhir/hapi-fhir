package org.hl7.fhir.dstu3.hapi.fluentpath;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.TypeDetails;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.fhirpath.FhirPathExecutionException;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.fhirpath.IFhirPathEvaluationContext;
import ca.uhn.fhir.i18n.Msg;

public class FhirPathDstu3 implements IFhirPath {

    private FHIRPathEngine myEngine;

    public FhirPathDstu3(FhirContext theCtx) {
        IValidationSupport validationSupport = theCtx.getValidationSupport();
        myEngine = new FHIRPathEngine(new HapiWorkerContext(theCtx, validationSupport));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IBase> List<T> evaluate(
            IBase theInput, String thePath, Class<T> theReturnType) {
        List<Base> result;
        try {
            result = myEngine.evaluate((Base) theInput, thePath);
        } catch (FHIRException e) {
            throw new FhirPathExecutionException(Msg.code(607) + e);
        }

        for (Base next : result) {
            if (!theReturnType.isAssignableFrom(next.getClass())) {
                throw new FhirPathExecutionException(
                        Msg.code(608)
                                + "FluentPath expression \""
                                + thePath
                                + "\" returned unexpected type "
                                + next.getClass().getSimpleName()
                                + " - Expected "
                                + theReturnType.getName());
            }
        }

        return (List<T>) result;
    }

    @Override
    public <T extends IBase> Optional<T> evaluateFirst(
            IBase theInput, String thePath, Class<T> theReturnType) {
        return evaluate(theInput, thePath, theReturnType).stream().findFirst();
    }

    @Override
    public void parse(String theExpression) {
        myEngine.parse(theExpression);
    }

    @Override
    public void setEvaluationContext(@Nonnull IFhirPathEvaluationContext theEvaluationContext) {
        myEngine.setHostServices(
                new FHIRPathEngine.IEvaluationContext() {

                    @Override
                    public Base resolveConstant(Object appContext, String name)
                            throws PathEngineException {
                        return null;
                    }

                    @Override
                    public TypeDetails resolveConstantType(Object appContext, String name)
                            throws PathEngineException {
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
                    public TypeDetails checkFunction(
                            Object appContext, String functionName, List<TypeDetails> parameters)
                            throws PathEngineException {
                        return null;
                    }

                    @Override
                    public List<Base> executeFunction(
                            Object appContext, String functionName, List<List<Base>> parameters) {
                        return null;
                    }

                    @Override
                    public Base resolveReference(Object appContext, String theUrl)
                            throws FHIRException {
                        return (Base)
                                theEvaluationContext.resolveReference(new IdType(theUrl), null);
                    }
                });
    }
}
