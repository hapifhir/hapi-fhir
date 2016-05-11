package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class FormsEnumFactory implements EnumFactory<Forms> {

  public Forms fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return Forms._1;
    if ("2".equals(codeString))
      return Forms._2;
    throw new IllegalArgumentException("Unknown Forms code '"+codeString+"'");
  }

  public String toCode(Forms code) {
    if (code == Forms._1)
      return "1";
    if (code == Forms._2)
      return "2";
    return "?";
  }

    public String toSystem(Forms code) {
      return code.getSystem();
      }

}

