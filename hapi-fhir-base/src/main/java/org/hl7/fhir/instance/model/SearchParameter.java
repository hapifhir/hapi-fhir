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
 * A Search Parameter that defines a named search item that can be used to search/filter on a resource.
 */
@ResourceDef(name="SearchParameter", profile="http://hl7.org/fhir/Profile/SearchParameter")
public class SearchParameter extends DomainResource {

    public enum SearchParamType {
        /**
         * Search parameter SHALL be a number (a whole number, or a decimal).
         */
        NUMBER, 
        /**
         * Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.
         */
        DATE, 
        /**
         * Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.
         */
        STRING, 
        /**
         * Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a "|", depending on the modifier used.
         */
        TOKEN, 
        /**
         * A reference to another resource.
         */
        REFERENCE, 
        /**
         * A composite search parameter that combines a search on two values together.
         */
        COMPOSITE, 
        /**
         * A search parameter that searches on a quantity.
         */
        QUANTITY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SearchParamType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("number".equals(codeString))
          return NUMBER;
        if ("date".equals(codeString))
          return DATE;
        if ("string".equals(codeString))
          return STRING;
        if ("token".equals(codeString))
          return TOKEN;
        if ("reference".equals(codeString))
          return REFERENCE;
        if ("composite".equals(codeString))
          return COMPOSITE;
        if ("quantity".equals(codeString))
          return QUANTITY;
        throw new Exception("Unknown SearchParamType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NUMBER: return "number";
            case DATE: return "date";
            case STRING: return "string";
            case TOKEN: return "token";
            case REFERENCE: return "reference";
            case COMPOSITE: return "composite";
            case QUANTITY: return "quantity";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case NUMBER: return "";
            case DATE: return "";
            case STRING: return "";
            case TOKEN: return "";
            case REFERENCE: return "";
            case COMPOSITE: return "";
            case QUANTITY: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case NUMBER: return "Search parameter SHALL be a number (a whole number, or a decimal).";
            case DATE: return "Search parameter is on a date/time. The date format is the standard XML format, though other formats may be supported.";
            case STRING: return "Search parameter is a simple string, like a name part. Search is case-insensitive and accent-insensitive. May match just the start of a string. String parameters may contain spaces.";
            case TOKEN: return "Search parameter on a coded element or identifier. May be used to search through the text, displayname, code and code/codesystem (for codes) and label, system and key (for identifier). Its value is either a string or a pair of namespace and value, separated by a '|', depending on the modifier used.";
            case REFERENCE: return "A reference to another resource.";
            case COMPOSITE: return "A composite search parameter that combines a search on two values together.";
            case QUANTITY: return "A search parameter that searches on a quantity.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NUMBER: return "number";
            case DATE: return "date";
            case STRING: return "string";
            case TOKEN: return "token";
            case REFERENCE: return "reference";
            case COMPOSITE: return "composite";
            case QUANTITY: return "quantity";
            default: return "?";
          }
        }
    }

  public static class SearchParamTypeEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("number".equals(codeString))
          return SearchParamType.NUMBER;
        if ("date".equals(codeString))
          return SearchParamType.DATE;
        if ("string".equals(codeString))
          return SearchParamType.STRING;
        if ("token".equals(codeString))
          return SearchParamType.TOKEN;
        if ("reference".equals(codeString))
          return SearchParamType.REFERENCE;
        if ("composite".equals(codeString))
          return SearchParamType.COMPOSITE;
        if ("quantity".equals(codeString))
          return SearchParamType.QUANTITY;
        throw new Exception("Unknown SearchParamType code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == SearchParamType.NUMBER)
        return "number";
      if (code == SearchParamType.DATE)
        return "date";
      if (code == SearchParamType.STRING)
        return "string";
      if (code == SearchParamType.TOKEN)
        return "token";
      if (code == SearchParamType.REFERENCE)
        return "reference";
      if (code == SearchParamType.COMPOSITE)
        return "composite";
      if (code == SearchParamType.QUANTITY)
        return "quantity";
      return "?";
      }
    }

    /**
     * The URL at which this search parameter is (or will be) published, and which is used to reference this profile in conformance statements.
     */
    @Child(name="url", type={UriType.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="Literal URL used to reference this search parameter", formalDefinition="The URL at which this search parameter is (or will be) published, and which is used to reference this profile in conformance statements." )
    protected UriType url;

    /**
     * The name of the standard or custom search parameter.
     */
    @Child(name="name", type={StringType.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Name of search parameter", formalDefinition="The name of the standard or custom search parameter." )
    protected StringType name;

    /**
     * Details of the individual or organization who accepts responsibility for publishing the search parameter.
     */
    @Child(name="publisher", type={StringType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="Details of the individual or organization who accepts responsibility for publishing the search parameter." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name="telecom", type={ContactPoint.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contact information of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactPoint> telecom;

    /**
     * The Scope and Usage that this search parameter was created to meet.
     */
    @Child(name="requirements", type={StringType.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Why this search parameter is defined", formalDefinition="The Scope and Usage that this search parameter was created to meet." )
    protected StringType requirements;

    /**
     * The base resource type that this search parameter refers to.
     */
    @Child(name="base", type={CodeType.class}, order=4, min=1, max=1)
    @Description(shortDefinition="The resource type this search parameter applies to", formalDefinition="The base resource type that this search parameter refers to." )
    protected CodeType base;

    /**
     * The type of value a search parameter refers to, and how the content is interpreted.
     */
    @Child(name="type", type={CodeType.class}, order=5, min=1, max=1)
    @Description(shortDefinition="number | date | string | token | reference | composite | quantity", formalDefinition="The type of value a search parameter refers to, and how the content is interpreted." )
    protected Enumeration<SearchParamType> type;

    /**
     * A description of the search parameters and how it used.
     */
    @Child(name="description", type={StringType.class}, order=6, min=1, max=1)
    @Description(shortDefinition="Documentation for  search parameter", formalDefinition="A description of the search parameters and how it used." )
    protected StringType description;

    /**
     * An XPath expression that returns a set of elements for the search parameter.
     */
    @Child(name="xpath", type={StringType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="XPath that extracts the values", formalDefinition="An XPath expression that returns a set of elements for the search parameter." )
    protected StringType xpath;

    /**
     * Types of resource (if a resource is referenced).
     */
    @Child(name="target", type={CodeType.class}, order=8, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Types of resource (if a resource reference)", formalDefinition="Types of resource (if a resource is referenced)." )
    protected List<CodeType> target;

    private static final long serialVersionUID = -720598887L;

    public SearchParameter() {
      super();
    }

    public SearchParameter(UriType url, StringType name, CodeType base, Enumeration<SearchParamType> type, StringType description) {
      super();
      this.url = url;
      this.name = name;
      this.base = base;
      this.type = type;
      this.description = description;
    }

    /**
     * @return {@link #url} (The URL at which this search parameter is (or will be) published, and which is used to reference this profile in conformance statements.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType();
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (The URL at which this search parameter is (or will be) published, and which is used to reference this profile in conformance statements.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public SearchParameter setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return The URL at which this search parameter is (or will be) published, and which is used to reference this profile in conformance statements.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value The URL at which this search parameter is (or will be) published, and which is used to reference this profile in conformance statements.
     */
    public SearchParameter setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (The name of the standard or custom search parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.name");
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
     * @param value {@link #name} (The name of the standard or custom search parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public SearchParameter setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The name of the standard or custom search parameter.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The name of the standard or custom search parameter.
     */
    public SearchParameter setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the search parameter.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType();
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (Details of the individual or organization who accepts responsibility for publishing the search parameter.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public SearchParameter setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return Details of the individual or organization who accepts responsibility for publishing the search parameter.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value Details of the individual or organization who accepts responsibility for publishing the search parameter.
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
     * @return {@link #requirements} (The Scope and Usage that this search parameter was created to meet.). This is the underlying object with id, value and extensions. The accessor "getRequirements" gives direct access to the value
     */
    public StringType getRequirementsElement() { 
      if (this.requirements == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.requirements");
        else if (Configuration.doAutoCreate())
          this.requirements = new StringType();
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
     * @return {@link #base} (The base resource type that this search parameter refers to.). This is the underlying object with id, value and extensions. The accessor "getBase" gives direct access to the value
     */
    public CodeType getBaseElement() { 
      if (this.base == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.base");
        else if (Configuration.doAutoCreate())
          this.base = new CodeType();
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
          this.type = new Enumeration<SearchParamType>();
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
          this.type = new Enumeration<SearchParamType>();
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
          this.xpath = new StringType();
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
        childrenList.add(new Property("url", "uri", "The URL at which this search parameter is (or will be) published, and which is used to reference this profile in conformance statements.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("name", "string", "The name of the standard or custom search parameter.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("publisher", "string", "Details of the individual or organization who accepts responsibility for publishing the search parameter.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("requirements", "string", "The Scope and Usage that this search parameter was created to meet.", 0, java.lang.Integer.MAX_VALUE, requirements));
        childrenList.add(new Property("base", "code", "The base resource type that this search parameter refers to.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("type", "code", "The type of value a search parameter refers to, and how the content is interpreted.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("description", "string", "A description of the search parameters and how it used.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("xpath", "string", "An XPath expression that returns a set of elements for the search parameter.", 0, java.lang.Integer.MAX_VALUE, xpath));
        childrenList.add(new Property("target", "code", "Types of resource (if a resource is referenced).", 0, java.lang.Integer.MAX_VALUE, target));
      }

      public SearchParameter copy() {
        SearchParameter dst = new SearchParameter();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.name = name == null ? null : name.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        dst.requirements = requirements == null ? null : requirements.copy();
        dst.base = base == null ? null : base.copy();
        dst.type = type == null ? null : type.copy();
        dst.description = description == null ? null : description.copy();
        dst.xpath = xpath == null ? null : xpath.copy();
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

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (name == null || name.isEmpty())
           && (publisher == null || publisher.isEmpty()) && (telecom == null || telecom.isEmpty()) && (requirements == null || requirements.isEmpty())
           && (base == null || base.isEmpty()) && (type == null || type.isEmpty()) && (description == null || description.isEmpty())
           && (xpath == null || xpath.isEmpty()) && (target == null || target.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SearchParameter;
   }

  @SearchParamDefinition(name="description", path="SearchParameter.description", description="Documentation for  search parameter", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="name", path="SearchParameter.name", description="Name of search parameter", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="target", path="SearchParameter.target", description="Types of resource (if a resource reference)", type="token" )
  public static final String SP_TARGET = "target";
  @SearchParamDefinition(name="base", path="SearchParameter.base", description="The resource type this search parameter applies to", type="token" )
  public static final String SP_BASE = "base";
  @SearchParamDefinition(name="type", path="SearchParameter.type", description="number | date | string | token | reference | composite | quantity", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="url", path="SearchParameter.url", description="Literal URL used to reference this search parameter", type="token" )
  public static final String SP_URL = "url";

}

