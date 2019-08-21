package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu2016may.model.Enumerations.ConformanceResourceStatus;
import org.hl7.fhir.dstu2016may.model.Enumerations.ConformanceResourceStatusEnumFactory;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * TestScript is a resource that specifies a suite of tests against a FHIR server implementation to determine compliance against the FHIR specification.
 */
@ResourceDef(name="TestScript", profile="http://hl7.org/fhir/Profile/TestScript")
public class TestScript extends DomainResource {

    public enum ContentType {
        /**
         * XML content-type corresponding to the application/xml+fhir mime-type.
         */
        XML, 
        /**
         * JSON content-type corresponding to the application/json+fhir mime-type.
         */
        JSON, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("xml".equals(codeString))
          return XML;
        if ("json".equals(codeString))
          return JSON;
        throw new FHIRException("Unknown ContentType code '"+codeString+"'");
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
            case XML: return "XML content-type corresponding to the application/xml+fhir mime-type.";
            case JSON: return "JSON content-type corresponding to the application/json+fhir mime-type.";
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
        public Enumeration<ContentType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("xml".equals(codeString))
          return new Enumeration<ContentType>(this, ContentType.XML);
        if ("json".equals(codeString))
          return new Enumeration<ContentType>(this, ContentType.JSON);
        throw new FHIRException("Unknown ContentType code '"+codeString+"'");
        }
    public String toCode(ContentType code) {
      if (code == ContentType.XML)
        return "xml";
      if (code == ContentType.JSON)
        return "json";
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
         * added to help the parsers
         */
        NULL;
        public static AssertionDirectionType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("response".equals(codeString))
          return RESPONSE;
        if ("request".equals(codeString))
          return REQUEST;
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
          if (code == null || code.isEmpty())
            return null;
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
         * added to help the parsers
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
        public Enumeration<AssertionOperatorType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
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
      return "?";
      }
    public String toSystem(AssertionOperatorType code) {
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
         * added to help the parsers
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
          if (code == null || code.isEmpty())
            return null;
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
    public static class TestScriptContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the Test Script.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of an individual to contact", formalDefinition="The name of an individual to contact regarding the Test Script." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /**
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("telecom"))
          this.getTelecom().add(castToContactPoint(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1429363305:  return addTelecom(); // ContactPoint
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else
          return super.addChild(name);
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

  public String fhirType() {
    return "TestScript.contact";

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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 100346066: // index
          this.index = castToInteger(value); // IntegerType
          break;
        case -309425751: // profile
          this.profile = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("index"))
          this.index = castToInteger(value); // IntegerType
        else if (name.equals("profile"))
          this.profile = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 100346066: throw new FHIRException("Cannot make property index as it is not a complex type"); // IntegerType
        case -309425751:  return getProfile(); // Coding
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (index == null || index.isEmpty()) && (profile == null || profile.isEmpty())
          ;
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 100346066: // index
          this.index = castToInteger(value); // IntegerType
          break;
        case -309425751: // profile
          this.profile = castToCoding(value); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("index"))
          this.index = castToInteger(value); // IntegerType
        else if (name.equals("profile"))
          this.profile = castToCoding(value); // Coding
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 100346066: throw new FHIRException("Cannot make property index as it is not a complex type"); // IntegerType
        case -309425751:  return getProfile(); // Coding
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (index == null || index.isEmpty()) && (profile == null || profile.isEmpty())
          ;
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
         * @return {@link #capability} (Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.)
         */
        public List<TestScriptMetadataCapabilityComponent> getCapability() { 
          if (this.capability == null)
            this.capability = new ArrayList<TestScriptMetadataCapabilityComponent>();
          return this.capability;
        }

        public boolean hasCapability() { 
          if (this.capability == null)
            return false;
          for (TestScriptMetadataCapabilityComponent item : this.capability)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #capability} (Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.)
         */
    // syntactic sugar
        public TestScriptMetadataCapabilityComponent addCapability() { //3
          TestScriptMetadataCapabilityComponent t = new TestScriptMetadataCapabilityComponent();
          if (this.capability == null)
            this.capability = new ArrayList<TestScriptMetadataCapabilityComponent>();
          this.capability.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptMetadataComponent addCapability(TestScriptMetadataCapabilityComponent t) { //3
          if (t == null)
            return this;
          if (this.capability == null)
            this.capability = new ArrayList<TestScriptMetadataCapabilityComponent>();
          this.capability.add(t);
          return this;
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3321850: // link
          this.getLink().add((TestScriptMetadataLinkComponent) value); // TestScriptMetadataLinkComponent
          break;
        case -783669992: // capability
          this.getCapability().add((TestScriptMetadataCapabilityComponent) value); // TestScriptMetadataCapabilityComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("link"))
          this.getLink().add((TestScriptMetadataLinkComponent) value);
        else if (name.equals("capability"))
          this.getCapability().add((TestScriptMetadataCapabilityComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3321850:  return addLink(); // TestScriptMetadataLinkComponent
        case -783669992:  return addCapability(); // TestScriptMetadataCapabilityComponent
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (link == null || link.isEmpty()) && (capability == null || capability.isEmpty())
          ;
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (url == null || url.isEmpty()) && (description == null || description.isEmpty())
          ;
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
         * Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.
         */
        @Child(name = "conformance", type = {Conformance.class}, order=7, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Required Conformance", formalDefinition="Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped." )
        protected Reference conformance;

        /**
         * The actual object that is the target of the reference (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        protected Conformance conformanceTarget;

        private static final long serialVersionUID = 500671983L;

    /**
     * Constructor
     */
      public TestScriptMetadataCapabilityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptMetadataCapabilityComponent(Reference conformance) {
        super();
        this.conformance = conformance;
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
    // syntactic sugar
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
         * @return {@link #conformance} (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        public Reference getConformance() { 
          if (this.conformance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.conformance");
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
        public TestScriptMetadataCapabilityComponent setConformance(Reference value) { 
          this.conformance = value;
          return this;
        }

        /**
         * @return {@link #conformance} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        public Conformance getConformanceTarget() { 
          if (this.conformanceTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptMetadataCapabilityComponent.conformance");
            else if (Configuration.doAutoCreate())
              this.conformanceTarget = new Conformance(); // aa
          return this.conformanceTarget;
        }

        /**
         * @param value {@link #conformance} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.)
         */
        public TestScriptMetadataCapabilityComponent setConformanceTarget(Conformance value) { 
          this.conformanceTarget = value;
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
          childrenList.add(new Property("conformance", "Reference(Conformance)", "Minimum conformance required of server for test script to execute successfully.   If server does not meet at a minimum the reference conformance definition, then all tests in this script are skipped.", 0, java.lang.Integer.MAX_VALUE, conformance));
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
        case 1374858133: /*conformance*/ return this.conformance == null ? new Base[0] : new Base[] {this.conformance}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -393139297: // required
          this.required = castToBoolean(value); // BooleanType
          break;
        case -1109784050: // validated
          this.validated = castToBoolean(value); // BooleanType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -1008619738: // origin
          this.getOrigin().add(castToInteger(value)); // IntegerType
          break;
        case -1429847026: // destination
          this.destination = castToInteger(value); // IntegerType
          break;
        case 3321850: // link
          this.getLink().add(castToUri(value)); // UriType
          break;
        case 1374858133: // conformance
          this.conformance = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("required"))
          this.required = castToBoolean(value); // BooleanType
        else if (name.equals("validated"))
          this.validated = castToBoolean(value); // BooleanType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("origin"))
          this.getOrigin().add(castToInteger(value));
        else if (name.equals("destination"))
          this.destination = castToInteger(value); // IntegerType
        else if (name.equals("link"))
          this.getLink().add(castToUri(value));
        else if (name.equals("conformance"))
          this.conformance = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -393139297: throw new FHIRException("Cannot make property required as it is not a complex type"); // BooleanType
        case -1109784050: throw new FHIRException("Cannot make property validated as it is not a complex type"); // BooleanType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -1008619738: throw new FHIRException("Cannot make property origin as it is not a complex type"); // IntegerType
        case -1429847026: throw new FHIRException("Cannot make property destination as it is not a complex type"); // IntegerType
        case 3321850: throw new FHIRException("Cannot make property link as it is not a complex type"); // UriType
        case 1374858133:  return getConformance(); // Reference
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("conformance")) {
          this.conformance = new Reference();
          return this.conformance;
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
        dst.conformance = conformance == null ? null : conformance.copy();
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
           && compareDeep(conformance, o.conformance, true);
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
        return super.isEmpty() && (required == null || required.isEmpty()) && (validated == null || validated.isEmpty())
           && (description == null || description.isEmpty()) && (origin == null || origin.isEmpty())
           && (destination == null || destination.isEmpty()) && (link == null || link.isEmpty()) && (conformance == null || conformance.isEmpty())
          ;
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 73154411: // autocreate
          this.autocreate = castToBoolean(value); // BooleanType
          break;
        case 89990170: // autodelete
          this.autodelete = castToBoolean(value); // BooleanType
          break;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("autocreate"))
          this.autocreate = castToBoolean(value); // BooleanType
        else if (name.equals("autodelete"))
          this.autodelete = castToBoolean(value); // BooleanType
        else if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 73154411: throw new FHIRException("Cannot make property autocreate as it is not a complex type"); // BooleanType
        case 89990170: throw new FHIRException("Cannot make property autodelete as it is not a complex type"); // BooleanType
        case -341064690:  return getResource(); // Reference
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (autocreate == null || autocreate.isEmpty()) && (autodelete == null || autodelete.isEmpty())
           && (resource == null || resource.isEmpty());
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
         * Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.
         */
        @Child(name = "headerField", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP header field name for source", formalDefinition="Will be used to grab the HTTP header field value from the headers that sourceId is pointing to." )
        protected StringType headerField;

        /**
         * XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.
         */
        @Child(name = "path", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="XPath or JSONPath against the fixture body", formalDefinition="XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both." )
        protected StringType path;

        /**
         * Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of source expression or headerField within this variable", formalDefinition="Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable." )
        protected IdType sourceId;

        private static final long serialVersionUID = 1821729272L;

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
          childrenList.add(new Property("headerField", "string", "Will be used to grab the HTTP header field value from the headers that sourceId is pointing to.", 0, java.lang.Integer.MAX_VALUE, headerField));
          childrenList.add(new Property("path", "string", "XPath or JSONPath against the fixture body.  When variables are defined, either headerField must be specified or path, but not both.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("sourceId", "id", "Fixture to evaluate the XPath/JSONPath expression or the headerField  against within this variable.", 0, java.lang.Integer.MAX_VALUE, sourceId));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -659125328: /*defaultValue*/ return this.defaultValue == null ? new Base[0] : new Base[] {this.defaultValue}; // StringType
        case 1160732269: /*headerField*/ return this.headerField == null ? new Base[0] : new Base[] {this.headerField}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case 1746327190: /*sourceId*/ return this.sourceId == null ? new Base[0] : new Base[] {this.sourceId}; // IdType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -659125328: // defaultValue
          this.defaultValue = castToString(value); // StringType
          break;
        case 1160732269: // headerField
          this.headerField = castToString(value); // StringType
          break;
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        case 1746327190: // sourceId
          this.sourceId = castToId(value); // IdType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("defaultValue"))
          this.defaultValue = castToString(value); // StringType
        else if (name.equals("headerField"))
          this.headerField = castToString(value); // StringType
        else if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("sourceId"))
          this.sourceId = castToId(value); // IdType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -659125328: throw new FHIRException("Cannot make property defaultValue as it is not a complex type"); // StringType
        case 1160732269: throw new FHIRException("Cannot make property headerField as it is not a complex type"); // StringType
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        case 1746327190: throw new FHIRException("Cannot make property sourceId as it is not a complex type"); // IdType
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("headerField")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.headerField");
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
        return compareDeep(name, o.name, true) && compareDeep(defaultValue, o.defaultValue, true) && compareDeep(headerField, o.headerField, true)
           && compareDeep(path, o.path, true) && compareDeep(sourceId, o.sourceId, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptVariableComponent))
          return false;
        TestScriptVariableComponent o = (TestScriptVariableComponent) other;
        return compareValues(name, o.name, true) && compareValues(defaultValue, o.defaultValue, true) && compareValues(headerField, o.headerField, true)
           && compareValues(path, o.path, true) && compareValues(sourceId, o.sourceId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (defaultValue == null || defaultValue.isEmpty())
           && (headerField == null || headerField.isEmpty()) && (path == null || path.isEmpty()) && (sourceId == null || sourceId.isEmpty())
          ;
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
        @Child(name = "resource", type = {}, order=1, min=1, max=1, modifier=false, summary=false)
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
        protected List<TestScriptRuleParamComponent> param;

        private static final long serialVersionUID = -524260474L;

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
        public List<TestScriptRuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<TestScriptRuleParamComponent>();
          return this.param;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (TestScriptRuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
    // syntactic sugar
        public TestScriptRuleParamComponent addParam() { //3
          TestScriptRuleParamComponent t = new TestScriptRuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<TestScriptRuleParamComponent>();
          this.param.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptRuleComponent addParam(TestScriptRuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<TestScriptRuleParamComponent>();
          this.param.add(t);
          return this;
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
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // TestScriptRuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          break;
        case 106436749: // param
          this.getParam().add((TestScriptRuleParamComponent) value); // TestScriptRuleParamComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else if (name.equals("param"))
          this.getParam().add((TestScriptRuleParamComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -341064690:  return getResource(); // Reference
        case 106436749:  return addParam(); // TestScriptRuleParamComponent
        default: return super.makeProperty(hash, name);
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
          dst.param = new ArrayList<TestScriptRuleParamComponent>();
          for (TestScriptRuleParamComponent i : param)
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
        return super.isEmpty() && (resource == null || resource.isEmpty()) && (param == null || param.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.rule";

  }

  }

    @Block()
    public static class TestScriptRuleParamComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Descriptive name for this parameter that matches the external assert rule parameter name.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter name matching external assert rule parameter", formalDefinition="Descriptive name for this parameter that matches the external assert rule parameter name." )
        protected StringType name;

        /**
         * The explict or dynamic value for the parameter that will be passed on to the external rule template.
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Parameter value defined either explicitly or dynamically", formalDefinition="The explict or dynamic value for the parameter that will be passed on to the external rule template." )
        protected StringType value;

        private static final long serialVersionUID = 395259392L;

    /**
     * Constructor
     */
      public TestScriptRuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptRuleParamComponent(StringType name, StringType value) {
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
              throw new Error("Attempt to auto-create TestScriptRuleParamComponent.name");
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
        public TestScriptRuleParamComponent setNameElement(StringType value) { 
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
        public TestScriptRuleParamComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The explict or dynamic value for the parameter that will be passed on to the external rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptRuleParamComponent.value");
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
         * @param value {@link #value} (The explict or dynamic value for the parameter that will be passed on to the external rule template.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public TestScriptRuleParamComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The explict or dynamic value for the parameter that will be passed on to the external rule template.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The explict or dynamic value for the parameter that will be passed on to the external rule template.
         */
        public TestScriptRuleParamComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Descriptive name for this parameter that matches the external assert rule parameter name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value", "string", "The explict or dynamic value for the parameter that will be passed on to the external rule template.", 0, java.lang.Integer.MAX_VALUE, value));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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

      public TestScriptRuleParamComponent copy() {
        TestScriptRuleParamComponent dst = new TestScriptRuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptRuleParamComponent))
          return false;
        TestScriptRuleParamComponent o = (TestScriptRuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptRuleParamComponent))
          return false;
        TestScriptRuleParamComponent o = (TestScriptRuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
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
        @Child(name = "resource", type = {}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Assert ruleset resource reference", formalDefinition="Reference to the resource (containing the contents of the ruleset needed for assertions)." )
        protected Reference resource;

        /**
         * The actual object that is the target of the reference (Reference to the resource (containing the contents of the ruleset needed for assertions).)
         */
        protected Resource resourceTarget;

        /**
         * The referenced rule within the external ruleset template.
         */
        @Child(name = "rule", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The referenced rule within the ruleset", formalDefinition="The referenced rule within the external ruleset template." )
        protected List<TestScriptRulesetRuleComponent> rule;

        private static final long serialVersionUID = 5813554L;

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
        public List<TestScriptRulesetRuleComponent> getRule() { 
          if (this.rule == null)
            this.rule = new ArrayList<TestScriptRulesetRuleComponent>();
          return this.rule;
        }

        public boolean hasRule() { 
          if (this.rule == null)
            return false;
          for (TestScriptRulesetRuleComponent item : this.rule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #rule} (The referenced rule within the external ruleset template.)
         */
    // syntactic sugar
        public TestScriptRulesetRuleComponent addRule() { //3
          TestScriptRulesetRuleComponent t = new TestScriptRulesetRuleComponent();
          if (this.rule == null)
            this.rule = new ArrayList<TestScriptRulesetRuleComponent>();
          this.rule.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptRulesetComponent addRule(TestScriptRulesetRuleComponent t) { //3
          if (t == null)
            return this;
          if (this.rule == null)
            this.rule = new ArrayList<TestScriptRulesetRuleComponent>();
          this.rule.add(t);
          return this;
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
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // TestScriptRulesetRuleComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          break;
        case 3512060: // rule
          this.getRule().add((TestScriptRulesetRuleComponent) value); // TestScriptRulesetRuleComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else if (name.equals("rule"))
          this.getRule().add((TestScriptRulesetRuleComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -341064690:  return getResource(); // Reference
        case 3512060:  return addRule(); // TestScriptRulesetRuleComponent
        default: return super.makeProperty(hash, name);
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
          dst.rule = new ArrayList<TestScriptRulesetRuleComponent>();
          for (TestScriptRulesetRuleComponent i : rule)
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
        return super.isEmpty() && (resource == null || resource.isEmpty()) && (rule == null || rule.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.ruleset";

  }

  }

    @Block()
    public static class TestScriptRulesetRuleComponent extends BackboneElement implements IBaseBackboneElement {
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
        protected List<TestScriptRulesetRuleParamComponent> param;

        private static final long serialVersionUID = 155033950L;

    /**
     * Constructor
     */
      public TestScriptRulesetRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptRulesetRuleComponent(IdType ruleId) {
        super();
        this.ruleId = ruleId;
      }

        /**
         * @return {@link #ruleId} (Id of the referenced rule within the external ruleset template.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public IdType getRuleIdElement() { 
          if (this.ruleId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TestScriptRulesetRuleComponent.ruleId");
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
        public TestScriptRulesetRuleComponent setRuleIdElement(IdType value) { 
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
        public TestScriptRulesetRuleComponent setRuleId(String value) { 
            if (this.ruleId == null)
              this.ruleId = new IdType();
            this.ruleId.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
        public List<TestScriptRulesetRuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<TestScriptRulesetRuleParamComponent>();
          return this.param;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (TestScriptRulesetRuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
    // syntactic sugar
        public TestScriptRulesetRuleParamComponent addParam() { //3
          TestScriptRulesetRuleParamComponent t = new TestScriptRulesetRuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<TestScriptRulesetRuleParamComponent>();
          this.param.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptRulesetRuleComponent addParam(TestScriptRulesetRuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<TestScriptRulesetRuleParamComponent>();
          this.param.add(t);
          return this;
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
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // TestScriptRulesetRuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -919875273: // ruleId
          this.ruleId = castToId(value); // IdType
          break;
        case 106436749: // param
          this.getParam().add((TestScriptRulesetRuleParamComponent) value); // TestScriptRulesetRuleParamComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("ruleId"))
          this.ruleId = castToId(value); // IdType
        else if (name.equals("param"))
          this.getParam().add((TestScriptRulesetRuleParamComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273: throw new FHIRException("Cannot make property ruleId as it is not a complex type"); // IdType
        case 106436749:  return addParam(); // TestScriptRulesetRuleParamComponent
        default: return super.makeProperty(hash, name);
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

      public TestScriptRulesetRuleComponent copy() {
        TestScriptRulesetRuleComponent dst = new TestScriptRulesetRuleComponent();
        copyValues(dst);
        dst.ruleId = ruleId == null ? null : ruleId.copy();
        if (param != null) {
          dst.param = new ArrayList<TestScriptRulesetRuleParamComponent>();
          for (TestScriptRulesetRuleParamComponent i : param)
            dst.param.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptRulesetRuleComponent))
          return false;
        TestScriptRulesetRuleComponent o = (TestScriptRulesetRuleComponent) other;
        return compareDeep(ruleId, o.ruleId, true) && compareDeep(param, o.param, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptRulesetRuleComponent))
          return false;
        TestScriptRulesetRuleComponent o = (TestScriptRulesetRuleComponent) other;
        return compareValues(ruleId, o.ruleId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (ruleId == null || ruleId.isEmpty()) && (param == null || param.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.ruleset.rule";

  }

  }

    @Block()
    public static class TestScriptRulesetRuleParamComponent extends BackboneElement implements IBaseBackboneElement {
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
      public TestScriptRulesetRuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TestScriptRulesetRuleParamComponent(StringType name, StringType value) {
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
              throw new Error("Attempt to auto-create TestScriptRulesetRuleParamComponent.name");
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
        public TestScriptRulesetRuleParamComponent setNameElement(StringType value) { 
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
        public TestScriptRulesetRuleParamComponent setName(String value) { 
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
              throw new Error("Attempt to auto-create TestScriptRulesetRuleParamComponent.value");
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
        public TestScriptRulesetRuleParamComponent setValueElement(StringType value) { 
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
        public TestScriptRulesetRuleParamComponent setValue(String value) { 
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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

      public TestScriptRulesetRuleParamComponent copy() {
        TestScriptRulesetRuleParamComponent dst = new TestScriptRulesetRuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TestScriptRulesetRuleParamComponent))
          return false;
        TestScriptRulesetRuleParamComponent o = (TestScriptRulesetRuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TestScriptRulesetRuleParamComponent))
          return false;
        TestScriptRulesetRuleParamComponent o = (TestScriptRulesetRuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.ruleset.rule.param";

  }

  }

    @Block()
    public static class TestScriptSetupComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.
         */
        @Child(name = "metadata", type = {TestScriptMetadataComponent.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Capabilities  that are assumed to function correctly on the FHIR server being tested", formalDefinition="Capabilities that must exist and are assumed to function correctly on the FHIR server being tested." )
        protected TestScriptMetadataComponent metadata;

        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A setup operation or assert to perform", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<SetupActionComponent> action;

        private static final long serialVersionUID = 9850834L;

    /**
     * Constructor
     */
      public TestScriptSetupComponent() {
        super();
      }

        /**
         * @return {@link #metadata} (Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.)
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
         * @param value {@link #metadata} (Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.)
         */
        public TestScriptSetupComponent setMetadata(TestScriptMetadataComponent value) { 
          this.metadata = value;
          return this;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
        public List<SetupActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<SetupActionComponent>();
          return this.action;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (SetupActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
    // syntactic sugar
        public SetupActionComponent addAction() { //3
          SetupActionComponent t = new SetupActionComponent();
          if (this.action == null)
            this.action = new ArrayList<SetupActionComponent>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptSetupComponent addAction(SetupActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<SetupActionComponent>();
          this.action.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("metadata", "@TestScript.metadata", "Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, metadata));
          childrenList.add(new Property("action", "", "Action would contain either an operation or an assertion.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -450004177: /*metadata*/ return this.metadata == null ? new Base[0] : new Base[] {this.metadata}; // TestScriptMetadataComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // SetupActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -450004177: // metadata
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
          break;
        case -1422950858: // action
          this.getAction().add((SetupActionComponent) value); // SetupActionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("metadata"))
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
        else if (name.equals("action"))
          this.getAction().add((SetupActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -450004177:  return getMetadata(); // TestScriptMetadataComponent
        case -1422950858:  return addAction(); // SetupActionComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("metadata")) {
          this.metadata = new TestScriptMetadataComponent();
          return this.metadata;
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else
          return super.addChild(name);
      }

      public TestScriptSetupComponent copy() {
        TestScriptSetupComponent dst = new TestScriptSetupComponent();
        copyValues(dst);
        dst.metadata = metadata == null ? null : metadata.copy();
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1662702951: // operation
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
          break;
        case -1408208058: // assert
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("operation"))
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
        else if (name.equals("assert"))
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951:  return getOperation(); // SetupActionOperationComponent
        case -1408208058:  return getAssert(); // SetupActionAssertComponent
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (operation == null || operation.isEmpty()) && (assert_ == null || assert_.isEmpty())
          ;
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
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Accept' header." )
        protected Enumeration<ContentType> accept;

        /**
         * The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.
         */
        @Child(name = "contentType", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="xml | json", formalDefinition="The content-type or mime-type to use for RESTful operation in the 'Content-Type' header." )
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
        @Description(shortDefinition="Each operation can have one ore more header elements", formalDefinition="Header elements would be used to set HTTP headers." )
        protected List<SetupActionOperationRequestHeaderComponent> requestHeader;

        /**
         * The fixture id (maybe new) to map to the response.
         */
        @Child(name = "responseId", type = {IdType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of mapped response", formalDefinition="The fixture id (maybe new) to map to the response." )
        protected IdType responseId;

        /**
         * The id of the fixture used as the body of a PUT or POST request.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of body for PUT and POST requests", formalDefinition="The id of the fixture used as the body of a PUT or POST request." )
        protected IdType sourceId;

        /**
         * Id of fixture used for extracting the [id],  [type], and [vid] for GET requests.
         */
        @Child(name = "targetId", type = {IdType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Id of fixture used for extracting the [id],  [type], and [vid] for GET requests", formalDefinition="Id of fixture used for extracting the [id],  [type], and [vid] for GET requests." )
        protected IdType targetId;

        /**
         * Complete request URL.
         */
        @Child(name = "url", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Request URL", formalDefinition="Complete request URL." )
        protected StringType url;

        private static final long serialVersionUID = 606457859L;

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
         * @return {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
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
         * @param value {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public SetupActionOperationComponent setResourceElement(CodeType value) { 
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

        public boolean hasRequestHeader() { 
          if (this.requestHeader == null)
            return false;
          for (SetupActionOperationRequestHeaderComponent item : this.requestHeader)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #requestHeader} (Header elements would be used to set HTTP headers.)
         */
    // syntactic sugar
        public SetupActionOperationRequestHeaderComponent addRequestHeader() { //3
          SetupActionOperationRequestHeaderComponent t = new SetupActionOperationRequestHeaderComponent();
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<SetupActionOperationRequestHeaderComponent>();
          this.requestHeader.add(t);
          return t;
        }

    // syntactic sugar
        public SetupActionOperationComponent addRequestHeader(SetupActionOperationRequestHeaderComponent t) { //3
          if (t == null)
            return this;
          if (this.requestHeader == null)
            this.requestHeader = new ArrayList<SetupActionOperationRequestHeaderComponent>();
          this.requestHeader.add(t);
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
          childrenList.add(new Property("resource", "code", "The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.", 0, java.lang.Integer.MAX_VALUE, resource));
          childrenList.add(new Property("label", "string", "The label would be used for tracking/logging purposes by test engines.", 0, java.lang.Integer.MAX_VALUE, label));
          childrenList.add(new Property("description", "string", "The description would be used by test engines for tracking and reporting purposes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("accept", "code", "The content-type or mime-type to use for RESTful operation in the 'Accept' header.", 0, java.lang.Integer.MAX_VALUE, accept));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.", 0, java.lang.Integer.MAX_VALUE, contentType));
          childrenList.add(new Property("destination", "integer", "The server where the request message is destined for.  Must be one of the server numbers listed in TestScript.destination section.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("encodeRequestUrl", "boolean", "Whether or not to implicitly send the request url in encoded format. The default is true to match the standard RESTful client behavior. Set to false when communicating with a server that does not support encoded url paths.", 0, java.lang.Integer.MAX_VALUE, encodeRequestUrl));
          childrenList.add(new Property("origin", "integer", "The server where the request message originates from.  Must be one of the server numbers listed in TestScript.origin section.", 0, java.lang.Integer.MAX_VALUE, origin));
          childrenList.add(new Property("params", "string", "Path plus parameters after [type].  Used to set parts of the request URL explicitly.", 0, java.lang.Integer.MAX_VALUE, params));
          childrenList.add(new Property("requestHeader", "", "Header elements would be used to set HTTP headers.", 0, java.lang.Integer.MAX_VALUE, requestHeader));
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
        case -633138884: /*responseId*/ return this.responseId == null ? new Base[0] : new Base[] {this.responseId}; // IdType
        case 1746327190: /*sourceId*/ return this.sourceId == null ? new Base[0] : new Base[] {this.sourceId}; // IdType
        case -441951604: /*targetId*/ return this.targetId == null ? new Base[0] : new Base[] {this.targetId}; // IdType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case -341064690: // resource
          this.resource = castToCode(value); // CodeType
          break;
        case 102727412: // label
          this.label = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -1423461112: // accept
          this.accept = new ContentTypeEnumFactory().fromType(value); // Enumeration<ContentType>
          break;
        case -389131437: // contentType
          this.contentType = new ContentTypeEnumFactory().fromType(value); // Enumeration<ContentType>
          break;
        case -1429847026: // destination
          this.destination = castToInteger(value); // IntegerType
          break;
        case -1760554218: // encodeRequestUrl
          this.encodeRequestUrl = castToBoolean(value); // BooleanType
          break;
        case -1008619738: // origin
          this.origin = castToInteger(value); // IntegerType
          break;
        case -995427962: // params
          this.params = castToString(value); // StringType
          break;
        case 1074158076: // requestHeader
          this.getRequestHeader().add((SetupActionOperationRequestHeaderComponent) value); // SetupActionOperationRequestHeaderComponent
          break;
        case -633138884: // responseId
          this.responseId = castToId(value); // IdType
          break;
        case 1746327190: // sourceId
          this.sourceId = castToId(value); // IdType
          break;
        case -441951604: // targetId
          this.targetId = castToId(value); // IdType
          break;
        case 116079: // url
          this.url = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("resource"))
          this.resource = castToCode(value); // CodeType
        else if (name.equals("label"))
          this.label = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("accept"))
          this.accept = new ContentTypeEnumFactory().fromType(value); // Enumeration<ContentType>
        else if (name.equals("contentType"))
          this.contentType = new ContentTypeEnumFactory().fromType(value); // Enumeration<ContentType>
        else if (name.equals("destination"))
          this.destination = castToInteger(value); // IntegerType
        else if (name.equals("encodeRequestUrl"))
          this.encodeRequestUrl = castToBoolean(value); // BooleanType
        else if (name.equals("origin"))
          this.origin = castToInteger(value); // IntegerType
        else if (name.equals("params"))
          this.params = castToString(value); // StringType
        else if (name.equals("requestHeader"))
          this.getRequestHeader().add((SetupActionOperationRequestHeaderComponent) value);
        else if (name.equals("responseId"))
          this.responseId = castToId(value); // IdType
        else if (name.equals("sourceId"))
          this.sourceId = castToId(value); // IdType
        else if (name.equals("targetId"))
          this.targetId = castToId(value); // IdType
        else if (name.equals("url"))
          this.url = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case -341064690: throw new FHIRException("Cannot make property resource as it is not a complex type"); // CodeType
        case 102727412: throw new FHIRException("Cannot make property label as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -1423461112: throw new FHIRException("Cannot make property accept as it is not a complex type"); // Enumeration<ContentType>
        case -389131437: throw new FHIRException("Cannot make property contentType as it is not a complex type"); // Enumeration<ContentType>
        case -1429847026: throw new FHIRException("Cannot make property destination as it is not a complex type"); // IntegerType
        case -1760554218: throw new FHIRException("Cannot make property encodeRequestUrl as it is not a complex type"); // BooleanType
        case -1008619738: throw new FHIRException("Cannot make property origin as it is not a complex type"); // IntegerType
        case -995427962: throw new FHIRException("Cannot make property params as it is not a complex type"); // StringType
        case 1074158076:  return addRequestHeader(); // SetupActionOperationRequestHeaderComponent
        case -633138884: throw new FHIRException("Cannot make property responseId as it is not a complex type"); // IdType
        case 1746327190: throw new FHIRException("Cannot make property sourceId as it is not a complex type"); // IdType
        case -441951604: throw new FHIRException("Cannot make property targetId as it is not a complex type"); // IdType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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
           && compareDeep(responseId, o.responseId, true) && compareDeep(sourceId, o.sourceId, true) && compareDeep(targetId, o.targetId, true)
           && compareDeep(url, o.url, true);
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
           && compareValues(params, o.params, true) && compareValues(responseId, o.responseId, true) && compareValues(sourceId, o.sourceId, true)
           && compareValues(targetId, o.targetId, true) && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (resource == null || resource.isEmpty())
           && (label == null || label.isEmpty()) && (description == null || description.isEmpty()) && (accept == null || accept.isEmpty())
           && (contentType == null || contentType.isEmpty()) && (destination == null || destination.isEmpty())
           && (encodeRequestUrl == null || encodeRequestUrl.isEmpty()) && (origin == null || origin.isEmpty())
           && (params == null || params.isEmpty()) && (requestHeader == null || requestHeader.isEmpty())
           && (responseId == null || responseId.isEmpty()) && (sourceId == null || sourceId.isEmpty())
           && (targetId == null || targetId.isEmpty()) && (url == null || url.isEmpty());
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
         * The value of the header e.g. "application/xml".
         */
        @Child(name = "value", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP headerfield value", formalDefinition="The value of the header e.g. \"application/xml\"." )
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
         * @return {@link #value} (The value of the header e.g. "application/xml".). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
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
         * @param value {@link #value} (The value of the header e.g. "application/xml".). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public SetupActionOperationRequestHeaderComponent setValueElement(StringType value) { 
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
        public SetupActionOperationRequestHeaderComponent setValue(String value) { 
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 97427706: /*field*/ return this.field == null ? new Base[0] : new Base[] {this.field}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 97427706: // field
          this.field = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("field"))
          this.field = castToString(value); // StringType
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 97427706: throw new FHIRException("Cannot make property field as it is not a complex type"); // StringType
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (field == null || field.isEmpty()) && (value == null || value.isEmpty())
          ;
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
         * The HTTP header field name e.g. 'Location'.
         */
        @Child(name = "headerField", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP header field name", formalDefinition="The HTTP header field name e.g. 'Location'." )
        protected StringType headerField;

        /**
         * The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.
         */
        @Child(name = "minimumId", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of minimum content resource", formalDefinition="The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId." )
        protected StringType minimumId;

        /**
         * Whether or not the test execution performs validation on the bundle navigation links.
         */
        @Child(name = "navigationLinks", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Perform validation on navigation links?", formalDefinition="Whether or not the test execution performs validation on the bundle navigation links." )
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
         * The value of the HTTP response code to be tested.
         */
        @Child(name = "responseCode", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="HTTP response code to test", formalDefinition="The value of the HTTP response code to be tested." )
        protected StringType responseCode;

        /**
         * The TestScript.rule this assert will evaluate.
         */
        @Child(name = "rule", type = {}, order=15, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The reference to a TestScript.rule", formalDefinition="The TestScript.rule this assert will evaluate." )
        protected SetupActionAssertRuleComponent rule;

        /**
         * The TestScript.ruleset this assert will evaluate.
         */
        @Child(name = "ruleset", type = {}, order=16, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The reference to a TestScript.ruleset", formalDefinition="The TestScript.ruleset this assert will evaluate." )
        protected SetupActionAssertRulesetComponent ruleset;

        /**
         * Fixture to evaluate the XPath/JSONPath expression or the headerField  against.
         */
        @Child(name = "sourceId", type = {IdType.class}, order=17, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fixture Id of source expression or headerField", formalDefinition="Fixture to evaluate the XPath/JSONPath expression or the headerField  against." )
        protected IdType sourceId;

        /**
         * The ID of the Profile to validate against.
         */
        @Child(name = "validateProfileId", type = {IdType.class}, order=18, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Profile Id of validation profile reference", formalDefinition="The ID of the Profile to validate against." )
        protected IdType validateProfileId;

        /**
         * The value to compare to.
         */
        @Child(name = "value", type = {StringType.class}, order=19, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The value to compare to", formalDefinition="The value to compare to." )
        protected StringType value;

        /**
         * Whether or not the test execution will produce a warning only on error for this assert.
         */
        @Child(name = "warningOnly", type = {BooleanType.class}, order=20, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Will this assert produce a warning only on error?", formalDefinition="Whether or not the test execution will produce a warning only on error for this assert." )
        protected BooleanType warningOnly;

        private static final long serialVersionUID = -561290310L;

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
         * @return {@link #compareToSourceId} (Id of fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceId" gives direct access to the value
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
         * @param value {@link #compareToSourceId} (Id of fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourceId" gives direct access to the value
         */
        public SetupActionAssertComponent setCompareToSourceIdElement(StringType value) { 
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
         * @return {@link #compareToSourcePath} (XPath or JSONPath expression against fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourcePath" gives direct access to the value
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
         * @param value {@link #compareToSourcePath} (XPath or JSONPath expression against fixture used to compare the "sourceId/path" evaluations to.). This is the underlying object with id, value and extensions. The accessor "getCompareToSourcePath" gives direct access to the value
         */
        public SetupActionAssertComponent setCompareToSourcePathElement(StringType value) { 
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
         * @return {@link #operator} (The operator type.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
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
         * @param value {@link #operator} (The operator type.). This is the underlying object with id, value and extensions. The accessor "getOperator" gives direct access to the value
         */
        public SetupActionAssertComponent setOperatorElement(Enumeration<AssertionOperatorType> value) { 
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
         * @return {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
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
         * @param value {@link #resource} (The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.). This is the underlying object with id, value and extensions. The accessor "getResource" gives direct access to the value
         */
        public SetupActionAssertComponent setResourceElement(CodeType value) { 
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
        public SetupActionAssertRuleComponent getRule() { 
          if (this.rule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.rule");
            else if (Configuration.doAutoCreate())
              this.rule = new SetupActionAssertRuleComponent(); // cc
          return this.rule;
        }

        public boolean hasRule() { 
          return this.rule != null && !this.rule.isEmpty();
        }

        /**
         * @param value {@link #rule} (The TestScript.rule this assert will evaluate.)
         */
        public SetupActionAssertComponent setRule(SetupActionAssertRuleComponent value) { 
          this.rule = value;
          return this;
        }

        /**
         * @return {@link #ruleset} (The TestScript.ruleset this assert will evaluate.)
         */
        public SetupActionAssertRulesetComponent getRuleset() { 
          if (this.ruleset == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertComponent.ruleset");
            else if (Configuration.doAutoCreate())
              this.ruleset = new SetupActionAssertRulesetComponent(); // cc
          return this.ruleset;
        }

        public boolean hasRuleset() { 
          return this.ruleset != null && !this.ruleset.isEmpty();
        }

        /**
         * @param value {@link #ruleset} (The TestScript.ruleset this assert will evaluate.)
         */
        public SetupActionAssertComponent setRuleset(SetupActionAssertRulesetComponent value) { 
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
          childrenList.add(new Property("compareToSourceId", "string", "Id of fixture used to compare the \"sourceId/path\" evaluations to.", 0, java.lang.Integer.MAX_VALUE, compareToSourceId));
          childrenList.add(new Property("compareToSourcePath", "string", "XPath or JSONPath expression against fixture used to compare the \"sourceId/path\" evaluations to.", 0, java.lang.Integer.MAX_VALUE, compareToSourcePath));
          childrenList.add(new Property("contentType", "code", "The content-type or mime-type to use for RESTful operation in the 'Content-Type' header.", 0, java.lang.Integer.MAX_VALUE, contentType));
          childrenList.add(new Property("headerField", "string", "The HTTP header field name e.g. 'Location'.", 0, java.lang.Integer.MAX_VALUE, headerField));
          childrenList.add(new Property("minimumId", "string", "The ID of a fixture.  Asserts that the response contains at a minimum the fixture specified by minimumId.", 0, java.lang.Integer.MAX_VALUE, minimumId));
          childrenList.add(new Property("navigationLinks", "boolean", "Whether or not the test execution performs validation on the bundle navigation links.", 0, java.lang.Integer.MAX_VALUE, navigationLinks));
          childrenList.add(new Property("operator", "code", "The operator type.", 0, java.lang.Integer.MAX_VALUE, operator));
          childrenList.add(new Property("path", "string", "The XPath or JSONPath expression to be evaluated against the fixture representing the response received from server.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("resource", "code", "The type of the resource.  See http://hl7-fhir.github.io/resourcelist.html.", 0, java.lang.Integer.MAX_VALUE, resource));
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
        case -790206144: /*compareToSourcePath*/ return this.compareToSourcePath == null ? new Base[0] : new Base[] {this.compareToSourcePath}; // StringType
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // Enumeration<ContentType>
        case 1160732269: /*headerField*/ return this.headerField == null ? new Base[0] : new Base[] {this.headerField}; // StringType
        case 818925001: /*minimumId*/ return this.minimumId == null ? new Base[0] : new Base[] {this.minimumId}; // StringType
        case 1001488901: /*navigationLinks*/ return this.navigationLinks == null ? new Base[0] : new Base[] {this.navigationLinks}; // BooleanType
        case -500553564: /*operator*/ return this.operator == null ? new Base[0] : new Base[] {this.operator}; // Enumeration<AssertionOperatorType>
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // CodeType
        case -340323263: /*response*/ return this.response == null ? new Base[0] : new Base[] {this.response}; // Enumeration<AssertionResponseTypes>
        case 1438723534: /*responseCode*/ return this.responseCode == null ? new Base[0] : new Base[] {this.responseCode}; // StringType
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : new Base[] {this.rule}; // SetupActionAssertRuleComponent
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // SetupActionAssertRulesetComponent
        case 1746327190: /*sourceId*/ return this.sourceId == null ? new Base[0] : new Base[] {this.sourceId}; // IdType
        case 1555541038: /*validateProfileId*/ return this.validateProfileId == null ? new Base[0] : new Base[] {this.validateProfileId}; // IdType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // StringType
        case -481159832: /*warningOnly*/ return this.warningOnly == null ? new Base[0] : new Base[] {this.warningOnly}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 102727412: // label
          this.label = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -962590849: // direction
          this.direction = new AssertionDirectionTypeEnumFactory().fromType(value); // Enumeration<AssertionDirectionType>
          break;
        case 2081856758: // compareToSourceId
          this.compareToSourceId = castToString(value); // StringType
          break;
        case -790206144: // compareToSourcePath
          this.compareToSourcePath = castToString(value); // StringType
          break;
        case -389131437: // contentType
          this.contentType = new ContentTypeEnumFactory().fromType(value); // Enumeration<ContentType>
          break;
        case 1160732269: // headerField
          this.headerField = castToString(value); // StringType
          break;
        case 818925001: // minimumId
          this.minimumId = castToString(value); // StringType
          break;
        case 1001488901: // navigationLinks
          this.navigationLinks = castToBoolean(value); // BooleanType
          break;
        case -500553564: // operator
          this.operator = new AssertionOperatorTypeEnumFactory().fromType(value); // Enumeration<AssertionOperatorType>
          break;
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        case -341064690: // resource
          this.resource = castToCode(value); // CodeType
          break;
        case -340323263: // response
          this.response = new AssertionResponseTypesEnumFactory().fromType(value); // Enumeration<AssertionResponseTypes>
          break;
        case 1438723534: // responseCode
          this.responseCode = castToString(value); // StringType
          break;
        case 3512060: // rule
          this.rule = (SetupActionAssertRuleComponent) value; // SetupActionAssertRuleComponent
          break;
        case 1548678118: // ruleset
          this.ruleset = (SetupActionAssertRulesetComponent) value; // SetupActionAssertRulesetComponent
          break;
        case 1746327190: // sourceId
          this.sourceId = castToId(value); // IdType
          break;
        case 1555541038: // validateProfileId
          this.validateProfileId = castToId(value); // IdType
          break;
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        case -481159832: // warningOnly
          this.warningOnly = castToBoolean(value); // BooleanType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("label"))
          this.label = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("direction"))
          this.direction = new AssertionDirectionTypeEnumFactory().fromType(value); // Enumeration<AssertionDirectionType>
        else if (name.equals("compareToSourceId"))
          this.compareToSourceId = castToString(value); // StringType
        else if (name.equals("compareToSourcePath"))
          this.compareToSourcePath = castToString(value); // StringType
        else if (name.equals("contentType"))
          this.contentType = new ContentTypeEnumFactory().fromType(value); // Enumeration<ContentType>
        else if (name.equals("headerField"))
          this.headerField = castToString(value); // StringType
        else if (name.equals("minimumId"))
          this.minimumId = castToString(value); // StringType
        else if (name.equals("navigationLinks"))
          this.navigationLinks = castToBoolean(value); // BooleanType
        else if (name.equals("operator"))
          this.operator = new AssertionOperatorTypeEnumFactory().fromType(value); // Enumeration<AssertionOperatorType>
        else if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("resource"))
          this.resource = castToCode(value); // CodeType
        else if (name.equals("response"))
          this.response = new AssertionResponseTypesEnumFactory().fromType(value); // Enumeration<AssertionResponseTypes>
        else if (name.equals("responseCode"))
          this.responseCode = castToString(value); // StringType
        else if (name.equals("rule"))
          this.rule = (SetupActionAssertRuleComponent) value; // SetupActionAssertRuleComponent
        else if (name.equals("ruleset"))
          this.ruleset = (SetupActionAssertRulesetComponent) value; // SetupActionAssertRulesetComponent
        else if (name.equals("sourceId"))
          this.sourceId = castToId(value); // IdType
        else if (name.equals("validateProfileId"))
          this.validateProfileId = castToId(value); // IdType
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else if (name.equals("warningOnly"))
          this.warningOnly = castToBoolean(value); // BooleanType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 102727412: throw new FHIRException("Cannot make property label as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -962590849: throw new FHIRException("Cannot make property direction as it is not a complex type"); // Enumeration<AssertionDirectionType>
        case 2081856758: throw new FHIRException("Cannot make property compareToSourceId as it is not a complex type"); // StringType
        case -790206144: throw new FHIRException("Cannot make property compareToSourcePath as it is not a complex type"); // StringType
        case -389131437: throw new FHIRException("Cannot make property contentType as it is not a complex type"); // Enumeration<ContentType>
        case 1160732269: throw new FHIRException("Cannot make property headerField as it is not a complex type"); // StringType
        case 818925001: throw new FHIRException("Cannot make property minimumId as it is not a complex type"); // StringType
        case 1001488901: throw new FHIRException("Cannot make property navigationLinks as it is not a complex type"); // BooleanType
        case -500553564: throw new FHIRException("Cannot make property operator as it is not a complex type"); // Enumeration<AssertionOperatorType>
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        case -341064690: throw new FHIRException("Cannot make property resource as it is not a complex type"); // CodeType
        case -340323263: throw new FHIRException("Cannot make property response as it is not a complex type"); // Enumeration<AssertionResponseTypes>
        case 1438723534: throw new FHIRException("Cannot make property responseCode as it is not a complex type"); // StringType
        case 3512060:  return getRule(); // SetupActionAssertRuleComponent
        case 1548678118:  return getRuleset(); // SetupActionAssertRulesetComponent
        case 1746327190: throw new FHIRException("Cannot make property sourceId as it is not a complex type"); // IdType
        case 1555541038: throw new FHIRException("Cannot make property validateProfileId as it is not a complex type"); // IdType
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        case -481159832: throw new FHIRException("Cannot make property warningOnly as it is not a complex type"); // BooleanType
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("compareToSourcePath")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.compareToSourcePath");
        }
        else if (name.equals("contentType")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.contentType");
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
          this.rule = new SetupActionAssertRuleComponent();
          return this.rule;
        }
        else if (name.equals("ruleset")) {
          this.ruleset = new SetupActionAssertRulesetComponent();
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
           && compareDeep(compareToSourceId, o.compareToSourceId, true) && compareDeep(compareToSourcePath, o.compareToSourcePath, true)
           && compareDeep(contentType, o.contentType, true) && compareDeep(headerField, o.headerField, true)
           && compareDeep(minimumId, o.minimumId, true) && compareDeep(navigationLinks, o.navigationLinks, true)
           && compareDeep(operator, o.operator, true) && compareDeep(path, o.path, true) && compareDeep(resource, o.resource, true)
           && compareDeep(response, o.response, true) && compareDeep(responseCode, o.responseCode, true) && compareDeep(rule, o.rule, true)
           && compareDeep(ruleset, o.ruleset, true) && compareDeep(sourceId, o.sourceId, true) && compareDeep(validateProfileId, o.validateProfileId, true)
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
           && (responseCode == null || responseCode.isEmpty()) && (rule == null || rule.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (sourceId == null || sourceId.isEmpty()) && (validateProfileId == null || validateProfileId.isEmpty())
           && (value == null || value.isEmpty()) && (warningOnly == null || warningOnly.isEmpty());
      }

  public String fhirType() {
    return "TestScript.setup.action.assert";

  }

  }

    @Block()
    public static class SetupActionAssertRuleComponent extends BackboneElement implements IBaseBackboneElement {
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
        protected List<SetupActionAssertRuleParamComponent> param;

        private static final long serialVersionUID = -319928210L;

    /**
     * Constructor
     */
      public SetupActionAssertRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionAssertRuleComponent(IdType ruleId) {
        super();
        this.ruleId = ruleId;
      }

        /**
         * @return {@link #ruleId} (The TestScript.rule id value this assert will evaluate.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public IdType getRuleIdElement() { 
          if (this.ruleId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertRuleComponent.ruleId");
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
        public SetupActionAssertRuleComponent setRuleIdElement(IdType value) { 
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
        public SetupActionAssertRuleComponent setRuleId(String value) { 
            if (this.ruleId == null)
              this.ruleId = new IdType();
            this.ruleId.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
        public List<SetupActionAssertRuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<SetupActionAssertRuleParamComponent>();
          return this.param;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (SetupActionAssertRuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
    // syntactic sugar
        public SetupActionAssertRuleParamComponent addParam() { //3
          SetupActionAssertRuleParamComponent t = new SetupActionAssertRuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<SetupActionAssertRuleParamComponent>();
          this.param.add(t);
          return t;
        }

    // syntactic sugar
        public SetupActionAssertRuleComponent addParam(SetupActionAssertRuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<SetupActionAssertRuleParamComponent>();
          this.param.add(t);
          return this;
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
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // SetupActionAssertRuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -919875273: // ruleId
          this.ruleId = castToId(value); // IdType
          break;
        case 106436749: // param
          this.getParam().add((SetupActionAssertRuleParamComponent) value); // SetupActionAssertRuleParamComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("ruleId"))
          this.ruleId = castToId(value); // IdType
        else if (name.equals("param"))
          this.getParam().add((SetupActionAssertRuleParamComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273: throw new FHIRException("Cannot make property ruleId as it is not a complex type"); // IdType
        case 106436749:  return addParam(); // SetupActionAssertRuleParamComponent
        default: return super.makeProperty(hash, name);
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

      public SetupActionAssertRuleComponent copy() {
        SetupActionAssertRuleComponent dst = new SetupActionAssertRuleComponent();
        copyValues(dst);
        dst.ruleId = ruleId == null ? null : ruleId.copy();
        if (param != null) {
          dst.param = new ArrayList<SetupActionAssertRuleParamComponent>();
          for (SetupActionAssertRuleParamComponent i : param)
            dst.param.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionAssertRuleComponent))
          return false;
        SetupActionAssertRuleComponent o = (SetupActionAssertRuleComponent) other;
        return compareDeep(ruleId, o.ruleId, true) && compareDeep(param, o.param, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionAssertRuleComponent))
          return false;
        SetupActionAssertRuleComponent o = (SetupActionAssertRuleComponent) other;
        return compareValues(ruleId, o.ruleId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (ruleId == null || ruleId.isEmpty()) && (param == null || param.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.rule";

  }

  }

    @Block()
    public static class SetupActionAssertRuleParamComponent extends BackboneElement implements IBaseBackboneElement {
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
      public SetupActionAssertRuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionAssertRuleParamComponent(StringType name, StringType value) {
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
              throw new Error("Attempt to auto-create SetupActionAssertRuleParamComponent.name");
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
        public SetupActionAssertRuleParamComponent setNameElement(StringType value) { 
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
        public SetupActionAssertRuleParamComponent setName(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertRuleParamComponent.value");
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
        public SetupActionAssertRuleParamComponent setValueElement(StringType value) { 
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
        public SetupActionAssertRuleParamComponent setValue(String value) { 
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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

      public SetupActionAssertRuleParamComponent copy() {
        SetupActionAssertRuleParamComponent dst = new SetupActionAssertRuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionAssertRuleParamComponent))
          return false;
        SetupActionAssertRuleParamComponent o = (SetupActionAssertRuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionAssertRuleParamComponent))
          return false;
        SetupActionAssertRuleParamComponent o = (SetupActionAssertRuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.rule.param";

  }

  }

    @Block()
    public static class SetupActionAssertRulesetComponent extends BackboneElement implements IBaseBackboneElement {
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
        protected List<SetupActionAssertRulesetRuleComponent> rule;

        private static final long serialVersionUID = 2070600738L;

    /**
     * Constructor
     */
      public SetupActionAssertRulesetComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionAssertRulesetComponent(IdType rulesetId) {
        super();
        this.rulesetId = rulesetId;
      }

        /**
         * @return {@link #rulesetId} (The TestScript.ruleset id value this assert will evaluate.). This is the underlying object with id, value and extensions. The accessor "getRulesetId" gives direct access to the value
         */
        public IdType getRulesetIdElement() { 
          if (this.rulesetId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertRulesetComponent.rulesetId");
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
        public SetupActionAssertRulesetComponent setRulesetIdElement(IdType value) { 
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
        public SetupActionAssertRulesetComponent setRulesetId(String value) { 
            if (this.rulesetId == null)
              this.rulesetId = new IdType();
            this.rulesetId.setValue(value);
          return this;
        }

        /**
         * @return {@link #rule} (The referenced rule within the external ruleset template.)
         */
        public List<SetupActionAssertRulesetRuleComponent> getRule() { 
          if (this.rule == null)
            this.rule = new ArrayList<SetupActionAssertRulesetRuleComponent>();
          return this.rule;
        }

        public boolean hasRule() { 
          if (this.rule == null)
            return false;
          for (SetupActionAssertRulesetRuleComponent item : this.rule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #rule} (The referenced rule within the external ruleset template.)
         */
    // syntactic sugar
        public SetupActionAssertRulesetRuleComponent addRule() { //3
          SetupActionAssertRulesetRuleComponent t = new SetupActionAssertRulesetRuleComponent();
          if (this.rule == null)
            this.rule = new ArrayList<SetupActionAssertRulesetRuleComponent>();
          this.rule.add(t);
          return t;
        }

    // syntactic sugar
        public SetupActionAssertRulesetComponent addRule(SetupActionAssertRulesetRuleComponent t) { //3
          if (t == null)
            return this;
          if (this.rule == null)
            this.rule = new ArrayList<SetupActionAssertRulesetRuleComponent>();
          this.rule.add(t);
          return this;
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
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // SetupActionAssertRulesetRuleComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -2073977951: // rulesetId
          this.rulesetId = castToId(value); // IdType
          break;
        case 3512060: // rule
          this.getRule().add((SetupActionAssertRulesetRuleComponent) value); // SetupActionAssertRulesetRuleComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("rulesetId"))
          this.rulesetId = castToId(value); // IdType
        else if (name.equals("rule"))
          this.getRule().add((SetupActionAssertRulesetRuleComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -2073977951: throw new FHIRException("Cannot make property rulesetId as it is not a complex type"); // IdType
        case 3512060:  return addRule(); // SetupActionAssertRulesetRuleComponent
        default: return super.makeProperty(hash, name);
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

      public SetupActionAssertRulesetComponent copy() {
        SetupActionAssertRulesetComponent dst = new SetupActionAssertRulesetComponent();
        copyValues(dst);
        dst.rulesetId = rulesetId == null ? null : rulesetId.copy();
        if (rule != null) {
          dst.rule = new ArrayList<SetupActionAssertRulesetRuleComponent>();
          for (SetupActionAssertRulesetRuleComponent i : rule)
            dst.rule.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionAssertRulesetComponent))
          return false;
        SetupActionAssertRulesetComponent o = (SetupActionAssertRulesetComponent) other;
        return compareDeep(rulesetId, o.rulesetId, true) && compareDeep(rule, o.rule, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionAssertRulesetComponent))
          return false;
        SetupActionAssertRulesetComponent o = (SetupActionAssertRulesetComponent) other;
        return compareValues(rulesetId, o.rulesetId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (rulesetId == null || rulesetId.isEmpty()) && (rule == null || rule.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.ruleset";

  }

  }

    @Block()
    public static class SetupActionAssertRulesetRuleComponent extends BackboneElement implements IBaseBackboneElement {
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
        protected List<SetupActionAssertRulesetRuleParamComponent> param;

        private static final long serialVersionUID = 52246314L;

    /**
     * Constructor
     */
      public SetupActionAssertRulesetRuleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionAssertRulesetRuleComponent(IdType ruleId) {
        super();
        this.ruleId = ruleId;
      }

        /**
         * @return {@link #ruleId} (Id of the referenced rule within the external ruleset template.). This is the underlying object with id, value and extensions. The accessor "getRuleId" gives direct access to the value
         */
        public IdType getRuleIdElement() { 
          if (this.ruleId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SetupActionAssertRulesetRuleComponent.ruleId");
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
        public SetupActionAssertRulesetRuleComponent setRuleIdElement(IdType value) { 
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
        public SetupActionAssertRulesetRuleComponent setRuleId(String value) { 
            if (this.ruleId == null)
              this.ruleId = new IdType();
            this.ruleId.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
        public List<SetupActionAssertRulesetRuleParamComponent> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<SetupActionAssertRulesetRuleParamComponent>();
          return this.param;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (SetupActionAssertRulesetRuleParamComponent item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #param} (Each rule template can take one or more parameters for rule evaluation.)
         */
    // syntactic sugar
        public SetupActionAssertRulesetRuleParamComponent addParam() { //3
          SetupActionAssertRulesetRuleParamComponent t = new SetupActionAssertRulesetRuleParamComponent();
          if (this.param == null)
            this.param = new ArrayList<SetupActionAssertRulesetRuleParamComponent>();
          this.param.add(t);
          return t;
        }

    // syntactic sugar
        public SetupActionAssertRulesetRuleComponent addParam(SetupActionAssertRulesetRuleParamComponent t) { //3
          if (t == null)
            return this;
          if (this.param == null)
            this.param = new ArrayList<SetupActionAssertRulesetRuleParamComponent>();
          this.param.add(t);
          return this;
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
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // SetupActionAssertRulesetRuleParamComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -919875273: // ruleId
          this.ruleId = castToId(value); // IdType
          break;
        case 106436749: // param
          this.getParam().add((SetupActionAssertRulesetRuleParamComponent) value); // SetupActionAssertRulesetRuleParamComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("ruleId"))
          this.ruleId = castToId(value); // IdType
        else if (name.equals("param"))
          this.getParam().add((SetupActionAssertRulesetRuleParamComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -919875273: throw new FHIRException("Cannot make property ruleId as it is not a complex type"); // IdType
        case 106436749:  return addParam(); // SetupActionAssertRulesetRuleParamComponent
        default: return super.makeProperty(hash, name);
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

      public SetupActionAssertRulesetRuleComponent copy() {
        SetupActionAssertRulesetRuleComponent dst = new SetupActionAssertRulesetRuleComponent();
        copyValues(dst);
        dst.ruleId = ruleId == null ? null : ruleId.copy();
        if (param != null) {
          dst.param = new ArrayList<SetupActionAssertRulesetRuleParamComponent>();
          for (SetupActionAssertRulesetRuleParamComponent i : param)
            dst.param.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionAssertRulesetRuleComponent))
          return false;
        SetupActionAssertRulesetRuleComponent o = (SetupActionAssertRulesetRuleComponent) other;
        return compareDeep(ruleId, o.ruleId, true) && compareDeep(param, o.param, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionAssertRulesetRuleComponent))
          return false;
        SetupActionAssertRulesetRuleComponent o = (SetupActionAssertRulesetRuleComponent) other;
        return compareValues(ruleId, o.ruleId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (ruleId == null || ruleId.isEmpty()) && (param == null || param.isEmpty())
          ;
      }

  public String fhirType() {
    return "TestScript.setup.action.assert.ruleset.rule";

  }

  }

    @Block()
    public static class SetupActionAssertRulesetRuleParamComponent extends BackboneElement implements IBaseBackboneElement {
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
      public SetupActionAssertRulesetRuleParamComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SetupActionAssertRulesetRuleParamComponent(StringType name, StringType value) {
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
              throw new Error("Attempt to auto-create SetupActionAssertRulesetRuleParamComponent.name");
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
        public SetupActionAssertRulesetRuleParamComponent setNameElement(StringType value) { 
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
        public SetupActionAssertRulesetRuleParamComponent setName(String value) { 
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
              throw new Error("Attempt to auto-create SetupActionAssertRulesetRuleParamComponent.value");
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
        public SetupActionAssertRulesetRuleParamComponent setValueElement(StringType value) { 
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
        public SetupActionAssertRulesetRuleParamComponent setValue(String value) { 
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 111972721: throw new FHIRException("Cannot make property value as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
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

      public SetupActionAssertRulesetRuleParamComponent copy() {
        SetupActionAssertRulesetRuleParamComponent dst = new SetupActionAssertRulesetRuleParamComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SetupActionAssertRulesetRuleParamComponent))
          return false;
        SetupActionAssertRulesetRuleParamComponent o = (SetupActionAssertRulesetRuleParamComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SetupActionAssertRulesetRuleParamComponent))
          return false;
        SetupActionAssertRulesetRuleParamComponent o = (SetupActionAssertRulesetRuleParamComponent) other;
        return compareValues(name, o.name, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
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
         * Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.
         */
        @Child(name = "metadata", type = {TestScriptMetadataComponent.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Capabilities  that are expected to function correctly on the FHIR server being tested", formalDefinition="Capabilities that must exist and are assumed to function correctly on the FHIR server being tested." )
        protected TestScriptMetadataComponent metadata;

        /**
         * Action would contain either an operation or an assertion.
         */
        @Child(name = "action", type = {}, order=4, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A test operation or assert to perform", formalDefinition="Action would contain either an operation or an assertion." )
        protected List<TestActionComponent> action;

        private static final long serialVersionUID = -1607790780L;

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
         * @return {@link #metadata} (Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.)
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
         * @param value {@link #metadata} (Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.)
         */
        public TestScriptTestComponent setMetadata(TestScriptMetadataComponent value) { 
          this.metadata = value;
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

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (TestActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (Action would contain either an operation or an assertion.)
         */
    // syntactic sugar
        public TestActionComponent addAction() { //3
          TestActionComponent t = new TestActionComponent();
          if (this.action == null)
            this.action = new ArrayList<TestActionComponent>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTestComponent addAction(TestActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<TestActionComponent>();
          this.action.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of this test used for tracking/logging purposes by test engines.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "A short description of the test used by test engines for tracking and reporting purposes.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("metadata", "@TestScript.metadata", "Capabilities that must exist and are assumed to function correctly on the FHIR server being tested.", 0, java.lang.Integer.MAX_VALUE, metadata));
          childrenList.add(new Property("action", "", "Action would contain either an operation or an assertion.", 0, java.lang.Integer.MAX_VALUE, action));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -450004177: /*metadata*/ return this.metadata == null ? new Base[0] : new Base[] {this.metadata}; // TestScriptMetadataComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // TestActionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -450004177: // metadata
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
          break;
        case -1422950858: // action
          this.getAction().add((TestActionComponent) value); // TestActionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("metadata"))
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
        else if (name.equals("action"))
          this.getAction().add((TestActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -450004177:  return getMetadata(); // TestScriptMetadataComponent
        case -1422950858:  return addAction(); // TestActionComponent
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("metadata")) {
          this.metadata = new TestScriptMetadataComponent();
          return this.metadata;
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
        dst.metadata = metadata == null ? null : metadata.copy();
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1662702951: // operation
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
          break;
        case -1408208058: // assert
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("operation"))
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
        else if (name.equals("assert"))
          this.assert_ = (SetupActionAssertComponent) value; // SetupActionAssertComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951:  return getOperation(); // SetupActionOperationComponent
        case -1408208058:  return getAssert(); // SetupActionAssertComponent
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (operation == null || operation.isEmpty()) && (assert_ == null || assert_.isEmpty())
          ;
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

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (TeardownActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #action} (The teardown action will only contain an operation.)
         */
    // syntactic sugar
        public TeardownActionComponent addAction() { //3
          TeardownActionComponent t = new TeardownActionComponent();
          if (this.action == null)
            this.action = new ArrayList<TeardownActionComponent>();
          this.action.add(t);
          return t;
        }

    // syntactic sugar
        public TestScriptTeardownComponent addAction(TeardownActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<TeardownActionComponent>();
          this.action.add(t);
          return this;
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950858: // action
          this.getAction().add((TeardownActionComponent) value); // TeardownActionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action"))
          this.getAction().add((TeardownActionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858:  return addAction(); // TeardownActionComponent
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (action == null || action.isEmpty());
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
        @Child(name = "operation", type = {SetupActionOperationComponent.class}, order=1, min=0, max=1, modifier=false, summary=false)
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1662702951: // operation
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("operation"))
          this.operation = (SetupActionOperationComponent) value; // SetupActionOperationComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1662702951:  return getOperation(); // SetupActionOperationComponent
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (operation == null || operation.isEmpty());
      }

  public String fhirType() {
    return "TestScript.teardown.action";

  }

  }

    /**
     * An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Absolute URL used to reference this TestScript", formalDefinition="An absolute URL that is used to identify this Test Script. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this Test Script is (or will be) published." )
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
     * Identifier for the TestScript assigned for external purposes outside the context of FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="Identifier for the TestScript assigned for external purposes outside the context of FHIR." )
    protected Identifier identifier;

    /**
     * This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the Test Script.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the Test Script." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<TestScriptContactComponent> contact;

    /**
     * The date this version of the test tcript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the test cases change.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for this version of the TestScript", formalDefinition="The date this version of the test tcript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the test cases change." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the TestScript and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Natural language description of the TestScript", formalDefinition="A free text natural language description of the TestScript and its use." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts." )
    protected List<CodeableConcept> useContext;

    /**
     * Explains why this Test Script is needed and why it's been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Scope and Usage this Test Script is for", formalDefinition="Explains why this Test Script is needed and why it's been constrained as it has." )
    protected StringType requirements;

    /**
     * A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    @Child(name = "copyright", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings." )
    protected StringType copyright;

    /**
     * An abstract server used in operations within this test script in the origin element.
     */
    @Child(name = "origin", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="An abstract server representing a client or sender in a message exchange", formalDefinition="An abstract server used in operations within this test script in the origin element." )
    protected List<TestScriptOriginComponent> origin;

    /**
     * An abstract server used in operations within this test script in the destination element.
     */
    @Child(name = "destination", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="An abstract server representing a destination or receiver in a message exchange", formalDefinition="An abstract server used in operations within this test script in the destination element." )
    protected List<TestScriptDestinationComponent> destination;

    /**
     * The required capability must exist and are assumed to function correctly on the FHIR server being tested.
     */
    @Child(name = "metadata", type = {}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Required capability that is assumed to function correctly on the FHIR server being tested", formalDefinition="The required capability must exist and are assumed to function correctly on the FHIR server being tested." )
    protected TestScriptMetadataComponent metadata;

    /**
     * Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute.
     */
    @Child(name = "fixture", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Fixture in the test script - by reference (uri)", formalDefinition="Fixture in the test script - by reference (uri). All fixtures are required for the test script to execute." )
    protected List<TestScriptFixtureComponent> fixture;

    /**
     * Reference to the profile to be used for validation.
     */
    @Child(name = "profile", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reference of the validation profile", formalDefinition="Reference to the profile to be used for validation." )
    protected List<Reference> profile;
    /**
     * The actual objects that are the target of the reference (Reference to the profile to be used for validation.)
     */
    protected List<Resource> profileTarget;


    /**
     * Variable is set based either on element value in response body or on header field value in the response headers.
     */
    @Child(name = "variable", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Placeholder for evaluated elements", formalDefinition="Variable is set based either on element value in response body or on header field value in the response headers." )
    protected List<TestScriptVariableComponent> variable;

    /**
     * Assert rule to be used in one or more asserts within the test script.
     */
    @Child(name = "rule", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Assert rule used within the test script", formalDefinition="Assert rule to be used in one or more asserts within the test script." )
    protected List<TestScriptRuleComponent> rule;

    /**
     * Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied.
     */
    @Child(name = "ruleset", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Assert ruleset used within the test script", formalDefinition="Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied." )
    protected List<TestScriptRulesetComponent> ruleset;

    /**
     * A series of required setup operations before tests are executed.
     */
    @Child(name = "setup", type = {}, order=21, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A series of required setup operations before tests are executed", formalDefinition="A series of required setup operations before tests are executed." )
    protected TestScriptSetupComponent setup;

    /**
     * A test in this script.
     */
    @Child(name = "test", type = {}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A test in this script", formalDefinition="A test in this script." )
    protected List<TestScriptTestComponent> test;

    /**
     * A series of operations required to clean up after the all the tests are executed (successfully or otherwise).
     */
    @Child(name = "teardown", type = {}, order=23, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A series of required clean up steps", formalDefinition="A series of operations required to clean up after the all the tests are executed (successfully or otherwise)." )
    protected TestScriptTeardownComponent teardown;

    private static final long serialVersionUID = -468958725L;

  /**
   * Constructor
   */
    public TestScript() {
      super();
    }

  /**
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
     * @return {@link #identifier} (Identifier for the TestScript assigned for external purposes outside the context of FHIR.)
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
     * @param value {@link #identifier} (Identifier for the TestScript assigned for external purposes outside the context of FHIR.)
     */
    public TestScript setIdentifier(Identifier value) { 
      this.identifier = value;
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
     * @return {@link #date} (The date this version of the test tcript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the test cases change.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date this version of the test tcript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the test cases change.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public TestScript setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date this version of the test tcript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the test cases change.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date this version of the test tcript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the test cases change.
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
     * @return {@link #origin} (An abstract server used in operations within this test script in the origin element.)
     */
    public List<TestScriptOriginComponent> getOrigin() { 
      if (this.origin == null)
        this.origin = new ArrayList<TestScriptOriginComponent>();
      return this.origin;
    }

    public boolean hasOrigin() { 
      if (this.origin == null)
        return false;
      for (TestScriptOriginComponent item : this.origin)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #origin} (An abstract server used in operations within this test script in the origin element.)
     */
    // syntactic sugar
    public TestScriptOriginComponent addOrigin() { //3
      TestScriptOriginComponent t = new TestScriptOriginComponent();
      if (this.origin == null)
        this.origin = new ArrayList<TestScriptOriginComponent>();
      this.origin.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addOrigin(TestScriptOriginComponent t) { //3
      if (t == null)
        return this;
      if (this.origin == null)
        this.origin = new ArrayList<TestScriptOriginComponent>();
      this.origin.add(t);
      return this;
    }

    /**
     * @return {@link #destination} (An abstract server used in operations within this test script in the destination element.)
     */
    public List<TestScriptDestinationComponent> getDestination() { 
      if (this.destination == null)
        this.destination = new ArrayList<TestScriptDestinationComponent>();
      return this.destination;
    }

    public boolean hasDestination() { 
      if (this.destination == null)
        return false;
      for (TestScriptDestinationComponent item : this.destination)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #destination} (An abstract server used in operations within this test script in the destination element.)
     */
    // syntactic sugar
    public TestScriptDestinationComponent addDestination() { //3
      TestScriptDestinationComponent t = new TestScriptDestinationComponent();
      if (this.destination == null)
        this.destination = new ArrayList<TestScriptDestinationComponent>();
      this.destination.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addDestination(TestScriptDestinationComponent t) { //3
      if (t == null)
        return this;
      if (this.destination == null)
        this.destination = new ArrayList<TestScriptDestinationComponent>();
      this.destination.add(t);
      return this;
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
     * @return {@link #rule} (Assert rule to be used in one or more asserts within the test script.)
     */
    public List<TestScriptRuleComponent> getRule() { 
      if (this.rule == null)
        this.rule = new ArrayList<TestScriptRuleComponent>();
      return this.rule;
    }

    public boolean hasRule() { 
      if (this.rule == null)
        return false;
      for (TestScriptRuleComponent item : this.rule)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #rule} (Assert rule to be used in one or more asserts within the test script.)
     */
    // syntactic sugar
    public TestScriptRuleComponent addRule() { //3
      TestScriptRuleComponent t = new TestScriptRuleComponent();
      if (this.rule == null)
        this.rule = new ArrayList<TestScriptRuleComponent>();
      this.rule.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addRule(TestScriptRuleComponent t) { //3
      if (t == null)
        return this;
      if (this.rule == null)
        this.rule = new ArrayList<TestScriptRuleComponent>();
      this.rule.add(t);
      return this;
    }

    /**
     * @return {@link #ruleset} (Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied.)
     */
    public List<TestScriptRulesetComponent> getRuleset() { 
      if (this.ruleset == null)
        this.ruleset = new ArrayList<TestScriptRulesetComponent>();
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      if (this.ruleset == null)
        return false;
      for (TestScriptRulesetComponent item : this.ruleset)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #ruleset} (Contains one or more rules.  Offers a way to group rules so assertions could reference the group of rules and have them all applied.)
     */
    // syntactic sugar
    public TestScriptRulesetComponent addRuleset() { //3
      TestScriptRulesetComponent t = new TestScriptRulesetComponent();
      if (this.ruleset == null)
        this.ruleset = new ArrayList<TestScriptRulesetComponent>();
      this.ruleset.add(t);
      return t;
    }

    // syntactic sugar
    public TestScript addRuleset(TestScriptRulesetComponent t) { //3
      if (t == null)
        return this;
      if (this.ruleset == null)
        this.ruleset = new ArrayList<TestScriptRulesetComponent>();
      this.ruleset.add(t);
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
        childrenList.add(new Property("identifier", "Identifier", "Identifier for the TestScript assigned for external purposes outside the context of FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("experimental", "boolean", "This TestScript was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the Test Script.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date this version of the test tcript was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the test cases change.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the TestScript and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of Test Scripts.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("requirements", "string", "Explains why this Test Script is needed and why it's been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the Test Script and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.", 0, java.lang.Integer.MAX_VALUE, copyright));
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
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ConformanceResourceStatus>
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // TestScriptContactComponent
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // CodeableConcept
        case -1619874672: /*requirements*/ return this.requirements == null ? new Base[0] : new Base[] {this.requirements}; // StringType
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // StringType
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -892481550: // status
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
          break;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add((TestScriptContactComponent) value); // TestScriptContactComponent
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -669707736: // useContext
          this.getUseContext().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1619874672: // requirements
          this.requirements = castToString(value); // StringType
          break;
        case 1522889671: // copyright
          this.copyright = castToString(value); // StringType
          break;
        case -1008619738: // origin
          this.getOrigin().add((TestScriptOriginComponent) value); // TestScriptOriginComponent
          break;
        case -1429847026: // destination
          this.getDestination().add((TestScriptDestinationComponent) value); // TestScriptDestinationComponent
          break;
        case -450004177: // metadata
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
          break;
        case -843449847: // fixture
          this.getFixture().add((TestScriptFixtureComponent) value); // TestScriptFixtureComponent
          break;
        case -309425751: // profile
          this.getProfile().add(castToReference(value)); // Reference
          break;
        case -1249586564: // variable
          this.getVariable().add((TestScriptVariableComponent) value); // TestScriptVariableComponent
          break;
        case 3512060: // rule
          this.getRule().add((TestScriptRuleComponent) value); // TestScriptRuleComponent
          break;
        case 1548678118: // ruleset
          this.getRuleset().add((TestScriptRulesetComponent) value); // TestScriptRulesetComponent
          break;
        case 109329021: // setup
          this.setup = (TestScriptSetupComponent) value; // TestScriptSetupComponent
          break;
        case 3556498: // test
          this.getTest().add((TestScriptTestComponent) value); // TestScriptTestComponent
          break;
        case -1663474172: // teardown
          this.teardown = (TestScriptTeardownComponent) value; // TestScriptTeardownComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add((TestScriptContactComponent) value);
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("useContext"))
          this.getUseContext().add(castToCodeableConcept(value));
        else if (name.equals("requirements"))
          this.requirements = castToString(value); // StringType
        else if (name.equals("copyright"))
          this.copyright = castToString(value); // StringType
        else if (name.equals("origin"))
          this.getOrigin().add((TestScriptOriginComponent) value);
        else if (name.equals("destination"))
          this.getDestination().add((TestScriptDestinationComponent) value);
        else if (name.equals("metadata"))
          this.metadata = (TestScriptMetadataComponent) value; // TestScriptMetadataComponent
        else if (name.equals("fixture"))
          this.getFixture().add((TestScriptFixtureComponent) value);
        else if (name.equals("profile"))
          this.getProfile().add(castToReference(value));
        else if (name.equals("variable"))
          this.getVariable().add((TestScriptVariableComponent) value);
        else if (name.equals("rule"))
          this.getRule().add((TestScriptRuleComponent) value);
        else if (name.equals("ruleset"))
          this.getRuleset().add((TestScriptRulesetComponent) value);
        else if (name.equals("setup"))
          this.setup = (TestScriptSetupComponent) value; // TestScriptSetupComponent
        else if (name.equals("test"))
          this.getTest().add((TestScriptTestComponent) value);
        else if (name.equals("teardown"))
          this.teardown = (TestScriptTeardownComponent) value; // TestScriptTeardownComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ConformanceResourceStatus>
        case -1618432855:  return getIdentifier(); // Identifier
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // TestScriptContactComponent
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -669707736:  return addUseContext(); // CodeableConcept
        case -1619874672: throw new FHIRException("Cannot make property requirements as it is not a complex type"); // StringType
        case 1522889671: throw new FHIRException("Cannot make property copyright as it is not a complex type"); // StringType
        case -1008619738:  return addOrigin(); // TestScriptOriginComponent
        case -1429847026:  return addDestination(); // TestScriptDestinationComponent
        case -450004177:  return getMetadata(); // TestScriptMetadataComponent
        case -843449847:  return addFixture(); // TestScriptFixtureComponent
        case -309425751:  return addProfile(); // Reference
        case -1249586564:  return addVariable(); // TestScriptVariableComponent
        case 3512060:  return addRule(); // TestScriptRuleComponent
        case 1548678118:  return addRuleset(); // TestScriptRulesetComponent
        case 109329021:  return getSetup(); // TestScriptSetupComponent
        case 3556498:  return addTest(); // TestScriptTestComponent
        case -1663474172:  return getTeardown(); // TestScriptTeardownComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.status");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.experimental");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.date");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("requirements")) {
          throw new FHIRException("Cannot call addChild on a primitive type TestScript.requirements");
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
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
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
        return compareDeep(url, o.url, true) && compareDeep(version, o.version, true) && compareDeep(name, o.name, true)
           && compareDeep(status, o.status, true) && compareDeep(identifier, o.identifier, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(description, o.description, true) && compareDeep(useContext, o.useContext, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(copyright, o.copyright, true)
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
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(requirements, o.requirements, true)
           && compareValues(copyright, o.copyright, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (version == null || version.isEmpty())
           && (name == null || name.isEmpty()) && (status == null || status.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (description == null || description.isEmpty())
           && (useContext == null || useContext.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (origin == null || origin.isEmpty()) && (destination == null || destination.isEmpty())
           && (metadata == null || metadata.isEmpty()) && (fixture == null || fixture.isEmpty()) && (profile == null || profile.isEmpty())
           && (variable == null || variable.isEmpty()) && (rule == null || rule.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (setup == null || setup.isEmpty()) && (test == null || test.isEmpty()) && (teardown == null || teardown.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.TestScript;
   }

 /**
   * Search parameter: <b>testscript-test-capability</b>
   * <p>
   * Description: <b>TestScript test required and validated capability</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.test.metadata.capability.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="testscript-test-capability", path="TestScript.test.metadata.capability.description", description="TestScript test required and validated capability", type="string" )
  public static final String SP_TESTSCRIPT_TEST_CAPABILITY = "testscript-test-capability";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>testscript-test-capability</b>
   * <p>
   * Description: <b>TestScript test required and validated capability</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.test.metadata.capability.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TESTSCRIPT_TEST_CAPABILITY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TESTSCRIPT_TEST_CAPABILITY);

 /**
   * Search parameter: <b>testscript-setup-capability</b>
   * <p>
   * Description: <b>TestScript setup required and validated capability</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.setup.metadata.capability.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="testscript-setup-capability", path="TestScript.setup.metadata.capability.description", description="TestScript setup required and validated capability", type="string" )
  public static final String SP_TESTSCRIPT_SETUP_CAPABILITY = "testscript-setup-capability";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>testscript-setup-capability</b>
   * <p>
   * Description: <b>TestScript setup required and validated capability</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.setup.metadata.capability.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TESTSCRIPT_SETUP_CAPABILITY = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TESTSCRIPT_SETUP_CAPABILITY);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Natural language description of the TestScript</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="TestScript.description", description="Natural language description of the TestScript", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Natural language description of the TestScript</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Informal name for this TestScript</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="TestScript.name", description="Informal name for this TestScript", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Informal name for this TestScript</b><br>
   * Type: <b>string</b><br>
   * Path: <b>TestScript.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

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
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="TestScript.identifier", description="External identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>TestScript.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>Absolute URL used to reference this TestScript</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>TestScript.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="TestScript.url", description="Absolute URL used to reference this TestScript", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>Absolute URL used to reference this TestScript</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>TestScript.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);


}

