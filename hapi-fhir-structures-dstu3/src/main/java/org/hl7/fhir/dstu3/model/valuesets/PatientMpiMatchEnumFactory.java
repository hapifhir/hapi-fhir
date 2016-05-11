package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class PatientMpiMatchEnumFactory implements EnumFactory<PatientMpiMatch> {

  public PatientMpiMatch fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("certain".equals(codeString))
      return PatientMpiMatch.CERTAIN;
    if ("probable".equals(codeString))
      return PatientMpiMatch.PROBABLE;
    if ("possible".equals(codeString))
      return PatientMpiMatch.POSSIBLE;
    if ("certainly-not".equals(codeString))
      return PatientMpiMatch.CERTAINLYNOT;
    throw new IllegalArgumentException("Unknown PatientMpiMatch code '"+codeString+"'");
  }

  public String toCode(PatientMpiMatch code) {
    if (code == PatientMpiMatch.CERTAIN)
      return "certain";
    if (code == PatientMpiMatch.PROBABLE)
      return "probable";
    if (code == PatientMpiMatch.POSSIBLE)
      return "possible";
    if (code == PatientMpiMatch.CERTAINLYNOT)
      return "certainly-not";
    return "?";
  }

    public String toSystem(PatientMpiMatch code) {
      return code.getSystem();
      }

}

