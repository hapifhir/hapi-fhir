package org.hl7.fhir.r4.utils.transform;

public class VariableForProfiling {
  private VariableMode mode;
  private String name;
  private PropertyWithType property;

  public VariableForProfiling(VariableMode mode, String name, PropertyWithType property) {
    super();
    this.mode = mode;
    this.name = name;
    this.property = property;
  }

  public VariableMode getMode() {
    return mode;
  }

  public String getName() {
    return name;
  }

  public PropertyWithType getProperty() {
    return property;
  }

  public String summary() {
    return name + ": " + property.summary();
  }
}
