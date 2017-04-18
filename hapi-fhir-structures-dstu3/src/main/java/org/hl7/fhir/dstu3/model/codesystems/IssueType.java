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

public enum IssueType {

        /**
         * Content invalid against the specification or a profile.
         */
        INVALID, 
        /**
         * A structural issue in the content such as wrong namespace, or unable to parse the content completely, or invalid json syntax.
         */
        STRUCTURE, 
        /**
         * A required element is missing.
         */
        REQUIRED, 
        /**
         * An element value is invalid.
         */
        VALUE, 
        /**
         * A content validation rule failed - e.g. a schematron rule.
         */
        INVARIANT, 
        /**
         * An authentication/authorization/permissions issue of some kind.
         */
        SECURITY, 
        /**
         * The client needs to initiate an authentication process.
         */
        LOGIN, 
        /**
         * The user or system was not able to be authenticated (either there is no process, or the proferred token is unacceptable).
         */
        UNKNOWN, 
        /**
         * User session expired; a login may be required.
         */
        EXPIRED, 
        /**
         * The user does not have the rights to perform this action.
         */
        FORBIDDEN, 
        /**
         * Some information was not or may not have been returned due to business rules, consent or privacy rules, or access permission constraints.  This information may be accessible through alternate processes.
         */
        SUPPRESSED, 
        /**
         * Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged.
         */
        PROCESSING, 
        /**
         * The resource or profile is not supported.
         */
        NOTSUPPORTED, 
        /**
         * An attempt was made to create a duplicate record.
         */
        DUPLICATE, 
        /**
         * The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the content is not found further into the application architecture.
         */
        NOTFOUND, 
        /**
         * Provided content is too long (typically, this is a denial of service protection type of error).
         */
        TOOLONG, 
        /**
         * The code or system could not be understood, or it was not valid in the context of a particular ValueSet.code.
         */
        CODEINVALID, 
        /**
         * An extension was found that was not acceptable, could not be resolved, or a modifierExtension was not recognized.
         */
        EXTENSION, 
        /**
         * The operation was stopped to protect server resources; e.g. a request for a value set expansion on all of SNOMED CT.
         */
        TOOCOSTLY, 
        /**
         * The content/operation failed to pass some business rule, and so could not proceed.
         */
        BUSINESSRULE, 
        /**
         * Content could not be accepted because of an edit conflict (i.e. version aware updates) (In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the conflict is discovered further into the application architecture.)
         */
        CONFLICT, 
        /**
         * Not all data sources typically accessed could be reached, or responded in time, so the returned information may not be complete.
         */
        INCOMPLETE, 
        /**
         * Transient processing issues. The system receiving the error may be able to resubmit the same content once an underlying issue is resolved.
         */
        TRANSIENT, 
        /**
         * A resource/record locking failure (usually in an underlying database).
         */
        LOCKERROR, 
        /**
         * The persistent store is unavailable; e.g. the database is down for maintenance or similar action.
         */
        NOSTORE, 
        /**
         * An unexpected internal error has occurred.
         */
        EXCEPTION, 
        /**
         * An internal timeout has occurred.
         */
        TIMEOUT, 
        /**
         * The system is not prepared to handle this request due to load management.
         */
        THROTTLED, 
        /**
         * A message unrelated to the processing success of the completed operation (examples of the latter include things like reminders of password expiry, system maintenance times, etc.).
         */
        INFORMATIONAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IssueType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("invalid".equals(codeString))
          return INVALID;
        if ("structure".equals(codeString))
          return STRUCTURE;
        if ("required".equals(codeString))
          return REQUIRED;
        if ("value".equals(codeString))
          return VALUE;
        if ("invariant".equals(codeString))
          return INVARIANT;
        if ("security".equals(codeString))
          return SECURITY;
        if ("login".equals(codeString))
          return LOGIN;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("expired".equals(codeString))
          return EXPIRED;
        if ("forbidden".equals(codeString))
          return FORBIDDEN;
        if ("suppressed".equals(codeString))
          return SUPPRESSED;
        if ("processing".equals(codeString))
          return PROCESSING;
        if ("not-supported".equals(codeString))
          return NOTSUPPORTED;
        if ("duplicate".equals(codeString))
          return DUPLICATE;
        if ("not-found".equals(codeString))
          return NOTFOUND;
        if ("too-long".equals(codeString))
          return TOOLONG;
        if ("code-invalid".equals(codeString))
          return CODEINVALID;
        if ("extension".equals(codeString))
          return EXTENSION;
        if ("too-costly".equals(codeString))
          return TOOCOSTLY;
        if ("business-rule".equals(codeString))
          return BUSINESSRULE;
        if ("conflict".equals(codeString))
          return CONFLICT;
        if ("incomplete".equals(codeString))
          return INCOMPLETE;
        if ("transient".equals(codeString))
          return TRANSIENT;
        if ("lock-error".equals(codeString))
          return LOCKERROR;
        if ("no-store".equals(codeString))
          return NOSTORE;
        if ("exception".equals(codeString))
          return EXCEPTION;
        if ("timeout".equals(codeString))
          return TIMEOUT;
        if ("throttled".equals(codeString))
          return THROTTLED;
        if ("informational".equals(codeString))
          return INFORMATIONAL;
        throw new FHIRException("Unknown IssueType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INVALID: return "invalid";
            case STRUCTURE: return "structure";
            case REQUIRED: return "required";
            case VALUE: return "value";
            case INVARIANT: return "invariant";
            case SECURITY: return "security";
            case LOGIN: return "login";
            case UNKNOWN: return "unknown";
            case EXPIRED: return "expired";
            case FORBIDDEN: return "forbidden";
            case SUPPRESSED: return "suppressed";
            case PROCESSING: return "processing";
            case NOTSUPPORTED: return "not-supported";
            case DUPLICATE: return "duplicate";
            case NOTFOUND: return "not-found";
            case TOOLONG: return "too-long";
            case CODEINVALID: return "code-invalid";
            case EXTENSION: return "extension";
            case TOOCOSTLY: return "too-costly";
            case BUSINESSRULE: return "business-rule";
            case CONFLICT: return "conflict";
            case INCOMPLETE: return "incomplete";
            case TRANSIENT: return "transient";
            case LOCKERROR: return "lock-error";
            case NOSTORE: return "no-store";
            case EXCEPTION: return "exception";
            case TIMEOUT: return "timeout";
            case THROTTLED: return "throttled";
            case INFORMATIONAL: return "informational";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/issue-type";
        }
        public String getDefinition() {
          switch (this) {
            case INVALID: return "Content invalid against the specification or a profile.";
            case STRUCTURE: return "A structural issue in the content such as wrong namespace, or unable to parse the content completely, or invalid json syntax.";
            case REQUIRED: return "A required element is missing.";
            case VALUE: return "An element value is invalid.";
            case INVARIANT: return "A content validation rule failed - e.g. a schematron rule.";
            case SECURITY: return "An authentication/authorization/permissions issue of some kind.";
            case LOGIN: return "The client needs to initiate an authentication process.";
            case UNKNOWN: return "The user or system was not able to be authenticated (either there is no process, or the proferred token is unacceptable).";
            case EXPIRED: return "User session expired; a login may be required.";
            case FORBIDDEN: return "The user does not have the rights to perform this action.";
            case SUPPRESSED: return "Some information was not or may not have been returned due to business rules, consent or privacy rules, or access permission constraints.  This information may be accessible through alternate processes.";
            case PROCESSING: return "Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged.";
            case NOTSUPPORTED: return "The resource or profile is not supported.";
            case DUPLICATE: return "An attempt was made to create a duplicate record.";
            case NOTFOUND: return "The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the content is not found further into the application architecture.";
            case TOOLONG: return "Provided content is too long (typically, this is a denial of service protection type of error).";
            case CODEINVALID: return "The code or system could not be understood, or it was not valid in the context of a particular ValueSet.code.";
            case EXTENSION: return "An extension was found that was not acceptable, could not be resolved, or a modifierExtension was not recognized.";
            case TOOCOSTLY: return "The operation was stopped to protect server resources; e.g. a request for a value set expansion on all of SNOMED CT.";
            case BUSINESSRULE: return "The content/operation failed to pass some business rule, and so could not proceed.";
            case CONFLICT: return "Content could not be accepted because of an edit conflict (i.e. version aware updates) (In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the conflict is discovered further into the application architecture.)";
            case INCOMPLETE: return "Not all data sources typically accessed could be reached, or responded in time, so the returned information may not be complete.";
            case TRANSIENT: return "Transient processing issues. The system receiving the error may be able to resubmit the same content once an underlying issue is resolved.";
            case LOCKERROR: return "A resource/record locking failure (usually in an underlying database).";
            case NOSTORE: return "The persistent store is unavailable; e.g. the database is down for maintenance or similar action.";
            case EXCEPTION: return "An unexpected internal error has occurred.";
            case TIMEOUT: return "An internal timeout has occurred.";
            case THROTTLED: return "The system is not prepared to handle this request due to load management.";
            case INFORMATIONAL: return "A message unrelated to the processing success of the completed operation (examples of the latter include things like reminders of password expiry, system maintenance times, etc.).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INVALID: return "Invalid Content";
            case STRUCTURE: return "Structural Issue";
            case REQUIRED: return "Required element missing";
            case VALUE: return "Element value invalid";
            case INVARIANT: return "Validation rule failed";
            case SECURITY: return "Security Problem";
            case LOGIN: return "Login Required";
            case UNKNOWN: return "Unknown User";
            case EXPIRED: return "Session Expired";
            case FORBIDDEN: return "Forbidden";
            case SUPPRESSED: return "Information  Suppressed";
            case PROCESSING: return "Processing Failure";
            case NOTSUPPORTED: return "Content not supported";
            case DUPLICATE: return "Duplicate";
            case NOTFOUND: return "Not Found";
            case TOOLONG: return "Content Too Long";
            case CODEINVALID: return "Invalid Code";
            case EXTENSION: return "Unacceptable Extension";
            case TOOCOSTLY: return "Operation Too Costly";
            case BUSINESSRULE: return "Business Rule Violation";
            case CONFLICT: return "Edit Version Conflict";
            case INCOMPLETE: return "Incomplete Results";
            case TRANSIENT: return "Transient Issue";
            case LOCKERROR: return "Lock Error";
            case NOSTORE: return "No Store Available";
            case EXCEPTION: return "Exception";
            case TIMEOUT: return "Timeout";
            case THROTTLED: return "Throttled";
            case INFORMATIONAL: return "Informational Note";
            default: return "?";
          }
    }


}

