package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3RelationshipConjunctionEnumFactory implements EnumFactory<V3RelationshipConjunction> {

  public V3RelationshipConjunction fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AND".equals(codeString))
      return V3RelationshipConjunction.AND;
    if ("OR".equals(codeString))
      return V3RelationshipConjunction.OR;
    if ("XOR".equals(codeString))
      return V3RelationshipConjunction.XOR;
    throw new IllegalArgumentException("Unknown V3RelationshipConjunction code '"+codeString+"'");
  }

  public String toCode(V3RelationshipConjunction code) {
    if (code == V3RelationshipConjunction.AND)
      return "AND";
    if (code == V3RelationshipConjunction.OR)
      return "OR";
    if (code == V3RelationshipConjunction.XOR)
      return "XOR";
    return "?";
  }

    public String toSystem(V3RelationshipConjunction code) {
      return code.getSystem();
      }

}

