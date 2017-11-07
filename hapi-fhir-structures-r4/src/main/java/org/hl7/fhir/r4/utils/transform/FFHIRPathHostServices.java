package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.utils.FHIRPathEngine;

import java.util.List;

/**
 * @author Claude Nanjo
 */
class FFHIRPathHostServices implements FHIRPathEngine.IEvaluationContext {

  /**
   *
   */
  private FhirTransformationEngine transformationEngine;

  /**
   * @param transformationEngine
   */
  public FFHIRPathHostServices(FhirTransformationEngine transformationEngine) {
    this.transformationEngine = transformationEngine;
  }

  /**
   * @param appContext - content passed into the fluent path engine
   * @param name       - name reference to resolve
   * @return
   * @throws PathEngineException
   */
  public Base resolveConstant(Object appContext, String name) throws PathEngineException {
    Variables vars = (Variables) appContext;
    Base res = vars.get(VariableMode.INPUT, name);
    if (res == null)
      res = vars.get(VariableMode.OUTPUT, name);
    return res;
  }

  /**
   * @param appContext
   * @param name
   * @return
   * @throws PathEngineException
   */
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

  /**
   * @param argument
   * @param focus
   * @return
   */
  @Override
  public boolean log(String argument, List<Base> focus) {
    throw new Error("Not Implemented Yet");
  }

  /**
   * @param functionName
   * @return
   */
  @Override
  public FunctionDetails resolveFunction(String functionName) {
    return null; // throw new Error("Not Implemented Yet");
  }

  /**
   * @param appContext
   * @param functionName
   * @param parameters
   * @return
   * @throws PathEngineException
   */
  @Override
  public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException {
    throw new Error("Not Implemented Yet");
  }

  /**
   * @param appContext
   * @param functionName
   * @param parameters
   * @return
   */
  @Override
  public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
    throw new Error("Not Implemented Yet");
  }

  /**
   * @param appContext
   * @param url
   * @return
   */
  @Override
  public Base resolveReference(Object appContext, String url) {
    if (transformationEngine.getServices() == null)
      return null;
    return transformationEngine.getServices().resolveReference(appContext, url);
  }

}
