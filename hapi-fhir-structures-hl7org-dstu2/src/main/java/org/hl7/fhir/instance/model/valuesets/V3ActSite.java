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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3ActSite {

        /**
         * An anatomical location on a human which can be the focus of an act.
         */
        _HUMANACTSITE, 
        /**
         * The set of body locations to or through which a drug product may be administered.
         */
        _HUMANSUBSTANCEADMINISTRATIONSITE, 
        /**
         * bilateral ears
         */
        BE, 
        /**
         * bilateral nares
         */
        BN, 
        /**
         * buttock
         */
        BU, 
        /**
         * left arm
         */
        LA, 
        /**
         * left anterior chest
         */
        LAC, 
        /**
         * left antecubital fossa
         */
        LACF, 
        /**
         * left deltoid
         */
        LD, 
        /**
         * left ear
         */
        LE, 
        /**
         * left external jugular
         */
        LEJ, 
        /**
         * left foot
         */
        LF, 
        /**
         * left gluteus medius
         */
        LG, 
        /**
         * left hand
         */
        LH, 
        /**
         * left internal jugular
         */
        LIJ, 
        /**
         * left lower abd quadrant
         */
        LLAQ, 
        /**
         * left lower forearm
         */
        LLFA, 
        /**
         * left mid forearm
         */
        LMFA, 
        /**
         * left naris
         */
        LN, 
        /**
         * left posterior chest
         */
        LPC, 
        /**
         * left subclavian
         */
        LSC, 
        /**
         * left thigh
         */
        LT, 
        /**
         * left upper arm
         */
        LUA, 
        /**
         * left upper abd quadrant
         */
        LUAQ, 
        /**
         * left upper forearm
         */
        LUFA, 
        /**
         * left ventragluteal
         */
        LVG, 
        /**
         * left vastus lateralis
         */
        LVL, 
        /**
         * right eye
         */
        OD, 
        /**
         * left eye
         */
        OS, 
        /**
         * bilateral eyes
         */
        OU, 
        /**
         * perianal
         */
        PA, 
        /**
         * perineal
         */
        PERIN, 
        /**
         * right arm
         */
        RA, 
        /**
         * right anterior chest
         */
        RAC, 
        /**
         * right antecubital fossa
         */
        RACF, 
        /**
         * right deltoid
         */
        RD, 
        /**
         * right ear
         */
        RE, 
        /**
         * right external jugular
         */
        REJ, 
        /**
         * right foot
         */
        RF, 
        /**
         * right gluteus medius
         */
        RG, 
        /**
         * right hand
         */
        RH, 
        /**
         * right internal jugular
         */
        RIJ, 
        /**
         * right lower abd quadrant
         */
        RLAQ, 
        /**
         * right lower forearm
         */
        RLFA, 
        /**
         * right mid forearm
         */
        RMFA, 
        /**
         * right naris
         */
        RN, 
        /**
         * right posterior chest
         */
        RPC, 
        /**
         * right subclavian
         */
        RSC, 
        /**
         * right thigh
         */
        RT, 
        /**
         * right upper arm
         */
        RUA, 
        /**
         * right upper abd quadrant
         */
        RUAQ, 
        /**
         * right upper forearm
         */
        RUFA, 
        /**
         * right ventragluteal
         */
        RVG, 
        /**
         * right vastus lateralis
         */
        RVL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActSite fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_HumanActSite".equals(codeString))
          return _HUMANACTSITE;
        if ("_HumanSubstanceAdministrationSite".equals(codeString))
          return _HUMANSUBSTANCEADMINISTRATIONSITE;
        if ("BE".equals(codeString))
          return BE;
        if ("BN".equals(codeString))
          return BN;
        if ("BU".equals(codeString))
          return BU;
        if ("LA".equals(codeString))
          return LA;
        if ("LAC".equals(codeString))
          return LAC;
        if ("LACF".equals(codeString))
          return LACF;
        if ("LD".equals(codeString))
          return LD;
        if ("LE".equals(codeString))
          return LE;
        if ("LEJ".equals(codeString))
          return LEJ;
        if ("LF".equals(codeString))
          return LF;
        if ("LG".equals(codeString))
          return LG;
        if ("LH".equals(codeString))
          return LH;
        if ("LIJ".equals(codeString))
          return LIJ;
        if ("LLAQ".equals(codeString))
          return LLAQ;
        if ("LLFA".equals(codeString))
          return LLFA;
        if ("LMFA".equals(codeString))
          return LMFA;
        if ("LN".equals(codeString))
          return LN;
        if ("LPC".equals(codeString))
          return LPC;
        if ("LSC".equals(codeString))
          return LSC;
        if ("LT".equals(codeString))
          return LT;
        if ("LUA".equals(codeString))
          return LUA;
        if ("LUAQ".equals(codeString))
          return LUAQ;
        if ("LUFA".equals(codeString))
          return LUFA;
        if ("LVG".equals(codeString))
          return LVG;
        if ("LVL".equals(codeString))
          return LVL;
        if ("OD".equals(codeString))
          return OD;
        if ("OS".equals(codeString))
          return OS;
        if ("OU".equals(codeString))
          return OU;
        if ("PA".equals(codeString))
          return PA;
        if ("PERIN".equals(codeString))
          return PERIN;
        if ("RA".equals(codeString))
          return RA;
        if ("RAC".equals(codeString))
          return RAC;
        if ("RACF".equals(codeString))
          return RACF;
        if ("RD".equals(codeString))
          return RD;
        if ("RE".equals(codeString))
          return RE;
        if ("REJ".equals(codeString))
          return REJ;
        if ("RF".equals(codeString))
          return RF;
        if ("RG".equals(codeString))
          return RG;
        if ("RH".equals(codeString))
          return RH;
        if ("RIJ".equals(codeString))
          return RIJ;
        if ("RLAQ".equals(codeString))
          return RLAQ;
        if ("RLFA".equals(codeString))
          return RLFA;
        if ("RMFA".equals(codeString))
          return RMFA;
        if ("RN".equals(codeString))
          return RN;
        if ("RPC".equals(codeString))
          return RPC;
        if ("RSC".equals(codeString))
          return RSC;
        if ("RT".equals(codeString))
          return RT;
        if ("RUA".equals(codeString))
          return RUA;
        if ("RUAQ".equals(codeString))
          return RUAQ;
        if ("RUFA".equals(codeString))
          return RUFA;
        if ("RVG".equals(codeString))
          return RVG;
        if ("RVL".equals(codeString))
          return RVL;
        throw new Exception("Unknown V3ActSite code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _HUMANACTSITE: return "_HumanActSite";
            case _HUMANSUBSTANCEADMINISTRATIONSITE: return "_HumanSubstanceAdministrationSite";
            case BE: return "BE";
            case BN: return "BN";
            case BU: return "BU";
            case LA: return "LA";
            case LAC: return "LAC";
            case LACF: return "LACF";
            case LD: return "LD";
            case LE: return "LE";
            case LEJ: return "LEJ";
            case LF: return "LF";
            case LG: return "LG";
            case LH: return "LH";
            case LIJ: return "LIJ";
            case LLAQ: return "LLAQ";
            case LLFA: return "LLFA";
            case LMFA: return "LMFA";
            case LN: return "LN";
            case LPC: return "LPC";
            case LSC: return "LSC";
            case LT: return "LT";
            case LUA: return "LUA";
            case LUAQ: return "LUAQ";
            case LUFA: return "LUFA";
            case LVG: return "LVG";
            case LVL: return "LVL";
            case OD: return "OD";
            case OS: return "OS";
            case OU: return "OU";
            case PA: return "PA";
            case PERIN: return "PERIN";
            case RA: return "RA";
            case RAC: return "RAC";
            case RACF: return "RACF";
            case RD: return "RD";
            case RE: return "RE";
            case REJ: return "REJ";
            case RF: return "RF";
            case RG: return "RG";
            case RH: return "RH";
            case RIJ: return "RIJ";
            case RLAQ: return "RLAQ";
            case RLFA: return "RLFA";
            case RMFA: return "RMFA";
            case RN: return "RN";
            case RPC: return "RPC";
            case RSC: return "RSC";
            case RT: return "RT";
            case RUA: return "RUA";
            case RUAQ: return "RUAQ";
            case RUFA: return "RUFA";
            case RVG: return "RVG";
            case RVL: return "RVL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActSite";
        }
        public String getDefinition() {
          switch (this) {
            case _HUMANACTSITE: return "An anatomical location on a human which can be the focus of an act.";
            case _HUMANSUBSTANCEADMINISTRATIONSITE: return "The set of body locations to or through which a drug product may be administered.";
            case BE: return "bilateral ears";
            case BN: return "bilateral nares";
            case BU: return "buttock";
            case LA: return "left arm";
            case LAC: return "left anterior chest";
            case LACF: return "left antecubital fossa";
            case LD: return "left deltoid";
            case LE: return "left ear";
            case LEJ: return "left external jugular";
            case LF: return "left foot";
            case LG: return "left gluteus medius";
            case LH: return "left hand";
            case LIJ: return "left internal jugular";
            case LLAQ: return "left lower abd quadrant";
            case LLFA: return "left lower forearm";
            case LMFA: return "left mid forearm";
            case LN: return "left naris";
            case LPC: return "left posterior chest";
            case LSC: return "left subclavian";
            case LT: return "left thigh";
            case LUA: return "left upper arm";
            case LUAQ: return "left upper abd quadrant";
            case LUFA: return "left upper forearm";
            case LVG: return "left ventragluteal";
            case LVL: return "left vastus lateralis";
            case OD: return "right eye";
            case OS: return "left eye";
            case OU: return "bilateral eyes";
            case PA: return "perianal";
            case PERIN: return "perineal";
            case RA: return "right arm";
            case RAC: return "right anterior chest";
            case RACF: return "right antecubital fossa";
            case RD: return "right deltoid";
            case RE: return "right ear";
            case REJ: return "right external jugular";
            case RF: return "right foot";
            case RG: return "right gluteus medius";
            case RH: return "right hand";
            case RIJ: return "right internal jugular";
            case RLAQ: return "right lower abd quadrant";
            case RLFA: return "right lower forearm";
            case RMFA: return "right mid forearm";
            case RN: return "right naris";
            case RPC: return "right posterior chest";
            case RSC: return "right subclavian";
            case RT: return "right thigh";
            case RUA: return "right upper arm";
            case RUAQ: return "right upper abd quadrant";
            case RUFA: return "right upper forearm";
            case RVG: return "right ventragluteal";
            case RVL: return "right vastus lateralis";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _HUMANACTSITE: return "HumanActSite";
            case _HUMANSUBSTANCEADMINISTRATIONSITE: return "HumanSubstanceAdministrationSite";
            case BE: return "bilateral ears";
            case BN: return "bilateral nares";
            case BU: return "buttock";
            case LA: return "left arm";
            case LAC: return "left anterior chest";
            case LACF: return "left antecubital fossa";
            case LD: return "left deltoid";
            case LE: return "left ear";
            case LEJ: return "left external jugular";
            case LF: return "left foot";
            case LG: return "left gluteus medius";
            case LH: return "left hand";
            case LIJ: return "left internal jugular";
            case LLAQ: return "left lower abd quadrant";
            case LLFA: return "left lower forearm";
            case LMFA: return "left mid forearm";
            case LN: return "left naris";
            case LPC: return "left posterior chest";
            case LSC: return "left subclavian";
            case LT: return "left thigh";
            case LUA: return "left upper arm";
            case LUAQ: return "left upper abd quadrant";
            case LUFA: return "left upper forearm";
            case LVG: return "left ventragluteal";
            case LVL: return "left vastus lateralis";
            case OD: return "right eye";
            case OS: return "left eye";
            case OU: return "bilateral eyes";
            case PA: return "perianal";
            case PERIN: return "perineal";
            case RA: return "right arm";
            case RAC: return "right anterior chest";
            case RACF: return "right antecubital fossa";
            case RD: return "right deltoid";
            case RE: return "right ear";
            case REJ: return "right external jugular";
            case RF: return "right foot";
            case RG: return "right gluteus medius";
            case RH: return "right hand";
            case RIJ: return "right internal jugular";
            case RLAQ: return "right lower abd quadrant";
            case RLFA: return "right lower forearm";
            case RMFA: return "right mid forearm";
            case RN: return "right naris";
            case RPC: return "right posterior chest";
            case RSC: return "right subclavian";
            case RT: return "right thigh";
            case RUA: return "right upper arm";
            case RUAQ: return "right upper abd quadrant";
            case RUFA: return "right upper forearm";
            case RVG: return "right ventragluteal";
            case RVL: return "right vastus lateralis";
            default: return "?";
          }
    }


}

