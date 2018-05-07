package org.hl7.fhir.r4.model;

import java.util.List;

public interface Request {

  public List<Identifier> getRequestIdentifier();
  public boolean hasRequestIdentifier();
  public int getRequestIdentifierMin();
  public int getRequestIdentifierMax(); // 0 means that it is not implemented on this resource (or not able to be generated
  
  public List<Reference> getRequestDefinition();
  public boolean hasRequestDefinition();
  public int getRequestDefinitionMin();
  public int getRequestDefinitionMax(); // 0 means that it is not implemented on this resource (or not able to be generated
  
}
