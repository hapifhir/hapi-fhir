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

public enum V3DocumentCompletion {

        /**
         * A completion status in which a document has been signed manually or electronically by one or more individuals who attest to its accuracy.  No explicit determination is made that the assigned individual has performed the authentication.  While the standard allows multiple instances of authentication, it would be typical to have a single instance of authentication, usually by the assigned individual.
         */
        AU, 
        /**
         * A completion status in which information has been orally recorded but not yet transcribed.
         */
        DI, 
        /**
         * A completion status in which document content, other than dictation, has been received but has not been translated into the final electronic format.  Examples include paper documents, whether hand-written or typewritten, and intermediate electronic forms, such as voice to text.
         */
        DO, 
        /**
         * A completion status in which information is known to be missing from a transcribed document.
         */
        IN, 
        /**
         * A workflow status where the material has been assigned to personnel to perform the task of transcription. The document remains in this state until the document is transcribed.
         */
        IP, 
        /**
         * A completion status in which a document has been signed manually or electronically by the individual who is legally responsible for that document. This is the most mature state in the workflow progression.
         */
        LA, 
        /**
         * A completion status in which a document was created in error or was placed in the wrong chart. The document is no longer available.
         */
        NU, 
        /**
         * A completion status in which a document is transcribed but not authenticated.
         */
        PA, 
        /**
         * A completion status where the document is complete and there is no expectation that the document will be signed.
         */
        UC, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3DocumentCompletion fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AU".equals(codeString))
          return AU;
        if ("DI".equals(codeString))
          return DI;
        if ("DO".equals(codeString))
          return DO;
        if ("IN".equals(codeString))
          return IN;
        if ("IP".equals(codeString))
          return IP;
        if ("LA".equals(codeString))
          return LA;
        if ("NU".equals(codeString))
          return NU;
        if ("PA".equals(codeString))
          return PA;
        if ("UC".equals(codeString))
          return UC;
        throw new FHIRException("Unknown V3DocumentCompletion code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AU: return "AU";
            case DI: return "DI";
            case DO: return "DO";
            case IN: return "IN";
            case IP: return "IP";
            case LA: return "LA";
            case NU: return "NU";
            case PA: return "PA";
            case UC: return "UC";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/DocumentCompletion";
        }
        public String getDefinition() {
          switch (this) {
            case AU: return "A completion status in which a document has been signed manually or electronically by one or more individuals who attest to its accuracy.  No explicit determination is made that the assigned individual has performed the authentication.  While the standard allows multiple instances of authentication, it would be typical to have a single instance of authentication, usually by the assigned individual.";
            case DI: return "A completion status in which information has been orally recorded but not yet transcribed.";
            case DO: return "A completion status in which document content, other than dictation, has been received but has not been translated into the final electronic format.  Examples include paper documents, whether hand-written or typewritten, and intermediate electronic forms, such as voice to text.";
            case IN: return "A completion status in which information is known to be missing from a transcribed document.";
            case IP: return "A workflow status where the material has been assigned to personnel to perform the task of transcription. The document remains in this state until the document is transcribed.";
            case LA: return "A completion status in which a document has been signed manually or electronically by the individual who is legally responsible for that document. This is the most mature state in the workflow progression.";
            case NU: return "A completion status in which a document was created in error or was placed in the wrong chart. The document is no longer available.";
            case PA: return "A completion status in which a document is transcribed but not authenticated.";
            case UC: return "A completion status where the document is complete and there is no expectation that the document will be signed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AU: return "authenticated";
            case DI: return "dictated";
            case DO: return "documented";
            case IN: return "incomplete";
            case IP: return "in progress";
            case LA: return "legally authenticated";
            case NU: return "nullified document";
            case PA: return "pre-authenticated";
            case UC: return "unsigned completed document";
            default: return "?";
          }
    }


}

