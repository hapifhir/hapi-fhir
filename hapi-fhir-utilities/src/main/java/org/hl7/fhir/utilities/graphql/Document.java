package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;

public class Document {
  private List<Fragment> fragments = new ArrayList<Fragment>();
  private List<Operation> operations = new ArrayList<Operation>();
  
  public List<Fragment> getFragments() {
    return fragments;
  }

  public List<Operation> getOperations() {
    return operations;
  }

  public Fragment fragment(String name) {
    for (Fragment f : fragments)
      if (f.name.equals(name))
        return f;
    return null;
  }

  public Operation operation(String name) {
    for (Operation  o : operations)
      if (o.getName().equals(name))
        return o;
    return null;
  }
}