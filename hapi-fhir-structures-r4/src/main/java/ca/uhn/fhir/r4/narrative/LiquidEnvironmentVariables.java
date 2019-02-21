package ca.uhn.fhir.r4.narrative;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.TypeDetails;
import org.hl7.fhir.r4.utils.FHIRPathEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiquidEnvironmentVariables implements FHIRPathEngine.IEvaluationContext {
  private Map<String, String> constantMap = new HashMap<>();

  void put(String key, String value) {
    constantMap.put(key, value);
    if (!key.startsWith("\"")) {
      // Support both quoted and unquoted version of constant
      constantMap.put("\"" + key + "\"", value);
    }
  }

  @Override
  public Base resolveConstant(Object appContext, String name, boolean beforeContext) throws PathEngineException {
    String value = constantMap.get(name);
    if (value == null) {
      return null;
    }
    return new StringType(constantMap.get(name));
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
  public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters) {
    return null;
  }

  @Override
  public Base resolveReference(Object appContext, String url) throws FHIRException {
    return null;
  }

  @Override
  public boolean conformsToProfile(Object appContext, Base item, String url) throws FHIRException {
    return false;
  }
}
