package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3MapRelationshipEnumFactory implements EnumFactory<V3MapRelationship> {

  public V3MapRelationship fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("BT".equals(codeString))
      return V3MapRelationship.BT;
    if ("E".equals(codeString))
      return V3MapRelationship.E;
    if ("NT".equals(codeString))
      return V3MapRelationship.NT;
    throw new IllegalArgumentException("Unknown V3MapRelationship code '"+codeString+"'");
  }

  public String toCode(V3MapRelationship code) {
    if (code == V3MapRelationship.BT)
      return "BT";
    if (code == V3MapRelationship.E)
      return "E";
    if (code == V3MapRelationship.NT)
      return "NT";
    return "?";
  }

    public String toSystem(V3MapRelationship code) {
      return code.getSystem();
      }

}

