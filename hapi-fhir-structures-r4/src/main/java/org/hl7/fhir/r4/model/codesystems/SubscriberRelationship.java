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

public enum SubscriberRelationship {

        /**
         * The Beneficiary is a child of the Subscriber
         */
        CHILD, 
        /**
         * The Beneficiary is a parent of the Subscriber
         */
        PARENT, 
        /**
         * The Beneficiary is a spouse or equivalent of the Subscriber
         */
        SPOUSE, 
        /**
         * The Beneficiary is a common law spouse or equivalent of the Subscriber
         */
        COMMON, 
        /**
         * The Beneficiary has some other relationship the Subscriber
         */
        OTHER, 
        /**
         * The Beneficiary is the Subscriber
         */
        SELF, 
        /**
         * The Beneficiary is covered under insurance of the subscriber due to an injury.
         */
        INJURED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SubscriberRelationship fromCode(String codeString) throws FHIRException {
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
        if ("injured".equals(codeString))
          return INJURED;
        throw new FHIRException("Unknown SubscriberRelationship code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CHILD: return "child";
            case PARENT: return "parent";
            case SPOUSE: return "spouse";
            case COMMON: return "common";
            case OTHER: return "other";
            case SELF: return "self";
            case INJURED: return "injured";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/subscriber-relationship";
        }
        public String getDefinition() {
          switch (this) {
            case CHILD: return "The Beneficiary is a child of the Subscriber";
            case PARENT: return "The Beneficiary is a parent of the Subscriber";
            case SPOUSE: return "The Beneficiary is a spouse or equivalent of the Subscriber";
            case COMMON: return "The Beneficiary is a common law spouse or equivalent of the Subscriber";
            case OTHER: return "The Beneficiary has some other relationship the Subscriber";
            case SELF: return "The Beneficiary is the Subscriber";
            case INJURED: return "The Beneficiary is covered under insurance of the subscriber due to an injury.";
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
            case INJURED: return "Injured Party";
            default: return "?";
          }
    }


}

