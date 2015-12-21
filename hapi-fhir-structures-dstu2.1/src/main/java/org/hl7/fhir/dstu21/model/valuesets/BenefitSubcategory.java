package org.hl7.fhir.dstu21.model.valuesets;

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

// Generated on Sun, Dec 20, 2015 20:55-0500 for FHIR v1.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum BenefitSubcategory {

        /**
         * Dental Basic Coverage - exams, fillings, xrays, cleaning, minor procedures
         */
        ORALBASIC, 
        /**
         * Dental Major Coverage - crowns, bridge, major procedures
         */
        ORALMAJOR, 
        /**
         * Dental Orthodontic Coverage - braces, adjustments, ortho exams, related procedures
         */
        ORALORTHO, 
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
        public static BenefitSubcategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("oral-basic".equals(codeString))
          return ORALBASIC;
        if ("oral-major".equals(codeString))
          return ORALMAJOR;
        if ("oral-ortho".equals(codeString))
          return ORALORTHO;
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
        throw new FHIRException("Unknown BenefitSubcategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ORALBASIC: return "oral-basic";
            case ORALMAJOR: return "oral-major";
            case ORALORTHO: return "oral-ortho";
            case VISIONEXAM: return "vision-exam";
            case VISIONGLASSES: return "vision-glasses";
            case VISIONCONTACTS: return "vision-contacts";
            case MEDICALPRIMARYCARE: return "medical-primarycare";
            case PHARMACYDISPENSE: return "pharmacy-dispense";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-subcategory";
        }
        public String getDefinition() {
          switch (this) {
            case ORALBASIC: return "Dental Basic Coverage - exams, fillings, xrays, cleaning, minor procedures";
            case ORALMAJOR: return "Dental Major Coverage - crowns, bridge, major procedures";
            case ORALORTHO: return "Dental Orthodontic Coverage - braces, adjustments, ortho exams, related procedures";
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
            case ORALBASIC: return "Dental: Basic Coverage";
            case ORALMAJOR: return "Dental: Major Coverage";
            case ORALORTHO: return "Dental: Orthodontic Coverage";
            case VISIONEXAM: return "Vision Exam";
            case VISIONGLASSES: return "Vision Glasses";
            case VISIONCONTACTS: return "Vision Contacts Coverage";
            case MEDICALPRIMARYCARE: return "Medical Primary Health Coverage";
            case PHARMACYDISPENSE: return "Pharmacy Dispense Coverage";
            default: return "?";
          }
    }


}

