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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum PolicyholderRelationship {

        /**
         * The Beneficiary is a child of the Policyholder
         */
        CHILD, 
        /**
         * The Beneficiary is a parent of the Policyholder
         */
        PARENT, 
        /**
         * The Beneficiary is a spouse or equivalent of the Policyholder
         */
        SPOUSE, 
        /**
         * The Beneficiary is a common law spouse or equivalent of the Policyholder
         */
        COMMON, 
        /**
         * The Beneficiary has some other relationship the Policyholder
         */
        OTHER, 
        /**
         * The Beneficiary is the Policyholder
         */
        SELF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PolicyholderRelationship fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("child".equals(codeString))
          return CHILD;
        if ("parent".equals(codeString))
          return PARENT;
        if ("spouse".equals(codeString))
          return SPOUSE;
        if ("common".equals(codeString))
          return COMMON;
        if ("other".equals(codeString))
          return OTHER;
        if ("self".equals(codeString))
          return SELF;
        throw new FHIRException("Unknown PolicyholderRelationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CHILD: return "child";
            case PARENT: return "parent";
            case SPOUSE: return "spouse";
            case COMMON: return "common";
            case OTHER: return "other";
            case SELF: return "self";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/policyholder-relationship";
        }
        public String getDefinition() {
          switch (this) {
            case CHILD: return "The Beneficiary is a child of the Policyholder";
            case PARENT: return "The Beneficiary is a parent of the Policyholder";
            case SPOUSE: return "The Beneficiary is a spouse or equivalent of the Policyholder";
            case COMMON: return "The Beneficiary is a common law spouse or equivalent of the Policyholder";
            case OTHER: return "The Beneficiary has some other relationship the Policyholder";
            case SELF: return "The Beneficiary is the Policyholder";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CHILD: return "Child";
            case PARENT: return "Parent";
            case SPOUSE: return "Spouse";
            case COMMON: return "Common Law Spouse";
            case OTHER: return "Other";
            case SELF: return "Self";
            default: return "?";
          }
    }


}

