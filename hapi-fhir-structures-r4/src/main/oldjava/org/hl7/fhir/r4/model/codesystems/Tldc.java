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

public enum Tldc {

        /**
         * Design is under development (nascent).
         */
        DRAFT, 
        /**
         * Design is completed and is being reviewed.
         */
        PENDING, 
        /**
         * Design has been deemed fit for the intended purpose and is published by the governance group.
         */
        ACTIVE, 
        /**
         * Design is active, but is under review. The review may result in a change to the design. The change may necessitate a new version to be created. This in turn may result in the prior version of the template to be retired. Alternatively, the review may result in a change to the design that does not require a new version to be created, or it may result in no change to the design at all.
         */
        REVIEW, 
        /**
         * A drafted design is determined to be erroneous or not fit for intended purpose and is discontinued before ever being published in an active state.
         */
        CANCELLED, 
        /**
         * A previously drafted design is determined to be erroneous or not fit for intended purpose and is discontinued before ever being published for consideration in a pending state.
         */
        REJECTED, 
        /**
         * A previously active design is discontinued from use. It should no longer be used for future designs, but for historical purposes may be used to process data previously recorded using this design. A newer design may or may not exist. The design is published in the retired state.
         */
        RETIRED, 
        /**
         * A design is determined to be erroneous or not fit for the intended purpose and should no longer be used, even for historical purposes. No new designs can be developed for this template. The associated template no longer needs to be published, but if published, is shown in the terminated state.
         */
        TERMINATED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Tldc fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("pending".equals(codeString))
          return PENDING;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("review".equals(codeString))
          return REVIEW;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("retired".equals(codeString))
          return RETIRED;
        if ("terminated".equals(codeString))
          return TERMINATED;
        throw new FHIRException("Unknown Tldc code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case PENDING: return "pending";
            case ACTIVE: return "active";
            case REVIEW: return "review";
            case CANCELLED: return "cancelled";
            case REJECTED: return "rejected";
            case RETIRED: return "retired";
            case TERMINATED: return "terminated";
            default: return "?";
          }
        }
        public String getSystem() {
          return "urn:oid:2.16.840.1.113883.3.1937.98.5.8";
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "Design is under development (nascent).";
            case PENDING: return "Design is completed and is being reviewed.";
            case ACTIVE: return "Design has been deemed fit for the intended purpose and is published by the governance group.";
            case REVIEW: return "Design is active, but is under review. The review may result in a change to the design. The change may necessitate a new version to be created. This in turn may result in the prior version of the template to be retired. Alternatively, the review may result in a change to the design that does not require a new version to be created, or it may result in no change to the design at all.";
            case CANCELLED: return "A drafted design is determined to be erroneous or not fit for intended purpose and is discontinued before ever being published in an active state.";
            case REJECTED: return "A previously drafted design is determined to be erroneous or not fit for intended purpose and is discontinued before ever being published for consideration in a pending state.";
            case RETIRED: return "A previously active design is discontinued from use. It should no longer be used for future designs, but for historical purposes may be used to process data previously recorded using this design. A newer design may or may not exist. The design is published in the retired state.";
            case TERMINATED: return "A design is determined to be erroneous or not fit for the intended purpose and should no longer be used, even for historical purposes. No new designs can be developed for this template. The associated template no longer needs to be published, but if published, is shown in the terminated state.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case PENDING: return "Under pre-publication review";
            case ACTIVE: return "active";
            case REVIEW: return "In Review";
            case CANCELLED: return "Cancelled";
            case REJECTED: return "Rejected";
            case RETIRED: return "retired";
            case TERMINATED: return "Terminated";
            default: return "?";
          }
    }


}

