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

// Generated on Thu, Feb 9, 2017 08:03-0500 for FHIR v1.9.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class ProcedureStatusEnumFactory implements EnumFactory<ProcedureStatus> {

  public ProcedureStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("preparation".equals(codeString))
      return ProcedureStatus.PREPARATION;
    if ("in-progress".equals(codeString))
      return ProcedureStatus.INPROGRESS;
    if ("suspended".equals(codeString))
      return ProcedureStatus.SUSPENDED;
    if ("aborted".equals(codeString))
      return ProcedureStatus.ABORTED;
    if ("completed".equals(codeString))
      return ProcedureStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return ProcedureStatus.ENTEREDINERROR;
    if ("unknown".equals(codeString))
      return ProcedureStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown ProcedureStatus code '"+codeString+"'");
  }

  public String toCode(ProcedureStatus code) {
    if (code == ProcedureStatus.PREPARATION)
      return "preparation";
    if (code == ProcedureStatus.INPROGRESS)
      return "in-progress";
    if (code == ProcedureStatus.SUSPENDED)
      return "suspended";
    if (code == ProcedureStatus.ABORTED)
      return "aborted";
    if (code == ProcedureStatus.COMPLETED)
      return "completed";
    if (code == ProcedureStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == ProcedureStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(ProcedureStatus code) {
      return code.getSystem();
      }

}

