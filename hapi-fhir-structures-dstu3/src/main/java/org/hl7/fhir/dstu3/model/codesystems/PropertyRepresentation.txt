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

public enum PropertyRepresentation {

        /**
         * In XML, this property is represented as an attribute not an element.
         */
        XMLATTR, 
        /**
         * This element is represented using the XML text attribute (primitives only)
         */
        XMLTEXT, 
        /**
         * The type of this element is indicated using xsi:type
         */
        TYPEATTR, 
        /**
         * Use CDA narrative instead of XHTML
         */
        CDATEXT, 
        /**
         * The property is represented using XHTML
         */
        XHTML, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PropertyRepresentation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xmlAttr".equals(codeString))
          return XMLATTR;
        if ("xmlText".equals(codeString))
          return XMLTEXT;
        if ("typeAttr".equals(codeString))
          return TYPEATTR;
        if ("cdaText".equals(codeString))
          return CDATEXT;
        if ("xhtml".equals(codeString))
          return XHTML;
        throw new FHIRException("Unknown PropertyRepresentation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case XMLATTR: return "xmlAttr";
            case XMLTEXT: return "xmlText";
            case TYPEATTR: return "typeAttr";
            case CDATEXT: return "cdaText";
            case XHTML: return "xhtml";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/property-representation";
        }
        public String getDefinition() {
          switch (this) {
            case XMLATTR: return "In XML, this property is represented as an attribute not an element.";
            case XMLTEXT: return "This element is represented using the XML text attribute (primitives only)";
            case TYPEATTR: return "The type of this element is indicated using xsi:type";
            case CDATEXT: return "Use CDA narrative instead of XHTML";
            case XHTML: return "The property is represented using XHTML";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case XMLATTR: return "XML Attribute";
            case XMLTEXT: return "XML Text";
            case TYPEATTR: return "Type Attribute";
            case CDATEXT: return "CDA Text Format";
            case XHTML: return "XHTML";
            default: return "?";
          }
    }


}

