package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;

public class Field {
  private String name;
  private List<Selection> selectionSet = new ArrayList<Selection>();
  private String alias;
  private List<Argument> arguments = new ArrayList<Argument>();
  private List<Directive> directives = new ArrayList<Directive>();

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public String getAlias() {
    return alias;
  }


  public void setAlias(String alias) {
    this.alias = alias;
  }


  public List<Selection> getSelectionSet() {
    return selectionSet;
  }


  public List<Argument> getArguments() {
    return arguments;
  }


  public List<Directive> getDirectives() {
    return directives;
  }


  public Argument argument(String name) {
    for (Argument p : arguments) {
      if (p.name.equals(name))
        return p;
    }
    return null;
  }


  public boolean hasDirective(String name) {
    for (Directive d : directives)
      if (d.getName().equals(name))
        return true;
    return false;
  }

  public Directive directive(String name) {
    for (Directive d : directives)
      if (d.getName().equals(name))
        return d;
    return null;
  }
}