package org.hl7.fhir.utilities.graphql;

public abstract class Value {
  public abstract void write(StringBuilder b, int indent) throws EGraphEngine, EGraphQLException;

  public boolean isValue(String v) {
    return false;
  }

  public abstract String getValue();
}
