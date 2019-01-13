package org.hl7.fhir.utilities;

/*-
 * #%L
 * org.hl7.fhir.utilities
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
