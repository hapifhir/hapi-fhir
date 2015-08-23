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


public enum RestfulSecurityService {

        /**
         * Oauth (unspecified version see oauth.net)
         */
        OAUTH, 
        /**
         * OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org/)
         */
        SMARTONFHIR, 
        /**
         * Microsoft NTLM Authentication
         */
        NTLM, 
        /**
         * Basic authentication defined in HTTP specification
         */
        BASIC, 
        /**
         * see http://www.ietf.org/rfc/rfc4120.txt
         */
        KERBEROS, 
        /**
         * SSL where client must have a certificate registered with the server
         */
        CERTIFICATES, 
        /**
         * added to help the parsers
         */
        NULL;
        public static RestfulSecurityService fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("OAuth".equals(codeString))
          return OAUTH;
        if ("SMART-on-FHIR".equals(codeString))
          return SMARTONFHIR;
        if ("NTLM".equals(codeString))
          return NTLM;
        if ("Basic".equals(codeString))
          return BASIC;
        if ("Kerberos".equals(codeString))
          return KERBEROS;
        if ("Certificates".equals(codeString))
          return CERTIFICATES;
        throw new Exception("Unknown RestfulSecurityService code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OAUTH: return "OAuth";
            case SMARTONFHIR: return "SMART-on-FHIR";
            case NTLM: return "NTLM";
            case BASIC: return "Basic";
            case KERBEROS: return "Kerberos";
            case CERTIFICATES: return "Certificates";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/restful-security-service";
        }
        public String getDefinition() {
          switch (this) {
            case OAUTH: return "Oauth (unspecified version see oauth.net)";
            case SMARTONFHIR: return "OAuth2 using SMART-on-FHIR profile (see http://docs.smarthealthit.org/)";
            case NTLM: return "Microsoft NTLM Authentication";
            case BASIC: return "Basic authentication defined in HTTP specification";
            case KERBEROS: return "see http://www.ietf.org/rfc/rfc4120.txt";
            case CERTIFICATES: return "SSL where client must have a certificate registered with the server";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OAUTH: return "OAuth";
            case SMARTONFHIR: return "SMART-on-FHIR";
            case NTLM: return "NTLM";
            case BASIC: return "Basic";
            case KERBEROS: return "Kerberos";
            case CERTIFICATES: return "Certificates";
            default: return "?";
          }
    }


}

