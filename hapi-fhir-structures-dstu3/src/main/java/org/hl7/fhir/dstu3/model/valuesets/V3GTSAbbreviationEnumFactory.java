package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3GTSAbbreviationEnumFactory implements EnumFactory<V3GTSAbbreviation> {

  public V3GTSAbbreviation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AM".equals(codeString))
      return V3GTSAbbreviation.AM;
    if ("BID".equals(codeString))
      return V3GTSAbbreviation.BID;
    if ("JB".equals(codeString))
      return V3GTSAbbreviation.JB;
    if ("JE".equals(codeString))
      return V3GTSAbbreviation.JE;
    if ("JH".equals(codeString))
      return V3GTSAbbreviation.JH;
    if ("_GTSAbbreviationHolidaysChristianRoman".equals(codeString))
      return V3GTSAbbreviation._GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN;
    if ("JHCHREAS".equals(codeString))
      return V3GTSAbbreviation.JHCHREAS;
    if ("JHCHRGFR".equals(codeString))
      return V3GTSAbbreviation.JHCHRGFR;
    if ("JHCHRNEW".equals(codeString))
      return V3GTSAbbreviation.JHCHRNEW;
    if ("JHCHRPEN".equals(codeString))
      return V3GTSAbbreviation.JHCHRPEN;
    if ("JHCHRXME".equals(codeString))
      return V3GTSAbbreviation.JHCHRXME;
    if ("JHCHRXMS".equals(codeString))
      return V3GTSAbbreviation.JHCHRXMS;
    if ("JHNNL".equals(codeString))
      return V3GTSAbbreviation.JHNNL;
    if ("JHNNLLD".equals(codeString))
      return V3GTSAbbreviation.JHNNLLD;
    if ("JHNNLQD".equals(codeString))
      return V3GTSAbbreviation.JHNNLQD;
    if ("JHNNLSK".equals(codeString))
      return V3GTSAbbreviation.JHNNLSK;
    if ("JHNUS".equals(codeString))
      return V3GTSAbbreviation.JHNUS;
    if ("JHNUSCLM".equals(codeString))
      return V3GTSAbbreviation.JHNUSCLM;
    if ("JHNUSIND".equals(codeString))
      return V3GTSAbbreviation.JHNUSIND;
    if ("JHNUSIND1".equals(codeString))
      return V3GTSAbbreviation.JHNUSIND1;
    if ("JHNUSIND5".equals(codeString))
      return V3GTSAbbreviation.JHNUSIND5;
    if ("JHNUSLBR".equals(codeString))
      return V3GTSAbbreviation.JHNUSLBR;
    if ("JHNUSMEM".equals(codeString))
      return V3GTSAbbreviation.JHNUSMEM;
    if ("JHNUSMEM5".equals(codeString))
      return V3GTSAbbreviation.JHNUSMEM5;
    if ("JHNUSMEM6".equals(codeString))
      return V3GTSAbbreviation.JHNUSMEM6;
    if ("JHNUSMLK".equals(codeString))
      return V3GTSAbbreviation.JHNUSMLK;
    if ("JHNUSPRE".equals(codeString))
      return V3GTSAbbreviation.JHNUSPRE;
    if ("JHNUSTKS".equals(codeString))
      return V3GTSAbbreviation.JHNUSTKS;
    if ("JHNUSTKS5".equals(codeString))
      return V3GTSAbbreviation.JHNUSTKS5;
    if ("JHNUSVET".equals(codeString))
      return V3GTSAbbreviation.JHNUSVET;
    if ("PM".equals(codeString))
      return V3GTSAbbreviation.PM;
    if ("QID".equals(codeString))
      return V3GTSAbbreviation.QID;
    if ("TID".equals(codeString))
      return V3GTSAbbreviation.TID;
    throw new IllegalArgumentException("Unknown V3GTSAbbreviation code '"+codeString+"'");
  }

  public String toCode(V3GTSAbbreviation code) {
    if (code == V3GTSAbbreviation.AM)
      return "AM";
    if (code == V3GTSAbbreviation.BID)
      return "BID";
    if (code == V3GTSAbbreviation.JB)
      return "JB";
    if (code == V3GTSAbbreviation.JE)
      return "JE";
    if (code == V3GTSAbbreviation.JH)
      return "JH";
    if (code == V3GTSAbbreviation._GTSABBREVIATIONHOLIDAYSCHRISTIANROMAN)
      return "_GTSAbbreviationHolidaysChristianRoman";
    if (code == V3GTSAbbreviation.JHCHREAS)
      return "JHCHREAS";
    if (code == V3GTSAbbreviation.JHCHRGFR)
      return "JHCHRGFR";
    if (code == V3GTSAbbreviation.JHCHRNEW)
      return "JHCHRNEW";
    if (code == V3GTSAbbreviation.JHCHRPEN)
      return "JHCHRPEN";
    if (code == V3GTSAbbreviation.JHCHRXME)
      return "JHCHRXME";
    if (code == V3GTSAbbreviation.JHCHRXMS)
      return "JHCHRXMS";
    if (code == V3GTSAbbreviation.JHNNL)
      return "JHNNL";
    if (code == V3GTSAbbreviation.JHNNLLD)
      return "JHNNLLD";
    if (code == V3GTSAbbreviation.JHNNLQD)
      return "JHNNLQD";
    if (code == V3GTSAbbreviation.JHNNLSK)
      return "JHNNLSK";
    if (code == V3GTSAbbreviation.JHNUS)
      return "JHNUS";
    if (code == V3GTSAbbreviation.JHNUSCLM)
      return "JHNUSCLM";
    if (code == V3GTSAbbreviation.JHNUSIND)
      return "JHNUSIND";
    if (code == V3GTSAbbreviation.JHNUSIND1)
      return "JHNUSIND1";
    if (code == V3GTSAbbreviation.JHNUSIND5)
      return "JHNUSIND5";
    if (code == V3GTSAbbreviation.JHNUSLBR)
      return "JHNUSLBR";
    if (code == V3GTSAbbreviation.JHNUSMEM)
      return "JHNUSMEM";
    if (code == V3GTSAbbreviation.JHNUSMEM5)
      return "JHNUSMEM5";
    if (code == V3GTSAbbreviation.JHNUSMEM6)
      return "JHNUSMEM6";
    if (code == V3GTSAbbreviation.JHNUSMLK)
      return "JHNUSMLK";
    if (code == V3GTSAbbreviation.JHNUSPRE)
      return "JHNUSPRE";
    if (code == V3GTSAbbreviation.JHNUSTKS)
      return "JHNUSTKS";
    if (code == V3GTSAbbreviation.JHNUSTKS5)
      return "JHNUSTKS5";
    if (code == V3GTSAbbreviation.JHNUSVET)
      return "JHNUSVET";
    if (code == V3GTSAbbreviation.PM)
      return "PM";
    if (code == V3GTSAbbreviation.QID)
      return "QID";
    if (code == V3GTSAbbreviation.TID)
      return "TID";
    return "?";
  }

    public String toSystem(V3GTSAbbreviation code) {
      return code.getSystem();
      }

}

