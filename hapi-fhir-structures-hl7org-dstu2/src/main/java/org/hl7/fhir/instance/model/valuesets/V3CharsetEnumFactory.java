package org.hl7.fhir.instance.model.valuesets;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

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


}

