package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3TimingEventEnumFactory implements EnumFactory<V3TimingEvent> {

  public V3TimingEvent fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AC".equals(codeString))
      return V3TimingEvent.AC;
    if ("ACD".equals(codeString))
      return V3TimingEvent.ACD;
    if ("ACM".equals(codeString))
      return V3TimingEvent.ACM;
    if ("ACV".equals(codeString))
      return V3TimingEvent.ACV;
    if ("C".equals(codeString))
      return V3TimingEvent.C;
    if ("CD".equals(codeString))
      return V3TimingEvent.CD;
    if ("CM".equals(codeString))
      return V3TimingEvent.CM;
    if ("CV".equals(codeString))
      return V3TimingEvent.CV;
    if ("HS".equals(codeString))
      return V3TimingEvent.HS;
    if ("IC".equals(codeString))
      return V3TimingEvent.IC;
    if ("ICD".equals(codeString))
      return V3TimingEvent.ICD;
    if ("ICM".equals(codeString))
      return V3TimingEvent.ICM;
    if ("ICV".equals(codeString))
      return V3TimingEvent.ICV;
    if ("PC".equals(codeString))
      return V3TimingEvent.PC;
    if ("PCD".equals(codeString))
      return V3TimingEvent.PCD;
    if ("PCM".equals(codeString))
      return V3TimingEvent.PCM;
    if ("PCV".equals(codeString))
      return V3TimingEvent.PCV;
    if ("WAKE".equals(codeString))
      return V3TimingEvent.WAKE;
    throw new IllegalArgumentException("Unknown V3TimingEvent code '"+codeString+"'");
  }

  public String toCode(V3TimingEvent code) {
    if (code == V3TimingEvent.AC)
      return "AC";
    if (code == V3TimingEvent.ACD)
      return "ACD";
    if (code == V3TimingEvent.ACM)
      return "ACM";
    if (code == V3TimingEvent.ACV)
      return "ACV";
    if (code == V3TimingEvent.C)
      return "C";
    if (code == V3TimingEvent.CD)
      return "CD";
    if (code == V3TimingEvent.CM)
      return "CM";
    if (code == V3TimingEvent.CV)
      return "CV";
    if (code == V3TimingEvent.HS)
      return "HS";
    if (code == V3TimingEvent.IC)
      return "IC";
    if (code == V3TimingEvent.ICD)
      return "ICD";
    if (code == V3TimingEvent.ICM)
      return "ICM";
    if (code == V3TimingEvent.ICV)
      return "ICV";
    if (code == V3TimingEvent.PC)
      return "PC";
    if (code == V3TimingEvent.PCD)
      return "PCD";
    if (code == V3TimingEvent.PCM)
      return "PCM";
    if (code == V3TimingEvent.PCV)
      return "PCV";
    if (code == V3TimingEvent.WAKE)
      return "WAKE";
    return "?";
  }

    public String toSystem(V3TimingEvent code) {
      return code.getSystem();
      }

}

