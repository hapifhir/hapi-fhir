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

public class TaskStatusEnumFactory implements EnumFactory<TaskStatus> {

  public TaskStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("draft".equals(codeString))
      return TaskStatus.DRAFT;
    if ("requested".equals(codeString))
      return TaskStatus.REQUESTED;
    if ("received".equals(codeString))
      return TaskStatus.RECEIVED;
    if ("accepted".equals(codeString))
      return TaskStatus.ACCEPTED;
    if ("rejected".equals(codeString))
      return TaskStatus.REJECTED;
    if ("ready".equals(codeString))
      return TaskStatus.READY;
    if ("cancelled".equals(codeString))
      return TaskStatus.CANCELLED;
    if ("in-progress".equals(codeString))
      return TaskStatus.INPROGRESS;
    if ("on-hold".equals(codeString))
      return TaskStatus.ONHOLD;
    if ("failed".equals(codeString))
      return TaskStatus.FAILED;
    if ("completed".equals(codeString))
      return TaskStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return TaskStatus.ENTEREDINERROR;
    throw new IllegalArgumentException("Unknown TaskStatus code '"+codeString+"'");
  }

  public String toCode(TaskStatus code) {
    if (code == TaskStatus.DRAFT)
      return "draft";
    if (code == TaskStatus.REQUESTED)
      return "requested";
    if (code == TaskStatus.RECEIVED)
      return "received";
    if (code == TaskStatus.ACCEPTED)
      return "accepted";
    if (code == TaskStatus.REJECTED)
      return "rejected";
    if (code == TaskStatus.READY)
      return "ready";
    if (code == TaskStatus.CANCELLED)
      return "cancelled";
    if (code == TaskStatus.INPROGRESS)
      return "in-progress";
    if (code == TaskStatus.ONHOLD)
      return "on-hold";
    if (code == TaskStatus.FAILED)
      return "failed";
    if (code == TaskStatus.COMPLETED)
      return "completed";
    if (code == TaskStatus.ENTEREDINERROR)
      return "entered-in-error";
    return "?";
  }

    public String toSystem(TaskStatus code) {
      return code.getSystem();
      }

}

