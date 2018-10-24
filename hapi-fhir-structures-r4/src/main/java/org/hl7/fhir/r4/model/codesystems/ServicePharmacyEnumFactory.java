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

public class ServicePharmacyEnumFactory implements EnumFactory<ServicePharmacy> {

  public ServicePharmacy fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("smokecess".equals(codeString))
      return ServicePharmacy.SMOKECESS;
    if ("flushot".equals(codeString))
      return ServicePharmacy.FLUSHOT;
    if ("drugcost".equals(codeString))
      return ServicePharmacy.DRUGCOST;
    if ("markup".equals(codeString))
      return ServicePharmacy.MARKUP;
    if ("dispensefee".equals(codeString))
      return ServicePharmacy.DISPENSEFEE;
    if ("compoundfee".equals(codeString))
      return ServicePharmacy.COMPOUNDFEE;
    throw new IllegalArgumentException("Unknown ServicePharmacy code '"+codeString+"'");
  }

  public String toCode(ServicePharmacy code) {
    if (code == ServicePharmacy.SMOKECESS)
      return "smokecess";
    if (code == ServicePharmacy.FLUSHOT)
      return "flushot";
    if (code == ServicePharmacy.DRUGCOST)
      return "drugcost";
    if (code == ServicePharmacy.MARKUP)
      return "markup";
    if (code == ServicePharmacy.DISPENSEFEE)
      return "dispensefee";
    if (code == ServicePharmacy.COMPOUNDFEE)
      return "compoundfee";
    return "?";
  }

    public String toSystem(ServicePharmacy code) {
      return code.getSystem();
      }

}

