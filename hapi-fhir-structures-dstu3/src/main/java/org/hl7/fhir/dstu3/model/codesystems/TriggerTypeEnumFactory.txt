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

public class TriggerTypeEnumFactory implements EnumFactory<TriggerType> {

  public TriggerType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("named-event".equals(codeString))
      return TriggerType.NAMEDEVENT;
    if ("periodic".equals(codeString))
      return TriggerType.PERIODIC;
    if ("data-added".equals(codeString))
      return TriggerType.DATAADDED;
    if ("data-modified".equals(codeString))
      return TriggerType.DATAMODIFIED;
    if ("data-removed".equals(codeString))
      return TriggerType.DATAREMOVED;
    if ("data-accessed".equals(codeString))
      return TriggerType.DATAACCESSED;
    if ("data-access-ended".equals(codeString))
      return TriggerType.DATAACCESSENDED;
    throw new IllegalArgumentException("Unknown TriggerType code '"+codeString+"'");
  }

  public String toCode(TriggerType code) {
    if (code == TriggerType.NAMEDEVENT)
      return "named-event";
    if (code == TriggerType.PERIODIC)
      return "periodic";
    if (code == TriggerType.DATAADDED)
      return "data-added";
    if (code == TriggerType.DATAMODIFIED)
      return "data-modified";
    if (code == TriggerType.DATAREMOVED)
      return "data-removed";
    if (code == TriggerType.DATAACCESSED)
      return "data-accessed";
    if (code == TriggerType.DATAACCESSENDED)
      return "data-access-ended";
    return "?";
  }

    public String toSystem(TriggerType code) {
      return code.getSystem();
      }

}

