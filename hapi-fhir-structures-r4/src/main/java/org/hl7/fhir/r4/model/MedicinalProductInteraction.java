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
 * The interactions of the medicinal product with other medicinal products, or other forms of interactions.
 */
@ResourceDef(name="MedicinalProductInteraction", profile="http://hl7.org/fhir/Profile/MedicinalProductInteraction")
public class MedicinalProductInteraction extends DomainResource {

    /**
     * The medication for which this is an indication.
     */
    @Child(name = "subject", type = {MedicinalProduct.class, Medication.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The medication for which this is an indication", formalDefinition="The medication for which this is an indication." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (The medication for which this is an indication.)
     */
    protected List<Resource> subjectTarget;


    /**
     * The interaction described.
     */
    @Child(name = "interaction", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The interaction described", formalDefinition="The interaction described." )
    protected StringType interaction;

    /**
     * The specific medication, food or laboratory test that interacts.
     */
    @Child(name = "interactant", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The specific medication, food or laboratory test that interacts", formalDefinition="The specific medication, food or laboratory test that interacts." )
    protected List<CodeableConcept> interactant;

    /**
     * The type of the interaction.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of the interaction", formalDefinition="The type of the interaction." )
    protected CodeableConcept type;

    /**
     * The effect of the interaction.
     */
    @Child(name = "effect", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The effect of the interaction", formalDefinition="The effect of the interaction." )
    protected CodeableConcept effect;

    /**
     * The incidence of the interaction.
     */
    @Child(name = "incidence", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The incidence of the interaction", formalDefinition="The incidence of the interaction." )
    protected CodeableConcept incidence;

    /**
     * Actions for managing the interaction.
     */
    @Child(name = "management", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Actions for managing the interaction", formalDefinition="Actions for managing the interaction." )
    protected CodeableConcept management;

    private static final long serialVersionUID = 1175278617L;

  /**
   * Constructor
   */
    public MedicinalProductInteraction() {
      super();
    }

    /**
     * @return {@link #subject} (The medication for which this is an indication.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductInteraction setSubject(List<Reference> theSubject) { 
      this.subject = theSubject;
      return this;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    public MedicinalProductInteraction addSubject(Reference t) { //3
      if (t == null)
        return this;
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subject}, creating it if it does not already exist
     */
    public Reference getSubjectFirstRep() { 
      if (getSubject().isEmpty()) {
        addSubject();
      }
      return getSubject().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<Resource>();
      return this.subjectTarget;
    }

    /**
     * @return {@link #interaction} (The interaction described.). This is the underlying object with id, value and extensions. The accessor "getInteraction" gives direct access to the value
     */
    public StringType getInteractionElement() { 
      if (this.interaction == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductInteraction.interaction");
        else if (Configuration.doAutoCreate())
          this.interaction = new StringType(); // bb
      return this.interaction;
    }

    public boolean hasInteractionElement() { 
      return this.interaction != null && !this.interaction.isEmpty();
    }

    public boolean hasInteraction() { 
      return this.interaction != null && !this.interaction.isEmpty();
    }

    /**
     * @param value {@link #interaction} (The interaction described.). This is the underlying object with id, value and extensions. The accessor "getInteraction" gives direct access to the value
     */
    public MedicinalProductInteraction setInteractionElement(StringType value) { 
      this.interaction = value;
      return this;
    }

    /**
     * @return The interaction described.
     */
    public String getInteraction() { 
      return this.interaction == null ? null : this.interaction.getValue();
    }

    /**
     * @param value The interaction described.
     */
    public MedicinalProductInteraction setInteraction(String value) { 
      if (Utilities.noString(value))
        this.interaction = null;
      else {
        if (this.interaction == null)
          this.interaction = new StringType();
        this.interaction.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #interactant} (The specific medication, food or laboratory test that interacts.)
     */
    public List<CodeableConcept> getInteractant() { 
      if (this.interactant == null)
        this.interactant = new ArrayList<CodeableConcept>();
      return this.interactant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicinalProductInteraction setInteractant(List<CodeableConcept> theInteractant) { 
      this.interactant = theInteractant;
      return this;
    }

    public boolean hasInteractant() { 
      if (this.interactant == null)
        return false;
      for (CodeableConcept item : this.interactant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addInteractant() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.interactant == null)
        this.interactant = new ArrayList<CodeableConcept>();
      this.interactant.add(t);
      return t;
    }

    public MedicinalProductInteraction addInteractant(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.interactant == null)
        this.interactant = new ArrayList<CodeableConcept>();
      this.interactant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #interactant}, creating it if it does not already exist
     */
    public CodeableConcept getInteractantFirstRep() { 
      if (getInteractant().isEmpty()) {
        addInteractant();
      }
      return getInteractant().get(0);
    }

    /**
     * @return {@link #type} (The type of the interaction.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductInteraction.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of the interaction.)
     */
    public MedicinalProductInteraction setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #effect} (The effect of the interaction.)
     */
    public CodeableConcept getEffect() { 
      if (this.effect == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductInteraction.effect");
        else if (Configuration.doAutoCreate())
          this.effect = new CodeableConcept(); // cc
      return this.effect;
    }

    public boolean hasEffect() { 
      return this.effect != null && !this.effect.isEmpty();
    }

    /**
     * @param value {@link #effect} (The effect of the interaction.)
     */
    public MedicinalProductInteraction setEffect(CodeableConcept value) { 
      this.effect = value;
      return this;
    }

    /**
     * @return {@link #incidence} (The incidence of the interaction.)
     */
    public CodeableConcept getIncidence() { 
      if (this.incidence == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductInteraction.incidence");
        else if (Configuration.doAutoCreate())
          this.incidence = new CodeableConcept(); // cc
      return this.incidence;
    }

    public boolean hasIncidence() { 
      return this.incidence != null && !this.incidence.isEmpty();
    }

    /**
     * @param value {@link #incidence} (The incidence of the interaction.)
     */
    public MedicinalProductInteraction setIncidence(CodeableConcept value) { 
      this.incidence = value;
      return this;
    }

    /**
     * @return {@link #management} (Actions for managing the interaction.)
     */
    public CodeableConcept getManagement() { 
      if (this.management == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicinalProductInteraction.management");
        else if (Configuration.doAutoCreate())
          this.management = new CodeableConcept(); // cc
      return this.management;
    }

    public boolean hasManagement() { 
      return this.management != null && !this.management.isEmpty();
    }

    /**
     * @param value {@link #management} (Actions for managing the interaction.)
     */
    public MedicinalProductInteraction setManagement(CodeableConcept value) { 
      this.management = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("subject", "Reference(MedicinalProduct|Medication)", "The medication for which this is an indication.", 0, java.lang.Integer.MAX_VALUE, subject));
        children.add(new Property("interaction", "string", "The interaction described.", 0, 1, interaction));
        children.add(new Property("interactant", "CodeableConcept", "The specific medication, food or laboratory test that interacts.", 0, java.lang.Integer.MAX_VALUE, interactant));
        children.add(new Property("type", "CodeableConcept", "The type of the interaction.", 0, 1, type));
        children.add(new Property("effect", "CodeableConcept", "The effect of the interaction.", 0, 1, effect));
        children.add(new Property("incidence", "CodeableConcept", "The incidence of the interaction.", 0, 1, incidence));
        children.add(new Property("management", "CodeableConcept", "Actions for managing the interaction.", 0, 1, management));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1867885268: /*subject*/  return new Property("subject", "Reference(MedicinalProduct|Medication)", "The medication for which this is an indication.", 0, java.lang.Integer.MAX_VALUE, subject);
        case 1844104722: /*interaction*/  return new Property("interaction", "string", "The interaction described.", 0, 1, interaction);
        case 1844097009: /*interactant*/  return new Property("interactant", "CodeableConcept", "The specific medication, food or laboratory test that interacts.", 0, java.lang.Integer.MAX_VALUE, interactant);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of the interaction.", 0, 1, type);
        case -1306084975: /*effect*/  return new Property("effect", "CodeableConcept", "The effect of the interaction.", 0, 1, effect);
        case -1598467132: /*incidence*/  return new Property("incidence", "CodeableConcept", "The incidence of the interaction.", 0, 1, incidence);
        case -1799980989: /*management*/  return new Property("management", "CodeableConcept", "Actions for managing the interaction.", 0, 1, management);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // Reference
        case 1844104722: /*interaction*/ return this.interaction == null ? new Base[0] : new Base[] {this.interaction}; // StringType
        case 1844097009: /*interactant*/ return this.interactant == null ? new Base[0] : this.interactant.toArray(new Base[this.interactant.size()]); // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1306084975: /*effect*/ return this.effect == null ? new Base[0] : new Base[] {this.effect}; // CodeableConcept
        case -1598467132: /*incidence*/ return this.incidence == null ? new Base[0] : new Base[] {this.incidence}; // CodeableConcept
        case -1799980989: /*management*/ return this.management == null ? new Base[0] : new Base[] {this.management}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1867885268: // subject
          this.getSubject().add(castToReference(value)); // Reference
          return value;
        case 1844104722: // interaction
          this.interaction = castToString(value); // StringType
          return value;
        case 1844097009: // interactant
          this.getInteractant().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1306084975: // effect
          this.effect = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1598467132: // incidence
          this.incidence = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1799980989: // management
          this.management = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("subject")) {
          this.getSubject().add(castToReference(value));
        } else if (name.equals("interaction")) {
          this.interaction = castToString(value); // StringType
        } else if (name.equals("interactant")) {
          this.getInteractant().add(castToCodeableConcept(value));
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("effect")) {
          this.effect = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("incidence")) {
          this.incidence = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("management")) {
          this.management = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867885268:  return addSubject(); 
        case 1844104722:  return getInteractionElement();
        case 1844097009:  return addInteractant(); 
        case 3575610:  return getType(); 
        case -1306084975:  return getEffect(); 
        case -1598467132:  return getIncidence(); 
        case -1799980989:  return getManagement(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1844104722: /*interaction*/ return new String[] {"string"};
        case 1844097009: /*interactant*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1306084975: /*effect*/ return new String[] {"CodeableConcept"};
        case -1598467132: /*incidence*/ return new String[] {"CodeableConcept"};
        case -1799980989: /*management*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("interaction")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicinalProductInteraction.interaction");
        }
        else if (name.equals("interactant")) {
          return addInteractant();
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("effect")) {
          this.effect = new CodeableConcept();
          return this.effect;
        }
        else if (name.equals("incidence")) {
          this.incidence = new CodeableConcept();
          return this.incidence;
        }
        else if (name.equals("management")) {
          this.management = new CodeableConcept();
          return this.management;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicinalProductInteraction";

  }

      public MedicinalProductInteraction copy() {
        MedicinalProductInteraction dst = new MedicinalProductInteraction();
        copyValues(dst);
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        dst.interaction = interaction == null ? null : interaction.copy();
        if (interactant != null) {
          dst.interactant = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : interactant)
            dst.interactant.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.effect = effect == null ? null : effect.copy();
        dst.incidence = incidence == null ? null : incidence.copy();
        dst.management = management == null ? null : management.copy();
        return dst;
      }

      protected MedicinalProductInteraction typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MedicinalProductInteraction))
          return false;
        MedicinalProductInteraction o = (MedicinalProductInteraction) other_;
        return compareDeep(subject, o.subject, true) && compareDeep(interaction, o.interaction, true) && compareDeep(interactant, o.interactant, true)
           && compareDeep(type, o.type, true) && compareDeep(effect, o.effect, true) && compareDeep(incidence, o.incidence, true)
           && compareDeep(management, o.management, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MedicinalProductInteraction))
          return false;
        MedicinalProductInteraction o = (MedicinalProductInteraction) other_;
        return compareValues(interaction, o.interaction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(subject, interaction, interactant
          , type, effect, incidence, management);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicinalProductInteraction;
   }

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an interaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductInteraction.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MedicinalProductInteraction.subject", description="The medication for which this is an interaction", type="reference", target={Medication.class, MedicinalProduct.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The medication for which this is an interaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicinalProductInteraction.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicinalProductInteraction:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MedicinalProductInteraction:subject").toLocked();


}

