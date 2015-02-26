package org.hl7.fhir.instance.model;

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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
 */
@ResourceDef(name="SecurityEvent", profile="http://hl7.org/fhir/Profile/SecurityEvent")
public class SecurityEvent extends DomainResource {

    public enum SecurityEventAction {
        /**
         * Create a new database object, such as Placing an Order.
         */
        C, 
        /**
         * Display or print data, such as a Doctor Census.
         */
        R, 
        /**
         * Update data, such as Revise Patient Information.
         */
        U, 
        /**
         * Delete items, such as a doctor master file record.
         */
        D, 
        /**
         * Perform a system or application function such as log-on, program execution or use of an object's method, or perform a query/search operation.
         */
        E, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SecurityEventAction fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("C".equals(codeString))
          return C;
        if ("R".equals(codeString))
          return R;
        if ("U".equals(codeString))
          return U;
        if ("D".equals(codeString))
          return D;
        if ("E".equals(codeString))
          return E;
        throw new Exception("Unknown SecurityEventAction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case C: return "C";
            case R: return "R";
            case U: return "U";
            case D: return "D";
            case E: return "E";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case C: return "";
            case R: return "";
            case U: return "";
            case D: return "";
            case E: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case C: return "Create a new database object, such as Placing an Order.";
            case R: return "Display or print data, such as a Doctor Census.";
            case U: return "Update data, such as Revise Patient Information.";
            case D: return "Delete items, such as a doctor master file record.";
            case E: return "Perform a system or application function such as log-on, program execution or use of an object's method, or perform a query/search operation.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case C: return "Create";
            case R: return "Read/View/Print";
            case U: return "Update";
            case D: return "Delete";
            case E: return "Execute";
            default: return "?";
          }
        }
    }

  public static class SecurityEventActionEnumFactory implements EnumFactory<SecurityEventAction> {
    public SecurityEventAction fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("C".equals(codeString))
          return SecurityEventAction.C;
        if ("R".equals(codeString))
          return SecurityEventAction.R;
        if ("U".equals(codeString))
          return SecurityEventAction.U;
        if ("D".equals(codeString))
          return SecurityEventAction.D;
        if ("E".equals(codeString))
          return SecurityEventAction.E;
        throw new IllegalArgumentException("Unknown SecurityEventAction code '"+codeString+"'");
        }
    public String toCode(SecurityEventAction code) {
      if (code == SecurityEventAction.C)
        return "C";
      if (code == SecurityEventAction.R)
        return "R";
      if (code == SecurityEventAction.U)
        return "U";
      if (code == SecurityEventAction.D)
        return "D";
      if (code == SecurityEventAction.E)
        return "E";
      return "?";
      }
    }

    public enum SecurityEventOutcome {
        /**
         * The operation completed successfully (whether with warnings or not).
         */
        _0, 
        /**
         * The action was not successful due to some kind of catered for error (often equivalent to an HTTP 400 response).
         */
        _4, 
        /**
         * The action was not successful due to some kind of unexpected error (often equivalent to an HTTP 500 response).
         */
        _8, 
        /**
         * An error of such magnitude occurred that the system is not longer available for use (i.e. the system died).
         */
        _12, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SecurityEventOutcome fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("0".equals(codeString))
          return _0;
        if ("4".equals(codeString))
          return _4;
        if ("8".equals(codeString))
          return _8;
        if ("12".equals(codeString))
          return _12;
        throw new Exception("Unknown SecurityEventOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _0: return "0";
            case _4: return "4";
            case _8: return "8";
            case _12: return "12";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case _0: return "";
            case _4: return "";
            case _8: return "";
            case _12: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case _0: return "The operation completed successfully (whether with warnings or not).";
            case _4: return "The action was not successful due to some kind of catered for error (often equivalent to an HTTP 400 response).";
            case _8: return "The action was not successful due to some kind of unexpected error (often equivalent to an HTTP 500 response).";
            case _12: return "An error of such magnitude occurred that the system is not longer available for use (i.e. the system died).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _0: return "Success";
            case _4: return "Minor failure";
            case _8: return "Serious failure";
            case _12: return "Major failure";
            default: return "?";
          }
        }
    }

  public static class SecurityEventOutcomeEnumFactory implements EnumFactory<SecurityEventOutcome> {
    public SecurityEventOutcome fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("0".equals(codeString))
          return SecurityEventOutcome._0;
        if ("4".equals(codeString))
          return SecurityEventOutcome._4;
        if ("8".equals(codeString))
          return SecurityEventOutcome._8;
        if ("12".equals(codeString))
          return SecurityEventOutcome._12;
        throw new IllegalArgumentException("Unknown SecurityEventOutcome code '"+codeString+"'");
        }
    public String toCode(SecurityEventOutcome code) {
      if (code == SecurityEventOutcome._0)
        return "0";
      if (code == SecurityEventOutcome._4)
        return "4";
      if (code == SecurityEventOutcome._8)
        return "8";
      if (code == SecurityEventOutcome._12)
        return "12";
      return "?";
      }
    }

    public enum NetworkType {
        /**
         * Machine Name, including DNS name.
         */
        _1, 
        /**
         * IP Address.
         */
        _2, 
        /**
         * Telephone Number.
         */
        _3, 
        /**
         * Email address.
         */
        _4, 
        /**
         * URI (User directory, HTTP-PUT, ftp, etc.).
         */
        _5, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NetworkType fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown NetworkType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case _1: return "";
            case _2: return "";
            case _3: return "";
            case _4: return "";
            case _5: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Machine Name, including DNS name.";
            case _2: return "IP Address.";
            case _3: return "Telephone Number.";
            case _4: return "Email address.";
            case _5: return "URI (User directory, HTTP-PUT, ftp, etc.).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            default: return "?";
          }
        }
    }

  public static class NetworkTypeEnumFactory implements EnumFactory<NetworkType> {
    public NetworkType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return NetworkType._1;
        if ("2".equals(codeString))
          return NetworkType._2;
        if ("3".equals(codeString))
          return NetworkType._3;
        if ("4".equals(codeString))
          return NetworkType._4;
        if ("5".equals(codeString))
          return NetworkType._5;
        throw new IllegalArgumentException("Unknown NetworkType code '"+codeString+"'");
        }
    public String toCode(NetworkType code) {
      if (code == NetworkType._1)
        return "1";
      if (code == NetworkType._2)
        return "2";
      if (code == NetworkType._3)
        return "3";
      if (code == NetworkType._4)
        return "4";
      if (code == NetworkType._5)
        return "5";
      return "?";
      }
    }

    public enum ObjectType {
        /**
         * Person.
         */
        _1, 
        /**
         * System Object.
         */
        _2, 
        /**
         * Organization.
         */
        _3, 
        /**
         * Other.
         */
        _4, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObjectType fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown ObjectType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case _1: return "";
            case _2: return "";
            case _3: return "";
            case _4: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Person.";
            case _2: return "System Object.";
            case _3: return "Organization.";
            case _4: return "Other.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            default: return "?";
          }
        }
    }

  public static class ObjectTypeEnumFactory implements EnumFactory<ObjectType> {
    public ObjectType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return ObjectType._1;
        if ("2".equals(codeString))
          return ObjectType._2;
        if ("3".equals(codeString))
          return ObjectType._3;
        if ("4".equals(codeString))
          return ObjectType._4;
        throw new IllegalArgumentException("Unknown ObjectType code '"+codeString+"'");
        }
    public String toCode(ObjectType code) {
      if (code == ObjectType._1)
        return "1";
      if (code == ObjectType._2)
        return "2";
      if (code == ObjectType._3)
        return "3";
      if (code == ObjectType._4)
        return "4";
      return "?";
      }
    }

    public enum ObjectRole {
        /**
         * This object is the patient that is the subject of care related to this event.  It is identifiable by patient ID or equivalent.  The patient may be either human or animal.
         */
        _1, 
        /**
         * This is a location identified as related to the event.  This is usually the location where the event took place.  Note that for shipping, the usual events are arrival at a location or departure from a location.
         */
        _2, 
        /**
         * This object is any kind of persistent document created as a result of the event.  This could be a paper report, film, electronic report, DICOM Study, etc.  Issues related to medical records life cycle management are conveyed elsewhere.
         */
        _3, 
        /**
         * A logical object related to the event.  (Deprecated).
         */
        _4, 
        /**
         * This is any configurable file used to control creation of documents.  Examples include the objects maintained by the HL7 Master File transactions, Value Sets, etc.
         */
        _5, 
        /**
         * A human participant not otherwise identified by some other category.
         */
        _6, 
        /**
         * (deprecated).
         */
        _7, 
        /**
         * Typically a licensed person who is providing or performing care related to the event, generally a physician.   The key distinction between doctor and practitioner is with regards to their role, not the licensing.  The doctor is the human who actually performed the work.  The practitioner is the human or organization that is responsible for the work.
         */
        _8, 
        /**
         * A person or system that is being notified as part of the event.  This is relevant in situations where automated systems provide notifications to other parties when an event took place.
         */
        _9, 
        /**
         * Insurance company, or any other organization who accepts responsibility for paying for the healthcare event.
         */
        _10, 
        /**
         * A person or active system object involved in the event with a security role.
         */
        _11, 
        /**
         * A person or system object involved in the event with the authority to modify security roles of other objects.
         */
        _12, 
        /**
         * A passive object, such as a role table, that is relevant to the event.
         */
        _13, 
        /**
         * (deprecated)  Relevant to certain RBAC security methodologies.
         */
        _14, 
        /**
         * Any person or organization responsible for providing care.  This encompasses all forms of care, licensed or otherwise, and all sorts of teams and care groups. Note, the distinction between practitioners and the doctor that actually provided the care to the patient.
         */
        _15, 
        /**
         * The source or destination for data transfer, when it does not match some other role.
         */
        _16, 
        /**
         * A source or destination for data transfer, that acts as an archive, database, or similar role.
         */
        _17, 
        /**
         * An object that holds schedule information.  This could be an appointment book, availability information, etc.
         */
        _18, 
        /**
         * An organization or person that is the recipient of services.  This could be an organization that is buying services for a patient, or a person that is buying services for an animal.
         */
        _19, 
        /**
         * An order, task, work item, procedure step, or other description of work to be performed.  E.g., a particular instance of an MPPS.
         */
        _20, 
        /**
         * A list of jobs or a system that provides lists of jobs.  E.g., an MWL SCP.
         */
        _21, 
        /**
         * (Deprecated).
         */
        _22, 
        /**
         * An object that specifies or controls the routing or delivery of items.  For example, a distribution list is the routing criteria for mail.  The items delivered may be documents, jobs, or other objects.
         */
        _23, 
        /**
         * The contents of a query.  This is used to capture the contents of any kind of query.  For security surveillance purposes knowing the queries being made is very important.
         */
        _24, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObjectRole fromCode(String codeString) throws Exception {
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
        if ("10".equals(codeString))
          return _10;
        if ("11".equals(codeString))
          return _11;
        if ("12".equals(codeString))
          return _12;
        if ("13".equals(codeString))
          return _13;
        if ("14".equals(codeString))
          return _14;
        if ("15".equals(codeString))
          return _15;
        if ("16".equals(codeString))
          return _16;
        if ("17".equals(codeString))
          return _17;
        if ("18".equals(codeString))
          return _18;
        if ("19".equals(codeString))
          return _19;
        if ("20".equals(codeString))
          return _20;
        if ("21".equals(codeString))
          return _21;
        if ("22".equals(codeString))
          return _22;
        if ("23".equals(codeString))
          return _23;
        if ("24".equals(codeString))
          return _24;
        throw new Exception("Unknown ObjectRole code '"+codeString+"'");
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
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            case _16: return "16";
            case _17: return "17";
            case _18: return "18";
            case _19: return "19";
            case _20: return "20";
            case _21: return "21";
            case _22: return "22";
            case _23: return "23";
            case _24: return "24";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case _1: return "";
            case _2: return "";
            case _3: return "";
            case _4: return "";
            case _5: return "";
            case _6: return "";
            case _7: return "";
            case _8: return "";
            case _9: return "";
            case _10: return "";
            case _11: return "";
            case _12: return "";
            case _13: return "";
            case _14: return "";
            case _15: return "";
            case _16: return "";
            case _17: return "";
            case _18: return "";
            case _19: return "";
            case _20: return "";
            case _21: return "";
            case _22: return "";
            case _23: return "";
            case _24: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "This object is the patient that is the subject of care related to this event.  It is identifiable by patient ID or equivalent.  The patient may be either human or animal.";
            case _2: return "This is a location identified as related to the event.  This is usually the location where the event took place.  Note that for shipping, the usual events are arrival at a location or departure from a location.";
            case _3: return "This object is any kind of persistent document created as a result of the event.  This could be a paper report, film, electronic report, DICOM Study, etc.  Issues related to medical records life cycle management are conveyed elsewhere.";
            case _4: return "A logical object related to the event.  (Deprecated).";
            case _5: return "This is any configurable file used to control creation of documents.  Examples include the objects maintained by the HL7 Master File transactions, Value Sets, etc.";
            case _6: return "A human participant not otherwise identified by some other category.";
            case _7: return "(deprecated).";
            case _8: return "Typically a licensed person who is providing or performing care related to the event, generally a physician.   The key distinction between doctor and practitioner is with regards to their role, not the licensing.  The doctor is the human who actually performed the work.  The practitioner is the human or organization that is responsible for the work.";
            case _9: return "A person or system that is being notified as part of the event.  This is relevant in situations where automated systems provide notifications to other parties when an event took place.";
            case _10: return "Insurance company, or any other organization who accepts responsibility for paying for the healthcare event.";
            case _11: return "A person or active system object involved in the event with a security role.";
            case _12: return "A person or system object involved in the event with the authority to modify security roles of other objects.";
            case _13: return "A passive object, such as a role table, that is relevant to the event.";
            case _14: return "(deprecated)  Relevant to certain RBAC security methodologies.";
            case _15: return "Any person or organization responsible for providing care.  This encompasses all forms of care, licensed or otherwise, and all sorts of teams and care groups. Note, the distinction between practitioners and the doctor that actually provided the care to the patient.";
            case _16: return "The source or destination for data transfer, when it does not match some other role.";
            case _17: return "A source or destination for data transfer, that acts as an archive, database, or similar role.";
            case _18: return "An object that holds schedule information.  This could be an appointment book, availability information, etc.";
            case _19: return "An organization or person that is the recipient of services.  This could be an organization that is buying services for a patient, or a person that is buying services for an animal.";
            case _20: return "An order, task, work item, procedure step, or other description of work to be performed.  E.g., a particular instance of an MPPS.";
            case _21: return "A list of jobs or a system that provides lists of jobs.  E.g., an MWL SCP.";
            case _22: return "(Deprecated).";
            case _23: return "An object that specifies or controls the routing or delivery of items.  For example, a distribution list is the routing criteria for mail.  The items delivered may be documents, jobs, or other objects.";
            case _24: return "The contents of a query.  This is used to capture the contents of any kind of query.  For security surveillance purposes knowing the queries being made is very important.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Patient";
            case _2: return "Location";
            case _3: return "Report";
            case _4: return "DomainResource";
            case _5: return "Master file";
            case _6: return "User";
            case _7: return "List";
            case _8: return "Doctor";
            case _9: return "Subscriber";
            case _10: return "Guarantor";
            case _11: return "Security User Entity";
            case _12: return "Security User Group";
            case _13: return "Security Resource";
            case _14: return "Security Granularity Definition";
            case _15: return "Practitioner";
            case _16: return "Data Destination";
            case _17: return "Data Repository";
            case _18: return "Schedule";
            case _19: return "Customer";
            case _20: return "Job";
            case _21: return "Job Stream";
            case _22: return "Table";
            case _23: return "Routing Criteria";
            case _24: return "Query";
            default: return "?";
          }
        }
    }

  public static class ObjectRoleEnumFactory implements EnumFactory<ObjectRole> {
    public ObjectRole fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return ObjectRole._1;
        if ("2".equals(codeString))
          return ObjectRole._2;
        if ("3".equals(codeString))
          return ObjectRole._3;
        if ("4".equals(codeString))
          return ObjectRole._4;
        if ("5".equals(codeString))
          return ObjectRole._5;
        if ("6".equals(codeString))
          return ObjectRole._6;
        if ("7".equals(codeString))
          return ObjectRole._7;
        if ("8".equals(codeString))
          return ObjectRole._8;
        if ("9".equals(codeString))
          return ObjectRole._9;
        if ("10".equals(codeString))
          return ObjectRole._10;
        if ("11".equals(codeString))
          return ObjectRole._11;
        if ("12".equals(codeString))
          return ObjectRole._12;
        if ("13".equals(codeString))
          return ObjectRole._13;
        if ("14".equals(codeString))
          return ObjectRole._14;
        if ("15".equals(codeString))
          return ObjectRole._15;
        if ("16".equals(codeString))
          return ObjectRole._16;
        if ("17".equals(codeString))
          return ObjectRole._17;
        if ("18".equals(codeString))
          return ObjectRole._18;
        if ("19".equals(codeString))
          return ObjectRole._19;
        if ("20".equals(codeString))
          return ObjectRole._20;
        if ("21".equals(codeString))
          return ObjectRole._21;
        if ("22".equals(codeString))
          return ObjectRole._22;
        if ("23".equals(codeString))
          return ObjectRole._23;
        if ("24".equals(codeString))
          return ObjectRole._24;
        throw new IllegalArgumentException("Unknown ObjectRole code '"+codeString+"'");
        }
    public String toCode(ObjectRole code) {
      if (code == ObjectRole._1)
        return "1";
      if (code == ObjectRole._2)
        return "2";
      if (code == ObjectRole._3)
        return "3";
      if (code == ObjectRole._4)
        return "4";
      if (code == ObjectRole._5)
        return "5";
      if (code == ObjectRole._6)
        return "6";
      if (code == ObjectRole._7)
        return "7";
      if (code == ObjectRole._8)
        return "8";
      if (code == ObjectRole._9)
        return "9";
      if (code == ObjectRole._10)
        return "10";
      if (code == ObjectRole._11)
        return "11";
      if (code == ObjectRole._12)
        return "12";
      if (code == ObjectRole._13)
        return "13";
      if (code == ObjectRole._14)
        return "14";
      if (code == ObjectRole._15)
        return "15";
      if (code == ObjectRole._16)
        return "16";
      if (code == ObjectRole._17)
        return "17";
      if (code == ObjectRole._18)
        return "18";
      if (code == ObjectRole._19)
        return "19";
      if (code == ObjectRole._20)
        return "20";
      if (code == ObjectRole._21)
        return "21";
      if (code == ObjectRole._22)
        return "22";
      if (code == ObjectRole._23)
        return "23";
      if (code == ObjectRole._24)
        return "24";
      return "?";
      }
    }

    public enum ObjectLifecycle {
        /**
         * Origination / Creation.
         */
        _1, 
        /**
         * Import / Copy from original.
         */
        _2, 
        /**
         * Amendment.
         */
        _3, 
        /**
         * Verification.
         */
        _4, 
        /**
         * Translation.
         */
        _5, 
        /**
         * Access / Use.
         */
        _6, 
        /**
         * De-identification.
         */
        _7, 
        /**
         * Aggregation, summarization, derivation.
         */
        _8, 
        /**
         * Report.
         */
        _9, 
        /**
         * Export / Copy to target.
         */
        _10, 
        /**
         * Disclosure.
         */
        _11, 
        /**
         * Receipt of disclosure.
         */
        _12, 
        /**
         * Archiving.
         */
        _13, 
        /**
         * Logical deletion.
         */
        _14, 
        /**
         * Permanent erasure / Physical destruction.
         */
        _15, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObjectLifecycle fromCode(String codeString) throws Exception {
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
        if ("10".equals(codeString))
          return _10;
        if ("11".equals(codeString))
          return _11;
        if ("12".equals(codeString))
          return _12;
        if ("13".equals(codeString))
          return _13;
        if ("14".equals(codeString))
          return _14;
        if ("15".equals(codeString))
          return _15;
        throw new Exception("Unknown ObjectLifecycle code '"+codeString+"'");
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
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case _1: return "";
            case _2: return "";
            case _3: return "";
            case _4: return "";
            case _5: return "";
            case _6: return "";
            case _7: return "";
            case _8: return "";
            case _9: return "";
            case _10: return "";
            case _11: return "";
            case _12: return "";
            case _13: return "";
            case _14: return "";
            case _15: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Origination / Creation.";
            case _2: return "Import / Copy from original.";
            case _3: return "Amendment.";
            case _4: return "Verification.";
            case _5: return "Translation.";
            case _6: return "Access / Use.";
            case _7: return "De-identification.";
            case _8: return "Aggregation, summarization, derivation.";
            case _9: return "Report.";
            case _10: return "Export / Copy to target.";
            case _11: return "Disclosure.";
            case _12: return "Receipt of disclosure.";
            case _13: return "Archiving.";
            case _14: return "Logical deletion.";
            case _15: return "Permanent erasure / Physical destruction.";
            default: return "?";
          }
        }
        public String getDisplay() {
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
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            default: return "?";
          }
        }
    }

  public static class ObjectLifecycleEnumFactory implements EnumFactory<ObjectLifecycle> {
    public ObjectLifecycle fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return ObjectLifecycle._1;
        if ("2".equals(codeString))
          return ObjectLifecycle._2;
        if ("3".equals(codeString))
          return ObjectLifecycle._3;
        if ("4".equals(codeString))
          return ObjectLifecycle._4;
        if ("5".equals(codeString))
          return ObjectLifecycle._5;
        if ("6".equals(codeString))
          return ObjectLifecycle._6;
        if ("7".equals(codeString))
          return ObjectLifecycle._7;
        if ("8".equals(codeString))
          return ObjectLifecycle._8;
        if ("9".equals(codeString))
          return ObjectLifecycle._9;
        if ("10".equals(codeString))
          return ObjectLifecycle._10;
        if ("11".equals(codeString))
          return ObjectLifecycle._11;
        if ("12".equals(codeString))
          return ObjectLifecycle._12;
        if ("13".equals(codeString))
          return ObjectLifecycle._13;
        if ("14".equals(codeString))
          return ObjectLifecycle._14;
        if ("15".equals(codeString))
          return ObjectLifecycle._15;
        throw new IllegalArgumentException("Unknown ObjectLifecycle code '"+codeString+"'");
        }
    public String toCode(ObjectLifecycle code) {
      if (code == ObjectLifecycle._1)
        return "1";
      if (code == ObjectLifecycle._2)
        return "2";
      if (code == ObjectLifecycle._3)
        return "3";
      if (code == ObjectLifecycle._4)
        return "4";
      if (code == ObjectLifecycle._5)
        return "5";
      if (code == ObjectLifecycle._6)
        return "6";
      if (code == ObjectLifecycle._7)
        return "7";
      if (code == ObjectLifecycle._8)
        return "8";
      if (code == ObjectLifecycle._9)
        return "9";
      if (code == ObjectLifecycle._10)
        return "10";
      if (code == ObjectLifecycle._11)
        return "11";
      if (code == ObjectLifecycle._12)
        return "12";
      if (code == ObjectLifecycle._13)
        return "13";
      if (code == ObjectLifecycle._14)
        return "14";
      if (code == ObjectLifecycle._15)
        return "15";
      return "?";
      }
    }

    @Block()
    public static class SecurityEventEventComponent extends BackboneElement {
        /**
         * Identifier for a family of the event.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Type/identifier of event", formalDefinition="Identifier for a family of the event." )
        protected CodeableConcept type;

        /**
         * Identifier for the category of event.
         */
        @Child(name="subtype", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="More specific type/id for the event", formalDefinition="Identifier for the category of event." )
        protected List<CodeableConcept> subtype;

        /**
         * Indicator for type of action performed during the event that generated the audit.
         */
        @Child(name="action", type={CodeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Type of action performed during the event", formalDefinition="Indicator for type of action performed during the event that generated the audit." )
        protected Enumeration<SecurityEventAction> action;

        /**
         * The time when the event occurred on the source.
         */
        @Child(name="dateTime", type={InstantType.class}, order=4, min=1, max=1)
        @Description(shortDefinition="Time when the event occurred on source", formalDefinition="The time when the event occurred on the source." )
        protected InstantType dateTime;

        /**
         * Indicates whether the event succeeded or failed.
         */
        @Child(name="outcome", type={CodeType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Whether the event succeeded or failed", formalDefinition="Indicates whether the event succeeded or failed." )
        protected Enumeration<SecurityEventOutcome> outcome;

        /**
         * A free text description of the outcome of the event.
         */
        @Child(name="outcomeDesc", type={StringType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Description of the event outcome", formalDefinition="A free text description of the outcome of the event." )
        protected StringType outcomeDesc;

        private static final long serialVersionUID = 1351587588L;

      public SecurityEventEventComponent() {
        super();
      }

      public SecurityEventEventComponent(CodeableConcept type, InstantType dateTime) {
        super();
        this.type = type;
        this.dateTime = dateTime;
      }

        /**
         * @return {@link #type} (Identifier for a family of the event.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventEventComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Identifier for a family of the event.)
         */
        public SecurityEventEventComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subtype} (Identifier for the category of event.)
         */
        public List<CodeableConcept> getSubtype() { 
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          return this.subtype;
        }

        public boolean hasSubtype() { 
          if (this.subtype == null)
            return false;
          for (CodeableConcept item : this.subtype)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #subtype} (Identifier for the category of event.)
         */
    // syntactic sugar
        public CodeableConcept addSubtype() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          this.subtype.add(t);
          return t;
        }

        /**
         * @return {@link #action} (Indicator for type of action performed during the event that generated the audit.). This is the underlying object with id, value and extensions. The accessor "getAction" gives direct access to the value
         */
        public Enumeration<SecurityEventAction> getActionElement() { 
          if (this.action == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventEventComponent.action");
            else if (Configuration.doAutoCreate())
              this.action = new Enumeration<SecurityEventAction>(new SecurityEventActionEnumFactory()); // bb
          return this.action;
        }

        public boolean hasActionElement() { 
          return this.action != null && !this.action.isEmpty();
        }

        public boolean hasAction() { 
          return this.action != null && !this.action.isEmpty();
        }

        /**
         * @param value {@link #action} (Indicator for type of action performed during the event that generated the audit.). This is the underlying object with id, value and extensions. The accessor "getAction" gives direct access to the value
         */
        public SecurityEventEventComponent setActionElement(Enumeration<SecurityEventAction> value) { 
          this.action = value;
          return this;
        }

        /**
         * @return Indicator for type of action performed during the event that generated the audit.
         */
        public SecurityEventAction getAction() { 
          return this.action == null ? null : this.action.getValue();
        }

        /**
         * @param value Indicator for type of action performed during the event that generated the audit.
         */
        public SecurityEventEventComponent setAction(SecurityEventAction value) { 
          if (value == null)
            this.action = null;
          else {
            if (this.action == null)
              this.action = new Enumeration<SecurityEventAction>(new SecurityEventActionEnumFactory());
            this.action.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #dateTime} (The time when the event occurred on the source.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public InstantType getDateTimeElement() { 
          if (this.dateTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventEventComponent.dateTime");
            else if (Configuration.doAutoCreate())
              this.dateTime = new InstantType(); // bb
          return this.dateTime;
        }

        public boolean hasDateTimeElement() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        public boolean hasDateTime() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        /**
         * @param value {@link #dateTime} (The time when the event occurred on the source.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public SecurityEventEventComponent setDateTimeElement(InstantType value) { 
          this.dateTime = value;
          return this;
        }

        /**
         * @return The time when the event occurred on the source.
         */
        public Date getDateTime() { 
          return this.dateTime == null ? null : this.dateTime.getValue();
        }

        /**
         * @param value The time when the event occurred on the source.
         */
        public SecurityEventEventComponent setDateTime(Date value) { 
            if (this.dateTime == null)
              this.dateTime = new InstantType();
            this.dateTime.setValue(value);
          return this;
        }

        /**
         * @return {@link #outcome} (Indicates whether the event succeeded or failed.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
         */
        public Enumeration<SecurityEventOutcome> getOutcomeElement() { 
          if (this.outcome == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventEventComponent.outcome");
            else if (Configuration.doAutoCreate())
              this.outcome = new Enumeration<SecurityEventOutcome>(new SecurityEventOutcomeEnumFactory()); // bb
          return this.outcome;
        }

        public boolean hasOutcomeElement() { 
          return this.outcome != null && !this.outcome.isEmpty();
        }

        public boolean hasOutcome() { 
          return this.outcome != null && !this.outcome.isEmpty();
        }

        /**
         * @param value {@link #outcome} (Indicates whether the event succeeded or failed.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
         */
        public SecurityEventEventComponent setOutcomeElement(Enumeration<SecurityEventOutcome> value) { 
          this.outcome = value;
          return this;
        }

        /**
         * @return Indicates whether the event succeeded or failed.
         */
        public SecurityEventOutcome getOutcome() { 
          return this.outcome == null ? null : this.outcome.getValue();
        }

        /**
         * @param value Indicates whether the event succeeded or failed.
         */
        public SecurityEventEventComponent setOutcome(SecurityEventOutcome value) { 
          if (value == null)
            this.outcome = null;
          else {
            if (this.outcome == null)
              this.outcome = new Enumeration<SecurityEventOutcome>(new SecurityEventOutcomeEnumFactory());
            this.outcome.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #outcomeDesc} (A free text description of the outcome of the event.). This is the underlying object with id, value and extensions. The accessor "getOutcomeDesc" gives direct access to the value
         */
        public StringType getOutcomeDescElement() { 
          if (this.outcomeDesc == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventEventComponent.outcomeDesc");
            else if (Configuration.doAutoCreate())
              this.outcomeDesc = new StringType(); // bb
          return this.outcomeDesc;
        }

        public boolean hasOutcomeDescElement() { 
          return this.outcomeDesc != null && !this.outcomeDesc.isEmpty();
        }

        public boolean hasOutcomeDesc() { 
          return this.outcomeDesc != null && !this.outcomeDesc.isEmpty();
        }

        /**
         * @param value {@link #outcomeDesc} (A free text description of the outcome of the event.). This is the underlying object with id, value and extensions. The accessor "getOutcomeDesc" gives direct access to the value
         */
        public SecurityEventEventComponent setOutcomeDescElement(StringType value) { 
          this.outcomeDesc = value;
          return this;
        }

        /**
         * @return A free text description of the outcome of the event.
         */
        public String getOutcomeDesc() { 
          return this.outcomeDesc == null ? null : this.outcomeDesc.getValue();
        }

        /**
         * @param value A free text description of the outcome of the event.
         */
        public SecurityEventEventComponent setOutcomeDesc(String value) { 
          if (Utilities.noString(value))
            this.outcomeDesc = null;
          else {
            if (this.outcomeDesc == null)
              this.outcomeDesc = new StringType();
            this.outcomeDesc.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Identifier for a family of the event.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("subtype", "CodeableConcept", "Identifier for the category of event.", 0, java.lang.Integer.MAX_VALUE, subtype));
          childrenList.add(new Property("action", "code", "Indicator for type of action performed during the event that generated the audit.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("dateTime", "instant", "The time when the event occurred on the source.", 0, java.lang.Integer.MAX_VALUE, dateTime));
          childrenList.add(new Property("outcome", "code", "Indicates whether the event succeeded or failed.", 0, java.lang.Integer.MAX_VALUE, outcome));
          childrenList.add(new Property("outcomeDesc", "string", "A free text description of the outcome of the event.", 0, java.lang.Integer.MAX_VALUE, outcomeDesc));
        }

      public SecurityEventEventComponent copy() {
        SecurityEventEventComponent dst = new SecurityEventEventComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (subtype != null) {
          dst.subtype = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subtype)
            dst.subtype.add(i.copy());
        };
        dst.action = action == null ? null : action.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.outcomeDesc = outcomeDesc == null ? null : outcomeDesc.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SecurityEventEventComponent))
          return false;
        SecurityEventEventComponent o = (SecurityEventEventComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(subtype, o.subtype, true) && compareDeep(action, o.action, true)
           && compareDeep(dateTime, o.dateTime, true) && compareDeep(outcome, o.outcome, true) && compareDeep(outcomeDesc, o.outcomeDesc, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SecurityEventEventComponent))
          return false;
        SecurityEventEventComponent o = (SecurityEventEventComponent) other;
        return compareValues(action, o.action, true) && compareValues(dateTime, o.dateTime, true) && compareValues(outcome, o.outcome, true)
           && compareValues(outcomeDesc, o.outcomeDesc, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (subtype == null || subtype.isEmpty())
           && (action == null || action.isEmpty()) && (dateTime == null || dateTime.isEmpty()) && (outcome == null || outcome.isEmpty())
           && (outcomeDesc == null || outcomeDesc.isEmpty());
      }

  }

    @Block()
    public static class SecurityEventParticipantComponent extends BackboneElement {
        /**
         * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.
         */
        @Child(name="role", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="User roles (e.g. local RBAC codes)", formalDefinition="Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context." )
        protected List<CodeableConcept> role;

        /**
         * Direct reference to a resource that identifies the participant.
         */
        @Child(name="reference", type={Practitioner.class, Patient.class, Device.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Direct reference to resource", formalDefinition="Direct reference to a resource that identifies the participant." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Direct reference to a resource that identifies the participant.)
         */
        protected Resource referenceTarget;

        /**
         * Unique identifier for the user actively participating in the event.
         */
        @Child(name="userId", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Unique identifier for the user", formalDefinition="Unique identifier for the user actively participating in the event." )
        protected StringType userId;

        /**
         * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available.
         */
        @Child(name="altId", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Alternative User id e.g. authentication", formalDefinition="Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available." )
        protected StringType altId;

        /**
         * Human-meaningful name for the user.
         */
        @Child(name="name", type={StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Human-meaningful name for the user", formalDefinition="Human-meaningful name for the user." )
        protected StringType name;

        /**
         * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         */
        @Child(name="requestor", type={BooleanType.class}, order=6, min=1, max=1)
        @Description(shortDefinition="Whether user is initiator", formalDefinition="Indicator that the user is or is not the requestor, or initiator, for the event being audited." )
        protected BooleanType requestor;

        /**
         * Type of media involved. Used when the event is about exporting/importing onto media.
         */
        @Child(name="media", type={Coding.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Type of media", formalDefinition="Type of media involved. Used when the event is about exporting/importing onto media." )
        protected Coding media;

        /**
         * Logical network location for application activity, if the activity has a network location.
         */
        @Child(name="network", type={}, order=8, min=0, max=1)
        @Description(shortDefinition="Logical network location for application activity", formalDefinition="Logical network location for application activity, if the activity has a network location." )
        protected SecurityEventParticipantNetworkComponent network;

        private static final long serialVersionUID = 594416195L;

      public SecurityEventParticipantComponent() {
        super();
      }

      public SecurityEventParticipantComponent(BooleanType requestor) {
        super();
        this.requestor = requestor;
      }

        /**
         * @return {@link #role} (Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        public boolean hasRole() { 
          if (this.role == null)
            return false;
          for (CodeableConcept item : this.role)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #role} (Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.)
         */
    // syntactic sugar
        public CodeableConcept addRole() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return t;
        }

        /**
         * @return {@link #reference} (Direct reference to a resource that identifies the participant.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Direct reference to a resource that identifies the participant.)
         */
        public SecurityEventParticipantComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Direct reference to a resource that identifies the participant.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Direct reference to a resource that identifies the participant.)
         */
        public SecurityEventParticipantComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #userId} (Unique identifier for the user actively participating in the event.). This is the underlying object with id, value and extensions. The accessor "getUserId" gives direct access to the value
         */
        public StringType getUserIdElement() { 
          if (this.userId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantComponent.userId");
            else if (Configuration.doAutoCreate())
              this.userId = new StringType(); // bb
          return this.userId;
        }

        public boolean hasUserIdElement() { 
          return this.userId != null && !this.userId.isEmpty();
        }

        public boolean hasUserId() { 
          return this.userId != null && !this.userId.isEmpty();
        }

        /**
         * @param value {@link #userId} (Unique identifier for the user actively participating in the event.). This is the underlying object with id, value and extensions. The accessor "getUserId" gives direct access to the value
         */
        public SecurityEventParticipantComponent setUserIdElement(StringType value) { 
          this.userId = value;
          return this;
        }

        /**
         * @return Unique identifier for the user actively participating in the event.
         */
        public String getUserId() { 
          return this.userId == null ? null : this.userId.getValue();
        }

        /**
         * @param value Unique identifier for the user actively participating in the event.
         */
        public SecurityEventParticipantComponent setUserId(String value) { 
          if (Utilities.noString(value))
            this.userId = null;
          else {
            if (this.userId == null)
              this.userId = new StringType();
            this.userId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #altId} (Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available.). This is the underlying object with id, value and extensions. The accessor "getAltId" gives direct access to the value
         */
        public StringType getAltIdElement() { 
          if (this.altId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantComponent.altId");
            else if (Configuration.doAutoCreate())
              this.altId = new StringType(); // bb
          return this.altId;
        }

        public boolean hasAltIdElement() { 
          return this.altId != null && !this.altId.isEmpty();
        }

        public boolean hasAltId() { 
          return this.altId != null && !this.altId.isEmpty();
        }

        /**
         * @param value {@link #altId} (Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available.). This is the underlying object with id, value and extensions. The accessor "getAltId" gives direct access to the value
         */
        public SecurityEventParticipantComponent setAltIdElement(StringType value) { 
          this.altId = value;
          return this;
        }

        /**
         * @return Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available.
         */
        public String getAltId() { 
          return this.altId == null ? null : this.altId.getValue();
        }

        /**
         * @param value Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available.
         */
        public SecurityEventParticipantComponent setAltId(String value) { 
          if (Utilities.noString(value))
            this.altId = null;
          else {
            if (this.altId == null)
              this.altId = new StringType();
            this.altId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #name} (Human-meaningful name for the user.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Human-meaningful name for the user.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SecurityEventParticipantComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Human-meaningful name for the user.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Human-meaningful name for the user.
         */
        public SecurityEventParticipantComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #requestor} (Indicator that the user is or is not the requestor, or initiator, for the event being audited.). This is the underlying object with id, value and extensions. The accessor "getRequestor" gives direct access to the value
         */
        public BooleanType getRequestorElement() { 
          if (this.requestor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantComponent.requestor");
            else if (Configuration.doAutoCreate())
              this.requestor = new BooleanType(); // bb
          return this.requestor;
        }

        public boolean hasRequestorElement() { 
          return this.requestor != null && !this.requestor.isEmpty();
        }

        public boolean hasRequestor() { 
          return this.requestor != null && !this.requestor.isEmpty();
        }

        /**
         * @param value {@link #requestor} (Indicator that the user is or is not the requestor, or initiator, for the event being audited.). This is the underlying object with id, value and extensions. The accessor "getRequestor" gives direct access to the value
         */
        public SecurityEventParticipantComponent setRequestorElement(BooleanType value) { 
          this.requestor = value;
          return this;
        }

        /**
         * @return Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         */
        public boolean getRequestor() { 
          return this.requestor == null ? false : this.requestor.getValue();
        }

        /**
         * @param value Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         */
        public SecurityEventParticipantComponent setRequestor(boolean value) { 
            if (this.requestor == null)
              this.requestor = new BooleanType();
            this.requestor.setValue(value);
          return this;
        }

        /**
         * @return {@link #media} (Type of media involved. Used when the event is about exporting/importing onto media.)
         */
        public Coding getMedia() { 
          if (this.media == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantComponent.media");
            else if (Configuration.doAutoCreate())
              this.media = new Coding(); // cc
          return this.media;
        }

        public boolean hasMedia() { 
          return this.media != null && !this.media.isEmpty();
        }

        /**
         * @param value {@link #media} (Type of media involved. Used when the event is about exporting/importing onto media.)
         */
        public SecurityEventParticipantComponent setMedia(Coding value) { 
          this.media = value;
          return this;
        }

        /**
         * @return {@link #network} (Logical network location for application activity, if the activity has a network location.)
         */
        public SecurityEventParticipantNetworkComponent getNetwork() { 
          if (this.network == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantComponent.network");
            else if (Configuration.doAutoCreate())
              this.network = new SecurityEventParticipantNetworkComponent(); // cc
          return this.network;
        }

        public boolean hasNetwork() { 
          return this.network != null && !this.network.isEmpty();
        }

        /**
         * @param value {@link #network} (Logical network location for application activity, if the activity has a network location.)
         */
        public SecurityEventParticipantComponent setNetwork(SecurityEventParticipantNetworkComponent value) { 
          this.network = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("reference", "Reference(Practitioner|Patient|Device)", "Direct reference to a resource that identifies the participant.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("userId", "string", "Unique identifier for the user actively participating in the event.", 0, java.lang.Integer.MAX_VALUE, userId));
          childrenList.add(new Property("altId", "string", "Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g., single sign-on), if available.", 0, java.lang.Integer.MAX_VALUE, altId));
          childrenList.add(new Property("name", "string", "Human-meaningful name for the user.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("requestor", "boolean", "Indicator that the user is or is not the requestor, or initiator, for the event being audited.", 0, java.lang.Integer.MAX_VALUE, requestor));
          childrenList.add(new Property("media", "Coding", "Type of media involved. Used when the event is about exporting/importing onto media.", 0, java.lang.Integer.MAX_VALUE, media));
          childrenList.add(new Property("network", "", "Logical network location for application activity, if the activity has a network location.", 0, java.lang.Integer.MAX_VALUE, network));
        }

      public SecurityEventParticipantComponent copy() {
        SecurityEventParticipantComponent dst = new SecurityEventParticipantComponent();
        copyValues(dst);
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        dst.reference = reference == null ? null : reference.copy();
        dst.userId = userId == null ? null : userId.copy();
        dst.altId = altId == null ? null : altId.copy();
        dst.name = name == null ? null : name.copy();
        dst.requestor = requestor == null ? null : requestor.copy();
        dst.media = media == null ? null : media.copy();
        dst.network = network == null ? null : network.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SecurityEventParticipantComponent))
          return false;
        SecurityEventParticipantComponent o = (SecurityEventParticipantComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(reference, o.reference, true) && compareDeep(userId, o.userId, true)
           && compareDeep(altId, o.altId, true) && compareDeep(name, o.name, true) && compareDeep(requestor, o.requestor, true)
           && compareDeep(media, o.media, true) && compareDeep(network, o.network, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SecurityEventParticipantComponent))
          return false;
        SecurityEventParticipantComponent o = (SecurityEventParticipantComponent) other;
        return compareValues(userId, o.userId, true) && compareValues(altId, o.altId, true) && compareValues(name, o.name, true)
           && compareValues(requestor, o.requestor, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (role == null || role.isEmpty()) && (reference == null || reference.isEmpty())
           && (userId == null || userId.isEmpty()) && (altId == null || altId.isEmpty()) && (name == null || name.isEmpty())
           && (requestor == null || requestor.isEmpty()) && (media == null || media.isEmpty()) && (network == null || network.isEmpty())
          ;
      }

  }

    @Block()
    public static class SecurityEventParticipantNetworkComponent extends BackboneElement {
        /**
         * An identifier for the network access point of the user device for the audit event.
         */
        @Child(name="identifier", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Identifier for the network access point of the user device", formalDefinition="An identifier for the network access point of the user device for the audit event." )
        protected StringType identifier;

        /**
         * An identifier for the type of network access point that originated the audit event.
         */
        @Child(name="type", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The type of network access point", formalDefinition="An identifier for the type of network access point that originated the audit event." )
        protected Enumeration<NetworkType> type;

        private static final long serialVersionUID = -1946856025L;

      public SecurityEventParticipantNetworkComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (An identifier for the network access point of the user device for the audit event.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantNetworkComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new StringType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (An identifier for the network access point of the user device for the audit event.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public SecurityEventParticipantNetworkComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return An identifier for the network access point of the user device for the audit event.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value An identifier for the network access point of the user device for the audit event.
         */
        public SecurityEventParticipantNetworkComponent setIdentifier(String value) { 
          if (Utilities.noString(value))
            this.identifier = null;
          else {
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (An identifier for the type of network access point that originated the audit event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<NetworkType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventParticipantNetworkComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<NetworkType>(new NetworkTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (An identifier for the type of network access point that originated the audit event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public SecurityEventParticipantNetworkComponent setTypeElement(Enumeration<NetworkType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return An identifier for the type of network access point that originated the audit event.
         */
        public NetworkType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value An identifier for the type of network access point that originated the audit event.
         */
        public SecurityEventParticipantNetworkComponent setType(NetworkType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<NetworkType>(new NetworkTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "string", "An identifier for the network access point of the user device for the audit event.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("type", "code", "An identifier for the type of network access point that originated the audit event.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      public SecurityEventParticipantNetworkComponent copy() {
        SecurityEventParticipantNetworkComponent dst = new SecurityEventParticipantNetworkComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SecurityEventParticipantNetworkComponent))
          return false;
        SecurityEventParticipantNetworkComponent o = (SecurityEventParticipantNetworkComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SecurityEventParticipantNetworkComponent))
          return false;
        SecurityEventParticipantNetworkComponent o = (SecurityEventParticipantNetworkComponent) other;
        return compareValues(identifier, o.identifier, true) && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
          ;
      }

  }

    @Block()
    public static class SecurityEventSourceComponent extends BackboneElement {
        /**
         * Logical source location within the healthcare enterprise network.
         */
        @Child(name="site", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Logical source location within the enterprise", formalDefinition="Logical source location within the healthcare enterprise network." )
        protected StringType site;

        /**
         * Identifier of the source where the event originated.
         */
        @Child(name="identifier", type={StringType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="The id of source where event originated", formalDefinition="Identifier of the source where the event originated." )
        protected StringType identifier;

        /**
         * Code specifying the type of source where event originated.
         */
        @Child(name="type", type={Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="The type of source where event originated", formalDefinition="Code specifying the type of source where event originated." )
        protected List<Coding> type;

        private static final long serialVersionUID = -382040480L;

      public SecurityEventSourceComponent() {
        super();
      }

      public SecurityEventSourceComponent(StringType identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #site} (Logical source location within the healthcare enterprise network.). This is the underlying object with id, value and extensions. The accessor "getSite" gives direct access to the value
         */
        public StringType getSiteElement() { 
          if (this.site == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventSourceComponent.site");
            else if (Configuration.doAutoCreate())
              this.site = new StringType(); // bb
          return this.site;
        }

        public boolean hasSiteElement() { 
          return this.site != null && !this.site.isEmpty();
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (Logical source location within the healthcare enterprise network.). This is the underlying object with id, value and extensions. The accessor "getSite" gives direct access to the value
         */
        public SecurityEventSourceComponent setSiteElement(StringType value) { 
          this.site = value;
          return this;
        }

        /**
         * @return Logical source location within the healthcare enterprise network.
         */
        public String getSite() { 
          return this.site == null ? null : this.site.getValue();
        }

        /**
         * @param value Logical source location within the healthcare enterprise network.
         */
        public SecurityEventSourceComponent setSite(String value) { 
          if (Utilities.noString(value))
            this.site = null;
          else {
            if (this.site == null)
              this.site = new StringType();
            this.site.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #identifier} (Identifier of the source where the event originated.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventSourceComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new StringType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifier of the source where the event originated.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public SecurityEventSourceComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return Identifier of the source where the event originated.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value Identifier of the source where the event originated.
         */
        public SecurityEventSourceComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (Code specifying the type of source where event originated.)
         */
        public List<Coding> getType() { 
          if (this.type == null)
            this.type = new ArrayList<Coding>();
          return this.type;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (Coding item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #type} (Code specifying the type of source where event originated.)
         */
    // syntactic sugar
        public Coding addType() { //3
          Coding t = new Coding();
          if (this.type == null)
            this.type = new ArrayList<Coding>();
          this.type.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("site", "string", "Logical source location within the healthcare enterprise network.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("identifier", "string", "Identifier of the source where the event originated.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("type", "Coding", "Code specifying the type of source where event originated.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      public SecurityEventSourceComponent copy() {
        SecurityEventSourceComponent dst = new SecurityEventSourceComponent();
        copyValues(dst);
        dst.site = site == null ? null : site.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        if (type != null) {
          dst.type = new ArrayList<Coding>();
          for (Coding i : type)
            dst.type.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SecurityEventSourceComponent))
          return false;
        SecurityEventSourceComponent o = (SecurityEventSourceComponent) other;
        return compareDeep(site, o.site, true) && compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SecurityEventSourceComponent))
          return false;
        SecurityEventSourceComponent o = (SecurityEventSourceComponent) other;
        return compareValues(site, o.site, true) && compareValues(identifier, o.identifier, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (site == null || site.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (type == null || type.isEmpty());
      }

  }

    @Block()
    public static class SecurityEventObjectComponent extends BackboneElement {
        /**
         * Identifies a specific instance of the participant object. The reference should always be version specific.
         */
        @Child(name="identifier", type={Identifier.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Specific instance of object (e.g. versioned)", formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific." )
        protected Identifier identifier;

        /**
         * Identifies a specific instance of the participant object. The reference should always be version specific.
         */
        @Child(name="reference", type={}, order=2, min=0, max=1)
        @Description(shortDefinition="Specific instance of resource (e.g. versioned)", formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        protected Resource referenceTarget;

        /**
         * Object type being audited.
         */
        @Child(name="type", type={CodeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Object type being audited", formalDefinition="Object type being audited." )
        protected Enumeration<ObjectType> type;

        /**
         * Code representing the functional application role of Participant Object being audited.
         */
        @Child(name="role", type={CodeType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Functional application role of Object", formalDefinition="Code representing the functional application role of Participant Object being audited." )
        protected Enumeration<ObjectRole> role;

        /**
         * Identifier for the data life-cycle stage for the participant object.
         */
        @Child(name="lifecycle", type={CodeType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Life-cycle stage for the object", formalDefinition="Identifier for the data life-cycle stage for the participant object." )
        protected Enumeration<ObjectLifecycle> lifecycle;

        /**
         * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics.
         */
        @Child(name="sensitivity", type={CodeableConcept.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Policy-defined sensitivity for the object", formalDefinition="Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics." )
        protected CodeableConcept sensitivity;

        /**
         * An instance-specific descriptor of the Participant Object ID audited, such as a person's name.
         */
        @Child(name="name", type={StringType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Instance-specific descriptor for Object", formalDefinition="An instance-specific descriptor of the Participant Object ID audited, such as a person's name." )
        protected StringType name;

        /**
         * Text that describes the object in more detail.
         */
        @Child(name="description", type={StringType.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Descriptive text", formalDefinition="Text that describes the object in more detail." )
        protected StringType description;

        /**
         * The actual query for a query-type participant object.
         */
        @Child(name="query", type={Base64BinaryType.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Actual query for object", formalDefinition="The actual query for a query-type participant object." )
        protected Base64BinaryType query;

        /**
         * Additional Information about the Object.
         */
        @Child(name="detail", type={}, order=10, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Additional Information about the Object", formalDefinition="Additional Information about the Object." )
        protected List<SecurityEventObjectDetailComponent> detail;

        private static final long serialVersionUID = -268126947L;

      public SecurityEventObjectComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public SecurityEventObjectComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #reference} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public SecurityEventObjectComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public SecurityEventObjectComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #type} (Object type being audited.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ObjectType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ObjectType>(new ObjectTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Object type being audited.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public SecurityEventObjectComponent setTypeElement(Enumeration<ObjectType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Object type being audited.
         */
        public ObjectType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Object type being audited.
         */
        public SecurityEventObjectComponent setType(ObjectType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<ObjectType>(new ObjectTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #role} (Code representing the functional application role of Participant Object being audited.). This is the underlying object with id, value and extensions. The accessor "getRole" gives direct access to the value
         */
        public Enumeration<ObjectRole> getRoleElement() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new Enumeration<ObjectRole>(new ObjectRoleEnumFactory()); // bb
          return this.role;
        }

        public boolean hasRoleElement() { 
          return this.role != null && !this.role.isEmpty();
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Code representing the functional application role of Participant Object being audited.). This is the underlying object with id, value and extensions. The accessor "getRole" gives direct access to the value
         */
        public SecurityEventObjectComponent setRoleElement(Enumeration<ObjectRole> value) { 
          this.role = value;
          return this;
        }

        /**
         * @return Code representing the functional application role of Participant Object being audited.
         */
        public ObjectRole getRole() { 
          return this.role == null ? null : this.role.getValue();
        }

        /**
         * @param value Code representing the functional application role of Participant Object being audited.
         */
        public SecurityEventObjectComponent setRole(ObjectRole value) { 
          if (value == null)
            this.role = null;
          else {
            if (this.role == null)
              this.role = new Enumeration<ObjectRole>(new ObjectRoleEnumFactory());
            this.role.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #lifecycle} (Identifier for the data life-cycle stage for the participant object.). This is the underlying object with id, value and extensions. The accessor "getLifecycle" gives direct access to the value
         */
        public Enumeration<ObjectLifecycle> getLifecycleElement() { 
          if (this.lifecycle == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.lifecycle");
            else if (Configuration.doAutoCreate())
              this.lifecycle = new Enumeration<ObjectLifecycle>(new ObjectLifecycleEnumFactory()); // bb
          return this.lifecycle;
        }

        public boolean hasLifecycleElement() { 
          return this.lifecycle != null && !this.lifecycle.isEmpty();
        }

        public boolean hasLifecycle() { 
          return this.lifecycle != null && !this.lifecycle.isEmpty();
        }

        /**
         * @param value {@link #lifecycle} (Identifier for the data life-cycle stage for the participant object.). This is the underlying object with id, value and extensions. The accessor "getLifecycle" gives direct access to the value
         */
        public SecurityEventObjectComponent setLifecycleElement(Enumeration<ObjectLifecycle> value) { 
          this.lifecycle = value;
          return this;
        }

        /**
         * @return Identifier for the data life-cycle stage for the participant object.
         */
        public ObjectLifecycle getLifecycle() { 
          return this.lifecycle == null ? null : this.lifecycle.getValue();
        }

        /**
         * @param value Identifier for the data life-cycle stage for the participant object.
         */
        public SecurityEventObjectComponent setLifecycle(ObjectLifecycle value) { 
          if (value == null)
            this.lifecycle = null;
          else {
            if (this.lifecycle == null)
              this.lifecycle = new Enumeration<ObjectLifecycle>(new ObjectLifecycleEnumFactory());
            this.lifecycle.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #sensitivity} (Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics.)
         */
        public CodeableConcept getSensitivity() { 
          if (this.sensitivity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.sensitivity");
            else if (Configuration.doAutoCreate())
              this.sensitivity = new CodeableConcept(); // cc
          return this.sensitivity;
        }

        public boolean hasSensitivity() { 
          return this.sensitivity != null && !this.sensitivity.isEmpty();
        }

        /**
         * @param value {@link #sensitivity} (Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics.)
         */
        public SecurityEventObjectComponent setSensitivity(CodeableConcept value) { 
          this.sensitivity = value;
          return this;
        }

        /**
         * @return {@link #name} (An instance-specific descriptor of the Participant Object ID audited, such as a person's name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (An instance-specific descriptor of the Participant Object ID audited, such as a person's name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SecurityEventObjectComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return An instance-specific descriptor of the Participant Object ID audited, such as a person's name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value An instance-specific descriptor of the Participant Object ID audited, such as a person's name.
         */
        public SecurityEventObjectComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (Text that describes the object in more detail.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (Text that describes the object in more detail.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public SecurityEventObjectComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Text that describes the object in more detail.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Text that describes the object in more detail.
         */
        public SecurityEventObjectComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #query} (The actual query for a query-type participant object.). This is the underlying object with id, value and extensions. The accessor "getQuery" gives direct access to the value
         */
        public Base64BinaryType getQueryElement() { 
          if (this.query == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectComponent.query");
            else if (Configuration.doAutoCreate())
              this.query = new Base64BinaryType(); // bb
          return this.query;
        }

        public boolean hasQueryElement() { 
          return this.query != null && !this.query.isEmpty();
        }

        public boolean hasQuery() { 
          return this.query != null && !this.query.isEmpty();
        }

        /**
         * @param value {@link #query} (The actual query for a query-type participant object.). This is the underlying object with id, value and extensions. The accessor "getQuery" gives direct access to the value
         */
        public SecurityEventObjectComponent setQueryElement(Base64BinaryType value) { 
          this.query = value;
          return this;
        }

        /**
         * @return The actual query for a query-type participant object.
         */
        public byte[] getQuery() { 
          return this.query == null ? null : this.query.getValue();
        }

        /**
         * @param value The actual query for a query-type participant object.
         */
        public SecurityEventObjectComponent setQuery(byte[] value) { 
          if (value == null)
            this.query = null;
          else {
            if (this.query == null)
              this.query = new Base64BinaryType();
            this.query.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #detail} (Additional Information about the Object.)
         */
        public List<SecurityEventObjectDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<SecurityEventObjectDetailComponent>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (SecurityEventObjectDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (Additional Information about the Object.)
         */
    // syntactic sugar
        public SecurityEventObjectDetailComponent addDetail() { //3
          SecurityEventObjectDetailComponent t = new SecurityEventObjectDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<SecurityEventObjectDetailComponent>();
          this.detail.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Identifies a specific instance of the participant object. The reference should always be version specific.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("reference", "Reference(Any)", "Identifies a specific instance of the participant object. The reference should always be version specific.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("type", "code", "Object type being audited.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("role", "code", "Code representing the functional application role of Participant Object being audited.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("lifecycle", "code", "Identifier for the data life-cycle stage for the participant object.", 0, java.lang.Integer.MAX_VALUE, lifecycle));
          childrenList.add(new Property("sensitivity", "CodeableConcept", "Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status, mental health status or similar topics.", 0, java.lang.Integer.MAX_VALUE, sensitivity));
          childrenList.add(new Property("name", "string", "An instance-specific descriptor of the Participant Object ID audited, such as a person's name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "Text that describes the object in more detail.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("query", "base64Binary", "The actual query for a query-type participant object.", 0, java.lang.Integer.MAX_VALUE, query));
          childrenList.add(new Property("detail", "", "Additional Information about the Object.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      public SecurityEventObjectComponent copy() {
        SecurityEventObjectComponent dst = new SecurityEventObjectComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.reference = reference == null ? null : reference.copy();
        dst.type = type == null ? null : type.copy();
        dst.role = role == null ? null : role.copy();
        dst.lifecycle = lifecycle == null ? null : lifecycle.copy();
        dst.sensitivity = sensitivity == null ? null : sensitivity.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.query = query == null ? null : query.copy();
        if (detail != null) {
          dst.detail = new ArrayList<SecurityEventObjectDetailComponent>();
          for (SecurityEventObjectDetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SecurityEventObjectComponent))
          return false;
        SecurityEventObjectComponent o = (SecurityEventObjectComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(reference, o.reference, true)
           && compareDeep(type, o.type, true) && compareDeep(role, o.role, true) && compareDeep(lifecycle, o.lifecycle, true)
           && compareDeep(sensitivity, o.sensitivity, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(query, o.query, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SecurityEventObjectComponent))
          return false;
        SecurityEventObjectComponent o = (SecurityEventObjectComponent) other;
        return compareValues(type, o.type, true) && compareValues(role, o.role, true) && compareValues(lifecycle, o.lifecycle, true)
           && compareValues(name, o.name, true) && compareValues(description, o.description, true) && compareValues(query, o.query, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (reference == null || reference.isEmpty())
           && (type == null || type.isEmpty()) && (role == null || role.isEmpty()) && (lifecycle == null || lifecycle.isEmpty())
           && (sensitivity == null || sensitivity.isEmpty()) && (name == null || name.isEmpty()) && (description == null || description.isEmpty())
           && (query == null || query.isEmpty()) && (detail == null || detail.isEmpty());
      }

  }

    @Block()
    public static class SecurityEventObjectDetailComponent extends BackboneElement {
        /**
         * Name of the property.
         */
        @Child(name="type", type={StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Name of the property", formalDefinition="Name of the property." )
        protected StringType type;

        /**
         * Property value.
         */
        @Child(name="value", type={Base64BinaryType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Property value", formalDefinition="Property value." )
        protected Base64BinaryType value;

        private static final long serialVersionUID = 11139504L;

      public SecurityEventObjectDetailComponent() {
        super();
      }

      public SecurityEventObjectDetailComponent(StringType type, Base64BinaryType value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (Name of the property.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectDetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Name of the property.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public SecurityEventObjectDetailComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Name of the property.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Name of the property.
         */
        public SecurityEventObjectDetailComponent setType(String value) { 
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (Property value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public Base64BinaryType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityEventObjectDetailComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new Base64BinaryType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Property value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public SecurityEventObjectDetailComponent setValueElement(Base64BinaryType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return Property value.
         */
        public byte[] getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value Property value.
         */
        public SecurityEventObjectDetailComponent setValue(byte[] value) { 
            if (this.value == null)
              this.value = new Base64BinaryType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "string", "Name of the property.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value", "base64Binary", "Property value.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public SecurityEventObjectDetailComponent copy() {
        SecurityEventObjectDetailComponent dst = new SecurityEventObjectDetailComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SecurityEventObjectDetailComponent))
          return false;
        SecurityEventObjectDetailComponent o = (SecurityEventObjectDetailComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SecurityEventObjectDetailComponent))
          return false;
        SecurityEventObjectDetailComponent o = (SecurityEventObjectDetailComponent) other;
        return compareValues(type, o.type, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  }

    /**
     * Identifies the name, action type, time, and disposition of the audited event.
     */
    @Child(name = "event", type = {}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="What was done", formalDefinition="Identifies the name, action type, time, and disposition of the audited event." )
    protected SecurityEventEventComponent event;

    /**
     * A person, a hardware device or software process.
     */
    @Child(name = "participant", type = {}, order = 1, min = 1, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="A person, a hardware device or software process", formalDefinition="A person, a hardware device or software process." )
    protected List<SecurityEventParticipantComponent> participant;

    /**
     * Application systems and processes.
     */
    @Child(name = "source", type = {}, order = 2, min = 1, max = 1)
    @Description(shortDefinition="Application systems and processes", formalDefinition="Application systems and processes." )
    protected SecurityEventSourceComponent source;

    /**
     * Specific instances of data or objects that have been accessed.
     */
    @Child(name = "object", type = {}, order = 3, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Specific instances of data or objects that have been accessed", formalDefinition="Specific instances of data or objects that have been accessed." )
    protected List<SecurityEventObjectComponent> object;

    private static final long serialVersionUID = -1695871760L;

    public SecurityEvent() {
      super();
    }

    public SecurityEvent(SecurityEventEventComponent event, SecurityEventSourceComponent source) {
      super();
      this.event = event;
      this.source = source;
    }

    /**
     * @return {@link #event} (Identifies the name, action type, time, and disposition of the audited event.)
     */
    public SecurityEventEventComponent getEvent() { 
      if (this.event == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SecurityEvent.event");
        else if (Configuration.doAutoCreate())
          this.event = new SecurityEventEventComponent(); // cc
      return this.event;
    }

    public boolean hasEvent() { 
      return this.event != null && !this.event.isEmpty();
    }

    /**
     * @param value {@link #event} (Identifies the name, action type, time, and disposition of the audited event.)
     */
    public SecurityEvent setEvent(SecurityEventEventComponent value) { 
      this.event = value;
      return this;
    }

    /**
     * @return {@link #participant} (A person, a hardware device or software process.)
     */
    public List<SecurityEventParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<SecurityEventParticipantComponent>();
      return this.participant;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (SecurityEventParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participant} (A person, a hardware device or software process.)
     */
    // syntactic sugar
    public SecurityEventParticipantComponent addParticipant() { //3
      SecurityEventParticipantComponent t = new SecurityEventParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<SecurityEventParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    /**
     * @return {@link #source} (Application systems and processes.)
     */
    public SecurityEventSourceComponent getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SecurityEvent.source");
        else if (Configuration.doAutoCreate())
          this.source = new SecurityEventSourceComponent(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (Application systems and processes.)
     */
    public SecurityEvent setSource(SecurityEventSourceComponent value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #object} (Specific instances of data or objects that have been accessed.)
     */
    public List<SecurityEventObjectComponent> getObject() { 
      if (this.object == null)
        this.object = new ArrayList<SecurityEventObjectComponent>();
      return this.object;
    }

    public boolean hasObject() { 
      if (this.object == null)
        return false;
      for (SecurityEventObjectComponent item : this.object)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #object} (Specific instances of data or objects that have been accessed.)
     */
    // syntactic sugar
    public SecurityEventObjectComponent addObject() { //3
      SecurityEventObjectComponent t = new SecurityEventObjectComponent();
      if (this.object == null)
        this.object = new ArrayList<SecurityEventObjectComponent>();
      this.object.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("event", "", "Identifies the name, action type, time, and disposition of the audited event.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("participant", "", "A person, a hardware device or software process.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("source", "", "Application systems and processes.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("object", "", "Specific instances of data or objects that have been accessed.", 0, java.lang.Integer.MAX_VALUE, object));
      }

      public SecurityEvent copy() {
        SecurityEvent dst = new SecurityEvent();
        copyValues(dst);
        dst.event = event == null ? null : event.copy();
        if (participant != null) {
          dst.participant = new ArrayList<SecurityEventParticipantComponent>();
          for (SecurityEventParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.source = source == null ? null : source.copy();
        if (object != null) {
          dst.object = new ArrayList<SecurityEventObjectComponent>();
          for (SecurityEventObjectComponent i : object)
            dst.object.add(i.copy());
        };
        return dst;
      }

      protected SecurityEvent typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SecurityEvent))
          return false;
        SecurityEvent o = (SecurityEvent) other;
        return compareDeep(event, o.event, true) && compareDeep(participant, o.participant, true) && compareDeep(source, o.source, true)
           && compareDeep(object, o.object, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SecurityEvent))
          return false;
        SecurityEvent o = (SecurityEvent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (event == null || event.isEmpty()) && (participant == null || participant.isEmpty())
           && (source == null || source.isEmpty()) && (object == null || object.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SecurityEvent;
   }

  @SearchParamDefinition(name="date", path="SecurityEvent.event.dateTime", description="Time when the event occurred on source", type="date" )
  public static final String SP_DATE = "date";
    @SearchParamDefinition(name = "address", path = "SecurityEvent.participant.network.identifier", description = "Identifier for the network access point of the user device", type = "token")
    public static final String SP_ADDRESS = "address";
  @SearchParamDefinition(name="patientid", path="", description="The id of the patient (one of multiple kinds of participations)", type="token" )
  public static final String SP_PATIENTID = "patientid";
  @SearchParamDefinition(name="source", path="SecurityEvent.source.identifier", description="The id of source where event originated", type="token" )
  public static final String SP_SOURCE = "source";
    @SearchParamDefinition(name = "type", path = "SecurityEvent.event.type", description = "Type/identifier of event", type = "token")
    public static final String SP_TYPE = "type";
    @SearchParamDefinition(name = "altid", path = "SecurityEvent.participant.altId", description = "Alternative User id e.g. authentication", type = "token")
    public static final String SP_ALTID = "altid";
    @SearchParamDefinition(name = "reference", path = "SecurityEvent.object.reference", description = "Specific instance of resource (e.g. versioned)", type = "reference")
    public static final String SP_REFERENCE = "reference";
    @SearchParamDefinition(name = "site", path = "SecurityEvent.source.site", description = "Logical source location within the enterprise", type = "token")
    public static final String SP_SITE = "site";
  @SearchParamDefinition(name="subtype", path="SecurityEvent.event.subtype", description="More specific type/id for the event", type="token" )
  public static final String SP_SUBTYPE = "subtype";
    @SearchParamDefinition(name = "identity", path = "SecurityEvent.object.identifier", description = "Specific instance of object (e.g. versioned)", type = "token")
    public static final String SP_IDENTITY = "identity";
    @SearchParamDefinition(name = "patient", path = "", description = "A patient that the .object.reference refers to", type = "reference")
    public static final String SP_PATIENT = "patient";
    @SearchParamDefinition(name = "object-type", path = "SecurityEvent.object.type", description = "Object type being audited", type = "token")
    public static final String SP_OBJECTTYPE = "object-type";
  @SearchParamDefinition(name="name", path="SecurityEvent.participant.name", description="Human-meaningful name for the user", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="action", path="SecurityEvent.event.action", description="Type of action performed during the event", type="token" )
  public static final String SP_ACTION = "action";
  @SearchParamDefinition(name="user", path="SecurityEvent.participant.userId", description="Unique identifier for the user", type="token" )
  public static final String SP_USER = "user";
    @SearchParamDefinition(name = "desc", path = "SecurityEvent.object.name", description = "Instance-specific descriptor for Object", type = "string")
    public static final String SP_DESC = "desc";

}

