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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Record details about the anatomical location of a specimen or body part.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
 */
@ResourceDef(name="BodySite", profile="http://hl7.org/fhir/Profile/BodySite")
public class BodySite extends DomainResource {

    /**
     * Identifier for this instance of the anatomical location.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Bodysite identifier", formalDefinition="Identifier for this instance of the anatomical location." )
    protected List<Identifier> identifier;

    /**
     * Whether this body site is in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Whether this body site record is in active use", formalDefinition="Whether this body site is in active use." )
    protected BooleanType active;

    /**
     * Named anatomical location - ideally coded where possible.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Named anatomical location", formalDefinition="Named anatomical location - ideally coded where possible." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected CodeableConcept code;

    /**
     * Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane.
     */
    @Child(name = "qualifier", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Modification to location code", formalDefinition="Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/bodysite-relative-location")
    protected List<CodeableConcept> qualifier;

    /**
     * A summary, charactarization or explanation of the anatomic location.
     */
    @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Anatomical location description", formalDefinition="A summary, charactarization or explanation of the anatomic location." )
    protected StringType description;

    /**
     * Image or images used to identify a location.
     */
    @Child(name = "image", type = {Attachment.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Attached images", formalDefinition="Image or images used to identify a location." )
    protected List<Attachment> image;

    /**
     * The person to which the body site belongs.
     */
    @Child(name = "patient", type = {Patient.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this is about", formalDefinition="The person to which the body site belongs." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person to which the body site belongs.)
     */
    protected Patient patientTarget;

    private static final long serialVersionUID = -871837171L;

  /**
   * Constructor
   */
    public BodySite() {
      super();
    }

  /**
   * Constructor
   */
    public BodySite(Reference patient) {
      super();
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (Identifier for this instance of the anatomical location.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BodySite setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public BodySite addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #active} (Whether this body site is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodySite.active");
        else if (Configuration.doAutoCreate())
          this.active = new BooleanType(); // bb
      return this.active;
    }

    public boolean hasActiveElement() { 
      return this.active != null && !this.active.isEmpty();
    }

    public boolean hasActive() { 
      return this.active != null && !this.active.isEmpty();
    }

    /**
     * @param value {@link #active} (Whether this body site is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BodySite setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Whether this body site is in active use.
     */
    public boolean getActive() { 
      return this.active == null || this.active.isEmpty() ? false : this.active.getValue();
    }

    /**
     * @param value Whether this body site is in active use.
     */
    public BodySite setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #code} (Named anatomical location - ideally coded where possible.)
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
     * @param value {@link #code} (Named anatomical location - ideally coded where possible.)
     */
    public BodySite setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #qualifier} (Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane.)
     */
    public List<CodeableConcept> getQualifier() { 
      if (this.qualifier == null)
        this.qualifier = new ArrayList<CodeableConcept>();
      return this.qualifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BodySite setQualifier(List<CodeableConcept> theQualifier) { 
      this.qualifier = theQualifier;
      return this;
    }

    public boolean hasQualifier() { 
      if (this.qualifier == null)
        return false;
      for (CodeableConcept item : this.qualifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addQualifier() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.qualifier == null)
        this.qualifier = new ArrayList<CodeableConcept>();
      this.qualifier.add(t);
      return t;
    }

    public BodySite addQualifier(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.qualifier == null)
        this.qualifier = new ArrayList<CodeableConcept>();
      this.qualifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #qualifier}, creating it if it does not already exist
     */
    public CodeableConcept getQualifierFirstRep() { 
      if (getQualifier().isEmpty()) {
        addQualifier();
      }
      return getQualifier().get(0);
    }

    /**
     * @return {@link #description} (A summary, charactarization or explanation of the anatomic location.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
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
     * @param value {@link #description} (A summary, charactarization or explanation of the anatomic location.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public BodySite setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A summary, charactarization or explanation of the anatomic location.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A summary, charactarization or explanation of the anatomic location.
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BodySite setImage(List<Attachment> theImage) { 
      this.image = theImage;
      return this;
    }

    public boolean hasImage() { 
      if (this.image == null)
        return false;
      for (Attachment item : this.image)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Attachment addImage() { //3
      Attachment t = new Attachment();
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return t;
    }

    public BodySite addImage(Attachment t) { //3
      if (t == null)
        return this;
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #image}, creating it if it does not already exist
     */
    public Attachment getImageFirstRep() { 
      if (getImage().isEmpty()) {
        addImage();
      }
      return getImage().get(0);
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for this instance of the anatomical location.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("active", "boolean", "Whether this body site is in active use.", 0, java.lang.Integer.MAX_VALUE, active));
        childrenList.add(new Property("code", "CodeableConcept", "Named anatomical location - ideally coded where possible.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("qualifier", "CodeableConcept", "Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane.", 0, java.lang.Integer.MAX_VALUE, qualifier));
        childrenList.add(new Property("description", "string", "A summary, charactarization or explanation of the anatomic location.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("image", "Attachment", "Image or images used to identify a location.", 0, java.lang.Integer.MAX_VALUE, image));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person to which the body site belongs.", 0, java.lang.Integer.MAX_VALUE, patient));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1422950650: /*active*/ return this.active == null ? new Base[0] : new Base[] {this.active}; // BooleanType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1247940438: /*qualifier*/ return this.qualifier == null ? new Base[0] : this.qualifier.toArray(new Base[this.qualifier.size()]); // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 100313435: /*image*/ return this.image == null ? new Base[0] : this.image.toArray(new Base[this.image.size()]); // Attachment
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1422950650: // active
          this.active = castToBoolean(value); // BooleanType
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1247940438: // qualifier
          this.getQualifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 100313435: // image
          this.getImage().add(castToAttachment(value)); // Attachment
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("active")) {
          this.active = castToBoolean(value); // BooleanType
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("qualifier")) {
          this.getQualifier().add(castToCodeableConcept(value));
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("image")) {
          this.getImage().add(castToAttachment(value));
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1422950650:  return getActiveElement();
        case 3059181:  return getCode(); 
        case -1247940438:  return addQualifier(); 
        case -1724546052:  return getDescriptionElement();
        case 100313435:  return addImage(); 
        case -791418107:  return getPatient(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1422950650: /*active*/ return new String[] {"boolean"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1247940438: /*qualifier*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 100313435: /*image*/ return new String[] {"Attachment"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("active")) {
          throw new FHIRException("Cannot call addChild on a primitive type BodySite.active");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("qualifier")) {
          return addQualifier();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type BodySite.description");
        }
        else if (name.equals("image")) {
          return addImage();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "BodySite";

  }

      public BodySite copy() {
        BodySite dst = new BodySite();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.active = active == null ? null : active.copy();
        dst.code = code == null ? null : code.copy();
        if (qualifier != null) {
          dst.qualifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : qualifier)
            dst.qualifier.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (image != null) {
          dst.image = new ArrayList<Attachment>();
          for (Attachment i : image)
            dst.image.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(active, o.active, true) && compareDeep(code, o.code, true)
           && compareDeep(qualifier, o.qualifier, true) && compareDeep(description, o.description, true) && compareDeep(image, o.image, true)
           && compareDeep(patient, o.patient, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BodySite))
          return false;
        BodySite o = (BodySite) other;
        return compareValues(active, o.active, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, active, code
          , qualifier, description, image, patient);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.BodySite;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifier for this instance of the anatomical location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodySite.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="BodySite.identifier", description="Identifier for this instance of the anatomical location", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifier for this instance of the anatomical location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodySite.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Named anatomical location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodySite.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="BodySite.code", description="Named anatomical location", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Named anatomical location</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodySite.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient to whom bodysite belongs</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>BodySite.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="BodySite.patient", description="Patient to whom bodysite belongs", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Patient to whom bodysite belongs</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>BodySite.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>BodySite:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("BodySite:patient").toLocked();


}

