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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3QueryRequestLimit {

        /**
         * Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.
         */
        _QUERYREQUESTLIMIT, 
        /**
         * Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.
         */
        RD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryRequestLimit fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_QueryRequestLimit".equals(codeString))
          return _QUERYREQUESTLIMIT;
        if ("RD".equals(codeString))
          return RD;
        throw new FHIRException("Unknown V3QueryRequestLimit code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _QUERYREQUESTLIMIT: return "_QueryRequestLimit";
            case RD: return "RD";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryRequestLimit";
        }
        public String getDefinition() {
          switch (this) {
            case _QUERYREQUESTLIMIT: return "Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.";
            case RD: return "Definition: The number of matching instances (number of focal classes). The document header class is the focal class of a document, a record would therefore be equal to a document.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _QUERYREQUESTLIMIT: return "QueryRequestLimit";
            case RD: return "record";
            default: return "?";
          }
    }


}

