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

public class EndpointConnectionTypeEnumFactory implements EnumFactory<EndpointConnectionType> {

  public EndpointConnectionType fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("ihe-xcpd".equals(codeString))
      return EndpointConnectionType.IHEXCPD;
    if ("ihe-xca".equals(codeString))
      return EndpointConnectionType.IHEXCA;
    if ("ihe-xdr".equals(codeString))
      return EndpointConnectionType.IHEXDR;
    if ("ihe-xds".equals(codeString))
      return EndpointConnectionType.IHEXDS;
    if ("ihe-iid".equals(codeString))
      return EndpointConnectionType.IHEIID;
    if ("dicom-wado-rs".equals(codeString))
      return EndpointConnectionType.DICOMWADORS;
    if ("dicom-qido-rs".equals(codeString))
      return EndpointConnectionType.DICOMQIDORS;
    if ("dicom-stow-rs".equals(codeString))
      return EndpointConnectionType.DICOMSTOWRS;
    if ("dicom-wado-uri".equals(codeString))
      return EndpointConnectionType.DICOMWADOURI;
    if ("hl7-fhir-rest".equals(codeString))
      return EndpointConnectionType.HL7FHIRREST;
    if ("hl7-fhir-msg".equals(codeString))
      return EndpointConnectionType.HL7FHIRMSG;
    if ("hl7v2-mllp".equals(codeString))
      return EndpointConnectionType.HL7V2MLLP;
    if ("secure-email".equals(codeString))
      return EndpointConnectionType.SECUREEMAIL;
    if ("direct-project".equals(codeString))
      return EndpointConnectionType.DIRECTPROJECT;
    throw new IllegalArgumentException("Unknown EndpointConnectionType code '"+codeString+"'");
  }

  public String toCode(EndpointConnectionType code) {
    if (code == EndpointConnectionType.IHEXCPD)
      return "ihe-xcpd";
    if (code == EndpointConnectionType.IHEXCA)
      return "ihe-xca";
    if (code == EndpointConnectionType.IHEXDR)
      return "ihe-xdr";
    if (code == EndpointConnectionType.IHEXDS)
      return "ihe-xds";
    if (code == EndpointConnectionType.IHEIID)
      return "ihe-iid";
    if (code == EndpointConnectionType.DICOMWADORS)
      return "dicom-wado-rs";
    if (code == EndpointConnectionType.DICOMQIDORS)
      return "dicom-qido-rs";
    if (code == EndpointConnectionType.DICOMSTOWRS)
      return "dicom-stow-rs";
    if (code == EndpointConnectionType.DICOMWADOURI)
      return "dicom-wado-uri";
    if (code == EndpointConnectionType.HL7FHIRREST)
      return "hl7-fhir-rest";
    if (code == EndpointConnectionType.HL7FHIRMSG)
      return "hl7-fhir-msg";
    if (code == EndpointConnectionType.HL7V2MLLP)
      return "hl7v2-mllp";
    if (code == EndpointConnectionType.SECUREEMAIL)
      return "secure-email";
    if (code == EndpointConnectionType.DIRECTPROJECT)
      return "direct-project";
    return "?";
  }

    public String toSystem(EndpointConnectionType code) {
      return code.getSystem();
      }

}

