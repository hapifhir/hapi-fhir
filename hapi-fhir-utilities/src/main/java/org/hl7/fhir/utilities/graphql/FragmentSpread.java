package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;

public class FragmentSpread {    
  private String name;
  private List<Directive> directives = new ArrayList<Directive>();
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public List<Directive> getDirectives() {
    return directives;
  }
}