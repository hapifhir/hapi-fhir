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
         * Referrals may be accepted via a secure email. To send please encrypt with the services public key.
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
            case SEMAIL: return "Referrals may be accepted via a secure email. To send please encrypt with the services public key.";
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

