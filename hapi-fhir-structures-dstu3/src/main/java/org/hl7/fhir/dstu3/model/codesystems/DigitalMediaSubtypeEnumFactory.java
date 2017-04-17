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

public class DigitalMediaSubtypeEnumFactory implements EnumFactory<DigitalMediaSubtype> {

  public DigitalMediaSubtype fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("diagram".equals(codeString))
      return DigitalMediaSubtype.DIAGRAM;
    if ("fax".equals(codeString))
      return DigitalMediaSubtype.FAX;
    if ("scan".equals(codeString))
      return DigitalMediaSubtype.SCAN;
    if ("retina".equals(codeString))
      return DigitalMediaSubtype.RETINA;
    if ("fingerprint".equals(codeString))
      return DigitalMediaSubtype.FINGERPRINT;
    if ("iris".equals(codeString))
      return DigitalMediaSubtype.IRIS;
    if ("palm".equals(codeString))
      return DigitalMediaSubtype.PALM;
    if ("face".equals(codeString))
      return DigitalMediaSubtype.FACE;
    throw new IllegalArgumentException("Unknown DigitalMediaSubtype code '"+codeString+"'");
  }

  public String toCode(DigitalMediaSubtype code) {
    if (code == DigitalMediaSubtype.DIAGRAM)
      return "diagram";
    if (code == DigitalMediaSubtype.FAX)
      return "fax";
    if (code == DigitalMediaSubtype.SCAN)
      return "scan";
    if (code == DigitalMediaSubtype.RETINA)
      return "retina";
    if (code == DigitalMediaSubtype.FINGERPRINT)
      return "fingerprint";
    if (code == DigitalMediaSubtype.IRIS)
      return "iris";
    if (code == DigitalMediaSubtype.PALM)
      return "palm";
    if (code == DigitalMediaSubtype.FACE)
      return "face";
    return "?";
  }

    public String toSystem(DigitalMediaSubtype code) {
      return code.getSystem();
      }

}

