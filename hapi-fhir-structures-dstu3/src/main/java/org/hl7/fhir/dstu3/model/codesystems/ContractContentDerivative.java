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

public enum ContractContentDerivative {

        /**
         * Content derivative that conveys sufficient information needed to register the source basal content from which it is derived.  This derivative content may be used to register the basal content as it changes status in its lifecycle.  For example, content registration may occur when the basal content is created, updated, inactive, or deleted.
         */
        REGISTRATION, 
        /**
         * A content derivative that conveys sufficient information to locate and retrieve the content.
         */
        RETRIEVAL, 
        /**
         * Content derivative that has less than full fidelity to the basal information source from which it was 'transcribed'. It provides recipients with the full content representation they may require for compliance purposes, and typically include a reference to or an attached unstructured representation for recipients needing an exact copy of the legal agreement.
         */
        STATEMENT, 
        /**
         * A Content Derivative that conveys sufficient information to determine the authorized entities with which the content may be shared.
         */
        SHAREABLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractContentDerivative fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registration".equals(codeString))
          return REGISTRATION;
        if ("retrieval".equals(codeString))
          return RETRIEVAL;
        if ("statement".equals(codeString))
          return STATEMENT;
        if ("shareable".equals(codeString))
          return SHAREABLE;
        throw new FHIRException("Unknown ContractContentDerivative code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTRATION: return "registration";
            case RETRIEVAL: return "retrieval";
            case STATEMENT: return "statement";
            case SHAREABLE: return "shareable";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contract-content-derivative";
        }
        public String getDefinition() {
          switch (this) {
            case REGISTRATION: return "Content derivative that conveys sufficient information needed to register the source basal content from which it is derived.  This derivative content may be used to register the basal content as it changes status in its lifecycle.  For example, content registration may occur when the basal content is created, updated, inactive, or deleted.";
            case RETRIEVAL: return "A content derivative that conveys sufficient information to locate and retrieve the content.";
            case STATEMENT: return "Content derivative that has less than full fidelity to the basal information source from which it was 'transcribed'. It provides recipients with the full content representation they may require for compliance purposes, and typically include a reference to or an attached unstructured representation for recipients needing an exact copy of the legal agreement.";
            case SHAREABLE: return "A Content Derivative that conveys sufficient information to determine the authorized entities with which the content may be shared.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTRATION: return "Content Registration";
            case RETRIEVAL: return "Content Retrieval";
            case STATEMENT: return "Content Statement";
            case SHAREABLE: return "Shareable Content";
            default: return "?";
          }
    }


}

