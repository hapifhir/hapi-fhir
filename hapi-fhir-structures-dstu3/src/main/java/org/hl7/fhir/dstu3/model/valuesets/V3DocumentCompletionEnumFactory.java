package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3DocumentCompletionEnumFactory implements EnumFactory<V3DocumentCompletion> {

  public V3DocumentCompletion fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AU".equals(codeString))
      return V3DocumentCompletion.AU;
    if ("DI".equals(codeString))
      return V3DocumentCompletion.DI;
    if ("DO".equals(codeString))
      return V3DocumentCompletion.DO;
    if ("IN".equals(codeString))
      return V3DocumentCompletion.IN;
    if ("IP".equals(codeString))
      return V3DocumentCompletion.IP;
    if ("LA".equals(codeString))
      return V3DocumentCompletion.LA;
    if ("NU".equals(codeString))
      return V3DocumentCompletion.NU;
    if ("PA".equals(codeString))
      return V3DocumentCompletion.PA;
    if ("UC".equals(codeString))
      return V3DocumentCompletion.UC;
    throw new IllegalArgumentException("Unknown V3DocumentCompletion code '"+codeString+"'");
  }

  public String toCode(V3DocumentCompletion code) {
    if (code == V3DocumentCompletion.AU)
      return "AU";
    if (code == V3DocumentCompletion.DI)
      return "DI";
    if (code == V3DocumentCompletion.DO)
      return "DO";
    if (code == V3DocumentCompletion.IN)
      return "IN";
    if (code == V3DocumentCompletion.IP)
      return "IP";
    if (code == V3DocumentCompletion.LA)
      return "LA";
    if (code == V3DocumentCompletion.NU)
      return "NU";
    if (code == V3DocumentCompletion.PA)
      return "PA";
    if (code == V3DocumentCompletion.UC)
      return "UC";
    return "?";
  }

    public String toSystem(V3DocumentCompletion code) {
      return code.getSystem();
      }

}

