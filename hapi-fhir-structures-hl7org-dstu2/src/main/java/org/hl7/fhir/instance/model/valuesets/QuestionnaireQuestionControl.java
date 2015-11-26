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


public enum QuestionnaireQuestionControl {

        /**
         * A control which provides a list of potential matches based on text entered into a control.  Used for large choice sets where text-matching is an appropriate discovery mechanism.
         */
        AUTOCOMPLETE, 
        /**
         * A control where an item (or multiple items) can be selected from a list that is only displayed when the user is editing the field.
         */
        DROPDOWN, 
        /**
         * A control where choices are listed with a box beside them.  The box can be toggled to select or de-select a given choice.  Multiple selections may be possible.
         */
        CHECKBOX, 
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
        public static QuestionnaireQuestionControl fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("autocomplete".equals(codeString))
          return AUTOCOMPLETE;
        if ("drop-down".equals(codeString))
          return DROPDOWN;
        if ("check-box".equals(codeString))
          return CHECKBOX;
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
        throw new Exception("Unknown QuestionnaireQuestionControl code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AUTOCOMPLETE: return "autocomplete";
            case DROPDOWN: return "drop-down";
            case CHECKBOX: return "check-box";
            case LOOKUP: return "lookup";
            case RADIOBUTTON: return "radio-button";
            case SLIDER: return "slider";
            case SPINNER: return "spinner";
            case TEXTBOX: return "text-box";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/questionnaire-question-control";
        }
        public String getDefinition() {
          switch (this) {
            case AUTOCOMPLETE: return "A control which provides a list of potential matches based on text entered into a control.  Used for large choice sets where text-matching is an appropriate discovery mechanism.";
            case DROPDOWN: return "A control where an item (or multiple items) can be selected from a list that is only displayed when the user is editing the field.";
            case CHECKBOX: return "A control where choices are listed with a box beside them.  The box can be toggled to select or de-select a given choice.  Multiple selections may be possible.";
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
            case AUTOCOMPLETE: return "Auto-complete";
            case DROPDOWN: return "Drop down";
            case CHECKBOX: return "Check-box";
            case LOOKUP: return "Lookup";
            case RADIOBUTTON: return "Radio Button";
            case SLIDER: return "Slider";
            case SPINNER: return "Spinner";
            case TEXTBOX: return "Text Box";
            default: return "?";
          }
    }


}

