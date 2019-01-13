package org.hl7.fhir.r4.context;

/*-
 * #%L
 * org.hl7.fhir.r4
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


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.hl7.fhir.r4.utils.client.ToolingClientLogger;
import org.hl7.fhir.utilities.Utilities;

public class HTMLClientLogger implements ToolingClientLogger {

  private PrintStream file;
  private int id = 0;
  private String lastId;
  
  public HTMLClientLogger(String log) {
    if (log != null) {
      try {
        file = new PrintStream(new FileOutputStream(log));
      } catch (FileNotFoundException e) {
      }
    }
  }

  @Override
  public void logRequest(String method, String url, List<String> headers, byte[] body) {
    if (file == null)
      return;
    id++;
    lastId = Integer.toString(id);
    file.println("<hr/><a name=\"l"+lastId+"\"> </a>");
    file.println("<pre>");
    file.println(method+" "+url+" HTTP/1.0");
    for (String s : headers)  
      file.println(Utilities.escapeXml(s));
    if (body != null) {
      file.println("");
      try {
        file.println(Utilities.escapeXml(new String(body, "UTF-8")));
      } catch (UnsupportedEncodingException e) {
      }
    }
    file.println("</pre>");
  }

  @Override
  public void logResponse(String outcome, List<String> headers, byte[] body) {
    if (file == null)
      return;
    file.println("<pre>");
    file.println(outcome);
    for (String s : headers)  
      file.println(Utilities.escapeXml(s));
    if (body != null) {
      file.println("");
      try {
        file.println(Utilities.escapeXml(new String(body, "UTF-8")));
      } catch (UnsupportedEncodingException e) {
      }
    }
    file.println("</pre>");
  }

  public String getLastId() {
    return lastId;
  }

  public void clearLastId() {
    lastId = null;    
  }

}
