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
         * A logical object related to a health record event.  This is any healthcare  specific resource (object) not restricted to FHIR defined Resources.
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
         * (deprecated)
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
         * A source or destination for data transfer that acts as an archive, database, or similar role.
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
         * An order, task, work item, procedure step, or other description of work to be performed; e.g. a particular instance of an MPPS.
         */
        _20, 
        /**
         * A list of jobs or a system that provides lists of jobs; e.g. an MWL SCP.
         */
        _21, 
        /**
         * (Deprecated)
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
          return "http://hl7.org/fhir/object-role";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "This object is the patient that is the subject of care related to this event.  It is identifiable by patient ID or equivalent.  The patient may be either human or animal.";
            case _2: return "This is a location identified as related to the event.  This is usually the location where the event took place.  Note that for shipping, the usual events are arrival at a location or departure from a location.";
            case _3: return "This object is any kind of persistent document created as a result of the event.  This could be a paper report, film, electronic report, DICOM Study, etc.  Issues related to medical records life cycle management are conveyed elsewhere.";
            case _4: return "A logical object related to a health record event.  This is any healthcare  specific resource (object) not restricted to FHIR defined Resources.";
            case _5: return "This is any configurable file used to control creation of documents.  Examples include the objects maintained by the HL7 Master File transactions, Value Sets, etc.";
            case _6: return "A human participant not otherwise identified by some other category.";
            case _7: return "(deprecated)";
            case _8: return "Typically a licensed person who is providing or performing care related to the event, generally a physician.   The key distinction between doctor and practitioner is with regards to their role, not the licensing.  The doctor is the human who actually performed the work.  The practitioner is the human or organization that is responsible for the work.";
            case _9: return "A person or system that is being notified as part of the event.  This is relevant in situations where automated systems provide notifications to other parties when an event took place.";
            case _10: return "Insurance company, or any other organization who accepts responsibility for paying for the healthcare event.";
            case _11: return "A person or active system object involved in the event with a security role.";
            case _12: return "A person or system object involved in the event with the authority to modify security roles of other objects.";
            case _13: return "A passive object, such as a role table, that is relevant to the event.";
            case _14: return "(deprecated)  Relevant to certain RBAC security methodologies.";
            case _15: return "Any person or organization responsible for providing care.  This encompasses all forms of care, licensed or otherwise, and all sorts of teams and care groups. Note, the distinction between practitioners and the doctor that actually provided the care to the patient.";
            case _16: return "The source or destination for data transfer, when it does not match some other role.";
            case _17: return "A source or destination for data transfer that acts as an archive, database, or similar role.";
            case _18: return "An object that holds schedule information.  This could be an appointment book, availability information, etc.";
            case _19: return "An organization or person that is the recipient of services.  This could be an organization that is buying services for a patient, or a person that is buying services for an animal.";
            case _20: return "An order, task, work item, procedure step, or other description of work to be performed; e.g. a particular instance of an MPPS.";
            case _21: return "A list of jobs or a system that provides lists of jobs; e.g. an MWL SCP.";
            case _22: return "(Deprecated)";
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
            case _4: return "Domain Resource";
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

