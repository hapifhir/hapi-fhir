package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;

public class Fragment {      
  String name;
  private String typeCondition;
  private List<Selection> selectionSet = new ArrayList<Selection>();
  private List<Directive> directives = new ArrayList<Directive>();
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getTypeCondition() {
    return typeCondition;
  }
  public void setTypeCondition(String typeCondition) {
    this.typeCondition = typeCondition;
  }
  public List<Selection> getSelectionSet() {
    return selectionSet;
  }
  public List<Directive> getDirectives() {
    return directives;
  }

}