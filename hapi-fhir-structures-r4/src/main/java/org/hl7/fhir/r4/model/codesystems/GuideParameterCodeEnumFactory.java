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

public class GuideParameterCodeEnumFactory implements EnumFactory<GuideParameterCode> {

  public GuideParameterCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("apply-business-version".equals(codeString))
      return GuideParameterCode.APPLYBUSINESSVERSION;
    if ("apply-jurisdiction".equals(codeString))
      return GuideParameterCode.APPLYJURISDICTION;
    if ("path-resource".equals(codeString))
      return GuideParameterCode.PATHRESOURCE;
    if ("path-pages".equals(codeString))
      return GuideParameterCode.PATHPAGES;
    if ("path-tx-cache".equals(codeString))
      return GuideParameterCode.PATHTXCACHE;
    if ("expansion-parameter".equals(codeString))
      return GuideParameterCode.EXPANSIONPARAMETER;
    if ("rule-broken-links".equals(codeString))
      return GuideParameterCode.RULEBROKENLINKS;
    if ("generate-xml".equals(codeString))
      return GuideParameterCode.GENERATEXML;
    if ("generate-json".equals(codeString))
      return GuideParameterCode.GENERATEJSON;
    if ("generate-turtle".equals(codeString))
      return GuideParameterCode.GENERATETURTLE;
    if ("html-template".equals(codeString))
      return GuideParameterCode.HTMLTEMPLATE;
    throw new IllegalArgumentException("Unknown GuideParameterCode code '"+codeString+"'");
  }

  public String toCode(GuideParameterCode code) {
    if (code == GuideParameterCode.APPLYBUSINESSVERSION)
      return "apply-business-version";
    if (code == GuideParameterCode.APPLYJURISDICTION)
      return "apply-jurisdiction";
    if (code == GuideParameterCode.PATHRESOURCE)
      return "path-resource";
    if (code == GuideParameterCode.PATHPAGES)
      return "path-pages";
    if (code == GuideParameterCode.PATHTXCACHE)
      return "path-tx-cache";
    if (code == GuideParameterCode.EXPANSIONPARAMETER)
      return "expansion-parameter";
    if (code == GuideParameterCode.RULEBROKENLINKS)
      return "rule-broken-links";
    if (code == GuideParameterCode.GENERATEXML)
      return "generate-xml";
    if (code == GuideParameterCode.GENERATEJSON)
      return "generate-json";
    if (code == GuideParameterCode.GENERATETURTLE)
      return "generate-turtle";
    if (code == GuideParameterCode.HTMLTEMPLATE)
      return "html-template";
    return "?";
  }

    public String toSystem(GuideParameterCode code) {
      return code.getSystem();
      }

}

