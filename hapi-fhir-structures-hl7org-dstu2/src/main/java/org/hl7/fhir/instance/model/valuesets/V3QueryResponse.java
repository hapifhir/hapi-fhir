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


public enum V3QueryResponse {

        /**
         * Query Error.  Application Error.
         */
        AE, 
        /**
         * No errors, but no data was found matching the query request specification.
         */
        NF, 
        /**
         * Query reponse data found for 1 or more result sets matching the query request specification.
         */
        OK, 
        /**
         * QueryError. Problem with input ParmetersError
         */
        QE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryResponse fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AE".equals(codeString))
          return AE;
        if ("NF".equals(codeString))
          return NF;
        if ("OK".equals(codeString))
          return OK;
        if ("QE".equals(codeString))
          return QE;
        throw new Exception("Unknown V3QueryResponse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AE: return "AE";
            case NF: return "NF";
            case OK: return "OK";
            case QE: return "QE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryResponse";
        }
        public String getDefinition() {
          switch (this) {
            case AE: return "Query Error.  Application Error.";
            case NF: return "No errors, but no data was found matching the query request specification.";
            case OK: return "Query reponse data found for 1 or more result sets matching the query request specification.";
            case QE: return "QueryError. Problem with input ParmetersError";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AE: return "ApplicationError";
            case NF: return "No data found";
            case OK: return "Data found";
            case QE: return "QueryParameterError";
            default: return "?";
          }
    }


}

