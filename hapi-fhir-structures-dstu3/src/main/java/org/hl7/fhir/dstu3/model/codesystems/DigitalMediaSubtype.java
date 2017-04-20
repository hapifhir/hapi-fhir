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

public enum DigitalMediaSubtype {

        /**
         * A diagram. Often used in diagnostic reports
         */
        DIAGRAM, 
        /**
         * A digital record of a fax document
         */
        FAX, 
        /**
         * A digital scan of a document. This is reserved for when there is not enough metadata to create a document reference
         */
        SCAN, 
        /**
         * A retinal image used for identification purposes
         */
        RETINA, 
        /**
         * A finger print scan used for identification purposes
         */
        FINGERPRINT, 
        /**
         * An iris scan used for identification purposes
         */
        IRIS, 
        /**
         * A palm scan used for identification purposes
         */
        PALM, 
        /**
         * A face scan used for identification purposes
         */
        FACE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DigitalMediaSubtype fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("diagram".equals(codeString))
          return DIAGRAM;
        if ("fax".equals(codeString))
          return FAX;
        if ("scan".equals(codeString))
          return SCAN;
        if ("retina".equals(codeString))
          return RETINA;
        if ("fingerprint".equals(codeString))
          return FINGERPRINT;
        if ("iris".equals(codeString))
          return IRIS;
        if ("palm".equals(codeString))
          return PALM;
        if ("face".equals(codeString))
          return FACE;
        throw new FHIRException("Unknown DigitalMediaSubtype code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIAGRAM: return "diagram";
            case FAX: return "fax";
            case SCAN: return "scan";
            case RETINA: return "retina";
            case FINGERPRINT: return "fingerprint";
            case IRIS: return "iris";
            case PALM: return "palm";
            case FACE: return "face";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/media-subtype";
        }
        public String getDefinition() {
          switch (this) {
            case DIAGRAM: return "A diagram. Often used in diagnostic reports";
            case FAX: return "A digital record of a fax document";
            case SCAN: return "A digital scan of a document. This is reserved for when there is not enough metadata to create a document reference";
            case RETINA: return "A retinal image used for identification purposes";
            case FINGERPRINT: return "A finger print scan used for identification purposes";
            case IRIS: return "An iris scan used for identification purposes";
            case PALM: return "A palm scan used for identification purposes";
            case FACE: return "A face scan used for identification purposes";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIAGRAM: return "Diagram";
            case FAX: return "Fax";
            case SCAN: return "Scanned Document";
            case RETINA: return "Retina scan";
            case FINGERPRINT: return "Fingerprint";
            case IRIS: return "Iris";
            case PALM: return "Palm";
            case FACE: return "Face";
            default: return "?";
          }
    }


}

