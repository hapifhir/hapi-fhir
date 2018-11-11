package org.hl7.fhir.utilities;

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
