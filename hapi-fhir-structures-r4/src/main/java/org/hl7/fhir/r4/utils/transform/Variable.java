package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.Base;

public class Variable {
  /**
   * Mode of variable
   */
  private VariableMode mode;
  /**
   * Name of variable
   */
  private String name;
  /**
   * Type of variable based on Fhir Model objects
   */
  private Base object;

  /**
   * Constructor
   *
   * @param mode   initial Variable Mode
   * @param name   initial Variable name
   * @param object initial Object
   */
  public Variable(VariableMode mode, String name, Base object) {
    super();
    this.mode = mode;
    this.name = name;
    this.object = object;
  }

  /**
   * get accessor for mode
   *
   * @return mode value
   */
  public VariableMode getMode() {
    return mode;
  }

  /**
   * get accessor for name
   *
   * @return name value
   */
  public String getName() {
    return name;
  }

  /***
   * get accessor for object
   * @return object value
   */
  public Base getObject() {
    return object;
  }

  /**
   * Gets a string summary of the variable
   *
   * @return concatenated string of the variable properties
   */
  public String summary() {
    return name + ": " + object.fhirType();
  }
}
