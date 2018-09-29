package org.hl7.fhir.r4.model;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content might not always be associated with version changes to the resource.
 */
@DatatypeDef(name="Meta")
public class Meta extends Type implements IBaseMetaType {

    /**
     * The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted.
     */
    @Child(name = "versionId", type = {IdType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Version specific identifier", formalDefinition="The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted." )
    protected IdType versionId;

    /**
     * When the resource last changed - e.g. when the version changed.
     */
    @Child(name = "lastUpdated", type = {InstantType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the resource version last changed", formalDefinition="When the resource last changed - e.g. when the version changed." )
    protected InstantType lastUpdated;

    /**
     * A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc.
     */
    @Child(name = "source", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifies where the resource comes from", formalDefinition="A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc." )
    protected UriType source;

    /**
     * A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]].
     */
    @Child(name = "profile", type = {CanonicalType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Profiles this resource claims to conform to", formalDefinition="A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]]." )
    protected List<CanonicalType> profile;

    /**
     * Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.
     */
    @Child(name = "security", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Security Labels applied to this resource", formalDefinition="Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/security-labels")
    protected List<Coding> security;

    /**
     * Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.
     */
    @Child(name = "tag", type = {Coding.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Tags applied to this resource", formalDefinition="Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/common-tags")
    protected List<Coding> tag;

    private static final long serialVersionUID = -1386695622L;

  /**
   * Constructor
   */
    public Meta() {
      super();
    }

    /**
     * @return {@link #versionId} (The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
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
     * @param value {@link #versionId} (The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted.). This is the underlying object with id, value and extensions. The accessor "getVersionId" gives direct access to the value
     */
    public Meta setVersionIdElement(IdType value) { 
      this.versionId = value;
      return this;
    }

    /**
     * @return The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted.
     */
    public String getVersionId() { 
      return this.versionId == null ? null : this.versionId.getValue();
    }

    /**
     * @param value The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted.
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
     * @return {@link #source} (A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
     */
    public UriType getSourceElement() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Meta.source");
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
     * @param value {@link #source} (A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
     */
    public Meta setSourceElement(UriType value) { 
      this.source = value;
      return this;
    }

    /**
     * @return A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc.
     */
    public String getSource() { 
      return this.source == null ? null : this.source.getValue();
    }

    /**
     * @param value A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc.
     */
    public Meta setSource(String value) { 
      if (Utilities.noString(value))
        this.source = null;
      else {
        if (this.source == null)
          this.source = new UriType();
        this.source.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #profile} (A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]].)
     */
    public List<CanonicalType> getProfile() { 
      if (this.profile == null)
        this.profile = new ArrayList<CanonicalType>();
      return this.profile;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Meta setProfile(List<CanonicalType> theProfile) { 
      this.profile = theProfile;
      return this;
    }

    public boolean hasProfile() { 
      if (this.profile == null)
        return false;
      for (CanonicalType item : this.profile)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #profile} (A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]].)
     */
    public CanonicalType addProfileElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.profile == null)
        this.profile = new ArrayList<CanonicalType>();
      this.profile.add(t);
      return t;
    }

    /**
     * @param value {@link #profile} (A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]].)
     */
    public Meta addProfile(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.profile == null)
        this.profile = new ArrayList<CanonicalType>();
      this.profile.add(t);
      return this;
    }

    /**
     * @param value {@link #profile} (A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]].)
     */
    public boolean hasProfile(String value) { 
      if (this.profile == null)
        return false;
      for (CanonicalType v : this.profile)
        if (v.getValue().equals(value)) // canonical(StructureDefinition)
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Meta setSecurity(List<Coding> theSecurity) { 
      this.security = theSecurity;
      return this;
    }

    public boolean hasSecurity() { 
      if (this.security == null)
        return false;
      for (Coding item : this.security)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addSecurity() { //3
      Coding t = new Coding();
      if (this.security == null)
        this.security = new ArrayList<Coding>();
      this.security.add(t);
      return t;
    }

    public Meta addSecurity(Coding t) { //3
      if (t == null)
        return this;
      if (this.security == null)
        this.security = new ArrayList<Coding>();
      this.security.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #security}, creating it if it does not already exist
     */
    public Coding getSecurityFirstRep() { 
      if (getSecurity().isEmpty()) {
        addSecurity();
      }
      return getSecurity().get(0);
    }

    /**
     * @return {@link #tag} (Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.)
     */
    public List<Coding> getTag() { 
      if (this.tag == null)
        this.tag = new ArrayList<Coding>();
      return this.tag;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Meta setTag(List<Coding> theTag) { 
      this.tag = theTag;
      return this;
    }

    public boolean hasTag() { 
      if (this.tag == null)
        return false;
      for (Coding item : this.tag)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addTag() { //3
      Coding t = new Coding();
      if (this.tag == null)
        this.tag = new ArrayList<Coding>();
      this.tag.add(t);
      return t;
    }

    public Meta addTag(Coding t) { //3
      if (t == null)
        return this;
      if (this.tag == null)
        this.tag = new ArrayList<Coding>();
      this.tag.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #tag}, creating it if it does not already exist
     */
    public Coding getTagFirstRep() { 
      if (getTag().isEmpty()) {
        addTag();
      }
      return getTag().get(0);
    }

    /**
     * Convenience method which adds a tag
     * 
     * @param theSystem The code system
     * @param theCode The code
     * @param theDisplay The display name
     * @return Returns a reference to <code>this</code> for easy chaining
     */
    public Meta addTag(String theSystem, String theCode, String theDisplay) {
     addTag().setSystem(theSystem).setCode(theCode).setDisplay(theDisplay);
     return this;
    }
    /**
     * Convenience method which adds a security tag
     * 
     * @param theSystem The code system
     * @param theCode The code
     * @param theDisplay The display name
     * @return Returns a reference to <code>this</code> for easy chaining
     */
    public Meta addSecurity(String theSystem, String theCode, String theDisplay) {
     addSecurity().setSystem(theSystem).setCode(theCode).setDisplay(theDisplay);
     return this;
    }
   /**
   * Returns the first tag (if any) that has the given system and code, or returns
   * <code>null</code> if none
   */
  public Coding getTag(String theSystem, String theCode) {
    for (Coding next : getTag()) {
      if (ca.uhn.fhir.util.ObjectUtil.equals(next.getSystem(), theSystem) && ca.uhn.fhir.util.ObjectUtil.equals(next.getCode(), theCode)) {
        return next;
      }
    }
    return null;
  }

  /**
   * Returns the first security label (if any) that has the given system and code, or returns
   * <code>null</code> if none
   */
  public Coding getSecurity(String theSystem, String theCode) {
    for (Coding next : getTag()) {
      if (ca.uhn.fhir.util.ObjectUtil.equals(next.getSystem(), theSystem) && ca.uhn.fhir.util.ObjectUtil.equals(next.getCode(), theCode)) {
        return next;
      }
    }
    return null;
  }
      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("versionId", "id", "The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted.", 0, 1, versionId));
        children.add(new Property("lastUpdated", "instant", "When the resource last changed - e.g. when the version changed.", 0, 1, lastUpdated));
        children.add(new Property("source", "uri", "A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc.", 0, 1, source));
        children.add(new Property("profile", "canonical(StructureDefinition)", "A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]].", 0, java.lang.Integer.MAX_VALUE, profile));
        children.add(new Property("security", "Coding", "Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.", 0, java.lang.Integer.MAX_VALUE, security));
        children.add(new Property("tag", "Coding", "Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.", 0, java.lang.Integer.MAX_VALUE, tag));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1407102957: /*versionId*/  return new Property("versionId", "id", "The version specific identifier, as it appears in the version portion of the URL. This value changes when the resource is created, updated, or deleted.", 0, 1, versionId);
        case 1649733957: /*lastUpdated*/  return new Property("lastUpdated", "instant", "When the resource last changed - e.g. when the version changed.", 0, 1, lastUpdated);
        case -896505829: /*source*/  return new Property("source", "uri", "A uri that identifies the source system of the resource. This provides a minimal amount of [[[Provenance]]] information that can be used to track or differentiate the source of information in the resource. The source may identify another FHIR server, document, message, database, etc.", 0, 1, source);
        case -309425751: /*profile*/  return new Property("profile", "canonical(StructureDefinition)", "A list of profiles (references to [[[StructureDefinition]]] resources) that this resource claims to conform to. The URL is a reference to [[[StructureDefinition.url]]].", 0, java.lang.Integer.MAX_VALUE, profile);
        case 949122880: /*security*/  return new Property("security", "Coding", "Security labels applied to this resource. These tags connect specific resources to the overall security policy and infrastructure.", 0, java.lang.Integer.MAX_VALUE, security);
        case 114586: /*tag*/  return new Property("tag", "Coding", "Tags applied to this resource. Tags are intended to be used to identify and relate resources to process and workflow, and applications are not required to consider the tags when interpreting the meaning of a resource.", 0, java.lang.Integer.MAX_VALUE, tag);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1407102957: /*versionId*/ return this.versionId == null ? new Base[0] : new Base[] {this.versionId}; // IdType
        case 1649733957: /*lastUpdated*/ return this.lastUpdated == null ? new Base[0] : new Base[] {this.lastUpdated}; // InstantType
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // UriType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : this.profile.toArray(new Base[this.profile.size()]); // CanonicalType
        case 949122880: /*security*/ return this.security == null ? new Base[0] : this.security.toArray(new Base[this.security.size()]); // Coding
        case 114586: /*tag*/ return this.tag == null ? new Base[0] : this.tag.toArray(new Base[this.tag.size()]); // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1407102957: // versionId
          this.versionId = castToId(value); // IdType
          return value;
        case 1649733957: // lastUpdated
          this.lastUpdated = castToInstant(value); // InstantType
          return value;
        case -896505829: // source
          this.source = castToUri(value); // UriType
          return value;
        case -309425751: // profile
          this.getProfile().add(castToCanonical(value)); // CanonicalType
          return value;
        case 949122880: // security
          this.getSecurity().add(castToCoding(value)); // Coding
          return value;
        case 114586: // tag
          this.getTag().add(castToCoding(value)); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("versionId")) {
          this.versionId = castToId(value); // IdType
        } else if (name.equals("lastUpdated")) {
          this.lastUpdated = castToInstant(value); // InstantType
        } else if (name.equals("source")) {
          this.source = castToUri(value); // UriType
        } else if (name.equals("profile")) {
          this.getProfile().add(castToCanonical(value));
        } else if (name.equals("security")) {
          this.getSecurity().add(castToCoding(value));
        } else if (name.equals("tag")) {
          this.getTag().add(castToCoding(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1407102957:  return getVersionIdElement();
        case 1649733957:  return getLastUpdatedElement();
        case -896505829:  return getSourceElement();
        case -309425751:  return addProfileElement();
        case 949122880:  return addSecurity(); 
        case 114586:  return addTag(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1407102957: /*versionId*/ return new String[] {"id"};
        case 1649733957: /*lastUpdated*/ return new String[] {"instant"};
        case -896505829: /*source*/ return new String[] {"uri"};
        case -309425751: /*profile*/ return new String[] {"canonical"};
        case 949122880: /*security*/ return new String[] {"Coding"};
        case 114586: /*tag*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("versionId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Meta.versionId");
        }
        else if (name.equals("lastUpdated")) {
          throw new FHIRException("Cannot call addChild on a primitive type Meta.lastUpdated");
        }
        else if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type Meta.source");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type Meta.profile");
        }
        else if (name.equals("security")) {
          return addSecurity();
        }
        else if (name.equals("tag")) {
          return addTag();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Meta";

  }

      public Meta copy() {
        Meta dst = new Meta();
        copyValues(dst);
        dst.versionId = versionId == null ? null : versionId.copy();
        dst.lastUpdated = lastUpdated == null ? null : lastUpdated.copy();
        dst.source = source == null ? null : source.copy();
        if (profile != null) {
          dst.profile = new ArrayList<CanonicalType>();
          for (CanonicalType i : profile)
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Meta))
          return false;
        Meta o = (Meta) other_;
        return compareDeep(versionId, o.versionId, true) && compareDeep(lastUpdated, o.lastUpdated, true)
           && compareDeep(source, o.source, true) && compareDeep(profile, o.profile, true) && compareDeep(security, o.security, true)
           && compareDeep(tag, o.tag, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Meta))
          return false;
        Meta o = (Meta) other_;
        return compareValues(versionId, o.versionId, true) && compareValues(lastUpdated, o.lastUpdated, true)
           && compareValues(source, o.source, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(versionId, lastUpdated, source
          , profile, security, tag);
      }


}

