package org.hl7.fhir.utilities.graphql;

public class VariableValue extends Value {
  private String value;

  public VariableValue(java.lang.String value) {
    super();
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void write(StringBuilder b, int indent) throws EGraphEngine {
    throw new EGraphEngine("Cannot write a variable to JSON");
  }

  public String toString() {
    return value;
  }
}