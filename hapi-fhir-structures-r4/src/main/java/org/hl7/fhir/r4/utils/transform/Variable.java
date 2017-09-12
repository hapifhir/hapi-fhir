package org.hl7.fhir.r4.utils.transform;

import org.hl7.fhir.r4.model.Base;

public class Variable {
  private VariableMode mode;
  private String name;
  private Base object;

  public Variable(VariableMode mode, String name, Base object) {
    super();
    this.mode = mode;
    this.name = name;
    this.object = object;
  }

  public VariableMode getMode() {
    return mode;
  }

  public String getName() {
    return name;
  }

  public Base getObject() {
    return object;
  }

  public String summary() {
    return name + ": " + object.fhirType();
  }
}
