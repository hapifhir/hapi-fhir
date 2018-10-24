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

public enum V3Hl7PublishingSubSection {

        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds common or shared specifications within the Infrastructure Management (IM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        CO, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the management of financial information within the Administrative Management (AM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        FI, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the definition and control of interoperability messages within the Infrastructure Management (IM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        MC, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to master file and registry management activities within the Infrastructure Management (IM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        MF, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to managing clinical operations within the Health and Clinical Management (HM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        PO, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the management of practice settings within the Administrative Management (AM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        PR, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to query/response activities within the Infrastructure Management (IM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        QU, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the definition and communication of records of clinical care within the Health and Clinical Management (HM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        RC, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the definition and communication of reasoning (knowledge) within the Health and Clinical Management (HM) section.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        RE, 
        /**
         * Description: Represents the HL7 V3 publishing sub-section that holds specifications that are unassigned - that have not yet been assigned to one of the formal publishing sections.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.

                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.
         */
        UU, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7PublishingSubSection fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("CO".equals(codeString))
          return CO;
        if ("FI".equals(codeString))
          return FI;
        if ("MC".equals(codeString))
          return MC;
        if ("MF".equals(codeString))
          return MF;
        if ("PO".equals(codeString))
          return PO;
        if ("PR".equals(codeString))
          return PR;
        if ("QU".equals(codeString))
          return QU;
        if ("RC".equals(codeString))
          return RC;
        if ("RE".equals(codeString))
          return RE;
        if ("UU".equals(codeString))
          return UU;
        throw new FHIRException("Unknown V3Hl7PublishingSubSection code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CO: return "CO";
            case FI: return "FI";
            case MC: return "MC";
            case MF: return "MF";
            case PO: return "PO";
            case PR: return "PR";
            case QU: return "QU";
            case RC: return "RC";
            case RE: return "RE";
            case UU: return "UU";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-hl7PublishingSubSection";
        }
        public String getDefinition() {
          switch (this) {
            case CO: return "Description: Represents the HL7 V3 publishing sub-section that holds common or shared specifications within the Infrastructure Management (IM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case FI: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the management of financial information within the Administrative Management (AM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case MC: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the definition and control of interoperability messages within the Infrastructure Management (IM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case MF: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to master file and registry management activities within the Infrastructure Management (IM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case PO: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to managing clinical operations within the Health and Clinical Management (HM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case PR: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the management of practice settings within the Administrative Management (AM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case QU: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to query/response activities within the Infrastructure Management (IM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case RC: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the definition and communication of records of clinical care within the Health and Clinical Management (HM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case RE: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications related to the definition and communication of reasoning (knowledge) within the Health and Clinical Management (HM) section.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            case UU: return "Description: Represents the HL7 V3 publishing sub-section that holds specifications that are unassigned - that have not yet been assigned to one of the formal publishing sections.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.\r\n\n                        For publishing purposes, these domains are aggregated into sub-sections of related health care areas and these sub-sections are further aggregated into three major sets.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CO: return "common";
            case FI: return "financial information";
            case MC: return "message control";
            case MF: return "master file";
            case PO: return "operations";
            case PR: return "practice";
            case QU: return "query";
            case RC: return "records";
            case RE: return "reasoning";
            case UU: return "unknown";
            default: return "?";
          }
    }


}

