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


public enum V3AddressUse {

        /**
         * Description: Address uses that can apply to both postal and telecommunication addresses.
         */
        _GENERALADDRESSUSE, 
        /**
         * Description: A flag indicating that the address is bad, in fact, useless.
         */
        BAD, 
        /**
         * Description: Indicates that the address is considered sensitive and should only be shared or published in accordance with organizational controls governing patient demographic information with increased sensitivity. Uses of Addresses.   Lloyd to supply more complete description.
         */
        CONF, 
        /**
         * Description: A communication address at a home, attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.
         */
        H, 
        /**
         * Description: The primary home, to reach a person after business hours.
         */
        HP, 
        /**
         * Description: A vacation home, to reach a person while on vacation.
         */
        HV, 
        /**
         * This address is no longer in use.

                        
                           Usage Note: Address may also carry valid time ranges. This code is used to cover the situations where it is known that the address is no longer valid, but no particular time range for its use is known.
         */
        OLD, 
        /**
         * Description: A temporary address, may be good for visit or mailing. Note that an address history can provide more detailed information.
         */
        TMP, 
        /**
         * Description: An office address. First choice for business related contacts during business hours.
         */
        WP, 
        /**
         * Description: Indicates a work place address or telecommunication address that reaches the individual or organization directly without intermediaries. For phones, often referred to as a 'private line'.
         */
        DIR, 
        /**
         * Description: Indicates a work place address or telecommunication address that is a 'standard' address which may reach a reception service, mail-room, or other intermediary prior to the target entity.
         */
        PUB, 
        /**
         * Description: Address uses that only apply to postal addresses, not telecommunication addresses.
         */
        _POSTALADDRESSUSE, 
        /**
         * Description: Used primarily to visit an address.
         */
        PHYS, 
        /**
         * Description: Used to send mail.
         */
        PST, 
        /**
         * Description: Address uses that only apply to telecommunication addresses, not postal addresses.
         */
        _TELECOMMUNICATIONADDRESSUSE, 
        /**
         * Description: An automated answering machine used for less urgent cases and if the main purpose of contact is to leave a message or access an automated announcement.
         */
        AS, 
        /**
         * Description: A contact specifically designated to be used for emergencies. This is the first choice in emergencies, independent of any other use codes.
         */
        EC, 
        /**
         * Description: A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business.
         */
        MC, 
        /**
         * Description: A paging device suitable to solicit a callback or to leave a very short message.
         */
        PG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AddressUse fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_GeneralAddressUse".equals(codeString))
          return _GENERALADDRESSUSE;
        if ("BAD".equals(codeString))
          return BAD;
        if ("CONF".equals(codeString))
          return CONF;
        if ("H".equals(codeString))
          return H;
        if ("HP".equals(codeString))
          return HP;
        if ("HV".equals(codeString))
          return HV;
        if ("OLD".equals(codeString))
          return OLD;
        if ("TMP".equals(codeString))
          return TMP;
        if ("WP".equals(codeString))
          return WP;
        if ("DIR".equals(codeString))
          return DIR;
        if ("PUB".equals(codeString))
          return PUB;
        if ("_PostalAddressUse".equals(codeString))
          return _POSTALADDRESSUSE;
        if ("PHYS".equals(codeString))
          return PHYS;
        if ("PST".equals(codeString))
          return PST;
        if ("_TelecommunicationAddressUse".equals(codeString))
          return _TELECOMMUNICATIONADDRESSUSE;
        if ("AS".equals(codeString))
          return AS;
        if ("EC".equals(codeString))
          return EC;
        if ("MC".equals(codeString))
          return MC;
        if ("PG".equals(codeString))
          return PG;
        throw new Exception("Unknown V3AddressUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _GENERALADDRESSUSE: return "_GeneralAddressUse";
            case BAD: return "BAD";
            case CONF: return "CONF";
            case H: return "H";
            case HP: return "HP";
            case HV: return "HV";
            case OLD: return "OLD";
            case TMP: return "TMP";
            case WP: return "WP";
            case DIR: return "DIR";
            case PUB: return "PUB";
            case _POSTALADDRESSUSE: return "_PostalAddressUse";
            case PHYS: return "PHYS";
            case PST: return "PST";
            case _TELECOMMUNICATIONADDRESSUSE: return "_TelecommunicationAddressUse";
            case AS: return "AS";
            case EC: return "EC";
            case MC: return "MC";
            case PG: return "PG";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AddressUse";
        }
        public String getDefinition() {
          switch (this) {
            case _GENERALADDRESSUSE: return "Description: Address uses that can apply to both postal and telecommunication addresses.";
            case BAD: return "Description: A flag indicating that the address is bad, in fact, useless.";
            case CONF: return "Description: Indicates that the address is considered sensitive and should only be shared or published in accordance with organizational controls governing patient demographic information with increased sensitivity. Uses of Addresses.   Lloyd to supply more complete description.";
            case H: return "Description: A communication address at a home, attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.";
            case HP: return "Description: The primary home, to reach a person after business hours.";
            case HV: return "Description: A vacation home, to reach a person while on vacation.";
            case OLD: return "This address is no longer in use.\r\n\n                        \n                           Usage Note: Address may also carry valid time ranges. This code is used to cover the situations where it is known that the address is no longer valid, but no particular time range for its use is known.";
            case TMP: return "Description: A temporary address, may be good for visit or mailing. Note that an address history can provide more detailed information.";
            case WP: return "Description: An office address. First choice for business related contacts during business hours.";
            case DIR: return "Description: Indicates a work place address or telecommunication address that reaches the individual or organization directly without intermediaries. For phones, often referred to as a 'private line'.";
            case PUB: return "Description: Indicates a work place address or telecommunication address that is a 'standard' address which may reach a reception service, mail-room, or other intermediary prior to the target entity.";
            case _POSTALADDRESSUSE: return "Description: Address uses that only apply to postal addresses, not telecommunication addresses.";
            case PHYS: return "Description: Used primarily to visit an address.";
            case PST: return "Description: Used to send mail.";
            case _TELECOMMUNICATIONADDRESSUSE: return "Description: Address uses that only apply to telecommunication addresses, not postal addresses.";
            case AS: return "Description: An automated answering machine used for less urgent cases and if the main purpose of contact is to leave a message or access an automated announcement.";
            case EC: return "Description: A contact specifically designated to be used for emergencies. This is the first choice in emergencies, independent of any other use codes.";
            case MC: return "Description: A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business.";
            case PG: return "Description: A paging device suitable to solicit a callback or to leave a very short message.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _GENERALADDRESSUSE: return "_GeneralAddressUse";
            case BAD: return "bad address";
            case CONF: return "confidential address";
            case H: return "home address";
            case HP: return "primary home";
            case HV: return "vacation home";
            case OLD: return "no longer in use";
            case TMP: return "temporary address";
            case WP: return "work place";
            case DIR: return "direct";
            case PUB: return "public";
            case _POSTALADDRESSUSE: return "_PostalAddressUse";
            case PHYS: return "physical visit address";
            case PST: return "postal address";
            case _TELECOMMUNICATIONADDRESSUSE: return "_TelecommunicationAddressUse";
            case AS: return "answering service";
            case EC: return "emergency contact";
            case MC: return "mobile contact)";
            case PG: return "pager";
            default: return "?";
          }
    }


}

