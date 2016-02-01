package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EducationLevelEnumFactory implements EnumFactory<V3EducationLevel> {

  public V3EducationLevel fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ASSOC".equals(codeString))
      return V3EducationLevel.ASSOC;
    if ("BD".equals(codeString))
      return V3EducationLevel.BD;
    if ("ELEM".equals(codeString))
      return V3EducationLevel.ELEM;
    if ("GD".equals(codeString))
      return V3EducationLevel.GD;
    if ("HS".equals(codeString))
      return V3EducationLevel.HS;
    if ("PB".equals(codeString))
      return V3EducationLevel.PB;
    if ("POSTG".equals(codeString))
      return V3EducationLevel.POSTG;
    if ("SCOL".equals(codeString))
      return V3EducationLevel.SCOL;
    if ("SEC".equals(codeString))
      return V3EducationLevel.SEC;
    throw new IllegalArgumentException("Unknown V3EducationLevel code '"+codeString+"'");
  }

  public String toCode(V3EducationLevel code) {
    if (code == V3EducationLevel.ASSOC)
      return "ASSOC";
    if (code == V3EducationLevel.BD)
      return "BD";
    if (code == V3EducationLevel.ELEM)
      return "ELEM";
    if (code == V3EducationLevel.GD)
      return "GD";
    if (code == V3EducationLevel.HS)
      return "HS";
    if (code == V3EducationLevel.PB)
      return "PB";
    if (code == V3EducationLevel.POSTG)
      return "POSTG";
    if (code == V3EducationLevel.SCOL)
      return "SCOL";
    if (code == V3EducationLevel.SEC)
      return "SEC";
    return "?";
  }

    public String toSystem(V3EducationLevel code) {
      return code.getSystem();
      }

}

