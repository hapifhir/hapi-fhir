package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class VaccinationProtocolDoseStatusEnumFactory implements EnumFactory<VaccinationProtocolDoseStatus> {

  public VaccinationProtocolDoseStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("count".equals(codeString))
      return VaccinationProtocolDoseStatus.COUNT;
    if ("nocount".equals(codeString))
      return VaccinationProtocolDoseStatus.NOCOUNT;
    throw new IllegalArgumentException("Unknown VaccinationProtocolDoseStatus code '"+codeString+"'");
  }

  public String toCode(VaccinationProtocolDoseStatus code) {
    if (code == VaccinationProtocolDoseStatus.COUNT)
      return "count";
    if (code == VaccinationProtocolDoseStatus.NOCOUNT)
      return "nocount";
    return "?";
  }

    public String toSystem(VaccinationProtocolDoseStatus code) {
      return code.getSystem();
      }

}

