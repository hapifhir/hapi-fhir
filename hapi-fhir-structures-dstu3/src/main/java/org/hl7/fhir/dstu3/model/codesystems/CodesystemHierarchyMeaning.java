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

public enum CodesystemHierarchyMeaning {

        /**
         * No particular relationship between the concepts can be assumed, except what can be determined by inspection of the definitions of the elements (possible reasons to use this: importing from a source where this is not defined, or where various parts of the hierarchy have different meanings)
         */
        GROUPEDBY, 
        /**
         * A hierarchy where the child concepts have an IS-A relationship with the parents - that is, all the properties of the parent are also true for its child concepts
         */
        ISA, 
        /**
         * Child elements list the individual parts of a composite whole (e.g. body site)
         */
        PARTOF, 
        /**
         * Child concepts in the hierarchy may have only one parent, and there is a presumption that the code system is a "closed world" meaning all things must be in the hierarchy. This results in concepts such as "not otherwise classified."
         */
        CLASSIFIEDWITH, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CodesystemHierarchyMeaning fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("grouped-by".equals(codeString))
          return GROUPEDBY;
        if ("is-a".equals(codeString))
          return ISA;
        if ("part-of".equals(codeString))
          return PARTOF;
        if ("classified-with".equals(codeString))
          return CLASSIFIEDWITH;
        throw new FHIRException("Unknown CodesystemHierarchyMeaning code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GROUPEDBY: return "grouped-by";
            case ISA: return "is-a";
            case PARTOF: return "part-of";
            case CLASSIFIEDWITH: return "classified-with";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/codesystem-hierarchy-meaning";
        }
        public String getDefinition() {
          switch (this) {
            case GROUPEDBY: return "No particular relationship between the concepts can be assumed, except what can be determined by inspection of the definitions of the elements (possible reasons to use this: importing from a source where this is not defined, or where various parts of the hierarchy have different meanings)";
            case ISA: return "A hierarchy where the child concepts have an IS-A relationship with the parents - that is, all the properties of the parent are also true for its child concepts";
            case PARTOF: return "Child elements list the individual parts of a composite whole (e.g. body site)";
            case CLASSIFIEDWITH: return "Child concepts in the hierarchy may have only one parent, and there is a presumption that the code system is a \"closed world\" meaning all things must be in the hierarchy. This results in concepts such as \"not otherwise classified.\"";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GROUPEDBY: return "Grouped By";
            case ISA: return "Is-A";
            case PARTOF: return "Part Of";
            case CLASSIFIEDWITH: return "Classified With";
            default: return "?";
          }
    }


}

