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

public enum ServicePlace {

        /**
         * A facility or location where drugs and other medically related items and services are sold, dispensed, or otherwise provided directly to patients.
         */
        _01, 
        /**
         * A facility whose primary purpose is education.
         */
        _03, 
        /**
         * A facility or location whose primary purpose is to provide temporary housing to homeless individuals (e.g., emergency shelters, individual or family shelters).
         */
        _04, 
        /**
         * A facility or location, owned and operated by the Indian Health Service, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services to American Indians and Alaska Natives who do not require hospitalization.
         */
        _05, 
        /**
         * A facility or location, owned and operated by the Indian Health Service, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services rendered by, or under the supervision of, physicians to American Indians and Alaska Natives admitted as inpatients or outpatients.
         */
        _06, 
        /**
         * A facility or location owned and operated by a federally recognized American Indian or Alaska Native tribe or tribal organization under a 638 agreement, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services to tribal members who do not require hospitalization.
         */
        _07, 
        /**
         * A facility or location owned and operated by a federally recognized American Indian or Alaska Native tribe or tribal organization under a 638 agreement, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services to tribal members admitted as inpatients or outpatients.
         */
        _08, 
        /**
         * A prison, jail, reformatory, work farm, detention center, or any other similar facility maintained by either Federal, State or local authorities for the purpose of confinement or rehabilitation of adult or juvenile criminal offenders.
         */
        _09, 
        /**
         * Location, other than a hospital, skilled nursing facility (SNF), military treatment facility, community health center, State or local public health clinic, or intermediate care facility (ICF), where the health professional routinely provides health examinations, diagnosis, and treatment of illness or injury on an ambulatory basis.
         */
        _11, 
        /**
         * Location, other than a hospital or other facility, where the patient receives care in a private residence.
         */
        _12, 
        /**
         * Congregate residential facility with self-contained living units providing assessment of each resident's needs and on-site support 24 hours a day, 7 days a week, with the capacity to deliver or arrange for services including some health care and other services.
         */
        _13, 
        /**
         * A residence, with shared living areas, where clients receive supervision and other services such as social and/or behavioral services, custodial service, and minimal services (e.g., medication administration).
         */
        _14, 
        /**
         * A facility/unit that moves from place-to-place equipped to provide preventive, screening, diagnostic, and/or treatment services.
         */
        _15, 
        /**
         * portion of an off-campus hospital provider based department which provides diagnostic, therapeutic (both surgical and nonsurgical), and rehabilitation services to sick or injured persons who do not require hospitalization or institutionalization.
         */
        _19, 
        /**
         * Location, distinct from a hospital emergency room, an office, or a clinic, whose purpose is to diagnose and treat illness or injury for unscheduled, ambulatory patients seeking immediate medical attention.
         */
        _20, 
        /**
         * A facility, other than psychiatric, which primarily provides diagnostic, therapeutic (both surgical and nonsurgical), and rehabilitation services by, or under, the supervision of physicians to patients admitted for a variety of medical conditions.
         */
        _21, 
        /**
         * A land vehicle specifically designed, equipped and staffed for lifesaving and transporting the sick or injured.
         */
        _41, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServicePlace fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("01".equals(codeString))
          return _01;
        if ("03".equals(codeString))
          return _03;
        if ("04".equals(codeString))
          return _04;
        if ("05".equals(codeString))
          return _05;
        if ("06".equals(codeString))
          return _06;
        if ("07".equals(codeString))
          return _07;
        if ("08".equals(codeString))
          return _08;
        if ("09".equals(codeString))
          return _09;
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
        if ("19".equals(codeString))
          return _19;
        if ("20".equals(codeString))
          return _20;
        if ("21".equals(codeString))
          return _21;
        if ("41".equals(codeString))
          return _41;
        throw new FHIRException("Unknown ServicePlace code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _01: return "01";
            case _03: return "03";
            case _04: return "04";
            case _05: return "05";
            case _06: return "06";
            case _07: return "07";
            case _08: return "08";
            case _09: return "09";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            case _19: return "19";
            case _20: return "20";
            case _21: return "21";
            case _41: return "41";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/ex-serviceplace";
        }
        public String getDefinition() {
          switch (this) {
            case _01: return "A facility or location where drugs and other medically related items and services are sold, dispensed, or otherwise provided directly to patients.";
            case _03: return "A facility whose primary purpose is education.";
            case _04: return "A facility or location whose primary purpose is to provide temporary housing to homeless individuals (e.g., emergency shelters, individual or family shelters).";
            case _05: return "A facility or location, owned and operated by the Indian Health Service, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services to American Indians and Alaska Natives who do not require hospitalization.";
            case _06: return "A facility or location, owned and operated by the Indian Health Service, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services rendered by, or under the supervision of, physicians to American Indians and Alaska Natives admitted as inpatients or outpatients.";
            case _07: return "A facility or location owned and operated by a federally recognized American Indian or Alaska Native tribe or tribal organization under a 638 agreement, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services to tribal members who do not require hospitalization.";
            case _08: return "A facility or location owned and operated by a federally recognized American Indian or Alaska Native tribe or tribal organization under a 638 agreement, which provides diagnostic, therapeutic (surgical and nonsurgical), and rehabilitation services to tribal members admitted as inpatients or outpatients.";
            case _09: return "A prison, jail, reformatory, work farm, detention center, or any other similar facility maintained by either Federal, State or local authorities for the purpose of confinement or rehabilitation of adult or juvenile criminal offenders.";
            case _11: return "Location, other than a hospital, skilled nursing facility (SNF), military treatment facility, community health center, State or local public health clinic, or intermediate care facility (ICF), where the health professional routinely provides health examinations, diagnosis, and treatment of illness or injury on an ambulatory basis.";
            case _12: return "Location, other than a hospital or other facility, where the patient receives care in a private residence.";
            case _13: return "Congregate residential facility with self-contained living units providing assessment of each resident's needs and on-site support 24 hours a day, 7 days a week, with the capacity to deliver or arrange for services including some health care and other services.";
            case _14: return "A residence, with shared living areas, where clients receive supervision and other services such as social and/or behavioral services, custodial service, and minimal services (e.g., medication administration).";
            case _15: return "A facility/unit that moves from place-to-place equipped to provide preventive, screening, diagnostic, and/or treatment services.";
            case _19: return "portion of an off-campus hospital provider based department which provides diagnostic, therapeutic (both surgical and nonsurgical), and rehabilitation services to sick or injured persons who do not require hospitalization or institutionalization.";
            case _20: return "Location, distinct from a hospital emergency room, an office, or a clinic, whose purpose is to diagnose and treat illness or injury for unscheduled, ambulatory patients seeking immediate medical attention.";
            case _21: return "A facility, other than psychiatric, which primarily provides diagnostic, therapeutic (both surgical and nonsurgical), and rehabilitation services by, or under, the supervision of physicians to patients admitted for a variety of medical conditions.";
            case _41: return "A land vehicle specifically designed, equipped and staffed for lifesaving and transporting the sick or injured.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _01: return "Pharmacy";
            case _03: return "School";
            case _04: return "Homeless Shelter";
            case _05: return "Indian Health Service Free-standing Facility";
            case _06: return "Indian Health Service Provider-based Facility";
            case _07: return "Tribal 638 Free-Standing Facility";
            case _08: return "Tribal 638 Provider-Based Facility";
            case _09: return "Prison/Correctional Facility";
            case _11: return "Office";
            case _12: return "Home";
            case _13: return "Assisted Living Fa";
            case _14: return "Group Home";
            case _15: return "Mobile Unit";
            case _19: return "Off Campus-Outpatient Hospital";
            case _20: return "Urgent Care Facility";
            case _21: return "Inpatient Hospital";
            case _41: return "Ambulance—Land";
            default: return "?";
          }
    }


}

