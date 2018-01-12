package org.hl7.fhir.r4.model;

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

// Generated on Tue, Jan 9, 2018 14:51-0500 for FHIR v3.2.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A set of rules of how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole and to publish a computable definition of all the parts.
 */
@ResourceDef(name="ImplementationGuideInput", profile="http://hl7.org/fhir/Profile/ImplementationGuideInput")
@ChildOrder(names={"url", "version", "name", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "copyright", "fhirVersion", "dependency", "package", "global", "page"})
public class ImplementationGuideInput extends MetadataResource {

    public enum GuideDependencyType {
        /**
         * The guide is referred to by URL.
         */
        REFERENCE, 
        /**
         * The guide is embedded in this guide when published.
         */
        INCLUSION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GuideDependencyType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("inclusion".equals(codeString))
          return INCLUSION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GuideDependencyType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REFERENCE: return "reference";
            case INCLUSION: return "inclusion";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REFERENCE: return "http://hl7.org/fhir/guide-dependency-type";
            case INCLUSION: return "http://hl7.org/fhir/guide-dependency-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REFERENCE: return "The guide is referred to by URL.";
            case INCLUSION: return "The guide is embedded in this guide when published.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REFERENCE: return "Reference";
            case INCLUSION: return "Inclusion";
            default: return "?";
          }
        }
    }

  public static class GuideDependencyTypeEnumFactory implements EnumFactory<GuideDependencyType> {
    public GuideDependencyType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("reference".equals(codeString))
          return GuideDependencyType.REFERENCE;
        if ("inclusion".equals(codeString))
          return GuideDependencyType.INCLUSION;
        throw new IllegalArgumentException("Unknown GuideDependencyType code '"+codeString+"'");
        }
        public Enumeration<GuideDependencyType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GuideDependencyType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("reference".equals(codeString))
          return new Enumeration<GuideDependencyType>(this, GuideDependencyType.REFERENCE);
        if ("inclusion".equals(codeString))
          return new Enumeration<GuideDependencyType>(this, GuideDependencyType.INCLUSION);
        throw new FHIRException("Unknown GuideDependencyType code '"+codeString+"'");
        }
    public String toCode(GuideDependencyType code) {
      if (code == GuideDependencyType.REFERENCE)
        return "reference";
      if (code == GuideDependencyType.INCLUSION)
        return "inclusion";
      return "?";
      }
    public String toSystem(GuideDependencyType code) {
      return code.getSystem();
      }
    }

    public enum GuidePageKind {
        /**
         * This is a page of content that is included in the implementation guide. It has no particular function.
         */
        PAGE, 
        /**
         * This is a page that represents a human readable rendering of an example.
         */
        EXAMPLE, 
        /**
         * This is a page that represents a list of resources of one or more types.
         */
        LIST, 
        /**
         * This is a page showing where an included guide is injected.
         */
        INCLUDE, 
        /**
         * This is a page that lists the resources of a given type, and also creates pages for all the listed types as other pages in the section.
         */
        DIRECTORY, 
        /**
         * This is a page that creates the listed resources as a dictionary.
         */
        DICTIONARY, 
        /**
         * This is a generated page that contains the table of contents.
         */
        TOC, 
        /**
         * This is a page that represents a presented resource. This is typically used for generated conformance resource presentations.
         */
        RESOURCE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GuidePageKind fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("page".equals(codeString))
          return PAGE;
        if ("example".equals(codeString))
          return EXAMPLE;
        if ("list".equals(codeString))
          return LIST;
        if ("include".equals(codeString))
          return INCLUDE;
        if ("directory".equals(codeString))
          return DIRECTORY;
        if ("dictionary".equals(codeString))
          return DICTIONARY;
        if ("toc".equals(codeString))
          return TOC;
        if ("resource".equals(codeString))
          return RESOURCE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GuidePageKind code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PAGE: return "page";
            case EXAMPLE: return "example";
            case LIST: return "list";
            case INCLUDE: return "include";
            case DIRECTORY: return "directory";
            case DICTIONARY: return "dictionary";
            case TOC: return "toc";
            case RESOURCE: return "resource";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PAGE: return "http://hl7.org/fhir/guide-page-kind";
            case EXAMPLE: return "http://hl7.org/fhir/guide-page-kind";
            case LIST: return "http://hl7.org/fhir/guide-page-kind";
            case INCLUDE: return "http://hl7.org/fhir/guide-page-kind";
            case DIRECTORY: return "http://hl7.org/fhir/guide-page-kind";
            case DICTIONARY: return "http://hl7.org/fhir/guide-page-kind";
            case TOC: return "http://hl7.org/fhir/guide-page-kind";
            case RESOURCE: return "http://hl7.org/fhir/guide-page-kind";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PAGE: return "This is a page of content that is included in the implementation guide. It has no particular function.";
            case EXAMPLE: return "This is a page that represents a human readable rendering of an example.";
            case LIST: return "This is a page that represents a list of resources of one or more types.";
            case INCLUDE: return "This is a page showing where an included guide is injected.";
            case DIRECTORY: return "This is a page that lists the resources of a given type, and also creates pages for all the listed types as other pages in the section.";
            case DICTIONARY: return "This is a page that creates the listed resources as a dictionary.";
            case TOC: return "This is a generated page that contains the table of contents.";
            case RESOURCE: return "This is a page that represents a presented resource. This is typically used for generated conformance resource presentations.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PAGE: return "Page";
            case EXAMPLE: return "Example";
            case LIST: return "List";
            case INCLUDE: return "Include";
            case DIRECTORY: return "Directory";
            case DICTIONARY: return "Dictionary";
            case TOC: return "Table Of Contents";
            case RESOURCE: return "Resource";
            default: return "?";
          }
        }
    }

  public static class GuidePageKindEnumFactory implements EnumFactory<GuidePageKind> {
    public GuidePageKind fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("page".equals(codeString))
          return GuidePageKind.PAGE;
        if ("example".equals(codeString))
          return GuidePageKind.EXAMPLE;
        if ("list".equals(codeString))
          return GuidePageKind.LIST;
        if ("include".equals(codeString))
          return GuidePageKind.INCLUDE;
        if ("directory".equals(codeString))
          return GuidePageKind.DIRECTORY;
        if ("dictionary".equals(codeString))
          return GuidePageKind.DICTIONARY;
        if ("toc".equals(codeString))
          return GuidePageKind.TOC;
        if ("resource".equals(codeString))
          return GuidePageKind.RESOURCE;
        throw new IllegalArgumentException("Unknown GuidePageKind code '"+codeString+"'");
        }
        public Enumeration<GuidePageKind> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GuidePageKind>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("page".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.PAGE);
        if ("example".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.EXAMPLE);
        if ("list".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.LIST);
        if ("include".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.INCLUDE);
        if ("directory".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.DIRECTORY);
        if ("dictionary".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.DICTIONARY);
        if ("toc".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.TOC);
        if ("resource".equals(codeString))
          return new Enumeration<GuidePageKind>(this, GuidePageKind.RESOURCE);
        throw new FHIRException("Unknown GuidePageKind code '"+codeString+"'");
        }
    public String toCode(GuidePageKind code) {
      if (code == GuidePageKind.PAGE)
        return "page";
      if (code == GuidePageKind.EXAMPLE)
        return "example";
      if (code == GuidePageKind.LIST)
        return "list";
      if (code == GuidePageKind.INCLUDE)
        return "include";
      if (code == GuidePageKind.DIRECTORY)
        return "directory";
      if (code == GuidePageKind.DICTIONARY)
        return "dictionary";
      if (code == GuidePageKind.TOC)
        return "toc";
      if (code == GuidePageKind.RESOURCE)
        return "resource";
      return "?";
      }
    public String toSystem(GuidePageKind code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ImplementationGuideInputDependencyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the dependency is represented when the guide is published.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="reference | inclusion", formalDefinition="How the dependency is represented when the guide is published." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/guide-dependency-type")
        protected Enumeration<GuideDependencyType> type;

        /**
         * Where the dependency is located.
         */
        @Child(name = "uri", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Where to find dependency", formalDefinition="Where the dependency is located." )
        protected UriType uri;

        private static final long serialVersionUID = 162447098L;

    /**
     * Constructor
     */
      public ImplementationGuideInputDependencyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputDependencyComponent(Enumeration<GuideDependencyType> type, UriType uri) {
        super();
        this.type = type;
        this.uri = uri;
      }

        /**
         * @return {@link #type} (How the dependency is represented when the guide is published.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<GuideDependencyType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDependencyComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<GuideDependencyType>(new GuideDependencyTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (How the dependency is represented when the guide is published.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ImplementationGuideInputDependencyComponent setTypeElement(Enumeration<GuideDependencyType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return How the dependency is represented when the guide is published.
         */
        public GuideDependencyType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value How the dependency is represented when the guide is published.
         */
        public ImplementationGuideInputDependencyComponent setType(GuideDependencyType value) { 
            if (this.type == null)
              this.type = new Enumeration<GuideDependencyType>(new GuideDependencyTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #uri} (Where the dependency is located.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputDependencyComponent.uri");
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
         * @param value {@link #uri} (Where the dependency is located.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public ImplementationGuideInputDependencyComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return Where the dependency is located.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value Where the dependency is located.
         */
        public ImplementationGuideInputDependencyComponent setUri(String value) { 
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "How the dependency is represented when the guide is published.", 0, 1, type));
          children.add(new Property("uri", "uri", "Where the dependency is located.", 0, 1, uri));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "How the dependency is represented when the guide is published.", 0, 1, type);
          case 116076: /*uri*/  return new Property("uri", "uri", "Where the dependency is located.", 0, 1, uri);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<GuideDependencyType>
        case 116076: /*uri*/ return this.uri == null ? new Base[0] : new Base[] {this.uri}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new GuideDependencyTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<GuideDependencyType>
          return value;
        case 116076: // uri
          this.uri = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new GuideDependencyTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<GuideDependencyType>
        } else if (name.equals("uri")) {
          this.uri = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 116076:  return getUriElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 116076: /*uri*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.type");
        }
        else if (name.equals("uri")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.uri");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputDependencyComponent copy() {
        ImplementationGuideInputDependencyComponent dst = new ImplementationGuideInputDependencyComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.uri = uri == null ? null : uri.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDependencyComponent))
          return false;
        ImplementationGuideInputDependencyComponent o = (ImplementationGuideInputDependencyComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(uri, o.uri, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputDependencyComponent))
          return false;
        ImplementationGuideInputDependencyComponent o = (ImplementationGuideInputDependencyComponent) other_;
        return compareValues(type, o.type, true) && compareValues(uri, o.uri, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, uri);
      }

  public String fhirType() {
    return "ImplementationGuideInput.dependency";

  }

  }

    @Block()
    public static class ImplementationGuideInputPackageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name for the group, as used in page.package.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name used .page.package", formalDefinition="The name for the group, as used in page.package." )
        protected StringType name;

        /**
         * Human readable text describing the package.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human readable text describing the package", formalDefinition="Human readable text describing the package." )
        protected StringType description;

        /**
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.
         */
        @Child(name = "resource", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Resource in the implementation guide", formalDefinition="A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource." )
        protected List<ImplementationGuideInputPackageResourceComponent> resource;

        private static final long serialVersionUID = 820676840L;

    /**
     * Constructor
     */
      public ImplementationGuideInputPackageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputPackageComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The name for the group, as used in page.package.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPackageComponent.name");
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
         * @param value {@link #name} (The name for the group, as used in page.package.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideInputPackageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name for the group, as used in page.package.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name for the group, as used in page.package.
         */
        public ImplementationGuideInputPackageComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Human readable text describing the package.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPackageComponent.description");
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
         * @param value {@link #description} (Human readable text describing the package.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImplementationGuideInputPackageComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Human readable text describing the package.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Human readable text describing the package.
         */
        public ImplementationGuideInputPackageComponent setDescription(String value) { 
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
         * @return {@link #resource} (A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.)
         */
        public List<ImplementationGuideInputPackageResourceComponent> getResource() { 
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideInputPackageResourceComponent>();
          return this.resource;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputPackageComponent setResource(List<ImplementationGuideInputPackageResourceComponent> theResource) { 
          this.resource = theResource;
          return this;
        }

        public boolean hasResource() { 
          if (this.resource == null)
            return false;
          for (ImplementationGuideInputPackageResourceComponent item : this.resource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideInputPackageResourceComponent addResource() { //3
          ImplementationGuideInputPackageResourceComponent t = new ImplementationGuideInputPackageResourceComponent();
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideInputPackageResourceComponent>();
          this.resource.add(t);
          return t;
        }

        public ImplementationGuideInputPackageComponent addResource(ImplementationGuideInputPackageResourceComponent t) { //3
          if (t == null)
            return this;
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuideInputPackageResourceComponent>();
          this.resource.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #resource}, creating it if it does not already exist
         */
        public ImplementationGuideInputPackageResourceComponent getResourceFirstRep() { 
          if (getResource().isEmpty()) {
            addResource();
          }
          return getResource().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "The name for the group, as used in page.package.", 0, 1, name));
          children.add(new Property("description", "string", "Human readable text describing the package.", 0, 1, description));
          children.add(new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "The name for the group, as used in page.package.", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "string", "Human readable text describing the package.", 0, 1, description);
          case -341064690: /*resource*/  return new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, capability statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // ImplementationGuideInputPackageResourceComponent
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
        case -341064690: // resource
          this.getResource().add((ImplementationGuideInputPackageResourceComponent) value); // ImplementationGuideInputPackageResourceComponent
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
        } else if (name.equals("resource")) {
          this.getResource().add((ImplementationGuideInputPackageResourceComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case -341064690:  return addResource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -341064690: /*resource*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.description");
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputPackageComponent copy() {
        ImplementationGuideInputPackageComponent dst = new ImplementationGuideInputPackageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        if (resource != null) {
          dst.resource = new ArrayList<ImplementationGuideInputPackageResourceComponent>();
          for (ImplementationGuideInputPackageResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputPackageComponent))
          return false;
        ImplementationGuideInputPackageComponent o = (ImplementationGuideInputPackageComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(resource, o.resource, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputPackageComponent))
          return false;
        ImplementationGuideInputPackageComponent o = (ImplementationGuideInputPackageComponent) other_;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, description, resource
          );
      }

  public String fhirType() {
    return "ImplementationGuideInput.package";

  }

  }

    @Block()
    public static class ImplementationGuideInputPackageResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Where this resource is found.
         */
        @Child(name = "reference", type = {Reference.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Location of the resource", formalDefinition="Where this resource is found." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Where this resource is found.)
         */
        protected Resource referenceTarget;

        /**
         * A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human Name for the resource", formalDefinition="A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name)." )
        protected StringType name;

        /**
         * A description of the reason that a resource has been included in the implementation guide.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason why included in guide", formalDefinition="A description of the reason that a resource has been included in the implementation guide." )
        protected StringType description;

        /**
         * If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.
         */
        @Child(name = "example", type = {BooleanType.class, StructureDefinition.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is an example/What is this an example of?", formalDefinition="If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile." )
        protected Type example;

        private static final long serialVersionUID = 1734191349L;

    /**
     * Constructor
     */
      public ImplementationGuideInputPackageResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputPackageResourceComponent(Reference reference) {
        super();
        this.reference = reference;
      }

        /**
         * @return {@link #reference} (Where this resource is found.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPackageResourceComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Where this resource is found.)
         */
        public ImplementationGuideInputPackageResourceComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where this resource is found.)
         */
        public ImplementationGuideInputPackageResourceComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #name} (A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPackageResourceComponent.name");
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
         * @param value {@link #name} (A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideInputPackageResourceComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).
         */
        public ImplementationGuideInputPackageResourceComponent setName(String value) { 
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
         * @return {@link #description} (A description of the reason that a resource has been included in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPackageResourceComponent.description");
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
         * @param value {@link #description} (A description of the reason that a resource has been included in the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImplementationGuideInputPackageResourceComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A description of the reason that a resource has been included in the implementation guide.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A description of the reason that a resource has been included in the implementation guide.
         */
        public ImplementationGuideInputPackageResourceComponent setDescription(String value) { 
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
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public Type getExample() { 
          return this.example;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public BooleanType getExampleBooleanType() throws FHIRException { 
          if (!(this.example instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.example.getClass().getName()+" was encountered");
          return (BooleanType) this.example;
        }

        public boolean hasExampleBooleanType() { 
          return this.example instanceof BooleanType;
        }

        /**
         * @return {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public Reference getExampleReference() throws FHIRException { 
          if (!(this.example instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.example.getClass().getName()+" was encountered");
          return (Reference) this.example;
        }

        public boolean hasExampleReference() { 
          return this.example instanceof Reference;
        }

        public boolean hasExample() { 
          return this.example != null && !this.example.isEmpty();
        }

        /**
         * @param value {@link #example} (If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.)
         */
        public ImplementationGuideInputPackageResourceComponent setExample(Type value) { 
          this.example = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference));
          children.add(new Property("name", "string", "A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).", 0, 1, name));
          children.add(new Property("description", "string", "A description of the reason that a resource has been included in the implementation guide.", 0, 1, description));
          children.add(new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "Where this resource is found.", 0, 1, reference);
          case 3373707: /*name*/  return new Property("name", "string", "A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).", 0, 1, name);
          case -1724546052: /*description*/  return new Property("description", "string", "A description of the reason that a resource has been included in the implementation guide.", 0, 1, description);
          case -2002328874: /*example[x]*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case -1322970774: /*example*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 159803230: /*exampleBoolean*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          case 263472385: /*exampleReference*/  return new Property("example[x]", "boolean|Reference(StructureDefinition)", "If true or a reference, indicates the resource is an example instance.  If a reference is present, indicates that the example is an example of the specified profile.", 0, 1, example);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1322970774: /*example*/ return this.example == null ? new Base[0] : new Base[] {this.example}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1322970774: // example
          this.example = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("example[x]")) {
          this.example = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return getReference(); 
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case -2002328874:  return getExample(); 
        case -1322970774:  return getExample(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1322970774: /*example*/ return new String[] {"boolean", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.description");
        }
        else if (name.equals("exampleBoolean")) {
          this.example = new BooleanType();
          return this.example;
        }
        else if (name.equals("exampleReference")) {
          this.example = new Reference();
          return this.example;
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputPackageResourceComponent copy() {
        ImplementationGuideInputPackageResourceComponent dst = new ImplementationGuideInputPackageResourceComponent();
        copyValues(dst);
        dst.reference = reference == null ? null : reference.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.example = example == null ? null : example.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputPackageResourceComponent))
          return false;
        ImplementationGuideInputPackageResourceComponent o = (ImplementationGuideInputPackageResourceComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(example, o.example, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputPackageResourceComponent))
          return false;
        ImplementationGuideInputPackageResourceComponent o = (ImplementationGuideInputPackageResourceComponent) other_;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, name, description
          , example);
      }

  public String fhirType() {
    return "ImplementationGuideInput.package.resource";

  }

  }

    @Block()
    public static class ImplementationGuideInputGlobalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of resource that all instances must conform to.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type this profiles applies to", formalDefinition="The type of resource that all instances must conform to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType type;

        /**
         * A reference to the profile that all instances must conform to.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Profile that all resources must conform to", formalDefinition="A reference to the profile that all instances must conform to." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (A reference to the profile that all instances must conform to.)
         */
        protected StructureDefinition profileTarget;

        private static final long serialVersionUID = 2011731959L;

    /**
     * Constructor
     */
      public ImplementationGuideInputGlobalComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputGlobalComponent(CodeType type, Reference profile) {
        super();
        this.type = type;
        this.profile = profile;
      }

        /**
         * @return {@link #type} (The type of resource that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputGlobalComponent.type");
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
         * @param value {@link #type} (The type of resource that all instances must conform to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ImplementationGuideInputGlobalComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of resource that all instances must conform to.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of resource that all instances must conform to.
         */
        public ImplementationGuideInputGlobalComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (A reference to the profile that all instances must conform to.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputGlobalComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (A reference to the profile that all instances must conform to.)
         */
        public ImplementationGuideInputGlobalComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the profile that all instances must conform to.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputGlobalComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the profile that all instances must conform to.)
         */
        public ImplementationGuideInputGlobalComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "The type of resource that all instances must conform to.", 0, 1, type));
          children.add(new Property("profile", "Reference(StructureDefinition)", "A reference to the profile that all instances must conform to.", 0, 1, profile));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "The type of resource that all instances must conform to.", 0, 1, type);
          case -309425751: /*profile*/  return new Property("profile", "Reference(StructureDefinition)", "A reference to the profile that all instances must conform to.", 0, 1, profile);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          return value;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.profile = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -309425751:  return getProfile(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.type");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputGlobalComponent copy() {
        ImplementationGuideInputGlobalComponent dst = new ImplementationGuideInputGlobalComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputGlobalComponent))
          return false;
        ImplementationGuideInputGlobalComponent o = (ImplementationGuideInputGlobalComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputGlobalComponent))
          return false;
        ImplementationGuideInputGlobalComponent o = (ImplementationGuideInputGlobalComponent) other_;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, profile);
      }

  public String fhirType() {
    return "ImplementationGuideInput.global";

  }

  }

    @Block()
    public static class ImplementationGuideInputPageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The source address for the page.
         */
        @Child(name = "source", type = {UriType.class, Binary.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Where to find that page", formalDefinition="The source address for the page." )
        protected Type source;

        /**
         * A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        @Child(name = "title", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Short title shown for navigational assistance", formalDefinition="A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc." )
        protected StringType title;

        /**
         * The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.
         */
        @Child(name = "kind", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="page | example | list | include | directory | dictionary | toc | resource", formalDefinition="The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/guide-page-kind")
        protected Enumeration<GuidePageKind> kind;

        /**
         * For constructed pages, what kind of resources to include in the list.
         */
        @Child(name = "type", type = {CodeType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Kind of resource to include in the list", formalDefinition="For constructed pages, what kind of resources to include in the list." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected List<CodeType> type;

        /**
         * For constructed pages, a list of packages to include in the page (or else empty for everything).
         */
        @Child(name = "package", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Name of package to include", formalDefinition="For constructed pages, a list of packages to include in the page (or else empty for everything)." )
        protected List<StringType> package_;

        /**
         * The format of the page.
         */
        @Child(name = "format", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Format of the page (e.g. html, markdown, etc.)", formalDefinition="The format of the page." )
        protected CodeType format;

        /**
         * Nested Pages/Sections under this page.
         */
        @Child(name = "page", type = {ImplementationGuideInputPageComponent.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Pages / Sections", formalDefinition="Nested Pages/Sections under this page." )
        protected List<ImplementationGuideInputPageComponent> page;

        private static final long serialVersionUID = -746243048L;

    /**
     * Constructor
     */
      public ImplementationGuideInputPageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideInputPageComponent(Type source, StringType title, Enumeration<GuidePageKind> kind) {
        super();
        this.source = source;
        this.title = title;
        this.kind = kind;
      }

        /**
         * @return {@link #source} (The source address for the page.)
         */
        public Type getSource() { 
          return this.source;
        }

        /**
         * @return {@link #source} (The source address for the page.)
         */
        public UriType getSourceUriType() throws FHIRException { 
          if (!(this.source instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.source.getClass().getName()+" was encountered");
          return (UriType) this.source;
        }

        public boolean hasSourceUriType() { 
          return this.source instanceof UriType;
        }

        /**
         * @return {@link #source} (The source address for the page.)
         */
        public Reference getSourceReference() throws FHIRException { 
          if (!(this.source instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.source.getClass().getName()+" was encountered");
          return (Reference) this.source;
        }

        public boolean hasSourceReference() { 
          return this.source instanceof Reference;
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The source address for the page.)
         */
        public ImplementationGuideInputPageComponent setSource(Type value) { 
          this.source = value;
          return this;
        }

        /**
         * @return {@link #title} (A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPageComponent.title");
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
         * @param value {@link #title} (A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public ImplementationGuideInputPageComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public ImplementationGuideInputPageComponent setTitle(String value) { 
            if (this.title == null)
              this.title = new StringType();
            this.title.setValue(value);
          return this;
        }

        /**
         * @return {@link #kind} (The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
         */
        public Enumeration<GuidePageKind> getKindElement() { 
          if (this.kind == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPageComponent.kind");
            else if (Configuration.doAutoCreate())
              this.kind = new Enumeration<GuidePageKind>(new GuidePageKindEnumFactory()); // bb
          return this.kind;
        }

        public boolean hasKindElement() { 
          return this.kind != null && !this.kind.isEmpty();
        }

        public boolean hasKind() { 
          return this.kind != null && !this.kind.isEmpty();
        }

        /**
         * @param value {@link #kind} (The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
         */
        public ImplementationGuideInputPageComponent setKindElement(Enumeration<GuidePageKind> value) { 
          this.kind = value;
          return this;
        }

        /**
         * @return The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.
         */
        public GuidePageKind getKind() { 
          return this.kind == null ? null : this.kind.getValue();
        }

        /**
         * @param value The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.
         */
        public ImplementationGuideInputPageComponent setKind(GuidePageKind value) { 
            if (this.kind == null)
              this.kind = new Enumeration<GuidePageKind>(new GuidePageKindEnumFactory());
            this.kind.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (For constructed pages, what kind of resources to include in the list.)
         */
        public List<CodeType> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeType>();
          return this.type;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputPageComponent setType(List<CodeType> theType) { 
          this.type = theType;
          return this;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeType item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #type} (For constructed pages, what kind of resources to include in the list.)
         */
        public CodeType addTypeElement() {//2 
          CodeType t = new CodeType();
          if (this.type == null)
            this.type = new ArrayList<CodeType>();
          this.type.add(t);
          return t;
        }

        /**
         * @param value {@link #type} (For constructed pages, what kind of resources to include in the list.)
         */
        public ImplementationGuideInputPageComponent addType(String value) { //1
          CodeType t = new CodeType();
          t.setValue(value);
          if (this.type == null)
            this.type = new ArrayList<CodeType>();
          this.type.add(t);
          return this;
        }

        /**
         * @param value {@link #type} (For constructed pages, what kind of resources to include in the list.)
         */
        public boolean hasType(String value) { 
          if (this.type == null)
            return false;
          for (CodeType v : this.type)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #package_} (For constructed pages, a list of packages to include in the page (or else empty for everything).)
         */
        public List<StringType> getPackage() { 
          if (this.package_ == null)
            this.package_ = new ArrayList<StringType>();
          return this.package_;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputPageComponent setPackage(List<StringType> thePackage) { 
          this.package_ = thePackage;
          return this;
        }

        public boolean hasPackage() { 
          if (this.package_ == null)
            return false;
          for (StringType item : this.package_)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #package_} (For constructed pages, a list of packages to include in the page (or else empty for everything).)
         */
        public StringType addPackageElement() {//2 
          StringType t = new StringType();
          if (this.package_ == null)
            this.package_ = new ArrayList<StringType>();
          this.package_.add(t);
          return t;
        }

        /**
         * @param value {@link #package_} (For constructed pages, a list of packages to include in the page (or else empty for everything).)
         */
        public ImplementationGuideInputPageComponent addPackage(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.package_ == null)
            this.package_ = new ArrayList<StringType>();
          this.package_.add(t);
          return this;
        }

        /**
         * @param value {@link #package_} (For constructed pages, a list of packages to include in the page (or else empty for everything).)
         */
        public boolean hasPackage(String value) { 
          if (this.package_ == null)
            return false;
          for (StringType v : this.package_)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #format} (The format of the page.). This is the underlying object with id, value and extensions. The accessor "getFormat" gives direct access to the value
         */
        public CodeType getFormatElement() { 
          if (this.format == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideInputPageComponent.format");
            else if (Configuration.doAutoCreate())
              this.format = new CodeType(); // bb
          return this.format;
        }

        public boolean hasFormatElement() { 
          return this.format != null && !this.format.isEmpty();
        }

        public boolean hasFormat() { 
          return this.format != null && !this.format.isEmpty();
        }

        /**
         * @param value {@link #format} (The format of the page.). This is the underlying object with id, value and extensions. The accessor "getFormat" gives direct access to the value
         */
        public ImplementationGuideInputPageComponent setFormatElement(CodeType value) { 
          this.format = value;
          return this;
        }

        /**
         * @return The format of the page.
         */
        public String getFormat() { 
          return this.format == null ? null : this.format.getValue();
        }

        /**
         * @param value The format of the page.
         */
        public ImplementationGuideInputPageComponent setFormat(String value) { 
          if (Utilities.noString(value))
            this.format = null;
          else {
            if (this.format == null)
              this.format = new CodeType();
            this.format.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #page} (Nested Pages/Sections under this page.)
         */
        public List<ImplementationGuideInputPageComponent> getPage() { 
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideInputPageComponent>();
          return this.page;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ImplementationGuideInputPageComponent setPage(List<ImplementationGuideInputPageComponent> thePage) { 
          this.page = thePage;
          return this;
        }

        public boolean hasPage() { 
          if (this.page == null)
            return false;
          for (ImplementationGuideInputPageComponent item : this.page)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ImplementationGuideInputPageComponent addPage() { //3
          ImplementationGuideInputPageComponent t = new ImplementationGuideInputPageComponent();
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideInputPageComponent>();
          this.page.add(t);
          return t;
        }

        public ImplementationGuideInputPageComponent addPage(ImplementationGuideInputPageComponent t) { //3
          if (t == null)
            return this;
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuideInputPageComponent>();
          this.page.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #page}, creating it if it does not already exist
         */
        public ImplementationGuideInputPageComponent getPageFirstRep() { 
          if (getPage().isEmpty()) {
            addPage();
          }
          return getPage().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("source[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, source));
          children.add(new Property("title", "string", "A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.", 0, 1, title));
          children.add(new Property("kind", "code", "The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.", 0, 1, kind));
          children.add(new Property("type", "code", "For constructed pages, what kind of resources to include in the list.", 0, java.lang.Integer.MAX_VALUE, type));
          children.add(new Property("package", "string", "For constructed pages, a list of packages to include in the page (or else empty for everything).", 0, java.lang.Integer.MAX_VALUE, package_));
          children.add(new Property("format", "code", "The format of the page.", 0, 1, format));
          children.add(new Property("page", "@ImplementationGuideInput.page", "Nested Pages/Sections under this page.", 0, java.lang.Integer.MAX_VALUE, page));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1698413947: /*source[x]*/  return new Property("source[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, source);
          case -896505829: /*source*/  return new Property("source[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, source);
          case -1698419887: /*sourceUri*/  return new Property("source[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, source);
          case -244259472: /*sourceReference*/  return new Property("source[x]", "uri|Reference(Binary)", "The source address for the page.", 0, 1, source);
          case 110371416: /*title*/  return new Property("title", "string", "A short title used to represent this page in navigational structures such as table of contents, bread crumbs, etc.", 0, 1, title);
          case 3292052: /*kind*/  return new Property("kind", "code", "The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.", 0, 1, kind);
          case 3575610: /*type*/  return new Property("type", "code", "For constructed pages, what kind of resources to include in the list.", 0, java.lang.Integer.MAX_VALUE, type);
          case -807062458: /*package*/  return new Property("package", "string", "For constructed pages, a list of packages to include in the page (or else empty for everything).", 0, java.lang.Integer.MAX_VALUE, package_);
          case -1268779017: /*format*/  return new Property("format", "code", "The format of the page.", 0, 1, format);
          case 3433103: /*page*/  return new Property("page", "@ImplementationGuideInput.page", "Nested Pages/Sections under this page.", 0, java.lang.Integer.MAX_VALUE, page);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Type
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 3292052: /*kind*/ return this.kind == null ? new Base[0] : new Base[] {this.kind}; // Enumeration<GuidePageKind>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeType
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : this.package_.toArray(new Base[this.package_.size()]); // StringType
        case -1268779017: /*format*/ return this.format == null ? new Base[0] : new Base[] {this.format}; // CodeType
        case 3433103: /*page*/ return this.page == null ? new Base[0] : this.page.toArray(new Base[this.page.size()]); // ImplementationGuideInputPageComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -896505829: // source
          this.source = castToType(value); // Type
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 3292052: // kind
          value = new GuidePageKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<GuidePageKind>
          return value;
        case 3575610: // type
          this.getType().add(castToCode(value)); // CodeType
          return value;
        case -807062458: // package
          this.getPackage().add(castToString(value)); // StringType
          return value;
        case -1268779017: // format
          this.format = castToCode(value); // CodeType
          return value;
        case 3433103: // page
          this.getPage().add((ImplementationGuideInputPageComponent) value); // ImplementationGuideInputPageComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("source[x]")) {
          this.source = castToType(value); // Type
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("kind")) {
          value = new GuidePageKindEnumFactory().fromType(castToCode(value));
          this.kind = (Enumeration) value; // Enumeration<GuidePageKind>
        } else if (name.equals("type")) {
          this.getType().add(castToCode(value));
        } else if (name.equals("package")) {
          this.getPackage().add(castToString(value));
        } else if (name.equals("format")) {
          this.format = castToCode(value); // CodeType
        } else if (name.equals("page")) {
          this.getPage().add((ImplementationGuideInputPageComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1698413947:  return getSource(); 
        case -896505829:  return getSource(); 
        case 110371416:  return getTitleElement();
        case 3292052:  return getKindElement();
        case 3575610:  return addTypeElement();
        case -807062458:  return addPackageElement();
        case -1268779017:  return getFormatElement();
        case 3433103:  return addPage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -896505829: /*source*/ return new String[] {"uri", "Reference"};
        case 110371416: /*title*/ return new String[] {"string"};
        case 3292052: /*kind*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"code"};
        case -807062458: /*package*/ return new String[] {"string"};
        case -1268779017: /*format*/ return new String[] {"code"};
        case 3433103: /*page*/ return new String[] {"@ImplementationGuideInput.page"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sourceUri")) {
          this.source = new UriType();
          return this.source;
        }
        else if (name.equals("sourceReference")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.title");
        }
        else if (name.equals("kind")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.kind");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.type");
        }
        else if (name.equals("package")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.package");
        }
        else if (name.equals("format")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.format");
        }
        else if (name.equals("page")) {
          return addPage();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideInputPageComponent copy() {
        ImplementationGuideInputPageComponent dst = new ImplementationGuideInputPageComponent();
        copyValues(dst);
        dst.source = source == null ? null : source.copy();
        dst.title = title == null ? null : title.copy();
        dst.kind = kind == null ? null : kind.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeType>();
          for (CodeType i : type)
            dst.type.add(i.copy());
        };
        if (package_ != null) {
          dst.package_ = new ArrayList<StringType>();
          for (StringType i : package_)
            dst.package_.add(i.copy());
        };
        dst.format = format == null ? null : format.copy();
        if (page != null) {
          dst.page = new ArrayList<ImplementationGuideInputPageComponent>();
          for (ImplementationGuideInputPageComponent i : page)
            dst.page.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputPageComponent))
          return false;
        ImplementationGuideInputPageComponent o = (ImplementationGuideInputPageComponent) other_;
        return compareDeep(source, o.source, true) && compareDeep(title, o.title, true) && compareDeep(kind, o.kind, true)
           && compareDeep(type, o.type, true) && compareDeep(package_, o.package_, true) && compareDeep(format, o.format, true)
           && compareDeep(page, o.page, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInputPageComponent))
          return false;
        ImplementationGuideInputPageComponent o = (ImplementationGuideInputPageComponent) other_;
        return compareValues(title, o.title, true) && compareValues(kind, o.kind, true) && compareValues(type, o.type, true)
           && compareValues(package_, o.package_, true) && compareValues(format, o.format, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(source, title, kind, type
          , package_, format, page);
      }

  public String fhirType() {
    return "ImplementationGuideInput.page";

  }

  }

    /**
     * A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.
     */
    @Child(name = "copyright", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input." )
    protected MarkdownType copyright;

    /**
     * The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="FHIR Version this Implementation Guide targets", formalDefinition="The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version." )
    protected IdType fhirVersion;

    /**
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.
     */
    @Child(name = "dependency", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Another Implementation guide this depends on", formalDefinition="Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides." )
    protected List<ImplementationGuideInputDependencyComponent> dependency;

    /**
     * A logical group of resources. Logical groups can be used when building pages.
     */
    @Child(name = "package", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Group of resources as used in .page.package", formalDefinition="A logical group of resources. Logical groups can be used when building pages." )
    protected List<ImplementationGuideInputPackageComponent> package_;

    /**
     * A set of profiles that all resources covered by this implementation guide must conform to.
     */
    @Child(name = "global", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Profiles that apply globally", formalDefinition="A set of profiles that all resources covered by this implementation guide must conform to." )
    protected List<ImplementationGuideInputGlobalComponent> global;

    /**
     * A page / section in the implementation guide. The root page is the implementation guide home page.
     */
    @Child(name = "page", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Page/Section in the Guide", formalDefinition="A page / section in the implementation guide. The root page is the implementation guide home page." )
    protected ImplementationGuideInputPageComponent page;

    private static final long serialVersionUID = -1326718607L;

  /**
   * Constructor
   */
    public ImplementationGuideInput() {
      super();
    }

  /**
   * Constructor
   */
    public ImplementationGuideInput(UriType url, StringType name, Enumeration<PublicationStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImplementationGuideInput setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.
     */
    public ImplementationGuideInput setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ImplementationGuideInput setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public ImplementationGuideInput setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.name");
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
     * @param value {@link #name} (A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ImplementationGuideInput setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public ImplementationGuideInput setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this implementation guide input. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.status");
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
     * @param value {@link #status} (The status of this implementation guide input. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ImplementationGuideInput setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this implementation guide input. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this implementation guide input. Enables tracking the life-cycle of the content.
     */
    public ImplementationGuideInput setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.experimental");
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
     * @param value {@link #experimental} (A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ImplementationGuideInput setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ImplementationGuideInput setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ImplementationGuideInput setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.
     */
    public ImplementationGuideInput setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the individual or organization that published the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ImplementationGuideInput setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the implementation guide input.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the implementation guide input.
     */
    public ImplementationGuideInput setPublisher(String value) { 
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
    public ImplementationGuideInput setContact(List<ContactDetail> theContact) { 
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

    public ImplementationGuideInput addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the implementation guide input from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.description");
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
     * @param value {@link #description} (A free text natural language description of the implementation guide input from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImplementationGuideInput setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the implementation guide input from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the implementation guide input from a consumer's perspective.
     */
    public ImplementationGuideInput setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide input instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setUseContext(List<UsageContext> theUseContext) { 
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

    public ImplementationGuideInput addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the implementation guide input is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public ImplementationGuideInput addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #copyright} (A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public MarkdownType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ImplementationGuideInput setCopyrightElement(MarkdownType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.
     */
    public ImplementationGuideInput setCopyright(String value) { 
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
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public IdType getFhirVersionElement() { 
      if (this.fhirVersion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.fhirVersion");
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
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public ImplementationGuideInput setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.
     */
    public ImplementationGuideInput setFhirVersion(String value) { 
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
     * @return {@link #dependency} (Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.)
     */
    public List<ImplementationGuideInputDependencyComponent> getDependency() { 
      if (this.dependency == null)
        this.dependency = new ArrayList<ImplementationGuideInputDependencyComponent>();
      return this.dependency;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setDependency(List<ImplementationGuideInputDependencyComponent> theDependency) { 
      this.dependency = theDependency;
      return this;
    }

    public boolean hasDependency() { 
      if (this.dependency == null)
        return false;
      for (ImplementationGuideInputDependencyComponent item : this.dependency)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideInputDependencyComponent addDependency() { //3
      ImplementationGuideInputDependencyComponent t = new ImplementationGuideInputDependencyComponent();
      if (this.dependency == null)
        this.dependency = new ArrayList<ImplementationGuideInputDependencyComponent>();
      this.dependency.add(t);
      return t;
    }

    public ImplementationGuideInput addDependency(ImplementationGuideInputDependencyComponent t) { //3
      if (t == null)
        return this;
      if (this.dependency == null)
        this.dependency = new ArrayList<ImplementationGuideInputDependencyComponent>();
      this.dependency.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dependency}, creating it if it does not already exist
     */
    public ImplementationGuideInputDependencyComponent getDependencyFirstRep() { 
      if (getDependency().isEmpty()) {
        addDependency();
      }
      return getDependency().get(0);
    }

    /**
     * @return {@link #package_} (A logical group of resources. Logical groups can be used when building pages.)
     */
    public List<ImplementationGuideInputPackageComponent> getPackage() { 
      if (this.package_ == null)
        this.package_ = new ArrayList<ImplementationGuideInputPackageComponent>();
      return this.package_;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setPackage(List<ImplementationGuideInputPackageComponent> thePackage) { 
      this.package_ = thePackage;
      return this;
    }

    public boolean hasPackage() { 
      if (this.package_ == null)
        return false;
      for (ImplementationGuideInputPackageComponent item : this.package_)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideInputPackageComponent addPackage() { //3
      ImplementationGuideInputPackageComponent t = new ImplementationGuideInputPackageComponent();
      if (this.package_ == null)
        this.package_ = new ArrayList<ImplementationGuideInputPackageComponent>();
      this.package_.add(t);
      return t;
    }

    public ImplementationGuideInput addPackage(ImplementationGuideInputPackageComponent t) { //3
      if (t == null)
        return this;
      if (this.package_ == null)
        this.package_ = new ArrayList<ImplementationGuideInputPackageComponent>();
      this.package_.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #package_}, creating it if it does not already exist
     */
    public ImplementationGuideInputPackageComponent getPackageFirstRep() { 
      if (getPackage().isEmpty()) {
        addPackage();
      }
      return getPackage().get(0);
    }

    /**
     * @return {@link #global} (A set of profiles that all resources covered by this implementation guide must conform to.)
     */
    public List<ImplementationGuideInputGlobalComponent> getGlobal() { 
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
      return this.global;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImplementationGuideInput setGlobal(List<ImplementationGuideInputGlobalComponent> theGlobal) { 
      this.global = theGlobal;
      return this;
    }

    public boolean hasGlobal() { 
      if (this.global == null)
        return false;
      for (ImplementationGuideInputGlobalComponent item : this.global)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ImplementationGuideInputGlobalComponent addGlobal() { //3
      ImplementationGuideInputGlobalComponent t = new ImplementationGuideInputGlobalComponent();
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
      this.global.add(t);
      return t;
    }

    public ImplementationGuideInput addGlobal(ImplementationGuideInputGlobalComponent t) { //3
      if (t == null)
        return this;
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
      this.global.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #global}, creating it if it does not already exist
     */
    public ImplementationGuideInputGlobalComponent getGlobalFirstRep() { 
      if (getGlobal().isEmpty()) {
        addGlobal();
      }
      return getGlobal().get(0);
    }

    /**
     * @return {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
     */
    public ImplementationGuideInputPageComponent getPage() { 
      if (this.page == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuideInput.page");
        else if (Configuration.doAutoCreate())
          this.page = new ImplementationGuideInputPageComponent(); // cc
      return this.page;
    }

    public boolean hasPage() { 
      return this.page != null && !this.page.isEmpty();
    }

    /**
     * @param value {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
     */
    public ImplementationGuideInput setPage(ImplementationGuideInputPageComponent value) { 
      this.page = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("url", "uri", "An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.", 0, 1, url));
        children.add(new Property("version", "string", "The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version));
        children.add(new Property("name", "string", "A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("status", "code", "The status of this implementation guide input. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("experimental", "boolean", "A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, 1, experimental));
        children.add(new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.", 0, 1, date));
        children.add(new Property("publisher", "string", "The name of the individual or organization that published the implementation guide input.", 0, 1, publisher));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("description", "markdown", "A free text natural language description of the implementation guide input from a consumer's perspective.", 0, 1, description));
        children.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide input instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        children.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide input is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        children.add(new Property("copyright", "markdown", "A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.", 0, 1, copyright));
        children.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.", 0, 1, fhirVersion));
        children.add(new Property("dependency", "", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependency));
        children.add(new Property("package", "", "A logical group of resources. Logical groups can be used when building pages.", 0, java.lang.Integer.MAX_VALUE, package_));
        children.add(new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global));
        children.add(new Property("page", "", "A page / section in the implementation guide. The root page is the implementation guide home page.", 0, 1, page));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 116079: /*url*/  return new Property("url", "uri", "An absolute URI that is used to identify this implementation guide input when it is referenced in a specification, model, design or an instance. This SHOULD be globally unique, and SHOULD be a literal address at which this implementation guide input is (or will be) published.", 0, 1, url);
        case 351608024: /*version*/  return new Property("version", "string", "The identifier that is used to identify this version of the implementation guide input when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the implementation guide input author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, 1, version);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying the implementation guide input. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this implementation guide input. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -404562712: /*experimental*/  return new Property("experimental", "boolean", "A boolean value to indicate that this implementation guide input is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, 1, experimental);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date  (and optionally time) when the implementation guide input was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide input changes.", 0, 1, date);
        case 1447404028: /*publisher*/  return new Property("publisher", "string", "The name of the individual or organization that published the implementation guide input.", 0, 1, publisher);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A free text natural language description of the implementation guide input from a consumer's perspective.", 0, 1, description);
        case -669707736: /*useContext*/  return new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate implementation guide input instances.", 0, java.lang.Integer.MAX_VALUE, useContext);
        case -507075711: /*jurisdiction*/  return new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the implementation guide input is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction);
        case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to the implementation guide input and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the implementation guide input.", 0, 1, copyright);
        case 461006061: /*fhirVersion*/  return new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 3.2.0 for this version.", 0, 1, fhirVersion);
        case -26291381: /*dependency*/  return new Property("dependency", "", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependency);
        case -807062458: /*package*/  return new Property("package", "", "A logical group of resources. Logical groups can be used when building pages.", 0, java.lang.Integer.MAX_VALUE, package_);
        case -1243020381: /*global*/  return new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global);
        case 3433103: /*page*/  return new Property("page", "", "A page / section in the implementation guide. The root page is the implementation guide home page.", 0, 1, page);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        case 461006061: /*fhirVersion*/ return this.fhirVersion == null ? new Base[0] : new Base[] {this.fhirVersion}; // IdType
        case -26291381: /*dependency*/ return this.dependency == null ? new Base[0] : this.dependency.toArray(new Base[this.dependency.size()]); // ImplementationGuideInputDependencyComponent
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : this.package_.toArray(new Base[this.package_.size()]); // ImplementationGuideInputPackageComponent
        case -1243020381: /*global*/ return this.global == null ? new Base[0] : this.global.toArray(new Base[this.global.size()]); // ImplementationGuideInputGlobalComponent
        case 3433103: /*page*/ return this.page == null ? new Base[0] : new Base[] {this.page}; // ImplementationGuideInputPageComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
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
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        case 461006061: // fhirVersion
          this.fhirVersion = castToId(value); // IdType
          return value;
        case -26291381: // dependency
          this.getDependency().add((ImplementationGuideInputDependencyComponent) value); // ImplementationGuideInputDependencyComponent
          return value;
        case -807062458: // package
          this.getPackage().add((ImplementationGuideInputPackageComponent) value); // ImplementationGuideInputPackageComponent
          return value;
        case -1243020381: // global
          this.getGlobal().add((ImplementationGuideInputGlobalComponent) value); // ImplementationGuideInputGlobalComponent
          return value;
        case 3433103: // page
          this.page = (ImplementationGuideInputPageComponent) value; // ImplementationGuideInputPageComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
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
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else if (name.equals("fhirVersion")) {
          this.fhirVersion = castToId(value); // IdType
        } else if (name.equals("dependency")) {
          this.getDependency().add((ImplementationGuideInputDependencyComponent) value);
        } else if (name.equals("package")) {
          this.getPackage().add((ImplementationGuideInputPackageComponent) value);
        } else if (name.equals("global")) {
          this.getGlobal().add((ImplementationGuideInputGlobalComponent) value);
        } else if (name.equals("page")) {
          this.page = (ImplementationGuideInputPageComponent) value; // ImplementationGuideInputPageComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 1522889671:  return getCopyrightElement();
        case 461006061:  return getFhirVersionElement();
        case -26291381:  return addDependency(); 
        case -807062458:  return addPackage(); 
        case -1243020381:  return addGlobal(); 
        case 3433103:  return getPage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        case 461006061: /*fhirVersion*/ return new String[] {"id"};
        case -26291381: /*dependency*/ return new String[] {};
        case -807062458: /*package*/ return new String[] {};
        case -1243020381: /*global*/ return new String[] {};
        case 3433103: /*page*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.copyright");
        }
        else if (name.equals("fhirVersion")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuideInput.fhirVersion");
        }
        else if (name.equals("dependency")) {
          return addDependency();
        }
        else if (name.equals("package")) {
          return addPackage();
        }
        else if (name.equals("global")) {
          return addGlobal();
        }
        else if (name.equals("page")) {
          this.page = new ImplementationGuideInputPageComponent();
          return this.page;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImplementationGuideInput";

  }

      public ImplementationGuideInput copy() {
        ImplementationGuideInput dst = new ImplementationGuideInput();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
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
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        if (dependency != null) {
          dst.dependency = new ArrayList<ImplementationGuideInputDependencyComponent>();
          for (ImplementationGuideInputDependencyComponent i : dependency)
            dst.dependency.add(i.copy());
        };
        if (package_ != null) {
          dst.package_ = new ArrayList<ImplementationGuideInputPackageComponent>();
          for (ImplementationGuideInputPackageComponent i : package_)
            dst.package_.add(i.copy());
        };
        if (global != null) {
          dst.global = new ArrayList<ImplementationGuideInputGlobalComponent>();
          for (ImplementationGuideInputGlobalComponent i : global)
            dst.global.add(i.copy());
        };
        dst.page = page == null ? null : page.copy();
        return dst;
      }

      protected ImplementationGuideInput typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInput))
          return false;
        ImplementationGuideInput o = (ImplementationGuideInput) other_;
        return compareDeep(copyright, o.copyright, true) && compareDeep(fhirVersion, o.fhirVersion, true)
           && compareDeep(dependency, o.dependency, true) && compareDeep(package_, o.package_, true) && compareDeep(global, o.global, true)
           && compareDeep(page, o.page, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImplementationGuideInput))
          return false;
        ImplementationGuideInput o = (ImplementationGuideInput) other_;
        return compareValues(copyright, o.copyright, true) && compareValues(fhirVersion, o.fhirVersion, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(copyright, fhirVersion, dependency
          , package_, global, page);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImplementationGuideInput;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The implementation guide input publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuideInput.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ImplementationGuideInput.date", description="The implementation guide input publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The implementation guide input publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuideInput.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>dependency</b>
   * <p>
   * Description: <b>Where to find dependency</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideInput.dependency.uri</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dependency", path="ImplementationGuideInput.dependency.uri", description="Where to find dependency", type="uri" )
  public static final String SP_DEPENDENCY = "dependency";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dependency</b>
   * <p>
   * Description: <b>Where to find dependency</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideInput.dependency.uri</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam DEPENDENCY = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_DEPENDENCY);

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideInput.package.resource.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="ImplementationGuideInput.package.resource.reference", description="Location of the resource", type="reference" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuideInput.package.resource.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuideInput:resource</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESOURCE = new ca.uhn.fhir.model.api.Include("ImplementationGuideInput:resource").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ImplementationGuideInput.jurisdiction", description="Intended jurisdiction for the implementation guide input", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ImplementationGuideInput.name", description="Computationally friendly name of the implementation guide input", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ImplementationGuideInput.description", description="The description of the implementation guide input", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ImplementationGuideInput.publisher", description="Name of the publisher of the implementation guide input", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide input</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuideInput.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.experimental</b><br>
   * </p>
   */
  @SearchParamDefinition(name="experimental", path="ImplementationGuideInput.experimental", description="For testing purposes, not real usage", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>experimental</b>
   * <p>
   * Description: <b>For testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.experimental</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EXPERIMENTAL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EXPERIMENTAL);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ImplementationGuideInput.version", description="The business version of the implementation guide input", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide input</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideInput.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ImplementationGuideInput.url", description="The uri that identifies the implementation guide input", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the implementation guide input</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuideInput.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ImplementationGuideInput.status", description="The current status of the implementation guide input", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide input</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuideInput.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

