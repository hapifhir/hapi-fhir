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


public enum PatientMpiMatch {

        /**
         * This record meets the MPI criteria to be automatically considered as a full match.
         */
        CERTAIN, 
        /**
         * This record is a close match, but not a certain match. Additional review (e.g. by a human) may be required before using this as a match.
         */
        PROBABLE, 
        /**
         * This record may be a matching one. Additional review (e.g. by a human) SHOULD be performed before using this as a match.
         */
        POSSIBLE, 
        /**
         * This record is known not to be a match. Note that usually non-matching records are not returned, but in some cases records previously or likely considered as a match may specifically be negated by the MPI.
         */
        CERTAINLYNOT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PatientMpiMatch fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("certain".equals(codeString))
          return CERTAIN;
        if ("probable".equals(codeString))
          return PROBABLE;
        if ("possible".equals(codeString))
          return POSSIBLE;
        if ("certainly-not".equals(codeString))
          return CERTAINLYNOT;
        throw new Exception("Unknown PatientMpiMatch code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CERTAIN: return "certain";
            case PROBABLE: return "probable";
            case POSSIBLE: return "possible";
            case CERTAINLYNOT: return "certainly-not";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/patient-mpi-match";
        }
        public String getDefinition() {
          switch (this) {
            case CERTAIN: return "This record meets the MPI criteria to be automatically considered as a full match.";
            case PROBABLE: return "This record is a close match, but not a certain match. Additional review (e.g. by a human) may be required before using this as a match.";
            case POSSIBLE: return "This record may be a matching one. Additional review (e.g. by a human) SHOULD be performed before using this as a match.";
            case CERTAINLYNOT: return "This record is known not to be a match. Note that usually non-matching records are not returned, but in some cases records previously or likely considered as a match may specifically be negated by the MPI.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CERTAIN: return "Certain Match";
            case PROBABLE: return "Probable Match";
            case POSSIBLE: return "Possible Match";
            case CERTAINLYNOT: return "Certainly Not a Match";
            default: return "?";
          }
    }


}

