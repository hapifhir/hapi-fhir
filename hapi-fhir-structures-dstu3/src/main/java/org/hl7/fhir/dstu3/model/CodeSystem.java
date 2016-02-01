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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import org.hl7.fhir.instance.model.api.*;
/**
 * A code system specifies a set of codes drawn from one or more code systems.
 */
@ResourceDef(name="CodeSystem", profile="http://hl7.org/fhir/Profile/CodeSystem")
public class CodeSystem extends DomainResource {

    public enum CodeSystemContentMode {
        /**
         * None of the concepts defined by the code system are included in the code system resource
         */
        NOTPRESENT, 
        /**
         * A few representative concepts are included in the code system resource
         */
        EXAMPLAR, 
        /**
         * A subset of the code system concepts are included in the code system resource
         */
        FRAGMENT, 
        /**
         * All the concepts defined by the code system are included in the code system resource
         */
        COMPLETE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static CodeSystemContentMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-present".equals(codeString))
          return NOTPRESENT;
        if ("examplar".equals(codeString))
          return EXAMPLAR;
        if ("fragment".equals(codeString))
          return FRAGMENT;
        if ("complete".equals(codeString))
          return COMPLETE;
        throw new FHIRException("Unknown CodeSystemContentMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NOTPRESENT: return "not-present";
            case EXAMPLAR: return "examplar";
            case FRAGMENT: return "fragment";
            case COMPLETE: return "complete";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NOTPRESENT: return "http://hl7.org/fhir/codesystem-content-mode";
            case EXAMPLAR: return "http://hl7.org/fhir/codesystem-content-mode";
            case FRAGMENT: return "http://hl7.org/fhir/codesystem-content-mode";
            case COMPLETE: return "http://hl7.org/fhir/codesystem-content-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NOTPRESENT: return "None of the concepts defined by the code system are included in the code system resource";
            case EXAMPLAR: return "A few representative concepts are included in the code system resource";
            case FRAGMENT: return "A subset of the code system concepts are included in the code system resource";
            case COMPLETE: return "All the concepts defined by the code system are included in the code system resource";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NOTPRESENT: return "Not Present";
            case EXAMPLAR: return "Examplar";
            case FRAGMENT: return "Fragment";
            case COMPLETE: return "Complete";
            default: return "?";
          }
        }
    }

  public static class CodeSystemContentModeEnumFactory implements EnumFactory<CodeSystemContentMode> {
    public CodeSystemContentMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("not-present".equals(codeString))
          return CodeSystemContentMode.NOTPRESENT;
        if ("examplar".equals(codeString))
          return CodeSystemContentMode.EXAMPLAR;
        if ("fragment".equals(codeString))
          return CodeSystemContentMode.FRAGMENT;
        if ("complete".equals(codeString))
          return CodeSystemContentMode.COMPLETE;
        throw new IllegalArgumentException("Unknown CodeSystemContentMode code '"+codeString+"'");
        }
        public Enumeration<CodeSystemContentMode> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("not-present".equals(codeString))
          return new Enumeration<CodeSystemContentMode>(this, CodeSystemContentMode.NOTPRESENT);
        if ("examplar".equals(codeString))
          return new Enumeration<CodeSystemContentMode>(this, CodeSystemContentMode.EXAMPLAR);
        if ("fragment".equals(codeString))
          return new Enumeration<CodeSystemContentMode>(this, CodeSystemContentMode.FRAGMENT);
        if ("complete".equals(codeString))
          return new Enumeration<CodeSystemContentMode>(this, CodeSystemContentMode.COMPLETE);
        throw new FHIRException("Unknown CodeSystemContentMode code '"+codeString+"'");
        }
    public String toCode(CodeSystemContentMode code) {
      if (code == CodeSystemContentMode.NOTPRESENT)
        return "not-present";
      if (code == CodeSystemContentMode.EXAMPLAR)
        return "examplar";
      if (code == CodeSystemContentMode.FRAGMENT)
        return "fragment";
      if (code == CodeSystemContentMode.COMPLETE)
        return "complete";
      return "?";
      }
    public String toSystem(CodeSystemContentMode code) {
      return code.getSystem();
      }
    }

    public enum PropertyType {
        /**
         * The property value is a code that identifies a concept defined in the code system
         */
        CODE, 
        /**
         * The property  value is a code defined in an external code system. This may be used for translations, but is not the intent
         */
        CODING, 
        /**
         * The property value is a string
         */
        STRING, 
        /**
         * The property value is a string (often used to assign ranking values to concepts for supporting score assessments)
         */
        INTEGER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PropertyType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("code".equals(codeString))
          return CODE;
        if ("Coding".equals(codeString))
          return CODING;
        if ("string".equals(codeString))
          return STRING;
        if ("integer".equals(codeString))
          return INTEGER;
        throw new FHIRException("Unknown PropertyType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CODE: return "code";
            case CODING: return "Coding";
            case STRING: return "string";
            case INTEGER: return "integer";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CODE: return "http://hl7.org/fhir/concept-property-type";
            case CODING: return "http://hl7.org/fhir/concept-property-type";
            case STRING: return "http://hl7.org/fhir/concept-property-type";
            case INTEGER: return "http://hl7.org/fhir/concept-property-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CODE: return "The property value is a code that identifies a concept defined in the code system";
            case CODING: return "The property  value is a code defined in an external code system. This may be used for translations, but is not the intent";
            case STRING: return "The property value is a string";
            case INTEGER: return "The property value is a string (often used to assign ranking values to concepts for supporting score assessments)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CODE: return "code (internal reference)";
            case CODING: return "Coding (external reference)";
            case STRING: return "string";
            case INTEGER: return "integer";
            default: return "?";
          }
        }
    }

  public static class PropertyTypeEnumFactory implements EnumFactory<PropertyType> {
    public PropertyType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("code".equals(codeString))
          return PropertyType.CODE;
        if ("Coding".equals(codeString))
          return PropertyType.CODING;
        if ("string".equals(codeString))
          return PropertyType.STRING;
        if ("integer".equals(codeString))
          return PropertyType.INTEGER;
        throw new IllegalArgumentException("Unknown PropertyType code '"+codeString+"'");
        }
        public Enumeration<PropertyType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("code".equals(codeString))
          return new Enumeration<PropertyType>(this, PropertyType.CODE);
        if ("Coding".equals(codeString))
          return new Enumeration<PropertyType>(this, PropertyType.CODING);
        if ("string".equals(codeString))
          return new Enumeration<PropertyType>(this, PropertyType.STRING);
        if ("integer".equals(codeString))
          return new Enumeration<PropertyType>(this, PropertyType.INTEGER);
        throw new FHIRException("Unknown PropertyType code '"+codeString+"'");
        }
    public String toCode(PropertyType code) {
      if (code == PropertyType.CODE)
        return "code";
      if (code == PropertyType.CODING)
        return "Coding";
      if (code == PropertyType.STRING)
        return "string";
      if (code == PropertyType.INTEGER)
        return "integer";
      return "?";
      }
    public String toSystem(PropertyType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CodeSystemContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the code system.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of an individual to contact", formalDefinition="The name of an individual to contact regarding the code system." )
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
      public CodeSystemContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CodeSystemContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CodeSystemContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the code system.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the code system.
         */
        public CodeSystemContactComponent setName(String value) { 
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
        public CodeSystemContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the code system.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
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
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.name");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else
          return super.addChild(name);
      }

      public CodeSystemContactComponent copy() {
        CodeSystemContactComponent dst = new CodeSystemContactComponent();
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
        if (!(other instanceof CodeSystemContactComponent))
          return false;
        CodeSystemContactComponent o = (CodeSystemContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CodeSystemContactComponent))
          return false;
        CodeSystemContactComponent o = (CodeSystemContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  public String fhirType() {
    return "CodeSystem.contact";

  }

  }

    @Block()
    public static class CodeSystemFilterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The code that identifies thise filter when it is used in the instance.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Code that identifies the filter", formalDefinition="The code that identifies thise filter when it is used in the instance." )
        protected CodeType code;

        /**
         * A description of how or why the filter is used.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How or why the filter is used", formalDefinition="A description of how or why the filter is used." )
        protected StringType description;

        /**
         * A list of operators that can be used with the filter.
         */
        @Child(name = "operator", type = {CodeType.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Operators that can be used with filter", formalDefinition="A list of operators that can be used with the filter." )
        protected List<CodeType> operator;

        /**
         * A description of what the value for the filter should be.
         */
        @Child(name = "value", type = {StringType.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What to use for the value", formalDefinition="A description of what the value for the filter should be." )
        protected StringType value;

        private static final long serialVersionUID = 20272432L;

    /**
     * Constructor
     */
      public CodeSystemFilterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CodeSystemFilterComponent(CodeType code, StringType value) {
        super();
        this.code = code;
        this.value = value;
      }

        /**
         * @return {@link #code} (The code that identifies thise filter when it is used in the instance.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CodeSystemFilterComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The code that identifies thise filter when it is used in the instance.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeSystemFilterComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The code that identifies thise filter when it is used in the instance.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The code that identifies thise filter when it is used in the instance.
         */
        public CodeSystemFilterComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (A description of how or why the filter is used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CodeSystemFilterComponent.description");
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
         * @param value {@link #description} (A description of how or why the filter is used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public CodeSystemFilterComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A description of how or why the filter is used.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A description of how or why the filter is used.
         */
        public CodeSystemFilterComponent setDescription(String value) { 
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
         * @return {@link #operator} (A list of operators that can be used with the filter.)
         */
        public List<CodeType> getOperator() { 
          if (this.operator == null)
            this.operator = new ArrayList<CodeType>();
          return this.operator;
        }

        public boolean hasOperator() { 
          if (this.operator == null)
            return false;
          for (CodeType item : this.operator)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #operator} (A list of operators that can be used with the filter.)
         */
    // syntactic sugar
        public CodeType addOperatorElement() {//2 
          CodeType t = new CodeType();
          if (this.operator == null)
            this.operator = new ArrayList<CodeType>();
          this.operator.add(t);
          return t;
        }

        /**
         * @param value {@link #operator} (A list of operators that can be used with the filter.)
         */
        public CodeSystemFilterComponent addOperator(String value) { //1
          CodeType t = new CodeType();
          t.setValue(value);
          if (this.operator == null)
            this.operator = new ArrayList<CodeType>();
          this.operator.add(t);
          return this;
        }

        /**
         * @param value {@link #operator} (A list of operators that can be used with the filter.)
         */
        public boolean hasOperator(String value) { 
          if (this.operator == null)
            return false;
          for (CodeType v : this.operator)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #value} (A description of what the value for the filter should be.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CodeSystemFilterComponent.value");
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
         * @param value {@link #value} (A description of what the value for the filter should be.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public CodeSystemFilterComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return A description of what the value for the filter should be.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value A description of what the value for the filter should be.
         */
        public CodeSystemFilterComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "The code that identifies thise filter when it is used in the instance.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("description", "string", "A description of how or why the filter is used.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("operator", "code", "A list of operators that can be used with the filter.", 0, java.lang.Integer.MAX_VALUE, operator));
          childrenList.add(new Property("value", "string", "A description of what the value for the filter should be.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCode(value); // CodeType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("operator"))
          this.getOperator().add(castToCode(value));
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.code");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.description");
        }
        else if (name.equals("operator")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.operator");
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.value");
        }
        else
          return super.addChild(name);
      }

      public CodeSystemFilterComponent copy() {
        CodeSystemFilterComponent dst = new CodeSystemFilterComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.description = description == null ? null : description.copy();
        if (operator != null) {
          dst.operator = new ArrayList<CodeType>();
          for (CodeType i : operator)
            dst.operator.add(i.copy());
        };
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CodeSystemFilterComponent))
          return false;
        CodeSystemFilterComponent o = (CodeSystemFilterComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(description, o.description, true) && compareDeep(operator, o.operator, true)
           && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CodeSystemFilterComponent))
          return false;
        CodeSystemFilterComponent o = (CodeSystemFilterComponent) other;
        return compareValues(code, o.code, true) && compareValues(description, o.description, true) && compareValues(operator, o.operator, true)
           && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (description == null || description.isEmpty())
           && (operator == null || operator.isEmpty()) && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "CodeSystem.filter";

  }

  }

    @Block()
    public static class CodeSystemPropertyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and also externally, such as in property filters.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifies the property, both internally and externally", formalDefinition="A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and also externally, such as in property filters." )
        protected CodeType code;

        /**
         * A description of the property- why it is defined, and how it's value might be used.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Why the property is defined, and/or what it conveys", formalDefinition="A description of the property- why it is defined, and how it's value might be used." )
        protected StringType description;

        /**
         * The type of the property value.
         */
        @Child(name = "type", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="code | Coding | string | integer", formalDefinition="The type of the property value." )
        protected Enumeration<PropertyType> type;

        private static final long serialVersionUID = -1346176181L;

    /**
     * Constructor
     */
      public CodeSystemPropertyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CodeSystemPropertyComponent(CodeType code, Enumeration<PropertyType> type) {
        super();
        this.code = code;
        this.type = type;
      }

        /**
         * @return {@link #code} (A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and also externally, such as in property filters.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CodeSystemPropertyComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and also externally, such as in property filters.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeSystemPropertyComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and also externally, such as in property filters.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and also externally, such as in property filters.
         */
        public CodeSystemPropertyComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (A description of the property- why it is defined, and how it's value might be used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CodeSystemPropertyComponent.description");
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
         * @param value {@link #description} (A description of the property- why it is defined, and how it's value might be used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public CodeSystemPropertyComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A description of the property- why it is defined, and how it's value might be used.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A description of the property- why it is defined, and how it's value might be used.
         */
        public CodeSystemPropertyComponent setDescription(String value) { 
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
         * @return {@link #type} (The type of the property value.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<PropertyType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CodeSystemPropertyComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<PropertyType>(new PropertyTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the property value.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeSystemPropertyComponent setTypeElement(Enumeration<PropertyType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of the property value.
         */
        public PropertyType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of the property value.
         */
        public CodeSystemPropertyComponent setType(PropertyType value) { 
            if (this.type == null)
              this.type = new Enumeration<PropertyType>(new PropertyTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "A code that is used to identify the property. The code is used internally (in CodeSystem.concept.property.code) and also externally, such as in property filters.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("description", "string", "A description of the property- why it is defined, and how it's value might be used.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("type", "code", "The type of the property value.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCode(value); // CodeType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = new PropertyTypeEnumFactory().fromType(value); // Enumeration<PropertyType>
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.code");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.description");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.type");
        }
        else
          return super.addChild(name);
      }

      public CodeSystemPropertyComponent copy() {
        CodeSystemPropertyComponent dst = new CodeSystemPropertyComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.description = description == null ? null : description.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CodeSystemPropertyComponent))
          return false;
        CodeSystemPropertyComponent o = (CodeSystemPropertyComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(description, o.description, true) && compareDeep(type, o.type, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CodeSystemPropertyComponent))
          return false;
        CodeSystemPropertyComponent o = (CodeSystemPropertyComponent) other;
        return compareValues(code, o.code, true) && compareValues(description, o.description, true) && compareValues(type, o.type, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (description == null || description.isEmpty())
           && (type == null || type.isEmpty());
      }

  public String fhirType() {
    return "CodeSystem.property";

  }

  }

    @Block()
    public static class ConceptDefinitionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code - a text symbol - that uniquely identifies the concept within the code system.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code that identifies concept", formalDefinition="A code - a text symbol - that uniquely identifies the concept within the code system." )
        protected CodeType code;

        /**
         * A human readable string that is the recommended default way to present this concept to a user.
         */
        @Child(name = "display", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Text to display to the user", formalDefinition="A human readable string that is the recommended default way to present this concept to a user." )
        protected StringType display;

        /**
         * The formal definition of the concept. The code system resource does not make formal definitions required, because of the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        @Child(name = "definition", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Formal definition", formalDefinition="The formal definition of the concept. The code system resource does not make formal definitions required, because of the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning associated with the concept." )
        protected StringType definition;

        /**
         * Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.
         */
        @Child(name = "designation", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional representations for the concept", formalDefinition="Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc." )
        protected List<ConceptDefinitionDesignationComponent> designation;

        /**
         * A property value for this concept.
         */
        @Child(name = "property", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Property value for the concept", formalDefinition="A property value for this concept." )
        protected List<ConceptDefinitionPropertyComponent> property;

        /**
         * Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-a/contains/categorizes) and can only be determined by examining the definitions of the concepts.
         */
        @Child(name = "concept", type = {ConceptDefinitionComponent.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Child Concepts (is-a/contains/categorizes)", formalDefinition="Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-a/contains/categorizes) and can only be determined by examining the definitions of the concepts." )
        protected List<ConceptDefinitionComponent> concept;

        private static final long serialVersionUID = 1495076297L;

    /**
     * Constructor
     */
      public ConceptDefinitionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ConceptDefinitionComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A code - a text symbol - that uniquely identifies the concept within the code system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code - a text symbol - that uniquely identifies the concept within the code system.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ConceptDefinitionComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return A code - a text symbol - that uniquely identifies the concept within the code system.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value A code - a text symbol - that uniquely identifies the concept within the code system.
         */
        public ConceptDefinitionComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #display} (A human readable string that is the recommended default way to present this concept to a user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.display");
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
         * @param value {@link #display} (A human readable string that is the recommended default way to present this concept to a user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public ConceptDefinitionComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return A human readable string that is the recommended default way to present this concept to a user.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value A human readable string that is the recommended default way to present this concept to a user.
         */
        public ConceptDefinitionComponent setDisplay(String value) { 
          if (Utilities.noString(value))
            this.display = null;
          else {
            if (this.display == null)
              this.display = new StringType();
            this.display.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #definition} (The formal definition of the concept. The code system resource does not make formal definitions required, because of the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning associated with the concept.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public StringType getDefinitionElement() { 
          if (this.definition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionComponent.definition");
            else if (Configuration.doAutoCreate())
              this.definition = new StringType(); // bb
          return this.definition;
        }

        public boolean hasDefinitionElement() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        public boolean hasDefinition() { 
          return this.definition != null && !this.definition.isEmpty();
        }

        /**
         * @param value {@link #definition} (The formal definition of the concept. The code system resource does not make formal definitions required, because of the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning associated with the concept.). This is the underlying object with id, value and extensions. The accessor "getDefinition" gives direct access to the value
         */
        public ConceptDefinitionComponent setDefinitionElement(StringType value) { 
          this.definition = value;
          return this;
        }

        /**
         * @return The formal definition of the concept. The code system resource does not make formal definitions required, because of the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        public String getDefinition() { 
          return this.definition == null ? null : this.definition.getValue();
        }

        /**
         * @param value The formal definition of the concept. The code system resource does not make formal definitions required, because of the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning associated with the concept.
         */
        public ConceptDefinitionComponent setDefinition(String value) { 
          if (Utilities.noString(value))
            this.definition = null;
          else {
            if (this.definition == null)
              this.definition = new StringType();
            this.definition.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #designation} (Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.)
         */
        public List<ConceptDefinitionDesignationComponent> getDesignation() { 
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          return this.designation;
        }

        public boolean hasDesignation() { 
          if (this.designation == null)
            return false;
          for (ConceptDefinitionDesignationComponent item : this.designation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #designation} (Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.)
         */
    // syntactic sugar
        public ConceptDefinitionDesignationComponent addDesignation() { //3
          ConceptDefinitionDesignationComponent t = new ConceptDefinitionDesignationComponent();
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return t;
        }

    // syntactic sugar
        public ConceptDefinitionComponent addDesignation(ConceptDefinitionDesignationComponent t) { //3
          if (t == null)
            return this;
          if (this.designation == null)
            this.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          this.designation.add(t);
          return this;
        }

        /**
         * @return {@link #property} (A property value for this concept.)
         */
        public List<ConceptDefinitionPropertyComponent> getProperty() { 
          if (this.property == null)
            this.property = new ArrayList<ConceptDefinitionPropertyComponent>();
          return this.property;
        }

        public boolean hasProperty() { 
          if (this.property == null)
            return false;
          for (ConceptDefinitionPropertyComponent item : this.property)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #property} (A property value for this concept.)
         */
    // syntactic sugar
        public ConceptDefinitionPropertyComponent addProperty() { //3
          ConceptDefinitionPropertyComponent t = new ConceptDefinitionPropertyComponent();
          if (this.property == null)
            this.property = new ArrayList<ConceptDefinitionPropertyComponent>();
          this.property.add(t);
          return t;
        }

    // syntactic sugar
        public ConceptDefinitionComponent addProperty(ConceptDefinitionPropertyComponent t) { //3
          if (t == null)
            return this;
          if (this.property == null)
            this.property = new ArrayList<ConceptDefinitionPropertyComponent>();
          this.property.add(t);
          return this;
        }

        /**
         * @return {@link #concept} (Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-a/contains/categorizes) and can only be determined by examining the definitions of the concepts.)
         */
        public List<ConceptDefinitionComponent> getConcept() { 
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          return this.concept;
        }

        public boolean hasConcept() { 
          if (this.concept == null)
            return false;
          for (ConceptDefinitionComponent item : this.concept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #concept} (Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-a/contains/categorizes) and can only be determined by examining the definitions of the concepts.)
         */
    // syntactic sugar
        public ConceptDefinitionComponent addConcept() { //3
          ConceptDefinitionComponent t = new ConceptDefinitionComponent();
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return t;
        }

    // syntactic sugar
        public ConceptDefinitionComponent addConcept(ConceptDefinitionComponent t) { //3
          if (t == null)
            return this;
          if (this.concept == null)
            this.concept = new ArrayList<ConceptDefinitionComponent>();
          this.concept.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "A code - a text symbol - that uniquely identifies the concept within the code system.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("display", "string", "A human readable string that is the recommended default way to present this concept to a user.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("definition", "string", "The formal definition of the concept. The code system resource does not make formal definitions required, because of the prevalence of legacy systems. However, they are highly recommended, as without them there is no formal meaning associated with the concept.", 0, java.lang.Integer.MAX_VALUE, definition));
          childrenList.add(new Property("designation", "", "Additional representations for the concept - other languages, aliases, specialized purposes, used for particular purposes, etc.", 0, java.lang.Integer.MAX_VALUE, designation));
          childrenList.add(new Property("property", "", "A property value for this concept.", 0, java.lang.Integer.MAX_VALUE, property));
          childrenList.add(new Property("concept", "@CodeSystem.concept", "Defines children of a concept to produce a hierarchy of concepts. The nature of the relationships is variable (is-a/contains/categorizes) and can only be determined by examining the definitions of the concepts.", 0, java.lang.Integer.MAX_VALUE, concept));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCode(value); // CodeType
        else if (name.equals("display"))
          this.display = castToString(value); // StringType
        else if (name.equals("definition"))
          this.definition = castToString(value); // StringType
        else if (name.equals("designation"))
          this.getDesignation().add((ConceptDefinitionDesignationComponent) value);
        else if (name.equals("property"))
          this.getProperty().add((ConceptDefinitionPropertyComponent) value);
        else if (name.equals("concept"))
          this.getConcept().add((ConceptDefinitionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.code");
        }
        else if (name.equals("display")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.display");
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.definition");
        }
        else if (name.equals("designation")) {
          return addDesignation();
        }
        else if (name.equals("property")) {
          return addProperty();
        }
        else if (name.equals("concept")) {
          return addConcept();
        }
        else
          return super.addChild(name);
      }

      public ConceptDefinitionComponent copy() {
        ConceptDefinitionComponent dst = new ConceptDefinitionComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.display = display == null ? null : display.copy();
        dst.definition = definition == null ? null : definition.copy();
        if (designation != null) {
          dst.designation = new ArrayList<ConceptDefinitionDesignationComponent>();
          for (ConceptDefinitionDesignationComponent i : designation)
            dst.designation.add(i.copy());
        };
        if (property != null) {
          dst.property = new ArrayList<ConceptDefinitionPropertyComponent>();
          for (ConceptDefinitionPropertyComponent i : property)
            dst.property.add(i.copy());
        };
        if (concept != null) {
          dst.concept = new ArrayList<ConceptDefinitionComponent>();
          for (ConceptDefinitionComponent i : concept)
            dst.concept.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptDefinitionComponent))
          return false;
        ConceptDefinitionComponent o = (ConceptDefinitionComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(display, o.display, true) && compareDeep(definition, o.definition, true)
           && compareDeep(designation, o.designation, true) && compareDeep(property, o.property, true) && compareDeep(concept, o.concept, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptDefinitionComponent))
          return false;
        ConceptDefinitionComponent o = (ConceptDefinitionComponent) other;
        return compareValues(code, o.code, true) && compareValues(display, o.display, true) && compareValues(definition, o.definition, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (display == null || display.isEmpty())
           && (definition == null || definition.isEmpty()) && (designation == null || designation.isEmpty())
           && (property == null || property.isEmpty()) && (concept == null || concept.isEmpty());
      }

  public String fhirType() {
    return "CodeSystem.concept";

  }

  }

    @Block()
    public static class ConceptDefinitionDesignationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The language this designation is defined for.
         */
        @Child(name = "language", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human language of the designation", formalDefinition="The language this designation is defined for." )
        protected CodeType language;

        /**
         * A code that details how this designation would be used.
         */
        @Child(name = "use", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Details how this designation would be used", formalDefinition="A code that details how this designation would be used." )
        protected Coding use;

        /**
         * The text value for this designation.
         */
        @Child(name = "value", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The text value for this designation", formalDefinition="The text value for this designation." )
        protected StringType value;

        private static final long serialVersionUID = 1515662414L;

    /**
     * Constructor
     */
      public ConceptDefinitionDesignationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ConceptDefinitionDesignationComponent(StringType value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public CodeType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionDesignationComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new CodeType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The language this designation is defined for.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public ConceptDefinitionDesignationComponent setLanguageElement(CodeType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The language this designation is defined for.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The language this designation is defined for.
         */
        public ConceptDefinitionDesignationComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new CodeType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #use} (A code that details how this designation would be used.)
         */
        public Coding getUse() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionDesignationComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new Coding(); // cc
          return this.use;
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (A code that details how this designation would be used.)
         */
        public ConceptDefinitionDesignationComponent setUse(Coding value) { 
          this.use = value;
          return this;
        }

        /**
         * @return {@link #value} (The text value for this designation.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionDesignationComponent.value");
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
         * @param value {@link #value} (The text value for this designation.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public ConceptDefinitionDesignationComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The text value for this designation.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The text value for this designation.
         */
        public ConceptDefinitionDesignationComponent setValue(String value) { 
            if (this.value == null)
              this.value = new StringType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("language", "code", "The language this designation is defined for.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("use", "Coding", "A code that details how this designation would be used.", 0, java.lang.Integer.MAX_VALUE, use));
          childrenList.add(new Property("value", "string", "The text value for this designation.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("language"))
          this.language = castToCode(value); // CodeType
        else if (name.equals("use"))
          this.use = castToCoding(value); // Coding
        else if (name.equals("value"))
          this.value = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.language");
        }
        else if (name.equals("use")) {
          this.use = new Coding();
          return this.use;
        }
        else if (name.equals("value")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.value");
        }
        else
          return super.addChild(name);
      }

      public ConceptDefinitionDesignationComponent copy() {
        ConceptDefinitionDesignationComponent dst = new ConceptDefinitionDesignationComponent();
        copyValues(dst);
        dst.language = language == null ? null : language.copy();
        dst.use = use == null ? null : use.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptDefinitionDesignationComponent))
          return false;
        ConceptDefinitionDesignationComponent o = (ConceptDefinitionDesignationComponent) other;
        return compareDeep(language, o.language, true) && compareDeep(use, o.use, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptDefinitionDesignationComponent))
          return false;
        ConceptDefinitionDesignationComponent o = (ConceptDefinitionDesignationComponent) other;
        return compareValues(language, o.language, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (language == null || language.isEmpty()) && (use == null || use.isEmpty())
           && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "CodeSystem.concept.designation";

  }

  }

    @Block()
    public static class ConceptDefinitionPropertyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that is a reference to CodeSystem.property.code.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to CodeSystem.property.code", formalDefinition="A code that is a reference to CodeSystem.property.code." )
        protected CodeType code;

        /**
         * The value of this property.
         */
        @Child(name = "value", type = {CodeType.class, Coding.class, StringType.class, IntegerType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Value of the property for this concept", formalDefinition="The value of this property." )
        protected Type value;

        private static final long serialVersionUID = 1742812311L;

    /**
     * Constructor
     */
      public ConceptDefinitionPropertyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ConceptDefinitionPropertyComponent(CodeType code, Type value) {
        super();
        this.code = code;
        this.value = value;
      }

        /**
         * @return {@link #code} (A code that is a reference to CodeSystem.property.code.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConceptDefinitionPropertyComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeType(); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that is a reference to CodeSystem.property.code.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public ConceptDefinitionPropertyComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return A code that is a reference to CodeSystem.property.code.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value A code that is a reference to CodeSystem.property.code.
         */
        public ConceptDefinitionPropertyComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of this property.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The value of this property.)
         */
        public CodeType getValueCodeType() throws FHIRException { 
          if (!(this.value instanceof CodeType))
            throw new FHIRException("Type mismatch: the type CodeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (CodeType) this.value;
        }

        public boolean hasValueCodeType() { 
          return this.value instanceof CodeType;
        }

        /**
         * @return {@link #value} (The value of this property.)
         */
        public Coding getValueCoding() throws FHIRException { 
          if (!(this.value instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

        public boolean hasValueCoding() { 
          return this.value instanceof Coding;
        }

        /**
         * @return {@link #value} (The value of this property.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (The value of this property.)
         */
        public IntegerType getValueIntegerType() throws FHIRException { 
          if (!(this.value instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() { 
          return this.value instanceof IntegerType;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of this property.)
         */
        public ConceptDefinitionPropertyComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "A code that is a reference to CodeSystem.property.code.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("value[x]", "code|Coding|string|integer", "The value of this property.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code"))
          this.code = castToCode(value); // CodeType
        else if (name.equals("value[x]"))
          this.value = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.code");
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public ConceptDefinitionPropertyComponent copy() {
        ConceptDefinitionPropertyComponent dst = new ConceptDefinitionPropertyComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConceptDefinitionPropertyComponent))
          return false;
        ConceptDefinitionPropertyComponent o = (ConceptDefinitionPropertyComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConceptDefinitionPropertyComponent))
          return false;
        ConceptDefinitionPropertyComponent o = (ConceptDefinitionPropertyComponent) other;
        return compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "CodeSystem.concept.property";

  }

  }

    /**
     * An absolute URL that is used to identify this code system when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this code system is (or will be) published. This is used in [Coding]{datatypes.html#Coding}.system.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Globally unique logical identifier for  code system (Coding.system)", formalDefinition="An absolute URL that is used to identify this code system when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this code system is (or will be) published. This is used in [Coding]{datatypes.html#Coding}.system." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this code system when it is represented in other formats, or referenced in a specification, model, design or an instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional identifier for the code system (e.g. HL7 v2 / CDA)", formalDefinition="Formal identifier that is used to identify this code system when it is represented in other formats, or referenced in a specification, model, design or an instance." )
    protected Identifier identifier;

    /**
     * Used to identify this version of the code system when it is referenced in a specification, model, design or instance (e.g. Coding.version). This is an arbitrary value managed by the profile author manually and the value should be a timestamp. This is used in [Coding]{datatypes.html#Coding}.version.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier for this version (Coding.version)", formalDefinition="Used to identify this version of the code system when it is referenced in a specification, model, design or instance (e.g. Coding.version). This is an arbitrary value managed by the profile author manually and the value should be a timestamp. This is used in [Coding]{datatypes.html#Coding}.version." )
    protected StringType version;

    /**
     * A free text natural language name describing the code system.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this code system", formalDefinition="A free text natural language name describing the code system." )
    protected StringType name;

    /**
     * The status of the code system.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the code system." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This CodeSystem was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This CodeSystem was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the code system.
     */
    @Child(name = "publisher", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (organization or individual)", formalDefinition="The name of the individual or organization that published the code system." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<CodeSystemContactComponent> contact;

    /**
     * The date that the code system status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes (e.g. the 'content logical definition').
     */
    @Child(name = "date", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for given status", formalDefinition="The date that the code system status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes (e.g. the 'content logical definition')." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the use of the code system - reason for definition, "the semantic space" to be included in the code system, conditions of use, etc. The description may include a list of expected usages for the code system and can also describe the approach taken to build the code system.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human language description of the code system", formalDefinition="A free text natural language description of the use of the code system - reason for definition, \"the semantic space\" to be included in the code system, conditions of use, etc. The description may include a list of expected usages for the code system and can also describe the approach taken to build the code system." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions." )
    protected List<CodeableConcept> useContext;

    /**
     * Explains why this code system is needed and why it has been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why needed", formalDefinition="Explains why this code system is needed and why it has been constrained as it has." )
    protected StringType requirements;

    /**
     * A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the code system.
     */
    @Child(name = "copyright", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the code system." )
    protected StringType copyright;

    /**
     * If code comparison is case sensitive when codes within this system are compared to each other.
     */
    @Child(name = "caseSensitive", type = {BooleanType.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If code comparison is case sensitive", formalDefinition="If code comparison is case sensitive when codes within this system are compared to each other." )
    protected BooleanType caseSensitive;

    /**
     * True If code system defines a post-composition grammar.
     */
    @Child(name = "compositional", type = {BooleanType.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If code system defines a post-composition grammar", formalDefinition="True If code system defines a post-composition grammar." )
    protected BooleanType compositional;

    /**
     * This flag is used to signify that the code system has not (or does not) maintain the definitions, and a version must be specified when referencing this code system.
     */
    @Child(name = "versionNeeded", type = {BooleanType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If definitions are not stable", formalDefinition="This flag is used to signify that the code system has not (or does not) maintain the definitions, and a version must be specified when referencing this code system." )
    protected BooleanType versionNeeded;

    /**
     * How much of the content of the code system - the concepts and codes it defines - are represented in this resource.
     */
    @Child(name = "content", type = {CodeType.class}, order=16, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="not-present | examplar | fragment | complete", formalDefinition="How much of the content of the code system - the concepts and codes it defines - are represented in this resource." )
    protected Enumeration<CodeSystemContentMode> content;

    /**
     * The total number of concepts defined by the code system. Where the code system has a compositional grammar, the count refers to the number of base (primitive) concepts.
     */
    @Child(name = "count", type = {UnsignedIntType.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Total concepts in the code system", formalDefinition="The total number of concepts defined by the code system. Where the code system has a compositional grammar, the count refers to the number of base (primitive) concepts." )
    protected UnsignedIntType count;

    /**
     * A filter that can be used in a value set compose statement when selecting concepts using a filter.
     */
    @Child(name = "filter", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Filter that can be used in a value set", formalDefinition="A filter that can be used in a value set compose statement when selecting concepts using a filter." )
    protected List<CodeSystemFilterComponent> filter;

    /**
     * A property defines an additional slot through which additional information can be provided about a concept.
     */
    @Child(name = "property", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional information supplied about each concept", formalDefinition="A property defines an additional slot through which additional information can be provided about a concept." )
    protected List<CodeSystemPropertyComponent> property;

    /**
     * Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be consulted to determine what the meaning of the hierarchical relationships are.
     */
    @Child(name = "concept", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Concepts in the code system", formalDefinition="Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be consulted to determine what the meaning of the hierarchical relationships are." )
    protected List<ConceptDefinitionComponent> concept;

    private static final long serialVersionUID = 331143159L;

  /**
   * Constructor
   */
    public CodeSystem() {
      super();
    }

  /**
   * Constructor
   */
    public CodeSystem(Enumeration<ConformanceResourceStatus> status, Enumeration<CodeSystemContentMode> content) {
      super();
      this.status = status;
      this.content = content;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this code system when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this code system is (or will be) published. This is used in [Coding]{datatypes.html#Coding}.system.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this code system when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this code system is (or will be) published. This is used in [Coding]{datatypes.html#Coding}.system.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public CodeSystem setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this code system when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this code system is (or will be) published. This is used in [Coding]{datatypes.html#Coding}.system.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this code system when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this code system is (or will be) published. This is used in [Coding]{datatypes.html#Coding}.system.
     */
    public CodeSystem setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (Formal identifier that is used to identify this code system when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Formal identifier that is used to identify this code system when it is represented in other formats, or referenced in a specification, model, design or an instance.)
     */
    public CodeSystem setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #version} (Used to identify this version of the code system when it is referenced in a specification, model, design or instance (e.g. Coding.version). This is an arbitrary value managed by the profile author manually and the value should be a timestamp. This is used in [Coding]{datatypes.html#Coding}.version.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.version");
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
     * @param value {@link #version} (Used to identify this version of the code system when it is referenced in a specification, model, design or instance (e.g. Coding.version). This is an arbitrary value managed by the profile author manually and the value should be a timestamp. This is used in [Coding]{datatypes.html#Coding}.version.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public CodeSystem setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return Used to identify this version of the code system when it is referenced in a specification, model, design or instance (e.g. Coding.version). This is an arbitrary value managed by the profile author manually and the value should be a timestamp. This is used in [Coding]{datatypes.html#Coding}.version.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value Used to identify this version of the code system when it is referenced in a specification, model, design or instance (e.g. Coding.version). This is an arbitrary value managed by the profile author manually and the value should be a timestamp. This is used in [Coding]{datatypes.html#Coding}.version.
     */
    public CodeSystem setVersion(String value) { 
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
     * @return {@link #name} (A free text natural language name describing the code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.name");
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
     * @param value {@link #name} (A free text natural language name describing the code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CodeSystem setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name describing the code system.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name describing the code system.
     */
    public CodeSystem setName(String value) { 
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
     * @return {@link #status} (The status of the code system.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.status");
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
     * @param value {@link #status} (The status of the code system.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CodeSystem setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the code system.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the code system.
     */
    public CodeSystem setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This CodeSystem was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.experimental");
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
     * @param value {@link #experimental} (This CodeSystem was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public CodeSystem setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This CodeSystem was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This CodeSystem was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public CodeSystem setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the code system.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the code system.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public CodeSystem setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the code system.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the code system.
     */
    public CodeSystem setPublisher(String value) { 
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
    public List<CodeSystemContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<CodeSystemContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (CodeSystemContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public CodeSystemContactComponent addContact() { //3
      CodeSystemContactComponent t = new CodeSystemContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<CodeSystemContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public CodeSystem addContact(CodeSystemContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<CodeSystemContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date that the code system status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes (e.g. the 'content logical definition').). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.date");
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
     * @param value {@link #date} (The date that the code system status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes (e.g. the 'content logical definition').). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public CodeSystem setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that the code system status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes (e.g. the 'content logical definition').
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that the code system status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes (e.g. the 'content logical definition').
     */
    public CodeSystem setDate(Date value) { 
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
     * @return {@link #description} (A free text natural language description of the use of the code system - reason for definition, "the semantic space" to be included in the code system, conditions of use, etc. The description may include a list of expected usages for the code system and can also describe the approach taken to build the code system.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.description");
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
     * @param value {@link #description} (A free text natural language description of the use of the code system - reason for definition, "the semantic space" to be included in the code system, conditions of use, etc. The description may include a list of expected usages for the code system and can also describe the approach taken to build the code system.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public CodeSystem setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the use of the code system - reason for definition, "the semantic space" to be included in the code system, conditions of use, etc. The description may include a list of expected usages for the code system and can also describe the approach taken to build the code system.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the use of the code system - reason for definition, "the semantic space" to be included in the code system, conditions of use, etc. The description may include a list of expected usages for the code system and can also describe the approach taken to build the code system.
     */
    public CodeSystem setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.)
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.)
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
    public CodeSystem addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #requirements} (Explains why this code system is needed and why it has been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.requirements");
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
     * @param value {@link #requirements} (Explains why this code system is needed and why it has been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public CodeSystem setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this code system is needed and why it has been constrained as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this code system is needed and why it has been constrained as it has.
     */
    public CodeSystem setRequirements(String value) { 
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
     * @return {@link #copyright} (A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the code system.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the code system.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public CodeSystem setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the code system.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the code system.
     */
    public CodeSystem setCopyright(String value) { 
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
     * @return {@link #caseSensitive} (If code comparison is case sensitive when codes within this system are compared to each other.). This is the underlying object with id, value and extensions. The accessor "getCaseSensitive" gives direct access to the value
     */
    public BooleanType getCaseSensitiveElement() { 
      if (this.caseSensitive == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.caseSensitive");
        else if (Configuration.doAutoCreate())
          this.caseSensitive = new BooleanType(); // bb
      return this.caseSensitive;
    }

    public boolean hasCaseSensitiveElement() { 
      return this.caseSensitive != null && !this.caseSensitive.isEmpty();
    }

    public boolean hasCaseSensitive() { 
      return this.caseSensitive != null && !this.caseSensitive.isEmpty();
    }

    /**
     * @param value {@link #caseSensitive} (If code comparison is case sensitive when codes within this system are compared to each other.). This is the underlying object with id, value and extensions. The accessor "getCaseSensitive" gives direct access to the value
     */
    public CodeSystem setCaseSensitiveElement(BooleanType value) { 
      this.caseSensitive = value;
      return this;
    }

    /**
     * @return If code comparison is case sensitive when codes within this system are compared to each other.
     */
    public boolean getCaseSensitive() { 
      return this.caseSensitive == null || this.caseSensitive.isEmpty() ? false : this.caseSensitive.getValue();
    }

    /**
     * @param value If code comparison is case sensitive when codes within this system are compared to each other.
     */
    public CodeSystem setCaseSensitive(boolean value) { 
        if (this.caseSensitive == null)
          this.caseSensitive = new BooleanType();
        this.caseSensitive.setValue(value);
      return this;
    }

    /**
     * @return {@link #compositional} (True If code system defines a post-composition grammar.). This is the underlying object with id, value and extensions. The accessor "getCompositional" gives direct access to the value
     */
    public BooleanType getCompositionalElement() { 
      if (this.compositional == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.compositional");
        else if (Configuration.doAutoCreate())
          this.compositional = new BooleanType(); // bb
      return this.compositional;
    }

    public boolean hasCompositionalElement() { 
      return this.compositional != null && !this.compositional.isEmpty();
    }

    public boolean hasCompositional() { 
      return this.compositional != null && !this.compositional.isEmpty();
    }

    /**
     * @param value {@link #compositional} (True If code system defines a post-composition grammar.). This is the underlying object with id, value and extensions. The accessor "getCompositional" gives direct access to the value
     */
    public CodeSystem setCompositionalElement(BooleanType value) { 
      this.compositional = value;
      return this;
    }

    /**
     * @return True If code system defines a post-composition grammar.
     */
    public boolean getCompositional() { 
      return this.compositional == null || this.compositional.isEmpty() ? false : this.compositional.getValue();
    }

    /**
     * @param value True If code system defines a post-composition grammar.
     */
    public CodeSystem setCompositional(boolean value) { 
        if (this.compositional == null)
          this.compositional = new BooleanType();
        this.compositional.setValue(value);
      return this;
    }

    /**
     * @return {@link #versionNeeded} (This flag is used to signify that the code system has not (or does not) maintain the definitions, and a version must be specified when referencing this code system.). This is the underlying object with id, value and extensions. The accessor "getVersionNeeded" gives direct access to the value
     */
    public BooleanType getVersionNeededElement() { 
      if (this.versionNeeded == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.versionNeeded");
        else if (Configuration.doAutoCreate())
          this.versionNeeded = new BooleanType(); // bb
      return this.versionNeeded;
    }

    public boolean hasVersionNeededElement() { 
      return this.versionNeeded != null && !this.versionNeeded.isEmpty();
    }

    public boolean hasVersionNeeded() { 
      return this.versionNeeded != null && !this.versionNeeded.isEmpty();
    }

    /**
     * @param value {@link #versionNeeded} (This flag is used to signify that the code system has not (or does not) maintain the definitions, and a version must be specified when referencing this code system.). This is the underlying object with id, value and extensions. The accessor "getVersionNeeded" gives direct access to the value
     */
    public CodeSystem setVersionNeededElement(BooleanType value) { 
      this.versionNeeded = value;
      return this;
    }

    /**
     * @return This flag is used to signify that the code system has not (or does not) maintain the definitions, and a version must be specified when referencing this code system.
     */
    public boolean getVersionNeeded() { 
      return this.versionNeeded == null || this.versionNeeded.isEmpty() ? false : this.versionNeeded.getValue();
    }

    /**
     * @param value This flag is used to signify that the code system has not (or does not) maintain the definitions, and a version must be specified when referencing this code system.
     */
    public CodeSystem setVersionNeeded(boolean value) { 
        if (this.versionNeeded == null)
          this.versionNeeded = new BooleanType();
        this.versionNeeded.setValue(value);
      return this;
    }

    /**
     * @return {@link #content} (How much of the content of the code system - the concepts and codes it defines - are represented in this resource.). This is the underlying object with id, value and extensions. The accessor "getContent" gives direct access to the value
     */
    public Enumeration<CodeSystemContentMode> getContentElement() { 
      if (this.content == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.content");
        else if (Configuration.doAutoCreate())
          this.content = new Enumeration<CodeSystemContentMode>(new CodeSystemContentModeEnumFactory()); // bb
      return this.content;
    }

    public boolean hasContentElement() { 
      return this.content != null && !this.content.isEmpty();
    }

    public boolean hasContent() { 
      return this.content != null && !this.content.isEmpty();
    }

    /**
     * @param value {@link #content} (How much of the content of the code system - the concepts and codes it defines - are represented in this resource.). This is the underlying object with id, value and extensions. The accessor "getContent" gives direct access to the value
     */
    public CodeSystem setContentElement(Enumeration<CodeSystemContentMode> value) { 
      this.content = value;
      return this;
    }

    /**
     * @return How much of the content of the code system - the concepts and codes it defines - are represented in this resource.
     */
    public CodeSystemContentMode getContent() { 
      return this.content == null ? null : this.content.getValue();
    }

    /**
     * @param value How much of the content of the code system - the concepts and codes it defines - are represented in this resource.
     */
    public CodeSystem setContent(CodeSystemContentMode value) { 
        if (this.content == null)
          this.content = new Enumeration<CodeSystemContentMode>(new CodeSystemContentModeEnumFactory());
        this.content.setValue(value);
      return this;
    }

    /**
     * @return {@link #count} (The total number of concepts defined by the code system. Where the code system has a compositional grammar, the count refers to the number of base (primitive) concepts.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
     */
    public UnsignedIntType getCountElement() { 
      if (this.count == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CodeSystem.count");
        else if (Configuration.doAutoCreate())
          this.count = new UnsignedIntType(); // bb
      return this.count;
    }

    public boolean hasCountElement() { 
      return this.count != null && !this.count.isEmpty();
    }

    public boolean hasCount() { 
      return this.count != null && !this.count.isEmpty();
    }

    /**
     * @param value {@link #count} (The total number of concepts defined by the code system. Where the code system has a compositional grammar, the count refers to the number of base (primitive) concepts.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
     */
    public CodeSystem setCountElement(UnsignedIntType value) { 
      this.count = value;
      return this;
    }

    /**
     * @return The total number of concepts defined by the code system. Where the code system has a compositional grammar, the count refers to the number of base (primitive) concepts.
     */
    public int getCount() { 
      return this.count == null || this.count.isEmpty() ? 0 : this.count.getValue();
    }

    /**
     * @param value The total number of concepts defined by the code system. Where the code system has a compositional grammar, the count refers to the number of base (primitive) concepts.
     */
    public CodeSystem setCount(int value) { 
        if (this.count == null)
          this.count = new UnsignedIntType();
        this.count.setValue(value);
      return this;
    }

    /**
     * @return {@link #filter} (A filter that can be used in a value set compose statement when selecting concepts using a filter.)
     */
    public List<CodeSystemFilterComponent> getFilter() { 
      if (this.filter == null)
        this.filter = new ArrayList<CodeSystemFilterComponent>();
      return this.filter;
    }

    public boolean hasFilter() { 
      if (this.filter == null)
        return false;
      for (CodeSystemFilterComponent item : this.filter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #filter} (A filter that can be used in a value set compose statement when selecting concepts using a filter.)
     */
    // syntactic sugar
    public CodeSystemFilterComponent addFilter() { //3
      CodeSystemFilterComponent t = new CodeSystemFilterComponent();
      if (this.filter == null)
        this.filter = new ArrayList<CodeSystemFilterComponent>();
      this.filter.add(t);
      return t;
    }

    // syntactic sugar
    public CodeSystem addFilter(CodeSystemFilterComponent t) { //3
      if (t == null)
        return this;
      if (this.filter == null)
        this.filter = new ArrayList<CodeSystemFilterComponent>();
      this.filter.add(t);
      return this;
    }

    /**
     * @return {@link #property} (A property defines an additional slot through which additional information can be provided about a concept.)
     */
    public List<CodeSystemPropertyComponent> getProperty() { 
      if (this.property == null)
        this.property = new ArrayList<CodeSystemPropertyComponent>();
      return this.property;
    }

    public boolean hasProperty() { 
      if (this.property == null)
        return false;
      for (CodeSystemPropertyComponent item : this.property)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #property} (A property defines an additional slot through which additional information can be provided about a concept.)
     */
    // syntactic sugar
    public CodeSystemPropertyComponent addProperty() { //3
      CodeSystemPropertyComponent t = new CodeSystemPropertyComponent();
      if (this.property == null)
        this.property = new ArrayList<CodeSystemPropertyComponent>();
      this.property.add(t);
      return t;
    }

    // syntactic sugar
    public CodeSystem addProperty(CodeSystemPropertyComponent t) { //3
      if (t == null)
        return this;
      if (this.property == null)
        this.property = new ArrayList<CodeSystemPropertyComponent>();
      this.property.add(t);
      return this;
    }

    /**
     * @return {@link #concept} (Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be consulted to determine what the meaning of the hierarchical relationships are.)
     */
    public List<ConceptDefinitionComponent> getConcept() { 
      if (this.concept == null)
        this.concept = new ArrayList<ConceptDefinitionComponent>();
      return this.concept;
    }

    public boolean hasConcept() { 
      if (this.concept == null)
        return false;
      for (ConceptDefinitionComponent item : this.concept)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #concept} (Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be consulted to determine what the meaning of the hierarchical relationships are.)
     */
    // syntactic sugar
    public ConceptDefinitionComponent addConcept() { //3
      ConceptDefinitionComponent t = new ConceptDefinitionComponent();
      if (this.concept == null)
        this.concept = new ArrayList<ConceptDefinitionComponent>();
      this.concept.add(t);
      return t;
    }

    // syntactic sugar
    public CodeSystem addConcept(ConceptDefinitionComponent t) { //3
      if (t == null)
        return this;
      if (this.concept == null)
        this.concept = new ArrayList<ConceptDefinitionComponent>();
      this.concept.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this code system when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this code system is (or will be) published. This is used in [Coding]{datatypes.html#Coding}.system.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this code system when it is represented in other formats, or referenced in a specification, model, design or an instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "Used to identify this version of the code system when it is referenced in a specification, model, design or instance (e.g. Coding.version). This is an arbitrary value managed by the profile author manually and the value should be a timestamp. This is used in [Coding]{datatypes.html#Coding}.version.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name describing the code system.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the code system.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This CodeSystem was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the code system.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date that the code system status was last changed. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes (e.g. the 'content logical definition').", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the use of the code system - reason for definition, \"the semantic space\" to be included in the code system, conditions of use, etc. The description may include a list of expected usages for the code system and can also describe the approach taken to build the code system.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("requirements", "string", "Explains why this code system is needed and why it has been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the code system and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the code system.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("caseSensitive", "boolean", "If code comparison is case sensitive when codes within this system are compared to each other.", 0, java.lang.Integer.MAX_VALUE, caseSensitive));
        childrenList.add(new Property("compositional", "boolean", "True If code system defines a post-composition grammar.", 0, java.lang.Integer.MAX_VALUE, compositional));
        childrenList.add(new Property("versionNeeded", "boolean", "This flag is used to signify that the code system has not (or does not) maintain the definitions, and a version must be specified when referencing this code system.", 0, java.lang.Integer.MAX_VALUE, versionNeeded));
        childrenList.add(new Property("content", "code", "How much of the content of the code system - the concepts and codes it defines - are represented in this resource.", 0, java.lang.Integer.MAX_VALUE, content));
        childrenList.add(new Property("count", "unsignedInt", "The total number of concepts defined by the code system. Where the code system has a compositional grammar, the count refers to the number of base (primitive) concepts.", 0, java.lang.Integer.MAX_VALUE, count));
        childrenList.add(new Property("filter", "", "A filter that can be used in a value set compose statement when selecting concepts using a filter.", 0, java.lang.Integer.MAX_VALUE, filter));
        childrenList.add(new Property("property", "", "A property defines an additional slot through which additional information can be provided about a concept.", 0, java.lang.Integer.MAX_VALUE, property));
        childrenList.add(new Property("concept", "", "Concepts that are in the code system. The concept definitions are inherently hierarchical, but the definitions must be consulted to determine what the meaning of the hierarchical relationships are.", 0, java.lang.Integer.MAX_VALUE, concept));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new ConformanceResourceStatusEnumFactory().fromType(value); // Enumeration<ConformanceResourceStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add((CodeSystemContactComponent) value);
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
        else if (name.equals("caseSensitive"))
          this.caseSensitive = castToBoolean(value); // BooleanType
        else if (name.equals("compositional"))
          this.compositional = castToBoolean(value); // BooleanType
        else if (name.equals("versionNeeded"))
          this.versionNeeded = castToBoolean(value); // BooleanType
        else if (name.equals("content"))
          this.content = new CodeSystemContentModeEnumFactory().fromType(value); // Enumeration<CodeSystemContentMode>
        else if (name.equals("count"))
          this.count = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("filter"))
          this.getFilter().add((CodeSystemFilterComponent) value);
        else if (name.equals("property"))
          this.getProperty().add((CodeSystemPropertyComponent) value);
        else if (name.equals("concept"))
          this.getConcept().add((ConceptDefinitionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.url");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.experimental");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.date");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("requirements")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.requirements");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.copyright");
        }
        else if (name.equals("caseSensitive")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.caseSensitive");
        }
        else if (name.equals("compositional")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.compositional");
        }
        else if (name.equals("versionNeeded")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.versionNeeded");
        }
        else if (name.equals("content")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.content");
        }
        else if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type CodeSystem.count");
        }
        else if (name.equals("filter")) {
          return addFilter();
        }
        else if (name.equals("property")) {
          return addProperty();
        }
        else if (name.equals("concept")) {
          return addConcept();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CodeSystem";

  }

      public CodeSystem copy() {
        CodeSystem dst = new CodeSystem();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<CodeSystemContactComponent>();
          for (CodeSystemContactComponent i : contact)
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
        dst.caseSensitive = caseSensitive == null ? null : caseSensitive.copy();
        dst.compositional = compositional == null ? null : compositional.copy();
        dst.versionNeeded = versionNeeded == null ? null : versionNeeded.copy();
        dst.content = content == null ? null : content.copy();
        dst.count = count == null ? null : count.copy();
        if (filter != null) {
          dst.filter = new ArrayList<CodeSystemFilterComponent>();
          for (CodeSystemFilterComponent i : filter)
            dst.filter.add(i.copy());
        };
        if (property != null) {
          dst.property = new ArrayList<CodeSystemPropertyComponent>();
          for (CodeSystemPropertyComponent i : property)
            dst.property.add(i.copy());
        };
        if (concept != null) {
          dst.concept = new ArrayList<ConceptDefinitionComponent>();
          for (ConceptDefinitionComponent i : concept)
            dst.concept.add(i.copy());
        };
        return dst;
      }

      protected CodeSystem typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CodeSystem))
          return false;
        CodeSystem o = (CodeSystem) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true)
           && compareDeep(description, o.description, true) && compareDeep(useContext, o.useContext, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(caseSensitive, o.caseSensitive, true) && compareDeep(compositional, o.compositional, true)
           && compareDeep(versionNeeded, o.versionNeeded, true) && compareDeep(content, o.content, true) && compareDeep(count, o.count, true)
           && compareDeep(filter, o.filter, true) && compareDeep(property, o.property, true) && compareDeep(concept, o.concept, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CodeSystem))
          return false;
        CodeSystem o = (CodeSystem) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(requirements, o.requirements, true)
           && compareValues(copyright, o.copyright, true) && compareValues(caseSensitive, o.caseSensitive, true)
           && compareValues(compositional, o.compositional, true) && compareValues(versionNeeded, o.versionNeeded, true)
           && compareValues(content, o.content, true) && compareValues(count, o.count, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty()) && (description == null || description.isEmpty())
           && (useContext == null || useContext.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (caseSensitive == null || caseSensitive.isEmpty())
           && (compositional == null || compositional.isEmpty()) && (versionNeeded == null || versionNeeded.isEmpty())
           && (content == null || content.isEmpty()) && (count == null || count.isEmpty()) && (filter == null || filter.isEmpty())
           && (property == null || property.isEmpty()) && (concept == null || concept.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CodeSystem;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The code system publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CodeSystem.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="CodeSystem.date", description="The code system publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The code system publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CodeSystem.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identifier for the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="CodeSystem.identifier", description="The identifier for the code system", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identifier for the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>system</b>
   * <p>
   * Description: <b>The system for any codes defined by this code system (same as 'url')</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CodeSystem.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="system", path="CodeSystem.url", description="The system for any codes defined by this code system (same as 'url')", type="uri" )
  public static final String SP_SYSTEM = "system";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>system</b>
   * <p>
   * Description: <b>The system for any codes defined by this code system (same as 'url')</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CodeSystem.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam SYSTEM = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_SYSTEM);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>A code defined in the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.concept.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="CodeSystem.concept.code", description="A code defined in the code system", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A code defined in the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.concept.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>The name of the code system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CodeSystem.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="CodeSystem.name", description="The name of the code system", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>The name of the code system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CodeSystem.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.useContext</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="CodeSystem.useContext", description="A use context assigned to the code system", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.useContext</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the code system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CodeSystem.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="CodeSystem.publisher", description="Name of the publisher of the code system", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the code system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CodeSystem.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the code system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CodeSystem.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="CodeSystem.description", description="Text search in the description of the code system", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the code system</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CodeSystem.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The version identifier of the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="CodeSystem.version", description="The version identifier of the code system", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The version identifier of the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The logical URL for the code system</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CodeSystem.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="CodeSystem.url", description="The logical URL for the code system", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The logical URL for the code system</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CodeSystem.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CodeSystem.status", description="The status of the code system", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the code system</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CodeSystem.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

