package org.hl7.fhir.dstu3.model;

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

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A summary of information based on the results of executing a TestScript.
 */
@ResourceDef(name="TestReport", profile="http://hl7.org/fhir/Profile/TestReport")
public class TestReport extends DomainResource {

    public enum TestReportStatus {
        /**
         * All test operations have completed
         */
        COMPLETED, 
        /**
         * A test operations is currently executing
         */
        INPROGRESS, 
        /**
         * A test operation is waiting for an external client request
         */
        WAITING, 
        /**
         * The test script execution was manually stopped
         */
        STOPPED, 
        /**
         * This test report was entered or created in error
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TestReportStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("waiting".equals(codeString))
          return WAITING;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TestReportStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETED: return "completed";
            case INPROGRESS: return "in-progress";
            case WAITING: return "waiting";
            case STOPPED: return "stopped";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETED: return "http://hl7.org/fhir/report-status-codes";
            case INPROGRESS: return "http://hl7.org/fhir/report-status-codes";
            case WAITING: return "http://hl7.org/fhir/report-status-codes";
            case STOPPED: return "http://hl7.org/fhir/report-status-codes";
            case ENTEREDINERROR: return "http://hl7.org/fhir/report-status-codes";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETED: return "All test operations have completed";
            case INPROGRESS: return "A test operations is currently executing";
            case WAITING: return "A test operation is waiting for an external client request";
            case STOPPED: return "The test script execution was manually stopped";
            case ENTEREDINERROR: return "This test report was entered or created in error";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETED: return "Completed";
            case INPROGRESS: return "In Progress";
            case WAITING: return "Waiting";
            case STOPPED: return "Stopped";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class TestReportStatusEnumFactory implements EnumFactory<TestReportStatus> {
    public TestReportStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("completed".equals(codeString))
          return TestReportStatus.COMPLETED;
        if ("in-progress".equals(codeString))
          return TestReportStatus.INPROGRESS;
        if ("waiting".equals(codeString))
          return TestReportStatus.WAITING;
        if ("stopped".equals(codeString))
          return TestReportStatus.STOPPED;
        if ("entered-in-error".equals(codeString))
          return TestReportStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown TestReportStatus code '"+codeString+"'");
        }
        public Enumeration<TestReportStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TestReportStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("completed".equals(codeString))
          return new Enumeration<TestReportStatus>(this, TestReportStatus.COMPLETED);
        if ("in-progress".equals(codeString))
          return new Enumeration<TestReportStatus>(this, TestReportStatus.INPROGRESS);
        if ("waiting".equals(codeString))
          return new Enumeration<TestReportStatus>(this, TestReportStatus.WAITING);
        if ("stopped".equals(codeString))
          return new Enumeration<TestReportStatus>(this, TestReportStatus.STOPPED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<TestReportStatus>(this, TestReportStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown TestReportStatus code '"+codeString+"'");
        }
    public String toCode(TestReportStatus code) {
      if (code == TestReportStatus.COMPLETED)
        return "completed";
      if (code == TestReportStatus.INPROGRESS)
        return "in-progress";
      if (code == TestReportStatus.WAITING)
        return "waiting";
      if (code == TestReportStatus.STOPPED)
        return "stopped";
      if (code == TestReportStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(TestReportStatus code) {
      return code.getSystem();
      }
    }

    public enum TestReportResult {
        /**
         * All test operations successfully passed all asserts
         */
        PASS, 
        /**
         * One or more test operations failed one or more asserts
         */
        FAIL, 
        /**
         * One or more test operations is pending execution completion
         */
        PENDING, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TestReportResult fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("pass".equals(codeString))
          return PASS;
        if ("fail".equals(codeString))
          return FAIL;
        if ("pending".equals(codeString))
          return PENDING;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TestReportResult code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PASS: return "pass";
            case FAIL: return "fail";
            case PENDING: return "pending";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PASS: return "http://hl7.org/fhir/report-result-codes";
            case FAIL: return "http://hl7.org/fhir/report-result-codes";
            case PENDING: return "http://hl7.org/fhir/report-result-codes";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PASS: return "All test operations successfully passed all asserts";
            case FAIL: return "One or more test operations failed one or more asserts";
            case PENDING: return "One or more test operations is pending execution completion";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PASS: return "Pass";
            case FAIL: return "Fail";
            case PENDING: return "Pending";
            default: return "?";
          }
        }
    }

  public static class TestReportResultEnumFactory implements EnumFactory<TestReportResult> {
    public TestReportResult fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("pass".equals(codeString))
          return TestReportResult.PASS;
        if ("fail".equals(codeString))
          return TestReportResult.FAIL;
        if ("pending".equals(codeString))
          return TestReportResult.PENDING;
        throw new IllegalArgumentException("Unknown TestReportResult code '"+codeString+"'");
        }
        public Enumeration<TestReportResult> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TestReportResult>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("pass".equals(codeString))
          return new Enumeration<TestReportResult>(this, TestReportResult.PASS);
        if ("fail".equals(codeString))
          return new Enumeration<TestReportResult>(this, TestReportResult.FAIL);
        if ("pending".equals(codeString))
          return new Enumeration<TestReportResult>(this, TestReportResult.PENDING);
        throw new FHIRException("Unknown TestReportResult code '"+codeString+"'");
        }
    public String toCode(TestReportResult code) {
      if (code == TestReportResult.PASS)
        return "pass";
      if (code == TestReportResult.FAIL)
        return "fail";
      if (code == TestReportResult.PENDING)
        return "pending";
      return "?";
      }
    public String toSystem(TestReportResult code) {
      return code.getSystem();
      }
    }

    public enum TestReportParticipantType {
        /**
         * The test execution engine.
         */
        TESTENGINE, 
        /**
         * A FHIR Client
         */
        CLIENT, 
        /**
         * A FHIR Server
         */
        SERVER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TestReportParticipantType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("test-engine".equals(codeString))
          return TESTENGINE;
        if ("client".equals(codeString))
          return CLIENT;
        if ("server".equals(codeString))
          return SERVER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TestReportParticipantType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TESTENGINE: return "test-engine";
            case CLIENT: return "client";
            case SERVER: return "server";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case TESTENGINE: return "http://hl7.org/fhir/report-participant-type";
            case CLIENT: return "http://hl7.org/fhir/report-participant-type";
            case SERVER: return "http://hl7.org/fhir/report-participant-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case TESTENGINE: return "The test execution engine.";
            case CLIENT: return "A FHIR Client";
            case SERVER: return "A FHIR Server";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TESTENGINE: return "Test Engine";
            case CLIENT: return "Client";
            case SERVER: return "Server";
            default: return "?";
          }
        }
    }

  public static class TestReportParticipantTypeEnumFactory implements EnumFactory<TestReportParticipantType> {
    public TestReportParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("test-engine".equals(codeString))
          return TestReportParticipantType.TESTENGINE;
        if ("client".equals(codeString))
          return TestReportParticipantType.CLIENT;
        if ("server".equals(codeString))
          return TestReportParticipantType.SERVER;
        throw new IllegalArgumentException("Unknown TestReportParticipantType code '"+codeString+"'");
        }
        public Enumeration<TestReportParticipantType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TestReportParticipantType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("test-engine".equals(codeString))
          return new Enumeration<TestReportParticipantType>(this, TestReportParticipantType.TESTENGINE);
        if ("client".equals(codeString))
          return new Enumeration<TestReportParticipantType>(this, TestReportParticipantType.CLIENT);
        if ("server".equals(codeString))
          return new Enumeration<TestReportParticipantType>(this, TestReportParticipantType.SERVER);
        throw new FHIRException("Unknown TestReportParticipantType code '"+codeString+"'");
        }
    public String toCode(TestReportParticipantType code) {
      if (code == TestReportParticipantType.TESTENGINE)
        return "test-engine";
      if (code == TestReportParticipantType.CLIENT)
        return "client";
      if (code == TestReportParticipantType.SERVER)
        return "server";
      return "?";
      }
    public String toSystem(TestReportParticipantType code) {
      return code.getSystem();
      }
    }

    public enum TestReportActionResult {
        /**
         * The action was successful.
         */
        PASS, 
        /**
         * The action was skipped.
         */
        SKIP, 
        /**
         * The action failed.
         */
        FAIL, 
        /**
         * The action passed but with warnings.
         */
        WARNING, 
        /**
         * The action encountered a fatal error and the engine was unable to process.
         */
        ERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TestReportActionResult fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("pass".equals(codeString))
          return PASS;
        if ("skip".equals(codeString))
          return SKIP;
        if ("fail".equals(codeString))
          return FAIL;
        if ("warning".equals(codeString))
          return WARNING;
        if ("error".equals(codeString))
          return ERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TestReportActionResult code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PASS: return "pass";
            case SKIP: return "skip";
            case FAIL: return "fail";
            case WARNING: return "warning";
            case ERROR: return "error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PASS: return "http://hl7.org/fhir/report-action-result-codes";
            case SKIP: return "http://hl7.org/fhir/report-action-result-codes";
            case FAIL: return "http://hl7.org/fhir/report-action-result-codes";
            case WARNING: return "http://hl7.org/fhir/report-action-result-codes";
            case ERROR: return "http://hl7.org/fhir/report-action-result-codes";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PASS: return "The action was successful.";
            case SKIP: return "The action was skipped.";
            case FAIL: return "The action failed.";
            case WARNING: return "The action passed but with warnings.";
            case ERROR: return "The action encountered a fatal error and the engine was unable to process.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PASS: return "Pass";
            case SKIP: return "Skip";
            case FAIL: return "Fail";
            case WARNING: return "Warning";
            case ERROR: return "Error";
            default: return "?";
          }
        }
    }

  public static class TestReportActionResultEnumFactory implements EnumFactory<TestReportActionResult> {
    public TestReportActionResult fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("pass".equals(codeString))
          return TestReportActionResult.PASS;
        if ("skip".equals(codeString))
          return TestReportActionResult.SKIP;
        if ("fail".equals(codeString))
          return TestReportActionResult.FAIL;
        if ("warning".equals(codeString))
          return TestReportActionResult.WARNING;
        if ("error".equals(codeString))
          return TestReportActionResult.ERROR;
        throw new IllegalArgumentException("Unknown TestReportActionResult code '"+codeString+"'");
        }
        public Enumeration<TestReportActionResult> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TestReportActionResult>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("pass".equals(codeString))
          return new Enumeration<TestReportActionResult>(this, TestReportActionResult.PASS);
        if ("skip".equals(codeString))
          return new Enumeration<TestReportActionResult>(this, TestReportActionResult.SKIP);
        if ("fail".equals(codeString))
          return new Enumeration<TestReportActionResult>(this, TestReportActionResult.FAIL);
        if ("warning".equals(codeString))
          return new Enumeration<TestReportActionResult>(this, TestReportActionResult.WARNING);
        if ("error".equals(codeString))
          return new Enumeration<TestReportActionResult>(this, TestReportActionResult.ERROR);
        throw new FHIRException("Unknown TestReportActionResult code '"+codeString+"'");
        }
    public String toCode(TestReportActionResult code) {
      if (code == TestReportActionResult.PASS)
        return "pass";
      if (code == TestReportActionResult.SKIP)
        return "skip";
      if (code == TestReportActionResult.FAIL)
        return "fail";
      if (code == TestReportActionResult.WARNING)
        return "warning";
      if (code == TestReportActionResult.ERROR)
        return "error";
      return "?";
      }
    public String toSystem(TestReportActionResult code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class TestReportParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of participant.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="test-engine | client | server", formalDefinition="The type of participant." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/report-participant-type")
        protected Enumeration<TestReportParticipantType> type;

        /**
         * The uri of the participant. An absolute URL is preferred.
         */
        @Child(name = "uri", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The uri of the participant. An absolute URL is preferred", formalDefinition="The uri of the participant. An absolute URL is preferred." )
        protected UriType uri;

        /**
         * The display name of the participant.
         */
        @Child(name = "display", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The display name of the participant", formalDefinition="The display name of the participant." )
        protected StringType display;

        private static final long serialVersionUID = 577488357L;

    /**
     * Constructor
     */
      public TestReportParticipantComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestReportParticipantComponent(Enumeration<TestReportParticipantType> type, UriType uri) {
        super();
        this.type = type;
        this.uri = uri;
      }

        /**
         * @return {@link #type} (The type of participant.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<TestReportParticipantType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestReportParticipantComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<TestReportParticipantType>(new TestReportParticipantTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of participant.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public TestReportParticipantComponent setTypeElement(Enumeration<TestReportParticipantType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of participant.
         */
        public TestReportParticipantType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of participant.
         */
        public TestReportParticipantComponent setType(TestReportParticipantType value) { 
            if (this.type == null)
              this.type = new Enumeration<TestReportParticipantType>(new TestReportParticipantTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #uri} (The uri of the participant. An absolute URL is preferred.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestReportParticipantComponent.uri");
            else if (Configuration.doAutoCreate())
              this.uri = new UriType(); // bb
          return this.uri;
        }

        public boolean hasUriElement() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        public boolean hasUri() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        /**
         * @param value {@link #uri} (The uri of the participant. An absolute URL is preferred.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public TestReportParticipantComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return The uri of the participant. An absolute URL is preferred.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value The uri of the participant. An absolute URL is preferred.
         */
        public TestReportParticipantComponent setUri(String value) { 
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          return this;
        }

        /**
         * @return {@link #display} (The display name of the participant.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestReportParticipantComponent.display");
            else if (Configuration.doAutoCreate())
              this.display = new StringType(); // bb
          return this.display;
        }

        public boolean hasDisplayElement() { 
          return this.display != null && !this.display.isEmpty();
        }

        public boolean hasDisplay() { 
          return this.display != null && !this.display.isEmpty();
        }

        /**
         * @param value {@link #display} (The display name of the participant.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public TestReportParticipantComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return The display name of the participant.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value The display name of the participant.
         */
        public TestReportParticipantComponent setDisplay(String value) { 
          if (Utilities.noString(value))
            this.display = null;
          else {
            if (this.display == null)
              this.display = new StringType();
            this.display.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of participant.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("uri", "uri", "The uri of the participant. An absolute URL is preferred.", 0, java.lang.Integer.MAX_VALUE, uri));
          childrenList.add(new Property("display", "string", "The display name of the participant.", 0, java.lang.Integer.MAX_VALUE, display));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<TestReportParticipantType>
        case 116076: /*uri*/ return this.uri == null ? new Base[0] : new Base[] {this.uri}; // UriType
        case 1671764162: /*display*/ return this.display == null ? new Base[0] : new Base[] {this.display}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new TestReportParticipantTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<TestReportParticipantType>
          return value;
        case 116076: // uri
          this.uri = castToUri(value); // UriType
          return value;
        case 1671764162: // display
          this.display = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new TestReportParticipantTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<TestReportParticipantType>
        } else if (name.equals("uri")) {
          this.uri = castToUri(value); // UriType
        } else if (name.equals("display")) {
          this.display = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 116076:  return getUriElement();
        case 1671764162:  return getDisplayElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 116076: /*uri*/ return new String[] {"uri"};
        case 1671764162: /*display*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.type");
        }
        else if (name.equals("uri")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.uri");
        }
        else if (name.equals("display")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.display");
        }
        else
          return super.addChild(name);
      }

      public TestReportParticipantComponent copy() {
        TestReportParticipantComponent dst = new TestReportParticipantComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.uri = uri == null ? null : uri.copy();
        dst.display = display == null ? null : display.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestReportParticipantComponent))
          return false;
        TestReportParticipantComponent o = (TestReportParticipantComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(uri, o.uri, true) && compareDeep(display, o.display, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestReportParticipantComponent))
          return false;
        TestReportParticipantComponent o = (TestReportParticipantComponent) other;
        return compareValues(type, o.type, true) && compareValues(uri, o.uri, true) && compareValues(display, o.display, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, uri, display);
      }

  public String fhirType() {
    return "TestReport.participant";

  }

  }

    @Block()
    public static class TestReportSetupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A setup operation or assert that was executed", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<SetupActionComponent> action;

        private static final long serialVersionUID = -123374486L;

    /**
     * Constructor
     */
      public TestReportSetupComponent() {
        super();
      }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
        public List<SetupActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<SetupActionComponent>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestReportSetupComponent setAction(List<SetupActionComponent> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (SetupActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SetupActionComponent addAction() { //3
          SetupActionComponent t = new SetupActionComponent();
          if (this.action == null)
            this.action = new ArrayList<SetupActionComponent>();
          this.action.add(t);
          return t;
        }

        public TestReportSetupComponent addAction(SetupActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<SetupActionComponent>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public SetupActionComponent getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("action", "", "Action would contain either an operation or an assertion.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // SetupActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950858: // action
          this.getAction().add((SetupActionComponent) value); // SetupActionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action")) {
          this.getAction().add((SetupActionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858:  return addAction(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public TestReportSetupComponent copy() {
        TestReportSetupComponent dst = new TestReportSetupComponent();
        copyValues(dst);
        if (action != null) {
          dst.action = new ArrayList<SetupActionComponent>();
          for (SetupActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestReportSetupComponent))
          return false;
        TestReportSetupComponent o = (TestReportSetupComponent) other;
        return compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestReportSetupComponent))
          return false;
        TestReportSetupComponent o = (TestReportSetupComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(action);
      }

  public String fhirType() {
    return "TestReport.setup";

  }

  }

    @Block()
    public static class SetupActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The operation performed.
         */
        @Child(name = "operation", type = {}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The operation to perform", formalDefinition="The operation performed." )
        protected SetupActionOperationComponent operation;

        /**
         * The results of the assertion performed on the previous operations.
         */
        @Child(name = "assert", type = {}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The assertion to perform", formalDefinition="The results of the assertion performed on the previous operations." )
        protected SetupActionAssertComponent assert_;

        private static final long serialVersionUID = -252088305L;

    /**
     * Constructor
     */
      public SetupActionComponent() {
        super();
      }

        /**
         * @return {@link #operation} (The operation performed.)
         */
        public SetupActionOperationComponent getOperation() { 
          if (this.operation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionComponent.operation");
            else if (Configuration.doAutoCreate())
              this.operation = new SetupActionOperationComponent(); // cc
          return this.operation;
        }

        public boolean hasOperation() { 
          return this.operation != null && !this.operation.isEmpty();
        }

        /**
         * @param value {@link #operation} (The operation performed.)
         */
        public SetupActionComponent setOperation(SetupActionOperationComponent value) { 
          this.operation = value;
          return this;
        }

        /**
         * @return {@link #assert_} (The results of the assertion performed on the previous operations.)
         */
        public SetupActionAssertComponent getAssert() { 
          if (this.assert_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionComponent.assert_");
            else if (Configuration.doAutoCreate())
              this.assert_ = new SetupActionAssertComponent(); // cc
          return this.assert_;
        }

        public boolean hasAssert() { 
          return this.assert_ != null && !this.assert_.isEmpty();
        }

        /**
         * @param value {@link #assert_} (The results of the assertion performed on the previous operations.)
         */
        public SetupActionComponent setAssert(SetupActionAssertComponent value) { 
          this.assert_ = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "", "The operation performed.", 0, java.lang.Integer.MAX_VALUE, operation));
          childrenList.add(new Property("assert", "", "The results of the assertion performed on the previous operations.", 0, java.lang.Integer.MAX_VALUE, assert_));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1662702951: /*operation*/ return this.operation == null ? new Base[0] : new Base[] {this.operation}; // SetupActionOperationComponent
        case -1408208058: /*assert*/ return this.assert_ == null ? new Base[0] : new Base[] {this.assert_}; // SetupActionAssertComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1662702951: // operation
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
          return value;
        case -1408208058: // assert
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("operation")) {
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
        } else if (name.equals("assert")) {
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951:  return getOperation(); 
        case -1408208058:  return getAssert(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951: /*operation*/ return new String[] {};
        case -1408208058: /*assert*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("operation")) {
          this.operation = new SetupActionOperationComponent();
          return this.operation;
        }
        else if (name.equals("assert")) {
          this.assert_ = new SetupActionAssertComponent();
          return this.assert_;
        }
        else
          return super.addChild(name);
      }

      public SetupActionComponent copy() {
        SetupActionComponent dst = new SetupActionComponent();
        copyValues(dst);
        dst.operation = operation == null ? null : operation.copy();
        dst.assert_ = assert_ == null ? null : assert_.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionComponent))
          return false;
        SetupActionComponent o = (SetupActionComponent) other;
        return compareDeep(operation, o.operation, true) && compareDeep(assert_, o.assert_, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionComponent))
          return false;
        SetupActionComponent o = (SetupActionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(operation, assert_);
      }

  public String fhirType() {
    return "TestReport.setup.action";

  }

  }

    @Block()
    public static class SetupActionOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The result of this operation.
         */
        @Child(name = "result", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="pass | skip | fail | warning | error", formalDefinition="The result of this operation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/report-action-result-codes")
        protected Enumeration<TestReportActionResult> result;

        /**
         * An explanatory message associated with the result.
         */
        @Child(name = "message", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A message associated with the result", formalDefinition="An explanatory message associated with the result." )
        protected MarkdownType message;

        /**
         * A link to further details on the result.
         */
        @Child(name = "detail", type = {UriType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A link to further details on the result", formalDefinition="A link to further details on the result." )
        protected UriType detail;

        private static final long serialVersionUID = 269088798L;

    /**
     * Constructor
     */
      public SetupActionOperationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionOperationComponent(Enumeration<TestReportActionResult> result) {
        super();
        this.result = result;
      }

        /**
         * @return {@link #result} (The result of this operation.). This is the underlying object with id, value and extensions. The accessor "getResult" gives direct access to the value
         */
        public Enumeration<TestReportActionResult> getResultElement() { 
          if (this.result == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.result");
            else if (Configuration.doAutoCreate())
              this.result = new Enumeration<TestReportActionResult>(new TestReportActionResultEnumFactory()); // bb
          return this.result;
        }

        public boolean hasResultElement() { 
          return this.result != null && !this.result.isEmpty();
        }

        public boolean hasResult() { 
          return this.result != null && !this.result.isEmpty();
        }

        /**
         * @param value {@link #result} (The result of this operation.). This is the underlying object with id, value and extensions. The accessor "getResult" gives direct access to the value
         */
        public SetupActionOperationComponent setResultElement(Enumeration<TestReportActionResult> value) { 
          this.result = value;
          return this;
        }

        /**
         * @return The result of this operation.
         */
        public TestReportActionResult getResult() { 
          return this.result == null ? null : this.result.getValue();
        }

        /**
         * @param value The result of this operation.
         */
        public SetupActionOperationComponent setResult(TestReportActionResult value) { 
            if (this.result == null)
              this.result = new Enumeration<TestReportActionResult>(new TestReportActionResultEnumFactory());
            this.result.setValue(value);
          return this;
        }

        /**
         * @return {@link #message} (An explanatory message associated with the result.). This is the underlying object with id, value and extensions. The accessor "getMessage" gives direct access to the value
         */
        public MarkdownType getMessageElement() { 
          if (this.message == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.message");
            else if (Configuration.doAutoCreate())
              this.message = new MarkdownType(); // bb
          return this.message;
        }

        public boolean hasMessageElement() { 
          return this.message != null && !this.message.isEmpty();
        }

        public boolean hasMessage() { 
          return this.message != null && !this.message.isEmpty();
        }

        /**
         * @param value {@link #message} (An explanatory message associated with the result.). This is the underlying object with id, value and extensions. The accessor "getMessage" gives direct access to the value
         */
        public SetupActionOperationComponent setMessageElement(MarkdownType value) { 
          this.message = value;
          return this;
        }

        /**
         * @return An explanatory message associated with the result.
         */
        public String getMessage() { 
          return this.message == null ? null : this.message.getValue();
        }

        /**
         * @param value An explanatory message associated with the result.
         */
        public SetupActionOperationComponent setMessage(String value) { 
          if (value == null)
            this.message = null;
          else {
            if (this.message == null)
              this.message = new MarkdownType();
            this.message.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #detail} (A link to further details on the result.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
         */
        public UriType getDetailElement() { 
          if (this.detail == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detail = new UriType(); // bb
          return this.detail;
        }

        public boolean hasDetailElement() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (A link to further details on the result.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
         */
        public SetupActionOperationComponent setDetailElement(UriType value) { 
          this.detail = value;
          return this;
        }

        /**
         * @return A link to further details on the result.
         */
        public String getDetail() { 
          return this.detail == null ? null : this.detail.getValue();
        }

        /**
         * @param value A link to further details on the result.
         */
        public SetupActionOperationComponent setDetail(String value) { 
          if (Utilities.noString(value))
            this.detail = null;
          else {
            if (this.detail == null)
              this.detail = new UriType();
            this.detail.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("result", "code", "The result of this operation.", 0, java.lang.Integer.MAX_VALUE, result));
          childrenList.add(new Property("message", "markdown", "An explanatory message associated with the result.", 0, java.lang.Integer.MAX_VALUE, message));
          childrenList.add(new Property("detail", "uri", "A link to further details on the result.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -934426595: /*result*/ return this.result == null ? new Base[0] : new Base[] {this.result}; // Enumeration<TestReportActionResult>
        case 954925063: /*message*/ return this.message == null ? new Base[0] : new Base[] {this.message}; // MarkdownType
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : new Base[] {this.detail}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -934426595: // result
          value = new TestReportActionResultEnumFactory().fromType(castToCode(value));
          this.result = (Enumeration) value; // Enumeration<TestReportActionResult>
          return value;
        case 954925063: // message
          this.message = castToMarkdown(value); // MarkdownType
          return value;
        case -1335224239: // detail
          this.detail = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("result")) {
          value = new TestReportActionResultEnumFactory().fromType(castToCode(value));
          this.result = (Enumeration) value; // Enumeration<TestReportActionResult>
        } else if (name.equals("message")) {
          this.message = castToMarkdown(value); // MarkdownType
        } else if (name.equals("detail")) {
          this.detail = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -934426595:  return getResultElement();
        case 954925063:  return getMessageElement();
        case -1335224239:  return getDetailElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -934426595: /*result*/ return new String[] {"code"};
        case 954925063: /*message*/ return new String[] {"markdown"};
        case -1335224239: /*detail*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("result")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.result");
        }
        else if (name.equals("message")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.message");
        }
        else if (name.equals("detail")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.detail");
        }
        else
          return super.addChild(name);
      }

      public SetupActionOperationComponent copy() {
        SetupActionOperationComponent dst = new SetupActionOperationComponent();
        copyValues(dst);
        dst.result = result == null ? null : result.copy();
        dst.message = message == null ? null : message.copy();
        dst.detail = detail == null ? null : detail.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionOperationComponent))
          return false;
        SetupActionOperationComponent o = (SetupActionOperationComponent) other;
        return compareDeep(result, o.result, true) && compareDeep(message, o.message, true) && compareDeep(detail, o.detail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionOperationComponent))
          return false;
        SetupActionOperationComponent o = (SetupActionOperationComponent) other;
        return compareValues(result, o.result, true) && compareValues(message, o.message, true) && compareValues(detail, o.detail, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(result, message, detail
          );
      }

  public String fhirType() {
    return "TestReport.setup.action.operation";

  }

  }

    @Block()
    public static class SetupActionAssertComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The result of this assertion.
         */
        @Child(name = "result", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="pass | skip | fail | warning | error", formalDefinition="The result of this assertion." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/report-action-result-codes")
        protected Enumeration<TestReportActionResult> result;

        /**
         * An explanatory message associated with the result.
         */
        @Child(name = "message", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A message associated with the result", formalDefinition="An explanatory message associated with the result." )
        protected MarkdownType message;

        /**
         * A link to further details on the result.
         */
        @Child(name = "detail", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A link to further details on the result", formalDefinition="A link to further details on the result." )
        protected StringType detail;

        private static final long serialVersionUID = 467968193L;

    /**
     * Constructor
     */
      public SetupActionAssertComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionAssertComponent(Enumeration<TestReportActionResult> result) {
        super();
        this.result = result;
      }

        /**
         * @return {@link #result} (The result of this assertion.). This is the underlying object with id, value and extensions. The accessor "getResult" gives direct access to the value
         */
        public Enumeration<TestReportActionResult> getResultElement() { 
          if (this.result == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.result");
            else if (Configuration.doAutoCreate())
              this.result = new Enumeration<TestReportActionResult>(new TestReportActionResultEnumFactory()); // bb
          return this.result;
        }

        public boolean hasResultElement() { 
          return this.result != null && !this.result.isEmpty();
        }

        public boolean hasResult() { 
          return this.result != null && !this.result.isEmpty();
        }

        /**
         * @param value {@link #result} (The result of this assertion.). This is the underlying object with id, value and extensions. The accessor "getResult" gives direct access to the value
         */
        public SetupActionAssertComponent setResultElement(Enumeration<TestReportActionResult> value) { 
          this.result = value;
          return this;
        }

        /**
         * @return The result of this assertion.
         */
        public TestReportActionResult getResult() { 
          return this.result == null ? null : this.result.getValue();
        }

        /**
         * @param value The result of this assertion.
         */
        public SetupActionAssertComponent setResult(TestReportActionResult value) { 
            if (this.result == null)
              this.result = new Enumeration<TestReportActionResult>(new TestReportActionResultEnumFactory());
            this.result.setValue(value);
          return this;
        }

        /**
         * @return {@link #message} (An explanatory message associated with the result.). This is the underlying object with id, value and extensions. The accessor "getMessage" gives direct access to the value
         */
        public MarkdownType getMessageElement() { 
          if (this.message == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.message");
            else if (Configuration.doAutoCreate())
              this.message = new MarkdownType(); // bb
          return this.message;
        }

        public boolean hasMessageElement() { 
          return this.message != null && !this.message.isEmpty();
        }

        public boolean hasMessage() { 
          return this.message != null && !this.message.isEmpty();
        }

        /**
         * @param value {@link #message} (An explanatory message associated with the result.). This is the underlying object with id, value and extensions. The accessor "getMessage" gives direct access to the value
         */
        public SetupActionAssertComponent setMessageElement(MarkdownType value) { 
          this.message = value;
          return this;
        }

        /**
         * @return An explanatory message associated with the result.
         */
        public String getMessage() { 
          return this.message == null ? null : this.message.getValue();
        }

        /**
         * @param value An explanatory message associated with the result.
         */
        public SetupActionAssertComponent setMessage(String value) { 
          if (value == null)
            this.message = null;
          else {
            if (this.message == null)
              this.message = new MarkdownType();
            this.message.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #detail} (A link to further details on the result.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
         */
        public StringType getDetailElement() { 
          if (this.detail == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detail = new StringType(); // bb
          return this.detail;
        }

        public boolean hasDetailElement() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (A link to further details on the result.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
         */
        public SetupActionAssertComponent setDetailElement(StringType value) { 
          this.detail = value;
          return this;
        }

        /**
         * @return A link to further details on the result.
         */
        public String getDetail() { 
          return this.detail == null ? null : this.detail.getValue();
        }

        /**
         * @param value A link to further details on the result.
         */
        public SetupActionAssertComponent setDetail(String value) { 
          if (Utilities.noString(value))
            this.detail = null;
          else {
            if (this.detail == null)
              this.detail = new StringType();
            this.detail.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("result", "code", "The result of this assertion.", 0, java.lang.Integer.MAX_VALUE, result));
          childrenList.add(new Property("message", "markdown", "An explanatory message associated with the result.", 0, java.lang.Integer.MAX_VALUE, message));
          childrenList.add(new Property("detail", "string", "A link to further details on the result.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -934426595: /*result*/ return this.result == null ? new Base[0] : new Base[] {this.result}; // Enumeration<TestReportActionResult>
        case 954925063: /*message*/ return this.message == null ? new Base[0] : new Base[] {this.message}; // MarkdownType
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : new Base[] {this.detail}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -934426595: // result
          value = new TestReportActionResultEnumFactory().fromType(castToCode(value));
          this.result = (Enumeration) value; // Enumeration<TestReportActionResult>
          return value;
        case 954925063: // message
          this.message = castToMarkdown(value); // MarkdownType
          return value;
        case -1335224239: // detail
          this.detail = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("result")) {
          value = new TestReportActionResultEnumFactory().fromType(castToCode(value));
          this.result = (Enumeration) value; // Enumeration<TestReportActionResult>
        } else if (name.equals("message")) {
          this.message = castToMarkdown(value); // MarkdownType
        } else if (name.equals("detail")) {
          this.detail = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -934426595:  return getResultElement();
        case 954925063:  return getMessageElement();
        case -1335224239:  return getDetailElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -934426595: /*result*/ return new String[] {"code"};
        case 954925063: /*message*/ return new String[] {"markdown"};
        case -1335224239: /*detail*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("result")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.result");
        }
        else if (name.equals("message")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.message");
        }
        else if (name.equals("detail")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.detail");
        }
        else
          return super.addChild(name);
      }

      public SetupActionAssertComponent copy() {
        SetupActionAssertComponent dst = new SetupActionAssertComponent();
        copyValues(dst);
        dst.result = result == null ? null : result.copy();
        dst.message = message == null ? null : message.copy();
        dst.detail = detail == null ? null : detail.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionAssertComponent))
          return false;
        SetupActionAssertComponent o = (SetupActionAssertComponent) other;
        return compareDeep(result, o.result, true) && compareDeep(message, o.message, true) && compareDeep(detail, o.detail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionAssertComponent))
          return false;
        SetupActionAssertComponent o = (SetupActionAssertComponent) other;
        return compareValues(result, o.result, true) && compareValues(message, o.message, true) && compareValues(detail, o.detail, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(result, message, detail
          );
      }

  public String fhirType() {
    return "TestReport.setup.action.assert";

  }

  }

    @Block()
    public static class TestReportTestComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of this test used for tracking/logging purposes by test engines.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Tracking/logging name of this test", formalDefinition="The name of this test used for tracking/logging purposes by test engines." )
        protected StringType name;

        /**
         * A short description of the test used by test engines for tracking and reporting purposes.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Tracking/reporting short description of the test", formalDefinition="A short description of the test used by test engines for tracking and reporting purposes." )
        protected StringType description;

        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A test operation or assert that was performed", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<TestActionComponent> action;

        private static final long serialVersionUID = -865006110L;

    /**
     * Constructor
     */
      public TestReportTestComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of this test used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestReportTestComponent.name");
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
         * @param value {@link #name} (The name of this test used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public TestReportTestComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of this test used for tracking/logging purposes by test engines.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of this test used for tracking/logging purposes by test engines.
         */
        public TestReportTestComponent setName(String value) { 
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
         * @return {@link #description} (A short description of the test used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestReportTestComponent.description");
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
         * @param value {@link #description} (A short description of the test used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestReportTestComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A short description of the test used by test engines for tracking and reporting purposes.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A short description of the test used by test engines for tracking and reporting purposes.
         */
        public TestReportTestComponent setDescription(String value) { 
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
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
        public List<TestActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<TestActionComponent>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestReportTestComponent setAction(List<TestActionComponent> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (TestActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public TestActionComponent addAction() { //3
          TestActionComponent t = new TestActionComponent();
          if (this.action == null)
            this.action = new ArrayList<TestActionComponent>();
          this.action.add(t);
          return t;
        }

        public TestReportTestComponent addAction(TestActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<TestActionComponent>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public TestActionComponent getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of this test used for tracking/logging purposes by test engines.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "A short description of the test used by test engines for tracking and reporting purposes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("action", "", "Action would contain either an operation or an assertion.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // TestActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1422950858: // action
          this.getAction().add((TestActionComponent) value); // TestActionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("action")) {
          this.getAction().add((TestActionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case -1422950858:  return addAction(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1422950858: /*action*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.description");
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public TestReportTestComponent copy() {
        TestReportTestComponent dst = new TestReportTestComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        if (action != null) {
          dst.action = new ArrayList<TestActionComponent>();
          for (TestActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestReportTestComponent))
          return false;
        TestReportTestComponent o = (TestReportTestComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(action, o.action, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestReportTestComponent))
          return false;
        TestReportTestComponent o = (TestReportTestComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, description, action
          );
      }

  public String fhirType() {
    return "TestReport.test";

  }

  }

    @Block()
    public static class TestActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An operation would involve a REST request to a server.
         */
        @Child(name = "operation", type = {SetupActionOperationComponent.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The operation performed", formalDefinition="An operation would involve a REST request to a server." )
        protected SetupActionOperationComponent operation;

        /**
         * The results of the assertion performed on the previous operations.
         */
        @Child(name = "assert", type = {SetupActionAssertComponent.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The assertion performed", formalDefinition="The results of the assertion performed on the previous operations." )
        protected SetupActionAssertComponent assert_;

        private static final long serialVersionUID = -252088305L;

    /**
     * Constructor
     */
      public TestActionComponent() {
        super();
      }

        /**
         * @return {@link #operation} (An operation would involve a REST request to a server.)
         */
        public SetupActionOperationComponent getOperation() { 
          if (this.operation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestActionComponent.operation");
            else if (Configuration.doAutoCreate())
              this.operation = new SetupActionOperationComponent(); // cc
          return this.operation;
        }

        public boolean hasOperation() { 
          return this.operation != null && !this.operation.isEmpty();
        }

        /**
         * @param value {@link #operation} (An operation would involve a REST request to a server.)
         */
        public TestActionComponent setOperation(SetupActionOperationComponent value) { 
          this.operation = value;
          return this;
        }

        /**
         * @return {@link #assert_} (The results of the assertion performed on the previous operations.)
         */
        public SetupActionAssertComponent getAssert() { 
          if (this.assert_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestActionComponent.assert_");
            else if (Configuration.doAutoCreate())
              this.assert_ = new SetupActionAssertComponent(); // cc
          return this.assert_;
        }

        public boolean hasAssert() { 
          return this.assert_ != null && !this.assert_.isEmpty();
        }

        /**
         * @param value {@link #assert_} (The results of the assertion performed on the previous operations.)
         */
        public TestActionComponent setAssert(SetupActionAssertComponent value) { 
          this.assert_ = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "@TestReport.setup.action.operation", "An operation would involve a REST request to a server.", 0, java.lang.Integer.MAX_VALUE, operation));
          childrenList.add(new Property("assert", "@TestReport.setup.action.assert", "The results of the assertion performed on the previous operations.", 0, java.lang.Integer.MAX_VALUE, assert_));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1662702951: /*operation*/ return this.operation == null ? new Base[0] : new Base[] {this.operation}; // SetupActionOperationComponent
        case -1408208058: /*assert*/ return this.assert_ == null ? new Base[0] : new Base[] {this.assert_}; // SetupActionAssertComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1662702951: // operation
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
          return value;
        case -1408208058: // assert
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("operation")) {
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
        } else if (name.equals("assert")) {
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951:  return getOperation(); 
        case -1408208058:  return getAssert(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951: /*operation*/ return new String[] {"@TestReport.setup.action.operation"};
        case -1408208058: /*assert*/ return new String[] {"@TestReport.setup.action.assert"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("operation")) {
          this.operation = new SetupActionOperationComponent();
          return this.operation;
        }
        else if (name.equals("assert")) {
          this.assert_ = new SetupActionAssertComponent();
          return this.assert_;
        }
        else
          return super.addChild(name);
      }

      public TestActionComponent copy() {
        TestActionComponent dst = new TestActionComponent();
        copyValues(dst);
        dst.operation = operation == null ? null : operation.copy();
        dst.assert_ = assert_ == null ? null : assert_.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestActionComponent))
          return false;
        TestActionComponent o = (TestActionComponent) other;
        return compareDeep(operation, o.operation, true) && compareDeep(assert_, o.assert_, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestActionComponent))
          return false;
        TestActionComponent o = (TestActionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(operation, assert_);
      }

  public String fhirType() {
    return "TestReport.test.action";

  }

  }

    @Block()
    public static class TestReportTeardownComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The teardown action will only contain an operation.
         */
        @Child(name = "action", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="One or more teardown operations performed", formalDefinition="The teardown action will only contain an operation." )
        protected List<TeardownActionComponent> action;

        private static final long serialVersionUID = 1168638089L;

    /**
     * Constructor
     */
      public TestReportTeardownComponent() {
        super();
      }

        /**
         * @return {@link #action} (The teardown action will only contain an operation.)
         */
        public List<TeardownActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<TeardownActionComponent>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestReportTeardownComponent setAction(List<TeardownActionComponent> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (TeardownActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public TeardownActionComponent addAction() { //3
          TeardownActionComponent t = new TeardownActionComponent();
          if (this.action == null)
            this.action = new ArrayList<TeardownActionComponent>();
          this.action.add(t);
          return t;
        }

        public TestReportTeardownComponent addAction(TeardownActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<TeardownActionComponent>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public TeardownActionComponent getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("action", "", "The teardown action will only contain an operation.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // TeardownActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950858: // action
          this.getAction().add((TeardownActionComponent) value); // TeardownActionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action")) {
          this.getAction().add((TeardownActionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858:  return addAction(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public TestReportTeardownComponent copy() {
        TestReportTeardownComponent dst = new TestReportTeardownComponent();
        copyValues(dst);
        if (action != null) {
          dst.action = new ArrayList<TeardownActionComponent>();
          for (TeardownActionComponent i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestReportTeardownComponent))
          return false;
        TestReportTeardownComponent o = (TestReportTeardownComponent) other;
        return compareDeep(action, o.action, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestReportTeardownComponent))
          return false;
        TestReportTeardownComponent o = (TestReportTeardownComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(action);
      }

  public String fhirType() {
    return "TestReport.teardown";

  }

  }

    @Block()
    public static class TeardownActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An operation would involve a REST request to a server.
         */
        @Child(name = "operation", type = {SetupActionOperationComponent.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The teardown operation performed", formalDefinition="An operation would involve a REST request to a server." )
        protected SetupActionOperationComponent operation;

        private static final long serialVersionUID = -1099598054L;

    /**
     * Constructor
     */
      public TeardownActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TeardownActionComponent(SetupActionOperationComponent operation) {
        super();
        this.operation = operation;
      }

        /**
         * @return {@link #operation} (An operation would involve a REST request to a server.)
         */
        public SetupActionOperationComponent getOperation() { 
          if (this.operation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TeardownActionComponent.operation");
            else if (Configuration.doAutoCreate())
              this.operation = new SetupActionOperationComponent(); // cc
          return this.operation;
        }

        public boolean hasOperation() { 
          return this.operation != null && !this.operation.isEmpty();
        }

        /**
         * @param value {@link #operation} (An operation would involve a REST request to a server.)
         */
        public TeardownActionComponent setOperation(SetupActionOperationComponent value) { 
          this.operation = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "@TestReport.setup.action.operation", "An operation would involve a REST request to a server.", 0, java.lang.Integer.MAX_VALUE, operation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1662702951: /*operation*/ return this.operation == null ? new Base[0] : new Base[] {this.operation}; // SetupActionOperationComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1662702951: // operation
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("operation")) {
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951:  return getOperation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951: /*operation*/ return new String[] {"@TestReport.setup.action.operation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("operation")) {
          this.operation = new SetupActionOperationComponent();
          return this.operation;
        }
        else
          return super.addChild(name);
      }

      public TeardownActionComponent copy() {
        TeardownActionComponent dst = new TeardownActionComponent();
        copyValues(dst);
        dst.operation = operation == null ? null : operation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TeardownActionComponent))
          return false;
        TeardownActionComponent o = (TeardownActionComponent) other;
        return compareDeep(operation, o.operation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TeardownActionComponent))
          return false;
        TeardownActionComponent o = (TeardownActionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(operation);
      }

  public String fhirType() {
    return "TestReport.teardown.action";

  }

  }

    /**
     * Identifier for the TestScript assigned for external purposes outside the context of FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="Identifier for the TestScript assigned for external purposes outside the context of FHIR." )
    protected Identifier identifier;

    /**
     * A free text natural language name identifying the executed TestScript.
     */
    @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name of the executed TestScript", formalDefinition="A free text natural language name identifying the executed TestScript." )
    protected StringType name;

    /**
     * The current state of this test report.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="completed | in-progress | waiting | stopped | entered-in-error", formalDefinition="The current state of this test report." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/report-status-codes")
    protected Enumeration<TestReportStatus> status;

    /**
     * Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`.
     */
    @Child(name = "testScript", type = {TestScript.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reference to the  version-specific TestScript that was executed to produce this TestReport", formalDefinition="Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`." )
    protected Reference testScript;

    /**
     * The actual object that is the target of the reference (Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`.)
     */
    protected TestScript testScriptTarget;

    /**
     * The overall result from the execution of the TestScript.
     */
    @Child(name = "result", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="pass | fail | pending", formalDefinition="The overall result from the execution of the TestScript." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/report-result-codes")
    protected Enumeration<TestReportResult> result;

    /**
     * The final score (percentage of tests passed) resulting from the execution of the TestScript.
     */
    @Child(name = "score", type = {DecimalType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The final score (percentage of tests passed) resulting from the execution of the TestScript", formalDefinition="The final score (percentage of tests passed) resulting from the execution of the TestScript." )
    protected DecimalType score;

    /**
     * Name of the tester producing this report (Organization or individual).
     */
    @Child(name = "tester", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the tester producing this report (Organization or individual)", formalDefinition="Name of the tester producing this report (Organization or individual)." )
    protected StringType tester;

    /**
     * When the TestScript was executed and this TestReport was generated.
     */
    @Child(name = "issued", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the TestScript was executed and this TestReport was generated", formalDefinition="When the TestScript was executed and this TestReport was generated." )
    protected DateTimeType issued;

    /**
     * A participant in the test execution, either the execution engine, a client, or a server.
     */
    @Child(name = "participant", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A participant in the test execution, either the execution engine, a client, or a server", formalDefinition="A participant in the test execution, either the execution engine, a client, or a server." )
    protected List<TestReportParticipantComponent> participant;

    /**
     * The results of the series of required setup operations before the tests were executed.
     */
    @Child(name = "setup", type = {}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The results of the series of required setup operations before the tests were executed", formalDefinition="The results of the series of required setup operations before the tests were executed." )
    protected TestReportSetupComponent setup;

    /**
     * A test executed from the test script.
     */
    @Child(name = "test", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A test executed from the test script", formalDefinition="A test executed from the test script." )
    protected List<TestReportTestComponent> test;

    /**
     * The results of the series of operations required to clean up after the all the tests were executed (successfully or otherwise).
     */
    @Child(name = "teardown", type = {}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The results of running the series of required clean up steps", formalDefinition="The results of the series of operations required to clean up after the all the tests were executed (successfully or otherwise)." )
    protected TestReportTeardownComponent teardown;

    private static final long serialVersionUID = 79474516L;

  /**
   * Constructor
   */
    public TestReport() {
      super();
    }

  /**
   * Constructor
   */
    public TestReport(Enumeration<TestReportStatus> status, Reference testScript, Enumeration<TestReportResult> result) {
      super();
      this.status = status;
      this.testScript = testScript;
      this.result = result;
    }

    /**
     * @return {@link #identifier} (Identifier for the TestScript assigned for external purposes outside the context of FHIR.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Identifier for the TestScript assigned for external purposes outside the context of FHIR.)
     */
    public TestReport setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #name} (A free text natural language name identifying the executed TestScript.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.name");
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
     * @param value {@link #name} (A free text natural language name identifying the executed TestScript.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public TestReport setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the executed TestScript.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the executed TestScript.
     */
    public TestReport setName(String value) { 
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
     * @return {@link #status} (The current state of this test report.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<TestReportStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<TestReportStatus>(new TestReportStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of this test report.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public TestReport setStatusElement(Enumeration<TestReportStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of this test report.
     */
    public TestReportStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of this test report.
     */
    public TestReport setStatus(TestReportStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<TestReportStatus>(new TestReportStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #testScript} (Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`.)
     */
    public Reference getTestScript() { 
      if (this.testScript == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.testScript");
        else if (Configuration.doAutoCreate())
          this.testScript = new Reference(); // cc
      return this.testScript;
    }

    public boolean hasTestScript() { 
      return this.testScript != null && !this.testScript.isEmpty();
    }

    /**
     * @param value {@link #testScript} (Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`.)
     */
    public TestReport setTestScript(Reference value) { 
      this.testScript = value;
      return this;
    }

    /**
     * @return {@link #testScript} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`.)
     */
    public TestScript getTestScriptTarget() { 
      if (this.testScriptTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.testScript");
        else if (Configuration.doAutoCreate())
          this.testScriptTarget = new TestScript(); // aa
      return this.testScriptTarget;
    }

    /**
     * @param value {@link #testScript} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`.)
     */
    public TestReport setTestScriptTarget(TestScript value) { 
      this.testScriptTarget = value;
      return this;
    }

    /**
     * @return {@link #result} (The overall result from the execution of the TestScript.). This is the underlying object with id, value and extensions. The accessor "getResult" gives direct access to the value
     */
    public Enumeration<TestReportResult> getResultElement() { 
      if (this.result == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.result");
        else if (Configuration.doAutoCreate())
          this.result = new Enumeration<TestReportResult>(new TestReportResultEnumFactory()); // bb
      return this.result;
    }

    public boolean hasResultElement() { 
      return this.result != null && !this.result.isEmpty();
    }

    public boolean hasResult() { 
      return this.result != null && !this.result.isEmpty();
    }

    /**
     * @param value {@link #result} (The overall result from the execution of the TestScript.). This is the underlying object with id, value and extensions. The accessor "getResult" gives direct access to the value
     */
    public TestReport setResultElement(Enumeration<TestReportResult> value) { 
      this.result = value;
      return this;
    }

    /**
     * @return The overall result from the execution of the TestScript.
     */
    public TestReportResult getResult() { 
      return this.result == null ? null : this.result.getValue();
    }

    /**
     * @param value The overall result from the execution of the TestScript.
     */
    public TestReport setResult(TestReportResult value) { 
        if (this.result == null)
          this.result = new Enumeration<TestReportResult>(new TestReportResultEnumFactory());
        this.result.setValue(value);
      return this;
    }

    /**
     * @return {@link #score} (The final score (percentage of tests passed) resulting from the execution of the TestScript.). This is the underlying object with id, value and extensions. The accessor "getScore" gives direct access to the value
     */
    public DecimalType getScoreElement() { 
      if (this.score == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.score");
        else if (Configuration.doAutoCreate())
          this.score = new DecimalType(); // bb
      return this.score;
    }

    public boolean hasScoreElement() { 
      return this.score != null && !this.score.isEmpty();
    }

    public boolean hasScore() { 
      return this.score != null && !this.score.isEmpty();
    }

    /**
     * @param value {@link #score} (The final score (percentage of tests passed) resulting from the execution of the TestScript.). This is the underlying object with id, value and extensions. The accessor "getScore" gives direct access to the value
     */
    public TestReport setScoreElement(DecimalType value) { 
      this.score = value;
      return this;
    }

    /**
     * @return The final score (percentage of tests passed) resulting from the execution of the TestScript.
     */
    public BigDecimal getScore() { 
      return this.score == null ? null : this.score.getValue();
    }

    /**
     * @param value The final score (percentage of tests passed) resulting from the execution of the TestScript.
     */
    public TestReport setScore(BigDecimal value) { 
      if (value == null)
        this.score = null;
      else {
        if (this.score == null)
          this.score = new DecimalType();
        this.score.setValue(value);
      }
      return this;
    }

    /**
     * @param value The final score (percentage of tests passed) resulting from the execution of the TestScript.
     */
    public TestReport setScore(long value) { 
          this.score = new DecimalType();
        this.score.setValue(value);
      return this;
    }

    /**
     * @param value The final score (percentage of tests passed) resulting from the execution of the TestScript.
     */
    public TestReport setScore(double value) { 
          this.score = new DecimalType();
        this.score.setValue(value);
      return this;
    }

    /**
     * @return {@link #tester} (Name of the tester producing this report (Organization or individual).). This is the underlying object with id, value and extensions. The accessor "getTester" gives direct access to the value
     */
    public StringType getTesterElement() { 
      if (this.tester == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.tester");
        else if (Configuration.doAutoCreate())
          this.tester = new StringType(); // bb
      return this.tester;
    }

    public boolean hasTesterElement() { 
      return this.tester != null && !this.tester.isEmpty();
    }

    public boolean hasTester() { 
      return this.tester != null && !this.tester.isEmpty();
    }

    /**
     * @param value {@link #tester} (Name of the tester producing this report (Organization or individual).). This is the underlying object with id, value and extensions. The accessor "getTester" gives direct access to the value
     */
    public TestReport setTesterElement(StringType value) { 
      this.tester = value;
      return this;
    }

    /**
     * @return Name of the tester producing this report (Organization or individual).
     */
    public String getTester() { 
      return this.tester == null ? null : this.tester.getValue();
    }

    /**
     * @param value Name of the tester producing this report (Organization or individual).
     */
    public TestReport setTester(String value) { 
      if (Utilities.noString(value))
        this.tester = null;
      else {
        if (this.tester == null)
          this.tester = new StringType();
        this.tester.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #issued} (When the TestScript was executed and this TestReport was generated.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DateTimeType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new DateTimeType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (When the TestScript was executed and this TestReport was generated.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public TestReport setIssuedElement(DateTimeType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return When the TestScript was executed and this TestReport was generated.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value When the TestScript was executed and this TestReport was generated.
     */
    public TestReport setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new DateTimeType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #participant} (A participant in the test execution, either the execution engine, a client, or a server.)
     */
    public List<TestReportParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<TestReportParticipantComponent>();
      return this.participant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestReport setParticipant(List<TestReportParticipantComponent> theParticipant) { 
      this.participant = theParticipant;
      return this;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (TestReportParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestReportParticipantComponent addParticipant() { //3
      TestReportParticipantComponent t = new TestReportParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<TestReportParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    public TestReport addParticipant(TestReportParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<TestReportParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
     */
    public TestReportParticipantComponent getParticipantFirstRep() { 
      if (getParticipant().isEmpty()) {
        addParticipant();
      }
      return getParticipant().get(0);
    }

    /**
     * @return {@link #setup} (The results of the series of required setup operations before the tests were executed.)
     */
    public TestReportSetupComponent getSetup() { 
      if (this.setup == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.setup");
        else if (Configuration.doAutoCreate())
          this.setup = new TestReportSetupComponent(); // cc
      return this.setup;
    }

    public boolean hasSetup() { 
      return this.setup != null && !this.setup.isEmpty();
    }

    /**
     * @param value {@link #setup} (The results of the series of required setup operations before the tests were executed.)
     */
    public TestReport setSetup(TestReportSetupComponent value) { 
      this.setup = value;
      return this;
    }

    /**
     * @return {@link #test} (A test executed from the test script.)
     */
    public List<TestReportTestComponent> getTest() { 
      if (this.test == null)
        this.test = new ArrayList<TestReportTestComponent>();
      return this.test;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestReport setTest(List<TestReportTestComponent> theTest) { 
      this.test = theTest;
      return this;
    }

    public boolean hasTest() { 
      if (this.test == null)
        return false;
      for (TestReportTestComponent item : this.test)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestReportTestComponent addTest() { //3
      TestReportTestComponent t = new TestReportTestComponent();
      if (this.test == null)
        this.test = new ArrayList<TestReportTestComponent>();
      this.test.add(t);
      return t;
    }

    public TestReport addTest(TestReportTestComponent t) { //3
      if (t == null)
        return this;
      if (this.test == null)
        this.test = new ArrayList<TestReportTestComponent>();
      this.test.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #test}, creating it if it does not already exist
     */
    public TestReportTestComponent getTestFirstRep() { 
      if (getTest().isEmpty()) {
        addTest();
      }
      return getTest().get(0);
    }

    /**
     * @return {@link #teardown} (The results of the series of operations required to clean up after the all the tests were executed (successfully or otherwise).)
     */
    public TestReportTeardownComponent getTeardown() { 
      if (this.teardown == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestReport.teardown");
        else if (Configuration.doAutoCreate())
          this.teardown = new TestReportTeardownComponent(); // cc
      return this.teardown;
    }

    public boolean hasTeardown() { 
      return this.teardown != null && !this.teardown.isEmpty();
    }

    /**
     * @param value {@link #teardown} (The results of the series of operations required to clean up after the all the tests were executed (successfully or otherwise).)
     */
    public TestReport setTeardown(TestReportTeardownComponent value) { 
      this.teardown = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for the TestScript assigned for external purposes outside the context of FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the executed TestScript.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The current state of this test report.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("testScript", "Reference(TestScript)", "Ideally this is an absolute URL that is used to identify the version-specific TestScript that was executed, matching the `TestScript.url`.", 0, java.lang.Integer.MAX_VALUE, testScript));
        childrenList.add(new Property("result", "code", "The overall result from the execution of the TestScript.", 0, java.lang.Integer.MAX_VALUE, result));
        childrenList.add(new Property("score", "decimal", "The final score (percentage of tests passed) resulting from the execution of the TestScript.", 0, java.lang.Integer.MAX_VALUE, score));
        childrenList.add(new Property("tester", "string", "Name of the tester producing this report (Organization or individual).", 0, java.lang.Integer.MAX_VALUE, tester));
        childrenList.add(new Property("issued", "dateTime", "When the TestScript was executed and this TestReport was generated.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("participant", "", "A participant in the test execution, either the execution engine, a client, or a server.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("setup", "", "The results of the series of required setup operations before the tests were executed.", 0, java.lang.Integer.MAX_VALUE, setup));
        childrenList.add(new Property("test", "", "A test executed from the test script.", 0, java.lang.Integer.MAX_VALUE, test));
        childrenList.add(new Property("teardown", "", "The results of the series of operations required to clean up after the all the tests were executed (successfully or otherwise).", 0, java.lang.Integer.MAX_VALUE, teardown));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<TestReportStatus>
        case 1712049149: /*testScript*/ return this.testScript == null ? new Base[0] : new Base[] {this.testScript}; // Reference
        case -934426595: /*result*/ return this.result == null ? new Base[0] : new Base[] {this.result}; // Enumeration<TestReportResult>
        case 109264530: /*score*/ return this.score == null ? new Base[0] : new Base[] {this.score}; // DecimalType
        case -877169473: /*tester*/ return this.tester == null ? new Base[0] : new Base[] {this.tester}; // StringType
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // DateTimeType
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // TestReportParticipantComponent
        case 109329021: /*setup*/ return this.setup == null ? new Base[0] : new Base[] {this.setup}; // TestReportSetupComponent
        case 3556498: /*test*/ return this.test == null ? new Base[0] : this.test.toArray(new Base[this.test.size()]); // TestReportTestComponent
        case -1663474172: /*teardown*/ return this.teardown == null ? new Base[0] : new Base[] {this.teardown}; // TestReportTeardownComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new TestReportStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<TestReportStatus>
          return value;
        case 1712049149: // testScript
          this.testScript = castToReference(value); // Reference
          return value;
        case -934426595: // result
          value = new TestReportResultEnumFactory().fromType(castToCode(value));
          this.result = (Enumeration) value; // Enumeration<TestReportResult>
          return value;
        case 109264530: // score
          this.score = castToDecimal(value); // DecimalType
          return value;
        case -877169473: // tester
          this.tester = castToString(value); // StringType
          return value;
        case -1179159893: // issued
          this.issued = castToDateTime(value); // DateTimeType
          return value;
        case 767422259: // participant
          this.getParticipant().add((TestReportParticipantComponent) value); // TestReportParticipantComponent
          return value;
        case 109329021: // setup
          this.setup = (TestReportSetupComponent) value; // TestReportSetupComponent
          return value;
        case 3556498: // test
          this.getTest().add((TestReportTestComponent) value); // TestReportTestComponent
          return value;
        case -1663474172: // teardown
          this.teardown = (TestReportTeardownComponent) value; // TestReportTeardownComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new TestReportStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<TestReportStatus>
        } else if (name.equals("testScript")) {
          this.testScript = castToReference(value); // Reference
        } else if (name.equals("result")) {
          value = new TestReportResultEnumFactory().fromType(castToCode(value));
          this.result = (Enumeration) value; // Enumeration<TestReportResult>
        } else if (name.equals("score")) {
          this.score = castToDecimal(value); // DecimalType
        } else if (name.equals("tester")) {
          this.tester = castToString(value); // StringType
        } else if (name.equals("issued")) {
          this.issued = castToDateTime(value); // DateTimeType
        } else if (name.equals("participant")) {
          this.getParticipant().add((TestReportParticipantComponent) value);
        } else if (name.equals("setup")) {
          this.setup = (TestReportSetupComponent) value; // TestReportSetupComponent
        } else if (name.equals("test")) {
          this.getTest().add((TestReportTestComponent) value);
        } else if (name.equals("teardown")) {
          this.teardown = (TestReportTeardownComponent) value; // TestReportTeardownComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 3373707:  return getNameElement();
        case -892481550:  return getStatusElement();
        case 1712049149:  return getTestScript(); 
        case -934426595:  return getResultElement();
        case 109264530:  return getScoreElement();
        case -877169473:  return getTesterElement();
        case -1179159893:  return getIssuedElement();
        case 767422259:  return addParticipant(); 
        case 109329021:  return getSetup(); 
        case 3556498:  return addTest(); 
        case -1663474172:  return getTeardown(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1712049149: /*testScript*/ return new String[] {"Reference"};
        case -934426595: /*result*/ return new String[] {"code"};
        case 109264530: /*score*/ return new String[] {"decimal"};
        case -877169473: /*tester*/ return new String[] {"string"};
        case -1179159893: /*issued*/ return new String[] {"dateTime"};
        case 767422259: /*participant*/ return new String[] {};
        case 109329021: /*setup*/ return new String[] {};
        case 3556498: /*test*/ return new String[] {};
        case -1663474172: /*teardown*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.status");
        }
        else if (name.equals("testScript")) {
          this.testScript = new Reference();
          return this.testScript;
        }
        else if (name.equals("result")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.result");
        }
        else if (name.equals("score")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.score");
        }
        else if (name.equals("tester")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.tester");
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestReport.issued");
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("setup")) {
          this.setup = new TestReportSetupComponent();
          return this.setup;
        }
        else if (name.equals("test")) {
          return addTest();
        }
        else if (name.equals("teardown")) {
          this.teardown = new TestReportTeardownComponent();
          return this.teardown;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "TestReport";

  }

      public TestReport copy() {
        TestReport dst = new TestReport();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.testScript = testScript == null ? null : testScript.copy();
        dst.result = result == null ? null : result.copy();
        dst.score = score == null ? null : score.copy();
        dst.tester = tester == null ? null : tester.copy();
        dst.issued = issued == null ? null : issued.copy();
        if (participant != null) {
          dst.participant = new ArrayList<TestReportParticipantComponent>();
          for (TestReportParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.setup = setup == null ? null : setup.copy();
        if (test != null) {
          dst.test = new ArrayList<TestReportTestComponent>();
          for (TestReportTestComponent i : test)
            dst.test.add(i.copy());
        };
        dst.teardown = teardown == null ? null : teardown.copy();
        return dst;
      }

      protected TestReport typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestReport))
          return false;
        TestReport o = (TestReport) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(status, o.status, true)
           && compareDeep(testScript, o.testScript, true) && compareDeep(result, o.result, true) && compareDeep(score, o.score, true)
           && compareDeep(tester, o.tester, true) && compareDeep(issued, o.issued, true) && compareDeep(participant, o.participant, true)
           && compareDeep(setup, o.setup, true) && compareDeep(test, o.test, true) && compareDeep(teardown, o.teardown, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestReport))
          return false;
        TestReport o = (TestReport) other;
        return compareValues(name, o.name, true) && compareValues(status, o.status, true) && compareValues(result, o.result, true)
           && compareValues(score, o.score, true) && compareValues(tester, o.tester, true) && compareValues(issued, o.issued, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, name, status
          , testScript, result, score, tester, issued, participant, setup, test, teardown
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.TestReport;
   }

 /**
   * Search parameter: <b>result</b>
   * <p>
   * Description: <b>The result disposition of the test execution</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestReport.result</b><br>
   * </p>
   */
  @SearchParamDefinition(name="result", path="TestReport.result", description="The result disposition of the test execution", type="token" )
  public static final String SP_RESULT = "result";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>result</b>
   * <p>
   * Description: <b>The result disposition of the test execution</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestReport.result</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RESULT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RESULT);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>An external identifier for the test report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestReport.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="TestReport.identifier", description="An external identifier for the test report", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>An external identifier for the test report</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestReport.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>tester</b>
   * <p>
   * Description: <b>The name of the testing organization</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestReport.tester</b><br>
   * </p>
   */
  @SearchParamDefinition(name="tester", path="TestReport.tester", description="The name of the testing organization", type="string" )
  public static final String SP_TESTER = "tester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>tester</b>
   * <p>
   * Description: <b>The name of the testing organization</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestReport.tester</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TESTER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TESTER);

 /**
   * Search parameter: <b>testscript</b>
   * <p>
   * Description: <b>The test script executed to produce this report</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>TestReport.testScript</b><br>
   * </p>
   */
  @SearchParamDefinition(name="testscript", path="TestReport.testScript", description="The test script executed to produce this report", type="reference", target={TestScript.class } )
  public static final String SP_TESTSCRIPT = "testscript";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>testscript</b>
   * <p>
   * Description: <b>The test script executed to produce this report</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>TestReport.testScript</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam TESTSCRIPT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_TESTSCRIPT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>TestReport:testscript</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_TESTSCRIPT = new ca.uhn.fhir.model.api.Include("TestReport:testscript").toLocked();

 /**
   * Search parameter: <b>issued</b>
   * <p>
   * Description: <b>The test report generation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>TestReport.issued</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issued", path="TestReport.issued", description="The test report generation date", type="date" )
  public static final String SP_ISSUED = "issued";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issued</b>
   * <p>
   * Description: <b>The test report generation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>TestReport.issued</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ISSUED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ISSUED);

 /**
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>The reference to a participant in the test execution</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>TestReport.participant.uri</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="TestReport.participant.uri", description="The reference to a participant in the test execution", type="uri" )
  public static final String SP_PARTICIPANT = "participant";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant</b>
   * <p>
   * Description: <b>The reference to a participant in the test execution</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>TestReport.participant.uri</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam PARTICIPANT = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_PARTICIPANT);


}

