package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum HttpVerb {

        /**
         * HTTP GET
         */
        GET, 
        /**
         * HTTP POST
         */
        POST, 
        /**
         * HTTP PUT
         */
        PUT, 
        /**
         * HTTP DELETE
         */
        DELETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HttpVerb fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("GET".equals(codeString))
          return GET;
        if ("POST".equals(codeString))
          return POST;
        if ("PUT".equals(codeString))
          return PUT;
        if ("DELETE".equals(codeString))
          return DELETE;
        throw new FHIRException("Unknown HttpVerb code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GET: return "GET";
            case POST: return "POST";
            case PUT: return "PUT";
            case DELETE: return "DELETE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/http-verb";
        }
        public String getDefinition() {
          switch (this) {
            case GET: return "HTTP GET";
            case POST: return "HTTP POST";
            case PUT: return "HTTP PUT";
            case DELETE: return "HTTP DELETE";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GET: return "GET";
            case POST: return "POST";
            case PUT: return "PUT";
            case DELETE: return "DELETE";
            default: return "?";
          }
    }


}

