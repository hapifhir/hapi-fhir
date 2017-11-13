package org.hl7.fhir.r4.utils.transform.deserializer;

/**
 * Describes the Inputs for Fhir StructureMaps
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */
public enum FhirMapInputModes {
  /**
   * Fhir mapper
   * Unset value
   */
  NotSet,
  /**
   * Source mode
   */
  Source,
  /**
   * Target mode
   */
  Target;

  /**
   * gets the string representation of the current enumerated value
   *
   * @return lowercase string representation
   */
  public String getValue() {
    switch (this) {
      case Source:
        return "source";
      case Target:
        return "target";
      default:
        return "?";
    }
  }
}
