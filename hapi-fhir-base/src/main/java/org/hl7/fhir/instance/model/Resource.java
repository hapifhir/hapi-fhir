package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Fri, Dec 5, 2014 09:17+1100 for FHIR v0.3.0

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.utilities.Utilities;
/**
 * Base Resource for everything.
 */
@ResourceDef(name="Resource", profile="http://hl7.org/fhir/Profile/Resource")
public abstract class Resource extends Base implements IBaseResource {

    @Block()
    public static class ResourceMetaComponent extends BackboneElement {
        /**
         * The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.
         */
        @Child(name="versionId", type={IdType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Version specific identifier", formalDefinition="The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted." )
        protected IdType versionId;

        /**
         * When the resource last changed - e.g. when the version changed.
         */
        @Child(name="lastUpdated", type={InstantType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="When the resource version last changed", formalDefinition="When the resource last changed - e.g. when the version changed." )
        protected InstantType lastUpdated;

        /**
         * A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.
         */
        @Child(name="profile", type={UriType.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Profiles this resource claims to conform to", formalDefinition="A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url." )
        protected List<UriType> profile;

        /**
         * Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.
         */
        @Child(name="security", type={Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Security Labels applied to this resource", formalDefinition="Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure." )
        protected List<Coding> security;

        /**
         * Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.
         */
        @Child(name="tag", type={Coding.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Tags applied", formalDefinition="Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource." )
        protected List<Coding> tag;

        private static final long serialVersionUID = 650918851L;

      public ResourceMetaComponent() {
        super();
      }

        /**
         * @return {@link #versionId} (The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public IdType getVersionIdElement() { 
          if (this.versionId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResourceMetaComponent.versionId");
            else if (Configuration.doAutoCreate())
              this.versionId = new IdType();
          return this.versionId;
        }

        public boolean hasVersionIdElement() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        public boolean hasVersionId() { 
          return this.versionId != null && !this.versionId.isEmpty();
        }

        /**
         * @param value {@link #versionId} (The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
         */
        public ResourceMetaComponent setVersionIdElement(IdType value) { 
          this.versionId = value;
          return this;
        }

        /**
         * @return The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.
         */
        public String getVersionId() { 
          return this.versionId == null ? null : this.versionId.getValue();
        }

        /**
         * @param value The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.
         */
        public ResourceMetaComponent setVersionId(String value) { 
          if (Utilities.noString(value))
            this.versionId = null;
          else {
            if (this.versionId == null)
              this.versionId = new IdType();
            this.versionId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #lastUpdated} (When the resource last changed - e.g. when the version changed.). This is the underlying object with id, value and extensions. The accessor "getLastUpdated" gives direct access to the value
         */
        public InstantType getLastUpdatedElement() { 
          if (this.lastUpdated == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResourceMetaComponent.lastUpdated");
            else if (Configuration.doAutoCreate())
              this.lastUpdated = new InstantType();
          return this.lastUpdated;
        }

        public boolean hasLastUpdatedElement() { 
          return this.lastUpdated != null && !this.lastUpdated.isEmpty();
        }

        public boolean hasLastUpdated() { 
          return this.lastUpdated != null && !this.lastUpdated.isEmpty();
        }

        /**
         * @param value {@link #lastUpdated} (When the resource last changed - e.g. when the version changed.). This is the underlying object with id, value and extensions. The accessor "getLastUpdated" gives direct access to the value
         */
        public ResourceMetaComponent setLastUpdatedElement(InstantType value) { 
          this.lastUpdated = value;
          return this;
        }

        /**
         * @return When the resource last changed - e.g. when the version changed.
         */
        public Date getLastUpdated() { 
          return this.lastUpdated == null ? null : this.lastUpdated.getValue();
        }

        /**
         * @param value When the resource last changed - e.g. when the version changed.
         */
        public ResourceMetaComponent setLastUpdated(Date value) { 
          if (value == null)
            this.lastUpdated = null;
          else {
            if (this.lastUpdated == null)
              this.lastUpdated = new InstantType();
            this.lastUpdated.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #profile} (A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.)
         */
        public List<UriType> getProfile() { 
          if (this.profile == null)
            this.profile = new ArrayList<UriType>();
          return this.profile;
        }

        public boolean hasProfile() { 
          if (this.profile == null)
            return false;
          for (UriType item : this.profile)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #profile} (A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.)
         */
    // syntactic sugar
        public UriType addProfileElement() {//2 
          UriType t = new UriType();
          if (this.profile == null)
            this.profile = new ArrayList<UriType>();
          this.profile.add(t);
          return t;
        }

        /**
         * @param value {@link #profile} (A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.)
         */
        public ResourceMetaComponent addProfile(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.profile == null)
            this.profile = new ArrayList<UriType>();
          this.profile.add(t);
          return this;
        }

        /**
         * @param value {@link #profile} (A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.)
         */
        public boolean hasProfile(String value) { 
          if (this.profile == null)
            return false;
          for (UriType v : this.profile)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #security} (Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.)
         */
        public List<Coding> getSecurity() { 
          if (this.security == null)
            this.security = new ArrayList<Coding>();
          return this.security;
        }

        public boolean hasSecurity() { 
          if (this.security == null)
            return false;
          for (Coding item : this.security)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #security} (Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.)
         */
    // syntactic sugar
        public Coding addSecurity() { //3
          Coding t = new Coding();
          if (this.security == null)
            this.security = new ArrayList<Coding>();
          this.security.add(t);
          return t;
        }

        /**
         * @return {@link #tag} (Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.)
         */
        public List<Coding> getTag() { 
          if (this.tag == null)
            this.tag = new ArrayList<Coding>();
          return this.tag;
        }

        public boolean hasTag() { 
          if (this.tag == null)
            return false;
          for (Coding item : this.tag)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #tag} (Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.)
         */
    // syntactic sugar
        public Coding addTag() { //3
          Coding t = new Coding();
          if (this.tag == null)
            this.tag = new ArrayList<Coding>();
          this.tag.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("versionId", "id", "The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.", 0, java.lang.Integer.MAX_VALUE, versionId));
          childrenList.add(new Property("lastUpdated", "instant", "When the resource last changed - e.g. when the version changed.", 0, java.lang.Integer.MAX_VALUE, lastUpdated));
          childrenList.add(new Property("profile", "uri", "A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("security", "Coding", "Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.", 0, java.lang.Integer.MAX_VALUE, security));
          childrenList.add(new Property("tag", "Coding", "Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.", 0, java.lang.Integer.MAX_VALUE, tag));
        }

      public ResourceMetaComponent copy() {
        ResourceMetaComponent dst = new ResourceMetaComponent();
        copyValues(dst);
        dst.versionId = versionId == null ? null : versionId.copy();
        dst.lastUpdated = lastUpdated == null ? null : lastUpdated.copy();
        if (profile != null) {
          dst.profile = new ArrayList<UriType>();
          for (UriType i : profile)
            dst.profile.add(i.copy());
        };
        if (security != null) {
          dst.security = new ArrayList<Coding>();
          for (Coding i : security)
            dst.security.add(i.copy());
        };
        if (tag != null) {
          dst.tag = new ArrayList<Coding>();
          for (Coding i : tag)
            dst.tag.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (versionId == null || versionId.isEmpty()) && (lastUpdated == null || lastUpdated.isEmpty())
           && (profile == null || profile.isEmpty()) && (security == null || security.isEmpty()) && (tag == null || tag.isEmpty())
          ;
      }

  }

    /**
     * The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes.
     */
    @Child(name="id", type={IdType.class}, order=-1, min=0, max=1)
    @Description(shortDefinition="Logical id of this artefact", formalDefinition="The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes." )
    protected IdType id;

    /**
     * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.
     */
    @Child(name="meta", type={}, order=0, min=0, max=1)
    @Description(shortDefinition="Metadata about the resource", formalDefinition="The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource." )
    protected ResourceMetaComponent meta;

    /**
     * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content.
     */
    @Child(name="implicitRules", type={UriType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="A set of rules under which this content was created", formalDefinition="A reference to a set of rules that were followed when the resource was constructed, and which must be understood when processing the content." )
    protected UriType implicitRules;

    /**
     * The base language in which the resource is written.
     */
    @Child(name="language", type={CodeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Language of the resource content", formalDefinition="The base language in which the resource is written." )
    protected CodeType language;

    private static final long serialVersionUID = -519506254L;

    public Resource() {
      super();
    }

    /**
     * @return {@link #id} (The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes.). This is the underlying object with id, value and extensions. The accessor "getId" gives direct access to the value
     */
    public IdType getIdElement() { 
      if (this.id == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Resource.id");
        else if (Configuration.doAutoCreate())
          this.id = new IdType();
      return this.id;
    }

    public boolean hasIdElement() { 
      return this.id != null && !this.id.isEmpty();
    }

    public boolean hasId() { 
      return this.id != null && !this.id.isEmpty();
    }

    /**
     * @param value {@link #id} (The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes.). This is the underlying object with id, value and extensions. The accessor "getId" gives direct access to the value
     */
    public Resource setIdElement(IdType value) { 
      this.id = value;
      return this;
    }

    /**
     * @return The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes.
     */
    public String getId() { 
      return this.id == null ? null : this.id.getValue();
    }

    /**
     * @param value The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes.
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
    public ResourceMetaComponent getMeta() { 
      if (this.meta == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Resource.meta");
        else if (Configuration.doAutoCreate())
          this.meta = new ResourceMetaComponent();
      return this.meta;
    }

    public boolean hasMeta() { 
      return this.meta != null && !this.meta.isEmpty();
    }

    /**
     * @param value {@link #meta} (The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.)
     */
    public Resource setMeta(ResourceMetaComponent value) { 
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
          this.implicitRules = new UriType();
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
          this.language = new CodeType();
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
        childrenList.add(new Property("id", "id", "The logical id of the resource, as used in the url for the resoure. Once assigned, this value never changes.", 0, java.lang.Integer.MAX_VALUE, id));
        childrenList.add(new Property("meta", "", "The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.", 0, java.lang.Integer.MAX_VALUE, meta));
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

      public boolean isEmpty() {
        return super.isEmpty() && (id == null || id.isEmpty()) && (meta == null || meta.isEmpty()) && (implicitRules == null || implicitRules.isEmpty())
           && (language == null || language.isEmpty());
      }

  public abstract ResourceType getResourceType();

}

