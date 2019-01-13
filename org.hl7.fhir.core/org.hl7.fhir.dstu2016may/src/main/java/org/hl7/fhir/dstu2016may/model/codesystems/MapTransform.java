package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


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
         * something
         */
        DATEOP, 
        /**
         * something
         */
        UUID, 
        /**
         * something
         */
        POINTER, 
        /**
         * something
         */
        EVALUATE, 
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
            case DATEOP: return "something";
            case UUID: return "something";
            case POINTER: return "something";
            case EVALUATE: return "something";
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
            default: return "?";
          }
    }


}

