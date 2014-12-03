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

// Generated on Tue, Dec 2, 2014 21:09+1100 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A reference to a document.
 */
@ResourceDef(name="DocumentReference", profile="http://hl7.org/fhir/Profile/DocumentReference")
public class DocumentReference extends DomainResource {

    public enum DocumentReferenceStatus {
        /**
         * This is the current reference for this document.
         */
        CURRENT, 
        /**
         * This reference has been superseded by another reference.
         */
        SUPERCEDED, 
        /**
         * This reference was created in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DocumentReferenceStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return CURRENT;
        if ("superceded".equals(codeString))
          return SUPERCEDED;
        if ("entered in error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown DocumentReferenceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CURRENT: return "current";
            case SUPERCEDED: return "superceded";
            case ENTEREDINERROR: return "entered in error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CURRENT: return "";
            case SUPERCEDED: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CURRENT: return "This is the current reference for this document.";
            case SUPERCEDED: return "This reference has been superseded by another reference.";
            case ENTEREDINERROR: return "This reference was created in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CURRENT: return "current";
            case SUPERCEDED: return "superceded";
            case ENTEREDINERROR: return "entered in error";
            default: return "?";
          }
        }
    }

  public static class DocumentReferenceStatusEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return DocumentReferenceStatus.CURRENT;
        if ("superceded".equals(codeString))
          return DocumentReferenceStatus.SUPERCEDED;
        if ("entered in error".equals(codeString))
          return DocumentReferenceStatus.ENTEREDINERROR;
        throw new Exception("Unknown DocumentReferenceStatus code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == DocumentReferenceStatus.CURRENT)
        return "current";
      if (code == DocumentReferenceStatus.SUPERCEDED)
        return "superceded";
      if (code == DocumentReferenceStatus.ENTEREDINERROR)
        return "entered in error";
      return "?";
      }
    }

    public enum DocumentRelationshipType {
        /**
         * This document logically replaces or supercedes the target document.
         */
        REPLACES, 
        /**
         * This document was generated by transforming the target document (e.g. format or language conversion).
         */
        TRANSFORMS, 
        /**
         * This document is a signature of the target document.
         */
        SIGNS, 
        /**
         * This document adds additional information to the target document.
         */
        APPENDS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DocumentRelationshipType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("transforms".equals(codeString))
          return TRANSFORMS;
        if ("signs".equals(codeString))
          return SIGNS;
        if ("appends".equals(codeString))
          return APPENDS;
        throw new Exception("Unknown DocumentRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REPLACES: return "replaces";
            case TRANSFORMS: return "transforms";
            case SIGNS: return "signs";
            case APPENDS: return "appends";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REPLACES: return "";
            case TRANSFORMS: return "";
            case SIGNS: return "";
            case APPENDS: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REPLACES: return "This document logically replaces or supercedes the target document.";
            case TRANSFORMS: return "This document was generated by transforming the target document (e.g. format or language conversion).";
            case SIGNS: return "This document is a signature of the target document.";
            case APPENDS: return "This document adds additional information to the target document.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REPLACES: return "replaces";
            case TRANSFORMS: return "transforms";
            case SIGNS: return "signs";
            case APPENDS: return "appends";
            default: return "?";
          }
        }
    }

  public static class DocumentRelationshipTypeEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replaces".equals(codeString))
          return DocumentRelationshipType.REPLACES;
        if ("transforms".equals(codeString))
          return DocumentRelationshipType.TRANSFORMS;
        if ("signs".equals(codeString))
          return DocumentRelationshipType.SIGNS;
        if ("appends".equals(codeString))
          return DocumentRelationshipType.APPENDS;
        throw new Exception("Unknown DocumentRelationshipType code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == DocumentRelationshipType.REPLACES)
        return "replaces";
      if (code == DocumentRelationshipType.TRANSFORMS)
        return "transforms";
      if (code == DocumentRelationshipType.SIGNS)
        return "signs";
      if (code == DocumentRelationshipType.APPENDS)
        return "appends";
      return "?";
      }
    }

    @Block()
    public static class DocumentReferenceRelatesToComponent extends BackboneElement {
        /**
         * The type of relationship that this document has with anther document.
         */
        @Child(name="code", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="replaces | transforms | signs | appends", formalDefinition="The type of relationship that this document has with anther document." )
        protected Enumeration<DocumentRelationshipType> code;

        /**
         * The target document of this relationship.
         */
        @Child(name="target", type={DocumentReference.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Target of the relationship", formalDefinition="The target document of this relationship." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (The target document of this relationship.)
         */
        protected DocumentReference targetTarget;

        private static final long serialVersionUID = -347257495L;

      public DocumentReferenceRelatesToComponent() {
        super();
      }

      public DocumentReferenceRelatesToComponent(Enumeration<DocumentRelationshipType> code, Reference target) {
        super();
        this.code = code;
        this.target = target;
      }

        /**
         * @return {@link #code} (The type of relationship that this document has with anther document.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<DocumentRelationshipType> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceRelatesToComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<DocumentRelationshipType>();
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The type of relationship that this document has with anther document.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public DocumentReferenceRelatesToComponent setCodeElement(Enumeration<DocumentRelationshipType> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The type of relationship that this document has with anther document.
         */
        public DocumentRelationshipType getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The type of relationship that this document has with anther document.
         */
        public DocumentReferenceRelatesToComponent setCode(DocumentRelationshipType value) { 
            if (this.code == null)
              this.code = new Enumeration<DocumentRelationshipType>();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #target} (The target document of this relationship.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceRelatesToComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference();
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (The target document of this relationship.)
         */
        public DocumentReferenceRelatesToComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The target document of this relationship.)
         */
        public DocumentReference getTargetTarget() { 
          if (this.targetTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceRelatesToComponent.target");
            else if (Configuration.doAutoCreate())
              this.targetTarget = new DocumentReference();
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The target document of this relationship.)
         */
        public DocumentReferenceRelatesToComponent setTargetTarget(DocumentReference value) { 
          this.targetTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "The type of relationship that this document has with anther document.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("target", "Reference(DocumentReference)", "The target document of this relationship.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      public DocumentReferenceRelatesToComponent copy() {
        DocumentReferenceRelatesToComponent dst = new DocumentReferenceRelatesToComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (target == null || target.isEmpty())
          ;
      }

  }

    @Block()
    public static class DocumentReferenceServiceComponent extends BackboneElement {
        /**
         * The type of the service that can be used to access the documents.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Type of service (i.e. XDS.b)", formalDefinition="The type of the service that can be used to access the documents." )
        protected CodeableConcept type;

        /**
         * Where the service end-point is located.
         */
        @Child(name="address", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Where service is located (usually a URL)", formalDefinition="Where the service end-point is located." )
        protected StringType address;

        /**
         * A list of named parameters that is used in the service call.
         */
        @Child(name="parameter", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Service call parameters", formalDefinition="A list of named parameters that is used in the service call." )
        protected List<DocumentReferenceServiceParameterComponent> parameter;

        private static final long serialVersionUID = 1797455740L;

      public DocumentReferenceServiceComponent() {
        super();
      }

      public DocumentReferenceServiceComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of the service that can be used to access the documents.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceServiceComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the service that can be used to access the documents.)
         */
        public DocumentReferenceServiceComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #address} (Where the service end-point is located.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
         */
        public StringType getAddressElement() { 
          if (this.address == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceServiceComponent.address");
            else if (Configuration.doAutoCreate())
              this.address = new StringType();
          return this.address;
        }

        public boolean hasAddressElement() { 
          return this.address != null && !this.address.isEmpty();
        }

        public boolean hasAddress() { 
          return this.address != null && !this.address.isEmpty();
        }

        /**
         * @param value {@link #address} (Where the service end-point is located.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
         */
        public DocumentReferenceServiceComponent setAddressElement(StringType value) { 
          this.address = value;
          return this;
        }

        /**
         * @return Where the service end-point is located.
         */
        public String getAddress() { 
          return this.address == null ? null : this.address.getValue();
        }

        /**
         * @param value Where the service end-point is located.
         */
        public DocumentReferenceServiceComponent setAddress(String value) { 
          if (Utilities.noString(value))
            this.address = null;
          else {
            if (this.address == null)
              this.address = new StringType();
            this.address.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #parameter} (A list of named parameters that is used in the service call.)
         */
        public List<DocumentReferenceServiceParameterComponent> getParameter() { 
          if (this.parameter == null)
            this.parameter = new ArrayList<DocumentReferenceServiceParameterComponent>();
          return this.parameter;
        }

        public boolean hasParameter() { 
          if (this.parameter == null)
            return false;
          for (DocumentReferenceServiceParameterComponent item : this.parameter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #parameter} (A list of named parameters that is used in the service call.)
         */
    // syntactic sugar
        public DocumentReferenceServiceParameterComponent addParameter() { //3
          DocumentReferenceServiceParameterComponent t = new DocumentReferenceServiceParameterComponent();
          if (this.parameter == null)
            this.parameter = new ArrayList<DocumentReferenceServiceParameterComponent>();
          this.parameter.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "The type of the service that can be used to access the documents.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("address", "string", "Where the service end-point is located.", 0, java.lang.Integer.MAX_VALUE, address));
          childrenList.add(new Property("parameter", "", "A list of named parameters that is used in the service call.", 0, java.lang.Integer.MAX_VALUE, parameter));
        }

      public DocumentReferenceServiceComponent copy() {
        DocumentReferenceServiceComponent dst = new DocumentReferenceServiceComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.address = address == null ? null : address.copy();
        if (parameter != null) {
          dst.parameter = new ArrayList<DocumentReferenceServiceParameterComponent>();
          for (DocumentReferenceServiceParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (address == null || address.isEmpty())
           && (parameter == null || parameter.isEmpty());
      }

  }

    @Block()
    public static class DocumentReferenceServiceParameterComponent extends BackboneElement {
        /**
         * The name of a parameter.
         */
        @Child(name="name", type={StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Parameter name in service call", formalDefinition="The name of a parameter." )
        protected StringType name;

        /**
         * The value of the named parameter.
         */
        @Child(name="value", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Parameter value for the name", formalDefinition="The value of the named parameter." )
        protected StringType value;

        private static final long serialVersionUID = 395259392L;

      public DocumentReferenceServiceParameterComponent() {
        super();
      }

      public DocumentReferenceServiceParameterComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The name of a parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceServiceParameterComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType();
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of a parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public DocumentReferenceServiceParameterComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of a parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of a parameter.
         */
        public DocumentReferenceServiceParameterComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the named parameter.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public StringType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceServiceParameterComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new StringType();
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the named parameter.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public DocumentReferenceServiceParameterComponent setValueElement(StringType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return The value of the named parameter.
         */
        public String getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value The value of the named parameter.
         */
        public DocumentReferenceServiceParameterComponent setValue(String value) { 
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
          childrenList.add(new Property("name", "string", "The name of a parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("value", "string", "The value of the named parameter.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public DocumentReferenceServiceParameterComponent copy() {
        DocumentReferenceServiceParameterComponent dst = new DocumentReferenceServiceParameterComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  }

    @Block()
    public static class DocumentReferenceContextComponent extends BackboneElement {
        /**
         * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.
         */
        @Child(name="event", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Main Clinical Acts Documented", formalDefinition="This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a 'History and Physical Report' in which the procedure being documented is necessarily a 'History and Physical' act." )
        protected List<CodeableConcept> event;

        /**
         * The time period over which the service that is described by the document was provided.
         */
        @Child(name="period", type={Period.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Time of service that is being documented", formalDefinition="The time period over which the service that is described by the document was provided." )
        protected Period period;

        /**
         * The kind of facility where the patient was seen.
         */
        @Child(name="facilityType", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Kind of facility where patient was seen", formalDefinition="The kind of facility where the patient was seen." )
        protected CodeableConcept facilityType;

        private static final long serialVersionUID = -1762960949L;

      public DocumentReferenceContextComponent() {
        super();
      }

        /**
         * @return {@link #event} (This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.)
         */
        public List<CodeableConcept> getEvent() { 
          if (this.event == null)
            this.event = new ArrayList<CodeableConcept>();
          return this.event;
        }

        public boolean hasEvent() { 
          if (this.event == null)
            return false;
          for (CodeableConcept item : this.event)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #event} (This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.)
         */
    // syntactic sugar
        public CodeableConcept addEvent() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.event == null)
            this.event = new ArrayList<CodeableConcept>();
          this.event.add(t);
          return t;
        }

        /**
         * @return {@link #period} (The time period over which the service that is described by the document was provided.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceContextComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period();
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The time period over which the service that is described by the document was provided.)
         */
        public DocumentReferenceContextComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #facilityType} (The kind of facility where the patient was seen.)
         */
        public CodeableConcept getFacilityType() { 
          if (this.facilityType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentReferenceContextComponent.facilityType");
            else if (Configuration.doAutoCreate())
              this.facilityType = new CodeableConcept();
          return this.facilityType;
        }

        public boolean hasFacilityType() { 
          return this.facilityType != null && !this.facilityType.isEmpty();
        }

        /**
         * @param value {@link #facilityType} (The kind of facility where the patient was seen.)
         */
        public DocumentReferenceContextComponent setFacilityType(CodeableConcept value) { 
          this.facilityType = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("event", "CodeableConcept", "This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a 'History and Physical Report' in which the procedure being documented is necessarily a 'History and Physical' act.", 0, java.lang.Integer.MAX_VALUE, event));
          childrenList.add(new Property("period", "Period", "The time period over which the service that is described by the document was provided.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("facilityType", "CodeableConcept", "The kind of facility where the patient was seen.", 0, java.lang.Integer.MAX_VALUE, facilityType));
        }

      public DocumentReferenceContextComponent copy() {
        DocumentReferenceContextComponent dst = new DocumentReferenceContextComponent();
        copyValues(dst);
        if (event != null) {
          dst.event = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : event)
            dst.event.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        dst.facilityType = facilityType == null ? null : facilityType.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (event == null || event.isEmpty()) && (period == null || period.isEmpty())
           && (facilityType == null || facilityType.isEmpty());
      }

  }

    /**
     * Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document.
     */
    @Child(name="masterIdentifier", type={Identifier.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="Master Version Specific Identifier", formalDefinition="Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document." )
    protected Identifier masterIdentifier;

    /**
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers.
     */
    @Child(name="identifier", type={Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Other identifiers for the document", formalDefinition="Other identifiers associated with the document, including version independent, source record and workflow related identifiers." )
    protected List<Identifier> identifier;

    /**
     * Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure).
     */
    @Child(name="subject", type={Patient.class, Practitioner.class, Group.class, Device.class}, order=1, min=1, max=1)
    @Description(shortDefinition="Who|what is the subject of the document", formalDefinition="Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure).)
     */
    protected Resource subjectTarget;

    /**
     * Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.).
     */
    @Child(name="type", type={CodeableConcept.class}, order=2, min=1, max=1)
    @Description(shortDefinition="What kind of document this is (LOINC if possible)", formalDefinition="Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.)." )
    protected CodeableConcept type;

    /**
     * A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type.
     */
    @Child(name="class_", type={CodeableConcept.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Categorization of Document", formalDefinition="A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type." )
    protected CodeableConcept class_;

    /**
     * Identifies who is responsible for adding the information to the document.
     */
    @Child(name="author", type={Practitioner.class, Device.class, Patient.class, RelatedPerson.class}, order=4, min=1, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who and/or what authored the document", formalDefinition="Identifies who is responsible for adding the information to the document." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies who is responsible for adding the information to the document.)
     */
    protected List<Resource> authorTarget;


    /**
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.
     */
    @Child(name="custodian", type={Organization.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Org which maintains the document", formalDefinition="Identifies the organization or group who is responsible for ongoing maintenance of and access to the document." )
    protected Reference custodian;

    /**
     * The actual object that is the target of the reference (Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.)
     */
    protected Organization custodianTarget;

    /**
     * A reference to a domain or server that manages policies under which the document is accessed and/or made available.
     */
    @Child(name="policyManager", type={UriType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Manages access policies for the document", formalDefinition="A reference to a domain or server that manages policies under which the document is accessed and/or made available." )
    protected UriType policyManager;

    /**
     * Which person or organization authenticates that this document is valid.
     */
    @Child(name="authenticator", type={Practitioner.class, Organization.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Who/What authenticated the document", formalDefinition="Which person or organization authenticates that this document is valid." )
    protected Reference authenticator;

    /**
     * The actual object that is the target of the reference (Which person or organization authenticates that this document is valid.)
     */
    protected Resource authenticatorTarget;

    /**
     * When the document was created.
     */
    @Child(name="created", type={DateTimeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Document creation time", formalDefinition="When the document was created." )
    protected DateTimeType created;

    /**
     * When the document reference was created.
     */
    @Child(name="indexed", type={InstantType.class}, order=9, min=1, max=1)
    @Description(shortDefinition="When this document reference created", formalDefinition="When the document reference was created." )
    protected InstantType indexed;

    /**
     * The status of this document reference.
     */
    @Child(name="status", type={CodeType.class}, order=10, min=1, max=1)
    @Description(shortDefinition="current | superceded | entered in error", formalDefinition="The status of this document reference." )
    protected Enumeration<DocumentReferenceStatus> status;

    /**
     * The status of the underlying document.
     */
    @Child(name="docStatus", type={CodeableConcept.class}, order=11, min=0, max=1)
    @Description(shortDefinition="preliminary | final | appended | amended | entered in error", formalDefinition="The status of the underlying document." )
    protected CodeableConcept docStatus;

    /**
     * Relationships that this document has with other document references that already exist.
     */
    @Child(name="relatesTo", type={}, order=12, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Relationships to other documents", formalDefinition="Relationships that this document has with other document references that already exist." )
    protected List<DocumentReferenceRelatesToComponent> relatesTo;

    /**
     * Human-readable description of the source document. This is sometimes known as the "title".
     */
    @Child(name="description", type={StringType.class}, order=13, min=0, max=1)
    @Description(shortDefinition="Human-readable description (title)", formalDefinition="Human-readable description of the source document. This is sometimes known as the 'title'." )
    protected StringType description;

    /**
     * A code specifying the level of confidentiality of the XDS Document.
     */
    @Child(name="confidentiality", type={CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Sensitivity of source document", formalDefinition="A code specifying the level of confidentiality of the XDS Document." )
    protected List<CodeableConcept> confidentiality;

    /**
     * The primary language in which the source document is written.
     */
    @Child(name="primaryLanguage", type={CodeType.class}, order=15, min=0, max=1)
    @Description(shortDefinition="The marked primary language for the document", formalDefinition="The primary language in which the source document is written." )
    protected CodeType primaryLanguage;

    /**
     * The mime type of the source document.
     */
    @Child(name="mimeType", type={CodeType.class}, order=16, min=1, max=1)
    @Description(shortDefinition="Mime type, + maybe character encoding", formalDefinition="The mime type of the source document." )
    protected CodeType mimeType;

    /**
     * An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType.
     */
    @Child(name="format", type={UriType.class}, order=17, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Format/content rules for the document", formalDefinition="An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType." )
    protected List<UriType> format;

    /**
     * The size of the source document this reference refers to in bytes.
     */
    @Child(name="size", type={IntegerType.class}, order=18, min=0, max=1)
    @Description(shortDefinition="Size of the document in bytes", formalDefinition="The size of the source document this reference refers to in bytes." )
    protected IntegerType size;

    /**
     * A hash of the source document to ensure that changes have not occurred.
     */
    @Child(name="hash", type={Base64BinaryType.class}, order=19, min=0, max=1)
    @Description(shortDefinition="Base64 representation of SHA1", formalDefinition="A hash of the source document to ensure that changes have not occurred." )
    protected Base64BinaryType hash;

    /**
     * A url at which the document can be accessed.
     */
    @Child(name="location", type={UriType.class}, order=20, min=0, max=1)
    @Description(shortDefinition="Where to access the document", formalDefinition="A url at which the document can be accessed." )
    protected UriType location;

    /**
     * A description of a service call that can be used to retrieve the document.
     */
    @Child(name="service", type={}, order=21, min=0, max=1)
    @Description(shortDefinition="If access is not fully described by location", formalDefinition="A description of a service call that can be used to retrieve the document." )
    protected DocumentReferenceServiceComponent service;

    /**
     * The clinical context in which the document was prepared.
     */
    @Child(name="context", type={}, order=22, min=0, max=1)
    @Description(shortDefinition="Clinical context of document", formalDefinition="The clinical context in which the document was prepared." )
    protected DocumentReferenceContextComponent context;

    private static final long serialVersionUID = -752030368L;

    public DocumentReference() {
      super();
    }

    public DocumentReference(Identifier masterIdentifier, Reference subject, CodeableConcept type, InstantType indexed, Enumeration<DocumentReferenceStatus> status, CodeType mimeType) {
      super();
      this.masterIdentifier = masterIdentifier;
      this.subject = subject;
      this.type = type;
      this.indexed = indexed;
      this.status = status;
      this.mimeType = mimeType;
    }

    /**
     * @return {@link #masterIdentifier} (Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document.)
     */
    public Identifier getMasterIdentifier() { 
      if (this.masterIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.masterIdentifier");
        else if (Configuration.doAutoCreate())
          this.masterIdentifier = new Identifier();
      return this.masterIdentifier;
    }

    public boolean hasMasterIdentifier() { 
      return this.masterIdentifier != null && !this.masterIdentifier.isEmpty();
    }

    /**
     * @param value {@link #masterIdentifier} (Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document.)
     */
    public DocumentReference setMasterIdentifier(Identifier value) { 
      this.masterIdentifier = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Other identifiers associated with the document, including version independent, source record and workflow related identifiers.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Other identifiers associated with the document, including version independent, source record and workflow related identifiers.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #subject} (Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure).)
     */
    public DocumentReference setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure).)
     */
    public DocumentReference setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #type} (Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.).)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept();
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.).)
     */
    public DocumentReference setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #class_} (A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type.)
     */
    public CodeableConcept getClass_() { 
      if (this.class_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.class_");
        else if (Configuration.doAutoCreate())
          this.class_ = new CodeableConcept();
      return this.class_;
    }

    public boolean hasClass_() { 
      return this.class_ != null && !this.class_.isEmpty();
    }

    /**
     * @param value {@link #class_} (A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type.)
     */
    public DocumentReference setClass_(CodeableConcept value) { 
      this.class_ = value;
      return this;
    }

    /**
     * @return {@link #author} (Identifies who is responsible for adding the information to the document.)
     */
    public List<Reference> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      return this.author;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #author} (Identifies who is responsible for adding the information to the document.)
     */
    // syntactic sugar
    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    /**
     * @return {@link #author} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies who is responsible for adding the information to the document.)
     */
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #custodian} (Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.)
     */
    public Reference getCustodian() { 
      if (this.custodian == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.custodian");
        else if (Configuration.doAutoCreate())
          this.custodian = new Reference();
      return this.custodian;
    }

    public boolean hasCustodian() { 
      return this.custodian != null && !this.custodian.isEmpty();
    }

    /**
     * @param value {@link #custodian} (Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.)
     */
    public DocumentReference setCustodian(Reference value) { 
      this.custodian = value;
      return this;
    }

    /**
     * @return {@link #custodian} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.)
     */
    public Organization getCustodianTarget() { 
      if (this.custodianTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.custodian");
        else if (Configuration.doAutoCreate())
          this.custodianTarget = new Organization();
      return this.custodianTarget;
    }

    /**
     * @param value {@link #custodian} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.)
     */
    public DocumentReference setCustodianTarget(Organization value) { 
      this.custodianTarget = value;
      return this;
    }

    /**
     * @return {@link #policyManager} (A reference to a domain or server that manages policies under which the document is accessed and/or made available.). This is the underlying object with id, value and extensions. The accessor "getPolicyManager" gives direct access to the value
     */
    public UriType getPolicyManagerElement() { 
      if (this.policyManager == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.policyManager");
        else if (Configuration.doAutoCreate())
          this.policyManager = new UriType();
      return this.policyManager;
    }

    public boolean hasPolicyManagerElement() { 
      return this.policyManager != null && !this.policyManager.isEmpty();
    }

    public boolean hasPolicyManager() { 
      return this.policyManager != null && !this.policyManager.isEmpty();
    }

    /**
     * @param value {@link #policyManager} (A reference to a domain or server that manages policies under which the document is accessed and/or made available.). This is the underlying object with id, value and extensions. The accessor "getPolicyManager" gives direct access to the value
     */
    public DocumentReference setPolicyManagerElement(UriType value) { 
      this.policyManager = value;
      return this;
    }

    /**
     * @return A reference to a domain or server that manages policies under which the document is accessed and/or made available.
     */
    public String getPolicyManager() { 
      return this.policyManager == null ? null : this.policyManager.getValue();
    }

    /**
     * @param value A reference to a domain or server that manages policies under which the document is accessed and/or made available.
     */
    public DocumentReference setPolicyManager(String value) { 
      if (Utilities.noString(value))
        this.policyManager = null;
      else {
        if (this.policyManager == null)
          this.policyManager = new UriType();
        this.policyManager.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #authenticator} (Which person or organization authenticates that this document is valid.)
     */
    public Reference getAuthenticator() { 
      if (this.authenticator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.authenticator");
        else if (Configuration.doAutoCreate())
          this.authenticator = new Reference();
      return this.authenticator;
    }

    public boolean hasAuthenticator() { 
      return this.authenticator != null && !this.authenticator.isEmpty();
    }

    /**
     * @param value {@link #authenticator} (Which person or organization authenticates that this document is valid.)
     */
    public DocumentReference setAuthenticator(Reference value) { 
      this.authenticator = value;
      return this;
    }

    /**
     * @return {@link #authenticator} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Which person or organization authenticates that this document is valid.)
     */
    public Resource getAuthenticatorTarget() { 
      return this.authenticatorTarget;
    }

    /**
     * @param value {@link #authenticator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Which person or organization authenticates that this document is valid.)
     */
    public DocumentReference setAuthenticatorTarget(Resource value) { 
      this.authenticatorTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (When the document was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType();
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (When the document was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DocumentReference setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return When the document was created.
     */
    public DateAndTime getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value When the document was created.
     */
    public DocumentReference setCreated(DateAndTime value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #indexed} (When the document reference was created.). This is the underlying object with id, value and extensions. The accessor "getIndexed" gives direct access to the value
     */
    public InstantType getIndexedElement() { 
      if (this.indexed == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.indexed");
        else if (Configuration.doAutoCreate())
          this.indexed = new InstantType();
      return this.indexed;
    }

    public boolean hasIndexedElement() { 
      return this.indexed != null && !this.indexed.isEmpty();
    }

    public boolean hasIndexed() { 
      return this.indexed != null && !this.indexed.isEmpty();
    }

    /**
     * @param value {@link #indexed} (When the document reference was created.). This is the underlying object with id, value and extensions. The accessor "getIndexed" gives direct access to the value
     */
    public DocumentReference setIndexedElement(InstantType value) { 
      this.indexed = value;
      return this;
    }

    /**
     * @return When the document reference was created.
     */
    public DateAndTime getIndexed() { 
      return this.indexed == null ? null : this.indexed.getValue();
    }

    /**
     * @param value When the document reference was created.
     */
    public DocumentReference setIndexed(DateAndTime value) { 
        if (this.indexed == null)
          this.indexed = new InstantType();
        this.indexed.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this document reference.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DocumentReferenceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DocumentReferenceStatus>();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this document reference.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DocumentReference setStatusElement(Enumeration<DocumentReferenceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this document reference.
     */
    public DocumentReferenceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this document reference.
     */
    public DocumentReference setStatus(DocumentReferenceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<DocumentReferenceStatus>();
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #docStatus} (The status of the underlying document.)
     */
    public CodeableConcept getDocStatus() { 
      if (this.docStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.docStatus");
        else if (Configuration.doAutoCreate())
          this.docStatus = new CodeableConcept();
      return this.docStatus;
    }

    public boolean hasDocStatus() { 
      return this.docStatus != null && !this.docStatus.isEmpty();
    }

    /**
     * @param value {@link #docStatus} (The status of the underlying document.)
     */
    public DocumentReference setDocStatus(CodeableConcept value) { 
      this.docStatus = value;
      return this;
    }

    /**
     * @return {@link #relatesTo} (Relationships that this document has with other document references that already exist.)
     */
    public List<DocumentReferenceRelatesToComponent> getRelatesTo() { 
      if (this.relatesTo == null)
        this.relatesTo = new ArrayList<DocumentReferenceRelatesToComponent>();
      return this.relatesTo;
    }

    public boolean hasRelatesTo() { 
      if (this.relatesTo == null)
        return false;
      for (DocumentReferenceRelatesToComponent item : this.relatesTo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #relatesTo} (Relationships that this document has with other document references that already exist.)
     */
    // syntactic sugar
    public DocumentReferenceRelatesToComponent addRelatesTo() { //3
      DocumentReferenceRelatesToComponent t = new DocumentReferenceRelatesToComponent();
      if (this.relatesTo == null)
        this.relatesTo = new ArrayList<DocumentReferenceRelatesToComponent>();
      this.relatesTo.add(t);
      return t;
    }

    /**
     * @return {@link #description} (Human-readable description of the source document. This is sometimes known as the "title".). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType();
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Human-readable description of the source document. This is sometimes known as the "title".). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public DocumentReference setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Human-readable description of the source document. This is sometimes known as the "title".
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Human-readable description of the source document. This is sometimes known as the "title".
     */
    public DocumentReference setDescription(String value) { 
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
     * @return {@link #confidentiality} (A code specifying the level of confidentiality of the XDS Document.)
     */
    public List<CodeableConcept> getConfidentiality() { 
      if (this.confidentiality == null)
        this.confidentiality = new ArrayList<CodeableConcept>();
      return this.confidentiality;
    }

    public boolean hasConfidentiality() { 
      if (this.confidentiality == null)
        return false;
      for (CodeableConcept item : this.confidentiality)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #confidentiality} (A code specifying the level of confidentiality of the XDS Document.)
     */
    // syntactic sugar
    public CodeableConcept addConfidentiality() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.confidentiality == null)
        this.confidentiality = new ArrayList<CodeableConcept>();
      this.confidentiality.add(t);
      return t;
    }

    /**
     * @return {@link #primaryLanguage} (The primary language in which the source document is written.). This is the underlying object with id, value and extensions. The accessor "getPrimaryLanguage" gives direct access to the value
     */
    public CodeType getPrimaryLanguageElement() { 
      if (this.primaryLanguage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.primaryLanguage");
        else if (Configuration.doAutoCreate())
          this.primaryLanguage = new CodeType();
      return this.primaryLanguage;
    }

    public boolean hasPrimaryLanguageElement() { 
      return this.primaryLanguage != null && !this.primaryLanguage.isEmpty();
    }

    public boolean hasPrimaryLanguage() { 
      return this.primaryLanguage != null && !this.primaryLanguage.isEmpty();
    }

    /**
     * @param value {@link #primaryLanguage} (The primary language in which the source document is written.). This is the underlying object with id, value and extensions. The accessor "getPrimaryLanguage" gives direct access to the value
     */
    public DocumentReference setPrimaryLanguageElement(CodeType value) { 
      this.primaryLanguage = value;
      return this;
    }

    /**
     * @return The primary language in which the source document is written.
     */
    public String getPrimaryLanguage() { 
      return this.primaryLanguage == null ? null : this.primaryLanguage.getValue();
    }

    /**
     * @param value The primary language in which the source document is written.
     */
    public DocumentReference setPrimaryLanguage(String value) { 
      if (Utilities.noString(value))
        this.primaryLanguage = null;
      else {
        if (this.primaryLanguage == null)
          this.primaryLanguage = new CodeType();
        this.primaryLanguage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #mimeType} (The mime type of the source document.). This is the underlying object with id, value and extensions. The accessor "getMimeType" gives direct access to the value
     */
    public CodeType getMimeTypeElement() { 
      if (this.mimeType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.mimeType");
        else if (Configuration.doAutoCreate())
          this.mimeType = new CodeType();
      return this.mimeType;
    }

    public boolean hasMimeTypeElement() { 
      return this.mimeType != null && !this.mimeType.isEmpty();
    }

    public boolean hasMimeType() { 
      return this.mimeType != null && !this.mimeType.isEmpty();
    }

    /**
     * @param value {@link #mimeType} (The mime type of the source document.). This is the underlying object with id, value and extensions. The accessor "getMimeType" gives direct access to the value
     */
    public DocumentReference setMimeTypeElement(CodeType value) { 
      this.mimeType = value;
      return this;
    }

    /**
     * @return The mime type of the source document.
     */
    public String getMimeType() { 
      return this.mimeType == null ? null : this.mimeType.getValue();
    }

    /**
     * @param value The mime type of the source document.
     */
    public DocumentReference setMimeType(String value) { 
        if (this.mimeType == null)
          this.mimeType = new CodeType();
        this.mimeType.setValue(value);
      return this;
    }

    /**
     * @return {@link #format} (An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType.)
     */
    public List<UriType> getFormat() { 
      if (this.format == null)
        this.format = new ArrayList<UriType>();
      return this.format;
    }

    public boolean hasFormat() { 
      if (this.format == null)
        return false;
      for (UriType item : this.format)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #format} (An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType.)
     */
    // syntactic sugar
    public UriType addFormatElement() {//2 
      UriType t = new UriType();
      if (this.format == null)
        this.format = new ArrayList<UriType>();
      this.format.add(t);
      return t;
    }

    /**
     * @param value {@link #format} (An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType.)
     */
    public DocumentReference addFormat(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.format == null)
        this.format = new ArrayList<UriType>();
      this.format.add(t);
      return this;
    }

    /**
     * @param value {@link #format} (An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType.)
     */
    public boolean hasFormat(String value) { 
      if (this.format == null)
        return false;
      for (UriType v : this.format)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #size} (The size of the source document this reference refers to in bytes.). This is the underlying object with id, value and extensions. The accessor "getSize" gives direct access to the value
     */
    public IntegerType getSizeElement() { 
      if (this.size == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.size");
        else if (Configuration.doAutoCreate())
          this.size = new IntegerType();
      return this.size;
    }

    public boolean hasSizeElement() { 
      return this.size != null && !this.size.isEmpty();
    }

    public boolean hasSize() { 
      return this.size != null && !this.size.isEmpty();
    }

    /**
     * @param value {@link #size} (The size of the source document this reference refers to in bytes.). This is the underlying object with id, value and extensions. The accessor "getSize" gives direct access to the value
     */
    public DocumentReference setSizeElement(IntegerType value) { 
      this.size = value;
      return this;
    }

    /**
     * @return The size of the source document this reference refers to in bytes.
     */
    public int getSize() { 
      return this.size == null ? null : this.size.getValue();
    }

    /**
     * @param value The size of the source document this reference refers to in bytes.
     */
    public DocumentReference setSize(int value) { 
      if (value == -1)
        this.size = null;
      else {
        if (this.size == null)
          this.size = new IntegerType();
        this.size.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #hash} (A hash of the source document to ensure that changes have not occurred.). This is the underlying object with id, value and extensions. The accessor "getHash" gives direct access to the value
     */
    public Base64BinaryType getHashElement() { 
      if (this.hash == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.hash");
        else if (Configuration.doAutoCreate())
          this.hash = new Base64BinaryType();
      return this.hash;
    }

    public boolean hasHashElement() { 
      return this.hash != null && !this.hash.isEmpty();
    }

    public boolean hasHash() { 
      return this.hash != null && !this.hash.isEmpty();
    }

    /**
     * @param value {@link #hash} (A hash of the source document to ensure that changes have not occurred.). This is the underlying object with id, value and extensions. The accessor "getHash" gives direct access to the value
     */
    public DocumentReference setHashElement(Base64BinaryType value) { 
      this.hash = value;
      return this;
    }

    /**
     * @return A hash of the source document to ensure that changes have not occurred.
     */
    public byte[] getHash() { 
      return this.hash == null ? null : this.hash.getValue();
    }

    /**
     * @param value A hash of the source document to ensure that changes have not occurred.
     */
    public DocumentReference setHash(byte[] value) { 
      if (value == null)
        this.hash = null;
      else {
        if (this.hash == null)
          this.hash = new Base64BinaryType();
        this.hash.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #location} (A url at which the document can be accessed.). This is the underlying object with id, value and extensions. The accessor "getLocation" gives direct access to the value
     */
    public UriType getLocationElement() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.location");
        else if (Configuration.doAutoCreate())
          this.location = new UriType();
      return this.location;
    }

    public boolean hasLocationElement() { 
      return this.location != null && !this.location.isEmpty();
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (A url at which the document can be accessed.). This is the underlying object with id, value and extensions. The accessor "getLocation" gives direct access to the value
     */
    public DocumentReference setLocationElement(UriType value) { 
      this.location = value;
      return this;
    }

    /**
     * @return A url at which the document can be accessed.
     */
    public String getLocation() { 
      return this.location == null ? null : this.location.getValue();
    }

    /**
     * @param value A url at which the document can be accessed.
     */
    public DocumentReference setLocation(String value) { 
      if (Utilities.noString(value))
        this.location = null;
      else {
        if (this.location == null)
          this.location = new UriType();
        this.location.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #service} (A description of a service call that can be used to retrieve the document.)
     */
    public DocumentReferenceServiceComponent getService() { 
      if (this.service == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.service");
        else if (Configuration.doAutoCreate())
          this.service = new DocumentReferenceServiceComponent();
      return this.service;
    }

    public boolean hasService() { 
      return this.service != null && !this.service.isEmpty();
    }

    /**
     * @param value {@link #service} (A description of a service call that can be used to retrieve the document.)
     */
    public DocumentReference setService(DocumentReferenceServiceComponent value) { 
      this.service = value;
      return this;
    }

    /**
     * @return {@link #context} (The clinical context in which the document was prepared.)
     */
    public DocumentReferenceContextComponent getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentReference.context");
        else if (Configuration.doAutoCreate())
          this.context = new DocumentReferenceContextComponent();
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The clinical context in which the document was prepared.)
     */
    public DocumentReference setContext(DocumentReferenceContextComponent value) { 
      this.context = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("masterIdentifier", "Identifier", "Document identifier as assigned by the source of the document. This identifier is specific to this version of the document. This unique identifier may be used elsewhere to identify this version of the document.", 0, java.lang.Integer.MAX_VALUE, masterIdentifier));
        childrenList.add(new Property("identifier", "Identifier", "Other identifiers associated with the document, including version independent, source record and workflow related identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device)", "Who or what the document is about. The document can be about a person, (patient or healthcare practitioner), a device (I.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("type", "CodeableConcept", "Specifies the particular kind of document (e.g. Patient Summary, Discharge Summary, Prescription, etc.).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("class", "CodeableConcept", "A categorization for the type of the document. This may be implied by or derived from the code specified in the Document Type.", 0, java.lang.Integer.MAX_VALUE, class_));
        childrenList.add(new Property("author", "Reference(Practitioner|Device|Patient|RelatedPerson)", "Identifies who is responsible for adding the information to the document.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("custodian", "Reference(Organization)", "Identifies the organization or group who is responsible for ongoing maintenance of and access to the document.", 0, java.lang.Integer.MAX_VALUE, custodian));
        childrenList.add(new Property("policyManager", "uri", "A reference to a domain or server that manages policies under which the document is accessed and/or made available.", 0, java.lang.Integer.MAX_VALUE, policyManager));
        childrenList.add(new Property("authenticator", "Reference(Practitioner|Organization)", "Which person or organization authenticates that this document is valid.", 0, java.lang.Integer.MAX_VALUE, authenticator));
        childrenList.add(new Property("created", "dateTime", "When the document was created.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("indexed", "instant", "When the document reference was created.", 0, java.lang.Integer.MAX_VALUE, indexed));
        childrenList.add(new Property("status", "code", "The status of this document reference.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("docStatus", "CodeableConcept", "The status of the underlying document.", 0, java.lang.Integer.MAX_VALUE, docStatus));
        childrenList.add(new Property("relatesTo", "", "Relationships that this document has with other document references that already exist.", 0, java.lang.Integer.MAX_VALUE, relatesTo));
        childrenList.add(new Property("description", "string", "Human-readable description of the source document. This is sometimes known as the 'title'.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("confidentiality", "CodeableConcept", "A code specifying the level of confidentiality of the XDS Document.", 0, java.lang.Integer.MAX_VALUE, confidentiality));
        childrenList.add(new Property("primaryLanguage", "code", "The primary language in which the source document is written.", 0, java.lang.Integer.MAX_VALUE, primaryLanguage));
        childrenList.add(new Property("mimeType", "code", "The mime type of the source document.", 0, java.lang.Integer.MAX_VALUE, mimeType));
        childrenList.add(new Property("format", "uri", "An identifier that identifies that the format and content of the document conforms to additional rules beyond the base format indicated in the mimeType.", 0, java.lang.Integer.MAX_VALUE, format));
        childrenList.add(new Property("size", "integer", "The size of the source document this reference refers to in bytes.", 0, java.lang.Integer.MAX_VALUE, size));
        childrenList.add(new Property("hash", "base64Binary", "A hash of the source document to ensure that changes have not occurred.", 0, java.lang.Integer.MAX_VALUE, hash));
        childrenList.add(new Property("location", "uri", "A url at which the document can be accessed.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("service", "", "A description of a service call that can be used to retrieve the document.", 0, java.lang.Integer.MAX_VALUE, service));
        childrenList.add(new Property("context", "", "The clinical context in which the document was prepared.", 0, java.lang.Integer.MAX_VALUE, context));
      }

      public DocumentReference copy() {
        DocumentReference dst = new DocumentReference();
        copyValues(dst);
        dst.masterIdentifier = masterIdentifier == null ? null : masterIdentifier.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.type = type == null ? null : type.copy();
        dst.class_ = class_ == null ? null : class_.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        dst.custodian = custodian == null ? null : custodian.copy();
        dst.policyManager = policyManager == null ? null : policyManager.copy();
        dst.authenticator = authenticator == null ? null : authenticator.copy();
        dst.created = created == null ? null : created.copy();
        dst.indexed = indexed == null ? null : indexed.copy();
        dst.status = status == null ? null : status.copy();
        dst.docStatus = docStatus == null ? null : docStatus.copy();
        if (relatesTo != null) {
          dst.relatesTo = new ArrayList<DocumentReferenceRelatesToComponent>();
          for (DocumentReferenceRelatesToComponent i : relatesTo)
            dst.relatesTo.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (confidentiality != null) {
          dst.confidentiality = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : confidentiality)
            dst.confidentiality.add(i.copy());
        };
        dst.primaryLanguage = primaryLanguage == null ? null : primaryLanguage.copy();
        dst.mimeType = mimeType == null ? null : mimeType.copy();
        if (format != null) {
          dst.format = new ArrayList<UriType>();
          for (UriType i : format)
            dst.format.add(i.copy());
        };
        dst.size = size == null ? null : size.copy();
        dst.hash = hash == null ? null : hash.copy();
        dst.location = location == null ? null : location.copy();
        dst.service = service == null ? null : service.copy();
        dst.context = context == null ? null : context.copy();
        return dst;
      }

      protected DocumentReference typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (masterIdentifier == null || masterIdentifier.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (subject == null || subject.isEmpty()) && (type == null || type.isEmpty()) && (class_ == null || class_.isEmpty())
           && (author == null || author.isEmpty()) && (custodian == null || custodian.isEmpty()) && (policyManager == null || policyManager.isEmpty())
           && (authenticator == null || authenticator.isEmpty()) && (created == null || created.isEmpty())
           && (indexed == null || indexed.isEmpty()) && (status == null || status.isEmpty()) && (docStatus == null || docStatus.isEmpty())
           && (relatesTo == null || relatesTo.isEmpty()) && (description == null || description.isEmpty())
           && (confidentiality == null || confidentiality.isEmpty()) && (primaryLanguage == null || primaryLanguage.isEmpty())
           && (mimeType == null || mimeType.isEmpty()) && (format == null || format.isEmpty()) && (size == null || size.isEmpty())
           && (hash == null || hash.isEmpty()) && (location == null || location.isEmpty()) && (service == null || service.isEmpty())
           && (context == null || context.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DocumentReference;
   }

  @SearchParamDefinition(name="location", path="DocumentReference.location", description="Where to access the document", type="string" )
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name="indexed", path="DocumentReference.indexed", description="When this document reference created", type="date" )
  public static final String SP_INDEXED = "indexed";
  @SearchParamDefinition(name="status", path="DocumentReference.status", description="current | superceded | entered in error", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="subject", path="DocumentReference.subject", description="Who|what is the subject of the document", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="relatesto", path="DocumentReference.relatesTo.target", description="Target of the relationship", type="reference" )
  public static final String SP_RELATESTO = "relatesto";
  @SearchParamDefinition(name="relation", path="DocumentReference.relatesTo.code", description="replaces | transforms | signs | appends", type="token" )
  public static final String SP_RELATION = "relation";
  @SearchParamDefinition(name="class", path="DocumentReference.class", description="Categorization of Document", type="token" )
  public static final String SP_CLASS = "class";
  @SearchParamDefinition(name="format", path="DocumentReference.format", description="Format/content rules for the document", type="token" )
  public static final String SP_FORMAT = "format";
  @SearchParamDefinition(name="period", path="DocumentReference.context.period", description="Time of service that is being documented", type="date" )
  public static final String SP_PERIOD = "period";
  @SearchParamDefinition(name="type", path="DocumentReference.type", description="What kind of document this is (LOINC if possible)", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="authenticator", path="DocumentReference.authenticator", description="Who/What authenticated the document", type="reference" )
  public static final String SP_AUTHENTICATOR = "authenticator";
  @SearchParamDefinition(name="size", path="DocumentReference.size", description="Size of the document in bytes", type="number" )
  public static final String SP_SIZE = "size";
  @SearchParamDefinition(name="relationship", path="", description="Combination of relation and relatesTo", type="composite" )
  public static final String SP_RELATIONSHIP = "relationship";
  @SearchParamDefinition(name="author", path="DocumentReference.author", description="Who and/or what authored the document", type="reference" )
  public static final String SP_AUTHOR = "author";
  @SearchParamDefinition(name="patient", path="DocumentReference.subject", description="Who|what is the subject of the document", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="custodian", path="DocumentReference.custodian", description="Org which maintains the document", type="reference" )
  public static final String SP_CUSTODIAN = "custodian";
  @SearchParamDefinition(name="facility", path="DocumentReference.context.facilityType", description="Kind of facility where patient was seen", type="token" )
  public static final String SP_FACILITY = "facility";
  @SearchParamDefinition(name="created", path="DocumentReference.created", description="Document creation time", type="date" )
  public static final String SP_CREATED = "created";
  @SearchParamDefinition(name="event", path="DocumentReference.context.event", description="Main Clinical Acts Documented", type="token" )
  public static final String SP_EVENT = "event";
  @SearchParamDefinition(name="confidentiality", path="DocumentReference.confidentiality", description="Sensitivity of source document", type="token" )
  public static final String SP_CONFIDENTIALITY = "confidentiality";
  @SearchParamDefinition(name="description", path="DocumentReference.description", description="Human-readable description (title)", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="language", path="DocumentReference.primaryLanguage", description="The marked primary language for the document", type="token" )
  public static final String SP_LANGUAGE = "language";
  @SearchParamDefinition(name="identifier", path="DocumentReference.masterIdentifier|DocumentReference.identifier", description="Master Version Specific Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

