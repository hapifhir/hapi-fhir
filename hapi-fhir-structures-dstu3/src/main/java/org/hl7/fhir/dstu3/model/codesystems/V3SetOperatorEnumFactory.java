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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3SetOperatorEnumFactory implements EnumFactory<V3SetOperator> {

  public V3SetOperator fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ValueSetOperator".equals(codeString))
      return V3SetOperator._VALUESETOPERATOR;
    if ("E".equals(codeString))
      return V3SetOperator.E;
    if ("I".equals(codeString))
      return V3SetOperator.I;
    if ("A".equals(codeString))
      return V3SetOperator.A;
    if ("H".equals(codeString))
      return V3SetOperator.H;
    if ("P".equals(codeString))
      return V3SetOperator.P;
    throw new IllegalArgumentException("Unknown V3SetOperator code '"+codeString+"'");
  }

  public String toCode(V3SetOperator code) {
    if (code == V3SetOperator._VALUESETOPERATOR)
      return "_ValueSetOperator";
    if (code == V3SetOperator.E)
      return "E";
    if (code == V3SetOperator.I)
      return "I";
    if (code == V3SetOperator.A)
      return "A";
    if (code == V3SetOperator.H)
      return "H";
    if (code == V3SetOperator.P)
      return "P";
    return "?";
  }

    public String toSystem(V3SetOperator code) {
      return code.getSystem();
      }

}

