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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3ParticipationMode {

        /**
         * Participation by non-human-languaged based electronic signal
         */
        ELECTRONIC, 
        /**
         * Participation by direct action where subject and actor are in the same location. (The participation involves more than communication.)
         */
        PHYSICAL, 
        /**
         * Participation by direct action where subject and actor are in separate locations, and the actions of the actor are transmitted by electronic or mechanical means. (The participation involves more than communication.)
         */
        REMOTE, 
        /**
         * Participation by voice communication
         */
        VERBAL, 
        /**
         * Participation by pre-recorded voice.  Communication is limited to one direction (from the recorder to recipient).
         */
        DICTATE, 
        /**
         * Participation by voice communication where parties speak to each other directly.
         */
        FACE, 
        /**
         * Participation by voice communication where the voices of the communicating parties are transported over an electronic medium
         */
        PHONE, 
        /**
         * Participation by voice and visual communication where the voices and images of the communicating parties are transported over an electronic medium
         */
        VIDEOCONF, 
        /**
         * Participation by human language recorded on a physical material
         */
        WRITTEN, 
        /**
         * Participation by text or diagrams printed on paper that have been transmitted over a fax device
         */
        FAXWRIT, 
        /**
         * Participation by text or diagrams printed on paper or other recording medium
         */
        HANDWRIT, 
        /**
         * Participation by text or diagrams printed on paper transmitted physically (e.g. by courier service, postal service).
         */
        MAILWRIT, 
        /**
         * Participation by text or diagrams submitted by computer network, e.g. online survey.
         */
        ONLINEWRIT, 
        /**
         * Participation by text or diagrams transmitted over an electronic mail system.
         */
        EMAILWRIT, 
        /**
         * Participation by text or diagrams printed on paper or other recording medium where the recording was performed using a typewriter, typesetter, computer or similar mechanism.
         */
        TYPEWRIT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ParticipationMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ELECTRONIC".equals(codeString))
          return ELECTRONIC;
        if ("PHYSICAL".equals(codeString))
          return PHYSICAL;
        if ("REMOTE".equals(codeString))
          return REMOTE;
        if ("VERBAL".equals(codeString))
          return VERBAL;
        if ("DICTATE".equals(codeString))
          return DICTATE;
        if ("FACE".equals(codeString))
          return FACE;
        if ("PHONE".equals(codeString))
          return PHONE;
        if ("VIDEOCONF".equals(codeString))
          return VIDEOCONF;
        if ("WRITTEN".equals(codeString))
          return WRITTEN;
        if ("FAXWRIT".equals(codeString))
          return FAXWRIT;
        if ("HANDWRIT".equals(codeString))
          return HANDWRIT;
        if ("MAILWRIT".equals(codeString))
          return MAILWRIT;
        if ("ONLINEWRIT".equals(codeString))
          return ONLINEWRIT;
        if ("EMAILWRIT".equals(codeString))
          return EMAILWRIT;
        if ("TYPEWRIT".equals(codeString))
          return TYPEWRIT;
        throw new FHIRException("Unknown V3ParticipationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ELECTRONIC: return "ELECTRONIC";
            case PHYSICAL: return "PHYSICAL";
            case REMOTE: return "REMOTE";
            case VERBAL: return "VERBAL";
            case DICTATE: return "DICTATE";
            case FACE: return "FACE";
            case PHONE: return "PHONE";
            case VIDEOCONF: return "VIDEOCONF";
            case WRITTEN: return "WRITTEN";
            case FAXWRIT: return "FAXWRIT";
            case HANDWRIT: return "HANDWRIT";
            case MAILWRIT: return "MAILWRIT";
            case ONLINEWRIT: return "ONLINEWRIT";
            case EMAILWRIT: return "EMAILWRIT";
            case TYPEWRIT: return "TYPEWRIT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-ParticipationMode";
        }
        public String getDefinition() {
          switch (this) {
            case ELECTRONIC: return "Participation by non-human-languaged based electronic signal";
            case PHYSICAL: return "Participation by direct action where subject and actor are in the same location. (The participation involves more than communication.)";
            case REMOTE: return "Participation by direct action where subject and actor are in separate locations, and the actions of the actor are transmitted by electronic or mechanical means. (The participation involves more than communication.)";
            case VERBAL: return "Participation by voice communication";
            case DICTATE: return "Participation by pre-recorded voice.  Communication is limited to one direction (from the recorder to recipient).";
            case FACE: return "Participation by voice communication where parties speak to each other directly.";
            case PHONE: return "Participation by voice communication where the voices of the communicating parties are transported over an electronic medium";
            case VIDEOCONF: return "Participation by voice and visual communication where the voices and images of the communicating parties are transported over an electronic medium";
            case WRITTEN: return "Participation by human language recorded on a physical material";
            case FAXWRIT: return "Participation by text or diagrams printed on paper that have been transmitted over a fax device";
            case HANDWRIT: return "Participation by text or diagrams printed on paper or other recording medium";
            case MAILWRIT: return "Participation by text or diagrams printed on paper transmitted physically (e.g. by courier service, postal service).";
            case ONLINEWRIT: return "Participation by text or diagrams submitted by computer network, e.g. online survey.";
            case EMAILWRIT: return "Participation by text or diagrams transmitted over an electronic mail system.";
            case TYPEWRIT: return "Participation by text or diagrams printed on paper or other recording medium where the recording was performed using a typewriter, typesetter, computer or similar mechanism.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ELECTRONIC: return "electronic data";
            case PHYSICAL: return "physical presence";
            case REMOTE: return "remote presence";
            case VERBAL: return "verbal";
            case DICTATE: return "dictated";
            case FACE: return "face-to-face";
            case PHONE: return "telephone";
            case VIDEOCONF: return "videoconferencing";
            case WRITTEN: return "written";
            case FAXWRIT: return "telefax";
            case HANDWRIT: return "handwritten";
            case MAILWRIT: return "mail";
            case ONLINEWRIT: return "online written";
            case EMAILWRIT: return "email";
            case TYPEWRIT: return "typewritten";
            default: return "?";
          }
    }


}

