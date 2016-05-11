package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityNameUseEnumFactory implements EnumFactory<V3EntityNameUse> {

  public V3EntityNameUse fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_NameRepresentationUse".equals(codeString))
      return V3EntityNameUse._NAMEREPRESENTATIONUSE;
    if ("ABC".equals(codeString))
      return V3EntityNameUse.ABC;
    if ("IDE".equals(codeString))
      return V3EntityNameUse.IDE;
    if ("SYL".equals(codeString))
      return V3EntityNameUse.SYL;
    if ("ASGN".equals(codeString))
      return V3EntityNameUse.ASGN;
    if ("C".equals(codeString))
      return V3EntityNameUse.C;
    if ("I".equals(codeString))
      return V3EntityNameUse.I;
    if ("L".equals(codeString))
      return V3EntityNameUse.L;
    if ("OR".equals(codeString))
      return V3EntityNameUse.OR;
    if ("P".equals(codeString))
      return V3EntityNameUse.P;
    if ("A".equals(codeString))
      return V3EntityNameUse.A;
    if ("R".equals(codeString))
      return V3EntityNameUse.R;
    if ("SRCH".equals(codeString))
      return V3EntityNameUse.SRCH;
    if ("PHON".equals(codeString))
      return V3EntityNameUse.PHON;
    if ("SNDX".equals(codeString))
      return V3EntityNameUse.SNDX;
    throw new IllegalArgumentException("Unknown V3EntityNameUse code '"+codeString+"'");
  }

  public String toCode(V3EntityNameUse code) {
    if (code == V3EntityNameUse._NAMEREPRESENTATIONUSE)
      return "_NameRepresentationUse";
    if (code == V3EntityNameUse.ABC)
      return "ABC";
    if (code == V3EntityNameUse.IDE)
      return "IDE";
    if (code == V3EntityNameUse.SYL)
      return "SYL";
    if (code == V3EntityNameUse.ASGN)
      return "ASGN";
    if (code == V3EntityNameUse.C)
      return "C";
    if (code == V3EntityNameUse.I)
      return "I";
    if (code == V3EntityNameUse.L)
      return "L";
    if (code == V3EntityNameUse.OR)
      return "OR";
    if (code == V3EntityNameUse.P)
      return "P";
    if (code == V3EntityNameUse.A)
      return "A";
    if (code == V3EntityNameUse.R)
      return "R";
    if (code == V3EntityNameUse.SRCH)
      return "SRCH";
    if (code == V3EntityNameUse.PHON)
      return "PHON";
    if (code == V3EntityNameUse.SNDX)
      return "SNDX";
    return "?";
  }

    public String toSystem(V3EntityNameUse code) {
      return code.getSystem();
      }

}

