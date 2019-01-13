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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MimeType {

  private String source;
  private String base;
  private Map<String, String> params = new HashMap<String, String>();

  public MimeType(String s) {
    source = s;
    for (String p : s.split("\\;"))
      if (base == null)
        base = p;
      else
        params.put(p.substring(0, p.indexOf("=")), p.substring(p.indexOf("=")+1));
    if ("xml".equals(base))
      base = "application/fhir+xml";
    if ("json".equals(base))
      base = "application/fhir+json";
    if ("ttl".equals(base))
      base = "application/fhir+ttl";
  }

  public String main() {
    if (base.contains("/"))
      return base.substring(0, base.indexOf("/"));
    else
      return base;
  }

  public String sub() {
    if (base.contains("/"))
      return base.substring(base.indexOf("/")+1);
    else
      return null;
  }

  public boolean hasParam(String name) {
    return params.containsKey(name);
  }

  public boolean isValid() {
    return (Utilities.existsInList(main(), "application", "audio", "font", "example", "image", "message", "model", "multipart", "text", "video") || main().startsWith("x-")) && !Utilities.noString(sub());
  }

  public static List<MimeType> parseList(String s) {
    List<MimeType> result = new ArrayList<MimeType>();
    for (String e : s.split("\\,"))
        result.add(new MimeType(e));
    return result;
  }

  public String display() {
    return source;
  }

}
