package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TransmissionRelationshipTypeCodeEnumFactory implements EnumFactory<V3TransmissionRelationshipTypeCode> {

  public V3TransmissionRelationshipTypeCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("SEQL".equals(codeString))
      return V3TransmissionRelationshipTypeCode.SEQL;
    throw new IllegalArgumentException("Unknown V3TransmissionRelationshipTypeCode code '"+codeString+"'");
  }

  public String toCode(V3TransmissionRelationshipTypeCode code) {
    if (code == V3TransmissionRelationshipTypeCode.SEQL)
      return "SEQL";
    return "?";
  }

    public String toSystem(V3TransmissionRelationshipTypeCode code) {
      return code.getSystem();
      }

}

