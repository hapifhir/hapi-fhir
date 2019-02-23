package ca.uhn.fhir.dstu3.narrative;

import ca.uhn.fhir.fluentpath.INarrativeConstantResolver;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.TypeDetails;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.utilities.liquid.BaseLiquidHostServices;

import java.util.List;

public class LiquidHostServices extends BaseLiquidHostServices implements FHIRPathEngine.IEvaluationContext {

  public LiquidHostServices(INarrativeConstantResolver theLiquidEngine) {
    super(theLiquidEngine);
  }

  @Override
  public Base resolveConstant(Object appContext, String name) throws PathEngineException {
    return (Base)super.resolveConstant(appContext, name, true);
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
  public Base resolveReference(Object appContext, String url) {
    return null;
  }

  @Override
  protected IBase toStringtype(String s) {
    return new StringType(s);
  }
}
