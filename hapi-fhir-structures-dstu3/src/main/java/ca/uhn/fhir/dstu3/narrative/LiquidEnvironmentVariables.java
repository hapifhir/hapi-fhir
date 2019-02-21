package ca.uhn.fhir.dstu3.narrative;

import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.TypeDetails;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiquidEnvironmentVariables implements FHIRPathEngine.IEvaluationContext {
  private Map<String, String> constantMap = new HashMap<>();

  public void put(String key, String value) {
    constantMap.put(key, value);
    if (!key.startsWith("\"")) {
      // Support both quoted and unquoted version of constant
      constantMap.put("\"" + key + "\"", value);
    }
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
  public Base resolveConstant(Object appContext, String name) throws PathEngineException {
    String value = constantMap.get(name);
    if (value == null) {
      return null;
    }
    return new StringType(constantMap.get(name));
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
}
