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

public class SupplyrequestStatusEnumFactory implements EnumFactory<SupplyrequestStatus> {

  public SupplyrequestStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("draft".equals(codeString))
      return SupplyrequestStatus.DRAFT;
    if ("active".equals(codeString))
      return SupplyrequestStatus.ACTIVE;
    if ("suspended".equals(codeString))
      return SupplyrequestStatus.SUSPENDED;
    if ("cancelled".equals(codeString))
      return SupplyrequestStatus.CANCELLED;
    if ("completed".equals(codeString))
      return SupplyrequestStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return SupplyrequestStatus.ENTEREDINERROR;
    if ("unknown".equals(codeString))
      return SupplyrequestStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown SupplyrequestStatus code '"+codeString+"'");
  }

  public String toCode(SupplyrequestStatus code) {
    if (code == SupplyrequestStatus.DRAFT)
      return "draft";
    if (code == SupplyrequestStatus.ACTIVE)
      return "active";
    if (code == SupplyrequestStatus.SUSPENDED)
      return "suspended";
    if (code == SupplyrequestStatus.CANCELLED)
      return "cancelled";
    if (code == SupplyrequestStatus.COMPLETED)
      return "completed";
    if (code == SupplyrequestStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == SupplyrequestStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(SupplyrequestStatus code) {
      return code.getSystem();
      }

}

