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

public enum V3Hl7ApprovalStatus {

        /**
         * Description: Content that is being presented to an international affiliate for consideration as a realm-specific draft standard for trial use.
         */
        AFFD, 
        /**
         * Description: Content that is being presented to an international affiliate for consideration as a realm-specific informative standard.
         */
        AFFI, 
        /**
         * Description: Content that is being presented to an international affiliate for consideration as a realm-specific normative standard.
         */
        AFFN, 
        /**
         * Description: Content that has passed ballot as a realm-specific draft standard for trial use.
         */
        APPAD, 
        /**
         * Description: Content that has passed ballot as a realm-specific informative standard.
         */
        APPAI, 
        /**
         * Description: Content that has passed ballot as a realm-specific normative standard
         */
        APPAN, 
        /**
         * Description: Content that has passed ballot as a draft standard for trial use.
         */
        APPD, 
        /**
         * Description: Content that has passed ballot as a normative standard.
         */
        APPI, 
        /**
         * Description: Content that has passed ballot as a normative standard.
         */
        APPN, 
        /**
         * Description: Content prepared by a committee and submitted for internal consideration as an informative standard.

                        
                           
                              Deprecation Comment
                            No longer supported as ballot statuses within the HL7 Governance and Operations Manual.  Use normative or informative variants instead.
         */
        COMI, 
        /**
         * Description: Content prepared by a committee and submitted for internal consideration as an informative standard.

                        
                           
                              Deprecation Comment
                            No longer supported as ballot statuses within the HL7 Governance and Operations Manual.  Use normative or informative variants instead.
         */
        COMN, 
        /**
         * Description: Content that is under development and is not intended to be used.
         */
        DRAFT, 
        /**
         * Description: Content that represents an adaption of a implementable balloted material to represent the needs or capabilities of a particular installation.
         */
        LOC, 
        /**
         * Description: Content prepared by a committee and submitted for membership consideration as a draft standard for trial use.
         */
        MEMD, 
        /**
         * Description: Content prepared by a committee and submitted for membership consideration as an informative standard.
         */
        MEMI, 
        /**
         * Description: Content prepared by a committee and submitted for membership consideration as a normative standard.
         */
        MEMN, 
        /**
         * Description: Content developed independently by an organization or individual that is declared to be 'usable' but for which there is no present intention to submit through the standards submission and review process.
         */
        NS, 
        /**
         * Description: Content submitted to a committee for consideration for future inclusion in the standard.
         */
        PROP, 
        /**
         * Description: Content intended to support other content that is subject to approval, but which is not itself subject to formal approval.
         */
        REF, 
        /**
         * Description: Content that represents an item that was at one point a normative or informative standard, but was subsequently withdrawn.
         */
        WD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7ApprovalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("affd".equals(codeString))
          return AFFD;
        if ("affi".equals(codeString))
          return AFFI;
        if ("affn".equals(codeString))
          return AFFN;
        if ("appad".equals(codeString))
          return APPAD;
        if ("appai".equals(codeString))
          return APPAI;
        if ("appan".equals(codeString))
          return APPAN;
        if ("appd".equals(codeString))
          return APPD;
        if ("appi".equals(codeString))
          return APPI;
        if ("appn".equals(codeString))
          return APPN;
        if ("comi".equals(codeString))
          return COMI;
        if ("comn".equals(codeString))
          return COMN;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("loc".equals(codeString))
          return LOC;
        if ("memd".equals(codeString))
          return MEMD;
        if ("memi".equals(codeString))
          return MEMI;
        if ("memn".equals(codeString))
          return MEMN;
        if ("ns".equals(codeString))
          return NS;
        if ("prop".equals(codeString))
          return PROP;
        if ("ref".equals(codeString))
          return REF;
        if ("wd".equals(codeString))
          return WD;
        throw new FHIRException("Unknown V3Hl7ApprovalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AFFD: return "affd";
            case AFFI: return "affi";
            case AFFN: return "affn";
            case APPAD: return "appad";
            case APPAI: return "appai";
            case APPAN: return "appan";
            case APPD: return "appd";
            case APPI: return "appi";
            case APPN: return "appn";
            case COMI: return "comi";
            case COMN: return "comn";
            case DRAFT: return "draft";
            case LOC: return "loc";
            case MEMD: return "memd";
            case MEMI: return "memi";
            case MEMN: return "memn";
            case NS: return "ns";
            case PROP: return "prop";
            case REF: return "ref";
            case WD: return "wd";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-hl7ApprovalStatus";
        }
        public String getDefinition() {
          switch (this) {
            case AFFD: return "Description: Content that is being presented to an international affiliate for consideration as a realm-specific draft standard for trial use.";
            case AFFI: return "Description: Content that is being presented to an international affiliate for consideration as a realm-specific informative standard.";
            case AFFN: return "Description: Content that is being presented to an international affiliate for consideration as a realm-specific normative standard.";
            case APPAD: return "Description: Content that has passed ballot as a realm-specific draft standard for trial use.";
            case APPAI: return "Description: Content that has passed ballot as a realm-specific informative standard.";
            case APPAN: return "Description: Content that has passed ballot as a realm-specific normative standard";
            case APPD: return "Description: Content that has passed ballot as a draft standard for trial use.";
            case APPI: return "Description: Content that has passed ballot as a normative standard.";
            case APPN: return "Description: Content that has passed ballot as a normative standard.";
            case COMI: return "Description: Content prepared by a committee and submitted for internal consideration as an informative standard.\r\n\n                        \n                           \n                              Deprecation Comment\n                            No longer supported as ballot statuses within the HL7 Governance and Operations Manual.  Use normative or informative variants instead.";
            case COMN: return "Description: Content prepared by a committee and submitted for internal consideration as an informative standard.\r\n\n                        \n                           \n                              Deprecation Comment\n                            No longer supported as ballot statuses within the HL7 Governance and Operations Manual.  Use normative or informative variants instead.";
            case DRAFT: return "Description: Content that is under development and is not intended to be used.";
            case LOC: return "Description: Content that represents an adaption of a implementable balloted material to represent the needs or capabilities of a particular installation.";
            case MEMD: return "Description: Content prepared by a committee and submitted for membership consideration as a draft standard for trial use.";
            case MEMI: return "Description: Content prepared by a committee and submitted for membership consideration as an informative standard.";
            case MEMN: return "Description: Content prepared by a committee and submitted for membership consideration as a normative standard.";
            case NS: return "Description: Content developed independently by an organization or individual that is declared to be 'usable' but for which there is no present intention to submit through the standards submission and review process.";
            case PROP: return "Description: Content submitted to a committee for consideration for future inclusion in the standard.";
            case REF: return "Description: Content intended to support other content that is subject to approval, but which is not itself subject to formal approval.";
            case WD: return "Description: Content that represents an item that was at one point a normative or informative standard, but was subsequently withdrawn.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AFFD: return "affiliate ballot - DSTU";
            case AFFI: return "affiliate ballot - informative";
            case AFFN: return "affiliate ballot - normative";
            case APPAD: return "approved affiliate DSTU";
            case APPAI: return "approved affiliate informative";
            case APPAN: return "approved affiliate normative";
            case APPD: return "approved DSTU";
            case APPI: return "approved informative";
            case APPN: return "approved normative";
            case COMI: return "committee ballot - informative";
            case COMN: return "committee ballot - normative";
            case DRAFT: return "draft";
            case LOC: return "localized adaptation";
            case MEMD: return "membership ballot - DSTU";
            case MEMI: return "membership ballot - informative";
            case MEMN: return "membership ballot - normative";
            case NS: return "non-standard - available for use";
            case PROP: return "proposal";
            case REF: return "reference";
            case WD: return "withdrawn";
            default: return "?";
          }
    }


}

