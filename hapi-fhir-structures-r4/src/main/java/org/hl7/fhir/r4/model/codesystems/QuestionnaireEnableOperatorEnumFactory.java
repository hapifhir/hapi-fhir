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


import org.hl7.fhir.r4.model.EnumFactory;

public class QuestionnaireEnableOperatorEnumFactory implements EnumFactory<QuestionnaireEnableOperator> {

  public QuestionnaireEnableOperator fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("exists".equals(codeString))
      return QuestionnaireEnableOperator.EXISTS;
    if ("=".equals(codeString))
      return QuestionnaireEnableOperator.EQUAL;
    if ("!=".equals(codeString))
      return QuestionnaireEnableOperator.NOT_EQUAL;
    if (">".equals(codeString))
      return QuestionnaireEnableOperator.GREATER_THAN;
    if ("<".equals(codeString))
      return QuestionnaireEnableOperator.LESS_THAN;
    if (">=".equals(codeString))
      return QuestionnaireEnableOperator.GREATER_OR_EQUAL;
    if ("<=".equals(codeString))
      return QuestionnaireEnableOperator.LESS_OR_EQUAL;
    throw new IllegalArgumentException("Unknown QuestionnaireEnableOperator code '"+codeString+"'");
  }

  public String toCode(QuestionnaireEnableOperator code) {
    if (code == QuestionnaireEnableOperator.EXISTS)
      return "exists";
    if (code == QuestionnaireEnableOperator.EQUAL)
      return "=";
    if (code == QuestionnaireEnableOperator.NOT_EQUAL)
      return "!=";
    if (code == QuestionnaireEnableOperator.GREATER_THAN)
      return ">";
    if (code == QuestionnaireEnableOperator.LESS_THAN)
      return "<";
    if (code == QuestionnaireEnableOperator.GREATER_OR_EQUAL)
      return ">=";
    if (code == QuestionnaireEnableOperator.LESS_OR_EQUAL)
      return "<=";
    return "?";
  }

    public String toSystem(QuestionnaireEnableOperator code) {
      return code.getSystem();
      }

}

