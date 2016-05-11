package org.hl7.fhir.instance.utilities;

/**
 * Encapsulates StringBuilder to build strings of values separated by comma
 * @author Ewout
 */

public class CommaSeparatedStringBuilder {

  boolean first = true;
  StringBuilder b = new StringBuilder();

  public void append(String value) {
    if (!first)
      b.append(", ");
    b.append(value);
    first = false;    
    
  }
  
  @Override
  public String toString() {
    return b.toString();
  }
}
