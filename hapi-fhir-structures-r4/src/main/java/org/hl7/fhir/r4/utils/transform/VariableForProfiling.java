package org.hl7.fhir.r4.utils.transform;

public class VariableForProfiling {
  /**
   * mode of Variable
   */
  private VariableMode mode;
  /**
   * Name of variable
   */
  private String name;
  /**
   * Property of the variable
   */
  private PropertyWithType property;

  /**
   * Constructor
   *
   * @param mode     initial mode
   * @param name     initial name
   * @param property initial property
   */
  public VariableForProfiling(VariableMode mode, String name, PropertyWithType property) {
    super();
    this.mode = mode;
    this.name = name;
    this.property = property;
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

  /**
   * get accessor for property
   *
   * @return property value
   */
  public PropertyWithType getProperty() {
    return property;
  }

  /**
   * creates a concatenated string containing the name and property summary
   *
   * @return concatenated string
   */
  public String summary() {
    return name + ": " + property.summary();
  }
}
