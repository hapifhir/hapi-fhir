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

// Generated on Wed, Jan 10, 2018 14:53-0500 for FHIR v3.2.0


import org.hl7.fhir.r4.model.EnumFactory;

public class MediaStatusEnumFactory implements EnumFactory<MediaStatus> {

  public MediaStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("preparation".equals(codeString))
      return MediaStatus.PREPARATION;
    if ("in-progress".equals(codeString))
      return MediaStatus.INPROGRESS;
    if ("not-done".equals(codeString))
      return MediaStatus.NOTDONE;
    if ("suspended".equals(codeString))
      return MediaStatus.SUSPENDED;
    if ("aborted".equals(codeString))
      return MediaStatus.ABORTED;
    if ("completed".equals(codeString))
      return MediaStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return MediaStatus.ENTEREDINERROR;
    if ("unknown".equals(codeString))
      return MediaStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown MediaStatus code '"+codeString+"'");
  }

  public String toCode(MediaStatus code) {
    if (code == MediaStatus.PREPARATION)
      return "preparation";
    if (code == MediaStatus.INPROGRESS)
      return "in-progress";
    if (code == MediaStatus.NOTDONE)
      return "not-done";
    if (code == MediaStatus.SUSPENDED)
      return "suspended";
    if (code == MediaStatus.ABORTED)
      return "aborted";
    if (code == MediaStatus.COMPLETED)
      return "completed";
    if (code == MediaStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == MediaStatus.UNKNOWN)
      return "unknown";
    return "?";
  }

    public String toSystem(MediaStatus code) {
      return code.getSystem();
      }

}

