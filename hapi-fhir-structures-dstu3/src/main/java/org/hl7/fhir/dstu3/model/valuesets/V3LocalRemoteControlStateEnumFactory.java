package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3LocalRemoteControlStateEnumFactory implements EnumFactory<V3LocalRemoteControlState> {

  public V3LocalRemoteControlState fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("L".equals(codeString))
      return V3LocalRemoteControlState.L;
    if ("R".equals(codeString))
      return V3LocalRemoteControlState.R;
    throw new IllegalArgumentException("Unknown V3LocalRemoteControlState code '"+codeString+"'");
  }

  public String toCode(V3LocalRemoteControlState code) {
    if (code == V3LocalRemoteControlState.L)
      return "L";
    if (code == V3LocalRemoteControlState.R)
      return "R";
    return "?";
  }

    public String toSystem(V3LocalRemoteControlState code) {
      return code.getSystem();
      }

}

