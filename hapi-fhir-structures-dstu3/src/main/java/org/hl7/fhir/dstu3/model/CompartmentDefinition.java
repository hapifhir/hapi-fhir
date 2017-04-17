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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

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
 * A compartment definition that defines how resources are accessed on a server.
 */
@ResourceDef(name="CompartmentDefinition", profile="http://hl7.org/fhir/Profile/CompartmentDefinition")
@ChildOrder(names={"url", "name", "title", "status", "experimental", "date", "publisher", "contact", "description", "purpose", "useContext", "jurisdiction", "code", "search", "resource"})
public class CompartmentDefinition extends MetadataResource {

    public enum CompartmentType {
        /**
         * The compartment definition is for the patient compartment
         */
        PATIENT, 
        /**
         * The compartment definition is for the encounter compartment
         */
        ENCOUNTER, 
        /**
         * The compartment definition is for the related-person compartment
         */
        RELATEDPERSON, 
        /**
         * The compartment definition is for the practitioner compartment
         */
        PRACTITIONER, 
        /**
         * The compartment definition is for the device compartment
         */
        DEVICE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CompartmentType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Patient".equals(codeString))
          return PATIENT;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("Device".equals(codeString))
          return DEVICE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CompartmentType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/compartment-type";
            case ENCOUNTER: return "http://hl7.org/fhir/compartment-type";
            case RELATEDPERSON: return "http://hl7.org/fhir/compartment-type";
            case PRACTITIONER: return "http://hl7.org/fhir/compartment-type";
            case DEVICE: return "http://hl7.org/fhir/compartment-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The compartment definition is for the patient compartment";
            case ENCOUNTER: return "The compartment definition is for the encounter compartment";
            case RELATEDPERSON: return "The compartment definition is for the related-person compartment";
            case PRACTITIONER: return "The compartment definition is for the practitioner compartment";
            case DEVICE: return "The compartment definition is for the device compartment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
        }
    }

  public static class CompartmentTypeEnumFactory implements EnumFactory<CompartmentType> {
    public CompartmentType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Patient".equals(codeString))
          return CompartmentType.PATIENT;
        if ("Encounter".equals(codeString))
          return CompartmentType.ENCOUNTER;
        if ("RelatedPerson".equals(codeString))
          return CompartmentType.RELATEDPERSON;
        if ("Practitioner".equals(codeString))
          return CompartmentType.PRACTITIONER;
        if ("Device".equals(codeString))
          return CompartmentType.DEVICE;
        throw new IllegalArgumentException("Unknown CompartmentType code '"+codeString+"'");
        }
        public Enumeration<CompartmentType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CompartmentType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Patient".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.PATIENT);
        if ("Encounter".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.ENCOUNTER);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.RELATEDPERSON);
        if ("Practitioner".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.PRACTITIONER);
        if ("Device".equals(codeString))
          return new Enumeration<CompartmentType>(this, CompartmentType.DEVICE);
        throw new FHIRException("Unknown CompartmentType code '"+codeString+"'");
        }
    public String toCode(CompartmentType code) {
      if (code == CompartmentType.PATIENT)
        return "Patient";
      if (code == CompartmentType.ENCOUNTER)
        return "Encounter";
      if (code == CompartmentType.RELATEDPERSON)
        return "RelatedPerson";
      if (code == CompartmentType.PRACTITIONER)
        return "Practitioner";
      if (code == CompartmentType.DEVICE)
        return "Device";
      return "?";
      }
    public String toSystem(CompartmentType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CompartmentDefinitionResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of a resource supported by the server.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of resource type", formalDefinition="The name of a resource supported by the server." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType code;

        /**
         * The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment in more than one way,.
         */
        @Child(name = "param", type = {StringType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Search Parameter Name, or chained parameters", formalDefinition="The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment in more than one way,." )
        protected List<StringType> param;

        /**
         * Additional documentation about the resource and compartment.
         */
        @Child(name = "documentation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional documentation about the resource and compartment", formalDefinition="Additional documentation about the resource and compartment." )
        protected StringType documentation;

        private static final long serialVersionUID = 988080897L;

    /**
     * Constructor
     */
      public CompartmentDefinitionResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CompartmentDefinitionResourceComponent(CodeType code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (The name of a resource supported by the server.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CodeType getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompartmentDefinitionResourceComponent.code");
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
         * @param value {@link #code} (The name of a resource supported by the server.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CompartmentDefinitionResourceComponent setCodeElement(CodeType value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The name of a resource supported by the server.
         */
        public String getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The name of a resource supported by the server.
         */
        public CompartmentDefinitionResourceComponent setCode(String value) { 
            if (this.code == null)
              this.code = new CodeType();
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment in more than one way,.)
         */
        public List<StringType> getParam() { 
          if (this.param == null)
            this.param = new ArrayList<StringType>();
          return this.param;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CompartmentDefinitionResourceComponent setParam(List<StringType> theParam) { 
          this.param = theParam;
          return this;
        }

        public boolean hasParam() { 
          if (this.param == null)
            return false;
          for (StringType item : this.param)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment in more than one way,.)
         */
        public StringType addParamElement() {//2 
          StringType t = new StringType();
          if (this.param == null)
            this.param = new ArrayList<StringType>();
          this.param.add(t);
          return t;
        }

        /**
         * @param value {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment in more than one way,.)
         */
        public CompartmentDefinitionResourceComponent addParam(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.param == null)
            this.param = new ArrayList<StringType>();
          this.param.add(t);
          return this;
        }

        /**
         * @param value {@link #param} (The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment in more than one way,.)
         */
        public boolean hasParam(String value) { 
          if (this.param == null)
            return false;
          for (StringType v : this.param)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #documentation} (Additional documentation about the resource and compartment.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompartmentDefinitionResourceComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (Additional documentation about the resource and compartment.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public CompartmentDefinitionResourceComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return Additional documentation about the resource and compartment.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value Additional documentation about the resource and compartment.
         */
        public CompartmentDefinitionResourceComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "The name of a resource supported by the server.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("param", "string", "The name of a search parameter that represents the link to the compartment. More than one may be listed because a resource may be linked to a compartment in more than one way,.", 0, java.lang.Integer.MAX_VALUE, param));
          childrenList.add(new Property("documentation", "string", "Additional documentation about the resource and compartment.", 0, java.lang.Integer.MAX_VALUE, documentation));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeType
        case 106436749: /*param*/ return this.param == null ? new Base[0] : this.param.toArray(new Base[this.param.size()]); // StringType
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCode(value); // CodeType
          return value;
        case 106436749: // param
          this.getParam().add(castToString(value)); // StringType
          return value;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCode(value); // CodeType
        } else if (name.equals("param")) {
          this.getParam().add(castToString(value));
        } else if (name.equals("documentation")) {
          this.documentation = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case 106436749:  return addParamElement();
        case 1587405498:  return getDocumentationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case 106436749: /*param*/ return new String[] {"string"};
        case 1587405498: /*documentation*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.code");
        }
        else if (name.equals("param")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.param");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.documentation");
        }
        else
          return super.addChild(name);
      }

      public CompartmentDefinitionResourceComponent copy() {
        CompartmentDefinitionResourceComponent dst = new CompartmentDefinitionResourceComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (param != null) {
          dst.param = new ArrayList<StringType>();
          for (StringType i : param)
            dst.param.add(i.copy());
        };
        dst.documentation = documentation == null ? null : documentation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompartmentDefinitionResourceComponent))
          return false;
        CompartmentDefinitionResourceComponent o = (CompartmentDefinitionResourceComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(param, o.param, true) && compareDeep(documentation, o.documentation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompartmentDefinitionResourceComponent))
          return false;
        CompartmentDefinitionResourceComponent o = (CompartmentDefinitionResourceComponent) other;
        return compareValues(code, o.code, true) && compareValues(param, o.param, true) && compareValues(documentation, o.documentation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, param, documentation
          );
      }

  public String fhirType() {
    return "CompartmentDefinition.resource";

  }

  }

    /**
     * Explaination of why this compartment definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this compartment definition is defined", formalDefinition="Explaination of why this compartment definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * Which compartment this definition describes.
     */
    @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient | Encounter | RelatedPerson | Practitioner | Device", formalDefinition="Which compartment this definition describes." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/compartment-type")
    protected Enumeration<CompartmentType> code;

    /**
     * Whether the search syntax is supported,.
     */
    @Child(name = "search", type = {BooleanType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether the search syntax is supported", formalDefinition="Whether the search syntax is supported,." )
    protected BooleanType search;

    /**
     * Information about how a resource is related to the compartment.
     */
    @Child(name = "resource", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="How a resource is related to the compartment", formalDefinition="Information about how a resource is related to the compartment." )
    protected List<CompartmentDefinitionResourceComponent> resource;

    private static final long serialVersionUID = -1159172945L;

  /**
   * Constructor
   */
    public CompartmentDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public CompartmentDefinition(UriType url, StringType name, Enumeration<PublicationStatus> status, Enumeration<CompartmentType> code, BooleanType search) {
      super();
      this.url = url;
      this.name = name;
      this.status = status;
      this.code = code;
      this.search = search;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published. The URL SHOULD include the major version of the compartment definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published. The URL SHOULD include the major version of the compartment definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public CompartmentDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published. The URL SHOULD include the major version of the compartment definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published. The URL SHOULD include the major version of the compartment definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public CompartmentDefinition setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (A natural language name identifying the compartment definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.name");
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
     * @param value {@link #name} (A natural language name identifying the compartment definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CompartmentDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the compartment definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the compartment definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public CompartmentDefinition setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #title} (A short, descriptive, user-friendly title for the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.title");
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
     * @param value {@link #title} (A short, descriptive, user-friendly title for the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public CompartmentDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the compartment definition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the compartment definition.
     */
    public CompartmentDefinition setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of this compartment definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.status");
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
     * @param value {@link #status} (The status of this compartment definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CompartmentDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this compartment definition. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this compartment definition. Enables tracking the life-cycle of the content.
     */
    public CompartmentDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this compartment definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.experimental");
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
     * @param value {@link #experimental} (A boolean value to indicate that this compartment definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public CompartmentDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this compartment definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this compartment definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public CompartmentDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the compartment definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.date");
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
     * @param value {@link #date} (The date  (and optionally time) when the compartment definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public CompartmentDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the compartment definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the compartment definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.
     */
    public CompartmentDefinition setDate(Date value) { 
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
     * @return {@link #publisher} (The name of the individual or organization that published the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.publisher");
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
     * @param value {@link #publisher} (The name of the individual or organization that published the compartment definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public CompartmentDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the compartment definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the compartment definition.
     */
    public CompartmentDefinition setPublisher(String value) { 
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
    public CompartmentDefinition setContact(List<ContactDetail> theContact) { 
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

    public CompartmentDefinition addContact(ContactDetail t) { //3
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
     * @return {@link #description} (A free text natural language description of the compartment definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the compartment definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public CompartmentDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the compartment definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the compartment definition from a consumer's perspective.
     */
    public CompartmentDefinition setDescription(String value) { 
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
     * @return {@link #purpose} (Explaination of why this compartment definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.purpose");
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
     * @param value {@link #purpose} (Explaination of why this compartment definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public CompartmentDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this compartment definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this compartment definition is needed and why it has been designed as it has.
     */
    public CompartmentDefinition setPurpose(String value) { 
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
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate compartment definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CompartmentDefinition setUseContext(List<UsageContext> theUseContext) { 
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

    public CompartmentDefinition addUseContext(UsageContext t) { //3
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
     * @return {@link #jurisdiction} (A legal or geographic region in which the compartment definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CompartmentDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
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

    public CompartmentDefinition addJurisdiction(CodeableConcept t) { //3
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
     * @return {@link #code} (Which compartment this definition describes.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public Enumeration<CompartmentType> getCodeElement() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new Enumeration<CompartmentType>(new CompartmentTypeEnumFactory()); // bb
      return this.code;
    }

    public boolean hasCodeElement() { 
      return this.code != null && !this.code.isEmpty();
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Which compartment this definition describes.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public CompartmentDefinition setCodeElement(Enumeration<CompartmentType> value) { 
      this.code = value;
      return this;
    }

    /**
     * @return Which compartment this definition describes.
     */
    public CompartmentType getCode() { 
      return this.code == null ? null : this.code.getValue();
    }

    /**
     * @param value Which compartment this definition describes.
     */
    public CompartmentDefinition setCode(CompartmentType value) { 
        if (this.code == null)
          this.code = new Enumeration<CompartmentType>(new CompartmentTypeEnumFactory());
        this.code.setValue(value);
      return this;
    }

    /**
     * @return {@link #search} (Whether the search syntax is supported,.). This is the underlying object with id, value and extensions. The accessor "getSearch" gives direct access to the value
     */
    public BooleanType getSearchElement() { 
      if (this.search == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CompartmentDefinition.search");
        else if (Configuration.doAutoCreate())
          this.search = new BooleanType(); // bb
      return this.search;
    }

    public boolean hasSearchElement() { 
      return this.search != null && !this.search.isEmpty();
    }

    public boolean hasSearch() { 
      return this.search != null && !this.search.isEmpty();
    }

    /**
     * @param value {@link #search} (Whether the search syntax is supported,.). This is the underlying object with id, value and extensions. The accessor "getSearch" gives direct access to the value
     */
    public CompartmentDefinition setSearchElement(BooleanType value) { 
      this.search = value;
      return this;
    }

    /**
     * @return Whether the search syntax is supported,.
     */
    public boolean getSearch() { 
      return this.search == null || this.search.isEmpty() ? false : this.search.getValue();
    }

    /**
     * @param value Whether the search syntax is supported,.
     */
    public CompartmentDefinition setSearch(boolean value) { 
        if (this.search == null)
          this.search = new BooleanType();
        this.search.setValue(value);
      return this;
    }

    /**
     * @return {@link #resource} (Information about how a resource is related to the compartment.)
     */
    public List<CompartmentDefinitionResourceComponent> getResource() { 
      if (this.resource == null)
        this.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
      return this.resource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CompartmentDefinition setResource(List<CompartmentDefinitionResourceComponent> theResource) { 
      this.resource = theResource;
      return this;
    }

    public boolean hasResource() { 
      if (this.resource == null)
        return false;
      for (CompartmentDefinitionResourceComponent item : this.resource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CompartmentDefinitionResourceComponent addResource() { //3
      CompartmentDefinitionResourceComponent t = new CompartmentDefinitionResourceComponent();
      if (this.resource == null)
        this.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
      this.resource.add(t);
      return t;
    }

    public CompartmentDefinition addResource(CompartmentDefinitionResourceComponent t) { //3
      if (t == null)
        return this;
      if (this.resource == null)
        this.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
      this.resource.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #resource}, creating it if it does not already exist
     */
    public CompartmentDefinitionResourceComponent getResourceFirstRep() { 
      if (getResource().isEmpty()) {
        addResource();
      }
      return getResource().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this compartment definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this compartment definition is (or will be) published. The URL SHOULD include the major version of the compartment definition. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("name", "string", "A natural language name identifying the compartment definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the compartment definition.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of this compartment definition. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this compartment definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the compartment definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the compartment definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the compartment definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the compartment definition from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this compartment definition is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate compartment definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the compartment definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("code", "code", "Which compartment this definition describes.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("search", "boolean", "Whether the search syntax is supported,.", 0, java.lang.Integer.MAX_VALUE, search));
        childrenList.add(new Property("resource", "", "Information about how a resource is related to the compartment.", 0, java.lang.Integer.MAX_VALUE, resource));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<CompartmentType>
        case -906336856: /*search*/ return this.search == null ? new Base[0] : new Base[] {this.search}; // BooleanType
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : this.resource.toArray(new Base[this.resource.size()]); // CompartmentDefinitionResourceComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
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
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3059181: // code
          value = new CompartmentTypeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<CompartmentType>
          return value;
        case -906336856: // search
          this.search = castToBoolean(value); // BooleanType
          return value;
        case -341064690: // resource
          this.getResource().add((CompartmentDefinitionResourceComponent) value); // CompartmentDefinitionResourceComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
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
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("code")) {
          value = new CompartmentTypeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<CompartmentType>
        } else if (name.equals("search")) {
          this.search = castToBoolean(value); // BooleanType
        } else if (name.equals("resource")) {
          this.getResource().add((CompartmentDefinitionResourceComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -220463842:  return getPurposeElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case 3059181:  return getCodeElement();
        case -906336856:  return getSearchElement();
        case -341064690:  return addResource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"code"};
        case -906336856: /*search*/ return new String[] {"boolean"};
        case -341064690: /*resource*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.url");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.purpose");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.code");
        }
        else if (name.equals("search")) {
          throw new FHIRException("Cannot call addChild on a primitive type CompartmentDefinition.search");
        }
        else if (name.equals("resource")) {
          return addResource();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CompartmentDefinition";

  }

      public CompartmentDefinition copy() {
        CompartmentDefinition dst = new CompartmentDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
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
        dst.purpose = purpose == null ? null : purpose.copy();
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
        dst.code = code == null ? null : code.copy();
        dst.search = search == null ? null : search.copy();
        if (resource != null) {
          dst.resource = new ArrayList<CompartmentDefinitionResourceComponent>();
          for (CompartmentDefinitionResourceComponent i : resource)
            dst.resource.add(i.copy());
        };
        return dst;
      }

      protected CompartmentDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompartmentDefinition))
          return false;
        CompartmentDefinition o = (CompartmentDefinition) other;
        return compareDeep(purpose, o.purpose, true) && compareDeep(code, o.code, true) && compareDeep(search, o.search, true)
           && compareDeep(resource, o.resource, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompartmentDefinition))
          return false;
        CompartmentDefinition o = (CompartmentDefinition) other;
        return compareValues(purpose, o.purpose, true) && compareValues(code, o.code, true) && compareValues(search, o.search, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(purpose, code, search, resource
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CompartmentDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The compartment definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CompartmentDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="CompartmentDefinition.date", description="The compartment definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The compartment definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CompartmentDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Patient | Encounter | RelatedPerson | Practitioner | Device</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="CompartmentDefinition.code", description="Patient | Encounter | RelatedPerson | Practitioner | Device", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Patient | Encounter | RelatedPerson | Practitioner | Device</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>resource</b>
   * <p>
   * Description: <b>Name of resource type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.resource.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resource", path="CompartmentDefinition.resource.code", description="Name of resource type", type="token" )
  public static final String SP_RESOURCE = "resource";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resource</b>
   * <p>
   * Description: <b>Name of resource type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.resource.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RESOURCE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RESOURCE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the compartment definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="CompartmentDefinition.jurisdiction", description="Intended jurisdiction for the compartment definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the compartment definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="CompartmentDefinition.name", description="Computationally friendly name of the compartment definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="CompartmentDefinition.description", description="The description of the compartment definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="CompartmentDefinition.publisher", description="Name of the publisher of the compartment definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="CompartmentDefinition.title", description="The human-friendly name of the compartment definition", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>The human-friendly name of the compartment definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>CompartmentDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the compartment definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CompartmentDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="CompartmentDefinition.url", description="The uri that identifies the compartment definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the compartment definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>CompartmentDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the compartment definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CompartmentDefinition.status", description="The current status of the compartment definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the compartment definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CompartmentDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

