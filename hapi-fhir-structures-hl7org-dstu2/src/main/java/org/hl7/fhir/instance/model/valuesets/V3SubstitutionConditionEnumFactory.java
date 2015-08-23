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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class V3SubstitutionConditionEnumFactory implements EnumFactory<V3SubstitutionCondition> {

  public V3SubstitutionCondition fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_Conditional".equals(codeString))
      return V3SubstitutionCondition._CONDITIONAL;
    if ("CONFIRM".equals(codeString))
      return V3SubstitutionCondition.CONFIRM;
    if ("NOTIFY".equals(codeString))
      return V3SubstitutionCondition.NOTIFY;
    if ("NOSUB".equals(codeString))
      return V3SubstitutionCondition.NOSUB;
    if ("UNCOND".equals(codeString))
      return V3SubstitutionCondition.UNCOND;
    throw new IllegalArgumentException("Unknown V3SubstitutionCondition code '"+codeString+"'");
  }

  public String toCode(V3SubstitutionCondition code) {
    if (code == V3SubstitutionCondition._CONDITIONAL)
      return "_Conditional";
    if (code == V3SubstitutionCondition.CONFIRM)
      return "CONFIRM";
    if (code == V3SubstitutionCondition.NOTIFY)
      return "NOTIFY";
    if (code == V3SubstitutionCondition.NOSUB)
      return "NOSUB";
    if (code == V3SubstitutionCondition.UNCOND)
      return "UNCOND";
    return "?";
  }


}

