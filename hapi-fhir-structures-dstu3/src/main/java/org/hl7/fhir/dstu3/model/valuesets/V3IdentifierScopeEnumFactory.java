package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3IdentifierScopeEnumFactory implements EnumFactory<V3IdentifierScope> {

  public V3IdentifierScope fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("BUSN".equals(codeString))
      return V3IdentifierScope.BUSN;
    if ("OBJ".equals(codeString))
      return V3IdentifierScope.OBJ;
    if ("VER".equals(codeString))
      return V3IdentifierScope.VER;
    if ("VW".equals(codeString))
      return V3IdentifierScope.VW;
    throw new IllegalArgumentException("Unknown V3IdentifierScope code '"+codeString+"'");
  }

  public String toCode(V3IdentifierScope code) {
    if (code == V3IdentifierScope.BUSN)
      return "BUSN";
    if (code == V3IdentifierScope.OBJ)
      return "OBJ";
    if (code == V3IdentifierScope.VER)
      return "VER";
    if (code == V3IdentifierScope.VW)
      return "VW";
    return "?";
  }

    public String toSystem(V3IdentifierScope code) {
      return code.getSystem();
      }

}

