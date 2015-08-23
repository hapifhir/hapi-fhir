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

    public enum AssertionDirectionType {
        /**
         * Default value. Assertion is evaluated on the response.
         */
        RESPONSE, 
        /**
         * Not equals comparison.
         */
        REQUEST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AssertionDirectionType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("response".equals(codeString))
          return RESPONSE;
        if ("request".equals(codeString))
          return REQUEST;
        throw new Exception("Unknown AssertionDirectionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RESPONSE: return "response";
            case REQUEST: return "request";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case RESPONSE: return "http://hl7.org/fhir/assert-direction-codes";
            case REQUEST: return "http://hl7.org/fhir/assert-direction-codes";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case RESPONSE: return "Default value. Assertion is evaluated on the response.";
            case REQUEST: return "Not equals comparison.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RESPONSE: return "response";
            case REQUEST: return "request";
            default: return "?";
          }
        }
    }

  public static class AssertionDirectionTypeEnumFactory implements EnumFactory<AssertionDirectionType> {
    public AssertionDirectionType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("response".equals(codeString))
          return AssertionDirectionType.RESPONSE;
        if ("request".equals(codeString))
          return AssertionDirectionType.REQUEST;
        throw new IllegalArgumentException("Unknown AssertionDirectionType code '"+codeString+"'");
        }
    public String toCode(AssertionDirectionType code) {
      if (code == AssertionDirectionType.RESPONSE)
        return "response";
      if (code == AssertionDirectionType.REQUEST)
        return "request";
      return "?";
      }
    }

    public enum AssertionOperatorType {
        /**
         * Default value. Equals comparison.
         */
        EQUALS, 
        /**
         * Not equals comparison.
         */
        NOTEQUALS, 
        /**
         * Compare value within a known set of values.
         */
        IN, 
        /**
         * Compare value not within a known set of values.
         */
        NOTIN, 
        /**
         * Compare value to be greater than a known value.
         */
        GREATERTHAN, 
        /**
         * Compare value to be less than a known value.
         */
        LESSTHAN, 
        /**
         * Compare value is empty.
         */
        EMPTY, 
        /**
         * Compare value is not empty.
         */
        NOTEMPTY, 
        /**
         * Compare value string contains a known value.
         */
        CONTAINS, 
        /**
         * Compare value string does not contain a known value.
         */
        NOTCONTAINS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AssertionOperatorType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("equals".equals(codeString))
          return EQUALS;
        if ("notEquals".equals(codeString))
          return NOTEQUALS;
        if ("in".equals(codeString))
          return IN;
        if ("notIn".equals(codeString))
          return NOTIN;
        if ("greaterThan".equals(codeString))
          return GREATERTHAN;
        if ("lessThan".equals(codeString))
          return LESSTHAN;
        if ("empty".equals(codeString))
          return EMPTY;
        if ("notEmpty".equals(codeString))
          return NOTEMPTY;
        if ("contains".equals(codeString))
          return CONTAINS;
        if ("notContains".equals(codeString))
          return NOTCONTAINS;
        throw new Exception("Unknown AssertionOperatorType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case EQUALS: return "equals";
            case NOTEQUALS: return "notEquals";
            case IN: return "in";
            case NOTIN: return "notIn";
            case GREATERTHAN: return "greaterThan";
            case LESSTHAN: return "lessThan";
            case EMPTY: return "empty";
            case NOTEMPTY: return "notEmpty";
            case CONTAINS: return "contains";
            case NOTCONTAINS: return "notContains";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case EQUALS: return "http://hl7.org/fhir/assert-operator-codes";
            case NOTEQUALS: return "http://hl7.org/fhir/assert-operator-codes";
            case IN: return "http://hl7.org/fhir/assert-operator-codes";
            case NOTIN: return "http://hl7.org/fhir/assert-operator-codes";
            case GREATERTHAN: return "http://hl7.org/fhir/assert-operator-codes";
            case LESSTHAN: return "http://hl7.org/fhir/assert-operator-codes";
            case EMPTY: return "http://hl7.org/fhir/assert-operator-codes";
            case NOTEMPTY: return "http://hl7.org/fhir/assert-operator-codes";
            case CONTAINS: return "http://hl7.org/fhir/assert-operator-codes";
            case NOTCONTAINS: return "http://hl7.org/fhir/assert-operator-codes";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case EQUALS: return "Default value. Equals comparison.";
            case NOTEQUALS: return "Not equals comparison.";
            case IN: return "Compare value within a known set of values.";
            case NOTIN: return "Compare value not within a known set of values.";
            case GREATERTHAN: return "Compare value to be greater than a known value.";
            case LESSTHAN: return "Compare value to be less than a known value.";
            case EMPTY: return "Compare value is empty.";
            case NOTEMPTY: return "Compare value is not empty.";
            case CONTAINS: return "Compare value string contains a known value.";
            case NOTCONTAINS: return "Compare value string does not contain a known value.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case EQUALS: return "equals";
            case NOTEQUALS: return "notEquals";
            case IN: return "in";
            case NOTIN: return "notIn";
            case GREATERTHAN: return "greaterThan";
            case LESSTHAN: return "lessThan";
            case EMPTY: return "empty";
            case NOTEMPTY: return "notEmpty";
            case CONTAINS: return "contains";
            case NOTCONTAINS: return "notContains";
            default: return "?";
          }
        }
    }

  public static class AssertionOperatorTypeEnumFactory implements EnumFactory<AssertionOperatorType> {
    public AssertionOperatorType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("equals".equals(codeString))
          return AssertionOperatorType.EQUALS;
        if ("notEquals".equals(codeString))
          return AssertionOperatorType.NOTEQUALS;
        if ("in".equals(codeString))
          return AssertionOperatorType.IN;
        if ("notIn".equals(codeString))
          return AssertionOperatorType.NOTIN;
        if ("greaterThan".equals(codeString))
          return AssertionOperatorType.GREATERTHAN;
        if ("lessThan".equals(codeString))
          return AssertionOperatorType.LESSTHAN;
        if ("empty".equals(codeString))
          return AssertionOperatorType.EMPTY;
        if ("notEmpty".equals(codeString))
          return AssertionOperatorType.NOTEMPTY;
        if ("contains".equals(codeString))
          return AssertionOperatorType.CONTAINS;
        if ("notContains".equals(codeString))
          return AssertionOperatorType.NOTCONTAINS;
        throw new IllegalArgumentException("Unknown AssertionOperatorType code '"+codeString+"'");
        }
    public String toCode(AssertionOperatorType code) {
      if (code == AssertionOperatorType.EQUALS)
        return "equals";
      if (code == AssertionOperatorType.NOTEQUALS)
        return "notEquals";
      if (code == AssertionOperatorType.IN)
        return "in";
      if (code == AssertionOperatorType.NOTIN)
        return "notIn";
      if (code == AssertionOperatorType.GREATERTHAN)
        return "greaterThan";
      if (code == AssertionOperatorType.LESSTHAN)
        return "lessThan";
      if (code == AssertionOperatorType.EMPTY)
        return "empty";
      if (code == AssertionOperatorType.NOTEMPTY)
        return "notEmpty";
      if (code == AssertionOperatorType.CONTAINS)
        return "contains";
      if (code == AssertionOperatorType.NOTCONTAINS)
        return "notContains";
      return "?";
      }
    }

    public enum AssertionResponseTypes {
        /**
         * Response code is 200.
         */
        OKAY, 
        /**
         * Response code is 201.
         */
        CREATED, 
        /**
         * Response code is 204.
         */
        NOCONTENT, 
        /**
         * Response code is 304.
         */
        NOTMODIFIED, 
        /**
         * Response code is 400.
         */
        BAD, 
        /**
         * Response code is 403.
         */
        FORBIDDEN, 
        /**
         * Response code is 404.
         */
        NOTFOUND, 
        /**
         * Response code is 405.
         */
        METHODNOTALLOWED, 
        /**
         * Response code is 409.
         */
        CONFLICT, 
        /**
         * Response code is 410.
         */
        GONE, 
        /**
         * Response code is 412.
         */
        PRECONDITIONFAILED, 
        /**
         * Response code is 422.
         */
        UNPROCESSABLE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AssertionResponseTypes fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("okay".equals(codeString))
          return OKAY;
        if ("created".equals(codeString))
          return CREATED;
        if ("noContent".equals(codeString))
          return NOCONTENT;
        if ("notModified".equals(codeString))
          return NOTMODIFIED;
        if ("bad".equals(codeString))
          return BAD;
        if ("forbidden".equals(codeString))
          return FORBIDDEN;
        if ("notFound".equals(codeString))
          return NOTFOUND;
        if ("methodNotAllowed".equals(codeString))
          return METHODNOTALLOWED;
        if ("conflict".equals(codeString))
          return CONFLICT;
        if ("gone".equals(codeString))
          return GONE;
        if ("preconditionFailed".equals(codeString))
          return PRECONDITIONFAILED;
        if ("unprocessable".equals(codeString))
          return UNPROCESSABLE;
        throw new Exception("Unknown AssertionResponseTypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OKAY: return "okay";
            case CREATED: return "created";
            case NOCONTENT: return "noContent";
            case NOTMODIFIED: return "notModified";
            case BAD: return "bad";
            case FORBIDDEN: return "forbidden";
            case NOTFOUND: return "notFound";
            case METHODNOTALLOWED: return "methodNotAllowed";
            case CONFLICT: return "conflict";
            case GONE: return "gone";
            case PRECONDITIONFAILED: return "preconditionFailed";
            case UNPROCESSABLE: return "unprocessable";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case OKAY: return "http://hl7.org/fhir/assert-response-code-types";
            case CREATED: return "http://hl7.org/fhir/assert-response-code-types";
            case NOCONTENT: return "http://hl7.org/fhir/assert-response-code-types";
            case NOTMODIFIED: return "http://hl7.org/fhir/assert-response-code-types";
            case BAD: return "http://hl7.org/fhir/assert-response-code-types";
            case FORBIDDEN: return "http://hl7.org/fhir/assert-response-code-types";
            case NOTFOUND: return "http://hl7.org/fhir/assert-response-code-types";
            case METHODNOTALLOWED: return "http://hl7.org/fhir/assert-response-code-types";
            case CONFLICT: return "http://hl7.org/fhir/assert-response-code-types";
            case GONE: return "http://hl7.org/fhir/assert-response-code-types";
            case PRECONDITIONFAILED: return "http://hl7.org/fhir/assert-response-code-types";
            case UNPROCESSABLE: return "http://hl7.org/fhir/assert-response-code-types";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OKAY: return "Response code is 200.";
            case CREATED: return "Response code is 201.";
            case NOCONTENT: return "Response code is 204.";
            case NOTMODIFIED: return "Response code is 304.";
            case BAD: return "Response code is 400.";
            case FORBIDDEN: return "Response code is 403.";
            case NOTFOUND: return "Response code is 404.";
            case METHODNOTALLOWED: return "Response code is 405.";
            case CONFLICT: return "Response code is 409.";
            case GONE: return "Response code is 410.";
            case PRECONDITIONFAILED: return "Response code is 412.";
            case UNPROCESSABLE: return "Response code is 422.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OKAY: return "okay";
            case CREATED: return "created";
            case NOCONTENT: return "noContent";
            case NOTMODIFIED: return "notModified";
            case BAD: return "bad";
            case FORBIDDEN: return "forbidden";
            case NOTFOUND: return "notFound";
            case METHODNOTALLOWED: return "methodNotAllowed";
            case CONFLICT: return "conflict";
            case GONE: return "gone";
            case PRECONDITIONFAILED: return "preconditionFailed";
            case UNPROCESSABLE: return "unprocessable";
            default: return "?";
          }
        }
    }

  public static class AssertionResponseTypesEnumFactory implements EnumFactory<AssertionResponseTypes> {
    public AssertionResponseTypes fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("okay".equals(codeString))
          return AssertionResponseTypes.OKAY;
        if ("created".equals(codeString))
          return AssertionResponseTypes.CREATED;
        if ("noContent".equals(codeString))
          return AssertionResponseTypes.NOCONTENT;
        if ("notModified".equals(codeString))
          return AssertionResponseTypes.NOTMODIFIED;
        if ("bad".equals(codeString))
          return AssertionResponseTypes.BAD;
        if ("forbidden".equals(codeString))
          return AssertionResponseTypes.FORBIDDEN;
        if ("notFound".equals(codeString))
          return AssertionResponseTypes.NOTFOUND;
        if ("methodNotAllowed".equals(codeString))
          return AssertionResponseTypes.METHODNOTALLOWED;
        if ("conflict".equals(codeString))
          return AssertionResponseTypes.CONFLICT;
        if ("gone".equals(codeString))
          return AssertionResponseTypes.GONE;
        if ("preconditionFailed".equals(codeString))
          return AssertionResponseTypes.PRECONDITIONFAILED;
        if ("unprocessable".equals(codeString))
          return AssertionResponseTypes.UNPROCESSABLE;
        throw new IllegalArgumentException("Unknown AssertionResponseTypes code '"+codeString+"'");
        }
    public String toCode(AssertionResponseTypes code) {
      if (code == AssertionResponseTypes.OKAY)
        return "okay";
      if (code == AssertionResponseTypes.CREATED)
        return "created";
      if (code == AssertionResponseTypes.NOCONTENT)
        return "noContent";
      if (code == AssertionResponseTypes.NOTMODIFIED)
        return "notModified";
      if (code == AssertionResponseTypes.BAD)
        return "bad";
      if (code == AssertionResponseTypes.FORBIDDEN)
        return "forbidden";
      if (code == AssertionResponseTypes.NOTFOUND)
        return "notFound";
      if (code == AssertionResponseTypes.METHODNOTALLOWED)
        return "methodNotAllowed";
      if (code == AssertionResponseTypes.CONFLICT)
        return "conflict";
      if (code == AssertionResponseTypes.GONE)
        return "gone";
      if (code == AssertionResponseTypes.PRECONDITIONFAILED)
        return "preconditionFailed";
      if (code == AssertionResponseTypes.UNPROCESSABLE)
        return "unprocessable";
      return "?";
      }
    }

    @Block()
    public static class TestScriptContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the Test Script.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the Test Script." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /*
     * Constructor
     */
      public TestScriptContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the Test Script.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the Test Script.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public TestScriptContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the Test Script.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the Test Script.
         */
        public TestScriptContactComponent setName(String value) { 
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
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
         */
        public List<ContactPoint> getTelecom() { 
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          return this.telecom;
        }

        public boolean hasTelecom() { 
          if (this.telecom == null)
            return false;
          for (ContactPoint item : this.telecom)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #telecom} (Contact details for individual (if a name was provided) or the publisher.)
         */
    // syntactic sugar
        public ContactPoint addTelecom() { //3
          ContactPoint t = new ContactPoint();
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the Test Script.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public TestScriptContactComponent copy() {
        TestScriptContactComponent dst = new TestScriptContactComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptContactComponent))
          return false;
        TestScriptContactComponent o = (TestScriptContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptContactComponent))
          return false;
        TestScriptContactComponent o = (TestScriptContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptMetadataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A link to the FHIR specification that this test is covering.
         */
        @Child(name = "link", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Links to the FHIR specification", formalDefinition="A link to the FHIR specification that this test is covering." )
        protected List<TestScriptMetadataLinkComponent> link;

        /**
         * Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.
         */
        @Child(name = "capabilities", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Capabiltities that are assumed to function correctly on the FHIR server being tested", formalDefinition="Capabilties that must exist and is assumed to function correctly on the FHIR server being tested." )
        protected List<TestScriptMetadataCapabilitiesComponent> capabilities;

        private static final long serialVersionUID = -419664800L;

    /*
     * Constructor
     */
      public TestScriptMetadataComponent() {
        super();
      }

        /**
         * @return {@link #link} (A link to the FHIR specification that this test is covering.)
         */
        public List<TestScriptMetadataLinkComponent> getLink() { 
          if (this.link == null)
            this.link = new ArrayList<TestScriptMetadataLinkComponent>();
          return this.link;
        }

        public boolean hasLink() { 
          if (this.link == null)
            return false;
          for (TestScriptMetadataLinkComponent item : this.link)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #link} (A link to the FHIR specification that this test is covering.)
         */
    // syntactic sugar
        public TestScriptMetadataLinkComponent addLink() { //3
          TestScriptMetadataLinkComponent t = new TestScriptMetadataLinkComponent();
          if (this.link == null)
            this.link = new ArrayList<TestScriptMetadataLinkComponent>();
          this.link.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptMetadataComponent addLink(TestScriptMetadataLinkComponent t) { //3
          if (t == null)
            return this;
          if (this.link == null)
            this.link = new ArrayList<TestScriptMetadataLinkComponent>();
          this.link.add(t);
          return this;
        }

        /**
         * @return {@link #capabilities} (Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.)
         */
        public List<TestScriptMetadataCapabilitiesComponent> getCapabilities() { 
          if (this.capabilities == null)
            this.capabilities = new ArrayList<TestScriptMetadataCapabilitiesComponent>();
          return this.capabilities;
        }

        public boolean hasCapabilities() { 
          if (this.capabilities == null)
            return false;
          for (TestScriptMetadataCapabilitiesComponent item : this.capabilities)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #capabilities} (Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.)
         */
    // syntactic sugar
        public TestScriptMetadataCapabilitiesComponent addCapabilities() { //3
          TestScriptMetadataCapabilitiesComponent t = new TestScriptMetadataCapabilitiesComponent();
          if (this.capabilities == null)
            this.capabilities = new ArrayList<TestScriptMetadataCapabilitiesComponent>();
          this.capabilities.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptMetadataComponent addCapabilities(TestScriptMetadataCapabilitiesComponent t) { //3
          if (t == null)
            return this;
          if (this.capabilities == null)
            this.capabilities = new ArrayList<TestScriptMetadataCapabilitiesComponent>();
          this.capabilities.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("link", "", "A link to the FHIR specification that this test is covering.", 0, java.lang.Integer.MAX_VALUE, link));
          childrenList.add(new Property("capabilities", "", "Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, capabilities));
        }

      public TestScriptMetadataComponent copy() {
        TestScriptMetadataComponent dst = new TestScriptMetadataComponent();
        copyValues(dst);
        if (link != null) {
          dst.link = new ArrayList<TestScriptMetadataLinkComponent>();
          for (TestScriptMetadataLinkComponent i : link)
            dst.link.add(i.copy());
        };
        if (capabilities != null) {
          dst.capabilities = new ArrayList<TestScriptMetadataCapabilitiesComponent>();
          for (TestScriptMetadataCapabilitiesComponent i : capabilities)
            dst.capabilities.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptMetadataComponent))
          return false;
        TestScriptMetadataComponent o = (TestScriptMetadataComponent) other;
        return compareDeep(link, o.link, true) && compareDeep(capabilities, o.capabilities, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptMetadataComponent))
          return false;
        TestScriptMetadataComponent o = (TestScriptMetadataComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (link == null || link.isEmpty()) && (capabilities == null || capabilities.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptMetadataLinkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * URL to a particular requirement or feature within the FHIR specification.
         */
        @Child(name = "url", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="URL to the specification", formalDefinition="URL to a particular requirement or feature within the FHIR specification." )
        protected UriType url;

        /**
         * Short description of the link.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short description", formalDefinition="Short description of the link." )
        protected StringType description;

        private static final long serialVersionUID = 213372298L;

    /*
     * Constructor
     */
      public TestScriptMetadataLinkComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptMetadataLinkComponent(UriType url) {
        super();
        this.url = url;
      }

        /**
         * @return {@link #url} (URL to a particular requirement or feature within the FHIR specification.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataLinkComponent.url");
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
        public TestScriptMetadataLinkComponent setUrlElement(UriType value) { 
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
        public TestScriptMetadataLinkComponent setUrl(String value) { 
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
              throw new Error("Attempt to auto-create TestScriptMetadataLinkComponent.description");
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
        public TestScriptMetadataLinkComponent setDescriptionElement(StringType value) { 
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
        public TestScriptMetadataLinkComponent setDescription(String value) { 
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

      public TestScriptMetadataLinkComponent copy() {
        TestScriptMetadataLinkComponent dst = new TestScriptMetadataLinkComponent();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptMetadataLinkComponent))
          return false;
        TestScriptMetadataLinkComponent o = (TestScriptMetadataLinkComponent) other;
        return compareDeep(url, o.url, true) && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptMetadataLinkComponent))
          return false;
        TestScriptMetadataLinkComponent o = (TestScriptMetadataLinkComponent) other;
        return compareValues(url, o.url, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (description == null || description.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptMetadataCapabilitiesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The capabilities required of the server in order for this test script to execute.
         */
        @Child(name = "required", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Required capabilities", formalDefinition="The capabilities required of the server in order for this test script to execute." )
        protected BooleanType required;

        /**
         * Whether or not the capabilities are primarily getting validated by this test script.
         */
        @Child(name = "validated", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Validated capabilities", formalDefinition="Whether or not the capabilities are primarily getting validated by this test script." )
        protected BooleanType validated;

        /**
         * Description of the capabilities that this test script is requiring the server to support.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The description of the capabilities", formalDefinition="Description of the capabilities that this test script is requiring the server to support." )
        protected StringType description;

        /**
         * Which server these requirements apply to.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Which server these requirements apply to", formalDefinition="Which server these requirements apply to." )
        protected IntegerType destination;

        /**
         * Links to the FHIR specification that describes this interaction and the resources involved in more detail.
         */
        @Child(name = "link", type = {UriType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Links to the FHIR specification", formalDefinition="Links to the FHIR specification that describes this interaction and the resources involved in more detail." )
        protected List<UriType> link;

        /**
         * Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.
         */
        @Child(name = "conformance", type = {Conformance.class}, order=6, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Required Conformance", formalDefinition="Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped." )
        protected Reference conformance;

        /**
         * The actual object that is the target of the reference (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        protected Conformance conformanceTarget;

        private static final long serialVersionUID = 1318523355L;

    /*
     * Constructor
     */
      public TestScriptMetadataCapabilitiesComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptMetadataCapabilitiesComponent(Reference conformance) {
        super();
        this.conformance = conformance;
      }

        /**
         * @return {@link #required} (The capabilities required of the server in order for this test script to execute.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public BooleanType getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilitiesComponent.required");
            else if (Configuration.doAutoCreate())
              this.required = new BooleanType(); // bb
          return this.required;
        }

        public boolean hasRequiredElement() { 
          return this.required != null && !this.required.isEmpty();
        }

        public boolean hasRequired() { 
          return this.required != null && !this.required.isEmpty();
        }

        /**
         * @param value {@link #required} (The capabilities required of the server in order for this test script to execute.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public TestScriptMetadataCapabilitiesComponent setRequiredElement(BooleanType value) { 
          this.required = value;
          return this;
        }

        /**
         * @return The capabilities required of the server in order for this test script to execute.
         */
        public boolean getRequired() { 
          return this.required == null || this.required.isEmpty() ? false : this.required.getValue();
        }

        /**
         * @param value The capabilities required of the server in order for this test script to execute.
         */
        public TestScriptMetadataCapabilitiesComponent setRequired(boolean value) { 
            if (this.required == null)
              this.required = new BooleanType();
            this.required.setValue(value);
          return this;
        }

        /**
         * @return {@link #validated} (Whether or not the capabilities are primarily getting validated by this test script.). This is the underlying object with id, value and extensions. The accessor "getValidated" gives direct access to the value
         */
        public BooleanType getValidatedElement() { 
          if (this.validated == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilitiesComponent.validated");
            else if (Configuration.doAutoCreate())
              this.validated = new BooleanType(); // bb
          return this.validated;
        }

        public boolean hasValidatedElement() { 
          return this.validated != null && !this.validated.isEmpty();
        }

        public boolean hasValidated() { 
          return this.validated != null && !this.validated.isEmpty();
        }

        /**
         * @param value {@link #validated} (Whether or not the capabilities are primarily getting validated by this test script.). This is the underlying object with id, value and extensions. The accessor "getValidated" gives direct access to the value
         */
        public TestScriptMetadataCapabilitiesComponent setValidatedElement(BooleanType value) { 
          this.validated = value;
          return this;
        }

        /**
         * @return Whether or not the capabilities are primarily getting validated by this test script.
         */
        public boolean getValidated() { 
          return this.validated == null || this.validated.isEmpty() ? false : this.validated.getValue();
        }

        /**
         * @param value Whether or not the capabilities are primarily getting validated by this test script.
         */
        public TestScriptMetadataCapabilitiesComponent setValidated(boolean value) { 
            if (this.validated == null)
              this.validated = new BooleanType();
            this.validated.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Description of the capabilities that this test script is requiring the server to support.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilitiesComponent.description");
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
         * @param value {@link #description} (Description of the capabilities that this test script is requiring the server to support.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestScriptMetadataCapabilitiesComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Description of the capabilities that this test script is requiring the server to support.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Description of the capabilities that this test script is requiring the server to support.
         */
        public TestScriptMetadataCapabilitiesComponent setDescription(String value) { 
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
         * @return {@link #destination} (Which server these requirements apply to.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilitiesComponent.destination");
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
         * @param value {@link #destination} (Which server these requirements apply to.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public TestScriptMetadataCapabilitiesComponent setDestinationElement(IntegerType value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return Which server these requirements apply to.
         */
        public int getDestination() { 
          return this.destination == null || this.destination.isEmpty() ? 0 : this.destination.getValue();
        }

        /**
         * @param value Which server these requirements apply to.
         */
        public TestScriptMetadataCapabilitiesComponent setDestination(int value) { 
            if (this.destination == null)
              this.destination = new IntegerType();
            this.destination.setValue(value);
          return this;
        }

        /**
         * @return {@link #link} (Links to the FHIR specification that describes this interaction and the resources involved in more detail.)
         */
        public List<UriType> getLink() { 
          if (this.link == null)
            this.link = new ArrayList<UriType>();
          return this.link;
        }

        public boolean hasLink() { 
          if (this.link == null)
            return false;
          for (UriType item : this.link)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #link} (Links to the FHIR specification that describes this interaction and the resources involved in more detail.)
         */
    // syntactic sugar
        public UriType addLinkElement() {//2 
          UriType t = new UriType();
          if (this.link == null)
            this.link = new ArrayList<UriType>();
          this.link.add(t);
          return t;
        }

        /**
         * @param value {@link #link} (Links to the FHIR specification that describes this interaction and the resources involved in more detail.)
         */
        public TestScriptMetadataCapabilitiesComponent addLink(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.link == null)
            this.link = new ArrayList<UriType>();
          this.link.add(t);
          return this;
        }

        /**
         * @param value {@link #link} (Links to the FHIR specification that describes this interaction and the resources involved in more detail.)
         */
        public boolean hasLink(String value) { 
          if (this.link == null)
            return false;
          for (UriType v : this.link)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #conformance} (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        public Reference getConformance() { 
          if (this.conformance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilitiesComponent.conformance");
            else if (Configuration.doAutoCreate())
              this.conformance = new Reference(); // cc
          return this.conformance;
        }

        public boolean hasConformance() { 
          return this.conformance != null && !this.conformance.isEmpty();
        }

        /**
         * @param value {@link #conformance} (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        public TestScriptMetadataCapabilitiesComponent setConformance(Reference value) { 
          this.conformance = value;
          return this;
        }

        /**
         * @return {@link #conformance} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        public Conformance getConformanceTarget() { 
          if (this.conformanceTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilitiesComponent.conformance");
            else if (Configuration.doAutoCreate())
              this.conformanceTarget = new Conformance(); // aa
          return this.conformanceTarget;
        }

        /**
         * @param value {@link #conformance} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        public TestScriptMetadataCapabilitiesComponent setConformanceTarget(Conformance value) { 
          this.conformanceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("required", "boolean", "The capabilities required of the server in order for this test script to execute.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("validated", "boolean", "Whether or not the capabilities are primarily getting validated by this test script.", 0, java.lang.Integer.MAX_VALUE, validated));
          childrenList.add(new Property("description", "string", "Description of the capabilities that this test script is requiring the server to support.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("destination", "integer", "Which server these requirements apply to.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("link", "uri", "Links to the FHIR specification that describes this interaction and the resources involved in more detail.", 0, java.lang.Integer.MAX_VALUE, link));
          childrenList.add(new Property("conformance", "Reference(Conformance)", "Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.", 0, java.lang.Integer.MAX_VALUE, conformance));
        }

      public TestScriptMetadataCapabilitiesComponent copy() {
        TestScriptMetadataCapabilitiesComponent dst = new TestScriptMetadataCapabilitiesComponent();
        copyValues(dst);
        dst.required = required == null ? null : required.copy();
        dst.validated = validated == null ? null : validated.copy();
        dst.description = description == null ? null : description.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (link != null) {
          dst.link = new ArrayList<UriType>();
          for (UriType i : link)
            dst.link.add(i.copy());
        };
        dst.conformance = conformance == null ? null : conformance.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptMetadataCapabilitiesComponent))
          return false;
        TestScriptMetadataCapabilitiesComponent o = (TestScriptMetadataCapabilitiesComponent) other;
        return compareDeep(required, o.required, true) && compareDeep(validated, o.validated, true) && compareDeep(description, o.description, true)
           && compareDeep(destination, o.destination, true) && compareDeep(link, o.link, true) && compareDeep(conformance, o.conformance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptMetadataCapabilitiesComponent))
          return false;
        TestScriptMetadataCapabilitiesComponent o = (TestScriptMetadataCapabilitiesComponent) other;
        return compareValues(required, o.required, true) && compareValues(validated, o.validated, true) && compareValues(description, o.description, true)
           && compareValues(destination, o.destination, true) && compareValues(link, o.link, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (required == null || required.isEmpty()) && (validated == null || validated.isEmpty())
           && (description == null || description.isEmpty()) && (destination == null || destination.isEmpty())
           && (link == null || link.isEmpty()) && (conformance == null || conformance.isEmpty());
      }

  }

    @Block()
    public static class TestScriptFixtureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.
         */
        @Child(name = "autocreate", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not to implicitly create the fixture during setup", formalDefinition="Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section." )
        protected BooleanType autocreate;

        /**
         * Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.
         */
        @Child(name = "autodelete", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not to implicitly delete the fixture during teardown", formalDefinition="Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section." )
        protected BooleanType autodelete;

        /**
         * Reference to the resource (containing the contents of the resource needed for operations).
         */
        @Child(name = "resource", type = {}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference of the resource", formalDefinition="Reference to the resource (containing the contents of the resource needed for operations)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (Reference to the resource (containing the contents of the resource needed for operations).)
         */
        protected Resource resourceTarget;

        private static final long serialVersionUID = 1110683307L;

    /*
     * Constructor
     */
      public TestScriptFixtureComponent() {
        super();
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

        /**
         * @return {@link #resource} (Reference to the resource (containing the contents of the resource needed for operations).)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptFixtureComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new Reference(); // cc
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (Reference to the resource (containing the contents of the resource needed for operations).)
         */
        public TestScriptFixtureComponent setResource(Reference value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the resource (containing the contents of the resource needed for operations).)
         */
        public Resource getResourceTarget() { 
          return this.resourceTarget;
        }

        /**
         * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the resource (containing the contents of the resource needed for operations).)
         */
        public TestScriptFixtureComponent setResourceTarget(Resource value) { 
          this.resourceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("autocreate", "boolean", "Whether or not to implicitly create the fixture during setup. If true, the fixture is automatically created on each server being tested during setup, therefore no create operation is required for this fixture in the TestScript.setup section.", 0, java.lang.Integer.MAX_VALUE, autocreate));
          childrenList.add(new Property("autodelete", "boolean", "Whether or not to implicitly delete the fixture during teardown If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.", 0, java.lang.Integer.MAX_VALUE, autodelete));
          childrenList.add(new Property("resource", "Reference(Any)", "Reference to the resource (containing the contents of the resource needed for operations).", 0, java.lang.Integer.MAX_VALUE, resource));
        }

      public TestScriptFixtureComponent copy() {
        TestScriptFixtureComponent dst = new TestScriptFixtureComponent();
        copyValues(dst);
        dst.autocreate = autocreate == null ? null : autocreate.copy();
        dst.autodelete = autodelete == null ? null : autodelete.copy();
        dst.resource = resource == null ? null : resource.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptFixtureComponent))
          return false;
        TestScriptFixtureComponent o = (TestScriptFixtureComponent) other;
        return compareDeep(autocreate, o.autocreate, true) && compareDeep(autodelete, o.autodelete, true)
           && compareDeep(resource, o.resource, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptFixtureComponent))
          return false;
        TestScriptFixtureComponent o = (TestScriptFixtureComponent) other;
        return compareValues(autocreate, o.autocreate, true) && compareValues(autodelete, o.autodelete, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (autocreate == null || autocreate.isEmpty()) && (autodelete == null || autodelete.isEmpty())
           && (resource == null || resource.isEmpty());
      }

  }

    @Block()
    public static class TestScriptVariableComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Variable name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Variable name", formalDefinition="Variable name." )
        protected StringType name;

        /**
         * Will be used to grab the header field value from the headers that sourceId is pointing to.
         */
        @Child(name = "headerField", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Header field name", formalDefinition="Will be used to grab the header field value from the headers that sourceId is pointing to." )
        protected StringType headerField;

        /**
         * XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.
         */
        @Child(name = "path", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="XPath or JSONPath against the fixture body", formalDefinition="XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both." )
        protected StringType path;

        /**
         * Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id", formalDefinition="Fixture to evaluate the XPath/JSONPath expression or the headerField  against." )
        protected IdType sourceId;

        private static final long serialVersionUID = 1128806685L;

    /*
     * Constructor
     */
      public TestScriptVariableComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptVariableComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Variable name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.name");
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
         * @param value {@link #name} (Variable name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public TestScriptVariableComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Variable name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Variable name.
         */
        public TestScriptVariableComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #headerField} (Will be used to grab the header field value from the headers that sourceId is pointing to.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
         */
        public StringType getHeaderFieldElement() { 
          if (this.headerField == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.headerField");
            else if (Configuration.doAutoCreate())
              this.headerField = new StringType(); // bb
          return this.headerField;
        }

        public boolean hasHeaderFieldElement() { 
          return this.headerField != null && !this.headerField.isEmpty();
        }

        public boolean hasHeaderField() { 
          return this.headerField != null && !this.headerField.isEmpty();
        }

        /**
         * @param value {@link #headerField} (Will be used to grab the header field value from the headers that sourceId is pointing to.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
         */
        public TestScriptVariableComponent setHeaderFieldElement(StringType value) { 
          this.headerField = value;
          return this;
        }

        /**
         * @return Will be used to grab the header field value from the headers that sourceId is pointing to.
         */
        public String getHeaderField() { 
          return this.headerField == null ? null : this.headerField.getValue();
        }

        /**
         * @param value Will be used to grab the header field value from the headers that sourceId is pointing to.
         */
        public TestScriptVariableComponent setHeaderField(String value) { 
          if (Utilities.noString(value))
            this.headerField = null;
          else {
            if (this.headerField == null)
              this.headerField = new StringType();
            this.headerField.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #path} (XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public TestScriptVariableComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.
         */
        public TestScriptVariableComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #sourceId} (Fixture to evaluate the XPath/JSONPath expression or the headerField  against.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public IdType getSourceIdElement() { 
          if (this.sourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.sourceId");
            else if (Configuration.doAutoCreate())
              this.sourceId = new IdType(); // bb
          return this.sourceId;
        }

        public boolean hasSourceIdElement() { 
          return this.sourceId != null && !this.sourceId.isEmpty();
        }

        public boolean hasSourceId() { 
          return this.sourceId != null && !this.sourceId.isEmpty();
        }

        /**
         * @param value {@link #sourceId} (Fixture to evaluate the XPath/JSONPath expression or the headerField  against.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public TestScriptVariableComponent setSourceIdElement(IdType value) { 
          this.sourceId = value;
          return this;
        }

        /**
         * @return Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        public String getSourceId() { 
          return this.sourceId == null ? null : this.sourceId.getValue();
        }

        /**
         * @param value Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        public TestScriptVariableComponent setSourceId(String value) { 
          if (Utilities.noString(value))
            this.sourceId = null;
          else {
            if (this.sourceId == null)
              this.sourceId = new IdType();
            this.sourceId.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Variable name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("headerField", "string", "Will be used to grab the header field value from the headers that sourceId is pointing to.", 0, java.lang.Integer.MAX_VALUE, headerField));
          childrenList.add(new Property("path", "string", "XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("sourceId", "id", "Fixture to evaluate the XPath/JSONPath expression or the headerField  against.", 0, java.lang.Integer.MAX_VALUE, sourceId));
        }

      public TestScriptVariableComponent copy() {
        TestScriptVariableComponent dst = new TestScriptVariableComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.headerField = headerField == null ? null : headerField.copy();
        dst.path = path == null ? null : path.copy();
        dst.sourceId = sourceId == null ? null : sourceId.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptVariableComponent))
          return false;
        TestScriptVariableComponent o = (TestScriptVariableComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(headerField, o.headerField, true) && compareDeep(path, o.path, true)
           && compareDeep(sourceId, o.sourceId, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptVariableComponent))
          return false;
        TestScriptVariableComponent o = (TestScriptVariableComponent) other;
        return compareValues(name, o.name, true) && compareValues(headerField, o.headerField, true) && compareValues(path, o.path, true)
           && compareValues(sourceId, o.sourceId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (headerField == null || headerField.isEmpty())
           && (path == null || path.isEmpty()) && (sourceId == null || sourceId.isEmpty());
      }

  }

    @Block()
    public static class TestScriptSetupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.
         */
        @Child(name = "metadata", type = {TestScriptMetadataComponent.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Capabiltities that are assumed to function correctly on the FHIR server being tested", formalDefinition="Capabilties that must exist and is assumed to function correctly on the FHIR server being tested." )
        protected TestScriptMetadataComponent metadata;

        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Action", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<TestScriptSetupActionComponent> action;

        private static final long serialVersionUID = -1836543723L;

    /*
     * Constructor
     */
      public TestScriptSetupComponent() {
        super();
      }

        /**
         * @return {@link #metadata} (Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.)
         */
        public TestScriptMetadataComponent getMetadata() { 
          if (this.metadata == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupComponent.metadata");
            else if (Configuration.doAutoCreate())
              this.metadata = new TestScriptMetadataComponent(); // cc
          return this.metadata;
        }

        public boolean hasMetadata() { 
          return this.metadata != null && !this.metadata.isEmpty();
        }

        /**
         * @param value {@link #metadata} (Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.)
         */
        public TestScriptSetupComponent setMetadata(TestScriptMetadataComponent value) { 
          this.metadata = value;
          return this;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
        public List<TestScriptSetupActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<TestScriptSetupActionComponent>();
          return this.action;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (TestScriptSetupActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
    // syntactic sugar
        public TestScriptSetupActionComponent addAction() { //3
          TestScriptSetupActionComponent t = new TestScriptSetupActionComponent();
          if (this.action == null)
            this.action = new ArrayList<TestScriptSetupActionComponent>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptSetupComponent addAction(TestScriptSetupActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<TestScriptSetupActionComponent>();
          this.action.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("metadata", "@TestScript.metadata", "Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, metadata));
          childrenList.add(new Property("action", "", "Action would contain either an operation or an assertion.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      public TestScriptSetupComponent copy() {
        TestScriptSetupComponent dst = new TestScriptSetupComponent();
        copyValues(dst);
        dst.metadata = metadata == null ? null : metadata.copy();
        if (action != null) {
          dst.action = new ArrayList<TestScriptSetupActionComponent>();
          for (TestScriptSetupActionComponent i : action)
            dst.action.add(i.copy());
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
        return compareDeep(metadata, o.metadata, true) && compareDeep(action, o.action, true);
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
        return super.isEmpty() && (metadata == null || metadata.isEmpty()) && (action == null || action.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptSetupActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An operation.
         */
        @Child(name = "operation", type = {}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="An operation", formalDefinition="An operation." )
        protected TestScriptSetupActionOperationComponent operation;

        /**
         * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
         */
        @Child(name = "assert", type = {}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Assertion", formalDefinition="Evaluates the results of previous operations to determine if the server under test behaves appropriately." )
        protected TestScriptSetupActionAssertComponent assert_;

        private static final long serialVersionUID = 1411550037L;

    /*
     * Constructor
     */
      public TestScriptSetupActionComponent() {
        super();
      }

        /**
         * @return {@link #operation} (An operation.)
         */
        public TestScriptSetupActionOperationComponent getOperation() { 
          if (this.operation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionComponent.operation");
            else if (Configuration.doAutoCreate())
              this.operation = new TestScriptSetupActionOperationComponent(); // cc
          return this.operation;
        }

        public boolean hasOperation() { 
          return this.operation != null && !this.operation.isEmpty();
        }

        /**
         * @param value {@link #operation} (An operation.)
         */
        public TestScriptSetupActionComponent setOperation(TestScriptSetupActionOperationComponent value) { 
          this.operation = value;
          return this;
        }

        /**
         * @return {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
         */
        public TestScriptSetupActionAssertComponent getAssert() { 
          if (this.assert_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionComponent.assert_");
            else if (Configuration.doAutoCreate())
              this.assert_ = new TestScriptSetupActionAssertComponent(); // cc
          return this.assert_;
        }

        public boolean hasAssert() { 
          return this.assert_ != null && !this.assert_.isEmpty();
        }

        /**
         * @param value {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
         */
        public TestScriptSetupActionComponent setAssert(TestScriptSetupActionAssertComponent value) { 
          this.assert_ = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "", "An operation.", 0, java.lang.Integer.MAX_VALUE, operation));
          childrenList.add(new Property("assert", "", "Evaluates the results of previous operations to determine if the server under test behaves appropriately.", 0, java.lang.Integer.MAX_VALUE, assert_));
        }

      public TestScriptSetupActionComponent copy() {
        TestScriptSetupActionComponent dst = new TestScriptSetupActionComponent();
        copyValues(dst);
        dst.operation = operation == null ? null : operation.copy();
        dst.assert_ = assert_ == null ? null : assert_.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptSetupActionComponent))
          return false;
        TestScriptSetupActionComponent o = (TestScriptSetupActionComponent) other;
        return compareDeep(operation, o.operation, true) && compareDeep(assert_, o.assert_, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptSetupActionComponent))
          return false;
        TestScriptSetupActionComponent o = (TestScriptSetupActionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (operation == null || operation.isEmpty()) && (assert_ == null || assert_.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptSetupActionOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Server interaction or operation type.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The operation type that will be executed", formalDefinition="Server interaction or operation type." )
        protected Coding type;

        /**
         * The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.
         */
        @Child(name = "resource", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource type", formalDefinition="The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html." )
        protected CodeType resource;

        /**
         * The label would be used for tracking/logging purposes by test engines.
         */
        @Child(name = "label", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Operation label", formalDefinition="The label would be used for tracking/logging purposes by test engines." )
        protected StringType label;

        /**
         * The description would be used by test engines for tracking and reporting purposes.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Operation description", formalDefinition="The description would be used by test engines for tracking and reporting purposes." )
        protected StringType description;

        /**
         * The content-type or mime-type to use for RESTful operation in the 'Accept' header.
         */
        @Child(name = "accept", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Accept' header." )
        protected Enumeration<ContentType> accept;

        /**
         * The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Content-Type' header." )
        protected Enumeration<ContentType> contentType;

        /**
         * Which server to perform the operation on.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Which server to perform the operation on", formalDefinition="Which server to perform the operation on." )
        protected IntegerType destination;

        /**
         * Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.
         */
        @Child(name = "encodeRequestUrl", type = {BooleanType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not to send the request url in encoded format", formalDefinition="Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths." )
        protected BooleanType encodeRequestUrl;

        /**
         * Path plus parameters after [type].  Used to set parts of the request URL explicitly.
         */
        @Child(name = "params", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Params", formalDefinition="Path plus parameters after [type].  Used to set parts of the request URL explicitly." )
        protected StringType params;

        /**
         * Header elements would be used to set HTTP headers.
         */
        @Child(name = "requestHeader", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Each operation can have one ore more header elements", formalDefinition="Header elements would be used to set HTTP headers." )
        protected List<TestScriptSetupActionOperationRequestHeaderComponent> requestHeader;

        /**
         * The fixture id (maybe new) to map to the response.
         */
        @Child(name = "responseId", type = {IdType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Response Id", formalDefinition="The fixture id (maybe new) to map to the response." )
        protected IdType responseId;

        /**
         * The id of the fixture used as the body of a PUT or POST request.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of body for PUT and POST requests", formalDefinition="The id of the fixture used as the body of a PUT or POST request." )
        protected IdType sourceId;

        /**
         * Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.
         */
        @Child(name = "targetId", type = {IdType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of fixture used for extracting the [id],  [type], and [vid] for GET requests", formalDefinition="Id of fixture used for extracting the [id],  [type], and [vid] for GET requests." )
        protected IdType targetId;

        /**
         * Complete request URL.
         */
        @Child(name = "url", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Request URL", formalDefinition="Complete request URL." )
        protected StringType url;

        private static final long serialVersionUID = -590188078L;

    /*
     * Constructor
     */
      public TestScriptSetupActionOperationComponent() {
        super();
      }

        /**
         * @return {@link #type} (Server interaction or operation type.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Server interaction or operation type.)
         */
        public TestScriptSetupActionOperationComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public CodeType getResourceElement() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new CodeType(); // bb
          return this.resource;
        }

        public boolean hasResourceElement() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setResourceElement(CodeType value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.
         */
        public String getResource() { 
          return this.resource == null ? null : this.resource.getValue();
        }

        /**
         * @param value The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.
         */
        public TestScriptSetupActionOperationComponent setResource(String value) { 
          if (Utilities.noString(value))
            this.resource = null;
          else {
            if (this.resource == null)
              this.resource = new CodeType();
            this.resource.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #label} (The label would be used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.label");
            else if (Configuration.doAutoCreate())
              this.label = new StringType(); // bb
          return this.label;
        }

        public boolean hasLabelElement() { 
          return this.label != null && !this.label.isEmpty();
        }

        public boolean hasLabel() { 
          return this.label != null && !this.label.isEmpty();
        }

        /**
         * @param value {@link #label} (The label would be used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setLabelElement(StringType value) { 
          this.label = value;
          return this;
        }

        /**
         * @return The label would be used for tracking/logging purposes by test engines.
         */
        public String getLabel() { 
          return this.label == null ? null : this.label.getValue();
        }

        /**
         * @param value The label would be used for tracking/logging purposes by test engines.
         */
        public TestScriptSetupActionOperationComponent setLabel(String value) { 
          if (Utilities.noString(value))
            this.label = null;
          else {
            if (this.label == null)
              this.label = new StringType();
            this.label.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (The description would be used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.description");
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
         * @param value {@link #description} (The description would be used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The description would be used by test engines for tracking and reporting purposes.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The description would be used by test engines for tracking and reporting purposes.
         */
        public TestScriptSetupActionOperationComponent setDescription(String value) { 
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
         * @return {@link #accept} (The content-type or mime-type to use for RESTful operation in the 'Accept' header.). This is the underlying object with id, value and extensions. The accessor "getAccept" gives direct access to the value
         */
        public Enumeration<ContentType> getAcceptElement() { 
          if (this.accept == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.accept");
            else if (Configuration.doAutoCreate())
              this.accept = new Enumeration<ContentType>(new ContentTypeEnumFactory()); // bb
          return this.accept;
        }

        public boolean hasAcceptElement() { 
          return this.accept != null && !this.accept.isEmpty();
        }

        public boolean hasAccept() { 
          return this.accept != null && !this.accept.isEmpty();
        }

        /**
         * @param value {@link #accept} (The content-type or mime-type to use for RESTful operation in the 'Accept' header.). This is the underlying object with id, value and extensions. The accessor "getAccept" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setAcceptElement(Enumeration<ContentType> value) { 
          this.accept = value;
          return this;
        }

        /**
         * @return The content-type or mime-type to use for RESTful operation in the 'Accept' header.
         */
        public ContentType getAccept() { 
          return this.accept == null ? null : this.accept.getValue();
        }

        /**
         * @param value The content-type or mime-type to use for RESTful operation in the 'Accept' header.
         */
        public TestScriptSetupActionOperationComponent setAccept(ContentType value) { 
          if (value == null)
            this.accept = null;
          else {
            if (this.accept == null)
              this.accept = new Enumeration<ContentType>(new ContentTypeEnumFactory());
            this.accept.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #contentType} (The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public Enumeration<ContentType> getContentTypeElement() { 
          if (this.contentType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.contentType");
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
         * @param value {@link #contentType} (The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setContentTypeElement(Enumeration<ContentType> value) { 
          this.contentType = value;
          return this;
        }

        /**
         * @return The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        public ContentType getContentType() { 
          return this.contentType == null ? null : this.contentType.getValue();
        }

        /**
         * @param value The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        public TestScriptSetupActionOperationComponent setContentType(ContentType value) { 
          if (value == null)
            this.contentType = null;
          else {
            if (this.contentType == null)
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory());
            this.contentType.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #destination} (Which server to perform the operation on.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.destination");
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
        public TestScriptSetupActionOperationComponent setDestinationElement(IntegerType value) { 
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
        public TestScriptSetupActionOperationComponent setDestination(int value) { 
            if (this.destination == null)
              this.destination = new IntegerType();
            this.destination.setValue(value);
          return this;
        }

        /**
         * @return {@link #encodeRequestUrl} (Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.). This is the underlying object with id, value and extensions. The accessor "getEncodeRequestUrl" gives direct access to the value
         */
        public BooleanType getEncodeRequestUrlElement() { 
          if (this.encodeRequestUrl == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.encodeRequestUrl");
            else if (Configuration.doAutoCreate())
              this.encodeRequestUrl = new BooleanType(); // bb
          return this.encodeRequestUrl;
        }

        public boolean hasEncodeRequestUrlElement() { 
          return this.encodeRequestUrl != null && !this.encodeRequestUrl.isEmpty();
        }

        public boolean hasEncodeRequestUrl() { 
          return this.encodeRequestUrl != null && !this.encodeRequestUrl.isEmpty();
        }

        /**
         * @param value {@link #encodeRequestUrl} (Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.). This is the underlying object with id, value and extensions. The accessor "getEncodeRequestUrl" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setEncodeRequestUrlElement(BooleanType value) { 
          this.encodeRequestUrl = value;
          return this;
        }

        /**
         * @return Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.
         */
        public boolean getEncodeRequestUrl() { 
          return this.encodeRequestUrl == null || this.encodeRequestUrl.isEmpty() ? false : this.encodeRequestUrl.getValue();
        }

        /**
         * @param value Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.
         */
        public TestScriptSetupActionOperationComponent setEncodeRequestUrl(boolean value) { 
            if (this.encodeRequestUrl == null)
              this.encodeRequestUrl = new BooleanType();
            this.encodeRequestUrl.setValue(value);
          return this;
        }

        /**
         * @return {@link #params} (Path plus parameters after [type].  Used to set parts of the request URL explicitly.). This is the underlying object with id, value and extensions. The accessor "getParams" gives direct access to the value
         */
        public StringType getParamsElement() { 
          if (this.params == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.params");
            else if (Configuration.doAutoCreate())
              this.params = new StringType(); // bb
          return this.params;
        }

        public boolean hasParamsElement() { 
          return this.params != null && !this.params.isEmpty();
        }

        public boolean hasParams() { 
          return this.params != null && !this.params.isEmpty();
        }

        /**
         * @param value {@link #params} (Path plus parameters after [type].  Used to set parts of the request URL explicitly.). This is the underlying object with id, value and extensions. The accessor "getParams" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setParamsElement(StringType value) { 
          this.params = value;
          return this;
        }

        /**
         * @return Path plus parameters after [type].  Used to set parts of the request URL explicitly.
         */
        public String getParams() { 
          return this.params == null ? null : this.params.getValue();
        }

        /**
         * @param value Path plus parameters after [type].  Used to set parts of the request URL explicitly.
         */
        public TestScriptSetupActionOperationComponent setParams(String value) { 
          if (Utilities.noString(value))
            this.params = null;
          else {
            if (this.params == null)
              this.params = new StringType();
            this.params.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #requestHeader} (Header elements would be used to set HTTP headers.)
         */
        public List<TestScriptSetupActionOperationRequestHeaderComponent> getRequestHeader() { 
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<TestScriptSetupActionOperationRequestHeaderComponent>();
          return this.requestHeader;
        }

        public boolean hasRequestHeader() { 
          if (this.requestHeader == null)
            return false;
          for (TestScriptSetupActionOperationRequestHeaderComponent item : this.requestHeader)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #requestHeader} (Header elements would be used to set HTTP headers.)
         */
    // syntactic sugar
        public TestScriptSetupActionOperationRequestHeaderComponent addRequestHeader() { //3
          TestScriptSetupActionOperationRequestHeaderComponent t = new TestScriptSetupActionOperationRequestHeaderComponent();
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<TestScriptSetupActionOperationRequestHeaderComponent>();
          this.requestHeader.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptSetupActionOperationComponent addRequestHeader(TestScriptSetupActionOperationRequestHeaderComponent t) { //3
          if (t == null)
            return this;
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<TestScriptSetupActionOperationRequestHeaderComponent>();
          this.requestHeader.add(t);
          return this;
        }

        /**
         * @return {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public IdType getResponseIdElement() { 
          if (this.responseId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.responseId");
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
        public TestScriptSetupActionOperationComponent setResponseIdElement(IdType value) { 
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
        public TestScriptSetupActionOperationComponent setResponseId(String value) { 
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
         * @return {@link #sourceId} (The id of the fixture used as the body of a PUT or POST request.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public IdType getSourceIdElement() { 
          if (this.sourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.sourceId");
            else if (Configuration.doAutoCreate())
              this.sourceId = new IdType(); // bb
          return this.sourceId;
        }

        public boolean hasSourceIdElement() { 
          return this.sourceId != null && !this.sourceId.isEmpty();
        }

        public boolean hasSourceId() { 
          return this.sourceId != null && !this.sourceId.isEmpty();
        }

        /**
         * @param value {@link #sourceId} (The id of the fixture used as the body of a PUT or POST request.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setSourceIdElement(IdType value) { 
          this.sourceId = value;
          return this;
        }

        /**
         * @return The id of the fixture used as the body of a PUT or POST request.
         */
        public String getSourceId() { 
          return this.sourceId == null ? null : this.sourceId.getValue();
        }

        /**
         * @param value The id of the fixture used as the body of a PUT or POST request.
         */
        public TestScriptSetupActionOperationComponent setSourceId(String value) { 
          if (Utilities.noString(value))
            this.sourceId = null;
          else {
            if (this.sourceId == null)
              this.sourceId = new IdType();
            this.sourceId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #targetId} (Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.). This is the underlying object with id, value and extensions. The accessor "getTargetId" gives direct access to the value
         */
        public IdType getTargetIdElement() { 
          if (this.targetId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.targetId");
            else if (Configuration.doAutoCreate())
              this.targetId = new IdType(); // bb
          return this.targetId;
        }

        public boolean hasTargetIdElement() { 
          return this.targetId != null && !this.targetId.isEmpty();
        }

        public boolean hasTargetId() { 
          return this.targetId != null && !this.targetId.isEmpty();
        }

        /**
         * @param value {@link #targetId} (Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.). This is the underlying object with id, value and extensions. The accessor "getTargetId" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setTargetIdElement(IdType value) { 
          this.targetId = value;
          return this;
        }

        /**
         * @return Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.
         */
        public String getTargetId() { 
          return this.targetId == null ? null : this.targetId.getValue();
        }

        /**
         * @param value Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.
         */
        public TestScriptSetupActionOperationComponent setTargetId(String value) { 
          if (Utilities.noString(value))
            this.targetId = null;
          else {
            if (this.targetId == null)
              this.targetId = new IdType();
            this.targetId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #url} (Complete request URL.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public StringType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationComponent.url");
            else if (Configuration.doAutoCreate())
              this.url = new StringType(); // bb
          return this.url;
        }

        public boolean hasUrlElement() { 
          return this.url != null && !this.url.isEmpty();
        }

        public boolean hasUrl() { 
          return this.url != null && !this.url.isEmpty();
        }

        /**
         * @param value {@link #url} (Complete request URL.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public TestScriptSetupActionOperationComponent setUrlElement(StringType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return Complete request URL.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value Complete request URL.
         */
        public TestScriptSetupActionOperationComponent setUrl(String value) { 
          if (Utilities.noString(value))
            this.url = null;
          else {
            if (this.url == null)
              this.url = new StringType();
            this.url.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Server interaction or operation type.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("resource", "code", "The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("label", "string", "The label would be used for tracking/logging purposes by test engines.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("description", "string", "The description would be used by test engines for tracking and reporting purposes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("accept", "code", "The content-type or mime-type to use for RESTful operation in the 'Accept' header.", 0, java.lang.Integer.MAX_VALUE, accept));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.", 0, java.lang.Integer.MAX_VALUE, contentType));
          childrenList.add(new Property("destination", "integer", "Which server to perform the operation on.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("encodeRequestUrl", "boolean", "Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.", 0, java.lang.Integer.MAX_VALUE, encodeRequestUrl));
          childrenList.add(new Property("params", "string", "Path plus parameters after [type].  Used to set parts of the request URL explicitly.", 0, java.lang.Integer.MAX_VALUE, params));
          childrenList.add(new Property("requestHeader", "", "Header elements would be used to set HTTP headers.", 0, java.lang.Integer.MAX_VALUE, requestHeader));
          childrenList.add(new Property("responseId", "id", "The fixture id (maybe new) to map to the response.", 0, java.lang.Integer.MAX_VALUE, responseId));
          childrenList.add(new Property("sourceId", "id", "The id of the fixture used as the body of a PUT or POST request.", 0, java.lang.Integer.MAX_VALUE, sourceId));
          childrenList.add(new Property("targetId", "id", "Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.", 0, java.lang.Integer.MAX_VALUE, targetId));
          childrenList.add(new Property("url", "string", "Complete request URL.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      public TestScriptSetupActionOperationComponent copy() {
        TestScriptSetupActionOperationComponent dst = new TestScriptSetupActionOperationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.resource = resource == null ? null : resource.copy();
        dst.label = label == null ? null : label.copy();
        dst.description = description == null ? null : description.copy();
        dst.accept = accept == null ? null : accept.copy();
        dst.contentType = contentType == null ? null : contentType.copy();
        dst.destination = destination == null ? null : destination.copy();
        dst.encodeRequestUrl = encodeRequestUrl == null ? null : encodeRequestUrl.copy();
        dst.params = params == null ? null : params.copy();
        if (requestHeader != null) {
          dst.requestHeader = new ArrayList<TestScriptSetupActionOperationRequestHeaderComponent>();
          for (TestScriptSetupActionOperationRequestHeaderComponent i : requestHeader)
            dst.requestHeader.add(i.copy());
        };
        dst.responseId = responseId == null ? null : responseId.copy();
        dst.sourceId = sourceId == null ? null : sourceId.copy();
        dst.targetId = targetId == null ? null : targetId.copy();
        dst.url = url == null ? null : url.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptSetupActionOperationComponent))
          return false;
        TestScriptSetupActionOperationComponent o = (TestScriptSetupActionOperationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(resource, o.resource, true) && compareDeep(label, o.label, true)
           && compareDeep(description, o.description, true) && compareDeep(accept, o.accept, true) && compareDeep(contentType, o.contentType, true)
           && compareDeep(destination, o.destination, true) && compareDeep(encodeRequestUrl, o.encodeRequestUrl, true)
           && compareDeep(params, o.params, true) && compareDeep(requestHeader, o.requestHeader, true) && compareDeep(responseId, o.responseId, true)
           && compareDeep(sourceId, o.sourceId, true) && compareDeep(targetId, o.targetId, true) && compareDeep(url, o.url, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptSetupActionOperationComponent))
          return false;
        TestScriptSetupActionOperationComponent o = (TestScriptSetupActionOperationComponent) other;
        return compareValues(resource, o.resource, true) && compareValues(label, o.label, true) && compareValues(description, o.description, true)
           && compareValues(accept, o.accept, true) && compareValues(contentType, o.contentType, true) && compareValues(destination, o.destination, true)
           && compareValues(encodeRequestUrl, o.encodeRequestUrl, true) && compareValues(params, o.params, true)
           && compareValues(responseId, o.responseId, true) && compareValues(sourceId, o.sourceId, true) && compareValues(targetId, o.targetId, true)
           && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (resource == null || resource.isEmpty())
           && (label == null || label.isEmpty()) && (description == null || description.isEmpty()) && (accept == null || accept.isEmpty())
           && (contentType == null || contentType.isEmpty()) && (destination == null || destination.isEmpty())
           && (encodeRequestUrl == null || encodeRequestUrl.isEmpty()) && (params == null || params.isEmpty())
           && (requestHeader == null || requestHeader.isEmpty()) && (responseId == null || responseId.isEmpty())
           && (sourceId == null || sourceId.isEmpty()) && (targetId == null || targetId.isEmpty()) && (url == null || url.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptSetupActionOperationRequestHeaderComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The HTTP header field e.g. "Accept".
         */
        @Child(name = "field", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Header field name", formalDefinition="The HTTP header field e.g. \"Accept\"." )
        protected StringType field;

        /**
         * The value of the header e.g. "application/xml".
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Header value", formalDefinition="The value of the header e.g. \"application/xml\"." )
        protected StringType value;

        private static final long serialVersionUID = 274395337L;

    /*
     * Constructor
     */
      public TestScriptSetupActionOperationRequestHeaderComponent() {
        super();
      }

    /*
     * Constructor
     */
      public TestScriptSetupActionOperationRequestHeaderComponent(StringType field, StringType value) {
        super();
        this.field = field;
        this.value = value;
      }

        /**
         * @return {@link #field} (The HTTP header field e.g. "Accept".). This is the underlying object with id, value and extensions. The accessor "getField" gives direct access to the value
         */
        public StringType getFieldElement() { 
          if (this.field == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationRequestHeaderComponent.field");
            else if (Configuration.doAutoCreate())
              this.field = new StringType(); // bb
          return this.field;
        }

        public boolean hasFieldElement() { 
          return this.field != null && !this.field.isEmpty();
        }

        public boolean hasField() { 
          return this.field != null && !this.field.isEmpty();
        }

        /**
         * @param value {@link #field} (The HTTP header field e.g. "Accept".). This is the underlying object with id, value and extensions. The accessor "getField" gives direct access to the value
         */
        public TestScriptSetupActionOperationRequestHeaderComponent setFieldElement(StringType value) { 
          this.field = value;
          return this;
        }

        /**
         * @return The HTTP header field e.g. "Accept".
         */
        public String getField() { 
          return this.field == null ? null : this.field.getValue();
        }

        /**
         * @param value The HTTP header field e.g. "Accept".
         */
        public TestScriptSetupActionOperationRequestHeaderComponent setField(String value) { 
            if (this.field == null)
              this.field = new StringType();
            this.field.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the header e.g. "application/xml".). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionOperationRequestHeaderComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new StringType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the header e.g. "application/xml".). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public TestScriptSetupActionOperationRequestHeaderComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value of the header e.g. "application/xml".
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value of the header e.g. "application/xml".
         */
        public TestScriptSetupActionOperationRequestHeaderComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("field", "string", "The HTTP header field e.g. \"Accept\".", 0, java.lang.Integer.MAX_VALUE, field));
          childrenList.add(new Property("value", "string", "The value of the header e.g. \"application/xml\".", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public TestScriptSetupActionOperationRequestHeaderComponent copy() {
        TestScriptSetupActionOperationRequestHeaderComponent dst = new TestScriptSetupActionOperationRequestHeaderComponent();
        copyValues(dst);
        dst.field = field == null ? null : field.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptSetupActionOperationRequestHeaderComponent))
          return false;
        TestScriptSetupActionOperationRequestHeaderComponent o = (TestScriptSetupActionOperationRequestHeaderComponent) other;
        return compareDeep(field, o.field, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptSetupActionOperationRequestHeaderComponent))
          return false;
        TestScriptSetupActionOperationRequestHeaderComponent o = (TestScriptSetupActionOperationRequestHeaderComponent) other;
        return compareValues(field, o.field, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (field == null || field.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptSetupActionAssertComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The label would be used for tracking/logging purposes by test engines.
         */
        @Child(name = "label", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Assertion label", formalDefinition="The label would be used for tracking/logging purposes by test engines." )
        protected StringType label;

        /**
         * The description would be used by test engines for tracking and reporting purposes.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Assertion description", formalDefinition="The description would be used by test engines for tracking and reporting purposes." )
        protected StringType description;

        /**
         * The direction to use for the assertion.
         */
        @Child(name = "direction", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="response | request", formalDefinition="The direction to use for the assertion." )
        protected Enumeration<AssertionDirectionType> direction;

        /**
         * Id of fixture used to compare the "sourceId/path" evaluations to.
         */
        @Child(name = "compareToSourceId", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of fixture used to compare the \"sourceId/path\" evaluations to", formalDefinition="Id of fixture used to compare the \"sourceId/path\" evaluations to." )
        protected StringType compareToSourceId;

        /**
         * XPath or JSONPath expression against fixture used to compare the "sourceId/path" evaluations to.
         */
        @Child(name = "compareToSourcePath", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="XPath or JSONPath expression against fixture used to compare the \"sourceId/path\" evaluations to", formalDefinition="XPath or JSONPath expression against fixture used to compare the \"sourceId/path\" evaluations to." )
        protected StringType compareToSourcePath;

        /**
         * The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Content-Type' header." )
        protected Enumeration<ContentType> contentType;

        /**
         * The header field e.g. 'Content-Location'.
         */
        @Child(name = "headerField", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The header field", formalDefinition="The header field e.g. 'Content-Location'." )
        protected StringType headerField;

        /**
         * The ID of a fixture.  Asserts that the response contains at a minimumId the fixture specified by minimumId.
         */
        @Child(name = "minimumId", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="MinimumId", formalDefinition="The ID of a fixture.  Asserts that the response contains at a minimumId the fixture specified by minimumId." )
        protected StringType minimumId;

        /**
         * Navigation Links.
         */
        @Child(name = "navigationLinks", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Navigation Links", formalDefinition="Navigation Links." )
        protected BooleanType navigationLinks;

        /**
         * The operator type.
         */
        @Child(name = "operator", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="equals | notEquals | in | notIn | greaterThan | lessThan | empty | notEmpty | contains | notContains", formalDefinition="The operator type." )
        protected Enumeration<AssertionOperatorType> operator;

        /**
         * The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.
         */
        @Child(name = "path", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="XPath or JSONPath expression", formalDefinition="The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server." )
        protected StringType path;

        /**
         * The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.
         */
        @Child(name = "resource", type = {CodeType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource type", formalDefinition="The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html." )
        protected CodeType resource;

        /**
         * okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.
         */
        @Child(name = "response", type = {CodeType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable", formalDefinition="okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable." )
        protected Enumeration<AssertionResponseTypes> response;

        /**
         * HTTP Response Code.
         */
        @Child(name = "responseCode", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Response Code", formalDefinition="HTTP Response Code." )
        protected StringType responseCode;

        /**
         * Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id", formalDefinition="Fixture to evaluate the XPath/JSONPath expression or the headerField  against." )
        protected IdType sourceId;

        /**
         * The ID of the Profile to validate against.
         */
        @Child(name = "validateProfileId", type = {IdType.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Validate Profile Id", formalDefinition="The ID of the Profile to validate against." )
        protected IdType validateProfileId;

        /**
         * The value to compare to.
         */
        @Child(name = "value", type = {StringType.class}, order=17, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The value to compare to", formalDefinition="The value to compare to." )
        protected StringType value;

        /**
         * Warning Only.
         */
        @Child(name = "warningOnly", type = {BooleanType.class}, order=18, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Warning Only", formalDefinition="Warning Only." )
        protected BooleanType warningOnly;

        private static final long serialVersionUID = -607939856L;

    /*
     * Constructor
     */
      public TestScriptSetupActionAssertComponent() {
        super();
      }

        /**
         * @return {@link #label} (The label would be used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.label");
            else if (Configuration.doAutoCreate())
              this.label = new StringType(); // bb
          return this.label;
        }

        public boolean hasLabelElement() { 
          return this.label != null && !this.label.isEmpty();
        }

        public boolean hasLabel() { 
          return this.label != null && !this.label.isEmpty();
        }

        /**
         * @param value {@link #label} (The label would be used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setLabelElement(StringType value) { 
          this.label = value;
          return this;
        }

        /**
         * @return The label would be used for tracking/logging purposes by test engines.
         */
        public String getLabel() { 
          return this.label == null ? null : this.label.getValue();
        }

        /**
         * @param value The label would be used for tracking/logging purposes by test engines.
         */
        public TestScriptSetupActionAssertComponent setLabel(String value) { 
          if (Utilities.noString(value))
            this.label = null;
          else {
            if (this.label == null)
              this.label = new StringType();
            this.label.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (The description would be used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.description");
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
         * @param value {@link #description} (The description would be used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The description would be used by test engines for tracking and reporting purposes.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The description would be used by test engines for tracking and reporting purposes.
         */
        public TestScriptSetupActionAssertComponent setDescription(String value) { 
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
         * @return {@link #direction} (The direction to use for the assertion.). This is the underlying object with id, value and extensions. The accessor "getDirection" gives direct access to the value
         */
        public Enumeration<AssertionDirectionType> getDirectionElement() { 
          if (this.direction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.direction");
            else if (Configuration.doAutoCreate())
              this.direction = new Enumeration<AssertionDirectionType>(new AssertionDirectionTypeEnumFactory()); // bb
          return this.direction;
        }

        public boolean hasDirectionElement() { 
          return this.direction != null && !this.direction.isEmpty();
        }

        public boolean hasDirection() { 
          return this.direction != null && !this.direction.isEmpty();
        }

        /**
         * @param value {@link #direction} (The direction to use for the assertion.). This is the underlying object with id, value and extensions. The accessor "getDirection" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setDirectionElement(Enumeration<AssertionDirectionType> value) { 
          this.direction = value;
          return this;
        }

        /**
         * @return The direction to use for the assertion.
         */
        public AssertionDirectionType getDirection() { 
          return this.direction == null ? null : this.direction.getValue();
        }

        /**
         * @param value The direction to use for the assertion.
         */
        public TestScriptSetupActionAssertComponent setDirection(AssertionDirectionType value) { 
          if (value == null)
            this.direction = null;
          else {
            if (this.direction == null)
              this.direction = new Enumeration<AssertionDirectionType>(new AssertionDirectionTypeEnumFactory());
            this.direction.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #compareToSourceId} (Id of fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceId" gives direct access to the value
         */
        public StringType getCompareToSourceIdElement() { 
          if (this.compareToSourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.compareToSourceId");
            else if (Configuration.doAutoCreate())
              this.compareToSourceId = new StringType(); // bb
          return this.compareToSourceId;
        }

        public boolean hasCompareToSourceIdElement() { 
          return this.compareToSourceId != null && !this.compareToSourceId.isEmpty();
        }

        public boolean hasCompareToSourceId() { 
          return this.compareToSourceId != null && !this.compareToSourceId.isEmpty();
        }

        /**
         * @param value {@link #compareToSourceId} (Id of fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceId" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setCompareToSourceIdElement(StringType value) { 
          this.compareToSourceId = value;
          return this;
        }

        /**
         * @return Id of fixture used to compare the "sourceId/path" evaluations to.
         */
        public String getCompareToSourceId() { 
          return this.compareToSourceId == null ? null : this.compareToSourceId.getValue();
        }

        /**
         * @param value Id of fixture used to compare the "sourceId/path" evaluations to.
         */
        public TestScriptSetupActionAssertComponent setCompareToSourceId(String value) { 
          if (Utilities.noString(value))
            this.compareToSourceId = null;
          else {
            if (this.compareToSourceId == null)
              this.compareToSourceId = new StringType();
            this.compareToSourceId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #compareToSourcePath} (XPath or JSONPath expression against fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourcePath" gives direct access to the value
         */
        public StringType getCompareToSourcePathElement() { 
          if (this.compareToSourcePath == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.compareToSourcePath");
            else if (Configuration.doAutoCreate())
              this.compareToSourcePath = new StringType(); // bb
          return this.compareToSourcePath;
        }

        public boolean hasCompareToSourcePathElement() { 
          return this.compareToSourcePath != null && !this.compareToSourcePath.isEmpty();
        }

        public boolean hasCompareToSourcePath() { 
          return this.compareToSourcePath != null && !this.compareToSourcePath.isEmpty();
        }

        /**
         * @param value {@link #compareToSourcePath} (XPath or JSONPath expression against fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourcePath" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setCompareToSourcePathElement(StringType value) { 
          this.compareToSourcePath = value;
          return this;
        }

        /**
         * @return XPath or JSONPath expression against fixture used to compare the "sourceId/path" evaluations to.
         */
        public String getCompareToSourcePath() { 
          return this.compareToSourcePath == null ? null : this.compareToSourcePath.getValue();
        }

        /**
         * @param value XPath or JSONPath expression against fixture used to compare the "sourceId/path" evaluations to.
         */
        public TestScriptSetupActionAssertComponent setCompareToSourcePath(String value) { 
          if (Utilities.noString(value))
            this.compareToSourcePath = null;
          else {
            if (this.compareToSourcePath == null)
              this.compareToSourcePath = new StringType();
            this.compareToSourcePath.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #contentType} (The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public Enumeration<ContentType> getContentTypeElement() { 
          if (this.contentType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.contentType");
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
         * @param value {@link #contentType} (The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setContentTypeElement(Enumeration<ContentType> value) { 
          this.contentType = value;
          return this;
        }

        /**
         * @return The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        public ContentType getContentType() { 
          return this.contentType == null ? null : this.contentType.getValue();
        }

        /**
         * @param value The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        public TestScriptSetupActionAssertComponent setContentType(ContentType value) { 
          if (value == null)
            this.contentType = null;
          else {
            if (this.contentType == null)
              this.contentType = new Enumeration<ContentType>(new ContentTypeEnumFactory());
            this.contentType.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #headerField} (The header field e.g. 'Content-Location'.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
         */
        public StringType getHeaderFieldElement() { 
          if (this.headerField == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.headerField");
            else if (Configuration.doAutoCreate())
              this.headerField = new StringType(); // bb
          return this.headerField;
        }

        public boolean hasHeaderFieldElement() { 
          return this.headerField != null && !this.headerField.isEmpty();
        }

        public boolean hasHeaderField() { 
          return this.headerField != null && !this.headerField.isEmpty();
        }

        /**
         * @param value {@link #headerField} (The header field e.g. 'Content-Location'.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setHeaderFieldElement(StringType value) { 
          this.headerField = value;
          return this;
        }

        /**
         * @return The header field e.g. 'Content-Location'.
         */
        public String getHeaderField() { 
          return this.headerField == null ? null : this.headerField.getValue();
        }

        /**
         * @param value The header field e.g. 'Content-Location'.
         */
        public TestScriptSetupActionAssertComponent setHeaderField(String value) { 
          if (Utilities.noString(value))
            this.headerField = null;
          else {
            if (this.headerField == null)
              this.headerField = new StringType();
            this.headerField.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #minimumId} (The ID of a fixture.  Asserts that the response contains at a minimumId the fixture specified by minimumId.). This is the underlying object with id, value and extensions. The accessor "getMinimumId" gives direct access to the value
         */
        public StringType getMinimumIdElement() { 
          if (this.minimumId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.minimumId");
            else if (Configuration.doAutoCreate())
              this.minimumId = new StringType(); // bb
          return this.minimumId;
        }

        public boolean hasMinimumIdElement() { 
          return this.minimumId != null && !this.minimumId.isEmpty();
        }

        public boolean hasMinimumId() { 
          return this.minimumId != null && !this.minimumId.isEmpty();
        }

        /**
         * @param value {@link #minimumId} (The ID of a fixture.  Asserts that the response contains at a minimumId the fixture specified by minimumId.). This is the underlying object with id, value and extensions. The accessor "getMinimumId" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setMinimumIdElement(StringType value) { 
          this.minimumId = value;
          return this;
        }

        /**
         * @return The ID of a fixture.  Asserts that the response contains at a minimumId the fixture specified by minimumId.
         */
        public String getMinimumId() { 
          return this.minimumId == null ? null : this.minimumId.getValue();
        }

        /**
         * @param value The ID of a fixture.  Asserts that the response contains at a minimumId the fixture specified by minimumId.
         */
        public TestScriptSetupActionAssertComponent setMinimumId(String value) { 
          if (Utilities.noString(value))
            this.minimumId = null;
          else {
            if (this.minimumId == null)
              this.minimumId = new StringType();
            this.minimumId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #navigationLinks} (Navigation Links.). This is the underlying object with id, value and extensions. The accessor "getNavigationLinks" gives direct access to the value
         */
        public BooleanType getNavigationLinksElement() { 
          if (this.navigationLinks == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.navigationLinks");
            else if (Configuration.doAutoCreate())
              this.navigationLinks = new BooleanType(); // bb
          return this.navigationLinks;
        }

        public boolean hasNavigationLinksElement() { 
          return this.navigationLinks != null && !this.navigationLinks.isEmpty();
        }

        public boolean hasNavigationLinks() { 
          return this.navigationLinks != null && !this.navigationLinks.isEmpty();
        }

        /**
         * @param value {@link #navigationLinks} (Navigation Links.). This is the underlying object with id, value and extensions. The accessor "getNavigationLinks" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setNavigationLinksElement(BooleanType value) { 
          this.navigationLinks = value;
          return this;
        }

        /**
         * @return Navigation Links.
         */
        public boolean getNavigationLinks() { 
          return this.navigationLinks == null || this.navigationLinks.isEmpty() ? false : this.navigationLinks.getValue();
        }

        /**
         * @param value Navigation Links.
         */
        public TestScriptSetupActionAssertComponent setNavigationLinks(boolean value) { 
            if (this.navigationLinks == null)
              this.navigationLinks = new BooleanType();
            this.navigationLinks.setValue(value);
          return this;
        }

        /**
         * @return {@link #operator} (The operator type.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
         */
        public Enumeration<AssertionOperatorType> getOperatorElement() { 
          if (this.operator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.operator");
            else if (Configuration.doAutoCreate())
              this.operator = new Enumeration<AssertionOperatorType>(new AssertionOperatorTypeEnumFactory()); // bb
          return this.operator;
        }

        public boolean hasOperatorElement() { 
          return this.operator != null && !this.operator.isEmpty();
        }

        public boolean hasOperator() { 
          return this.operator != null && !this.operator.isEmpty();
        }

        /**
         * @param value {@link #operator} (The operator type.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setOperatorElement(Enumeration<AssertionOperatorType> value) { 
          this.operator = value;
          return this;
        }

        /**
         * @return The operator type.
         */
        public AssertionOperatorType getOperator() { 
          return this.operator == null ? null : this.operator.getValue();
        }

        /**
         * @param value The operator type.
         */
        public TestScriptSetupActionAssertComponent setOperator(AssertionOperatorType value) { 
          if (value == null)
            this.operator = null;
          else {
            if (this.operator == null)
              this.operator = new Enumeration<AssertionOperatorType>(new AssertionOperatorTypeEnumFactory());
            this.operator.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #path} (The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.
         */
        public TestScriptSetupActionAssertComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public CodeType getResourceElement() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new CodeType(); // bb
          return this.resource;
        }

        public boolean hasResourceElement() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setResourceElement(CodeType value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.
         */
        public String getResource() { 
          return this.resource == null ? null : this.resource.getValue();
        }

        /**
         * @param value The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.
         */
        public TestScriptSetupActionAssertComponent setResource(String value) { 
          if (Utilities.noString(value))
            this.resource = null;
          else {
            if (this.resource == null)
              this.resource = new CodeType();
            this.resource.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #response} (okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.). This is the underlying object with id, value and extensions. The accessor "getResponse" gives direct access to the value
         */
        public Enumeration<AssertionResponseTypes> getResponseElement() { 
          if (this.response == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.response");
            else if (Configuration.doAutoCreate())
              this.response = new Enumeration<AssertionResponseTypes>(new AssertionResponseTypesEnumFactory()); // bb
          return this.response;
        }

        public boolean hasResponseElement() { 
          return this.response != null && !this.response.isEmpty();
        }

        public boolean hasResponse() { 
          return this.response != null && !this.response.isEmpty();
        }

        /**
         * @param value {@link #response} (okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.). This is the underlying object with id, value and extensions. The accessor "getResponse" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setResponseElement(Enumeration<AssertionResponseTypes> value) { 
          this.response = value;
          return this;
        }

        /**
         * @return okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.
         */
        public AssertionResponseTypes getResponse() { 
          return this.response == null ? null : this.response.getValue();
        }

        /**
         * @param value okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.
         */
        public TestScriptSetupActionAssertComponent setResponse(AssertionResponseTypes value) { 
          if (value == null)
            this.response = null;
          else {
            if (this.response == null)
              this.response = new Enumeration<AssertionResponseTypes>(new AssertionResponseTypesEnumFactory());
            this.response.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #responseCode} (HTTP Response Code.). This is the underlying object with id, value and extensions. The accessor "getResponseCode" gives direct access to the value
         */
        public StringType getResponseCodeElement() { 
          if (this.responseCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.responseCode");
            else if (Configuration.doAutoCreate())
              this.responseCode = new StringType(); // bb
          return this.responseCode;
        }

        public boolean hasResponseCodeElement() { 
          return this.responseCode != null && !this.responseCode.isEmpty();
        }

        public boolean hasResponseCode() { 
          return this.responseCode != null && !this.responseCode.isEmpty();
        }

        /**
         * @param value {@link #responseCode} (HTTP Response Code.). This is the underlying object with id, value and extensions. The accessor "getResponseCode" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setResponseCodeElement(StringType value) { 
          this.responseCode = value;
          return this;
        }

        /**
         * @return HTTP Response Code.
         */
        public String getResponseCode() { 
          return this.responseCode == null ? null : this.responseCode.getValue();
        }

        /**
         * @param value HTTP Response Code.
         */
        public TestScriptSetupActionAssertComponent setResponseCode(String value) { 
          if (Utilities.noString(value))
            this.responseCode = null;
          else {
            if (this.responseCode == null)
              this.responseCode = new StringType();
            this.responseCode.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #sourceId} (Fixture to evaluate the XPath/JSONPath expression or the headerField  against.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public IdType getSourceIdElement() { 
          if (this.sourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.sourceId");
            else if (Configuration.doAutoCreate())
              this.sourceId = new IdType(); // bb
          return this.sourceId;
        }

        public boolean hasSourceIdElement() { 
          return this.sourceId != null && !this.sourceId.isEmpty();
        }

        public boolean hasSourceId() { 
          return this.sourceId != null && !this.sourceId.isEmpty();
        }

        /**
         * @param value {@link #sourceId} (Fixture to evaluate the XPath/JSONPath expression or the headerField  against.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setSourceIdElement(IdType value) { 
          this.sourceId = value;
          return this;
        }

        /**
         * @return Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        public String getSourceId() { 
          return this.sourceId == null ? null : this.sourceId.getValue();
        }

        /**
         * @param value Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        public TestScriptSetupActionAssertComponent setSourceId(String value) { 
          if (Utilities.noString(value))
            this.sourceId = null;
          else {
            if (this.sourceId == null)
              this.sourceId = new IdType();
            this.sourceId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #validateProfileId} (The ID of the Profile to validate against.). This is the underlying object with id, value and extensions. The accessor "getValidateProfileId" gives direct access to the value
         */
        public IdType getValidateProfileIdElement() { 
          if (this.validateProfileId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.validateProfileId");
            else if (Configuration.doAutoCreate())
              this.validateProfileId = new IdType(); // bb
          return this.validateProfileId;
        }

        public boolean hasValidateProfileIdElement() { 
          return this.validateProfileId != null && !this.validateProfileId.isEmpty();
        }

        public boolean hasValidateProfileId() { 
          return this.validateProfileId != null && !this.validateProfileId.isEmpty();
        }

        /**
         * @param value {@link #validateProfileId} (The ID of the Profile to validate against.). This is the underlying object with id, value and extensions. The accessor "getValidateProfileId" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setValidateProfileIdElement(IdType value) { 
          this.validateProfileId = value;
          return this;
        }

        /**
         * @return The ID of the Profile to validate against.
         */
        public String getValidateProfileId() { 
          return this.validateProfileId == null ? null : this.validateProfileId.getValue();
        }

        /**
         * @param value The ID of the Profile to validate against.
         */
        public TestScriptSetupActionAssertComponent setValidateProfileId(String value) { 
          if (Utilities.noString(value))
            this.validateProfileId = null;
          else {
            if (this.validateProfileId == null)
              this.validateProfileId = new IdType();
            this.validateProfileId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #value} (The value to compare to.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new StringType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value to compare to.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value to compare to.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value to compare to.
         */
        public TestScriptSetupActionAssertComponent setValue(String value) { 
          if (Utilities.noString(value))
            this.value = null;
          else {
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #warningOnly} (Warning Only.). This is the underlying object with id, value and extensions. The accessor "getWarningOnly" gives direct access to the value
         */
        public BooleanType getWarningOnlyElement() { 
          if (this.warningOnly == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptSetupActionAssertComponent.warningOnly");
            else if (Configuration.doAutoCreate())
              this.warningOnly = new BooleanType(); // bb
          return this.warningOnly;
        }

        public boolean hasWarningOnlyElement() { 
          return this.warningOnly != null && !this.warningOnly.isEmpty();
        }

        public boolean hasWarningOnly() { 
          return this.warningOnly != null && !this.warningOnly.isEmpty();
        }

        /**
         * @param value {@link #warningOnly} (Warning Only.). This is the underlying object with id, value and extensions. The accessor "getWarningOnly" gives direct access to the value
         */
        public TestScriptSetupActionAssertComponent setWarningOnlyElement(BooleanType value) { 
          this.warningOnly = value;
          return this;
        }

        /**
         * @return Warning Only.
         */
        public boolean getWarningOnly() { 
          return this.warningOnly == null || this.warningOnly.isEmpty() ? false : this.warningOnly.getValue();
        }

        /**
         * @param value Warning Only.
         */
        public TestScriptSetupActionAssertComponent setWarningOnly(boolean value) { 
            if (this.warningOnly == null)
              this.warningOnly = new BooleanType();
            this.warningOnly.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("label", "string", "The label would be used for tracking/logging purposes by test engines.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("description", "string", "The description would be used by test engines for tracking and reporting purposes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("direction", "code", "The direction to use for the assertion.", 0, java.lang.Integer.MAX_VALUE, direction));
          childrenList.add(new Property("compareToSourceId", "string", "Id of fixture used to compare the \"sourceId/path\" evaluations to.", 0, java.lang.Integer.MAX_VALUE, compareToSourceId));
          childrenList.add(new Property("compareToSourcePath", "string", "XPath or JSONPath expression against fixture used to compare the \"sourceId/path\" evaluations to.", 0, java.lang.Integer.MAX_VALUE, compareToSourcePath));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.", 0, java.lang.Integer.MAX_VALUE, contentType));
          childrenList.add(new Property("headerField", "string", "The header field e.g. 'Content-Location'.", 0, java.lang.Integer.MAX_VALUE, headerField));
          childrenList.add(new Property("minimumId", "string", "The ID of a fixture.  Asserts that the response contains at a minimumId the fixture specified by minimumId.", 0, java.lang.Integer.MAX_VALUE, minimumId));
          childrenList.add(new Property("navigationLinks", "boolean", "Navigation Links.", 0, java.lang.Integer.MAX_VALUE, navigationLinks));
          childrenList.add(new Property("operator", "code", "The operator type.", 0, java.lang.Integer.MAX_VALUE, operator));
          childrenList.add(new Property("path", "string", "The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("resource", "code", "The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("response", "code", "okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.", 0, java.lang.Integer.MAX_VALUE, response));
          childrenList.add(new Property("responseCode", "string", "HTTP Response Code.", 0, java.lang.Integer.MAX_VALUE, responseCode));
          childrenList.add(new Property("sourceId", "id", "Fixture to evaluate the XPath/JSONPath expression or the headerField  against.", 0, java.lang.Integer.MAX_VALUE, sourceId));
          childrenList.add(new Property("validateProfileId", "id", "The ID of the Profile to validate against.", 0, java.lang.Integer.MAX_VALUE, validateProfileId));
          childrenList.add(new Property("value", "string", "The value to compare to.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("warningOnly", "boolean", "Warning Only.", 0, java.lang.Integer.MAX_VALUE, warningOnly));
        }

      public TestScriptSetupActionAssertComponent copy() {
        TestScriptSetupActionAssertComponent dst = new TestScriptSetupActionAssertComponent();
        copyValues(dst);
        dst.label = label == null ? null : label.copy();
        dst.description = description == null ? null : description.copy();
        dst.direction = direction == null ? null : direction.copy();
        dst.compareToSourceId = compareToSourceId == null ? null : compareToSourceId.copy();
        dst.compareToSourcePath = compareToSourcePath == null ? null : compareToSourcePath.copy();
        dst.contentType = contentType == null ? null : contentType.copy();
        dst.headerField = headerField == null ? null : headerField.copy();
        dst.minimumId = minimumId == null ? null : minimumId.copy();
        dst.navigationLinks = navigationLinks == null ? null : navigationLinks.copy();
        dst.operator = operator == null ? null : operator.copy();
        dst.path = path == null ? null : path.copy();
        dst.resource = resource == null ? null : resource.copy();
        dst.response = response == null ? null : response.copy();
        dst.responseCode = responseCode == null ? null : responseCode.copy();
        dst.sourceId = sourceId == null ? null : sourceId.copy();
        dst.validateProfileId = validateProfileId == null ? null : validateProfileId.copy();
        dst.value = value == null ? null : value.copy();
        dst.warningOnly = warningOnly == null ? null : warningOnly.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptSetupActionAssertComponent))
          return false;
        TestScriptSetupActionAssertComponent o = (TestScriptSetupActionAssertComponent) other;
        return compareDeep(label, o.label, true) && compareDeep(description, o.description, true) && compareDeep(direction, o.direction, true)
           && compareDeep(compareToSourceId, o.compareToSourceId, true) && compareDeep(compareToSourcePath, o.compareToSourcePath, true)
           && compareDeep(contentType, o.contentType, true) && compareDeep(headerField, o.headerField, true)
           && compareDeep(minimumId, o.minimumId, true) && compareDeep(navigationLinks, o.navigationLinks, true)
           && compareDeep(operator, o.operator, true) && compareDeep(path, o.path, true) && compareDeep(resource, o.resource, true)
           && compareDeep(response, o.response, true) && compareDeep(responseCode, o.responseCode, true) && compareDeep(sourceId, o.sourceId, true)
           && compareDeep(validateProfileId, o.validateProfileId, true) && compareDeep(value, o.value, true)
           && compareDeep(warningOnly, o.warningOnly, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptSetupActionAssertComponent))
          return false;
        TestScriptSetupActionAssertComponent o = (TestScriptSetupActionAssertComponent) other;
        return compareValues(label, o.label, true) && compareValues(description, o.description, true) && compareValues(direction, o.direction, true)
           && compareValues(compareToSourceId, o.compareToSourceId, true) && compareValues(compareToSourcePath, o.compareToSourcePath, true)
           && compareValues(contentType, o.contentType, true) && compareValues(headerField, o.headerField, true)
           && compareValues(minimumId, o.minimumId, true) && compareValues(navigationLinks, o.navigationLinks, true)
           && compareValues(operator, o.operator, true) && compareValues(path, o.path, true) && compareValues(resource, o.resource, true)
           && compareValues(response, o.response, true) && compareValues(responseCode, o.responseCode, true) && compareValues(sourceId, o.sourceId, true)
           && compareValues(validateProfileId, o.validateProfileId, true) && compareValues(value, o.value, true)
           && compareValues(warningOnly, o.warningOnly, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (label == null || label.isEmpty()) && (description == null || description.isEmpty())
           && (direction == null || direction.isEmpty()) && (compareToSourceId == null || compareToSourceId.isEmpty())
           && (compareToSourcePath == null || compareToSourcePath.isEmpty()) && (contentType == null || contentType.isEmpty())
           && (headerField == null || headerField.isEmpty()) && (minimumId == null || minimumId.isEmpty())
           && (navigationLinks == null || navigationLinks.isEmpty()) && (operator == null || operator.isEmpty())
           && (path == null || path.isEmpty()) && (resource == null || resource.isEmpty()) && (response == null || response.isEmpty())
           && (responseCode == null || responseCode.isEmpty()) && (sourceId == null || sourceId.isEmpty())
           && (validateProfileId == null || validateProfileId.isEmpty()) && (value == null || value.isEmpty())
           && (warningOnly == null || warningOnly.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTestComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of this test.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The name of this test", formalDefinition="The name of this test." )
        protected StringType name;

        /**
         * A short description of the test.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short description of the test", formalDefinition="A short description of the test." )
        protected StringType description;

        /**
         * Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.
         */
        @Child(name = "metadata", type = {TestScriptMetadataComponent.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Capabiltities that are assumed to function correctly on the FHIR server being tested", formalDefinition="Capabilties that must exist and is assumed to function correctly on the FHIR server being tested." )
        protected TestScriptMetadataComponent metadata;

        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Action", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<TestScriptTestActionComponent> action;

        private static final long serialVersionUID = 408339297L;

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
         * @return {@link #metadata} (Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.)
         */
        public TestScriptMetadataComponent getMetadata() { 
          if (this.metadata == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestComponent.metadata");
            else if (Configuration.doAutoCreate())
              this.metadata = new TestScriptMetadataComponent(); // cc
          return this.metadata;
        }

        public boolean hasMetadata() { 
          return this.metadata != null && !this.metadata.isEmpty();
        }

        /**
         * @param value {@link #metadata} (Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.)
         */
        public TestScriptTestComponent setMetadata(TestScriptMetadataComponent value) { 
          this.metadata = value;
          return this;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
        public List<TestScriptTestActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<TestScriptTestActionComponent>();
          return this.action;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (TestScriptTestActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
    // syntactic sugar
        public TestScriptTestActionComponent addAction() { //3
          TestScriptTestActionComponent t = new TestScriptTestActionComponent();
          if (this.action == null)
            this.action = new ArrayList<TestScriptTestActionComponent>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTestComponent addAction(TestScriptTestActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<TestScriptTestActionComponent>();
          this.action.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of this test.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "A short description of the test.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("metadata", "@TestScript.metadata", "Capabilties that must exist and is assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, metadata));
          childrenList.add(new Property("action", "", "Action would contain either an operation or an assertion.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      public TestScriptTestComponent copy() {
        TestScriptTestComponent dst = new TestScriptTestComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.metadata = metadata == null ? null : metadata.copy();
        if (action != null) {
          dst.action = new ArrayList<TestScriptTestActionComponent>();
          for (TestScriptTestActionComponent i : action)
            dst.action.add(i.copy());
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
           && compareDeep(action, o.action, true);
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
           && (metadata == null || metadata.isEmpty()) && (action == null || action.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTestActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An operation would involve a REST request to a server.
         */
        @Child(name = "operation", type = {TestScriptSetupActionOperationComponent.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Operation", formalDefinition="An operation would involve a REST request to a server." )
        protected TestScriptSetupActionOperationComponent operation;

        /**
         * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
         */
        @Child(name = "assert", type = {TestScriptSetupActionAssertComponent.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Assertion", formalDefinition="Evaluates the results of previous operations to determine if the server under test behaves appropriately." )
        protected TestScriptSetupActionAssertComponent assert_;

        private static final long serialVersionUID = 1411550037L;

    /*
     * Constructor
     */
      public TestScriptTestActionComponent() {
        super();
      }

        /**
         * @return {@link #operation} (An operation would involve a REST request to a server.)
         */
        public TestScriptSetupActionOperationComponent getOperation() { 
          if (this.operation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestActionComponent.operation");
            else if (Configuration.doAutoCreate())
              this.operation = new TestScriptSetupActionOperationComponent(); // cc
          return this.operation;
        }

        public boolean hasOperation() { 
          return this.operation != null && !this.operation.isEmpty();
        }

        /**
         * @param value {@link #operation} (An operation would involve a REST request to a server.)
         */
        public TestScriptTestActionComponent setOperation(TestScriptSetupActionOperationComponent value) { 
          this.operation = value;
          return this;
        }

        /**
         * @return {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
         */
        public TestScriptSetupActionAssertComponent getAssert() { 
          if (this.assert_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTestActionComponent.assert_");
            else if (Configuration.doAutoCreate())
              this.assert_ = new TestScriptSetupActionAssertComponent(); // cc
          return this.assert_;
        }

        public boolean hasAssert() { 
          return this.assert_ != null && !this.assert_.isEmpty();
        }

        /**
         * @param value {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
         */
        public TestScriptTestActionComponent setAssert(TestScriptSetupActionAssertComponent value) { 
          this.assert_ = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "@TestScript.setup.action.operation", "An operation would involve a REST request to a server.", 0, java.lang.Integer.MAX_VALUE, operation));
          childrenList.add(new Property("assert", "@TestScript.setup.action.assert", "Evaluates the results of previous operations to determine if the server under test behaves appropriately.", 0, java.lang.Integer.MAX_VALUE, assert_));
        }

      public TestScriptTestActionComponent copy() {
        TestScriptTestActionComponent dst = new TestScriptTestActionComponent();
        copyValues(dst);
        dst.operation = operation == null ? null : operation.copy();
        dst.assert_ = assert_ == null ? null : assert_.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTestActionComponent))
          return false;
        TestScriptTestActionComponent o = (TestScriptTestActionComponent) other;
        return compareDeep(operation, o.operation, true) && compareDeep(assert_, o.assert_, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTestActionComponent))
          return false;
        TestScriptTestActionComponent o = (TestScriptTestActionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (operation == null || operation.isEmpty()) && (assert_ == null || assert_.isEmpty())
          ;
      }

  }

    @Block()
    public static class TestScriptTeardownComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Action", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<TestScriptTeardownActionComponent> action;

        private static final long serialVersionUID = 1850225254L;

    /*
     * Constructor
     */
      public TestScriptTeardownComponent() {
        super();
      }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
        public List<TestScriptTeardownActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<TestScriptTeardownActionComponent>();
          return this.action;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (TestScriptTeardownActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
    // syntactic sugar
        public TestScriptTeardownActionComponent addAction() { //3
          TestScriptTeardownActionComponent t = new TestScriptTeardownActionComponent();
          if (this.action == null)
            this.action = new ArrayList<TestScriptTeardownActionComponent>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTeardownComponent addAction(TestScriptTeardownActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<TestScriptTeardownActionComponent>();
          this.action.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("action", "", "Action would contain either an operation or an assertion.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      public TestScriptTeardownComponent copy() {
        TestScriptTeardownComponent dst = new TestScriptTeardownComponent();
        copyValues(dst);
        if (action != null) {
          dst.action = new ArrayList<TestScriptTeardownActionComponent>();
          for (TestScriptTeardownActionComponent i : action)
            dst.action.add(i.copy());
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
        return compareDeep(action, o.action, true);
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
        return super.isEmpty() && (action == null || action.isEmpty());
      }

  }

    @Block()
    public static class TestScriptTeardownActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An operation would involve a REST request to a server.
         */
        @Child(name = "operation", type = {TestScriptSetupActionOperationComponent.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Operation", formalDefinition="An operation would involve a REST request to a server." )
        protected TestScriptSetupActionOperationComponent operation;

        private static final long serialVersionUID = 1684092023L;

    /*
     * Constructor
     */
      public TestScriptTeardownActionComponent() {
        super();
      }

        /**
         * @return {@link #operation} (An operation would involve a REST request to a server.)
         */
        public TestScriptSetupActionOperationComponent getOperation() { 
          if (this.operation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptTeardownActionComponent.operation");
            else if (Configuration.doAutoCreate())
              this.operation = new TestScriptSetupActionOperationComponent(); // cc
          return this.operation;
        }

        public boolean hasOperation() { 
          return this.operation != null && !this.operation.isEmpty();
        }

        /**
         * @param value {@link #operation} (An operation would involve a REST request to a server.)
         */
        public TestScriptTeardownActionComponent setOperation(TestScriptSetupActionOperationComponent value) { 
          this.operation = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "@TestScript.setup.action.operation", "An operation would involve a REST request to a server.", 0, java.lang.Integer.MAX_VALUE, operation));
        }

      public TestScriptTeardownActionComponent copy() {
        TestScriptTeardownActionComponent dst = new TestScriptTeardownActionComponent();
        copyValues(dst);
        dst.operation = operation == null ? null : operation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptTeardownActionComponent))
          return false;
        TestScriptTeardownActionComponent o = (TestScriptTeardownActionComponent) other;
        return compareDeep(operation, o.operation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptTeardownActionComponent))
          return false;
        TestScriptTeardownActionComponent o = (TestScriptTeardownActionComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (operation == null || operation.isEmpty());
      }

  }

    /**
     * An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Literal URL used to reference this TestScript", formalDefinition="An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published." )
    protected UriType url;

    /**
     * The identifier that is used to identify this version of the TestScript. This is an arbitrary value managed by the TestScript author manually.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of the TestScript", formalDefinition="The identifier that is used to identify this version of the TestScript. This is an arbitrary value managed by the TestScript author manually." )
    protected StringType version;

    /**
     * A free text natural language name identifying the TestScript.
     */
    @Child(name = "name", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this TestScript", formalDefinition="A free text natural language name identifying the TestScript." )
    protected StringType name;

    /**
     * The status of the TestScript.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the TestScript." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the Test Script.
     */
    @Child(name = "publisher", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the Test Script." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<TestScriptContactComponent> contact;

    /**
     * The date that this version of the TestScript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the test cases change.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for this version of the TestScript", formalDefinition="The date that this version of the TestScript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the test cases change." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the TestScript and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Natural language description of the TestScript", formalDefinition="A free text natural language description of the TestScript and its use." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts." )
    protected List<CodeableConcept> useContext;

    /**
     * Explains why this Test Script is needed and why it's been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Scope and Usage this Test Script is for", formalDefinition="Explains why this Test Script is needed and why it's been constrained as it has." )
    protected StringType requirements;

    /**
     * A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    @Child(name = "copyright", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or Publishing restrictions", formalDefinition="A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings." )
    protected StringType copyright;

    /**
     * The required capability must exist and is assumed to function correctly on the FHIR server being tested.
     */
    @Child(name = "metadata", type = {}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Required capability that is assumed to function correctly on the FHIR server being tested", formalDefinition="The required capability must exist and is assumed to function correctly on the FHIR server being tested." )
    protected TestScriptMetadataComponent metadata;

    /**
     * If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults to false if value is unspecified.
     */
    @Child(name = "multiserver", type = {BooleanType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Whether or not the tests apply to more than one FHIR server", formalDefinition="If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults to false if value is unspecified." )
    protected BooleanType multiserver;

    /**
     * Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.
     */
    @Child(name = "fixture", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Fixture in the test script - by reference (uri)", formalDefinition="Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute." )
    protected List<TestScriptFixtureComponent> fixture;

    /**
     * Reference to the profile to be used for validation.
     */
    @Child(name = "profile", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reference of the validation profile", formalDefinition="Reference to the profile to be used for validation." )
    protected List<Reference> profile;
    /**
     * The actual objects that are the target of the reference (Reference to the profile to be used for validation.)
     */
    protected List<Resource> profileTarget;


    /**
     * Variable is set based either on element value in response body or on header field value in the response headers.
     */
    @Child(name = "variable", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Variable", formalDefinition="Variable is set based either on element value in response body or on header field value in the response headers." )
    protected List<TestScriptVariableComponent> variable;

    /**
     * A series of required setup operations before tests are executed.
     */
    @Child(name = "setup", type = {}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A series of required setup operations before tests are executed", formalDefinition="A series of required setup operations before tests are executed." )
    protected TestScriptSetupComponent setup;

    /**
     * A test in this script.
     */
    @Child(name = "test", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A test in this script", formalDefinition="A test in this script." )
    protected List<TestScriptTestComponent> test;

    /**
     * A series of operations required to clean up after the all the tests are executed (successfully or otherwise).
     */
    @Child(name = "teardown", type = {}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A series of required clean up steps", formalDefinition="A series of operations required to clean up after the all the tests are executed (successfully or otherwise)." )
    protected TestScriptTeardownComponent teardown;

    private static final long serialVersionUID = -77088763L;

  /*
   * Constructor
   */
    public TestScript() {
      super();
    }

  /*
   * Constructor
   */
    public TestScript(UriType url, StringType name, Enumeration<ConformanceResourceStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public TestScript setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published.
     */
    public TestScript setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the TestScript. This is an arbitrary value managed by the TestScript author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The identifier that is used to identify this version of the TestScript. This is an arbitrary value managed by the TestScript author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public TestScript setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the TestScript. This is an arbitrary value managed by the TestScript author manually.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the TestScript. This is an arbitrary value managed by the TestScript author manually.
     */
    public TestScript setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A free text natural language name identifying the TestScript.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (A free text natural language name identifying the TestScript.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public TestScript setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the TestScript.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the TestScript.
     */
    public TestScript setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of the TestScript.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the TestScript.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public TestScript setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the TestScript.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the TestScript.
     */
    public TestScript setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public TestScript setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public TestScript setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the Test Script.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the Test Script.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public TestScript setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the Test Script.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the Test Script.
     */
    public TestScript setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    public List<TestScriptContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<TestScriptContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (TestScriptContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public TestScriptContactComponent addContact() { //3
      TestScriptContactComponent t = new TestScriptContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<TestScriptContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addContact(TestScriptContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<TestScriptContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date that this version of the TestScript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the test cases change.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date that this version of the TestScript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the test cases change.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public TestScript setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that this version of the TestScript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the test cases change.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that this version of the TestScript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the test cases change.
     */
    public TestScript setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the TestScript and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
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
     * @param value {@link #description} (A free text natural language description of the TestScript and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public TestScript setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the TestScript and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the TestScript and its use.
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts.)
     */
    public List<CodeableConcept> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      return this.useContext;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (CodeableConcept item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts.)
     */
    // syntactic sugar
    public CodeableConcept addUseContext() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #requirements} (Explains why this Test Script is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.requirements");
        else if (Configuration.doAutoCreate())
          this.requirements = new StringType(); // bb
      return this.requirements;
    }

    public boolean hasRequirementsElement() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    public boolean hasRequirements() { 
      return this.requirements != null && !this.requirements.isEmpty();
    }

    /**
     * @param value {@link #requirements} (Explains why this Test Script is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public TestScript setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this Test Script is needed and why it's been constrained as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this Test Script is needed and why it's been constrained as it has.
     */
    public TestScript setRequirements(String value) { 
      if (Utilities.noString(value))
        this.requirements = null;
      else {
        if (this.requirements == null)
          this.requirements = new StringType();
        this.requirements.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public TestScript setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public TestScript setCopyright(String value) { 
      if (Utilities.noString(value))
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new StringType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #metadata} (The required capability must exist and is assumed to function correctly on the FHIR server being tested.)
     */
    public TestScriptMetadataComponent getMetadata() { 
      if (this.metadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.metadata");
        else if (Configuration.doAutoCreate())
          this.metadata = new TestScriptMetadataComponent(); // cc
      return this.metadata;
    }

    public boolean hasMetadata() { 
      return this.metadata != null && !this.metadata.isEmpty();
    }

    /**
     * @param value {@link #metadata} (The required capability must exist and is assumed to function correctly on the FHIR server being tested.)
     */
    public TestScript setMetadata(TestScriptMetadataComponent value) { 
      this.metadata = value;
      return this;
    }

    /**
     * @return {@link #multiserver} (If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults to false if value is unspecified.). This is the underlying object with id, value and extensions. The accessor "getMultiserver" gives direct access to the value
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
     * @param value {@link #multiserver} (If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults to false if value is unspecified.). This is the underlying object with id, value and extensions. The accessor "getMultiserver" gives direct access to the value
     */
    public TestScript setMultiserverElement(BooleanType value) { 
      this.multiserver = value;
      return this;
    }

    /**
     * @return If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults to false if value is unspecified.
     */
    public boolean getMultiserver() { 
      return this.multiserver == null || this.multiserver.isEmpty() ? false : this.multiserver.getValue();
    }

    /**
     * @param value If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults to false if value is unspecified.
     */
    public TestScript setMultiserver(boolean value) { 
        if (this.multiserver == null)
          this.multiserver = new BooleanType();
        this.multiserver.setValue(value);
      return this;
    }

    /**
     * @return {@link #fixture} (Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.)
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
     * @return {@link #fixture} (Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.)
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
     * @return {@link #profile} (Reference to the profile to be used for validation.)
     */
    public List<Reference> getProfile() { 
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      return this.profile;
    }

    public boolean hasProfile() { 
      if (this.profile == null)
        return false;
      for (Reference item : this.profile)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #profile} (Reference to the profile to be used for validation.)
     */
    // syntactic sugar
    public Reference addProfile() { //3
      Reference t = new Reference();
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      this.profile.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addProfile(Reference t) { //3
      if (t == null)
        return this;
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      this.profile.add(t);
      return this;
    }

    /**
     * @return {@link #profile} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Reference to the profile to be used for validation.)
     */
    public List<Resource> getProfileTarget() { 
      if (this.profileTarget == null)
        this.profileTarget = new ArrayList<Resource>();
      return this.profileTarget;
    }

    /**
     * @return {@link #variable} (Variable is set based either on element value in response body or on header field value in the response headers.)
     */
    public List<TestScriptVariableComponent> getVariable() { 
      if (this.variable == null)
        this.variable = new ArrayList<TestScriptVariableComponent>();
      return this.variable;
    }

    public boolean hasVariable() { 
      if (this.variable == null)
        return false;
      for (TestScriptVariableComponent item : this.variable)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #variable} (Variable is set based either on element value in response body or on header field value in the response headers.)
     */
    // syntactic sugar
    public TestScriptVariableComponent addVariable() { //3
      TestScriptVariableComponent t = new TestScriptVariableComponent();
      if (this.variable == null)
        this.variable = new ArrayList<TestScriptVariableComponent>();
      this.variable.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addVariable(TestScriptVariableComponent t) { //3
      if (t == null)
        return this;
      if (this.variable == null)
        this.variable = new ArrayList<TestScriptVariableComponent>();
      this.variable.add(t);
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
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the TestScript. This is an arbitrary value managed by the TestScript author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the TestScript.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the TestScript.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the Test Script.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date that this version of the TestScript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the test cases change.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the TestScript and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("requirements", "string", "Explains why this Test Script is needed and why it's been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("metadata", "", "The required capability must exist and is assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, metadata));
        childrenList.add(new Property("multiserver", "boolean", "If the tests apply to more than one FHIR server (e.g. cross-server interoperability tests) then multiserver=true. Defaults to false if value is unspecified.", 0, java.lang.Integer.MAX_VALUE, multiserver));
        childrenList.add(new Property("fixture", "", "Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.", 0, java.lang.Integer.MAX_VALUE, fixture));
        childrenList.add(new Property("profile", "Reference(Any)", "Reference to the profile to be used for validation.", 0, java.lang.Integer.MAX_VALUE, profile));
        childrenList.add(new Property("variable", "", "Variable is set based either on element value in response body or on header field value in the response headers.", 0, java.lang.Integer.MAX_VALUE, variable));
        childrenList.add(new Property("setup", "", "A series of required setup operations before tests are executed.", 0, java.lang.Integer.MAX_VALUE, setup));
        childrenList.add(new Property("test", "", "A test in this script.", 0, java.lang.Integer.MAX_VALUE, test));
        childrenList.add(new Property("teardown", "", "A series of operations required to clean up after the all the tests are executed (successfully or otherwise).", 0, java.lang.Integer.MAX_VALUE, teardown));
      }

      public TestScript copy() {
        TestScript dst = new TestScript();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<TestScriptContactComponent>();
          for (TestScriptContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.metadata = metadata == null ? null : metadata.copy();
        dst.multiserver = multiserver == null ? null : multiserver.copy();
        if (fixture != null) {
          dst.fixture = new ArrayList<TestScriptFixtureComponent>();
          for (TestScriptFixtureComponent i : fixture)
            dst.fixture.add(i.copy());
        };
        if (profile != null) {
          dst.profile = new ArrayList<Reference>();
          for (Reference i : profile)
            dst.profile.add(i.copy());
        };
        if (variable != null) {
          dst.variable = new ArrayList<TestScriptVariableComponent>();
          for (TestScriptVariableComponent i : variable)
            dst.variable.add(i.copy());
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
        return compareDeep(url, o.url, true) && compareDeep(version, o.version, true) && compareDeep(name, o.name, true)
           && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true) && compareDeep(description, o.description, true)
           && compareDeep(useContext, o.useContext, true) && compareDeep(requirements, o.requirements, true)
           && compareDeep(copyright, o.copyright, true) && compareDeep(metadata, o.metadata, true) && compareDeep(multiserver, o.multiserver, true)
           && compareDeep(fixture, o.fixture, true) && compareDeep(profile, o.profile, true) && compareDeep(variable, o.variable, true)
           && compareDeep(setup, o.setup, true) && compareDeep(test, o.test, true) && compareDeep(teardown, o.teardown, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScript))
          return false;
        TestScript o = (TestScript) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(requirements, o.requirements, true)
           && compareValues(copyright, o.copyright, true) && compareValues(multiserver, o.multiserver, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (version == null || version.isEmpty())
           && (name == null || name.isEmpty()) && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty())
           && (description == null || description.isEmpty()) && (useContext == null || useContext.isEmpty())
           && (requirements == null || requirements.isEmpty()) && (copyright == null || copyright.isEmpty())
           && (metadata == null || metadata.isEmpty()) && (multiserver == null || multiserver.isEmpty())
           && (fixture == null || fixture.isEmpty()) && (profile == null || profile.isEmpty()) && (variable == null || variable.isEmpty())
           && (setup == null || setup.isEmpty()) && (test == null || test.isEmpty()) && (teardown == null || teardown.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.TestScript;
   }

  @SearchParamDefinition(name="testscript-setup-capabilities", path="TestScript.setup.metadata.capabilities.description", description="TestScript setup required and validated capabilities", type="string" )
  public static final String SP_TESTSCRIPTSETUPCAPABILITIES = "testscript-setup-capabilities";
  @SearchParamDefinition(name="name", path="TestScript.name", description="TestScript name", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="description", path="TestScript.description", description="TestScript description", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="testscript-capabilities", path="TestScript.metadata.capabilities.description", description="TestScript required and validated capabilities", type="string" )
  public static final String SP_TESTSCRIPTCAPABILITIES = "testscript-capabilities";
  @SearchParamDefinition(name="testscript-test-capabilities", path="TestScript.test.metadata.capabilities.description", description="TestScript test required and validated capabilities", type="string" )
  public static final String SP_TESTSCRIPTTESTCAPABILITIES = "testscript-test-capabilities";

}

