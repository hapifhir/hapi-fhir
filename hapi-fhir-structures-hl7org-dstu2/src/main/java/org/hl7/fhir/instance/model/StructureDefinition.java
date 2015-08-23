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
 * A definition of a FHIR structure. This resource is used to describe the underlying resources, data types defined in FHIR, and also for describing extensions, and constraints on resources and data types.
 */
@ResourceDef(name="StructureDefinition", profile="http://hl7.org/fhir/Profile/StructureDefinition")
public class StructureDefinition extends DomainResource {

    public enum StructureDefinitionKind {
        /**
         * A data type - either a primitive or complex structure that defines a set of data elements. These can be used throughout Resource and extension definitions
         */
        DATATYPE, 
        /**
         * A resource defined by the FHIR specification
         */
        RESOURCE, 
        /**
         * A logical model - a conceptual package of data that will be mapped to resources for implementation
         */
        LOGICAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static StructureDefinitionKind fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("datatype".equals(codeString))
          return DATATYPE;
        if ("resource".equals(codeString))
          return RESOURCE;
        if ("logical".equals(codeString))
          return LOGICAL;
        throw new Exception("Unknown StructureDefinitionKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DATATYPE: return "datatype";
            case RESOURCE: return "resource";
            case LOGICAL: return "logical";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DATATYPE: return "http://hl7.org/fhir/structure-definition-kind";
            case RESOURCE: return "http://hl7.org/fhir/structure-definition-kind";
            case LOGICAL: return "http://hl7.org/fhir/structure-definition-kind";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DATATYPE: return "A data type - either a primitive or complex structure that defines a set of data elements. These can be used throughout Resource and extension definitions";
            case RESOURCE: return "A resource defined by the FHIR specification";
            case LOGICAL: return "A logical model - a conceptual package of data that will be mapped to resources for implementation";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DATATYPE: return "Data Type";
            case RESOURCE: return "Resource";
            case LOGICAL: return "Logical Model";
            default: return "?";
          }
        }
    }

  public static class StructureDefinitionKindEnumFactory implements EnumFactory<StructureDefinitionKind> {
    public StructureDefinitionKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("datatype".equals(codeString))
          return StructureDefinitionKind.DATATYPE;
        if ("resource".equals(codeString))
          return StructureDefinitionKind.RESOURCE;
        if ("logical".equals(codeString))
          return StructureDefinitionKind.LOGICAL;
        throw new IllegalArgumentException("Unknown StructureDefinitionKind code '"+codeString+"'");
        }
    public String toCode(StructureDefinitionKind code) {
      if (code == StructureDefinitionKind.DATATYPE)
        return "datatype";
      if (code == StructureDefinitionKind.RESOURCE)
        return "resource";
      if (code == StructureDefinitionKind.LOGICAL)
        return "logical";
      return "?";
      }
    }

    public enum ExtensionContext {
        /**
         * The context is all elements matching a particular resource element path
         */
        RESOURCE, 
        /**
         * The context is all nodes matching a particular data type element path (root or repeating element) or all elements referencing a particular primitive data type (expressed as the datatype name)
         */
        DATATYPE, 
        /**
         * The context is all nodes whose mapping to a specified reference model corresponds to a particular mapping structure.  The context identifies the mapping target. The mapping should clearly identify where such an extension could be used
         */
        MAPPING, 
        /**
         * The context is a particular extension from a particular profile, a uri that identifies the extension definition
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
            case RESOURCE: return "http://hl7.org/fhir/extension-context";
            case DATATYPE: return "http://hl7.org/fhir/extension-context";
            case MAPPING: return "http://hl7.org/fhir/extension-context";
            case EXTENSION: return "http://hl7.org/fhir/extension-context";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case RESOURCE: return "The context is all elements matching a particular resource element path";
            case DATATYPE: return "The context is all nodes matching a particular data type element path (root or repeating element) or all elements referencing a particular primitive data type (expressed as the datatype name)";
            case MAPPING: return "The context is all nodes whose mapping to a specified reference model corresponds to a particular mapping structure.  The context identifies the mapping target. The mapping should clearly identify where such an extension could be used";
            case EXTENSION: return "The context is a particular extension from a particular profile, a uri that identifies the extension definition";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case RESOURCE: return "Resource";
            case DATATYPE: return "Datatype";
            case MAPPING: return "Mapping";
            case EXTENSION: return "Extension";
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
    public static class StructureDefinitionContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the structure definition.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the structure definition." )
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
      public StructureDefinitionContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the structure definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StructureDefinitionContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the structure definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StructureDefinitionContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the structure definition.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the structure definition.
         */
        public StructureDefinitionContactComponent setName(String value) { 
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
        public StructureDefinitionContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the structure definition.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public StructureDefinitionContactComponent copy() {
        StructureDefinitionContactComponent dst = new StructureDefinitionContactComponent();
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
        if (!(other instanceof StructureDefinitionContactComponent))
          return false;
        StructureDefinitionContactComponent o = (StructureDefinitionContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureDefinitionContactComponent))
          return false;
        StructureDefinitionContactComponent o = (StructureDefinitionContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    @Block()
    public static class StructureDefinitionMappingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An Internal id that is used to identify this mapping set when specific mappings are made.
         */
        @Child(name = "identity", type = {IdType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Internal id when this mapping is used", formalDefinition="An Internal id that is used to identify this mapping set when specific mappings are made." )
        protected IdType identity;

        /**
         * An absolute URI that identifies the specification that this mapping is expressed to.
         */
        @Child(name = "uri", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifies what this mapping refers to", formalDefinition="An absolute URI that identifies the specification that this mapping is expressed to." )
        protected UriType uri;

        /**
         * A name for the specification that is being mapped to.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Names what this mapping refers to", formalDefinition="A name for the specification that is being mapped to." )
        protected StringType name;

        /**
         * Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage.
         */
        @Child(name = "comments", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Versions, Issues, Scope limitations etc", formalDefinition="Comments about this mapping, including version notes, issues, scope limitations, and other important notes for usage." )
        protected StringType comments;

        private static final long serialVersionUID = 299630820L;

    /*
     * Constructor
     */
      public StructureDefinitionMappingComponent() {
        super();
      }

    /*
     * Constructor
     */
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
         * @return {@link #uri} (An absolute URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
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
         * @param value {@link #uri} (An absolute URI that identifies the specification that this mapping is expressed to.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public StructureDefinitionMappingComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return An absolute URI that identifies the specification that this mapping is expressed to.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value An absolute URI that identifies the specification that this mapping is expressed to.
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
          childrenList.add(new Property("uri", "uri", "An absolute URI that identifies the specification that this mapping is expressed to.", 0, java.lang.Integer.MAX_VALUE, uri));
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
    public static class StructureDefinitionSnapshotComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Captures constraints on each element within the resource.
         */
        @Child(name = "element", type = {ElementDefinition.class}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Definition of elements in the resource (if no StructureDefinition)", formalDefinition="Captures constraints on each element within the resource." )
        protected List<ElementDefinition> element;

        private static final long serialVersionUID = 53896641L;

    /*
     * Constructor
     */
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

    // syntactic sugar
        public StructureDefinitionSnapshotComponent addElement(ElementDefinition t) { //3
          if (t == null)
            return this;
          if (this.element == null)
            this.element = new ArrayList<ElementDefinition>();
          this.element.add(t);
          return this;
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
    public static class StructureDefinitionDifferentialComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Captures constraints on each element within the resource.
         */
        @Child(name = "element", type = {ElementDefinition.class}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Definition of elements in the resource (if no StructureDefinition)", formalDefinition="Captures constraints on each element within the resource." )
        protected List<ElementDefinition> element;

        private static final long serialVersionUID = 53896641L;

    /*
     * Constructor
     */
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

    // syntactic sugar
        public StructureDefinitionDifferentialComponent addElement(ElementDefinition t) { //3
          if (t == null)
            return this;
          if (this.element == null)
            this.element = new ArrayList<ElementDefinition>();
          this.element.add(t);
          return this;
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
     * An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Literal URL used to reference this StructureDefinition", formalDefinition="An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published." )
    protected UriType url;

    /**
     * Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other identifiers for the StructureDefinition", formalDefinition="Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI)." )
    protected List<Identifier> identifier;

    /**
     * The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of the StructureDefinition", formalDefinition="The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually." )
    protected StringType version;

    /**
     * A free text natural language name identifying the StructureDefinition.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this StructureDefinition", formalDefinition="A free text natural language name identifying the StructureDefinition." )
    protected StringType name;

    /**
     * Defined so that applications can use this name when displaying the value of the extension to the user.
     */
    @Child(name = "display", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Use this name when displaying the value", formalDefinition="Defined so that applications can use this name when displaying the value of the extension to the user." )
    protected StringType display;

    /**
     * The status of the StructureDefinition.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the StructureDefinition." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the structure definition.
     */
    @Child(name = "publisher", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the structure definition." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<StructureDefinitionContactComponent> contact;

    /**
     * The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for this version of the StructureDefinition", formalDefinition="The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the StructureDefinition and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Natural language description of the StructureDefinition", formalDefinition="A free text natural language description of the StructureDefinition and its use." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Content intends to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions." )
    protected List<CodeableConcept> useContext;

    /**
     * Explains why this structure definition is needed and why it's been constrained as it has.
     */
    @Child(name = "requirements", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Scope and Usage this structure definition is for", formalDefinition="Explains why this structure definition is needed and why it's been constrained as it has." )
    protected StringType requirements;

    /**
     * A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    @Child(name = "copyright", type = {StringType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or Publishing restrictions", formalDefinition="A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings." )
    protected StringType copyright;

    /**
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     */
    @Child(name = "code", type = {Coding.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Assist with indexing and finding", formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates." )
    protected List<Coding> code;

    /**
     * The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="FHIR Version this StructureDefinition targets", formalDefinition="The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version." )
    protected IdType fhirVersion;

    /**
     * An external specification that the content is mapped to.
     */
    @Child(name = "mapping", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External specification that the content is mapped to", formalDefinition="An external specification that the content is mapped to." )
    protected List<StructureDefinitionMappingComponent> mapping;

    /**
     * Defines the kind of structure that this definition is describing.
     */
    @Child(name = "kind", type = {CodeType.class}, order=17, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="datatype | resource | logical", formalDefinition="Defines the kind of structure that this definition is describing." )
    protected Enumeration<StructureDefinitionKind> kind;

    /**
     * The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure.
     */
    @Child(name = "constrainedType", type = {CodeType.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Any datatype or resource, including abstract ones", formalDefinition="The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure." )
    protected CodeType constrainedType;

    /**
     * Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type.
     */
    @Child(name = "abstract", type = {BooleanType.class}, order=19, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the structure is abstract", formalDefinition="Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type." )
    protected BooleanType abstract_;

    /**
     * If this is an extension, Identifies the context within FHIR resources where the extension can be used.
     */
    @Child(name = "contextType", type = {CodeType.class}, order=20, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="resource | datatype | mapping | extension", formalDefinition="If this is an extension, Identifies the context within FHIR resources where the extension can be used." )
    protected Enumeration<ExtensionContext> contextType;

    /**
     * Identifies the types of resource or data type elements to which the extension can be applied.
     */
    @Child(name = "context", type = {StringType.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Where the extension can be used in instances", formalDefinition="Identifies the types of resource or data type elements to which the extension can be applied." )
    protected List<StringType> context;

    /**
     * An absolute URI that is the base structure from which this set of constraints is derived.
     */
    @Child(name = "base", type = {UriType.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Structure that this set of constraints applies to", formalDefinition="An absolute URI that is the base structure from which this set of constraints is derived." )
    protected UriType base;

    /**
     * A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition.
     */
    @Child(name = "snapshot", type = {}, order=23, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Snapshot view of the structure", formalDefinition="A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition." )
    protected StructureDefinitionSnapshotComponent snapshot;

    /**
     * A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.
     */
    @Child(name = "differential", type = {}, order=24, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Differential view of the structure", formalDefinition="A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies." )
    protected StructureDefinitionDifferentialComponent differential;

    private static final long serialVersionUID = -580779569L;

  /*
   * Constructor
   */
    public StructureDefinition() {
      super();
    }

  /*
   * Constructor
   */
    public StructureDefinition(UriType url, StringType name, Enumeration<ConformanceResourceStatus> status, Enumeration<StructureDefinitionKind> kind, BooleanType abstract_) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
      this.kind = kind;
      this.abstract_ = abstract_;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public StructureDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published.
     */
    public StructureDefinition setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #identifier} (Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).)
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
     * @return {@link #identifier} (Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public StructureDefinition addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
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
     * @param value {@link #version} (The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StructureDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually.
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
     * @return {@link #name} (A free text natural language name identifying the StructureDefinition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (A free text natural language name identifying the StructureDefinition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StructureDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the StructureDefinition.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the StructureDefinition.
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
     * @return {@link #status} (The status of the StructureDefinition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.status");
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
     * @param value {@link #status} (The status of the StructureDefinition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public StructureDefinition setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the StructureDefinition.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the StructureDefinition.
     */
    public StructureDefinition setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public StructureDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public StructureDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the structure definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
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
     * @param value {@link #publisher} (The name of the individual or organization that published the structure definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StructureDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the structure definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the structure definition.
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
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    public List<StructureDefinitionContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<StructureDefinitionContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (StructureDefinitionContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public StructureDefinitionContactComponent addContact() { //3
      StructureDefinitionContactComponent t = new StructureDefinitionContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<StructureDefinitionContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public StructureDefinition addContact(StructureDefinitionContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<StructureDefinitionContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public StructureDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes.
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
     * @return {@link #description} (A free text natural language description of the StructureDefinition and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
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
     * @param value {@link #description} (A free text natural language description of the StructureDefinition and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StructureDefinition setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the StructureDefinition and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the StructureDefinition and its use.
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.)
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.)
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
    public StructureDefinition addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #requirements} (Explains why this structure definition is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
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
     * @param value {@link #requirements} (Explains why this structure definition is needed and why it's been constrained as it has.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StructureDefinition setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return Explains why this structure definition is needed and why it's been constrained as it has.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value Explains why this structure definition is needed and why it's been constrained as it has.
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
     * @return {@link #copyright} (A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StructureDefinition setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public StructureDefinition setCopyright(String value) { 
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

    // syntactic sugar
    public StructureDefinition addCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
      return this;
    }

    /**
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
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
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public StructureDefinition setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.
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

    // syntactic sugar
    public StructureDefinition addMapping(StructureDefinitionMappingComponent t) { //3
      if (t == null)
        return this;
      if (this.mapping == null)
        this.mapping = new ArrayList<StructureDefinitionMappingComponent>();
      this.mapping.add(t);
      return this;
    }

    /**
     * @return {@link #kind} (Defines the kind of structure that this definition is describing.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public Enumeration<StructureDefinitionKind> getKindElement() { 
      if (this.kind == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.kind");
        else if (Configuration.doAutoCreate())
          this.kind = new Enumeration<StructureDefinitionKind>(new StructureDefinitionKindEnumFactory()); // bb
      return this.kind;
    }

    public boolean hasKindElement() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    public boolean hasKind() { 
      return this.kind != null && !this.kind.isEmpty();
    }

    /**
     * @param value {@link #kind} (Defines the kind of structure that this definition is describing.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
     */
    public StructureDefinition setKindElement(Enumeration<StructureDefinitionKind> value) { 
      this.kind = value;
      return this;
    }

    /**
     * @return Defines the kind of structure that this definition is describing.
     */
    public StructureDefinitionKind getKind() { 
      return this.kind == null ? null : this.kind.getValue();
    }

    /**
     * @param value Defines the kind of structure that this definition is describing.
     */
    public StructureDefinition setKind(StructureDefinitionKind value) { 
        if (this.kind == null)
          this.kind = new Enumeration<StructureDefinitionKind>(new StructureDefinitionKindEnumFactory());
        this.kind.setValue(value);
      return this;
    }

    /**
     * @return {@link #constrainedType} (The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure.). This is the underlying object with id, value and extensions. The accessor "getConstrainedType" gives direct access to the value
     */
    public CodeType getConstrainedTypeElement() { 
      if (this.constrainedType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.constrainedType");
        else if (Configuration.doAutoCreate())
          this.constrainedType = new CodeType(); // bb
      return this.constrainedType;
    }

    public boolean hasConstrainedTypeElement() { 
      return this.constrainedType != null && !this.constrainedType.isEmpty();
    }

    public boolean hasConstrainedType() { 
      return this.constrainedType != null && !this.constrainedType.isEmpty();
    }

    /**
     * @param value {@link #constrainedType} (The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure.). This is the underlying object with id, value and extensions. The accessor "getConstrainedType" gives direct access to the value
     */
    public StructureDefinition setConstrainedTypeElement(CodeType value) { 
      this.constrainedType = value;
      return this;
    }

    /**
     * @return The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure.
     */
    public String getConstrainedType() { 
      return this.constrainedType == null ? null : this.constrainedType.getValue();
    }

    /**
     * @param value The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure.
     */
    public StructureDefinition setConstrainedType(String value) { 
      if (Utilities.noString(value))
        this.constrainedType = null;
      else {
        if (this.constrainedType == null)
          this.constrainedType = new CodeType();
        this.constrainedType.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #abstract_} (Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type.). This is the underlying object with id, value and extensions. The accessor "getAbstract" gives direct access to the value
     */
    public BooleanType getAbstractElement() { 
      if (this.abstract_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create StructureDefinition.abstract_");
        else if (Configuration.doAutoCreate())
          this.abstract_ = new BooleanType(); // bb
      return this.abstract_;
    }

    public boolean hasAbstractElement() { 
      return this.abstract_ != null && !this.abstract_.isEmpty();
    }

    public boolean hasAbstract() { 
      return this.abstract_ != null && !this.abstract_.isEmpty();
    }

    /**
     * @param value {@link #abstract_} (Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type.). This is the underlying object with id, value and extensions. The accessor "getAbstract" gives direct access to the value
     */
    public StructureDefinition setAbstractElement(BooleanType value) { 
      this.abstract_ = value;
      return this;
    }

    /**
     * @return Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type.
     */
    public boolean getAbstract() { 
      return this.abstract_ == null || this.abstract_.isEmpty() ? false : this.abstract_.getValue();
    }

    /**
     * @param value Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type.
     */
    public StructureDefinition setAbstract(boolean value) { 
        if (this.abstract_ == null)
          this.abstract_ = new BooleanType();
        this.abstract_.setValue(value);
      return this;
    }

    /**
     * @return {@link #contextType} (If this is an extension, Identifies the context within FHIR resources where the extension can be used.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
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
     * @param value {@link #contextType} (If this is an extension, Identifies the context within FHIR resources where the extension can be used.). This is the underlying object with id, value and extensions. The accessor "getContextType" gives direct access to the value
     */
    public StructureDefinition setContextTypeElement(Enumeration<ExtensionContext> value) { 
      this.contextType = value;
      return this;
    }

    /**
     * @return If this is an extension, Identifies the context within FHIR resources where the extension can be used.
     */
    public ExtensionContext getContextType() { 
      return this.contextType == null ? null : this.contextType.getValue();
    }

    /**
     * @param value If this is an extension, Identifies the context within FHIR resources where the extension can be used.
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
     * @return {@link #base} (An absolute URI that is the base structure from which this set of constraints is derived.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
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
     * @param value {@link #base} (An absolute URI that is the base structure from which this set of constraints is derived.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public StructureDefinition setBaseElement(UriType value) { 
      this.base = value;
      return this;
    }

    /**
     * @return An absolute URI that is the base structure from which this set of constraints is derived.
     */
    public String getBase() { 
      return this.base == null ? null : this.base.getValue();
    }

    /**
     * @param value An absolute URI that is the base structure from which this set of constraints is derived.
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
     * @return {@link #snapshot} (A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition.)
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
     * @param value {@link #snapshot} (A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition.)
     */
    public StructureDefinition setSnapshot(StructureDefinitionSnapshotComponent value) { 
      this.snapshot = value;
      return this;
    }

    /**
     * @return {@link #differential} (A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.)
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
     * @param value {@link #differential} (A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.)
     */
    public StructureDefinition setDifferential(StructureDefinitionDifferentialComponent value) { 
      this.differential = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this structure definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this structure definition is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "Formal identifier that is used to identify this StructureDefinition when it is represented in other formats, or referenced in a specification, model, design or an instance  (should be globally unique OID, UUID, or URI), (if it's not possible to use the literal URI).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the StructureDefinition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the StructureDefinition author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the StructureDefinition.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("display", "string", "Defined so that applications can use this name when displaying the value of the extension to the user.", 0, java.lang.Integer.MAX_VALUE, display));
        childrenList.add(new Property("status", "code", "The status of the StructureDefinition.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This StructureDefinition was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the structure definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date that this version of the StructureDefinition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the structure definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the StructureDefinition and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of structure definitions.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("requirements", "string", "Explains why this structure definition is needed and why it's been constrained as it has.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the structure definition and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("code", "Coding", "A set of terms from external terminologies that may be used to assist with indexing and searching of templates.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this StructureDefinition is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.", 0, java.lang.Integer.MAX_VALUE, fhirVersion));
        childrenList.add(new Property("mapping", "", "An external specification that the content is mapped to.", 0, java.lang.Integer.MAX_VALUE, mapping));
        childrenList.add(new Property("kind", "code", "Defines the kind of structure that this definition is describing.", 0, java.lang.Integer.MAX_VALUE, kind));
        childrenList.add(new Property("constrainedType", "code", "The type of type that is being constrained - a data type, an extension, a resource, including abstract ones. If this field is present, it indicates that the structure definition is a constraint. If it is not present, then the structure definition is the definition of a base structure.", 0, java.lang.Integer.MAX_VALUE, constrainedType));
        childrenList.add(new Property("abstract", "boolean", "Whether structure this definition describes is abstract or not  - that is, whether an actual exchanged item can ever be of this type.", 0, java.lang.Integer.MAX_VALUE, abstract_));
        childrenList.add(new Property("contextType", "code", "If this is an extension, Identifies the context within FHIR resources where the extension can be used.", 0, java.lang.Integer.MAX_VALUE, contextType));
        childrenList.add(new Property("context", "string", "Identifies the types of resource or data type elements to which the extension can be applied.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("base", "uri", "An absolute URI that is the base structure from which this set of constraints is derived.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("snapshot", "", "A snapshot view is expressed in a stand alone form that can be used and interpreted without considering the base StructureDefinition.", 0, java.lang.Integer.MAX_VALUE, snapshot));
        childrenList.add(new Property("differential", "", "A differential view is expressed relative to the base StructureDefinition - a statement of differences that it applies.", 0, java.lang.Integer.MAX_VALUE, differential));
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
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<StructureDefinitionContactComponent>();
          for (StructureDefinitionContactComponent i : contact)
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
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        if (mapping != null) {
          dst.mapping = new ArrayList<StructureDefinitionMappingComponent>();
          for (StructureDefinitionMappingComponent i : mapping)
            dst.mapping.add(i.copy());
        };
        dst.kind = kind == null ? null : kind.copy();
        dst.constrainedType = constrainedType == null ? null : constrainedType.copy();
        dst.abstract_ = abstract_ == null ? null : abstract_.copy();
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
           && compareDeep(name, o.name, true) && compareDeep(display, o.display, true) && compareDeep(status, o.status, true)
           && compareDeep(experimental, o.experimental, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true) && compareDeep(description, o.description, true)
           && compareDeep(useContext, o.useContext, true) && compareDeep(requirements, o.requirements, true)
           && compareDeep(copyright, o.copyright, true) && compareDeep(code, o.code, true) && compareDeep(fhirVersion, o.fhirVersion, true)
           && compareDeep(mapping, o.mapping, true) && compareDeep(kind, o.kind, true) && compareDeep(constrainedType, o.constrainedType, true)
           && compareDeep(abstract_, o.abstract_, true) && compareDeep(contextType, o.contextType, true) && compareDeep(context, o.context, true)
           && compareDeep(base, o.base, true) && compareDeep(snapshot, o.snapshot, true) && compareDeep(differential, o.differential, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StructureDefinition))
          return false;
        StructureDefinition o = (StructureDefinition) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(display, o.display, true) && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true)
           && compareValues(publisher, o.publisher, true) && compareValues(date, o.date, true) && compareValues(description, o.description, true)
           && compareValues(requirements, o.requirements, true) && compareValues(copyright, o.copyright, true)
           && compareValues(fhirVersion, o.fhirVersion, true) && compareValues(kind, o.kind, true) && compareValues(constrainedType, o.constrainedType, true)
           && compareValues(abstract_, o.abstract_, true) && compareValues(contextType, o.contextType, true) && compareValues(context, o.context, true)
           && compareValues(base, o.base, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (name == null || name.isEmpty()) && (display == null || display.isEmpty())
           && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty())
           && (description == null || description.isEmpty()) && (useContext == null || useContext.isEmpty())
           && (requirements == null || requirements.isEmpty()) && (copyright == null || copyright.isEmpty())
           && (code == null || code.isEmpty()) && (fhirVersion == null || fhirVersion.isEmpty()) && (mapping == null || mapping.isEmpty())
           && (kind == null || kind.isEmpty()) && (constrainedType == null || constrainedType.isEmpty())
           && (abstract_ == null || abstract_.isEmpty()) && (contextType == null || contextType.isEmpty())
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
  @SearchParamDefinition(name="code", path="StructureDefinition.code", description="A code for the profile", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="valueset", path="StructureDefinition.snapshot.element.binding.valueSet[x]", description="A vocabulary binding reference", type="reference" )
  public static final String SP_VALUESET = "valueset";
  @SearchParamDefinition(name="kind", path="StructureDefinition.kind", description="datatype | resource | logical", type="token" )
  public static final String SP_KIND = "kind";
  @SearchParamDefinition(name="display", path="StructureDefinition.display", description="Use this name when displaying the value", type="string" )
  public static final String SP_DISPLAY = "display";
  @SearchParamDefinition(name="description", path="StructureDefinition.description", description="Text search in the description of the profile", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="experimental", path="StructureDefinition.experimental", description="If for testing purposes, not real usage", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
  @SearchParamDefinition(name="context-type", path="StructureDefinition.contextType", description="resource | datatype | mapping | extension", type="token" )
  public static final String SP_CONTEXTTYPE = "context-type";
  @SearchParamDefinition(name="abstract", path="StructureDefinition.abstract", description="Whether the structure is abstract", type="token" )
  public static final String SP_ABSTRACT = "abstract";
  @SearchParamDefinition(name="type", path="StructureDefinition.constrainedType", description="Any datatype or resource, including abstract ones", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="version", path="StructureDefinition.version", description="The version identifier of the profile", type="token" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="url", path="StructureDefinition.url", description="Literal URL used to reference this StructureDefinition", type="uri" )
  public static final String SP_URL = "url";
  @SearchParamDefinition(name="path", path="StructureDefinition.snapshot.element.path|StructureDefinition.differential.element.path", description="A path that is constrained in the profile", type="token" )
  public static final String SP_PATH = "path";
  @SearchParamDefinition(name="ext-context", path="StructureDefinition.context", description="Where the extension can be used in instances", type="string" )
  public static final String SP_EXTCONTEXT = "ext-context";
  @SearchParamDefinition(name="name", path="StructureDefinition.name", description="Name of the profile", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="context", path="StructureDefinition.useContext", description="A use context assigned to the structure", type="token" )
  public static final String SP_CONTEXT = "context";
  @SearchParamDefinition(name="base-path", path="StructureDefinition.snapshot.element.base.path|StructureDefinition.differential.element.base.path", description="Path that identifies the base element", type="token" )
  public static final String SP_BASEPATH = "base-path";
  @SearchParamDefinition(name="publisher", path="StructureDefinition.publisher", description="Name of the publisher of the profile", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="status", path="StructureDefinition.status", description="The current status of the profile", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="base", path="StructureDefinition.base", description="Structure that this set of constraints applies to", type="uri" )
  public static final String SP_BASE = "base";

}

