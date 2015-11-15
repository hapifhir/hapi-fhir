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

public class V3EducationLevelEnumFactory implements EnumFactory<V3EducationLevel> {

  public V3EducationLevel fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ASSOC".equals(codeString))
      return V3EducationLevel.ASSOC;
    if ("BD".equals(codeString))
      return V3EducationLevel.BD;
    if ("ELEM".equals(codeString))
      return V3EducationLevel.ELEM;
    if ("GD".equals(codeString))
      return V3EducationLevel.GD;
    if ("HS".equals(codeString))
      return V3EducationLevel.HS;
    if ("PB".equals(codeString))
      return V3EducationLevel.PB;
    if ("POSTG".equals(codeString))
      return V3EducationLevel.POSTG;
    if ("SCOL".equals(codeString))
      return V3EducationLevel.SCOL;
    if ("SEC".equals(codeString))
      return V3EducationLevel.SEC;
    throw new IllegalArgumentException("Unknown V3EducationLevel code '"+codeString+"'");
  }

  public String toCode(V3EducationLevel code) {
    if (code == V3EducationLevel.ASSOC)
      return "ASSOC";
    if (code == V3EducationLevel.BD)
      return "BD";
    if (code == V3EducationLevel.ELEM)
      return "ELEM";
    if (code == V3EducationLevel.GD)
      return "GD";
    if (code == V3EducationLevel.HS)
      return "HS";
    if (code == V3EducationLevel.PB)
      return "PB";
    if (code == V3EducationLevel.POSTG)
      return "POSTG";
    if (code == V3EducationLevel.SCOL)
      return "SCOL";
    if (code == V3EducationLevel.SEC)
      return "SEC";
    return "?";
  }


}

