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


import org.hl7.fhir.instance.model.EnumFactory;

public class QuestionnaireQuestionControlEnumFactory implements EnumFactory<QuestionnaireQuestionControl> {

  public QuestionnaireQuestionControl fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("autocomplete".equals(codeString))
      return QuestionnaireQuestionControl.AUTOCOMPLETE;
    if ("drop-down".equals(codeString))
      return QuestionnaireQuestionControl.DROPDOWN;
    if ("check-box".equals(codeString))
      return QuestionnaireQuestionControl.CHECKBOX;
    if ("lookup".equals(codeString))
      return QuestionnaireQuestionControl.LOOKUP;
    if ("radio-button".equals(codeString))
      return QuestionnaireQuestionControl.RADIOBUTTON;
    if ("slider".equals(codeString))
      return QuestionnaireQuestionControl.SLIDER;
    if ("spinner".equals(codeString))
      return QuestionnaireQuestionControl.SPINNER;
    if ("text-box".equals(codeString))
      return QuestionnaireQuestionControl.TEXTBOX;
    throw new IllegalArgumentException("Unknown QuestionnaireQuestionControl code '"+codeString+"'");
  }

  public String toCode(QuestionnaireQuestionControl code) {
    if (code == QuestionnaireQuestionControl.AUTOCOMPLETE)
      return "autocomplete";
    if (code == QuestionnaireQuestionControl.DROPDOWN)
      return "drop-down";
    if (code == QuestionnaireQuestionControl.CHECKBOX)
      return "check-box";
    if (code == QuestionnaireQuestionControl.LOOKUP)
      return "lookup";
    if (code == QuestionnaireQuestionControl.RADIOBUTTON)
      return "radio-button";
    if (code == QuestionnaireQuestionControl.SLIDER)
      return "slider";
    if (code == QuestionnaireQuestionControl.SPINNER)
      return "spinner";
    if (code == QuestionnaireQuestionControl.TEXTBOX)
      return "text-box";
    return "?";
  }


}

