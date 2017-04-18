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

public enum V3TimingEvent {

        /**
         * before meal (from lat. ante cibus)
         */
        AC, 
        /**
         * before lunch (from lat. ante cibus diurnus)
         */
        ACD, 
        /**
         * before breakfast (from lat. ante cibus matutinus)
         */
        ACM, 
        /**
         * before dinner (from lat. ante cibus vespertinus)
         */
        ACV, 
        /**
         * Description: meal (from lat. ante cibus)
         */
        C, 
        /**
         * Description: lunch (from lat. cibus diurnus)
         */
        CD, 
        /**
         * Description: breakfast (from lat. cibus matutinus)
         */
        CM, 
        /**
         * Description: dinner (from lat. cibus vespertinus)
         */
        CV, 
        /**
         * Description: Prior to beginning a regular period of extended sleep (this would exclude naps).  Note that this might occur at different times of day depending on a person's regular sleep schedule.
         */
        HS, 
        /**
         * between meals (from lat. inter cibus)
         */
        IC, 
        /**
         * between lunch and dinner
         */
        ICD, 
        /**
         * between breakfast and lunch
         */
        ICM, 
        /**
         * between dinner and the hour of sleep
         */
        ICV, 
        /**
         * after meal (from lat. post cibus)
         */
        PC, 
        /**
         * after lunch (from lat. post cibus diurnus)
         */
        PCD, 
        /**
         * after breakfast (from lat. post cibus matutinus)
         */
        PCM, 
        /**
         * after dinner (from lat. post cibus vespertinus)
         */
        PCV, 
        /**
         * Description: Upon waking up from a regular period of sleep, in order to start regular activities (this would exclude waking up from a nap or temporarily waking up during a period of sleep)

                        
                           Usage Notes: e.g.

                        Take pulse rate on waking in management of thyrotoxicosis.

                        Take BP on waking in management of hypertension

                        Take basal body temperature on waking in establishing date of ovulation
         */
        WAKE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3TimingEvent fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AC".equals(codeString))
          return AC;
        if ("ACD".equals(codeString))
          return ACD;
        if ("ACM".equals(codeString))
          return ACM;
        if ("ACV".equals(codeString))
          return ACV;
        if ("C".equals(codeString))
          return C;
        if ("CD".equals(codeString))
          return CD;
        if ("CM".equals(codeString))
          return CM;
        if ("CV".equals(codeString))
          return CV;
        if ("HS".equals(codeString))
          return HS;
        if ("IC".equals(codeString))
          return IC;
        if ("ICD".equals(codeString))
          return ICD;
        if ("ICM".equals(codeString))
          return ICM;
        if ("ICV".equals(codeString))
          return ICV;
        if ("PC".equals(codeString))
          return PC;
        if ("PCD".equals(codeString))
          return PCD;
        if ("PCM".equals(codeString))
          return PCM;
        if ("PCV".equals(codeString))
          return PCV;
        if ("WAKE".equals(codeString))
          return WAKE;
        throw new FHIRException("Unknown V3TimingEvent code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AC: return "AC";
            case ACD: return "ACD";
            case ACM: return "ACM";
            case ACV: return "ACV";
            case C: return "C";
            case CD: return "CD";
            case CM: return "CM";
            case CV: return "CV";
            case HS: return "HS";
            case IC: return "IC";
            case ICD: return "ICD";
            case ICM: return "ICM";
            case ICV: return "ICV";
            case PC: return "PC";
            case PCD: return "PCD";
            case PCM: return "PCM";
            case PCV: return "PCV";
            case WAKE: return "WAKE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/TimingEvent";
        }
        public String getDefinition() {
          switch (this) {
            case AC: return "before meal (from lat. ante cibus)";
            case ACD: return "before lunch (from lat. ante cibus diurnus)";
            case ACM: return "before breakfast (from lat. ante cibus matutinus)";
            case ACV: return "before dinner (from lat. ante cibus vespertinus)";
            case C: return "Description: meal (from lat. ante cibus)";
            case CD: return "Description: lunch (from lat. cibus diurnus)";
            case CM: return "Description: breakfast (from lat. cibus matutinus)";
            case CV: return "Description: dinner (from lat. cibus vespertinus)";
            case HS: return "Description: Prior to beginning a regular period of extended sleep (this would exclude naps).  Note that this might occur at different times of day depending on a person's regular sleep schedule.";
            case IC: return "between meals (from lat. inter cibus)";
            case ICD: return "between lunch and dinner";
            case ICM: return "between breakfast and lunch";
            case ICV: return "between dinner and the hour of sleep";
            case PC: return "after meal (from lat. post cibus)";
            case PCD: return "after lunch (from lat. post cibus diurnus)";
            case PCM: return "after breakfast (from lat. post cibus matutinus)";
            case PCV: return "after dinner (from lat. post cibus vespertinus)";
            case WAKE: return "Description: Upon waking up from a regular period of sleep, in order to start regular activities (this would exclude waking up from a nap or temporarily waking up during a period of sleep)\r\n\n                        \n                           Usage Notes: e.g.\r\n\n                        Take pulse rate on waking in management of thyrotoxicosis.\r\n\n                        Take BP on waking in management of hypertension\r\n\n                        Take basal body temperature on waking in establishing date of ovulation";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AC: return "AC";
            case ACD: return "ACD";
            case ACM: return "ACM";
            case ACV: return "ACV";
            case C: return "C";
            case CD: return "CD";
            case CM: return "CM";
            case CV: return "CV";
            case HS: return "HS";
            case IC: return "IC";
            case ICD: return "ICD";
            case ICM: return "ICM";
            case ICV: return "ICV";
            case PC: return "PC";
            case PCD: return "PCD";
            case PCM: return "PCM";
            case PCV: return "PCV";
            case WAKE: return "WAKE";
            default: return "?";
          }
    }


}

