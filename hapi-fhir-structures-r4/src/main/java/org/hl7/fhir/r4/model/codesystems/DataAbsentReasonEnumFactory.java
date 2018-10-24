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

public class DataAbsentReasonEnumFactory implements EnumFactory<DataAbsentReason> {

  public DataAbsentReason fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("unknown".equals(codeString))
      return DataAbsentReason.UNKNOWN;
    if ("asked-unknown".equals(codeString))
      return DataAbsentReason.ASKEDUNKNOWN;
    if ("temp-unknown".equals(codeString))
      return DataAbsentReason.TEMPUNKNOWN;
    if ("not-asked".equals(codeString))
      return DataAbsentReason.NOTASKED;
    if ("asked-declined".equals(codeString))
      return DataAbsentReason.ASKEDDECLINED;
    if ("masked".equals(codeString))
      return DataAbsentReason.MASKED;
    if ("not-applicable".equals(codeString))
      return DataAbsentReason.NOTAPPLICABLE;
    if ("unsupported".equals(codeString))
      return DataAbsentReason.UNSUPPORTED;
    if ("as-text".equals(codeString))
      return DataAbsentReason.ASTEXT;
    if ("error".equals(codeString))
      return DataAbsentReason.ERROR;
    if ("not-a-number".equals(codeString))
      return DataAbsentReason.NOTANUMBER;
    if ("negative-infinity".equals(codeString))
      return DataAbsentReason.NEGATIVEINFINITY;
    if ("positive-infinity".equals(codeString))
      return DataAbsentReason.POSITIVEINFINITY;
    if ("not-performed".equals(codeString))
      return DataAbsentReason.NOTPERFORMED;
    if ("not-permitted".equals(codeString))
      return DataAbsentReason.NOTPERMITTED;
    throw new IllegalArgumentException("Unknown DataAbsentReason code '"+codeString+"'");
  }

  public String toCode(DataAbsentReason code) {
    if (code == DataAbsentReason.UNKNOWN)
      return "unknown";
    if (code == DataAbsentReason.ASKEDUNKNOWN)
      return "asked-unknown";
    if (code == DataAbsentReason.TEMPUNKNOWN)
      return "temp-unknown";
    if (code == DataAbsentReason.NOTASKED)
      return "not-asked";
    if (code == DataAbsentReason.ASKEDDECLINED)
      return "asked-declined";
    if (code == DataAbsentReason.MASKED)
      return "masked";
    if (code == DataAbsentReason.NOTAPPLICABLE)
      return "not-applicable";
    if (code == DataAbsentReason.UNSUPPORTED)
      return "unsupported";
    if (code == DataAbsentReason.ASTEXT)
      return "as-text";
    if (code == DataAbsentReason.ERROR)
      return "error";
    if (code == DataAbsentReason.NOTANUMBER)
      return "not-a-number";
    if (code == DataAbsentReason.NEGATIVEINFINITY)
      return "negative-infinity";
    if (code == DataAbsentReason.POSITIVEINFINITY)
      return "positive-infinity";
    if (code == DataAbsentReason.NOTPERFORMED)
      return "not-performed";
    if (code == DataAbsentReason.NOTPERMITTED)
      return "not-permitted";
    return "?";
  }

    public String toSystem(DataAbsentReason code) {
      return code.getSystem();
      }

}

