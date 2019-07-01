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

public class CommunicationRequestStatusEnumFactory implements EnumFactory<CommunicationRequestStatus> {

  public CommunicationRequestStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("proposed".equals(codeString))
      return CommunicationRequestStatus.PROPOSED;
    if ("planned".equals(codeString))
      return CommunicationRequestStatus.PLANNED;
    if ("requested".equals(codeString))
      return CommunicationRequestStatus.REQUESTED;
    if ("received".equals(codeString))
      return CommunicationRequestStatus.RECEIVED;
    if ("accepted".equals(codeString))
      return CommunicationRequestStatus.ACCEPTED;
    if ("in-progress".equals(codeString))
      return CommunicationRequestStatus.INPROGRESS;
    if ("completed".equals(codeString))
      return CommunicationRequestStatus.COMPLETED;
    if ("suspended".equals(codeString))
      return CommunicationRequestStatus.SUSPENDED;
    if ("rejected".equals(codeString))
      return CommunicationRequestStatus.REJECTED;
    if ("failed".equals(codeString))
      return CommunicationRequestStatus.FAILED;
    throw new IllegalArgumentException("Unknown CommunicationRequestStatus code '"+codeString+"'");
  }

  public String toCode(CommunicationRequestStatus code) {
    if (code == CommunicationRequestStatus.PROPOSED)
      return "proposed";
    if (code == CommunicationRequestStatus.PLANNED)
      return "planned";
    if (code == CommunicationRequestStatus.REQUESTED)
      return "requested";
    if (code == CommunicationRequestStatus.RECEIVED)
      return "received";
    if (code == CommunicationRequestStatus.ACCEPTED)
      return "accepted";
    if (code == CommunicationRequestStatus.INPROGRESS)
      return "in-progress";
    if (code == CommunicationRequestStatus.COMPLETED)
      return "completed";
    if (code == CommunicationRequestStatus.SUSPENDED)
      return "suspended";
    if (code == CommunicationRequestStatus.REJECTED)
      return "rejected";
    if (code == CommunicationRequestStatus.FAILED)
      return "failed";
    return "?";
  }

    public String toSystem(CommunicationRequestStatus code) {
      return code.getSystem();
      }

}

