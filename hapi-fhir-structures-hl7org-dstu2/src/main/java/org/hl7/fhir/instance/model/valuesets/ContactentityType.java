package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum ContactentityType {

        /**
         * Contact details for information regarding to billing/general finance enquiries
         */
        BILL, 
        /**
         * Contact details for administrative enquiries
         */
        ADMIN, 
        /**
         * Contact details for issues related to Human Resources, such as staff matters, OH&S etc
         */
        HR, 
        /**
         * Contact details for dealing with issues related to insurance claims/adjudication/payment
         */
        PAYOR, 
        /**
         * Generic information contact for patients
         */
        PATINF, 
        /**
         * Dedicated contact point for matters relating to press enquiries
         */
        PRESS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContactentityType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BILL".equals(codeString))
          return BILL;
        if ("ADMIN".equals(codeString))
          return ADMIN;
        if ("HR".equals(codeString))
          return HR;
        if ("PAYOR".equals(codeString))
          return PAYOR;
        if ("PATINF".equals(codeString))
          return PATINF;
        if ("PRESS".equals(codeString))
          return PRESS;
        throw new Exception("Unknown ContactentityType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BILL: return "BILL";
            case ADMIN: return "ADMIN";
            case HR: return "HR";
            case PAYOR: return "PAYOR";
            case PATINF: return "PATINF";
            case PRESS: return "PRESS";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contactentity-type";
        }
        public String getDefinition() {
          switch (this) {
            case BILL: return "Contact details for information regarding to billing/general finance enquiries";
            case ADMIN: return "Contact details for administrative enquiries";
            case HR: return "Contact details for issues related to Human Resources, such as staff matters, OH&S etc";
            case PAYOR: return "Contact details for dealing with issues related to insurance claims/adjudication/payment";
            case PATINF: return "Generic information contact for patients";
            case PRESS: return "Dedicated contact point for matters relating to press enquiries";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BILL: return "Billing";
            case ADMIN: return "Administrative";
            case HR: return "Human Resource";
            case PAYOR: return "Payor";
            case PATINF: return "Patient";
            case PRESS: return "Press";
            default: return "?";
          }
    }


}

