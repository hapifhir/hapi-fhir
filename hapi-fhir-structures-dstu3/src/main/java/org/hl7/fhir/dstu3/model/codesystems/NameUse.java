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

public enum NameUse {

        /**
         * Known as/conventional/the one you normally use
         */
        USUAL, 
        /**
         * The formal name as registered in an official (government) registry, but which name might not be commonly used. May be called "legal name".
         */
        OFFICIAL, 
        /**
         * A temporary name. Name.period can provide more detailed information. This may also be used for temporary names assigned at birth or in emergency situations.
         */
        TEMP, 
        /**
         * A name that is used to address the person in an informal manner, but is not part of their formal or usual name
         */
        NICKNAME, 
        /**
         * Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons)
         */
        ANONYMOUS, 
        /**
         * This name is no longer in use (or was never correct, but retained for records)
         */
        OLD, 
        /**
         * A name used prior to changing name because of marriage. This name use is for use by applications that collect and store names that were used prior to a marriage. Marriage naming customs vary greatly around the world, and are constantly changing. This term is not gender specific. The use of this term does not imply any particular history for a person's name
         */
        MAIDEN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NameUse fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("usual".equals(codeString))
          return USUAL;
        if ("official".equals(codeString))
          return OFFICIAL;
        if ("temp".equals(codeString))
          return TEMP;
        if ("nickname".equals(codeString))
          return NICKNAME;
        if ("anonymous".equals(codeString))
          return ANONYMOUS;
        if ("old".equals(codeString))
          return OLD;
        if ("maiden".equals(codeString))
          return MAIDEN;
        throw new FHIRException("Unknown NameUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case USUAL: return "usual";
            case OFFICIAL: return "official";
            case TEMP: return "temp";
            case NICKNAME: return "nickname";
            case ANONYMOUS: return "anonymous";
            case OLD: return "old";
            case MAIDEN: return "maiden";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/name-use";
        }
        public String getDefinition() {
          switch (this) {
            case USUAL: return "Known as/conventional/the one you normally use";
            case OFFICIAL: return "The formal name as registered in an official (government) registry, but which name might not be commonly used. May be called \"legal name\".";
            case TEMP: return "A temporary name. Name.period can provide more detailed information. This may also be used for temporary names assigned at birth or in emergency situations.";
            case NICKNAME: return "A name that is used to address the person in an informal manner, but is not part of their formal or usual name";
            case ANONYMOUS: return "Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons)";
            case OLD: return "This name is no longer in use (or was never correct, but retained for records)";
            case MAIDEN: return "A name used prior to changing name because of marriage. This name use is for use by applications that collect and store names that were used prior to a marriage. Marriage naming customs vary greatly around the world, and are constantly changing. This term is not gender specific. The use of this term does not imply any particular history for a person's name";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case USUAL: return "Usual";
            case OFFICIAL: return "Official";
            case TEMP: return "Temp";
            case NICKNAME: return "Nickname";
            case ANONYMOUS: return "Anonymous";
            case OLD: return "Old";
            case MAIDEN: return "Name changed for Marriage";
            default: return "?";
          }
    }


}

