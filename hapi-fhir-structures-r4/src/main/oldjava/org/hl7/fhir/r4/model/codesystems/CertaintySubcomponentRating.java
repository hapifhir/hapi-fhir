package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum CertaintySubcomponentRating {

        /**
         * no change to quality rating.
         */
        NOCHANGE, 
        /**
         * reduce quality rating by 1.
         */
        DOWNCODE1, 
        /**
         * reduce quality rating by 2.
         */
        DOWNCODE2, 
        /**
         * reduce quality rating by 3.
         */
        DOWNCODE3, 
        /**
         * increase quality rating by 1.
         */
        UPCODE1, 
        /**
         * increase quality rating by 2.
         */
        UPCODE2, 
        /**
         * no serious concern.
         */
        NOCONCERN, 
        /**
         * serious concern.
         */
        SERIOUSCONCERN, 
        /**
         * critical concern.
         */
        CRITICALCONCERN, 
        /**
         * possible reason for increasing quality rating was checked and found to bepresent.
         */
        PRESENT, 
        /**
         * possible reason for increasing quality rating was checked and found to be absent.
         */
        ABSENT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CertaintySubcomponentRating fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("no-change".equals(codeString))
          return NOCHANGE;
        if ("downcode1".equals(codeString))
          return DOWNCODE1;
        if ("downcode2".equals(codeString))
          return DOWNCODE2;
        if ("downcode3".equals(codeString))
          return DOWNCODE3;
        if ("upcode1".equals(codeString))
          return UPCODE1;
        if ("upcode2".equals(codeString))
          return UPCODE2;
        if ("no-concern".equals(codeString))
          return NOCONCERN;
        if ("serious-concern".equals(codeString))
          return SERIOUSCONCERN;
        if ("critical-concern".equals(codeString))
          return CRITICALCONCERN;
        if ("present".equals(codeString))
          return PRESENT;
        if ("absent".equals(codeString))
          return ABSENT;
        throw new FHIRException("Unknown CertaintySubcomponentRating code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOCHANGE: return "no-change";
            case DOWNCODE1: return "downcode1";
            case DOWNCODE2: return "downcode2";
            case DOWNCODE3: return "downcode3";
            case UPCODE1: return "upcode1";
            case UPCODE2: return "upcode2";
            case NOCONCERN: return "no-concern";
            case SERIOUSCONCERN: return "serious-concern";
            case CRITICALCONCERN: return "critical-concern";
            case PRESENT: return "present";
            case ABSENT: return "absent";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/certainty-subcomponent-rating";
        }
        public String getDefinition() {
          switch (this) {
            case NOCHANGE: return "no change to quality rating.";
            case DOWNCODE1: return "reduce quality rating by 1.";
            case DOWNCODE2: return "reduce quality rating by 2.";
            case DOWNCODE3: return "reduce quality rating by 3.";
            case UPCODE1: return "increase quality rating by 1.";
            case UPCODE2: return "increase quality rating by 2.";
            case NOCONCERN: return "no serious concern.";
            case SERIOUSCONCERN: return "serious concern.";
            case CRITICALCONCERN: return "critical concern.";
            case PRESENT: return "possible reason for increasing quality rating was checked and found to bepresent.";
            case ABSENT: return "possible reason for increasing quality rating was checked and found to be absent.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOCHANGE: return "no change to rating";
            case DOWNCODE1: return "reduce rating: -1";
            case DOWNCODE2: return "reduce rating: -2";
            case DOWNCODE3: return "reduce rating: -3";
            case UPCODE1: return "increase rating: +1";
            case UPCODE2: return "increase rating: +2";
            case NOCONCERN: return "no serious concern";
            case SERIOUSCONCERN: return "serious concern";
            case CRITICALCONCERN: return "critical concern";
            case PRESENT: return "present";
            case ABSENT: return "absent";
            default: return "?";
          }
    }


}

