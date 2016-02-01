package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityNamePartTypeR2EnumFactory implements EnumFactory<V3EntityNamePartTypeR2> {

  public V3EntityNamePartTypeR2 fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("DEL".equals(codeString))
      return V3EntityNamePartTypeR2.DEL;
    if ("FAM".equals(codeString))
      return V3EntityNamePartTypeR2.FAM;
    if ("GIV".equals(codeString))
      return V3EntityNamePartTypeR2.GIV;
    if ("TITLE".equals(codeString))
      return V3EntityNamePartTypeR2.TITLE;
    throw new IllegalArgumentException("Unknown V3EntityNamePartTypeR2 code '"+codeString+"'");
  }

  public String toCode(V3EntityNamePartTypeR2 code) {
    if (code == V3EntityNamePartTypeR2.DEL)
      return "DEL";
    if (code == V3EntityNamePartTypeR2.FAM)
      return "FAM";
    if (code == V3EntityNamePartTypeR2.GIV)
      return "GIV";
    if (code == V3EntityNamePartTypeR2.TITLE)
      return "TITLE";
    return "?";
  }

    public String toSystem(V3EntityNamePartTypeR2 code) {
      return code.getSystem();
      }

}

