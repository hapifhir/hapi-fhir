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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0


import org.hl7.fhir.r4.model.EnumFactory;

public class Iso21089LifecycleEnumFactory implements EnumFactory<Iso21089Lifecycle> {

  public Iso21089Lifecycle fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("access".equals(codeString))
      return Iso21089Lifecycle.ACCESS;
    if ("hold".equals(codeString))
      return Iso21089Lifecycle.HOLD;
    if ("amend".equals(codeString))
      return Iso21089Lifecycle.AMEND;
    if ("archive".equals(codeString))
      return Iso21089Lifecycle.ARCHIVE;
    if ("attest".equals(codeString))
      return Iso21089Lifecycle.ATTEST;
    if ("decrypt".equals(codeString))
      return Iso21089Lifecycle.DECRYPT;
    if ("deidentify".equals(codeString))
      return Iso21089Lifecycle.DEIDENTIFY;
    if ("deprecate".equals(codeString))
      return Iso21089Lifecycle.DEPRECATE;
    if ("destroy".equals(codeString))
      return Iso21089Lifecycle.DESTROY;
    if ("disclose".equals(codeString))
      return Iso21089Lifecycle.DISCLOSE;
    if ("encrypt".equals(codeString))
      return Iso21089Lifecycle.ENCRYPT;
    if ("extract".equals(codeString))
      return Iso21089Lifecycle.EXTRACT;
    if ("link".equals(codeString))
      return Iso21089Lifecycle.LINK;
    if ("merge".equals(codeString))
      return Iso21089Lifecycle.MERGE;
    if ("originate".equals(codeString))
      return Iso21089Lifecycle.ORIGINATE;
    if ("pseudonymize".equals(codeString))
      return Iso21089Lifecycle.PSEUDONYMIZE;
    if ("reactivate".equals(codeString))
      return Iso21089Lifecycle.REACTIVATE;
    if ("receive".equals(codeString))
      return Iso21089Lifecycle.RECEIVE;
    if ("reidentify".equals(codeString))
      return Iso21089Lifecycle.REIDENTIFY;
    if ("unhold".equals(codeString))
      return Iso21089Lifecycle.UNHOLD;
    if ("report".equals(codeString))
      return Iso21089Lifecycle.REPORT;
    if ("restore".equals(codeString))
      return Iso21089Lifecycle.RESTORE;
    if ("transform".equals(codeString))
      return Iso21089Lifecycle.TRANSFORM;
    if ("transmit".equals(codeString))
      return Iso21089Lifecycle.TRANSMIT;
    if ("unlink".equals(codeString))
      return Iso21089Lifecycle.UNLINK;
    if ("unmerge".equals(codeString))
      return Iso21089Lifecycle.UNMERGE;
    if ("verify".equals(codeString))
      return Iso21089Lifecycle.VERIFY;
    throw new IllegalArgumentException("Unknown Iso21089Lifecycle code '"+codeString+"'");
  }

  public String toCode(Iso21089Lifecycle code) {
    if (code == Iso21089Lifecycle.ACCESS)
      return "access";
    if (code == Iso21089Lifecycle.HOLD)
      return "hold";
    if (code == Iso21089Lifecycle.AMEND)
      return "amend";
    if (code == Iso21089Lifecycle.ARCHIVE)
      return "archive";
    if (code == Iso21089Lifecycle.ATTEST)
      return "attest";
    if (code == Iso21089Lifecycle.DECRYPT)
      return "decrypt";
    if (code == Iso21089Lifecycle.DEIDENTIFY)
      return "deidentify";
    if (code == Iso21089Lifecycle.DEPRECATE)
      return "deprecate";
    if (code == Iso21089Lifecycle.DESTROY)
      return "destroy";
    if (code == Iso21089Lifecycle.DISCLOSE)
      return "disclose";
    if (code == Iso21089Lifecycle.ENCRYPT)
      return "encrypt";
    if (code == Iso21089Lifecycle.EXTRACT)
      return "extract";
    if (code == Iso21089Lifecycle.LINK)
      return "link";
    if (code == Iso21089Lifecycle.MERGE)
      return "merge";
    if (code == Iso21089Lifecycle.ORIGINATE)
      return "originate";
    if (code == Iso21089Lifecycle.PSEUDONYMIZE)
      return "pseudonymize";
    if (code == Iso21089Lifecycle.REACTIVATE)
      return "reactivate";
    if (code == Iso21089Lifecycle.RECEIVE)
      return "receive";
    if (code == Iso21089Lifecycle.REIDENTIFY)
      return "reidentify";
    if (code == Iso21089Lifecycle.UNHOLD)
      return "unhold";
    if (code == Iso21089Lifecycle.REPORT)
      return "report";
    if (code == Iso21089Lifecycle.RESTORE)
      return "restore";
    if (code == Iso21089Lifecycle.TRANSFORM)
      return "transform";
    if (code == Iso21089Lifecycle.TRANSMIT)
      return "transmit";
    if (code == Iso21089Lifecycle.UNLINK)
      return "unlink";
    if (code == Iso21089Lifecycle.UNMERGE)
      return "unmerge";
    if (code == Iso21089Lifecycle.VERIFY)
      return "verify";
    return "?";
  }

    public String toSystem(Iso21089Lifecycle code) {
      return code.getSystem();
      }

}

