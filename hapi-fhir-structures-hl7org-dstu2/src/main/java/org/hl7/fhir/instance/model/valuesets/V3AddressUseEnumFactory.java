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

public class V3AddressUseEnumFactory implements EnumFactory<V3AddressUse> {

  public V3AddressUse fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_GeneralAddressUse".equals(codeString))
      return V3AddressUse._GENERALADDRESSUSE;
    if ("BAD".equals(codeString))
      return V3AddressUse.BAD;
    if ("CONF".equals(codeString))
      return V3AddressUse.CONF;
    if ("H".equals(codeString))
      return V3AddressUse.H;
    if ("HP".equals(codeString))
      return V3AddressUse.HP;
    if ("HV".equals(codeString))
      return V3AddressUse.HV;
    if ("OLD".equals(codeString))
      return V3AddressUse.OLD;
    if ("TMP".equals(codeString))
      return V3AddressUse.TMP;
    if ("WP".equals(codeString))
      return V3AddressUse.WP;
    if ("DIR".equals(codeString))
      return V3AddressUse.DIR;
    if ("PUB".equals(codeString))
      return V3AddressUse.PUB;
    if ("_PostalAddressUse".equals(codeString))
      return V3AddressUse._POSTALADDRESSUSE;
    if ("PHYS".equals(codeString))
      return V3AddressUse.PHYS;
    if ("PST".equals(codeString))
      return V3AddressUse.PST;
    if ("_TelecommunicationAddressUse".equals(codeString))
      return V3AddressUse._TELECOMMUNICATIONADDRESSUSE;
    if ("AS".equals(codeString))
      return V3AddressUse.AS;
    if ("EC".equals(codeString))
      return V3AddressUse.EC;
    if ("MC".equals(codeString))
      return V3AddressUse.MC;
    if ("PG".equals(codeString))
      return V3AddressUse.PG;
    throw new IllegalArgumentException("Unknown V3AddressUse code '"+codeString+"'");
  }

  public String toCode(V3AddressUse code) {
    if (code == V3AddressUse._GENERALADDRESSUSE)
      return "_GeneralAddressUse";
    if (code == V3AddressUse.BAD)
      return "BAD";
    if (code == V3AddressUse.CONF)
      return "CONF";
    if (code == V3AddressUse.H)
      return "H";
    if (code == V3AddressUse.HP)
      return "HP";
    if (code == V3AddressUse.HV)
      return "HV";
    if (code == V3AddressUse.OLD)
      return "OLD";
    if (code == V3AddressUse.TMP)
      return "TMP";
    if (code == V3AddressUse.WP)
      return "WP";
    if (code == V3AddressUse.DIR)
      return "DIR";
    if (code == V3AddressUse.PUB)
      return "PUB";
    if (code == V3AddressUse._POSTALADDRESSUSE)
      return "_PostalAddressUse";
    if (code == V3AddressUse.PHYS)
      return "PHYS";
    if (code == V3AddressUse.PST)
      return "PST";
    if (code == V3AddressUse._TELECOMMUNICATIONADDRESSUSE)
      return "_TelecommunicationAddressUse";
    if (code == V3AddressUse.AS)
      return "AS";
    if (code == V3AddressUse.EC)
      return "EC";
    if (code == V3AddressUse.MC)
      return "MC";
    if (code == V3AddressUse.PG)
      return "PG";
    return "?";
  }


}

