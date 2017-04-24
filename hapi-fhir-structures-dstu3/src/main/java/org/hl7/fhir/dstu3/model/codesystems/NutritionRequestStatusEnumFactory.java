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

public class NutritionRequestStatusEnumFactory implements EnumFactory<NutritionRequestStatus> {

  public NutritionRequestStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("proposed".equals(codeString))
      return NutritionRequestStatus.PROPOSED;
    if ("draft".equals(codeString))
      return NutritionRequestStatus.DRAFT;
    if ("planned".equals(codeString))
      return NutritionRequestStatus.PLANNED;
    if ("requested".equals(codeString))
      return NutritionRequestStatus.REQUESTED;
    if ("active".equals(codeString))
      return NutritionRequestStatus.ACTIVE;
    if ("on-hold".equals(codeString))
      return NutritionRequestStatus.ONHOLD;
    if ("completed".equals(codeString))
      return NutritionRequestStatus.COMPLETED;
    if ("cancelled".equals(codeString))
      return NutritionRequestStatus.CANCELLED;
    if ("entered-in-error".equals(codeString))
      return NutritionRequestStatus.ENTEREDINERROR;
    throw new IllegalArgumentException("Unknown NutritionRequestStatus code '"+codeString+"'");
  }

  public String toCode(NutritionRequestStatus code) {
    if (code == NutritionRequestStatus.PROPOSED)
      return "proposed";
    if (code == NutritionRequestStatus.DRAFT)
      return "draft";
    if (code == NutritionRequestStatus.PLANNED)
      return "planned";
    if (code == NutritionRequestStatus.REQUESTED)
      return "requested";
    if (code == NutritionRequestStatus.ACTIVE)
      return "active";
    if (code == NutritionRequestStatus.ONHOLD)
      return "on-hold";
    if (code == NutritionRequestStatus.COMPLETED)
      return "completed";
    if (code == NutritionRequestStatus.CANCELLED)
      return "cancelled";
    if (code == NutritionRequestStatus.ENTEREDINERROR)
      return "entered-in-error";
    return "?";
  }

    public String toSystem(NutritionRequestStatus code) {
      return code.getSystem();
      }

}

