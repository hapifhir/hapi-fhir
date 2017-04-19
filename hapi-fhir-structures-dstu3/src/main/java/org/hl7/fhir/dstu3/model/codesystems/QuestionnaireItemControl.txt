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

public enum QuestionnaireItemControl {

        /**
         * UI controls relevant to organizing groups of questions
         */
        GROUP, 
        /**
         * Questions within the group should be listed sequentially
         */
        LIST, 
        /**
         * Questions within the group are rows in the table with possible answers as columns
         */
        TABLE, 
        /**
         * The group is to be continuously visible at the top of the questionnaire
         */
        HEADER, 
        /**
         * The group is to be continuously visible at the bottom of the questionnaire
         */
        FOOTER, 
        /**
         * UI controls relevant to rendering questionnaire text items
         */
        TEXT, 
        /**
         * Text is displayed as a paragraph in a sequential position between sibling items (default behavior)
         */
        INLINE, 
        /**
         * Text is displayed immediately below or within the answer-entry area of the containing question item (typically as a guide for what to enter)
         */
        PROMPT, 
        /**
         * Text is displayed adjacent (horizontally or vertically) to the answer space for the parent question, typically to indicate a unit of measure
         */
        UNIT, 
        /**
         * Text is displayed to the left of the set of answer choices or a scaling control for the parent question item to indicate the meaning of the 'lower' bound.  E.g. 'Strongly disagree'
         */
        LOWER, 
        /**
         * Text is displayed to the right of the set of answer choices or a scaling control for the parent question item to indicate the meaning of the 'upper' bound.  E.g. 'Strongly agree'
         */
        UPPER, 
        /**
         * Text is temporarily visible over top of an item if the mouse is positioned over top of the text for the containing item
         */
        FLYOVER, 
        /**
         * Text is displayed in a dialog box or similar control if invoked by pushing a button or some other UI-appropriate action to request 'help' for a question, group or the questionnaire as a whole (depending what the text is nested within)
         */
        HELP, 
        /**
         * UI controls relevant to capturing question data
         */
        QUESTION, 
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
        public static QuestionnaireItemControl fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("group".equals(codeString))
          return GROUP;
        if ("list".equals(codeString))
          return LIST;
        if ("table".equals(codeString))
          return TABLE;
        if ("header".equals(codeString))
          return HEADER;
        if ("footer".equals(codeString))
          return FOOTER;
        if ("text".equals(codeString))
          return TEXT;
        if ("inline".equals(codeString))
          return INLINE;
        if ("prompt".equals(codeString))
          return PROMPT;
        if ("unit".equals(codeString))
          return UNIT;
        if ("lower".equals(codeString))
          return LOWER;
        if ("upper".equals(codeString))
          return UPPER;
        if ("flyover".equals(codeString))
          return FLYOVER;
        if ("help".equals(codeString))
          return HELP;
        if ("question".equals(codeString))
          return QUESTION;
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
        throw new FHIRException("Unknown QuestionnaireItemControl code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GROUP: return "group";
            case LIST: return "list";
            case TABLE: return "table";
            case HEADER: return "header";
            case FOOTER: return "footer";
            case TEXT: return "text";
            case INLINE: return "inline";
            case PROMPT: return "prompt";
            case UNIT: return "unit";
            case LOWER: return "lower";
            case UPPER: return "upper";
            case FLYOVER: return "flyover";
            case HELP: return "help";
            case QUESTION: return "question";
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
          return "http://hl7.org/fhir/questionnaire-item-control";
        }
        public String getDefinition() {
          switch (this) {
            case GROUP: return "UI controls relevant to organizing groups of questions";
            case LIST: return "Questions within the group should be listed sequentially";
            case TABLE: return "Questions within the group are rows in the table with possible answers as columns";
            case HEADER: return "The group is to be continuously visible at the top of the questionnaire";
            case FOOTER: return "The group is to be continuously visible at the bottom of the questionnaire";
            case TEXT: return "UI controls relevant to rendering questionnaire text items";
            case INLINE: return "Text is displayed as a paragraph in a sequential position between sibling items (default behavior)";
            case PROMPT: return "Text is displayed immediately below or within the answer-entry area of the containing question item (typically as a guide for what to enter)";
            case UNIT: return "Text is displayed adjacent (horizontally or vertically) to the answer space for the parent question, typically to indicate a unit of measure";
            case LOWER: return "Text is displayed to the left of the set of answer choices or a scaling control for the parent question item to indicate the meaning of the 'lower' bound.  E.g. 'Strongly disagree'";
            case UPPER: return "Text is displayed to the right of the set of answer choices or a scaling control for the parent question item to indicate the meaning of the 'upper' bound.  E.g. 'Strongly agree'";
            case FLYOVER: return "Text is temporarily visible over top of an item if the mouse is positioned over top of the text for the containing item";
            case HELP: return "Text is displayed in a dialog box or similar control if invoked by pushing a button or some other UI-appropriate action to request 'help' for a question, group or the questionnaire as a whole (depending what the text is nested within)";
            case QUESTION: return "UI controls relevant to capturing question data";
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
            case GROUP: return "group";
            case LIST: return "List";
            case TABLE: return "Table";
            case HEADER: return "Header";
            case FOOTER: return "Footer";
            case TEXT: return "text";
            case INLINE: return "In-line";
            case PROMPT: return "Prompt";
            case UNIT: return "Unit";
            case LOWER: return "Lower-bound";
            case UPPER: return "Upper-bound";
            case FLYOVER: return "Fly-over";
            case HELP: return "Help-Button";
            case QUESTION: return "question";
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

