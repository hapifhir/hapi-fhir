package org.hl7.fhir.utilities;

/**
 * Encapsulates StringBuilder to build strings of values separated by comma
 * @author Ewout
 */

public class CommaSeparatedStringBuilder {

  boolean first = true;
  String sep = ", ";
  StringBuilder b = new StringBuilder();

  public CommaSeparatedStringBuilder() {
  }
  
  public CommaSeparatedStringBuilder(String sep) {
    this.sep = sep;
  }

  public void append(String value) {
    if (!first)
      b.append(sep);
    b.append(value);
    first = false;    
    
  }
  
  public int length() {
    return b.length();
  }
  @Override
  public String toString() {
    return b.toString();
  }

  public void appendIfNotNull(String s) {
   if (!Utilities.noString(s))
     append(s);
    
  }
}
