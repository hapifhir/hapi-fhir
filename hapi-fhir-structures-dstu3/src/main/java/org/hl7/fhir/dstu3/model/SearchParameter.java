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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A search parameter that defines a named search item that can be used to search/filter on a resource.
 */
@ResourceDef(name="SearchParameter", profile="http://hl7.org/fhir/Profile/SearchParameter")
@ChildOrder(names={"url", "version", "name", "status", "experimental", "date", "publisher", "contact", "useContext", "jurisdiction", "purpose", "code", "base", "type", "description", "expression", "xpath", "xpathUsage", "target", "component"})
public class SearchParameter extends MetadataResource {

    public enum XPathUsageType {
        /**
         * The search parameter is derived directly from the selected nodes based on the type definitions.
         */
        NORMAL, 
        /**
         * The search parameter is derived by a phonetic transform from the selected nodes.
         */
        PHONETIC, 
        /**
         * The search parameter is based on a spatial transform of the selected nodes.
         */
        NEARBY, 
        /**
         * The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle.
         */
        DISTANCE, 
        /**
         * The interpretation of the xpath statement is unknown (and can't be automated).
         */
        OTHER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static XPathUsageType fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown XPathUsageType code '"+codeString+"'");
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
            case NORMAL: return "The search parameter is derived directly from the selected nodes based on the type definitions.";
            case PHONETIC: return "The search parameter is derived by a phonetic transform from the selected nodes.";
            case NEARBY: return "The search parameter is based on a spatial transform of the selected nodes.";
            case DISTANCE: return "The search parameter is based on a spatial transform of the selected nodes, using physical distance from the middle.";
            case OTHER: return "The interpretation of the xpath statement is unknown (and can't be automated).";
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
        public Enumeration<XPathUsageType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("normal".equals(codeString))
          return new Enumeration<XPathUsageType>(this, XPathUsageType.NORMAL);
        if ("phonetic".equals(codeString))
          return new Enumeration<XPathUsageType>(this, XPathUsageType.PHONETIC);
        if ("nearby".equals(codeString))
          return new Enumeration<XPathUsageType>(this, XPathUsageType.NEARBY);
        if ("distance".equals(codeString))
          return new Enumeration<XPathUsageType>(this, XPathUsageType.DISTANCE);
        if ("other".equals(codeString))
          return new Enumeration<XPathUsageType>(this, XPathUsageType.OTHER);
        throw new FHIRException("Unknown XPathUsageType code '"+codeString+"'");
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
    public String toSystem(XPathUsageType code) {
      return code.getSystem();
      }
    }

    /**
     * Explains why this search parameter is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this search parameter is defined", formalDefinition="Explains why this search parameter is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * The code used in the URL or the parameter name in a parameters resource for this search parameter.
     */
    @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Code used in URL", formalDefinition="The code used in the URL or the parameter name in a parameters resource for this search parameter." )
    protected CodeType code;

    /**
     * The base resource type that this search parameter refers to.
     */
    @Child(name = "base", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The resource type this search parameter applies to", formalDefinition="The base resource type that this search parameter refers to." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected CodeType base;

    /**
     * The type of value a search parameter refers to, and how the content is interpreted.
     */
    @Child(name = "type", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="number | date | string | token | reference | composite | quantity | uri", formalDefinition="The type of value a search parameter refers to, and how the content is interpreted." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/search-param-type")
    protected Enumeration<SearchParamType> type;

    /**
     * A FluentPath expression that returns a set of elements for the search parameter.
     */
    @Child(name = "expression", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="FluentPath expression that extracts the values", formalDefinition="A FluentPath expression that returns a set of elements for the search parameter." )
    protected StringType expression;

    /**
     * An XPath expression that returns a set of elements for the search parameter.
     */
    @Child(name = "xpath", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="XPath that extracts the values", formalDefinition="An XPath expression that returns a set of elements for the search parameter." )
    protected StringType xpath;

    /**
     * How the search parameter relates to the set of elements returned by evaluating the xpath query.
     */
    @Child(name = "xpathUsage", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="normal | phonetic | nearby | distance | other", formalDefinition="How the search parameter relates to the set of elements returned by evaluating the xpath query." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/search-xpath-usage")
    protected Enumeration<XPathUsageType> xpathUsage;

    /**
     * Types of resource (if a resource is referenced).
     */
    @Child(name = "target", type = {CodeType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Types of resource (if a resource reference)", formalDefinition="Types of resource (if a resource is referenced)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected List<CodeType> target;

    /**
     * Used to define the parts of a composite search parameter.
     */
    @Child(name = "component", type = {SearchParameter.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="For Composite resources to define the parts", formalDefinition="Used to define the parts of a composite search parameter." )
    protected List<Reference> component;
    /**
     * The actual objects that are the target of the reference (Used to define the parts of a composite search parameter.)
     */
    protected List<SearchParameter> componentTarget;


    private static final long serialVersionUID = 295034946L;

  /**
   * Constructor
   */
    public SearchParameter() {
      super();
    }

  /**
   * Constructor
   */
    public SearchParameter(UriType url, StringType name, Enumeration<PublicationStatus> status, CodeType code, CodeType base, Enumeration<SearchParamType> type, MarkdownType description) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
      this.code = code;
      this.base = base;
      this.type = type;
      this.description = description;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published. The URL SHOULD include the major version of the search parameter. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
     * @param value {@link #url} (An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published. The URL SHOULD include the major version of the search parameter. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public SearchParameter setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published. The URL SHOULD include the major version of the search parameter. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published. The URL SHOULD include the major version of the search parameter. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public SearchParameter setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the search parameter when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the search parameter author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the search parameter when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the search parameter author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public SearchParameter setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the search parameter when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the search parameter author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the search parameter when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the search parameter author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable.
     */
    public SearchParameter setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the search parameter. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (A natural language name identifying the search parameter. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public SearchParameter setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the search parameter. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the search parameter. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public SearchParameter setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this search parameter. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.status");
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
     * @param value {@link #status} (The status of this search parameter. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public SearchParameter setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this search parameter. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this search parameter. Enables tracking the life-cycle of the content.
     */
    public SearchParameter setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A flag to indicate that this search parameter is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
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
     * @param value {@link #experimental} (A flag to indicate that this search parameter is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public SearchParameter setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A flag to indicate that this search parameter is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A flag to indicate that this search parameter is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public SearchParameter setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the search parameter was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the search parameter changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (The date  (and optionally time) when the search parameter was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the search parameter changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public SearchParameter setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the search parameter was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the search parameter changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the search parameter was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the search parameter changes.
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
    public SearchParameter setContact(List<ContactDetail> theContact) { 
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

    public SearchParameter addContact(ContactDetail t) { //3
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SearchParameter setUseContext(List<UsageContext> theUseContext) { 
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

    public SearchParameter addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A jurisdiction in which the search parameter is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SearchParameter setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public SearchParameter addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #purpose} (Explains why this search parameter is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explains why this search parameter is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public SearchParameter setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explains why this search parameter is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explains why this search parameter is needed and why it has been designed as it has.
     */
    public SearchParameter setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
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
     * @return {@link #description} (A free text natural language description of the search parameter from the consumer's perspective. and how it used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.description");
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
     * @param value {@link #description} (A free text natural language description of the search parameter from the consumer's perspective. and how it used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public SearchParameter setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the search parameter from the consumer's perspective. and how it used.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the search parameter from the consumer's perspective. and how it used.
     */
    public SearchParameter setDescription(String value) { 
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      return this;
    }

    /**
     * @return {@link #expression} (A FluentPath expression that returns a set of elements for the search parameter.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
     */
    public StringType getExpressionElement() { 
      if (this.expression == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SearchParameter.expression");
        else if (Configuration.doAutoCreate())
          this.expression = new StringType(); // bb
      return this.expression;
    }

    public boolean hasExpressionElement() { 
      return this.expression != null && !this.expression.isEmpty();
    }

    public boolean hasExpression() { 
      return this.expression != null && !this.expression.isEmpty();
    }

    /**
     * @param value {@link #expression} (A FluentPath expression that returns a set of elements for the search parameter.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
     */
    public SearchParameter setExpressionElement(StringType value) { 
      this.expression = value;
      return this;
    }

    /**
     * @return A FluentPath expression that returns a set of elements for the search parameter.
     */
    public String getExpression() { 
      return this.expression == null ? null : this.expression.getValue();
    }

    /**
     * @param value A FluentPath expression that returns a set of elements for the search parameter.
     */
    public SearchParameter setExpression(String value) { 
      if (Utilities.noString(value))
        this.expression = null;
      else {
        if (this.expression == null)
          this.expression = new StringType();
        this.expression.setValue(value);
      }
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SearchParameter setTarget(List<CodeType> theTarget) { 
      this.target = theTarget;
      return this;
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

    /**
     * @return {@link #component} (Used to define the parts of a composite search parameter.)
     */
    public List<Reference> getComponent() { 
      if (this.component == null)
        this.component = new ArrayList<Reference>();
      return this.component;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SearchParameter setComponent(List<Reference> theComponent) { 
      this.component = theComponent;
      return this;
    }

    public boolean hasComponent() { 
      if (this.component == null)
        return false;
      for (Reference item : this.component)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addComponent() { //3
      Reference t = new Reference();
      if (this.component == null)
        this.component = new ArrayList<Reference>();
      this.component.add(t);
      return t;
    }

    public SearchParameter addComponent(Reference t) { //3
      if (t == null)
        return this;
      if (this.component == null)
        this.component = new ArrayList<Reference>();
      this.component.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #component}, creating it if it does not already exist
     */
    public Reference getComponentFirstRep() { 
      if (getComponent().isEmpty()) {
        addComponent();
      }
      return getComponent().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<SearchParameter> getComponentTarget() { 
      if (this.componentTarget == null)
        this.componentTarget = new ArrayList<SearchParameter>();
      return this.componentTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public SearchParameter addComponentTarget() { 
      SearchParameter r = new SearchParameter();
      if (this.componentTarget == null)
        this.componentTarget = new ArrayList<SearchParameter>();
      this.componentTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this search parameter when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this search parameter is (or will be) published. The URL SHOULD include the major version of the search parameter. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the search parameter when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the search parameter author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions are orderable.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the search parameter. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of this search parameter. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A flag to indicate that this search parameter is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the search parameter was published. The date must change when the business version changes, if it does, and it must change if the status code changes. In addition, it should change when the substantive content of the search parameter changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the search parameter.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching of code system definitions.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A jurisdiction in which the search parameter is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("purpose", "markdown", "Explains why this search parameter is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("code", "code", "The code used in the URL or the parameter name in a parameters resource for this search parameter.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("base", "code", "The base resource type that this search parameter refers to.", 0, java.lang.Integer.MAX_VALUE, base));
        childrenList.add(new Property("type", "code", "The type of value a search parameter refers to, and how the content is interpreted.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the search parameter from the consumer's perspective. and how it used.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("expression", "string", "A FluentPath expression that returns a set of elements for the search parameter.", 0, java.lang.Integer.MAX_VALUE, expression));
        childrenList.add(new Property("xpath", "string", "An XPath expression that returns a set of elements for the search parameter.", 0, java.lang.Integer.MAX_VALUE, xpath));
        childrenList.add(new Property("xpathUsage", "code", "How the search parameter relates to the set of elements returned by evaluating the xpath query.", 0, java.lang.Integer.MAX_VALUE, xpathUsage));
        childrenList.add(new Property("target", "code", "Types of resource (if a resource is referenced).", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("component", "Reference(SearchParameter)", "Used to define the parts of a composite search parameter.", 0, java.lang.Integer.MAX_VALUE, component));
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
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeType
        case 3016401: /*base*/ return this.base == null ? new Base[0] : new Base[] {this.base}; // CodeType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<SearchParamType>
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        case 114256029: /*xpath*/ return this.xpath == null ? new Base[0] : new Base[] {this.xpath}; // StringType
        case 1801322244: /*xpathUsage*/ return this.xpathUsage == null ? new Base[0] : new Base[] {this.xpathUsage}; // Enumeration<XPathUsageType>
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // CodeType
        case -1399907075: /*component*/ return this.component == null ? new Base[0] : this.component.toArray(new Base[this.component.size()]); // Reference
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
          this.status = new PublicationStatusEnumFactory().fromType(value); // Enumeration<PublicationStatus>
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          break;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          break;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          break;
        case 3059181: // code
          this.code = castToCode(value); // CodeType
          break;
        case 3016401: // base
          this.base = castToCode(value); // CodeType
          break;
        case 3575610: // type
          this.type = new SearchParamTypeEnumFactory().fromType(value); // Enumeration<SearchParamType>
          break;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          break;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          break;
        case 114256029: // xpath
          this.xpath = castToString(value); // StringType
          break;
        case 1801322244: // xpathUsage
          this.xpathUsage = new XPathUsageTypeEnumFactory().fromType(value); // Enumeration<XPathUsageType>
          break;
        case -880905839: // target
          this.getTarget().add(castToCode(value)); // CodeType
          break;
        case -1399907075: // component
          this.getComponent().add(castToReference(value)); // Reference
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
          this.status = new PublicationStatusEnumFactory().fromType(value); // Enumeration<PublicationStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add(castToContactDetail(value));
        else if (name.equals("useContext"))
          this.getUseContext().add(castToUsageContext(value));
        else if (name.equals("jurisdiction"))
          this.getJurisdiction().add(castToCodeableConcept(value));
        else if (name.equals("purpose"))
          this.purpose = castToMarkdown(value); // MarkdownType
        else if (name.equals("code"))
          this.code = castToCode(value); // CodeType
        else if (name.equals("base"))
          this.base = castToCode(value); // CodeType
        else if (name.equals("type"))
          this.type = new SearchParamTypeEnumFactory().fromType(value); // Enumeration<SearchParamType>
        else if (name.equals("description"))
          this.description = castToMarkdown(value); // MarkdownType
        else if (name.equals("expression"))
          this.expression = castToString(value); // StringType
        else if (name.equals("xpath"))
          this.xpath = castToString(value); // StringType
        else if (name.equals("xpathUsage"))
          this.xpathUsage = new XPathUsageTypeEnumFactory().fromType(value); // Enumeration<XPathUsageType>
        else if (name.equals("target"))
          this.getTarget().add(castToCode(value));
        else if (name.equals("component"))
          this.getComponent().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<PublicationStatus>
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case 3076014: throw new FHIRException("Cannot make property date as it is not a complex type"); // DateTimeType
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // ContactDetail
        case -669707736:  return addUseContext(); // UsageContext
        case -507075711:  return addJurisdiction(); // CodeableConcept
        case -220463842: throw new FHIRException("Cannot make property purpose as it is not a complex type"); // MarkdownType
        case 3059181: throw new FHIRException("Cannot make property code as it is not a complex type"); // CodeType
        case 3016401: throw new FHIRException("Cannot make property base as it is not a complex type"); // CodeType
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<SearchParamType>
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // MarkdownType
        case -1795452264: throw new FHIRException("Cannot make property expression as it is not a complex type"); // StringType
        case 114256029: throw new FHIRException("Cannot make property xpath as it is not a complex type"); // StringType
        case 1801322244: throw new FHIRException("Cannot make property xpathUsage as it is not a complex type"); // Enumeration<XPathUsageType>
        case -880905839: throw new FHIRException("Cannot make property target as it is not a complex type"); // CodeType
        case -1399907075:  return addComponent(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.purpose");
        }
        else if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.code");
        }
        else if (name.equals("base")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.base");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.type");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.description");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.expression");
        }
        else if (name.equals("xpath")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.xpath");
        }
        else if (name.equals("xpathUsage")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.xpathUsage");
        }
        else if (name.equals("target")) {
          throw new FHIRException("Cannot call addChild on a primitive type SearchParameter.target");
        }
        else if (name.equals("component")) {
          return addComponent();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SearchParameter";

  }

      public SearchParameter copy() {
        SearchParameter dst = new SearchParameter();
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
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.code = code == null ? null : code.copy();
        dst.base = base == null ? null : base.copy();
        dst.type = type == null ? null : type.copy();
        dst.description = description == null ? null : description.copy();
        dst.expression = expression == null ? null : expression.copy();
        dst.xpath = xpath == null ? null : xpath.copy();
        dst.xpathUsage = xpathUsage == null ? null : xpathUsage.copy();
        if (target != null) {
          dst.target = new ArrayList<CodeType>();
          for (CodeType i : target)
            dst.target.add(i.copy());
        };
        if (component != null) {
          dst.component = new ArrayList<Reference>();
          for (Reference i : component)
            dst.component.add(i.copy());
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
        return compareDeep(purpose, o.purpose, true) && compareDeep(code, o.code, true) && compareDeep(base, o.base, true)
           && compareDeep(type, o.type, true) && compareDeep(expression, o.expression, true) && compareDeep(xpath, o.xpath, true)
           && compareDeep(xpathUsage, o.xpathUsage, true) && compareDeep(target, o.target, true) && compareDeep(component, o.component, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SearchParameter))
          return false;
        SearchParameter o = (SearchParameter) other;
        return compareValues(purpose, o.purpose, true) && compareValues(code, o.code, true) && compareValues(base, o.base, true)
           && compareValues(type, o.type, true) && compareValues(expression, o.expression, true) && compareValues(xpath, o.xpath, true)
           && compareValues(xpathUsage, o.xpathUsage, true) && compareValues(target, o.target, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(purpose, code, base, type
          , expression, xpath, xpathUsage, target, component);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SearchParameter;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The search parameter publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>SearchParameter.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="SearchParameter.date", description="The search parameter publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The search parameter publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>SearchParameter.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Code used in URL</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="SearchParameter.code", description="Code used in URL", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Code used in URL</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for search parameter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="SearchParameter.jurisdiction", description="Intended jurisdiction for search parameter", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for search parameter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description of the search parameter</b><br>
   * Type: <b>string</b><br>
   * Path: <b>SearchParameter.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="SearchParameter.description", description="Text search against the description of the search parameter", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description of the search parameter</b><br>
   * Type: <b>string</b><br>
   * Path: <b>SearchParameter.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>number | date | string | token | reference | composite | quantity | uri</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="SearchParameter.type", description="number | date | string | token | reference | composite | quantity | uri", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>number | date | string | token | reference | composite | quantity | uri</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The version identifier of the search parameter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="SearchParameter.version", description="The version identifier of the search parameter", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The version identifier of the search parameter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the search parameter</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>SearchParameter.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="SearchParameter.url", description="The uri that identifies the search parameter", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the search parameter</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>SearchParameter.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>target</b>
   * <p>
   * Description: <b>Types of resource (if a resource reference)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target", path="SearchParameter.target", description="Types of resource (if a resource reference)", type="token" )
  public static final String SP_TARGET = "target";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target</b>
   * <p>
   * Description: <b>Types of resource (if a resource reference)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.target</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TARGET = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TARGET);

 /**
   * Search parameter: <b>component</b>
   * <p>
   * Description: <b>For Composite resources to define the parts</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SearchParameter.component</b><br>
   * </p>
   */
  @SearchParamDefinition(name="component", path="SearchParameter.component", description="For Composite resources to define the parts", type="reference", target={SearchParameter.class } )
  public static final String SP_COMPONENT = "component";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>component</b>
   * <p>
   * Description: <b>For Composite resources to define the parts</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>SearchParameter.component</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COMPONENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COMPONENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>SearchParameter:component</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COMPONENT = new ca.uhn.fhir.model.api.Include("SearchParameter:component").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Name of the search parameter</b><br>
   * Type: <b>string</b><br>
   * Path: <b>SearchParameter.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="SearchParameter.name", description="Name of the search parameter", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Name of the search parameter</b><br>
   * Type: <b>string</b><br>
   * Path: <b>SearchParameter.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the search parameter</b><br>
   * Type: <b>string</b><br>
   * Path: <b>SearchParameter.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="SearchParameter.publisher", description="Name of the publisher of the search parameter", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the search parameter</b><br>
   * Type: <b>string</b><br>
   * Path: <b>SearchParameter.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the search parameter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="SearchParameter.status", description="The current status of the search parameter", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the search parameter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>base</b>
   * <p>
   * Description: <b>The resource type this search parameter applies to</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.base</b><br>
   * </p>
   */
  @SearchParamDefinition(name="base", path="SearchParameter.base", description="The resource type this search parameter applies to", type="token" )
  public static final String SP_BASE = "base";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>base</b>
   * <p>
   * Description: <b>The resource type this search parameter applies to</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SearchParameter.base</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BASE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BASE);


}

