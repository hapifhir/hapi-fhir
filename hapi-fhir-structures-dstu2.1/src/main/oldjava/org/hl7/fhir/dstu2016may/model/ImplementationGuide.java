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
 * A set of rules or how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole, and to publish a computable definition of all the parts.
 */
@ResourceDef(name="ImplementationGuide", profile="http://hl7.org/fhir/Profile/ImplementationGuide")
public class ImplementationGuide extends DomainResource {

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
         * added to help the parsers
         */
        NULL;
        public static GuideDependencyType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("inclusion".equals(codeString))
          return INCLUSION;
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
          if (code == null || code.isEmpty())
            return null;
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
         * added to help the parsers
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
          if (code == null || code.isEmpty())
            return null;
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
    public static class ImplementationGuideContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the implementation guide.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of an individual to contact", formalDefinition="The name of an individual to contact regarding the implementation guide." )
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
      public ImplementationGuideContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuideContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the implementation guide.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the implementation guide.
         */
        public ImplementationGuideContactComponent setName(String value) { 
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
        public ImplementationGuideContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the implementation guide.", 0, java.lang.Integer.MAX_VALUE, name));
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
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideContactComponent copy() {
        ImplementationGuideContactComponent dst = new ImplementationGuideContactComponent();
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
        if (!(other instanceof ImplementationGuideContactComponent))
          return false;
        ImplementationGuideContactComponent o = (ImplementationGuideContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuideContactComponent))
          return false;
        ImplementationGuideContactComponent o = (ImplementationGuideContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImplementationGuide.contact";

  }

  }

    @Block()
    public static class ImplementationGuideDependencyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the dependency is represented when the guide is published.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="reference | inclusion", formalDefinition="How the dependency is represented when the guide is published." )
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
      public ImplementationGuideDependencyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideDependencyComponent(Enumeration<GuideDependencyType> type, UriType uri) {
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
              throw new Error("Attempt to auto-create ImplementationGuideDependencyComponent.type");
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
        public ImplementationGuideDependencyComponent setTypeElement(Enumeration<GuideDependencyType> value) { 
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
        public ImplementationGuideDependencyComponent setType(GuideDependencyType value) { 
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
              throw new Error("Attempt to auto-create ImplementationGuideDependencyComponent.uri");
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
        public ImplementationGuideDependencyComponent setUriElement(UriType value) { 
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
        public ImplementationGuideDependencyComponent setUri(String value) { 
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "How the dependency is represented when the guide is published.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("uri", "uri", "Where the dependency is located.", 0, java.lang.Integer.MAX_VALUE, uri));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new GuideDependencyTypeEnumFactory().fromType(value); // Enumeration<GuideDependencyType>
          break;
        case 116076: // uri
          this.uri = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new GuideDependencyTypeEnumFactory().fromType(value); // Enumeration<GuideDependencyType>
        else if (name.equals("uri"))
          this.uri = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<GuideDependencyType>
        case 116076: throw new FHIRException("Cannot make property uri as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.type");
        }
        else if (name.equals("uri")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.uri");
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideDependencyComponent copy() {
        ImplementationGuideDependencyComponent dst = new ImplementationGuideDependencyComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.uri = uri == null ? null : uri.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuideDependencyComponent))
          return false;
        ImplementationGuideDependencyComponent o = (ImplementationGuideDependencyComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(uri, o.uri, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuideDependencyComponent))
          return false;
        ImplementationGuideDependencyComponent o = (ImplementationGuideDependencyComponent) other;
        return compareValues(type, o.type, true) && compareValues(uri, o.uri, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (uri == null || uri.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImplementationGuide.dependency";

  }

  }

    @Block()
    public static class ImplementationGuidePackageComponent extends BackboneElement implements IBaseBackboneElement {
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
         * A resource that is part of the implementation guide. Conformance resources (value set, structure definition, conformance statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.
         */
        @Child(name = "resource", type = {}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Resource in the implementation guide", formalDefinition="A resource that is part of the implementation guide. Conformance resources (value set, structure definition, conformance statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource." )
        protected List<ImplementationGuidePackageResourceComponent> resource;

        private static final long serialVersionUID = -701846580L;

    /**
     * Constructor
     */
      public ImplementationGuidePackageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuidePackageComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (The name for the group, as used in page.package.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageComponent.name");
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
        public ImplementationGuidePackageComponent setNameElement(StringType value) { 
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
        public ImplementationGuidePackageComponent setName(String value) { 
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
              throw new Error("Attempt to auto-create ImplementationGuidePackageComponent.description");
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
        public ImplementationGuidePackageComponent setDescriptionElement(StringType value) { 
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
        public ImplementationGuidePackageComponent setDescription(String value) { 
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
         * @return {@link #resource} (A resource that is part of the implementation guide. Conformance resources (value set, structure definition, conformance statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.)
         */
        public List<ImplementationGuidePackageResourceComponent> getResource() { 
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuidePackageResourceComponent>();
          return this.resource;
        }

        public boolean hasResource() { 
          if (this.resource == null)
            return false;
          for (ImplementationGuidePackageResourceComponent item : this.resource)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #resource} (A resource that is part of the implementation guide. Conformance resources (value set, structure definition, conformance statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.)
         */
    // syntactic sugar
        public ImplementationGuidePackageResourceComponent addResource() { //3
          ImplementationGuidePackageResourceComponent t = new ImplementationGuidePackageResourceComponent();
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuidePackageResourceComponent>();
          this.resource.add(t);
          return t;
        }

    // syntactic sugar
        public ImplementationGuidePackageComponent addResource(ImplementationGuidePackageResourceComponent t) { //3
          if (t == null)
            return this;
          if (this.resource == null)
            this.resource = new ArrayList<ImplementationGuidePackageResourceComponent>();
          this.resource.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name for the group, as used in page.package.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "Human readable text describing the package.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("resource", "", "A resource that is part of the implementation guide. Conformance resources (value set, structure definition, conformance statements etc.) are obvious candidates for inclusion, but any kind of resource can be included as an example resource.", 0, java.lang.Integer.MAX_VALUE, resource));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // ImplementationGuidePackageResourceComponent
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
        case -341064690: // resource
          this.getResource().add((ImplementationGuidePackageResourceComponent) value); // ImplementationGuidePackageResourceComponent
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
        else if (name.equals("resource"))
          this.getResource().add((ImplementationGuidePackageResourceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -341064690:  return addResource(); // ImplementationGuidePackageResourceComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.description");
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuidePackageComponent copy() {
        ImplementationGuidePackageComponent dst = new ImplementationGuidePackageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        if (resource != null) {
          dst.resource = new ArrayList<ImplementationGuidePackageResourceComponent>();
          for (ImplementationGuidePackageResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageComponent))
          return false;
        ImplementationGuidePackageComponent o = (ImplementationGuidePackageComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(description, o.description, true) && compareDeep(resource, o.resource, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageComponent))
          return false;
        ImplementationGuidePackageComponent o = (ImplementationGuidePackageComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (description == null || description.isEmpty())
           && (resource == null || resource.isEmpty());
      }

  public String fhirType() {
    return "ImplementationGuide.package";

  }

  }

    @Block()
    public static class ImplementationGuidePackageResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Whether a resource is included in the guide as part of the rules defined by the guide, or just as an example of a resource that conforms to the rules and/or help implementers understand the intent of the guide.
         */
        @Child(name = "example", type = {BooleanType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="If not an example, has it's normal meaning", formalDefinition="Whether a resource is included in the guide as part of the rules defined by the guide, or just as an example of a resource that conforms to the rules and/or help implementers understand the intent of the guide." )
        protected BooleanType example;

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
         * A short code that may be used to identify the resource throughout the implementation guide.
         */
        @Child(name = "acronym", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short code to identify the resource", formalDefinition="A short code that may be used to identify the resource throughout the implementation guide." )
        protected StringType acronym;

        /**
         * Where this resource is found.
         */
        @Child(name = "source", type = {UriType.class}, order=5, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Location of the resource", formalDefinition="Where this resource is found." )
        protected Type source;

        /**
         * Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions.
         */
        @Child(name = "exampleFor", type = {StructureDefinition.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource this is an example of (if applicable)", formalDefinition="Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions." )
        protected Reference exampleFor;

        /**
         * The actual object that is the target of the reference (Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions.)
         */
        protected StructureDefinition exampleForTarget;

        private static final long serialVersionUID = 2085404852L;

    /**
     * Constructor
     */
      public ImplementationGuidePackageResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuidePackageResourceComponent(BooleanType example, Type source) {
        super();
        this.example = example;
        this.source = source;
      }

        /**
         * @return {@link #example} (Whether a resource is included in the guide as part of the rules defined by the guide, or just as an example of a resource that conforms to the rules and/or help implementers understand the intent of the guide.). This is the underlying object with id, value and extensions. The accessor "getExample" gives direct access to the value
         */
        public BooleanType getExampleElement() { 
          if (this.example == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageResourceComponent.example");
            else if (Configuration.doAutoCreate())
              this.example = new BooleanType(); // bb
          return this.example;
        }

        public boolean hasExampleElement() { 
          return this.example != null && !this.example.isEmpty();
        }

        public boolean hasExample() { 
          return this.example != null && !this.example.isEmpty();
        }

        /**
         * @param value {@link #example} (Whether a resource is included in the guide as part of the rules defined by the guide, or just as an example of a resource that conforms to the rules and/or help implementers understand the intent of the guide.). This is the underlying object with id, value and extensions. The accessor "getExample" gives direct access to the value
         */
        public ImplementationGuidePackageResourceComponent setExampleElement(BooleanType value) { 
          this.example = value;
          return this;
        }

        /**
         * @return Whether a resource is included in the guide as part of the rules defined by the guide, or just as an example of a resource that conforms to the rules and/or help implementers understand the intent of the guide.
         */
        public boolean getExample() { 
          return this.example == null || this.example.isEmpty() ? false : this.example.getValue();
        }

        /**
         * @param value Whether a resource is included in the guide as part of the rules defined by the guide, or just as an example of a resource that conforms to the rules and/or help implementers understand the intent of the guide.
         */
        public ImplementationGuidePackageResourceComponent setExample(boolean value) { 
            if (this.example == null)
              this.example = new BooleanType();
            this.example.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageResourceComponent.name");
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
        public ImplementationGuidePackageResourceComponent setNameElement(StringType value) { 
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
        public ImplementationGuidePackageResourceComponent setName(String value) { 
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
              throw new Error("Attempt to auto-create ImplementationGuidePackageResourceComponent.description");
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
        public ImplementationGuidePackageResourceComponent setDescriptionElement(StringType value) { 
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
        public ImplementationGuidePackageResourceComponent setDescription(String value) { 
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
         * @return {@link #acronym} (A short code that may be used to identify the resource throughout the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getAcronym" gives direct access to the value
         */
        public StringType getAcronymElement() { 
          if (this.acronym == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageResourceComponent.acronym");
            else if (Configuration.doAutoCreate())
              this.acronym = new StringType(); // bb
          return this.acronym;
        }

        public boolean hasAcronymElement() { 
          return this.acronym != null && !this.acronym.isEmpty();
        }

        public boolean hasAcronym() { 
          return this.acronym != null && !this.acronym.isEmpty();
        }

        /**
         * @param value {@link #acronym} (A short code that may be used to identify the resource throughout the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getAcronym" gives direct access to the value
         */
        public ImplementationGuidePackageResourceComponent setAcronymElement(StringType value) { 
          this.acronym = value;
          return this;
        }

        /**
         * @return A short code that may be used to identify the resource throughout the implementation guide.
         */
        public String getAcronym() { 
          return this.acronym == null ? null : this.acronym.getValue();
        }

        /**
         * @param value A short code that may be used to identify the resource throughout the implementation guide.
         */
        public ImplementationGuidePackageResourceComponent setAcronym(String value) { 
          if (Utilities.noString(value))
            this.acronym = null;
          else {
            if (this.acronym == null)
              this.acronym = new StringType();
            this.acronym.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #source} (Where this resource is found.)
         */
        public Type getSource() { 
          return this.source;
        }

        /**
         * @return {@link #source} (Where this resource is found.)
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
         * @return {@link #source} (Where this resource is found.)
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
         * @param value {@link #source} (Where this resource is found.)
         */
        public ImplementationGuidePackageResourceComponent setSource(Type value) { 
          this.source = value;
          return this;
        }

        /**
         * @return {@link #exampleFor} (Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions.)
         */
        public Reference getExampleFor() { 
          if (this.exampleFor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageResourceComponent.exampleFor");
            else if (Configuration.doAutoCreate())
              this.exampleFor = new Reference(); // cc
          return this.exampleFor;
        }

        public boolean hasExampleFor() { 
          return this.exampleFor != null && !this.exampleFor.isEmpty();
        }

        /**
         * @param value {@link #exampleFor} (Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions.)
         */
        public ImplementationGuidePackageResourceComponent setExampleFor(Reference value) { 
          this.exampleFor = value;
          return this;
        }

        /**
         * @return {@link #exampleFor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions.)
         */
        public StructureDefinition getExampleForTarget() { 
          if (this.exampleForTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageResourceComponent.exampleFor");
            else if (Configuration.doAutoCreate())
              this.exampleForTarget = new StructureDefinition(); // aa
          return this.exampleForTarget;
        }

        /**
         * @param value {@link #exampleFor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions.)
         */
        public ImplementationGuidePackageResourceComponent setExampleForTarget(StructureDefinition value) { 
          this.exampleForTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("example", "boolean", "Whether a resource is included in the guide as part of the rules defined by the guide, or just as an example of a resource that conforms to the rules and/or help implementers understand the intent of the guide.", 0, java.lang.Integer.MAX_VALUE, example));
          childrenList.add(new Property("name", "string", "A human assigned name for the resource. All resources SHOULD have a name, but the name may be extracted from the resource (e.g. ValueSet.name).", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "A description of the reason that a resource has been included in the implementation guide.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("acronym", "string", "A short code that may be used to identify the resource throughout the implementation guide.", 0, java.lang.Integer.MAX_VALUE, acronym));
          childrenList.add(new Property("source[x]", "uri|Reference(Any)", "Where this resource is found.", 0, java.lang.Integer.MAX_VALUE, source));
          childrenList.add(new Property("exampleFor", "Reference(StructureDefinition)", "Another resource that this resource is an example for. This is mostly used for resources that are included as examples of StructureDefinitions.", 0, java.lang.Integer.MAX_VALUE, exampleFor));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1322970774: /*example*/ return this.example == null ? new Base[0] : new Base[] {this.example}; // BooleanType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1163472445: /*acronym*/ return this.acronym == null ? new Base[0] : new Base[] {this.acronym}; // StringType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Type
        case -2002349313: /*exampleFor*/ return this.exampleFor == null ? new Base[0] : new Base[] {this.exampleFor}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1322970774: // example
          this.example = castToBoolean(value); // BooleanType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -1163472445: // acronym
          this.acronym = castToString(value); // StringType
          break;
        case -896505829: // source
          this.source = (Type) value; // Type
          break;
        case -2002349313: // exampleFor
          this.exampleFor = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("example"))
          this.example = castToBoolean(value); // BooleanType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("acronym"))
          this.acronym = castToString(value); // StringType
        else if (name.equals("source[x]"))
          this.source = (Type) value; // Type
        else if (name.equals("exampleFor"))
          this.exampleFor = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1322970774: throw new FHIRException("Cannot make property example as it is not a complex type"); // BooleanType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -1163472445: throw new FHIRException("Cannot make property acronym as it is not a complex type"); // StringType
        case -1698413947:  return getSource(); // Type
        case -2002349313:  return getExampleFor(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("example")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.example");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.description");
        }
        else if (name.equals("acronym")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.acronym");
        }
        else if (name.equals("sourceUri")) {
          this.source = new UriType();
          return this.source;
        }
        else if (name.equals("sourceReference")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("exampleFor")) {
          this.exampleFor = new Reference();
          return this.exampleFor;
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuidePackageResourceComponent copy() {
        ImplementationGuidePackageResourceComponent dst = new ImplementationGuidePackageResourceComponent();
        copyValues(dst);
        dst.example = example == null ? null : example.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.acronym = acronym == null ? null : acronym.copy();
        dst.source = source == null ? null : source.copy();
        dst.exampleFor = exampleFor == null ? null : exampleFor.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageResourceComponent))
          return false;
        ImplementationGuidePackageResourceComponent o = (ImplementationGuidePackageResourceComponent) other;
        return compareDeep(example, o.example, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(acronym, o.acronym, true) && compareDeep(source, o.source, true) && compareDeep(exampleFor, o.exampleFor, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageResourceComponent))
          return false;
        ImplementationGuidePackageResourceComponent o = (ImplementationGuidePackageResourceComponent) other;
        return compareValues(example, o.example, true) && compareValues(name, o.name, true) && compareValues(description, o.description, true)
           && compareValues(acronym, o.acronym, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (example == null || example.isEmpty()) && (name == null || name.isEmpty())
           && (description == null || description.isEmpty()) && (acronym == null || acronym.isEmpty())
           && (source == null || source.isEmpty()) && (exampleFor == null || exampleFor.isEmpty());
      }

  public String fhirType() {
    return "ImplementationGuide.package.resource";

  }

  }

    @Block()
    public static class ImplementationGuideGlobalComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of resource that all instances must conform to.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type this profiles applies to", formalDefinition="The type of resource that all instances must conform to." )
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
      public ImplementationGuideGlobalComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuideGlobalComponent(CodeType type, Reference profile) {
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
              throw new Error("Attempt to auto-create ImplementationGuideGlobalComponent.type");
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
        public ImplementationGuideGlobalComponent setTypeElement(CodeType value) { 
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
        public ImplementationGuideGlobalComponent setType(String value) { 
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
              throw new Error("Attempt to auto-create ImplementationGuideGlobalComponent.profile");
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
        public ImplementationGuideGlobalComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the profile that all instances must conform to.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideGlobalComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the profile that all instances must conform to.)
         */
        public ImplementationGuideGlobalComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of resource that all instances must conform to.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "A reference to the profile that all instances must conform to.", 0, java.lang.Integer.MAX_VALUE, profile));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          break;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("profile"))
          this.profile = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case -309425751:  return getProfile(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.type");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuideGlobalComponent copy() {
        ImplementationGuideGlobalComponent dst = new ImplementationGuideGlobalComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuideGlobalComponent))
          return false;
        ImplementationGuideGlobalComponent o = (ImplementationGuideGlobalComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuideGlobalComponent))
          return false;
        ImplementationGuideGlobalComponent o = (ImplementationGuideGlobalComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (profile == null || profile.isEmpty())
          ;
      }

  public String fhirType() {
    return "ImplementationGuide.global";

  }

  }

    @Block()
    public static class ImplementationGuidePageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The source address for the page.
         */
        @Child(name = "source", type = {UriType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Where to find that page", formalDefinition="The source address for the page." )
        protected UriType source;

        /**
         * A short name used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Short name shown for navigational assistance", formalDefinition="A short name used to represent this page in navigational structures such as table of contents, bread crumbs, etc." )
        protected StringType name;

        /**
         * The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.
         */
        @Child(name = "kind", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="page | example | list | include | directory | dictionary | toc | resource", formalDefinition="The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest." )
        protected Enumeration<GuidePageKind> kind;

        /**
         * For constructed pages, what kind of resources to include in the list.
         */
        @Child(name = "type", type = {CodeType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Kind of resource to include in the list", formalDefinition="For constructed pages, what kind of resources to include in the list." )
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
        @Child(name = "page", type = {ImplementationGuidePageComponent.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Pages / Sections", formalDefinition="Nested Pages/Sections under this page." )
        protected List<ImplementationGuidePageComponent> page;

        private static final long serialVersionUID = -1620890043L;

    /**
     * Constructor
     */
      public ImplementationGuidePageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ImplementationGuidePageComponent(UriType source, StringType name, Enumeration<GuidePageKind> kind) {
        super();
        this.source = source;
        this.name = name;
        this.kind = kind;
      }

        /**
         * @return {@link #source} (The source address for the page.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public UriType getSourceElement() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePageComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new UriType(); // bb
          return this.source;
        }

        public boolean hasSourceElement() { 
          return this.source != null && !this.source.isEmpty();
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The source address for the page.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public ImplementationGuidePageComponent setSourceElement(UriType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return The source address for the page.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value The source address for the page.
         */
        public ImplementationGuidePageComponent setSource(String value) { 
            if (this.source == null)
              this.source = new UriType();
            this.source.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (A short name used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePageComponent.name");
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
         * @param value {@link #name} (A short name used to represent this page in navigational structures such as table of contents, bread crumbs, etc.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuidePageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A short name used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A short name used to represent this page in navigational structures such as table of contents, bread crumbs, etc.
         */
        public ImplementationGuidePageComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #kind} (The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.). This is the underlying object with id, value and extensions. The accessor "getKind" gives direct access to the value
         */
        public Enumeration<GuidePageKind> getKindElement() { 
          if (this.kind == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePageComponent.kind");
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
        public ImplementationGuidePageComponent setKindElement(Enumeration<GuidePageKind> value) { 
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
        public ImplementationGuidePageComponent setKind(GuidePageKind value) { 
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
    // syntactic sugar
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
        public ImplementationGuidePageComponent addType(String value) { //1
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
    // syntactic sugar
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
        public ImplementationGuidePageComponent addPackage(String value) { //1
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
              throw new Error("Attempt to auto-create ImplementationGuidePageComponent.format");
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
        public ImplementationGuidePageComponent setFormatElement(CodeType value) { 
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
        public ImplementationGuidePageComponent setFormat(String value) { 
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
        public List<ImplementationGuidePageComponent> getPage() { 
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuidePageComponent>();
          return this.page;
        }

        public boolean hasPage() { 
          if (this.page == null)
            return false;
          for (ImplementationGuidePageComponent item : this.page)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #page} (Nested Pages/Sections under this page.)
         */
    // syntactic sugar
        public ImplementationGuidePageComponent addPage() { //3
          ImplementationGuidePageComponent t = new ImplementationGuidePageComponent();
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuidePageComponent>();
          this.page.add(t);
          return t;
        }

    // syntactic sugar
        public ImplementationGuidePageComponent addPage(ImplementationGuidePageComponent t) { //3
          if (t == null)
            return this;
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuidePageComponent>();
          this.page.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("source", "uri", "The source address for the page.", 0, java.lang.Integer.MAX_VALUE, source));
          childrenList.add(new Property("name", "string", "A short name used to represent this page in navigational structures such as table of contents, bread crumbs, etc.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("kind", "code", "The kind of page that this is. Some pages are autogenerated (list, example), and other kinds are of interest so that tools can navigate the user to the page of interest.", 0, java.lang.Integer.MAX_VALUE, kind));
          childrenList.add(new Property("type", "code", "For constructed pages, what kind of resources to include in the list.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("package", "string", "For constructed pages, a list of packages to include in the page (or else empty for everything).", 0, java.lang.Integer.MAX_VALUE, package_));
          childrenList.add(new Property("format", "code", "The format of the page.", 0, java.lang.Integer.MAX_VALUE, format));
          childrenList.add(new Property("page", "@ImplementationGuide.page", "Nested Pages/Sections under this page.", 0, java.lang.Integer.MAX_VALUE, page));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // UriType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3292052: /*kind*/ return this.kind == null ? new Base[0] : new Base[] {this.kind}; // Enumeration<GuidePageKind>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeType
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : this.package_.toArray(new Base[this.package_.size()]); // StringType
        case -1268779017: /*format*/ return this.format == null ? new Base[0] : new Base[] {this.format}; // CodeType
        case 3433103: /*page*/ return this.page == null ? new Base[0] : this.page.toArray(new Base[this.page.size()]); // ImplementationGuidePageComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -896505829: // source
          this.source = castToUri(value); // UriType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 3292052: // kind
          this.kind = new GuidePageKindEnumFactory().fromType(value); // Enumeration<GuidePageKind>
          break;
        case 3575610: // type
          this.getType().add(castToCode(value)); // CodeType
          break;
        case -807062458: // package
          this.getPackage().add(castToString(value)); // StringType
          break;
        case -1268779017: // format
          this.format = castToCode(value); // CodeType
          break;
        case 3433103: // page
          this.getPage().add((ImplementationGuidePageComponent) value); // ImplementationGuidePageComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("source"))
          this.source = castToUri(value); // UriType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("kind"))
          this.kind = new GuidePageKindEnumFactory().fromType(value); // Enumeration<GuidePageKind>
        else if (name.equals("type"))
          this.getType().add(castToCode(value));
        else if (name.equals("package"))
          this.getPackage().add(castToString(value));
        else if (name.equals("format"))
          this.format = castToCode(value); // CodeType
        else if (name.equals("page"))
          this.getPage().add((ImplementationGuidePageComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -896505829: throw new FHIRException("Cannot make property source as it is not a complex type"); // UriType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 3292052: throw new FHIRException("Cannot make property kind as it is not a complex type"); // Enumeration<GuidePageKind>
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case -807062458: throw new FHIRException("Cannot make property package as it is not a complex type"); // StringType
        case -1268779017: throw new FHIRException("Cannot make property format as it is not a complex type"); // CodeType
        case 3433103:  return addPage(); // ImplementationGuidePageComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.source");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("kind")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.kind");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.type");
        }
        else if (name.equals("package")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.package");
        }
        else if (name.equals("format")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.format");
        }
        else if (name.equals("page")) {
          return addPage();
        }
        else
          return super.addChild(name);
      }

      public ImplementationGuidePageComponent copy() {
        ImplementationGuidePageComponent dst = new ImplementationGuidePageComponent();
        copyValues(dst);
        dst.source = source == null ? null : source.copy();
        dst.name = name == null ? null : name.copy();
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
          dst.page = new ArrayList<ImplementationGuidePageComponent>();
          for (ImplementationGuidePageComponent i : page)
            dst.page.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuidePageComponent))
          return false;
        ImplementationGuidePageComponent o = (ImplementationGuidePageComponent) other;
        return compareDeep(source, o.source, true) && compareDeep(name, o.name, true) && compareDeep(kind, o.kind, true)
           && compareDeep(type, o.type, true) && compareDeep(package_, o.package_, true) && compareDeep(format, o.format, true)
           && compareDeep(page, o.page, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuidePageComponent))
          return false;
        ImplementationGuidePageComponent o = (ImplementationGuidePageComponent) other;
        return compareValues(source, o.source, true) && compareValues(name, o.name, true) && compareValues(kind, o.kind, true)
           && compareValues(type, o.type, true) && compareValues(package_, o.package_, true) && compareValues(format, o.format, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (source == null || source.isEmpty()) && (name == null || name.isEmpty())
           && (kind == null || kind.isEmpty()) && (type == null || type.isEmpty()) && (package_ == null || package_.isEmpty())
           && (format == null || format.isEmpty()) && (page == null || page.isEmpty());
      }

  public String fhirType() {
    return "ImplementationGuide.page";

  }

  }

    /**
     * An absolute URL that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this implementation guide is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Absolute URL used to reference this Implementation Guide", formalDefinition="An absolute URL that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this implementation guide is (or will be) published." )
    protected UriType url;

    /**
     * The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id for this version of the Implementation Guide", formalDefinition="The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually." )
    protected StringType version;

    /**
     * A free text natural language name identifying the Implementation Guide.
     */
    @Child(name = "name", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this Implementation Guide", formalDefinition="A free text natural language name identifying the Implementation Guide." )
    protected StringType name;

    /**
     * The status of the Implementation Guide.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the Implementation Guide." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the implementation guide.
     */
    @Child(name = "publisher", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the implementation guide." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<ImplementationGuideContactComponent> contact;

    /**
     * The date this version of the implementation guide was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date for this version of the Implementation Guide", formalDefinition="The date this version of the implementation guide was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes." )
    protected DateTimeType date;

    /**
     * A free text natural language description of the Implementation Guide and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Natural language description of the Implementation Guide", formalDefinition="A free text natural language description of the Implementation Guide and its use." )
    protected StringType description;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdiction for which this implementation guide was defined.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The implementation guide is intended to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdiction for which this implementation guide was defined." )
    protected List<CodeableConcept> useContext;

    /**
     * A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    @Child(name = "copyright", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings." )
    protected StringType copyright;

    /**
     * The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 1.4.0 for this version.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="FHIR Version this Implementation Guide targets", formalDefinition="The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 1.4.0 for this version." )
    protected IdType fhirVersion;

    /**
     * Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.
     */
    @Child(name = "dependency", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Another Implementation guide this depends on", formalDefinition="Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides." )
    protected List<ImplementationGuideDependencyComponent> dependency;

    /**
     * A logical group of resources. Logical groups can be used when building pages.
     */
    @Child(name = "package", type = {}, order=13, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Group of resources as used in .page.package", formalDefinition="A logical group of resources. Logical groups can be used when building pages." )
    protected List<ImplementationGuidePackageComponent> package_;

    /**
     * A set of profiles that all resources covered by this implementation guide must conform to.
     */
    @Child(name = "global", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Profiles that apply globally", formalDefinition="A set of profiles that all resources covered by this implementation guide must conform to." )
    protected List<ImplementationGuideGlobalComponent> global;

    /**
     * A binary file that is included in the  implementation guide when it is published.
     */
    @Child(name = "binary", type = {UriType.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Image, css, script, etc.", formalDefinition="A binary file that is included in the  implementation guide when it is published." )
    protected List<UriType> binary;

    /**
     * A page / section in the implementation guide. The root page is the implementation guide home page.
     */
    @Child(name = "page", type = {}, order=16, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Page/Section in the Guide", formalDefinition="A page / section in the implementation guide. The root page is the implementation guide home page." )
    protected ImplementationGuidePageComponent page;

    private static final long serialVersionUID = 1150122415L;

  /**
   * Constructor
   */
    public ImplementationGuide() {
      super();
    }

  /**
   * Constructor
   */
    public ImplementationGuide(UriType url, StringType name, Enumeration<ConformanceResourceStatus> status, ImplementationGuidePageComponent page) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
      this.page = page;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this implementation guide is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this implementation guide is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImplementationGuide setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this implementation guide is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this implementation guide is (or will be) published.
     */
    public ImplementationGuide setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ImplementationGuide setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.
     */
    public ImplementationGuide setVersion(String value) { 
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
     * @return {@link #name} (A free text natural language name identifying the Implementation Guide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.name");
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
     * @param value {@link #name} (A free text natural language name identifying the Implementation Guide.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ImplementationGuide setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the Implementation Guide.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the Implementation Guide.
     */
    public ImplementationGuide setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of the Implementation Guide.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.status");
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
     * @param value {@link #status} (The status of the Implementation Guide.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ImplementationGuide setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the Implementation Guide.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the Implementation Guide.
     */
    public ImplementationGuide setStatus(ConformanceResourceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.experimental");
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
     * @param value {@link #experimental} (This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ImplementationGuide setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public ImplementationGuide setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the implementation guide.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ImplementationGuide setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the implementation guide.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the implementation guide.
     */
    public ImplementationGuide setPublisher(String value) { 
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
    public List<ImplementationGuideContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ImplementationGuideContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ImplementationGuideContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public ImplementationGuideContactComponent addContact() { //3
      ImplementationGuideContactComponent t = new ImplementationGuideContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<ImplementationGuideContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public ImplementationGuide addContact(ImplementationGuideContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ImplementationGuideContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date this version of the implementation guide was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.date");
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
     * @param value {@link #date} (The date this version of the implementation guide was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ImplementationGuide setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date this version of the implementation guide was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date this version of the implementation guide was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.
     */
    public ImplementationGuide setDate(Date value) { 
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
     * @return {@link #description} (A free text natural language description of the Implementation Guide and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.description");
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
     * @param value {@link #description} (A free text natural language description of the Implementation Guide and its use.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImplementationGuide setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the Implementation Guide and its use.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the Implementation Guide and its use.
     */
    public ImplementationGuide setDescription(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdiction for which this implementation guide was defined.)
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdiction for which this implementation guide was defined.)
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
    public ImplementationGuide addUseContext(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<CodeableConcept>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return {@link #copyright} (A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.copyright");
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
     * @param value {@link #copyright} (A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ImplementationGuide setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    public ImplementationGuide setCopyright(String value) { 
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
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 1.4.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public IdType getFhirVersionElement() { 
      if (this.fhirVersion == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.fhirVersion");
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
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 1.4.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public ImplementationGuide setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 1.4.0 for this version.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 1.4.0 for this version.
     */
    public ImplementationGuide setFhirVersion(String value) { 
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
    public List<ImplementationGuideDependencyComponent> getDependency() { 
      if (this.dependency == null)
        this.dependency = new ArrayList<ImplementationGuideDependencyComponent>();
      return this.dependency;
    }

    public boolean hasDependency() { 
      if (this.dependency == null)
        return false;
      for (ImplementationGuideDependencyComponent item : this.dependency)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dependency} (Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.)
     */
    // syntactic sugar
    public ImplementationGuideDependencyComponent addDependency() { //3
      ImplementationGuideDependencyComponent t = new ImplementationGuideDependencyComponent();
      if (this.dependency == null)
        this.dependency = new ArrayList<ImplementationGuideDependencyComponent>();
      this.dependency.add(t);
      return t;
    }

    // syntactic sugar
    public ImplementationGuide addDependency(ImplementationGuideDependencyComponent t) { //3
      if (t == null)
        return this;
      if (this.dependency == null)
        this.dependency = new ArrayList<ImplementationGuideDependencyComponent>();
      this.dependency.add(t);
      return this;
    }

    /**
     * @return {@link #package_} (A logical group of resources. Logical groups can be used when building pages.)
     */
    public List<ImplementationGuidePackageComponent> getPackage() { 
      if (this.package_ == null)
        this.package_ = new ArrayList<ImplementationGuidePackageComponent>();
      return this.package_;
    }

    public boolean hasPackage() { 
      if (this.package_ == null)
        return false;
      for (ImplementationGuidePackageComponent item : this.package_)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #package_} (A logical group of resources. Logical groups can be used when building pages.)
     */
    // syntactic sugar
    public ImplementationGuidePackageComponent addPackage() { //3
      ImplementationGuidePackageComponent t = new ImplementationGuidePackageComponent();
      if (this.package_ == null)
        this.package_ = new ArrayList<ImplementationGuidePackageComponent>();
      this.package_.add(t);
      return t;
    }

    // syntactic sugar
    public ImplementationGuide addPackage(ImplementationGuidePackageComponent t) { //3
      if (t == null)
        return this;
      if (this.package_ == null)
        this.package_ = new ArrayList<ImplementationGuidePackageComponent>();
      this.package_.add(t);
      return this;
    }

    /**
     * @return {@link #global} (A set of profiles that all resources covered by this implementation guide must conform to.)
     */
    public List<ImplementationGuideGlobalComponent> getGlobal() { 
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideGlobalComponent>();
      return this.global;
    }

    public boolean hasGlobal() { 
      if (this.global == null)
        return false;
      for (ImplementationGuideGlobalComponent item : this.global)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #global} (A set of profiles that all resources covered by this implementation guide must conform to.)
     */
    // syntactic sugar
    public ImplementationGuideGlobalComponent addGlobal() { //3
      ImplementationGuideGlobalComponent t = new ImplementationGuideGlobalComponent();
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideGlobalComponent>();
      this.global.add(t);
      return t;
    }

    // syntactic sugar
    public ImplementationGuide addGlobal(ImplementationGuideGlobalComponent t) { //3
      if (t == null)
        return this;
      if (this.global == null)
        this.global = new ArrayList<ImplementationGuideGlobalComponent>();
      this.global.add(t);
      return this;
    }

    /**
     * @return {@link #binary} (A binary file that is included in the  implementation guide when it is published.)
     */
    public List<UriType> getBinary() { 
      if (this.binary == null)
        this.binary = new ArrayList<UriType>();
      return this.binary;
    }

    public boolean hasBinary() { 
      if (this.binary == null)
        return false;
      for (UriType item : this.binary)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #binary} (A binary file that is included in the  implementation guide when it is published.)
     */
    // syntactic sugar
    public UriType addBinaryElement() {//2 
      UriType t = new UriType();
      if (this.binary == null)
        this.binary = new ArrayList<UriType>();
      this.binary.add(t);
      return t;
    }

    /**
     * @param value {@link #binary} (A binary file that is included in the  implementation guide when it is published.)
     */
    public ImplementationGuide addBinary(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.binary == null)
        this.binary = new ArrayList<UriType>();
      this.binary.add(t);
      return this;
    }

    /**
     * @param value {@link #binary} (A binary file that is included in the  implementation guide when it is published.)
     */
    public boolean hasBinary(String value) { 
      if (this.binary == null)
        return false;
      for (UriType v : this.binary)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
     */
    public ImplementationGuidePageComponent getPage() { 
      if (this.page == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImplementationGuide.page");
        else if (Configuration.doAutoCreate())
          this.page = new ImplementationGuidePageComponent(); // cc
      return this.page;
    }

    public boolean hasPage() { 
      return this.page != null && !this.page.isEmpty();
    }

    /**
     * @param value {@link #page} (A page / section in the implementation guide. The root page is the implementation guide home page.)
     */
    public ImplementationGuide setPage(ImplementationGuidePageComponent value) { 
      this.page = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this implementation guide when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this implementation guide is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the Implementation Guide.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of the Implementation Guide.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the implementation guide.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date this version of the implementation guide was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the implementation guide changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A free text natural language description of the Implementation Guide and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdiction for which this implementation guide was defined.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 1.4.0 for this version.", 0, java.lang.Integer.MAX_VALUE, fhirVersion));
        childrenList.add(new Property("dependency", "", "Another implementation guide that this implementation depends on. Typically, an implementation guide uses value sets, profiles etc.defined in other implementation guides.", 0, java.lang.Integer.MAX_VALUE, dependency));
        childrenList.add(new Property("package", "", "A logical group of resources. Logical groups can be used when building pages.", 0, java.lang.Integer.MAX_VALUE, package_));
        childrenList.add(new Property("global", "", "A set of profiles that all resources covered by this implementation guide must conform to.", 0, java.lang.Integer.MAX_VALUE, global));
        childrenList.add(new Property("binary", "uri", "A binary file that is included in the  implementation guide when it is published.", 0, java.lang.Integer.MAX_VALUE, binary));
        childrenList.add(new Property("page", "", "A page / section in the implementation guide. The root page is the implementation guide home page.", 0, java.lang.Integer.MAX_VALUE, page));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ConformanceResourceStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ImplementationGuideContactComponent
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // CodeableConcept
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // StringType
        case 461006061: /*fhirVersion*/ return this.fhirVersion == null ? new Base[0] : new Base[] {this.fhirVersion}; // IdType
        case -26291381: /*dependency*/ return this.dependency == null ? new Base[0] : this.dependency.toArray(new Base[this.dependency.size()]); // ImplementationGuideDependencyComponent
        case -807062458: /*package*/ return this.package_ == null ? new Base[0] : this.package_.toArray(new Base[this.package_.size()]); // ImplementationGuidePackageComponent
        case -1243020381: /*global*/ return this.global == null ? new Base[0] : this.global.toArray(new Base[this.global.size()]); // ImplementationGuideGlobalComponent
        case -1388966911: /*binary*/ return this.binary == null ? new Base[0] : this.binary.toArray(new Base[this.binary.size()]); // UriType
        case 3433103: /*page*/ return this.page == null ? new Base[0] : new Base[] {this.page}; // ImplementationGuidePageComponent
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
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add((ImplementationGuideContactComponent) value); // ImplementationGuideContactComponent
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
        case 1522889671: // copyright
          this.copyright = castToString(value); // StringType
          break;
        case 461006061: // fhirVersion
          this.fhirVersion = castToId(value); // IdType
          break;
        case -26291381: // dependency
          this.getDependency().add((ImplementationGuideDependencyComponent) value); // ImplementationGuideDependencyComponent
          break;
        case -807062458: // package
          this.getPackage().add((ImplementationGuidePackageComponent) value); // ImplementationGuidePackageComponent
          break;
        case -1243020381: // global
          this.getGlobal().add((ImplementationGuideGlobalComponent) value); // ImplementationGuideGlobalComponent
          break;
        case -1388966911: // binary
          this.getBinary().add(castToUri(value)); // UriType
          break;
        case 3433103: // page
          this.page = (ImplementationGuidePageComponent) value; // ImplementationGuidePageComponent
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
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add((ImplementationGuideContactComponent) value);
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("useContext"))
          this.getUseContext().add(castToCodeableConcept(value));
        else if (name.equals("copyright"))
          this.copyright = castToString(value); // StringType
        else if (name.equals("fhirVersion"))
          this.fhirVersion = castToId(value); // IdType
        else if (name.equals("dependency"))
          this.getDependency().add((ImplementationGuideDependencyComponent) value);
        else if (name.equals("package"))
          this.getPackage().add((ImplementationGuidePackageComponent) value);
        else if (name.equals("global"))
          this.getGlobal().add((ImplementationGuideGlobalComponent) value);
        else if (name.equals("binary"))
          this.getBinary().add(castToUri(value));
        else if (name.equals("page"))
          this.page = (ImplementationGuidePageComponent) value; // ImplementationGuidePageComponent
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
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // ImplementationGuideContactComponent
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -669707736:  return addUseContext(); // CodeableConcept
        case 1522889671: throw new FHIRException("Cannot make property copyright as it is not a complex type"); // StringType
        case 461006061: throw new FHIRException("Cannot make property fhirVersion as it is not a complex type"); // IdType
        case -26291381:  return addDependency(); // ImplementationGuideDependencyComponent
        case -807062458:  return addPackage(); // ImplementationGuidePackageComponent
        case -1243020381:  return addGlobal(); // ImplementationGuideGlobalComponent
        case -1388966911: throw new FHIRException("Cannot make property binary as it is not a complex type"); // UriType
        case 3433103:  return getPage(); // ImplementationGuidePageComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.experimental");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.date");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.copyright");
        }
        else if (name.equals("fhirVersion")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.fhirVersion");
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
        else if (name.equals("binary")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImplementationGuide.binary");
        }
        else if (name.equals("page")) {
          this.page = new ImplementationGuidePageComponent();
          return this.page;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImplementationGuide";

  }

      public ImplementationGuide copy() {
        ImplementationGuide dst = new ImplementationGuide();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ImplementationGuideContactComponent>();
          for (ImplementationGuideContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        if (dependency != null) {
          dst.dependency = new ArrayList<ImplementationGuideDependencyComponent>();
          for (ImplementationGuideDependencyComponent i : dependency)
            dst.dependency.add(i.copy());
        };
        if (package_ != null) {
          dst.package_ = new ArrayList<ImplementationGuidePackageComponent>();
          for (ImplementationGuidePackageComponent i : package_)
            dst.package_.add(i.copy());
        };
        if (global != null) {
          dst.global = new ArrayList<ImplementationGuideGlobalComponent>();
          for (ImplementationGuideGlobalComponent i : global)
            dst.global.add(i.copy());
        };
        if (binary != null) {
          dst.binary = new ArrayList<UriType>();
          for (UriType i : binary)
            dst.binary.add(i.copy());
        };
        dst.page = page == null ? null : page.copy();
        return dst;
      }

      protected ImplementationGuide typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuide))
          return false;
        ImplementationGuide o = (ImplementationGuide) other;
        return compareDeep(url, o.url, true) && compareDeep(version, o.version, true) && compareDeep(name, o.name, true)
           && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true) && compareDeep(description, o.description, true)
           && compareDeep(useContext, o.useContext, true) && compareDeep(copyright, o.copyright, true) && compareDeep(fhirVersion, o.fhirVersion, true)
           && compareDeep(dependency, o.dependency, true) && compareDeep(package_, o.package_, true) && compareDeep(global, o.global, true)
           && compareDeep(binary, o.binary, true) && compareDeep(page, o.page, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuide))
          return false;
        ImplementationGuide o = (ImplementationGuide) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(description, o.description, true) && compareValues(copyright, o.copyright, true)
           && compareValues(fhirVersion, o.fhirVersion, true) && compareValues(binary, o.binary, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (version == null || version.isEmpty())
           && (name == null || name.isEmpty()) && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty())
           && (description == null || description.isEmpty()) && (useContext == null || useContext.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (fhirVersion == null || fhirVersion.isEmpty())
           && (dependency == null || dependency.isEmpty()) && (package_ == null || package_.isEmpty())
           && (global == null || global.isEmpty()) && (binary == null || binary.isEmpty()) && (page == null || page.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImplementationGuide;
   }

 /**
   * Search parameter: <b>dependency</b>
   * <p>
   * Description: <b>Where to find dependency</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuide.dependency.uri</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dependency", path="ImplementationGuide.dependency.uri", description="Where to find dependency", type="uri" )
  public static final String SP_DEPENDENCY = "dependency";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dependency</b>
   * <p>
   * Description: <b>Where to find dependency</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuide.dependency.uri</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam DEPENDENCY = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_DEPENDENCY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ImplementationGuide.status", description="The current status of the implementation guide", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ImplementationGuide.description", description="Text search in the description of the implementation guide", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search in the description of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Name of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="ImplementationGuide.name", description="Name of the implementation guide", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Name of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.package.resource.source[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="ImplementationGuide.package.resource.source", description="Location of the resource", type="reference" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Location of the resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImplementationGuide.package.resource.source[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImplementationGuide:resource</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESOURCE = new ca.uhn.fhir.model.api.Include("ImplementationGuide:resource").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the structure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.useContext</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ImplementationGuide.useContext", description="A use context assigned to the structure", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>A use context assigned to the structure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.useContext</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>experimental</b>
   * <p>
   * Description: <b>If for testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.experimental</b><br>
   * </p>
   */
  @SearchParamDefinition(name="experimental", path="ImplementationGuide.experimental", description="If for testing purposes, not real usage", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>experimental</b>
   * <p>
   * Description: <b>If for testing purposes, not real usage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.experimental</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EXPERIMENTAL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EXPERIMENTAL);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The implementation guide publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuide.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ImplementationGuide.date", description="The implementation guide publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The implementation guide publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImplementationGuide.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>Absolute URL used to reference this Implementation Guide</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuide.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="ImplementationGuide.url", description="Absolute URL used to reference this Implementation Guide", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>Absolute URL used to reference this Implementation Guide</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>ImplementationGuide.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="ImplementationGuide.publisher", description="Name of the publisher of the implementation guide", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the implementation guide</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ImplementationGuide.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The version identifier of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ImplementationGuide.version", description="The version identifier of the implementation guide", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The version identifier of the implementation guide</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImplementationGuide.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);


}

