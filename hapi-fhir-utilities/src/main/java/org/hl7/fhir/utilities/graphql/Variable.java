package org.hl7.fhir.utilities.graphql;

public class Variable {
  private String name;
  private Value defaultValue;
  private String typeName;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Value getDefaultValue() {
    return defaultValue;
  }
  public void setDefaultValue(Value defaultValue) {
    this.defaultValue = defaultValue;
  }
  public String getTypeName() {
    return typeName;
  }
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

}