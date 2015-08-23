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
 * A Search Parameter that defines a named search item that can be used to search/filter on a resource.
 */
@ResourceDef(name="SearchParameter", profile="http://hl7.org/fhir/Profile/SearchParameter")
public class SearchParameter extends DomainResource {

    public enum XPathUsageType {
        /**
         * The search parameter is derived directly from the selected nodes based on the type definitions
         */
        NORMAL, 
        /**
         * The search parameter is derived by a phonetic transform from the selected nodes
         */
        PHONETIC, 
        /**
         * The search parameter is based on a spatial transform of the selected nodes
         */
        NEARBY, 
        /**
         * The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle
         */
        DISTANCE, 
        /**
         * The interpretation of the xpath statement is unknown (and can't be automated)
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static XPathUsageType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return NORMAL;
        if ("phonetic".equals(codeString))
          return PHONETIC;
        if ("nearby".equals(codeString))
          return NEARBY;
        if ("distance".equals(codeString))
          return DISTANCE;
        if ("other".equals(codeString))
          return OTHER;
        throw new Exception("Unknown XPathUsageType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NORMAL: return "normal";
            case PHONETIC: return "phonetic";
            case NEARBY: return "nearby";
            case DISTANCE: return "distance";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NORMAL: return "http://hl7.org/fhir/search-xpath-usage";
            case PHONETIC: return "http://hl7.org/fhir/search-xpath-usage";
            case NEARBY: return "http://hl7.org/fhir/search-xpath-usage";
            case DISTANCE: return "http://hl7.org/fhir/search-xpath-usage";
            case OTHER: return "http://hl7.org/fhir/search-xpath-usage";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NORMAL: return "The search parameter is derived directly from the selected nodes based on the type definitions";
            case PHONETIC: return "The search parameter is derived by a phonetic transform from the selected nodes";
            case NEARBY: return "The search parameter is based on a spatial transform of the selected nodes";
            case DISTANCE: return "The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle";
            case OTHER: return "The interpretation of the xpath statement is unknown (and can't be automated)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NORMAL: return "Normal";
            case PHONETIC: return "Phonetic";
            case NEARBY: return "Nearby";
            case DISTANCE: return "Distance";
            case OTHER: return "Other";
            default: return "?";
          }
        }
    }

  public static class XPathUsageTypeEnumFactory implements EnumFactory<XPathUsageType> {
    public XPathUsageType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("normal".equals(codeString))
          return XPathUsageType.NORMAL;
        if ("phonetic".equals(codeString))
          return XPathUsageType.PHONETIC;
        if ("nearby".equals(codeString))
          return XPathUsageType.NEARBY;
        if ("distance".equals(codeString))
          return XPathUsageType.DISTANCE;
        if ("other".equals(codeString))
          return XPathUsageType.OTHER;
        throw new IllegalArgumentException("Unknown XPathUsageType code '"+codeString+"'");
        }
    public String toCode(XPathUsageType code) {
      if (code == XPathUsageType.NORMAL)
        return "normal";
      if (code == XPathUsageType.PHONETIC)
        return "phonetic";
      if (code == XPathUsageType.NEARBY)
        return "nearby";
      if (code == XPathUsageType.DISTANCE)
        return "distance";
      if (code == XPathUsageType.OTHER)
        return "other";
      return "?";
      }
    }

    @Block()
    public static class SearchParameterContactComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of an individual to contact regarding the search parameter.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of a individual to contact", formalDefinition="The name of an individual to contact regarding the search parameter." )
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
      public SearchParameterContactComponent() {
        super();
      }

        /**
         * @return {@link #name} (The name of an individual to contact regarding the search parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SearchParameterContactComponent.name");
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
         * @param value {@link #name} (The name of an individual to contact regarding the search parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SearchParameterContactComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of an individual to contact regarding the search parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of an individual to contact regarding the search parameter.
         */
        public SearchParameterContactComponent setName(String value) { 
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
        public SearchParameterContactComponent addTelecom(ContactPoint t) { //3
          if (t == null)
            return this;
          if (this.telecom == null)
            this.telecom = new ArrayList<ContactPoint>();
          this.telecom.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name of an individual to contact regarding the search parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("telecom", "ContactPoint", "Contact details for individual (if a name was provided) or the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        }

      public SearchParameterContactComponent copy() {
        SearchParameterContactComponent dst = new SearchParameterContactComponent();
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
        if (!(other instanceof SearchParameterContactComponent))
          return false;
        SearchParameterContactComponent o = (SearchParameterContactComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(telecom, o.telecom, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SearchParameterContactComponent))
          return false;
        SearchParameterContactComponent o = (SearchParameterContactComponent) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (telecom == null || telecom.isEmpty())
          ;
      }

  }

    /**
     * An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Literal URL used to reference this search parameter", formalDefinition="An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published." )
    protected UriType url;

    /**
     * A free text natural language name identifying the search parameter.
     */
    @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Informal name for this search parameter", formalDefinition="A free text natural language name identifying the search parameter." )
    protected StringType name;

    /**
     * The status of this search parameter definition.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired", formalDefinition="The status of this search parameter definition." )
    protected Enumeration<ConformanceResourceStatus> status;

    /**
     * A flag to indicate that this search parameter definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="A flag to indicate that this search parameter definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage." )
    protected BooleanType experimental;

    /**
     * The name of the individual or organization that published the search parameter.
     */
    @Child(name = "publisher", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the search parameter." )
    protected StringType publisher;

    /**
     * Contacts to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contacts to assist a user in finding and communicating with the publisher." )
    protected List<SearchParameterContactComponent> contact;

    /**
     * The date  (and optionally time) when the search parameter definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the search parameter changes.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Publication Date(/time)", formalDefinition="The date  (and optionally time) when the search parameter definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the search parameter changes." )
    protected DateTimeType date;

    /**
     * The Scope and Usage that this search parameter was created to meet.
     */
    @Child(name = "requirements", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this search parameter is defined", formalDefinition="The Scope and Usage that this search parameter was created to meet." )
    protected StringType requirements;

    /**
     * The code used in the URL or the parameter name in a parameters resource for this search parameter.
     */
    @Child(name = "code", type = {CodeType.class}, order=8, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Code used in URL", formalDefinition="The code used in the URL or the parameter name in a parameters resource for this search parameter." )
    protected CodeType code;

    /**
     * The base resource type that this search parameter refers to.
     */
    @Child(name = "base", type = {CodeType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The resource type this search parameter applies to", formalDefinition="The base resource type that this search parameter refers to." )
    protected CodeType base;

    /**
     * The type of value a search parameter refers to, and how the content is interpreted.
     */
    @Child(name = "type", type = {CodeType.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="number | date | string | token | reference | composite | quantity | uri", formalDefinition="The type of value a search parameter refers to, and how the content is interpreted." )
    protected Enumeration<SearchParamType> type;

    /**
     * A description of the search parameters and how it used.
     */
    @Child(name = "description", type = {StringType.class}, order=11, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Documentation for  search parameter", formalDefinition="A description of the search parameters and how it used." )
    protected StringType description;

    /**
     * An XPath expression that returns a set of elements for the search parameter.
     */
    @Child(name = "xpath", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="XPath that extracts the values", formalDefinition="An XPath expression that returns a set of elements for the search parameter." )
    protected StringType xpath;

    /**
     * How the search parameter relates to the set of elements returned by evaluating the xpath query.
     */
    @Child(name = "xpathUsage", type = {CodeType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="normal | phonetic | nearby | distance | other", formalDefinition="How the search parameter relates to the set of elements returned by evaluating the xpath query." )
    protected Enumeration<XPathUsageType> xpathUsage;

    /**
     * Types of resource (if a resource is referenced).
     */
    @Child(name = "target", type = {CodeType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Types of resource (if a resource reference)", formalDefinition="Types of resource (if a resource is referenced)." )
    protected List<CodeType> target;

    private static final long serialVersionUID = -742596414L;

  /*
   * Constructor
   */
    public SearchParameter() {
      super();
    }

  /*
   * Constructor
   */
    public SearchParameter(UriType url, StringType name, CodeType code, CodeType base, Enumeration<SearchParamType> type, StringType description) {
      super();
      this.url = url;
      this.name = name;
      this.code = code;
      this.base = base;
      this.type = type;
      this.description = description;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.url");
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
     * @param value {@link #url} (An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public SearchParameter setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published.
     */
    public SearchParameter setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (A free text natural language name identifying the search parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.name");
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
     * @param value {@link #name} (A free text natural language name identifying the search parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public SearchParameter setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A free text natural language name identifying the search parameter.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A free text natural language name identifying the search parameter.
     */
    public SearchParameter setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this search parameter definition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConformanceResourceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.status");
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
     * @param value {@link #status} (The status of this search parameter definition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public SearchParameter setStatusElement(Enumeration<ConformanceResourceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this search parameter definition.
     */
    public ConformanceResourceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this search parameter definition.
     */
    public SearchParameter setStatus(ConformanceResourceStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ConformanceResourceStatus>(new ConformanceResourceStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #experimental} (A flag to indicate that this search parameter definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.experimental");
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
     * @param value {@link #experimental} (A flag to indicate that this search parameter definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public SearchParameter setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A flag to indicate that this search parameter definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A flag to indicate that this search parameter definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public SearchParameter setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the search parameter.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the search parameter.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public SearchParameter setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the search parameter.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the search parameter.
     */
    public SearchParameter setPublisher(String value) { 
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
    public List<SearchParameterContactComponent> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<SearchParameterContactComponent>();
      return this.contact;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (SearchParameterContactComponent item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contact} (Contacts to assist a user in finding and communicating with the publisher.)
     */
    // syntactic sugar
    public SearchParameterContactComponent addContact() { //3
      SearchParameterContactComponent t = new SearchParameterContactComponent();
      if (this.contact == null)
        this.contact = new ArrayList<SearchParameterContactComponent>();
      this.contact.add(t);
      return t;
    }

    // syntactic sugar
    public SearchParameter addContact(SearchParameterContactComponent t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<SearchParameterContactComponent>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the search parameter definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the search parameter changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the search parameter definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the search parameter changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public SearchParameter setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the search parameter definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the search parameter changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the search parameter definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the search parameter changes.
     */
    public SearchParameter setDate(Date value) { 
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
     * @return {@link #requirements} (The Scope and Usage that this search parameter was created to meet.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.requirements");
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
     * @param value {@link #requirements} (The Scope and Usage that this search parameter was created to meet.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public SearchParameter setRequirementsElement(StringType value) { 
      this.requirements = value;
      return this;
    }

    /**
     * @return The Scope and Usage that this search parameter was created to meet.
     */
    public String getRequirements() { 
      return this.requirements == null ? null : this.requirements.getValue();
    }

    /**
     * @param value The Scope and Usage that this search parameter was created to meet.
     */
    public SearchParameter setRequirements(String value) { 
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
     * @return {@link #code} (The code used in the URL or the parameter name in a parameters resource for this search parameter.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public CodeType getCodeElement() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.code");
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
     * @param value {@link #code} (The code used in the URL or the parameter name in a parameters resource for this search parameter.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public SearchParameter setCodeElement(CodeType value) { 
      this.code = value;
      return this;
    }

    /**
     * @return The code used in the URL or the parameter name in a parameters resource for this search parameter.
     */
    public String getCode() { 
      return this.code == null ? null : this.code.getValue();
    }

    /**
     * @param value The code used in the URL or the parameter name in a parameters resource for this search parameter.
     */
    public SearchParameter setCode(String value) { 
        if (this.code == null)
          this.code = new CodeType();
        this.code.setValue(value);
      return this;
    }

    /**
     * @return {@link #base} (The base resource type that this search parameter refers to.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public CodeType getBaseElement() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.base");
        else if (Configuration.doAutoCreate())
          this.base = new CodeType(); // bb
      return this.base;
    }

    public boolean hasBaseElement() { 
      return this.base != null && !this.base.isEmpty();
    }

    public boolean hasBase() { 
      return this.base != null && !this.base.isEmpty();
    }

    /**
     * @param value {@link #base} (The base resource type that this search parameter refers to.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public SearchParameter setBaseElement(CodeType value) { 
      this.base = value;
      return this;
    }

    /**
     * @return The base resource type that this search parameter refers to.
     */
    public String getBase() { 
      return this.base == null ? null : this.base.getValue();
    }

    /**
     * @param value The base resource type that this search parameter refers to.
     */
    public SearchParameter setBase(String value) { 
        if (this.base == null)
          this.base = new CodeType();
        this.base.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (The type of value a search parameter refers to, and how the content is interpreted.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<SearchParamType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<SearchParamType>(new SearchParamTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of value a search parameter refers to, and how the content is interpreted.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public SearchParameter setTypeElement(Enumeration<SearchParamType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of value a search parameter refers to, and how the content is interpreted.
     */
    public SearchParamType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of value a search parameter refers to, and how the content is interpreted.
     */
    public SearchParameter setType(SearchParamType value) { 
        if (this.type == null)
          this.type = new Enumeration<SearchParamType>(new SearchParamTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #description} (A description of the search parameters and how it used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.description");
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
     * @param value {@link #description} (A description of the search parameters and how it used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public SearchParameter setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A description of the search parameters and how it used.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A description of the search parameters and how it used.
     */
    public SearchParameter setDescription(String value) { 
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      return this;
    }

    /**
     * @return {@link #xpath} (An XPath expression that returns a set of elements for the search parameter.). This is the underlying object with id, value and extensions. The accessor "getXpath" gives direct access to the value
     */
    public StringType getXpathElement() { 
      if (this.xpath == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.xpath");
        else if (Configuration.doAutoCreate())
          this.xpath = new StringType(); // bb
      return this.xpath;
    }

    public boolean hasXpathElement() { 
      return this.xpath != null && !this.xpath.isEmpty();
    }

    public boolean hasXpath() { 
      return this.xpath != null && !this.xpath.isEmpty();
    }

    /**
     * @param value {@link #xpath} (An XPath expression that returns a set of elements for the search parameter.). This is the underlying object with id, value and extensions. The accessor "getXpath" gives direct access to the value
     */
    public SearchParameter setXpathElement(StringType value) { 
      this.xpath = value;
      return this;
    }

    /**
     * @return An XPath expression that returns a set of elements for the search parameter.
     */
    public String getXpath() { 
      return this.xpath == null ? null : this.xpath.getValue();
    }

    /**
     * @param value An XPath expression that returns a set of elements for the search parameter.
     */
    public SearchParameter setXpath(String value) { 
      if (Utilities.noString(value))
        this.xpath = null;
      else {
        if (this.xpath == null)
          this.xpath = new StringType();
        this.xpath.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #xpathUsage} (How the search parameter relates to the set of elements returned by evaluating the xpath query.). This is the underlying object with id, value and extensions. The accessor "getXpathUsage" gives direct access to the value
     */
    public Enumeration<XPathUsageType> getXpathUsageElement() { 
      if (this.xpathUsage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.xpathUsage");
        else if (Configuration.doAutoCreate())
          this.xpathUsage = new Enumeration<XPathUsageType>(new XPathUsageTypeEnumFactory()); // bb
      return this.xpathUsage;
    }

    public boolean hasXpathUsageElement() { 
      return this.xpathUsage != null && !this.xpathUsage.isEmpty();
    }

    public boolean hasXpathUsage() { 
      return this.xpathUsage != null && !this.xpathUsage.isEmpty();
    }

    /**
     * @param value {@link #xpathUsage} (How the search parameter relates to the set of elements returned by evaluating the xpath query.). This is the underlying object with id, value and extensions. The accessor "getXpathUsage" gives direct access to the value
     */
    public SearchParameter setXpathUsageElement(Enumeration<XPathUsageType> value) { 
      this.xpathUsage = value;
      return this;
    }

    /**
     * @return How the search parameter relates to the set of elements returned by evaluating the xpath query.
     */
    public XPathUsageType getXpathUsage() { 
      return this.xpathUsage == null ? null : this.xpathUsage.getValue();
    }

    /**
     * @param value How the search parameter relates to the set of elements returned by evaluating the xpath query.
     */
    public SearchParameter setXpathUsage(XPathUsageType value) { 
      if (value == null)
        this.xpathUsage = null;
      else {
        if (this.xpathUsage == null)
          this.xpathUsage = new Enumeration<XPathUsageType>(new XPathUsageTypeEnumFactory());
        this.xpathUsage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #target} (Types of resource (if a resource is referenced).)
     */
    public List<CodeType> getTarget() { 
      if (this.target == null)
        this.target = new ArrayList<CodeType>();
      return this.target;
    }

    public boolean hasTarget() { 
      if (this.target == null)
        return false;
      for (CodeType item : this.target)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #target} (Types of resource (if a resource is referenced).)
     */
    // syntactic sugar
    public CodeType addTargetElement() {//2 
      CodeType t = new CodeType();
      if (this.target == null)
        this.target = new ArrayList<CodeType>();
      this.target.add(t);
      return t;
    }

    /**
     * @param value {@link #target} (Types of resource (if a resource is referenced).)
     */
    public SearchParameter addTarget(String value) { //1
      CodeType t = new CodeType();
      t.setValue(value);
      if (this.target == null)
        this.target = new ArrayList<CodeType>();
      this.target.add(t);
      return this;
    }

    /**
     * @param value {@link #target} (Types of resource (if a resource is referenced).)
     */
    public boolean hasTarget(String value) { 
      if (this.target == null)
        return false;
      for (CodeType v : this.target)
        if (v.equals(value)) // code
          return true;
      return false;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("name", "string", "A free text natural language name identifying the search parameter.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of this search parameter definition.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A flag to indicate that this search parameter definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the search parameter.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "", "Contacts to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the search parameter definition was published. The date must change when the business version changes, if it does, and it must change if the status code changes. in addition, it should change when the substantiative content of the search parameter changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("requirements", "string", "The Scope and Usage that this search parameter was created to meet.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("code", "code", "The code used in the URL or the parameter name in a parameters resource for this search parameter.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("base", "code", "The base resource type that this search parameter refers to.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("type", "code", "The type of value a search parameter refers to, and how the content is interpreted.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("description", "string", "A description of the search parameters and how it used.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("xpath", "string", "An XPath expression that returns a set of elements for the search parameter.", 0, java.lang.Integer.MAX_VALUE, xpath));
        childrenList.add(new Property("xpathUsage", "code", "How the search parameter relates to the set of elements returned by evaluating the xpath query.", 0, java.lang.Integer.MAX_VALUE, xpathUsage));
        childrenList.add(new Property("target", "code", "Types of resource (if a resource is referenced).", 0, java.lang.Integer.MAX_VALUE, target));
      }

      public SearchParameter copy() {
        SearchParameter dst = new SearchParameter();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<SearchParameterContactComponent>();
          for (SearchParameterContactComponent i : contact)
            dst.contact.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.code = code == null ? null : code.copy();
        dst.base = base == null ? null : base.copy();
        dst.type = type == null ? null : type.copy();
        dst.description = description == null ? null : description.copy();
        dst.xpath = xpath == null ? null : xpath.copy();
        dst.xpathUsage = xpathUsage == null ? null : xpathUsage.copy();
        if (target != null) {
          dst.target = new ArrayList<CodeType>();
          for (CodeType i : target)
            dst.target.add(i.copy());
        };
        return dst;
      }

      protected SearchParameter typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SearchParameter))
          return false;
        SearchParameter o = (SearchParameter) other;
        return compareDeep(url, o.url, true) && compareDeep(name, o.name, true) && compareDeep(status, o.status, true)
           && compareDeep(experimental, o.experimental, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(contact, o.contact, true) && compareDeep(date, o.date, true) && compareDeep(requirements, o.requirements, true)
           && compareDeep(code, o.code, true) && compareDeep(base, o.base, true) && compareDeep(type, o.type, true)
           && compareDeep(description, o.description, true) && compareDeep(xpath, o.xpath, true) && compareDeep(xpathUsage, o.xpathUsage, true)
           && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SearchParameter))
          return false;
        SearchParameter o = (SearchParameter) other;
        return compareValues(url, o.url, true) && compareValues(name, o.name, true) && compareValues(status, o.status, true)
           && compareValues(experimental, o.experimental, true) && compareValues(publisher, o.publisher, true)
           && compareValues(date, o.date, true) && compareValues(requirements, o.requirements, true) && compareValues(code, o.code, true)
           && compareValues(base, o.base, true) && compareValues(type, o.type, true) && compareValues(description, o.description, true)
           && compareValues(xpath, o.xpath, true) && compareValues(xpathUsage, o.xpathUsage, true) && compareValues(target, o.target, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (name == null || name.isEmpty())
           && (status == null || status.isEmpty()) && (experimental == null || experimental.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (contact == null || contact.isEmpty()) && (date == null || date.isEmpty())
           && (requirements == null || requirements.isEmpty()) && (code == null || code.isEmpty()) && (base == null || base.isEmpty())
           && (type == null || type.isEmpty()) && (description == null || description.isEmpty()) && (xpath == null || xpath.isEmpty())
           && (xpathUsage == null || xpathUsage.isEmpty()) && (target == null || target.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SearchParameter;
   }

  @SearchParamDefinition(name="code", path="SearchParameter.code", description="Code used in URL", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="name", path="SearchParameter.name", description="Informal name for this search parameter", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="description", path="SearchParameter.description", description="Documentation for  search parameter", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="type", path="SearchParameter.type", description="number | date | string | token | reference | composite | quantity | uri", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="url", path="SearchParameter.url", description="Literal URL used to reference this search parameter", type="uri" )
  public static final String SP_URL = "url";
  @SearchParamDefinition(name="base", path="SearchParameter.base", description="The resource type this search parameter applies to", type="token" )
  public static final String SP_BASE = "base";
  @SearchParamDefinition(name="target", path="SearchParameter.target", description="Types of resource (if a resource reference)", type="token" )
  public static final String SP_TARGET = "target";

}

