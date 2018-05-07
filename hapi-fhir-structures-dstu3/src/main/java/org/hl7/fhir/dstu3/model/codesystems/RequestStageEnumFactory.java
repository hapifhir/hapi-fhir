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

// Generated on Mon, Jan 16, 2017 12:12-0500 for FHIR v1.9.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class RequestStageEnumFactory implements EnumFactory<RequestStage> {

  public RequestStage fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("proposal".equals(codeString))
      return RequestStage.PROPOSAL;
    if ("plan".equals(codeString))
      return RequestStage.PLAN;
    if ("original-order".equals(codeString))
      return RequestStage.ORIGINALORDER;
    if ("encoded".equals(codeString))
      return RequestStage.ENCODED;
    if ("reflex-order".equals(codeString))
      return RequestStage.REFLEXORDER;
    throw new IllegalArgumentException("Unknown RequestStage code '"+codeString+"'");
  }

  public String toCode(RequestStage code) {
    if (code == RequestStage.PROPOSAL)
      return "proposal";
    if (code == RequestStage.PLAN)
      return "plan";
    if (code == RequestStage.ORIGINALORDER)
      return "original-order";
    if (code == RequestStage.ENCODED)
      return "encoded";
    if (code == RequestStage.REFLEXORDER)
      return "reflex-order";
    return "?";
  }

    public String toSystem(RequestStage code) {
      return code.getSystem();
      }

}

