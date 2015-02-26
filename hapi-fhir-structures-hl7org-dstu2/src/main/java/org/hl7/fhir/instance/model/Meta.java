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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.api.IMetaType;
/**
 * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.
 */
@DatatypeDef(name="Meta")
public class Meta extends Type implements IMetaType {

    /**
     * The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.
     */
    @Child(name = "versionId", type = {IdType.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="Version specific identifier", formalDefinition="The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted." )
    protected IdType versionId;

    /**
     * When the resource last changed - e.g. when the version changed.
     */
    @Child(name = "lastUpdated", type = {InstantType.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="When the resource version last changed", formalDefinition="When the resource last changed - e.g. when the version changed." )
    protected InstantType lastUpdated;

    /**
     * Set to 'true' if the resource is deleted. Deleted resources can not be fetched via the RESTful API, but may appear in bundles for various reasons.
     */
    @Child(name = "deleted", type = {BooleanType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="True if the resource is deleted", formalDefinition="Set to 'true' if the resource is deleted. Deleted resources can not be fetched via the RESTful API, but may appear in bundles for various reasons." )
    protected BooleanType deleted;

    /**
     * A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.
     */
    @Child(name = "profile", type = {UriType.class}, order = 3, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Profiles this resource claims to conform to", formalDefinition="A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url." )
    protected List<UriType> profile;

    /**
     * Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.
     */
    @Child(name = "security", type = {Coding.class}, order = 4, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Security Labels applied to this resource", formalDefinition="Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure." )
    protected List<Coding> security;

    /**
     * Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.
     */
    @Child(name = "tag", type = {Coding.class}, order = 5, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Tags applied", formalDefinition="Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource." )
    protected List<Coding> tag;

    private static final long serialVersionUID = -1174731372L;

    public Meta() {
      super();
    }

    /**
     * @return {@link #versionId} (The version specific identifier, as it appears in the version portion of the url. This values changes when the resource is created, updated, or deleted.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
     */
    public IdType getVersionIdElement() { 
      if (this.versionId == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Meta.versionId");
        else if (Configuration.doAutoCreate())
          this.versionId = new IdType(); // bb
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
    public Meta setVersionIdElement(IdType value) { 
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
    public Meta setVersionId(String value) { 
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
          throw new Error("Attempt to auto-create Meta.lastUpdated");
        else if (Configuration.doAutoCreate())
          this.lastUpdated = new InstantType(); // bb
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
    public Meta setLastUpdatedElement(InstantType value) { 
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
    public Meta setLastUpdated(Date value) { 
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
     * @return {@link #deleted} (Set to 'true' if the resource is deleted. Deleted resources can not be fetched via the RESTful API, but may appear in bundles for various reasons.). This is the underlying object with id, value and extensions. The accessor "getDeleted" gives direct access to the value
     */
    public BooleanType getDeletedElement() { 
      if (this.deleted == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Meta.deleted");
        else if (Configuration.doAutoCreate())
          this.deleted = new BooleanType(); // bb
      return this.deleted;
    }

    public boolean hasDeletedElement() { 
      return this.deleted != null && !this.deleted.isEmpty();
    }

    public boolean hasDeleted() { 
      return this.deleted != null && !this.deleted.isEmpty();
    }

    /**
     * @param value {@link #deleted} (Set to 'true' if the resource is deleted. Deleted resources can not be fetched via the RESTful API, but may appear in bundles for various reasons.). This is the underlying object with id, value and extensions. The accessor "getDeleted" gives direct access to the value
     */
    public Meta setDeletedElement(BooleanType value) { 
      this.deleted = value;
      return this;
    }

    /**
     * @return Set to 'true' if the resource is deleted. Deleted resources can not be fetched via the RESTful API, but may appear in bundles for various reasons.
     */
    public boolean getDeleted() { 
      return this.deleted == null ? false : this.deleted.getValue();
    }

    /**
     * @param value Set to 'true' if the resource is deleted. Deleted resources can not be fetched via the RESTful API, but may appear in bundles for various reasons.
     */
    public Meta setDeleted(boolean value) { 
        if (this.deleted == null)
          this.deleted = new BooleanType();
        this.deleted.setValue(value);
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
    public Meta addProfile(String value) { //1
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
        childrenList.add(new Property("deleted", "boolean", "Set to 'true' if the resource is deleted. Deleted resources can not be fetched via the RESTful API, but may appear in bundles for various reasons.", 0, java.lang.Integer.MAX_VALUE, deleted));
        childrenList.add(new Property("profile", "uri", "A list of profiles that this resource claims to conform to. The URL is a reference to Profile.url.", 0, java.lang.Integer.MAX_VALUE, profile));
        childrenList.add(new Property("security", "Coding", "Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.", 0, java.lang.Integer.MAX_VALUE, security));
        childrenList.add(new Property("tag", "Coding", "Tags applied to this resource. Tags are intended to to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.", 0, java.lang.Integer.MAX_VALUE, tag));
      }

      public Meta copy() {
        Meta dst = new Meta();
        copyValues(dst);
        dst.versionId = versionId == null ? null : versionId.copy();
        dst.lastUpdated = lastUpdated == null ? null : lastUpdated.copy();
        dst.deleted = deleted == null ? null : deleted.copy();
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

      protected Meta typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Meta))
          return false;
        Meta o = (Meta) other;
        return compareDeep(versionId, o.versionId, true) && compareDeep(lastUpdated, o.lastUpdated, true)
           && compareDeep(deleted, o.deleted, true) && compareDeep(profile, o.profile, true) && compareDeep(security, o.security, true)
           && compareDeep(tag, o.tag, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Meta))
          return false;
        Meta o = (Meta) other;
        return compareValues(versionId, o.versionId, true) && compareValues(lastUpdated, o.lastUpdated, true)
           && compareValues(deleted, o.deleted, true) && compareValues(profile, o.profile, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (versionId == null || versionId.isEmpty()) && (lastUpdated == null || lastUpdated.isEmpty())
           && (deleted == null || deleted.isEmpty()) && (profile == null || profile.isEmpty()) && (security == null || security.isEmpty())
           && (tag == null || tag.isEmpty());
      }


}

