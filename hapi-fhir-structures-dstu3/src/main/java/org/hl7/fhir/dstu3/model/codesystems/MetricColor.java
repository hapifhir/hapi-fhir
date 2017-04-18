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

public enum MetricColor {

        /**
         * Color for representation - black.
         */
        BLACK, 
        /**
         * Color for representation - red.
         */
        RED, 
        /**
         * Color for representation - green.
         */
        GREEN, 
        /**
         * Color for representation - yellow.
         */
        YELLOW, 
        /**
         * Color for representation - blue.
         */
        BLUE, 
        /**
         * Color for representation - magenta.
         */
        MAGENTA, 
        /**
         * Color for representation - cyan.
         */
        CYAN, 
        /**
         * Color for representation - white.
         */
        WHITE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MetricColor fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("black".equals(codeString))
          return BLACK;
        if ("red".equals(codeString))
          return RED;
        if ("green".equals(codeString))
          return GREEN;
        if ("yellow".equals(codeString))
          return YELLOW;
        if ("blue".equals(codeString))
          return BLUE;
        if ("magenta".equals(codeString))
          return MAGENTA;
        if ("cyan".equals(codeString))
          return CYAN;
        if ("white".equals(codeString))
          return WHITE;
        throw new FHIRException("Unknown MetricColor code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BLACK: return "black";
            case RED: return "red";
            case GREEN: return "green";
            case YELLOW: return "yellow";
            case BLUE: return "blue";
            case MAGENTA: return "magenta";
            case CYAN: return "cyan";
            case WHITE: return "white";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/metric-color";
        }
        public String getDefinition() {
          switch (this) {
            case BLACK: return "Color for representation - black.";
            case RED: return "Color for representation - red.";
            case GREEN: return "Color for representation - green.";
            case YELLOW: return "Color for representation - yellow.";
            case BLUE: return "Color for representation - blue.";
            case MAGENTA: return "Color for representation - magenta.";
            case CYAN: return "Color for representation - cyan.";
            case WHITE: return "Color for representation - white.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BLACK: return "Color Black";
            case RED: return "Color Red";
            case GREEN: return "Color Green";
            case YELLOW: return "Color Yellow";
            case BLUE: return "Color Blue";
            case MAGENTA: return "Color Magenta";
            case CYAN: return "Color Cyan";
            case WHITE: return "Color White";
            default: return "?";
          }
    }


}

