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

// Generated on Thu, Jul 23, 2015 16:50-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class ObservationValueabsentreasonEnumFactory implements EnumFactory<ObservationValueabsentreason> {

  public ObservationValueabsentreason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("unknown".equals(codeString))
      return ObservationValueabsentreason.UNKNOWN;
    if ("asked".equals(codeString))
      return ObservationValueabsentreason.ASKED;
    if ("temp".equals(codeString))
      return ObservationValueabsentreason.TEMP;
    if ("notasked".equals(codeString))
      return ObservationValueabsentreason.NOTASKED;
    if ("masked".equals(codeString))
      return ObservationValueabsentreason.MASKED;
    if ("unsupported".equals(codeString))
      return ObservationValueabsentreason.UNSUPPORTED;
    if ("astext".equals(codeString))
      return ObservationValueabsentreason.ASTEXT;
    if ("error".equals(codeString))
      return ObservationValueabsentreason.ERROR;
    throw new IllegalArgumentException("Unknown ObservationValueabsentreason code '"+codeString+"'");
  }

  public String toCode(ObservationValueabsentreason code) {
    if (code == ObservationValueabsentreason.UNKNOWN)
      return "unknown";
    if (code == ObservationValueabsentreason.ASKED)
      return "asked";
    if (code == ObservationValueabsentreason.TEMP)
      return "temp";
    if (code == ObservationValueabsentreason.NOTASKED)
      return "notasked";
    if (code == ObservationValueabsentreason.MASKED)
      return "masked";
    if (code == ObservationValueabsentreason.UNSUPPORTED)
      return "unsupported";
    if (code == ObservationValueabsentreason.ASTEXT)
      return "astext";
    if (code == ObservationValueabsentreason.ERROR)
      return "error";
    return "?";
  }


}

