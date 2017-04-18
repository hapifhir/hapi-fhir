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

public class V3AcknowledgementTypeEnumFactory implements EnumFactory<V3AcknowledgementType> {

  public V3AcknowledgementType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("AA".equals(codeString))
      return V3AcknowledgementType.AA;
    if ("AE".equals(codeString))
      return V3AcknowledgementType.AE;
    if ("AR".equals(codeString))
      return V3AcknowledgementType.AR;
    if ("CA".equals(codeString))
      return V3AcknowledgementType.CA;
    if ("CE".equals(codeString))
      return V3AcknowledgementType.CE;
    if ("CR".equals(codeString))
      return V3AcknowledgementType.CR;
    throw new IllegalArgumentException("Unknown V3AcknowledgementType code '"+codeString+"'");
  }

  public String toCode(V3AcknowledgementType code) {
    if (code == V3AcknowledgementType.AA)
      return "AA";
    if (code == V3AcknowledgementType.AE)
      return "AE";
    if (code == V3AcknowledgementType.AR)
      return "AR";
    if (code == V3AcknowledgementType.CA)
      return "CA";
    if (code == V3AcknowledgementType.CE)
      return "CE";
    if (code == V3AcknowledgementType.CR)
      return "CR";
    return "?";
  }

    public String toSystem(V3AcknowledgementType code) {
      return code.getSystem();
      }

}

