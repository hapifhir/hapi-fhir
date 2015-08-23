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


public enum V3Hl7Realm {

        /**
         * Description: Concepts that can be used as Binding Realms when creating Binding Statements.  These codes are permitted to appear in the InfrastructureRoot.realmCode attribute.
         */
        BINDINGREALMS, 
        /**
         * Description: Realm codes for official HL7 organizational bodies.  This includes both the HL7 International organization as well as all recognized international affiliates (past and present).  These groups have the ability to bind vocabulary and develop artifacts.  As well, they have the ability to have "ownership" over other binding realms and namespace realms via the owningAffiliate property of those other realm codes.
         */
        AFFILIATEREALMS, 
        /**
         * Description: Realm code for use of Argentina
         */
        AR, 
        /**
         * Description: Realm code for use of Austria
         */
        AT, 
        /**
         * Description: Realm code for use of Australia
         */
        AU, 
        /**
         * Description: Realm code for use of Brazil
         */
        BR, 
        /**
         * Description: Realm code for use of Canada
         */
        CA, 
        /**
         * Description: Realm code for use of Switzerland
         */
        CH, 
        /**
         * Description: Realm code for use of Chile
         */
        CL, 
        /**
         * Description: Realm code for use of China
         */
        CN, 
        /**
         * Description: Realm code for use of Localized Version
         */
        CO, 
        /**
         * Description: Realm code for use of Czech Republic
         */
        CZ, 
        /**
         * Description: Realm code for use of Germany
         */
        DE, 
        /**
         * Description: Realm code for use of Denmark
         */
        DK, 
        /**
         * Description: Realm code for use of Spain
         */
        ES, 
        /**
         * Description: Realm code for use of Finland
         */
        FI, 
        /**
         * Description: Realm code for use of France
         */
        FR, 
        /**
         * Description: Realm code for use of Greece
         */
        GR, 
        /**
         * Description: Realm code for use of Croatia
         */
        HR, 
        /**
         * Description: Realm code for use of Ireland
         */
        IE, 
        /**
         * Description: Realm code for use of India
         */
        IN, 
        /**
         * Description: Realm code for use of Italy
         */
        IT, 
        /**
         * Description: Realm code for use of Japan
         */
        JP, 
        /**
         * Description: Realm code for use of Korea
         */
        KR, 
        /**
         * Description: Realm code for use of Lithuania
         */
        LT, 
        /**
         * Description: Realm code for use of Mexico
         */
        MX, 
        /**
         * Description: Realm code for use of The Netherlands
         */
        NL, 
        /**
         * Description: Realm code for use of New Zealand
         */
        NZ, 
        /**
         * Description: Realm code for use of Romania
         */
        RO, 
        /**
         * Description: Realm code for use of Russian Federation
         */
        RU, 
        /**
         * Description: Realm code for use of Sweden
         */
        SE, 
        /**
         * Description: Realm code for use of Localized Version
         */
        SG, 
        /**
         * Description: Realm code for use of Southern Africa
         */
        SOA, 
        /**
         * Description: Realm code for use of Turkey
         */
        TR, 
        /**
         * Description: Realm code for use of Taiwan
         */
        TW, 
        /**
         * Description: Realm code for use of United Kingdom
         */
        UK, 
        /**
         * Description: Realm code for use of United States of America
         */
        US, 
        /**
         * Description: Realm code for use of Universal realm or context, used in every instance
         */
        UV, 
        /**
         * Description: Realm code for use of Uruguay
         */
        UY, 
        /**
         * Description: Realm code for use of Unclassified Realm
         */
        C1, 
        /**
         * Description: Realm code for use of Great Britain
         */
        GB, 
        /**
         * Description: Realm code for use of Representative Realm
         */
        R1, 
        /**
         * Description: Realm code for use of Example Realm
         */
        X1, 
        /**
         * Description: Codes that can be used in the "realm" portion of HL7 v3 artifact identifiers.
         */
        NAMESPACEREALMS, 
        /**
         * Description: An artifact created for local use only.  This realm namespace has no owning affiliate.  Its use is uncontrolled, i.e. anyone can create artifacts using this realm namespace.  Because of this, there is a significant likelihood of artifact identifier collisions.  Implementers are encouraged to register their artifacts under an affiliate owned and controlled namespace to avoid such collision problems where possible.
         */
        ZZ, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7Realm fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BindingRealms".equals(codeString))
          return BINDINGREALMS;
        if ("AffiliateRealms".equals(codeString))
          return AFFILIATEREALMS;
        if ("AR".equals(codeString))
          return AR;
        if ("AT".equals(codeString))
          return AT;
        if ("AU".equals(codeString))
          return AU;
        if ("BR".equals(codeString))
          return BR;
        if ("CA".equals(codeString))
          return CA;
        if ("CH".equals(codeString))
          return CH;
        if ("CL".equals(codeString))
          return CL;
        if ("CN".equals(codeString))
          return CN;
        if ("CO".equals(codeString))
          return CO;
        if ("CZ".equals(codeString))
          return CZ;
        if ("DE".equals(codeString))
          return DE;
        if ("DK".equals(codeString))
          return DK;
        if ("ES".equals(codeString))
          return ES;
        if ("FI".equals(codeString))
          return FI;
        if ("FR".equals(codeString))
          return FR;
        if ("GR".equals(codeString))
          return GR;
        if ("HR".equals(codeString))
          return HR;
        if ("IE".equals(codeString))
          return IE;
        if ("IN".equals(codeString))
          return IN;
        if ("IT".equals(codeString))
          return IT;
        if ("JP".equals(codeString))
          return JP;
        if ("KR".equals(codeString))
          return KR;
        if ("LT".equals(codeString))
          return LT;
        if ("MX".equals(codeString))
          return MX;
        if ("NL".equals(codeString))
          return NL;
        if ("NZ".equals(codeString))
          return NZ;
        if ("RO".equals(codeString))
          return RO;
        if ("RU".equals(codeString))
          return RU;
        if ("SE".equals(codeString))
          return SE;
        if ("SG".equals(codeString))
          return SG;
        if ("SOA".equals(codeString))
          return SOA;
        if ("TR".equals(codeString))
          return TR;
        if ("TW".equals(codeString))
          return TW;
        if ("UK".equals(codeString))
          return UK;
        if ("US".equals(codeString))
          return US;
        if ("UV".equals(codeString))
          return UV;
        if ("UY".equals(codeString))
          return UY;
        if ("C1".equals(codeString))
          return C1;
        if ("GB".equals(codeString))
          return GB;
        if ("R1".equals(codeString))
          return R1;
        if ("X1".equals(codeString))
          return X1;
        if ("NamespaceRealms".equals(codeString))
          return NAMESPACEREALMS;
        if ("ZZ".equals(codeString))
          return ZZ;
        throw new Exception("Unknown V3Hl7Realm code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BINDINGREALMS: return "BindingRealms";
            case AFFILIATEREALMS: return "AffiliateRealms";
            case AR: return "AR";
            case AT: return "AT";
            case AU: return "AU";
            case BR: return "BR";
            case CA: return "CA";
            case CH: return "CH";
            case CL: return "CL";
            case CN: return "CN";
            case CO: return "CO";
            case CZ: return "CZ";
            case DE: return "DE";
            case DK: return "DK";
            case ES: return "ES";
            case FI: return "FI";
            case FR: return "FR";
            case GR: return "GR";
            case HR: return "HR";
            case IE: return "IE";
            case IN: return "IN";
            case IT: return "IT";
            case JP: return "JP";
            case KR: return "KR";
            case LT: return "LT";
            case MX: return "MX";
            case NL: return "NL";
            case NZ: return "NZ";
            case RO: return "RO";
            case RU: return "RU";
            case SE: return "SE";
            case SG: return "SG";
            case SOA: return "SOA";
            case TR: return "TR";
            case TW: return "TW";
            case UK: return "UK";
            case US: return "US";
            case UV: return "UV";
            case UY: return "UY";
            case C1: return "C1";
            case GB: return "GB";
            case R1: return "R1";
            case X1: return "X1";
            case NAMESPACEREALMS: return "NamespaceRealms";
            case ZZ: return "ZZ";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/hl7Realm";
        }
        public String getDefinition() {
          switch (this) {
            case BINDINGREALMS: return "Description: Concepts that can be used as Binding Realms when creating Binding Statements.  These codes are permitted to appear in the InfrastructureRoot.realmCode attribute.";
            case AFFILIATEREALMS: return "Description: Realm codes for official HL7 organizational bodies.  This includes both the HL7 International organization as well as all recognized international affiliates (past and present).  These groups have the ability to bind vocabulary and develop artifacts.  As well, they have the ability to have \"ownership\" over other binding realms and namespace realms via the owningAffiliate property of those other realm codes.";
            case AR: return "Description: Realm code for use of Argentina";
            case AT: return "Description: Realm code for use of Austria";
            case AU: return "Description: Realm code for use of Australia";
            case BR: return "Description: Realm code for use of Brazil";
            case CA: return "Description: Realm code for use of Canada";
            case CH: return "Description: Realm code for use of Switzerland";
            case CL: return "Description: Realm code for use of Chile";
            case CN: return "Description: Realm code for use of China";
            case CO: return "Description: Realm code for use of Localized Version";
            case CZ: return "Description: Realm code for use of Czech Republic";
            case DE: return "Description: Realm code for use of Germany";
            case DK: return "Description: Realm code for use of Denmark";
            case ES: return "Description: Realm code for use of Spain";
            case FI: return "Description: Realm code for use of Finland";
            case FR: return "Description: Realm code for use of France";
            case GR: return "Description: Realm code for use of Greece";
            case HR: return "Description: Realm code for use of Croatia";
            case IE: return "Description: Realm code for use of Ireland";
            case IN: return "Description: Realm code for use of India";
            case IT: return "Description: Realm code for use of Italy";
            case JP: return "Description: Realm code for use of Japan";
            case KR: return "Description: Realm code for use of Korea";
            case LT: return "Description: Realm code for use of Lithuania";
            case MX: return "Description: Realm code for use of Mexico";
            case NL: return "Description: Realm code for use of The Netherlands";
            case NZ: return "Description: Realm code for use of New Zealand";
            case RO: return "Description: Realm code for use of Romania";
            case RU: return "Description: Realm code for use of Russian Federation";
            case SE: return "Description: Realm code for use of Sweden";
            case SG: return "Description: Realm code for use of Localized Version";
            case SOA: return "Description: Realm code for use of Southern Africa";
            case TR: return "Description: Realm code for use of Turkey";
            case TW: return "Description: Realm code for use of Taiwan";
            case UK: return "Description: Realm code for use of United Kingdom";
            case US: return "Description: Realm code for use of United States of America";
            case UV: return "Description: Realm code for use of Universal realm or context, used in every instance";
            case UY: return "Description: Realm code for use of Uruguay";
            case C1: return "Description: Realm code for use of Unclassified Realm";
            case GB: return "Description: Realm code for use of Great Britain";
            case R1: return "Description: Realm code for use of Representative Realm";
            case X1: return "Description: Realm code for use of Example Realm";
            case NAMESPACEREALMS: return "Description: Codes that can be used in the \"realm\" portion of HL7 v3 artifact identifiers.";
            case ZZ: return "Description: An artifact created for local use only.  This realm namespace has no owning affiliate.  Its use is uncontrolled, i.e. anyone can create artifacts using this realm namespace.  Because of this, there is a significant likelihood of artifact identifier collisions.  Implementers are encouraged to register their artifacts under an affiliate owned and controlled namespace to avoid such collision problems where possible.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BINDINGREALMS: return "binding realms";
            case AFFILIATEREALMS: return "Affiliate Realms";
            case AR: return "Argentina";
            case AT: return "Austria";
            case AU: return "Australia";
            case BR: return "Brazil";
            case CA: return "Canada";
            case CH: return "Switzerland";
            case CL: return "Chile";
            case CN: return "China";
            case CO: return "Columbia";
            case CZ: return "Czech Republic";
            case DE: return "Germany";
            case DK: return "Denmark";
            case ES: return "Spain";
            case FI: return "Finland";
            case FR: return "France";
            case GR: return "Greece";
            case HR: return "Croatia";
            case IE: return "Ireland";
            case IN: return "India";
            case IT: return "Italy";
            case JP: return "Japan";
            case KR: return "Korea";
            case LT: return "Lithuania";
            case MX: return "Mexico";
            case NL: return "The Netherlands";
            case NZ: return "New Zealand";
            case RO: return "Romania";
            case RU: return "Russian Federation";
            case SE: return "Sweden";
            case SG: return "Singapore";
            case SOA: return "Southern Africa";
            case TR: return "Turkey";
            case TW: return "Taiwan";
            case UK: return "United Kingdom";
            case US: return "United States of America";
            case UV: return "Universal";
            case UY: return "Uruguay";
            case C1: return "Unclassified Realm";
            case GB: return "Great Britain";
            case R1: return "Representative Realm";
            case X1: return "Example Realm";
            case NAMESPACEREALMS: return "namespace realms";
            case ZZ: return "Localized Version";
            default: return "?";
          }
    }


}

