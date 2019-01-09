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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class MedicationrequestIntentEnumFactory implements EnumFactory<MedicationrequestIntent> {

  public MedicationrequestIntent fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("proposal".equals(codeString))
      return MedicationrequestIntent.PROPOSAL;
    if ("plan".equals(codeString))
      return MedicationrequestIntent.PLAN;
    if ("order".equals(codeString))
      return MedicationrequestIntent.ORDER;
    if ("original-order".equals(codeString))
      return MedicationrequestIntent.ORIGINALORDER;
    if ("reflex-order".equals(codeString))
      return MedicationrequestIntent.REFLEXORDER;
    if ("filler-order".equals(codeString))
      return MedicationrequestIntent.FILLERORDER;
    if ("instance-order".equals(codeString))
      return MedicationrequestIntent.INSTANCEORDER;
    if ("option".equals(codeString))
      return MedicationrequestIntent.OPTION;
    throw new IllegalArgumentException("Unknown MedicationrequestIntent code '"+codeString+"'");
  }

  public String toCode(MedicationrequestIntent code) {
    if (code == MedicationrequestIntent.PROPOSAL)
      return "proposal";
    if (code == MedicationrequestIntent.PLAN)
      return "plan";
    if (code == MedicationrequestIntent.ORDER)
      return "order";
    if (code == MedicationrequestIntent.ORIGINALORDER)
      return "original-order";
    if (code == MedicationrequestIntent.REFLEXORDER)
      return "reflex-order";
    if (code == MedicationrequestIntent.FILLERORDER)
      return "filler-order";
    if (code == MedicationrequestIntent.INSTANCEORDER)
      return "instance-order";
    if (code == MedicationrequestIntent.OPTION)
      return "option";
    return "?";
  }

    public String toSystem(MedicationrequestIntent code) {
      return code.getSystem();
      }

}

