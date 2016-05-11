package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AuditSourceTypeEnumFactory implements EnumFactory<AuditSourceType> {

  public AuditSourceType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return AuditSourceType._1;
    if ("2".equals(codeString))
      return AuditSourceType._2;
    if ("3".equals(codeString))
      return AuditSourceType._3;
    if ("4".equals(codeString))
      return AuditSourceType._4;
    if ("5".equals(codeString))
      return AuditSourceType._5;
    if ("6".equals(codeString))
      return AuditSourceType._6;
    if ("7".equals(codeString))
      return AuditSourceType._7;
    if ("8".equals(codeString))
      return AuditSourceType._8;
    if ("9".equals(codeString))
      return AuditSourceType._9;
    throw new IllegalArgumentException("Unknown AuditSourceType code '"+codeString+"'");
  }

  public String toCode(AuditSourceType code) {
    if (code == AuditSourceType._1)
      return "1";
    if (code == AuditSourceType._2)
      return "2";
    if (code == AuditSourceType._3)
      return "3";
    if (code == AuditSourceType._4)
      return "4";
    if (code == AuditSourceType._5)
      return "5";
    if (code == AuditSourceType._6)
      return "6";
    if (code == AuditSourceType._7)
      return "7";
    if (code == AuditSourceType._8)
      return "8";
    if (code == AuditSourceType._9)
      return "9";
    return "?";
  }

    public String toSystem(AuditSourceType code) {
      return code.getSystem();
      }

}

