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

public class CompartmentTypeEnumFactory implements EnumFactory<CompartmentType> {

  public CompartmentType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("Patient".equals(codeString))
      return CompartmentType.PATIENT;
    if ("Encounter".equals(codeString))
      return CompartmentType.ENCOUNTER;
    if ("RelatedPerson".equals(codeString))
      return CompartmentType.RELATEDPERSON;
    if ("Practitioner".equals(codeString))
      return CompartmentType.PRACTITIONER;
    if ("Device".equals(codeString))
      return CompartmentType.DEVICE;
    throw new IllegalArgumentException("Unknown CompartmentType code '"+codeString+"'");
  }

  public String toCode(CompartmentType code) {
    if (code == CompartmentType.PATIENT)
      return "Patient";
    if (code == CompartmentType.ENCOUNTER)
      return "Encounter";
    if (code == CompartmentType.RELATEDPERSON)
      return "RelatedPerson";
    if (code == CompartmentType.PRACTITIONER)
      return "Practitioner";
    if (code == CompartmentType.DEVICE)
      return "Device";
    return "?";
  }

    public String toSystem(CompartmentType code) {
      return code.getSystem();
      }

}

