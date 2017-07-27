package org.hl7.fhir.utilities.graphql;

import java.util.ArrayList;
import java.util.List;

public class Package {
  private Document document;
  private String operationName;
  private List<Argument> variables = new ArrayList<Argument>();
  public Package(Document document) {
    super();
    this.document = document;
  }
  public Document getDocument() {
    return document;
  }
  public void setDocument(Document document) {
    this.document = document;
  }
  public String getOperationName() {
    return operationName;
  }
  public void setOperationName(String operationName) {
    this.operationName = operationName;
  }
  public List<Argument> getVariables() {
    return variables;
  }

}