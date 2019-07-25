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


import org.hl7.fhir.exceptions.FHIRException;

public enum DeviceStatus {

  /**
   * The Device is available for use.  Note: This means for *implanted devices*  the device is implanted in the patient.
   */
  ACTIVE,
  /**
   * The Device is no longer available for use (e.g. lost, expired, damaged).  Note: This means for *implanted devices*  the device has been removed from the patient.
   */
  INACTIVE,
  /**
   * The Device was entered in error and voided.
   */
  ENTEREDINERROR,
  /**
   * The status of the device has not been determined.
   */
  UNKNOWN,
  /**
   * added to help the parsers
   */
  NULL;

  public String getDefinition() {
    switch (this) {
      case ACTIVE:
        return "The Device is available for use.  Note: This means for *implanted devices*  the device is implanted in the patient.";
      case INACTIVE:
        return "The Device is no longer available for use (e.g. lost, expired, damaged).  Note: This means for *implanted devices*  the device has been removed from the patient.";
      case ENTEREDINERROR:
        return "The Device was entered in error and voided.";
      case UNKNOWN:
        return "The status of the device has not been determined.";
      default:
        return "?";
    }
  }

  public String getDisplay() {
    switch (this) {
      case ACTIVE:
        return "Active";
      case INACTIVE:
        return "Inactive";
      case ENTEREDINERROR:
        return "Entered in Error";
      case UNKNOWN:
        return "Unknown";
      default:
        return "?";
    }
  }

  public String getSystem() {
    return "http://hl7.org/fhir/device-status";
  }

  public String toCode() {
    switch (this) {
      case ACTIVE:
        return "active";
      case INACTIVE:
        return "inactive";
      case ENTEREDINERROR:
        return "entered-in-error";
      case UNKNOWN:
        return "unknown";
      default:
        return "?";
    }
  }

  public static DeviceStatus fromCode(String codeString) throws FHIRException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("active".equals(codeString))
      return ACTIVE;
    if ("inactive".equals(codeString))
      return INACTIVE;
    if ("entered-in-error".equals(codeString))
      return ENTEREDINERROR;
    if ("unknown".equals(codeString))
      return UNKNOWN;
    throw new FHIRException("Unknown DeviceStatus code '" + codeString + "'");
  }


}

