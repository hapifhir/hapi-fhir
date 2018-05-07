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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3CalendarCycle {

        /**
         * CalendarCycleOneLetter
         */
        _CALENDARCYCLEONELETTER, 
        /**
         * week (continuous)
         */
        CW, 
        /**
         * year
         */
        CY, 
        /**
         * day of the month
         */
        D, 
        /**
         * day of the week (begins with Monday)
         */
        DW, 
        /**
         * hour of the day
         */
        H, 
        /**
         * month of the year
         */
        M, 
        /**
         * minute of the hour
         */
        N, 
        /**
         * second of the minute
         */
        S, 
        /**
         * CalendarCycleTwoLetter
         */
        _CALENDARCYCLETWOLETTER, 
        /**
         * day (continuous)
         */
        CD, 
        /**
         * hour (continuous)
         */
        CH, 
        /**
         * month (continuous)
         */
        CM, 
        /**
         * minute (continuous)
         */
        CN, 
        /**
         * second (continuous)
         */
        CS, 
        /**
         * day of the year
         */
        DY, 
        /**
         * week of the year
         */
        WY, 
        /**
         * The week with the month's first Thursday in it (analagous to the ISO 8601 definition for week of the year).
         */
        WM, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3CalendarCycle fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_CalendarCycleOneLetter".equals(codeString))
          return _CALENDARCYCLEONELETTER;
        if ("CW".equals(codeString))
          return CW;
        if ("CY".equals(codeString))
          return CY;
        if ("D".equals(codeString))
          return D;
        if ("DW".equals(codeString))
          return DW;
        if ("H".equals(codeString))
          return H;
        if ("M".equals(codeString))
          return M;
        if ("N".equals(codeString))
          return N;
        if ("S".equals(codeString))
          return S;
        if ("_CalendarCycleTwoLetter".equals(codeString))
          return _CALENDARCYCLETWOLETTER;
        if ("CD".equals(codeString))
          return CD;
        if ("CH".equals(codeString))
          return CH;
        if ("CM".equals(codeString))
          return CM;
        if ("CN".equals(codeString))
          return CN;
        if ("CS".equals(codeString))
          return CS;
        if ("DY".equals(codeString))
          return DY;
        if ("WY".equals(codeString))
          return WY;
        if ("WM".equals(codeString))
          return WM;
        throw new FHIRException("Unknown V3CalendarCycle code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _CALENDARCYCLEONELETTER: return "_CalendarCycleOneLetter";
            case CW: return "CW";
            case CY: return "CY";
            case D: return "D";
            case DW: return "DW";
            case H: return "H";
            case M: return "M";
            case N: return "N";
            case S: return "S";
            case _CALENDARCYCLETWOLETTER: return "_CalendarCycleTwoLetter";
            case CD: return "CD";
            case CH: return "CH";
            case CM: return "CM";
            case CN: return "CN";
            case CS: return "CS";
            case DY: return "DY";
            case WY: return "WY";
            case WM: return "WM";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/CalendarCycle";
        }
        public String getDefinition() {
          switch (this) {
            case _CALENDARCYCLEONELETTER: return "CalendarCycleOneLetter";
            case CW: return "week (continuous)";
            case CY: return "year";
            case D: return "day of the month";
            case DW: return "day of the week (begins with Monday)";
            case H: return "hour of the day";
            case M: return "month of the year";
            case N: return "minute of the hour";
            case S: return "second of the minute";
            case _CALENDARCYCLETWOLETTER: return "CalendarCycleTwoLetter";
            case CD: return "day (continuous)";
            case CH: return "hour (continuous)";
            case CM: return "month (continuous)";
            case CN: return "minute (continuous)";
            case CS: return "second (continuous)";
            case DY: return "day of the year";
            case WY: return "week of the year";
            case WM: return "The week with the month's first Thursday in it (analagous to the ISO 8601 definition for week of the year).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _CALENDARCYCLEONELETTER: return "CalendarCycleOneLetter";
            case CW: return "week (continuous)";
            case CY: return "year";
            case D: return "day of the month";
            case DW: return "day of the week (begins with Monday)";
            case H: return "hour of the day";
            case M: return "month of the year";
            case N: return "minute of the hour";
            case S: return "second of the minute";
            case _CALENDARCYCLETWOLETTER: return "CalendarCycleTwoLetter";
            case CD: return "day (continuous)";
            case CH: return "hour (continuous)";
            case CM: return "month (continuous)";
            case CN: return "minute (continuous)";
            case CS: return "second (continuous)";
            case DY: return "day of the year";
            case WY: return "week of the year";
            case WM: return "week of the month";
            default: return "?";
          }
    }


}

