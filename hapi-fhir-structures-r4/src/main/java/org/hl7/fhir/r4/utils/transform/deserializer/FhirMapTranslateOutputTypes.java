package org.hl7.fhir.r4.utils.transform.deserializer;

/**
 * Defines the Translate outputs when using a translate transform
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */

public enum FhirMapTranslateOutputTypes {
  /**
   * FhirMapTranslateOutputTypes.Code
   */
  Code,

  /**
   * FhirMapTranslateOutputTypes.System,
   */
  System,

  /**
   * FhirMapTranslateOutputTypes.Display
   */
  Display,

  /**
   * FhirMapTranslateOutputTypes.Coding
   */
  Coding,

  /**
   * FhirMapTranslateOutputTypes.CodeableConcept
   */
  CodeableConcept;

  /**
   * Returns a string representation of the enumerated value
   *
   * @return the desired string value based on the enumeration
   */
  public String getValue() {
    switch (this) {
      case Code:
        return "Code";
      case Coding:
        return "Coding";
      case System:
        return "System";
      case Display:
        return "Display";
      case CodeableConcept:
        return "CodeableConcept";
      default:
        return "?";
    }
  }

  @SuppressWarnings("unused")
  public static FhirMapTranslateOutputTypes forValue(int value) {
    return values()[value];
  }
}
