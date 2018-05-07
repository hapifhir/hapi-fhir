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

public class ImmunizationRecommendationDateCriterionEnumFactory implements EnumFactory<ImmunizationRecommendationDateCriterion> {

  public ImmunizationRecommendationDateCriterion fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("due".equals(codeString))
      return ImmunizationRecommendationDateCriterion.DUE;
    if ("recommended".equals(codeString))
      return ImmunizationRecommendationDateCriterion.RECOMMENDED;
    if ("earliest".equals(codeString))
      return ImmunizationRecommendationDateCriterion.EARLIEST;
    if ("overdue".equals(codeString))
      return ImmunizationRecommendationDateCriterion.OVERDUE;
    if ("latest".equals(codeString))
      return ImmunizationRecommendationDateCriterion.LATEST;
    throw new IllegalArgumentException("Unknown ImmunizationRecommendationDateCriterion code '"+codeString+"'");
  }

  public String toCode(ImmunizationRecommendationDateCriterion code) {
    if (code == ImmunizationRecommendationDateCriterion.DUE)
      return "due";
    if (code == ImmunizationRecommendationDateCriterion.RECOMMENDED)
      return "recommended";
    if (code == ImmunizationRecommendationDateCriterion.EARLIEST)
      return "earliest";
    if (code == ImmunizationRecommendationDateCriterion.OVERDUE)
      return "overdue";
    if (code == ImmunizationRecommendationDateCriterion.LATEST)
      return "latest";
    return "?";
  }

    public String toSystem(ImmunizationRecommendationDateCriterion code) {
      return code.getSystem();
      }

}

