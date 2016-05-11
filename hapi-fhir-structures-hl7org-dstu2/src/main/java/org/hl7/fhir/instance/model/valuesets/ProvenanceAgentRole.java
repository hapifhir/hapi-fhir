package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum ProvenanceAgentRole {

        /**
         * A person entering the data into the originating system
         */
        ENTERER, 
        /**
         * A person, animal, organization or device that who actually and principally carries out the activity
         */
        PERFORMER, 
        /**
         * A party that originates the resource and therefore has responsibility for the information given in the resource and ownership of this resource
         */
        AUTHOR, 
        /**
         * A person who verifies the correctness and appropriateness of activity
         */
        VERIFIER, 
        /**
         * The person authenticated the content and accepted legal responsibility for its content
         */
        LEGAL, 
        /**
         * A verifier who attests to the accuracy of the resource
         */
        ATTESTER, 
        /**
         * A person who reported information that contributed to the resource
         */
        INFORMANT, 
        /**
         * The entity that is accountable for maintaining a true an accurate copy of the original record
         */
        CUSTODIAN, 
        /**
         * A device that operates independently of an author on custodian's algorithms for data extraction of existing information for purpose of generating a new artifact.
         */
        ASSEMBLER, 
        /**
         * A device used by an author to record new information, which may also be used by the author to select existing information for aggregation with newly recorded information for the purpose of generating a new artifact.
         */
        COMPOSER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProvenanceAgentRole fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("enterer".equals(codeString))
          return ENTERER;
        if ("performer".equals(codeString))
          return PERFORMER;
        if ("author".equals(codeString))
          return AUTHOR;
        if ("verifier".equals(codeString))
          return VERIFIER;
        if ("legal".equals(codeString))
          return LEGAL;
        if ("attester".equals(codeString))
          return ATTESTER;
        if ("informant".equals(codeString))
          return INFORMANT;
        if ("custodian".equals(codeString))
          return CUSTODIAN;
        if ("assembler".equals(codeString))
          return ASSEMBLER;
        if ("composer".equals(codeString))
          return COMPOSER;
        throw new Exception("Unknown ProvenanceAgentRole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ENTERER: return "enterer";
            case PERFORMER: return "performer";
            case AUTHOR: return "author";
            case VERIFIER: return "verifier";
            case LEGAL: return "legal";
            case ATTESTER: return "attester";
            case INFORMANT: return "informant";
            case CUSTODIAN: return "custodian";
            case ASSEMBLER: return "assembler";
            case COMPOSER: return "composer";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/provenance-participant-role";
        }
        public String getDefinition() {
          switch (this) {
            case ENTERER: return "A person entering the data into the originating system";
            case PERFORMER: return "A person, animal, organization or device that who actually and principally carries out the activity";
            case AUTHOR: return "A party that originates the resource and therefore has responsibility for the information given in the resource and ownership of this resource";
            case VERIFIER: return "A person who verifies the correctness and appropriateness of activity";
            case LEGAL: return "The person authenticated the content and accepted legal responsibility for its content";
            case ATTESTER: return "A verifier who attests to the accuracy of the resource";
            case INFORMANT: return "A person who reported information that contributed to the resource";
            case CUSTODIAN: return "The entity that is accountable for maintaining a true an accurate copy of the original record";
            case ASSEMBLER: return "A device that operates independently of an author on custodian's algorithms for data extraction of existing information for purpose of generating a new artifact.";
            case COMPOSER: return "A device used by an author to record new information, which may also be used by the author to select existing information for aggregation with newly recorded information for the purpose of generating a new artifact.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ENTERER: return "Enterer";
            case PERFORMER: return "Performer";
            case AUTHOR: return "Author";
            case VERIFIER: return "Verifier";
            case LEGAL: return "Legal Authenticator";
            case ATTESTER: return "Attester";
            case INFORMANT: return "Informant";
            case CUSTODIAN: return "Custodian";
            case ASSEMBLER: return "Assembler";
            case COMPOSER: return "Composer";
            default: return "?";
          }
    }


}

