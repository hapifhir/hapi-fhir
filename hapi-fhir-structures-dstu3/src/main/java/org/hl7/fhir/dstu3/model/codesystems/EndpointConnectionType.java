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


import org.hl7.fhir.exceptions.FHIRException;

public enum EndpointConnectionType {

        /**
         * IHE Cross Community Patient Discovery Profile (XCPD) - http://wiki.ihe.net/index.php/Cross-Community_Patient_Discovery
         */
        IHEXCPD, 
        /**
         * IHE Cross Community Access Profile (XCA) - http://wiki.ihe.net/index.php/Cross-Community_Access
         */
        IHEXCA, 
        /**
         * IHE Cross-Enterprise Document Reliable Exchange (XDR) - http://wiki.ihe.net/index.php/Cross-enterprise_Document_Reliable_Interchange
         */
        IHEXDR, 
        /**
         * IHE Cross-Enterprise Document Sharing (XDS) - http://wiki.ihe.net/index.php/Cross-Enterprise_Document_Sharing
         */
        IHEXDS, 
        /**
         * IHE Invoke Image Display (IID) - http://wiki.ihe.net/index.php/Invoke_Image_Display
         */
        IHEIID, 
        /**
         * DICOMweb RESTful Image Retrieve - http://dicom.nema.org/medical/dicom/current/output/chtml/part18/sect_6.5.html
         */
        DICOMWADORS, 
        /**
         * DICOMweb RESTful Image query - http://dicom.nema.org/medical/dicom/current/output/chtml/part18/sect_6.7.html
         */
        DICOMQIDORS, 
        /**
         * DICOMweb RESTful image sending and storage - http://dicom.nema.org/medical/dicom/current/output/chtml/part18/sect_6.6.html
         */
        DICOMSTOWRS, 
        /**
         * DICOMweb Image Retrieve - http://dicom.nema.org/dicom/2013/output/chtml/part18/sect_6.3.html
         */
        DICOMWADOURI, 
        /**
         * Interact with the server interface using FHIR's RESTful interface. For details on its version/capabilities you should connect the the value in Endpoint.address and retrieve the FHIR CapabilityStatement.
         */
        HL7FHIRREST, 
        /**
         * Use the servers FHIR Messaging interface. Details can be found on the messaging.html page in the FHIR Specification. The FHIR server's base address is specified in the Endpoint.address property.
         */
        HL7FHIRMSG, 
        /**
         * HL7v2 messages over an LLP TCP connection
         */
        HL7V2MLLP, 
        /**
         * Email delivery using a digital certificate to encrypt the content using the public key, receiver must have the private key to decrypt the content
         */
        SECUREEMAIL, 
        /**
         * Direct Project information - http://wiki.directproject.org/
         */
        DIRECTPROJECT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EndpointConnectionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ihe-xcpd".equals(codeString))
          return IHEXCPD;
        if ("ihe-xca".equals(codeString))
          return IHEXCA;
        if ("ihe-xdr".equals(codeString))
          return IHEXDR;
        if ("ihe-xds".equals(codeString))
          return IHEXDS;
        if ("ihe-iid".equals(codeString))
          return IHEIID;
        if ("dicom-wado-rs".equals(codeString))
          return DICOMWADORS;
        if ("dicom-qido-rs".equals(codeString))
          return DICOMQIDORS;
        if ("dicom-stow-rs".equals(codeString))
          return DICOMSTOWRS;
        if ("dicom-wado-uri".equals(codeString))
          return DICOMWADOURI;
        if ("hl7-fhir-rest".equals(codeString))
          return HL7FHIRREST;
        if ("hl7-fhir-msg".equals(codeString))
          return HL7FHIRMSG;
        if ("hl7v2-mllp".equals(codeString))
          return HL7V2MLLP;
        if ("secure-email".equals(codeString))
          return SECUREEMAIL;
        if ("direct-project".equals(codeString))
          return DIRECTPROJECT;
        throw new FHIRException("Unknown EndpointConnectionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IHEXCPD: return "ihe-xcpd";
            case IHEXCA: return "ihe-xca";
            case IHEXDR: return "ihe-xdr";
            case IHEXDS: return "ihe-xds";
            case IHEIID: return "ihe-iid";
            case DICOMWADORS: return "dicom-wado-rs";
            case DICOMQIDORS: return "dicom-qido-rs";
            case DICOMSTOWRS: return "dicom-stow-rs";
            case DICOMWADOURI: return "dicom-wado-uri";
            case HL7FHIRREST: return "hl7-fhir-rest";
            case HL7FHIRMSG: return "hl7-fhir-msg";
            case HL7V2MLLP: return "hl7v2-mllp";
            case SECUREEMAIL: return "secure-email";
            case DIRECTPROJECT: return "direct-project";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/endpoint-connection-type";
        }
        public String getDefinition() {
          switch (this) {
            case IHEXCPD: return "IHE Cross Community Patient Discovery Profile (XCPD) - http://wiki.ihe.net/index.php/Cross-Community_Patient_Discovery";
            case IHEXCA: return "IHE Cross Community Access Profile (XCA) - http://wiki.ihe.net/index.php/Cross-Community_Access";
            case IHEXDR: return "IHE Cross-Enterprise Document Reliable Exchange (XDR) - http://wiki.ihe.net/index.php/Cross-enterprise_Document_Reliable_Interchange";
            case IHEXDS: return "IHE Cross-Enterprise Document Sharing (XDS) - http://wiki.ihe.net/index.php/Cross-Enterprise_Document_Sharing";
            case IHEIID: return "IHE Invoke Image Display (IID) - http://wiki.ihe.net/index.php/Invoke_Image_Display";
            case DICOMWADORS: return "DICOMweb RESTful Image Retrieve - http://dicom.nema.org/medical/dicom/current/output/chtml/part18/sect_6.5.html";
            case DICOMQIDORS: return "DICOMweb RESTful Image query - http://dicom.nema.org/medical/dicom/current/output/chtml/part18/sect_6.7.html";
            case DICOMSTOWRS: return "DICOMweb RESTful image sending and storage - http://dicom.nema.org/medical/dicom/current/output/chtml/part18/sect_6.6.html";
            case DICOMWADOURI: return "DICOMweb Image Retrieve - http://dicom.nema.org/dicom/2013/output/chtml/part18/sect_6.3.html";
            case HL7FHIRREST: return "Interact with the server interface using FHIR's RESTful interface. For details on its version/capabilities you should connect the the value in Endpoint.address and retrieve the FHIR CapabilityStatement.";
            case HL7FHIRMSG: return "Use the servers FHIR Messaging interface. Details can be found on the messaging.html page in the FHIR Specification. The FHIR server's base address is specified in the Endpoint.address property.";
            case HL7V2MLLP: return "HL7v2 messages over an LLP TCP connection";
            case SECUREEMAIL: return "Email delivery using a digital certificate to encrypt the content using the public key, receiver must have the private key to decrypt the content";
            case DIRECTPROJECT: return "Direct Project information - http://wiki.directproject.org/";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IHEXCPD: return "IHE XCPD";
            case IHEXCA: return "IHE XCA";
            case IHEXDR: return "IHE XDR";
            case IHEXDS: return "IHE XDS";
            case IHEIID: return "IHE IID";
            case DICOMWADORS: return "DICOM WADO-RS";
            case DICOMQIDORS: return "DICOM QIDO-RS";
            case DICOMSTOWRS: return "DICOM STOW-RS";
            case DICOMWADOURI: return "DICOM WADO-URI";
            case HL7FHIRREST: return "HL7 FHIR";
            case HL7FHIRMSG: return "HL7 FHIR Messaging";
            case HL7V2MLLP: return "HL7 v2 MLLP";
            case SECUREEMAIL: return "Secure email";
            case DIRECTPROJECT: return "Direct Project";
            default: return "?";
          }
    }


}

