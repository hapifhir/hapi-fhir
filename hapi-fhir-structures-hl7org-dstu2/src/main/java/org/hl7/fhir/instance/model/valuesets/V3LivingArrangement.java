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


public enum V3LivingArrangement {

        /**
         * Definition: Living arrangements lacking a permanent residence.
         */
        HL, 
        /**
         * Nomadic
         */
        M, 
        /**
         * Transient
         */
        T, 
        /**
         * Institution
         */
        I, 
        /**
         * Definition: A group living arrangement specifically for the care of those in need of temporary and crisis housing assistance.  Examples include domestic violence shelters, shelters for displaced or homeless individuals, Salvation Army, Jesus House, etc.  Community based services may be provided.
         */
        CS, 
        /**
         * Group Home
         */
        G, 
        /**
         * Nursing Home
         */
        N, 
        /**
         * Extended care facility
         */
        X, 
        /**
         * Definition:  A living arrangement within a private residence for single family.
         */
        PR, 
        /**
         * Independent Household
         */
        H, 
        /**
         * Retirement Community
         */
        R, 
        /**
         * Definition: Assisted living in a single family residence for persons with physical, behavioral, or functional health, or socio-economic challenges.  There may or may not be on-site supervision but the housing is designed to assist the client with developing independent living skills. Community based services may be provided.
         */
        SL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3LivingArrangement fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("HL".equals(codeString))
          return HL;
        if ("M".equals(codeString))
          return M;
        if ("T".equals(codeString))
          return T;
        if ("I".equals(codeString))
          return I;
        if ("CS".equals(codeString))
          return CS;
        if ("G".equals(codeString))
          return G;
        if ("N".equals(codeString))
          return N;
        if ("X".equals(codeString))
          return X;
        if ("PR".equals(codeString))
          return PR;
        if ("H".equals(codeString))
          return H;
        if ("R".equals(codeString))
          return R;
        if ("SL".equals(codeString))
          return SL;
        throw new Exception("Unknown V3LivingArrangement code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HL: return "HL";
            case M: return "M";
            case T: return "T";
            case I: return "I";
            case CS: return "CS";
            case G: return "G";
            case N: return "N";
            case X: return "X";
            case PR: return "PR";
            case H: return "H";
            case R: return "R";
            case SL: return "SL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/LivingArrangement";
        }
        public String getDefinition() {
          switch (this) {
            case HL: return "Definition: Living arrangements lacking a permanent residence.";
            case M: return "Nomadic";
            case T: return "Transient";
            case I: return "Institution";
            case CS: return "Definition: A group living arrangement specifically for the care of those in need of temporary and crisis housing assistance.  Examples include domestic violence shelters, shelters for displaced or homeless individuals, Salvation Army, Jesus House, etc.  Community based services may be provided.";
            case G: return "Group Home";
            case N: return "Nursing Home";
            case X: return "Extended care facility";
            case PR: return "Definition:  A living arrangement within a private residence for single family.";
            case H: return "Independent Household";
            case R: return "Retirement Community";
            case SL: return "Definition: Assisted living in a single family residence for persons with physical, behavioral, or functional health, or socio-economic challenges.  There may or may not be on-site supervision but the housing is designed to assist the client with developing independent living skills. Community based services may be provided.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HL: return "homeless";
            case M: return "Nomadic";
            case T: return "Transient";
            case I: return "Institution";
            case CS: return "community shelter";
            case G: return "Group Home";
            case N: return "Nursing Home";
            case X: return "Extended care facility";
            case PR: return "private residence";
            case H: return "Independent Household";
            case R: return "Retirement Community";
            case SL: return "supported living";
            default: return "?";
          }
    }


}

