package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class V3ParticipationModeEnumFactory implements EnumFactory<V3ParticipationMode> {

  public V3ParticipationMode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ELECTRONIC".equals(codeString))
      return V3ParticipationMode.ELECTRONIC;
    if ("PHYSICAL".equals(codeString))
      return V3ParticipationMode.PHYSICAL;
    if ("REMOTE".equals(codeString))
      return V3ParticipationMode.REMOTE;
    if ("VERBAL".equals(codeString))
      return V3ParticipationMode.VERBAL;
    if ("DICTATE".equals(codeString))
      return V3ParticipationMode.DICTATE;
    if ("FACE".equals(codeString))
      return V3ParticipationMode.FACE;
    if ("PHONE".equals(codeString))
      return V3ParticipationMode.PHONE;
    if ("VIDEOCONF".equals(codeString))
      return V3ParticipationMode.VIDEOCONF;
    if ("WRITTEN".equals(codeString))
      return V3ParticipationMode.WRITTEN;
    if ("FAXWRIT".equals(codeString))
      return V3ParticipationMode.FAXWRIT;
    if ("HANDWRIT".equals(codeString))
      return V3ParticipationMode.HANDWRIT;
    if ("MAILWRIT".equals(codeString))
      return V3ParticipationMode.MAILWRIT;
    if ("ONLINEWRIT".equals(codeString))
      return V3ParticipationMode.ONLINEWRIT;
    if ("EMAILWRIT".equals(codeString))
      return V3ParticipationMode.EMAILWRIT;
    if ("TYPEWRIT".equals(codeString))
      return V3ParticipationMode.TYPEWRIT;
    throw new IllegalArgumentException("Unknown V3ParticipationMode code '"+codeString+"'");
  }

  public String toCode(V3ParticipationMode code) {
    if (code == V3ParticipationMode.ELECTRONIC)
      return "ELECTRONIC";
    if (code == V3ParticipationMode.PHYSICAL)
      return "PHYSICAL";
    if (code == V3ParticipationMode.REMOTE)
      return "REMOTE";
    if (code == V3ParticipationMode.VERBAL)
      return "VERBAL";
    if (code == V3ParticipationMode.DICTATE)
      return "DICTATE";
    if (code == V3ParticipationMode.FACE)
      return "FACE";
    if (code == V3ParticipationMode.PHONE)
      return "PHONE";
    if (code == V3ParticipationMode.VIDEOCONF)
      return "VIDEOCONF";
    if (code == V3ParticipationMode.WRITTEN)
      return "WRITTEN";
    if (code == V3ParticipationMode.FAXWRIT)
      return "FAXWRIT";
    if (code == V3ParticipationMode.HANDWRIT)
      return "HANDWRIT";
    if (code == V3ParticipationMode.MAILWRIT)
      return "MAILWRIT";
    if (code == V3ParticipationMode.ONLINEWRIT)
      return "ONLINEWRIT";
    if (code == V3ParticipationMode.EMAILWRIT)
      return "EMAILWRIT";
    if (code == V3ParticipationMode.TYPEWRIT)
      return "TYPEWRIT";
    return "?";
  }


}

