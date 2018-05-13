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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class CodesystemAltcodeKindEnumFactory implements EnumFactory<CodesystemAltcodeKind> {

  public CodesystemAltcodeKind fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("alternate".equals(codeString))
      return CodesystemAltcodeKind.ALTERNATE;
    if ("deprecated".equals(codeString))
      return CodesystemAltcodeKind.DEPRECATED;
    if ("case-insensitive".equals(codeString))
      return CodesystemAltcodeKind.CASEINSENSITIVE;
    if ("case-sensitive".equals(codeString))
      return CodesystemAltcodeKind.CASESENSITIVE;
    if ("expression".equals(codeString))
      return CodesystemAltcodeKind.EXPRESSION;
    throw new IllegalArgumentException("Unknown CodesystemAltcodeKind code '"+codeString+"'");
  }

  public String toCode(CodesystemAltcodeKind code) {
    if (code == CodesystemAltcodeKind.ALTERNATE)
      return "alternate";
    if (code == CodesystemAltcodeKind.DEPRECATED)
      return "deprecated";
    if (code == CodesystemAltcodeKind.CASEINSENSITIVE)
      return "case-insensitive";
    if (code == CodesystemAltcodeKind.CASESENSITIVE)
      return "case-sensitive";
    if (code == CodesystemAltcodeKind.EXPRESSION)
      return "expression";
    return "?";
  }

    public String toSystem(CodesystemAltcodeKind code) {
      return code.getSystem();
      }

}

