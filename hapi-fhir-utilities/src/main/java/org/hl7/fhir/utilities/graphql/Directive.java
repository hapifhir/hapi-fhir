package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;

public class Directive {
  private String name;
  private List<Argument> arguments = new ArrayList<Argument>();

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public List<Argument> getArguments() {
    return arguments;
  }
}