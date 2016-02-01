package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3CommunicationFunctionTypeEnumFactory implements EnumFactory<V3CommunicationFunctionType> {

  public V3CommunicationFunctionType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("RCV".equals(codeString))
      return V3CommunicationFunctionType.RCV;
    if ("RSP".equals(codeString))
      return V3CommunicationFunctionType.RSP;
    if ("SND".equals(codeString))
      return V3CommunicationFunctionType.SND;
    throw new IllegalArgumentException("Unknown V3CommunicationFunctionType code '"+codeString+"'");
  }

  public String toCode(V3CommunicationFunctionType code) {
    if (code == V3CommunicationFunctionType.RCV)
      return "RCV";
    if (code == V3CommunicationFunctionType.RSP)
      return "RSP";
    if (code == V3CommunicationFunctionType.SND)
      return "SND";
    return "?";
  }

    public String toSystem(V3CommunicationFunctionType code) {
      return code.getSystem();
      }

}

