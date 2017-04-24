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

public enum SpecificationType {

        /**
         * Unspecified Production Specification - MDC_ID_PROD_SPEC_UNSPECIFIED
         */
        UNSPECIFIED, 
        /**
         * Serial Number - MDC_ID_PROD_SPEC_SERIAL
         */
        SERIALNUMBER, 
        /**
         * Part Number - MDC_ID_PROD_SPEC_PART
         */
        PARTNUMBER, 
        /**
         * Hardware Revision - MDC_ID_PROD_SPEC_HW
         */
        HARDWAREREVISION, 
        /**
         * Software Revision - MDC_ID_PROD_SPEC_SW
         */
        SOFTWAREREVISION, 
        /**
         * Firmware Revision - MDC_ID_PROD_SPEC_FW
         */
        FIRMWAREREVISION, 
        /**
         * Protocol Revision - MDC_ID_PROD_SPEC_PROTOCOL
         */
        PROTOCOLREVISION, 
        /**
         * GMDN - MDC_ID_PROD_SPEC_GMDN
         */
        GMDN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SpecificationType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unspecified".equals(codeString))
          return UNSPECIFIED;
        if ("serial-number".equals(codeString))
          return SERIALNUMBER;
        if ("part-number".equals(codeString))
          return PARTNUMBER;
        if ("hardware-revision".equals(codeString))
          return HARDWAREREVISION;
        if ("software-revision".equals(codeString))
          return SOFTWAREREVISION;
        if ("firmware-revision".equals(codeString))
          return FIRMWAREREVISION;
        if ("protocol-revision".equals(codeString))
          return PROTOCOLREVISION;
        if ("gmdn".equals(codeString))
          return GMDN;
        throw new FHIRException("Unknown SpecificationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNSPECIFIED: return "unspecified";
            case SERIALNUMBER: return "serial-number";
            case PARTNUMBER: return "part-number";
            case HARDWAREREVISION: return "hardware-revision";
            case SOFTWAREREVISION: return "software-revision";
            case FIRMWAREREVISION: return "firmware-revision";
            case PROTOCOLREVISION: return "protocol-revision";
            case GMDN: return "gmdn";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/specification-type";
        }
        public String getDefinition() {
          switch (this) {
            case UNSPECIFIED: return "Unspecified Production Specification - MDC_ID_PROD_SPEC_UNSPECIFIED";
            case SERIALNUMBER: return "Serial Number - MDC_ID_PROD_SPEC_SERIAL";
            case PARTNUMBER: return "Part Number - MDC_ID_PROD_SPEC_PART";
            case HARDWAREREVISION: return "Hardware Revision - MDC_ID_PROD_SPEC_HW";
            case SOFTWAREREVISION: return "Software Revision - MDC_ID_PROD_SPEC_SW";
            case FIRMWAREREVISION: return "Firmware Revision - MDC_ID_PROD_SPEC_FW";
            case PROTOCOLREVISION: return "Protocol Revision - MDC_ID_PROD_SPEC_PROTOCOL";
            case GMDN: return "GMDN - MDC_ID_PROD_SPEC_GMDN";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNSPECIFIED: return "Unspecified Production Specification";
            case SERIALNUMBER: return "Serial Number";
            case PARTNUMBER: return "Part Number";
            case HARDWAREREVISION: return "Hardware Revision";
            case SOFTWAREREVISION: return "Software Revision";
            case FIRMWAREREVISION: return "Firmware Revision";
            case PROTOCOLREVISION: return "Protocol Revision";
            case GMDN: return "GMDN";
            default: return "?";
          }
    }


}

