package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class AdditionalmaterialsEnumFactory implements EnumFactory<Additionalmaterials> {

  public Additionalmaterials fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("xray".equals(codeString))
      return Additionalmaterials.XRAY;
    if ("image".equals(codeString))
      return Additionalmaterials.IMAGE;
    if ("email".equals(codeString))
      return Additionalmaterials.EMAIL;
    if ("model".equals(codeString))
      return Additionalmaterials.MODEL;
    if ("document".equals(codeString))
      return Additionalmaterials.DOCUMENT;
    if ("other".equals(codeString))
      return Additionalmaterials.OTHER;
    throw new IllegalArgumentException("Unknown Additionalmaterials code '"+codeString+"'");
  }

  public String toCode(Additionalmaterials code) {
    if (code == Additionalmaterials.XRAY)
      return "xray";
    if (code == Additionalmaterials.IMAGE)
      return "image";
    if (code == Additionalmaterials.EMAIL)
      return "email";
    if (code == Additionalmaterials.MODEL)
      return "model";
    if (code == Additionalmaterials.DOCUMENT)
      return "document";
    if (code == Additionalmaterials.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(Additionalmaterials code) {
      return code.getSystem();
      }

}

