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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.r4.model.EnumFactory;

public class CoverageClassEnumFactory implements EnumFactory<CoverageClass> {

  public CoverageClass fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("group".equals(codeString))
      return CoverageClass.GROUP;
    if ("subgroup".equals(codeString))
      return CoverageClass.SUBGROUP;
    if ("plan".equals(codeString))
      return CoverageClass.PLAN;
    if ("subplan".equals(codeString))
      return CoverageClass.SUBPLAN;
    if ("class".equals(codeString))
      return CoverageClass.CLASS;
    if ("subclass".equals(codeString))
      return CoverageClass.SUBCLASS;
    if ("sequence".equals(codeString))
      return CoverageClass.SEQUENCE;
    if ("rxbin".equals(codeString))
      return CoverageClass.RXBIN;
    if ("rxpcn".equals(codeString))
      return CoverageClass.RXPCN;
    if ("rxid".equals(codeString))
      return CoverageClass.RXID;
    if ("rxgroup".equals(codeString))
      return CoverageClass.RXGROUP;
    throw new IllegalArgumentException("Unknown CoverageClass code '"+codeString+"'");
  }

  public String toCode(CoverageClass code) {
    if (code == CoverageClass.GROUP)
      return "group";
    if (code == CoverageClass.SUBGROUP)
      return "subgroup";
    if (code == CoverageClass.PLAN)
      return "plan";
    if (code == CoverageClass.SUBPLAN)
      return "subplan";
    if (code == CoverageClass.CLASS)
      return "class";
    if (code == CoverageClass.SUBCLASS)
      return "subclass";
    if (code == CoverageClass.SEQUENCE)
      return "sequence";
    if (code == CoverageClass.RXBIN)
      return "rxbin";
    if (code == CoverageClass.RXPCN)
      return "rxpcn";
    if (code == CoverageClass.RXID)
      return "rxid";
    if (code == CoverageClass.RXGROUP)
      return "rxgroup";
    return "?";
  }

    public String toSystem(CoverageClass code) {
      return code.getSystem();
      }

}

