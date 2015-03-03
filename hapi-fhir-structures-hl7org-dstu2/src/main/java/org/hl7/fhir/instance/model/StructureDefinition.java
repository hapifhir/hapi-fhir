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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A Resource Profile - a statement of use of one or more FHIR Resources.  It may include constraints on Resources and Data Types, Terminology Binding Statements and Extension Definitions.
 */
@ResourceDef(name="StructureDefinition", profile="http://hl7.org/fhir/Profile/StructureDefinition")
public class StructureDefinition extends DomainResource {

    public enum StructureDefinitionStatus {
        /**
         * This profile is still under development.
         */
        DRAFT, 
        /**
         * This profile is ready for normal use.
         */
        ACTIVE, 
        /**
         * This profile has been deprecated, withdrawn or superseded and should no longer be used.
         */
        RETIRED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureDefinitionStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("retired".equals(codeString))
          return RETIRED;
        throw new Exception("Unknown StructureDefinitionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "";
            case ACTIVE: return "";
            case RETIRED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "This profile is still under development.";
            case ACTIVE: return "This profile is ready for normal use.";
            case RETIRED: return "This profile has been deprecated, withdrawn or superseded and should no longer be used.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case RETIRED: return "retired";
            default: return "?";
          }
        }
    }

  public static class StructureDefinitionStatusEnumFactory implements EnumFactory<StructureDefinitionStatus> {
    public StructureDefinitionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return StructureDefinitionStatus.DRAFT;
        if ("active".equals(codeString))
          return StructureDefinitionStatus.ACTIVE;
        if ("retired".equals(codeString))
          return StructureDefinitionStatus.RETIRED;
        throw new IllegalArgumentException("Unknown StructureDefinitionStatus code '"+codeString+"'");
        }
    public String toCode(StructureDefinitionStatus code) {
      if (code == StructureDefinitionStatus.DRAFT)
        return "draft";
      if (code == StructureDefinitionStatus.ACTIVE)
        return "active";
      if (code == StructureDefinitionStatus.RETIRED)
        return "retired";
      return "?";
      }
    }

    public enum StructureDefinitionType {
        /**
         * An abstract Type. Structure Definitions for abstract types are provided for structural completeness, and to support functions such as code generation.
         */
        ABSTRACT, 
        /**
         * A data type - either a primitive or complex structure that defines a set of data elements. These can be used throughout Resource and extension definitions.
         */
        TYPE, 
        /**
         * A resource defined by the FHIR specification.
         */
        RESOURCE, 
        /**
         * A set of constraints on a resource or data type that describe how it is used for a particular use.
         */
        CONSTRAINT, 
        /**
         * A definition of an extension that can be used in a FHIR resource (or a set of constraints on an exsting extension).
         */
        EXTENSION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureDefinitionType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("abstract".equals(codeString))
          return ABSTRACT;
        if ("type".equals(codeString))
          return TYPE;
        if ("resource".equals(codeString))
          return RESOURCE;
        if ("constraint".equals(codeString))
          return CONSTRAINT;
        if ("extension".equals(codeString))
          return EXTENSION;
        throw new Exception("Unknown StructureDefinitionType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ABSTRACT: return "abstract";
            case TYPE: return "type";
            case RESOURCE: return "resource";
            case CONSTRAINT: return "constraint";
            case EXTENSION: return "extension";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ABSTRACT: return "";
            case TYPE: return "";
            case RESOURCE: return "";
            case CONSTRAINT: return "";
            case EXTENSION: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ABSTRACT: return "An abstract Type. Structure Definitions for abstract types are provided for structural completeness, and to support functions such as code generation.";
            case TYPE: return "A data type - either a primitive or complex structure that defines a set of data elements. These can be used throughout Resource and extension definitions.";
            case RESOURCE: return "A resource defined by the FHIR specification.";
            case CONSTRAINT: return "A set of constraints on a resource or data type that describe how it is used for a particular use.";
            case EXTENSION: return "A definition of an extension that can be used in a FHIR resource (or a set of constraints on an exsting extension).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ABSTRACT: return "Abstract type";
            case TYPE: return "Data Type";
            case RESOURCE: return "Resource";
            case CONSTRAINT: return "Constrained Type";
            case EXTENSION: return "Extension Definition";
            default: return "?";
          }
        }
    }

  public static class StructureDefinitionTypeEnumFactory implements EnumFactory<StructureDefinitionType> {
    public StructureDefinitionType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("abstract".equals(codeString))
          return StructureDefinitionType.ABSTRACT;
        if ("type".equals(codeString))
          return StructureDefinitionType.TYPE;
        if ("resource".equals(codeString))
          return StructureDefinitionType.RESOURCE;
        if ("constraint".equals(codeString))
          return StructureDefinitionType.CONSTRAINT;
        if ("extension".equals(codeString))
          return StructureDefinitionType.EXTENSION;
        throw new IllegalArgumentException("Unknown StructureDefinitionType code '"+codeString+"'");
        }
    public String toCode(StructureDefinitionType code) {
      if (code == StructureDefinitionType.ABSTRACT)
        return "abstract";
      if (code == StructureDefinitionType.TYPE)
        return "type";
      if (code == StructureDefinitionType.RESOURCE)
        return "resource";
      if (code == StructureDefinitionType.CONSTRAINT)
        return "constraint";
      if (code == StructureDefinitionType.EXTENSION)
        return "extension";
      return "?";
      }
    }

    public enum ExtensionContext {
        /**
         * The context is all elements matching a particular resource element path.
         */
        RESOURCE, 
        /**
         * The context is all nodes matching a particular data type element path (root or repeating element) or all elements referencing a particular primitive data type (expressed as the datatype name).
         */
        DATATYPE, 
        /**
         * The context is all nodes whose mapping to a specified reference model corresponds to a particular mapping structure.  The context identifies the mapping target. The mapping should clearly identify where such an extension could be used.
         */
        MAPPING, 
        /**
         * The context is a particular extension from a particular profile.  Expressed as uri#name, where uri identifies the profile and #name identifies the extension code.
         */
        EXTENSION, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ExtensionContext fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("resource".equals(codeString))
          return RESOURCE;
        if ("datatype".equals(codeString))
          return DATATYPE;
        if ("mapping".equals(codeString))
          return MAPPING;
        if ("extension".equals(codeString))
          return EXTENSION;
        throw new Exception("Unknown ExtensionContext code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case RESOURCE: return "resource";
            case DATATYPE: return "datatype";
            case MAPPING: return "mapping";
            case EXTENSION: return "extension";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case RESOURCE: return "";
            case DATATYPE: return "";
            case MAPPING: return "";
            case EXTENSION: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case RESOURCE: return "The context is all elements matching a particular resource element path.";
            case DATATYPE: return "The context is all nodes matching a particular data type element path (root or repeating element) or all elements referencing a particular primitive data type (expressed as the datatype name).";
            case MAPPING: return "The context is all nodes whose mapping to a specified reference model corresponds to a particular mapping structure.  The context identifies the mapping target. The mapping should clearly identify where such an extension could be used.";
            case EXTENSION: return "The context is a particular extension from a particular profile.  Expressed as uri#name, where uri identifies the profile and #name identifies the extension code.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RESOURCE: return "resource";
            case DATATYPE: return "datatype";
            case MAPPING: return "mapping";
            case EXTENSION: return "extension";
            default: return "?";
          }
        }
    }

  public static class ExtensionContextEnumFactory implements EnumFactory<ExtensionContext> {
    public ExtensionContext fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("resource".equals(codeString))
          return ExtensionContext.RESOURCE;
        if ("datatype".equals(codeString))
          return ExtensionContext.DATATYPE;
        if ("mapping".equals(codeString))
          return ExtensionContext.MAPPING;
        if ("extension".equals(codeString))
          return ExtensionContext.EXTENSION;
        throw new IllegalArgumentException("Unknown ExtensionContext code '"+codeString+"'");
        }
    public String toCode(ExtensionContext code) {
      if (code == ExtensionContext.RESOURCE)
        return "resource";
      if (code == ExtensionContext.DATATYPE)
        return "datatype";
      if (code == ExtensionContext.MAPPING)
        return "mapping";
      if (code == ExtensionContext.EXTENSION)
        return "extension";
      return "?";
      }
    }

    @Block()
    public static class StructureDefinitionMappingComponent extends BackboneElement {
        /**
         * An Internal id that is used to identify this mapping set when specific mappings are made.
         */
        @Child(name="identity", type={IdType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Internal id when this mapping is used", formalDefinition="An Internal id that is used to identify this mapping set when specific mappings are made." )
        protected IdType identity;

        /**
         * A URI that identifies the specification that this mapping is expressed to.
         */
        @Child(name="uri", type={UriType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Identifies what this mapping refers to", formalDefinition="A URI that identifies the specification that this mapping is expressed to." )
        protected UriType uri;

        /**
         * A name for the specification that is being mapped to.
         */
        @Child(name="name", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Names what this mapping refers to", formalDefinition="A name for the specification that is being mapped to." )
        protected StringType name;

        /**
         * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        @Child(name="comments", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Versions, Issues, Scope limitations etc", formalDefinition="Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage." )
        protected StringType comments;

        private static final long serialVersionUID = 299630820L;

      public StructureDefinitionMappingComponent() {
        super();
      }

      public StructureDefinitionMappingComponent(IdType identity) {
        super();
        this.identity = identity;
      }

        /**
         * @return {@link #identity} (An Internal id that is used to identify this mapping set when specific mappings are made.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public IdType getIdentityElement() { 
          if (this.identity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureDefinitionMappingComponent.identity");
            else if (Configuration.doAutoCreate())
              this.identity = new IdType(); // bb
          return this.identity;
        }

        public boolean hasIdentityElement() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        public boolean hasIdentity() { 
          return this.identity != null && !this.identity.isEmpty();
        }

        /**
         * @param value {@link #identity} (An Internal id that is used to identify this mapping set when specific mappings are made.). This is the underlying object with id, value and extensions. The accessor "getIdentity" gives direct access to the value
         */
        public StructureDefinitionMappingComponent setIdentityElement(IdType value) { 
          this.identity = value;
          return this;
        }

        /**
         * @return An Internal id that is used to identify this mapping set when specific mappings are made.
         */
        public String getIdentity() { 
          return this.identity == null ? null : this.identity.getValue();
        }

        /**
         * @param value An Internal id that is used to identify this mapping set when specific mappings are made.
         */
        public StructureDefinitionMappingComponent setIdentity(String value) { 
            if (this.identity == null)
              this.identity = new IdType();
            this.identity.setValue(value);
          return this;
        }

        /**
         * @return {@link #uri} (A URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureDefinitionMappingComponent.uri");
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
         * @param value {@link #uri} (A URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public StructureDefinitionMappingComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return A URI that identifies the specification that this mapping is expressed to.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value A URI that identifies the specification that this mapping is expressed to.
         */
        public StructureDefinitionMappingComponent setUri(String value) { 
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
         * @return {@link #name} (A name for the specification that is being mapped to.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureDefinitionMappingComponent.name");
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
         * @param value {@link #name} (A name for the specification that is being mapped to.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StructureDefinitionMappingComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A name for the specification that is being mapped to.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A name for the specification that is being mapped to.
         */
        public StructureDefinitionMappingComponent setName(String value) { 
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
         * @return {@link #comments} (Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public StringType getCommentsElement() { 
          if (this.comments == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureDefinitionMappingComponent.comments");
            else if (Configuration.doAutoCreate())
              this.comments = new StringType(); // bb
          return this.comments;
        }

        public boolean hasCommentsElement() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        public boolean hasComments() { 
          return this.comments != null && !this.comments.isEmpty();
        }

        /**
         * @param value {@link #comments} (Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
         */
        public StructureDefinitionMappingComponent setCommentsElement(StringType value) { 
          this.comments = value;
          return this;
        }

        /**
         * @return Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        public String getComments() { 
          return this.comments == null ? null : this.comments.getValue();
        }

        /**
         * @param value Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        public StructureDefinitionMappingComponent setComments(String value) { 
          if (Utilities.noString(value))
            this.comments = null;
          else {
            if (this.comments == null)
              this.comments = new StringType();
            this.comments.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identity", "id", "An Internal id that is used to identify this mapping set when specific mappings are made.", 0, java.lang.Integer.MAX_VALUE, identity));
          childrenList.add(new Property("uri", "uri", "A URI that identifies the specification that this mapping is expressed to.", 0, java.lang.Integer.MAX_VALUE, uri));
          childrenList.add(new Property("name", "string", "A name for the specification that is being mapped to.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("comments", "string", "Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.", 0, java.lang.Integer.MAX_VALUE, comments));
        }

      public StructureDefinitionMappingComponent copy() {
        StructureDefinitionMappingComponent dst = new StructureDefinitionMappingComponent();
        copyValues(dst);
        dst.identity = identity == null ? null : identity.copy();
        dst.uri = uri == null ? null : uri.copy();
        dst.name = name == null ? null : name.copy();
        dst.comments = comments == null ? null : comments.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureDefinitionMappingComponent))
          return false;
        StructureDefinitionMappingComponent o = (StructureDefinitionMappingComponent) other;
        return compareDeep(identity, o.identity, true) && compareDeep(uri, o.uri, true) && compareDeep(name, o.name, true)
           && compareDeep(comments, o.comments, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureDefinitionMappingComponent))
          return false;
        StructureDefinitionMappingComponent o = (StructureDefinitionMappingComponent) other;
        return compareValues(identity, o.identity, true) && compareValues(uri, o.uri, true) && compareValues(name, o.name, true)
           && compareValues(comments, o.comments, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identity == null || identity.isEmpty()) && (uri == null || uri.isEmpty())
           && (name == null || name.isEmpty()) && (comments == null || comments.isEmpty());
      }

  }

    @Block()
    public static class StructureDefinitionSnapshotComponent extends BackboneElement {
        /**
         * Captures constraints on each element within the resource.
         */
        @Child(name="element", type={ElementDefinition.class}, order=1, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Definition of elements in the resource (if no profile)", formalDefinition="Captures constraints on each element within the resource." )
        protected List<ElementDefinition> element;

        private static final long serialVersionUID = 53896641L;

      public StructureDefinitionSnapshotComponent() {
        super();
      }

        /**
         * @return {@link #element} (Captures constraints on each element within the resource.)
         */
        public List<ElementDefinition> getElement() { 
          if (this.element == null)
            this.element = new ArrayList<ElementDefinition>();
          return this.element;
        }

        public boolean hasElement() { 
          if (this.element == null)
            return false;
          for (ElementDefinition item : this.element)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #element} (Captures constraints on each element within the resource.)
         */
    // syntactic sugar
        public ElementDefinition addElement() { //3
          ElementDefinition t = new ElementDefinition();
          if (this.element == null)
            this.element = new ArrayList<ElementDefinition>();
          this.element.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("element", "ElementDefinition", "Captures constraints on each element within the resource.", 0, java.lang.Integer.MAX_VALUE, element));
        }

      public StructureDefinitionSnapshotComponent copy() {
        StructureDefinitionSnapshotComponent dst = new StructureDefinitionSnapshotComponent();
        copyValues(dst);
        if (element != null) {
          dst.element = new ArrayList<ElementDefinition>();
          for (ElementDefinition i : element)
            dst.element.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureDefinitionSnapshotComponent))
          return false;
        StructureDefinitionSnapshotComponent o = (StructureDefinitionSnapshotComponent) other;
        return compareDeep(element, o.element, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureDefinitionSnapshotComponent))
          return false;
        StructureDefinitionSnapshotComponent o = (StructureDefinitionSnapshotComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (element == null || element.isEmpty());
      }

  }

    @Block()
    public static class StructureDefinitionDifferentialComponent extends BackboneElement {
        /**
         * Captures constraints on each element within the resource.
         */
        @Child(name="element", type={ElementDefinition.class}, order=1, min=1, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Definition of elements in the resource (if no profile)", formalDefinition="Captures constraints on each element within the resource." )
        protected List<ElementDefinition> element;

        private static final long serialVersionUID = 53896641L;

      public StructureDefinitionDifferentialComponent() {
        super();
      }

        /**
         * @return {@link #element} (Captures constraints on each element within the resource.)
         */
        public List<ElementDefinition> getElement() { 
          if (this.element == null)
            this.element = new ArrayList<ElementDefinition>();
          return this.element;
        }

        public boolean hasElement() { 
          if (this.element == null)
            return false;
          for (ElementDefinition item : this.element)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #element} (Captures constraints on each element within the resource.)
         */
    // syntactic sugar
        public ElementDefinition addElement() { //3
          ElementDefinition t = new ElementDefinition();
          if (this.element == null)
            this.element = new ArrayList<ElementDefinition>();
          this.element.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("element", "ElementDefinition", "Captures constraints on each element within the resource.", 0, java.lang.Integer.MAX_VALUE, element));
        }

      public StructureDefinitionDifferentialComponent copy() {
        StructureDefinitionDifferentialComponent dst = new StructureDefinitionDifferentialComponent();
        copyValues(dst);
        if (element != null) {
          dst.element = new ArrayList<ElementDefinition>();
          for (ElementDefinition i : element)
            dst.element.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureDefinitionDifferentialComponent))
          return false;
        StructureDefinitionDifferentialComponent o = (StructureDefinitionDifferentialComponent) other;
        return compareDeep(element, o.element, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureDefinitionDifferentialComponent))
          return false;
        StructureDefinitionDifferentialComponent o = (StructureDefinitionDifferentialComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (element == null || element.isEmpty());
      }

  }

    /**
     * The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems.
     */
    @Child(name = "url", type = {UriType.class}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="Literal URL used to reference this profile", formalDefinition="The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 1, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Other identifiers for the profile", formalDefinition="Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)." )
    protected List<Identifier> identifier;

    /**
     * The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually.
     */
    @Child(name = "version", type = {StringType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Logical id for this version of the profile", formalDefinition="The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually." )
    protected StringType version;

    /**
     * A free text natural language name identifying the Profile.
     */
    @Child(name = "name", type = {StringType.class}, order = 3, min = 1, max = 1)
    @Description(shortDefinition="Informal name for this profile", formalDefinition="A free text natural language name identifying the Profile." )
    protected StringType name;

    /**
     * Defined so that applications can use this name when displaying the value of the extension to the user.
     */
    @Child(name = "display", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Use this name when displaying the value", formalDefinition="Defined so that applications can use this name when displaying the value of the extension to the user." )
    protected StringType display;

    /**
     * Details of the individual or organization who accepts responsibility for publishing the profile.
     */
    @Child(name = "publisher", type = {StringType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="Details of the individual or organization who accepts responsibility for publishing the profile." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order = 6, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * A free text natural language description of the profile and its use.
     */
    @Child(name = "description", type = {StringType.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="Natural language description of the profile", formalDefinition="A free text natural language description of the profile and its use." )
    protected StringType description;

    /**
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     */
    @Child(name = "code", type = {Coding.class}, order = 8, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Assist with indexing and finding", formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates." )
    protected List<Coding> code;

    /**
     * The status of the profile.
     */
    @Child(name = "status", type = {CodeType.class}, order = 9, min = 1, max = 1)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the profile." )
    protected Enumeration<StructureDefinitionStatus> status;

    /**
     * This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The date that this version of the profile was published.
     */
    @Child(name = "date", type = {DateTimeType.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="Date for this version of the profile", formalDefinition="The date that this version of the profile was published." )
    protected DateTimeType date;

    /**
     * The Scope and Usage that this profile was created to meet.
     */
    @Child(name = "requirements", type = {StringType.class}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="Scope and Usage this profile is for", formalDefinition="The Scope and Usage that this profile was created to meet." )
    protected StringType requirements;

    /**
     * The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.4.0 for this version.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order = 13, min = 0, max = 1)
    @Description(shortDefinition="FHIR Version this profile targets", formalDefinition="The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.4.0 for this version." )
    protected IdType fhirVersion;

    /**
     * An external specification that the content is mapped to.
     */
    @Child(name = "mapping", type = {}, order = 14, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="External specification that the content is mapped to", formalDefinition="An external specification that the content is mapped to." )
    protected List<StructureDefinitionMappingComponent> mapping;

    /**
     * Defines the type of structure that this definition is describing.
     */
    @Child(name = "type", type = {CodeType.class}, order = 15, min = 1, max = 1)
    @Description(shortDefinition="abstract | type | resource | constraint | extension", formalDefinition="Defines the type of structure that this definition is describing." )
    protected Enumeration<StructureDefinitionType> type;

    /**
     * Identifies the type of context to which the extension applies.
     */
    @Child(name = "contextType", type = {CodeType.class}, order = 16, min = 0, max = 1)
    @Description(shortDefinition="resource | datatype | mapping | extension", formalDefinition="Identifies the type of context to which the extension applies." )
    protected Enumeration<ExtensionContext> contextType;

    /**
     * Identifies the types of resource or data type elements to which the extension can be applied.
     */
    @Child(name = "context", type = {StringType.class}, order = 17, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Where the extension can be used in instances", formalDefinition="Identifies the types of resource or data type elements to which the extension can be applied." )
    protected List<StringType> context;

    /**
     * The structure that is the base on which this set of constraints is derived from.
     */
    @Child(name = "base", type = {UriType.class}, order = 18, min = 0, max = 1)
    @Description(shortDefinition="Structure that this set of constraints applies to", formalDefinition="The structure that is the base on which this set of constraints is derived from." )
    protected UriType base;

    /**
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile.
     */
    @Child(name = "snapshot", type = {}, order = 19, min = 0, max = 1)
    @Description(shortDefinition="Snapshot view of the structure", formalDefinition="A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile." )
    protected StructureDefinitionSnapshotComponent snapshot;

    /**
     * A differential view is expressed relative to the base profile - a statement of differences that it applies.
     */
    @Child(name = "differential", type = {}, order = 20, min = 0, max = 1)
    @Description(shortDefinition="Differential view of the structure", formalDefinition="A differential view is expressed relative to the base profile - a statement of differences that it applies." )
    protected StructureDefinitionDifferentialComponent differential;

    private static final long serialVersionUID = -149055817L;

    public StructureDefinition() {
      super();
    }

    public StructureDefinition(UriType url, StringType name, Enumeration<StructureDefinitionStatus> status, Enumeration<StructureDefinitionType> type) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
      this.type = type;
    }

    /**
     * @return {@link #url} (The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.url");
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
     * @param value {@link #url} (The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public StructureDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems.
     */
    public StructureDefinition setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).)
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
     * @return {@link #identifier} (Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).)
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
     * @return {@link #version} (The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StructureDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually.
     */
    public StructureDefinition setVersion(String value) { 
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
     * @return {@link #name} (A free text natural language name identifying the Profile.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.name");
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
     * @param value {@link #name} (A free text natural language name identifying the Profile.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StructureDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the Profile.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the Profile.
     */
    public StructureDefinition setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #display} (Defined so that applications can use this name when displaying the value of the extension to the user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public StringType getDisplayElement() { 
      if (this.display == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.display");
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
     * @param value {@link #display} (Defined so that applications can use this name when displaying the value of the extension to the user.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public StructureDefinition setDisplayElement(StringType value) { 
      this.display = value;
      return this;
    }

    /**
     * @return Defined so that applications can use this name when displaying the value of the extension to the user.
     */
    public String getDisplay() { 
      return this.display == null ? null : this.display.getValue();
    }

    /**
     * @param value Defined so that applications can use this name when displaying the value of the extension to the user.
     */
    public StructureDefinition setDisplay(String value) { 
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
     * @return {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the profile.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.publisher");
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
     * @param value {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the profile.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StructureDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return Details of the individual or organization who accepts responsibility for publishing the profile.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value Details of the individual or organization who accepts responsibility for publishing the profile.
     */
    public StructureDefinition setPublisher(String value) { 
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
     * @return {@link #telecom} (Contact details to assist a user in finding and communicating with the publisher.)
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
     * @return {@link #telecom} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    /**
     * @return {@link #description} (A free text natural language description of the profile and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the profile and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StructureDefinition setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the profile and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the profile and its use.
     */
    public StructureDefinition setDescription(String value) { 
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
     * @return {@link #code} (A set of terms from external terminologies that may be used to assist with indexing and searching of templates.)
     */
    public List<Coding> getCode() { 
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      return this.code;
    }

    public boolean hasCode() { 
      if (this.code == null)
        return false;
      for (Coding item : this.code)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #code} (A set of terms from external terminologies that may be used to assist with indexing and searching of templates.)
     */
    // syntactic sugar
    public Coding addCode() { //3
      Coding t = new Coding();
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return t;
    }

    /**
     * @return {@link #status} (The status of the profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<StructureDefinitionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<StructureDefinitionStatus>(new StructureDefinitionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the profile.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public StructureDefinition setStatusElement(Enumeration<StructureDefinitionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the profile.
     */
    public StructureDefinitionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the profile.
     */
    public StructureDefinition setStatus(StructureDefinitionStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<StructureDefinitionStatus>(new StructureDefinitionStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.experimental");
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
     * @param value {@link #experimental} (This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public StructureDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null ? false : this.experimental.getValue();
    }

    /**
     * @param value This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public StructureDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date that this version of the profile was published.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.date");
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
     * @param value {@link #date} (The date that this version of the profile was published.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public StructureDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that this version of the profile was published.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that this version of the profile was published.
     */
    public StructureDefinition setDate(Date value) { 
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
     * @return {@link #requirements} (The Scope and Usage that this profile was created to meet.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.requirements");
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
     * @param value {@link #requirements} (The Scope and Usage that this profile was created to meet.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StructureDefinition setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return The Scope and Usage that this profile was created to meet.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value The Scope and Usage that this profile was created to meet.
     */
    public StructureDefinition setRequirements(String value) { 
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
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.4.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public IdType getFhirVersionElement() { 
      if (this.fhirVersion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.fhirVersion");
        else if (Configuration.doAutoCreate())
          this.fhirVersion = new IdType(); // bb
      return this.fhirVersion;
    }

    public boolean hasFhirVersionElement() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    public boolean hasFhirVersion() { 
      return this.fhirVersion != null && !this.fhirVersion.isEmpty();
    }

    /**
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.4.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public StructureDefinition setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.4.0 for this version.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.4.0 for this version.
     */
    public StructureDefinition setFhirVersion(String value) { 
      if (Utilities.noString(value))
        this.fhirVersion = null;
      else {
        if (this.fhirVersion == null)
          this.fhirVersion = new IdType();
        this.fhirVersion.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #mapping} (An external specification that the content is mapped to.)
     */
    public List<StructureDefinitionMappingComponent> getMapping() { 
      if (this.mapping == null)
        this.mapping = new ArrayList<StructureDefinitionMappingComponent>();
      return this.mapping;
    }

    public boolean hasMapping() { 
      if (this.mapping == null)
        return false;
      for (StructureDefinitionMappingComponent item : this.mapping)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #mapping} (An external specification that the content is mapped to.)
     */
    // syntactic sugar
    public StructureDefinitionMappingComponent addMapping() { //3
      StructureDefinitionMappingComponent t = new StructureDefinitionMappingComponent();
      if (this.mapping == null)
        this.mapping = new ArrayList<StructureDefinitionMappingComponent>();
      this.mapping.add(t);
      return t;
    }

    /**
     * @return {@link #type} (Defines the type of structure that this definition is describing.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<StructureDefinitionType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<StructureDefinitionType>(new StructureDefinitionTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Defines the type of structure that this definition is describing.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public StructureDefinition setTypeElement(Enumeration<StructureDefinitionType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Defines the type of structure that this definition is describing.
     */
    public StructureDefinitionType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Defines the type of structure that this definition is describing.
     */
    public StructureDefinition setType(StructureDefinitionType value) { 
        if (this.type == null)
          this.type = new Enumeration<StructureDefinitionType>(new StructureDefinitionTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #contextType} (Identifies the type of context to which the extension applies.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
     */
    public Enumeration<ExtensionContext> getContextTypeElement() { 
      if (this.contextType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.contextType");
        else if (Configuration.doAutoCreate())
          this.contextType = new Enumeration<ExtensionContext>(new ExtensionContextEnumFactory()); // bb
      return this.contextType;
    }

    public boolean hasContextTypeElement() { 
      return this.contextType != null && !this.contextType.isEmpty();
    }

    public boolean hasContextType() { 
      return this.contextType != null && !this.contextType.isEmpty();
    }

    /**
     * @param value {@link #contextType} (Identifies the type of context to which the extension applies.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
     */
    public StructureDefinition setContextTypeElement(Enumeration<ExtensionContext> value) { 
      this.contextType = value;
      return this;
    }

    /**
     * @return Identifies the type of context to which the extension applies.
     */
    public ExtensionContext getContextType() { 
      return this.contextType == null ? null : this.contextType.getValue();
    }

    /**
     * @param value Identifies the type of context to which the extension applies.
     */
    public StructureDefinition setContextType(ExtensionContext value) { 
      if (value == null)
        this.contextType = null;
      else {
        if (this.contextType == null)
          this.contextType = new Enumeration<ExtensionContext>(new ExtensionContextEnumFactory());
        this.contextType.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #context} (Identifies the types of resource or data type elements to which the extension can be applied.)
     */
    public List<StringType> getContext() { 
      if (this.context == null)
        this.context = new ArrayList<StringType>();
      return this.context;
    }

    public boolean hasContext() { 
      if (this.context == null)
        return false;
      for (StringType item : this.context)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #context} (Identifies the types of resource or data type elements to which the extension can be applied.)
     */
    // syntactic sugar
    public StringType addContextElement() {//2 
      StringType t = new StringType();
      if (this.context == null)
        this.context = new ArrayList<StringType>();
      this.context.add(t);
      return t;
    }

    /**
     * @param value {@link #context} (Identifies the types of resource or data type elements to which the extension can be applied.)
     */
    public StructureDefinition addContext(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.context == null)
        this.context = new ArrayList<StringType>();
      this.context.add(t);
      return this;
    }

    /**
     * @param value {@link #context} (Identifies the types of resource or data type elements to which the extension can be applied.)
     */
    public boolean hasContext(String value) { 
      if (this.context == null)
        return false;
      for (StringType v : this.context)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #base} (The structure that is the base on which this set of constraints is derived from.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public UriType getBaseElement() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.base");
        else if (Configuration.doAutoCreate())
          this.base = new UriType(); // bb
      return this.base;
    }

    public boolean hasBaseElement() { 
      return this.base != null && !this.base.isEmpty();
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (The structure that is the base on which this set of constraints is derived from.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public StructureDefinition setBaseElement(UriType value) { 
      this.base = value;
      return this;
    }

    /**
     * @return The structure that is the base on which this set of constraints is derived from.
     */
    public String getBase() { 
      return this.base == null ? null : this.base.getValue();
    }

    /**
     * @param value The structure that is the base on which this set of constraints is derived from.
     */
    public StructureDefinition setBase(String value) { 
      if (Utilities.noString(value))
        this.base = null;
      else {
        if (this.base == null)
          this.base = new UriType();
        this.base.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #snapshot} (A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile.)
     */
    public StructureDefinitionSnapshotComponent getSnapshot() { 
      if (this.snapshot == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.snapshot");
        else if (Configuration.doAutoCreate())
          this.snapshot = new StructureDefinitionSnapshotComponent(); // cc
      return this.snapshot;
    }

    public boolean hasSnapshot() { 
      return this.snapshot != null && !this.snapshot.isEmpty();
    }

    /**
     * @param value {@link #snapshot} (A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile.)
     */
    public StructureDefinition setSnapshot(StructureDefinitionSnapshotComponent value) { 
      this.snapshot = value;
      return this;
    }

    /**
     * @return {@link #differential} (A differential view is expressed relative to the base profile - a statement of differences that it applies.)
     */
    public StructureDefinitionDifferentialComponent getDifferential() { 
      if (this.differential == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.differential");
        else if (Configuration.doAutoCreate())
          this.differential = new StructureDefinitionDifferentialComponent(); // cc
      return this.differential;
    }

    public boolean hasDifferential() { 
      return this.differential != null && !this.differential.isEmpty();
    }

    /**
     * @param value {@link #differential} (A differential view is expressed relative to the base profile - a statement of differences that it applies.)
     */
    public StructureDefinition setDifferential(StructureDefinitionDifferentialComponent value) { 
      this.differential = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "The URL at which this profile is (or will be) published, and which is used to reference this profile in extension urls and tag values in operational FHIR systems.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this profile when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the profile when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the profile author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the Profile.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("display", "string", "Defined so that applications can use this name when displaying the value of the extension to the user.", 0, java.lang.Integer.MAX_VALUE, display));
        childrenList.add(new Property("publisher", "string", "Details of the individual or organization who accepts responsibility for publishing the profile.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("description", "string", "A free text natural language description of the profile and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("code", "Coding", "A set of terms from external terminologies that may be used to assist with indexing and searching of templates.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("status", "code", "The status of the profile.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This profile was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date that this version of the profile was published.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("requirements", "string", "The Scope and Usage that this profile was created to meet.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this profile is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.4.0 for this version.", 0, java.lang.Integer.MAX_VALUE, fhirVersion));
        childrenList.add(new Property("mapping", "", "An external specification that the content is mapped to.", 0, java.lang.Integer.MAX_VALUE, mapping));
        childrenList.add(new Property("type", "code", "Defines the type of structure that this definition is describing.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("contextType", "code", "Identifies the type of context to which the extension applies.", 0, java.lang.Integer.MAX_VALUE, contextType));
        childrenList.add(new Property("context", "string", "Identifies the types of resource or data type elements to which the extension can be applied.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("base", "uri", "The structure that is the base on which this set of constraints is derived from.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("snapshot", "", "A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base profile.", 0, java.lang.Integer.MAX_VALUE, snapshot));
        childrenList.add(new Property("differential", "", "A differential view is expressed relative to the base profile - a statement of differences that it applies.", 0, java.lang.Integer.MAX_VALUE, differential));
      }

      public StructureDefinition copy() {
        StructureDefinition dst = new StructureDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.display = display == null ? null : display.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        if (mapping != null) {
          dst.mapping = new ArrayList<StructureDefinitionMappingComponent>();
          for (StructureDefinitionMappingComponent i : mapping)
            dst.mapping.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.contextType = contextType == null ? null : contextType.copy();
        if (context != null) {
          dst.context = new ArrayList<StringType>();
          for (StringType i : context)
            dst.context.add(i.copy());
        };
        dst.base = base == null ? null : base.copy();
        dst.snapshot = snapshot == null ? null : snapshot.copy();
        dst.differential = differential == null ? null : differential.copy();
        return dst;
      }

      protected StructureDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StructureDefinition))
          return false;
        StructureDefinition o = (StructureDefinition) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(display, o.display, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(description, o.description, true) && compareDeep(code, o.code, true)
           && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true) && compareDeep(date, o.date, true)
           && compareDeep(requirements, o.requirements, true) && compareDeep(fhirVersion, o.fhirVersion, true)
           && compareDeep(mapping, o.mapping, true) && compareDeep(type, o.type, true) && compareDeep(contextType, o.contextType, true)
           && compareDeep(context, o.context, true) && compareDeep(base, o.base, true) && compareDeep(snapshot, o.snapshot, true)
           && compareDeep(differential, o.differential, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureDefinition))
          return false;
        StructureDefinition o = (StructureDefinition) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(display, o.display, true) && compareValues(publisher, o.publisher, true) && compareValues(description, o.description, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(date, o.date, true)
           && compareValues(requirements, o.requirements, true) && compareValues(fhirVersion, o.fhirVersion, true)
           && compareValues(type, o.type, true) && compareValues(contextType, o.contextType, true) && compareValues(context, o.context, true)
           && compareValues(base, o.base, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (display == null || display.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (telecom == null || telecom.isEmpty()) && (description == null || description.isEmpty())
           && (code == null || code.isEmpty()) && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (date == null || date.isEmpty()) && (requirements == null || requirements.isEmpty()) && (fhirVersion == null || fhirVersion.isEmpty())
           && (mapping == null || mapping.isEmpty()) && (type == null || type.isEmpty()) && (contextType == null || contextType.isEmpty())
           && (context == null || context.isEmpty()) && (base == null || base.isEmpty()) && (snapshot == null || snapshot.isEmpty())
           && (differential == null || differential.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.StructureDefinition;
   }

  @SearchParamDefinition(name="date", path="StructureDefinition.date", description="The profile publication date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="StructureDefinition.identifier", description="The identifier of the profile", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
    @SearchParamDefinition(name = "path", path = "StructureDefinition.snapshot.element.path|StructureDefinition.differential.element.path", description = "A path that is constrained in the profile", type = "token")
    public static final String SP_PATH = "path";
    @SearchParamDefinition(name = "code", path = "StructureDefinition.code", description = "A code for the profile", type = "token")
    public static final String SP_CODE = "code";
    @SearchParamDefinition(name = "valueset", path = "StructureDefinition.snapshot.element.binding.reference[x]", description = "A vocabulary binding code", type = "reference")
    public static final String SP_VALUESET = "valueset";
    @SearchParamDefinition(name = "name", path = "StructureDefinition.name", description = "Name of the profile", type = "string")
    public static final String SP_NAME = "name";
  @SearchParamDefinition(name="publisher", path="StructureDefinition.publisher", description="Name of the publisher of the profile", type="string" )
  public static final String SP_PUBLISHER = "publisher";
    @SearchParamDefinition(name = "description", path = "StructureDefinition.description", description = "Text search in the description of the profile", type = "string")
    public static final String SP_DESCRIPTION = "description";
    @SearchParamDefinition(name = "type", path = "StructureDefinition.type", description = "abstract | type | resource | constraint | extension", type = "token")
    public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="version", path="StructureDefinition.version", description="The version identifier of the profile", type="token" )
  public static final String SP_VERSION = "version";
    @SearchParamDefinition(name = "url", path = "StructureDefinition.url", description = "Literal URL used to reference this profile", type = "token")
    public static final String SP_URL = "url";
    @SearchParamDefinition(name = "status", path = "StructureDefinition.status", description = "The current status of the profile", type = "token")
    public static final String SP_STATUS = "status";

}

