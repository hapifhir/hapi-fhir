package org.hl7.fhir.r4.utils.transform.deserializer;

import java.util.HashMap;

/**
 * Concept mapping operators.
 *
 * @author Travis Lukach
 * (c) Applicadia LLC.
 */
public enum FhirMapConceptMapOperators {
  /**
   * Unset value
   */
  NotSet(0),

  /**
   * '<='
   */
  LessThanOrEqual(1),

  /**
   * '='
   */
  Equal(2),

  /**
   * '=='
   */
  DoubleEqual(3),

  /**
   * '!='
   */
  NotEqual(4),

  /**
   * '>='
   */
  GreaterThanOrEqual(5),

  /**
   * '>-'
   */
  GreaterThanMinus(6),

  /**
   * '<-'
   */
  LessThanMinus(7),

  /**
   * '~'
   */
  Tilde(8);

  /**
   * Constant int value of the size of the Integer object
   */
  public static final int SIZE = java.lang.Integer.SIZE;

  private int intValue;
  private static HashMap<Integer, FhirMapConceptMapOperators> mappings;

  private static HashMap<Integer, FhirMapConceptMapOperators> getMappings() {
    if (mappings == null) {
      synchronized (FhirMapConceptMapOperators.class) {
        if (mappings == null) {
          mappings = new java.util.HashMap<>();
        }
      }
    }
    return mappings;
  }

  FhirMapConceptMapOperators(int value) {
    intValue = value;
    getMappings().put(value, this);
  }

  public int getValue() {
    return intValue;
  }

  public static FhirMapConceptMapOperators forValue(int value) {
    return getMappings().get(value);
  }
}
