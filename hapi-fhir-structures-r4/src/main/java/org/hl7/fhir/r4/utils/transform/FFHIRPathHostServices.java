package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.utils.FHIRPathEngine;

import java.util.List;

class FFHIRPathHostServices implements FHIRPathEngine.IEvaluationContext {

  private FhirTransformationEngine transformationEngine;

  public FFHIRPathHostServices(FhirTransformationEngine transformationEngine) {
    this.transformationEngine = transformationEngine;
  }

  public Base resolveConstant(Object appContext, String name) throws PathEngineException {
    Variables vars = (Variables) appContext;
    Base res = vars.get(VariableMode.INPUT, name);
    if (res == null)
      res = vars.get(VariableMode.OUTPUT, name);
    return res;
  }

  @Override
  public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException {
    if (!(appContext instanceof VariablesForProfiling))
      throw new Error("Internal Logic Error (wrong type '" + appContext.getClass().getName() + "' in resolveConstantType)");
    VariablesForProfiling vars = (VariablesForProfiling) appContext;
    VariableForProfiling v = vars.get(null, name);
    if (v == null)
      throw new PathEngineException("Unknown variable '" + name + "' from variables " + vars.summary());
    return v.getProperty().getTypes();
  }

  @Override
  public boolean log(String argument, List<Base> focus) {
    throw new Error("Not Implemented Yet");
  }

  @Override
  public FunctionDetails resolveFunction(String functionName) {
    return null; // throw new Error("Not Implemented Yet");
  }

  @Override
  public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
    throw new Error("Not Implemented Yet");
  }

  @Override
  public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
    throw new Error("Not Implemented Yet");
  }

  @Override
  public Base resolveReference(Object appContext, String url) {
    if (transformationEngine.getServices() == null)
      return null;
    return transformationEngine.getServices().resolveReference(appContext, url);
  }

}
