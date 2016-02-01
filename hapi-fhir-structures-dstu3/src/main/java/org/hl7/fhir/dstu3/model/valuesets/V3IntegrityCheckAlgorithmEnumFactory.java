package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3IntegrityCheckAlgorithmEnumFactory implements EnumFactory<V3IntegrityCheckAlgorithm> {

  public V3IntegrityCheckAlgorithm fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("SHA-1".equals(codeString))
      return V3IntegrityCheckAlgorithm.SHA1;
    if ("SHA-256".equals(codeString))
      return V3IntegrityCheckAlgorithm.SHA256;
    throw new IllegalArgumentException("Unknown V3IntegrityCheckAlgorithm code '"+codeString+"'");
  }

  public String toCode(V3IntegrityCheckAlgorithm code) {
    if (code == V3IntegrityCheckAlgorithm.SHA1)
      return "SHA-1";
    if (code == V3IntegrityCheckAlgorithm.SHA256)
      return "SHA-256";
    return "?";
  }

    public String toSystem(V3IntegrityCheckAlgorithm code) {
      return code.getSystem();
      }

}

