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

// Generated on Sat, Mar 3, 2018 18:00-0500 for FHIR v3.2.0


import org.hl7.fhir.r4.model.EnumFactory;

public class CarePlanActivityStatusEnumFactory implements EnumFactory<CarePlanActivityStatus> {

  public CarePlanActivityStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("not-started".equals(codeString))
      return CarePlanActivityStatus.NOTSTARTED;
    if ("scheduled".equals(codeString))
      return CarePlanActivityStatus.SCHEDULED;
    if ("in-progress".equals(codeString))
      return CarePlanActivityStatus.INPROGRESS;
    if ("on-hold".equals(codeString))
      return CarePlanActivityStatus.ONHOLD;
    if ("completed".equals(codeString))
      return CarePlanActivityStatus.COMPLETED;
    if ("cancelled".equals(codeString))
      return CarePlanActivityStatus.CANCELLED;
    if ("stopped".equals(codeString))
      return CarePlanActivityStatus.STOPPED;
    if ("unknown".equals(codeString))
      return CarePlanActivityStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown CarePlanActivityStatus code '"+codeString+"'");
  }

  public String toCode(CarePlanActivityStatus code) {
    if (code == CarePlanActivityStatus.NOTSTARTED)
      return "not-started";
    if (code == CarePlanActivityStatus.SCHEDULED)
      return "scheduled";
    if (code == CarePlanActivityStatus.INPROGRESS)
      return "in-progress";
    if (code == CarePlanActivityStatus.ONHOLD)
      return "on-hold";
    if (code == CarePlanActivityStatus.COMPLETED)
      return "completed";
    if (code == CarePlanActivityStatus.CANCELLED)
      return "cancelled";
    if (code == CarePlanActivityStatus.STOPPED)
      return "stopped";
    if (code == CarePlanActivityStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(CarePlanActivityStatus code) {
      return code.getSystem();
      }

}

