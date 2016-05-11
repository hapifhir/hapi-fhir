package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3CharsetEnumFactory implements EnumFactory<V3Charset> {

  public V3Charset fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("EBCDIC".equals(codeString))
      return V3Charset.EBCDIC;
    if ("ISO-10646-UCS-2".equals(codeString))
      return V3Charset.ISO10646UCS2;
    if ("ISO-10646-UCS-4".equals(codeString))
      return V3Charset.ISO10646UCS4;
    if ("ISO-8859-1".equals(codeString))
      return V3Charset.ISO88591;
    if ("ISO-8859-2".equals(codeString))
      return V3Charset.ISO88592;
    if ("ISO-8859-5".equals(codeString))
      return V3Charset.ISO88595;
    if ("JIS-2022-JP".equals(codeString))
      return V3Charset.JIS2022JP;
    if ("US-ASCII".equals(codeString))
      return V3Charset.USASCII;
    if ("UTF-7".equals(codeString))
      return V3Charset.UTF7;
    if ("UTF-8".equals(codeString))
      return V3Charset.UTF8;
    throw new IllegalArgumentException("Unknown V3Charset code '"+codeString+"'");
  }

  public String toCode(V3Charset code) {
    if (code == V3Charset.EBCDIC)
      return "EBCDIC";
    if (code == V3Charset.ISO10646UCS2)
      return "ISO-10646-UCS-2";
    if (code == V3Charset.ISO10646UCS4)
      return "ISO-10646-UCS-4";
    if (code == V3Charset.ISO88591)
      return "ISO-8859-1";
    if (code == V3Charset.ISO88592)
      return "ISO-8859-2";
    if (code == V3Charset.ISO88595)
      return "ISO-8859-5";
    if (code == V3Charset.JIS2022JP)
      return "JIS-2022-JP";
    if (code == V3Charset.USASCII)
      return "US-ASCII";
    if (code == V3Charset.UTF7)
      return "UTF-7";
    if (code == V3Charset.UTF8)
      return "UTF-8";
    return "?";
  }

    public String toSystem(V3Charset code) {
      return code.getSystem();
      }

}

