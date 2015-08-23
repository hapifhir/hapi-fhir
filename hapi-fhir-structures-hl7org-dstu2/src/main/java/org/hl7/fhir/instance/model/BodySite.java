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
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
 */
@ResourceDef(name="BodySite", profile="http://hl7.org/fhir/Profile/BodySite")
public class BodySite extends DomainResource {

    /**
     * The person to which the body site belongs.
     */
    @Child(name = "patient", type = {Patient.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient", formalDefinition="The person to which the body site belongs." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person to which the body site belongs.)
     */
    protected Patient patientTarget;

    /**
     * Identifier for this instance of the anatomical location.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Bodysite identifier", formalDefinition="Identifier for this instance of the anatomical location." )
    protected List<Identifier> identifier;

    /**
     * Named anatomical location - ideally would be coded where possible.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Named anatomical location", formalDefinition="Named anatomical location - ideally would be coded where possible." )
    protected CodeableConcept code;

    /**
     * Modifier to refine the anatomical location.  These include modifiers for laterality, relative location, directionality, number, and plane.
     */
    @Child(name = "modifier", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Modification to location code", formalDefinition="Modifier to refine the anatomical location.  These include modifiers for laterality, relative location, directionality, number, and plane." )
    protected List<CodeableConcept> modifier;

    /**
     * Description of anatomical location.
     */
    @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The Description of anatomical location", formalDefinition="Description of anatomical location." )
    protected StringType description;

    /**
     * Image or images used to identify a location.
     */
    @Child(name = "image", type = {Attachment.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Attached images", formalDefinition="Image or images used to identify a location." )
    protected List<Attachment> image;

    private static final long serialVersionUID = 1568109920L;

  /*
   * Constructor
   */
    public BodySite() {
      super();
    }

  /*
   * Constructor
   */
    public BodySite(Reference patient) {
      super();
      this.patient = patient;
    }

    /**
     * @return {@link #patient} (The person to which the body site belongs.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodySite.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The person to which the body site belongs.)
     */
    public BodySite setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person to which the body site belongs.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodySite.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person to which the body site belongs.)
     */
    public BodySite setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Identifier for this instance of the anatomical location.)
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
     * @return {@link #identifier} (Identifier for this instance of the anatomical location.)
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
    public BodySite addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #code} (Named anatomical location - ideally would be coded where possible.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodySite.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Named anatomical location - ideally would be coded where possible.)
     */
    public BodySite setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #modifier} (Modifier to refine the anatomical location.  These include modifiers for laterality, relative location, directionality, number, and plane.)
     */
    public List<CodeableConcept> getModifier() { 
      if (this.modifier == null)
        this.modifier = new ArrayList<CodeableConcept>();
      return this.modifier;
    }

    public boolean hasModifier() { 
      if (this.modifier == null)
        return false;
      for (CodeableConcept item : this.modifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #modifier} (Modifier to refine the anatomical location.  These include modifiers for laterality, relative location, directionality, number, and plane.)
     */
    // syntactic sugar
    public CodeableConcept addModifier() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.modifier == null)
        this.modifier = new ArrayList<CodeableConcept>();
      this.modifier.add(t);
      return t;
    }

    // syntactic sugar
    public BodySite addModifier(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.modifier == null)
        this.modifier = new ArrayList<CodeableConcept>();
      this.modifier.add(t);
      return this;
    }

    /**
     * @return {@link #description} (Description of anatomical location.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodySite.description");
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
     * @param value {@link #description} (Description of anatomical location.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public BodySite setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Description of anatomical location.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Description of anatomical location.
     */
    public BodySite setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #image} (Image or images used to identify a location.)
     */
    public List<Attachment> getImage() { 
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      return this.image;
    }

    public boolean hasImage() { 
      if (this.image == null)
        return false;
      for (Attachment item : this.image)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #image} (Image or images used to identify a location.)
     */
    // syntactic sugar
    public Attachment addImage() { //3
      Attachment t = new Attachment();
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return t;
    }

    // syntactic sugar
    public BodySite addImage(Attachment t) { //3
      if (t == null)
        return this;
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("patient", "Reference(Patient)", "The person to which the body site belongs.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("identifier", "Identifier", "Identifier for this instance of the anatomical location.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("code", "CodeableConcept", "Named anatomical location - ideally would be coded where possible.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("modifier", "CodeableConcept", "Modifier to refine the anatomical location.  These include modifiers for laterality, relative location, directionality, number, and plane.", 0, java.lang.Integer.MAX_VALUE, modifier));
        childrenList.add(new Property("description", "string", "Description of anatomical location.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("image", "Attachment", "Image or images used to identify a location.", 0, java.lang.Integer.MAX_VALUE, image));
      }

      public BodySite copy() {
        BodySite dst = new BodySite();
        copyValues(dst);
        dst.patient = patient == null ? null : patient.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (image != null) {
          dst.image = new ArrayList<Attachment>();
          for (Attachment i : image)
            dst.image.add(i.copy());
        };
        return dst;
      }

      protected BodySite typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BodySite))
          return false;
        BodySite o = (BodySite) other;
        return compareDeep(patient, o.patient, true) && compareDeep(identifier, o.identifier, true) && compareDeep(code, o.code, true)
           && compareDeep(modifier, o.modifier, true) && compareDeep(description, o.description, true) && compareDeep(image, o.image, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BodySite))
          return false;
        BodySite o = (BodySite) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (patient == null || patient.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (code == null || code.isEmpty()) && (modifier == null || modifier.isEmpty()) && (description == null || description.isEmpty())
           && (image == null || image.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.BodySite;
   }

  @SearchParamDefinition(name="code", path="BodySite.code", description="Named anatomical location", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="patient", path="BodySite.patient", description="Patient to whom bodysite belongs", type="reference" )
  public static final String SP_PATIENT = "patient";

}

