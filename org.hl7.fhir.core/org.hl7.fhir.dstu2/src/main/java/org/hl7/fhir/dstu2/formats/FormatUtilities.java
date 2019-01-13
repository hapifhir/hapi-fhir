package org.hl7.fhir.dstu2.formats;

/*-
 * #%L
 * org.hl7.fhir.dstu2
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


/*
Copyright (c) 2011+, HL7, Inc
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

import java.math.BigDecimal;
import java.net.URI;

import org.apache.commons.codec.binary.Base64;

public abstract class FormatUtilities {
  public static final String ID_REGEX = "[A-Za-z0-9\\-\\.]{1,64}";
  public static final String FHIR_NS = "http://hl7.org/fhir";
  public static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
 
  protected String toString(String value) {
    return value;
  }
  
  protected String toString(int value) {
    return java.lang.Integer.toString(value);
  }
  
  protected String toString(boolean value) {
    return java.lang.Boolean.toString(value);
  }
  
  protected String toString(BigDecimal value) {
    return value.toString();
  }
  
  protected String toString(URI value) {
    return value.toString();
  }

  public static String toString(byte[] value) {
    byte[] encodeBase64 = Base64.encodeBase64(value);
    return new String(encodeBase64);
  }
  
	public static boolean isValidId(String tail) {
	  return tail.matches(ID_REGEX);
  }

  public static String makeId(String candidate) {
    StringBuilder b = new StringBuilder();
    for (char c : candidate.toCharArray())
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '.' || c == '-')
        b.append(c);
    return b.toString();
  }
  
  public static ParserBase makeParser(FhirFormat format) {
    switch (format) {
    case XML : return new XmlParser();
    case JSON : return new JsonParser();
    case TURTLE : throw new Error("unsupported Format "+format.toString()); // return new TurtleParser();
    case VBAR : throw new Error("unsupported Format "+format.toString()); // 
    case TEXT : throw new Error("unsupported Format "+format.toString()); // 
    }
    throw new Error("unsupported Format "+format.toString());
  }
  
  public static ParserBase makeParser(String format) {
    if ("XML".equalsIgnoreCase(format)) return new XmlParser();
    if ("JSON".equalsIgnoreCase(format)) return new JsonParser();
    if ("TURTLE".equalsIgnoreCase(format)) throw new Error("unsupported Format "+format.toString()); // return new TurtleParser();
    if ("JSONLD".equalsIgnoreCase(format)) throw new Error("unsupported Format "+format.toString()); // return new JsonLdParser();
    if ("VBAR".equalsIgnoreCase(format)) throw new Error("unsupported Format "+format.toString()); // 
    if ("TEXT".equalsIgnoreCase(format)) throw new Error("unsupported Format "+format.toString()); // 
    throw new Error("unsupported Format "+format);
  }  




}
