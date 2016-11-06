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

// Generated on Sat, Nov 5, 2016 08:41-0400 for FHIR v1.7.0


import org.hl7.fhir.exceptions.FHIRException;

public enum SupplyrequestStatus {

        /**
         * Supply has been requested, but not dispensed.
         */
        REQUESTED, 
        /**
         * Supply has been received by the requestor.
         */
        COMPLETED, 
        /**
         * The supply will not be completed because the supplier was unable or unwilling to supply the item.
         */
        FAILED, 
        /**
         * The orderer of the supply cancelled the request.
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SupplyrequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("failed".equals(codeString))
          return FAILED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new FHIRException("Unknown SupplyrequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REQUESTED: return "requested";
            case COMPLETED: return "completed";
            case FAILED: return "failed";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/supplyrequest-status";
        }
        public String getDefinition() {
          switch (this) {
            case REQUESTED: return "Supply has been requested, but not dispensed.";
            case COMPLETED: return "Supply has been received by the requestor.";
            case FAILED: return "The supply will not be completed because the supplier was unable or unwilling to supply the item.";
            case CANCELLED: return "The orderer of the supply cancelled the request.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUESTED: return "Requested";
            case COMPLETED: return "Received";
            case FAILED: return "Failed";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
    }


}

