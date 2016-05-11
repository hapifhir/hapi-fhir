package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ExposureModeEnumFactory implements EnumFactory<V3ExposureMode> {

  public V3ExposureMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ExposureMode".equals(codeString))
      return V3ExposureMode._EXPOSUREMODE;
    if ("AIRBORNE".equals(codeString))
      return V3ExposureMode.AIRBORNE;
    if ("CONTACT".equals(codeString))
      return V3ExposureMode.CONTACT;
    if ("FOODBORNE".equals(codeString))
      return V3ExposureMode.FOODBORNE;
    if ("WATERBORNE".equals(codeString))
      return V3ExposureMode.WATERBORNE;
    throw new IllegalArgumentException("Unknown V3ExposureMode code '"+codeString+"'");
  }

  public String toCode(V3ExposureMode code) {
    if (code == V3ExposureMode._EXPOSUREMODE)
      return "_ExposureMode";
    if (code == V3ExposureMode.AIRBORNE)
      return "AIRBORNE";
    if (code == V3ExposureMode.CONTACT)
      return "CONTACT";
    if (code == V3ExposureMode.FOODBORNE)
      return "FOODBORNE";
    if (code == V3ExposureMode.WATERBORNE)
      return "WATERBORNE";
    return "?";
  }

    public String toSystem(V3ExposureMode code) {
      return code.getSystem();
      }

}

