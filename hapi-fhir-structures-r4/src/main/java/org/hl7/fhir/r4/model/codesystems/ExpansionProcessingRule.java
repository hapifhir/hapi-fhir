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

public enum ExpansionProcessingRule {

        /**
         * The expansion (when in UI mode) includes all codes *and* any defined groups (in extensions).
         */
        ALLCODES, 
        /**
         * The expanion (when in UI mode) lists the groups, and then any codes that have not been included in a group.
         */
        UNGROUPED, 
        /**
         * The expansion (when in UI mode) only includes the defined groups.
         */
        GROUPSONLY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExpansionProcessingRule fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("all-codes".equals(codeString))
          return ALLCODES;
        if ("ungrouped".equals(codeString))
          return UNGROUPED;
        if ("groups-only".equals(codeString))
          return GROUPSONLY;
        throw new FHIRException("Unknown ExpansionProcessingRule code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALLCODES: return "all-codes";
            case UNGROUPED: return "ungrouped";
            case GROUPSONLY: return "groups-only";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/expansion-processing-rule";
        }
        public String getDefinition() {
          switch (this) {
            case ALLCODES: return "The expansion (when in UI mode) includes all codes *and* any defined groups (in extensions).";
            case UNGROUPED: return "The expanion (when in UI mode) lists the groups, and then any codes that have not been included in a group.";
            case GROUPSONLY: return "The expansion (when in UI mode) only includes the defined groups.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALLCODES: return "All Codes";
            case UNGROUPED: return "Groups + Ungrouped codes";
            case GROUPSONLY: return "Groups Only";
            default: return "?";
          }
    }


}

