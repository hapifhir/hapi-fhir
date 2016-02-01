package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActExposureLevelCodeEnumFactory implements EnumFactory<V3ActExposureLevelCode> {

  public V3ActExposureLevelCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActExposureLevelCode".equals(codeString))
      return V3ActExposureLevelCode._ACTEXPOSURELEVELCODE;
    if ("HIGH".equals(codeString))
      return V3ActExposureLevelCode.HIGH;
    if ("LOW".equals(codeString))
      return V3ActExposureLevelCode.LOW;
    if ("MEDIUM".equals(codeString))
      return V3ActExposureLevelCode.MEDIUM;
    throw new IllegalArgumentException("Unknown V3ActExposureLevelCode code '"+codeString+"'");
  }

  public String toCode(V3ActExposureLevelCode code) {
    if (code == V3ActExposureLevelCode._ACTEXPOSURELEVELCODE)
      return "_ActExposureLevelCode";
    if (code == V3ActExposureLevelCode.HIGH)
      return "HIGH";
    if (code == V3ActExposureLevelCode.LOW)
      return "LOW";
    if (code == V3ActExposureLevelCode.MEDIUM)
      return "MEDIUM";
    return "?";
  }

    public String toSystem(V3ActExposureLevelCode code) {
      return code.getSystem();
      }

}

