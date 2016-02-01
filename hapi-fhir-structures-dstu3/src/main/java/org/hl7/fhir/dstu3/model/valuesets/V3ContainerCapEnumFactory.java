package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ContainerCapEnumFactory implements EnumFactory<V3ContainerCap> {

  public V3ContainerCap fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_MedicationCap".equals(codeString))
      return V3ContainerCap._MEDICATIONCAP;
    if ("CHILD".equals(codeString))
      return V3ContainerCap.CHILD;
    if ("EASY".equals(codeString))
      return V3ContainerCap.EASY;
    if ("FILM".equals(codeString))
      return V3ContainerCap.FILM;
    if ("FOIL".equals(codeString))
      return V3ContainerCap.FOIL;
    if ("PUSH".equals(codeString))
      return V3ContainerCap.PUSH;
    if ("SCR".equals(codeString))
      return V3ContainerCap.SCR;
    throw new IllegalArgumentException("Unknown V3ContainerCap code '"+codeString+"'");
  }

  public String toCode(V3ContainerCap code) {
    if (code == V3ContainerCap._MEDICATIONCAP)
      return "_MedicationCap";
    if (code == V3ContainerCap.CHILD)
      return "CHILD";
    if (code == V3ContainerCap.EASY)
      return "EASY";
    if (code == V3ContainerCap.FILM)
      return "FILM";
    if (code == V3ContainerCap.FOIL)
      return "FOIL";
    if (code == V3ContainerCap.PUSH)
      return "PUSH";
    if (code == V3ContainerCap.SCR)
      return "SCR";
    return "?";
  }

    public String toSystem(V3ContainerCap code) {
      return code.getSystem();
      }

}

