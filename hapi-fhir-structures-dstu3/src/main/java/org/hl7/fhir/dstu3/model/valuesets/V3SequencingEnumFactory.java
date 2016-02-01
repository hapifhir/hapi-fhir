package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3SequencingEnumFactory implements EnumFactory<V3Sequencing> {

  public V3Sequencing fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("A".equals(codeString))
      return V3Sequencing.A;
    if ("D".equals(codeString))
      return V3Sequencing.D;
    if ("N".equals(codeString))
      return V3Sequencing.N;
    throw new IllegalArgumentException("Unknown V3Sequencing code '"+codeString+"'");
  }

  public String toCode(V3Sequencing code) {
    if (code == V3Sequencing.A)
      return "A";
    if (code == V3Sequencing.D)
      return "D";
    if (code == V3Sequencing.N)
      return "N";
    return "?";
  }

    public String toSystem(V3Sequencing code) {
      return code.getSystem();
      }

}

