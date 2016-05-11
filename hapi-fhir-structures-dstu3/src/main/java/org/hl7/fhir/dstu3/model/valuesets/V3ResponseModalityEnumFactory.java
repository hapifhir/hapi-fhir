package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ResponseModalityEnumFactory implements EnumFactory<V3ResponseModality> {

  public V3ResponseModality fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("B".equals(codeString))
      return V3ResponseModality.B;
    if ("R".equals(codeString))
      return V3ResponseModality.R;
    if ("T".equals(codeString))
      return V3ResponseModality.T;
    throw new IllegalArgumentException("Unknown V3ResponseModality code '"+codeString+"'");
  }

  public String toCode(V3ResponseModality code) {
    if (code == V3ResponseModality.B)
      return "B";
    if (code == V3ResponseModality.R)
      return "R";
    if (code == V3ResponseModality.T)
      return "T";
    return "?";
  }

    public String toSystem(V3ResponseModality code) {
      return code.getSystem();
      }

}

