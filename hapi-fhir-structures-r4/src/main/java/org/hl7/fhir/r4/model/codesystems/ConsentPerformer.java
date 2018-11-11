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

public enum ConsentPerformer {

        /**
         * An entity or an entity's delegatee who is the grantee in an agreement such as a consent for services, advanced directive, or a privacy consent directive in accordance with jurisdictional, organizational, or patient policy.
         */
        CONSENTER, 
        /**
         * An entity which accepts certain rights or authority from a grantor.
         */
        GRANTEE, 
        /**
         * An entity which agrees to confer certain rights or authority to a grantee.
         */
        GRANTOR, 
        /**
         * A party to whom some right or authority is granted by a delegator.
         */
        DELEGATEE, 
        /**
         * A party that grants all or some portion its right or authority to another party.
         */
        DELEGATOR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConsentPerformer fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("consenter".equals(codeString))
          return CONSENTER;
        if ("grantee".equals(codeString))
          return GRANTEE;
        if ("grantor".equals(codeString))
          return GRANTOR;
        if ("delegatee".equals(codeString))
          return DELEGATEE;
        if ("delegator".equals(codeString))
          return DELEGATOR;
        throw new FHIRException("Unknown ConsentPerformer code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CONSENTER: return "consenter";
            case GRANTEE: return "grantee";
            case GRANTOR: return "grantor";
            case DELEGATEE: return "delegatee";
            case DELEGATOR: return "delegator";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/consentperformer";
        }
        public String getDefinition() {
          switch (this) {
            case CONSENTER: return "An entity or an entity's delegatee who is the grantee in an agreement such as a consent for services, advanced directive, or a privacy consent directive in accordance with jurisdictional, organizational, or patient policy.";
            case GRANTEE: return "An entity which accepts certain rights or authority from a grantor.";
            case GRANTOR: return "An entity which agrees to confer certain rights or authority to a grantee.";
            case DELEGATEE: return "A party to whom some right or authority is granted by a delegator.";
            case DELEGATOR: return "A party that grants all or some portion its right or authority to another party.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CONSENTER: return "Consenter";
            case GRANTEE: return "Grantee";
            case GRANTOR: return "Grantor";
            case DELEGATEE: return "Delegatee";
            case DELEGATOR: return "Delegator";
            default: return "?";
          }
    }


}

