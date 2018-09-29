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

public class ResourceStatusEnumFactory implements EnumFactory<ResourceStatus> {

  public ResourceStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("error".equals(codeString))
      return ResourceStatus.ERROR;
    if ("proposed".equals(codeString))
      return ResourceStatus.PROPOSED;
    if ("planned".equals(codeString))
      return ResourceStatus.PLANNED;
    if ("draft".equals(codeString))
      return ResourceStatus.DRAFT;
    if ("requested".equals(codeString))
      return ResourceStatus.REQUESTED;
    if ("received".equals(codeString))
      return ResourceStatus.RECEIVED;
    if ("declined".equals(codeString))
      return ResourceStatus.DECLINED;
    if ("accepted".equals(codeString))
      return ResourceStatus.ACCEPTED;
    if ("arrived".equals(codeString))
      return ResourceStatus.ARRIVED;
    if ("active".equals(codeString))
      return ResourceStatus.ACTIVE;
    if ("suspended".equals(codeString))
      return ResourceStatus.SUSPENDED;
    if ("failed".equals(codeString))
      return ResourceStatus.FAILED;
    if ("replaced".equals(codeString))
      return ResourceStatus.REPLACED;
    if ("complete".equals(codeString))
      return ResourceStatus.COMPLETE;
    if ("inactive".equals(codeString))
      return ResourceStatus.INACTIVE;
    if ("abandoned".equals(codeString))
      return ResourceStatus.ABANDONED;
    if ("unknown".equals(codeString))
      return ResourceStatus.UNKNOWN;
    if ("unconfirmed".equals(codeString))
      return ResourceStatus.UNCONFIRMED;
    if ("confirmed".equals(codeString))
      return ResourceStatus.CONFIRMED;
    if ("resolved".equals(codeString))
      return ResourceStatus.RESOLVED;
    if ("refuted".equals(codeString))
      return ResourceStatus.REFUTED;
    if ("differential".equals(codeString))
      return ResourceStatus.DIFFERENTIAL;
    if ("partial".equals(codeString))
      return ResourceStatus.PARTIAL;
    if ("busy-unavailable".equals(codeString))
      return ResourceStatus.BUSYUNAVAILABLE;
    if ("free".equals(codeString))
      return ResourceStatus.FREE;
    if ("on-target".equals(codeString))
      return ResourceStatus.ONTARGET;
    if ("ahead-of-target".equals(codeString))
      return ResourceStatus.AHEADOFTARGET;
    if ("behind-target".equals(codeString))
      return ResourceStatus.BEHINDTARGET;
    if ("not-ready".equals(codeString))
      return ResourceStatus.NOTREADY;
    if ("transduc-discon".equals(codeString))
      return ResourceStatus.TRANSDUCDISCON;
    if ("hw-discon".equals(codeString))
      return ResourceStatus.HWDISCON;
    throw new IllegalArgumentException("Unknown ResourceStatus code '"+codeString+"'");
  }

  public String toCode(ResourceStatus code) {
    if (code == ResourceStatus.ERROR)
      return "error";
    if (code == ResourceStatus.PROPOSED)
      return "proposed";
    if (code == ResourceStatus.PLANNED)
      return "planned";
    if (code == ResourceStatus.DRAFT)
      return "draft";
    if (code == ResourceStatus.REQUESTED)
      return "requested";
    if (code == ResourceStatus.RECEIVED)
      return "received";
    if (code == ResourceStatus.DECLINED)
      return "declined";
    if (code == ResourceStatus.ACCEPTED)
      return "accepted";
    if (code == ResourceStatus.ARRIVED)
      return "arrived";
    if (code == ResourceStatus.ACTIVE)
      return "active";
    if (code == ResourceStatus.SUSPENDED)
      return "suspended";
    if (code == ResourceStatus.FAILED)
      return "failed";
    if (code == ResourceStatus.REPLACED)
      return "replaced";
    if (code == ResourceStatus.COMPLETE)
      return "complete";
    if (code == ResourceStatus.INACTIVE)
      return "inactive";
    if (code == ResourceStatus.ABANDONED)
      return "abandoned";
    if (code == ResourceStatus.UNKNOWN)
      return "unknown";
    if (code == ResourceStatus.UNCONFIRMED)
      return "unconfirmed";
    if (code == ResourceStatus.CONFIRMED)
      return "confirmed";
    if (code == ResourceStatus.RESOLVED)
      return "resolved";
    if (code == ResourceStatus.REFUTED)
      return "refuted";
    if (code == ResourceStatus.DIFFERENTIAL)
      return "differential";
    if (code == ResourceStatus.PARTIAL)
      return "partial";
    if (code == ResourceStatus.BUSYUNAVAILABLE)
      return "busy-unavailable";
    if (code == ResourceStatus.FREE)
      return "free";
    if (code == ResourceStatus.ONTARGET)
      return "on-target";
    if (code == ResourceStatus.AHEADOFTARGET)
      return "ahead-of-target";
    if (code == ResourceStatus.BEHINDTARGET)
      return "behind-target";
    if (code == ResourceStatus.NOTREADY)
      return "not-ready";
    if (code == ResourceStatus.TRANSDUCDISCON)
      return "transduc-discon";
    if (code == ResourceStatus.HWDISCON)
      return "hw-discon";
    return "?";
  }

    public String toSystem(ResourceStatus code) {
      return code.getSystem();
      }

}

