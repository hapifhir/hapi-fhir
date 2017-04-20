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

public enum ServiceCategory {

        /**
         * Adoption
         */
        _1, 
        /**
         * Aged Care
         */
        _2, 
        /**
         * Allied Health
         */
        _34, 
        /**
         * Alternative & Complementary Therapies
         */
        _3, 
        /**
         * Child Care and/or Kindergarten
         */
        _4, 
        /**
         * Child Development
         */
        _5, 
        /**
         * Child Protection & Family Services
         */
        _6, 
        /**
         * Community Health Care
         */
        _7, 
        /**
         * Counselling
         */
        _8, 
        /**
         * Crisis Line (GPAH use only)
         */
        _36, 
        /**
         * Death Services
         */
        _9, 
        /**
         * Dental
         */
        _10, 
        /**
         * Disability Support
         */
        _11, 
        /**
         * Drug/Alcohol
         */
        _12, 
        /**
         * Education & Learning
         */
        _13, 
        /**
         * Emergency Department
         */
        _14, 
        /**
         * Employment
         */
        _15, 
        /**
         * Financial & Material aid
         */
        _16, 
        /**
         * General Practice/GP (doctor)
         */
        _17, 
        /**
         * Hospital
         */
        _35, 
        /**
         * Housing/Homelessness
         */
        _18, 
        /**
         * Interpreting
         */
        _19, 
        /**
         * Justice
         */
        _20, 
        /**
         * Legal
         */
        _21, 
        /**
         * Mental Health
         */
        _22, 
        /**
         * NDIA
         */
        _38, 
        /**
         * Physical Activity & Recreation
         */
        _23, 
        /**
         * Regulation
         */
        _24, 
        /**
         * Respite/Carer Support
         */
        _25, 
        /**
         * Specialist Clinical Pathology - requires referral
         */
        _26, 
        /**
         * Specialist Medical - requires referral
         */
        _27, 
        /**
         * Specialist Obstetrics & Gynaecology - requires referral
         */
        _28, 
        /**
         * Specialist Paediatric - requires referral
         */
        _29, 
        /**
         * Specialist Radiology/Imaging - requires referral
         */
        _30, 
        /**
         * Specialist Surgical - requires referral
         */
        _31, 
        /**
         * Support group/s
         */
        _32, 
        /**
         * Test Message (HSD admin use only)
         */
        _37, 
        /**
         * Transport
         */
        _33, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("34".equals(codeString))
          return _34;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("6".equals(codeString))
          return _6;
        if ("7".equals(codeString))
          return _7;
        if ("8".equals(codeString))
          return _8;
        if ("36".equals(codeString))
          return _36;
        if ("9".equals(codeString))
          return _9;
        if ("10".equals(codeString))
          return _10;
        if ("11".equals(codeString))
          return _11;
        if ("12".equals(codeString))
          return _12;
        if ("13".equals(codeString))
          return _13;
        if ("14".equals(codeString))
          return _14;
        if ("15".equals(codeString))
          return _15;
        if ("16".equals(codeString))
          return _16;
        if ("17".equals(codeString))
          return _17;
        if ("35".equals(codeString))
          return _35;
        if ("18".equals(codeString))
          return _18;
        if ("19".equals(codeString))
          return _19;
        if ("20".equals(codeString))
          return _20;
        if ("21".equals(codeString))
          return _21;
        if ("22".equals(codeString))
          return _22;
        if ("38".equals(codeString))
          return _38;
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
        if ("29".equals(codeString))
          return _29;
        if ("30".equals(codeString))
          return _30;
        if ("31".equals(codeString))
          return _31;
        if ("32".equals(codeString))
          return _32;
        if ("37".equals(codeString))
          return _37;
        if ("33".equals(codeString))
          return _33;
        throw new FHIRException("Unknown ServiceCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _34: return "34";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _8: return "8";
            case _36: return "36";
            case _9: return "9";
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            case _16: return "16";
            case _17: return "17";
            case _35: return "35";
            case _18: return "18";
            case _19: return "19";
            case _20: return "20";
            case _21: return "21";
            case _22: return "22";
            case _38: return "38";
            case _23: return "23";
            case _24: return "24";
            case _25: return "25";
            case _26: return "26";
            case _27: return "27";
            case _28: return "28";
            case _29: return "29";
            case _30: return "30";
            case _31: return "31";
            case _32: return "32";
            case _37: return "37";
            case _33: return "33";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/service-category";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Adoption";
            case _2: return "Aged Care";
            case _34: return "Allied Health";
            case _3: return "Alternative & Complementary Therapies";
            case _4: return "Child Care and/or Kindergarten";
            case _5: return "Child Development";
            case _6: return "Child Protection & Family Services";
            case _7: return "Community Health Care";
            case _8: return "Counselling";
            case _36: return "Crisis Line (GPAH use only)";
            case _9: return "Death Services";
            case _10: return "Dental";
            case _11: return "Disability Support";
            case _12: return "Drug/Alcohol";
            case _13: return "Education & Learning";
            case _14: return "Emergency Department";
            case _15: return "Employment";
            case _16: return "Financial & Material aid";
            case _17: return "General Practice/GP (doctor)";
            case _35: return "Hospital";
            case _18: return "Housing/Homelessness";
            case _19: return "Interpreting";
            case _20: return "Justice";
            case _21: return "Legal";
            case _22: return "Mental Health";
            case _38: return "NDIA";
            case _23: return "Physical Activity & Recreation";
            case _24: return "Regulation";
            case _25: return "Respite/Carer Support";
            case _26: return "Specialist Clinical Pathology - requires referral";
            case _27: return "Specialist Medical - requires referral";
            case _28: return "Specialist Obstetrics & Gynaecology - requires referral";
            case _29: return "Specialist Paediatric - requires referral";
            case _30: return "Specialist Radiology/Imaging - requires referral";
            case _31: return "Specialist Surgical - requires referral";
            case _32: return "Support group/s";
            case _37: return "Test Message (HSD admin use only)";
            case _33: return "Transport";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Adoption";
            case _2: return "Aged Care";
            case _34: return "Allied Health";
            case _3: return "Alternative/Complementary Therapies";
            case _4: return "Child Care /Kindergarten";
            case _5: return "Child Development";
            case _6: return "Child Protection & Family Services";
            case _7: return "Community Health Care";
            case _8: return "Counselling";
            case _36: return "Crisis Line (GPAH use only)";
            case _9: return "Death Services";
            case _10: return "Dental";
            case _11: return "Disability Support";
            case _12: return "Drug/Alcohol";
            case _13: return "Education & Learning";
            case _14: return "Emergency Department";
            case _15: return "Employment";
            case _16: return "Financial & Material Aid";
            case _17: return "General Practice";
            case _35: return "Hospital";
            case _18: return "Housing/Homelessness";
            case _19: return "Interpreting";
            case _20: return "Justice";
            case _21: return "Legal";
            case _22: return "Mental Health";
            case _38: return "NDIA";
            case _23: return "Physical Activity & Recreation";
            case _24: return "Regulation";
            case _25: return "Respite/Carer Support";
            case _26: return "Specialist Clinical Pathology";
            case _27: return "Specialist Medical";
            case _28: return "Specialist Obstetrics & Gynaecology";
            case _29: return "Specialist Paediatric";
            case _30: return "Specialist Radiology/Imaging";
            case _31: return "Specialist Surgical";
            case _32: return "Support Group/s";
            case _37: return "Test Message (HSD admin)";
            case _33: return "Transport";
            default: return "?";
          }
    }


}

