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

public class AppointmentstatusEnumFactory implements EnumFactory<Appointmentstatus> {

  public Appointmentstatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("proposed".equals(codeString))
      return Appointmentstatus.PROPOSED;
    if ("pending".equals(codeString))
      return Appointmentstatus.PENDING;
    if ("booked".equals(codeString))
      return Appointmentstatus.BOOKED;
    if ("arrived".equals(codeString))
      return Appointmentstatus.ARRIVED;
    if ("fulfilled".equals(codeString))
      return Appointmentstatus.FULFILLED;
    if ("cancelled".equals(codeString))
      return Appointmentstatus.CANCELLED;
    if ("noshow".equals(codeString))
      return Appointmentstatus.NOSHOW;
    if ("entered-in-error".equals(codeString))
      return Appointmentstatus.ENTEREDINERROR;
    throw new IllegalArgumentException("Unknown Appointmentstatus code '"+codeString+"'");
  }

  public String toCode(Appointmentstatus code) {
    if (code == Appointmentstatus.PROPOSED)
      return "proposed";
    if (code == Appointmentstatus.PENDING)
      return "pending";
    if (code == Appointmentstatus.BOOKED)
      return "booked";
    if (code == Appointmentstatus.ARRIVED)
      return "arrived";
    if (code == Appointmentstatus.FULFILLED)
      return "fulfilled";
    if (code == Appointmentstatus.CANCELLED)
      return "cancelled";
    if (code == Appointmentstatus.NOSHOW)
      return "noshow";
    if (code == Appointmentstatus.ENTEREDINERROR)
      return "entered-in-error";
    return "?";
  }

    public String toSystem(Appointmentstatus code) {
      return code.getSystem();
      }

}

