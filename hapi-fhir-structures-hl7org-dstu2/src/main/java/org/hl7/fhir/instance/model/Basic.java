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

import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Basic is used for handling concepts not yet defined in FHIR, narrative-only resources that don't map to an existing resource, and custom resources not appropriate for inclusion in the FHIR specification.
 */
@ResourceDef(name="Basic", profile="http://hl7.org/fhir/Profile/Basic")
public class Basic extends DomainResource {

    /**
     * Identifier assigned to the resource for business purposes, outside the context of FHIR.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Identifier assigned to the resource for business purposes, outside the context of FHIR." )
    protected List<Identifier> identifier;

    /**
     * Identifies the 'type' of resource - equivalent to the resource name for other resources.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Kind of Resource", formalDefinition="Identifies the 'type' of resource - equivalent to the resource name for other resources." )
    protected CodeableConcept code;

    /**
     * Identifies the patient, practitioner, device or any other resource that is the "focus" of this resoruce.
     */
    @Child(name = "subject", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifies the focus of this resource", formalDefinition="Identifies the patient, practitioner, device or any other resource that is the \"focus\" of this resoruce." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient, practitioner, device or any other resource that is the "focus" of this resoruce.)
     */
    protected Resource subjectTarget;

    /**
     * Indicates who was responsible for creating the resource instance.
     */
    @Child(name = "author", type = {Practitioner.class, Patient.class, RelatedPerson.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who created", formalDefinition="Indicates who was responsible for creating the resource instance." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Indicates who was responsible for creating the resource instance.)
     */
    protected Resource authorTarget;

    /**
     * Identifies when the resource was first created.
     */
    @Child(name = "created", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When created", formalDefinition="Identifies when the resource was first created." )
    protected DateType created;

    private static final long serialVersionUID = 916539354L;

  /*
   * Constructor
   */
    public Basic() {
      super();
    }

  /*
   * Constructor
   */
    public Basic(CodeableConcept code) {
      super();
      this.code = code;
    }

    /**
     * @return {@link #identifier} (Identifier assigned to the resource for business purposes, outside the context of FHIR.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Identifier assigned to the resource for business purposes, outside the context of FHIR.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public Basic addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #code} (Identifies the 'type' of resource - equivalent to the resource name for other resources.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Basic.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Identifies the 'type' of resource - equivalent to the resource name for other resources.)
     */
    public Basic setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (Identifies the patient, practitioner, device or any other resource that is the "focus" of this resoruce.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Basic.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Identifies the patient, practitioner, device or any other resource that is the "focus" of this resoruce.)
     */
    public Basic setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient, practitioner, device or any other resource that is the "focus" of this resoruce.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient, practitioner, device or any other resource that is the "focus" of this resoruce.)
     */
    public Basic setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (Indicates who was responsible for creating the resource instance.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Basic.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Indicates who was responsible for creating the resource instance.)
     */
    public Basic setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates who was responsible for creating the resource instance.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates who was responsible for creating the resource instance.)
     */
    public Basic setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (Identifies when the resource was first created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Basic.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateType(); // bb
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (Identifies when the resource was first created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public Basic setCreatedElement(DateType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return Identifies when the resource was first created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value Identifies when the resource was first created.
     */
    public Basic setCreated(Date value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new DateType();
        this.created.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier assigned to the resource for business purposes, outside the context of FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("code", "CodeableConcept", "Identifies the 'type' of resource - equivalent to the resource name for other resources.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Any)", "Identifies the patient, practitioner, device or any other resource that is the \"focus\" of this resoruce.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("author", "Reference(Practitioner|Patient|RelatedPerson)", "Indicates who was responsible for creating the resource instance.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("created", "date", "Identifies when the resource was first created.", 0, java.lang.Integer.MAX_VALUE, created));
      }

      public Basic copy() {
        Basic dst = new Basic();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.author = author == null ? null : author.copy();
        dst.created = created == null ? null : created.copy();
        return dst;
      }

      protected Basic typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Basic))
          return false;
        Basic o = (Basic) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true)
           && compareDeep(author, o.author, true) && compareDeep(created, o.created, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Basic))
          return false;
        Basic o = (Basic) other;
        return compareValues(created, o.created, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (code == null || code.isEmpty())
           && (subject == null || subject.isEmpty()) && (author == null || author.isEmpty()) && (created == null || created.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Basic;
   }

  @SearchParamDefinition(name="code", path="Basic.code", description="Kind of Resource", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="subject", path="Basic.subject", description="Identifies the focus of this resource", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="created", path="Basic.created", description="When created", type="date" )
  public static final String SP_CREATED = "created";
  @SearchParamDefinition(name="patient", path="Basic.subject", description="Identifies the focus of this resource", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="author", path="Basic.author", description="Who created", type="reference" )
  public static final String SP_AUTHOR = "author";

}

