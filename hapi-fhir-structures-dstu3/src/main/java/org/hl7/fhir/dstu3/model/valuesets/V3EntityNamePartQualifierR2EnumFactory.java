package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityNamePartQualifierR2EnumFactory implements EnumFactory<V3EntityNamePartQualifierR2> {

  public V3EntityNamePartQualifierR2 fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AD".equals(codeString))
      return V3EntityNamePartQualifierR2.AD;
    if ("SP".equals(codeString))
      return V3EntityNamePartQualifierR2.SP;
    if ("BR".equals(codeString))
      return V3EntityNamePartQualifierR2.BR;
    if ("CL".equals(codeString))
      return V3EntityNamePartQualifierR2.CL;
    if ("IN".equals(codeString))
      return V3EntityNamePartQualifierR2.IN;
    if ("LS".equals(codeString))
      return V3EntityNamePartQualifierR2.LS;
    if ("MID".equals(codeString))
      return V3EntityNamePartQualifierR2.MID;
    if ("PFX".equals(codeString))
      return V3EntityNamePartQualifierR2.PFX;
    if ("PharmaceuticalEntityNamePartQualifiers".equals(codeString))
      return V3EntityNamePartQualifierR2.PHARMACEUTICALENTITYNAMEPARTQUALIFIERS;
    if ("CON".equals(codeString))
      return V3EntityNamePartQualifierR2.CON;
    if ("DEV".equals(codeString))
      return V3EntityNamePartQualifierR2.DEV;
    if ("FLAV".equals(codeString))
      return V3EntityNamePartQualifierR2.FLAV;
    if ("FORMUL".equals(codeString))
      return V3EntityNamePartQualifierR2.FORMUL;
    if ("FRM".equals(codeString))
      return V3EntityNamePartQualifierR2.FRM;
    if ("INV".equals(codeString))
      return V3EntityNamePartQualifierR2.INV;
    if ("POPUL".equals(codeString))
      return V3EntityNamePartQualifierR2.POPUL;
    if ("SCI".equals(codeString))
      return V3EntityNamePartQualifierR2.SCI;
    if ("STR".equals(codeString))
      return V3EntityNamePartQualifierR2.STR;
    if ("TIME".equals(codeString))
      return V3EntityNamePartQualifierR2.TIME;
    if ("TMK".equals(codeString))
      return V3EntityNamePartQualifierR2.TMK;
    if ("USE".equals(codeString))
      return V3EntityNamePartQualifierR2.USE;
    if ("SFX".equals(codeString))
      return V3EntityNamePartQualifierR2.SFX;
    if ("TitleStyles".equals(codeString))
      return V3EntityNamePartQualifierR2.TITLESTYLES;
    if ("AC".equals(codeString))
      return V3EntityNamePartQualifierR2.AC;
    if ("HON".equals(codeString))
      return V3EntityNamePartQualifierR2.HON;
    if ("NB".equals(codeString))
      return V3EntityNamePartQualifierR2.NB;
    if ("PR".equals(codeString))
      return V3EntityNamePartQualifierR2.PR;
    throw new IllegalArgumentException("Unknown V3EntityNamePartQualifierR2 code '"+codeString+"'");
  }

  public String toCode(V3EntityNamePartQualifierR2 code) {
    if (code == V3EntityNamePartQualifierR2.AD)
      return "AD";
    if (code == V3EntityNamePartQualifierR2.SP)
      return "SP";
    if (code == V3EntityNamePartQualifierR2.BR)
      return "BR";
    if (code == V3EntityNamePartQualifierR2.CL)
      return "CL";
    if (code == V3EntityNamePartQualifierR2.IN)
      return "IN";
    if (code == V3EntityNamePartQualifierR2.LS)
      return "LS";
    if (code == V3EntityNamePartQualifierR2.MID)
      return "MID";
    if (code == V3EntityNamePartQualifierR2.PFX)
      return "PFX";
    if (code == V3EntityNamePartQualifierR2.PHARMACEUTICALENTITYNAMEPARTQUALIFIERS)
      return "PharmaceuticalEntityNamePartQualifiers";
    if (code == V3EntityNamePartQualifierR2.CON)
      return "CON";
    if (code == V3EntityNamePartQualifierR2.DEV)
      return "DEV";
    if (code == V3EntityNamePartQualifierR2.FLAV)
      return "FLAV";
    if (code == V3EntityNamePartQualifierR2.FORMUL)
      return "FORMUL";
    if (code == V3EntityNamePartQualifierR2.FRM)
      return "FRM";
    if (code == V3EntityNamePartQualifierR2.INV)
      return "INV";
    if (code == V3EntityNamePartQualifierR2.POPUL)
      return "POPUL";
    if (code == V3EntityNamePartQualifierR2.SCI)
      return "SCI";
    if (code == V3EntityNamePartQualifierR2.STR)
      return "STR";
    if (code == V3EntityNamePartQualifierR2.TIME)
      return "TIME";
    if (code == V3EntityNamePartQualifierR2.TMK)
      return "TMK";
    if (code == V3EntityNamePartQualifierR2.USE)
      return "USE";
    if (code == V3EntityNamePartQualifierR2.SFX)
      return "SFX";
    if (code == V3EntityNamePartQualifierR2.TITLESTYLES)
      return "TitleStyles";
    if (code == V3EntityNamePartQualifierR2.AC)
      return "AC";
    if (code == V3EntityNamePartQualifierR2.HON)
      return "HON";
    if (code == V3EntityNamePartQualifierR2.NB)
      return "NB";
    if (code == V3EntityNamePartQualifierR2.PR)
      return "PR";
    return "?";
  }

    public String toSystem(V3EntityNamePartQualifierR2 code) {
      return code.getSystem();
      }

}

