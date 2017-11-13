package org.hl7.fhir.r4.utils.transform.deserializer;


import java.util.Objects;

/**
 * Defines the Useage types when declaring the strcture definitions in the mapping language
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */

public enum FhirMapUseNames {
  /**
   * 'source'
   */
  NotSet,

  /**
   * 'source'
   */
  Source,

  /**
   * 'target'
   */
  Target,

  /**
   * 'queried'
   */
  Queried,

  /**
   * 'produced'
   */
  Produced;

  /**
   * For procuring a string value of one of the enumerated values.
   *
   * @return lowercase string representation of the enumerated value.
   */
  public String getValue() {
    switch (this) {
      case Source:
        return "source";
      case Queried:
        return "queried";
      case Target:
        return "target";
      case Produced:
        return "produced";
      default:
        return "?";
    }
  }

  /**
   * Get the enumerated value based on the position in the enumeration
   *
   * @param value integer value of the desired enumerated value
   * @return the enumerated value
   * @throws Exception if the value is out of bounds or invalid.
   */
  public static FhirMapUseNames forValue(int value) throws Exception {
    return values()[value];
  }

  /**
   * Processes a string and returns the corresponding enumerated value based on the inputs
   *
   * @param value string representation
   * @return the corresponding enumerated value
   */
  public static FhirMapUseNames forValue(String value) {
    if (Objects.equals(value.toLowerCase(), "source")) {
      return FhirMapUseNames.Source;
    } else if (Objects.equals(value.toLowerCase(), "target")) {
      return FhirMapUseNames.Target;
    } else if (Objects.equals(value.toLowerCase(), "queried")) {
      return FhirMapUseNames.Queried;
    } else if (Objects.equals(value.toLowerCase(), "produced")) {
      return FhirMapUseNames.Produced;
    } else {
      return FhirMapUseNames.NotSet;
    }
  }
}
