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
 * This is the base resource type for everything.
 */
public abstract class Resource extends BaseResource implements IAnyResource {

    /**
     * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
     */
    @Child(name = "id", type = {IdType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical id of this artifact", formalDefinition="The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes." )
    protected IdType id;

    /**
     * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.
     */
    @Child(name = "meta", type = {Meta.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Metadata about the resource", formalDefinition="The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource." )
    protected Meta meta;

    /**
     * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content.
     */
    @Child(name = "implicitRules", type = {UriType.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="A set of rules under which this content was created", formalDefinition="A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content." )
    protected UriType implicitRules;

    /**
     * The base language in which the resource is written.
     */
    @Child(name = "language", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Language of the resource content", formalDefinition="The base language in which the resource is written." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/languages")
    protected CodeType language;

    private static final long serialVersionUID = -559462759L;

  /**
   * Constructor
   */
    public Resource() {
      super();
    }

    /**
     * @return {@link #id} (The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.). This is the underlying object with id, value and extensions. The accessor "getId" gives direct access to the value
     */
    public IdType getIdElement() { 
      if (this.id == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Resource.id");
        else if (Configuration.doAutoCreate())
          this.id = new IdType(); // bb
      return this.id;
    }

    public boolean hasIdElement() { 
      return this.id != null && !this.id.isEmpty();
    }

    public boolean hasId() { 
      return this.id != null && !this.id.isEmpty();
    }

    /**
     * @param value {@link #id} (The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.). This is the underlying object with id, value and extensions. The accessor "getId" gives direct access to the value
     */
    public Resource setIdElement(IdType value) { 
      this.id = value;
      return this;
    }

    /**
     * @return The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
     */
    public String getId() { 
      return this.id == null ? null : this.id.getValue();
    }

    /**
     * @param value The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
     */
    public Resource setId(String value) { 
      if (Utilities.noString(value))
        this.id = null;
      else {
        if (this.id == null)
          this.id = new IdType();
        this.id.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #meta} (The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.)
     */
    public Meta getMeta() { 
      if (this.meta == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Resource.meta");
        else if (Configuration.doAutoCreate())
          this.meta = new Meta(); // cc
      return this.meta;
    }

    public boolean hasMeta() { 
      return this.meta != null && !this.meta.isEmpty();
    }

    /**
     * @param value {@link #meta} (The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.)
     */
    public Resource setMeta(Meta value) { 
      this.meta = value;
      return this;
    }

    /**
     * @return {@link #implicitRules} (A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content.). This is the underlying object with id, value and extensions. The accessor "getImplicitRules" gives direct access to the value
     */
    public UriType getImplicitRulesElement() { 
      if (this.implicitRules == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Resource.implicitRules");
        else if (Configuration.doAutoCreate())
          this.implicitRules = new UriType(); // bb
      return this.implicitRules;
    }

    public boolean hasImplicitRulesElement() { 
      return this.implicitRules != null && !this.implicitRules.isEmpty();
    }

    public boolean hasImplicitRules() { 
      return this.implicitRules != null && !this.implicitRules.isEmpty();
    }

    /**
     * @param value {@link #implicitRules} (A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content.). This is the underlying object with id, value and extensions. The accessor "getImplicitRules" gives direct access to the value
     */
    public Resource setImplicitRulesElement(UriType value) { 
      this.implicitRules = value;
      return this;
    }

    /**
     * @return A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content.
     */
    public String getImplicitRules() { 
      return this.implicitRules == null ? null : this.implicitRules.getValue();
    }

    /**
     * @param value A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content.
     */
    public Resource setImplicitRules(String value) { 
      if (Utilities.noString(value))
        this.implicitRules = null;
      else {
        if (this.implicitRules == null)
          this.implicitRules = new UriType();
        this.implicitRules.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #language} (The base language in which the resource is written.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
     */
    public CodeType getLanguageElement() { 
      if (this.language == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Resource.language");
        else if (Configuration.doAutoCreate())
          this.language = new CodeType(); // bb
      return this.language;
    }

    public boolean hasLanguageElement() { 
      return this.language != null && !this.language.isEmpty();
    }

    public boolean hasLanguage() { 
      return this.language != null && !this.language.isEmpty();
    }

    /**
     * @param value {@link #language} (The base language in which the resource is written.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
     */
    public Resource setLanguageElement(CodeType value) { 
      this.language = value;
      return this;
    }

    /**
     * @return The base language in which the resource is written.
     */
    public String getLanguage() { 
      return this.language == null ? null : this.language.getValue();
    }

    /**
     * @param value The base language in which the resource is written.
     */
    public Resource setLanguage(String value) { 
      if (Utilities.noString(value))
        this.language = null;
      else {
        if (this.language == null)
          this.language = new CodeType();
        this.language.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        childrenList.add(new Property("id", "id", "The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.", 0, java.lang.Integer.MAX_VALUE, id));
        childrenList.add(new Property("meta", "Meta", "The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.", 0, java.lang.Integer.MAX_VALUE, meta));
        childrenList.add(new Property("implicitRules", "uri", "A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content.", 0, java.lang.Integer.MAX_VALUE, implicitRules));
        childrenList.add(new Property("language", "code", "The base language in which the resource is written.", 0, java.lang.Integer.MAX_VALUE, language));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3355: /*id*/ return this.id == null ? new Base[0] : new Base[] {this.id}; // IdType
        case 3347973: /*meta*/ return this.meta == null ? new Base[0] : new Base[] {this.meta}; // Meta
        case -961826286: /*implicitRules*/ return this.implicitRules == null ? new Base[0] : new Base[] {this.implicitRules}; // UriType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3355: // id
          this.id = castToId(value); // IdType
          return value;
        case 3347973: // meta
          this.meta = castToMeta(value); // Meta
          return value;
        case -961826286: // implicitRules
          this.implicitRules = castToUri(value); // UriType
          return value;
        case -1613589672: // language
          this.language = castToCode(value); // CodeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("id")) {
          this.id = castToId(value); // IdType
        } else if (name.equals("meta")) {
          this.meta = castToMeta(value); // Meta
        } else if (name.equals("implicitRules")) {
          this.implicitRules = castToUri(value); // UriType
        } else if (name.equals("language")) {
          this.language = castToCode(value); // CodeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3355:  return getIdElement();
        case 3347973:  return getMeta(); 
        case -961826286:  return getImplicitRulesElement();
        case -1613589672:  return getLanguageElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3355: /*id*/ return new String[] {"id"};
        case 3347973: /*meta*/ return new String[] {"Meta"};
        case -961826286: /*implicitRules*/ return new String[] {"uri"};
        case -1613589672: /*language*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("id")) {
          throw new FHIRException("Cannot call addChild on a primitive type Resource.id");
        }
        else if (name.equals("meta")) {
          this.meta = new Meta();
          return this.meta;
        }
        else if (name.equals("implicitRules")) {
          throw new FHIRException("Cannot call addChild on a primitive type Resource.implicitRules");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type Resource.language");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Resource";

  }

      public abstract Resource copy();

      public void copyValues(Resource dst) {
        dst.id = id == null ? null : id.copy();
        dst.meta = meta == null ? null : meta.copy();
        dst.implicitRules = implicitRules == null ? null : implicitRules.copy();
        dst.language = language == null ? null : language.copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Resource))
          return false;
        Resource o = (Resource) other;
        return compareDeep(id, o.id, true) && compareDeep(meta, o.meta, true) && compareDeep(implicitRules, o.implicitRules, true)
           && compareDeep(language, o.language, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Resource))
          return false;
        Resource o = (Resource) other;
        return compareValues(id, o.id, true) && compareValues(implicitRules, o.implicitRules, true) && compareValues(language, o.language, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(id, meta, implicitRules
          , language);
      }


  @Override
  public String getIdBase() {
    return getId();
  }
  
  @Override
  public void setIdBase(String value) {
    setId(value);
  }
  public abstract ResourceType getResourceType();

}

