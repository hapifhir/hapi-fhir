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

public class V3DataOperationEnumFactory implements EnumFactory<V3DataOperation> {

  public V3DataOperation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("OPERATE".equals(codeString))
      return V3DataOperation.OPERATE;
    if ("CREATE".equals(codeString))
      return V3DataOperation.CREATE;
    if ("DELETE".equals(codeString))
      return V3DataOperation.DELETE;
    if ("EXECUTE".equals(codeString))
      return V3DataOperation.EXECUTE;
    if ("READ".equals(codeString))
      return V3DataOperation.READ;
    if ("UPDATE".equals(codeString))
      return V3DataOperation.UPDATE;
    if ("APPEND".equals(codeString))
      return V3DataOperation.APPEND;
    if ("MODIFYSTATUS".equals(codeString))
      return V3DataOperation.MODIFYSTATUS;
    if ("ABORT".equals(codeString))
      return V3DataOperation.ABORT;
    if ("ACTIVATE".equals(codeString))
      return V3DataOperation.ACTIVATE;
    if ("CANCEL".equals(codeString))
      return V3DataOperation.CANCEL;
    if ("COMPLETE".equals(codeString))
      return V3DataOperation.COMPLETE;
    if ("HOLD".equals(codeString))
      return V3DataOperation.HOLD;
    if ("JUMP".equals(codeString))
      return V3DataOperation.JUMP;
    if ("NULLIFY".equals(codeString))
      return V3DataOperation.NULLIFY;
    if ("OBSOLETE".equals(codeString))
      return V3DataOperation.OBSOLETE;
    if ("REACTIVATE".equals(codeString))
      return V3DataOperation.REACTIVATE;
    if ("RELEASE".equals(codeString))
      return V3DataOperation.RELEASE;
    if ("RESUME".equals(codeString))
      return V3DataOperation.RESUME;
    if ("SUSPEND".equals(codeString))
      return V3DataOperation.SUSPEND;
    throw new IllegalArgumentException("Unknown V3DataOperation code '"+codeString+"'");
  }

  public String toCode(V3DataOperation code) {
    if (code == V3DataOperation.OPERATE)
      return "OPERATE";
    if (code == V3DataOperation.CREATE)
      return "CREATE";
    if (code == V3DataOperation.DELETE)
      return "DELETE";
    if (code == V3DataOperation.EXECUTE)
      return "EXECUTE";
    if (code == V3DataOperation.READ)
      return "READ";
    if (code == V3DataOperation.UPDATE)
      return "UPDATE";
    if (code == V3DataOperation.APPEND)
      return "APPEND";
    if (code == V3DataOperation.MODIFYSTATUS)
      return "MODIFYSTATUS";
    if (code == V3DataOperation.ABORT)
      return "ABORT";
    if (code == V3DataOperation.ACTIVATE)
      return "ACTIVATE";
    if (code == V3DataOperation.CANCEL)
      return "CANCEL";
    if (code == V3DataOperation.COMPLETE)
      return "COMPLETE";
    if (code == V3DataOperation.HOLD)
      return "HOLD";
    if (code == V3DataOperation.JUMP)
      return "JUMP";
    if (code == V3DataOperation.NULLIFY)
      return "NULLIFY";
    if (code == V3DataOperation.OBSOLETE)
      return "OBSOLETE";
    if (code == V3DataOperation.REACTIVATE)
      return "REACTIVATE";
    if (code == V3DataOperation.RELEASE)
      return "RELEASE";
    if (code == V3DataOperation.RESUME)
      return "RESUME";
    if (code == V3DataOperation.SUSPEND)
      return "SUSPEND";
    return "?";
  }

    public String toSystem(V3DataOperation code) {
      return code.getSystem();
      }

}

