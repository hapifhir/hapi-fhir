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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Record details about an anatomical structure.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
 */
@ResourceDef(name="BodyStructure", profile="http://hl7.org/fhir/Profile/BodyStructure")
public class BodyStructure extends DomainResource {

    /**
     * Identifier for this instance of the anatomical structure.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Bodystructure identifier", formalDefinition="Identifier for this instance of the anatomical structure." )
    protected List<Identifier> identifier;

    /**
     * Whether this body site is in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="Whether this record is in active use", formalDefinition="Whether this body site is in active use." )
    protected BooleanType active;

    /**
     * The kind of structure being represented by the body structure at `BodyStructure.location`.  This can define both normal and abnormal morphologies.
     */
    @Child(name = "morphology", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Kind of Structure", formalDefinition="The kind of structure being represented by the body structure at `BodyStructure.location`.  This can define both normal and abnormal morphologies." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/bodystructure-code")
    protected CodeableConcept morphology;

    /**
     * The anatomical location or region of the specimen, lesion, or body structure.
     */
    @Child(name = "location", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Body site", formalDefinition="The anatomical location or region of the specimen, lesion, or body structure." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected CodeableConcept location;

    /**
     * Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane.
     */
    @Child(name = "locationQualifier", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Body site modifier", formalDefinition="Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/bodystructure-relative-location")
    protected List<CodeableConcept> locationQualifier;

    /**
     * A summary, charactarization or explanation of the body structure.
     */
    @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Text description", formalDefinition="A summary, charactarization or explanation of the body structure." )
    protected StringType description;

    /**
     * Image or images used to identify a location.
     */
    @Child(name = "image", type = {Attachment.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Attached images", formalDefinition="Image or images used to identify a location." )
    protected List<Attachment> image;

    /**
     * The person to which the body site belongs.
     */
    @Child(name = "patient", type = {Patient.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this is about", formalDefinition="The person to which the body site belongs." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person to which the body site belongs.)
     */
    protected Patient patientTarget;

    private static final long serialVersionUID = 1437500387L;

  /**
   * Constructor
   */
    public BodyStructure() {
      super();
    }

  /**
   * Constructor
   */
    public BodyStructure(Reference patient) {
      super();
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (Identifier for this instance of the anatomical structure.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BodyStructure setIdentifier(List<Identifier> theIdentifier) { 
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

    public BodyStructure addIdentifier(Identifier t) { //3
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
          throw new Error("Attempt to auto-create BodyStructure.active");
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
    public BodyStructure setActiveElement(BooleanType value) { 
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
    public BodyStructure setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #morphology} (The kind of structure being represented by the body structure at `BodyStructure.location`.  This can define both normal and abnormal morphologies.)
     */
    public CodeableConcept getMorphology() { 
      if (this.morphology == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodyStructure.morphology");
        else if (Configuration.doAutoCreate())
          this.morphology = new CodeableConcept(); // cc
      return this.morphology;
    }

    public boolean hasMorphology() { 
      return this.morphology != null && !this.morphology.isEmpty();
    }

    /**
     * @param value {@link #morphology} (The kind of structure being represented by the body structure at `BodyStructure.location`.  This can define both normal and abnormal morphologies.)
     */
    public BodyStructure setMorphology(CodeableConcept value) { 
      this.morphology = value;
      return this;
    }

    /**
     * @return {@link #location} (The anatomical location or region of the specimen, lesion, or body structure.)
     */
    public CodeableConcept getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodyStructure.location");
        else if (Configuration.doAutoCreate())
          this.location = new CodeableConcept(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The anatomical location or region of the specimen, lesion, or body structure.)
     */
    public BodyStructure setLocation(CodeableConcept value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #locationQualifier} (Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane.)
     */
    public List<CodeableConcept> getLocationQualifier() { 
      if (this.locationQualifier == null)
        this.locationQualifier = new ArrayList<CodeableConcept>();
      return this.locationQualifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public BodyStructure setLocationQualifier(List<CodeableConcept> theLocationQualifier) { 
      this.locationQualifier = theLocationQualifier;
      return this;
    }

    public boolean hasLocationQualifier() { 
      if (this.locationQualifier == null)
        return false;
      for (CodeableConcept item : this.locationQualifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addLocationQualifier() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.locationQualifier == null)
        this.locationQualifier = new ArrayList<CodeableConcept>();
      this.locationQualifier.add(t);
      return t;
    }

    public BodyStructure addLocationQualifier(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.locationQualifier == null)
        this.locationQualifier = new ArrayList<CodeableConcept>();
      this.locationQualifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #locationQualifier}, creating it if it does not already exist
     */
    public CodeableConcept getLocationQualifierFirstRep() { 
      if (getLocationQualifier().isEmpty()) {
        addLocationQualifier();
      }
      return getLocationQualifier().get(0);
    }

    /**
     * @return {@link #description} (A summary, charactarization or explanation of the body structure.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodyStructure.description");
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
     * @param value {@link #description} (A summary, charactarization or explanation of the body structure.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public BodyStructure setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A summary, charactarization or explanation of the body structure.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A summary, charactarization or explanation of the body structure.
     */
    public BodyStructure setDescription(String value) { 
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
    public BodyStructure setImage(List<Attachment> theImage) { 
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

    public BodyStructure addImage(Attachment t) { //3
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
          throw new Error("Attempt to auto-create BodyStructure.patient");
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
    public BodyStructure setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person to which the body site belongs.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodyStructure.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person to which the body site belongs.)
     */
    public BodyStructure setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Identifier for this instance of the anatomical structure.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("active", "boolean", "Whether this body site is in active use.", 0, 1, active));
        children.add(new Property("morphology", "CodeableConcept", "The kind of structure being represented by the body structure at `BodyStructure.location`.  This can define both normal and abnormal morphologies.", 0, 1, morphology));
        children.add(new Property("location", "CodeableConcept", "The anatomical location or region of the specimen, lesion, or body structure.", 0, 1, location));
        children.add(new Property("locationQualifier", "CodeableConcept", "Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane.", 0, java.lang.Integer.MAX_VALUE, locationQualifier));
        children.add(new Property("description", "string", "A summary, charactarization or explanation of the body structure.", 0, 1, description));
        children.add(new Property("image", "Attachment", "Image or images used to identify a location.", 0, java.lang.Integer.MAX_VALUE, image));
        children.add(new Property("patient", "Reference(Patient)", "The person to which the body site belongs.", 0, 1, patient));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifier for this instance of the anatomical structure.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -1422950650: /*active*/  return new Property("active", "boolean", "Whether this body site is in active use.", 0, 1, active);
        case 1807231644: /*morphology*/  return new Property("morphology", "CodeableConcept", "The kind of structure being represented by the body structure at `BodyStructure.location`.  This can define both normal and abnormal morphologies.", 0, 1, morphology);
        case 1901043637: /*location*/  return new Property("location", "CodeableConcept", "The anatomical location or region of the specimen, lesion, or body structure.", 0, 1, location);
        case 433081461: /*locationQualifier*/  return new Property("locationQualifier", "CodeableConcept", "Qualifier to refine the anatomical location.  These include qualifiers for laterality, relative location, directionality, number, and plane.", 0, java.lang.Integer.MAX_VALUE, locationQualifier);
        case -1724546052: /*description*/  return new Property("description", "string", "A summary, charactarization or explanation of the body structure.", 0, 1, description);
        case 100313435: /*image*/  return new Property("image", "Attachment", "Image or images used to identify a location.", 0, java.lang.Integer.MAX_VALUE, image);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "The person to which the body site belongs.", 0, 1, patient);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1422950650: /*active*/ return this.active == null ? new Base[0] : new Base[] {this.active}; // BooleanType
        case 1807231644: /*morphology*/ return this.morphology == null ? new Base[0] : new Base[] {this.morphology}; // CodeableConcept
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // CodeableConcept
        case 433081461: /*locationQualifier*/ return this.locationQualifier == null ? new Base[0] : this.locationQualifier.toArray(new Base[this.locationQualifier.size()]); // CodeableConcept
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
        case 1807231644: // morphology
          this.morphology = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1901043637: // location
          this.location = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 433081461: // locationQualifier
          this.getLocationQualifier().add(castToCodeableConcept(value)); // CodeableConcept
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
        } else if (name.equals("morphology")) {
          this.morphology = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("location")) {
          this.location = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("locationQualifier")) {
          this.getLocationQualifier().add(castToCodeableConcept(value));
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
        case 1807231644:  return getMorphology(); 
        case 1901043637:  return getLocation(); 
        case 433081461:  return addLocationQualifier(); 
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
        case 1807231644: /*morphology*/ return new String[] {"CodeableConcept"};
        case 1901043637: /*location*/ return new String[] {"CodeableConcept"};
        case 433081461: /*locationQualifier*/ return new String[] {"CodeableConcept"};
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
          throw new FHIRException("Cannot call addChild on a primitive type BodyStructure.active");
        }
        else if (name.equals("morphology")) {
          this.morphology = new CodeableConcept();
          return this.morphology;
        }
        else if (name.equals("location")) {
          this.location = new CodeableConcept();
          return this.location;
        }
        else if (name.equals("locationQualifier")) {
          return addLocationQualifier();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type BodyStructure.description");
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
    return "BodyStructure";

  }

      public BodyStructure copy() {
        BodyStructure dst = new BodyStructure();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.active = active == null ? null : active.copy();
        dst.morphology = morphology == null ? null : morphology.copy();
        dst.location = location == null ? null : location.copy();
        if (locationQualifier != null) {
          dst.locationQualifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : locationQualifier)
            dst.locationQualifier.add(i.copy());
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

      protected BodyStructure typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof BodyStructure))
          return false;
        BodyStructure o = (BodyStructure) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(active, o.active, true) && compareDeep(morphology, o.morphology, true)
           && compareDeep(location, o.location, true) && compareDeep(locationQualifier, o.locationQualifier, true)
           && compareDeep(description, o.description, true) && compareDeep(image, o.image, true) && compareDeep(patient, o.patient, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof BodyStructure))
          return false;
        BodyStructure o = (BodyStructure) other_;
        return compareValues(active, o.active, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, active, morphology
          , location, locationQualifier, description, image, patient);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.BodyStructure;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Bodystructure identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodyStructure.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="BodyStructure.identifier", description="Bodystructure identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Bodystructure identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodyStructure.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>morphology</b>
   * <p>
   * Description: <b>Kind of Structure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodyStructure.morphology</b><br>
   * </p>
   */
  @SearchParamDefinition(name="morphology", path="BodyStructure.morphology", description="Kind of Structure", type="token" )
  public static final String SP_MORPHOLOGY = "morphology";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>morphology</b>
   * <p>
   * Description: <b>Kind of Structure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodyStructure.morphology</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MORPHOLOGY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MORPHOLOGY);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who this is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>BodyStructure.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="BodyStructure.patient", description="Who this is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who this is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>BodyStructure.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>BodyStructure:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("BodyStructure:patient").toLocked();

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>Body site</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodyStructure.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="BodyStructure.location", description="Body site", type="token" )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>Body site</b><br>
   * Type: <b>token</b><br>
   * Path: <b>BodyStructure.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam LOCATION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_LOCATION);


}

