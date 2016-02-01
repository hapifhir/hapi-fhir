package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3AcknowledgementTypeEnumFactory implements EnumFactory<V3AcknowledgementType> {

  public V3AcknowledgementType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AA".equals(codeString))
      return V3AcknowledgementType.AA;
    if ("AE".equals(codeString))
      return V3AcknowledgementType.AE;
    if ("AR".equals(codeString))
      return V3AcknowledgementType.AR;
    if ("CA".equals(codeString))
      return V3AcknowledgementType.CA;
    if ("CE".equals(codeString))
      return V3AcknowledgementType.CE;
    if ("CR".equals(codeString))
      return V3AcknowledgementType.CR;
    throw new IllegalArgumentException("Unknown V3AcknowledgementType code '"+codeString+"'");
  }

  public String toCode(V3AcknowledgementType code) {
    if (code == V3AcknowledgementType.AA)
      return "AA";
    if (code == V3AcknowledgementType.AE)
      return "AE";
    if (code == V3AcknowledgementType.AR)
      return "AR";
    if (code == V3AcknowledgementType.CA)
      return "CA";
    if (code == V3AcknowledgementType.CE)
      return "CE";
    if (code == V3AcknowledgementType.CR)
      return "CR";
    return "?";
  }

    public String toSystem(V3AcknowledgementType code) {
      return code.getSystem();
      }

}

