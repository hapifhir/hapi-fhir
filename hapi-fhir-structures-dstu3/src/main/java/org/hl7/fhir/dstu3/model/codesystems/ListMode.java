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

public enum ListMode {

        /**
         * This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes
         */
        WORKING, 
        /**
         * This list was prepared as a snapshot. It should not be assumed to be current
         */
        SNAPSHOT, 
        /**
         * A list that indicates where changes have been made or recommended
         */
        CHANGES, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ListMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("working".equals(codeString))
          return WORKING;
        if ("snapshot".equals(codeString))
          return SNAPSHOT;
        if ("changes".equals(codeString))
          return CHANGES;
        throw new FHIRException("Unknown ListMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case WORKING: return "working";
            case SNAPSHOT: return "snapshot";
            case CHANGES: return "changes";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/list-mode";
        }
        public String getDefinition() {
          switch (this) {
            case WORKING: return "This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes";
            case SNAPSHOT: return "This list was prepared as a snapshot. It should not be assumed to be current";
            case CHANGES: return "A list that indicates where changes have been made or recommended";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WORKING: return "Working List";
            case SNAPSHOT: return "Snapshot List";
            case CHANGES: return "Change List";
            default: return "?";
          }
    }


}

