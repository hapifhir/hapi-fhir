package org.hl7.fhir.utilities.graphql;

public class StringValue extends Value {
  private String value;

  public StringValue(java.lang.String value) {
    super();
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isValue(String v) {
    return v.equals(value);      
  }
  public String toString() {
    return value;
  }
  public void write(StringBuilder b, int indent) {
    b.append("\"");
    for (char ch : value.toCharArray()) {
      if (ch == '"') b.append("\"");
      else if (ch == '\\') b.append("\\");
      else if (ch == '\r') b.append("\\r");
      else if (ch == '\n') b.append("\\n");
      else if (ch == '\t') b.append("\\t");
      else if (ch < 32)
        b.append("\\u"+Integer.toHexString(ch));
      else
        b.append(ch);
    }
    b.append("\"");
  }
}