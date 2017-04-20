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

public enum IdentifierType {

        /**
         * A identifier assigned to a device using the Universal Device Identifier framework as defined by FDA (http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/).
         */
        UDI, 
        /**
         * An identifier affixed to an item by the manufacturer when it is first made, where each item has a different identifier.
         */
        SNO, 
        /**
         * An identifier issued by a governmental organization to an individual for the purpose of the receipt of social services and benefits.
         */
        SB, 
        /**
         * The identifier associated with the person or service that requests or places an order.
         */
        PLAC, 
        /**
         * The Identifier associated with the person, or service, who produces the observations or fulfills the order requested by the requestor.
         */
        FILL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IdentifierType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("UDI".equals(codeString))
          return UDI;
        if ("SNO".equals(codeString))
          return SNO;
        if ("SB".equals(codeString))
          return SB;
        if ("PLAC".equals(codeString))
          return PLAC;
        if ("FILL".equals(codeString))
          return FILL;
        throw new FHIRException("Unknown IdentifierType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UDI: return "UDI";
            case SNO: return "SNO";
            case SB: return "SB";
            case PLAC: return "PLAC";
            case FILL: return "FILL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/identifier-type";
        }
        public String getDefinition() {
          switch (this) {
            case UDI: return "A identifier assigned to a device using the Universal Device Identifier framework as defined by FDA (http://www.fda.gov/MedicalDevices/DeviceRegulationandGuidance/UniqueDeviceIdentification/).";
            case SNO: return "An identifier affixed to an item by the manufacturer when it is first made, where each item has a different identifier.";
            case SB: return "An identifier issued by a governmental organization to an individual for the purpose of the receipt of social services and benefits.";
            case PLAC: return "The identifier associated with the person or service that requests or places an order.";
            case FILL: return "The Identifier associated with the person, or service, who produces the observations or fulfills the order requested by the requestor.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UDI: return "Universal Device Identifier";
            case SNO: return "Serial Number";
            case SB: return "Social Beneficiary Identifier";
            case PLAC: return "Placer Identifier";
            case FILL: return "Filler Identifier";
            default: return "?";
          }
    }


}

