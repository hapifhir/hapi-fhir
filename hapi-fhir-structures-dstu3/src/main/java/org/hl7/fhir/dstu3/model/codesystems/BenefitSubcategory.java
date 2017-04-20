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

public enum BenefitSubcategory {

        /**
         * Medical Care.
         */
        _1, 
        /**
         * Surgical.
         */
        _2, 
        /**
         * Consultation.
         */
        _3, 
        /**
         * Diagnostic XRay.
         */
        _4, 
        /**
         * Diagnostic Lab.
         */
        _5, 
        /**
         * Renal Supplies excluding Dialysis.
         */
        _14, 
        /**
         * Diagnostic Dental.
         */
        _23, 
        /**
         * Periodontics.
         */
        _24, 
        /**
         * Restorative.
         */
        _25, 
        /**
         * Endodontics.
         */
        _26, 
        /**
         * Maxillofacilial Prosthetics.
         */
        _27, 
        /**
         * Adjunctive Dental Services.
         */
        _28, 
        /**
         * Health Benefit Plan Coverage.
         */
        _30, 
        /**
         * Dental Care.
         */
        _35, 
        /**
         * Dental Crowns.
         */
        _36, 
        /**
         * Dental Accident.
         */
        _37, 
        /**
         * Hospital Room and Board.
         */
        _49, 
        /**
         * Major Medical.
         */
        _55, 
        /**
         * Medically Related Transportation.
         */
        _56, 
        /**
         * In-vitro Fertilization.
         */
        _61, 
        /**
         * MRI Scan.
         */
        _62, 
        /**
         * Donor Procedures such as organ harvest.
         */
        _63, 
        /**
         * Maternity.
         */
        _69, 
        /**
         * Renal dialysis.
         */
        _76, 
        /**
         * Medical Coverage.
         */
        F1, 
        /**
         * Dental Coverage.
         */
        F3, 
        /**
         * Hearing Coverage.
         */
        F4, 
        /**
         * Vision Coverage.
         */
        F6, 
        /**
         * added to help the parsers
         */
        NULL;
        public static BenefitSubcategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("14".equals(codeString))
          return _14;
        if ("23".equals(codeString))
          return _23;
        if ("24".equals(codeString))
          return _24;
        if ("25".equals(codeString))
          return _25;
        if ("26".equals(codeString))
          return _26;
        if ("27".equals(codeString))
          return _27;
        if ("28".equals(codeString))
          return _28;
        if ("30".equals(codeString))
          return _30;
        if ("35".equals(codeString))
          return _35;
        if ("36".equals(codeString))
          return _36;
        if ("37".equals(codeString))
          return _37;
        if ("49".equals(codeString))
          return _49;
        if ("55".equals(codeString))
          return _55;
        if ("56".equals(codeString))
          return _56;
        if ("61".equals(codeString))
          return _61;
        if ("62".equals(codeString))
          return _62;
        if ("63".equals(codeString))
          return _63;
        if ("69".equals(codeString))
          return _69;
        if ("76".equals(codeString))
          return _76;
        if ("F1".equals(codeString))
          return F1;
        if ("F3".equals(codeString))
          return F3;
        if ("F4".equals(codeString))
          return F4;
        if ("F6".equals(codeString))
          return F6;
        throw new FHIRException("Unknown BenefitSubcategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _14: return "14";
            case _23: return "23";
            case _24: return "24";
            case _25: return "25";
            case _26: return "26";
            case _27: return "27";
            case _28: return "28";
            case _30: return "30";
            case _35: return "35";
            case _36: return "36";
            case _37: return "37";
            case _49: return "49";
            case _55: return "55";
            case _56: return "56";
            case _61: return "61";
            case _62: return "62";
            case _63: return "63";
            case _69: return "69";
            case _76: return "76";
            case F1: return "F1";
            case F3: return "F3";
            case F4: return "F4";
            case F6: return "F6";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/benefit-subcategory";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Medical Care.";
            case _2: return "Surgical.";
            case _3: return "Consultation.";
            case _4: return "Diagnostic XRay.";
            case _5: return "Diagnostic Lab.";
            case _14: return "Renal Supplies excluding Dialysis.";
            case _23: return "Diagnostic Dental.";
            case _24: return "Periodontics.";
            case _25: return "Restorative.";
            case _26: return "Endodontics.";
            case _27: return "Maxillofacilial Prosthetics.";
            case _28: return "Adjunctive Dental Services.";
            case _30: return "Health Benefit Plan Coverage.";
            case _35: return "Dental Care.";
            case _36: return "Dental Crowns.";
            case _37: return "Dental Accident.";
            case _49: return "Hospital Room and Board.";
            case _55: return "Major Medical.";
            case _56: return "Medically Related Transportation.";
            case _61: return "In-vitro Fertilization.";
            case _62: return "MRI Scan.";
            case _63: return "Donor Procedures such as organ harvest.";
            case _69: return "Maternity.";
            case _76: return "Renal dialysis.";
            case F1: return "Medical Coverage.";
            case F3: return "Dental Coverage.";
            case F4: return "Hearing Coverage.";
            case F6: return "Vision Coverage.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Medical Care";
            case _2: return "Surgical";
            case _3: return "Consultation";
            case _4: return "Diagnostic XRay";
            case _5: return "Diagnostic Lab";
            case _14: return "Renal Supplies";
            case _23: return "Diagnostic Dental";
            case _24: return "Periodontics";
            case _25: return "Restorative";
            case _26: return "Endodontics";
            case _27: return "Maxillofacilial Prosthetics";
            case _28: return "Adjunctive Dental Services";
            case _30: return "Health Benefit Plan Coverage";
            case _35: return "Dental Care";
            case _36: return "Dental Crowns";
            case _37: return "Dental Accident";
            case _49: return "Hospital Room and Board";
            case _55: return "Major Medical";
            case _56: return "Medically Related Transportation";
            case _61: return "In-vitro Fertilization";
            case _62: return "MRI Scan";
            case _63: return "Donor Procedures";
            case _69: return "Maternity";
            case _76: return "Renal Dialysis";
            case F1: return "Medical Coverage";
            case F3: return "Dental Coverage";
            case F4: return "Hearing Coverage";
            case F6: return "Vision Coverage";
            default: return "?";
          }
    }


}

