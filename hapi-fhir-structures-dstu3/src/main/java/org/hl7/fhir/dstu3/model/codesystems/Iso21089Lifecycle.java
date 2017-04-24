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

public enum Iso21089Lifecycle {

        /**
         * occurs when an agent makes any change to record entry content currently residing in storage considered permanent (persistent)
         */
        _2, 
        /**
         * occurs when an agent causes the system to create and move archive artifacts containing record entry content, typically to long-term offline storage
         */
        _14, 
        /**
         * occurs when an agent causes the system to capture the agentâ€™s digital signature (or equivalent indication) during formal validation of record entry content
         */
        _4, 
        /**
         * occurs when an agent causes the system to decode record entry content from a cipher
         */
        _27, 
        /**
         * occurs when an agent causes the system to scrub record entry content to reduce the association between a set of identifying data and the data subject in a way that may or may not be reversible
         */
        _10, 
        /**
         * occurs when an agent causes the system to tag record entry(ies) as obsolete, erroneous or untrustworthy, to warn against its future use
         */
        _17, 
        /**
         * occurs when an agent causes the system to permanently erase record entry content from the system
         */
        _16, 
        /**
         * occurs when an agent causes the system to release, transfer, provision access to, or otherwise divulge record entry content
         */
        _7, 
        /**
         * occurs when an agent causes the system to encode record entry content in a cipher
         */
        _26, 
        /**
         * occurs when an agent causes the system to selectively pull out a subset of record entry content, based on explicit criteria
         */
        _13, 
        /**
         * occurs when an agent causes the system to connect related record entries
         */
        _21, 
        /**
         * occurs when an agent causes the system to combine or join content from two or more record entries, resulting in a single logical record entry
         */
        _19, 
        /**
         * occurs when an agent causes the system to:  a) initiate capture of potential record content, and b) incorporate that content into the storage considered a permanent part of the health record
         */
        _1, 
        /**
         * occurs when an agent causes the system to remove record entry content to reduce the association between a set of identifying data and the data subject in a way that may be reversible
         */
        _11, 
        /**
         * occurs when an agent causes the system to recreate or restore full status to record entries previously deleted or deprecated
         */
        _18, 
        /**
         * occurs when an agent causes the system to:  a) initiate capture of data content from elseware, and b) incorporate that content into the storage considered a permanent part of the health record
         */
        _9, 
        /**
         * occurs when an agent causes the system to produce and deliver record entry content in a particular form and manner
         */
        _6, 
        /**
         * occurs when an agent causes the system to restore information to data that allows identification of information source and/or information subject
         */
        _12, 
        /**
         * occurs when an agent causes the system to remove a tag or other cues for special access management had required to fulfill organizational policy under the legal doctrine of â€œduty to preserveâ€
         */
        _24, 
        /**
         * occurs when an agent causes the system to recreate record entries and their content from a previous created archive artifact
         */
        _15, 
        /**
         * occurs when an agent causes the system to change the form, language or code system used to represent record entry content
         */
        _3, 
        /**
         * occurs when an agent causes the system to send record entry content from one (EHR/PHR/other) system to another
         */
        _8, 
        /**
         * occurs when an agent causes the system to disconnect two or more record entries previously connected, rendering them separate (disconnected) again
         */
        _22, 
        /**
         * occurs when an agent causes the system to reverse a previous record entry merge operation, rendering them separate again
         */
        _20, 
        /**
         * occurs when an agent causes the system to confirm compliance of data or data objects with regulations, requirements, specifications, or other imposed conditions based on organizational policy
         */
        _25, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Iso21089Lifecycle fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("2".equals(codeString))
          return _2;
        if ("14".equals(codeString))
          return _14;
        if ("4".equals(codeString))
          return _4;
        if ("27".equals(codeString))
          return _27;
        if ("10".equals(codeString))
          return _10;
        if ("17".equals(codeString))
          return _17;
        if ("16".equals(codeString))
          return _16;
        if ("7".equals(codeString))
          return _7;
        if ("26".equals(codeString))
          return _26;
        if ("13".equals(codeString))
          return _13;
        if ("21".equals(codeString))
          return _21;
        if ("19".equals(codeString))
          return _19;
        if ("1".equals(codeString))
          return _1;
        if ("11".equals(codeString))
          return _11;
        if ("18".equals(codeString))
          return _18;
        if ("9".equals(codeString))
          return _9;
        if ("6".equals(codeString))
          return _6;
        if ("12".equals(codeString))
          return _12;
        if ("24".equals(codeString))
          return _24;
        if ("15".equals(codeString))
          return _15;
        if ("3".equals(codeString))
          return _3;
        if ("8".equals(codeString))
          return _8;
        if ("22".equals(codeString))
          return _22;
        if ("20".equals(codeString))
          return _20;
        if ("25".equals(codeString))
          return _25;
        throw new FHIRException("Unknown Iso21089Lifecycle code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _2: return "2";
            case _14: return "14";
            case _4: return "4";
            case _27: return "27";
            case _10: return "10";
            case _17: return "17";
            case _16: return "16";
            case _7: return "7";
            case _26: return "26";
            case _13: return "13";
            case _21: return "21";
            case _19: return "19";
            case _1: return "1";
            case _11: return "11";
            case _18: return "18";
            case _9: return "9";
            case _6: return "6";
            case _12: return "12";
            case _24: return "24";
            case _15: return "15";
            case _3: return "3";
            case _8: return "8";
            case _22: return "22";
            case _20: return "20";
            case _25: return "25";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/iso-21089-lifecycle";
        }
        public String getDefinition() {
          switch (this) {
            case _2: return "occurs when an agent makes any change to record entry content currently residing in storage considered permanent (persistent)";
            case _14: return "occurs when an agent causes the system to create and move archive artifacts containing record entry content, typically to long-term offline storage";
            case _4: return "occurs when an agent causes the system to capture the agentâ€™s digital signature (or equivalent indication) during formal validation of record entry content";
            case _27: return "occurs when an agent causes the system to decode record entry content from a cipher";
            case _10: return "occurs when an agent causes the system to scrub record entry content to reduce the association between a set of identifying data and the data subject in a way that may or may not be reversible";
            case _17: return "occurs when an agent causes the system to tag record entry(ies) as obsolete, erroneous or untrustworthy, to warn against its future use";
            case _16: return "occurs when an agent causes the system to permanently erase record entry content from the system";
            case _7: return "occurs when an agent causes the system to release, transfer, provision access to, or otherwise divulge record entry content";
            case _26: return "occurs when an agent causes the system to encode record entry content in a cipher";
            case _13: return "occurs when an agent causes the system to selectively pull out a subset of record entry content, based on explicit criteria";
            case _21: return "occurs when an agent causes the system to connect related record entries";
            case _19: return "occurs when an agent causes the system to combine or join content from two or more record entries, resulting in a single logical record entry";
            case _1: return "occurs when an agent causes the system to:  a) initiate capture of potential record content, and b) incorporate that content into the storage considered a permanent part of the health record";
            case _11: return "occurs when an agent causes the system to remove record entry content to reduce the association between a set of identifying data and the data subject in a way that may be reversible";
            case _18: return "occurs when an agent causes the system to recreate or restore full status to record entries previously deleted or deprecated";
            case _9: return "occurs when an agent causes the system to:  a) initiate capture of data content from elseware, and b) incorporate that content into the storage considered a permanent part of the health record";
            case _6: return "occurs when an agent causes the system to produce and deliver record entry content in a particular form and manner";
            case _12: return "occurs when an agent causes the system to restore information to data that allows identification of information source and/or information subject";
            case _24: return "occurs when an agent causes the system to remove a tag or other cues for special access management had required to fulfill organizational policy under the legal doctrine of â€œduty to preserveâ€";
            case _15: return "occurs when an agent causes the system to recreate record entries and their content from a previous created archive artifact";
            case _3: return "occurs when an agent causes the system to change the form, language or code system used to represent record entry content";
            case _8: return "occurs when an agent causes the system to send record entry content from one (EHR/PHR/other) system to another";
            case _22: return "occurs when an agent causes the system to disconnect two or more record entries previously connected, rendering them separate (disconnected) again";
            case _20: return "occurs when an agent causes the system to reverse a previous record entry merge operation, rendering them separate again";
            case _25: return "occurs when an agent causes the system to confirm compliance of data or data objects with regulations, requirements, specifications, or other imposed conditions based on organizational policy";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _2: return "Amend (Update) - Lifeycle Event";
            case _14: return "Archive - Lifeycle Event";
            case _4: return "Attest - Lifecycle Event";
            case _27: return "Decrypt - Lifecycle Event";
            case _10: return "De-Identify (Anononymize) - Lifecycle Event";
            case _17: return "Deprecate - Lifecycle Event";
            case _16: return "Destroy/Delete - Lifecycle Event";
            case _7: return "Disclose - Lifecycle Event";
            case _26: return "Encrypt - Lifecycle Event";
            case _13: return "Extract - Lifecycle Event";
            case _21: return "Link - Lifecycle Event";
            case _19: return "Merge - Lifecycle Event";
            case _1: return "Originate/Retain - Record Lifecyle Event";
            case _11: return "Pseudonymize - Lifecycle Event";
            case _18: return "Re-activate - Lifecycle Event";
            case _9: return "Receive/Retain - Lifecycle Event";
            case _6: return "Report (Output) - Lifecycle Event";
            case _12: return "Re-identify - Lifecycle Event";
            case _24: return "Remove Legal Hold - Lifecycle Event";
            case _15: return "Restore - Lifecycle Event";
            case _3: return "Transform/Translate - Lifecycle Event";
            case _8: return "Transmit - Lifecycle Event";
            case _22: return "Unlink - Lifecycle Event";
            case _20: return "Unmerge - Lifecycle Event";
            case _25: return "Verify - Lifecycle Event";
            default: return "?";
          }
    }


}

