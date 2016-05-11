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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


import org.hl7.fhir.instance.model.EnumFactory;

public class ProcedureProgressStatusCodesEnumFactory implements EnumFactory<ProcedureProgressStatusCodes> {

  public ProcedureProgressStatusCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("a".equals(codeString))
      return ProcedureProgressStatusCodes.A;
    if ("b".equals(codeString))
      return ProcedureProgressStatusCodes.B;
    if ("c".equals(codeString))
      return ProcedureProgressStatusCodes.C;
    if ("d".equals(codeString))
      return ProcedureProgressStatusCodes.D;
    if ("e".equals(codeString))
      return ProcedureProgressStatusCodes.E;
    if ("f".equals(codeString))
      return ProcedureProgressStatusCodes.F;
    throw new IllegalArgumentException("Unknown ProcedureProgressStatusCodes code '"+codeString+"'");
  }

  public String toCode(ProcedureProgressStatusCodes code) {
    if (code == ProcedureProgressStatusCodes.A)
      return "a";
    if (code == ProcedureProgressStatusCodes.B)
      return "b";
    if (code == ProcedureProgressStatusCodes.C)
      return "c";
    if (code == ProcedureProgressStatusCodes.D)
      return "d";
    if (code == ProcedureProgressStatusCodes.E)
      return "e";
    if (code == ProcedureProgressStatusCodes.F)
      return "f";
    return "?";
  }


}

