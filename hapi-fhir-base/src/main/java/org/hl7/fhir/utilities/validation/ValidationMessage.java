package org.hl7.fhir.utilities.validation;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


/*
 Copyright (c) 2011+, HL7, Inc
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

import java.util.Comparator;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

public class ValidationMessage implements Comparator<ValidationMessage>, Comparable<ValidationMessage>
{
  public enum Source {
    ExampleValidator, 
    ProfileValidator, 
    ResourceValidator, 
    InstanceValidator, 
    Schema, 
    Schematron, 
    Publisher, 
    Ontology, 
    ProfileComparer, 
    QuestionnaireResponseValidator
  }

  public enum IssueSeverity {
    /**
     * The issue caused the action to fail, and no further checking could be performed.
     */
    FATAL, 
    /**
     * The issue is sufficiently important to cause the action to fail.
     */
    ERROR, 
    /**
     * The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired.
     */
    WARNING, 
    /**
     * The issue has no relation to the degree of success of the action.
     */
    INFORMATION, 
    /**
     * added to help the parsers with the generic types
     */
    NULL;
    public static IssueSeverity fromCode(String codeString) throws FHIRException {
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
      else
        throw new FHIRException("Unknown IssueSeverity code '"+codeString+"'");
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
      case FATAL: return "The issue caused the action to fail, and no further checking could be performed.";
      case ERROR: return "The issue is sufficiently important to cause the action to fail.";
      case WARNING: return "The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired.";
      case INFORMATION: return "The issue has no relation to the degree of success of the action.";
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
     * added to help the parsers with the generic types
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
      else
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


  private Source source;
  private int line;
  private int col;
  private String location;
  private String message;
  private IssueType type;
  private IssueSeverity level;
  private String html;
  private String locationLink;


  /**
   * Constructor
   */
  public ValidationMessage() {
    super();
  }

  public ValidationMessage(Source source, IssueType type, String path, String message, IssueSeverity level) {
    super();
    this.line = -1;
    this.col = -1;
    this.location = path;
    if (message == null)
      throw new Error("message is null");
    this.message = message;
    this.html = Utilities.escapeXml(message);
    this.level = level;
    this.source = source;
    this.type = type;
    if (level == IssueSeverity.NULL)
      determineLevel(path);
    if (type == null)
      throw new Error("A type must be provided");
  }

  public ValidationMessage(Source source, IssueType type, int line, int col, String path, String message, IssueSeverity level) {
    super();
    this.line = line;
    this.col = col;
    this.location = path;
    this.message = message;
    this.html = Utilities.escapeXml(message);
    this.level = level;
    this.source = source;
    this.type = type;
    if (level == IssueSeverity.NULL)
      determineLevel(path);
    if (type == null)
      throw new Error("A type must be provided");
  }

  public ValidationMessage(Source source, IssueType type, String path, String message, String html, IssueSeverity level) {
    super();
    this.line = -1;
    this.col = -1;
    this.location = path;
    if (message == null)
      throw new Error("message is null");
    this.message = message;
    this.html = html;
    this.level = level;
    this.source = source;
    this.type = type;
    if (level == IssueSeverity.NULL)
      determineLevel(path);
    if (type == null)
      throw new Error("A type must be provided");
  }

  public ValidationMessage(Source source, IssueType type, int line, int col, String path, String message, String html, IssueSeverity level) {
    super();
    this.line = line;
    this.col = col;
    this.location = path;
    if (message == null)
      throw new Error("message is null");
    this.message = message;
    this.html = html;
    this.level = level;
    this.source = source;
    this.type = type;
    if (level == IssueSeverity.NULL)
      determineLevel(path);
    if (type == null)
      throw new Error("A type must be provided");
  }

  public ValidationMessage(Source source, IssueType type, String message, IssueSeverity level) {
    super();
    this.line = -1;
    this.col = -1;
    if (message == null)
      throw new Error("message is null");
    this.message = message;
    this.level = level;
    this.source = source;
    this.type = type;
    if (type == null)
      throw new Error("A type must be provided");
  }

  private IssueSeverity determineLevel(String path) {
    if (isGrandfathered(path))
      return IssueSeverity.WARNING;
    else
      return IssueSeverity.ERROR;
  }

  private boolean isGrandfathered(String path) {
    if (path.startsWith("xds-documentmanifest."))
      return true;
    if (path.startsWith("observation-device-metric-devicemetricobservation."))
      return true;
    if (path.startsWith("medicationadministration-immunization-vaccine."))
      return true;
    if (path.startsWith("elementdefinition-de-dataelement."))
      return true;
    if (path.startsWith("dataelement-sdc-sdcelement."))
      return true;
    if (path.startsWith("questionnaireresponse-sdc-structureddatacaptureanswers."))
      return true;
    if (path.startsWith("valueset-sdc-structureddatacapturevalueset."))
      return true;
    if (path.startsWith("dataelement-sdc-de-sdcelement."))
      return true;
    if (path.startsWith("do-uslab-uslabdo."))
      return true;
    if (path.startsWith("."))
      return true;
    if (path.startsWith("."))
      return true;
    if (path.startsWith("."))
      return true;
    if (path.startsWith("."))
      return true;

    return false;
  }

  public String getMessage() {
    return message;
  }
  public ValidationMessage setMessage(String message) {
    this.message = message;
    return this;
  }

  public IssueSeverity getLevel() {
    return level;
  }
  public ValidationMessage setLevel(IssueSeverity level) {
    this.level = level;
    return this;
  }

  public Source getSource() {
    return source;
  }
  public ValidationMessage setSource(Source source) {
    this.source = source;
    return this;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int theLine) {
    line = theLine;
  }

  public int getCol() {
    return col;
  }

  public void setCol(int theCol) {
    col = theCol;
  }

  public String getLocation() {
    return location;
  }
  public ValidationMessage setLocation(String location) {
    this.location = location;
    return this;
  }

  public IssueType getType() {
    return type;
  }

  public ValidationMessage setType(IssueType type) {
    this.type = type;
    return this;
  }

  public String summary() {
    return level.toString()+" @ "+location+(line>= 0 && col >= 0 ? " (line "+Integer.toString(line)+", col"+Integer.toString(col)+"): " : ": ") +message +(source != null ? " (src = "+source+")" : "");
  }


  public String toXML() {
    return "<message source=\"" + source + "\" line=\"" + line + "\" col=\"" + col + "\" location=\"" + Utilities.escapeXml(location) + "\" type=\"" + type + "\" level=\"" + level + "\" display=\"" + Utilities.escapeXml(getDisplay()) + "\" ><plain>" + Utilities.escapeXml(message) + "</plain><html>" + html + "</html></message>";
  }

  public String getHtml() {
    return html == null ? Utilities.escapeXml(message) : html;
  }

  public String getDisplay() {
    return level + ": " + (location.isEmpty() ? "" : (location + ": ")) + message;
  }
  
  /**
   * Returns a representation of this ValidationMessage suitable for logging. The values of
   * most of the internal fields are included, so this may not be suitable for display to 
   * an end user.
   */
  @Override
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
    b.append("level", level);
    b.append("type", type);
    b.append("location", location);
    b.append("message", message);
    return b.build();
  }

  @Override
  public boolean equals(Object o) {
    return (this.getMessage() != null && this.getMessage().equals(((ValidationMessage)o).getMessage())) && (this.getLocation() != null && this.getLocation().equals(((ValidationMessage)o).getLocation()));
  }

  @Override
  public int compare(ValidationMessage x, ValidationMessage y) {
    String sx = x.getLevel().getDisplay() + x.getType().getDisplay() + String.format("%06d", x.getLine()) + x.getMessage();
    String sy = y.getLevel().getDisplay() + y.getType().getDisplay() + String.format("%06d", y.getLine()) + y.getMessage();
    return sx.compareTo(sy);
  }  

  @Override
  public int compareTo(ValidationMessage y) {
    return compare(this, y);
  }

  public String getLocationLink() {
    return locationLink;
  }

  public ValidationMessage setLocationLink(String locationLink) {
    this.locationLink = locationLink;
    return this;
  }

  
}
