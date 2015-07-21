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

// Generated on Tue, Jul 21, 2015 10:37-0400 for FHIR v0.5.0

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

    @Block()
    public static class OperationOutcomeIssueComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates whether the issue indicates a variation from successful processing.
         */
        @Child(name = "severity", type = {CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="fatal | error | warning | information", formalDefinition="Indicates whether the issue indicates a variation from successful processing." )
        protected Enumeration<IssueSeverity> severity;

        /**
         * Describes the type of the issue.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Error or warning code", formalDefinition="Describes the type of the issue." )
        protected CodeableConcept code;

        /**
         * Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stck dump to help trace the issue.
         */
        @Child(name = "details", type = {StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Additional diagnostic information about the issue", formalDefinition="Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stck dump to help trace the issue." )
        protected StringType details;

        /**
         * A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.
         */
        @Child(name = "location", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="XPath of element(s) related to issue", formalDefinition="A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised." )
        protected List<StringType> location;

        private static final long serialVersionUID = -869408333L;

    /*
     * Constructor
     */
      public OperationOutcomeIssueComponent() {
        super();
      }

    /*
     * Constructor
     */
      public OperationOutcomeIssueComponent(Enumeration<IssueSeverity> severity, CodeableConcept code) {
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
         * @return {@link #code} (Describes the type of the issue.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationOutcomeIssueComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Describes the type of the issue.)
         */
        public OperationOutcomeIssueComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #details} (Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stck dump to help trace the issue.). This is the underlying object with id, value and extensions. The accessor "getDetails" gives direct access to the value
         */
        public StringType getDetailsElement() { 
          if (this.details == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OperationOutcomeIssueComponent.details");
            else if (Configuration.doAutoCreate())
              this.details = new StringType(); // bb
          return this.details;
        }

        public boolean hasDetailsElement() { 
          return this.details != null && !this.details.isEmpty();
        }

        public boolean hasDetails() { 
          return this.details != null && !this.details.isEmpty();
        }

        /**
         * @param value {@link #details} (Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stck dump to help trace the issue.). This is the underlying object with id, value and extensions. The accessor "getDetails" gives direct access to the value
         */
        public OperationOutcomeIssueComponent setDetailsElement(StringType value) { 
          this.details = value;
          return this;
        }

        /**
         * @return Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stck dump to help trace the issue.
         */
        public String getDetails() { 
          return this.details == null ? null : this.details.getValue();
        }

        /**
         * @param value Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stck dump to help trace the issue.
         */
        public OperationOutcomeIssueComponent setDetails(String value) { 
          if (Utilities.noString(value))
            this.details = null;
          else {
            if (this.details == null)
              this.details = new StringType();
            this.details.setValue(value);
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
          childrenList.add(new Property("code", "CodeableConcept", "Describes the type of the issue.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("details", "string", "Additional diagnostic information about the issue.  Typically, this may be a description of how a value is erroneous, or a stck dump to help trace the issue.", 0, java.lang.Integer.MAX_VALUE, details));
          childrenList.add(new Property("location", "string", "A simple XPath limited to element names, repetition indicators and the default child access that identifies one of the elements in the resource that caused this issue to be raised.", 0, java.lang.Integer.MAX_VALUE, location));
        }

      public OperationOutcomeIssueComponent copy() {
        OperationOutcomeIssueComponent dst = new OperationOutcomeIssueComponent();
        copyValues(dst);
        dst.severity = severity == null ? null : severity.copy();
        dst.code = code == null ? null : code.copy();
        dst.details = details == null ? null : details.copy();
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
           && compareDeep(location, o.location, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof OperationOutcomeIssueComponent))
          return false;
        OperationOutcomeIssueComponent o = (OperationOutcomeIssueComponent) other;
        return compareValues(severity, o.severity, true) && compareValues(details, o.details, true) && compareValues(location, o.location, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (severity == null || severity.isEmpty()) && (code == null || code.isEmpty())
           && (details == null || details.isEmpty()) && (location == null || location.isEmpty());
      }

  }

    /**
     * An error, warning or information message that results from a system action.
     */
    @Child(name = "issue", type = {}, order=0, min=1, max=Child.MAX_UNLIMITED)
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

