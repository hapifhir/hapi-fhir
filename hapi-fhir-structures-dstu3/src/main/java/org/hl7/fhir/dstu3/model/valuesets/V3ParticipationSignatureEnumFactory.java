package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ParticipationSignatureEnumFactory implements EnumFactory<V3ParticipationSignature> {

  public V3ParticipationSignature fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("I".equals(codeString))
      return V3ParticipationSignature.I;
    if ("S".equals(codeString))
      return V3ParticipationSignature.S;
    if ("X".equals(codeString))
      return V3ParticipationSignature.X;
    throw new IllegalArgumentException("Unknown V3ParticipationSignature code '"+codeString+"'");
  }

  public String toCode(V3ParticipationSignature code) {
    if (code == V3ParticipationSignature.I)
      return "I";
    if (code == V3ParticipationSignature.S)
      return "S";
    if (code == V3ParticipationSignature.X)
      return "X";
    return "?";
  }

    public String toSystem(V3ParticipationSignature code) {
      return code.getSystem();
      }

}

