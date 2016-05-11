package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActMoodEnumFactory implements EnumFactory<V3ActMood> {

  public V3ActMood fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActMoodCompletionTrack".equals(codeString))
      return V3ActMood._ACTMOODCOMPLETIONTRACK;
    if ("_ActMoodPotential".equals(codeString))
      return V3ActMood._ACTMOODPOTENTIAL;
    if ("DEF".equals(codeString))
      return V3ActMood.DEF;
    if ("PERM".equals(codeString))
      return V3ActMood.PERM;
    if ("SLOT".equals(codeString))
      return V3ActMood.SLOT;
    if ("EVN".equals(codeString))
      return V3ActMood.EVN;
    if ("INT".equals(codeString))
      return V3ActMood.INT;
    if ("_ActMoodDesire".equals(codeString))
      return V3ActMood._ACTMOODDESIRE;
    if ("_ActMoodActRequest".equals(codeString))
      return V3ActMood._ACTMOODACTREQUEST;
    if ("ARQ".equals(codeString))
      return V3ActMood.ARQ;
    if ("PERMRQ".equals(codeString))
      return V3ActMood.PERMRQ;
    if ("RQO".equals(codeString))
      return V3ActMood.RQO;
    if ("PRP".equals(codeString))
      return V3ActMood.PRP;
    if ("RMD".equals(codeString))
      return V3ActMood.RMD;
    if ("PRMS".equals(codeString))
      return V3ActMood.PRMS;
    if ("APT".equals(codeString))
      return V3ActMood.APT;
    if ("_ActMoodPredicate".equals(codeString))
      return V3ActMood._ACTMOODPREDICATE;
    if ("CRT".equals(codeString))
      return V3ActMood.CRT;
    if ("EVN.CRT".equals(codeString))
      return V3ActMood.EVN_CRT;
    if ("GOL.CRT".equals(codeString))
      return V3ActMood.GOL_CRT;
    if ("INT.CRT".equals(codeString))
      return V3ActMood.INT_CRT;
    if ("PRMS.CRT".equals(codeString))
      return V3ActMood.PRMS_CRT;
    if ("RQO.CRT".equals(codeString))
      return V3ActMood.RQO_CRT;
    if ("RSK.CRT".equals(codeString))
      return V3ActMood.RSK_CRT;
    if ("EXPEC".equals(codeString))
      return V3ActMood.EXPEC;
    if ("GOL".equals(codeString))
      return V3ActMood.GOL;
    if ("RSK".equals(codeString))
      return V3ActMood.RSK;
    if ("OPT".equals(codeString))
      return V3ActMood.OPT;
    throw new IllegalArgumentException("Unknown V3ActMood code '"+codeString+"'");
  }

  public String toCode(V3ActMood code) {
    if (code == V3ActMood._ACTMOODCOMPLETIONTRACK)
      return "_ActMoodCompletionTrack";
    if (code == V3ActMood._ACTMOODPOTENTIAL)
      return "_ActMoodPotential";
    if (code == V3ActMood.DEF)
      return "DEF";
    if (code == V3ActMood.PERM)
      return "PERM";
    if (code == V3ActMood.SLOT)
      return "SLOT";
    if (code == V3ActMood.EVN)
      return "EVN";
    if (code == V3ActMood.INT)
      return "INT";
    if (code == V3ActMood._ACTMOODDESIRE)
      return "_ActMoodDesire";
    if (code == V3ActMood._ACTMOODACTREQUEST)
      return "_ActMoodActRequest";
    if (code == V3ActMood.ARQ)
      return "ARQ";
    if (code == V3ActMood.PERMRQ)
      return "PERMRQ";
    if (code == V3ActMood.RQO)
      return "RQO";
    if (code == V3ActMood.PRP)
      return "PRP";
    if (code == V3ActMood.RMD)
      return "RMD";
    if (code == V3ActMood.PRMS)
      return "PRMS";
    if (code == V3ActMood.APT)
      return "APT";
    if (code == V3ActMood._ACTMOODPREDICATE)
      return "_ActMoodPredicate";
    if (code == V3ActMood.CRT)
      return "CRT";
    if (code == V3ActMood.EVN_CRT)
      return "EVN.CRT";
    if (code == V3ActMood.GOL_CRT)
      return "GOL.CRT";
    if (code == V3ActMood.INT_CRT)
      return "INT.CRT";
    if (code == V3ActMood.PRMS_CRT)
      return "PRMS.CRT";
    if (code == V3ActMood.RQO_CRT)
      return "RQO.CRT";
    if (code == V3ActMood.RSK_CRT)
      return "RSK.CRT";
    if (code == V3ActMood.EXPEC)
      return "EXPEC";
    if (code == V3ActMood.GOL)
      return "GOL";
    if (code == V3ActMood.RSK)
      return "RSK";
    if (code == V3ActMood.OPT)
      return "OPT";
    return "?";
  }

    public String toSystem(V3ActMood code) {
      return code.getSystem();
      }

}

