package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class VaccinationProtocolDoseStatusReasonEnumFactory implements EnumFactory<VaccinationProtocolDoseStatusReason> {

  public VaccinationProtocolDoseStatusReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("advstorage".equals(codeString))
      return VaccinationProtocolDoseStatusReason.ADVSTORAGE;
    if ("coldchbrk".equals(codeString))
      return VaccinationProtocolDoseStatusReason.COLDCHBRK;
    if ("explot".equals(codeString))
      return VaccinationProtocolDoseStatusReason.EXPLOT;
    if ("outsidesched".equals(codeString))
      return VaccinationProtocolDoseStatusReason.OUTSIDESCHED;
    if ("prodrecall".equals(codeString))
      return VaccinationProtocolDoseStatusReason.PRODRECALL;
    throw new IllegalArgumentException("Unknown VaccinationProtocolDoseStatusReason code '"+codeString+"'");
  }

  public String toCode(VaccinationProtocolDoseStatusReason code) {
    if (code == VaccinationProtocolDoseStatusReason.ADVSTORAGE)
      return "advstorage";
    if (code == VaccinationProtocolDoseStatusReason.COLDCHBRK)
      return "coldchbrk";
    if (code == VaccinationProtocolDoseStatusReason.EXPLOT)
      return "explot";
    if (code == VaccinationProtocolDoseStatusReason.OUTSIDESCHED)
      return "outsidesched";
    if (code == VaccinationProtocolDoseStatusReason.PRODRECALL)
      return "prodrecall";
    return "?";
  }

    public String toSystem(VaccinationProtocolDoseStatusReason code) {
      return code.getSystem();
      }

}

