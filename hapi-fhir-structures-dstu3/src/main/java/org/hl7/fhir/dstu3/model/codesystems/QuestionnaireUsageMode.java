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

public enum QuestionnaireUsageMode {

        /**
         * Render the item regardless of usage mode
         */
        CAPTUREDISPLAY, 
        /**
         * Render the item only when capturing data
         */
        CAPTURE, 
        /**
         * Render the item only when displaying a completed form
         */
        DISPLAY, 
        /**
         * Render the item only when displaying a completed form and the item has been answered (or has child items that have been answered)
         */
        DISPLAYNONEMPTY, 
        /**
         * Render the item when capturing data or when displaying a completed form and the item has been answered (or has child items that have been answered)
         */
        CAPTUREDISPLAYNONEMPTY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuestionnaireUsageMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("capture-display".equals(codeString))
          return CAPTUREDISPLAY;
        if ("capture".equals(codeString))
          return CAPTURE;
        if ("display".equals(codeString))
          return DISPLAY;
        if ("display-non-empty".equals(codeString))
          return DISPLAYNONEMPTY;
        if ("capture-display-non-empty".equals(codeString))
          return CAPTUREDISPLAYNONEMPTY;
        throw new FHIRException("Unknown QuestionnaireUsageMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CAPTUREDISPLAY: return "capture-display";
            case CAPTURE: return "capture";
            case DISPLAY: return "display";
            case DISPLAYNONEMPTY: return "display-non-empty";
            case CAPTUREDISPLAYNONEMPTY: return "capture-display-non-empty";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/questionnaire-usage-mode";
        }
        public String getDefinition() {
          switch (this) {
            case CAPTUREDISPLAY: return "Render the item regardless of usage mode";
            case CAPTURE: return "Render the item only when capturing data";
            case DISPLAY: return "Render the item only when displaying a completed form";
            case DISPLAYNONEMPTY: return "Render the item only when displaying a completed form and the item has been answered (or has child items that have been answered)";
            case CAPTUREDISPLAYNONEMPTY: return "Render the item when capturing data or when displaying a completed form and the item has been answered (or has child items that have been answered)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CAPTUREDISPLAY: return "Capture & Display";
            case CAPTURE: return "Capture Only";
            case DISPLAY: return "Display Only";
            case DISPLAYNONEMPTY: return "Display when Answered";
            case CAPTUREDISPLAYNONEMPTY: return "Capture or, if answered, Display";
            default: return "?";
          }
    }


}

