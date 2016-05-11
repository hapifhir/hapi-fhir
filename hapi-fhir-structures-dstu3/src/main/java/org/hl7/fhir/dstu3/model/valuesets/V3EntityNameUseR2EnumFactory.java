package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityNameUseR2EnumFactory implements EnumFactory<V3EntityNameUseR2> {

  public V3EntityNameUseR2 fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Assumed".equals(codeString))
      return V3EntityNameUseR2.ASSUMED;
    if ("A".equals(codeString))
      return V3EntityNameUseR2.A;
    if ("ANON".equals(codeString))
      return V3EntityNameUseR2.ANON;
    if ("I".equals(codeString))
      return V3EntityNameUseR2.I;
    if ("P".equals(codeString))
      return V3EntityNameUseR2.P;
    if ("R".equals(codeString))
      return V3EntityNameUseR2.R;
    if ("C".equals(codeString))
      return V3EntityNameUseR2.C;
    if ("M".equals(codeString))
      return V3EntityNameUseR2.M;
    if ("NameRepresentationUse".equals(codeString))
      return V3EntityNameUseR2.NAMEREPRESENTATIONUSE;
    if ("ABC".equals(codeString))
      return V3EntityNameUseR2.ABC;
    if ("IDE".equals(codeString))
      return V3EntityNameUseR2.IDE;
    if ("SYL".equals(codeString))
      return V3EntityNameUseR2.SYL;
    if ("OLD".equals(codeString))
      return V3EntityNameUseR2.OLD;
    if ("DN".equals(codeString))
      return V3EntityNameUseR2.DN;
    if ("OR".equals(codeString))
      return V3EntityNameUseR2.OR;
    if ("PHON".equals(codeString))
      return V3EntityNameUseR2.PHON;
    if ("SRCH".equals(codeString))
      return V3EntityNameUseR2.SRCH;
    if ("T".equals(codeString))
      return V3EntityNameUseR2.T;
    throw new IllegalArgumentException("Unknown V3EntityNameUseR2 code '"+codeString+"'");
  }

  public String toCode(V3EntityNameUseR2 code) {
    if (code == V3EntityNameUseR2.ASSUMED)
      return "Assumed";
    if (code == V3EntityNameUseR2.A)
      return "A";
    if (code == V3EntityNameUseR2.ANON)
      return "ANON";
    if (code == V3EntityNameUseR2.I)
      return "I";
    if (code == V3EntityNameUseR2.P)
      return "P";
    if (code == V3EntityNameUseR2.R)
      return "R";
    if (code == V3EntityNameUseR2.C)
      return "C";
    if (code == V3EntityNameUseR2.M)
      return "M";
    if (code == V3EntityNameUseR2.NAMEREPRESENTATIONUSE)
      return "NameRepresentationUse";
    if (code == V3EntityNameUseR2.ABC)
      return "ABC";
    if (code == V3EntityNameUseR2.IDE)
      return "IDE";
    if (code == V3EntityNameUseR2.SYL)
      return "SYL";
    if (code == V3EntityNameUseR2.OLD)
      return "OLD";
    if (code == V3EntityNameUseR2.DN)
      return "DN";
    if (code == V3EntityNameUseR2.OR)
      return "OR";
    if (code == V3EntityNameUseR2.PHON)
      return "PHON";
    if (code == V3EntityNameUseR2.SRCH)
      return "SRCH";
    if (code == V3EntityNameUseR2.T)
      return "T";
    return "?";
  }

    public String toSystem(V3EntityNameUseR2 code) {
      return code.getSystem();
      }

}

