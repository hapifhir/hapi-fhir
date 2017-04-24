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

public enum ConsentStateCodes {

        /**
         * The consent is in development or awaiting use but is not yet intended to be acted upon.
         */
        DRAFT, 
        /**
         * The consent has been proposed but not yet agreed to by all parties. The negotiation stage.
         */
        PROPOSED, 
        /**
         * The consent is to be followed and enforced.
         */
        ACTIVE, 
        /**
         * The consent has been rejected by one or more of the parties.
         */
        REJECTED, 
        /**
         * The consent is terminated or replaced.
         */
        INACTIVE, 
        /**
         * The consent was created wrongly (e.g. wrong patient) and should be ignored
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConsentStateCodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown ConsentStateCodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case PROPOSED: return "proposed";
            case ACTIVE: return "active";
            case REJECTED: return "rejected";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/consent-state-codes";
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The consent is in development or awaiting use but is not yet intended to be acted upon.";
            case PROPOSED: return "The consent has been proposed but not yet agreed to by all parties. The negotiation stage.";
            case ACTIVE: return "The consent is to be followed and enforced.";
            case REJECTED: return "The consent has been rejected by one or more of the parties.";
            case INACTIVE: return "The consent is terminated or replaced.";
            case ENTEREDINERROR: return "The consent was created wrongly (e.g. wrong patient) and should be ignored";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Pending";
            case PROPOSED: return "Proposed";
            case ACTIVE: return "Active";
            case REJECTED: return "Rejected";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
    }


}

