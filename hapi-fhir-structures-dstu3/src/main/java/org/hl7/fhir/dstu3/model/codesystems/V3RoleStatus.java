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

public enum V3RoleStatus {

        /**
         * The 'typical' state. Excludes "nullified" which represents the termination state of a Role instance that was created in error.
         */
        NORMAL, 
        /**
         * The state representing the fact that the Entity is currently active in the Role.
         */
        ACTIVE, 
        /**
         * The terminal state resulting from cancellation of the role prior to activation.
         */
        CANCELLED, 
        /**
         * The state representing that fact that the role has not yet become active.
         */
        PENDING, 
        /**
         * The state that represents a suspension of the Entity playing the Role. This state is arrived at from the "active" state.
         */
        SUSPENDED, 
        /**
         * The state representing the successful termination of the Role.
         */
        TERMINATED, 
        /**
         * The state representing the termination of a Role instance that was created in error.
         */
        NULLIFIED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RoleStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("pending".equals(codeString))
          return PENDING;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("terminated".equals(codeString))
          return TERMINATED;
        if ("nullified".equals(codeString))
          return NULLIFIED;
        throw new FHIRException("Unknown V3RoleStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "normal";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case PENDING: return "pending";
            case SUSPENDED: return "suspended";
            case TERMINATED: return "terminated";
            case NULLIFIED: return "nullified";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RoleStatus";
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "The 'typical' state. Excludes \"nullified\" which represents the termination state of a Role instance that was created in error.";
            case ACTIVE: return "The state representing the fact that the Entity is currently active in the Role.";
            case CANCELLED: return "The terminal state resulting from cancellation of the role prior to activation.";
            case PENDING: return "The state representing that fact that the role has not yet become active.";
            case SUSPENDED: return "The state that represents a suspension of the Entity playing the Role. This state is arrived at from the \"active\" state.";
            case TERMINATED: return "The state representing the successful termination of the Role.";
            case NULLIFIED: return "The state representing the termination of a Role instance that was created in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "normal";
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case PENDING: return "pending";
            case SUSPENDED: return "suspended";
            case TERMINATED: return "terminated";
            case NULLIFIED: return "nullified";
            default: return "?";
          }
    }


}

