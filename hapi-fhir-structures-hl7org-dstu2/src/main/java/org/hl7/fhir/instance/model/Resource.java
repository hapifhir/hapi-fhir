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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.List;

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
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
    protected CodeType language;

    private static final long serialVersionUID = -559462759L;

  /*
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
        return super.isEmpty() && (id == null || id.isEmpty()) && (meta == null || meta.isEmpty()) && (implicitRules == null || implicitRules.isEmpty())
           && (language == null || language.isEmpty());
      }

  public abstract ResourceType getResourceType();

}

