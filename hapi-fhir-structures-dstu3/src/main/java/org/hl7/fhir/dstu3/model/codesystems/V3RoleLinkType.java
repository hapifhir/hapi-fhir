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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3RoleLinkType {

        /**
         * An action taken with respect to a subject Entity by a regulatory or authoritative body with supervisory capacity over that entity. The action is taken in response to behavior by the subject Entity that body finds to be undesirable.

                        Suspension, license restrictions, monetary fine, letter of reprimand, mandated training, mandated supervision, etc.Examples:
         */
        REL, 
        /**
         * This relationship indicates the source Role is available to the target Role as a backup. An entity in a backup role will be available as a substitute or replacement in the event that the entity assigned the role is unavailable. In medical roles where it is critical that the function be performed and there is a possibility that the individual assigned may be ill or otherwise indisposed, another individual is assigned to cover for the individual originally assigned the role. A backup may be required to be identified, but unless the backup is actually used, he/she would not assume the assigned entity role.
         */
        BACKUP, 
        /**
         * This relationship indicates the target Role provides or receives information regarding the target role.  For example, AssignedEntity is a contact for a ServiceDeliveryLocation.
         */
        CONT, 
        /**
         * The source Role has direct authority over the target role in a chain of authority.
         */
        DIRAUTH, 
        /**
         * Description: The source role provides identification for the target role. The source role must be IDENT. The player entity of the source role is constrained to be the same as (i.e. the equivalent of, or equal to) the player of the target role if present. If the player is absent from the source role, then it is assumed to be the same as the player of the target role.
         */
        IDENT, 
        /**
         * The source Role has indirect authority over the target role in a chain of authority.
         */
        INDAUTH, 
        /**
         * The target Role is part of the Source Role.
         */
        PART, 
        /**
         * This relationship indicates that the source Role replaces (or subsumes) the target Role.  Allows for new identifiers and/or new effective time for a registry entry or a certification, etc.
         */
        REPL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RoleLinkType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("REL".equals(codeString))
          return REL;
        if ("BACKUP".equals(codeString))
          return BACKUP;
        if ("CONT".equals(codeString))
          return CONT;
        if ("DIRAUTH".equals(codeString))
          return DIRAUTH;
        if ("IDENT".equals(codeString))
          return IDENT;
        if ("INDAUTH".equals(codeString))
          return INDAUTH;
        if ("PART".equals(codeString))
          return PART;
        if ("REPL".equals(codeString))
          return REPL;
        throw new FHIRException("Unknown V3RoleLinkType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REL: return "REL";
            case BACKUP: return "BACKUP";
            case CONT: return "CONT";
            case DIRAUTH: return "DIRAUTH";
            case IDENT: return "IDENT";
            case INDAUTH: return "INDAUTH";
            case PART: return "PART";
            case REPL: return "REPL";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RoleLinkType";
        }
        public String getDefinition() {
          switch (this) {
            case REL: return "An action taken with respect to a subject Entity by a regulatory or authoritative body with supervisory capacity over that entity. The action is taken in response to behavior by the subject Entity that body finds to be undesirable.\r\n\n                        Suspension, license restrictions, monetary fine, letter of reprimand, mandated training, mandated supervision, etc.Examples:";
            case BACKUP: return "This relationship indicates the source Role is available to the target Role as a backup. An entity in a backup role will be available as a substitute or replacement in the event that the entity assigned the role is unavailable. In medical roles where it is critical that the function be performed and there is a possibility that the individual assigned may be ill or otherwise indisposed, another individual is assigned to cover for the individual originally assigned the role. A backup may be required to be identified, but unless the backup is actually used, he/she would not assume the assigned entity role.";
            case CONT: return "This relationship indicates the target Role provides or receives information regarding the target role.  For example, AssignedEntity is a contact for a ServiceDeliveryLocation.";
            case DIRAUTH: return "The source Role has direct authority over the target role in a chain of authority.";
            case IDENT: return "Description: The source role provides identification for the target role. The source role must be IDENT. The player entity of the source role is constrained to be the same as (i.e. the equivalent of, or equal to) the player of the target role if present. If the player is absent from the source role, then it is assumed to be the same as the player of the target role.";
            case INDAUTH: return "The source Role has indirect authority over the target role in a chain of authority.";
            case PART: return "The target Role is part of the Source Role.";
            case REPL: return "This relationship indicates that the source Role replaces (or subsumes) the target Role.  Allows for new identifiers and/or new effective time for a registry entry or a certification, etc.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REL: return "related";
            case BACKUP: return "is backup for";
            case CONT: return "has contact";
            case DIRAUTH: return "has direct authority over";
            case IDENT: return "Identification";
            case INDAUTH: return "has indirect authority over";
            case PART: return "has part";
            case REPL: return "replaces";
            default: return "?";
          }
    }


}

