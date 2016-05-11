package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ConfidentialityEnumFactory implements EnumFactory<V3Confidentiality> {

  public V3Confidentiality fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_Confidentiality".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITY;
    if ("L".equals(codeString))
      return V3Confidentiality.L;
    if ("M".equals(codeString))
      return V3Confidentiality.M;
    if ("N".equals(codeString))
      return V3Confidentiality.N;
    if ("R".equals(codeString))
      return V3Confidentiality.R;
    if ("U".equals(codeString))
      return V3Confidentiality.U;
    if ("V".equals(codeString))
      return V3Confidentiality.V;
    if ("_ConfidentialityByAccessKind".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITYBYACCESSKIND;
    if ("B".equals(codeString))
      return V3Confidentiality.B;
    if ("D".equals(codeString))
      return V3Confidentiality.D;
    if ("I".equals(codeString))
      return V3Confidentiality.I;
    if ("_ConfidentialityByInfoType".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITYBYINFOTYPE;
    if ("ETH".equals(codeString))
      return V3Confidentiality.ETH;
    if ("HIV".equals(codeString))
      return V3Confidentiality.HIV;
    if ("PSY".equals(codeString))
      return V3Confidentiality.PSY;
    if ("SDV".equals(codeString))
      return V3Confidentiality.SDV;
    if ("_ConfidentialityModifiers".equals(codeString))
      return V3Confidentiality._CONFIDENTIALITYMODIFIERS;
    if ("C".equals(codeString))
      return V3Confidentiality.C;
    if ("S".equals(codeString))
      return V3Confidentiality.S;
    if ("T".equals(codeString))
      return V3Confidentiality.T;
    throw new IllegalArgumentException("Unknown V3Confidentiality code '"+codeString+"'");
  }

  public String toCode(V3Confidentiality code) {
    if (code == V3Confidentiality._CONFIDENTIALITY)
      return "_Confidentiality";
    if (code == V3Confidentiality.L)
      return "L";
    if (code == V3Confidentiality.M)
      return "M";
    if (code == V3Confidentiality.N)
      return "N";
    if (code == V3Confidentiality.R)
      return "R";
    if (code == V3Confidentiality.U)
      return "U";
    if (code == V3Confidentiality.V)
      return "V";
    if (code == V3Confidentiality._CONFIDENTIALITYBYACCESSKIND)
      return "_ConfidentialityByAccessKind";
    if (code == V3Confidentiality.B)
      return "B";
    if (code == V3Confidentiality.D)
      return "D";
    if (code == V3Confidentiality.I)
      return "I";
    if (code == V3Confidentiality._CONFIDENTIALITYBYINFOTYPE)
      return "_ConfidentialityByInfoType";
    if (code == V3Confidentiality.ETH)
      return "ETH";
    if (code == V3Confidentiality.HIV)
      return "HIV";
    if (code == V3Confidentiality.PSY)
      return "PSY";
    if (code == V3Confidentiality.SDV)
      return "SDV";
    if (code == V3Confidentiality._CONFIDENTIALITYMODIFIERS)
      return "_ConfidentialityModifiers";
    if (code == V3Confidentiality.C)
      return "C";
    if (code == V3Confidentiality.S)
      return "S";
    if (code == V3Confidentiality.T)
      return "T";
    return "?";
  }

    public String toSystem(V3Confidentiality code) {
      return code.getSystem();
      }

}

