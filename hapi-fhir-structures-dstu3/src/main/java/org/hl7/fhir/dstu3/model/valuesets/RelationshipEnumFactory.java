package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class RelationshipEnumFactory implements EnumFactory<Relationship> {

  public Relationship fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return Relationship._1;
    if ("2".equals(codeString))
      return Relationship._2;
    if ("3".equals(codeString))
      return Relationship._3;
    if ("4".equals(codeString))
      return Relationship._4;
    if ("5".equals(codeString))
      return Relationship._5;
    throw new IllegalArgumentException("Unknown Relationship code '"+codeString+"'");
  }

  public String toCode(Relationship code) {
    if (code == Relationship._1)
      return "1";
    if (code == Relationship._2)
      return "2";
    if (code == Relationship._3)
      return "3";
    if (code == Relationship._4)
      return "4";
    if (code == Relationship._5)
      return "5";
    return "?";
  }

    public String toSystem(Relationship code) {
      return code.getSystem();
      }

}

