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

public class MeasurePopulationEnumFactory implements EnumFactory<MeasurePopulation> {

  public MeasurePopulation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("initial-population".equals(codeString))
      return MeasurePopulation.INITIALPOPULATION;
    if ("numerator".equals(codeString))
      return MeasurePopulation.NUMERATOR;
    if ("numerator-exclusion".equals(codeString))
      return MeasurePopulation.NUMERATOREXCLUSION;
    if ("denominator".equals(codeString))
      return MeasurePopulation.DENOMINATOR;
    if ("denominator-exclusion".equals(codeString))
      return MeasurePopulation.DENOMINATOREXCLUSION;
    if ("denominator-exception".equals(codeString))
      return MeasurePopulation.DENOMINATOREXCEPTION;
    if ("measure-population".equals(codeString))
      return MeasurePopulation.MEASUREPOPULATION;
    if ("measure-population-exclusion".equals(codeString))
      return MeasurePopulation.MEASUREPOPULATIONEXCLUSION;
    if ("measure-observation".equals(codeString))
      return MeasurePopulation.MEASUREOBSERVATION;
    throw new IllegalArgumentException("Unknown MeasurePopulation code '"+codeString+"'");
  }

  public String toCode(MeasurePopulation code) {
    if (code == MeasurePopulation.INITIALPOPULATION)
      return "initial-population";
    if (code == MeasurePopulation.NUMERATOR)
      return "numerator";
    if (code == MeasurePopulation.NUMERATOREXCLUSION)
      return "numerator-exclusion";
    if (code == MeasurePopulation.DENOMINATOR)
      return "denominator";
    if (code == MeasurePopulation.DENOMINATOREXCLUSION)
      return "denominator-exclusion";
    if (code == MeasurePopulation.DENOMINATOREXCEPTION)
      return "denominator-exception";
    if (code == MeasurePopulation.MEASUREPOPULATION)
      return "measure-population";
    if (code == MeasurePopulation.MEASUREPOPULATIONEXCLUSION)
      return "measure-population-exclusion";
    if (code == MeasurePopulation.MEASUREOBSERVATION)
      return "measure-observation";
    return "?";
  }

    public String toSystem(MeasurePopulation code) {
      return code.getSystem();
      }

}

