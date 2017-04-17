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

public class ActionRelationshipTypeEnumFactory implements EnumFactory<ActionRelationshipType> {

  public ActionRelationshipType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("before-start".equals(codeString))
      return ActionRelationshipType.BEFORESTART;
    if ("before".equals(codeString))
      return ActionRelationshipType.BEFORE;
    if ("before-end".equals(codeString))
      return ActionRelationshipType.BEFOREEND;
    if ("concurrent-with-start".equals(codeString))
      return ActionRelationshipType.CONCURRENTWITHSTART;
    if ("concurrent".equals(codeString))
      return ActionRelationshipType.CONCURRENT;
    if ("concurrent-with-end".equals(codeString))
      return ActionRelationshipType.CONCURRENTWITHEND;
    if ("after-start".equals(codeString))
      return ActionRelationshipType.AFTERSTART;
    if ("after".equals(codeString))
      return ActionRelationshipType.AFTER;
    if ("after-end".equals(codeString))
      return ActionRelationshipType.AFTEREND;
    throw new IllegalArgumentException("Unknown ActionRelationshipType code '"+codeString+"'");
  }

  public String toCode(ActionRelationshipType code) {
    if (code == ActionRelationshipType.BEFORESTART)
      return "before-start";
    if (code == ActionRelationshipType.BEFORE)
      return "before";
    if (code == ActionRelationshipType.BEFOREEND)
      return "before-end";
    if (code == ActionRelationshipType.CONCURRENTWITHSTART)
      return "concurrent-with-start";
    if (code == ActionRelationshipType.CONCURRENT)
      return "concurrent";
    if (code == ActionRelationshipType.CONCURRENTWITHEND)
      return "concurrent-with-end";
    if (code == ActionRelationshipType.AFTERSTART)
      return "after-start";
    if (code == ActionRelationshipType.AFTER)
      return "after";
    if (code == ActionRelationshipType.AFTEREND)
      return "after-end";
    return "?";
  }

    public String toSystem(ActionRelationshipType code) {
      return code.getSystem();
      }

}

