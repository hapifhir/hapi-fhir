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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum V3QueryStatusCode {

        /**
         * Query status aborted
         */
        ABORTED, 
        /**
         * Query Status delivered response
         */
        DELIVEREDRESPONSE, 
        /**
         * Query Status executing
         */
        EXECUTING, 
        /**
         * Query Status new
         */
        NEW, 
        /**
         * Query Status wait continued
         */
        WAITCONTINUEDQUERYRESPONSE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3QueryStatusCode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("aborted".equals(codeString))
          return ABORTED;
        if ("deliveredResponse".equals(codeString))
          return DELIVEREDRESPONSE;
        if ("executing".equals(codeString))
          return EXECUTING;
        if ("new".equals(codeString))
          return NEW;
        if ("waitContinuedQueryResponse".equals(codeString))
          return WAITCONTINUEDQUERYRESPONSE;
        throw new Exception("Unknown V3QueryStatusCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ABORTED: return "aborted";
            case DELIVEREDRESPONSE: return "deliveredResponse";
            case EXECUTING: return "executing";
            case NEW: return "new";
            case WAITCONTINUEDQUERYRESPONSE: return "waitContinuedQueryResponse";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/QueryStatusCode";
        }
        public String getDefinition() {
          switch (this) {
            case ABORTED: return "Query status aborted";
            case DELIVEREDRESPONSE: return "Query Status delivered response";
            case EXECUTING: return "Query Status executing";
            case NEW: return "Query Status new";
            case WAITCONTINUEDQUERYRESPONSE: return "Query Status wait continued";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ABORTED: return "aborted";
            case DELIVEREDRESPONSE: return "deliveredResponse";
            case EXECUTING: return "executing";
            case NEW: return "new";
            case WAITCONTINUEDQUERYRESPONSE: return "waitContinuedQueryResponse";
            default: return "?";
          }
    }


}

