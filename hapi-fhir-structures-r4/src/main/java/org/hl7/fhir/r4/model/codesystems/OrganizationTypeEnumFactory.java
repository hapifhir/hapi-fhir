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

public class OrganizationTypeEnumFactory implements EnumFactory<OrganizationType> {

  public OrganizationType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("prov".equals(codeString))
      return OrganizationType.PROV;
    if ("dept".equals(codeString))
      return OrganizationType.DEPT;
    if ("team".equals(codeString))
      return OrganizationType.TEAM;
    if ("govt".equals(codeString))
      return OrganizationType.GOVT;
    if ("ins".equals(codeString))
      return OrganizationType.INS;
    if ("pay".equals(codeString))
      return OrganizationType.PAY;
    if ("edu".equals(codeString))
      return OrganizationType.EDU;
    if ("reli".equals(codeString))
      return OrganizationType.RELI;
    if ("crs".equals(codeString))
      return OrganizationType.CRS;
    if ("cg".equals(codeString))
      return OrganizationType.CG;
    if ("bus".equals(codeString))
      return OrganizationType.BUS;
    if ("other".equals(codeString))
      return OrganizationType.OTHER;
    throw new IllegalArgumentException("Unknown OrganizationType code '"+codeString+"'");
  }

  public String toCode(OrganizationType code) {
    if (code == OrganizationType.PROV)
      return "prov";
    if (code == OrganizationType.DEPT)
      return "dept";
    if (code == OrganizationType.TEAM)
      return "team";
    if (code == OrganizationType.GOVT)
      return "govt";
    if (code == OrganizationType.INS)
      return "ins";
    if (code == OrganizationType.PAY)
      return "pay";
    if (code == OrganizationType.EDU)
      return "edu";
    if (code == OrganizationType.RELI)
      return "reli";
    if (code == OrganizationType.CRS)
      return "crs";
    if (code == OrganizationType.CG)
      return "cg";
    if (code == OrganizationType.BUS)
      return "bus";
    if (code == OrganizationType.OTHER)
      return "other";
    return "?";
  }

    public String toSystem(OrganizationType code) {
      return code.getSystem();
      }

}

