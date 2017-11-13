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

public class CommunicationStatusEnumFactory implements EnumFactory<CommunicationStatus> {

  public CommunicationStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("in-progress".equals(codeString))
      return CommunicationStatus.INPROGRESS;
    if ("completed".equals(codeString))
      return CommunicationStatus.COMPLETED;
    if ("suspended".equals(codeString))
      return CommunicationStatus.SUSPENDED;
    if ("rejected".equals(codeString))
      return CommunicationStatus.REJECTED;
    if ("failed".equals(codeString))
      return CommunicationStatus.FAILED;
    throw new IllegalArgumentException("Unknown CommunicationStatus code '"+codeString+"'");
  }

  public String toCode(CommunicationStatus code) {
    if (code == CommunicationStatus.INPROGRESS)
      return "in-progress";
    if (code == CommunicationStatus.COMPLETED)
      return "completed";
    if (code == CommunicationStatus.SUSPENDED)
      return "suspended";
    if (code == CommunicationStatus.REJECTED)
      return "rejected";
    if (code == CommunicationStatus.FAILED)
      return "failed";
    return "?";
  }

    public String toSystem(CommunicationStatus code) {
      return code.getSystem();
      }

}

