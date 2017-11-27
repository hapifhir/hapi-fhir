package org.hl7.fhir.r4.utils.transform.deserializer;


/**
 * Enum for group types.
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */
public enum FhirMapGroupTypes {
  /**
   * Unset value
   */
  NotSet,

  /**
   * Group type, types
   */
  Types,

  /**
   * Group type, type types
   */
  TypeTypes;

  /**
   * Returns a lowercase string of the relevant values of FhirMapGroupTypes
   *
   * @return lowercase string representation
   */
  public String getValue() {
    switch (this) {
      case Types:
        return "types";
      case TypeTypes:
        return "type-and-types";
      default:
        return "?";
    }
  }

  /**
   * to get an enumerated value based on its position
   *
   * @param value numeric value desired
   * @return the enumeration based on position
   * @throws Exception if value is invalid or out of bounds
   */
  @SuppressWarnings("unused")
  public static FhirMapGroupTypes forValue(int value) throws Exception {
    return values()[value];
  }
}
