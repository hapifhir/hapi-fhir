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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.Enumerations.*;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * TestScript is a resource that specifies a suite of tests against a FHIR server implementation to determine compliance against the FHIR specification.
 */
@ResourceDef(name="TestScript", profile="http://hl7.org/fhir/Profile/TestScript")
public class TestScript extends DomainResource {

    public enum TestOperationType {
        /**
         * Read the current state of the resource.
         */
        READ, 
        /**
         * Read the state of a specific version of the resource.
         */
        VREAD, 
        /**
         * Update an existing resource by its id (or create it if it is new).
         */
        UPDATE, 
        /**
         * Delete a resource.
         */
        DELETE, 
        /**
         * Retrieve the update history for a particular resource or resource type.
         */
        HISTORY, 
        /**
         * Create a new resource with a server assigned id.
         */
        CREATE, 
        /**
         * Search based on some filter criteria.
         */
        SEARCH, 
        /**
         * Update, create or delete a set of resources as a single transaction.
         */
        TRANSACTION, 
        /**
         * Get a conformance statement for the system.
         */
        CONFORMANCE, 
        /**
         * Tag operations.
         */
        TAGS, 
        /**
         * Not currently supported test operation.
         */
        MAILBOX, 
        /**
         * Not currently supported test operation.
         */
        DOCUMENT, 
        /**
         * Make an assertion against the result of the last non-assertion operation.
         */
        ASSERTION, 
        /**
         * Make a negative or false assertion against the result of the last non-assertion operation.
         */
        ASSERTIONFALSE, 
        /**
         * Run an assertion function as a warning (instead of a failture) against the result of the last non-assertion operation.
         */
        ASSERTIONWARNING, 
        /**
         * added to help the parsers
         */
        NULL;
        public static TestOperationType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("read".equals(codeString))
          return READ;
        if ("vread".equals(codeString))
          return VREAD;
        if ("update".equals(codeString))
          return UPDATE;
        if ("delete".equals(codeString))
          return DELETE;
        if ("history".equals(codeString))
          return HISTORY;
        if ("create".equals(codeString))
          return CREATE;
        if ("search".equals(codeString))
          return SEARCH;
        if ("transaction".equals(codeString))
          return TRANSACTION;
        if ("conformance".equals(codeString))
          return CONFORMANCE;
        if ("tags".equals(codeString))
          return TAGS;
        if ("mailbox".equals(codeString))
          return MAILBOX;
        if ("document".equals(codeString))
          return DOCUMENT;
        if ("assertion".equals(codeString))
          return ASSERTION;
        if ("assertion_false".equals(codeString))
          return ASSERTIONFALSE;
        if ("assertion_warning".equals(codeString))
          return ASSERTIONWARNING;
        throw new Exception("Unknown TestOperationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case READ: return "read";
            case VREAD: return "vread";
            case UPDATE: return "update";
            case DELETE: return "delete";
            case HISTORY: return "history";
            case CREATE: return "create";
            case SEARCH: return "search";
            case TRANSACTION: return "transaction";
            case CONFORMANCE: return "conformance";
            case TAGS: return "tags";
            case MAILBOX: return "mailbox";
            case DOCUMENT: return "document";
            case ASSERTION: return "assertion";
            case ASSERTIONFALSE: return "assertion_false";
            case ASSERTIONWARNING: return "assertion_warning";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case READ: return "http://hl7.org/fhir/test-operation-codes";
            case VREAD: return "http://hl7.org/fhir/test-operation-codes";
            case UPDATE: return "http://hl7.org/fhir/test-operation-codes";
            case DELETE: return "http://hl7.org/fhir/test-operation-codes";
            case HISTORY: return "http://hl7.org/fhir/test-operation-codes";
            case CREATE: return "http://hl7.org/fhir/test-operation-codes";
            case SEARCH: return "http://hl7.org/fhir/test-operation-codes";
            case TRANSACTION: return "http://hl7.org/fhir/test-operation-codes";
            case CONFORMANCE: return "http://hl7.org/fhir/test-operation-codes";
            case TAGS: return "http://hl7.org/fhir/test-operation-codes";
            case MAILBOX: return "http://hl7.org/fhir/test-operation-codes";
            case DOCUMENT: return "http://hl7.org/fhir/test-operation-codes";
            case ASSERTION: return "http://hl7.org/fhir/test-operation-codes";
            case ASSERTIONFALSE: return "http://hl7.org/fhir/test-operation-codes";
            case ASSERTIONWARNING: return "http://hl7.org/fhir/test-operation-codes";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case READ: return "Read the current state of the resource.";
            case VREAD: return "Read the state of a specific version of the resource.";
            case UPDATE: return "Update an existing resource by its id (or create it if it is new).";
            case DELETE: return "Delete a resource.";
            case HISTORY: return "Retrieve the update history for a particular resource or resource type.";
            case CREATE: return "Create a new resource with a server assigned id.";
            case SEARCH: return "Search based on some filter criteria.";
            case TRANSACTION: return "Update, create or delete a set of resources as a single transaction.";
            case CONFORMANCE: return "Get a conformance statement for the system.";
            case TAGS: return "Tag operations.";
            case MAILBOX: return "Not currently supported test operation.";
            case DOCUMENT: return "Not currently supported test operation.";
            case ASSERTION: return "Make an assertion against the result of the last non-assertion operation.";
            case ASSERTIONFALSE: return "Make a negative or false assertion against the result of the last non-assertion operation.";
            case ASSERTIONWARNING: return "Run an assertion function as a warning (instead of a failture) against the result of the last non-assertion operation.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case READ: return "read";
            case VREAD: return "vread";
            case UPDATE: return "update";
            case DELETE: return "delete";
            case HISTORY: return "history";
            case CREATE: return "create";
            case SEARCH: return "search";
            case TRANSACTION: return "transaction";
            case CONFORMANCE: return "conformance";
            case TAGS: return "tags";
            case MAILBOX: return "mailbox";
            case DOCUMENT: return "document";
            case ASSERTION: return "assertion";
            case ASSERTIONFALSE: return "assertion_false";
            case ASSERTIONWARNING: return "assertion_warning";
            default: return "?";
          }
        }
    }

  public static class TestOperationTypeEnumFactory implements EnumFactory<TestOperationType> {
    public TestOperationType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("read".equals(codeString))
          return TestOperationType.READ;
        if ("vread".equals(codeString))
          return TestOperationType.VREAD;
        if ("update".equals(codeString))
          return TestOperationType.UPDATE;
        if ("delete".equals(codeString))
          return TestOperationType.DELETE;
        if ("history".equals(codeString))
          return TestOperationType.HISTORY;
        if ("create".equals(codeString))
          return TestOperationType.CREATE;
        if ("search".equals(codeString))
          return TestOperationType.SEARCH;
        if ("transaction".equals(codeString))
          return TestOperationType.TRANSACTION;
        if ("conformance".equals(codeString))
          return TestOperationType.CONFORMANCE;
        if ("tags".equals(codeString))
          return TestOperationType.TAGS;
        if ("mailbox".equals(codeString))
          return TestOperationType.MAILBOX;
        if ("document".equals(codeString))
          return TestOperationType.DOCUMENT;
        if ("assertion".equals(codeString))
          return TestOperationType.ASSERTION;
        if ("assertion_false".equals(codeString))
          return TestOperationType.ASSERTIONFALSE;
        if ("assertion_warning".equals(codeString))
          return TestOperationType.ASSERTIONWARNING;
        throw new IllegalArgumentException("Unknown TestOperationType code '"+codeString+"'");
        }
    public String toCode(TestOperationType code) {
      if (code == TestOperationType.READ)
        return "read";
      if (code == TestOperationType.VREAD)
        return "vread";
      if (code == TestOperationType.UPDATE)
        return "update";
      if (code == TestOperationType.DELETE)
        return "delete";
      if (code == TestOperationType.HISTORY)
        return "history";
      if (code == TestOperationType.CREATE)
        return "create";
      if (code == TestOperationType.SEARCH)
        return "search";
      if (code == TestOperationType.TRANSACTION)
        return "transaction";
      if (code == TestOperationType.CONFORMANCE)
        return "conformance";
      if (code == TestOperationType.TAGS)
        return "tags";
      if (code == TestOperationType.MAILBOX)
        return "mailbox";
      if (code == TestOperationType.DOCUMENT)
        return "document";
      if (code == TestOperationType.ASSERTION)
        return "assertion";
      if (code == TestOperationType.ASSERTIONFALSE)
        return "assertion_false";
      if (code == TestOperationType.ASSERTIONWARNING)
        return "assertion_warning";
      return "?";
      }
    }

    public enum ContentType {
        /**
         * XML content-type corresponding to the application/xml+fhir mime-type
         */
        XML, 
        /**
         * JSON content-type corresponding to the application/json+fhir mime-type
         */
        JSON, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContentType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xml".equals(codeString))
          return XML;
        if ("json".equals(codeString))
          return JSON;
        throw new Exception("Unknown ContentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case XML: return "xml";
            case JSON: return "json";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case XML: return "http://hl7.org/fhir/content-type";
            case JSON: return "http://hl7.org/fhir/content-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case XML: return "XML content-type corresponding to the application/xml+fhir mime-type";
            case JSON: return "JSON content-type corresponding to the application/json+fhir mime-type";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case XML: return "xml";
            case JSON: return "json";
            default: return "?";
          }
        }
    }

  public static class ContentTypeEnumFactory implements EnumFactory<ContentType> {
    public ContentType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xml".equals(codeString))
          return ContentType.XML;
        if ("json".equals(codeString))
          return ContentType.JSON;
        throw new IllegalArgumentException("Unknown ContentType code '"+codeString+"'");
        }
    public String toCode(ContentType code) {
      if (code == ContentType.XML)
        return "xml";
      if (code == ContentType.JSON)
        return "json";
      return "?";
      }
    }

    @Block()
    public static class TestScriptFixtureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The URI of the fixture. Each fixture shall have a "uri" or "resource" but not both. The "uri" shall resolve to a valid Resource. The "uri" may be local or remote, absolute or relative.
         */
        @Child(name = "uri", type = {UriType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="URI of the fixture", formalDefinition="The URI of the fixture. Each fixture shall have a 'uri' or 'resource' but not both. The 'uri' shall resolve to a valid Resource. The 'uri' may be local or remote, absolute or relative." )
        protected UriType uri;

        /**
         * The fixture resource actually embedded in this TestScript. Each fixture should have a "uri" or "resource" but not both.
         */
        @Child(name = "resource", type = {Resource.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Fixture resource", formalDefinition="The fixture resource actually embedded in this TestScript. Each fixture should have a 'uri' or 'resource' but not both." )
        protected Resource resource;

        /**
         * Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.
         */
        @Child(name = "autocreate", type = {BooleanType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Whether or not to implicitly create the fixture during setup", formalDefinition="Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section." )
        protected BooleanType autocreate;

        /**
         * Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.
         */
        @Child(name = "autodelete", type = {BooleanType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Whether or not to implicitly delete the fixture during teardown", formalDefinition="Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section." )
        protected BooleanType autodelete;

        private static final long serialVersionUID = 1455581555L;

    /*
     * Constructor
     */
      public TestScriptFixtureComponent() {
        super();
      }

        /**
         * @return {@link #uri} (The URI of the fixture. Each fixture shall have a "uri" or "resource" but not both. The "uri" shall resolve to a valid Resource. The "uri" may be local or remote, absolute or relative.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptFixtureComponent.uri");
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
         * @param value {@link #uri} (The URI of the fixture. Each fixture shall have a "uri" or "resource" but not both. The "uri" shall resolve to a valid Resource. The "uri" may be local or remote, absolute or relative.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public TestScriptFixtureComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return The URI of the fixture. Each fixture shall have a "uri" or "resource" but not both. The "uri" shall resolve to a valid Resource. The "uri" may be local or remote, absolute or relative.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value The URI of the fixture. Each fixture shall have a "uri" or "resource" but not both. The "uri" shall resolve to a valid Resource. The "uri" may be local or remote, absolute or relative.
         */
        public TestScriptFixtureComponent setUri(String value) { 
          if (Utilities.noString(value))
            this.uri = null;
          else {
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #resource} (The fixture resource actually embedded in this TestScript. Each fixture should have a "uri" or "resource" but not both.)
         */
        public Resource getResource() { 
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The fixture resource actually embedded in this TestScript. Each fixture should have a "uri" or "resource" but not both.)
         */
        public TestScriptFixtureComponent setResource(Resource value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #autocreate} (Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.). This is the underlying object with id, value and extensions. The accessor "getAutocreate" gives direct access to the value
         */
        public BooleanType getAutocreateElement() { 
          if (this.autocreate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptFixtureComponent.autocreate");
            else if (Configuration.doAutoCreate())
              this.autocreate = new BooleanType(); // bb
          return this.autocreate;
        }

        public boolean hasAutocreateElement() { 
          return this.autocreate != null && !this.autocreate.isEmpty();
        }

        public boolean hasAutocreate() { 
          return this.autocreate != null && !this.autocreate.isEmpty();
        }

        /**
         * @param value {@link #autocreate} (Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.). This is the underlying object with id, value and extensions. The accessor "getAutocreate" gives direct access to the value
         */
        public TestScriptFixtureComponent setAutocreateElement(BooleanType value) { 
          this.autocreate = value;
          return this;
        }

        /**
         * @return Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.
         */
        public boolean getAutocreate() { 
          return this.autocreate == null || this.autocreate.isEmpty() ? false : this.autocreate.getValue();
        }

        /**
         * @param value Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.
         */
        public TestScriptFixtureComponent setAutocreate(boolean value) { 
            if (this.autocreate == null)
              this.autocreate = new BooleanType();
            this.autocreate.setValue(value);
          return this;
        }

        /**
         * @return {@link #autodelete} (Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.). This is the underlying object with id, value and extensions. The accessor "getAutodelete" gives direct access to the value
         */
        public BooleanType getAutodeleteElement() { 
          if (this.autodelete == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptFixtureComponent.autodelete");
            else if (Configuration.doAutoCreate())
              this.autodelete = new BooleanType(); // bb
          return this.autodelete;
        }

        public boolean hasAutodeleteElement() { 
          return this.autodelete != null && !this.autodelete.isEmpty();
        }

        public boolean hasAutodelete() { 
          return this.autodelete != null && !this.autodelete.isEmpty();
        }

        /**
         * @param value {@link #autodelete} (Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.). This is the underlying object with id, value and extensions. The accessor "getAutodelete" gives direct access to the value
         */
        public TestScriptFixtureComponent setAutodeleteElement(BooleanType value) { 
          this.autodelete = value;
          return this;
        }

        /**
         * @return Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.
         */
        public boolean getAutodelete() { 
          return this.autodelete == null || this.autodelete.isEmpty() ? false : this.autodelete.getValue();
        }

        /**
         * @param value Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.
         */
        public TestScriptFixtureComponent setAutodelete(boolean value) { 
            if (this.autodelete == null)
              this.autodelete = new BooleanType();
            this.autodelete.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("uri", "uri", "The URI of the fixture. Each fixture shall have a 'uri' or 'resource' but not both. The 'uri' shall resolve to a valid Resource. The 'uri' may be local or remote, absolute or relative.", 0, java.lang.Integer.MAX_VALUE, uri));
          childrenList.add(new Property("resource", "Resource", "The fixture resource actually embedded in this TestScript. Each fixture should have a 'uri' or 'resource' but not both.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("autocreate", "boolean", "Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.", 0, java.lang.Integer.MAX_VALUE, autocreate));
          childrenList.add(new Property("autodelete", "boolean", "Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.", 0, java.lang.Integer.MAX_VALUE, autodelete));
        }

      public TestScriptFixtureComponent copy() {
        TestScriptFixtureComponent dst = new TestScriptFixtureComponent();
        copyValues(dst);
        dst.uri = uri == null ? null : uri.copy();
        dst.resource = resource == null ? null : resource.copy();
        dst.autocreate = autocreate == null ? null : autocreate.copy();
        dst.autodelete = autodelete == null ? null : autodelete.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptFixtureComponent))
          return false;
        TestScriptFixtureComponent o = (TestScriptFixtureComponent) other;
        return compareDeep(uri, o.uri, true) && compareDeep(resource, o.resource, true) && compareDeep(autocreate, o.autocreate, true)
           && compareDeep(autodelete, o.autodelete, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptFixtureComponent))
          return false;
        TestScriptFixtureComponent o = (TestScriptFixtureComponent) other;
        return compareValues(uri, o.uri, true) && compareValues(autocreate, o.autocreate, true) && compareValues(autodelete, o.autodelete, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (uri == null || uri.isEmpty()) && (resource == null || resource.isEmpty())
           && (autocreate == null || autocreate.isEmpty()) && (autodelete == null || autodelete.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptSetupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A setup operation.
         */
        @Child(name = "operation", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="A setup operation", formalDefinition="A setup operation." )
        protected List<TestScriptSetupOperationComponent> operation;

        private static final long serialVersionUID = -61721475L;

    /*
     * Constructor
     */
      public TestScriptSetupComponent() {
        super();
      }

        /**
         * @return {@link #operation} (A setup operation.)
         */
        public List<TestScriptSetupOperationComponent> getOperation() { 
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptSetupOperationComponent>();
          return this.operation;
        }

        public boolean hasOperation() { 
          if (this.operation == null)
            return false;
          for (TestScriptSetupOperationComponent item : this.operation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #operation} (A setup operation.)
         */
    // syntactic sugar
        public TestScriptSetupOperationComponent addOperation() { //3
          TestScriptSetupOperationComponent t = new TestScriptSetupOperationComponent();
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptSetupOperationComponent>();
          this.operation.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptSetupComponent addOperation(TestScriptSetupOperationComponent t) { //3
          if (t == null)
            return this;
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptSetupOperationComponent>();
          this.operation.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "", "A setup operation.", 0, java.lang.Integer.MAX_VALUE, operation));
        }

      public TestScriptSetupComponent copy() {
        TestScriptSetupComponent dst = new TestScriptSetupComponent();
        copyValues(dst);
        if (operation != null) {
          dst.operation = new ArrayList<TestScriptSetupOperationComponent>();
          for (TestScriptSetupOperationComponent i : operation)
            dst.operation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptSetupComponent))
          return false;
        TestScriptSetupComponent o = (TestScriptSetupComponent) other;
        return compareDeep(operation, o.operation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptSetupComponent))
          return false;
        TestScriptSetupComponent o = (TestScriptSetupComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (operation == null || operation.isEmpty());
      }

  }

    @Block()
    public static class TestScriptSetupOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning", formalDefinition="read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning." )
        protected Enumeration<TestOperationType> type;

        /**
         * The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        @Child(name = "source", type = {IdType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The id of the fixture used as the body in a PUT or POST", formalDefinition="The internal id of the fixture used as the body of any operation.type that results in a PUT or POST." )
        protected IdType source;

        /**
         * The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        @Child(name = "target", type = {IdType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="The id of the fixture used as the target of a PUT or POST, or the id of the fixture used to store the results of a GET", formalDefinition="The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET." )
        protected IdType target;

        /**
         * Which server to perform the operation on.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Which server to perform the operation on", formalDefinition="Which server to perform the operation on." )
        protected IntegerType destination;

        /**
         * Arguments to an operation.
         */
        @Child(name = "parameter", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Arguments to an operation", formalDefinition="Arguments to an operation." )
        protected List<StringType> parameter;

        /**
         * The fixture id (maybe new) to map to the response.
         */
        @Child(name = "responseId", type = {IdType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Response id", formalDefinition="The fixture id (maybe new) to map to the response." )
        protected IdType responseId;

        /**
         * The content-type or mime-type to use for RESTful operation.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation." )
        protected Enumeration<ContentType> contentType;

        private static final long serialVersionUID = 1788056082L;

    /*
     * Constructor
     */
      public TestScriptSetupOperationComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptSetupOperationComponent(Enumeration<TestOperationType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<TestOperationType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupOperationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<TestOperationType>(new TestOperationTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public TestScriptSetupOperationComponent setTypeElement(Enumeration<TestOperationType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        public TestOperationType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        public TestScriptSetupOperationComponent setType(TestOperationType value) { 
            if (this.type == null)
              this.type = new Enumeration<TestOperationType>(new TestOperationTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public IdType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupOperationComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new IdType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public TestScriptSetupOperationComponent setSourceElement(IdType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        public TestScriptSetupOperationComponent setSource(String value) { 
          if (Utilities.noString(value))
            this.source = null;
          else {
            if (this.source == null)
              this.source = new IdType();
            this.source.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #target} (The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public IdType getTargetElement() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupOperationComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new IdType(); // bb
          return this.target;
        }

        public boolean hasTargetElement() { 
          return this.target != null && !this.target.isEmpty();
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public TestScriptSetupOperationComponent setTargetElement(IdType value) { 
          this.target = value;
          return this;
        }

        /**
         * @return The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        public String getTarget() { 
          return this.target == null ? null : this.target.getValue();
        }

        /**
         * @param value The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        public TestScriptSetupOperationComponent setTarget(String value) { 
          if (Utilities.noString(value))
            this.target = null;
          else {
            if (this.target == null)
              this.target = new IdType();
            this.target.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #destination} (Which server to perform the operation on.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupOperationComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new IntegerType(); // bb
          return this.destination;
        }

        public boolean hasDestinationElement() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Which server to perform the operation on.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public TestScriptSetupOperationComponent setDestinationElement(IntegerType value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return Which server to perform the operation on.
         */
        public int getDestination() { 
          return this.destination == null || this.destination.isEmpty() ? 0 : this.destination.getValue();
        }

        /**
         * @param value Which server to perform the operation on.
         */
        public TestScriptSetupOperationComponent setDestination(int value) { 
            if (this.destination == null)
              this.destination = new IntegerType();
            this.destination.setValue(value);
          return this;
        }

        /**
         * @return {@link #parameter} (Arguments to an operation.)
         */
        public List<StringType> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          return this.parameter;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (StringType item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #parameter} (Arguments to an operation.)
         */
    // syntactic sugar
        public StringType addParameterElement() {//2 
          StringType t = new StringType();
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          this.parameter.add(t);
          return t;
        }

        /**
         * @param value {@link #parameter} (Arguments to an operation.)
         */
        public TestScriptSetupOperationComponent addParameter(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          this.parameter.add(t);
          return this;
        }

        /**
         * @param value {@link #parameter} (Arguments to an operation.)
         */
        public boolean hasParameter(String value) { 
          if (this.parameter == null)
            return false;
          for (StringType v : this.parameter)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public IdType getResponseIdElement() { 
          if (this.responseId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupOperationComponent.responseId");
            else if (Configuration.doAutoCreate())
              this.responseId = new IdType(); // bb
          return this.responseId;
        }

        public boolean hasResponseIdElement() { 
          return this.responseId != null && !this.responseId.isEmpty();
        }

        public boolean hasResponseId() { 
          return this.responseId != null && !this.responseId.isEmpty();
        }

        /**
         * @param value {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public TestScriptSetupOperationComponent setResponseIdElement(IdType value) { 
          this.responseId = value;
          return this;
        }

        /**
         * @return The fixture id (maybe new) to map to the response.
         */
        public String getResponseId() { 
          return this.responseId == null ? null : this.responseId.getValue();
        }

        /**
         * @param value The fixture id (maybe new) to map to the response.
         */
        public TestScriptSetupOperationComponent setResponseId(String value) { 
          if (Utilities.noString(value))
            this.responseId = null;
          else {
            if (this.responseId == null)
              this.responseId = new IdType();
            this.responseId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #contentType} (The content-type or mime-type to use for RESTful operation.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public Enumeration<ContentType> getContentTypeElement() { 
          if (this.contentType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupOperationComponent.contentType");
            else if (Configuration.doAutoCreate())
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory()); // bb
          return this.contentType;
        }

        public boolean hasContentTypeElement() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        public boolean hasContentType() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        /**
         * @param value {@link #contentType} (The content-type or mime-type to use for RESTful operation.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public TestScriptSetupOperationComponent setContentTypeElement(Enumeration<ContentType> value) { 
          this.contentType = value;
          return this;
        }

        /**
         * @return The content-type or mime-type to use for RESTful operation.
         */
        public ContentType getContentType() { 
          return this.contentType == null ? null : this.contentType.getValue();
        }

        /**
         * @param value The content-type or mime-type to use for RESTful operation.
         */
        public TestScriptSetupOperationComponent setContentType(ContentType value) { 
          if (value == null)
            this.contentType = null;
          else {
            if (this.contentType == null)
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory());
            this.contentType.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("source", "id", "The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.", 0, java.lang.Integer.MAX_VALUE, source));
          childrenList.add(new Property("target", "id", "The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.", 0, java.lang.Integer.MAX_VALUE, target));
          childrenList.add(new Property("destination", "integer", "Which server to perform the operation on.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("parameter", "string", "Arguments to an operation.", 0, java.lang.Integer.MAX_VALUE, parameter));
          childrenList.add(new Property("responseId", "id", "The fixture id (maybe new) to map to the response.", 0, java.lang.Integer.MAX_VALUE, responseId));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation.", 0, java.lang.Integer.MAX_VALUE, contentType));
        }

      public TestScriptSetupOperationComponent copy() {
        TestScriptSetupOperationComponent dst = new TestScriptSetupOperationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.source = source == null ? null : source.copy();
        dst.target = target == null ? null : target.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<StringType>();
          for (StringType i : parameter)
            dst.parameter.add(i.copy());
        };
        dst.responseId = responseId == null ? null : responseId.copy();
        dst.contentType = contentType == null ? null : contentType.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptSetupOperationComponent))
          return false;
        TestScriptSetupOperationComponent o = (TestScriptSetupOperationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(source, o.source, true) && compareDeep(target, o.target, true)
           && compareDeep(destination, o.destination, true) && compareDeep(parameter, o.parameter, true) && compareDeep(responseId, o.responseId, true)
           && compareDeep(contentType, o.contentType, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptSetupOperationComponent))
          return false;
        TestScriptSetupOperationComponent o = (TestScriptSetupOperationComponent) other;
        return compareValues(type, o.type, true) && compareValues(source, o.source, true) && compareValues(target, o.target, true)
           && compareValues(destination, o.destination, true) && compareValues(parameter, o.parameter, true) && compareValues(responseId, o.responseId, true)
           && compareValues(contentType, o.contentType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (source == null || source.isEmpty())
           && (target == null || target.isEmpty()) && (destination == null || destination.isEmpty())
           && (parameter == null || parameter.isEmpty()) && (responseId == null || responseId.isEmpty())
           && (contentType == null || contentType.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTestComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of this test.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="The name of this test", formalDefinition="The name of this test." )
        protected StringType name;

        /**
         * A short description of the test.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Short description of the test", formalDefinition="A short description of the test." )
        protected StringType description;

        /**
         * Metadata about this Test.
         */
        @Child(name = "metadata", type = {}, order=3, min=0, max=1)
        @Description(shortDefinition="Metadata about this Test", formalDefinition="Metadata about this Test." )
        protected TestScriptTestMetadataComponent metadata;

        /**
         * Each test must have at least one operation. Operation and assertion elements can be mixed together.
         */
        @Child(name = "operation", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Each test must have at least one operation", formalDefinition="Each test must have at least one operation. Operation and assertion elements can be mixed together." )
        protected List<TestScriptTestOperationComponent> operation;

        private static final long serialVersionUID = -84782237L;

    /*
     * Constructor
     */
      public TestScriptTestComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of this test.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestComponent.name");
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
         * @param value {@link #name} (The name of this test.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public TestScriptTestComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of this test.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of this test.
         */
        public TestScriptTestComponent setName(String value) { 
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
         * @return {@link #description} (A short description of the test.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestComponent.description");
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
         * @param value {@link #description} (A short description of the test.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestScriptTestComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A short description of the test.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A short description of the test.
         */
        public TestScriptTestComponent setDescription(String value) { 
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
         * @return {@link #metadata} (Metadata about this Test.)
         */
        public TestScriptTestMetadataComponent getMetadata() { 
          if (this.metadata == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestComponent.metadata");
            else if (Configuration.doAutoCreate())
              this.metadata = new TestScriptTestMetadataComponent(); // cc
          return this.metadata;
        }

        public boolean hasMetadata() { 
          return this.metadata != null && !this.metadata.isEmpty();
        }

        /**
         * @param value {@link #metadata} (Metadata about this Test.)
         */
        public TestScriptTestComponent setMetadata(TestScriptTestMetadataComponent value) { 
          this.metadata = value;
          return this;
        }

        /**
         * @return {@link #operation} (Each test must have at least one operation. Operation and assertion elements can be mixed together.)
         */
        public List<TestScriptTestOperationComponent> getOperation() { 
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptTestOperationComponent>();
          return this.operation;
        }

        public boolean hasOperation() { 
          if (this.operation == null)
            return false;
          for (TestScriptTestOperationComponent item : this.operation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #operation} (Each test must have at least one operation. Operation and assertion elements can be mixed together.)
         */
    // syntactic sugar
        public TestScriptTestOperationComponent addOperation() { //3
          TestScriptTestOperationComponent t = new TestScriptTestOperationComponent();
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptTestOperationComponent>();
          this.operation.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTestComponent addOperation(TestScriptTestOperationComponent t) { //3
          if (t == null)
            return this;
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptTestOperationComponent>();
          this.operation.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of this test.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "A short description of the test.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("metadata", "", "Metadata about this Test.", 0, java.lang.Integer.MAX_VALUE, metadata));
          childrenList.add(new Property("operation", "", "Each test must have at least one operation. Operation and assertion elements can be mixed together.", 0, java.lang.Integer.MAX_VALUE, operation));
        }

      public TestScriptTestComponent copy() {
        TestScriptTestComponent dst = new TestScriptTestComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.metadata = metadata == null ? null : metadata.copy();
        if (operation != null) {
          dst.operation = new ArrayList<TestScriptTestOperationComponent>();
          for (TestScriptTestOperationComponent i : operation)
            dst.operation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTestComponent))
          return false;
        TestScriptTestComponent o = (TestScriptTestComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(metadata, o.metadata, true)
           && compareDeep(operation, o.operation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTestComponent))
          return false;
        TestScriptTestComponent o = (TestScriptTestComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (description == null || description.isEmpty())
           && (metadata == null || metadata.isEmpty()) && (operation == null || operation.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptTestMetadataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A link to the FHIR specification that this test is covering.
         */
        @Child(name = "link", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Link this test to the specification", formalDefinition="A link to the FHIR specification that this test is covering." )
        protected List<TestScriptTestMetadataLinkComponent> link;

        /**
         * The required capability must exist and is assumed to function correctly on the FHIR server being tested.
         */
        @Child(name = "requires", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Required capability that is assumed to function correctly on the FHIR server being tested", formalDefinition="The required capability must exist and is assumed to function correctly on the FHIR server being tested." )
        protected List<TestScriptTestMetadataRequiresComponent> requires;

        /**
         * The capability that this test verifies.
         */
        @Child(name = "validates", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Capability being verified", formalDefinition="The capability that this test verifies." )
        protected List<TestScriptTestMetadataValidatesComponent> validates;

        private static final long serialVersionUID = 749998279L;

    /*
     * Constructor
     */
      public TestScriptTestMetadataComponent() {
        super();
      }

        /**
         * @return {@link #link} (A link to the FHIR specification that this test is covering.)
         */
        public List<TestScriptTestMetadataLinkComponent> getLink() { 
          if (this.link == null)
            this.link = new ArrayList<TestScriptTestMetadataLinkComponent>();
          return this.link;
        }

        public boolean hasLink() { 
          if (this.link == null)
            return false;
          for (TestScriptTestMetadataLinkComponent item : this.link)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #link} (A link to the FHIR specification that this test is covering.)
         */
    // syntactic sugar
        public TestScriptTestMetadataLinkComponent addLink() { //3
          TestScriptTestMetadataLinkComponent t = new TestScriptTestMetadataLinkComponent();
          if (this.link == null)
            this.link = new ArrayList<TestScriptTestMetadataLinkComponent>();
          this.link.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTestMetadataComponent addLink(TestScriptTestMetadataLinkComponent t) { //3
          if (t == null)
            return this;
          if (this.link == null)
            this.link = new ArrayList<TestScriptTestMetadataLinkComponent>();
          this.link.add(t);
          return this;
        }

        /**
         * @return {@link #requires} (The required capability must exist and is assumed to function correctly on the FHIR server being tested.)
         */
        public List<TestScriptTestMetadataRequiresComponent> getRequires() { 
          if (this.requires == null)
            this.requires = new ArrayList<TestScriptTestMetadataRequiresComponent>();
          return this.requires;
        }

        public boolean hasRequires() { 
          if (this.requires == null)
            return false;
          for (TestScriptTestMetadataRequiresComponent item : this.requires)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #requires} (The required capability must exist and is assumed to function correctly on the FHIR server being tested.)
         */
    // syntactic sugar
        public TestScriptTestMetadataRequiresComponent addRequires() { //3
          TestScriptTestMetadataRequiresComponent t = new TestScriptTestMetadataRequiresComponent();
          if (this.requires == null)
            this.requires = new ArrayList<TestScriptTestMetadataRequiresComponent>();
          this.requires.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTestMetadataComponent addRequires(TestScriptTestMetadataRequiresComponent t) { //3
          if (t == null)
            return this;
          if (this.requires == null)
            this.requires = new ArrayList<TestScriptTestMetadataRequiresComponent>();
          this.requires.add(t);
          return this;
        }

        /**
         * @return {@link #validates} (The capability that this test verifies.)
         */
        public List<TestScriptTestMetadataValidatesComponent> getValidates() { 
          if (this.validates == null)
            this.validates = new ArrayList<TestScriptTestMetadataValidatesComponent>();
          return this.validates;
        }

        public boolean hasValidates() { 
          if (this.validates == null)
            return false;
          for (TestScriptTestMetadataValidatesComponent item : this.validates)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #validates} (The capability that this test verifies.)
         */
    // syntactic sugar
        public TestScriptTestMetadataValidatesComponent addValidates() { //3
          TestScriptTestMetadataValidatesComponent t = new TestScriptTestMetadataValidatesComponent();
          if (this.validates == null)
            this.validates = new ArrayList<TestScriptTestMetadataValidatesComponent>();
          this.validates.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTestMetadataComponent addValidates(TestScriptTestMetadataValidatesComponent t) { //3
          if (t == null)
            return this;
          if (this.validates == null)
            this.validates = new ArrayList<TestScriptTestMetadataValidatesComponent>();
          this.validates.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("link", "", "A link to the FHIR specification that this test is covering.", 0, java.lang.Integer.MAX_VALUE, link));
          childrenList.add(new Property("requires", "", "The required capability must exist and is assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, requires));
          childrenList.add(new Property("validates", "", "The capability that this test verifies.", 0, java.lang.Integer.MAX_VALUE, validates));
        }

      public TestScriptTestMetadataComponent copy() {
        TestScriptTestMetadataComponent dst = new TestScriptTestMetadataComponent();
        copyValues(dst);
        if (link != null) {
          dst.link = new ArrayList<TestScriptTestMetadataLinkComponent>();
          for (TestScriptTestMetadataLinkComponent i : link)
            dst.link.add(i.copy());
        };
        if (requires != null) {
          dst.requires = new ArrayList<TestScriptTestMetadataRequiresComponent>();
          for (TestScriptTestMetadataRequiresComponent i : requires)
            dst.requires.add(i.copy());
        };
        if (validates != null) {
          dst.validates = new ArrayList<TestScriptTestMetadataValidatesComponent>();
          for (TestScriptTestMetadataValidatesComponent i : validates)
            dst.validates.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataComponent))
          return false;
        TestScriptTestMetadataComponent o = (TestScriptTestMetadataComponent) other;
        return compareDeep(link, o.link, true) && compareDeep(requires, o.requires, true) && compareDeep(validates, o.validates, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataComponent))
          return false;
        TestScriptTestMetadataComponent o = (TestScriptTestMetadataComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (link == null || link.isEmpty()) && (requires == null || requires.isEmpty())
           && (validates == null || validates.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTestMetadataLinkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * URL to a particular requirement or feature within the FHIR specification.
         */
        @Child(name = "url", type = {UriType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="URL to the specification", formalDefinition="URL to a particular requirement or feature within the FHIR specification." )
        protected UriType url;

        /**
         * Short description of the link.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Short description", formalDefinition="Short description of the link." )
        protected StringType description;

        private static final long serialVersionUID = 213372298L;

    /*
     * Constructor
     */
      public TestScriptTestMetadataLinkComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptTestMetadataLinkComponent(UriType url) {
        super();
        this.url = url;
      }

        /**
         * @return {@link #url} (URL to a particular requirement or feature within the FHIR specification.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataLinkComponent.url");
            else if (Configuration.doAutoCreate())
              this.url = new UriType(); // bb
          return this.url;
        }

        public boolean hasUrlElement() { 
          return this.url != null && !this.url.isEmpty();
        }

        public boolean hasUrl() { 
          return this.url != null && !this.url.isEmpty();
        }

        /**
         * @param value {@link #url} (URL to a particular requirement or feature within the FHIR specification.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public TestScriptTestMetadataLinkComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return URL to a particular requirement or feature within the FHIR specification.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value URL to a particular requirement or feature within the FHIR specification.
         */
        public TestScriptTestMetadataLinkComponent setUrl(String value) { 
            if (this.url == null)
              this.url = new UriType();
            this.url.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Short description of the link.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataLinkComponent.description");
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
         * @param value {@link #description} (Short description of the link.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestScriptTestMetadataLinkComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Short description of the link.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Short description of the link.
         */
        public TestScriptTestMetadataLinkComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("url", "uri", "URL to a particular requirement or feature within the FHIR specification.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("description", "string", "Short description of the link.", 0, java.lang.Integer.MAX_VALUE, description));
        }

      public TestScriptTestMetadataLinkComponent copy() {
        TestScriptTestMetadataLinkComponent dst = new TestScriptTestMetadataLinkComponent();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataLinkComponent))
          return false;
        TestScriptTestMetadataLinkComponent o = (TestScriptTestMetadataLinkComponent) other;
        return compareDeep(url, o.url, true) && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataLinkComponent))
          return false;
        TestScriptTestMetadataLinkComponent o = (TestScriptTestMetadataLinkComponent) other;
        return compareValues(url, o.url, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (description == null || description.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptTestMetadataRequiresComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The resource type that the FHIR server must support.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Required resource type", formalDefinition="The resource type that the FHIR server must support." )
        protected CodeType type;

        /**
         * The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.
         */
        @Child(name = "operations", type = {StringType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Required operations", formalDefinition="The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType." )
        protected StringType operations;

        /**
         * Which server this requirement applies to.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Which server this requirement applies to", formalDefinition="Which server this requirement applies to." )
        protected IntegerType destination;

        private static final long serialVersionUID = 753954308L;

    /*
     * Constructor
     */
      public TestScriptTestMetadataRequiresComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptTestMetadataRequiresComponent(CodeType type, StringType operations) {
        super();
        this.type = type;
        this.operations = operations;
      }

        /**
         * @return {@link #type} (The resource type that the FHIR server must support.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataRequiresComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The resource type that the FHIR server must support.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public TestScriptTestMetadataRequiresComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The resource type that the FHIR server must support.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The resource type that the FHIR server must support.
         */
        public TestScriptTestMetadataRequiresComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #operations} (The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.). This is the underlying object with id, value and extensions. The accessor "getOperations" gives direct access to the value
         */
        public StringType getOperationsElement() { 
          if (this.operations == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataRequiresComponent.operations");
            else if (Configuration.doAutoCreate())
              this.operations = new StringType(); // bb
          return this.operations;
        }

        public boolean hasOperationsElement() { 
          return this.operations != null && !this.operations.isEmpty();
        }

        public boolean hasOperations() { 
          return this.operations != null && !this.operations.isEmpty();
        }

        /**
         * @param value {@link #operations} (The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.). This is the underlying object with id, value and extensions. The accessor "getOperations" gives direct access to the value
         */
        public TestScriptTestMetadataRequiresComponent setOperationsElement(StringType value) { 
          this.operations = value;
          return this;
        }

        /**
         * @return The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.
         */
        public String getOperations() { 
          return this.operations == null ? null : this.operations.getValue();
        }

        /**
         * @param value The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.
         */
        public TestScriptTestMetadataRequiresComponent setOperations(String value) { 
            if (this.operations == null)
              this.operations = new StringType();
            this.operations.setValue(value);
          return this;
        }

        /**
         * @return {@link #destination} (Which server this requirement applies to.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataRequiresComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new IntegerType(); // bb
          return this.destination;
        }

        public boolean hasDestinationElement() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Which server this requirement applies to.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public TestScriptTestMetadataRequiresComponent setDestinationElement(IntegerType value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return Which server this requirement applies to.
         */
        public int getDestination() { 
          return this.destination == null || this.destination.isEmpty() ? 0 : this.destination.getValue();
        }

        /**
         * @param value Which server this requirement applies to.
         */
        public TestScriptTestMetadataRequiresComponent setDestination(int value) { 
            if (this.destination == null)
              this.destination = new IntegerType();
            this.destination.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The resource type that the FHIR server must support.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("operations", "string", "The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.", 0, java.lang.Integer.MAX_VALUE, operations));
          childrenList.add(new Property("destination", "integer", "Which server this requirement applies to.", 0, java.lang.Integer.MAX_VALUE, destination));
        }

      public TestScriptTestMetadataRequiresComponent copy() {
        TestScriptTestMetadataRequiresComponent dst = new TestScriptTestMetadataRequiresComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.operations = operations == null ? null : operations.copy();
        dst.destination = destination == null ? null : destination.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataRequiresComponent))
          return false;
        TestScriptTestMetadataRequiresComponent o = (TestScriptTestMetadataRequiresComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(operations, o.operations, true) && compareDeep(destination, o.destination, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataRequiresComponent))
          return false;
        TestScriptTestMetadataRequiresComponent o = (TestScriptTestMetadataRequiresComponent) other;
        return compareValues(type, o.type, true) && compareValues(operations, o.operations, true) && compareValues(destination, o.destination, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (operations == null || operations.isEmpty())
           && (destination == null || destination.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTestMetadataValidatesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The resource type that the FHIR server must support.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Verified resource type", formalDefinition="The resource type that the FHIR server must support." )
        protected CodeType type;

        /**
         * The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.
         */
        @Child(name = "operations", type = {StringType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Verified operations", formalDefinition="The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType." )
        protected StringType operations;

        /**
         * Which server this validation applies to.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Which server this validation applies to", formalDefinition="Which server this validation applies to." )
        protected IntegerType destination;

        private static final long serialVersionUID = 753954308L;

    /*
     * Constructor
     */
      public TestScriptTestMetadataValidatesComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptTestMetadataValidatesComponent(CodeType type, StringType operations) {
        super();
        this.type = type;
        this.operations = operations;
      }

        /**
         * @return {@link #type} (The resource type that the FHIR server must support.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataValidatesComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The resource type that the FHIR server must support.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public TestScriptTestMetadataValidatesComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The resource type that the FHIR server must support.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The resource type that the FHIR server must support.
         */
        public TestScriptTestMetadataValidatesComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #operations} (The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.). This is the underlying object with id, value and extensions. The accessor "getOperations" gives direct access to the value
         */
        public StringType getOperationsElement() { 
          if (this.operations == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataValidatesComponent.operations");
            else if (Configuration.doAutoCreate())
              this.operations = new StringType(); // bb
          return this.operations;
        }

        public boolean hasOperationsElement() { 
          return this.operations != null && !this.operations.isEmpty();
        }

        public boolean hasOperations() { 
          return this.operations != null && !this.operations.isEmpty();
        }

        /**
         * @param value {@link #operations} (The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.). This is the underlying object with id, value and extensions. The accessor "getOperations" gives direct access to the value
         */
        public TestScriptTestMetadataValidatesComponent setOperationsElement(StringType value) { 
          this.operations = value;
          return this;
        }

        /**
         * @return The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.
         */
        public String getOperations() { 
          return this.operations == null ? null : this.operations.getValue();
        }

        /**
         * @param value The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.
         */
        public TestScriptTestMetadataValidatesComponent setOperations(String value) { 
            if (this.operations == null)
              this.operations = new StringType();
            this.operations.setValue(value);
          return this;
        }

        /**
         * @return {@link #destination} (Which server this validation applies to.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestMetadataValidatesComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new IntegerType(); // bb
          return this.destination;
        }

        public boolean hasDestinationElement() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Which server this validation applies to.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public TestScriptTestMetadataValidatesComponent setDestinationElement(IntegerType value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return Which server this validation applies to.
         */
        public int getDestination() { 
          return this.destination == null || this.destination.isEmpty() ? 0 : this.destination.getValue();
        }

        /**
         * @param value Which server this validation applies to.
         */
        public TestScriptTestMetadataValidatesComponent setDestination(int value) { 
            if (this.destination == null)
              this.destination = new IntegerType();
            this.destination.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The resource type that the FHIR server must support.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("operations", "string", "The operations (comma-separated) that the FHIR server must support for the specified resource type. See TestOperationType.", 0, java.lang.Integer.MAX_VALUE, operations));
          childrenList.add(new Property("destination", "integer", "Which server this validation applies to.", 0, java.lang.Integer.MAX_VALUE, destination));
        }

      public TestScriptTestMetadataValidatesComponent copy() {
        TestScriptTestMetadataValidatesComponent dst = new TestScriptTestMetadataValidatesComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.operations = operations == null ? null : operations.copy();
        dst.destination = destination == null ? null : destination.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataValidatesComponent))
          return false;
        TestScriptTestMetadataValidatesComponent o = (TestScriptTestMetadataValidatesComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(operations, o.operations, true) && compareDeep(destination, o.destination, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTestMetadataValidatesComponent))
          return false;
        TestScriptTestMetadataValidatesComponent o = (TestScriptTestMetadataValidatesComponent) other;
        return compareValues(type, o.type, true) && compareValues(operations, o.operations, true) && compareValues(destination, o.destination, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (operations == null || operations.isEmpty())
           && (destination == null || destination.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTestOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning", formalDefinition="read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning." )
        protected Enumeration<TestOperationType> type;

        /**
         * The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        @Child(name = "source", type = {IdType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The id of the fixture used as the body in a PUT or POST", formalDefinition="The internal id of the fixture used as the body of any operation.type that results in a PUT or POST." )
        protected IdType source;

        /**
         * The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        @Child(name = "target", type = {IdType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="The id of the fixture used as the target of a PUT or POST, or the id of the fixture used to store the results of a GET", formalDefinition="The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET." )
        protected IdType target;

        /**
         * Which server to perform the operation on.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Which server to perform the operation on", formalDefinition="Which server to perform the operation on." )
        protected IntegerType destination;

        /**
         * Arguments to an operation.
         */
        @Child(name = "parameter", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Arguments to an operation", formalDefinition="Arguments to an operation." )
        protected List<StringType> parameter;

        /**
         * The fixture id (maybe new) to map to the response.
         */
        @Child(name = "responseId", type = {IdType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Response id", formalDefinition="The fixture id (maybe new) to map to the response." )
        protected IdType responseId;

        /**
         * The content-type or mime-type to use for RESTful operation.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation." )
        protected Enumeration<ContentType> contentType;

        private static final long serialVersionUID = 1788056082L;

    /*
     * Constructor
     */
      public TestScriptTestOperationComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptTestOperationComponent(Enumeration<TestOperationType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<TestOperationType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestOperationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<TestOperationType>(new TestOperationTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public TestScriptTestOperationComponent setTypeElement(Enumeration<TestOperationType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        public TestOperationType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        public TestScriptTestOperationComponent setType(TestOperationType value) { 
            if (this.type == null)
              this.type = new Enumeration<TestOperationType>(new TestOperationTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public IdType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestOperationComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new IdType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public TestScriptTestOperationComponent setSourceElement(IdType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        public TestScriptTestOperationComponent setSource(String value) { 
          if (Utilities.noString(value))
            this.source = null;
          else {
            if (this.source == null)
              this.source = new IdType();
            this.source.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #target} (The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public IdType getTargetElement() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestOperationComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new IdType(); // bb
          return this.target;
        }

        public boolean hasTargetElement() { 
          return this.target != null && !this.target.isEmpty();
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public TestScriptTestOperationComponent setTargetElement(IdType value) { 
          this.target = value;
          return this;
        }

        /**
         * @return The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        public String getTarget() { 
          return this.target == null ? null : this.target.getValue();
        }

        /**
         * @param value The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        public TestScriptTestOperationComponent setTarget(String value) { 
          if (Utilities.noString(value))
            this.target = null;
          else {
            if (this.target == null)
              this.target = new IdType();
            this.target.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #destination} (Which server to perform the operation on.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestOperationComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new IntegerType(); // bb
          return this.destination;
        }

        public boolean hasDestinationElement() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Which server to perform the operation on.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public TestScriptTestOperationComponent setDestinationElement(IntegerType value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return Which server to perform the operation on.
         */
        public int getDestination() { 
          return this.destination == null || this.destination.isEmpty() ? 0 : this.destination.getValue();
        }

        /**
         * @param value Which server to perform the operation on.
         */
        public TestScriptTestOperationComponent setDestination(int value) { 
            if (this.destination == null)
              this.destination = new IntegerType();
            this.destination.setValue(value);
          return this;
        }

        /**
         * @return {@link #parameter} (Arguments to an operation.)
         */
        public List<StringType> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          return this.parameter;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (StringType item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #parameter} (Arguments to an operation.)
         */
    // syntactic sugar
        public StringType addParameterElement() {//2 
          StringType t = new StringType();
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          this.parameter.add(t);
          return t;
        }

        /**
         * @param value {@link #parameter} (Arguments to an operation.)
         */
        public TestScriptTestOperationComponent addParameter(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          this.parameter.add(t);
          return this;
        }

        /**
         * @param value {@link #parameter} (Arguments to an operation.)
         */
        public boolean hasParameter(String value) { 
          if (this.parameter == null)
            return false;
          for (StringType v : this.parameter)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public IdType getResponseIdElement() { 
          if (this.responseId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestOperationComponent.responseId");
            else if (Configuration.doAutoCreate())
              this.responseId = new IdType(); // bb
          return this.responseId;
        }

        public boolean hasResponseIdElement() { 
          return this.responseId != null && !this.responseId.isEmpty();
        }

        public boolean hasResponseId() { 
          return this.responseId != null && !this.responseId.isEmpty();
        }

        /**
         * @param value {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public TestScriptTestOperationComponent setResponseIdElement(IdType value) { 
          this.responseId = value;
          return this;
        }

        /**
         * @return The fixture id (maybe new) to map to the response.
         */
        public String getResponseId() { 
          return this.responseId == null ? null : this.responseId.getValue();
        }

        /**
         * @param value The fixture id (maybe new) to map to the response.
         */
        public TestScriptTestOperationComponent setResponseId(String value) { 
          if (Utilities.noString(value))
            this.responseId = null;
          else {
            if (this.responseId == null)
              this.responseId = new IdType();
            this.responseId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #contentType} (The content-type or mime-type to use for RESTful operation.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public Enumeration<ContentType> getContentTypeElement() { 
          if (this.contentType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestOperationComponent.contentType");
            else if (Configuration.doAutoCreate())
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory()); // bb
          return this.contentType;
        }

        public boolean hasContentTypeElement() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        public boolean hasContentType() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        /**
         * @param value {@link #contentType} (The content-type or mime-type to use for RESTful operation.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public TestScriptTestOperationComponent setContentTypeElement(Enumeration<ContentType> value) { 
          this.contentType = value;
          return this;
        }

        /**
         * @return The content-type or mime-type to use for RESTful operation.
         */
        public ContentType getContentType() { 
          return this.contentType == null ? null : this.contentType.getValue();
        }

        /**
         * @param value The content-type or mime-type to use for RESTful operation.
         */
        public TestScriptTestOperationComponent setContentType(ContentType value) { 
          if (value == null)
            this.contentType = null;
          else {
            if (this.contentType == null)
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory());
            this.contentType.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("source", "id", "The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.", 0, java.lang.Integer.MAX_VALUE, source));
          childrenList.add(new Property("target", "id", "The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.", 0, java.lang.Integer.MAX_VALUE, target));
          childrenList.add(new Property("destination", "integer", "Which server to perform the operation on.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("parameter", "string", "Arguments to an operation.", 0, java.lang.Integer.MAX_VALUE, parameter));
          childrenList.add(new Property("responseId", "id", "The fixture id (maybe new) to map to the response.", 0, java.lang.Integer.MAX_VALUE, responseId));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation.", 0, java.lang.Integer.MAX_VALUE, contentType));
        }

      public TestScriptTestOperationComponent copy() {
        TestScriptTestOperationComponent dst = new TestScriptTestOperationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.source = source == null ? null : source.copy();
        dst.target = target == null ? null : target.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<StringType>();
          for (StringType i : parameter)
            dst.parameter.add(i.copy());
        };
        dst.responseId = responseId == null ? null : responseId.copy();
        dst.contentType = contentType == null ? null : contentType.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTestOperationComponent))
          return false;
        TestScriptTestOperationComponent o = (TestScriptTestOperationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(source, o.source, true) && compareDeep(target, o.target, true)
           && compareDeep(destination, o.destination, true) && compareDeep(parameter, o.parameter, true) && compareDeep(responseId, o.responseId, true)
           && compareDeep(contentType, o.contentType, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTestOperationComponent))
          return false;
        TestScriptTestOperationComponent o = (TestScriptTestOperationComponent) other;
        return compareValues(type, o.type, true) && compareValues(source, o.source, true) && compareValues(target, o.target, true)
           && compareValues(destination, o.destination, true) && compareValues(parameter, o.parameter, true) && compareValues(responseId, o.responseId, true)
           && compareValues(contentType, o.contentType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (source == null || source.isEmpty())
           && (target == null || target.isEmpty()) && (destination == null || destination.isEmpty())
           && (parameter == null || parameter.isEmpty()) && (responseId == null || responseId.isEmpty())
           && (contentType == null || contentType.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTeardownComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A teardown operation.
         */
        @Child(name = "operation", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="A teardown operation", formalDefinition="A teardown operation." )
        protected List<TestScriptTeardownOperationComponent> operation;

        private static final long serialVersionUID = -1832198026L;

    /*
     * Constructor
     */
      public TestScriptTeardownComponent() {
        super();
      }

        /**
         * @return {@link #operation} (A teardown operation.)
         */
        public List<TestScriptTeardownOperationComponent> getOperation() { 
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptTeardownOperationComponent>();
          return this.operation;
        }

        public boolean hasOperation() { 
          if (this.operation == null)
            return false;
          for (TestScriptTeardownOperationComponent item : this.operation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #operation} (A teardown operation.)
         */
    // syntactic sugar
        public TestScriptTeardownOperationComponent addOperation() { //3
          TestScriptTeardownOperationComponent t = new TestScriptTeardownOperationComponent();
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptTeardownOperationComponent>();
          this.operation.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTeardownComponent addOperation(TestScriptTeardownOperationComponent t) { //3
          if (t == null)
            return this;
          if (this.operation == null)
            this.operation = new ArrayList<TestScriptTeardownOperationComponent>();
          this.operation.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "", "A teardown operation.", 0, java.lang.Integer.MAX_VALUE, operation));
        }

      public TestScriptTeardownComponent copy() {
        TestScriptTeardownComponent dst = new TestScriptTeardownComponent();
        copyValues(dst);
        if (operation != null) {
          dst.operation = new ArrayList<TestScriptTeardownOperationComponent>();
          for (TestScriptTeardownOperationComponent i : operation)
            dst.operation.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTeardownComponent))
          return false;
        TestScriptTeardownComponent o = (TestScriptTeardownComponent) other;
        return compareDeep(operation, o.operation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTeardownComponent))
          return false;
        TestScriptTeardownComponent o = (TestScriptTeardownComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (operation == null || operation.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTeardownOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning", formalDefinition="read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning." )
        protected Enumeration<TestOperationType> type;

        /**
         * The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        @Child(name = "source", type = {IdType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The id of the fixture used as the body in a PUT or POST", formalDefinition="The internal id of the fixture used as the body of any operation.type that results in a PUT or POST." )
        protected IdType source;

        /**
         * The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        @Child(name = "target", type = {IdType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="The id of the fixture used as the target of a PUT or POST, or the id of the fixture used to store the results of a GET", formalDefinition="The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET." )
        protected IdType target;

        /**
         * Which server to perform the operation on.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Which server to perform the operation on", formalDefinition="Which server to perform the operation on." )
        protected IntegerType destination;

        /**
         * Arguments to an operation.
         */
        @Child(name = "parameter", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Arguments to an operation", formalDefinition="Arguments to an operation." )
        protected List<StringType> parameter;

        /**
         * The fixture id (maybe new) to map to the response.
         */
        @Child(name = "responseId", type = {IdType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Response id", formalDefinition="The fixture id (maybe new) to map to the response." )
        protected IdType responseId;

        /**
         * The content-type or mime-type to use for RESTful operation.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation." )
        protected Enumeration<ContentType> contentType;

        private static final long serialVersionUID = 1788056082L;

    /*
     * Constructor
     */
      public TestScriptTeardownOperationComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptTeardownOperationComponent(Enumeration<TestOperationType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<TestOperationType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTeardownOperationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<TestOperationType>(new TestOperationTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public TestScriptTeardownOperationComponent setTypeElement(Enumeration<TestOperationType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        public TestOperationType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.
         */
        public TestScriptTeardownOperationComponent setType(TestOperationType value) { 
            if (this.type == null)
              this.type = new Enumeration<TestOperationType>(new TestOperationTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #source} (The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public IdType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTeardownOperationComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new IdType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public TestScriptTeardownOperationComponent setSourceElement(IdType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.
         */
        public TestScriptTeardownOperationComponent setSource(String value) { 
          if (Utilities.noString(value))
            this.source = null;
          else {
            if (this.source == null)
              this.source = new IdType();
            this.source.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #target} (The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public IdType getTargetElement() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTeardownOperationComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new IdType(); // bb
          return this.target;
        }

        public boolean hasTargetElement() { 
          return this.target != null && !this.target.isEmpty();
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public TestScriptTeardownOperationComponent setTargetElement(IdType value) { 
          this.target = value;
          return this;
        }

        /**
         * @return The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        public String getTarget() { 
          return this.target == null ? null : this.target.getValue();
        }

        /**
         * @param value The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.
         */
        public TestScriptTeardownOperationComponent setTarget(String value) { 
          if (Utilities.noString(value))
            this.target = null;
          else {
            if (this.target == null)
              this.target = new IdType();
            this.target.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #destination} (Which server to perform the operation on.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTeardownOperationComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new IntegerType(); // bb
          return this.destination;
        }

        public boolean hasDestinationElement() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Which server to perform the operation on.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public TestScriptTeardownOperationComponent setDestinationElement(IntegerType value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return Which server to perform the operation on.
         */
        public int getDestination() { 
          return this.destination == null || this.destination.isEmpty() ? 0 : this.destination.getValue();
        }

        /**
         * @param value Which server to perform the operation on.
         */
        public TestScriptTeardownOperationComponent setDestination(int value) { 
            if (this.destination == null)
              this.destination = new IntegerType();
            this.destination.setValue(value);
          return this;
        }

        /**
         * @return {@link #parameter} (Arguments to an operation.)
         */
        public List<StringType> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          return this.parameter;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (StringType item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #parameter} (Arguments to an operation.)
         */
    // syntactic sugar
        public StringType addParameterElement() {//2 
          StringType t = new StringType();
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          this.parameter.add(t);
          return t;
        }

        /**
         * @param value {@link #parameter} (Arguments to an operation.)
         */
        public TestScriptTeardownOperationComponent addParameter(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.parameter == null)
            this.parameter = new ArrayList<StringType>();
          this.parameter.add(t);
          return this;
        }

        /**
         * @param value {@link #parameter} (Arguments to an operation.)
         */
        public boolean hasParameter(String value) { 
          if (this.parameter == null)
            return false;
          for (StringType v : this.parameter)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public IdType getResponseIdElement() { 
          if (this.responseId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTeardownOperationComponent.responseId");
            else if (Configuration.doAutoCreate())
              this.responseId = new IdType(); // bb
          return this.responseId;
        }

        public boolean hasResponseIdElement() { 
          return this.responseId != null && !this.responseId.isEmpty();
        }

        public boolean hasResponseId() { 
          return this.responseId != null && !this.responseId.isEmpty();
        }

        /**
         * @param value {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public TestScriptTeardownOperationComponent setResponseIdElement(IdType value) { 
          this.responseId = value;
          return this;
        }

        /**
         * @return The fixture id (maybe new) to map to the response.
         */
        public String getResponseId() { 
          return this.responseId == null ? null : this.responseId.getValue();
        }

        /**
         * @param value The fixture id (maybe new) to map to the response.
         */
        public TestScriptTeardownOperationComponent setResponseId(String value) { 
          if (Utilities.noString(value))
            this.responseId = null;
          else {
            if (this.responseId == null)
              this.responseId = new IdType();
            this.responseId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #contentType} (The content-type or mime-type to use for RESTful operation.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public Enumeration<ContentType> getContentTypeElement() { 
          if (this.contentType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTeardownOperationComponent.contentType");
            else if (Configuration.doAutoCreate())
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory()); // bb
          return this.contentType;
        }

        public boolean hasContentTypeElement() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        public boolean hasContentType() { 
          return this.contentType != null && !this.contentType.isEmpty();
        }

        /**
         * @param value {@link #contentType} (The content-type or mime-type to use for RESTful operation.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public TestScriptTeardownOperationComponent setContentTypeElement(Enumeration<ContentType> value) { 
          this.contentType = value;
          return this;
        }

        /**
         * @return The content-type or mime-type to use for RESTful operation.
         */
        public ContentType getContentType() { 
          return this.contentType == null ? null : this.contentType.getValue();
        }

        /**
         * @param value The content-type or mime-type to use for RESTful operation.
         */
        public TestScriptTeardownOperationComponent setContentType(ContentType value) { 
          if (value == null)
            this.contentType = null;
          else {
            if (this.contentType == null)
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory());
            this.contentType.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "read | vread | update | delete | history | create | search | transaction | conformance | tags | mailbox | document | assertion | assertion_false | assertion_warning.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("source", "id", "The internal id of the fixture used as the body of any operation.type that results in a PUT or POST.", 0, java.lang.Integer.MAX_VALUE, source));
          childrenList.add(new Property("target", "id", "The id of the fixture used as the target of any operation.type that results in a PUT or POST, or the id of the fixture used to store the results of a GET.", 0, java.lang.Integer.MAX_VALUE, target));
          childrenList.add(new Property("destination", "integer", "Which server to perform the operation on.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("parameter", "string", "Arguments to an operation.", 0, java.lang.Integer.MAX_VALUE, parameter));
          childrenList.add(new Property("responseId", "id", "The fixture id (maybe new) to map to the response.", 0, java.lang.Integer.MAX_VALUE, responseId));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation.", 0, java.lang.Integer.MAX_VALUE, contentType));
        }

      public TestScriptTeardownOperationComponent copy() {
        TestScriptTeardownOperationComponent dst = new TestScriptTeardownOperationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.source = source == null ? null : source.copy();
        dst.target = target == null ? null : target.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<StringType>();
          for (StringType i : parameter)
            dst.parameter.add(i.copy());
        };
        dst.responseId = responseId == null ? null : responseId.copy();
        dst.contentType = contentType == null ? null : contentType.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTeardownOperationComponent))
          return false;
        TestScriptTeardownOperationComponent o = (TestScriptTeardownOperationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(source, o.source, true) && compareDeep(target, o.target, true)
           && compareDeep(destination, o.destination, true) && compareDeep(parameter, o.parameter, true) && compareDeep(responseId, o.responseId, true)
           && compareDeep(contentType, o.contentType, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTeardownOperationComponent))
          return false;
        TestScriptTeardownOperationComponent o = (TestScriptTeardownOperationComponent) other;
        return compareValues(type, o.type, true) && compareValues(source, o.source, true) && compareValues(target, o.target, true)
           && compareValues(destination, o.destination, true) && compareValues(parameter, o.parameter, true) && compareValues(responseId, o.responseId, true)
           && compareValues(contentType, o.contentType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (source == null || source.isEmpty())
           && (target == null || target.isEmpty()) && (destination == null || destination.isEmpty())
           && (parameter == null || parameter.isEmpty()) && (responseId == null || responseId.isEmpty())
           && (contentType == null || contentType.isEmpty());
      }

  }

    /**
     * Name of the Test Script.
     */
    @Child(name = "name", type = {StringType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Name", formalDefinition="Name of the Test Script." )
    protected StringType name;

    /**
     * Description of the Test Script.
     */
    @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Short description", formalDefinition="Description of the Test Script." )
    protected StringType description;

    /**
     * If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults or missing values to false.
     */
    @Child(name = "multiserver", type = {BooleanType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="If the tests apply to more than one FHIR server", formalDefinition="If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults or missing values to false." )
    protected BooleanType multiserver;

    /**
     * Fixture in the test script - either by reference (uri) or embedded (Resource). All fixtures are required for the test script to execute.
     */
    @Child(name = "fixture", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Fixture in the test script - either by reference (uri) or embedded (Resource)", formalDefinition="Fixture in the test script - either by reference (uri) or embedded (Resource). All fixtures are required for the test script to execute." )
    protected List<TestScriptFixtureComponent> fixture;

    /**
     * A series of required setup operations before tests are executed.
     */
    @Child(name = "setup", type = {}, order=4, min=0, max=1)
    @Description(shortDefinition="A series of required setup operations before tests are executed", formalDefinition="A series of required setup operations before tests are executed." )
    protected TestScriptSetupComponent setup;

    /**
     * A test in this script.
     */
    @Child(name = "test", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="A test in this script", formalDefinition="A test in this script." )
    protected List<TestScriptTestComponent> test;

    /**
     * A series of operations required to clean up after the all the tests are executed (successfully or otherwise).
     */
    @Child(name = "teardown", type = {}, order=6, min=0, max=1)
    @Description(shortDefinition="A series of required clean up steps", formalDefinition="A series of operations required to clean up after the all the tests are executed (successfully or otherwise)." )
    protected TestScriptTeardownComponent teardown;

    private static final long serialVersionUID = 334757467L;

  /*
   * Constructor
   */
    public TestScript() {
      super();
    }

    /**
     * @return {@link #name} (Name of the Test Script.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.name");
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
     * @param value {@link #name} (Name of the Test Script.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public TestScript setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return Name of the Test Script.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value Name of the Test Script.
     */
    public TestScript setName(String value) { 
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
     * @return {@link #description} (Description of the Test Script.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.description");
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
     * @param value {@link #description} (Description of the Test Script.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public TestScript setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Description of the Test Script.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Description of the Test Script.
     */
    public TestScript setDescription(String value) { 
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
     * @return {@link #multiserver} (If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults or missing values to false.). This is the underlying object with id, value and extensions. The accessor "getMultiserver" gives direct access to the value
     */
    public BooleanType getMultiserverElement() { 
      if (this.multiserver == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.multiserver");
        else if (Configuration.doAutoCreate())
          this.multiserver = new BooleanType(); // bb
      return this.multiserver;
    }

    public boolean hasMultiserverElement() { 
      return this.multiserver != null && !this.multiserver.isEmpty();
    }

    public boolean hasMultiserver() { 
      return this.multiserver != null && !this.multiserver.isEmpty();
    }

    /**
     * @param value {@link #multiserver} (If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults or missing values to false.). This is the underlying object with id, value and extensions. The accessor "getMultiserver" gives direct access to the value
     */
    public TestScript setMultiserverElement(BooleanType value) { 
      this.multiserver = value;
      return this;
    }

    /**
     * @return If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults or missing values to false.
     */
    public boolean getMultiserver() { 
      return this.multiserver == null || this.multiserver.isEmpty() ? false : this.multiserver.getValue();
    }

    /**
     * @param value If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults or missing values to false.
     */
    public TestScript setMultiserver(boolean value) { 
        if (this.multiserver == null)
          this.multiserver = new BooleanType();
        this.multiserver.setValue(value);
      return this;
    }

    /**
     * @return {@link #fixture} (Fixture in the test script - either by reference (uri) or embedded (Resource). All fixtures are required for the test script to execute.)
     */
    public List<TestScriptFixtureComponent> getFixture() { 
      if (this.fixture == null)
        this.fixture = new ArrayList<TestScriptFixtureComponent>();
      return this.fixture;
    }

    public boolean hasFixture() { 
      if (this.fixture == null)
        return false;
      for (TestScriptFixtureComponent item : this.fixture)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #fixture} (Fixture in the test script - either by reference (uri) or embedded (Resource). All fixtures are required for the test script to execute.)
     */
    // syntactic sugar
    public TestScriptFixtureComponent addFixture() { //3
      TestScriptFixtureComponent t = new TestScriptFixtureComponent();
      if (this.fixture == null)
        this.fixture = new ArrayList<TestScriptFixtureComponent>();
      this.fixture.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addFixture(TestScriptFixtureComponent t) { //3
      if (t == null)
        return this;
      if (this.fixture == null)
        this.fixture = new ArrayList<TestScriptFixtureComponent>();
      this.fixture.add(t);
      return this;
    }

    /**
     * @return {@link #setup} (A series of required setup operations before tests are executed.)
     */
    public TestScriptSetupComponent getSetup() { 
      if (this.setup == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.setup");
        else if (Configuration.doAutoCreate())
          this.setup = new TestScriptSetupComponent(); // cc
      return this.setup;
    }

    public boolean hasSetup() { 
      return this.setup != null && !this.setup.isEmpty();
    }

    /**
     * @param value {@link #setup} (A series of required setup operations before tests are executed.)
     */
    public TestScript setSetup(TestScriptSetupComponent value) { 
      this.setup = value;
      return this;
    }

    /**
     * @return {@link #test} (A test in this script.)
     */
    public List<TestScriptTestComponent> getTest() { 
      if (this.test == null)
        this.test = new ArrayList<TestScriptTestComponent>();
      return this.test;
    }

    public boolean hasTest() { 
      if (this.test == null)
        return false;
      for (TestScriptTestComponent item : this.test)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #test} (A test in this script.)
     */
    // syntactic sugar
    public TestScriptTestComponent addTest() { //3
      TestScriptTestComponent t = new TestScriptTestComponent();
      if (this.test == null)
        this.test = new ArrayList<TestScriptTestComponent>();
      this.test.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addTest(TestScriptTestComponent t) { //3
      if (t == null)
        return this;
      if (this.test == null)
        this.test = new ArrayList<TestScriptTestComponent>();
      this.test.add(t);
      return this;
    }

    /**
     * @return {@link #teardown} (A series of operations required to clean up after the all the tests are executed (successfully or otherwise).)
     */
    public TestScriptTeardownComponent getTeardown() { 
      if (this.teardown == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.teardown");
        else if (Configuration.doAutoCreate())
          this.teardown = new TestScriptTeardownComponent(); // cc
      return this.teardown;
    }

    public boolean hasTeardown() { 
      return this.teardown != null && !this.teardown.isEmpty();
    }

    /**
     * @param value {@link #teardown} (A series of operations required to clean up after the all the tests are executed (successfully or otherwise).)
     */
    public TestScript setTeardown(TestScriptTeardownComponent value) { 
      this.teardown = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("name", "string", "Name of the Test Script.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("description", "string", "Description of the Test Script.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("multiserver", "boolean", "If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults or missing values to false.", 0, java.lang.Integer.MAX_VALUE, multiserver));
        childrenList.add(new Property("fixture", "", "Fixture in the test script - either by reference (uri) or embedded (Resource). All fixtures are required for the test script to execute.", 0, java.lang.Integer.MAX_VALUE, fixture));
        childrenList.add(new Property("setup", "", "A series of required setup operations before tests are executed.", 0, java.lang.Integer.MAX_VALUE, setup));
        childrenList.add(new Property("test", "", "A test in this script.", 0, java.lang.Integer.MAX_VALUE, test));
        childrenList.add(new Property("teardown", "", "A series of operations required to clean up after the all the tests are executed (successfully or otherwise).", 0, java.lang.Integer.MAX_VALUE, teardown));
      }

      public TestScript copy() {
        TestScript dst = new TestScript();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.multiserver = multiserver == null ? null : multiserver.copy();
        if (fixture != null) {
          dst.fixture = new ArrayList<TestScriptFixtureComponent>();
          for (TestScriptFixtureComponent i : fixture)
            dst.fixture.add(i.copy());
        };
        dst.setup = setup == null ? null : setup.copy();
        if (test != null) {
          dst.test = new ArrayList<TestScriptTestComponent>();
          for (TestScriptTestComponent i : test)
            dst.test.add(i.copy());
        };
        dst.teardown = teardown == null ? null : teardown.copy();
        return dst;
      }

      protected TestScript typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScript))
          return false;
        TestScript o = (TestScript) other;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(multiserver, o.multiserver, true)
           && compareDeep(fixture, o.fixture, true) && compareDeep(setup, o.setup, true) && compareDeep(test, o.test, true)
           && compareDeep(teardown, o.teardown, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScript))
          return false;
        TestScript o = (TestScript) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true) && compareValues(multiserver, o.multiserver, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (description == null || description.isEmpty())
           && (multiserver == null || multiserver.isEmpty()) && (fixture == null || fixture.isEmpty())
           && (setup == null || setup.isEmpty()) && (test == null || test.isEmpty()) && (teardown == null || teardown.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.TestScript;
   }

  @SearchParamDefinition(name="validates-operations", path="TestScript.test.metadata.validates.operations", description="Test that validates the server supports certain TestOpertionType operations.", type="string" )
  public static final String SP_VALIDATESOPERATIONS = "validates-operations";
  @SearchParamDefinition(name="validates-type", path="TestScript.test.metadata.validates.type", description="Test that validates the server supports a certain Resource type.", type="string" )
  public static final String SP_VALIDATESTYPE = "validates-type";
  @SearchParamDefinition(name="requires-type", path="TestScript.test.metadata.requires.type", description="Test that requires the server supports a certain Resource type.", type="string" )
  public static final String SP_REQUIRESTYPE = "requires-type";
  @SearchParamDefinition(name="name", path="TestScript.name", description="TestScript name", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="description", path="TestScript.description", description="TestScript description", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="requires-operations", path="TestScript.test.metadata.requires.operations", description="Test that requires the server supports certain TestOpertionType operations.", type="string" )
  public static final String SP_REQUIRESOPERATIONS = "requires-operations";

}

