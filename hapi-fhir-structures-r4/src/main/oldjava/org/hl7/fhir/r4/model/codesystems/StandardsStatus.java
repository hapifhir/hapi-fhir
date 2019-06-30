package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum StandardsStatus {

        /**
         * This portion of the specification is not considered to be complete enough or sufficiently reviewed to be safe for implementation. It may have known issues or still be in the "in development" stage. It is included in the publication as a place-holder, to solicit feedback from the implementation community and/or to give implementers some insight as to functionality likely to be included in future versions of the specification. Content at this level should only be implemented by the brave or desperate and is very much "use at your own risk". The content that is Draft that will usually be elevated to Trial Use once review and correction is complete after it has been subjected to ballot.
         */
        DRAFT, 
        /**
         * This content has been subject to review and production implementation in a wide variety of environments. The content is considered to be stable and has been 'locked', subjecting it to FHIR Inter-version Compatibility Rules. While changes are possible, they are expected to be infrequent and are tightly constrained. Compatibility Rules.
         */
        NORMATIVE, 
        /**
         * This content has been well reviewed and is considered by the authors to be ready for use in production systems. It has been subjected to ballot and approved as an official standard. However, it has not yet seen widespread use in production across the full spectrum of environments it is intended to be used in. In some cases, there may be documented known issues that require implementation experience to determine appropriate resolutions for.

Future versions of FHIR may make significant changes to Trial Use content that are not compatible with previously published content.
         */
        TRIALUSE, 
        /**
         * This portion of the specification is provided for implementer assistance, and does not make rules that implementers are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, examples, and implementer advice.
         */
        INFORMATIVE, 
        /**
         * This portion of the specification is provided for implementer assistance, and does not make rules that implementers are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, examples, and implementer advice.
         */
        DEPRECATED, 
        /**
         * This is content that is managed outside the FHIR Specification, but included for implementer convenience.
         */
        EXTERNAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StandardsStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("normative".equals(codeString))
          return NORMATIVE;
        if ("trial-use".equals(codeString))
          return TRIALUSE;
        if ("informative".equals(codeString))
          return INFORMATIVE;
        if ("deprecated".equals(codeString))
          return DEPRECATED;
        if ("external".equals(codeString))
          return EXTERNAL;
        throw new FHIRException("Unknown StandardsStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case NORMATIVE: return "normative";
            case TRIALUSE: return "trial-use";
            case INFORMATIVE: return "informative";
            case DEPRECATED: return "deprecated";
            case EXTERNAL: return "external";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/standards-status";
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "This portion of the specification is not considered to be complete enough or sufficiently reviewed to be safe for implementation. It may have known issues or still be in the \"in development\" stage. It is included in the publication as a place-holder, to solicit feedback from the implementation community and/or to give implementers some insight as to functionality likely to be included in future versions of the specification. Content at this level should only be implemented by the brave or desperate and is very much \"use at your own risk\". The content that is Draft that will usually be elevated to Trial Use once review and correction is complete after it has been subjected to ballot.";
            case NORMATIVE: return "This content has been subject to review and production implementation in a wide variety of environments. The content is considered to be stable and has been 'locked', subjecting it to FHIR Inter-version Compatibility Rules. While changes are possible, they are expected to be infrequent and are tightly constrained. Compatibility Rules.";
            case TRIALUSE: return "This content has been well reviewed and is considered by the authors to be ready for use in production systems. It has been subjected to ballot and approved as an official standard. However, it has not yet seen widespread use in production across the full spectrum of environments it is intended to be used in. In some cases, there may be documented known issues that require implementation experience to determine appropriate resolutions for.\n\nFuture versions of FHIR may make significant changes to Trial Use content that are not compatible with previously published content.";
            case INFORMATIVE: return "This portion of the specification is provided for implementer assistance, and does not make rules that implementers are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, examples, and implementer advice.";
            case DEPRECATED: return "This portion of the specification is provided for implementer assistance, and does not make rules that implementers are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, examples, and implementer advice.";
            case EXTERNAL: return "This is content that is managed outside the FHIR Specification, but included for implementer convenience.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case NORMATIVE: return "Normative";
            case TRIALUSE: return "Trial-Use";
            case INFORMATIVE: return "Informative";
            case DEPRECATED: return "Deprecated";
            case EXTERNAL: return "External";
            default: return "?";
          }
    }


}

