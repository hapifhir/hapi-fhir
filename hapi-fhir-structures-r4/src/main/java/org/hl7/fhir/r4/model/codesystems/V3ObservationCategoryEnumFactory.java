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

public class V3ObservationCategoryEnumFactory implements EnumFactory<V3ObservationCategory> {

  public V3ObservationCategory fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("exam".equals(codeString))
      return V3ObservationCategory.EXAM;
    if ("imaging".equals(codeString))
      return V3ObservationCategory.IMAGING;
    if ("laboratory".equals(codeString))
      return V3ObservationCategory.LABORATORY;
    if ("procedure".equals(codeString))
      return V3ObservationCategory.PROCEDURE;
    if ("social-history".equals(codeString))
      return V3ObservationCategory.SOCIALHISTORY;
    if ("survey".equals(codeString))
      return V3ObservationCategory.SURVEY;
    if ("therapy".equals(codeString))
      return V3ObservationCategory.THERAPY;
    if ("vital-signs".equals(codeString))
      return V3ObservationCategory.VITALSIGNS;
    throw new IllegalArgumentException("Unknown V3ObservationCategory code '"+codeString+"'");
  }

  public String toCode(V3ObservationCategory code) {
    if (code == V3ObservationCategory.EXAM)
      return "exam";
    if (code == V3ObservationCategory.IMAGING)
      return "imaging";
    if (code == V3ObservationCategory.LABORATORY)
      return "laboratory";
    if (code == V3ObservationCategory.PROCEDURE)
      return "procedure";
    if (code == V3ObservationCategory.SOCIALHISTORY)
      return "social-history";
    if (code == V3ObservationCategory.SURVEY)
      return "survey";
    if (code == V3ObservationCategory.THERAPY)
      return "therapy";
    if (code == V3ObservationCategory.VITALSIGNS)
      return "vital-signs";
    return "?";
  }

    public String toSystem(V3ObservationCategory code) {
      return code.getSystem();
      }

}

