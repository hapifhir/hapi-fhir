package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EncounterAdmissionSourceEnumFactory implements EnumFactory<V3EncounterAdmissionSource> {

  public V3EncounterAdmissionSource fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("E".equals(codeString))
      return V3EncounterAdmissionSource.E;
    if ("LD".equals(codeString))
      return V3EncounterAdmissionSource.LD;
    if ("NB".equals(codeString))
      return V3EncounterAdmissionSource.NB;
    throw new IllegalArgumentException("Unknown V3EncounterAdmissionSource code '"+codeString+"'");
  }

  public String toCode(V3EncounterAdmissionSource code) {
    if (code == V3EncounterAdmissionSource.E)
      return "E";
    if (code == V3EncounterAdmissionSource.LD)
      return "LD";
    if (code == V3EncounterAdmissionSource.NB)
      return "NB";
    return "?";
  }

    public String toSystem(V3EncounterAdmissionSource code) {
      return code.getSystem();
      }

}

