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


import org.hl7.fhir.r4.model.EnumFactory;

public class QuestionnaireItemControlEnumFactory implements EnumFactory<QuestionnaireItemControl> {

  public QuestionnaireItemControl fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("group".equals(codeString))
      return QuestionnaireItemControl.GROUP;
    if ("list".equals(codeString))
      return QuestionnaireItemControl.LIST;
    if ("table".equals(codeString))
      return QuestionnaireItemControl.TABLE;
    if ("header".equals(codeString))
      return QuestionnaireItemControl.HEADER;
    if ("footer".equals(codeString))
      return QuestionnaireItemControl.FOOTER;
    if ("text".equals(codeString))
      return QuestionnaireItemControl.TEXT;
    if ("inline".equals(codeString))
      return QuestionnaireItemControl.INLINE;
    if ("prompt".equals(codeString))
      return QuestionnaireItemControl.PROMPT;
    if ("unit".equals(codeString))
      return QuestionnaireItemControl.UNIT;
    if ("lower".equals(codeString))
      return QuestionnaireItemControl.LOWER;
    if ("upper".equals(codeString))
      return QuestionnaireItemControl.UPPER;
    if ("flyover".equals(codeString))
      return QuestionnaireItemControl.FLYOVER;
    if ("help".equals(codeString))
      return QuestionnaireItemControl.HELP;
    if ("question".equals(codeString))
      return QuestionnaireItemControl.QUESTION;
    if ("autocomplete".equals(codeString))
      return QuestionnaireItemControl.AUTOCOMPLETE;
    if ("drop-down".equals(codeString))
      return QuestionnaireItemControl.DROPDOWN;
    if ("check-box".equals(codeString))
      return QuestionnaireItemControl.CHECKBOX;
    if ("lookup".equals(codeString))
      return QuestionnaireItemControl.LOOKUP;
    if ("radio-button".equals(codeString))
      return QuestionnaireItemControl.RADIOBUTTON;
    if ("slider".equals(codeString))
      return QuestionnaireItemControl.SLIDER;
    if ("spinner".equals(codeString))
      return QuestionnaireItemControl.SPINNER;
    if ("text-box".equals(codeString))
      return QuestionnaireItemControl.TEXTBOX;
    throw new IllegalArgumentException("Unknown QuestionnaireItemControl code '"+codeString+"'");
  }

  public String toCode(QuestionnaireItemControl code) {
    if (code == QuestionnaireItemControl.GROUP)
      return "group";
    if (code == QuestionnaireItemControl.LIST)
      return "list";
    if (code == QuestionnaireItemControl.TABLE)
      return "table";
    if (code == QuestionnaireItemControl.HEADER)
      return "header";
    if (code == QuestionnaireItemControl.FOOTER)
      return "footer";
    if (code == QuestionnaireItemControl.TEXT)
      return "text";
    if (code == QuestionnaireItemControl.INLINE)
      return "inline";
    if (code == QuestionnaireItemControl.PROMPT)
      return "prompt";
    if (code == QuestionnaireItemControl.UNIT)
      return "unit";
    if (code == QuestionnaireItemControl.LOWER)
      return "lower";
    if (code == QuestionnaireItemControl.UPPER)
      return "upper";
    if (code == QuestionnaireItemControl.FLYOVER)
      return "flyover";
    if (code == QuestionnaireItemControl.HELP)
      return "help";
    if (code == QuestionnaireItemControl.QUESTION)
      return "question";
    if (code == QuestionnaireItemControl.AUTOCOMPLETE)
      return "autocomplete";
    if (code == QuestionnaireItemControl.DROPDOWN)
      return "drop-down";
    if (code == QuestionnaireItemControl.CHECKBOX)
      return "check-box";
    if (code == QuestionnaireItemControl.LOOKUP)
      return "lookup";
    if (code == QuestionnaireItemControl.RADIOBUTTON)
      return "radio-button";
    if (code == QuestionnaireItemControl.SLIDER)
      return "slider";
    if (code == QuestionnaireItemControl.SPINNER)
      return "spinner";
    if (code == QuestionnaireItemControl.TEXTBOX)
      return "text-box";
    return "?";
  }

    public String toSystem(QuestionnaireItemControl code) {
      return code.getSystem();
      }

}

