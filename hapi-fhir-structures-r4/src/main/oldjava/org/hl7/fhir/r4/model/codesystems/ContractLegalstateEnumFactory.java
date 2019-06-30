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

public class ContractLegalstateEnumFactory implements EnumFactory<ContractLegalstate> {

  public ContractLegalstate fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("amended".equals(codeString))
      return ContractLegalstate.AMENDED;
    if ("appended".equals(codeString))
      return ContractLegalstate.APPENDED;
    if ("cancelled".equals(codeString))
      return ContractLegalstate.CANCELLED;
    if ("disputed".equals(codeString))
      return ContractLegalstate.DISPUTED;
    if ("entered-in-error".equals(codeString))
      return ContractLegalstate.ENTEREDINERROR;
    if ("executable".equals(codeString))
      return ContractLegalstate.EXECUTABLE;
    if ("executed".equals(codeString))
      return ContractLegalstate.EXECUTED;
    if ("negotiable".equals(codeString))
      return ContractLegalstate.NEGOTIABLE;
    if ("offered".equals(codeString))
      return ContractLegalstate.OFFERED;
    if ("policy".equals(codeString))
      return ContractLegalstate.POLICY;
    if ("rejected".equals(codeString))
      return ContractLegalstate.REJECTED;
    if ("renewed".equals(codeString))
      return ContractLegalstate.RENEWED;
    if ("revoked".equals(codeString))
      return ContractLegalstate.REVOKED;
    if ("resolved".equals(codeString))
      return ContractLegalstate.RESOLVED;
    if ("terminated".equals(codeString))
      return ContractLegalstate.TERMINATED;
    throw new IllegalArgumentException("Unknown ContractLegalstate code '"+codeString+"'");
  }

  public String toCode(ContractLegalstate code) {
    if (code == ContractLegalstate.AMENDED)
      return "amended";
    if (code == ContractLegalstate.APPENDED)
      return "appended";
    if (code == ContractLegalstate.CANCELLED)
      return "cancelled";
    if (code == ContractLegalstate.DISPUTED)
      return "disputed";
    if (code == ContractLegalstate.ENTEREDINERROR)
      return "entered-in-error";
    if (code == ContractLegalstate.EXECUTABLE)
      return "executable";
    if (code == ContractLegalstate.EXECUTED)
      return "executed";
    if (code == ContractLegalstate.NEGOTIABLE)
      return "negotiable";
    if (code == ContractLegalstate.OFFERED)
      return "offered";
    if (code == ContractLegalstate.POLICY)
      return "policy";
    if (code == ContractLegalstate.REJECTED)
      return "rejected";
    if (code == ContractLegalstate.RENEWED)
      return "renewed";
    if (code == ContractLegalstate.REVOKED)
      return "revoked";
    if (code == ContractLegalstate.RESOLVED)
      return "resolved";
    if (code == ContractLegalstate.TERMINATED)
      return "terminated";
    return "?";
  }

    public String toSystem(ContractLegalstate code) {
      return code.getSystem();
      }

}

