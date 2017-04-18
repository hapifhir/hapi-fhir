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

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A structured set of tests against a FHIR server implementation to determine compliance against the FHIR specification.
 */
@ResourceDef(name="TestScript", profile="http://hl7.org/fhir/Profile/TestScript")
@ChildOrder(names={"url", "identifier", "version", "name", "title", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "purpose", "copyright", "origin", "destination", "metadata", "fixture", "profile", "variable", "rule", "ruleset", "setup", "test", "teardown"})
public class TestScript extends MetadataResource {

    public enum ContentType {
        /**
         * XML content-type corresponding to the application/fhir+xml mime-type.
         */
        XML, 
        /**
         * JSON content-type corresponding to the application/fhir+json mime-type.
         */
        JSON, 
        /**
         * RDF content-type corresponding to the text/turtle mime-type.
         */
        TTL, 
        /**
         * Prevent the use of the corresponding http header.
         */
        NONE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ContentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xml".equals(codeString))
          return XML;
        if ("json".equals(codeString))
          return JSON;
        if ("ttl".equals(codeString))
          return TTL;
        if ("none".equals(codeString))
          return NONE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ContentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case XML: return "xml";
            case JSON: return "json";
            case TTL: return "ttl";
            case NONE: return "none";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case XML: return "http://hl7.org/fhir/content-type";
            case JSON: return "http://hl7.org/fhir/content-type";
            case TTL: return "http://hl7.org/fhir/content-type";
            case NONE: return "http://hl7.org/fhir/content-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case XML: return "XML content-type corresponding to the application/fhir+xml mime-type.";
            case JSON: return "JSON content-type corresponding to the application/fhir+json mime-type.";
            case TTL: return "RDF content-type corresponding to the text/turtle mime-type.";
            case NONE: return "Prevent the use of the corresponding http header.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case XML: return "xml";
            case JSON: return "json";
            case TTL: return "ttl";
            case NONE: return "none";
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
        if ("ttl".equals(codeString))
          return ContentType.TTL;
        if ("none".equals(codeString))
          return ContentType.NONE;
        throw new IllegalArgumentException("Unknown ContentType code '"+codeString+"'");
        }
        public Enumeration<ContentType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ContentType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("xml".equals(codeString))
          return new Enumeration<ContentType>(this, ContentType.XML);
        if ("json".equals(codeString))
          return new Enumeration<ContentType>(this, ContentType.JSON);
        if ("ttl".equals(codeString))
          return new Enumeration<ContentType>(this, ContentType.TTL);
        if ("none".equals(codeString))
          return new Enumeration<ContentType>(this, ContentType.NONE);
        throw new FHIRException("Unknown ContentType code '"+codeString+"'");
        }
    public String toCode(ContentType code) {
      if (code == ContentType.XML)
        return "xml";
      if (code == ContentType.JSON)
        return "json";
      if (code == ContentType.TTL)
        return "ttl";
      if (code == ContentType.NONE)
        return "none";
      return "?";
      }
    public String toSystem(ContentType code) {
      return code.getSystem();
      }
    }

    public enum AssertionDirectionType {
        /**
         * The assertion is evaluated on the response. This is the default value.
         */
        RESPONSE, 
        /**
         * The assertion is evaluated on the request.
         */
        REQUEST, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AssertionDirectionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("response".equals(codeString))
          return RESPONSE;
        if ("request".equals(codeString))
          return REQUEST;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AssertionDirectionType code '"+codeString+"'");
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
            case RESPONSE: return "The assertion is evaluated on the response. This is the default value.";
            case REQUEST: return "The assertion is evaluated on the request.";
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
        public Enumeration<AssertionDirectionType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AssertionDirectionType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("response".equals(codeString))
          return new Enumeration<AssertionDirectionType>(this, AssertionDirectionType.RESPONSE);
        if ("request".equals(codeString))
          return new Enumeration<AssertionDirectionType>(this, AssertionDirectionType.REQUEST);
        throw new FHIRException("Unknown AssertionDirectionType code '"+codeString+"'");
        }
    public String toCode(AssertionDirectionType code) {
      if (code == AssertionDirectionType.RESPONSE)
        return "response";
      if (code == AssertionDirectionType.REQUEST)
        return "request";
      return "?";
      }
    public String toSystem(AssertionDirectionType code) {
      return code.getSystem();
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
         * Evaluate the fluentpath expression as a boolean condition.
         */
        EVAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AssertionOperatorType fromCode(String codeString) throws FHIRException {
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
        if ("eval".equals(codeString))
          return EVAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AssertionOperatorType code '"+codeString+"'");
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
            case EVAL: return "eval";
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
            case EVAL: return "http://hl7.org/fhir/assert-operator-codes";
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
            case EVAL: return "Evaluate the fluentpath expression as a boolean condition.";
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
            case EVAL: return "evaluate";
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
        if ("eval".equals(codeString))
          return AssertionOperatorType.EVAL;
        throw new IllegalArgumentException("Unknown AssertionOperatorType code '"+codeString+"'");
        }
        public Enumeration<AssertionOperatorType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AssertionOperatorType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("equals".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.EQUALS);
        if ("notEquals".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.NOTEQUALS);
        if ("in".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.IN);
        if ("notIn".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.NOTIN);
        if ("greaterThan".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.GREATERTHAN);
        if ("lessThan".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.LESSTHAN);
        if ("empty".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.EMPTY);
        if ("notEmpty".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.NOTEMPTY);
        if ("contains".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.CONTAINS);
        if ("notContains".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.NOTCONTAINS);
        if ("eval".equals(codeString))
          return new Enumeration<AssertionOperatorType>(this, AssertionOperatorType.EVAL);
        throw new FHIRException("Unknown AssertionOperatorType code '"+codeString+"'");
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
      if (code == AssertionOperatorType.EVAL)
        return "eval";
      return "?";
      }
    public String toSystem(AssertionOperatorType code) {
      return code.getSystem();
      }
    }

    public enum TestScriptRequestMethodCode {
        /**
         * HTTP DELETE operation
         */
        DELETE, 
        /**
         * HTTP GET operation
         */
        GET, 
        /**
         * HTTP OPTIONS operation
         */
        OPTIONS, 
        /**
         * HTTP PATCH operation
         */
        PATCH, 
        /**
         * HTTP POST operation
         */
        POST, 
        /**
         * HTTP PUT operation
         */
        PUT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static TestScriptRequestMethodCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("delete".equals(codeString))
          return DELETE;
        if ("get".equals(codeString))
          return GET;
        if ("options".equals(codeString))
          return OPTIONS;
        if ("patch".equals(codeString))
          return PATCH;
        if ("post".equals(codeString))
          return POST;
        if ("put".equals(codeString))
          return PUT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown TestScriptRequestMethodCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DELETE: return "delete";
            case GET: return "get";
            case OPTIONS: return "options";
            case PATCH: return "patch";
            case POST: return "post";
            case PUT: return "put";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DELETE: return "http://hl7.org/fhir/http-operations";
            case GET: return "http://hl7.org/fhir/http-operations";
            case OPTIONS: return "http://hl7.org/fhir/http-operations";
            case PATCH: return "http://hl7.org/fhir/http-operations";
            case POST: return "http://hl7.org/fhir/http-operations";
            case PUT: return "http://hl7.org/fhir/http-operations";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DELETE: return "HTTP DELETE operation";
            case GET: return "HTTP GET operation";
            case OPTIONS: return "HTTP OPTIONS operation";
            case PATCH: return "HTTP PATCH operation";
            case POST: return "HTTP POST operation";
            case PUT: return "HTTP PUT operation";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DELETE: return "DELETE";
            case GET: return "GET";
            case OPTIONS: return "OPTIONS";
            case PATCH: return "PATCH";
            case POST: return "POST";
            case PUT: return "PUT";
            default: return "?";
          }
        }
    }

  public static class TestScriptRequestMethodCodeEnumFactory implements EnumFactory<TestScriptRequestMethodCode> {
    public TestScriptRequestMethodCode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("delete".equals(codeString))
          return TestScriptRequestMethodCode.DELETE;
        if ("get".equals(codeString))
          return TestScriptRequestMethodCode.GET;
        if ("options".equals(codeString))
          return TestScriptRequestMethodCode.OPTIONS;
        if ("patch".equals(codeString))
          return TestScriptRequestMethodCode.PATCH;
        if ("post".equals(codeString))
          return TestScriptRequestMethodCode.POST;
        if ("put".equals(codeString))
          return TestScriptRequestMethodCode.PUT;
        throw new IllegalArgumentException("Unknown TestScriptRequestMethodCode code '"+codeString+"'");
        }
        public Enumeration<TestScriptRequestMethodCode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<TestScriptRequestMethodCode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("delete".equals(codeString))
          return new Enumeration<TestScriptRequestMethodCode>(this, TestScriptRequestMethodCode.DELETE);
        if ("get".equals(codeString))
          return new Enumeration<TestScriptRequestMethodCode>(this, TestScriptRequestMethodCode.GET);
        if ("options".equals(codeString))
          return new Enumeration<TestScriptRequestMethodCode>(this, TestScriptRequestMethodCode.OPTIONS);
        if ("patch".equals(codeString))
          return new Enumeration<TestScriptRequestMethodCode>(this, TestScriptRequestMethodCode.PATCH);
        if ("post".equals(codeString))
          return new Enumeration<TestScriptRequestMethodCode>(this, TestScriptRequestMethodCode.POST);
        if ("put".equals(codeString))
          return new Enumeration<TestScriptRequestMethodCode>(this, TestScriptRequestMethodCode.PUT);
        throw new FHIRException("Unknown TestScriptRequestMethodCode code '"+codeString+"'");
        }
    public String toCode(TestScriptRequestMethodCode code) {
      if (code == TestScriptRequestMethodCode.DELETE)
        return "delete";
      if (code == TestScriptRequestMethodCode.GET)
        return "get";
      if (code == TestScriptRequestMethodCode.OPTIONS)
        return "options";
      if (code == TestScriptRequestMethodCode.PATCH)
        return "patch";
      if (code == TestScriptRequestMethodCode.POST)
        return "post";
      if (code == TestScriptRequestMethodCode.PUT)
        return "put";
      return "?";
      }
    public String toSystem(TestScriptRequestMethodCode code) {
      return code.getSystem();
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static AssertionResponseTypes fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AssertionResponseTypes code '"+codeString+"'");
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
        public Enumeration<AssertionResponseTypes> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AssertionResponseTypes>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("okay".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.OKAY);
        if ("created".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.CREATED);
        if ("noContent".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.NOCONTENT);
        if ("notModified".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.NOTMODIFIED);
        if ("bad".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.BAD);
        if ("forbidden".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.FORBIDDEN);
        if ("notFound".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.NOTFOUND);
        if ("methodNotAllowed".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.METHODNOTALLOWED);
        if ("conflict".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.CONFLICT);
        if ("gone".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.GONE);
        if ("preconditionFailed".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.PRECONDITIONFAILED);
        if ("unprocessable".equals(codeString))
          return new Enumeration<AssertionResponseTypes>(this, AssertionResponseTypes.UNPROCESSABLE);
        throw new FHIRException("Unknown AssertionResponseTypes code '"+codeString+"'");
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
    public String toSystem(AssertionResponseTypes code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class TestScriptOriginComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Abstract name given to an origin server in this test script.  The name is provided as a number starting at 1.
         */
        @Child(name = "index", type = {IntegerType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The index of the abstract origin server starting at 1", formalDefinition="Abstract name given to an origin server in this test script.  The name is provided as a number starting at 1." )
        protected IntegerType index;

        /**
         * The type of origin profile the test system supports.
         */
        @Child(name = "profile", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="FHIR-Client | FHIR-SDC-FormFiller", formalDefinition="The type of origin profile the test system supports." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/testscript-profile-origin-types")
        protected Coding profile;

        private static final long serialVersionUID = -1239935149L;

    /**
     * Constructor
     */
      public TestScriptOriginComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptOriginComponent(IntegerType index, Coding profile) {
        super();
        this.index = index;
        this.profile = profile;
      }

        /**
         * @return {@link #index} (Abstract name given to an origin server in this test script.  The name is provided as a number starting at 1.). This is the underlying object with id, value and extensions. The accessor "getIndex" gives direct access to the value
         */
        public IntegerType getIndexElement() { 
          if (this.index == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptOriginComponent.index");
            else if (Configuration.doAutoCreate())
              this.index = new IntegerType(); // bb
          return this.index;
        }

        public boolean hasIndexElement() { 
          return this.index != null && !this.index.isEmpty();
        }

        public boolean hasIndex() { 
          return this.index != null && !this.index.isEmpty();
        }

        /**
         * @param value {@link #index} (Abstract name given to an origin server in this test script.  The name is provided as a number starting at 1.). This is the underlying object with id, value and extensions. The accessor "getIndex" gives direct access to the value
         */
        public TestScriptOriginComponent setIndexElement(IntegerType value) { 
          this.index = value;
          return this;
        }

        /**
         * @return Abstract name given to an origin server in this test script.  The name is provided as a number starting at 1.
         */
        public int getIndex() { 
          return this.index == null || this.index.isEmpty() ? 0 : this.index.getValue();
        }

        /**
         * @param value Abstract name given to an origin server in this test script.  The name is provided as a number starting at 1.
         */
        public TestScriptOriginComponent setIndex(int value) { 
            if (this.index == null)
              this.index = new IntegerType();
            this.index.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (The type of origin profile the test system supports.)
         */
        public Coding getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptOriginComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Coding(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (The type of origin profile the test system supports.)
         */
        public TestScriptOriginComponent setProfile(Coding value) { 
          this.profile = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("index", "integer", "Abstract name given to an origin server in this test script.  The name is provided as a number starting at 1.", 0, java.lang.Integer.MAX_VALUE, index));
          childrenList.add(new Property("profile", "Coding", "The type of origin profile the test system supports.", 0, java.lang.Integer.MAX_VALUE, profile));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 100346066: /*index*/ return this.index == null ? new Base[0] : new Base[] {this.index}; // IntegerType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 100346066: // index
          this.index = castToInteger(value); // IntegerType
          return value;
        case -309425751: // profile
          this.profile = castToCoding(value); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("index")) {
          this.index = castToInteger(value); // IntegerType
        } else if (name.equals("profile")) {
          this.profile = castToCoding(value); // Coding
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 100346066:  return getIndexElement();
        case -309425751:  return getProfile(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 100346066: /*index*/ return new String[] {"integer"};
        case -309425751: /*profile*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("index")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.index");
        }
        else if (name.equals("profile")) {
          this.profile = new Coding();
          return this.profile;
        }
        else
          return super.addChild(name);
      }

      public TestScriptOriginComponent copy() {
        TestScriptOriginComponent dst = new TestScriptOriginComponent();
        copyValues(dst);
        dst.index = index == null ? null : index.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptOriginComponent))
          return false;
        TestScriptOriginComponent o = (TestScriptOriginComponent) other;
        return compareDeep(index, o.index, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptOriginComponent))
          return false;
        TestScriptOriginComponent o = (TestScriptOriginComponent) other;
        return compareValues(index, o.index, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(index, profile);
      }

  public String fhirType() {
    return "TestScript.origin";

  }

  }

    @Block()
    public static class TestScriptDestinationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Abstract name given to a destination server in this test script.  The name is provided as a number starting at 1.
         */
        @Child(name = "index", type = {IntegerType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The index of the abstract destination server starting at 1", formalDefinition="Abstract name given to a destination server in this test script.  The name is provided as a number starting at 1." )
        protected IntegerType index;

        /**
         * The type of destination profile the test system supports.
         */
        @Child(name = "profile", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="FHIR-Server | FHIR-SDC-FormManager | FHIR-SDC-FormReceiver | FHIR-SDC-FormProcessor", formalDefinition="The type of destination profile the test system supports." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/testscript-profile-destination-types")
        protected Coding profile;

        private static final long serialVersionUID = -1239935149L;

    /**
     * Constructor
     */
      public TestScriptDestinationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptDestinationComponent(IntegerType index, Coding profile) {
        super();
        this.index = index;
        this.profile = profile;
      }

        /**
         * @return {@link #index} (Abstract name given to a destination server in this test script.  The name is provided as a number starting at 1.). This is the underlying object with id, value and extensions. The accessor "getIndex" gives direct access to the value
         */
        public IntegerType getIndexElement() { 
          if (this.index == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptDestinationComponent.index");
            else if (Configuration.doAutoCreate())
              this.index = new IntegerType(); // bb
          return this.index;
        }

        public boolean hasIndexElement() { 
          return this.index != null && !this.index.isEmpty();
        }

        public boolean hasIndex() { 
          return this.index != null && !this.index.isEmpty();
        }

        /**
         * @param value {@link #index} (Abstract name given to a destination server in this test script.  The name is provided as a number starting at 1.). This is the underlying object with id, value and extensions. The accessor "getIndex" gives direct access to the value
         */
        public TestScriptDestinationComponent setIndexElement(IntegerType value) { 
          this.index = value;
          return this;
        }

        /**
         * @return Abstract name given to a destination server in this test script.  The name is provided as a number starting at 1.
         */
        public int getIndex() { 
          return this.index == null || this.index.isEmpty() ? 0 : this.index.getValue();
        }

        /**
         * @param value Abstract name given to a destination server in this test script.  The name is provided as a number starting at 1.
         */
        public TestScriptDestinationComponent setIndex(int value) { 
            if (this.index == null)
              this.index = new IntegerType();
            this.index.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (The type of destination profile the test system supports.)
         */
        public Coding getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptDestinationComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Coding(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (The type of destination profile the test system supports.)
         */
        public TestScriptDestinationComponent setProfile(Coding value) { 
          this.profile = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("index", "integer", "Abstract name given to a destination server in this test script.  The name is provided as a number starting at 1.", 0, java.lang.Integer.MAX_VALUE, index));
          childrenList.add(new Property("profile", "Coding", "The type of destination profile the test system supports.", 0, java.lang.Integer.MAX_VALUE, profile));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 100346066: /*index*/ return this.index == null ? new Base[0] : new Base[] {this.index}; // IntegerType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 100346066: // index
          this.index = castToInteger(value); // IntegerType
          return value;
        case -309425751: // profile
          this.profile = castToCoding(value); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("index")) {
          this.index = castToInteger(value); // IntegerType
        } else if (name.equals("profile")) {
          this.profile = castToCoding(value); // Coding
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 100346066:  return getIndexElement();
        case -309425751:  return getProfile(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 100346066: /*index*/ return new String[] {"integer"};
        case -309425751: /*profile*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("index")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.index");
        }
        else if (name.equals("profile")) {
          this.profile = new Coding();
          return this.profile;
        }
        else
          return super.addChild(name);
      }

      public TestScriptDestinationComponent copy() {
        TestScriptDestinationComponent dst = new TestScriptDestinationComponent();
        copyValues(dst);
        dst.index = index == null ? null : index.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptDestinationComponent))
          return false;
        TestScriptDestinationComponent o = (TestScriptDestinationComponent) other;
        return compareDeep(index, o.index, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptDestinationComponent))
          return false;
        TestScriptDestinationComponent o = (TestScriptDestinationComponent) other;
        return compareValues(index, o.index, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(index, profile);
      }

  public String fhirType() {
    return "TestScript.destination";

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
         * Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.
         */
        @Child(name = "capability", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Capabilities  that are assumed to function correctly on the FHIR server being tested", formalDefinition="Capabilities that must exist and are assumed to function correctly on the FHIR server being tested." )
        protected List<TestScriptMetadataCapabilityComponent> capability;

        private static final long serialVersionUID = 745183328L;

    /**
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestScriptMetadataComponent setLink(List<TestScriptMetadataLinkComponent> theLink) { 
          this.link = theLink;
          return this;
        }

        public boolean hasLink() { 
          if (this.link == null)
            return false;
          for (TestScriptMetadataLinkComponent item : this.link)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public TestScriptMetadataLinkComponent addLink() { //3
          TestScriptMetadataLinkComponent t = new TestScriptMetadataLinkComponent();
          if (this.link == null)
            this.link = new ArrayList<TestScriptMetadataLinkComponent>();
          this.link.add(t);
          return t;
        }

        public TestScriptMetadataComponent addLink(TestScriptMetadataLinkComponent t) { //3
          if (t == null)
            return this;
          if (this.link == null)
            this.link = new ArrayList<TestScriptMetadataLinkComponent>();
          this.link.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #link}, creating it if it does not already exist
         */
        public TestScriptMetadataLinkComponent getLinkFirstRep() { 
          if (getLink().isEmpty()) {
            addLink();
          }
          return getLink().get(0);
        }

        /**
         * @return {@link #capability} (Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.)
         */
        public List<TestScriptMetadataCapabilityComponent> getCapability() { 
          if (this.capability == null)
            this.capability = new ArrayList<TestScriptMetadataCapabilityComponent>();
          return this.capability;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestScriptMetadataComponent setCapability(List<TestScriptMetadataCapabilityComponent> theCapability) { 
          this.capability = theCapability;
          return this;
        }

        public boolean hasCapability() { 
          if (this.capability == null)
            return false;
          for (TestScriptMetadataCapabilityComponent item : this.capability)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public TestScriptMetadataCapabilityComponent addCapability() { //3
          TestScriptMetadataCapabilityComponent t = new TestScriptMetadataCapabilityComponent();
          if (this.capability == null)
            this.capability = new ArrayList<TestScriptMetadataCapabilityComponent>();
          this.capability.add(t);
          return t;
        }

        public TestScriptMetadataComponent addCapability(TestScriptMetadataCapabilityComponent t) { //3
          if (t == null)
            return this;
          if (this.capability == null)
            this.capability = new ArrayList<TestScriptMetadataCapabilityComponent>();
          this.capability.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #capability}, creating it if it does not already exist
         */
        public TestScriptMetadataCapabilityComponent getCapabilityFirstRep() { 
          if (getCapability().isEmpty()) {
            addCapability();
          }
          return getCapability().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("link", "", "A link to the FHIR specification that this test is covering.", 0, java.lang.Integer.MAX_VALUE, link));
          childrenList.add(new Property("capability", "", "Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, capability));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3321850: /*link*/ return this.link == null ? new Base[0] : this.link.toArray(new Base[this.link.size()]); // TestScriptMetadataLinkComponent
        case -783669992: /*capability*/ return this.capability == null ? new Base[0] : this.capability.toArray(new Base[this.capability.size()]); // TestScriptMetadataCapabilityComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3321850: // link
          this.getLink().add((TestScriptMetadataLinkComponent) value); // TestScriptMetadataLinkComponent
          return value;
        case -783669992: // capability
          this.getCapability().add((TestScriptMetadataCapabilityComponent) value); // TestScriptMetadataCapabilityComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("link")) {
          this.getLink().add((TestScriptMetadataLinkComponent) value);
        } else if (name.equals("capability")) {
          this.getCapability().add((TestScriptMetadataCapabilityComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3321850:  return addLink(); 
        case -783669992:  return addCapability(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3321850: /*link*/ return new String[] {};
        case -783669992: /*capability*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("link")) {
          return addLink();
        }
        else if (name.equals("capability")) {
          return addCapability();
        }
        else
          return super.addChild(name);
      }

      public TestScriptMetadataComponent copy() {
        TestScriptMetadataComponent dst = new TestScriptMetadataComponent();
        copyValues(dst);
        if (link != null) {
          dst.link = new ArrayList<TestScriptMetadataLinkComponent>();
          for (TestScriptMetadataLinkComponent i : link)
            dst.link.add(i.copy());
        };
        if (capability != null) {
          dst.capability = new ArrayList<TestScriptMetadataCapabilityComponent>();
          for (TestScriptMetadataCapabilityComponent i : capability)
            dst.capability.add(i.copy());
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
        return compareDeep(link, o.link, true) && compareDeep(capability, o.capability, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(link, capability);
      }

  public String fhirType() {
    return "TestScript.metadata";

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

    /**
     * Constructor
     */
      public TestScriptMetadataLinkComponent() {
        super();
      }

    /**
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1724546052: /*description*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.url");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else
          return super.addChild(name);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(url, description);
      }

  public String fhirType() {
    return "TestScript.metadata.link";

  }

  }

    @Block()
    public static class TestScriptMetadataCapabilityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Whether or not the test execution will require the given capabilities of the server in order for this test script to execute.
         */
        @Child(name = "required", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Are the capabilities required?", formalDefinition="Whether or not the test execution will require the given capabilities of the server in order for this test script to execute." )
        protected BooleanType required;

        /**
         * Whether or not the test execution will validate the given capabilities of the server in order for this test script to execute.
         */
        @Child(name = "validated", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Are the capabilities validated?", formalDefinition="Whether or not the test execution will validate the given capabilities of the server in order for this test script to execute." )
        protected BooleanType validated;

        /**
         * Description of the capabilities that this test script is requiring the server to support.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The expected capabilities of the server", formalDefinition="Description of the capabilities that this test script is requiring the server to support." )
        protected StringType description;

        /**
         * Which origin server these requirements apply to.
         */
        @Child(name = "origin", type = {IntegerType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Which origin server these requirements apply to", formalDefinition="Which origin server these requirements apply to." )
        protected List<IntegerType> origin;

        /**
         * Which server these requirements apply to.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Which server these requirements apply to", formalDefinition="Which server these requirements apply to." )
        protected IntegerType destination;

        /**
         * Links to the FHIR specification that describes this interaction and the resources involved in more detail.
         */
        @Child(name = "link", type = {UriType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Links to the FHIR specification", formalDefinition="Links to the FHIR specification that describes this interaction and the resources involved in more detail." )
        protected List<UriType> link;

        /**
         * Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped.
         */
        @Child(name = "capabilities", type = {CapabilityStatement.class}, order=7, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Required Capability Statement", formalDefinition="Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped." )
        protected Reference capabilities;

        /**
         * The actual object that is the target of the reference (Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped.)
         */
        protected CapabilityStatement capabilitiesTarget;

        private static final long serialVersionUID = -106110735L;

    /**
     * Constructor
     */
      public TestScriptMetadataCapabilityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptMetadataCapabilityComponent(Reference capabilities) {
        super();
        this.capabilities = capabilities;
      }

        /**
         * @return {@link #required} (Whether or not the test execution will require the given capabilities of the server in order for this test script to execute.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public BooleanType getRequiredElement() { 
          if (this.required == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.required");
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
         * @param value {@link #required} (Whether or not the test execution will require the given capabilities of the server in order for this test script to execute.). This is the underlying object with id, value and extensions. The accessor "getRequired" gives direct access to the value
         */
        public TestScriptMetadataCapabilityComponent setRequiredElement(BooleanType value) { 
          this.required = value;
          return this;
        }

        /**
         * @return Whether or not the test execution will require the given capabilities of the server in order for this test script to execute.
         */
        public boolean getRequired() { 
          return this.required == null || this.required.isEmpty() ? false : this.required.getValue();
        }

        /**
         * @param value Whether or not the test execution will require the given capabilities of the server in order for this test script to execute.
         */
        public TestScriptMetadataCapabilityComponent setRequired(boolean value) { 
            if (this.required == null)
              this.required = new BooleanType();
            this.required.setValue(value);
          return this;
        }

        /**
         * @return {@link #validated} (Whether or not the test execution will validate the given capabilities of the server in order for this test script to execute.). This is the underlying object with id, value and extensions. The accessor "getValidated" gives direct access to the value
         */
        public BooleanType getValidatedElement() { 
          if (this.validated == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.validated");
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
         * @param value {@link #validated} (Whether or not the test execution will validate the given capabilities of the server in order for this test script to execute.). This is the underlying object with id, value and extensions. The accessor "getValidated" gives direct access to the value
         */
        public TestScriptMetadataCapabilityComponent setValidatedElement(BooleanType value) { 
          this.validated = value;
          return this;
        }

        /**
         * @return Whether or not the test execution will validate the given capabilities of the server in order for this test script to execute.
         */
        public boolean getValidated() { 
          return this.validated == null || this.validated.isEmpty() ? false : this.validated.getValue();
        }

        /**
         * @param value Whether or not the test execution will validate the given capabilities of the server in order for this test script to execute.
         */
        public TestScriptMetadataCapabilityComponent setValidated(boolean value) { 
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
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.description");
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
        public TestScriptMetadataCapabilityComponent setDescriptionElement(StringType value) { 
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
        public TestScriptMetadataCapabilityComponent setDescription(String value) { 
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
         * @return {@link #origin} (Which origin server these requirements apply to.)
         */
        public List<IntegerType> getOrigin() { 
          if (this.origin == null)
            this.origin = new ArrayList<IntegerType>();
          return this.origin;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestScriptMetadataCapabilityComponent setOrigin(List<IntegerType> theOrigin) { 
          this.origin = theOrigin;
          return this;
        }

        public boolean hasOrigin() { 
          if (this.origin == null)
            return false;
          for (IntegerType item : this.origin)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #origin} (Which origin server these requirements apply to.)
         */
        public IntegerType addOriginElement() {//2 
          IntegerType t = new IntegerType();
          if (this.origin == null)
            this.origin = new ArrayList<IntegerType>();
          this.origin.add(t);
          return t;
        }

        /**
         * @param value {@link #origin} (Which origin server these requirements apply to.)
         */
        public TestScriptMetadataCapabilityComponent addOrigin(int value) { //1
          IntegerType t = new IntegerType();
          t.setValue(value);
          if (this.origin == null)
            this.origin = new ArrayList<IntegerType>();
          this.origin.add(t);
          return this;
        }

        /**
         * @param value {@link #origin} (Which origin server these requirements apply to.)
         */
        public boolean hasOrigin(int value) { 
          if (this.origin == null)
            return false;
          for (IntegerType v : this.origin)
            if (v.equals(value)) // integer
              return true;
          return false;
        }

        /**
         * @return {@link #destination} (Which server these requirements apply to.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.destination");
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
        public TestScriptMetadataCapabilityComponent setDestinationElement(IntegerType value) { 
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
        public TestScriptMetadataCapabilityComponent setDestination(int value) { 
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestScriptMetadataCapabilityComponent setLink(List<UriType> theLink) { 
          this.link = theLink;
          return this;
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
        public TestScriptMetadataCapabilityComponent addLink(String value) { //1
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
         * @return {@link #capabilities} (Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped.)
         */
        public Reference getCapabilities() { 
          if (this.capabilities == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.capabilities");
            else if (Configuration.doAutoCreate())
              this.capabilities = new Reference(); // cc
          return this.capabilities;
        }

        public boolean hasCapabilities() { 
          return this.capabilities != null && !this.capabilities.isEmpty();
        }

        /**
         * @param value {@link #capabilities} (Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped.)
         */
        public TestScriptMetadataCapabilityComponent setCapabilities(Reference value) { 
          this.capabilities = value;
          return this;
        }

        /**
         * @return {@link #capabilities} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped.)
         */
        public CapabilityStatement getCapabilitiesTarget() { 
          if (this.capabilitiesTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.capabilities");
            else if (Configuration.doAutoCreate())
              this.capabilitiesTarget = new CapabilityStatement(); // aa
          return this.capabilitiesTarget;
        }

        /**
         * @param value {@link #capabilities} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped.)
         */
        public TestScriptMetadataCapabilityComponent setCapabilitiesTarget(CapabilityStatement value) { 
          this.capabilitiesTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("required", "boolean", "Whether or not the test execution will require the given capabilities of the server in order for this test script to execute.", 0, java.lang.Integer.MAX_VALUE, required));
          childrenList.add(new Property("validated", "boolean", "Whether or not the test execution will validate the given capabilities of the server in order for this test script to execute.", 0, java.lang.Integer.MAX_VALUE, validated));
          childrenList.add(new Property("description", "string", "Description of the capabilities that this test script is requiring the server to support.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("origin", "integer", "Which origin server these requirements apply to.", 0, java.lang.Integer.MAX_VALUE, origin));
          childrenList.add(new Property("destination", "integer", "Which server these requirements apply to.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("link", "uri", "Links to the FHIR specification that describes this interaction and the resources involved in more detail.", 0, java.lang.Integer.MAX_VALUE, link));
          childrenList.add(new Property("capabilities", "Reference(CapabilityStatement)", "Minimum capabilities required of server for test script to execute successfully.   If server does not meet at a minimum the referenced capability statement, then all tests in this script are skipped.", 0, java.lang.Integer.MAX_VALUE, capabilities));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -393139297: /*required*/ return this.required == null ? new Base[0] : new Base[] {this.required}; // BooleanType
        case -1109784050: /*validated*/ return this.validated == null ? new Base[0] : new Base[] {this.validated}; // BooleanType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1008619738: /*origin*/ return this.origin == null ? new Base[0] : this.origin.toArray(new Base[this.origin.size()]); // IntegerType
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : new Base[] {this.destination}; // IntegerType
        case 3321850: /*link*/ return this.link == null ? new Base[0] : this.link.toArray(new Base[this.link.size()]); // UriType
        case -1487597642: /*capabilities*/ return this.capabilities == null ? new Base[0] : new Base[] {this.capabilities}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -393139297: // required
          this.required = castToBoolean(value); // BooleanType
          return value;
        case -1109784050: // validated
          this.validated = castToBoolean(value); // BooleanType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1008619738: // origin
          this.getOrigin().add(castToInteger(value)); // IntegerType
          return value;
        case -1429847026: // destination
          this.destination = castToInteger(value); // IntegerType
          return value;
        case 3321850: // link
          this.getLink().add(castToUri(value)); // UriType
          return value;
        case -1487597642: // capabilities
          this.capabilities = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("required")) {
          this.required = castToBoolean(value); // BooleanType
        } else if (name.equals("validated")) {
          this.validated = castToBoolean(value); // BooleanType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("origin")) {
          this.getOrigin().add(castToInteger(value));
        } else if (name.equals("destination")) {
          this.destination = castToInteger(value); // IntegerType
        } else if (name.equals("link")) {
          this.getLink().add(castToUri(value));
        } else if (name.equals("capabilities")) {
          this.capabilities = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -393139297:  return getRequiredElement();
        case -1109784050:  return getValidatedElement();
        case -1724546052:  return getDescriptionElement();
        case -1008619738:  return addOriginElement();
        case -1429847026:  return getDestinationElement();
        case 3321850:  return addLinkElement();
        case -1487597642:  return getCapabilities(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -393139297: /*required*/ return new String[] {"boolean"};
        case -1109784050: /*validated*/ return new String[] {"boolean"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1008619738: /*origin*/ return new String[] {"integer"};
        case -1429847026: /*destination*/ return new String[] {"integer"};
        case 3321850: /*link*/ return new String[] {"uri"};
        case -1487597642: /*capabilities*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("required")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.required");
        }
        else if (name.equals("validated")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.validated");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else if (name.equals("origin")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.origin");
        }
        else if (name.equals("destination")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.destination");
        }
        else if (name.equals("link")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.link");
        }
        else if (name.equals("capabilities")) {
          this.capabilities = new Reference();
          return this.capabilities;
        }
        else
          return super.addChild(name);
      }

      public TestScriptMetadataCapabilityComponent copy() {
        TestScriptMetadataCapabilityComponent dst = new TestScriptMetadataCapabilityComponent();
        copyValues(dst);
        dst.required = required == null ? null : required.copy();
        dst.validated = validated == null ? null : validated.copy();
        dst.description = description == null ? null : description.copy();
        if (origin != null) {
          dst.origin = new ArrayList<IntegerType>();
          for (IntegerType i : origin)
            dst.origin.add(i.copy());
        };
        dst.destination = destination == null ? null : destination.copy();
        if (link != null) {
          dst.link = new ArrayList<UriType>();
          for (UriType i : link)
            dst.link.add(i.copy());
        };
        dst.capabilities = capabilities == null ? null : capabilities.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptMetadataCapabilityComponent))
          return false;
        TestScriptMetadataCapabilityComponent o = (TestScriptMetadataCapabilityComponent) other;
        return compareDeep(required, o.required, true) && compareDeep(validated, o.validated, true) && compareDeep(description, o.description, true)
           && compareDeep(origin, o.origin, true) && compareDeep(destination, o.destination, true) && compareDeep(link, o.link, true)
           && compareDeep(capabilities, o.capabilities, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptMetadataCapabilityComponent))
          return false;
        TestScriptMetadataCapabilityComponent o = (TestScriptMetadataCapabilityComponent) other;
        return compareValues(required, o.required, true) && compareValues(validated, o.validated, true) && compareValues(description, o.description, true)
           && compareValues(origin, o.origin, true) && compareValues(destination, o.destination, true) && compareValues(link, o.link, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(required, validated, description
          , origin, destination, link, capabilities);
      }

  public String fhirType() {
    return "TestScript.metadata.capability";

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
         * Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.
         */
        @Child(name = "autodelete", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not to implicitly delete the fixture during teardown", formalDefinition="Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section." )
        protected BooleanType autodelete;

        /**
         * Reference to the resource (containing the contents of the resource needed for operations).
         */
        @Child(name = "resource", type = {Reference.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference of the resource", formalDefinition="Reference to the resource (containing the contents of the resource needed for operations)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (Reference to the resource (containing the contents of the resource needed for operations).)
         */
        protected Resource resourceTarget;

        private static final long serialVersionUID = 1110683307L;

    /**
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
         * @return {@link #autodelete} (Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.). This is the underlying object with id, value and extensions. The accessor "getAutodelete" gives direct access to the value
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
         * @param value {@link #autodelete} (Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.). This is the underlying object with id, value and extensions. The accessor "getAutodelete" gives direct access to the value
         */
        public TestScriptFixtureComponent setAutodeleteElement(BooleanType value) { 
          this.autodelete = value;
          return this;
        }

        /**
         * @return Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.
         */
        public boolean getAutodelete() { 
          return this.autodelete == null || this.autodelete.isEmpty() ? false : this.autodelete.getValue();
        }

        /**
         * @param value Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.
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
          childrenList.add(new Property("autodelete", "boolean", "Whether or not to implicitly delete the fixture during teardown. If true, the fixture is automatically deleted on each server being tested during teardown, therefore no delete operation is required for this fixture in the TestScript.teardown section.", 0, java.lang.Integer.MAX_VALUE, autodelete));
          childrenList.add(new Property("resource", "Reference(Any)", "Reference to the resource (containing the contents of the resource needed for operations).", 0, java.lang.Integer.MAX_VALUE, resource));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 73154411: /*autocreate*/ return this.autocreate == null ? new Base[0] : new Base[] {this.autocreate}; // BooleanType
        case 89990170: /*autodelete*/ return this.autodelete == null ? new Base[0] : new Base[] {this.autodelete}; // BooleanType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 73154411: // autocreate
          this.autocreate = castToBoolean(value); // BooleanType
          return value;
        case 89990170: // autodelete
          this.autodelete = castToBoolean(value); // BooleanType
          return value;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("autocreate")) {
          this.autocreate = castToBoolean(value); // BooleanType
        } else if (name.equals("autodelete")) {
          this.autodelete = castToBoolean(value); // BooleanType
        } else if (name.equals("resource")) {
          this.resource = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 73154411:  return getAutocreateElement();
        case 89990170:  return getAutodeleteElement();
        case -341064690:  return getResource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 73154411: /*autocreate*/ return new String[] {"boolean"};
        case 89990170: /*autodelete*/ return new String[] {"boolean"};
        case -341064690: /*resource*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("autocreate")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.autocreate");
        }
        else if (name.equals("autodelete")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.autodelete");
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else
          return super.addChild(name);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(autocreate, autodelete, resource
          );
      }

  public String fhirType() {
    return "TestScript.fixture";

  }

  }

    @Block()
    public static class TestScriptVariableComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Descriptive name for this variable.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Descriptive name for this variable", formalDefinition="Descriptive name for this variable." )
        protected StringType name;

        /**
         * A default, hard-coded, or user-defined value for this variable.
         */
        @Child(name = "defaultValue", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Default, hard-coded, or user-defined value for this variable", formalDefinition="A default, hard-coded, or user-defined value for this variable." )
        protected StringType defaultValue;

        /**
         * A free text natural language description of the variable and its purpose.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the variable", formalDefinition="A free text natural language description of the variable and its purpose." )
        protected StringType description;

        /**
         * The fluentpath expression to evaluate against the fixture body. When variables are defined, only one of either expression, headerField or path must be specified.
         */
        @Child(name = "expression", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The fluentpath expression against the fixture body", formalDefinition="The fluentpath expression to evaluate against the fixture body. When variables are defined, only one of either expression, headerField or path must be specified." )
        protected StringType expression;

        /**
         * Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.
         */
        @Child(name = "headerField", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP header field name for source", formalDefinition="Will be used to grab the HTTP header field value from the headers that sourceId is pointing to." )
        protected StringType headerField;

        /**
         * Displayable text string with hint help information to the user when entering a default value.
         */
        @Child(name = "hint", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Hint help text for default value to enter", formalDefinition="Displayable text string with hint help information to the user when entering a default value." )
        protected StringType hint;

        /**
         * XPath or JSONPath to evaluate against the fixture body.  When variables are defined, only one of either expression, headerField or path must be specified.
         */
        @Child(name = "path", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="XPath or JSONPath against the fixture body", formalDefinition="XPath or JSONPath to evaluate against the fixture body.  When variables are defined, only one of either expression, headerField or path must be specified." )
        protected StringType path;

        /**
         * Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of source expression or headerField within this variable", formalDefinition="Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable." )
        protected IdType sourceId;

        private static final long serialVersionUID = -1592325432L;

    /**
     * Constructor
     */
      public TestScriptVariableComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptVariableComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Descriptive name for this variable.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
         * @param value {@link #name} (Descriptive name for this variable.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public TestScriptVariableComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Descriptive name for this variable.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Descriptive name for this variable.
         */
        public TestScriptVariableComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #defaultValue} (A default, hard-coded, or user-defined value for this variable.). This is the underlying object with id, value and extensions. The accessor "getDefaultValue" gives direct access to the value
         */
        public StringType getDefaultValueElement() { 
          if (this.defaultValue == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.defaultValue");
            else if (Configuration.doAutoCreate())
              this.defaultValue = new StringType(); // bb
          return this.defaultValue;
        }

        public boolean hasDefaultValueElement() { 
          return this.defaultValue != null && !this.defaultValue.isEmpty();
        }

        public boolean hasDefaultValue() { 
          return this.defaultValue != null && !this.defaultValue.isEmpty();
        }

        /**
         * @param value {@link #defaultValue} (A default, hard-coded, or user-defined value for this variable.). This is the underlying object with id, value and extensions. The accessor "getDefaultValue" gives direct access to the value
         */
        public TestScriptVariableComponent setDefaultValueElement(StringType value) { 
          this.defaultValue = value;
          return this;
        }

        /**
         * @return A default, hard-coded, or user-defined value for this variable.
         */
        public String getDefaultValue() { 
          return this.defaultValue == null ? null : this.defaultValue.getValue();
        }

        /**
         * @param value A default, hard-coded, or user-defined value for this variable.
         */
        public TestScriptVariableComponent setDefaultValue(String value) { 
          if (Utilities.noString(value))
            this.defaultValue = null;
          else {
            if (this.defaultValue == null)
              this.defaultValue = new StringType();
            this.defaultValue.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (A free text natural language description of the variable and its purpose.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.description");
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
         * @param value {@link #description} (A free text natural language description of the variable and its purpose.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestScriptVariableComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A free text natural language description of the variable and its purpose.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A free text natural language description of the variable and its purpose.
         */
        public TestScriptVariableComponent setDescription(String value) { 
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
         * @return {@link #expression} (The fluentpath expression to evaluate against the fixture body. When variables are defined, only one of either expression, headerField or path must be specified.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (The fluentpath expression to evaluate against the fixture body. When variables are defined, only one of either expression, headerField or path must be specified.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public TestScriptVariableComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return The fluentpath expression to evaluate against the fixture body. When variables are defined, only one of either expression, headerField or path must be specified.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value The fluentpath expression to evaluate against the fixture body. When variables are defined, only one of either expression, headerField or path must be specified.
         */
        public TestScriptVariableComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #headerField} (Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
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
         * @param value {@link #headerField} (Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
         */
        public TestScriptVariableComponent setHeaderFieldElement(StringType value) { 
          this.headerField = value;
          return this;
        }

        /**
         * @return Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.
         */
        public String getHeaderField() { 
          return this.headerField == null ? null : this.headerField.getValue();
        }

        /**
         * @param value Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.
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
         * @return {@link #hint} (Displayable text string with hint help information to the user when entering a default value.). This is the underlying object with id, value and extensions. The accessor "getHint" gives direct access to the value
         */
        public StringType getHintElement() { 
          if (this.hint == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptVariableComponent.hint");
            else if (Configuration.doAutoCreate())
              this.hint = new StringType(); // bb
          return this.hint;
        }

        public boolean hasHintElement() { 
          return this.hint != null && !this.hint.isEmpty();
        }

        public boolean hasHint() { 
          return this.hint != null && !this.hint.isEmpty();
        }

        /**
         * @param value {@link #hint} (Displayable text string with hint help information to the user when entering a default value.). This is the underlying object with id, value and extensions. The accessor "getHint" gives direct access to the value
         */
        public TestScriptVariableComponent setHintElement(StringType value) { 
          this.hint = value;
          return this;
        }

        /**
         * @return Displayable text string with hint help information to the user when entering a default value.
         */
        public String getHint() { 
          return this.hint == null ? null : this.hint.getValue();
        }

        /**
         * @param value Displayable text string with hint help information to the user when entering a default value.
         */
        public TestScriptVariableComponent setHint(String value) { 
          if (Utilities.noString(value))
            this.hint = null;
          else {
            if (this.hint == null)
              this.hint = new StringType();
            this.hint.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #path} (XPath or JSONPath to evaluate against the fixture body.  When variables are defined, only one of either expression, headerField or path must be specified.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
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
         * @param value {@link #path} (XPath or JSONPath to evaluate against the fixture body.  When variables are defined, only one of either expression, headerField or path must be specified.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public TestScriptVariableComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return XPath or JSONPath to evaluate against the fixture body.  When variables are defined, only one of either expression, headerField or path must be specified.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value XPath or JSONPath to evaluate against the fixture body.  When variables are defined, only one of either expression, headerField or path must be specified.
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
         * @return {@link #sourceId} (Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
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
         * @param value {@link #sourceId} (Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public TestScriptVariableComponent setSourceIdElement(IdType value) { 
          this.sourceId = value;
          return this;
        }

        /**
         * @return Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.
         */
        public String getSourceId() { 
          return this.sourceId == null ? null : this.sourceId.getValue();
        }

        /**
         * @param value Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.
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
          childrenList.add(new Property("name", "string", "Descriptive name for this variable.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("defaultValue", "string", "A default, hard-coded, or user-defined value for this variable.", 0, java.lang.Integer.MAX_VALUE, defaultValue));
          childrenList.add(new Property("description", "string", "A free text natural language description of the variable and its purpose.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("expression", "string", "The fluentpath expression to evaluate against the fixture body. When variables are defined, only one of either expression, headerField or path must be specified.", 0, java.lang.Integer.MAX_VALUE, expression));
          childrenList.add(new Property("headerField", "string", "Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.", 0, java.lang.Integer.MAX_VALUE, headerField));
          childrenList.add(new Property("hint", "string", "Displayable text string with hint help information to the user when entering a default value.", 0, java.lang.Integer.MAX_VALUE, hint));
          childrenList.add(new Property("path", "string", "XPath or JSONPath to evaluate against the fixture body.  When variables are defined, only one of either expression, headerField or path must be specified.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("sourceId", "id", "Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.", 0, java.lang.Integer.MAX_VALUE, sourceId));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -659125328: /*defaultValue*/ return this.defaultValue == null ? new Base[0] : new Base[] {this.defaultValue}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        case 1160732269: /*headerField*/ return this.headerField == null ? new Base[0] : new Base[] {this.headerField}; // StringType
        case 3202695: /*hint*/ return this.hint == null ? new Base[0] : new Base[] {this.hint}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case 1746327190: /*sourceId*/ return this.sourceId == null ? new Base[0] : new Base[] {this.sourceId}; // IdType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -659125328: // defaultValue
          this.defaultValue = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        case 1160732269: // headerField
          this.headerField = castToString(value); // StringType
          return value;
        case 3202695: // hint
          this.hint = castToString(value); // StringType
          return value;
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case 1746327190: // sourceId
          this.sourceId = castToId(value); // IdType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("defaultValue")) {
          this.defaultValue = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else if (name.equals("headerField")) {
          this.headerField = castToString(value); // StringType
        } else if (name.equals("hint")) {
          this.hint = castToString(value); // StringType
        } else if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("sourceId")) {
          this.sourceId = castToId(value); // IdType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -659125328:  return getDefaultValueElement();
        case -1724546052:  return getDescriptionElement();
        case -1795452264:  return getExpressionElement();
        case 1160732269:  return getHeaderFieldElement();
        case 3202695:  return getHintElement();
        case 3433509:  return getPathElement();
        case 1746327190:  return getSourceIdElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -659125328: /*defaultValue*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        case 1160732269: /*headerField*/ return new String[] {"string"};
        case 3202695: /*hint*/ return new String[] {"string"};
        case 3433509: /*path*/ return new String[] {"string"};
        case 1746327190: /*sourceId*/ return new String[] {"id"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("defaultValue")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.defaultValue");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.expression");
        }
        else if (name.equals("headerField")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.headerField");
        }
        else if (name.equals("hint")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.hint");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.path");
        }
        else if (name.equals("sourceId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.sourceId");
        }
        else
          return super.addChild(name);
      }

      public TestScriptVariableComponent copy() {
        TestScriptVariableComponent dst = new TestScriptVariableComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.defaultValue = defaultValue == null ? null : defaultValue.copy();
        dst.description = description == null ? null : description.copy();
        dst.expression = expression == null ? null : expression.copy();
        dst.headerField = headerField == null ? null : headerField.copy();
        dst.hint = hint == null ? null : hint.copy();
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
        return compareDeep(name, o.name, true) && compareDeep(defaultValue, o.defaultValue, true) && compareDeep(description, o.description, true)
           && compareDeep(expression, o.expression, true) && compareDeep(headerField, o.headerField, true)
           && compareDeep(hint, o.hint, true) && compareDeep(path, o.path, true) && compareDeep(sourceId, o.sourceId, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptVariableComponent))
          return false;
        TestScriptVariableComponent o = (TestScriptVariableComponent) other;
        return compareValues(name, o.name, true) && compareValues(defaultValue, o.defaultValue, true) && compareValues(description, o.description, true)
           && compareValues(expression, o.expression, true) && compareValues(headerField, o.headerField, true)
           && compareValues(hint, o.hint, true) && compareValues(path, o.path, true) && compareValues(sourceId, o.sourceId, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, defaultValue, description
          , expression, headerField, hint, path, sourceId);
      }

  public String fhirType() {
    return "TestScript.variable";

  }

  }

    @Block()
    public static class TestScriptRuleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to the resource (containing the contents of the rule needed for assertions).
         */
        @Child(name = "resource", type = {Reference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Assert rule resource reference", formalDefinition="Reference to the resource (containing the contents of the rule needed for assertions)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (Reference to the resource (containing the contents of the rule needed for assertions).)
         */
        protected Resource resourceTarget;

        /**
         * Each rule template can take one or more parameters for rule evaluation.
         */
        @Child(name = "param", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Rule parameter template", formalDefinition="Each rule template can take one or more parameters for rule evaluation." )
        protected List<RuleParamComponent> param;

        private static final long serialVersionUID = -1869267735L;

    /**
     * Constructor
     */
      public TestScriptRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptRuleComponent(Reference resource) {
        super();
        this.resource = resource;
      }

        /**
         * @return {@link #resource} (Reference to the resource (containing the contents of the rule needed for assertions).)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptRuleComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new Reference(); // cc
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (Reference to the resource (containing the contents of the rule needed for assertions).)
         */
        public TestScriptRuleComponent setResource(Reference value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the resource (containing the contents of the rule needed for assertions).)
         */
        public Resource getResourceTarget() { 
          return this.resourceTarget;
        }

        /**
         * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the resource (containing the contents of the rule needed for assertions).)
         */
        public TestScriptRuleComponent setResourceTarget(Resource value) { 
          this.resourceTarget = value;
          return this;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
        public List<RuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<RuleParamComponent>();
          return this.param;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestScriptRuleComponent setParam(List<RuleParamComponent> theParam) { 
          this.param = theParam;
          return this;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (RuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public RuleParamComponent addParam() { //3
          RuleParamComponent t = new RuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<RuleParamComponent>();
          this.param.add(t);
          return t;
        }

        public TestScriptRuleComponent addParam(RuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<RuleParamComponent>();
          this.param.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #param}, creating it if it does not already exist
         */
        public RuleParamComponent getParamFirstRep() { 
          if (getParam().isEmpty()) {
            addParam();
          }
          return getParam().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("resource", "Reference(Any)", "Reference to the resource (containing the contents of the rule needed for assertions).", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("param", "", "Each rule template can take one or more parameters for rule evaluation.", 0, java.lang.Integer.MAX_VALUE, param));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // RuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          return value;
        case 106436749: // param
          this.getParam().add((RuleParamComponent) value); // RuleParamComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("resource")) {
          this.resource = castToReference(value); // Reference
        } else if (name.equals("param")) {
          this.getParam().add((RuleParamComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -341064690:  return getResource(); 
        case 106436749:  return addParam(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -341064690: /*resource*/ return new String[] {"Reference"};
        case 106436749: /*param*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else if (name.equals("param")) {
          return addParam();
        }
        else
          return super.addChild(name);
      }

      public TestScriptRuleComponent copy() {
        TestScriptRuleComponent dst = new TestScriptRuleComponent();
        copyValues(dst);
        dst.resource = resource == null ? null : resource.copy();
        if (param != null) {
          dst.param = new ArrayList<RuleParamComponent>();
          for (RuleParamComponent i : param)
            dst.param.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptRuleComponent))
          return false;
        TestScriptRuleComponent o = (TestScriptRuleComponent) other;
        return compareDeep(resource, o.resource, true) && compareDeep(param, o.param, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptRuleComponent))
          return false;
        TestScriptRuleComponent o = (TestScriptRuleComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(resource, param);
      }

  public String fhirType() {
    return "TestScript.rule";

  }

  }

    @Block()
    public static class RuleParamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Descriptive name for this parameter that matches the external assert rule parameter name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter name matching external assert rule parameter", formalDefinition="Descriptive name for this parameter that matches the external assert rule parameter name." )
        protected StringType name;

        /**
         * The explicit or dynamic value for the parameter that will be passed on to the external rule template.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter value defined either explicitly or dynamically", formalDefinition="The explicit or dynamic value for the parameter that will be passed on to the external rule template." )
        protected StringType value;

        private static final long serialVersionUID = 395259392L;

    /**
     * Constructor
     */
      public RuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public RuleParamComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Descriptive name for this parameter that matches the external assert rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RuleParamComponent.name");
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
         * @param value {@link #name} (Descriptive name for this parameter that matches the external assert rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public RuleParamComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Descriptive name for this parameter that matches the external assert rule parameter name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Descriptive name for this parameter that matches the external assert rule parameter name.
         */
        public RuleParamComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The explicit or dynamic value for the parameter that will be passed on to the external rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RuleParamComponent.value");
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
         * @param value {@link #value} (The explicit or dynamic value for the parameter that will be passed on to the external rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public RuleParamComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The explicit or dynamic value for the parameter that will be passed on to the external rule template.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The explicit or dynamic value for the parameter that will be passed on to the external rule template.
         */
        public RuleParamComponent setValue(String value) { 
          if (Utilities.noString(value))
            this.value = null;
          else {
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Descriptive name for this parameter that matches the external assert rule parameter name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value", "string", "The explicit or dynamic value for the parameter that will be passed on to the external rule template.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.value");
        }
        else
          return super.addChild(name);
      }

      public RuleParamComponent copy() {
        RuleParamComponent dst = new RuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RuleParamComponent))
          return false;
        RuleParamComponent o = (RuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RuleParamComponent))
          return false;
        RuleParamComponent o = (RuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, value);
      }

  public String fhirType() {
    return "TestScript.rule.param";

  }

  }

    @Block()
    public static class TestScriptRulesetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to the resource (containing the contents of the ruleset needed for assertions).
         */
        @Child(name = "resource", type = {Reference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Assert ruleset resource reference", formalDefinition="Reference to the resource (containing the contents of the ruleset needed for assertions)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (Reference to the resource (containing the contents of the ruleset needed for assertions).)
         */
        protected Resource resourceTarget;

        /**
         * The referenced rule within the external ruleset template.
         */
        @Child(name = "rule", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The referenced rule within the ruleset", formalDefinition="The referenced rule within the external ruleset template." )
        protected List<RulesetRuleComponent> rule;

        private static final long serialVersionUID = 1260261423L;

    /**
     * Constructor
     */
      public TestScriptRulesetComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptRulesetComponent(Reference resource) {
        super();
        this.resource = resource;
      }

        /**
         * @return {@link #resource} (Reference to the resource (containing the contents of the ruleset needed for assertions).)
         */
        public Reference getResource() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptRulesetComponent.resource");
            else if (Configuration.doAutoCreate())
              this.resource = new Reference(); // cc
          return this.resource;
        }

        public boolean hasResource() { 
          return this.resource != null && !this.resource.isEmpty();
        }

        /**
         * @param value {@link #resource} (Reference to the resource (containing the contents of the ruleset needed for assertions).)
         */
        public TestScriptRulesetComponent setResource(Reference value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the resource (containing the contents of the ruleset needed for assertions).)
         */
        public Resource getResourceTarget() { 
          return this.resourceTarget;
        }

        /**
         * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the resource (containing the contents of the ruleset needed for assertions).)
         */
        public TestScriptRulesetComponent setResourceTarget(Resource value) { 
          this.resourceTarget = value;
          return this;
        }

        /**
         * @return {@link #rule} (The referenced rule within the external ruleset template.)
         */
        public List<RulesetRuleComponent> getRule() { 
          if (this.rule == null)
            this.rule = new ArrayList<RulesetRuleComponent>();
          return this.rule;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TestScriptRulesetComponent setRule(List<RulesetRuleComponent> theRule) { 
          this.rule = theRule;
          return this;
        }

        public boolean hasRule() { 
          if (this.rule == null)
            return false;
          for (RulesetRuleComponent item : this.rule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public RulesetRuleComponent addRule() { //3
          RulesetRuleComponent t = new RulesetRuleComponent();
          if (this.rule == null)
            this.rule = new ArrayList<RulesetRuleComponent>();
          this.rule.add(t);
          return t;
        }

        public TestScriptRulesetComponent addRule(RulesetRuleComponent t) { //3
          if (t == null)
            return this;
          if (this.rule == null)
            this.rule = new ArrayList<RulesetRuleComponent>();
          this.rule.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #rule}, creating it if it does not already exist
         */
        public RulesetRuleComponent getRuleFirstRep() { 
          if (getRule().isEmpty()) {
            addRule();
          }
          return getRule().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("resource", "Reference(Any)", "Reference to the resource (containing the contents of the ruleset needed for assertions).", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("rule", "", "The referenced rule within the external ruleset template.", 0, java.lang.Integer.MAX_VALUE, rule));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // RulesetRuleComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          return value;
        case 3512060: // rule
          this.getRule().add((RulesetRuleComponent) value); // RulesetRuleComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("resource")) {
          this.resource = castToReference(value); // Reference
        } else if (name.equals("rule")) {
          this.getRule().add((RulesetRuleComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -341064690:  return getResource(); 
        case 3512060:  return addRule(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -341064690: /*resource*/ return new String[] {"Reference"};
        case 3512060: /*rule*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else if (name.equals("rule")) {
          return addRule();
        }
        else
          return super.addChild(name);
      }

      public TestScriptRulesetComponent copy() {
        TestScriptRulesetComponent dst = new TestScriptRulesetComponent();
        copyValues(dst);
        dst.resource = resource == null ? null : resource.copy();
        if (rule != null) {
          dst.rule = new ArrayList<RulesetRuleComponent>();
          for (RulesetRuleComponent i : rule)
            dst.rule.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptRulesetComponent))
          return false;
        TestScriptRulesetComponent o = (TestScriptRulesetComponent) other;
        return compareDeep(resource, o.resource, true) && compareDeep(rule, o.rule, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptRulesetComponent))
          return false;
        TestScriptRulesetComponent o = (TestScriptRulesetComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(resource, rule);
      }

  public String fhirType() {
    return "TestScript.ruleset";

  }

  }

    @Block()
    public static class RulesetRuleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Id of the referenced rule within the external ruleset template.
         */
        @Child(name = "ruleId", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of referenced rule within the ruleset", formalDefinition="Id of the referenced rule within the external ruleset template." )
        protected IdType ruleId;

        /**
         * Each rule template can take one or more parameters for rule evaluation.
         */
        @Child(name = "param", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Ruleset rule parameter template", formalDefinition="Each rule template can take one or more parameters for rule evaluation." )
        protected List<RulesetRuleParamComponent> param;

        private static final long serialVersionUID = -1399866981L;

    /**
     * Constructor
     */
      public RulesetRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public RulesetRuleComponent(IdType ruleId) {
        super();
        this.ruleId = ruleId;
      }

        /**
         * @return {@link #ruleId} (Id of the referenced rule within the external ruleset template.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public IdType getRuleIdElement() { 
          if (this.ruleId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RulesetRuleComponent.ruleId");
            else if (Configuration.doAutoCreate())
              this.ruleId = new IdType(); // bb
          return this.ruleId;
        }

        public boolean hasRuleIdElement() { 
          return this.ruleId != null && !this.ruleId.isEmpty();
        }

        public boolean hasRuleId() { 
          return this.ruleId != null && !this.ruleId.isEmpty();
        }

        /**
         * @param value {@link #ruleId} (Id of the referenced rule within the external ruleset template.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public RulesetRuleComponent setRuleIdElement(IdType value) { 
          this.ruleId = value;
          return this;
        }

        /**
         * @return Id of the referenced rule within the external ruleset template.
         */
        public String getRuleId() { 
          return this.ruleId == null ? null : this.ruleId.getValue();
        }

        /**
         * @param value Id of the referenced rule within the external ruleset template.
         */
        public RulesetRuleComponent setRuleId(String value) { 
            if (this.ruleId == null)
              this.ruleId = new IdType();
            this.ruleId.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
        public List<RulesetRuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<RulesetRuleParamComponent>();
          return this.param;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public RulesetRuleComponent setParam(List<RulesetRuleParamComponent> theParam) { 
          this.param = theParam;
          return this;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (RulesetRuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public RulesetRuleParamComponent addParam() { //3
          RulesetRuleParamComponent t = new RulesetRuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<RulesetRuleParamComponent>();
          this.param.add(t);
          return t;
        }

        public RulesetRuleComponent addParam(RulesetRuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<RulesetRuleParamComponent>();
          this.param.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #param}, creating it if it does not already exist
         */
        public RulesetRuleParamComponent getParamFirstRep() { 
          if (getParam().isEmpty()) {
            addParam();
          }
          return getParam().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("ruleId", "id", "Id of the referenced rule within the external ruleset template.", 0, java.lang.Integer.MAX_VALUE, ruleId));
          childrenList.add(new Property("param", "", "Each rule template can take one or more parameters for rule evaluation.", 0, java.lang.Integer.MAX_VALUE, param));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -919875273: /*ruleId*/ return this.ruleId == null ? new Base[0] : new Base[] {this.ruleId}; // IdType
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // RulesetRuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -919875273: // ruleId
          this.ruleId = castToId(value); // IdType
          return value;
        case 106436749: // param
          this.getParam().add((RulesetRuleParamComponent) value); // RulesetRuleParamComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("ruleId")) {
          this.ruleId = castToId(value); // IdType
        } else if (name.equals("param")) {
          this.getParam().add((RulesetRuleParamComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273:  return getRuleIdElement();
        case 106436749:  return addParam(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273: /*ruleId*/ return new String[] {"id"};
        case 106436749: /*param*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("ruleId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.ruleId");
        }
        else if (name.equals("param")) {
          return addParam();
        }
        else
          return super.addChild(name);
      }

      public RulesetRuleComponent copy() {
        RulesetRuleComponent dst = new RulesetRuleComponent();
        copyValues(dst);
        dst.ruleId = ruleId == null ? null : ruleId.copy();
        if (param != null) {
          dst.param = new ArrayList<RulesetRuleParamComponent>();
          for (RulesetRuleParamComponent i : param)
            dst.param.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RulesetRuleComponent))
          return false;
        RulesetRuleComponent o = (RulesetRuleComponent) other;
        return compareDeep(ruleId, o.ruleId, true) && compareDeep(param, o.param, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RulesetRuleComponent))
          return false;
        RulesetRuleComponent o = (RulesetRuleComponent) other;
        return compareValues(ruleId, o.ruleId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(ruleId, param);
      }

  public String fhirType() {
    return "TestScript.ruleset.rule";

  }

  }

    @Block()
    public static class RulesetRuleParamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Descriptive name for this parameter that matches the external assert ruleset rule parameter name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter name matching external assert ruleset rule parameter", formalDefinition="Descriptive name for this parameter that matches the external assert ruleset rule parameter name." )
        protected StringType name;

        /**
         * The value for the parameter that will be passed on to the external ruleset rule template.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter value defined either explicitly or dynamically", formalDefinition="The value for the parameter that will be passed on to the external ruleset rule template." )
        protected StringType value;

        private static final long serialVersionUID = 395259392L;

    /**
     * Constructor
     */
      public RulesetRuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public RulesetRuleParamComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Descriptive name for this parameter that matches the external assert ruleset rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RulesetRuleParamComponent.name");
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
         * @param value {@link #name} (Descriptive name for this parameter that matches the external assert ruleset rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public RulesetRuleParamComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Descriptive name for this parameter that matches the external assert ruleset rule parameter name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Descriptive name for this parameter that matches the external assert ruleset rule parameter name.
         */
        public RulesetRuleParamComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value for the parameter that will be passed on to the external ruleset rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create RulesetRuleParamComponent.value");
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
         * @param value {@link #value} (The value for the parameter that will be passed on to the external ruleset rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public RulesetRuleParamComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value for the parameter that will be passed on to the external ruleset rule template.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value for the parameter that will be passed on to the external ruleset rule template.
         */
        public RulesetRuleParamComponent setValue(String value) { 
          if (Utilities.noString(value))
            this.value = null;
          else {
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Descriptive name for this parameter that matches the external assert ruleset rule parameter name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value", "string", "The value for the parameter that will be passed on to the external ruleset rule template.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.value");
        }
        else
          return super.addChild(name);
      }

      public RulesetRuleParamComponent copy() {
        RulesetRuleParamComponent dst = new RulesetRuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RulesetRuleParamComponent))
          return false;
        RulesetRuleParamComponent o = (RulesetRuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RulesetRuleParamComponent))
          return false;
        RulesetRuleParamComponent o = (RulesetRuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, value);
      }

  public String fhirType() {
    return "TestScript.ruleset.rule.param";

  }

  }

    @Block()
    public static class TestScriptSetupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A setup operation or assert to perform", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<SetupActionComponent> action;

        private static final long serialVersionUID = -123374486L;

    /**
     * Constructor
     */
      public TestScriptSetupComponent() {
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
        public TestScriptSetupComponent setAction(List<SetupActionComponent> theAction) { 
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

        public TestScriptSetupComponent addAction(SetupActionComponent t) { //3
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

      public TestScriptSetupComponent copy() {
        TestScriptSetupComponent dst = new TestScriptSetupComponent();
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
        if (!(other instanceof TestScriptSetupComponent))
          return false;
        TestScriptSetupComponent o = (TestScriptSetupComponent) other;
        return compareDeep(action, o.action, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(action);
      }

  public String fhirType() {
    return "TestScript.setup";

  }

  }

    @Block()
    public static class SetupActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The operation to perform.
         */
        @Child(name = "operation", type = {}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The setup operation to perform", formalDefinition="The operation to perform." )
        protected SetupActionOperationComponent operation;

        /**
         * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
         */
        @Child(name = "assert", type = {}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The assertion to perform", formalDefinition="Evaluates the results of previous operations to determine if the server under test behaves appropriately." )
        protected SetupActionAssertComponent assert_;

        private static final long serialVersionUID = -252088305L;

    /**
     * Constructor
     */
      public SetupActionComponent() {
        super();
      }

        /**
         * @return {@link #operation} (The operation to perform.)
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
         * @param value {@link #operation} (The operation to perform.)
         */
        public SetupActionComponent setOperation(SetupActionOperationComponent value) { 
          this.operation = value;
          return this;
        }

        /**
         * @return {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
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
         * @param value {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
         */
        public SetupActionComponent setAssert(SetupActionAssertComponent value) { 
          this.assert_ = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "", "The operation to perform.", 0, java.lang.Integer.MAX_VALUE, operation));
          childrenList.add(new Property("assert", "", "Evaluates the results of previous operations to determine if the server under test behaves appropriately.", 0, java.lang.Integer.MAX_VALUE, assert_));
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
    return "TestScript.setup.action";

  }

  }

    @Block()
    public static class SetupActionOperationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Server interaction or operation type.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The operation code type that will be executed", formalDefinition="Server interaction or operation type." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/testscript-operation-codes")
        protected Coding type;

        /**
         * The type of the resource.  See http://build.fhir.org/resourcelist.html.
         */
        @Child(name = "resource", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource type", formalDefinition="The type of the resource.  See http://build.fhir.org/resourcelist.html." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/defined-types")
        protected CodeType resource;

        /**
         * The label would be used for tracking/logging purposes by test engines.
         */
        @Child(name = "label", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Tracking/logging operation label", formalDefinition="The label would be used for tracking/logging purposes by test engines." )
        protected StringType label;

        /**
         * The description would be used by test engines for tracking and reporting purposes.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Tracking/reporting operation description", formalDefinition="The description would be used by test engines for tracking and reporting purposes." )
        protected StringType description;

        /**
         * The content-type or mime-type to use for RESTful operation in the 'Accept' header.
         */
        @Child(name = "accept", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="xml | json | ttl | none", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Accept' header." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/content-type")
        protected Enumeration<ContentType> accept;

        /**
         * The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="xml | json | ttl | none", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Content-Type' header." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/content-type")
        protected Enumeration<ContentType> contentType;

        /**
         * The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section.
         */
        @Child(name = "destination", type = {IntegerType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Server responding to the request", formalDefinition="The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section." )
        protected IntegerType destination;

        /**
         * Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.
         */
        @Child(name = "encodeRequestUrl", type = {BooleanType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether or not to send the request url in encoded format", formalDefinition="Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths." )
        protected BooleanType encodeRequestUrl;

        /**
         * The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section.
         */
        @Child(name = "origin", type = {IntegerType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Server initiating the request", formalDefinition="The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section." )
        protected IntegerType origin;

        /**
         * Path plus parameters after [type].  Used to set parts of the request URL explicitly.
         */
        @Child(name = "params", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Explicitly defined path parameters", formalDefinition="Path plus parameters after [type].  Used to set parts of the request URL explicitly." )
        protected StringType params;

        /**
         * Header elements would be used to set HTTP headers.
         */
        @Child(name = "requestHeader", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Each operation can have one or more header elements", formalDefinition="Header elements would be used to set HTTP headers." )
        protected List<SetupActionOperationRequestHeaderComponent> requestHeader;

        /**
         * The fixture id (maybe new) to map to the request.
         */
        @Child(name = "requestId", type = {IdType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of mapped request", formalDefinition="The fixture id (maybe new) to map to the request." )
        protected IdType requestId;

        /**
         * The fixture id (maybe new) to map to the response.
         */
        @Child(name = "responseId", type = {IdType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of mapped response", formalDefinition="The fixture id (maybe new) to map to the response." )
        protected IdType responseId;

        /**
         * The id of the fixture used as the body of a PUT or POST request.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of body for PUT and POST requests", formalDefinition="The id of the fixture used as the body of a PUT or POST request." )
        protected IdType sourceId;

        /**
         * Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.
         */
        @Child(name = "targetId", type = {IdType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of fixture used for extracting the [id],  [type], and [vid] for GET requests", formalDefinition="Id of fixture used for extracting the [id],  [type], and [vid] for GET requests." )
        protected IdType targetId;

        /**
         * Complete request URL.
         */
        @Child(name = "url", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Request URL", formalDefinition="Complete request URL." )
        protected StringType url;

        private static final long serialVersionUID = -488909648L;

    /**
     * Constructor
     */
      public SetupActionOperationComponent() {
        super();
      }

        /**
         * @return {@link #type} (Server interaction or operation type.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.type");
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
        public SetupActionOperationComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #resource} (The type of the resource.  See http://build.fhir.org/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public CodeType getResourceElement() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.resource");
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
         * @param value {@link #resource} (The type of the resource.  See http://build.fhir.org/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public SetupActionOperationComponent setResourceElement(CodeType value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return The type of the resource.  See http://build.fhir.org/resourcelist.html.
         */
        public String getResource() { 
          return this.resource == null ? null : this.resource.getValue();
        }

        /**
         * @param value The type of the resource.  See http://build.fhir.org/resourcelist.html.
         */
        public SetupActionOperationComponent setResource(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.label");
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
        public SetupActionOperationComponent setLabelElement(StringType value) { 
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
        public SetupActionOperationComponent setLabel(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.description");
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
        public SetupActionOperationComponent setDescriptionElement(StringType value) { 
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
        public SetupActionOperationComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.accept");
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
        public SetupActionOperationComponent setAcceptElement(Enumeration<ContentType> value) { 
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
        public SetupActionOperationComponent setAccept(ContentType value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.contentType");
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
        public SetupActionOperationComponent setContentTypeElement(Enumeration<ContentType> value) { 
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
        public SetupActionOperationComponent setContentType(ContentType value) { 
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
         * @return {@link #destination} (The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public IntegerType getDestinationElement() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.destination");
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
         * @param value {@link #destination} (The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section.). This is the underlying object with id, value and extensions. The accessor "getDestination" gives direct access to the value
         */
        public SetupActionOperationComponent setDestinationElement(IntegerType value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section.
         */
        public int getDestination() { 
          return this.destination == null || this.destination.isEmpty() ? 0 : this.destination.getValue();
        }

        /**
         * @param value The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section.
         */
        public SetupActionOperationComponent setDestination(int value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.encodeRequestUrl");
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
        public SetupActionOperationComponent setEncodeRequestUrlElement(BooleanType value) { 
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
        public SetupActionOperationComponent setEncodeRequestUrl(boolean value) { 
            if (this.encodeRequestUrl == null)
              this.encodeRequestUrl = new BooleanType();
            this.encodeRequestUrl.setValue(value);
          return this;
        }

        /**
         * @return {@link #origin} (The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section.). This is the underlying object with id, value and extensions. The accessor "getOrigin" gives direct access to the value
         */
        public IntegerType getOriginElement() { 
          if (this.origin == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.origin");
            else if (Configuration.doAutoCreate())
              this.origin = new IntegerType(); // bb
          return this.origin;
        }

        public boolean hasOriginElement() { 
          return this.origin != null && !this.origin.isEmpty();
        }

        public boolean hasOrigin() { 
          return this.origin != null && !this.origin.isEmpty();
        }

        /**
         * @param value {@link #origin} (The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section.). This is the underlying object with id, value and extensions. The accessor "getOrigin" gives direct access to the value
         */
        public SetupActionOperationComponent setOriginElement(IntegerType value) { 
          this.origin = value;
          return this;
        }

        /**
         * @return The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section.
         */
        public int getOrigin() { 
          return this.origin == null || this.origin.isEmpty() ? 0 : this.origin.getValue();
        }

        /**
         * @param value The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section.
         */
        public SetupActionOperationComponent setOrigin(int value) { 
            if (this.origin == null)
              this.origin = new IntegerType();
            this.origin.setValue(value);
          return this;
        }

        /**
         * @return {@link #params} (Path plus parameters after [type].  Used to set parts of the request URL explicitly.). This is the underlying object with id, value and extensions. The accessor "getParams" gives direct access to the value
         */
        public StringType getParamsElement() { 
          if (this.params == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.params");
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
        public SetupActionOperationComponent setParamsElement(StringType value) { 
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
        public SetupActionOperationComponent setParams(String value) { 
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
        public List<SetupActionOperationRequestHeaderComponent> getRequestHeader() { 
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<SetupActionOperationRequestHeaderComponent>();
          return this.requestHeader;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SetupActionOperationComponent setRequestHeader(List<SetupActionOperationRequestHeaderComponent> theRequestHeader) { 
          this.requestHeader = theRequestHeader;
          return this;
        }

        public boolean hasRequestHeader() { 
          if (this.requestHeader == null)
            return false;
          for (SetupActionOperationRequestHeaderComponent item : this.requestHeader)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SetupActionOperationRequestHeaderComponent addRequestHeader() { //3
          SetupActionOperationRequestHeaderComponent t = new SetupActionOperationRequestHeaderComponent();
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<SetupActionOperationRequestHeaderComponent>();
          this.requestHeader.add(t);
          return t;
        }

        public SetupActionOperationComponent addRequestHeader(SetupActionOperationRequestHeaderComponent t) { //3
          if (t == null)
            return this;
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<SetupActionOperationRequestHeaderComponent>();
          this.requestHeader.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #requestHeader}, creating it if it does not already exist
         */
        public SetupActionOperationRequestHeaderComponent getRequestHeaderFirstRep() { 
          if (getRequestHeader().isEmpty()) {
            addRequestHeader();
          }
          return getRequestHeader().get(0);
        }

        /**
         * @return {@link #requestId} (The fixture id (maybe new) to map to the request.). This is the underlying object with id, value and extensions. The accessor "getRequestId" gives direct access to the value
         */
        public IdType getRequestIdElement() { 
          if (this.requestId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.requestId");
            else if (Configuration.doAutoCreate())
              this.requestId = new IdType(); // bb
          return this.requestId;
        }

        public boolean hasRequestIdElement() { 
          return this.requestId != null && !this.requestId.isEmpty();
        }

        public boolean hasRequestId() { 
          return this.requestId != null && !this.requestId.isEmpty();
        }

        /**
         * @param value {@link #requestId} (The fixture id (maybe new) to map to the request.). This is the underlying object with id, value and extensions. The accessor "getRequestId" gives direct access to the value
         */
        public SetupActionOperationComponent setRequestIdElement(IdType value) { 
          this.requestId = value;
          return this;
        }

        /**
         * @return The fixture id (maybe new) to map to the request.
         */
        public String getRequestId() { 
          return this.requestId == null ? null : this.requestId.getValue();
        }

        /**
         * @param value The fixture id (maybe new) to map to the request.
         */
        public SetupActionOperationComponent setRequestId(String value) { 
          if (Utilities.noString(value))
            this.requestId = null;
          else {
            if (this.requestId == null)
              this.requestId = new IdType();
            this.requestId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #responseId} (The fixture id (maybe new) to map to the response.). This is the underlying object with id, value and extensions. The accessor "getResponseId" gives direct access to the value
         */
        public IdType getResponseIdElement() { 
          if (this.responseId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationComponent.responseId");
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
        public SetupActionOperationComponent setResponseIdElement(IdType value) { 
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
        public SetupActionOperationComponent setResponseId(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.sourceId");
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
        public SetupActionOperationComponent setSourceIdElement(IdType value) { 
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
        public SetupActionOperationComponent setSourceId(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.targetId");
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
        public SetupActionOperationComponent setTargetIdElement(IdType value) { 
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
        public SetupActionOperationComponent setTargetId(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionOperationComponent.url");
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
        public SetupActionOperationComponent setUrlElement(StringType value) { 
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
        public SetupActionOperationComponent setUrl(String value) { 
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
          childrenList.add(new Property("resource", "code", "The type of the resource.  See http://build.fhir.org/resourcelist.html.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("label", "string", "The label would be used for tracking/logging purposes by test engines.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("description", "string", "The description would be used by test engines for tracking and reporting purposes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("accept", "code", "The content-type or mime-type to use for RESTful operation in the 'Accept' header.", 0, java.lang.Integer.MAX_VALUE, accept));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.", 0, java.lang.Integer.MAX_VALUE, contentType));
          childrenList.add(new Property("destination", "integer", "The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("encodeRequestUrl", "boolean", "Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.", 0, java.lang.Integer.MAX_VALUE, encodeRequestUrl));
          childrenList.add(new Property("origin", "integer", "The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section.", 0, java.lang.Integer.MAX_VALUE, origin));
          childrenList.add(new Property("params", "string", "Path plus parameters after [type].  Used to set parts of the request URL explicitly.", 0, java.lang.Integer.MAX_VALUE, params));
          childrenList.add(new Property("requestHeader", "", "Header elements would be used to set HTTP headers.", 0, java.lang.Integer.MAX_VALUE, requestHeader));
          childrenList.add(new Property("requestId", "id", "The fixture id (maybe new) to map to the request.", 0, java.lang.Integer.MAX_VALUE, requestId));
          childrenList.add(new Property("responseId", "id", "The fixture id (maybe new) to map to the response.", 0, java.lang.Integer.MAX_VALUE, responseId));
          childrenList.add(new Property("sourceId", "id", "The id of the fixture used as the body of a PUT or POST request.", 0, java.lang.Integer.MAX_VALUE, sourceId));
          childrenList.add(new Property("targetId", "id", "Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.", 0, java.lang.Integer.MAX_VALUE, targetId));
          childrenList.add(new Property("url", "string", "Complete request URL.", 0, java.lang.Integer.MAX_VALUE, url));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // CodeType
        case 102727412: /*label*/ return this.label == null ? new Base[0] : new Base[] {this.label}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1423461112: /*accept*/ return this.accept == null ? new Base[0] : new Base[] {this.accept}; // Enumeration<ContentType>
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // Enumeration<ContentType>
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : new Base[] {this.destination}; // IntegerType
        case -1760554218: /*encodeRequestUrl*/ return this.encodeRequestUrl == null ? new Base[0] : new Base[] {this.encodeRequestUrl}; // BooleanType
        case -1008619738: /*origin*/ return this.origin == null ? new Base[0] : new Base[] {this.origin}; // IntegerType
        case -995427962: /*params*/ return this.params == null ? new Base[0] : new Base[] {this.params}; // StringType
        case 1074158076: /*requestHeader*/ return this.requestHeader == null ? new Base[0] : this.requestHeader.toArray(new Base[this.requestHeader.size()]); // SetupActionOperationRequestHeaderComponent
        case 693933066: /*requestId*/ return this.requestId == null ? new Base[0] : new Base[] {this.requestId}; // IdType
        case -633138884: /*responseId*/ return this.responseId == null ? new Base[0] : new Base[] {this.responseId}; // IdType
        case 1746327190: /*sourceId*/ return this.sourceId == null ? new Base[0] : new Base[] {this.sourceId}; // IdType
        case -441951604: /*targetId*/ return this.targetId == null ? new Base[0] : new Base[] {this.targetId}; // IdType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          return value;
        case -341064690: // resource
          this.resource = castToCode(value); // CodeType
          return value;
        case 102727412: // label
          this.label = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1423461112: // accept
          value = new ContentTypeEnumFactory().fromType(castToCode(value));
          this.accept = (Enumeration) value; // Enumeration<ContentType>
          return value;
        case -389131437: // contentType
          value = new ContentTypeEnumFactory().fromType(castToCode(value));
          this.contentType = (Enumeration) value; // Enumeration<ContentType>
          return value;
        case -1429847026: // destination
          this.destination = castToInteger(value); // IntegerType
          return value;
        case -1760554218: // encodeRequestUrl
          this.encodeRequestUrl = castToBoolean(value); // BooleanType
          return value;
        case -1008619738: // origin
          this.origin = castToInteger(value); // IntegerType
          return value;
        case -995427962: // params
          this.params = castToString(value); // StringType
          return value;
        case 1074158076: // requestHeader
          this.getRequestHeader().add((SetupActionOperationRequestHeaderComponent) value); // SetupActionOperationRequestHeaderComponent
          return value;
        case 693933066: // requestId
          this.requestId = castToId(value); // IdType
          return value;
        case -633138884: // responseId
          this.responseId = castToId(value); // IdType
          return value;
        case 1746327190: // sourceId
          this.sourceId = castToId(value); // IdType
          return value;
        case -441951604: // targetId
          this.targetId = castToId(value); // IdType
          return value;
        case 116079: // url
          this.url = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCoding(value); // Coding
        } else if (name.equals("resource")) {
          this.resource = castToCode(value); // CodeType
        } else if (name.equals("label")) {
          this.label = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("accept")) {
          value = new ContentTypeEnumFactory().fromType(castToCode(value));
          this.accept = (Enumeration) value; // Enumeration<ContentType>
        } else if (name.equals("contentType")) {
          value = new ContentTypeEnumFactory().fromType(castToCode(value));
          this.contentType = (Enumeration) value; // Enumeration<ContentType>
        } else if (name.equals("destination")) {
          this.destination = castToInteger(value); // IntegerType
        } else if (name.equals("encodeRequestUrl")) {
          this.encodeRequestUrl = castToBoolean(value); // BooleanType
        } else if (name.equals("origin")) {
          this.origin = castToInteger(value); // IntegerType
        } else if (name.equals("params")) {
          this.params = castToString(value); // StringType
        } else if (name.equals("requestHeader")) {
          this.getRequestHeader().add((SetupActionOperationRequestHeaderComponent) value);
        } else if (name.equals("requestId")) {
          this.requestId = castToId(value); // IdType
        } else if (name.equals("responseId")) {
          this.responseId = castToId(value); // IdType
        } else if (name.equals("sourceId")) {
          this.sourceId = castToId(value); // IdType
        } else if (name.equals("targetId")) {
          this.targetId = castToId(value); // IdType
        } else if (name.equals("url")) {
          this.url = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -341064690:  return getResourceElement();
        case 102727412:  return getLabelElement();
        case -1724546052:  return getDescriptionElement();
        case -1423461112:  return getAcceptElement();
        case -389131437:  return getContentTypeElement();
        case -1429847026:  return getDestinationElement();
        case -1760554218:  return getEncodeRequestUrlElement();
        case -1008619738:  return getOriginElement();
        case -995427962:  return getParamsElement();
        case 1074158076:  return addRequestHeader(); 
        case 693933066:  return getRequestIdElement();
        case -633138884:  return getResponseIdElement();
        case 1746327190:  return getSourceIdElement();
        case -441951604:  return getTargetIdElement();
        case 116079:  return getUrlElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"Coding"};
        case -341064690: /*resource*/ return new String[] {"code"};
        case 102727412: /*label*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1423461112: /*accept*/ return new String[] {"code"};
        case -389131437: /*contentType*/ return new String[] {"code"};
        case -1429847026: /*destination*/ return new String[] {"integer"};
        case -1760554218: /*encodeRequestUrl*/ return new String[] {"boolean"};
        case -1008619738: /*origin*/ return new String[] {"integer"};
        case -995427962: /*params*/ return new String[] {"string"};
        case 1074158076: /*requestHeader*/ return new String[] {};
        case 693933066: /*requestId*/ return new String[] {"id"};
        case -633138884: /*responseId*/ return new String[] {"id"};
        case 1746327190: /*sourceId*/ return new String[] {"id"};
        case -441951604: /*targetId*/ return new String[] {"id"};
        case 116079: /*url*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("resource")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.resource");
        }
        else if (name.equals("label")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.label");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else if (name.equals("accept")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.accept");
        }
        else if (name.equals("contentType")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.contentType");
        }
        else if (name.equals("destination")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.destination");
        }
        else if (name.equals("encodeRequestUrl")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.encodeRequestUrl");
        }
        else if (name.equals("origin")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.origin");
        }
        else if (name.equals("params")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.params");
        }
        else if (name.equals("requestHeader")) {
          return addRequestHeader();
        }
        else if (name.equals("requestId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.requestId");
        }
        else if (name.equals("responseId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.responseId");
        }
        else if (name.equals("sourceId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.sourceId");
        }
        else if (name.equals("targetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.targetId");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.url");
        }
        else
          return super.addChild(name);
      }

      public SetupActionOperationComponent copy() {
        SetupActionOperationComponent dst = new SetupActionOperationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.resource = resource == null ? null : resource.copy();
        dst.label = label == null ? null : label.copy();
        dst.description = description == null ? null : description.copy();
        dst.accept = accept == null ? null : accept.copy();
        dst.contentType = contentType == null ? null : contentType.copy();
        dst.destination = destination == null ? null : destination.copy();
        dst.encodeRequestUrl = encodeRequestUrl == null ? null : encodeRequestUrl.copy();
        dst.origin = origin == null ? null : origin.copy();
        dst.params = params == null ? null : params.copy();
        if (requestHeader != null) {
          dst.requestHeader = new ArrayList<SetupActionOperationRequestHeaderComponent>();
          for (SetupActionOperationRequestHeaderComponent i : requestHeader)
            dst.requestHeader.add(i.copy());
        };
        dst.requestId = requestId == null ? null : requestId.copy();
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
        if (!(other instanceof SetupActionOperationComponent))
          return false;
        SetupActionOperationComponent o = (SetupActionOperationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(resource, o.resource, true) && compareDeep(label, o.label, true)
           && compareDeep(description, o.description, true) && compareDeep(accept, o.accept, true) && compareDeep(contentType, o.contentType, true)
           && compareDeep(destination, o.destination, true) && compareDeep(encodeRequestUrl, o.encodeRequestUrl, true)
           && compareDeep(origin, o.origin, true) && compareDeep(params, o.params, true) && compareDeep(requestHeader, o.requestHeader, true)
           && compareDeep(requestId, o.requestId, true) && compareDeep(responseId, o.responseId, true) && compareDeep(sourceId, o.sourceId, true)
           && compareDeep(targetId, o.targetId, true) && compareDeep(url, o.url, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionOperationComponent))
          return false;
        SetupActionOperationComponent o = (SetupActionOperationComponent) other;
        return compareValues(resource, o.resource, true) && compareValues(label, o.label, true) && compareValues(description, o.description, true)
           && compareValues(accept, o.accept, true) && compareValues(contentType, o.contentType, true) && compareValues(destination, o.destination, true)
           && compareValues(encodeRequestUrl, o.encodeRequestUrl, true) && compareValues(origin, o.origin, true)
           && compareValues(params, o.params, true) && compareValues(requestId, o.requestId, true) && compareValues(responseId, o.responseId, true)
           && compareValues(sourceId, o.sourceId, true) && compareValues(targetId, o.targetId, true) && compareValues(url, o.url, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, resource, label, description
          , accept, contentType, destination, encodeRequestUrl, origin, params, requestHeader
          , requestId, responseId, sourceId, targetId, url);
      }

  public String fhirType() {
    return "TestScript.setup.action.operation";

  }

  }

    @Block()
    public static class SetupActionOperationRequestHeaderComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The HTTP header field e.g. "Accept".
         */
        @Child(name = "field", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP header field name", formalDefinition="The HTTP header field e.g. \"Accept\"." )
        protected StringType field;

        /**
         * The value of the header e.g. "application/fhir+xml".
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP headerfield value", formalDefinition="The value of the header e.g. \"application/fhir+xml\"." )
        protected StringType value;

        private static final long serialVersionUID = 274395337L;

    /**
     * Constructor
     */
      public SetupActionOperationRequestHeaderComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionOperationRequestHeaderComponent(StringType field, StringType value) {
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
              throw new Error("Attempt to auto-create SetupActionOperationRequestHeaderComponent.field");
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
        public SetupActionOperationRequestHeaderComponent setFieldElement(StringType value) { 
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
        public SetupActionOperationRequestHeaderComponent setField(String value) { 
            if (this.field == null)
              this.field = new StringType();
            this.field.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the header e.g. "application/fhir+xml".). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionOperationRequestHeaderComponent.value");
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
         * @param value {@link #value} (The value of the header e.g. "application/fhir+xml".). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public SetupActionOperationRequestHeaderComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value of the header e.g. "application/fhir+xml".
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value of the header e.g. "application/fhir+xml".
         */
        public SetupActionOperationRequestHeaderComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("field", "string", "The HTTP header field e.g. \"Accept\".", 0, java.lang.Integer.MAX_VALUE, field));
          childrenList.add(new Property("value", "string", "The value of the header e.g. \"application/fhir+xml\".", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 97427706: /*field*/ return this.field == null ? new Base[0] : new Base[] {this.field}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 97427706: // field
          this.field = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("field")) {
          this.field = castToString(value); // StringType
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 97427706:  return getFieldElement();
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 97427706: /*field*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("field")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.field");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.value");
        }
        else
          return super.addChild(name);
      }

      public SetupActionOperationRequestHeaderComponent copy() {
        SetupActionOperationRequestHeaderComponent dst = new SetupActionOperationRequestHeaderComponent();
        copyValues(dst);
        dst.field = field == null ? null : field.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionOperationRequestHeaderComponent))
          return false;
        SetupActionOperationRequestHeaderComponent o = (SetupActionOperationRequestHeaderComponent) other;
        return compareDeep(field, o.field, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionOperationRequestHeaderComponent))
          return false;
        SetupActionOperationRequestHeaderComponent o = (SetupActionOperationRequestHeaderComponent) other;
        return compareValues(field, o.field, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(field, value);
      }

  public String fhirType() {
    return "TestScript.setup.action.operation.requestHeader";

  }

  }

    @Block()
    public static class SetupActionAssertComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The label would be used for tracking/logging purposes by test engines.
         */
        @Child(name = "label", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Tracking/logging assertion label", formalDefinition="The label would be used for tracking/logging purposes by test engines." )
        protected StringType label;

        /**
         * The description would be used by test engines for tracking and reporting purposes.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Tracking/reporting assertion description", formalDefinition="The description would be used by test engines for tracking and reporting purposes." )
        protected StringType description;

        /**
         * The direction to use for the assertion.
         */
        @Child(name = "direction", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="response | request", formalDefinition="The direction to use for the assertion." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/assert-direction-codes")
        protected Enumeration<AssertionDirectionType> direction;

        /**
         * Id of the source fixture used as the contents to be evaluated by either the "source/expression" or "sourceId/path" definition.
         */
        @Child(name = "compareToSourceId", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of the source fixture to be evaluated", formalDefinition="Id of the source fixture used as the contents to be evaluated by either the \"source/expression\" or \"sourceId/path\" definition." )
        protected StringType compareToSourceId;

        /**
         * The fluentpath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.
         */
        @Child(name = "compareToSourceExpression", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The fluentpath expression to evaluate against the source fixture", formalDefinition="The fluentpath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both." )
        protected StringType compareToSourceExpression;

        /**
         * XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.
         */
        @Child(name = "compareToSourcePath", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="XPath or JSONPath expression to evaluate against the source fixture", formalDefinition="XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both." )
        protected StringType compareToSourcePath;

        /**
         * The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="xml | json | ttl | none", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Content-Type' header." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/content-type")
        protected Enumeration<ContentType> contentType;

        /**
         * The fluentpath expression to be evaluated against the request or response message contents - HTTP headers and payload.
         */
        @Child(name = "expression", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The fluentpath expression to be evaluated", formalDefinition="The fluentpath expression to be evaluated against the request or response message contents - HTTP headers and payload." )
        protected StringType expression;

        /**
         * The HTTP header field name e.g. 'Location'.
         */
        @Child(name = "headerField", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP header field name", formalDefinition="The HTTP header field name e.g. 'Location'." )
        protected StringType headerField;

        /**
         * The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.
         */
        @Child(name = "minimumId", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of minimum content resource", formalDefinition="The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId." )
        protected StringType minimumId;

        /**
         * Whether or not the test execution performs validation on the bundle navigation links.
         */
        @Child(name = "navigationLinks", type = {BooleanType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Perform validation on navigation links?", formalDefinition="Whether or not the test execution performs validation on the bundle navigation links." )
        protected BooleanType navigationLinks;

        /**
         * The operator type defines the conditional behavior of the assert. If not defined, the default is equals.
         */
        @Child(name = "operator", type = {CodeType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="equals | notEquals | in | notIn | greaterThan | lessThan | empty | notEmpty | contains | notContains | eval", formalDefinition="The operator type defines the conditional behavior of the assert. If not defined, the default is equals." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/assert-operator-codes")
        protected Enumeration<AssertionOperatorType> operator;

        /**
         * The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.
         */
        @Child(name = "path", type = {StringType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="XPath or JSONPath expression", formalDefinition="The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server." )
        protected StringType path;

        /**
         * The request method or HTTP operation code to compare against that used by the client system under test.
         */
        @Child(name = "requestMethod", type = {CodeType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="delete | get | options | patch | post | put", formalDefinition="The request method or HTTP operation code to compare against that used by the client system under test." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/http-operations")
        protected Enumeration<TestScriptRequestMethodCode> requestMethod;

        /**
         * The value to use in a comparison against the request URL path string.
         */
        @Child(name = "requestURL", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Request URL comparison value", formalDefinition="The value to use in a comparison against the request URL path string." )
        protected StringType requestURL;

        /**
         * The type of the resource.  See http://build.fhir.org/resourcelist.html.
         */
        @Child(name = "resource", type = {CodeType.class}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource type", formalDefinition="The type of the resource.  See http://build.fhir.org/resourcelist.html." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/defined-types")
        protected CodeType resource;

        /**
         * okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.
         */
        @Child(name = "response", type = {CodeType.class}, order=17, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable", formalDefinition="okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/assert-response-code-types")
        protected Enumeration<AssertionResponseTypes> response;

        /**
         * The value of the HTTP response code to be tested.
         */
        @Child(name = "responseCode", type = {StringType.class}, order=18, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP response code to test", formalDefinition="The value of the HTTP response code to be tested." )
        protected StringType responseCode;

        /**
         * The TestScript.rule this assert will evaluate.
         */
        @Child(name = "rule", type = {}, order=19, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The reference to a TestScript.rule", formalDefinition="The TestScript.rule this assert will evaluate." )
        protected ActionAssertRuleComponent rule;

        /**
         * The TestScript.ruleset this assert will evaluate.
         */
        @Child(name = "ruleset", type = {}, order=20, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The reference to a TestScript.ruleset", formalDefinition="The TestScript.ruleset this assert will evaluate." )
        protected ActionAssertRulesetComponent ruleset;

        /**
         * Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=21, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of source expression or headerField", formalDefinition="Fixture to evaluate the XPath/JSONPath expression or the headerField  against." )
        protected IdType sourceId;

        /**
         * The ID of the Profile to validate against.
         */
        @Child(name = "validateProfileId", type = {IdType.class}, order=22, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Profile Id of validation profile reference", formalDefinition="The ID of the Profile to validate against." )
        protected IdType validateProfileId;

        /**
         * The value to compare to.
         */
        @Child(name = "value", type = {StringType.class}, order=23, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The value to compare to", formalDefinition="The value to compare to." )
        protected StringType value;

        /**
         * Whether or not the test execution will produce a warning only on error for this assert.
         */
        @Child(name = "warningOnly", type = {BooleanType.class}, order=24, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Will this assert produce a warning only on error?", formalDefinition="Whether or not the test execution will produce a warning only on error for this assert." )
        protected BooleanType warningOnly;

        private static final long serialVersionUID = 171718507L;

    /**
     * Constructor
     */
      public SetupActionAssertComponent() {
        super();
      }

        /**
         * @return {@link #label} (The label would be used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getLabel" gives direct access to the value
         */
        public StringType getLabelElement() { 
          if (this.label == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.label");
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
        public SetupActionAssertComponent setLabelElement(StringType value) { 
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
        public SetupActionAssertComponent setLabel(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertComponent.description");
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
        public SetupActionAssertComponent setDescriptionElement(StringType value) { 
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
        public SetupActionAssertComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertComponent.direction");
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
        public SetupActionAssertComponent setDirectionElement(Enumeration<AssertionDirectionType> value) { 
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
        public SetupActionAssertComponent setDirection(AssertionDirectionType value) { 
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
         * @return {@link #compareToSourceId} (Id of the source fixture used as the contents to be evaluated by either the "source/expression" or "sourceId/path" definition.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceId" gives direct access to the value
         */
        public StringType getCompareToSourceIdElement() { 
          if (this.compareToSourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.compareToSourceId");
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
         * @param value {@link #compareToSourceId} (Id of the source fixture used as the contents to be evaluated by either the "source/expression" or "sourceId/path" definition.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceId" gives direct access to the value
         */
        public SetupActionAssertComponent setCompareToSourceIdElement(StringType value) { 
          this.compareToSourceId = value;
          return this;
        }

        /**
         * @return Id of the source fixture used as the contents to be evaluated by either the "source/expression" or "sourceId/path" definition.
         */
        public String getCompareToSourceId() { 
          return this.compareToSourceId == null ? null : this.compareToSourceId.getValue();
        }

        /**
         * @param value Id of the source fixture used as the contents to be evaluated by either the "source/expression" or "sourceId/path" definition.
         */
        public SetupActionAssertComponent setCompareToSourceId(String value) { 
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
         * @return {@link #compareToSourceExpression} (The fluentpath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceExpression" gives direct access to the value
         */
        public StringType getCompareToSourceExpressionElement() { 
          if (this.compareToSourceExpression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.compareToSourceExpression");
            else if (Configuration.doAutoCreate())
              this.compareToSourceExpression = new StringType(); // bb
          return this.compareToSourceExpression;
        }

        public boolean hasCompareToSourceExpressionElement() { 
          return this.compareToSourceExpression != null && !this.compareToSourceExpression.isEmpty();
        }

        public boolean hasCompareToSourceExpression() { 
          return this.compareToSourceExpression != null && !this.compareToSourceExpression.isEmpty();
        }

        /**
         * @param value {@link #compareToSourceExpression} (The fluentpath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceExpression" gives direct access to the value
         */
        public SetupActionAssertComponent setCompareToSourceExpressionElement(StringType value) { 
          this.compareToSourceExpression = value;
          return this;
        }

        /**
         * @return The fluentpath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.
         */
        public String getCompareToSourceExpression() { 
          return this.compareToSourceExpression == null ? null : this.compareToSourceExpression.getValue();
        }

        /**
         * @param value The fluentpath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.
         */
        public SetupActionAssertComponent setCompareToSourceExpression(String value) { 
          if (Utilities.noString(value))
            this.compareToSourceExpression = null;
          else {
            if (this.compareToSourceExpression == null)
              this.compareToSourceExpression = new StringType();
            this.compareToSourceExpression.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #compareToSourcePath} (XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourcePath" gives direct access to the value
         */
        public StringType getCompareToSourcePathElement() { 
          if (this.compareToSourcePath == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.compareToSourcePath");
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
         * @param value {@link #compareToSourcePath} (XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourcePath" gives direct access to the value
         */
        public SetupActionAssertComponent setCompareToSourcePathElement(StringType value) { 
          this.compareToSourcePath = value;
          return this;
        }

        /**
         * @return XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.
         */
        public String getCompareToSourcePath() { 
          return this.compareToSourcePath == null ? null : this.compareToSourcePath.getValue();
        }

        /**
         * @param value XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.
         */
        public SetupActionAssertComponent setCompareToSourcePath(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertComponent.contentType");
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
        public SetupActionAssertComponent setContentTypeElement(Enumeration<ContentType> value) { 
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
        public SetupActionAssertComponent setContentType(ContentType value) { 
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
         * @return {@link #expression} (The fluentpath expression to be evaluated against the request or response message contents - HTTP headers and payload.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (The fluentpath expression to be evaluated against the request or response message contents - HTTP headers and payload.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public SetupActionAssertComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return The fluentpath expression to be evaluated against the request or response message contents - HTTP headers and payload.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value The fluentpath expression to be evaluated against the request or response message contents - HTTP headers and payload.
         */
        public SetupActionAssertComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #headerField} (The HTTP header field name e.g. 'Location'.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
         */
        public StringType getHeaderFieldElement() { 
          if (this.headerField == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.headerField");
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
         * @param value {@link #headerField} (The HTTP header field name e.g. 'Location'.). This is the underlying object with id, value and extensions. The accessor "getHeaderField" gives direct access to the value
         */
        public SetupActionAssertComponent setHeaderFieldElement(StringType value) { 
          this.headerField = value;
          return this;
        }

        /**
         * @return The HTTP header field name e.g. 'Location'.
         */
        public String getHeaderField() { 
          return this.headerField == null ? null : this.headerField.getValue();
        }

        /**
         * @param value The HTTP header field name e.g. 'Location'.
         */
        public SetupActionAssertComponent setHeaderField(String value) { 
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
         * @return {@link #minimumId} (The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.). This is the underlying object with id, value and extensions. The accessor "getMinimumId" gives direct access to the value
         */
        public StringType getMinimumIdElement() { 
          if (this.minimumId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.minimumId");
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
         * @param value {@link #minimumId} (The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.). This is the underlying object with id, value and extensions. The accessor "getMinimumId" gives direct access to the value
         */
        public SetupActionAssertComponent setMinimumIdElement(StringType value) { 
          this.minimumId = value;
          return this;
        }

        /**
         * @return The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.
         */
        public String getMinimumId() { 
          return this.minimumId == null ? null : this.minimumId.getValue();
        }

        /**
         * @param value The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.
         */
        public SetupActionAssertComponent setMinimumId(String value) { 
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
         * @return {@link #navigationLinks} (Whether or not the test execution performs validation on the bundle navigation links.). This is the underlying object with id, value and extensions. The accessor "getNavigationLinks" gives direct access to the value
         */
        public BooleanType getNavigationLinksElement() { 
          if (this.navigationLinks == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.navigationLinks");
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
         * @param value {@link #navigationLinks} (Whether or not the test execution performs validation on the bundle navigation links.). This is the underlying object with id, value and extensions. The accessor "getNavigationLinks" gives direct access to the value
         */
        public SetupActionAssertComponent setNavigationLinksElement(BooleanType value) { 
          this.navigationLinks = value;
          return this;
        }

        /**
         * @return Whether or not the test execution performs validation on the bundle navigation links.
         */
        public boolean getNavigationLinks() { 
          return this.navigationLinks == null || this.navigationLinks.isEmpty() ? false : this.navigationLinks.getValue();
        }

        /**
         * @param value Whether or not the test execution performs validation on the bundle navigation links.
         */
        public SetupActionAssertComponent setNavigationLinks(boolean value) { 
            if (this.navigationLinks == null)
              this.navigationLinks = new BooleanType();
            this.navigationLinks.setValue(value);
          return this;
        }

        /**
         * @return {@link #operator} (The operator type defines the conditional behavior of the assert. If not defined, the default is equals.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
         */
        public Enumeration<AssertionOperatorType> getOperatorElement() { 
          if (this.operator == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.operator");
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
         * @param value {@link #operator} (The operator type defines the conditional behavior of the assert. If not defined, the default is equals.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
         */
        public SetupActionAssertComponent setOperatorElement(Enumeration<AssertionOperatorType> value) { 
          this.operator = value;
          return this;
        }

        /**
         * @return The operator type defines the conditional behavior of the assert. If not defined, the default is equals.
         */
        public AssertionOperatorType getOperator() { 
          return this.operator == null ? null : this.operator.getValue();
        }

        /**
         * @param value The operator type defines the conditional behavior of the assert. If not defined, the default is equals.
         */
        public SetupActionAssertComponent setOperator(AssertionOperatorType value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertComponent.path");
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
        public SetupActionAssertComponent setPathElement(StringType value) { 
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
        public SetupActionAssertComponent setPath(String value) { 
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
         * @return {@link #requestMethod} (The request method or HTTP operation code to compare against that used by the client system under test.). This is the underlying object with id, value and extensions. The accessor "getRequestMethod" gives direct access to the value
         */
        public Enumeration<TestScriptRequestMethodCode> getRequestMethodElement() { 
          if (this.requestMethod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.requestMethod");
            else if (Configuration.doAutoCreate())
              this.requestMethod = new Enumeration<TestScriptRequestMethodCode>(new TestScriptRequestMethodCodeEnumFactory()); // bb
          return this.requestMethod;
        }

        public boolean hasRequestMethodElement() { 
          return this.requestMethod != null && !this.requestMethod.isEmpty();
        }

        public boolean hasRequestMethod() { 
          return this.requestMethod != null && !this.requestMethod.isEmpty();
        }

        /**
         * @param value {@link #requestMethod} (The request method or HTTP operation code to compare against that used by the client system under test.). This is the underlying object with id, value and extensions. The accessor "getRequestMethod" gives direct access to the value
         */
        public SetupActionAssertComponent setRequestMethodElement(Enumeration<TestScriptRequestMethodCode> value) { 
          this.requestMethod = value;
          return this;
        }

        /**
         * @return The request method or HTTP operation code to compare against that used by the client system under test.
         */
        public TestScriptRequestMethodCode getRequestMethod() { 
          return this.requestMethod == null ? null : this.requestMethod.getValue();
        }

        /**
         * @param value The request method or HTTP operation code to compare against that used by the client system under test.
         */
        public SetupActionAssertComponent setRequestMethod(TestScriptRequestMethodCode value) { 
          if (value == null)
            this.requestMethod = null;
          else {
            if (this.requestMethod == null)
              this.requestMethod = new Enumeration<TestScriptRequestMethodCode>(new TestScriptRequestMethodCodeEnumFactory());
            this.requestMethod.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #requestURL} (The value to use in a comparison against the request URL path string.). This is the underlying object with id, value and extensions. The accessor "getRequestURL" gives direct access to the value
         */
        public StringType getRequestURLElement() { 
          if (this.requestURL == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.requestURL");
            else if (Configuration.doAutoCreate())
              this.requestURL = new StringType(); // bb
          return this.requestURL;
        }

        public boolean hasRequestURLElement() { 
          return this.requestURL != null && !this.requestURL.isEmpty();
        }

        public boolean hasRequestURL() { 
          return this.requestURL != null && !this.requestURL.isEmpty();
        }

        /**
         * @param value {@link #requestURL} (The value to use in a comparison against the request URL path string.). This is the underlying object with id, value and extensions. The accessor "getRequestURL" gives direct access to the value
         */
        public SetupActionAssertComponent setRequestURLElement(StringType value) { 
          this.requestURL = value;
          return this;
        }

        /**
         * @return The value to use in a comparison against the request URL path string.
         */
        public String getRequestURL() { 
          return this.requestURL == null ? null : this.requestURL.getValue();
        }

        /**
         * @param value The value to use in a comparison against the request URL path string.
         */
        public SetupActionAssertComponent setRequestURL(String value) { 
          if (Utilities.noString(value))
            this.requestURL = null;
          else {
            if (this.requestURL == null)
              this.requestURL = new StringType();
            this.requestURL.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #resource} (The type of the resource.  See http://build.fhir.org/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public CodeType getResourceElement() { 
          if (this.resource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.resource");
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
         * @param value {@link #resource} (The type of the resource.  See http://build.fhir.org/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public SetupActionAssertComponent setResourceElement(CodeType value) { 
          this.resource = value;
          return this;
        }

        /**
         * @return The type of the resource.  See http://build.fhir.org/resourcelist.html.
         */
        public String getResource() { 
          return this.resource == null ? null : this.resource.getValue();
        }

        /**
         * @param value The type of the resource.  See http://build.fhir.org/resourcelist.html.
         */
        public SetupActionAssertComponent setResource(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertComponent.response");
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
        public SetupActionAssertComponent setResponseElement(Enumeration<AssertionResponseTypes> value) { 
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
        public SetupActionAssertComponent setResponse(AssertionResponseTypes value) { 
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
         * @return {@link #responseCode} (The value of the HTTP response code to be tested.). This is the underlying object with id, value and extensions. The accessor "getResponseCode" gives direct access to the value
         */
        public StringType getResponseCodeElement() { 
          if (this.responseCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.responseCode");
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
         * @param value {@link #responseCode} (The value of the HTTP response code to be tested.). This is the underlying object with id, value and extensions. The accessor "getResponseCode" gives direct access to the value
         */
        public SetupActionAssertComponent setResponseCodeElement(StringType value) { 
          this.responseCode = value;
          return this;
        }

        /**
         * @return The value of the HTTP response code to be tested.
         */
        public String getResponseCode() { 
          return this.responseCode == null ? null : this.responseCode.getValue();
        }

        /**
         * @param value The value of the HTTP response code to be tested.
         */
        public SetupActionAssertComponent setResponseCode(String value) { 
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
         * @return {@link #rule} (The TestScript.rule this assert will evaluate.)
         */
        public ActionAssertRuleComponent getRule() { 
          if (this.rule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.rule");
            else if (Configuration.doAutoCreate())
              this.rule = new ActionAssertRuleComponent(); // cc
          return this.rule;
        }

        public boolean hasRule() { 
          return this.rule != null && !this.rule.isEmpty();
        }

        /**
         * @param value {@link #rule} (The TestScript.rule this assert will evaluate.)
         */
        public SetupActionAssertComponent setRule(ActionAssertRuleComponent value) { 
          this.rule = value;
          return this;
        }

        /**
         * @return {@link #ruleset} (The TestScript.ruleset this assert will evaluate.)
         */
        public ActionAssertRulesetComponent getRuleset() { 
          if (this.ruleset == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.ruleset");
            else if (Configuration.doAutoCreate())
              this.ruleset = new ActionAssertRulesetComponent(); // cc
          return this.ruleset;
        }

        public boolean hasRuleset() { 
          return this.ruleset != null && !this.ruleset.isEmpty();
        }

        /**
         * @param value {@link #ruleset} (The TestScript.ruleset this assert will evaluate.)
         */
        public SetupActionAssertComponent setRuleset(ActionAssertRulesetComponent value) { 
          this.ruleset = value;
          return this;
        }

        /**
         * @return {@link #sourceId} (Fixture to evaluate the XPath/JSONPath expression or the headerField  against.). This is the underlying object with id, value and extensions. The accessor "getSourceId" gives direct access to the value
         */
        public IdType getSourceIdElement() { 
          if (this.sourceId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.sourceId");
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
        public SetupActionAssertComponent setSourceIdElement(IdType value) { 
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
        public SetupActionAssertComponent setSourceId(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertComponent.validateProfileId");
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
        public SetupActionAssertComponent setValidateProfileIdElement(IdType value) { 
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
        public SetupActionAssertComponent setValidateProfileId(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertComponent.value");
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
        public SetupActionAssertComponent setValueElement(StringType value) { 
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
        public SetupActionAssertComponent setValue(String value) { 
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
         * @return {@link #warningOnly} (Whether or not the test execution will produce a warning only on error for this assert.). This is the underlying object with id, value and extensions. The accessor "getWarningOnly" gives direct access to the value
         */
        public BooleanType getWarningOnlyElement() { 
          if (this.warningOnly == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.warningOnly");
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
         * @param value {@link #warningOnly} (Whether or not the test execution will produce a warning only on error for this assert.). This is the underlying object with id, value and extensions. The accessor "getWarningOnly" gives direct access to the value
         */
        public SetupActionAssertComponent setWarningOnlyElement(BooleanType value) { 
          this.warningOnly = value;
          return this;
        }

        /**
         * @return Whether or not the test execution will produce a warning only on error for this assert.
         */
        public boolean getWarningOnly() { 
          return this.warningOnly == null || this.warningOnly.isEmpty() ? false : this.warningOnly.getValue();
        }

        /**
         * @param value Whether or not the test execution will produce a warning only on error for this assert.
         */
        public SetupActionAssertComponent setWarningOnly(boolean value) { 
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
          childrenList.add(new Property("compareToSourceId", "string", "Id of the source fixture used as the contents to be evaluated by either the \"source/expression\" or \"sourceId/path\" definition.", 0, java.lang.Integer.MAX_VALUE, compareToSourceId));
          childrenList.add(new Property("compareToSourceExpression", "string", "The fluentpath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.", 0, java.lang.Integer.MAX_VALUE, compareToSourceExpression));
          childrenList.add(new Property("compareToSourcePath", "string", "XPath or JSONPath expression to evaluate against the source fixture. When compareToSourceId is defined, either compareToSourceExpression or compareToSourcePath must be defined, but not both.", 0, java.lang.Integer.MAX_VALUE, compareToSourcePath));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.", 0, java.lang.Integer.MAX_VALUE, contentType));
          childrenList.add(new Property("expression", "string", "The fluentpath expression to be evaluated against the request or response message contents - HTTP headers and payload.", 0, java.lang.Integer.MAX_VALUE, expression));
          childrenList.add(new Property("headerField", "string", "The HTTP header field name e.g. 'Location'.", 0, java.lang.Integer.MAX_VALUE, headerField));
          childrenList.add(new Property("minimumId", "string", "The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.", 0, java.lang.Integer.MAX_VALUE, minimumId));
          childrenList.add(new Property("navigationLinks", "boolean", "Whether or not the test execution performs validation on the bundle navigation links.", 0, java.lang.Integer.MAX_VALUE, navigationLinks));
          childrenList.add(new Property("operator", "code", "The operator type defines the conditional behavior of the assert. If not defined, the default is equals.", 0, java.lang.Integer.MAX_VALUE, operator));
          childrenList.add(new Property("path", "string", "The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("requestMethod", "code", "The request method or HTTP operation code to compare against that used by the client system under test.", 0, java.lang.Integer.MAX_VALUE, requestMethod));
          childrenList.add(new Property("requestURL", "string", "The value to use in a comparison against the request URL path string.", 0, java.lang.Integer.MAX_VALUE, requestURL));
          childrenList.add(new Property("resource", "code", "The type of the resource.  See http://build.fhir.org/resourcelist.html.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("response", "code", "okay | created | noContent | notModified | bad | forbidden | notFound | methodNotAllowed | conflict | gone | preconditionFailed | unprocessable.", 0, java.lang.Integer.MAX_VALUE, response));
          childrenList.add(new Property("responseCode", "string", "The value of the HTTP response code to be tested.", 0, java.lang.Integer.MAX_VALUE, responseCode));
          childrenList.add(new Property("rule", "", "The TestScript.rule this assert will evaluate.", 0, java.lang.Integer.MAX_VALUE, rule));
          childrenList.add(new Property("ruleset", "", "The TestScript.ruleset this assert will evaluate.", 0, java.lang.Integer.MAX_VALUE, ruleset));
          childrenList.add(new Property("sourceId", "id", "Fixture to evaluate the XPath/JSONPath expression or the headerField  against.", 0, java.lang.Integer.MAX_VALUE, sourceId));
          childrenList.add(new Property("validateProfileId", "id", "The ID of the Profile to validate against.", 0, java.lang.Integer.MAX_VALUE, validateProfileId));
          childrenList.add(new Property("value", "string", "The value to compare to.", 0, java.lang.Integer.MAX_VALUE, value));
          childrenList.add(new Property("warningOnly", "boolean", "Whether or not the test execution will produce a warning only on error for this assert.", 0, java.lang.Integer.MAX_VALUE, warningOnly));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 102727412: /*label*/ return this.label == null ? new Base[0] : new Base[] {this.label}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -962590849: /*direction*/ return this.direction == null ? new Base[0] : new Base[] {this.direction}; // Enumeration<AssertionDirectionType>
        case 2081856758: /*compareToSourceId*/ return this.compareToSourceId == null ? new Base[0] : new Base[] {this.compareToSourceId}; // StringType
        case -1415702669: /*compareToSourceExpression*/ return this.compareToSourceExpression == null ? new Base[0] : new Base[] {this.compareToSourceExpression}; // StringType
        case -790206144: /*compareToSourcePath*/ return this.compareToSourcePath == null ? new Base[0] : new Base[] {this.compareToSourcePath}; // StringType
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // Enumeration<ContentType>
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        case 1160732269: /*headerField*/ return this.headerField == null ? new Base[0] : new Base[] {this.headerField}; // StringType
        case 818925001: /*minimumId*/ return this.minimumId == null ? new Base[0] : new Base[] {this.minimumId}; // StringType
        case 1001488901: /*navigationLinks*/ return this.navigationLinks == null ? new Base[0] : new Base[] {this.navigationLinks}; // BooleanType
        case -500553564: /*operator*/ return this.operator == null ? new Base[0] : new Base[] {this.operator}; // Enumeration<AssertionOperatorType>
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case 1217874000: /*requestMethod*/ return this.requestMethod == null ? new Base[0] : new Base[] {this.requestMethod}; // Enumeration<TestScriptRequestMethodCode>
        case 37099616: /*requestURL*/ return this.requestURL == null ? new Base[0] : new Base[] {this.requestURL}; // StringType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // CodeType
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Enumeration<AssertionResponseTypes>
        case 1438723534: /*responseCode*/ return this.responseCode == null ? new Base[0] : new Base[] {this.responseCode}; // StringType
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : new Base[] {this.rule}; // ActionAssertRuleComponent
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // ActionAssertRulesetComponent
        case 1746327190: /*sourceId*/ return this.sourceId == null ? new Base[0] : new Base[] {this.sourceId}; // IdType
        case 1555541038: /*validateProfileId*/ return this.validateProfileId == null ? new Base[0] : new Base[] {this.validateProfileId}; // IdType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        case -481159832: /*warningOnly*/ return this.warningOnly == null ? new Base[0] : new Base[] {this.warningOnly}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 102727412: // label
          this.label = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -962590849: // direction
          value = new AssertionDirectionTypeEnumFactory().fromType(castToCode(value));
          this.direction = (Enumeration) value; // Enumeration<AssertionDirectionType>
          return value;
        case 2081856758: // compareToSourceId
          this.compareToSourceId = castToString(value); // StringType
          return value;
        case -1415702669: // compareToSourceExpression
          this.compareToSourceExpression = castToString(value); // StringType
          return value;
        case -790206144: // compareToSourcePath
          this.compareToSourcePath = castToString(value); // StringType
          return value;
        case -389131437: // contentType
          value = new ContentTypeEnumFactory().fromType(castToCode(value));
          this.contentType = (Enumeration) value; // Enumeration<ContentType>
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        case 1160732269: // headerField
          this.headerField = castToString(value); // StringType
          return value;
        case 818925001: // minimumId
          this.minimumId = castToString(value); // StringType
          return value;
        case 1001488901: // navigationLinks
          this.navigationLinks = castToBoolean(value); // BooleanType
          return value;
        case -500553564: // operator
          value = new AssertionOperatorTypeEnumFactory().fromType(castToCode(value));
          this.operator = (Enumeration) value; // Enumeration<AssertionOperatorType>
          return value;
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case 1217874000: // requestMethod
          value = new TestScriptRequestMethodCodeEnumFactory().fromType(castToCode(value));
          this.requestMethod = (Enumeration) value; // Enumeration<TestScriptRequestMethodCode>
          return value;
        case 37099616: // requestURL
          this.requestURL = castToString(value); // StringType
          return value;
        case -341064690: // resource
          this.resource = castToCode(value); // CodeType
          return value;
        case -340323263: // response
          value = new AssertionResponseTypesEnumFactory().fromType(castToCode(value));
          this.response = (Enumeration) value; // Enumeration<AssertionResponseTypes>
          return value;
        case 1438723534: // responseCode
          this.responseCode = castToString(value); // StringType
          return value;
        case 3512060: // rule
          this.rule = (ActionAssertRuleComponent) value; // ActionAssertRuleComponent
          return value;
        case 1548678118: // ruleset
          this.ruleset = (ActionAssertRulesetComponent) value; // ActionAssertRulesetComponent
          return value;
        case 1746327190: // sourceId
          this.sourceId = castToId(value); // IdType
          return value;
        case 1555541038: // validateProfileId
          this.validateProfileId = castToId(value); // IdType
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        case -481159832: // warningOnly
          this.warningOnly = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("label")) {
          this.label = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("direction")) {
          value = new AssertionDirectionTypeEnumFactory().fromType(castToCode(value));
          this.direction = (Enumeration) value; // Enumeration<AssertionDirectionType>
        } else if (name.equals("compareToSourceId")) {
          this.compareToSourceId = castToString(value); // StringType
        } else if (name.equals("compareToSourceExpression")) {
          this.compareToSourceExpression = castToString(value); // StringType
        } else if (name.equals("compareToSourcePath")) {
          this.compareToSourcePath = castToString(value); // StringType
        } else if (name.equals("contentType")) {
          value = new ContentTypeEnumFactory().fromType(castToCode(value));
          this.contentType = (Enumeration) value; // Enumeration<ContentType>
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else if (name.equals("headerField")) {
          this.headerField = castToString(value); // StringType
        } else if (name.equals("minimumId")) {
          this.minimumId = castToString(value); // StringType
        } else if (name.equals("navigationLinks")) {
          this.navigationLinks = castToBoolean(value); // BooleanType
        } else if (name.equals("operator")) {
          value = new AssertionOperatorTypeEnumFactory().fromType(castToCode(value));
          this.operator = (Enumeration) value; // Enumeration<AssertionOperatorType>
        } else if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("requestMethod")) {
          value = new TestScriptRequestMethodCodeEnumFactory().fromType(castToCode(value));
          this.requestMethod = (Enumeration) value; // Enumeration<TestScriptRequestMethodCode>
        } else if (name.equals("requestURL")) {
          this.requestURL = castToString(value); // StringType
        } else if (name.equals("resource")) {
          this.resource = castToCode(value); // CodeType
        } else if (name.equals("response")) {
          value = new AssertionResponseTypesEnumFactory().fromType(castToCode(value));
          this.response = (Enumeration) value; // Enumeration<AssertionResponseTypes>
        } else if (name.equals("responseCode")) {
          this.responseCode = castToString(value); // StringType
        } else if (name.equals("rule")) {
          this.rule = (ActionAssertRuleComponent) value; // ActionAssertRuleComponent
        } else if (name.equals("ruleset")) {
          this.ruleset = (ActionAssertRulesetComponent) value; // ActionAssertRulesetComponent
        } else if (name.equals("sourceId")) {
          this.sourceId = castToId(value); // IdType
        } else if (name.equals("validateProfileId")) {
          this.validateProfileId = castToId(value); // IdType
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else if (name.equals("warningOnly")) {
          this.warningOnly = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 102727412:  return getLabelElement();
        case -1724546052:  return getDescriptionElement();
        case -962590849:  return getDirectionElement();
        case 2081856758:  return getCompareToSourceIdElement();
        case -1415702669:  return getCompareToSourceExpressionElement();
        case -790206144:  return getCompareToSourcePathElement();
        case -389131437:  return getContentTypeElement();
        case -1795452264:  return getExpressionElement();
        case 1160732269:  return getHeaderFieldElement();
        case 818925001:  return getMinimumIdElement();
        case 1001488901:  return getNavigationLinksElement();
        case -500553564:  return getOperatorElement();
        case 3433509:  return getPathElement();
        case 1217874000:  return getRequestMethodElement();
        case 37099616:  return getRequestURLElement();
        case -341064690:  return getResourceElement();
        case -340323263:  return getResponseElement();
        case 1438723534:  return getResponseCodeElement();
        case 3512060:  return getRule(); 
        case 1548678118:  return getRuleset(); 
        case 1746327190:  return getSourceIdElement();
        case 1555541038:  return getValidateProfileIdElement();
        case 111972721:  return getValueElement();
        case -481159832:  return getWarningOnlyElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 102727412: /*label*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -962590849: /*direction*/ return new String[] {"code"};
        case 2081856758: /*compareToSourceId*/ return new String[] {"string"};
        case -1415702669: /*compareToSourceExpression*/ return new String[] {"string"};
        case -790206144: /*compareToSourcePath*/ return new String[] {"string"};
        case -389131437: /*contentType*/ return new String[] {"code"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        case 1160732269: /*headerField*/ return new String[] {"string"};
        case 818925001: /*minimumId*/ return new String[] {"string"};
        case 1001488901: /*navigationLinks*/ return new String[] {"boolean"};
        case -500553564: /*operator*/ return new String[] {"code"};
        case 3433509: /*path*/ return new String[] {"string"};
        case 1217874000: /*requestMethod*/ return new String[] {"code"};
        case 37099616: /*requestURL*/ return new String[] {"string"};
        case -341064690: /*resource*/ return new String[] {"code"};
        case -340323263: /*response*/ return new String[] {"code"};
        case 1438723534: /*responseCode*/ return new String[] {"string"};
        case 3512060: /*rule*/ return new String[] {};
        case 1548678118: /*ruleset*/ return new String[] {};
        case 1746327190: /*sourceId*/ return new String[] {"id"};
        case 1555541038: /*validateProfileId*/ return new String[] {"id"};
        case 111972721: /*value*/ return new String[] {"string"};
        case -481159832: /*warningOnly*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("label")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.label");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else if (name.equals("direction")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.direction");
        }
        else if (name.equals("compareToSourceId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.compareToSourceId");
        }
        else if (name.equals("compareToSourceExpression")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.compareToSourceExpression");
        }
        else if (name.equals("compareToSourcePath")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.compareToSourcePath");
        }
        else if (name.equals("contentType")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.contentType");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.expression");
        }
        else if (name.equals("headerField")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.headerField");
        }
        else if (name.equals("minimumId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.minimumId");
        }
        else if (name.equals("navigationLinks")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.navigationLinks");
        }
        else if (name.equals("operator")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.operator");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.path");
        }
        else if (name.equals("requestMethod")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.requestMethod");
        }
        else if (name.equals("requestURL")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.requestURL");
        }
        else if (name.equals("resource")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.resource");
        }
        else if (name.equals("response")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.response");
        }
        else if (name.equals("responseCode")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.responseCode");
        }
        else if (name.equals("rule")) {
          this.rule = new ActionAssertRuleComponent();
          return this.rule;
        }
        else if (name.equals("ruleset")) {
          this.ruleset = new ActionAssertRulesetComponent();
          return this.ruleset;
        }
        else if (name.equals("sourceId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.sourceId");
        }
        else if (name.equals("validateProfileId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.validateProfileId");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.value");
        }
        else if (name.equals("warningOnly")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.warningOnly");
        }
        else
          return super.addChild(name);
      }

      public SetupActionAssertComponent copy() {
        SetupActionAssertComponent dst = new SetupActionAssertComponent();
        copyValues(dst);
        dst.label = label == null ? null : label.copy();
        dst.description = description == null ? null : description.copy();
        dst.direction = direction == null ? null : direction.copy();
        dst.compareToSourceId = compareToSourceId == null ? null : compareToSourceId.copy();
        dst.compareToSourceExpression = compareToSourceExpression == null ? null : compareToSourceExpression.copy();
        dst.compareToSourcePath = compareToSourcePath == null ? null : compareToSourcePath.copy();
        dst.contentType = contentType == null ? null : contentType.copy();
        dst.expression = expression == null ? null : expression.copy();
        dst.headerField = headerField == null ? null : headerField.copy();
        dst.minimumId = minimumId == null ? null : minimumId.copy();
        dst.navigationLinks = navigationLinks == null ? null : navigationLinks.copy();
        dst.operator = operator == null ? null : operator.copy();
        dst.path = path == null ? null : path.copy();
        dst.requestMethod = requestMethod == null ? null : requestMethod.copy();
        dst.requestURL = requestURL == null ? null : requestURL.copy();
        dst.resource = resource == null ? null : resource.copy();
        dst.response = response == null ? null : response.copy();
        dst.responseCode = responseCode == null ? null : responseCode.copy();
        dst.rule = rule == null ? null : rule.copy();
        dst.ruleset = ruleset == null ? null : ruleset.copy();
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
        if (!(other instanceof SetupActionAssertComponent))
          return false;
        SetupActionAssertComponent o = (SetupActionAssertComponent) other;
        return compareDeep(label, o.label, true) && compareDeep(description, o.description, true) && compareDeep(direction, o.direction, true)
           && compareDeep(compareToSourceId, o.compareToSourceId, true) && compareDeep(compareToSourceExpression, o.compareToSourceExpression, true)
           && compareDeep(compareToSourcePath, o.compareToSourcePath, true) && compareDeep(contentType, o.contentType, true)
           && compareDeep(expression, o.expression, true) && compareDeep(headerField, o.headerField, true)
           && compareDeep(minimumId, o.minimumId, true) && compareDeep(navigationLinks, o.navigationLinks, true)
           && compareDeep(operator, o.operator, true) && compareDeep(path, o.path, true) && compareDeep(requestMethod, o.requestMethod, true)
           && compareDeep(requestURL, o.requestURL, true) && compareDeep(resource, o.resource, true) && compareDeep(response, o.response, true)
           && compareDeep(responseCode, o.responseCode, true) && compareDeep(rule, o.rule, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(sourceId, o.sourceId, true) && compareDeep(validateProfileId, o.validateProfileId, true)
           && compareDeep(value, o.value, true) && compareDeep(warningOnly, o.warningOnly, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionAssertComponent))
          return false;
        SetupActionAssertComponent o = (SetupActionAssertComponent) other;
        return compareValues(label, o.label, true) && compareValues(description, o.description, true) && compareValues(direction, o.direction, true)
           && compareValues(compareToSourceId, o.compareToSourceId, true) && compareValues(compareToSourceExpression, o.compareToSourceExpression, true)
           && compareValues(compareToSourcePath, o.compareToSourcePath, true) && compareValues(contentType, o.contentType, true)
           && compareValues(expression, o.expression, true) && compareValues(headerField, o.headerField, true)
           && compareValues(minimumId, o.minimumId, true) && compareValues(navigationLinks, o.navigationLinks, true)
           && compareValues(operator, o.operator, true) && compareValues(path, o.path, true) && compareValues(requestMethod, o.requestMethod, true)
           && compareValues(requestURL, o.requestURL, true) && compareValues(resource, o.resource, true) && compareValues(response, o.response, true)
           && compareValues(responseCode, o.responseCode, true) && compareValues(sourceId, o.sourceId, true) && compareValues(validateProfileId, o.validateProfileId, true)
           && compareValues(value, o.value, true) && compareValues(warningOnly, o.warningOnly, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(label, description, direction
          , compareToSourceId, compareToSourceExpression, compareToSourcePath, contentType, expression
          , headerField, minimumId, navigationLinks, operator, path, requestMethod, requestURL
          , resource, response, responseCode, rule, ruleset, sourceId, validateProfileId
          , value, warningOnly);
      }

  public String fhirType() {
    return "TestScript.setup.action.assert";

  }

  }

    @Block()
    public static class ActionAssertRuleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The TestScript.rule id value this assert will evaluate.
         */
        @Child(name = "ruleId", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of the TestScript.rule", formalDefinition="The TestScript.rule id value this assert will evaluate." )
        protected IdType ruleId;

        /**
         * Each rule template can take one or more parameters for rule evaluation.
         */
        @Child(name = "param", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Rule parameter template", formalDefinition="Each rule template can take one or more parameters for rule evaluation." )
        protected List<ActionAssertRuleParamComponent> param;

        private static final long serialVersionUID = -1860715431L;

    /**
     * Constructor
     */
      public ActionAssertRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionAssertRuleComponent(IdType ruleId) {
        super();
        this.ruleId = ruleId;
      }

        /**
         * @return {@link #ruleId} (The TestScript.rule id value this assert will evaluate.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public IdType getRuleIdElement() { 
          if (this.ruleId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionAssertRuleComponent.ruleId");
            else if (Configuration.doAutoCreate())
              this.ruleId = new IdType(); // bb
          return this.ruleId;
        }

        public boolean hasRuleIdElement() { 
          return this.ruleId != null && !this.ruleId.isEmpty();
        }

        public boolean hasRuleId() { 
          return this.ruleId != null && !this.ruleId.isEmpty();
        }

        /**
         * @param value {@link #ruleId} (The TestScript.rule id value this assert will evaluate.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public ActionAssertRuleComponent setRuleIdElement(IdType value) { 
          this.ruleId = value;
          return this;
        }

        /**
         * @return The TestScript.rule id value this assert will evaluate.
         */
        public String getRuleId() { 
          return this.ruleId == null ? null : this.ruleId.getValue();
        }

        /**
         * @param value The TestScript.rule id value this assert will evaluate.
         */
        public ActionAssertRuleComponent setRuleId(String value) { 
            if (this.ruleId == null)
              this.ruleId = new IdType();
            this.ruleId.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
        public List<ActionAssertRuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<ActionAssertRuleParamComponent>();
          return this.param;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionAssertRuleComponent setParam(List<ActionAssertRuleParamComponent> theParam) { 
          this.param = theParam;
          return this;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (ActionAssertRuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ActionAssertRuleParamComponent addParam() { //3
          ActionAssertRuleParamComponent t = new ActionAssertRuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<ActionAssertRuleParamComponent>();
          this.param.add(t);
          return t;
        }

        public ActionAssertRuleComponent addParam(ActionAssertRuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<ActionAssertRuleParamComponent>();
          this.param.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #param}, creating it if it does not already exist
         */
        public ActionAssertRuleParamComponent getParamFirstRep() { 
          if (getParam().isEmpty()) {
            addParam();
          }
          return getParam().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("ruleId", "id", "The TestScript.rule id value this assert will evaluate.", 0, java.lang.Integer.MAX_VALUE, ruleId));
          childrenList.add(new Property("param", "", "Each rule template can take one or more parameters for rule evaluation.", 0, java.lang.Integer.MAX_VALUE, param));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -919875273: /*ruleId*/ return this.ruleId == null ? new Base[0] : new Base[] {this.ruleId}; // IdType
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // ActionAssertRuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -919875273: // ruleId
          this.ruleId = castToId(value); // IdType
          return value;
        case 106436749: // param
          this.getParam().add((ActionAssertRuleParamComponent) value); // ActionAssertRuleParamComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("ruleId")) {
          this.ruleId = castToId(value); // IdType
        } else if (name.equals("param")) {
          this.getParam().add((ActionAssertRuleParamComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273:  return getRuleIdElement();
        case 106436749:  return addParam(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273: /*ruleId*/ return new String[] {"id"};
        case 106436749: /*param*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("ruleId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.ruleId");
        }
        else if (name.equals("param")) {
          return addParam();
        }
        else
          return super.addChild(name);
      }

      public ActionAssertRuleComponent copy() {
        ActionAssertRuleComponent dst = new ActionAssertRuleComponent();
        copyValues(dst);
        dst.ruleId = ruleId == null ? null : ruleId.copy();
        if (param != null) {
          dst.param = new ArrayList<ActionAssertRuleParamComponent>();
          for (ActionAssertRuleParamComponent i : param)
            dst.param.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionAssertRuleComponent))
          return false;
        ActionAssertRuleComponent o = (ActionAssertRuleComponent) other;
        return compareDeep(ruleId, o.ruleId, true) && compareDeep(param, o.param, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionAssertRuleComponent))
          return false;
        ActionAssertRuleComponent o = (ActionAssertRuleComponent) other;
        return compareValues(ruleId, o.ruleId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(ruleId, param);
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.rule";

  }

  }

    @Block()
    public static class ActionAssertRuleParamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Descriptive name for this parameter that matches the external assert rule parameter name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter name matching external assert rule parameter", formalDefinition="Descriptive name for this parameter that matches the external assert rule parameter name." )
        protected StringType name;

        /**
         * The value for the parameter that will be passed on to the external rule template.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter value defined either explicitly or dynamically", formalDefinition="The value for the parameter that will be passed on to the external rule template." )
        protected StringType value;

        private static final long serialVersionUID = 395259392L;

    /**
     * Constructor
     */
      public ActionAssertRuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionAssertRuleParamComponent(StringType name, StringType value) {
        super();
        this.name = name;
        this.value = value;
      }

        /**
         * @return {@link #name} (Descriptive name for this parameter that matches the external assert rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionAssertRuleParamComponent.name");
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
         * @param value {@link #name} (Descriptive name for this parameter that matches the external assert rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ActionAssertRuleParamComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Descriptive name for this parameter that matches the external assert rule parameter name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Descriptive name for this parameter that matches the external assert rule parameter name.
         */
        public ActionAssertRuleParamComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value for the parameter that will be passed on to the external rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionAssertRuleParamComponent.value");
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
         * @param value {@link #value} (The value for the parameter that will be passed on to the external rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ActionAssertRuleParamComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value for the parameter that will be passed on to the external rule template.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value for the parameter that will be passed on to the external rule template.
         */
        public ActionAssertRuleParamComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Descriptive name for this parameter that matches the external assert rule parameter name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value", "string", "The value for the parameter that will be passed on to the external rule template.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.value");
        }
        else
          return super.addChild(name);
      }

      public ActionAssertRuleParamComponent copy() {
        ActionAssertRuleParamComponent dst = new ActionAssertRuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionAssertRuleParamComponent))
          return false;
        ActionAssertRuleParamComponent o = (ActionAssertRuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionAssertRuleParamComponent))
          return false;
        ActionAssertRuleParamComponent o = (ActionAssertRuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, value);
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.rule.param";

  }

  }

    @Block()
    public static class ActionAssertRulesetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The TestScript.ruleset id value this assert will evaluate.
         */
        @Child(name = "rulesetId", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of the TestScript.ruleset", formalDefinition="The TestScript.ruleset id value this assert will evaluate." )
        protected IdType rulesetId;

        /**
         * The referenced rule within the external ruleset template.
         */
        @Child(name = "rule", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The referenced rule within the ruleset", formalDefinition="The referenced rule within the external ruleset template." )
        protected List<ActionAssertRulesetRuleComponent> rule;

        private static final long serialVersionUID = -976736025L;

    /**
     * Constructor
     */
      public ActionAssertRulesetComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionAssertRulesetComponent(IdType rulesetId) {
        super();
        this.rulesetId = rulesetId;
      }

        /**
         * @return {@link #rulesetId} (The TestScript.ruleset id value this assert will evaluate.). This is the underlying object with id, value and extensions. The accessor "getRulesetId" gives direct access to the value
         */
        public IdType getRulesetIdElement() { 
          if (this.rulesetId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionAssertRulesetComponent.rulesetId");
            else if (Configuration.doAutoCreate())
              this.rulesetId = new IdType(); // bb
          return this.rulesetId;
        }

        public boolean hasRulesetIdElement() { 
          return this.rulesetId != null && !this.rulesetId.isEmpty();
        }

        public boolean hasRulesetId() { 
          return this.rulesetId != null && !this.rulesetId.isEmpty();
        }

        /**
         * @param value {@link #rulesetId} (The TestScript.ruleset id value this assert will evaluate.). This is the underlying object with id, value and extensions. The accessor "getRulesetId" gives direct access to the value
         */
        public ActionAssertRulesetComponent setRulesetIdElement(IdType value) { 
          this.rulesetId = value;
          return this;
        }

        /**
         * @return The TestScript.ruleset id value this assert will evaluate.
         */
        public String getRulesetId() { 
          return this.rulesetId == null ? null : this.rulesetId.getValue();
        }

        /**
         * @param value The TestScript.ruleset id value this assert will evaluate.
         */
        public ActionAssertRulesetComponent setRulesetId(String value) { 
            if (this.rulesetId == null)
              this.rulesetId = new IdType();
            this.rulesetId.setValue(value);
          return this;
        }

        /**
         * @return {@link #rule} (The referenced rule within the external ruleset template.)
         */
        public List<ActionAssertRulesetRuleComponent> getRule() { 
          if (this.rule == null)
            this.rule = new ArrayList<ActionAssertRulesetRuleComponent>();
          return this.rule;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionAssertRulesetComponent setRule(List<ActionAssertRulesetRuleComponent> theRule) { 
          this.rule = theRule;
          return this;
        }

        public boolean hasRule() { 
          if (this.rule == null)
            return false;
          for (ActionAssertRulesetRuleComponent item : this.rule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ActionAssertRulesetRuleComponent addRule() { //3
          ActionAssertRulesetRuleComponent t = new ActionAssertRulesetRuleComponent();
          if (this.rule == null)
            this.rule = new ArrayList<ActionAssertRulesetRuleComponent>();
          this.rule.add(t);
          return t;
        }

        public ActionAssertRulesetComponent addRule(ActionAssertRulesetRuleComponent t) { //3
          if (t == null)
            return this;
          if (this.rule == null)
            this.rule = new ArrayList<ActionAssertRulesetRuleComponent>();
          this.rule.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #rule}, creating it if it does not already exist
         */
        public ActionAssertRulesetRuleComponent getRuleFirstRep() { 
          if (getRule().isEmpty()) {
            addRule();
          }
          return getRule().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("rulesetId", "id", "The TestScript.ruleset id value this assert will evaluate.", 0, java.lang.Integer.MAX_VALUE, rulesetId));
          childrenList.add(new Property("rule", "", "The referenced rule within the external ruleset template.", 0, java.lang.Integer.MAX_VALUE, rule));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -2073977951: /*rulesetId*/ return this.rulesetId == null ? new Base[0] : new Base[] {this.rulesetId}; // IdType
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // ActionAssertRulesetRuleComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -2073977951: // rulesetId
          this.rulesetId = castToId(value); // IdType
          return value;
        case 3512060: // rule
          this.getRule().add((ActionAssertRulesetRuleComponent) value); // ActionAssertRulesetRuleComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("rulesetId")) {
          this.rulesetId = castToId(value); // IdType
        } else if (name.equals("rule")) {
          this.getRule().add((ActionAssertRulesetRuleComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2073977951:  return getRulesetIdElement();
        case 3512060:  return addRule(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2073977951: /*rulesetId*/ return new String[] {"id"};
        case 3512060: /*rule*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("rulesetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.rulesetId");
        }
        else if (name.equals("rule")) {
          return addRule();
        }
        else
          return super.addChild(name);
      }

      public ActionAssertRulesetComponent copy() {
        ActionAssertRulesetComponent dst = new ActionAssertRulesetComponent();
        copyValues(dst);
        dst.rulesetId = rulesetId == null ? null : rulesetId.copy();
        if (rule != null) {
          dst.rule = new ArrayList<ActionAssertRulesetRuleComponent>();
          for (ActionAssertRulesetRuleComponent i : rule)
            dst.rule.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionAssertRulesetComponent))
          return false;
        ActionAssertRulesetComponent o = (ActionAssertRulesetComponent) other;
        return compareDeep(rulesetId, o.rulesetId, true) && compareDeep(rule, o.rule, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionAssertRulesetComponent))
          return false;
        ActionAssertRulesetComponent o = (ActionAssertRulesetComponent) other;
        return compareValues(rulesetId, o.rulesetId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(rulesetId, rule);
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.ruleset";

  }

  }

    @Block()
    public static class ActionAssertRulesetRuleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Id of the referenced rule within the external ruleset template.
         */
        @Child(name = "ruleId", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of referenced rule within the ruleset", formalDefinition="Id of the referenced rule within the external ruleset template." )
        protected IdType ruleId;

        /**
         * Each rule template can take one or more parameters for rule evaluation.
         */
        @Child(name = "param", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Rule parameter template", formalDefinition="Each rule template can take one or more parameters for rule evaluation." )
        protected List<ActionAssertRulesetRuleParamComponent> param;

        private static final long serialVersionUID = -1850698529L;

    /**
     * Constructor
     */
      public ActionAssertRulesetRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionAssertRulesetRuleComponent(IdType ruleId) {
        super();
        this.ruleId = ruleId;
      }

        /**
         * @return {@link #ruleId} (Id of the referenced rule within the external ruleset template.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public IdType getRuleIdElement() { 
          if (this.ruleId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionAssertRulesetRuleComponent.ruleId");
            else if (Configuration.doAutoCreate())
              this.ruleId = new IdType(); // bb
          return this.ruleId;
        }

        public boolean hasRuleIdElement() { 
          return this.ruleId != null && !this.ruleId.isEmpty();
        }

        public boolean hasRuleId() { 
          return this.ruleId != null && !this.ruleId.isEmpty();
        }

        /**
         * @param value {@link #ruleId} (Id of the referenced rule within the external ruleset template.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public ActionAssertRulesetRuleComponent setRuleIdElement(IdType value) { 
          this.ruleId = value;
          return this;
        }

        /**
         * @return Id of the referenced rule within the external ruleset template.
         */
        public String getRuleId() { 
          return this.ruleId == null ? null : this.ruleId.getValue();
        }

        /**
         * @param value Id of the referenced rule within the external ruleset template.
         */
        public ActionAssertRulesetRuleComponent setRuleId(String value) { 
            if (this.ruleId == null)
              this.ruleId = new IdType();
            this.ruleId.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
        public List<ActionAssertRulesetRuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<ActionAssertRulesetRuleParamComponent>();
          return this.param;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionAssertRulesetRuleComponent setParam(List<ActionAssertRulesetRuleParamComponent> theParam) { 
          this.param = theParam;
          return this;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (ActionAssertRulesetRuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ActionAssertRulesetRuleParamComponent addParam() { //3
          ActionAssertRulesetRuleParamComponent t = new ActionAssertRulesetRuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<ActionAssertRulesetRuleParamComponent>();
          this.param.add(t);
          return t;
        }

        public ActionAssertRulesetRuleComponent addParam(ActionAssertRulesetRuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<ActionAssertRulesetRuleParamComponent>();
          this.param.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #param}, creating it if it does not already exist
         */
        public ActionAssertRulesetRuleParamComponent getParamFirstRep() { 
          if (getParam().isEmpty()) {
            addParam();
          }
          return getParam().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("ruleId", "id", "Id of the referenced rule within the external ruleset template.", 0, java.lang.Integer.MAX_VALUE, ruleId));
          childrenList.add(new Property("param", "", "Each rule template can take one or more parameters for rule evaluation.", 0, java.lang.Integer.MAX_VALUE, param));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -919875273: /*ruleId*/ return this.ruleId == null ? new Base[0] : new Base[] {this.ruleId}; // IdType
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // ActionAssertRulesetRuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -919875273: // ruleId
          this.ruleId = castToId(value); // IdType
          return value;
        case 106436749: // param
          this.getParam().add((ActionAssertRulesetRuleParamComponent) value); // ActionAssertRulesetRuleParamComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("ruleId")) {
          this.ruleId = castToId(value); // IdType
        } else if (name.equals("param")) {
          this.getParam().add((ActionAssertRulesetRuleParamComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273:  return getRuleIdElement();
        case 106436749:  return addParam(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273: /*ruleId*/ return new String[] {"id"};
        case 106436749: /*param*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("ruleId")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.ruleId");
        }
        else if (name.equals("param")) {
          return addParam();
        }
        else
          return super.addChild(name);
      }

      public ActionAssertRulesetRuleComponent copy() {
        ActionAssertRulesetRuleComponent dst = new ActionAssertRulesetRuleComponent();
        copyValues(dst);
        dst.ruleId = ruleId == null ? null : ruleId.copy();
        if (param != null) {
          dst.param = new ArrayList<ActionAssertRulesetRuleParamComponent>();
          for (ActionAssertRulesetRuleParamComponent i : param)
            dst.param.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionAssertRulesetRuleComponent))
          return false;
        ActionAssertRulesetRuleComponent o = (ActionAssertRulesetRuleComponent) other;
        return compareDeep(ruleId, o.ruleId, true) && compareDeep(param, o.param, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionAssertRulesetRuleComponent))
          return false;
        ActionAssertRulesetRuleComponent o = (ActionAssertRulesetRuleComponent) other;
        return compareValues(ruleId, o.ruleId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(ruleId, param);
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.ruleset.rule";

  }

  }

    @Block()
    public static class ActionAssertRulesetRuleParamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Descriptive name for this parameter that matches the external assert ruleset rule parameter name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter name matching external assert ruleset rule parameter", formalDefinition="Descriptive name for this parameter that matches the external assert ruleset rule parameter name." )
        protected StringType name;

        /**
         * The value for the parameter that will be passed on to the external ruleset rule template.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter value defined either explicitly or dynamically", formalDefinition="The value for the parameter that will be passed on to the external ruleset rule template." )
        protected StringType value;

        private static final long serialVersionUID = 395259392L;

    /**
     * Constructor
     */
      public ActionAssertRulesetRuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionAssertRulesetRuleParamComponent(StringType name, StringType value) {
        super();
        this.name = name;
        this.value = value;
      }

        /**
         * @return {@link #name} (Descriptive name for this parameter that matches the external assert ruleset rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionAssertRulesetRuleParamComponent.name");
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
         * @param value {@link #name} (Descriptive name for this parameter that matches the external assert ruleset rule parameter name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ActionAssertRulesetRuleParamComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Descriptive name for this parameter that matches the external assert ruleset rule parameter name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Descriptive name for this parameter that matches the external assert ruleset rule parameter name.
         */
        public ActionAssertRulesetRuleParamComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value for the parameter that will be passed on to the external ruleset rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionAssertRulesetRuleParamComponent.value");
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
         * @param value {@link #value} (The value for the parameter that will be passed on to the external ruleset rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ActionAssertRulesetRuleParamComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value for the parameter that will be passed on to the external ruleset rule template.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value for the parameter that will be passed on to the external ruleset rule template.
         */
        public ActionAssertRulesetRuleParamComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Descriptive name for this parameter that matches the external assert ruleset rule parameter name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value", "string", "The value for the parameter that will be passed on to the external ruleset rule template.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("value")) {
          this.value = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 111972721:  return getValueElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.value");
        }
        else
          return super.addChild(name);
      }

      public ActionAssertRulesetRuleParamComponent copy() {
        ActionAssertRulesetRuleParamComponent dst = new ActionAssertRulesetRuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActionAssertRulesetRuleParamComponent))
          return false;
        ActionAssertRulesetRuleParamComponent o = (ActionAssertRulesetRuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActionAssertRulesetRuleParamComponent))
          return false;
        ActionAssertRulesetRuleParamComponent o = (ActionAssertRulesetRuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, value);
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.ruleset.rule.param";

  }

  }

    @Block()
    public static class TestScriptTestComponent extends BackboneElement implements IBaseBackboneElement {
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
        @Description(shortDefinition="A test operation or assert to perform", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<TestActionComponent> action;

        private static final long serialVersionUID = -865006110L;

    /**
     * Constructor
     */
      public TestScriptTestComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of this test used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
         * @param value {@link #name} (The name of this test used for tracking/logging purposes by test engines.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public TestScriptTestComponent setNameElement(StringType value) { 
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
         * @return {@link #description} (A short description of the test used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
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
         * @param value {@link #description} (A short description of the test used by test engines for tracking and reporting purposes.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public TestScriptTestComponent setDescriptionElement(StringType value) { 
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
        public TestScriptTestComponent setAction(List<TestActionComponent> theAction) { 
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

        public TestScriptTestComponent addAction(TestActionComponent t) { //3
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
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public TestScriptTestComponent copy() {
        TestScriptTestComponent dst = new TestScriptTestComponent();
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
        if (!(other instanceof TestScriptTestComponent))
          return false;
        TestScriptTestComponent o = (TestScriptTestComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(action, o.action, true)
          ;
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, description, action
          );
      }

  public String fhirType() {
    return "TestScript.test";

  }

  }

    @Block()
    public static class TestActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An operation would involve a REST request to a server.
         */
        @Child(name = "operation", type = {SetupActionOperationComponent.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The setup operation to perform", formalDefinition="An operation would involve a REST request to a server." )
        protected SetupActionOperationComponent operation;

        /**
         * Evaluates the results of previous operations to determine if the server under test behaves appropriately.
         */
        @Child(name = "assert", type = {SetupActionAssertComponent.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The setup assertion to perform", formalDefinition="Evaluates the results of previous operations to determine if the server under test behaves appropriately." )
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
         * @return {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
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
         * @param value {@link #assert_} (Evaluates the results of previous operations to determine if the server under test behaves appropriately.)
         */
        public TestActionComponent setAssert(SetupActionAssertComponent value) { 
          this.assert_ = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("operation", "@TestScript.setup.action.operation", "An operation would involve a REST request to a server.", 0, java.lang.Integer.MAX_VALUE, operation));
          childrenList.add(new Property("assert", "@TestScript.setup.action.assert", "Evaluates the results of previous operations to determine if the server under test behaves appropriately.", 0, java.lang.Integer.MAX_VALUE, assert_));
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
        case 1662702951: /*operation*/ return new String[] {"@TestScript.setup.action.operation"};
        case -1408208058: /*assert*/ return new String[] {"@TestScript.setup.action.assert"};
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
    return "TestScript.test.action";

  }

  }

    @Block()
    public static class TestScriptTeardownComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The teardown action will only contain an operation.
         */
        @Child(name = "action", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="One or more teardown operations to perform", formalDefinition="The teardown action will only contain an operation." )
        protected List<TeardownActionComponent> action;

        private static final long serialVersionUID = 1168638089L;

    /**
     * Constructor
     */
      public TestScriptTeardownComponent() {
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
        public TestScriptTeardownComponent setAction(List<TeardownActionComponent> theAction) { 
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

        public TestScriptTeardownComponent addAction(TeardownActionComponent t) { //3
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

      public TestScriptTeardownComponent copy() {
        TestScriptTeardownComponent dst = new TestScriptTeardownComponent();
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(action);
      }

  public String fhirType() {
    return "TestScript.teardown";

  }

  }

    @Block()
    public static class TeardownActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An operation would involve a REST request to a server.
         */
        @Child(name = "operation", type = {SetupActionOperationComponent.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The teardown operation to perform", formalDefinition="An operation would involve a REST request to a server." )
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
          childrenList.add(new Property("operation", "@TestScript.setup.action.operation", "An operation would involve a REST request to a server.", 0, java.lang.Integer.MAX_VALUE, operation));
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
        case 1662702951: /*operation*/ return new String[] {"@TestScript.setup.action.operation"};
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
    return "TestScript.teardown.action";

  }

  }

    /**
     * A formal identifier that is used to identify this test script when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the test script", formalDefinition="A formal identifier that is used to identify this test script when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected Identifier identifier;

    /**
     * Explaination of why this test script is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this test script is defined", formalDefinition="Explaination of why this test script is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the test script.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the test script." )
    protected MarkdownType copyright;

    /**
     * An abstract server used in operations within this test script in the origin element.
     */
    @Child(name = "origin", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="An abstract server representing a client or sender in a message exchange", formalDefinition="An abstract server used in operations within this test script in the origin element." )
    protected List<TestScriptOriginComponent> origin;

    /**
     * An abstract server used in operations within this test script in the destination element.
     */
    @Child(name = "destination", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="An abstract server representing a destination or receiver in a message exchange", formalDefinition="An abstract server used in operations within this test script in the destination element." )
    protected List<TestScriptDestinationComponent> destination;

    /**
     * The required capability must exist and are assumed to function correctly on the FHIR server being tested.
     */
    @Child(name = "metadata", type = {}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Required capability that is assumed to function correctly on the FHIR server being tested", formalDefinition="The required capability must exist and are assumed to function correctly on the FHIR server being tested." )
    protected TestScriptMetadataComponent metadata;

    /**
     * Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.
     */
    @Child(name = "fixture", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Fixture in the test script - by reference (uri)", formalDefinition="Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute." )
    protected List<TestScriptFixtureComponent> fixture;

    /**
     * Reference to the profile to be used for validation.
     */
    @Child(name = "profile", type = {Reference.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reference of the validation profile", formalDefinition="Reference to the profile to be used for validation." )
    protected List<Reference> profile;
    /**
     * The actual objects that are the target of the reference (Reference to the profile to be used for validation.)
     */
    protected List<Resource> profileTarget;


    /**
     * Variable is set based either on element value in response body or on header field value in the response headers.
     */
    @Child(name = "variable", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Placeholder for evaluated elements", formalDefinition="Variable is set based either on element value in response body or on header field value in the response headers." )
    protected List<TestScriptVariableComponent> variable;

    /**
     * Assert rule to be used in one or more asserts within the test script.
     */
    @Child(name = "rule", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Assert rule used within the test script", formalDefinition="Assert rule to be used in one or more asserts within the test script." )
    protected List<TestScriptRuleComponent> rule;

    /**
     * Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied.
     */
    @Child(name = "ruleset", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Assert ruleset used within the test script", formalDefinition="Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied." )
    protected List<TestScriptRulesetComponent> ruleset;

    /**
     * A series of required setup operations before tests are executed.
     */
    @Child(name = "setup", type = {}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A series of required setup operations before tests are executed", formalDefinition="A series of required setup operations before tests are executed." )
    protected TestScriptSetupComponent setup;

    /**
     * A test in this script.
     */
    @Child(name = "test", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A test in this script", formalDefinition="A test in this script." )
    protected List<TestScriptTestComponent> test;

    /**
     * A series of operations required to clean up after the all the tests are executed (successfully or otherwise).
     */
    @Child(name = "teardown", type = {}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A series of required clean up steps", formalDefinition="A series of operations required to clean up after the all the tests are executed (successfully or otherwise)." )
    protected TestScriptTeardownComponent teardown;

    private static final long serialVersionUID = 1164952373L;

  /**
   * Constructor
   */
    public TestScript() {
      super();
    }

  /**
   * Constructor
   */
    public TestScript(UriType url, StringType name, Enumeration<PublicationStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this test script when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this test script is (or will be) published. The URL SHOULD include the major version of the test script. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URI that is used to identify this test script when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this test script is (or will be) published. The URL SHOULD include the major version of the test script. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public TestScript setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this test script when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this test script is (or will be) published. The URL SHOULD include the major version of the test script. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this test script when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this test script is (or will be) published. The URL SHOULD include the major version of the test script. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public TestScript setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (A formal identifier that is used to identify this test script when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (A formal identifier that is used to identify this test script when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public TestScript setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the test script when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the test script author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
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
     * @param value {@link #version} (The identifier that is used to identify this version of the test script when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the test script author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public TestScript setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the test script when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the test script author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the test script when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the test script author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
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
     * @return {@link #name} (A natural language name identifying the test script. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (A natural language name identifying the test script. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public TestScript setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the test script. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the test script. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public TestScript setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #title} (A short, descriptive, user-friendly title for the test script.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for the test script.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public TestScript setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the test script.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the test script.
     */
    public TestScript setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of this test script. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this test script. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public TestScript setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this test script. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this test script. Enables tracking the life-cycle of the content.
     */
    public TestScript setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this test script is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (A boolean value to indicate that this test script is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public TestScript setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this test script is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this test script is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public TestScript setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the test script was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the test script changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date  (and optionally time) when the test script was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the test script changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public TestScript setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the test script was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the test script changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the test script was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the test script changes.
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
     * @return {@link #publisher} (The name of the individual or organization that published the test script.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
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
     * @param value {@link #publisher} (The name of the individual or organization that published the test script.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public TestScript setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the test script.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the test script.
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
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public TestScript addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the test script from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the test script from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public TestScript setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the test script from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the test script from a consumer's perspective.
     */
    public TestScript setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate test script instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public TestScript addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the test script is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public TestScript addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #purpose} (Explaination of why this test script is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explaination of why this test script is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public TestScript setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this test script is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this test script is needed and why it has been designed as it has.
     */
    public TestScript setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the test script.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create TestScript.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new MarkdownType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the test script.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public TestScript setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the test script.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the test script.
     */
    public TestScript setCopyright(String value) { 
      if (value == null)
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new MarkdownType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #origin} (An abstract server used in operations within this test script in the origin element.)
     */
    public List<TestScriptOriginComponent> getOrigin() { 
      if (this.origin == null)
        this.origin = new ArrayList<TestScriptOriginComponent>();
      return this.origin;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setOrigin(List<TestScriptOriginComponent> theOrigin) { 
      this.origin = theOrigin;
      return this;
    }

    public boolean hasOrigin() { 
      if (this.origin == null)
        return false;
      for (TestScriptOriginComponent item : this.origin)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestScriptOriginComponent addOrigin() { //3
      TestScriptOriginComponent t = new TestScriptOriginComponent();
      if (this.origin == null)
        this.origin = new ArrayList<TestScriptOriginComponent>();
      this.origin.add(t);
      return t;
    }

    public TestScript addOrigin(TestScriptOriginComponent t) { //3
      if (t == null)
        return this;
      if (this.origin == null)
        this.origin = new ArrayList<TestScriptOriginComponent>();
      this.origin.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #origin}, creating it if it does not already exist
     */
    public TestScriptOriginComponent getOriginFirstRep() { 
      if (getOrigin().isEmpty()) {
        addOrigin();
      }
      return getOrigin().get(0);
    }

    /**
     * @return {@link #destination} (An abstract server used in operations within this test script in the destination element.)
     */
    public List<TestScriptDestinationComponent> getDestination() { 
      if (this.destination == null)
        this.destination = new ArrayList<TestScriptDestinationComponent>();
      return this.destination;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setDestination(List<TestScriptDestinationComponent> theDestination) { 
      this.destination = theDestination;
      return this;
    }

    public boolean hasDestination() { 
      if (this.destination == null)
        return false;
      for (TestScriptDestinationComponent item : this.destination)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestScriptDestinationComponent addDestination() { //3
      TestScriptDestinationComponent t = new TestScriptDestinationComponent();
      if (this.destination == null)
        this.destination = new ArrayList<TestScriptDestinationComponent>();
      this.destination.add(t);
      return t;
    }

    public TestScript addDestination(TestScriptDestinationComponent t) { //3
      if (t == null)
        return this;
      if (this.destination == null)
        this.destination = new ArrayList<TestScriptDestinationComponent>();
      this.destination.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #destination}, creating it if it does not already exist
     */
    public TestScriptDestinationComponent getDestinationFirstRep() { 
      if (getDestination().isEmpty()) {
        addDestination();
      }
      return getDestination().get(0);
    }

    /**
     * @return {@link #metadata} (The required capability must exist and are assumed to function correctly on the FHIR server being tested.)
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
     * @param value {@link #metadata} (The required capability must exist and are assumed to function correctly on the FHIR server being tested.)
     */
    public TestScript setMetadata(TestScriptMetadataComponent value) { 
      this.metadata = value;
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setFixture(List<TestScriptFixtureComponent> theFixture) { 
      this.fixture = theFixture;
      return this;
    }

    public boolean hasFixture() { 
      if (this.fixture == null)
        return false;
      for (TestScriptFixtureComponent item : this.fixture)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestScriptFixtureComponent addFixture() { //3
      TestScriptFixtureComponent t = new TestScriptFixtureComponent();
      if (this.fixture == null)
        this.fixture = new ArrayList<TestScriptFixtureComponent>();
      this.fixture.add(t);
      return t;
    }

    public TestScript addFixture(TestScriptFixtureComponent t) { //3
      if (t == null)
        return this;
      if (this.fixture == null)
        this.fixture = new ArrayList<TestScriptFixtureComponent>();
      this.fixture.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #fixture}, creating it if it does not already exist
     */
    public TestScriptFixtureComponent getFixtureFirstRep() { 
      if (getFixture().isEmpty()) {
        addFixture();
      }
      return getFixture().get(0);
    }

    /**
     * @return {@link #profile} (Reference to the profile to be used for validation.)
     */
    public List<Reference> getProfile() { 
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      return this.profile;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setProfile(List<Reference> theProfile) { 
      this.profile = theProfile;
      return this;
    }

    public boolean hasProfile() { 
      if (this.profile == null)
        return false;
      for (Reference item : this.profile)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addProfile() { //3
      Reference t = new Reference();
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      this.profile.add(t);
      return t;
    }

    public TestScript addProfile(Reference t) { //3
      if (t == null)
        return this;
      if (this.profile == null)
        this.profile = new ArrayList<Reference>();
      this.profile.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #profile}, creating it if it does not already exist
     */
    public Reference getProfileFirstRep() { 
      if (getProfile().isEmpty()) {
        addProfile();
      }
      return getProfile().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setVariable(List<TestScriptVariableComponent> theVariable) { 
      this.variable = theVariable;
      return this;
    }

    public boolean hasVariable() { 
      if (this.variable == null)
        return false;
      for (TestScriptVariableComponent item : this.variable)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestScriptVariableComponent addVariable() { //3
      TestScriptVariableComponent t = new TestScriptVariableComponent();
      if (this.variable == null)
        this.variable = new ArrayList<TestScriptVariableComponent>();
      this.variable.add(t);
      return t;
    }

    public TestScript addVariable(TestScriptVariableComponent t) { //3
      if (t == null)
        return this;
      if (this.variable == null)
        this.variable = new ArrayList<TestScriptVariableComponent>();
      this.variable.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #variable}, creating it if it does not already exist
     */
    public TestScriptVariableComponent getVariableFirstRep() { 
      if (getVariable().isEmpty()) {
        addVariable();
      }
      return getVariable().get(0);
    }

    /**
     * @return {@link #rule} (Assert rule to be used in one or more asserts within the test script.)
     */
    public List<TestScriptRuleComponent> getRule() { 
      if (this.rule == null)
        this.rule = new ArrayList<TestScriptRuleComponent>();
      return this.rule;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setRule(List<TestScriptRuleComponent> theRule) { 
      this.rule = theRule;
      return this;
    }

    public boolean hasRule() { 
      if (this.rule == null)
        return false;
      for (TestScriptRuleComponent item : this.rule)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestScriptRuleComponent addRule() { //3
      TestScriptRuleComponent t = new TestScriptRuleComponent();
      if (this.rule == null)
        this.rule = new ArrayList<TestScriptRuleComponent>();
      this.rule.add(t);
      return t;
    }

    public TestScript addRule(TestScriptRuleComponent t) { //3
      if (t == null)
        return this;
      if (this.rule == null)
        this.rule = new ArrayList<TestScriptRuleComponent>();
      this.rule.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #rule}, creating it if it does not already exist
     */
    public TestScriptRuleComponent getRuleFirstRep() { 
      if (getRule().isEmpty()) {
        addRule();
      }
      return getRule().get(0);
    }

    /**
     * @return {@link #ruleset} (Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied.)
     */
    public List<TestScriptRulesetComponent> getRuleset() { 
      if (this.ruleset == null)
        this.ruleset = new ArrayList<TestScriptRulesetComponent>();
      return this.ruleset;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setRuleset(List<TestScriptRulesetComponent> theRuleset) { 
      this.ruleset = theRuleset;
      return this;
    }

    public boolean hasRuleset() { 
      if (this.ruleset == null)
        return false;
      for (TestScriptRulesetComponent item : this.ruleset)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestScriptRulesetComponent addRuleset() { //3
      TestScriptRulesetComponent t = new TestScriptRulesetComponent();
      if (this.ruleset == null)
        this.ruleset = new ArrayList<TestScriptRulesetComponent>();
      this.ruleset.add(t);
      return t;
    }

    public TestScript addRuleset(TestScriptRulesetComponent t) { //3
      if (t == null)
        return this;
      if (this.ruleset == null)
        this.ruleset = new ArrayList<TestScriptRulesetComponent>();
      this.ruleset.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #ruleset}, creating it if it does not already exist
     */
    public TestScriptRulesetComponent getRulesetFirstRep() { 
      if (getRuleset().isEmpty()) {
        addRuleset();
      }
      return getRuleset().get(0);
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public TestScript setTest(List<TestScriptTestComponent> theTest) { 
      this.test = theTest;
      return this;
    }

    public boolean hasTest() { 
      if (this.test == null)
        return false;
      for (TestScriptTestComponent item : this.test)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TestScriptTestComponent addTest() { //3
      TestScriptTestComponent t = new TestScriptTestComponent();
      if (this.test == null)
        this.test = new ArrayList<TestScriptTestComponent>();
      this.test.add(t);
      return t;
    }

    public TestScript addTest(TestScriptTestComponent t) { //3
      if (t == null)
        return this;
      if (this.test == null)
        this.test = new ArrayList<TestScriptTestComponent>();
      this.test.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #test}, creating it if it does not already exist
     */
    public TestScriptTestComponent getTestFirstRep() { 
      if (getTest().isEmpty()) {
        addTest();
      }
      return getTest().get(0);
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
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this test script when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this test script is (or will be) published. The URL SHOULD include the major version of the test script. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A formal identifier that is used to identify this test script when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the test script when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the test script author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the test script. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the test script.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of this test script. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this test script is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the test script was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the test script changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the test script.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the test script from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate test script instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the test script is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this test script is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("copyright", "markdown", "A copyright statement relating to the test script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the test script.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("origin", "", "An abstract server used in operations within this test script in the origin element.", 0, java.lang.Integer.MAX_VALUE, origin));
        childrenList.add(new Property("destination", "", "An abstract server used in operations within this test script in the destination element.", 0, java.lang.Integer.MAX_VALUE, destination));
        childrenList.add(new Property("metadata", "", "The required capability must exist and are assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, metadata));
        childrenList.add(new Property("fixture", "", "Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.", 0, java.lang.Integer.MAX_VALUE, fixture));
        childrenList.add(new Property("profile", "Reference(Any)", "Reference to the profile to be used for validation.", 0, java.lang.Integer.MAX_VALUE, profile));
        childrenList.add(new Property("variable", "", "Variable is set based either on element value in response body or on header field value in the response headers.", 0, java.lang.Integer.MAX_VALUE, variable));
        childrenList.add(new Property("rule", "", "Assert rule to be used in one or more asserts within the test script.", 0, java.lang.Integer.MAX_VALUE, rule));
        childrenList.add(new Property("ruleset", "", "Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("setup", "", "A series of required setup operations before tests are executed.", 0, java.lang.Integer.MAX_VALUE, setup));
        childrenList.add(new Property("test", "", "A test in this script.", 0, java.lang.Integer.MAX_VALUE, test));
        childrenList.add(new Property("teardown", "", "A series of operations required to clean up after the all the tests are executed (successfully or otherwise).", 0, java.lang.Integer.MAX_VALUE, teardown));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case -1008619738: /*origin*/ return this.origin == null ? new Base[0] : this.origin.toArray(new Base[this.origin.size()]); // TestScriptOriginComponent
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : this.destination.toArray(new Base[this.destination.size()]); // TestScriptDestinationComponent
        case -450004177: /*metadata*/ return this.metadata == null ? new Base[0] : new Base[] {this.metadata}; // TestScriptMetadataComponent
        case -843449847: /*fixture*/ return this.fixture == null ? new Base[0] : this.fixture.toArray(new Base[this.fixture.size()]); // TestScriptFixtureComponent
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : this.profile.toArray(new Base[this.profile.size()]); // Reference
        case -1249586564: /*variable*/ return this.variable == null ? new Base[0] : this.variable.toArray(new Base[this.variable.size()]); // TestScriptVariableComponent
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // TestScriptRuleComponent
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : this.ruleset.toArray(new Base[this.ruleset.size()]); // TestScriptRulesetComponent
        case 109329021: /*setup*/ return this.setup == null ? new Base[0] : new Base[] {this.setup}; // TestScriptSetupComponent
        case 3556498: /*test*/ return this.test == null ? new Base[0] : this.test.toArray(new Base[this.test.size()]); // TestScriptTestComponent
        case -1663474172: /*teardown*/ return this.teardown == null ? new Base[0] : new Base[] {this.teardown}; // TestScriptTeardownComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case -1008619738: // origin
          this.getOrigin().add((TestScriptOriginComponent) value); // TestScriptOriginComponent
          return value;
        case -1429847026: // destination
          this.getDestination().add((TestScriptDestinationComponent) value); // TestScriptDestinationComponent
          return value;
        case -450004177: // metadata
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
          return value;
        case -843449847: // fixture
          this.getFixture().add((TestScriptFixtureComponent) value); // TestScriptFixtureComponent
          return value;
        case -309425751: // profile
          this.getProfile().add(castToReference(value)); // Reference
          return value;
        case -1249586564: // variable
          this.getVariable().add((TestScriptVariableComponent) value); // TestScriptVariableComponent
          return value;
        case 3512060: // rule
          this.getRule().add((TestScriptRuleComponent) value); // TestScriptRuleComponent
          return value;
        case 1548678118: // ruleset
          this.getRuleset().add((TestScriptRulesetComponent) value); // TestScriptRulesetComponent
          return value;
        case 109329021: // setup
          this.setup = (TestScriptSetupComponent) value; // TestScriptSetupComponent
          return value;
        case 3556498: // test
          this.getTest().add((TestScriptTestComponent) value); // TestScriptTestComponent
          return value;
        case -1663474172: // teardown
          this.teardown = (TestScriptTeardownComponent) value; // TestScriptTeardownComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("origin")) {
          this.getOrigin().add((TestScriptOriginComponent) value);
        } else if (name.equals("destination")) {
          this.getDestination().add((TestScriptDestinationComponent) value);
        } else if (name.equals("metadata")) {
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
        } else if (name.equals("fixture")) {
          this.getFixture().add((TestScriptFixtureComponent) value);
        } else if (name.equals("profile")) {
          this.getProfile().add(castToReference(value));
        } else if (name.equals("variable")) {
          this.getVariable().add((TestScriptVariableComponent) value);
        } else if (name.equals("rule")) {
          this.getRule().add((TestScriptRuleComponent) value);
        } else if (name.equals("ruleset")) {
          this.getRuleset().add((TestScriptRulesetComponent) value);
        } else if (name.equals("setup")) {
          this.setup = (TestScriptSetupComponent) value; // TestScriptSetupComponent
        } else if (name.equals("test")) {
          this.getTest().add((TestScriptTestComponent) value);
        } else if (name.equals("teardown")) {
          this.teardown = (TestScriptTeardownComponent) value; // TestScriptTeardownComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case -1618432855:  return getIdentifier(); 
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case -220463842:  return getPurposeElement();
        case 1522889671:  return getCopyrightElement();
        case -1008619738:  return addOrigin(); 
        case -1429847026:  return addDestination(); 
        case -450004177:  return getMetadata(); 
        case -843449847:  return addFixture(); 
        case -309425751:  return addProfile(); 
        case -1249586564:  return addVariable(); 
        case 3512060:  return addRule(); 
        case 1548678118:  return addRuleset(); 
        case 109329021:  return getSetup(); 
        case 3556498:  return addTest(); 
        case -1663474172:  return getTeardown(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case -1008619738: /*origin*/ return new String[] {};
        case -1429847026: /*destination*/ return new String[] {};
        case -450004177: /*metadata*/ return new String[] {};
        case -843449847: /*fixture*/ return new String[] {};
        case -309425751: /*profile*/ return new String[] {"Reference"};
        case -1249586564: /*variable*/ return new String[] {};
        case 3512060: /*rule*/ return new String[] {};
        case 1548678118: /*ruleset*/ return new String[] {};
        case 109329021: /*setup*/ return new String[] {};
        case 3556498: /*test*/ return new String[] {};
        case -1663474172: /*teardown*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.url");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.purpose");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.copyright");
        }
        else if (name.equals("origin")) {
          return addOrigin();
        }
        else if (name.equals("destination")) {
          return addDestination();
        }
        else if (name.equals("metadata")) {
          this.metadata = new TestScriptMetadataComponent();
          return this.metadata;
        }
        else if (name.equals("fixture")) {
          return addFixture();
        }
        else if (name.equals("profile")) {
          return addProfile();
        }
        else if (name.equals("variable")) {
          return addVariable();
        }
        else if (name.equals("rule")) {
          return addRule();
        }
        else if (name.equals("ruleset")) {
          return addRuleset();
        }
        else if (name.equals("setup")) {
          this.setup = new TestScriptSetupComponent();
          return this.setup;
        }
        else if (name.equals("test")) {
          return addTest();
        }
        else if (name.equals("teardown")) {
          this.teardown = new TestScriptTeardownComponent();
          return this.teardown;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "TestScript";

  }

      public TestScript copy() {
        TestScript dst = new TestScript();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        if (origin != null) {
          dst.origin = new ArrayList<TestScriptOriginComponent>();
          for (TestScriptOriginComponent i : origin)
            dst.origin.add(i.copy());
        };
        if (destination != null) {
          dst.destination = new ArrayList<TestScriptDestinationComponent>();
          for (TestScriptDestinationComponent i : destination)
            dst.destination.add(i.copy());
        };
        dst.metadata = metadata == null ? null : metadata.copy();
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
        if (rule != null) {
          dst.rule = new ArrayList<TestScriptRuleComponent>();
          for (TestScriptRuleComponent i : rule)
            dst.rule.add(i.copy());
        };
        if (ruleset != null) {
          dst.ruleset = new ArrayList<TestScriptRulesetComponent>();
          for (TestScriptRulesetComponent i : ruleset)
            dst.ruleset.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(purpose, o.purpose, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(origin, o.origin, true) && compareDeep(destination, o.destination, true) && compareDeep(metadata, o.metadata, true)
           && compareDeep(fixture, o.fixture, true) && compareDeep(profile, o.profile, true) && compareDeep(variable, o.variable, true)
           && compareDeep(rule, o.rule, true) && compareDeep(ruleset, o.ruleset, true) && compareDeep(setup, o.setup, true)
           && compareDeep(test, o.test, true) && compareDeep(teardown, o.teardown, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScript))
          return false;
        TestScript o = (TestScript) other;
        return compareValues(purpose, o.purpose, true) && compareValues(copyright, o.copyright, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, purpose, copyright
          , origin, destination, metadata, fixture, profile, variable, rule, ruleset, setup
          , test, teardown);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.TestScript;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The test script publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>TestScript.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="TestScript.date", description="The test script publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The test script publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>TestScript.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="TestScript.identifier", description="External identifier for the test script", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier for the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="TestScript.jurisdiction", description="Intended jurisdiction for the test script", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="TestScript.name", description="Computationally friendly name of the test script", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="TestScript.description", description="The description of the test script", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="TestScript.publisher", description="Name of the publisher of the test script", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>testscript-capability</b>
   * <p>
   * Description: <b>TestScript required and validated capability</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.metadata.capability.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="testscript-capability", path="TestScript.metadata.capability.description", description="TestScript required and validated capability", type="string" )
  public static final String SP_TESTSCRIPT_CAPABILITY = "testscript-capability";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>testscript-capability</b>
   * <p>
   * Description: <b>TestScript required and validated capability</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.metadata.capability.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TESTSCRIPT_CAPABILITY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TESTSCRIPT_CAPABILITY);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="TestScript.title", description="The human-friendly name of the test script", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the test script</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="TestScript.version", description="The business version of the test script", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the test script</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>TestScript.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="TestScript.url", description="The uri that identifies the test script", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the test script</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>TestScript.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="TestScript.status", description="The current status of the test script", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the test script</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

