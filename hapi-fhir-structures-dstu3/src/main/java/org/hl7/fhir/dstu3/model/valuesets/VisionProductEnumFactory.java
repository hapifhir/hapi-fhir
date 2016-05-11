package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class VisionProductEnumFactory implements EnumFactory<VisionProduct> {

  public VisionProduct fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("lens".equals(codeString))
      return VisionProduct.LENS;
    if ("contact".equals(codeString))
      return VisionProduct.CONTACT;
    throw new IllegalArgumentException("Unknown VisionProduct code '"+codeString+"'");
  }

  public String toCode(VisionProduct code) {
    if (code == VisionProduct.LENS)
      return "lens";
    if (code == VisionProduct.CONTACT)
      return "contact";
    return "?";
  }

    public String toSystem(VisionProduct code) {
      return code.getSystem();
      }

}

