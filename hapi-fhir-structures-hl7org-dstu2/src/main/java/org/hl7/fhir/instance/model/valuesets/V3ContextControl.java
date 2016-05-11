package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3ContextControl {

        /**
         * The association adds to the existing context associated with the Act.  Both this association and any associations propagated from ancestor Acts are interpreted as being related to this Act.
         */
        _CONTEXTCONTROLADDITIVE, 
        /**
         * The association adds to the existing context associated with the Act, but will not propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as "Additive, Non-Propagating" it means that the author will be added to the set of author participations that have propagated from ancestor Acts for the purpose of this Act. However only the previously propagated authors will propagate to any child Acts that allow context to be propagated.
         */
        AN, 
        /**
         * The association adds to the existing context associated with the Act, and will propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as "Additive, Propagating" it means that the author will be added to the set of author participations that have propagated from ancestor Acts, and will itself propagate with the other authors to any child Acts that allow context to be propagated.
         */
        AP, 
        /**
         * The association applies only to the current Act and will not propagate to any child Acts that are related via a conducting ActRelationship (refer to contextConductionInd).
         */
        _CONTEXTCONTROLNONPROPAGATING, 
        /**
         * The association is added to the existing context associated with the Act, but overrides an association with the same typeCode. However, this overriding association will not propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as "Overriding, Non-Propagating" it means that the author will replace the set of author participations that have propagated from ancestor Acts. Furthermore, no author participations whatsoever will propagate to any child Acts that allow context to be propagated.
         */
        ON, 
        /**
         * The association adds to the existing context associated with the Act, but replaces associations propagated from ancestor Acts whose typeCodes are the same or more specific.
         */
        _CONTEXTCONTROLOVERRIDING, 
        /**
         * The association is added to the existing context associated with the Act, but overrides an association with the same typeCode. This overriding association will propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as "Overriding, Propagating" it means that the author will replace the set of author participations that have propagated from ancestor Acts, and will itself be the only author to propagate to any child Acts that allow context to be propagated.
         */
        OP, 
        /**
         * The association propagates to any child Acts that are related via a conducting ActRelationship (refer to contextConductionInd).
         */
        _CONTEXTCONTROLPROPAGATING, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ContextControl fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ContextControlAdditive".equals(codeString))
          return _CONTEXTCONTROLADDITIVE;
        if ("AN".equals(codeString))
          return AN;
        if ("AP".equals(codeString))
          return AP;
        if ("_ContextControlNonPropagating".equals(codeString))
          return _CONTEXTCONTROLNONPROPAGATING;
        if ("ON".equals(codeString))
          return ON;
        if ("_ContextControlOverriding".equals(codeString))
          return _CONTEXTCONTROLOVERRIDING;
        if ("OP".equals(codeString))
          return OP;
        if ("_ContextControlPropagating".equals(codeString))
          return _CONTEXTCONTROLPROPAGATING;
        throw new Exception("Unknown V3ContextControl code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _CONTEXTCONTROLADDITIVE: return "_ContextControlAdditive";
            case AN: return "AN";
            case AP: return "AP";
            case _CONTEXTCONTROLNONPROPAGATING: return "_ContextControlNonPropagating";
            case ON: return "ON";
            case _CONTEXTCONTROLOVERRIDING: return "_ContextControlOverriding";
            case OP: return "OP";
            case _CONTEXTCONTROLPROPAGATING: return "_ContextControlPropagating";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ContextControl";
        }
        public String getDefinition() {
          switch (this) {
            case _CONTEXTCONTROLADDITIVE: return "The association adds to the existing context associated with the Act.  Both this association and any associations propagated from ancestor Acts are interpreted as being related to this Act.";
            case AN: return "The association adds to the existing context associated with the Act, but will not propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as \"Additive, Non-Propagating\" it means that the author will be added to the set of author participations that have propagated from ancestor Acts for the purpose of this Act. However only the previously propagated authors will propagate to any child Acts that allow context to be propagated.";
            case AP: return "The association adds to the existing context associated with the Act, and will propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as \"Additive, Propagating\" it means that the author will be added to the set of author participations that have propagated from ancestor Acts, and will itself propagate with the other authors to any child Acts that allow context to be propagated.";
            case _CONTEXTCONTROLNONPROPAGATING: return "The association applies only to the current Act and will not propagate to any child Acts that are related via a conducting ActRelationship (refer to contextConductionInd).";
            case ON: return "The association is added to the existing context associated with the Act, but overrides an association with the same typeCode. However, this overriding association will not propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as \"Overriding, Non-Propagating\" it means that the author will replace the set of author participations that have propagated from ancestor Acts. Furthermore, no author participations whatsoever will propagate to any child Acts that allow context to be propagated.";
            case _CONTEXTCONTROLOVERRIDING: return "The association adds to the existing context associated with the Act, but replaces associations propagated from ancestor Acts whose typeCodes are the same or more specific.";
            case OP: return "The association is added to the existing context associated with the Act, but overrides an association with the same typeCode. This overriding association will propagate to any descendant Acts reached by conducting ActRelationships (see contextControlCode). Examples: If an 'Author' Participation were marked as \"Overriding, Propagating\" it means that the author will replace the set of author participations that have propagated from ancestor Acts, and will itself be the only author to propagate to any child Acts that allow context to be propagated.";
            case _CONTEXTCONTROLPROPAGATING: return "The association propagates to any child Acts that are related via a conducting ActRelationship (refer to contextConductionInd).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _CONTEXTCONTROLADDITIVE: return "ContextControlAdditive";
            case AN: return "additive, non-propagating";
            case AP: return "additive, propagating";
            case _CONTEXTCONTROLNONPROPAGATING: return "ContextControlNonPropagating";
            case ON: return "overriding, non-propagating";
            case _CONTEXTCONTROLOVERRIDING: return "ContextControlOverriding";
            case OP: return "overriding, propagating";
            case _CONTEXTCONTROLPROPAGATING: return "ContextControlPropagating";
            default: return "?";
          }
    }


}

