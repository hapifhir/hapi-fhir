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

public class V3ActRelationshipSubsetEnumFactory implements EnumFactory<V3ActRelationshipSubset> {

  public V3ActRelationshipSubset fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ParticipationSubset".equals(codeString))
      return V3ActRelationshipSubset._PARTICIPATIONSUBSET;
    if ("FUTURE".equals(codeString))
      return V3ActRelationshipSubset.FUTURE;
    if ("FUTSUM".equals(codeString))
      return V3ActRelationshipSubset.FUTSUM;
    if ("LAST".equals(codeString))
      return V3ActRelationshipSubset.LAST;
    if ("NEXT".equals(codeString))
      return V3ActRelationshipSubset.NEXT;
    if ("PAST".equals(codeString))
      return V3ActRelationshipSubset.PAST;
    if ("FIRST".equals(codeString))
      return V3ActRelationshipSubset.FIRST;
    if ("PREVSUM".equals(codeString))
      return V3ActRelationshipSubset.PREVSUM;
    if ("RECENT".equals(codeString))
      return V3ActRelationshipSubset.RECENT;
    if ("SUM".equals(codeString))
      return V3ActRelationshipSubset.SUM;
    if ("ActRelationshipExpectedSubset".equals(codeString))
      return V3ActRelationshipSubset.ACTRELATIONSHIPEXPECTEDSUBSET;
    if ("ActRelationshipPastSubset".equals(codeString))
      return V3ActRelationshipSubset.ACTRELATIONSHIPPASTSUBSET;
    if ("MAX".equals(codeString))
      return V3ActRelationshipSubset.MAX;
    if ("MIN".equals(codeString))
      return V3ActRelationshipSubset.MIN;
    throw new IllegalArgumentException("Unknown V3ActRelationshipSubset code '"+codeString+"'");
  }

  public String toCode(V3ActRelationshipSubset code) {
    if (code == V3ActRelationshipSubset._PARTICIPATIONSUBSET)
      return "_ParticipationSubset";
    if (code == V3ActRelationshipSubset.FUTURE)
      return "FUTURE";
    if (code == V3ActRelationshipSubset.FUTSUM)
      return "FUTSUM";
    if (code == V3ActRelationshipSubset.LAST)
      return "LAST";
    if (code == V3ActRelationshipSubset.NEXT)
      return "NEXT";
    if (code == V3ActRelationshipSubset.PAST)
      return "PAST";
    if (code == V3ActRelationshipSubset.FIRST)
      return "FIRST";
    if (code == V3ActRelationshipSubset.PREVSUM)
      return "PREVSUM";
    if (code == V3ActRelationshipSubset.RECENT)
      return "RECENT";
    if (code == V3ActRelationshipSubset.SUM)
      return "SUM";
    if (code == V3ActRelationshipSubset.ACTRELATIONSHIPEXPECTEDSUBSET)
      return "ActRelationshipExpectedSubset";
    if (code == V3ActRelationshipSubset.ACTRELATIONSHIPPASTSUBSET)
      return "ActRelationshipPastSubset";
    if (code == V3ActRelationshipSubset.MAX)
      return "MAX";
    if (code == V3ActRelationshipSubset.MIN)
      return "MIN";
    return "?";
  }


}

