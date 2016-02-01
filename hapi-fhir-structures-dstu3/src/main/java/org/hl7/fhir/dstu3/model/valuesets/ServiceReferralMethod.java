package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum ServiceReferralMethod {

        /**
         * Referrals may be accepted by fax.
         */
        FAX, 
        /**
         * Referrals may be accepted over the phone from a practitioner.
         */
        PHONE, 
        /**
         * Referrals may be accepted via a secure messaging system. To determine the types of secure messaging systems supported, refer to the identifiers collection. Callers will need to understand the specific identifier system used to know that they are able to transmit messages.
         */
        ELEC, 
        /**
         * Referrals may be accepted via a secure email. To send please enrypt with the services public key.
         */
        SEMAIL, 
        /**
         * Referrals may be accepted via regular postage (or hand delivered).
         */
        MAIL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceReferralMethod fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("fax".equals(codeString))
          return FAX;
        if ("phone".equals(codeString))
          return PHONE;
        if ("elec".equals(codeString))
          return ELEC;
        if ("semail".equals(codeString))
          return SEMAIL;
        if ("mail".equals(codeString))
          return MAIL;
        throw new FHIRException("Unknown ServiceReferralMethod code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FAX: return "fax";
            case PHONE: return "phone";
            case ELEC: return "elec";
            case SEMAIL: return "semail";
            case MAIL: return "mail";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/service-referral-method";
        }
        public String getDefinition() {
          switch (this) {
            case FAX: return "Referrals may be accepted by fax.";
            case PHONE: return "Referrals may be accepted over the phone from a practitioner.";
            case ELEC: return "Referrals may be accepted via a secure messaging system. To determine the types of secure messaging systems supported, refer to the identifiers collection. Callers will need to understand the specific identifier system used to know that they are able to transmit messages.";
            case SEMAIL: return "Referrals may be accepted via a secure email. To send please enrypt with the services public key.";
            case MAIL: return "Referrals may be accepted via regular postage (or hand delivered).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FAX: return "Fax";
            case PHONE: return "Phone";
            case ELEC: return "Secure Messaging";
            case SEMAIL: return "Secure Email";
            case MAIL: return "Mail";
            default: return "?";
          }
    }


}

