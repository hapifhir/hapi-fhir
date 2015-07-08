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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0


import org.hl7.fhir.instance.model.EnumFactory;

public class BasicResourceTypeEnumFactory implements EnumFactory<BasicResourceType> {

  public BasicResourceType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("CONSENT".equals(codeString))
      return BasicResourceType.CONSENT;
    if ("REFERRAL".equals(codeString))
      return BasicResourceType.REFERRAL;
    if ("SLOT".equals(codeString))
      return BasicResourceType.SLOT;
    if ("ADVEVENT".equals(codeString))
      return BasicResourceType.ADVEVENT;
    if ("APTMTREQ".equals(codeString))
      return BasicResourceType.APTMTREQ;
    if ("TRANSFER".equals(codeString))
      return BasicResourceType.TRANSFER;
    if ("DIET".equals(codeString))
      return BasicResourceType.DIET;
    if ("ADMINACT".equals(codeString))
      return BasicResourceType.ADMINACT;
    if ("EXPOSURE".equals(codeString))
      return BasicResourceType.EXPOSURE;
    if ("INVESTIGATION".equals(codeString))
      return BasicResourceType.INVESTIGATION;
    if ("ACCOUNT".equals(codeString))
      return BasicResourceType.ACCOUNT;
    if ("INVOICE".equals(codeString))
      return BasicResourceType.INVOICE;
    if ("ADJUDICAT".equals(codeString))
      return BasicResourceType.ADJUDICAT;
    if ("PAYMENT".equals(codeString))
      return BasicResourceType.PAYMENT;
    if ("PREDETREQ".equals(codeString))
      return BasicResourceType.PREDETREQ;
    if ("PREDETERMINE".equals(codeString))
      return BasicResourceType.PREDETERMINE;
    if ("STUDY".equals(codeString))
      return BasicResourceType.STUDY;
    if ("PROTOCOL".equals(codeString))
      return BasicResourceType.PROTOCOL;
    throw new IllegalArgumentException("Unknown BasicResourceType code '"+codeString+"'");
  }

  public String toCode(BasicResourceType code) {
    if (code == BasicResourceType.CONSENT)
      return "CONSENT";
    if (code == BasicResourceType.REFERRAL)
      return "REFERRAL";
    if (code == BasicResourceType.SLOT)
      return "SLOT";
    if (code == BasicResourceType.ADVEVENT)
      return "ADVEVENT";
    if (code == BasicResourceType.APTMTREQ)
      return "APTMTREQ";
    if (code == BasicResourceType.TRANSFER)
      return "TRANSFER";
    if (code == BasicResourceType.DIET)
      return "DIET";
    if (code == BasicResourceType.ADMINACT)
      return "ADMINACT";
    if (code == BasicResourceType.EXPOSURE)
      return "EXPOSURE";
    if (code == BasicResourceType.INVESTIGATION)
      return "INVESTIGATION";
    if (code == BasicResourceType.ACCOUNT)
      return "ACCOUNT";
    if (code == BasicResourceType.INVOICE)
      return "INVOICE";
    if (code == BasicResourceType.ADJUDICAT)
      return "ADJUDICAT";
    if (code == BasicResourceType.PAYMENT)
      return "PAYMENT";
    if (code == BasicResourceType.PREDETREQ)
      return "PREDETREQ";
    if (code == BasicResourceType.PREDETERMINE)
      return "PREDETERMINE";
    if (code == BasicResourceType.STUDY)
      return "STUDY";
    if (code == BasicResourceType.PROTOCOL)
      return "PROTOCOL";
    return "?";
  }


}

