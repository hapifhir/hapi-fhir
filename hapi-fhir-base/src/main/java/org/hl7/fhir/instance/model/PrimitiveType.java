package org.hl7.fhir.instance.model;

public abstract class PrimitiveType extends Type {

  private static final long serialVersionUID = 3767314386514236407L;

	public abstract String asStringValue();
  
  public abstract boolean hasValue();  
}
