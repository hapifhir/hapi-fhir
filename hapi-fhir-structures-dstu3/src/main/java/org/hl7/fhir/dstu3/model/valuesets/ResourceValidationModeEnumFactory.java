package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ResourceValidationModeEnumFactory implements EnumFactory<ResourceValidationMode> {

  public ResourceValidationMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("create".equals(codeString))
      return ResourceValidationMode.CREATE;
    if ("update".equals(codeString))
      return ResourceValidationMode.UPDATE;
    if ("delete".equals(codeString))
      return ResourceValidationMode.DELETE;
    throw new IllegalArgumentException("Unknown ResourceValidationMode code '"+codeString+"'");
  }

  public String toCode(ResourceValidationMode code) {
    if (code == ResourceValidationMode.CREATE)
      return "create";
    if (code == ResourceValidationMode.UPDATE)
      return "update";
    if (code == ResourceValidationMode.DELETE)
      return "delete";
    return "?";
  }

    public String toSystem(ResourceValidationMode code) {
      return code.getSystem();
      }

}

