package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityRiskEnumFactory implements EnumFactory<V3EntityRisk> {

  public V3EntityRisk fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AGG".equals(codeString))
      return V3EntityRisk.AGG;
    if ("BIO".equals(codeString))
      return V3EntityRisk.BIO;
    if ("COR".equals(codeString))
      return V3EntityRisk.COR;
    if ("ESC".equals(codeString))
      return V3EntityRisk.ESC;
    if ("IFL".equals(codeString))
      return V3EntityRisk.IFL;
    if ("EXP".equals(codeString))
      return V3EntityRisk.EXP;
    if ("INF".equals(codeString))
      return V3EntityRisk.INF;
    if ("BHZ".equals(codeString))
      return V3EntityRisk.BHZ;
    if ("INJ".equals(codeString))
      return V3EntityRisk.INJ;
    if ("POI".equals(codeString))
      return V3EntityRisk.POI;
    if ("RAD".equals(codeString))
      return V3EntityRisk.RAD;
    throw new IllegalArgumentException("Unknown V3EntityRisk code '"+codeString+"'");
  }

  public String toCode(V3EntityRisk code) {
    if (code == V3EntityRisk.AGG)
      return "AGG";
    if (code == V3EntityRisk.BIO)
      return "BIO";
    if (code == V3EntityRisk.COR)
      return "COR";
    if (code == V3EntityRisk.ESC)
      return "ESC";
    if (code == V3EntityRisk.IFL)
      return "IFL";
    if (code == V3EntityRisk.EXP)
      return "EXP";
    if (code == V3EntityRisk.INF)
      return "INF";
    if (code == V3EntityRisk.BHZ)
      return "BHZ";
    if (code == V3EntityRisk.INJ)
      return "INJ";
    if (code == V3EntityRisk.POI)
      return "POI";
    if (code == V3EntityRisk.RAD)
      return "RAD";
    return "?";
  }

    public String toSystem(V3EntityRisk code) {
      return code.getSystem();
      }

}

