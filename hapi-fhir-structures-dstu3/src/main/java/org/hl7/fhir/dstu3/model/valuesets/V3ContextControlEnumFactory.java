package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ContextControlEnumFactory implements EnumFactory<V3ContextControl> {

  public V3ContextControl fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ContextControlAdditive".equals(codeString))
      return V3ContextControl._CONTEXTCONTROLADDITIVE;
    if ("AN".equals(codeString))
      return V3ContextControl.AN;
    if ("AP".equals(codeString))
      return V3ContextControl.AP;
    if ("_ContextControlNonPropagating".equals(codeString))
      return V3ContextControl._CONTEXTCONTROLNONPROPAGATING;
    if ("ON".equals(codeString))
      return V3ContextControl.ON;
    if ("_ContextControlOverriding".equals(codeString))
      return V3ContextControl._CONTEXTCONTROLOVERRIDING;
    if ("OP".equals(codeString))
      return V3ContextControl.OP;
    if ("_ContextControlPropagating".equals(codeString))
      return V3ContextControl._CONTEXTCONTROLPROPAGATING;
    throw new IllegalArgumentException("Unknown V3ContextControl code '"+codeString+"'");
  }

  public String toCode(V3ContextControl code) {
    if (code == V3ContextControl._CONTEXTCONTROLADDITIVE)
      return "_ContextControlAdditive";
    if (code == V3ContextControl.AN)
      return "AN";
    if (code == V3ContextControl.AP)
      return "AP";
    if (code == V3ContextControl._CONTEXTCONTROLNONPROPAGATING)
      return "_ContextControlNonPropagating";
    if (code == V3ContextControl.ON)
      return "ON";
    if (code == V3ContextControl._CONTEXTCONTROLOVERRIDING)
      return "_ContextControlOverriding";
    if (code == V3ContextControl.OP)
      return "OP";
    if (code == V3ContextControl._CONTEXTCONTROLPROPAGATING)
      return "_ContextControlPropagating";
    return "?";
  }

    public String toSystem(V3ContextControl code) {
      return code.getSystem();
      }

}

