package org.hl7.fhir.utilities;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class CSVWriter extends TextStreamWriter {

  public CSVWriter(OutputStream out) throws UnsupportedEncodingException {
    super(out);
  }

  protected String csvEscape(String s) {
    if (s==null)
      return "";
    else if (s.contains("\""))
      return s.substring(0,s.indexOf("\"")) + "\"" + csvEscape(s.substring(s.indexOf("\"")+1));
    else if (s.contains(","))
      return "\""+s+"\"";
    else
      return s;
  }
  

  public void line(String... fields) throws IOException {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (String s : fields) {
      if (first)
        first = false;
      else
        b.append(",");
      b.append(csvEscape(s));
    }
    ln(b.toString());
  }
  
}
