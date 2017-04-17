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

public enum BenefitType {

        /**
         * Maximum benefit allowable.
         */
        BENEFIT, 
        /**
         * Cost to be incurred before benefits are applied
         */
        DEDUCTABLE, 
        /**
         * Service visit
         */
        VISIT, 
        /**
         * Type of room
         */
        ROOM, 
        /**
         * Copayment per service
         */
        COPAY, 
        /**
         * Copayment percentage per service
         */
        COPAYPERCENT, 
        /**
         * Copayment maximum per service
         */
        COPAYMAXIMUM, 
        /**
         * Vision Exam
         */
        VISIONEXAM, 
        /**
         * Frames and lenses
         */
        VISIONGLASSES, 
        /**
         * Contact Lenses
         */
        VISIONCONTACTS, 
        /**
         * Medical Primary Health Coverage
         */
        MEDICALPRIMARYCARE, 
        /**
         * Pharmacy Dispense Coverage
         */
        PHARMACYDISPENSE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BenefitType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("benefit".equals(codeString))
          return BENEFIT;
        if ("deductable".equals(codeString))
          return DEDUCTABLE;
        if ("visit".equals(codeString))
          return VISIT;
        if ("room".equals(codeString))
          return ROOM;
        if ("copay".equals(codeString))
          return COPAY;
        if ("copay-percent".equals(codeString))
          return COPAYPERCENT;
        if ("copay-maximum".equals(codeString))
          return COPAYMAXIMUM;
        if ("vision-exam".equals(codeString))
          return VISIONEXAM;
        if ("vision-glasses".equals(codeString))
          return VISIONGLASSES;
        if ("vision-contacts".equals(codeString))
          return VISIONCONTACTS;
        if ("medical-primarycare".equals(codeString))
          return MEDICALPRIMARYCARE;
        if ("pharmacy-dispense".equals(codeString))
          return PHARMACYDISPENSE;
        throw new FHIRException("Unknown BenefitType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BENEFIT: return "benefit";
            case DEDUCTABLE: return "deductable";
            case VISIT: return "visit";
            case ROOM: return "room";
            case COPAY: return "copay";
            case COPAYPERCENT: return "copay-percent";
            case COPAYMAXIMUM: return "copay-maximum";
            case VISIONEXAM: return "vision-exam";
            case VISIONGLASSES: return "vision-glasses";
            case VISIONCONTACTS: return "vision-contacts";
            case MEDICALPRIMARYCARE: return "medical-primarycare";
            case PHARMACYDISPENSE: return "pharmacy-dispense";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-type";
        }
        public String getDefinition() {
          switch (this) {
            case BENEFIT: return "Maximum benefit allowable.";
            case DEDUCTABLE: return "Cost to be incurred before benefits are applied";
            case VISIT: return "Service visit";
            case ROOM: return "Type of room";
            case COPAY: return "Copayment per service";
            case COPAYPERCENT: return "Copayment percentage per service";
            case COPAYMAXIMUM: return "Copayment maximum per service";
            case VISIONEXAM: return "Vision Exam";
            case VISIONGLASSES: return "Frames and lenses";
            case VISIONCONTACTS: return "Contact Lenses";
            case MEDICALPRIMARYCARE: return "Medical Primary Health Coverage";
            case PHARMACYDISPENSE: return "Pharmacy Dispense Coverage";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BENEFIT: return "Benefit";
            case DEDUCTABLE: return "Deductable";
            case VISIT: return "Visit";
            case ROOM: return "Room";
            case COPAY: return "Copayment per service";
            case COPAYPERCENT: return "Copayment Percent per service";
            case COPAYMAXIMUM: return "Copayment maximum per service";
            case VISIONEXAM: return "Vision Exam";
            case VISIONGLASSES: return "Vision Glasses";
            case VISIONCONTACTS: return "Vision Contacts Coverage";
            case MEDICALPRIMARYCARE: return "Medical Primary Health Coverage";
            case PHARMACYDISPENSE: return "Pharmacy Dispense Coverage";
            default: return "?";
          }
    }


}

