package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ContactentityType {

        /**
         * Contact details for information regarding to billing/general finance enquiries.
         */
        BILL, 
        /**
         * Contact details for administrative enquiries.
         */
        ADMIN, 
        /**
         * Contact details for issues related to Human Resources, such as staff matters, OH&S etc.
         */
        HR, 
        /**
         * Contact details for dealing with issues related to insurance claims/adjudication/payment.
         */
        PAYOR, 
        /**
         * Generic information contact for patients.
         */
        PATINF, 
        /**
         * Dedicated contact point for matters relating to press enquiries.
         */
        PRESS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContactentityType fromCode(String codeString) throws FHIRException {
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
        throw new FHIRException("Unknown ContactentityType code '"+codeString+"'");
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
            case BILL: return "Contact details for information regarding to billing/general finance enquiries.";
            case ADMIN: return "Contact details for administrative enquiries.";
            case HR: return "Contact details for issues related to Human Resources, such as staff matters, OH&S etc.";
            case PAYOR: return "Contact details for dealing with issues related to insurance claims/adjudication/payment.";
            case PATINF: return "Generic information contact for patients.";
            case PRESS: return "Dedicated contact point for matters relating to press enquiries.";
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

