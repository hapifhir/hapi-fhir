package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityNamePartTypeEnumFactory implements EnumFactory<V3EntityNamePartType> {

  public V3EntityNamePartType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("DEL".equals(codeString))
      return V3EntityNamePartType.DEL;
    if ("FAM".equals(codeString))
      return V3EntityNamePartType.FAM;
    if ("GIV".equals(codeString))
      return V3EntityNamePartType.GIV;
    if ("PFX".equals(codeString))
      return V3EntityNamePartType.PFX;
    if ("SFX".equals(codeString))
      return V3EntityNamePartType.SFX;
    throw new IllegalArgumentException("Unknown V3EntityNamePartType code '"+codeString+"'");
  }

  public String toCode(V3EntityNamePartType code) {
    if (code == V3EntityNamePartType.DEL)
      return "DEL";
    if (code == V3EntityNamePartType.FAM)
      return "FAM";
    if (code == V3EntityNamePartType.GIV)
      return "GIV";
    if (code == V3EntityNamePartType.PFX)
      return "PFX";
    if (code == V3EntityNamePartType.SFX)
      return "SFX";
    return "?";
  }

    public String toSystem(V3EntityNamePartType code) {
      return code.getSystem();
      }

}

