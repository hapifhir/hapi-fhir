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

public class RulesetEnumFactory implements EnumFactory<Ruleset> {

  public Ruleset fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("x12-4010".equals(codeString))
      return Ruleset.X124010;
    if ("x12-5010".equals(codeString))
      return Ruleset.X125010;
    if ("x12-7010".equals(codeString))
      return Ruleset.X127010;
    if ("cdanet-v2".equals(codeString))
      return Ruleset.CDANETV2;
    if ("cdanet-v4".equals(codeString))
      return Ruleset.CDANETV4;
    if ("cpha-3".equals(codeString))
      return Ruleset.CPHA3;
    throw new IllegalArgumentException("Unknown Ruleset code '"+codeString+"'");
  }

  public String toCode(Ruleset code) {
    if (code == Ruleset.X124010)
      return "x12-4010";
    if (code == Ruleset.X125010)
      return "x12-5010";
    if (code == Ruleset.X127010)
      return "x12-7010";
    if (code == Ruleset.CDANETV2)
      return "cdanet-v2";
    if (code == Ruleset.CDANETV4)
      return "cdanet-v4";
    if (code == Ruleset.CPHA3)
      return "cpha-3";
    return "?";
  }


}

