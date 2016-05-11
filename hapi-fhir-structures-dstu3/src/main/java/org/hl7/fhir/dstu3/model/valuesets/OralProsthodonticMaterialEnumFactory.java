package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class OralProsthodonticMaterialEnumFactory implements EnumFactory<OralProsthodonticMaterial> {

  public OralProsthodonticMaterial fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return OralProsthodonticMaterial._1;
    if ("2".equals(codeString))
      return OralProsthodonticMaterial._2;
    if ("3".equals(codeString))
      return OralProsthodonticMaterial._3;
    if ("4".equals(codeString))
      return OralProsthodonticMaterial._4;
    throw new IllegalArgumentException("Unknown OralProsthodonticMaterial code '"+codeString+"'");
  }

  public String toCode(OralProsthodonticMaterial code) {
    if (code == OralProsthodonticMaterial._1)
      return "1";
    if (code == OralProsthodonticMaterial._2)
      return "2";
    if (code == OralProsthodonticMaterial._3)
      return "3";
    if (code == OralProsthodonticMaterial._4)
      return "4";
    return "?";
  }

    public String toSystem(OralProsthodonticMaterial code) {
      return code.getSystem();
      }

}

