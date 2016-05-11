package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3CompressionAlgorithmEnumFactory implements EnumFactory<V3CompressionAlgorithm> {

  public V3CompressionAlgorithm fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("BZ".equals(codeString))
      return V3CompressionAlgorithm.BZ;
    if ("DF".equals(codeString))
      return V3CompressionAlgorithm.DF;
    if ("GZ".equals(codeString))
      return V3CompressionAlgorithm.GZ;
    if ("Z".equals(codeString))
      return V3CompressionAlgorithm.Z;
    if ("Z7".equals(codeString))
      return V3CompressionAlgorithm.Z7;
    if ("ZL".equals(codeString))
      return V3CompressionAlgorithm.ZL;
    throw new IllegalArgumentException("Unknown V3CompressionAlgorithm code '"+codeString+"'");
  }

  public String toCode(V3CompressionAlgorithm code) {
    if (code == V3CompressionAlgorithm.BZ)
      return "BZ";
    if (code == V3CompressionAlgorithm.DF)
      return "DF";
    if (code == V3CompressionAlgorithm.GZ)
      return "GZ";
    if (code == V3CompressionAlgorithm.Z)
      return "Z";
    if (code == V3CompressionAlgorithm.Z7)
      return "Z7";
    if (code == V3CompressionAlgorithm.ZL)
      return "ZL";
    return "?";
  }

    public String toSystem(V3CompressionAlgorithm code) {
      return code.getSystem();
      }

}

