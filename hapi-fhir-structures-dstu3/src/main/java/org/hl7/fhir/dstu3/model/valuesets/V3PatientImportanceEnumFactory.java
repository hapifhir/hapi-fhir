package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3PatientImportanceEnumFactory implements EnumFactory<V3PatientImportance> {

  public V3PatientImportance fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("BM".equals(codeString))
      return V3PatientImportance.BM;
    if ("DFM".equals(codeString))
      return V3PatientImportance.DFM;
    if ("DR".equals(codeString))
      return V3PatientImportance.DR;
    if ("FD".equals(codeString))
      return V3PatientImportance.FD;
    if ("FOR".equals(codeString))
      return V3PatientImportance.FOR;
    if ("GOVT".equals(codeString))
      return V3PatientImportance.GOVT;
    if ("SFM".equals(codeString))
      return V3PatientImportance.SFM;
    if ("STF".equals(codeString))
      return V3PatientImportance.STF;
    if ("VIP".equals(codeString))
      return V3PatientImportance.VIP;
    throw new IllegalArgumentException("Unknown V3PatientImportance code '"+codeString+"'");
  }

  public String toCode(V3PatientImportance code) {
    if (code == V3PatientImportance.BM)
      return "BM";
    if (code == V3PatientImportance.DFM)
      return "DFM";
    if (code == V3PatientImportance.DR)
      return "DR";
    if (code == V3PatientImportance.FD)
      return "FD";
    if (code == V3PatientImportance.FOR)
      return "FOR";
    if (code == V3PatientImportance.GOVT)
      return "GOVT";
    if (code == V3PatientImportance.SFM)
      return "SFM";
    if (code == V3PatientImportance.STF)
      return "STF";
    if (code == V3PatientImportance.VIP)
      return "VIP";
    return "?";
  }

    public String toSystem(V3PatientImportance code) {
      return code.getSystem();
      }

}

