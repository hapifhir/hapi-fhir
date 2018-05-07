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

// Generated on Sun, Mar 12, 2017 20:35-0400 for FHIR v1.9.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class LOINC480194AnswerlistEnumFactory implements EnumFactory<LOINC480194Answerlist> {

  public LOINC480194Answerlist fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("LA9658-1".equals(codeString))
      return LOINC480194Answerlist.LA96581;
    if ("LA6692-3".equals(codeString))
      return LOINC480194Answerlist.LA66923;
    if ("LA6686-5".equals(codeString))
      return LOINC480194Answerlist.LA66865;
    if ("LA6687-3".equals(codeString))
      return LOINC480194Answerlist.LA66873;
    if ("LA6688-1".equals(codeString))
      return LOINC480194Answerlist.LA66881;
    if ("LA6689-9".equals(codeString))
      return LOINC480194Answerlist.LA66899;
    if ("LA6690-7".equals(codeString))
      return LOINC480194Answerlist.LA66907;
    throw new IllegalArgumentException("Unknown LOINC480194Answerlist code '"+codeString+"'");
  }

  public String toCode(LOINC480194Answerlist code) {
    if (code == LOINC480194Answerlist.LA96581)
      return "LA9658-1";
    if (code == LOINC480194Answerlist.LA66923)
      return "LA6692-3";
    if (code == LOINC480194Answerlist.LA66865)
      return "LA6686-5";
    if (code == LOINC480194Answerlist.LA66873)
      return "LA6687-3";
    if (code == LOINC480194Answerlist.LA66881)
      return "LA6688-1";
    if (code == LOINC480194Answerlist.LA66899)
      return "LA6689-9";
    if (code == LOINC480194Answerlist.LA66907)
      return "LA6690-7";
    return "?";
  }

    public String toSystem(LOINC480194Answerlist code) {
      return code.getSystem();
      }

}

