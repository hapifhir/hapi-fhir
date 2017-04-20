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

public enum MapTransform {

        /**
         * create(type : string) - type is passed through to the application on the standard API, and must be known by it
         */
        CREATE, 
        /**
         * copy(source)
         */
        COPY, 
        /**
         * truncate(source, length) - source must be stringy type
         */
        TRUNCATE, 
        /**
         * escape(source, fmt1, fmt2) - change source from one kind of escaping to another (plain, java, xml, json). note that this is for when the string itself is escaped
         */
        ESCAPE, 
        /**
         * cast(source, type?) - case source from one type to another. target type can be left as implicit if there is one and only one target type known
         */
        CAST, 
        /**
         * append(source...) - source is element or string
         */
        APPEND, 
        /**
         * translate(source, uri_of_map) - use the translate operation
         */
        TRANSLATE, 
        /**
         * reference(source : object) - return a string that references the provided tree properly
         */
        REFERENCE, 
        /**
         * Perform a date operation. *Parameters to be documented*
         */
        DATEOP, 
        /**
         * Generate a random UUID (in lowercase). No Parameters
         */
        UUID, 
        /**
         * Return the appropriate string to put in a reference that refers to the resource provided as a parameter
         */
        POINTER, 
        /**
         * Execute the supplied fluentpath expression and use the value returned by that
         */
        EVALUATE, 
        /**
         * Create a CodeableConcept. Parameters = (text) or (system. Code[, display])
         */
        CC, 
        /**
         * Create a Coding. Parameters = (system. Code[, display])
         */
        C, 
        /**
         * Create a quantity. Parameters = (text) or (value, unit, [system, code]) where text is the natural representation e.g. [comparator]value[space]unit
         */
        QTY, 
        /**
         * Create an identifier. Parameters = (system, value[, type]) where type is a code from the identifier type value set
         */
        ID, 
        /**
         * Create a contact details. Parameters = (value) or (system, value). If no system is provided, the system should be inferred from the content of the value
         */
        CP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MapTransform fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("create".equals(codeString))
          return CREATE;
        if ("copy".equals(codeString))
          return COPY;
        if ("truncate".equals(codeString))
          return TRUNCATE;
        if ("escape".equals(codeString))
          return ESCAPE;
        if ("cast".equals(codeString))
          return CAST;
        if ("append".equals(codeString))
          return APPEND;
        if ("translate".equals(codeString))
          return TRANSLATE;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("dateOp".equals(codeString))
          return DATEOP;
        if ("uuid".equals(codeString))
          return UUID;
        if ("pointer".equals(codeString))
          return POINTER;
        if ("evaluate".equals(codeString))
          return EVALUATE;
        if ("cc".equals(codeString))
          return CC;
        if ("c".equals(codeString))
          return C;
        if ("qty".equals(codeString))
          return QTY;
        if ("id".equals(codeString))
          return ID;
        if ("cp".equals(codeString))
          return CP;
        throw new FHIRException("Unknown MapTransform code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CREATE: return "create";
            case COPY: return "copy";
            case TRUNCATE: return "truncate";
            case ESCAPE: return "escape";
            case CAST: return "cast";
            case APPEND: return "append";
            case TRANSLATE: return "translate";
            case REFERENCE: return "reference";
            case DATEOP: return "dateOp";
            case UUID: return "uuid";
            case POINTER: return "pointer";
            case EVALUATE: return "evaluate";
            case CC: return "cc";
            case C: return "c";
            case QTY: return "qty";
            case ID: return "id";
            case CP: return "cp";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/map-transform";
        }
        public String getDefinition() {
          switch (this) {
            case CREATE: return "create(type : string) - type is passed through to the application on the standard API, and must be known by it";
            case COPY: return "copy(source)";
            case TRUNCATE: return "truncate(source, length) - source must be stringy type";
            case ESCAPE: return "escape(source, fmt1, fmt2) - change source from one kind of escaping to another (plain, java, xml, json). note that this is for when the string itself is escaped";
            case CAST: return "cast(source, type?) - case source from one type to another. target type can be left as implicit if there is one and only one target type known";
            case APPEND: return "append(source...) - source is element or string";
            case TRANSLATE: return "translate(source, uri_of_map) - use the translate operation";
            case REFERENCE: return "reference(source : object) - return a string that references the provided tree properly";
            case DATEOP: return "Perform a date operation. *Parameters to be documented*";
            case UUID: return "Generate a random UUID (in lowercase). No Parameters";
            case POINTER: return "Return the appropriate string to put in a reference that refers to the resource provided as a parameter";
            case EVALUATE: return "Execute the supplied fluentpath expression and use the value returned by that";
            case CC: return "Create a CodeableConcept. Parameters = (text) or (system. Code[, display])";
            case C: return "Create a Coding. Parameters = (system. Code[, display])";
            case QTY: return "Create a quantity. Parameters = (text) or (value, unit, [system, code]) where text is the natural representation e.g. [comparator]value[space]unit";
            case ID: return "Create an identifier. Parameters = (system, value[, type]) where type is a code from the identifier type value set";
            case CP: return "Create a contact details. Parameters = (value) or (system, value). If no system is provided, the system should be inferred from the content of the value";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CREATE: return "create";
            case COPY: return "copy";
            case TRUNCATE: return "truncate";
            case ESCAPE: return "escape";
            case CAST: return "cast";
            case APPEND: return "append";
            case TRANSLATE: return "translate";
            case REFERENCE: return "reference";
            case DATEOP: return "dateOp";
            case UUID: return "uuid";
            case POINTER: return "pointer";
            case EVALUATE: return "evaluate";
            case CC: return "cc";
            case C: return "c";
            case QTY: return "qty";
            case ID: return "id";
            case CP: return "cp";
            default: return "?";
          }
    }


}

