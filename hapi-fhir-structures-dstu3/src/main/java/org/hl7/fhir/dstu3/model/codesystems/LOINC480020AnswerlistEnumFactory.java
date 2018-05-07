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

public class LOINC480020AnswerlistEnumFactory implements EnumFactory<LOINC480020Answerlist> {

  public LOINC480020Answerlist fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("LA6683-2".equals(codeString))
      return LOINC480020Answerlist.LA66832;
    if ("LA6684-0".equals(codeString))
      return LOINC480020Answerlist.LA66840;
    if ("LA10429-1".equals(codeString))
      return LOINC480020Answerlist.LA104291;
    if ("LA18194-3".equals(codeString))
      return LOINC480020Answerlist.LA181943;
    if ("LA18195-0".equals(codeString))
      return LOINC480020Answerlist.LA181950;
    if ("LA18196-8".equals(codeString))
      return LOINC480020Answerlist.LA181968;
    if ("LA18197-6".equals(codeString))
      return LOINC480020Answerlist.LA181976;
    throw new IllegalArgumentException("Unknown LOINC480020Answerlist code '"+codeString+"'");
  }

  public String toCode(LOINC480020Answerlist code) {
    if (code == LOINC480020Answerlist.LA66832)
      return "LA6683-2";
    if (code == LOINC480020Answerlist.LA66840)
      return "LA6684-0";
    if (code == LOINC480020Answerlist.LA104291)
      return "LA10429-1";
    if (code == LOINC480020Answerlist.LA181943)
      return "LA18194-3";
    if (code == LOINC480020Answerlist.LA181950)
      return "LA18195-0";
    if (code == LOINC480020Answerlist.LA181968)
      return "LA18196-8";
    if (code == LOINC480020Answerlist.LA181976)
      return "LA18197-6";
    return "?";
  }

    public String toSystem(LOINC480020Answerlist code) {
      return code.getSystem();
      }

}

