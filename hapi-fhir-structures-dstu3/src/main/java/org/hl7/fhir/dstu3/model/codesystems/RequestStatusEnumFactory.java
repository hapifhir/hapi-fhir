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

public class RequestStatusEnumFactory implements EnumFactory<RequestStatus> {

  public RequestStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("draft".equals(codeString))
      return RequestStatus.DRAFT;
    if ("active".equals(codeString))
      return RequestStatus.ACTIVE;
    if ("suspended".equals(codeString))
      return RequestStatus.SUSPENDED;
    if ("cancelled".equals(codeString))
      return RequestStatus.CANCELLED;
    if ("completed".equals(codeString))
      return RequestStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return RequestStatus.ENTEREDINERROR;
    if ("unknown".equals(codeString))
      return RequestStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown RequestStatus code '"+codeString+"'");
  }

  public String toCode(RequestStatus code) {
    if (code == RequestStatus.DRAFT)
      return "draft";
    if (code == RequestStatus.ACTIVE)
      return "active";
    if (code == RequestStatus.SUSPENDED)
      return "suspended";
    if (code == RequestStatus.CANCELLED)
      return "cancelled";
    if (code == RequestStatus.COMPLETED)
      return "completed";
    if (code == RequestStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == RequestStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(RequestStatus code) {
      return code.getSystem();
      }

}

