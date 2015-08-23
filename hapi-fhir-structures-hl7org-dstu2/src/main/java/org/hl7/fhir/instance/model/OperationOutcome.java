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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * A collection of error, warning or information messages that result from a system action.
 */
@ResourceDef(name="OperationOutcome", profile="http://hl7.org/fhir/Profile/OperationOutcome")
public class OperationOutcome extends DomainResource implements IBaseOperationOutcome {

    public enum IssueSeverity {
        /**
         * The issue caused the action to fail, and no further checking could be performed
         */
        FATAL, 
        /**
         * The issue is sufficiently important to cause the action to fail
         */
        ERROR, 
        /**
         * The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired
         */
        WARNING, 
        /**
         * The issue has no relation to the degree of success of the action
         */
        INFORMATION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IssueSeverity fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("fatal".equals(codeString))
          return FATAL;
        if ("error".equals(codeString))
          return ERROR;
        if ("warning".equals(codeString))
          return WARNING;
        if ("information".equals(codeString))
          return INFORMATION;
        throw new Exception("Unknown IssueSeverity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FATAL: return "fatal";
            case ERROR: return "error";
            case WARNING: return "warning";
            case INFORMATION: return "information";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case FATAL: return "http://hl7.org/fhir/issue-severity";
            case ERROR: return "http://hl7.org/fhir/issue-severity";
            case WARNING: return "http://hl7.org/fhir/issue-severity";
            case INFORMATION: return "http://hl7.org/fhir/issue-severity";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case FATAL: return "The issue caused the action to fail, and no further checking could be performed";
            case ERROR: return "The issue is sufficiently important to cause the action to fail";
            case WARNING: return "The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired";
            case INFORMATION: return "The issue has no relation to the degree of success of the action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FATAL: return "Fatal";
            case ERROR: return "Error";
            case WARNING: return "Warning";
            case INFORMATION: return "Information";
            default: return "?";
          }
        }
    }

  public static class IssueSeverityEnumFactory implements EnumFactory<IssueSeverity> {
    public IssueSeverity fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("fatal".equals(codeString))
          return IssueSeverity.FATAL;
        if ("error".equals(codeString))
          return IssueSeverity.ERROR;
        if ("warning".equals(codeString))
          return IssueSeverity.WARNING;
        if ("information".equals(codeString))
          return IssueSeverity.INFORMATION;
        throw new IllegalArgumentException("Unknown IssueSeverity code '"+codeString+"'");
        }
    public String toCode(IssueSeverity code) {
      if (code == IssueSeverity.FATAL)
        return "fatal";
      if (code == IssueSeverity.ERROR)
        return "error";
      if (code == IssueSeverity.WARNING)
        return "warning";
      if (code == IssueSeverity.INFORMATION)
        return "information";
      return "?";
      }
    }

    public enum IssueType {
        /**
         * Content invalid against the Specification or a Profile
         */
        INVALID, 
        /**
         * A structural issue in the content such as wrong namespace, or unable to parse the content completely, or invalid json syntax
         */
        STRUCTURE, 
        /**
         * A required element is missing
         */
        REQUIRED, 
        /**
         * element value invalid
         */
        VALUE, 
        /**
         * A content validation rule failed - e.g. a schematron rule
         */
        INVARIANT, 
        /**
         * An authentication/authorization/permissions issueof some kind
         */
        SECURITY, 
        /**
         * the client needs to initiate an authentication process
         */
        LOGIN, 
        /**
         * The user or system was not able to be authenticated (either there is no process, or the proferred token is unacceptable)
         */
        UNKNOWN, 
        /**
         * User session expired; a login may be required
         */
        EXPIRED, 
        /**
         * The user does not have the rights to perform this action
         */
        FORBIDDEN, 
        /**
         * Some information was not or may not have been returned due to business rules, consent or privacy rules, or access permission constraints.  This information may be accessible through alternate processes
         */
        SUPPRESSED, 
        /**
         * Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged
         */
        PROCESSING, 
        /**
         * The resource or profile is not supported
         */
        NOTSUPPORTED, 
        /**
         * An attempt was made to create a duplicate record
         */
        DUPLICATE, 
        /**
         * The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the content is not found further into the application architecture
         */
        NOTFOUND, 
        /**
         * Provided content is too long (typically, this is a denial of service protection type of error)
         */
        TOOLONG, 
        /**
         * The code or system could not be understood, or it was not valid in the context of a particular ValueSet
         */
        CODEINVALID, 
        /**
         * An extension was found that was not acceptable, or that could not be resolved, or a modifierExtension that was not recognised
         */
        EXTENSION, 
        /**
         * The operation was stopped to protect server resources. E.g. a request for a value set expansion on all of SNOMED CT
         */
        TOOCOSTLY, 
        /**
         * The content/operation failed to pass some business rule, and so could not proceed
         */
        BUSINESSRULE, 
        /**
         * content could not be accepted because of an edit conflict (i.e. version aware updates) (In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the conflict is discovered further into the application architecture)
         */
        CONFLICT, 
        /**
         * Not all data sources typically accessed could be reached, or responded in time, so the returned information may not be complete
         */
        INCOMPLETE, 
        /**
         * Transient processing issues. The system receiving the error may be able to resubmit the same content once an underlying issue is resolved
         */
        TRANSIENT, 
        /**
         * A resource/record locking failure (usually in an underlying database)
         */
        LOCKERROR, 
        /**
         * The persistent store unavailable. E.g. the database is down for maintenance or similar
         */
        NOSTORE, 
        /**
         * An unexpected internal error
         */
        EXCEPTION, 
        /**
         * An internal timeout occurred
         */
        TIMEOUT, 
        /**
         * The system is not prepared to handle this request due to load management
         */
        THROTTLED, 
        /**
         * A message unrelated to the processing success of the completed operation (Examples of the latter include things like reminders of password expiry, system maintenance times, etc.)
         */
        INFORMATIONAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static IssueType fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown IssueType code '"+codeString+"'");
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
          switch (this) {
            case INVALID: return "http://hl7.org/fhir/issue-type";
            case STRUCTURE: return "http://hl7.org/fhir/issue-type";
            case REQUIRED: return "http://hl7.org/fhir/issue-type";
            case VALUE: return "http://hl7.org/fhir/issue-type";
            case INVARIANT: return "http://hl7.org/fhir/issue-type";
            case SECURITY: return "http://hl7.org/fhir/issue-type";
            case LOGIN: return "http://hl7.org/fhir/issue-type";
            case UNKNOWN: return "http://hl7.org/fhir/issue-type";
            case EXPIRED: return "http://hl7.org/fhir/issue-type";
            case FORBIDDEN: return "http://hl7.org/fhir/issue-type";
            case SUPPRESSED: return "http://hl7.org/fhir/issue-type";
            case PROCESSING: return "http://hl7.org/fhir/issue-type";
            case NOTSUPPORTED: return "http://hl7.org/fhir/issue-type";
            case DUPLICATE: return "http://hl7.org/fhir/issue-type";
            case NOTFOUND: return "http://hl7.org/fhir/issue-type";
            case TOOLONG: return "http://hl7.org/fhir/issue-type";
            case CODEINVALID: return "http://hl7.org/fhir/issue-type";
            case EXTENSION: return "http://hl7.org/fhir/issue-type";
            case TOOCOSTLY: return "http://hl7.org/fhir/issue-type";
            case BUSINESSRULE: return "http://hl7.org/fhir/issue-type";
            case CONFLICT: return "http://hl7.org/fhir/issue-type";
            case INCOMPLETE: return "http://hl7.org/fhir/issue-type";
            case TRANSIENT: return "http://hl7.org/fhir/issue-type";
            case LOCKERROR: return "http://hl7.org/fhir/issue-type";
            case NOSTORE: return "http://hl7.org/fhir/issue-type";
            case EXCEPTION: return "http://hl7.org/fhir/issue-type";
            case TIMEOUT: return "http://hl7.org/fhir/issue-type";
            case THROTTLED: return "http://hl7.org/fhir/issue-type";
            case INFORMATIONAL: return "http://hl7.org/fhir/issue-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INVALID: return "Content invalid against the Specification or a Profile";
            case STRUCTURE: return "A structural issue in the content such as wrong namespace, or unable to parse the content completely, or invalid json syntax";
            case REQUIRED: return "A required element is missing";
            case VALUE: return "element value invalid";
            case INVARIANT: return "A content validation rule failed - e.g. a schematron rule";
            case SECURITY: return "An authentication/authorization/permissions issueof some kind";
            case LOGIN: return "the client needs to initiate an authentication process";
            case UNKNOWN: return "The user or system was not able to be authenticated (either there is no process, or the proferred token is unacceptable)";
            case EXPIRED: return "User session expired; a login may be required";
            case FORBIDDEN: return "The user does not have the rights to perform this action";
            case SUPPRESSED: return "Some information was not or may not have been returned due to business rules, consent or privacy rules, or access permission constraints.  This information may be accessible through alternate processes";
            case PROCESSING: return "Processing issues. These are expected to be final e.g. there is no point resubmitting the same content unchanged";
            case NOTSUPPORTED: return "The resource or profile is not supported";
            case DUPLICATE: return "An attempt was made to create a duplicate record";
            case NOTFOUND: return "The reference provided was not found. In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the content is not found further into the application architecture";
            case TOOLONG: return "Provided content is too long (typically, this is a denial of service protection type of error)";
            case CODEINVALID: return "The code or system could not be understood, or it was not valid in the context of a particular ValueSet";
            case EXTENSION: return "An extension was found that was not acceptable, or that could not be resolved, or a modifierExtension that was not recognised";
            case TOOCOSTLY: return "The operation was stopped to protect server resources. E.g. a request for a value set expansion on all of SNOMED CT";
            case BUSINESSRULE: return "The content/operation failed to pass some business rule, and so could not proceed";
            case CONFLICT: return "content could not be accepted because of an edit conflict (i.e. version aware updates) (In a pure RESTful environment, this would be an HTTP 404 error, but this code may be used where the conflict is discovered further into the application architecture)";
            case INCOMPLETE: return "Not all data sources typically accessed could be reached, or responded in time, so the returned information may not be complete";
            case TRANSIENT: return "Transient processing issues. The system receiving the error may be able to resubmit the same content once an underlying issue is resolved";
            case LOCKERROR: return "A resource/record locking failure (usually in an underlying database)";
            case NOSTORE: return "The persistent store unavailable. E.g. the database is down for maintenance or similar";
            case EXCEPTION: return "An unexpected internal error";
            case TIMEOUT: return "An internal timeout occurred";
            case THROTTLED: return "The system is not prepared to handle this request due to load management";
            case INFORMATIONAL: return "A message unrelated to the processing success of the completed operation (Examples of the latter include things like reminders of password expiry, system maintenance times, etc.)";
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

  public static class IssueTypeEnumFactory implements EnumFactory<IssueType> {
    public IssueType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("invalid".equals(codeString))
          return IssueType.INVALID;
        if ("structure".equals(codeString))
          return IssueType.STRUCTURE;
        if ("required".equals(codeString))
          return IssueType.REQUIRED;
        if ("value".equals(codeString))
          return IssueType.VALUE;
        if ("invariant".equals(codeString))
          return IssueType.INVARIANT;
        if ("security".equals(codeString))
          return IssueType.SECURITY;
        if ("login".equals(codeString))
          return IssueType.LOGIN;
        if ("unknown".equals(codeString))
          return IssueType.UNKNOWN;
        if ("expired".equals(codeString))
          return IssueType.EXPIRED;
        if ("forbidden".equals(codeString))
          return IssueType.FORBIDDEN;
        if ("suppressed".equals(codeString))
          return IssueType.SUPPRESSED;
        if ("processing".equals(codeString))
          return IssueType.PROCESSING;
        if ("not-supported".equals(codeString))
          return IssueType.NOTSUPPORTED;
        if ("duplicate".equals(codeString))
          return IssueType.DUPLICATE;
        if ("not-found".equals(codeString))
          return IssueType.NOTFOUND;
        if ("too-long".equals(codeString))
          return IssueType.TOOLONG;
        if ("code-invalid".equals(codeString))
          return IssueType.CODEINVALID;
        if ("extension".equals(codeString))
          return IssueType.EXTENSION;
        if ("too-costly".equals(codeString))
          return IssueType.TOOCOSTLY;
        if ("business-rule".equals(codeString))
          return IssueType.BUSINESSRULE;
        if ("conflict".equals(codeString))
          return IssueType.CONFLICT;
        if ("incomplete".equals(codeString))
          return IssueType.INCOMPLETE;
        if ("transient".equals(codeString))
          return IssueType.TRANSIENT;
        if ("lock-error".equals(codeString))
          return IssueType.LOCKERROR;
        if ("no-store".equals(codeString))
          return IssueType.NOSTORE;
        if ("exception".equals(codeString))
          return IssueType.EXCEPTION;
        if ("timeout".equals(codeString))
          return IssueType.TIMEOUT;
        if ("throttled".equals(codeString))
          return IssueType.THROTTLED;
        if ("informational".equals(codeString))
          return IssueType.INFORMATIONAL;
        throw new IllegalArgumentException("Unknown IssueType code '"+codeString+"'");
        }
    public String toCode(IssueType code) {
      if (code == IssueType.INVALID)
        return "invalid";
      if (code == IssueType.STRUCTURE)
        return "structure";
      if (code == IssueType.REQUIRED)
        return "required";
      if (code == IssueType.VALUE)
        return "value";
      if (code == IssueType.INVARIANT)
        return "invariant";
      if (code == IssueType.SECURITY)
        return "security";
      if (code == IssueType.LOGIN)
        return "login";
      if (code == IssueType.UNKNOWN)
        return "unknown";
      if (code == IssueType.EXPIRED)
        return "expired";
      if (code == IssueType.FORBIDDEN)
        return "forbidden";
      if (code == IssueType.SUPPRESSED)
        return "suppressed";
      if (code == IssueType.PROCESSING)
        return "processing";
      if (code == IssueType.NOTSUPPORTED)
        return "not-supported";
      if (code == IssueType.DUPLICATE)
        return "duplicate";
      if (code == IssueType.NOTFOUND)
        return "not-found";
      if (code == IssueType.TOOLONG)
        return "too-long";
      if (code == IssueType.CODEINVALID)
        return "code-invalid";
      if (code == IssueType.EXTENSION)
        return "extension";
      if (code == IssueType.TOOCOSTLY)
        return "too-costly";
      if (code == IssueType.BUSINESSRULE)
        return "business-rule";
      if (code == IssueType.CONFLICT)
        return "conflict";
      if (code == IssueType.INCOMPLETE)
        return "incomplete";
      if (code == IssueType.TRANSIENT)
        return "transient";
      if (code == IssueType.LOCKERROR)
        return "lock-error";
      if (code == IssueType.NOSTORE)
        return "no-store";
      if (code == IssueType.EXCEPTION)
        return "exception";
      if (code == IssueType.TIMEOUT)
        return "timeout";
      if (code == IssueType.THROTTLED)
        return "throttled";
      if (code == IssueType.INFORMATIONAL)
        return "informational";
      return "?";
      }
    }

    @Block()
    public static class OperationOutcomeIssueComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates whether the issue indicates a variation from successful processing.
         */
        @Child(name = "severity", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
        @Description(shortDefinition="fatal | error | warning | information", formalDefinition="Indicates whether the issue indicates a variation from successful processing." )
        protected Enumeration<IssueSeverity> severity;

        /**
         * Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code from the IssueType value set, and may additional provide its own code for the error in the details element.
         */
        @Child(name = "code", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Error or warning code", formalDefinition="Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code from the IssueType value set, and may additional provide its own code for the error in the details element." )
        protected Enumeration<IssueType> code;

        /**
         * Additional details about the error. This may be a text description of the error, or a system code that identifies the error.
         */
        @Child(name = "details", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Additional details about the error", formalDefinition="Additional details about the error. This may be a text description of the error, or a system code that identifies the error." )
        protected CodeableConcept details;

        /**
         * Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stack dump to help trace the issue.
         */
        @Child(name = "diagnostics", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Additional diagnostic information about the issue", formalDefinition="Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stack dump to help trace the issue." )
        protected StringType diagnostics;

        /**
         * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
         */
        @Child(name = "location", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="XPath of element(s) related to issue", formalDefinition="A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised." )
        protected List<StringType> location;

        private static final long serialVersionUID = 930165515L;

    /*
     * Constructor
     */
      public OperationOutcomeIssueComponent() {
        super();
      }

    /*
     * Constructor
     */
      public OperationOutcomeIssueComponent(Enumeration<IssueSeverity> severity, Enumeration<IssueType> code) {
        super();
        this.severity = severity;
        this.code = code;
      }

        /**
         * @return {@link #severity} (Indicates whether the issue indicates a variation from successful processing.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public Enumeration<IssueSeverity> getSeverityElement() { 
          if (this.severity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationOutcomeIssueComponent.severity");
            else if (Configuration.doAutoCreate())
              this.severity = new Enumeration<IssueSeverity>(new IssueSeverityEnumFactory()); // bb
          return this.severity;
        }

        public boolean hasSeverityElement() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        public boolean hasSeverity() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        /**
         * @param value {@link #severity} (Indicates whether the issue indicates a variation from successful processing.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public OperationOutcomeIssueComponent setSeverityElement(Enumeration<IssueSeverity> value) { 
          this.severity = value;
          return this;
        }

        /**
         * @return Indicates whether the issue indicates a variation from successful processing.
         */
        public IssueSeverity getSeverity() { 
          return this.severity == null ? null : this.severity.getValue();
        }

        /**
         * @param value Indicates whether the issue indicates a variation from successful processing.
         */
        public OperationOutcomeIssueComponent setSeverity(IssueSeverity value) { 
            if (this.severity == null)
              this.severity = new Enumeration<IssueSeverity>(new IssueSeverityEnumFactory());
            this.severity.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code from the IssueType value set, and may additional provide its own code for the error in the details element.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<IssueType> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationOutcomeIssueComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<IssueType>(new IssueTypeEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code from the IssueType value set, and may additional provide its own code for the error in the details element.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public OperationOutcomeIssueComponent setCodeElement(Enumeration<IssueType> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code from the IssueType value set, and may additional provide its own code for the error in the details element.
         */
        public IssueType getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code from the IssueType value set, and may additional provide its own code for the error in the details element.
         */
        public OperationOutcomeIssueComponent setCode(IssueType value) { 
            if (this.code == null)
              this.code = new Enumeration<IssueType>(new IssueTypeEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #details} (Additional details about the error. This may be a text description of the error, or a system code that identifies the error.)
         */
        public CodeableConcept getDetails() { 
          if (this.details == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationOutcomeIssueComponent.details");
            else if (Configuration.doAutoCreate())
              this.details = new CodeableConcept(); // cc
          return this.details;
        }

        public boolean hasDetails() { 
          return this.details != null && !this.details.isEmpty();
        }

        /**
         * @param value {@link #details} (Additional details about the error. This may be a text description of the error, or a system code that identifies the error.)
         */
        public OperationOutcomeIssueComponent setDetails(CodeableConcept value) { 
          this.details = value;
          return this;
        }

        /**
         * @return {@link #diagnostics} (Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stack dump to help trace the issue.). This is the underlying object with id, value and extensions. The accessor "getDiagnostics" gives direct access to the value
         */
        public StringType getDiagnosticsElement() { 
          if (this.diagnostics == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationOutcomeIssueComponent.diagnostics");
            else if (Configuration.doAutoCreate())
              this.diagnostics = new StringType(); // bb
          return this.diagnostics;
        }

        public boolean hasDiagnosticsElement() { 
          return this.diagnostics != null && !this.diagnostics.isEmpty();
        }

        public boolean hasDiagnostics() { 
          return this.diagnostics != null && !this.diagnostics.isEmpty();
        }

        /**
         * @param value {@link #diagnostics} (Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stack dump to help trace the issue.). This is the underlying object with id, value and extensions. The accessor "getDiagnostics" gives direct access to the value
         */
        public OperationOutcomeIssueComponent setDiagnosticsElement(StringType value) { 
          this.diagnostics = value;
          return this;
        }

        /**
         * @return Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stack dump to help trace the issue.
         */
        public String getDiagnostics() { 
          return this.diagnostics == null ? null : this.diagnostics.getValue();
        }

        /**
         * @param value Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stack dump to help trace the issue.
         */
        public OperationOutcomeIssueComponent setDiagnostics(String value) { 
          if (Utilities.noString(value))
            this.diagnostics = null;
          else {
            if (this.diagnostics == null)
              this.diagnostics = new StringType();
            this.diagnostics.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #location} (A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.)
         */
        public List<StringType> getLocation() { 
          if (this.location == null)
            this.location = new ArrayList<StringType>();
          return this.location;
        }

        public boolean hasLocation() { 
          if (this.location == null)
            return false;
          for (StringType item : this.location)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #location} (A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.)
         */
    // syntactic sugar
        public StringType addLocationElement() {//2 
          StringType t = new StringType();
          if (this.location == null)
            this.location = new ArrayList<StringType>();
          this.location.add(t);
          return t;
        }

        /**
         * @param value {@link #location} (A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.)
         */
        public OperationOutcomeIssueComponent addLocation(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.location == null)
            this.location = new ArrayList<StringType>();
          this.location.add(t);
          return this;
        }

        /**
         * @param value {@link #location} (A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.)
         */
        public boolean hasLocation(String value) { 
          if (this.location == null)
            return false;
          for (StringType v : this.location)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("severity", "code", "Indicates whether the issue indicates a variation from successful processing.", 0, java.lang.Integer.MAX_VALUE, severity));
          childrenList.add(new Property("code", "code", "Describes the type of the issue. The system that creates an OperationOutcome SHALL choose the most applicable code from the IssueType value set, and may additional provide its own code for the error in the details element.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("details", "CodeableConcept", "Additional details about the error. This may be a text description of the error, or a system code that identifies the error.", 0, java.lang.Integer.MAX_VALUE, details));
          childrenList.add(new Property("diagnostics", "string", "Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stack dump to help trace the issue.", 0, java.lang.Integer.MAX_VALUE, diagnostics));
          childrenList.add(new Property("location", "string", "A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.", 0, java.lang.Integer.MAX_VALUE, location));
        }

      public OperationOutcomeIssueComponent copy() {
        OperationOutcomeIssueComponent dst = new OperationOutcomeIssueComponent();
        copyValues(dst);
        dst.severity = severity == null ? null : severity.copy();
        dst.code = code == null ? null : code.copy();
        dst.details = details == null ? null : details.copy();
        dst.diagnostics = diagnostics == null ? null : diagnostics.copy();
        if (location != null) {
          dst.location = new ArrayList<StringType>();
          for (StringType i : location)
            dst.location.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationOutcomeIssueComponent))
          return false;
        OperationOutcomeIssueComponent o = (OperationOutcomeIssueComponent) other;
        return compareDeep(severity, o.severity, true) && compareDeep(code, o.code, true) && compareDeep(details, o.details, true)
           && compareDeep(diagnostics, o.diagnostics, true) && compareDeep(location, o.location, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationOutcomeIssueComponent))
          return false;
        OperationOutcomeIssueComponent o = (OperationOutcomeIssueComponent) other;
        return compareValues(severity, o.severity, true) && compareValues(code, o.code, true) && compareValues(diagnostics, o.diagnostics, true)
           && compareValues(location, o.location, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (severity == null || severity.isEmpty()) && (code == null || code.isEmpty())
           && (details == null || details.isEmpty()) && (diagnostics == null || diagnostics.isEmpty())
           && (location == null || location.isEmpty());
      }

  }

    /**
     * An error, warning or information message that results from a system action.
     */
    @Child(name = "issue", type = {}, order=0, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A single issue associated with the action", formalDefinition="An error, warning or information message that results from a system action." )
    protected List<OperationOutcomeIssueComponent> issue;

    private static final long serialVersionUID = -152150052L;

  /*
   * Constructor
   */
    public OperationOutcome() {
      super();
    }

    /**
     * @return {@link #issue} (An error, warning or information message that results from a system action.)
     */
    public List<OperationOutcomeIssueComponent> getIssue() { 
      if (this.issue == null)
        this.issue = new ArrayList<OperationOutcomeIssueComponent>();
      return this.issue;
    }

    public boolean hasIssue() { 
      if (this.issue == null)
        return false;
      for (OperationOutcomeIssueComponent item : this.issue)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #issue} (An error, warning or information message that results from a system action.)
     */
    // syntactic sugar
    public OperationOutcomeIssueComponent addIssue() { //3
      OperationOutcomeIssueComponent t = new OperationOutcomeIssueComponent();
      if (this.issue == null)
        this.issue = new ArrayList<OperationOutcomeIssueComponent>();
      this.issue.add(t);
      return t;
    }

    // syntactic sugar
    public OperationOutcome addIssue(OperationOutcomeIssueComponent t) { //3
      if (t == null)
        return this;
      if (this.issue == null)
        this.issue = new ArrayList<OperationOutcomeIssueComponent>();
      this.issue.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("issue", "", "An error, warning or information message that results from a system action.", 0, java.lang.Integer.MAX_VALUE, issue));
      }

      public OperationOutcome copy() {
        OperationOutcome dst = new OperationOutcome();
        copyValues(dst);
        if (issue != null) {
          dst.issue = new ArrayList<OperationOutcomeIssueComponent>();
          for (OperationOutcomeIssueComponent i : issue)
            dst.issue.add(i.copy());
        };
        return dst;
      }

      protected OperationOutcome typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof OperationOutcome))
          return false;
        OperationOutcome o = (OperationOutcome) other;
        return compareDeep(issue, o.issue, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationOutcome))
          return false;
        OperationOutcome o = (OperationOutcome) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (issue == null || issue.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OperationOutcome;
   }


}

