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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class ContractPublicationstatusEnumFactory implements EnumFactory<ContractPublicationstatus> {

  public ContractPublicationstatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("amended".equals(codeString))
      return ContractPublicationstatus.AMENDED;
    if ("appended".equals(codeString))
      return ContractPublicationstatus.APPENDED;
    if ("cancelled".equals(codeString))
      return ContractPublicationstatus.CANCELLED;
    if ("disputed".equals(codeString))
      return ContractPublicationstatus.DISPUTED;
    if ("entered-in-error".equals(codeString))
      return ContractPublicationstatus.ENTEREDINERROR;
    if ("executable".equals(codeString))
      return ContractPublicationstatus.EXECUTABLE;
    if ("executed".equals(codeString))
      return ContractPublicationstatus.EXECUTED;
    if ("negotiable".equals(codeString))
      return ContractPublicationstatus.NEGOTIABLE;
    if ("offered".equals(codeString))
      return ContractPublicationstatus.OFFERED;
    if ("policy".equals(codeString))
      return ContractPublicationstatus.POLICY;
    if ("rejected".equals(codeString))
      return ContractPublicationstatus.REJECTED;
    if ("renewed".equals(codeString))
      return ContractPublicationstatus.RENEWED;
    if ("revoked".equals(codeString))
      return ContractPublicationstatus.REVOKED;
    if ("resolved".equals(codeString))
      return ContractPublicationstatus.RESOLVED;
    if ("terminated".equals(codeString))
      return ContractPublicationstatus.TERMINATED;
    throw new IllegalArgumentException("Unknown ContractPublicationstatus code '"+codeString+"'");
  }

  public String toCode(ContractPublicationstatus code) {
    if (code == ContractPublicationstatus.AMENDED)
      return "amended";
    if (code == ContractPublicationstatus.APPENDED)
      return "appended";
    if (code == ContractPublicationstatus.CANCELLED)
      return "cancelled";
    if (code == ContractPublicationstatus.DISPUTED)
      return "disputed";
    if (code == ContractPublicationstatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == ContractPublicationstatus.EXECUTABLE)
      return "executable";
    if (code == ContractPublicationstatus.EXECUTED)
      return "executed";
    if (code == ContractPublicationstatus.NEGOTIABLE)
      return "negotiable";
    if (code == ContractPublicationstatus.OFFERED)
      return "offered";
    if (code == ContractPublicationstatus.POLICY)
      return "policy";
    if (code == ContractPublicationstatus.REJECTED)
      return "rejected";
    if (code == ContractPublicationstatus.RENEWED)
      return "renewed";
    if (code == ContractPublicationstatus.REVOKED)
      return "revoked";
    if (code == ContractPublicationstatus.RESOLVED)
      return "resolved";
    if (code == ContractPublicationstatus.TERMINATED)
      return "terminated";
    return "?";
  }

    public String toSystem(ContractPublicationstatus code) {
      return code.getSystem();
      }

}

