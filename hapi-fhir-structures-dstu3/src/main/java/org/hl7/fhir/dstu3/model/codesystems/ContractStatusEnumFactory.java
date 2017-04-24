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

public class ContractStatusEnumFactory implements EnumFactory<ContractStatus> {

  public ContractStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("amended".equals(codeString))
      return ContractStatus.AMENDED;
    if ("appended".equals(codeString))
      return ContractStatus.APPENDED;
    if ("cancelled".equals(codeString))
      return ContractStatus.CANCELLED;
    if ("disputed".equals(codeString))
      return ContractStatus.DISPUTED;
    if ("entered-in-error".equals(codeString))
      return ContractStatus.ENTEREDINERROR;
    if ("executable".equals(codeString))
      return ContractStatus.EXECUTABLE;
    if ("executed".equals(codeString))
      return ContractStatus.EXECUTED;
    if ("negotiable".equals(codeString))
      return ContractStatus.NEGOTIABLE;
    if ("offered".equals(codeString))
      return ContractStatus.OFFERED;
    if ("policy".equals(codeString))
      return ContractStatus.POLICY;
    if ("rejected".equals(codeString))
      return ContractStatus.REJECTED;
    if ("renewed".equals(codeString))
      return ContractStatus.RENEWED;
    if ("revoked".equals(codeString))
      return ContractStatus.REVOKED;
    if ("resolved".equals(codeString))
      return ContractStatus.RESOLVED;
    if ("terminated".equals(codeString))
      return ContractStatus.TERMINATED;
    throw new IllegalArgumentException("Unknown ContractStatus code '"+codeString+"'");
  }

  public String toCode(ContractStatus code) {
    if (code == ContractStatus.AMENDED)
      return "amended";
    if (code == ContractStatus.APPENDED)
      return "appended";
    if (code == ContractStatus.CANCELLED)
      return "cancelled";
    if (code == ContractStatus.DISPUTED)
      return "disputed";
    if (code == ContractStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == ContractStatus.EXECUTABLE)
      return "executable";
    if (code == ContractStatus.EXECUTED)
      return "executed";
    if (code == ContractStatus.NEGOTIABLE)
      return "negotiable";
    if (code == ContractStatus.OFFERED)
      return "offered";
    if (code == ContractStatus.POLICY)
      return "policy";
    if (code == ContractStatus.REJECTED)
      return "rejected";
    if (code == ContractStatus.RENEWED)
      return "renewed";
    if (code == ContractStatus.REVOKED)
      return "revoked";
    if (code == ContractStatus.RESOLVED)
      return "resolved";
    if (code == ContractStatus.TERMINATED)
      return "terminated";
    return "?";
  }

    public String toSystem(ContractStatus code) {
      return code.getSystem();
      }

}

