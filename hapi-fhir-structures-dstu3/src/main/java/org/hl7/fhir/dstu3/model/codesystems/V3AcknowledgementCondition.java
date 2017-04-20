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

public enum V3AcknowledgementCondition {

        /**
         * Always send an acknowledgement.
         */
        AL, 
        /**
         * Send an acknowledgement for error/reject conditions only.
         */
        ER, 
        /**
         * Never send an acknowledgement.
         */
        NE, 
        /**
         * Send an acknowledgement for successful completions only.
         */
        SU, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AcknowledgementCondition fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AL".equals(codeString))
          return AL;
        if ("ER".equals(codeString))
          return ER;
        if ("NE".equals(codeString))
          return NE;
        if ("SU".equals(codeString))
          return SU;
        throw new FHIRException("Unknown V3AcknowledgementCondition code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AL: return "AL";
            case ER: return "ER";
            case NE: return "NE";
            case SU: return "SU";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AcknowledgementCondition";
        }
        public String getDefinition() {
          switch (this) {
            case AL: return "Always send an acknowledgement.";
            case ER: return "Send an acknowledgement for error/reject conditions only.";
            case NE: return "Never send an acknowledgement.";
            case SU: return "Send an acknowledgement for successful completions only.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AL: return "Always";
            case ER: return "Error/reject only";
            case NE: return "Never";
            case SU: return "Successful only";
            default: return "?";
          }
    }


}

