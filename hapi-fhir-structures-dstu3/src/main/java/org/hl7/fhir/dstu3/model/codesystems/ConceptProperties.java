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

public enum ConceptProperties {

        /**
         * True if the concept is not considered active - e.g. not a valid concept any more. Property type is boolean, default value is false
         */
        INACTIVE, 
        /**
         * The date at which a concept was deprecated. Concepts that are deprecated but not inactive can still be used, but their use is discouraged, and they should be expected to be made inactive in a future release. Property type is dateTime
         */
        DEPRECATED, 
        /**
         * The concept is not intended to be chosen by the user - only intended to be used as a selector for other concepts. Note, though, that the interpretation of this is highly contextual; all concepts are selectable in some context. Property type is boolean, default value is false
         */
        NOTSELECTABLE, 
        /**
         * The concept identified in this property is a parent of the concept on which it is a property. The property type will be 'code'. The meaning of 'parent' is defined by the hierarchyMeaning attribute
         */
        PARENT, 
        /**
         * The concept identified in this property is a child of the concept on which it is a property. The property type will be 'code'. The meaning of 'child' is defined by the hierarchyMeaning attribute
         */
        CHILD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConceptProperties fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("deprecated".equals(codeString))
          return DEPRECATED;
        if ("notSelectable".equals(codeString))
          return NOTSELECTABLE;
        if ("parent".equals(codeString))
          return PARENT;
        if ("child".equals(codeString))
          return CHILD;
        throw new FHIRException("Unknown ConceptProperties code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INACTIVE: return "inactive";
            case DEPRECATED: return "deprecated";
            case NOTSELECTABLE: return "notSelectable";
            case PARENT: return "parent";
            case CHILD: return "child";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/concept-properties";
        }
        public String getDefinition() {
          switch (this) {
            case INACTIVE: return "True if the concept is not considered active - e.g. not a valid concept any more. Property type is boolean, default value is false";
            case DEPRECATED: return "The date at which a concept was deprecated. Concepts that are deprecated but not inactive can still be used, but their use is discouraged, and they should be expected to be made inactive in a future release. Property type is dateTime";
            case NOTSELECTABLE: return "The concept is not intended to be chosen by the user - only intended to be used as a selector for other concepts. Note, though, that the interpretation of this is highly contextual; all concepts are selectable in some context. Property type is boolean, default value is false";
            case PARENT: return "The concept identified in this property is a parent of the concept on which it is a property. The property type will be 'code'. The meaning of 'parent' is defined by the hierarchyMeaning attribute";
            case CHILD: return "The concept identified in this property is a child of the concept on which it is a property. The property type will be 'code'. The meaning of 'child' is defined by the hierarchyMeaning attribute";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INACTIVE: return "Inactive";
            case DEPRECATED: return "Deprecated";
            case NOTSELECTABLE: return "Not Selectable";
            case PARENT: return "Parent";
            case CHILD: return "Child";
            default: return "?";
          }
    }


}

