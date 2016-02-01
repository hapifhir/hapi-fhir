package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ClassificationOrContextEnumFactory implements EnumFactory<ClassificationOrContext> {

  public ClassificationOrContext fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("classification".equals(codeString))
      return ClassificationOrContext.CLASSIFICATION;
    if ("context".equals(codeString))
      return ClassificationOrContext.CONTEXT;
    throw new IllegalArgumentException("Unknown ClassificationOrContext code '"+codeString+"'");
  }

  public String toCode(ClassificationOrContext code) {
    if (code == ClassificationOrContext.CLASSIFICATION)
      return "classification";
    if (code == ClassificationOrContext.CONTEXT)
      return "context";
    return "?";
  }

    public String toSystem(ClassificationOrContext code) {
      return code.getSystem();
      }

}

