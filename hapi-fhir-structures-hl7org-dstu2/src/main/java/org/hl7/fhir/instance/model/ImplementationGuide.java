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

// Generated on Tue, Jul 21, 2015 10:37-0400 for FHIR v0.5.0

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
 * A set of rules or how FHIR is used to solve a particular problem. This resource is used to gather all the parts of an implementation guide into a logical whole, and to publish a computable definition of all the parts.
 */
@ResourceDef(name="ImplementationGuide", profile="http://hl7.org/fhir/Profile/ImplementationGuide")
public class ImplementationGuide extends DomainResource {

    public enum PagePurpose {
        /**
         * The home page - the start page for the appendix
         */
        HOME, 
        /**
         * Introduction material
         */
        INTRO, 
        /**
         * General Content
         */
        CONTENT, 
        /**
         * Security Related Page
         */
        SECURITY, 
        /**
         * An appendix to the IG
         */
        APPENDIX, 
        /**
         * added to help the parsers
         */
        NULL;
        public static PagePurpose fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return HOME;
        if ("intro".equals(codeString))
          return INTRO;
        if ("content".equals(codeString))
          return CONTENT;
        if ("security".equals(codeString))
          return SECURITY;
        if ("appendix".equals(codeString))
          return APPENDIX;
        throw new Exception("Unknown PagePurpose code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HOME: return "home";
            case INTRO: return "intro";
            case CONTENT: return "content";
            case SECURITY: return "security";
            case APPENDIX: return "appendix";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HOME: return "http://hl7.org/fhir/page-purpose";
            case INTRO: return "http://hl7.org/fhir/page-purpose";
            case CONTENT: return "http://hl7.org/fhir/page-purpose";
            case SECURITY: return "http://hl7.org/fhir/page-purpose";
            case APPENDIX: return "http://hl7.org/fhir/page-purpose";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HOME: return "The home page - the start page for the appendix";
            case INTRO: return "Introduction material";
            case CONTENT: return "General Content";
            case SECURITY: return "Security Related Page";
            case APPENDIX: return "An appendix to the IG";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HOME: return "Home Page";
            case INTRO: return "Introduction";
            case CONTENT: return "General Content";
            case SECURITY: return "Security Page";
            case APPENDIX: return "Appendix";
            default: return "?";
          }
        }
    }

  public static class PagePurposeEnumFactory implements EnumFactory<PagePurpose> {
    public PagePurpose fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return PagePurpose.HOME;
        if ("intro".equals(codeString))
          return PagePurpose.INTRO;
        if ("content".equals(codeString))
          return PagePurpose.CONTENT;
        if ("security".equals(codeString))
          return PagePurpose.SECURITY;
        if ("appendix".equals(codeString))
          return PagePurpose.APPENDIX;
        throw new IllegalArgumentException("Unknown PagePurpose code '"+codeString+"'");
        }
    public String toCode(PagePurpose code) {
      if (code == PagePurpose.HOME)
        return "home";
      if (code == PagePurpose.INTRO)
        return "intro";
      if (code == PagePurpose.CONTENT)
        return "content";
      if (code == PagePurpose.SECURITY)
        return "security";
      if (code == PagePurpose.APPENDIX)
        return "appendix";
      return "?";
      }
    }

    @Block()
    public static class ImplementationGuideContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the implementation guide.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the implementation guide." )
        protected StringType name;

        /**
         * Contact details for individual (if a name was provided) or the publisher.
         */
        @Child(name = "telecom", type = {ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Contact details for individual or publisher", formalDefinition="Contact details for individual (if a name was provided) or the publisher." )
        protected List<ContactPoint> telecom;

        private static final long serialVersionUID = -1179697803L;

    /*
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

  }

    @Block()
    public static class ImplementationGuidePageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A URL that identifies the html page.
         */
        @Child(name = "source", type = {UriType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Source for the page", formalDefinition="A URL that identifies the html page." )
        protected UriType source;

        /**
         * A code that specifies how the page is used in the IG.
         */
        @Child(name = "purpose", type = {CodeType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="home | intro | content | security | appendix", formalDefinition="A code that specifies how the page is used in the IG." )
        protected Enumeration<PagePurpose> purpose;

        private static final long serialVersionUID = -1176194637L;

    /*
     * Constructor
     */
      public ImplementationGuidePageComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImplementationGuidePageComponent(UriType source, Enumeration<PagePurpose> purpose) {
        super();
        this.source = source;
        this.purpose = purpose;
      }

        /**
         * @return {@link #source} (A URL that identifies the html page.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
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
         * @param value {@link #source} (A URL that identifies the html page.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
         */
        public ImplementationGuidePageComponent setSourceElement(UriType value) { 
          this.source = value;
          return this;
        }

        /**
         * @return A URL that identifies the html page.
         */
        public String getSource() { 
          return this.source == null ? null : this.source.getValue();
        }

        /**
         * @param value A URL that identifies the html page.
         */
        public ImplementationGuidePageComponent setSource(String value) { 
            if (this.source == null)
              this.source = new UriType();
            this.source.setValue(value);
          return this;
        }

        /**
         * @return {@link #purpose} (A code that specifies how the page is used in the IG.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
         */
        public Enumeration<PagePurpose> getPurposeElement() { 
          if (this.purpose == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePageComponent.purpose");
            else if (Configuration.doAutoCreate())
              this.purpose = new Enumeration<PagePurpose>(new PagePurposeEnumFactory()); // bb
          return this.purpose;
        }

        public boolean hasPurposeElement() { 
          return this.purpose != null && !this.purpose.isEmpty();
        }

        public boolean hasPurpose() { 
          return this.purpose != null && !this.purpose.isEmpty();
        }

        /**
         * @param value {@link #purpose} (A code that specifies how the page is used in the IG.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
         */
        public ImplementationGuidePageComponent setPurposeElement(Enumeration<PagePurpose> value) { 
          this.purpose = value;
          return this;
        }

        /**
         * @return A code that specifies how the page is used in the IG.
         */
        public PagePurpose getPurpose() { 
          return this.purpose == null ? null : this.purpose.getValue();
        }

        /**
         * @param value A code that specifies how the page is used in the IG.
         */
        public ImplementationGuidePageComponent setPurpose(PagePurpose value) { 
            if (this.purpose == null)
              this.purpose = new Enumeration<PagePurpose>(new PagePurposeEnumFactory());
            this.purpose.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("source", "uri", "A URL that identifies the html page.", 0, java.lang.Integer.MAX_VALUE, source));
          childrenList.add(new Property("purpose", "code", "A code that specifies how the page is used in the IG.", 0, java.lang.Integer.MAX_VALUE, purpose));
        }

      public ImplementationGuidePageComponent copy() {
        ImplementationGuidePageComponent dst = new ImplementationGuidePageComponent();
        copyValues(dst);
        dst.source = source == null ? null : source.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuidePageComponent))
          return false;
        ImplementationGuidePageComponent o = (ImplementationGuidePageComponent) other;
        return compareDeep(source, o.source, true) && compareDeep(purpose, o.purpose, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuidePageComponent))
          return false;
        ImplementationGuidePageComponent o = (ImplementationGuidePageComponent) other;
        return compareValues(source, o.source, true) && compareValues(purpose, o.purpose, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (source == null || source.isEmpty()) && (purpose == null || purpose.isEmpty())
          ;
      }

  }

    @Block()
    public static class ImplementationGuidePackageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A human readable name that introduces the item.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Title of the item", formalDefinition="A human readable name that introduces the item." )
        protected StringType name;

        /**
         * An item in the set of resources that is a section in the implementation Guide.
         */
        @Child(name = "item", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="An item in the section / package", formalDefinition="An item in the set of resources that is a section in the implementation Guide." )
        protected List<ImplementationGuidePackageItemComponent> item;

        /**
         * An html page that is published with the Implementation Guide, as part of the package.
         */
        @Child(name = "page", type = {ImplementationGuidePageComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Page in the Implementation Guide", formalDefinition="An html page that is published with the Implementation Guide, as part of the package." )
        protected List<ImplementationGuidePageComponent> page;

        private static final long serialVersionUID = -110143794L;

    /*
     * Constructor
     */
      public ImplementationGuidePackageComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImplementationGuidePackageComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (A human readable name that introduces the item.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
         * @param value {@link #name} (A human readable name that introduces the item.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuidePackageComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A human readable name that introduces the item.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A human readable name that introduces the item.
         */
        public ImplementationGuidePackageComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #item} (An item in the set of resources that is a section in the implementation Guide.)
         */
        public List<ImplementationGuidePackageItemComponent> getItem() { 
          if (this.item == null)
            this.item = new ArrayList<ImplementationGuidePackageItemComponent>();
          return this.item;
        }

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (ImplementationGuidePackageItemComponent item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #item} (An item in the set of resources that is a section in the implementation Guide.)
         */
    // syntactic sugar
        public ImplementationGuidePackageItemComponent addItem() { //3
          ImplementationGuidePackageItemComponent t = new ImplementationGuidePackageItemComponent();
          if (this.item == null)
            this.item = new ArrayList<ImplementationGuidePackageItemComponent>();
          this.item.add(t);
          return t;
        }

    // syntactic sugar
        public ImplementationGuidePackageComponent addItem(ImplementationGuidePackageItemComponent t) { //3
          if (t == null)
            return this;
          if (this.item == null)
            this.item = new ArrayList<ImplementationGuidePackageItemComponent>();
          this.item.add(t);
          return this;
        }

        /**
         * @return {@link #page} (An html page that is published with the Implementation Guide, as part of the package.)
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
         * @return {@link #page} (An html page that is published with the Implementation Guide, as part of the package.)
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
        public ImplementationGuidePackageComponent addPage(ImplementationGuidePageComponent t) { //3
          if (t == null)
            return this;
          if (this.page == null)
            this.page = new ArrayList<ImplementationGuidePageComponent>();
          this.page.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "A human readable name that introduces the item.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("item", "", "An item in the set of resources that is a section in the implementation Guide.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("page", "@ImplementationGuide.page", "An html page that is published with the Implementation Guide, as part of the package.", 0, java.lang.Integer.MAX_VALUE, page));
        }

      public ImplementationGuidePackageComponent copy() {
        ImplementationGuidePackageComponent dst = new ImplementationGuidePackageComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        if (item != null) {
          dst.item = new ArrayList<ImplementationGuidePackageItemComponent>();
          for (ImplementationGuidePackageItemComponent i : item)
            dst.item.add(i.copy());
        };
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
        if (!(other instanceof ImplementationGuidePackageComponent))
          return false;
        ImplementationGuidePackageComponent o = (ImplementationGuidePackageComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(item, o.item, true) && compareDeep(page, o.page, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageComponent))
          return false;
        ImplementationGuidePackageComponent o = (ImplementationGuidePackageComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (item == null || item.isEmpty())
           && (page == null || page.isEmpty());
      }

  }

    @Block()
    public static class ImplementationGuidePackageItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A human readable name that introduces the package.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Title of the package", formalDefinition="A human readable name that introduces the package." )
        protected StringType name;

        /**
         * A reference to a conformance resource that is part of the implementation Guide.
         */
        @Child(name = "content", type = {}, order=2, min=1, max=1)
        @Description(shortDefinition="StructureDefinition, ConformanceStatement, ValueSet, DataElement etc", formalDefinition="A reference to a conformance resource that is part of the implementation Guide." )
        protected Reference content;

        /**
         * The actual object that is the target of the reference (A reference to a conformance resource that is part of the implementation Guide.)
         */
        protected Resource contentTarget;

        /**
         * If the item is a structure definition, an example that conforms to the structure definition.
         */
        @Child(name = "example", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="An example (If Item is StructureDefinition)", formalDefinition="If the item is a structure definition, an example that conforms to the structure definition." )
        protected List<ImplementationGuidePackageItemExampleComponent> example;

        private static final long serialVersionUID = -1640700896L;

    /*
     * Constructor
     */
      public ImplementationGuidePackageItemComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImplementationGuidePackageItemComponent(Reference content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #name} (A human readable name that introduces the package.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageItemComponent.name");
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
         * @param value {@link #name} (A human readable name that introduces the package.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuidePackageItemComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A human readable name that introduces the package.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A human readable name that introduces the package.
         */
        public ImplementationGuidePackageItemComponent setName(String value) { 
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
         * @return {@link #content} (A reference to a conformance resource that is part of the implementation Guide.)
         */
        public Reference getContent() { 
          if (this.content == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageItemComponent.content");
            else if (Configuration.doAutoCreate())
              this.content = new Reference(); // cc
          return this.content;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (A reference to a conformance resource that is part of the implementation Guide.)
         */
        public ImplementationGuidePackageItemComponent setContent(Reference value) { 
          this.content = value;
          return this;
        }

        /**
         * @return {@link #content} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a conformance resource that is part of the implementation Guide.)
         */
        public Resource getContentTarget() { 
          return this.contentTarget;
        }

        /**
         * @param value {@link #content} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a conformance resource that is part of the implementation Guide.)
         */
        public ImplementationGuidePackageItemComponent setContentTarget(Resource value) { 
          this.contentTarget = value;
          return this;
        }

        /**
         * @return {@link #example} (If the item is a structure definition, an example that conforms to the structure definition.)
         */
        public List<ImplementationGuidePackageItemExampleComponent> getExample() { 
          if (this.example == null)
            this.example = new ArrayList<ImplementationGuidePackageItemExampleComponent>();
          return this.example;
        }

        public boolean hasExample() { 
          if (this.example == null)
            return false;
          for (ImplementationGuidePackageItemExampleComponent item : this.example)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #example} (If the item is a structure definition, an example that conforms to the structure definition.)
         */
    // syntactic sugar
        public ImplementationGuidePackageItemExampleComponent addExample() { //3
          ImplementationGuidePackageItemExampleComponent t = new ImplementationGuidePackageItemExampleComponent();
          if (this.example == null)
            this.example = new ArrayList<ImplementationGuidePackageItemExampleComponent>();
          this.example.add(t);
          return t;
        }

    // syntactic sugar
        public ImplementationGuidePackageItemComponent addExample(ImplementationGuidePackageItemExampleComponent t) { //3
          if (t == null)
            return this;
          if (this.example == null)
            this.example = new ArrayList<ImplementationGuidePackageItemExampleComponent>();
          this.example.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "A human readable name that introduces the package.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("content", "Reference(Any)", "A reference to a conformance resource that is part of the implementation Guide.", 0, java.lang.Integer.MAX_VALUE, content));
          childrenList.add(new Property("example", "", "If the item is a structure definition, an example that conforms to the structure definition.", 0, java.lang.Integer.MAX_VALUE, example));
        }

      public ImplementationGuidePackageItemComponent copy() {
        ImplementationGuidePackageItemComponent dst = new ImplementationGuidePackageItemComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.content = content == null ? null : content.copy();
        if (example != null) {
          dst.example = new ArrayList<ImplementationGuidePackageItemExampleComponent>();
          for (ImplementationGuidePackageItemExampleComponent i : example)
            dst.example.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageItemComponent))
          return false;
        ImplementationGuidePackageItemComponent o = (ImplementationGuidePackageItemComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(content, o.content, true) && compareDeep(example, o.example, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageItemComponent))
          return false;
        ImplementationGuidePackageItemComponent o = (ImplementationGuidePackageItemComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (content == null || content.isEmpty())
           && (example == null || example.isEmpty());
      }

  }

    @Block()
    public static class ImplementationGuidePackageItemExampleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A human readable name that introduces the example.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Title of the example", formalDefinition="A human readable name that introduces the example." )
        protected StringType name;

        /**
         * A reference to a resource that is an example for a structure definition.
         */
        @Child(name = "target", type = {}, order=2, min=1, max=1)
        @Description(shortDefinition="Example for a StructureDefinition", formalDefinition="A reference to a resource that is an example for a structure definition." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (A reference to a resource that is an example for a structure definition.)
         */
        protected Resource targetTarget;

        /**
         * Additional text that explains why the example was provided (e.g. what to look for in it).
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Explanatory Text", formalDefinition="Additional text that explains why the example was provided (e.g. what to look for in it)." )
        protected StringType description;

        private static final long serialVersionUID = -1636938221L;

    /*
     * Constructor
     */
      public ImplementationGuidePackageItemExampleComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImplementationGuidePackageItemExampleComponent(StringType name, Reference target, StringType description) {
        super();
        this.name = name;
        this.target = target;
        this.description = description;
      }

        /**
         * @return {@link #name} (A human readable name that introduces the example.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageItemExampleComponent.name");
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
         * @param value {@link #name} (A human readable name that introduces the example.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ImplementationGuidePackageItemExampleComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A human readable name that introduces the example.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A human readable name that introduces the example.
         */
        public ImplementationGuidePackageItemExampleComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #target} (A reference to a resource that is an example for a structure definition.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageItemExampleComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (A reference to a resource that is an example for a structure definition.)
         */
        public ImplementationGuidePackageItemExampleComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a resource that is an example for a structure definition.)
         */
        public Resource getTargetTarget() { 
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a resource that is an example for a structure definition.)
         */
        public ImplementationGuidePackageItemExampleComponent setTargetTarget(Resource value) { 
          this.targetTarget = value;
          return this;
        }

        /**
         * @return {@link #description} (Additional text that explains why the example was provided (e.g. what to look for in it).). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuidePackageItemExampleComponent.description");
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
         * @param value {@link #description} (Additional text that explains why the example was provided (e.g. what to look for in it).). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ImplementationGuidePackageItemExampleComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Additional text that explains why the example was provided (e.g. what to look for in it).
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Additional text that explains why the example was provided (e.g. what to look for in it).
         */
        public ImplementationGuidePackageItemExampleComponent setDescription(String value) { 
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "A human readable name that introduces the example.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("target", "Reference(Any)", "A reference to a resource that is an example for a structure definition.", 0, java.lang.Integer.MAX_VALUE, target));
          childrenList.add(new Property("description", "string", "Additional text that explains why the example was provided (e.g. what to look for in it).", 0, java.lang.Integer.MAX_VALUE, description));
        }

      public ImplementationGuidePackageItemExampleComponent copy() {
        ImplementationGuidePackageItemExampleComponent dst = new ImplementationGuidePackageItemExampleComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.target = target == null ? null : target.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageItemExampleComponent))
          return false;
        ImplementationGuidePackageItemExampleComponent o = (ImplementationGuidePackageItemExampleComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(target, o.target, true) && compareDeep(description, o.description, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuidePackageItemExampleComponent))
          return false;
        ImplementationGuidePackageItemExampleComponent o = (ImplementationGuidePackageItemExampleComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (target == null || target.isEmpty())
           && (description == null || description.isEmpty());
      }

  }

    @Block()
    public static class ImplementationGuideDefaultComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of resource that this default applies to.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Type of resource default applies to", formalDefinition="The type of resource that this default applies to." )
        protected CodeType type;

        /**
         * The profile (StructureDefinition resource) that applies by default.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=2, min=1, max=1)
        @Description(shortDefinition="StructureDefinition that applies by default", formalDefinition="The profile (StructureDefinition resource) that applies by default." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (The profile (StructureDefinition resource) that applies by default.)
         */
        protected StructureDefinition profileTarget;

        private static final long serialVersionUID = 2011731959L;

    /*
     * Constructor
     */
      public ImplementationGuideDefaultComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ImplementationGuideDefaultComponent(CodeType type, Reference profile) {
        super();
        this.type = type;
        this.profile = profile;
      }

        /**
         * @return {@link #type} (The type of resource that this default applies to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefaultComponent.type");
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
         * @param value {@link #type} (The type of resource that this default applies to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ImplementationGuideDefaultComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of resource that this default applies to.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of resource that this default applies to.
         */
        public ImplementationGuideDefaultComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (The profile (StructureDefinition resource) that applies by default.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefaultComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (The profile (StructureDefinition resource) that applies by default.)
         */
        public ImplementationGuideDefaultComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The profile (StructureDefinition resource) that applies by default.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ImplementationGuideDefaultComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The profile (StructureDefinition resource) that applies by default.)
         */
        public ImplementationGuideDefaultComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of resource that this default applies to.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "The profile (StructureDefinition resource) that applies by default.", 0, java.lang.Integer.MAX_VALUE, profile));
        }

      public ImplementationGuideDefaultComponent copy() {
        ImplementationGuideDefaultComponent dst = new ImplementationGuideDefaultComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ImplementationGuideDefaultComponent))
          return false;
        ImplementationGuideDefaultComponent o = (ImplementationGuideDefaultComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuideDefaultComponent))
          return false;
        ImplementationGuideDefaultComponent o = (ImplementationGuideDefaultComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (profile == null || profile.isEmpty())
          ;
      }

  }

    /**
     * An absolute URL at which this Implementation Guide is (or will be) published, and which is used to reference this Implementation Guide in conformance statements.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Literal URL used to reference this Implementation Guide", formalDefinition="An absolute URL at which this Implementation Guide is (or will be) published, and which is used to reference this Implementation Guide in conformance statements." )
    protected UriType url;

    /**
     * The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Logical id for this version of the Implementation Guide", formalDefinition="The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually." )
    protected StringType version;

    /**
     * A free text natural language name identifying the Implementation Guide.
     */
    @Child(name = "name", type = {StringType.class}, order=2, min=1, max=1)
    @Description(shortDefinition="Informal name for this Implementation Guide", formalDefinition="A free text natural language name identifying the Implementation Guide." )
    protected StringType name;

    /**
     * The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdication for which this implementation guide was defined.
     */
    @Child(name = "useContext", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="The implementation guide is intended to support these contexts", formalDefinition="The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdication for which this implementation guide was defined." )
    protected List<CodeableConcept> useContext;

    /**
     * The name of the individual or organization that published the implementation guide.
     */
    @Child(name = "publisher", type = {StringType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the implementation guide." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<ImplementationGuideContactComponent> contact;

    /**
     * A free text natural language description of the Implementation Guide and its use.
     */
    @Child(name = "description", type = {StringType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="Natural language description of the Implementation Guide", formalDefinition="A free text natural language description of the Implementation Guide and its use." )
    protected StringType description;

    /**
     * A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.
     */
    @Child(name = "copyright", type = {StringType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Use and/or Publishing restrictions", formalDefinition="A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings." )
    protected StringType copyright;

    /**
     * A set of terms from external terminologies that may be used to assist with indexing and searching of templates.
     */
    @Child(name = "code", type = {Coding.class}, order=8, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Assist with indexing and finding", formalDefinition="A set of terms from external terminologies that may be used to assist with indexing and searching of templates." )
    protected List<Coding> code;

    /**
     * The status of the Implementation Guide.
     */
    @Child(name = "status", type = {CodeType.class}, order=9, min=1, max=1)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of the Implementation Guide." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=10, min=0, max=1)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The date that this version of the Implementation Guide was published.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=11, min=0, max=1)
    @Description(shortDefinition="Date for this version of the Implementation Guide", formalDefinition="The date that this version of the Implementation Guide was published." )
    protected DateTimeType date;

    /**
     * The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.
     */
    @Child(name = "fhirVersion", type = {IdType.class}, order=12, min=0, max=1)
    @Description(shortDefinition="FHIR Version this Implementation Guide targets", formalDefinition="The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version." )
    protected IdType fhirVersion;

    /**
     * An html page that is published with the Implementation Guide.
     */
    @Child(name = "page", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Page in the Implementation Guide", formalDefinition="An html page that is published with the Implementation Guide." )
    protected List<ImplementationGuidePageComponent> page;

    /**
     * A logical set of resources that is a section in the implementation Guide.
     */
    @Child(name = "package", type = {}, order=14, min=1, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="A section in the IG", formalDefinition="A logical set of resources that is a section in the implementation Guide." )
    protected List<ImplementationGuidePackageComponent> package_;

    /**
     * A default profile that applies to a particular resource type. Whenever a resource type is referenced in any structure definition (explicitly, or by inheritance from the base specification), and no explicit profile is applied, then this default profile applies.
     */
    @Child(name = "default", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Applies when no explicit profile applied", formalDefinition="A default profile that applies to a particular resource type. Whenever a resource type is referenced in any structure definition (explicitly, or by inheritance from the base specification), and no explicit profile is applied, then this default profile applies." )
    protected List<ImplementationGuideDefaultComponent> default_;

    private static final long serialVersionUID = 2099417617L;

  /*
   * Constructor
   */
    public ImplementationGuide() {
      super();
    }

  /*
   * Constructor
   */
    public ImplementationGuide(UriType url, StringType name, Enumeration<ConformanceResourceStatus> status) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL at which this Implementation Guide is (or will be) published, and which is used to reference this Implementation Guide in conformance statements.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URL at which this Implementation Guide is (or will be) published, and which is used to reference this Implementation Guide in conformance statements.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ImplementationGuide setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL at which this Implementation Guide is (or will be) published, and which is used to reference this Implementation Guide in conformance statements.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL at which this Implementation Guide is (or will be) published, and which is used to reference this Implementation Guide in conformance statements.
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdication for which this implementation guide was defined.)
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdication for which this implementation guide was defined.)
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
    public ImplementationGuide addCode(Coding t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<Coding>();
      this.code.add(t);
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
     * @return {@link #date} (The date that this version of the Implementation Guide was published.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date that this version of the Implementation Guide was published.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ImplementationGuide setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date that this version of the Implementation Guide was published.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date that this version of the Implementation Guide was published.
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
     * @return {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
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
     * @param value {@link #fhirVersion} (The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.). This is the underlying object with id, value and extensions. The accessor "getFhirVersion" gives direct access to the value
     */
    public ImplementationGuide setFhirVersionElement(IdType value) { 
      this.fhirVersion = value;
      return this;
    }

    /**
     * @return The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.
     */
    public String getFhirVersion() { 
      return this.fhirVersion == null ? null : this.fhirVersion.getValue();
    }

    /**
     * @param value The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.
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
     * @return {@link #page} (An html page that is published with the Implementation Guide.)
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
     * @return {@link #page} (An html page that is published with the Implementation Guide.)
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
    public ImplementationGuide addPage(ImplementationGuidePageComponent t) { //3
      if (t == null)
        return this;
      if (this.page == null)
        this.page = new ArrayList<ImplementationGuidePageComponent>();
      this.page.add(t);
      return this;
    }

    /**
     * @return {@link #package_} (A logical set of resources that is a section in the implementation Guide.)
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
     * @return {@link #package_} (A logical set of resources that is a section in the implementation Guide.)
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
     * @return {@link #default_} (A default profile that applies to a particular resource type. Whenever a resource type is referenced in any structure definition (explicitly, or by inheritance from the base specification), and no explicit profile is applied, then this default profile applies.)
     */
    public List<ImplementationGuideDefaultComponent> getDefault() { 
      if (this.default_ == null)
        this.default_ = new ArrayList<ImplementationGuideDefaultComponent>();
      return this.default_;
    }

    public boolean hasDefault() { 
      if (this.default_ == null)
        return false;
      for (ImplementationGuideDefaultComponent item : this.default_)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #default_} (A default profile that applies to a particular resource type. Whenever a resource type is referenced in any structure definition (explicitly, or by inheritance from the base specification), and no explicit profile is applied, then this default profile applies.)
     */
    // syntactic sugar
    public ImplementationGuideDefaultComponent addDefault() { //3
      ImplementationGuideDefaultComponent t = new ImplementationGuideDefaultComponent();
      if (this.default_ == null)
        this.default_ = new ArrayList<ImplementationGuideDefaultComponent>();
      this.default_.add(t);
      return t;
    }

    // syntactic sugar
    public ImplementationGuide addDefault(ImplementationGuideDefaultComponent t) { //3
      if (t == null)
        return this;
      if (this.default_ == null)
        this.default_ = new ArrayList<ImplementationGuideDefaultComponent>();
      this.default_.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL at which this Implementation Guide is (or will be) published, and which is used to reference this Implementation Guide in conformance statements.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the Implementation Guide when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the Implementation Guide author manually.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the Implementation Guide.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("useContext", "CodeableConcept", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of implementation guides. The most common use of this element is to represent the country / jurisdication for which this implementation guide was defined.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the implementation guide.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "string", "A free text natural language description of the Implementation Guide and its use.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the implementation guide and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the details of the constraints and mappings.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("code", "Coding", "A set of terms from external terminologies that may be used to assist with indexing and searching of templates.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("status", "code", "The status of the Implementation Guide.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "This Implementation Guide was authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date that this version of the Implementation Guide was published.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("fhirVersion", "id", "The version of the FHIR specification on which this ImplementationGuide is based - this is the formal version of the specification, without the revision number, e.g. [publication].[major].[minor], which is 0.5.0 for this version.", 0, java.lang.Integer.MAX_VALUE, fhirVersion));
        childrenList.add(new Property("page", "", "An html page that is published with the Implementation Guide.", 0, java.lang.Integer.MAX_VALUE, page));
        childrenList.add(new Property("package", "", "A logical set of resources that is a section in the implementation Guide.", 0, java.lang.Integer.MAX_VALUE, package_));
        childrenList.add(new Property("default", "", "A default profile that applies to a particular resource type. Whenever a resource type is referenced in any structure definition (explicitly, or by inheritance from the base specification), and no explicit profile is applied, then this default profile applies.", 0, java.lang.Integer.MAX_VALUE, default_));
      }

      public ImplementationGuide copy() {
        ImplementationGuide dst = new ImplementationGuide();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : useContext)
            dst.useContext.add(i.copy());
        };
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ImplementationGuideContactComponent>();
          for (ImplementationGuideContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.fhirVersion = fhirVersion == null ? null : fhirVersion.copy();
        if (page != null) {
          dst.page = new ArrayList<ImplementationGuidePageComponent>();
          for (ImplementationGuidePageComponent i : page)
            dst.page.add(i.copy());
        };
        if (package_ != null) {
          dst.package_ = new ArrayList<ImplementationGuidePackageComponent>();
          for (ImplementationGuidePackageComponent i : package_)
            dst.package_.add(i.copy());
        };
        if (default_ != null) {
          dst.default_ = new ArrayList<ImplementationGuideDefaultComponent>();
          for (ImplementationGuideDefaultComponent i : default_)
            dst.default_.add(i.copy());
        };
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
           && compareDeep(useContext, o.useContext, true) && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true)
           && compareDeep(description, o.description, true) && compareDeep(copyright, o.copyright, true) && compareDeep(code, o.code, true)
           && compareDeep(status, o.status, true) && compareDeep(experimental, o.experimental, true) && compareDeep(date, o.date, true)
           && compareDeep(fhirVersion, o.fhirVersion, true) && compareDeep(page, o.page, true) && compareDeep(package_, o.package_, true)
           && compareDeep(default_, o.default_, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ImplementationGuide))
          return false;
        ImplementationGuide o = (ImplementationGuide) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(publisher, o.publisher, true) && compareValues(description, o.description, true) && compareValues(copyright, o.copyright, true)
           && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true) && compareValues(date, o.date, true)
           && compareValues(fhirVersion, o.fhirVersion, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (version == null || version.isEmpty())
           && (name == null || name.isEmpty()) && (useContext == null || useContext.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (contact == null || contact.isEmpty()) && (description == null || description.isEmpty())
           && (copyright == null || copyright.isEmpty()) && (code == null || code.isEmpty()) && (status == null || status.isEmpty())
           && (experimental == null || experimental.isEmpty()) && (date == null || date.isEmpty()) && (fhirVersion == null || fhirVersion.isEmpty())
           && (page == null || page.isEmpty()) && (package_ == null || package_.isEmpty()) && (default_ == null || default_.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImplementationGuide;
   }

  @SearchParamDefinition(name="date", path="ImplementationGuide.date", description="The implementation guide publication date", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="code", path="ImplementationGuide.code", description="A code for the implementation guide", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="name", path="ImplementationGuide.name", description="Name of the implementation guide", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="context", path="ImplementationGuide.useContext", description="A use context assigned to the structure", type="token" )
  public static final String SP_CONTEXT = "context";
  @SearchParamDefinition(name="publisher", path="ImplementationGuide.publisher", description="Name of the publisher of the implementation guide", type="string" )
  public static final String SP_PUBLISHER = "publisher";
  @SearchParamDefinition(name="description", path="ImplementationGuide.description", description="Text search in the description of the implementation guide", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="experimental", path="ImplementationGuide.experimental", description="If for testing purposes, not real usage", type="token" )
  public static final String SP_EXPERIMENTAL = "experimental";
  @SearchParamDefinition(name="version", path="ImplementationGuide.version", description="The version identifier of the implementation guide", type="token" )
  public static final String SP_VERSION = "version";
  @SearchParamDefinition(name="url", path="ImplementationGuide.url", description="Literal URL used to reference this Implementation Guide", type="uri" )
  public static final String SP_URL = "url";
  @SearchParamDefinition(name="status", path="ImplementationGuide.status", description="The current status of the implementation guide", type="token" )
  public static final String SP_STATUS = "status";

}

