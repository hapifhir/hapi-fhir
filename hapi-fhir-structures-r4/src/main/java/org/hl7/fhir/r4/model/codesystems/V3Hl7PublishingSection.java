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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3Hl7PublishingSection {

        /**
         * Description: Represents the HL7 V3 publishing section that deals with the administration and management of health care activities and organizations.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        AM, 
        /**
         * Description: Represents the HL7 V3 publishing section that deals with the health care provision and clinical management.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        HM, 
        /**
         * Description: Represents the HL7 V3 publishing section that deals with the definition and management of the computing and communication infrastructure necessary to support health care.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        IM, 
        /**
         * Description: Represents the HL7 V3 publishing section that holds specifications that are unassigned - that have not yet been assigned to one of the formal publishing sections.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        UU, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7PublishingSection fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AM".equals(codeString))
          return AM;
        if ("HM".equals(codeString))
          return HM;
        if ("IM".equals(codeString))
          return IM;
        if ("UU".equals(codeString))
          return UU;
        throw new FHIRException("Unknown V3Hl7PublishingSection code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AM: return "AM";
            case HM: return "HM";
            case IM: return "IM";
            case UU: return "UU";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-hl7PublishingSection";
        }
        public String getDefinition() {
          switch (this) {
            case AM: return "Description: Represents the HL7 V3 publishing section that deals with the administration and management of health care activities and organizations.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case HM: return "Description: Represents the HL7 V3 publishing section that deals with the health care provision and clinical management.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case IM: return "Description: Represents the HL7 V3 publishing section that deals with the definition and management of the computing and communication infrastructure necessary to support health care.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case UU: return "Description: Represents the HL7 V3 publishing section that holds specifications that are unassigned - that have not yet been assigned to one of the formal publishing sections.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AM: return "administrative management";
            case HM: return "health and clinical management";
            case IM: return "infrastructure management";
            case UU: return "unknown";
            default: return "?";
          }
    }


}

