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

public enum ConditionClinical {

        /**
         * The subject is currently experiencing the symptoms of the condition or there is evidence of the condition.
         */
        ACTIVE, 
        /**
         * The subject is having a relapse or re-experiencing the condition after a period of remission or presumed resolution.
         */
        RECURRENCE, 
        /**
         * The subject is no longer experiencing the symptoms of the condition or there is no longer evidence of the condition.
         */
        INACTIVE, 
        /**
         * The subject is no longer experiencing the symptoms of the condition, but there is a risk of the symptoms returning.
         */
        REMISSION, 
        /**
         * The subject is no longer experiencing the symptoms of the condition and there is a negligible perceived risk of the symptoms returning.
         */
        RESOLVED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionClinical fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("recurrence".equals(codeString))
          return RECURRENCE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("remission".equals(codeString))
          return REMISSION;
        if ("resolved".equals(codeString))
          return RESOLVED;
        throw new FHIRException("Unknown ConditionClinical code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case RECURRENCE: return "recurrence";
            case INACTIVE: return "inactive";
            case REMISSION: return "remission";
            case RESOLVED: return "resolved";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/condition-clinical";
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The subject is currently experiencing the symptoms of the condition or there is evidence of the condition.";
            case RECURRENCE: return "The subject is having a relapse or re-experiencing the condition after a period of remission or presumed resolution.";
            case INACTIVE: return "The subject is no longer experiencing the symptoms of the condition or there is no longer evidence of the condition.";
            case REMISSION: return "The subject is no longer experiencing the symptoms of the condition, but there is a risk of the symptoms returning.";
            case RESOLVED: return "The subject is no longer experiencing the symptoms of the condition and there is a negligible perceived risk of the symptoms returning.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case RECURRENCE: return "Recurrence";
            case INACTIVE: return "Inactive";
            case REMISSION: return "Remission";
            case RESOLVED: return "Resolved";
            default: return "?";
          }
    }


}

