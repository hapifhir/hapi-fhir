package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3WorkClassificationODH {

        /**
         * A situation in which an individual serves in a government-sponsored military force.
         */
        PWAF, 
        /**
         * A situation in which an individual works for a national government organization, not including armed forces, and receives a paid salary or wage.
         */
        PWFG, 
        /**
         * A situation in which an individual works for a government organization with jurisdiction below the level of state/provincial/territorial/tribal government (e.g., city, town, township), not armed forces, and receives a paid salary or wage.
         */
        PWLG, 
        /**
         * A situation in which an individual works for a business (not government) that they do not own and receives a paid salary or wage.
         */
        PWNSE, 
        /**
         * A situation in which an individual earns a salary or wage working for himself or herself instead of working for an employer.
         */
        PWSE, 
        /**
         * A situation in which an individual works for a government organization with jurisdiction immediately below the level of national government (between national government and local government), not armed forces and receives a paid salary or wage.  Often called a state, provincial, territorial, or tribal government.
         */
        PWSG, 
        /**
         * A situation in which an individual works for a business (not government) that they do not own without receiving a paid salary or wage.
         */
        UWNSE, 
        /**
         * A situation in which an individual works for himself or herself without receiving a paid salary or wage.
         */
        UWSE, 
        /**
         * A situation in which an individual chooses to do something, especially for other people or for an organization, willingly and without being forced or compensated to do it.  This can include formal activity undertaken through public, private and voluntary organizations as well as informal community participation.
         */
        VW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3WorkClassificationODH fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("PWAF".equals(codeString))
          return PWAF;
        if ("PWFG".equals(codeString))
          return PWFG;
        if ("PWLG".equals(codeString))
          return PWLG;
        if ("PWNSE".equals(codeString))
          return PWNSE;
        if ("PWSE".equals(codeString))
          return PWSE;
        if ("PWSG".equals(codeString))
          return PWSG;
        if ("UWNSE".equals(codeString))
          return UWNSE;
        if ("UWSE".equals(codeString))
          return UWSE;
        if ("VW".equals(codeString))
          return VW;
        throw new FHIRException("Unknown V3WorkClassificationODH code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PWAF: return "PWAF";
            case PWFG: return "PWFG";
            case PWLG: return "PWLG";
            case PWNSE: return "PWNSE";
            case PWSE: return "PWSE";
            case PWSG: return "PWSG";
            case UWNSE: return "UWNSE";
            case UWSE: return "UWSE";
            case VW: return "VW";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-WorkClassificationODH";
        }
        public String getDefinition() {
          switch (this) {
            case PWAF: return "A situation in which an individual serves in a government-sponsored military force.";
            case PWFG: return "A situation in which an individual works for a national government organization, not including armed forces, and receives a paid salary or wage.";
            case PWLG: return "A situation in which an individual works for a government organization with jurisdiction below the level of state/provincial/territorial/tribal government (e.g., city, town, township), not armed forces, and receives a paid salary or wage.";
            case PWNSE: return "A situation in which an individual works for a business (not government) that they do not own and receives a paid salary or wage.";
            case PWSE: return "A situation in which an individual earns a salary or wage working for himself or herself instead of working for an employer.";
            case PWSG: return "A situation in which an individual works for a government organization with jurisdiction immediately below the level of national government (between national government and local government), not armed forces and receives a paid salary or wage.  Often called a state, provincial, territorial, or tribal government.";
            case UWNSE: return "A situation in which an individual works for a business (not government) that they do not own without receiving a paid salary or wage.";
            case UWSE: return "A situation in which an individual works for himself or herself without receiving a paid salary or wage.";
            case VW: return "A situation in which an individual chooses to do something, especially for other people or for an organization, willingly and without being forced or compensated to do it.  This can include formal activity undertaken through public, private and voluntary organizations as well as informal community participation.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PWAF: return "Paid work, Armed Forces";
            case PWFG: return "Paid work, national government, not armed forces";
            case PWLG: return "Paid work, local government, not armed forces";
            case PWNSE: return "Paid non-governmental work, not self-employed";
            case PWSE: return "Paid work, self-employed";
            case PWSG: return "Paid work, state government, not armed forces";
            case UWNSE: return "Unpaid non-governmental work, not self-employed";
            case UWSE: return "Unpaid work, self-employed";
            case VW: return "Voluntary work";
            default: return "?";
          }
    }


}

