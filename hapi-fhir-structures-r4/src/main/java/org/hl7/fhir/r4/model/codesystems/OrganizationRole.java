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

public enum OrganizationRole {

        /**
         * null
         */
        PROVIDER, 
        /**
         * An organization such as a public health agency, community/social services provider, etc.
         */
        AGENCY, 
        /**
         * An organization providing research-related services such as conducting research, recruiting research participants, analyzing data, etc.
         */
        RESEARCH, 
        /**
         * An organization providing reimbursement, payment, or related services
         */
        PAYER, 
        /**
         * An organization providing diagnostic testing/laboratory services
         */
        DIAGNOSTICS, 
        /**
         * An organization that provides medical supplies (e.g. medical devices, equipment, pharmaceutical products, etc.)
         */
        SUPPLIER, 
        /**
         * An organization that facilitates electronic clinical data exchange between entities
         */
        HIE_HIO, 
        /**
         * A type of non-ownership relationship between entities (encompassess partnerships, collaboration, joint ventures, etc.)
         */
        MEMBER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OrganizationRole fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("provider".equals(codeString))
          return PROVIDER;
        if ("agency".equals(codeString))
          return AGENCY;
        if ("research".equals(codeString))
          return RESEARCH;
        if ("payer".equals(codeString))
          return PAYER;
        if ("diagnostics".equals(codeString))
          return DIAGNOSTICS;
        if ("supplier".equals(codeString))
          return SUPPLIER;
        if ("HIE/HIO".equals(codeString))
          return HIE_HIO;
        if ("member".equals(codeString))
          return MEMBER;
        throw new FHIRException("Unknown OrganizationRole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROVIDER: return "provider";
            case AGENCY: return "agency";
            case RESEARCH: return "research";
            case PAYER: return "payer";
            case DIAGNOSTICS: return "diagnostics";
            case SUPPLIER: return "supplier";
            case HIE_HIO: return "HIE/HIO";
            case MEMBER: return "member";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/organization-role";
        }
        public String getDefinition() {
          switch (this) {
            case PROVIDER: return "";
            case AGENCY: return "An organization such as a public health agency, community/social services provider, etc.";
            case RESEARCH: return "An organization providing research-related services such as conducting research, recruiting research participants, analyzing data, etc.";
            case PAYER: return "An organization providing reimbursement, payment, or related services";
            case DIAGNOSTICS: return "An organization providing diagnostic testing/laboratory services";
            case SUPPLIER: return "An organization that provides medical supplies (e.g. medical devices, equipment, pharmaceutical products, etc.)";
            case HIE_HIO: return "An organization that facilitates electronic clinical data exchange between entities";
            case MEMBER: return "A type of non-ownership relationship between entities (encompassess partnerships, collaboration, joint ventures, etc.)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROVIDER: return "Provider";
            case AGENCY: return "Agency";
            case RESEARCH: return "Research";
            case PAYER: return "Payer";
            case DIAGNOSTICS: return "Diagnostics";
            case SUPPLIER: return "Supplier";
            case HIE_HIO: return "HIE/HIO";
            case MEMBER: return "Member";
            default: return "?";
          }
    }


}

