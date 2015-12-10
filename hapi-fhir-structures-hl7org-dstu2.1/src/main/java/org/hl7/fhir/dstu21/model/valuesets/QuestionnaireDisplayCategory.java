package org.hl7.fhir.dstu21.model.valuesets;

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

// Generated on Sun, Dec 6, 2015 19:25-0500 for FHIR v1.1.0


import org.hl7.fhir.exceptions.FHIRException;

public enum QuestionnaireDisplayCategory {

        /**
         * The text provides guidances on how to populate or use a portion of the questionnaire (or the questionnaire as a whole).
         */
        INSTRUCTIONS, 
        /**
         * The text indicates the units of measure associated with the parent question.
         */
        UNITS, 
        /**
         * The text provides guidance on how the information should be or will be handled from a security/confidentiality/access control perspective when completed
         */
        SECURITY, 
        /**
         * A control where editing an item spawns a separate dialog box or screen permitting a user to navigate, filter or otherwise discover an appropriate match.  Useful for large choice sets where text matching is not an appropriate discovery mechanism.  Such screens must generally be tuned for the specific choice list structure.
         */
        LOOKUP, 
        /**
         * A control where choices are listed with a button beside them.  The button can be toggled to select or de-select a given choice.  Selecting one item deselects all others.
         */
        RADIOBUTTON, 
        /**
         * A control where an axis is displayed between the high and low values and the control can be visually manipulated to select a value anywhere on the axis.
         */
        SLIDER, 
        /**
         * A control where a list of numeric or other ordered values can be scrolled through via arrows.
         */
        SPINNER, 
        /**
         * A control where a user can type in their answer freely.
         */
        TEXTBOX, 
        /**
         * added to help the parsers
         */
        NULL;
        public static QuestionnaireDisplayCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instructions".equals(codeString))
          return INSTRUCTIONS;
        if ("units".equals(codeString))
          return UNITS;
        if ("security".equals(codeString))
          return SECURITY;
        if ("lookup".equals(codeString))
          return LOOKUP;
        if ("radio-button".equals(codeString))
          return RADIOBUTTON;
        if ("slider".equals(codeString))
          return SLIDER;
        if ("spinner".equals(codeString))
          return SPINNER;
        if ("text-box".equals(codeString))
          return TEXTBOX;
        throw new FHIRException("Unknown QuestionnaireDisplayCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTRUCTIONS: return "instructions";
            case UNITS: return "units";
            case SECURITY: return "security";
            case LOOKUP: return "lookup";
            case RADIOBUTTON: return "radio-button";
            case SLIDER: return "slider";
            case SPINNER: return "spinner";
            case TEXTBOX: return "text-box";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/questionnaire-display-category";
        }
        public String getDefinition() {
          switch (this) {
            case INSTRUCTIONS: return "The text provides guidances on how to populate or use a portion of the questionnaire (or the questionnaire as a whole).";
            case UNITS: return "The text indicates the units of measure associated with the parent question.";
            case SECURITY: return "The text provides guidance on how the information should be or will be handled from a security/confidentiality/access control perspective when completed";
            case LOOKUP: return "A control where editing an item spawns a separate dialog box or screen permitting a user to navigate, filter or otherwise discover an appropriate match.  Useful for large choice sets where text matching is not an appropriate discovery mechanism.  Such screens must generally be tuned for the specific choice list structure.";
            case RADIOBUTTON: return "A control where choices are listed with a button beside them.  The button can be toggled to select or de-select a given choice.  Selecting one item deselects all others.";
            case SLIDER: return "A control where an axis is displayed between the high and low values and the control can be visually manipulated to select a value anywhere on the axis.";
            case SPINNER: return "A control where a list of numeric or other ordered values can be scrolled through via arrows.";
            case TEXTBOX: return "A control where a user can type in their answer freely.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTRUCTIONS: return "Instructions";
            case UNITS: return "Units";
            case SECURITY: return "Security";
            case LOOKUP: return "Lookup";
            case RADIOBUTTON: return "Radio Button";
            case SLIDER: return "Slider";
            case SPINNER: return "Spinner";
            case TEXTBOX: return "Text Box";
            default: return "?";
          }
    }


}

