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

// Generated on Mon, Dec 21, 2015 19:58-0500 for FHIR v1.2.0


import org.hl7.fhir.dstu21.model.EnumFactory;

public class LOINC530378AnswerlistEnumFactory implements EnumFactory<LOINC530378Answerlist> {

  public LOINC530378Answerlist fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("LA6668-3".equals(codeString))
      return LOINC530378Answerlist.LA66683;
    if ("LA6669-1".equals(codeString))
      return LOINC530378Answerlist.LA66691;
    if ("LA6682-4".equals(codeString))
      return LOINC530378Answerlist.LA66824;
    if ("LA6675-8".equals(codeString))
      return LOINC530378Answerlist.LA66758;
    if ("LA6674-1".equals(codeString))
      return LOINC530378Answerlist.LA66741;
    throw new IllegalArgumentException("Unknown LOINC530378Answerlist code '"+codeString+"'");
  }

  public String toCode(LOINC530378Answerlist code) {
    if (code == LOINC530378Answerlist.LA66683)
      return "LA6668-3";
    if (code == LOINC530378Answerlist.LA66691)
      return "LA6669-1";
    if (code == LOINC530378Answerlist.LA66824)
      return "LA6682-4";
    if (code == LOINC530378Answerlist.LA66758)
      return "LA6675-8";
    if (code == LOINC530378Answerlist.LA66741)
      return "LA6674-1";
    return "?";
  }

    public String toSystem(LOINC530378Answerlist code) {
      return code.getSystem();
      }

}

