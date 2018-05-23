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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum MedAdminPerformFunction {

        /**
         * A person, non-person living subject, organization or device that who actually and principally carries out the action
         */
        PERFORMER, 
        /**
         * A person who verifies the correctness and appropriateness of the service (plan, order, event, etc.) and hence takes on accountability.
         */
        VERIFIER, 
        /**
         * A person witnessing the action happening without doing anything. A witness is not necessarily aware, much less approves of anything stated in the service event. Example for a witness is students watching an operation or an advanced directive witness.
         */
        WITNESS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MedAdminPerformFunction fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("performer".equals(codeString))
          return PERFORMER;
        if ("verifier".equals(codeString))
          return VERIFIER;
        if ("witness".equals(codeString))
          return WITNESS;
        throw new FHIRException("Unknown MedAdminPerformFunction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PERFORMER: return "performer";
            case VERIFIER: return "verifier";
            case WITNESS: return "witness";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/med-admin-perform-function";
        }
        public String getDefinition() {
          switch (this) {
            case PERFORMER: return "A person, non-person living subject, organization or device that who actually and principally carries out the action";
            case VERIFIER: return "A person who verifies the correctness and appropriateness of the service (plan, order, event, etc.) and hence takes on accountability.";
            case WITNESS: return "A person witnessing the action happening without doing anything. A witness is not necessarily aware, much less approves of anything stated in the service event. Example for a witness is students watching an operation or an advanced directive witness.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PERFORMER: return "Performer";
            case VERIFIER: return "Verifier";
            case WITNESS: return "Witness";
            default: return "?";
          }
    }


}

