package org.hl7.fhir.r4.model;

public interface ICoding {

  public String getSystem();
  public boolean hasSystem();
  
  public String getVersion();
  public boolean hasVersion();
  public boolean supportsVersion();
  
  public String getCode();
  public boolean hasCode();
  
  public String getDisplay();
  public boolean hasDisplay();
  public boolean supportsDisplay();
  
}
