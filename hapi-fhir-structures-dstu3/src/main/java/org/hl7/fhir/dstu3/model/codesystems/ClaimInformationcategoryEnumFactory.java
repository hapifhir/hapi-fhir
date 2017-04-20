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


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ClaimInformationcategoryEnumFactory implements EnumFactory<ClaimInformationcategory> {

  public ClaimInformationcategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("info".equals(codeString))
      return ClaimInformationcategory.INFO;
    if ("discharge".equals(codeString))
      return ClaimInformationcategory.DISCHARGE;
    if ("onset".equals(codeString))
      return ClaimInformationcategory.ONSET;
    if ("related".equals(codeString))
      return ClaimInformationcategory.RELATED;
    if ("exception".equals(codeString))
      return ClaimInformationcategory.EXCEPTION;
    if ("material".equals(codeString))
      return ClaimInformationcategory.MATERIAL;
    if ("attachment".equals(codeString))
      return ClaimInformationcategory.ATTACHMENT;
    if ("missingtooth".equals(codeString))
      return ClaimInformationcategory.MISSINGTOOTH;
    if ("prosthesis".equals(codeString))
      return ClaimInformationcategory.PROSTHESIS;
    if ("other".equals(codeString))
      return ClaimInformationcategory.OTHER;
    throw new IllegalArgumentException("Unknown ClaimInformationcategory code '"+codeString+"'");
  }

  public String toCode(ClaimInformationcategory code) {
    if (code == ClaimInformationcategory.INFO)
      return "info";
    if (code == ClaimInformationcategory.DISCHARGE)
      return "discharge";
    if (code == ClaimInformationcategory.ONSET)
      return "onset";
    if (code == ClaimInformationcategory.RELATED)
      return "related";
    if (code == ClaimInformationcategory.EXCEPTION)
      return "exception";
    if (code == ClaimInformationcategory.MATERIAL)
      return "material";
    if (code == ClaimInformationcategory.ATTACHMENT)
      return "attachment";
    if (code == ClaimInformationcategory.MISSINGTOOTH)
      return "missingtooth";
    if (code == ClaimInformationcategory.PROSTHESIS)
      return "prosthesis";
    if (code == ClaimInformationcategory.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(ClaimInformationcategory code) {
      return code.getSystem();
      }

}

