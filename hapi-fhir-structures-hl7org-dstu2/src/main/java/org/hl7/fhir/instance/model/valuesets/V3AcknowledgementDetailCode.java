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


public enum V3AcknowledgementDetailCode {

        /**
         * Refelects rejections because elements of the communication are not supported in the current context.
         */
        _ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE, 
        /**
         * The interaction (or: this version of the interaction) is not supported.
         */
        NS200, 
        /**
         * The Processing ID is not supported.
         */
        NS202, 
        /**
         * The Version ID is not supported.
         */
        NS203, 
        /**
         * The processing mode is not supported.
         */
        NS250, 
        /**
         * The Device.id of the sender is unknown.
         */
        NS260, 
        /**
         * The receiver requires information in the attentionLine classes for routing purposes.
         */
        NS261, 
        /**
         * An internal software component (database, application, queue mechanism, etc.) has failed, leading to inability to process the message.
         */
        INTERR, 
        /**
         * Rejection: The message can't be stored by the receiver due to an unspecified internal application issue. The message was neither processed nor stored by the receiving application.
         */
        NOSTORE, 
        /**
         * Error: The destination of this message is known to the receiving application. Messages have been successfully routed to that destination in the past. The link to the destination application or an intermediate application is unavailable.
         */
        RTEDEST, 
        /**
         * The destination of this message is unknown to the receiving application. The receiving application in the message does not match the application which received the message. The message was neither routed, processed nor stored by the receiving application.
         */
        RTUDEST, 
        /**
         * Warning: The destination of this message is known to the receiving application. Messages have been successfully routed to that destination in the past. The link to the destination application or an intermediate application is (temporarily) unavailable. The receiving application will forward the message as soon as the destination can be reached again.
         */
        RTWDEST, 
        /**
         * Reflects errors in the syntax or structure of the communication.
         */
        SYN, 
        /**
         * The attribute contained data of the wrong data type, e.g. a numeric attribute contained "FOO".
         */
        SYN102, 
        /**
         * Description: Required association or attribute missing in message; or the sequence of the classes is different than required by the standard or one of the conformance profiles identified in the message.
         */
        SYN105, 
        /**
         * Required association missing in message; or the sequence of the classes is different than required by the standard or one of the conformance profiles identified in the message.
         */
        SYN100, 
        /**
         * A required attribute is missing in a class.
         */
        SYN101, 
        /**
         * Description: The number of repetitions of a group of association or attributes is less than the required minimum for the standard or of one of the conformance profiles or templates identified in the message.
         */
        SYN114, 
        /**
         * Description: A coded attribute or datatype property violates one of the terminology constraints specified in the standard or one of the conformance profiles or templates declared by the instance.
         */
        SYN106, 
        /**
         * An attribute value was compared against the corresponding code system, and no match was found.
         */
        SYN103, 
        /**
         * An attribute value referenced a code system that is not valid for an attribute constrained to CNE.
         */
        SYN104, 
        /**
         * Description: A coded attribute is referencing a code that has been deprecated by the owning code system.
         */
        SYN107, 
        /**
         * Description: The number of repetitions of a (group of) association(s) or attribute(s) exceeds the limits of the standard or of one of the conformance profiles or templates identified in the message.
         */
        SYN108, 
        /**
         * The number of repetitions of a (group of) association(s) exceeds the limits of the standard or of one of the conformance profiles identified in the message.
         */
        SYN110, 
        /**
         * The number of repetitions of an attribute exceeds the limits of the standard or of one of the conformance profiles identified in the message.
         */
        SYN112, 
        /**
         * Description: An attribute or association identified as mandatory in a specification or declared conformance profile or template has been specified with a null flavor.
         */
        SYN109, 
        /**
         * Description: The value of an attribute or property differs from the fixed value asserted in the standard or one of the conformance profiles or templates declared in the message.
         */
        SYN111, 
        /**
         * Description: A formal constraint asserted in the standard or one of the conformance profiles or templates declared in the message has been violated.
         */
        SYN113, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3AcknowledgementDetailCode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_AcknowledgementDetailNotSupportedCode".equals(codeString))
          return _ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE;
        if ("NS200".equals(codeString))
          return NS200;
        if ("NS202".equals(codeString))
          return NS202;
        if ("NS203".equals(codeString))
          return NS203;
        if ("NS250".equals(codeString))
          return NS250;
        if ("NS260".equals(codeString))
          return NS260;
        if ("NS261".equals(codeString))
          return NS261;
        if ("INTERR".equals(codeString))
          return INTERR;
        if ("NOSTORE".equals(codeString))
          return NOSTORE;
        if ("RTEDEST".equals(codeString))
          return RTEDEST;
        if ("RTUDEST".equals(codeString))
          return RTUDEST;
        if ("RTWDEST".equals(codeString))
          return RTWDEST;
        if ("SYN".equals(codeString))
          return SYN;
        if ("SYN102".equals(codeString))
          return SYN102;
        if ("SYN105".equals(codeString))
          return SYN105;
        if ("SYN100".equals(codeString))
          return SYN100;
        if ("SYN101".equals(codeString))
          return SYN101;
        if ("SYN114".equals(codeString))
          return SYN114;
        if ("SYN106".equals(codeString))
          return SYN106;
        if ("SYN103".equals(codeString))
          return SYN103;
        if ("SYN104".equals(codeString))
          return SYN104;
        if ("SYN107".equals(codeString))
          return SYN107;
        if ("SYN108".equals(codeString))
          return SYN108;
        if ("SYN110".equals(codeString))
          return SYN110;
        if ("SYN112".equals(codeString))
          return SYN112;
        if ("SYN109".equals(codeString))
          return SYN109;
        if ("SYN111".equals(codeString))
          return SYN111;
        if ("SYN113".equals(codeString))
          return SYN113;
        throw new Exception("Unknown V3AcknowledgementDetailCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE: return "_AcknowledgementDetailNotSupportedCode";
            case NS200: return "NS200";
            case NS202: return "NS202";
            case NS203: return "NS203";
            case NS250: return "NS250";
            case NS260: return "NS260";
            case NS261: return "NS261";
            case INTERR: return "INTERR";
            case NOSTORE: return "NOSTORE";
            case RTEDEST: return "RTEDEST";
            case RTUDEST: return "RTUDEST";
            case RTWDEST: return "RTWDEST";
            case SYN: return "SYN";
            case SYN102: return "SYN102";
            case SYN105: return "SYN105";
            case SYN100: return "SYN100";
            case SYN101: return "SYN101";
            case SYN114: return "SYN114";
            case SYN106: return "SYN106";
            case SYN103: return "SYN103";
            case SYN104: return "SYN104";
            case SYN107: return "SYN107";
            case SYN108: return "SYN108";
            case SYN110: return "SYN110";
            case SYN112: return "SYN112";
            case SYN109: return "SYN109";
            case SYN111: return "SYN111";
            case SYN113: return "SYN113";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/AcknowledgementDetailCode";
        }
        public String getDefinition() {
          switch (this) {
            case _ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE: return "Refelects rejections because elements of the communication are not supported in the current context.";
            case NS200: return "The interaction (or: this version of the interaction) is not supported.";
            case NS202: return "The Processing ID is not supported.";
            case NS203: return "The Version ID is not supported.";
            case NS250: return "The processing mode is not supported.";
            case NS260: return "The Device.id of the sender is unknown.";
            case NS261: return "The receiver requires information in the attentionLine classes for routing purposes.";
            case INTERR: return "An internal software component (database, application, queue mechanism, etc.) has failed, leading to inability to process the message.";
            case NOSTORE: return "Rejection: The message can't be stored by the receiver due to an unspecified internal application issue. The message was neither processed nor stored by the receiving application.";
            case RTEDEST: return "Error: The destination of this message is known to the receiving application. Messages have been successfully routed to that destination in the past. The link to the destination application or an intermediate application is unavailable.";
            case RTUDEST: return "The destination of this message is unknown to the receiving application. The receiving application in the message does not match the application which received the message. The message was neither routed, processed nor stored by the receiving application.";
            case RTWDEST: return "Warning: The destination of this message is known to the receiving application. Messages have been successfully routed to that destination in the past. The link to the destination application or an intermediate application is (temporarily) unavailable. The receiving application will forward the message as soon as the destination can be reached again.";
            case SYN: return "Reflects errors in the syntax or structure of the communication.";
            case SYN102: return "The attribute contained data of the wrong data type, e.g. a numeric attribute contained \"FOO\".";
            case SYN105: return "Description: Required association or attribute missing in message; or the sequence of the classes is different than required by the standard or one of the conformance profiles identified in the message.";
            case SYN100: return "Required association missing in message; or the sequence of the classes is different than required by the standard or one of the conformance profiles identified in the message.";
            case SYN101: return "A required attribute is missing in a class.";
            case SYN114: return "Description: The number of repetitions of a group of association or attributes is less than the required minimum for the standard or of one of the conformance profiles or templates identified in the message.";
            case SYN106: return "Description: A coded attribute or datatype property violates one of the terminology constraints specified in the standard or one of the conformance profiles or templates declared by the instance.";
            case SYN103: return "An attribute value was compared against the corresponding code system, and no match was found.";
            case SYN104: return "An attribute value referenced a code system that is not valid for an attribute constrained to CNE.";
            case SYN107: return "Description: A coded attribute is referencing a code that has been deprecated by the owning code system.";
            case SYN108: return "Description: The number of repetitions of a (group of) association(s) or attribute(s) exceeds the limits of the standard or of one of the conformance profiles or templates identified in the message.";
            case SYN110: return "The number of repetitions of a (group of) association(s) exceeds the limits of the standard or of one of the conformance profiles identified in the message.";
            case SYN112: return "The number of repetitions of an attribute exceeds the limits of the standard or of one of the conformance profiles identified in the message.";
            case SYN109: return "Description: An attribute or association identified as mandatory in a specification or declared conformance profile or template has been specified with a null flavor.";
            case SYN111: return "Description: The value of an attribute or property differs from the fixed value asserted in the standard or one of the conformance profiles or templates declared in the message.";
            case SYN113: return "Description: A formal constraint asserted in the standard or one of the conformance profiles or templates declared in the message has been violated.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACKNOWLEDGEMENTDETAILNOTSUPPORTEDCODE: return "AcknowledgementDetailNotSupportedCode";
            case NS200: return "Unsupported interaction";
            case NS202: return "Unsupported processing id";
            case NS203: return "Unsupported version id";
            case NS250: return "Unsupported processing Mode";
            case NS260: return "Unknown sender";
            case NS261: return "Unrecognized attentionline";
            case INTERR: return "Internal system error";
            case NOSTORE: return "No storage space for message.";
            case RTEDEST: return "Message routing error, destination unreachable.";
            case RTUDEST: return "Error: Message routing error, unknown destination.";
            case RTWDEST: return "Message routing warning, destination unreachable.";
            case SYN: return "Syntax error";
            case SYN102: return "Data type error";
            case SYN105: return "Required element missing";
            case SYN100: return "Required association missing";
            case SYN101: return "Required attribute missing";
            case SYN114: return "Insufficient repetitions";
            case SYN106: return "Terminology error";
            case SYN103: return "Value not found in code system";
            case SYN104: return "Invalid code system in CNE";
            case SYN107: return "Deprecated code";
            case SYN108: return "Number of repetitions exceeds limit";
            case SYN110: return "Number of association repetitions exceeds limit";
            case SYN112: return "Number of attribute repetitions exceeds limit";
            case SYN109: return "Mandatory element with null value";
            case SYN111: return "Value does not match fixed value";
            case SYN113: return "Formal constraint violation";
            default: return "?";
          }
    }


}

