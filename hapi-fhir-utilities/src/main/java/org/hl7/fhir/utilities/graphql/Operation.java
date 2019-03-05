package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;

public class  Operation {
  public enum OperationType {qglotQuery, qglotMutation}
  
  private String name;
  private Operation.OperationType operationType;
  private List<Selection> selectionSet = new ArrayList<Selection>();
  private List<Variable> variables = new ArrayList<Variable>();
  private List<Directive> directives = new ArrayList<Directive>();
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Operation.OperationType getOperationType() {
    return operationType;
  }
  public void setOperationType(Operation.OperationType operationType) {
    this.operationType = operationType;
  }
  public List<Selection> getSelectionSet() {
    return selectionSet;
  }
  public List<Variable> getVariables() {
    return variables;
  }
  public List<Directive> getDirectives() {
    return directives;
  }

}