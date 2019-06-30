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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.r4.model.EnumFactory;

public class SmartCapabilitiesEnumFactory implements EnumFactory<SmartCapabilities> {

  public SmartCapabilities fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("launch-ehr".equals(codeString))
      return SmartCapabilities.LAUNCHEHR;
    if ("launch-standalone".equals(codeString))
      return SmartCapabilities.LAUNCHSTANDALONE;
    if ("client-public".equals(codeString))
      return SmartCapabilities.CLIENTPUBLIC;
    if ("client-confidential-symmetric".equals(codeString))
      return SmartCapabilities.CLIENTCONFIDENTIALSYMMETRIC;
    if ("sso-openid-connect".equals(codeString))
      return SmartCapabilities.SSOOPENIDCONNECT;
    if ("context-passthrough-banner".equals(codeString))
      return SmartCapabilities.CONTEXTPASSTHROUGHBANNER;
    if ("context-passthrough-style".equals(codeString))
      return SmartCapabilities.CONTEXTPASSTHROUGHSTYLE;
    if ("context-ehr-patient".equals(codeString))
      return SmartCapabilities.CONTEXTEHRPATIENT;
    if ("context-ehr-encounter".equals(codeString))
      return SmartCapabilities.CONTEXTEHRENCOUNTER;
    if ("context-standalone-patient".equals(codeString))
      return SmartCapabilities.CONTEXTSTANDALONEPATIENT;
    if ("context-standalone-encounter".equals(codeString))
      return SmartCapabilities.CONTEXTSTANDALONEENCOUNTER;
    if ("permission-offline".equals(codeString))
      return SmartCapabilities.PERMISSIONOFFLINE;
    if ("permission-patient".equals(codeString))
      return SmartCapabilities.PERMISSIONPATIENT;
    if ("permission-user".equals(codeString))
      return SmartCapabilities.PERMISSIONUSER;
    throw new IllegalArgumentException("Unknown SmartCapabilities code '"+codeString+"'");
  }

  public String toCode(SmartCapabilities code) {
    if (code == SmartCapabilities.LAUNCHEHR)
      return "launch-ehr";
    if (code == SmartCapabilities.LAUNCHSTANDALONE)
      return "launch-standalone";
    if (code == SmartCapabilities.CLIENTPUBLIC)
      return "client-public";
    if (code == SmartCapabilities.CLIENTCONFIDENTIALSYMMETRIC)
      return "client-confidential-symmetric";
    if (code == SmartCapabilities.SSOOPENIDCONNECT)
      return "sso-openid-connect";
    if (code == SmartCapabilities.CONTEXTPASSTHROUGHBANNER)
      return "context-passthrough-banner";
    if (code == SmartCapabilities.CONTEXTPASSTHROUGHSTYLE)
      return "context-passthrough-style";
    if (code == SmartCapabilities.CONTEXTEHRPATIENT)
      return "context-ehr-patient";
    if (code == SmartCapabilities.CONTEXTEHRENCOUNTER)
      return "context-ehr-encounter";
    if (code == SmartCapabilities.CONTEXTSTANDALONEPATIENT)
      return "context-standalone-patient";
    if (code == SmartCapabilities.CONTEXTSTANDALONEENCOUNTER)
      return "context-standalone-encounter";
    if (code == SmartCapabilities.PERMISSIONOFFLINE)
      return "permission-offline";
    if (code == SmartCapabilities.PERMISSIONPATIENT)
      return "permission-patient";
    if (code == SmartCapabilities.PERMISSIONUSER)
      return "permission-user";
    return "?";
  }

    public String toSystem(SmartCapabilities code) {
      return code.getSystem();
      }

}

