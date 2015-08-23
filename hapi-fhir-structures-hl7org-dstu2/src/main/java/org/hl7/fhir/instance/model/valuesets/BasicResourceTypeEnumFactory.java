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

public class BasicResourceTypeEnumFactory implements EnumFactory<BasicResourceType> {

  public BasicResourceType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("consent".equals(codeString))
      return BasicResourceType.CONSENT;
    if ("referral".equals(codeString))
      return BasicResourceType.REFERRAL;
    if ("advevent".equals(codeString))
      return BasicResourceType.ADVEVENT;
    if ("aptmtreq".equals(codeString))
      return BasicResourceType.APTMTREQ;
    if ("transfer".equals(codeString))
      return BasicResourceType.TRANSFER;
    if ("diet".equals(codeString))
      return BasicResourceType.DIET;
    if ("adminact".equals(codeString))
      return BasicResourceType.ADMINACT;
    if ("exposure".equals(codeString))
      return BasicResourceType.EXPOSURE;
    if ("investigation".equals(codeString))
      return BasicResourceType.INVESTIGATION;
    if ("account".equals(codeString))
      return BasicResourceType.ACCOUNT;
    if ("invoice".equals(codeString))
      return BasicResourceType.INVOICE;
    if ("adjudicat".equals(codeString))
      return BasicResourceType.ADJUDICAT;
    if ("predetreq".equals(codeString))
      return BasicResourceType.PREDETREQ;
    if ("predetermine".equals(codeString))
      return BasicResourceType.PREDETERMINE;
    if ("study".equals(codeString))
      return BasicResourceType.STUDY;
    if ("protocol".equals(codeString))
      return BasicResourceType.PROTOCOL;
    throw new IllegalArgumentException("Unknown BasicResourceType code '"+codeString+"'");
  }

  public String toCode(BasicResourceType code) {
    if (code == BasicResourceType.CONSENT)
      return "consent";
    if (code == BasicResourceType.REFERRAL)
      return "referral";
    if (code == BasicResourceType.ADVEVENT)
      return "advevent";
    if (code == BasicResourceType.APTMTREQ)
      return "aptmtreq";
    if (code == BasicResourceType.TRANSFER)
      return "transfer";
    if (code == BasicResourceType.DIET)
      return "diet";
    if (code == BasicResourceType.ADMINACT)
      return "adminact";
    if (code == BasicResourceType.EXPOSURE)
      return "exposure";
    if (code == BasicResourceType.INVESTIGATION)
      return "investigation";
    if (code == BasicResourceType.ACCOUNT)
      return "account";
    if (code == BasicResourceType.INVOICE)
      return "invoice";
    if (code == BasicResourceType.ADJUDICAT)
      return "adjudicat";
    if (code == BasicResourceType.PREDETREQ)
      return "predetreq";
    if (code == BasicResourceType.PREDETERMINE)
      return "predetermine";
    if (code == BasicResourceType.STUDY)
      return "study";
    if (code == BasicResourceType.PROTOCOL)
      return "protocol";
    return "?";
  }


}

