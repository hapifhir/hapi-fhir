package org.hl7.fhir.r4.model;

import org.hl7.fhir.r4.model.Enumerations.FHIRVersion;

/**
 * This enumreation is special, and hand crafted. It only supports a subset of the actual published FHIR versions, those that are still supported.
 * @author graha
 *
 */
public enum FhirPublication {
  NULL,
  DSTU1,
  DSTU2,
  DSTU2016May,
  STU3,
  R4;

  public static FhirPublication fromCode(String v) {
    if ("1.0.2".equals(v))
      return FhirPublication.DSTU2;
    if ("1.0".equals(v))
      return FhirPublication.DSTU2;
    if ("1.4.0".equals(v))
      return FhirPublication.DSTU2016May;
    if ("1.4".equals(v))
      return FhirPublication.DSTU2016May;
    if ("3.0.1".equals(v))
      return FhirPublication.STU3;
    if ("3.0".equals(v))
      return FhirPublication.STU3;
    if ("3.5.0".equals(v))
      return FhirPublication.R4;
    if ("4.0.0".equals(v))
      return FhirPublication.R4;
    if ("3.5".equals(v))
      return FhirPublication.R4;
    if ("4.0".equals(v))
      return FhirPublication.R4;
    if ("1.0.0".equals(v))
      return FhirPublication.R4; // hack workaround build problem
    return null;
  }

  public String toCode() {
    switch (this) {
    case DSTU1: return "0.01";
    case DSTU2: return "1.0.2";
    case DSTU2016May: return "1.4.0";
    case STU3: return "3.0.1";
    case R4: return Constants.VERSION;
    default:
      return "??";
    }
  }

  public static FhirPublication fromVersion(FHIRVersion v) {
    return fromCode(v.toCode());
  }


}
