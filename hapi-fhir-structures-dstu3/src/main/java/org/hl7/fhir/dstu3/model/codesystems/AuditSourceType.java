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

public enum AuditSourceType {

        /**
         * End-user display device, diagnostic device.
         */
        _1, 
        /**
         * Data acquisition device or instrument.
         */
        _2, 
        /**
         * Web Server process or thread.
         */
        _3, 
        /**
         * Application Server process or thread.
         */
        _4, 
        /**
         * Database Server process or thread.
         */
        _5, 
        /**
         * Security server, e.g. a domain controller.
         */
        _6, 
        /**
         * ISO level 1-3 network component.
         */
        _7, 
        /**
         * ISO level 4-6 operating software.
         */
        _8, 
        /**
         * other kind of device (defined by DICOM, but some other code/system can be used).
         */
        _9, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AuditSourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("6".equals(codeString))
          return _6;
        if ("7".equals(codeString))
          return _7;
        if ("8".equals(codeString))
          return _8;
        if ("9".equals(codeString))
          return _9;
        throw new FHIRException("Unknown AuditSourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _8: return "8";
            case _9: return "9";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/security-source-type";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "End-user display device, diagnostic device.";
            case _2: return "Data acquisition device or instrument.";
            case _3: return "Web Server process or thread.";
            case _4: return "Application Server process or thread.";
            case _5: return "Database Server process or thread.";
            case _6: return "Security server, e.g. a domain controller.";
            case _7: return "ISO level 1-3 network component.";
            case _8: return "ISO level 4-6 operating software.";
            case _9: return "other kind of device (defined by DICOM, but some other code/system can be used).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "User Device";
            case _2: return "Data Interface";
            case _3: return "Web Server";
            case _4: return "Application Server";
            case _5: return "Database Server";
            case _6: return "Security Server";
            case _7: return "Network Device";
            case _8: return "Network Router";
            case _9: return "Other";
            default: return "?";
          }
    }


}

