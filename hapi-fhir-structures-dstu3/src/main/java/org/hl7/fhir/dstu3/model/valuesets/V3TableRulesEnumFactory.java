package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TableRulesEnumFactory implements EnumFactory<V3TableRules> {

  public V3TableRules fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("all".equals(codeString))
      return V3TableRules.ALL;
    if ("cols".equals(codeString))
      return V3TableRules.COLS;
    if ("groups".equals(codeString))
      return V3TableRules.GROUPS;
    if ("none".equals(codeString))
      return V3TableRules.NONE;
    if ("rows".equals(codeString))
      return V3TableRules.ROWS;
    throw new IllegalArgumentException("Unknown V3TableRules code '"+codeString+"'");
  }

  public String toCode(V3TableRules code) {
    if (code == V3TableRules.ALL)
      return "all";
    if (code == V3TableRules.COLS)
      return "cols";
    if (code == V3TableRules.GROUPS)
      return "groups";
    if (code == V3TableRules.NONE)
      return "none";
    if (code == V3TableRules.ROWS)
      return "rows";
    return "?";
  }

    public String toSystem(V3TableRules code) {
      return code.getSystem();
      }

}

