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

public enum VerificationresultPrimarySourceType {

        /**
         * null
         */
        LICBOARD, 
        /**
         * null
         */
        PRIM, 
        /**
         * null
         */
        CONTED, 
        /**
         * null
         */
        POSTSERV, 
        /**
         * null
         */
        RELOWN, 
        /**
         * null
         */
        REGAUTH, 
        /**
         * null
         */
        LEGAL, 
        /**
         * null
         */
        ISSUER, 
        /**
         * null
         */
        AUTHSOURCE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static VerificationresultPrimarySourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("lic-board".equals(codeString))
          return LICBOARD;
        if ("prim".equals(codeString))
          return PRIM;
        if ("cont-ed".equals(codeString))
          return CONTED;
        if ("post-serv".equals(codeString))
          return POSTSERV;
        if ("rel-own".equals(codeString))
          return RELOWN;
        if ("reg-auth".equals(codeString))
          return REGAUTH;
        if ("legal".equals(codeString))
          return LEGAL;
        if ("issuer".equals(codeString))
          return ISSUER;
        if ("auth-source".equals(codeString))
          return AUTHSOURCE;
        throw new FHIRException("Unknown VerificationresultPrimarySourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LICBOARD: return "lic-board";
            case PRIM: return "prim";
            case CONTED: return "cont-ed";
            case POSTSERV: return "post-serv";
            case RELOWN: return "rel-own";
            case REGAUTH: return "reg-auth";
            case LEGAL: return "legal";
            case ISSUER: return "issuer";
            case AUTHSOURCE: return "auth-source";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/primary-source-type";
        }
        public String getDefinition() {
          switch (this) {
            case LICBOARD: return "";
            case PRIM: return "";
            case CONTED: return "";
            case POSTSERV: return "";
            case RELOWN: return "";
            case REGAUTH: return "";
            case LEGAL: return "";
            case ISSUER: return "";
            case AUTHSOURCE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LICBOARD: return "License Board";
            case PRIM: return "Primary Education";
            case CONTED: return "Continuing Education";
            case POSTSERV: return "Postal Service";
            case RELOWN: return "Relationship owner";
            case REGAUTH: return "Registration Authority";
            case LEGAL: return "Legal source";
            case ISSUER: return "Issuing source";
            case AUTHSOURCE: return "Authoritative source";
            default: return "?";
          }
    }


}

