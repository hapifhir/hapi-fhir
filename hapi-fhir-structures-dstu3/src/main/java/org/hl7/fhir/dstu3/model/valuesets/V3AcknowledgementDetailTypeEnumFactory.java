package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3AcknowledgementDetailTypeEnumFactory implements EnumFactory<V3AcknowledgementDetailType> {

  public V3AcknowledgementDetailType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("E".equals(codeString))
      return V3AcknowledgementDetailType.E;
    if ("I".equals(codeString))
      return V3AcknowledgementDetailType.I;
    if ("W".equals(codeString))
      return V3AcknowledgementDetailType.W;
    if ("ERR".equals(codeString))
      return V3AcknowledgementDetailType.ERR;
    if ("INFO".equals(codeString))
      return V3AcknowledgementDetailType.INFO;
    if ("WARN".equals(codeString))
      return V3AcknowledgementDetailType.WARN;
    throw new IllegalArgumentException("Unknown V3AcknowledgementDetailType code '"+codeString+"'");
  }

  public String toCode(V3AcknowledgementDetailType code) {
    if (code == V3AcknowledgementDetailType.E)
      return "E";
    if (code == V3AcknowledgementDetailType.I)
      return "I";
    if (code == V3AcknowledgementDetailType.W)
      return "W";
    if (code == V3AcknowledgementDetailType.ERR)
      return "ERR";
    if (code == V3AcknowledgementDetailType.INFO)
      return "INFO";
    if (code == V3AcknowledgementDetailType.WARN)
      return "WARN";
    return "?";
  }

    public String toSystem(V3AcknowledgementDetailType code) {
      return code.getSystem();
      }

}

