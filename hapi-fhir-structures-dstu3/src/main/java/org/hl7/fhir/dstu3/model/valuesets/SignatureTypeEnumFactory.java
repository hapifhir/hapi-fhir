package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class SignatureTypeEnumFactory implements EnumFactory<SignatureType> {

  public SignatureType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1.2.840.10065.1.12.1.1".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_1;
    if ("1.2.840.10065.1.12.1.2".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_2;
    if ("1.2.840.10065.1.12.1.3".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_3;
    if ("1.2.840.10065.1.12.1.4".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_4;
    if ("1.2.840.10065.1.12.1.5".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_5;
    if ("1.2.840.10065.1.12.1.6".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_6;
    if ("1.2.840.10065.1.12.1.7".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_7;
    if ("1.2.840.10065.1.12.1.8".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_8;
    if ("1.2.840.10065.1.12.1.9".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_9;
    if ("1.2.840.10065.1.12.1.10".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_10;
    if ("1.2.840.10065.1.12.1.11".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_11;
    if ("1.2.840.10065.1.12.1.12".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_12;
    if ("1.2.840.10065.1.12.1.13".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_13;
    if ("1.2.840.10065.1.12.1.14".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_14;
    if ("1.2.840.10065.1.12.1.15".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_15;
    if ("1.2.840.10065.1.12.1.16".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_16;
    if ("1.2.840.10065.1.12.1.17".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_17;
    if ("1.2.840.10065.1.12.1.18".equals(codeString))
      return SignatureType.OID_1_2_840_10065_1_12_1_18;
    throw new IllegalArgumentException("Unknown SignatureType code '"+codeString+"'");
  }

  public String toCode(SignatureType code) {
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_1)
      return "1.2.840.10065.1.12.1.1";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_2)
      return "1.2.840.10065.1.12.1.2";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_3)
      return "1.2.840.10065.1.12.1.3";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_4)
      return "1.2.840.10065.1.12.1.4";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_5)
      return "1.2.840.10065.1.12.1.5";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_6)
      return "1.2.840.10065.1.12.1.6";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_7)
      return "1.2.840.10065.1.12.1.7";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_8)
      return "1.2.840.10065.1.12.1.8";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_9)
      return "1.2.840.10065.1.12.1.9";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_10)
      return "1.2.840.10065.1.12.1.10";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_11)
      return "1.2.840.10065.1.12.1.11";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_12)
      return "1.2.840.10065.1.12.1.12";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_13)
      return "1.2.840.10065.1.12.1.13";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_14)
      return "1.2.840.10065.1.12.1.14";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_15)
      return "1.2.840.10065.1.12.1.15";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_16)
      return "1.2.840.10065.1.12.1.16";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_17)
      return "1.2.840.10065.1.12.1.17";
    if (code == SignatureType.OID_1_2_840_10065_1_12_1_18)
      return "1.2.840.10065.1.12.1.18";
    return "?";
  }

    public String toSystem(SignatureType code) {
      return code.getSystem();
      }

}

