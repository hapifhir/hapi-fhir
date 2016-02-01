package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class EncounterPriorityEnumFactory implements EnumFactory<EncounterPriority> {

  public EncounterPriority fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("imm".equals(codeString))
      return EncounterPriority.IMM;
    if ("emg".equals(codeString))
      return EncounterPriority.EMG;
    if ("urg".equals(codeString))
      return EncounterPriority.URG;
    if ("s-urg".equals(codeString))
      return EncounterPriority.SURG;
    if ("no-urg".equals(codeString))
      return EncounterPriority.NOURG;
    throw new IllegalArgumentException("Unknown EncounterPriority code '"+codeString+"'");
  }

  public String toCode(EncounterPriority code) {
    if (code == EncounterPriority.IMM)
      return "imm";
    if (code == EncounterPriority.EMG)
      return "emg";
    if (code == EncounterPriority.URG)
      return "urg";
    if (code == EncounterPriority.SURG)
      return "s-urg";
    if (code == EncounterPriority.NOURG)
      return "no-urg";
    return "?";
  }

    public String toSystem(EncounterPriority code) {
      return code.getSystem();
      }

}

