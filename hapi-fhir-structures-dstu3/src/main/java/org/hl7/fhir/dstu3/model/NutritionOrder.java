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
 * A request to supply a diet, formula feeding (enteral) or oral nutritional supplement to a patient/resident.
 */
@ResourceDef(name="NutritionOrder", profile="http://hl7.org/fhir/Profile/NutritionOrder")
public class NutritionOrder extends DomainResource {

    public enum NutritionOrderStatus {
        /**
         * The request has been proposed.
         */
        PROPOSED, 
        /**
         * The request is in preliminary form prior to being sent.
         */
        DRAFT, 
        /**
         * The request has been planned.
         */
        PLANNED, 
        /**
         * The request has been placed.
         */
        REQUESTED, 
        /**
         * The request is 'actionable', but not all actions that are implied by it have occurred yet.
         */
        ACTIVE, 
        /**
         * Actions implied by the request have been temporarily halted, but are expected to continue later. May also be called "suspended".
         */
        ONHOLD, 
        /**
         * All actions that are implied by the order have occurred and no continuation is planned (this will rarely be made explicit).
         */
        COMPLETED, 
        /**
         * The request has been withdrawn and is no longer actionable.
         */
        CANCELLED, 
        /**
         * The request was entered in error and voided.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static NutritionOrderStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown NutritionOrderStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case DRAFT: return "draft";
            case PLANNED: return "planned";
            case REQUESTED: return "requested";
            case ACTIVE: return "active";
            case ONHOLD: return "on-hold";
            case COMPLETED: return "completed";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/nutrition-request-status";
            case DRAFT: return "http://hl7.org/fhir/nutrition-request-status";
            case PLANNED: return "http://hl7.org/fhir/nutrition-request-status";
            case REQUESTED: return "http://hl7.org/fhir/nutrition-request-status";
            case ACTIVE: return "http://hl7.org/fhir/nutrition-request-status";
            case ONHOLD: return "http://hl7.org/fhir/nutrition-request-status";
            case COMPLETED: return "http://hl7.org/fhir/nutrition-request-status";
            case CANCELLED: return "http://hl7.org/fhir/nutrition-request-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/nutrition-request-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "The request has been proposed.";
            case DRAFT: return "The request is in preliminary form prior to being sent.";
            case PLANNED: return "The request has been planned.";
            case REQUESTED: return "The request has been placed.";
            case ACTIVE: return "The request is 'actionable', but not all actions that are implied by it have occurred yet.";
            case ONHOLD: return "Actions implied by the request have been temporarily halted, but are expected to continue later. May also be called \"suspended\".";
            case COMPLETED: return "All actions that are implied by the order have occurred and no continuation is planned (this will rarely be made explicit).";
            case CANCELLED: return "The request has been withdrawn and is no longer actionable.";
            case ENTEREDINERROR: return "The request was entered in error and voided.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case DRAFT: return "Draft";
            case PLANNED: return "Planned";
            case REQUESTED: return "Requested";
            case ACTIVE: return "Active";
            case ONHOLD: return "On-Hold";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class NutritionOrderStatusEnumFactory implements EnumFactory<NutritionOrderStatus> {
    public NutritionOrderStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return NutritionOrderStatus.PROPOSED;
        if ("draft".equals(codeString))
          return NutritionOrderStatus.DRAFT;
        if ("planned".equals(codeString))
          return NutritionOrderStatus.PLANNED;
        if ("requested".equals(codeString))
          return NutritionOrderStatus.REQUESTED;
        if ("active".equals(codeString))
          return NutritionOrderStatus.ACTIVE;
        if ("on-hold".equals(codeString))
          return NutritionOrderStatus.ONHOLD;
        if ("completed".equals(codeString))
          return NutritionOrderStatus.COMPLETED;
        if ("cancelled".equals(codeString))
          return NutritionOrderStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return NutritionOrderStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown NutritionOrderStatus code '"+codeString+"'");
        }
        public Enumeration<NutritionOrderStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<NutritionOrderStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.PROPOSED);
        if ("draft".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.DRAFT);
        if ("planned".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.PLANNED);
        if ("requested".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.REQUESTED);
        if ("active".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.ACTIVE);
        if ("on-hold".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.ONHOLD);
        if ("completed".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.COMPLETED);
        if ("cancelled".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<NutritionOrderStatus>(this, NutritionOrderStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown NutritionOrderStatus code '"+codeString+"'");
        }
    public String toCode(NutritionOrderStatus code) {
      if (code == NutritionOrderStatus.PROPOSED)
        return "proposed";
      if (code == NutritionOrderStatus.DRAFT)
        return "draft";
      if (code == NutritionOrderStatus.PLANNED)
        return "planned";
      if (code == NutritionOrderStatus.REQUESTED)
        return "requested";
      if (code == NutritionOrderStatus.ACTIVE)
        return "active";
      if (code == NutritionOrderStatus.ONHOLD)
        return "on-hold";
      if (code == NutritionOrderStatus.COMPLETED)
        return "completed";
      if (code == NutritionOrderStatus.CANCELLED)
        return "cancelled";
      if (code == NutritionOrderStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(NutritionOrderStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class NutritionOrderOralDietComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of diet or dietary restriction such as fiber restricted diet or diabetic diet.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Type of oral diet or diet restrictions that describe what can be consumed orally", formalDefinition="The kind of diet or dietary restriction such as fiber restricted diet or diabetic diet." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/diet-type")
        protected List<CodeableConcept> type;

        /**
         * The time period and frequency at which the diet should be given.  The diet should be given for the combination of all schedules if more than one schedule is present.
         */
        @Child(name = "schedule", type = {Timing.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Scheduled frequency of diet", formalDefinition="The time period and frequency at which the diet should be given.  The diet should be given for the combination of all schedules if more than one schedule is present." )
        protected List<Timing> schedule;

        /**
         * Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) required for the oral diet.
         */
        @Child(name = "nutrient", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Required  nutrient modifications", formalDefinition="Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) required for the oral diet." )
        protected List<NutritionOrderOralDietNutrientComponent> nutrient;

        /**
         * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
         */
        @Child(name = "texture", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Required  texture modifications", formalDefinition="Class that describes any texture modifications required for the patient to safely consume various types of solid foods." )
        protected List<NutritionOrderOralDietTextureComponent> texture;

        /**
         * The required consistency (e.g. honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
         */
        @Child(name = "fluidConsistencyType", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The required consistency of fluids and liquids provided to the patient", formalDefinition="The required consistency (e.g. honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consistency-type")
        protected List<CodeableConcept> fluidConsistencyType;

        /**
         * Free text or additional instructions or information pertaining to the oral diet.
         */
        @Child(name = "instruction", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Instructions or additional information about the oral diet", formalDefinition="Free text or additional instructions or information pertaining to the oral diet." )
        protected StringType instruction;

        private static final long serialVersionUID = 973058412L;

    /**
     * Constructor
     */
      public NutritionOrderOralDietComponent() {
        super();
      }

        /**
         * @return {@link #type} (The kind of diet or dietary restriction such as fiber restricted diet or diabetic diet.)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public NutritionOrderOralDietComponent setType(List<CodeableConcept> theType) { 
          this.type = theType;
          return this;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

        public NutritionOrderOralDietComponent addType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
         */
        public CodeableConcept getTypeFirstRep() { 
          if (getType().isEmpty()) {
            addType();
          }
          return getType().get(0);
        }

        /**
         * @return {@link #schedule} (The time period and frequency at which the diet should be given.  The diet should be given for the combination of all schedules if more than one schedule is present.)
         */
        public List<Timing> getSchedule() { 
          if (this.schedule == null)
            this.schedule = new ArrayList<Timing>();
          return this.schedule;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public NutritionOrderOralDietComponent setSchedule(List<Timing> theSchedule) { 
          this.schedule = theSchedule;
          return this;
        }

        public boolean hasSchedule() { 
          if (this.schedule == null)
            return false;
          for (Timing item : this.schedule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Timing addSchedule() { //3
          Timing t = new Timing();
          if (this.schedule == null)
            this.schedule = new ArrayList<Timing>();
          this.schedule.add(t);
          return t;
        }

        public NutritionOrderOralDietComponent addSchedule(Timing t) { //3
          if (t == null)
            return this;
          if (this.schedule == null)
            this.schedule = new ArrayList<Timing>();
          this.schedule.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #schedule}, creating it if it does not already exist
         */
        public Timing getScheduleFirstRep() { 
          if (getSchedule().isEmpty()) {
            addSchedule();
          }
          return getSchedule().get(0);
        }

        /**
         * @return {@link #nutrient} (Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) required for the oral diet.)
         */
        public List<NutritionOrderOralDietNutrientComponent> getNutrient() { 
          if (this.nutrient == null)
            this.nutrient = new ArrayList<NutritionOrderOralDietNutrientComponent>();
          return this.nutrient;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public NutritionOrderOralDietComponent setNutrient(List<NutritionOrderOralDietNutrientComponent> theNutrient) { 
          this.nutrient = theNutrient;
          return this;
        }

        public boolean hasNutrient() { 
          if (this.nutrient == null)
            return false;
          for (NutritionOrderOralDietNutrientComponent item : this.nutrient)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public NutritionOrderOralDietNutrientComponent addNutrient() { //3
          NutritionOrderOralDietNutrientComponent t = new NutritionOrderOralDietNutrientComponent();
          if (this.nutrient == null)
            this.nutrient = new ArrayList<NutritionOrderOralDietNutrientComponent>();
          this.nutrient.add(t);
          return t;
        }

        public NutritionOrderOralDietComponent addNutrient(NutritionOrderOralDietNutrientComponent t) { //3
          if (t == null)
            return this;
          if (this.nutrient == null)
            this.nutrient = new ArrayList<NutritionOrderOralDietNutrientComponent>();
          this.nutrient.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #nutrient}, creating it if it does not already exist
         */
        public NutritionOrderOralDietNutrientComponent getNutrientFirstRep() { 
          if (getNutrient().isEmpty()) {
            addNutrient();
          }
          return getNutrient().get(0);
        }

        /**
         * @return {@link #texture} (Class that describes any texture modifications required for the patient to safely consume various types of solid foods.)
         */
        public List<NutritionOrderOralDietTextureComponent> getTexture() { 
          if (this.texture == null)
            this.texture = new ArrayList<NutritionOrderOralDietTextureComponent>();
          return this.texture;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public NutritionOrderOralDietComponent setTexture(List<NutritionOrderOralDietTextureComponent> theTexture) { 
          this.texture = theTexture;
          return this;
        }

        public boolean hasTexture() { 
          if (this.texture == null)
            return false;
          for (NutritionOrderOralDietTextureComponent item : this.texture)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public NutritionOrderOralDietTextureComponent addTexture() { //3
          NutritionOrderOralDietTextureComponent t = new NutritionOrderOralDietTextureComponent();
          if (this.texture == null)
            this.texture = new ArrayList<NutritionOrderOralDietTextureComponent>();
          this.texture.add(t);
          return t;
        }

        public NutritionOrderOralDietComponent addTexture(NutritionOrderOralDietTextureComponent t) { //3
          if (t == null)
            return this;
          if (this.texture == null)
            this.texture = new ArrayList<NutritionOrderOralDietTextureComponent>();
          this.texture.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #texture}, creating it if it does not already exist
         */
        public NutritionOrderOralDietTextureComponent getTextureFirstRep() { 
          if (getTexture().isEmpty()) {
            addTexture();
          }
          return getTexture().get(0);
        }

        /**
         * @return {@link #fluidConsistencyType} (The required consistency (e.g. honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.)
         */
        public List<CodeableConcept> getFluidConsistencyType() { 
          if (this.fluidConsistencyType == null)
            this.fluidConsistencyType = new ArrayList<CodeableConcept>();
          return this.fluidConsistencyType;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public NutritionOrderOralDietComponent setFluidConsistencyType(List<CodeableConcept> theFluidConsistencyType) { 
          this.fluidConsistencyType = theFluidConsistencyType;
          return this;
        }

        public boolean hasFluidConsistencyType() { 
          if (this.fluidConsistencyType == null)
            return false;
          for (CodeableConcept item : this.fluidConsistencyType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addFluidConsistencyType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.fluidConsistencyType == null)
            this.fluidConsistencyType = new ArrayList<CodeableConcept>();
          this.fluidConsistencyType.add(t);
          return t;
        }

        public NutritionOrderOralDietComponent addFluidConsistencyType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.fluidConsistencyType == null)
            this.fluidConsistencyType = new ArrayList<CodeableConcept>();
          this.fluidConsistencyType.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #fluidConsistencyType}, creating it if it does not already exist
         */
        public CodeableConcept getFluidConsistencyTypeFirstRep() { 
          if (getFluidConsistencyType().isEmpty()) {
            addFluidConsistencyType();
          }
          return getFluidConsistencyType().get(0);
        }

        /**
         * @return {@link #instruction} (Free text or additional instructions or information pertaining to the oral diet.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public StringType getInstructionElement() { 
          if (this.instruction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietComponent.instruction");
            else if (Configuration.doAutoCreate())
              this.instruction = new StringType(); // bb
          return this.instruction;
        }

        public boolean hasInstructionElement() { 
          return this.instruction != null && !this.instruction.isEmpty();
        }

        public boolean hasInstruction() { 
          return this.instruction != null && !this.instruction.isEmpty();
        }

        /**
         * @param value {@link #instruction} (Free text or additional instructions or information pertaining to the oral diet.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public NutritionOrderOralDietComponent setInstructionElement(StringType value) { 
          this.instruction = value;
          return this;
        }

        /**
         * @return Free text or additional instructions or information pertaining to the oral diet.
         */
        public String getInstruction() { 
          return this.instruction == null ? null : this.instruction.getValue();
        }

        /**
         * @param value Free text or additional instructions or information pertaining to the oral diet.
         */
        public NutritionOrderOralDietComponent setInstruction(String value) { 
          if (Utilities.noString(value))
            this.instruction = null;
          else {
            if (this.instruction == null)
              this.instruction = new StringType();
            this.instruction.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "The kind of diet or dietary restriction such as fiber restricted diet or diabetic diet.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("schedule", "Timing", "The time period and frequency at which the diet should be given.  The diet should be given for the combination of all schedules if more than one schedule is present.", 0, java.lang.Integer.MAX_VALUE, schedule));
          childrenList.add(new Property("nutrient", "", "Class that defines the quantity and type of nutrient modifications (for example carbohydrate, fiber or sodium) required for the oral diet.", 0, java.lang.Integer.MAX_VALUE, nutrient));
          childrenList.add(new Property("texture", "", "Class that describes any texture modifications required for the patient to safely consume various types of solid foods.", 0, java.lang.Integer.MAX_VALUE, texture));
          childrenList.add(new Property("fluidConsistencyType", "CodeableConcept", "The required consistency (e.g. honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.", 0, java.lang.Integer.MAX_VALUE, fluidConsistencyType));
          childrenList.add(new Property("instruction", "string", "Free text or additional instructions or information pertaining to the oral diet.", 0, java.lang.Integer.MAX_VALUE, instruction));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case -697920873: /*schedule*/ return this.schedule == null ? new Base[0] : this.schedule.toArray(new Base[this.schedule.size()]); // Timing
        case -1671151641: /*nutrient*/ return this.nutrient == null ? new Base[0] : this.nutrient.toArray(new Base[this.nutrient.size()]); // NutritionOrderOralDietNutrientComponent
        case -1417816805: /*texture*/ return this.texture == null ? new Base[0] : this.texture.toArray(new Base[this.texture.size()]); // NutritionOrderOralDietTextureComponent
        case -525105592: /*fluidConsistencyType*/ return this.fluidConsistencyType == null ? new Base[0] : this.fluidConsistencyType.toArray(new Base[this.fluidConsistencyType.size()]); // CodeableConcept
        case 301526158: /*instruction*/ return this.instruction == null ? new Base[0] : new Base[] {this.instruction}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -697920873: // schedule
          this.getSchedule().add(castToTiming(value)); // Timing
          return value;
        case -1671151641: // nutrient
          this.getNutrient().add((NutritionOrderOralDietNutrientComponent) value); // NutritionOrderOralDietNutrientComponent
          return value;
        case -1417816805: // texture
          this.getTexture().add((NutritionOrderOralDietTextureComponent) value); // NutritionOrderOralDietTextureComponent
          return value;
        case -525105592: // fluidConsistencyType
          this.getFluidConsistencyType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 301526158: // instruction
          this.instruction = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("schedule")) {
          this.getSchedule().add(castToTiming(value));
        } else if (name.equals("nutrient")) {
          this.getNutrient().add((NutritionOrderOralDietNutrientComponent) value);
        } else if (name.equals("texture")) {
          this.getTexture().add((NutritionOrderOralDietTextureComponent) value);
        } else if (name.equals("fluidConsistencyType")) {
          this.getFluidConsistencyType().add(castToCodeableConcept(value));
        } else if (name.equals("instruction")) {
          this.instruction = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return addType(); 
        case -697920873:  return addSchedule(); 
        case -1671151641:  return addNutrient(); 
        case -1417816805:  return addTexture(); 
        case -525105592:  return addFluidConsistencyType(); 
        case 301526158:  return getInstructionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -697920873: /*schedule*/ return new String[] {"Timing"};
        case -1671151641: /*nutrient*/ return new String[] {};
        case -1417816805: /*texture*/ return new String[] {};
        case -525105592: /*fluidConsistencyType*/ return new String[] {"CodeableConcept"};
        case 301526158: /*instruction*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("schedule")) {
          return addSchedule();
        }
        else if (name.equals("nutrient")) {
          return addNutrient();
        }
        else if (name.equals("texture")) {
          return addTexture();
        }
        else if (name.equals("fluidConsistencyType")) {
          return addFluidConsistencyType();
        }
        else if (name.equals("instruction")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.instruction");
        }
        else
          return super.addChild(name);
      }

      public NutritionOrderOralDietComponent copy() {
        NutritionOrderOralDietComponent dst = new NutritionOrderOralDietComponent();
        copyValues(dst);
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        if (schedule != null) {
          dst.schedule = new ArrayList<Timing>();
          for (Timing i : schedule)
            dst.schedule.add(i.copy());
        };
        if (nutrient != null) {
          dst.nutrient = new ArrayList<NutritionOrderOralDietNutrientComponent>();
          for (NutritionOrderOralDietNutrientComponent i : nutrient)
            dst.nutrient.add(i.copy());
        };
        if (texture != null) {
          dst.texture = new ArrayList<NutritionOrderOralDietTextureComponent>();
          for (NutritionOrderOralDietTextureComponent i : texture)
            dst.texture.add(i.copy());
        };
        if (fluidConsistencyType != null) {
          dst.fluidConsistencyType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : fluidConsistencyType)
            dst.fluidConsistencyType.add(i.copy());
        };
        dst.instruction = instruction == null ? null : instruction.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietComponent))
          return false;
        NutritionOrderOralDietComponent o = (NutritionOrderOralDietComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(schedule, o.schedule, true) && compareDeep(nutrient, o.nutrient, true)
           && compareDeep(texture, o.texture, true) && compareDeep(fluidConsistencyType, o.fluidConsistencyType, true)
           && compareDeep(instruction, o.instruction, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietComponent))
          return false;
        NutritionOrderOralDietComponent o = (NutritionOrderOralDietComponent) other;
        return compareValues(instruction, o.instruction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, schedule, nutrient
          , texture, fluidConsistencyType, instruction);
      }

  public String fhirType() {
    return "NutritionOrder.oralDiet";

  }

  }

    @Block()
    public static class NutritionOrderOralDietNutrientComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The nutrient that is being modified such as carbohydrate or sodium.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of nutrient that is being modified", formalDefinition="The nutrient that is being modified such as carbohydrate or sodium." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/nutrient-code")
        protected CodeableConcept modifier;

        /**
         * The quantity of the specified nutrient to include in diet.
         */
        @Child(name = "amount", type = {SimpleQuantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Quantity of the specified nutrient", formalDefinition="The quantity of the specified nutrient to include in diet." )
        protected SimpleQuantity amount;

        private static final long serialVersionUID = 465107295L;

    /**
     * Constructor
     */
      public NutritionOrderOralDietNutrientComponent() {
        super();
      }

        /**
         * @return {@link #modifier} (The nutrient that is being modified such as carbohydrate or sodium.)
         */
        public CodeableConcept getModifier() { 
          if (this.modifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietNutrientComponent.modifier");
            else if (Configuration.doAutoCreate())
              this.modifier = new CodeableConcept(); // cc
          return this.modifier;
        }

        public boolean hasModifier() { 
          return this.modifier != null && !this.modifier.isEmpty();
        }

        /**
         * @param value {@link #modifier} (The nutrient that is being modified such as carbohydrate or sodium.)
         */
        public NutritionOrderOralDietNutrientComponent setModifier(CodeableConcept value) { 
          this.modifier = value;
          return this;
        }

        /**
         * @return {@link #amount} (The quantity of the specified nutrient to include in diet.)
         */
        public SimpleQuantity getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietNutrientComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new SimpleQuantity(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (The quantity of the specified nutrient to include in diet.)
         */
        public NutritionOrderOralDietNutrientComponent setAmount(SimpleQuantity value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("modifier", "CodeableConcept", "The nutrient that is being modified such as carbohydrate or sodium.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("amount", "SimpleQuantity", "The quantity of the specified nutrient to include in diet.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : new Base[] {this.modifier}; // CodeableConcept
        case -1413853096: /*amount*/ return this.amount == null ? new Base[0] : new Base[] {this.amount}; // SimpleQuantity
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -615513385: // modifier
          this.modifier = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1413853096: // amount
          this.amount = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("modifier")) {
          this.modifier = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("amount")) {
          this.amount = castToSimpleQuantity(value); // SimpleQuantity
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -615513385:  return getModifier(); 
        case -1413853096:  return getAmount(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case -1413853096: /*amount*/ return new String[] {"SimpleQuantity"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("modifier")) {
          this.modifier = new CodeableConcept();
          return this.modifier;
        }
        else if (name.equals("amount")) {
          this.amount = new SimpleQuantity();
          return this.amount;
        }
        else
          return super.addChild(name);
      }

      public NutritionOrderOralDietNutrientComponent copy() {
        NutritionOrderOralDietNutrientComponent dst = new NutritionOrderOralDietNutrientComponent();
        copyValues(dst);
        dst.modifier = modifier == null ? null : modifier.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietNutrientComponent))
          return false;
        NutritionOrderOralDietNutrientComponent o = (NutritionOrderOralDietNutrientComponent) other;
        return compareDeep(modifier, o.modifier, true) && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietNutrientComponent))
          return false;
        NutritionOrderOralDietNutrientComponent o = (NutritionOrderOralDietNutrientComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(modifier, amount);
      }

  public String fhirType() {
    return "NutritionOrder.oralDiet.nutrient";

  }

  }

    @Block()
    public static class NutritionOrderOralDietTextureComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Code to indicate how to alter the texture of the foods, e.g. pureed", formalDefinition="Any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/texture-code")
        protected CodeableConcept modifier;

        /**
         * The food type(s) (e.g. meats, all foods)  that the texture modification applies to.  This could be all foods types.
         */
        @Child(name = "foodType", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Concepts that are used to identify an entity that is ingested for nutritional purposes", formalDefinition="The food type(s) (e.g. meats, all foods)  that the texture modification applies to.  This could be all foods types." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/modified-foodtype")
        protected CodeableConcept foodType;

        private static final long serialVersionUID = -56402817L;

    /**
     * Constructor
     */
      public NutritionOrderOralDietTextureComponent() {
        super();
      }

        /**
         * @return {@link #modifier} (Any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.)
         */
        public CodeableConcept getModifier() { 
          if (this.modifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietTextureComponent.modifier");
            else if (Configuration.doAutoCreate())
              this.modifier = new CodeableConcept(); // cc
          return this.modifier;
        }

        public boolean hasModifier() { 
          return this.modifier != null && !this.modifier.isEmpty();
        }

        /**
         * @param value {@link #modifier} (Any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.)
         */
        public NutritionOrderOralDietTextureComponent setModifier(CodeableConcept value) { 
          this.modifier = value;
          return this;
        }

        /**
         * @return {@link #foodType} (The food type(s) (e.g. meats, all foods)  that the texture modification applies to.  This could be all foods types.)
         */
        public CodeableConcept getFoodType() { 
          if (this.foodType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietTextureComponent.foodType");
            else if (Configuration.doAutoCreate())
              this.foodType = new CodeableConcept(); // cc
          return this.foodType;
        }

        public boolean hasFoodType() { 
          return this.foodType != null && !this.foodType.isEmpty();
        }

        /**
         * @param value {@link #foodType} (The food type(s) (e.g. meats, all foods)  that the texture modification applies to.  This could be all foods types.)
         */
        public NutritionOrderOralDietTextureComponent setFoodType(CodeableConcept value) { 
          this.foodType = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("modifier", "CodeableConcept", "Any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("foodType", "CodeableConcept", "The food type(s) (e.g. meats, all foods)  that the texture modification applies to.  This could be all foods types.", 0, java.lang.Integer.MAX_VALUE, foodType));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : new Base[] {this.modifier}; // CodeableConcept
        case 379498680: /*foodType*/ return this.foodType == null ? new Base[0] : new Base[] {this.foodType}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -615513385: // modifier
          this.modifier = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 379498680: // foodType
          this.foodType = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("modifier")) {
          this.modifier = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("foodType")) {
          this.foodType = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -615513385:  return getModifier(); 
        case 379498680:  return getFoodType(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case 379498680: /*foodType*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("modifier")) {
          this.modifier = new CodeableConcept();
          return this.modifier;
        }
        else if (name.equals("foodType")) {
          this.foodType = new CodeableConcept();
          return this.foodType;
        }
        else
          return super.addChild(name);
      }

      public NutritionOrderOralDietTextureComponent copy() {
        NutritionOrderOralDietTextureComponent dst = new NutritionOrderOralDietTextureComponent();
        copyValues(dst);
        dst.modifier = modifier == null ? null : modifier.copy();
        dst.foodType = foodType == null ? null : foodType.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietTextureComponent))
          return false;
        NutritionOrderOralDietTextureComponent o = (NutritionOrderOralDietTextureComponent) other;
        return compareDeep(modifier, o.modifier, true) && compareDeep(foodType, o.foodType, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietTextureComponent))
          return false;
        NutritionOrderOralDietTextureComponent o = (NutritionOrderOralDietTextureComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(modifier, foodType);
      }

  public String fhirType() {
    return "NutritionOrder.oralDiet.texture";

  }

  }

    @Block()
    public static class NutritionOrderSupplementComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of nutritional supplement product required such as a high protein or pediatric clear liquid supplement.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of supplement product requested", formalDefinition="The kind of nutritional supplement product required such as a high protein or pediatric clear liquid supplement." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supplement-type")
        protected CodeableConcept type;

        /**
         * The product or brand name of the nutritional supplement such as "Acme Protein Shake".
         */
        @Child(name = "productName", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Product or brand name of the nutritional supplement", formalDefinition="The product or brand name of the nutritional supplement such as \"Acme Protein Shake\"." )
        protected StringType productName;

        /**
         * The time period and frequency at which the supplement(s) should be given.  The supplement should be given for the combination of all schedules if more than one schedule is present.
         */
        @Child(name = "schedule", type = {Timing.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Scheduled frequency of supplement", formalDefinition="The time period and frequency at which the supplement(s) should be given.  The supplement should be given for the combination of all schedules if more than one schedule is present." )
        protected List<Timing> schedule;

        /**
         * The amount of the nutritional supplement to be given.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Amount of the nutritional supplement", formalDefinition="The amount of the nutritional supplement to be given." )
        protected SimpleQuantity quantity;

        /**
         * Free text or additional instructions or information pertaining to the oral supplement.
         */
        @Child(name = "instruction", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Instructions or additional information about the oral supplement", formalDefinition="Free text or additional instructions or information pertaining to the oral supplement." )
        protected StringType instruction;

        private static final long serialVersionUID = 297545236L;

    /**
     * Constructor
     */
      public NutritionOrderSupplementComponent() {
        super();
      }

        /**
         * @return {@link #type} (The kind of nutritional supplement product required such as a high protein or pediatric clear liquid supplement.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderSupplementComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The kind of nutritional supplement product required such as a high protein or pediatric clear liquid supplement.)
         */
        public NutritionOrderSupplementComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #productName} (The product or brand name of the nutritional supplement such as "Acme Protein Shake".). This is the underlying object with id, value and extensions. The accessor "getProductName" gives direct access to the value
         */
        public StringType getProductNameElement() { 
          if (this.productName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderSupplementComponent.productName");
            else if (Configuration.doAutoCreate())
              this.productName = new StringType(); // bb
          return this.productName;
        }

        public boolean hasProductNameElement() { 
          return this.productName != null && !this.productName.isEmpty();
        }

        public boolean hasProductName() { 
          return this.productName != null && !this.productName.isEmpty();
        }

        /**
         * @param value {@link #productName} (The product or brand name of the nutritional supplement such as "Acme Protein Shake".). This is the underlying object with id, value and extensions. The accessor "getProductName" gives direct access to the value
         */
        public NutritionOrderSupplementComponent setProductNameElement(StringType value) { 
          this.productName = value;
          return this;
        }

        /**
         * @return The product or brand name of the nutritional supplement such as "Acme Protein Shake".
         */
        public String getProductName() { 
          return this.productName == null ? null : this.productName.getValue();
        }

        /**
         * @param value The product or brand name of the nutritional supplement such as "Acme Protein Shake".
         */
        public NutritionOrderSupplementComponent setProductName(String value) { 
          if (Utilities.noString(value))
            this.productName = null;
          else {
            if (this.productName == null)
              this.productName = new StringType();
            this.productName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #schedule} (The time period and frequency at which the supplement(s) should be given.  The supplement should be given for the combination of all schedules if more than one schedule is present.)
         */
        public List<Timing> getSchedule() { 
          if (this.schedule == null)
            this.schedule = new ArrayList<Timing>();
          return this.schedule;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public NutritionOrderSupplementComponent setSchedule(List<Timing> theSchedule) { 
          this.schedule = theSchedule;
          return this;
        }

        public boolean hasSchedule() { 
          if (this.schedule == null)
            return false;
          for (Timing item : this.schedule)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Timing addSchedule() { //3
          Timing t = new Timing();
          if (this.schedule == null)
            this.schedule = new ArrayList<Timing>();
          this.schedule.add(t);
          return t;
        }

        public NutritionOrderSupplementComponent addSchedule(Timing t) { //3
          if (t == null)
            return this;
          if (this.schedule == null)
            this.schedule = new ArrayList<Timing>();
          this.schedule.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #schedule}, creating it if it does not already exist
         */
        public Timing getScheduleFirstRep() { 
          if (getSchedule().isEmpty()) {
            addSchedule();
          }
          return getSchedule().get(0);
        }

        /**
         * @return {@link #quantity} (The amount of the nutritional supplement to be given.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderSupplementComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of the nutritional supplement to be given.)
         */
        public NutritionOrderSupplementComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #instruction} (Free text or additional instructions or information pertaining to the oral supplement.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public StringType getInstructionElement() { 
          if (this.instruction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderSupplementComponent.instruction");
            else if (Configuration.doAutoCreate())
              this.instruction = new StringType(); // bb
          return this.instruction;
        }

        public boolean hasInstructionElement() { 
          return this.instruction != null && !this.instruction.isEmpty();
        }

        public boolean hasInstruction() { 
          return this.instruction != null && !this.instruction.isEmpty();
        }

        /**
         * @param value {@link #instruction} (Free text or additional instructions or information pertaining to the oral supplement.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public NutritionOrderSupplementComponent setInstructionElement(StringType value) { 
          this.instruction = value;
          return this;
        }

        /**
         * @return Free text or additional instructions or information pertaining to the oral supplement.
         */
        public String getInstruction() { 
          return this.instruction == null ? null : this.instruction.getValue();
        }

        /**
         * @param value Free text or additional instructions or information pertaining to the oral supplement.
         */
        public NutritionOrderSupplementComponent setInstruction(String value) { 
          if (Utilities.noString(value))
            this.instruction = null;
          else {
            if (this.instruction == null)
              this.instruction = new StringType();
            this.instruction.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "The kind of nutritional supplement product required such as a high protein or pediatric clear liquid supplement.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("productName", "string", "The product or brand name of the nutritional supplement such as \"Acme Protein Shake\".", 0, java.lang.Integer.MAX_VALUE, productName));
          childrenList.add(new Property("schedule", "Timing", "The time period and frequency at which the supplement(s) should be given.  The supplement should be given for the combination of all schedules if more than one schedule is present.", 0, java.lang.Integer.MAX_VALUE, schedule));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The amount of the nutritional supplement to be given.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("instruction", "string", "Free text or additional instructions or information pertaining to the oral supplement.", 0, java.lang.Integer.MAX_VALUE, instruction));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1491817446: /*productName*/ return this.productName == null ? new Base[0] : new Base[] {this.productName}; // StringType
        case -697920873: /*schedule*/ return this.schedule == null ? new Base[0] : this.schedule.toArray(new Base[this.schedule.size()]); // Timing
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case 301526158: /*instruction*/ return this.instruction == null ? new Base[0] : new Base[] {this.instruction}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1491817446: // productName
          this.productName = castToString(value); // StringType
          return value;
        case -697920873: // schedule
          this.getSchedule().add(castToTiming(value)); // Timing
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case 301526158: // instruction
          this.instruction = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("productName")) {
          this.productName = castToString(value); // StringType
        } else if (name.equals("schedule")) {
          this.getSchedule().add(castToTiming(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("instruction")) {
          this.instruction = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1491817446:  return getProductNameElement();
        case -697920873:  return addSchedule(); 
        case -1285004149:  return getQuantity(); 
        case 301526158:  return getInstructionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1491817446: /*productName*/ return new String[] {"string"};
        case -697920873: /*schedule*/ return new String[] {"Timing"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case 301526158: /*instruction*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("productName")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.productName");
        }
        else if (name.equals("schedule")) {
          return addSchedule();
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("instruction")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.instruction");
        }
        else
          return super.addChild(name);
      }

      public NutritionOrderSupplementComponent copy() {
        NutritionOrderSupplementComponent dst = new NutritionOrderSupplementComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.productName = productName == null ? null : productName.copy();
        if (schedule != null) {
          dst.schedule = new ArrayList<Timing>();
          for (Timing i : schedule)
            dst.schedule.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.instruction = instruction == null ? null : instruction.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderSupplementComponent))
          return false;
        NutritionOrderSupplementComponent o = (NutritionOrderSupplementComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(productName, o.productName, true) && compareDeep(schedule, o.schedule, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(instruction, o.instruction, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderSupplementComponent))
          return false;
        NutritionOrderSupplementComponent o = (NutritionOrderSupplementComponent) other;
        return compareValues(productName, o.productName, true) && compareValues(instruction, o.instruction, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, productName, schedule
          , quantity, instruction);
      }

  public String fhirType() {
    return "NutritionOrder.supplement";

  }

  }

    @Block()
    public static class NutritionOrderEnteralFormulaComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of enteral or infant formula such as an adult standard formula with fiber or a soy-based infant formula.
         */
        @Child(name = "baseFormulaType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of enteral or infant formula", formalDefinition="The type of enteral or infant formula such as an adult standard formula with fiber or a soy-based infant formula." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/entformula-type")
        protected CodeableConcept baseFormulaType;

        /**
         * The product or brand name of the enteral or infant formula product such as "ACME Adult Standard Formula".
         */
        @Child(name = "baseFormulaProductName", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Product or brand name of the enteral or infant formula", formalDefinition="The product or brand name of the enteral or infant formula product such as \"ACME Adult Standard Formula\"." )
        protected StringType baseFormulaProductName;

        /**
         * Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.
         */
        @Child(name = "additiveType", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of modular component to add to the feeding", formalDefinition="Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/entformula-additive")
        protected CodeableConcept additiveType;

        /**
         * The product or brand name of the type of modular component to be added to the formula.
         */
        @Child(name = "additiveProductName", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Product or brand name of the modular additive", formalDefinition="The product or brand name of the type of modular component to be added to the formula." )
        protected StringType additiveProductName;

        /**
         * The amount of energy (calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula that provides 24 calories per fluid ounce or an adult may require an enteral formula that provides 1.5 calorie/mL.
         */
        @Child(name = "caloricDensity", type = {SimpleQuantity.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Amount of energy per specified volume that is required", formalDefinition="The amount of energy (calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula that provides 24 calories per fluid ounce or an adult may require an enteral formula that provides 1.5 calorie/mL." )
        protected SimpleQuantity caloricDensity;

        /**
         * The route or physiological path of administration into the patient's gastrointestinal  tract for purposes of providing the formula feeding, e.g. nasogastric tube.
         */
        @Child(name = "routeofAdministration", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How the formula should enter the patient's gastrointestinal tract", formalDefinition="The route or physiological path of administration into the patient's gastrointestinal  tract for purposes of providing the formula feeding, e.g. nasogastric tube." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/enteral-route")
        protected CodeableConcept routeofAdministration;

        /**
         * Formula administration instructions as structured data.  This repeating structure allows for changing the administration rate or volume over time for both bolus and continuous feeding.  An example of this would be an instruction to increase the rate of continuous feeding every 2 hours.
         */
        @Child(name = "administration", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Formula feeding instruction as structured data", formalDefinition="Formula administration instructions as structured data.  This repeating structure allows for changing the administration rate or volume over time for both bolus and continuous feeding.  An example of this would be an instruction to increase the rate of continuous feeding every 2 hours." )
        protected List<NutritionOrderEnteralFormulaAdministrationComponent> administration;

        /**
         * The maximum total quantity of formula that may be administered to a subject over the period of time, e.g. 1440 mL over 24 hours.
         */
        @Child(name = "maxVolumeToDeliver", type = {SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Upper limit on formula volume per unit of time", formalDefinition="The maximum total quantity of formula that may be administered to a subject over the period of time, e.g. 1440 mL over 24 hours." )
        protected SimpleQuantity maxVolumeToDeliver;

        /**
         * Free text formula administration, feeding instructions or additional instructions or information.
         */
        @Child(name = "administrationInstruction", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Formula feeding instructions expressed as text", formalDefinition="Free text formula administration, feeding instructions or additional instructions or information." )
        protected StringType administrationInstruction;

        private static final long serialVersionUID = 292116061L;

    /**
     * Constructor
     */
      public NutritionOrderEnteralFormulaComponent() {
        super();
      }

        /**
         * @return {@link #baseFormulaType} (The type of enteral or infant formula such as an adult standard formula with fiber or a soy-based infant formula.)
         */
        public CodeableConcept getBaseFormulaType() { 
          if (this.baseFormulaType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.baseFormulaType");
            else if (Configuration.doAutoCreate())
              this.baseFormulaType = new CodeableConcept(); // cc
          return this.baseFormulaType;
        }

        public boolean hasBaseFormulaType() { 
          return this.baseFormulaType != null && !this.baseFormulaType.isEmpty();
        }

        /**
         * @param value {@link #baseFormulaType} (The type of enteral or infant formula such as an adult standard formula with fiber or a soy-based infant formula.)
         */
        public NutritionOrderEnteralFormulaComponent setBaseFormulaType(CodeableConcept value) { 
          this.baseFormulaType = value;
          return this;
        }

        /**
         * @return {@link #baseFormulaProductName} (The product or brand name of the enteral or infant formula product such as "ACME Adult Standard Formula".). This is the underlying object with id, value and extensions. The accessor "getBaseFormulaProductName" gives direct access to the value
         */
        public StringType getBaseFormulaProductNameElement() { 
          if (this.baseFormulaProductName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.baseFormulaProductName");
            else if (Configuration.doAutoCreate())
              this.baseFormulaProductName = new StringType(); // bb
          return this.baseFormulaProductName;
        }

        public boolean hasBaseFormulaProductNameElement() { 
          return this.baseFormulaProductName != null && !this.baseFormulaProductName.isEmpty();
        }

        public boolean hasBaseFormulaProductName() { 
          return this.baseFormulaProductName != null && !this.baseFormulaProductName.isEmpty();
        }

        /**
         * @param value {@link #baseFormulaProductName} (The product or brand name of the enteral or infant formula product such as "ACME Adult Standard Formula".). This is the underlying object with id, value and extensions. The accessor "getBaseFormulaProductName" gives direct access to the value
         */
        public NutritionOrderEnteralFormulaComponent setBaseFormulaProductNameElement(StringType value) { 
          this.baseFormulaProductName = value;
          return this;
        }

        /**
         * @return The product or brand name of the enteral or infant formula product such as "ACME Adult Standard Formula".
         */
        public String getBaseFormulaProductName() { 
          return this.baseFormulaProductName == null ? null : this.baseFormulaProductName.getValue();
        }

        /**
         * @param value The product or brand name of the enteral or infant formula product such as "ACME Adult Standard Formula".
         */
        public NutritionOrderEnteralFormulaComponent setBaseFormulaProductName(String value) { 
          if (Utilities.noString(value))
            this.baseFormulaProductName = null;
          else {
            if (this.baseFormulaProductName == null)
              this.baseFormulaProductName = new StringType();
            this.baseFormulaProductName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #additiveType} (Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.)
         */
        public CodeableConcept getAdditiveType() { 
          if (this.additiveType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.additiveType");
            else if (Configuration.doAutoCreate())
              this.additiveType = new CodeableConcept(); // cc
          return this.additiveType;
        }

        public boolean hasAdditiveType() { 
          return this.additiveType != null && !this.additiveType.isEmpty();
        }

        /**
         * @param value {@link #additiveType} (Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.)
         */
        public NutritionOrderEnteralFormulaComponent setAdditiveType(CodeableConcept value) { 
          this.additiveType = value;
          return this;
        }

        /**
         * @return {@link #additiveProductName} (The product or brand name of the type of modular component to be added to the formula.). This is the underlying object with id, value and extensions. The accessor "getAdditiveProductName" gives direct access to the value
         */
        public StringType getAdditiveProductNameElement() { 
          if (this.additiveProductName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.additiveProductName");
            else if (Configuration.doAutoCreate())
              this.additiveProductName = new StringType(); // bb
          return this.additiveProductName;
        }

        public boolean hasAdditiveProductNameElement() { 
          return this.additiveProductName != null && !this.additiveProductName.isEmpty();
        }

        public boolean hasAdditiveProductName() { 
          return this.additiveProductName != null && !this.additiveProductName.isEmpty();
        }

        /**
         * @param value {@link #additiveProductName} (The product or brand name of the type of modular component to be added to the formula.). This is the underlying object with id, value and extensions. The accessor "getAdditiveProductName" gives direct access to the value
         */
        public NutritionOrderEnteralFormulaComponent setAdditiveProductNameElement(StringType value) { 
          this.additiveProductName = value;
          return this;
        }

        /**
         * @return The product or brand name of the type of modular component to be added to the formula.
         */
        public String getAdditiveProductName() { 
          return this.additiveProductName == null ? null : this.additiveProductName.getValue();
        }

        /**
         * @param value The product or brand name of the type of modular component to be added to the formula.
         */
        public NutritionOrderEnteralFormulaComponent setAdditiveProductName(String value) { 
          if (Utilities.noString(value))
            this.additiveProductName = null;
          else {
            if (this.additiveProductName == null)
              this.additiveProductName = new StringType();
            this.additiveProductName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #caloricDensity} (The amount of energy (calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula that provides 24 calories per fluid ounce or an adult may require an enteral formula that provides 1.5 calorie/mL.)
         */
        public SimpleQuantity getCaloricDensity() { 
          if (this.caloricDensity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.caloricDensity");
            else if (Configuration.doAutoCreate())
              this.caloricDensity = new SimpleQuantity(); // cc
          return this.caloricDensity;
        }

        public boolean hasCaloricDensity() { 
          return this.caloricDensity != null && !this.caloricDensity.isEmpty();
        }

        /**
         * @param value {@link #caloricDensity} (The amount of energy (calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula that provides 24 calories per fluid ounce or an adult may require an enteral formula that provides 1.5 calorie/mL.)
         */
        public NutritionOrderEnteralFormulaComponent setCaloricDensity(SimpleQuantity value) { 
          this.caloricDensity = value;
          return this;
        }

        /**
         * @return {@link #routeofAdministration} (The route or physiological path of administration into the patient's gastrointestinal  tract for purposes of providing the formula feeding, e.g. nasogastric tube.)
         */
        public CodeableConcept getRouteofAdministration() { 
          if (this.routeofAdministration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.routeofAdministration");
            else if (Configuration.doAutoCreate())
              this.routeofAdministration = new CodeableConcept(); // cc
          return this.routeofAdministration;
        }

        public boolean hasRouteofAdministration() { 
          return this.routeofAdministration != null && !this.routeofAdministration.isEmpty();
        }

        /**
         * @param value {@link #routeofAdministration} (The route or physiological path of administration into the patient's gastrointestinal  tract for purposes of providing the formula feeding, e.g. nasogastric tube.)
         */
        public NutritionOrderEnteralFormulaComponent setRouteofAdministration(CodeableConcept value) { 
          this.routeofAdministration = value;
          return this;
        }

        /**
         * @return {@link #administration} (Formula administration instructions as structured data.  This repeating structure allows for changing the administration rate or volume over time for both bolus and continuous feeding.  An example of this would be an instruction to increase the rate of continuous feeding every 2 hours.)
         */
        public List<NutritionOrderEnteralFormulaAdministrationComponent> getAdministration() { 
          if (this.administration == null)
            this.administration = new ArrayList<NutritionOrderEnteralFormulaAdministrationComponent>();
          return this.administration;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public NutritionOrderEnteralFormulaComponent setAdministration(List<NutritionOrderEnteralFormulaAdministrationComponent> theAdministration) { 
          this.administration = theAdministration;
          return this;
        }

        public boolean hasAdministration() { 
          if (this.administration == null)
            return false;
          for (NutritionOrderEnteralFormulaAdministrationComponent item : this.administration)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public NutritionOrderEnteralFormulaAdministrationComponent addAdministration() { //3
          NutritionOrderEnteralFormulaAdministrationComponent t = new NutritionOrderEnteralFormulaAdministrationComponent();
          if (this.administration == null)
            this.administration = new ArrayList<NutritionOrderEnteralFormulaAdministrationComponent>();
          this.administration.add(t);
          return t;
        }

        public NutritionOrderEnteralFormulaComponent addAdministration(NutritionOrderEnteralFormulaAdministrationComponent t) { //3
          if (t == null)
            return this;
          if (this.administration == null)
            this.administration = new ArrayList<NutritionOrderEnteralFormulaAdministrationComponent>();
          this.administration.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #administration}, creating it if it does not already exist
         */
        public NutritionOrderEnteralFormulaAdministrationComponent getAdministrationFirstRep() { 
          if (getAdministration().isEmpty()) {
            addAdministration();
          }
          return getAdministration().get(0);
        }

        /**
         * @return {@link #maxVolumeToDeliver} (The maximum total quantity of formula that may be administered to a subject over the period of time, e.g. 1440 mL over 24 hours.)
         */
        public SimpleQuantity getMaxVolumeToDeliver() { 
          if (this.maxVolumeToDeliver == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.maxVolumeToDeliver");
            else if (Configuration.doAutoCreate())
              this.maxVolumeToDeliver = new SimpleQuantity(); // cc
          return this.maxVolumeToDeliver;
        }

        public boolean hasMaxVolumeToDeliver() { 
          return this.maxVolumeToDeliver != null && !this.maxVolumeToDeliver.isEmpty();
        }

        /**
         * @param value {@link #maxVolumeToDeliver} (The maximum total quantity of formula that may be administered to a subject over the period of time, e.g. 1440 mL over 24 hours.)
         */
        public NutritionOrderEnteralFormulaComponent setMaxVolumeToDeliver(SimpleQuantity value) { 
          this.maxVolumeToDeliver = value;
          return this;
        }

        /**
         * @return {@link #administrationInstruction} (Free text formula administration, feeding instructions or additional instructions or information.). This is the underlying object with id, value and extensions. The accessor "getAdministrationInstruction" gives direct access to the value
         */
        public StringType getAdministrationInstructionElement() { 
          if (this.administrationInstruction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.administrationInstruction");
            else if (Configuration.doAutoCreate())
              this.administrationInstruction = new StringType(); // bb
          return this.administrationInstruction;
        }

        public boolean hasAdministrationInstructionElement() { 
          return this.administrationInstruction != null && !this.administrationInstruction.isEmpty();
        }

        public boolean hasAdministrationInstruction() { 
          return this.administrationInstruction != null && !this.administrationInstruction.isEmpty();
        }

        /**
         * @param value {@link #administrationInstruction} (Free text formula administration, feeding instructions or additional instructions or information.). This is the underlying object with id, value and extensions. The accessor "getAdministrationInstruction" gives direct access to the value
         */
        public NutritionOrderEnteralFormulaComponent setAdministrationInstructionElement(StringType value) { 
          this.administrationInstruction = value;
          return this;
        }

        /**
         * @return Free text formula administration, feeding instructions or additional instructions or information.
         */
        public String getAdministrationInstruction() { 
          return this.administrationInstruction == null ? null : this.administrationInstruction.getValue();
        }

        /**
         * @param value Free text formula administration, feeding instructions or additional instructions or information.
         */
        public NutritionOrderEnteralFormulaComponent setAdministrationInstruction(String value) { 
          if (Utilities.noString(value))
            this.administrationInstruction = null;
          else {
            if (this.administrationInstruction == null)
              this.administrationInstruction = new StringType();
            this.administrationInstruction.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("baseFormulaType", "CodeableConcept", "The type of enteral or infant formula such as an adult standard formula with fiber or a soy-based infant formula.", 0, java.lang.Integer.MAX_VALUE, baseFormulaType));
          childrenList.add(new Property("baseFormulaProductName", "string", "The product or brand name of the enteral or infant formula product such as \"ACME Adult Standard Formula\".", 0, java.lang.Integer.MAX_VALUE, baseFormulaProductName));
          childrenList.add(new Property("additiveType", "CodeableConcept", "Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.", 0, java.lang.Integer.MAX_VALUE, additiveType));
          childrenList.add(new Property("additiveProductName", "string", "The product or brand name of the type of modular component to be added to the formula.", 0, java.lang.Integer.MAX_VALUE, additiveProductName));
          childrenList.add(new Property("caloricDensity", "SimpleQuantity", "The amount of energy (calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula that provides 24 calories per fluid ounce or an adult may require an enteral formula that provides 1.5 calorie/mL.", 0, java.lang.Integer.MAX_VALUE, caloricDensity));
          childrenList.add(new Property("routeofAdministration", "CodeableConcept", "The route or physiological path of administration into the patient's gastrointestinal  tract for purposes of providing the formula feeding, e.g. nasogastric tube.", 0, java.lang.Integer.MAX_VALUE, routeofAdministration));
          childrenList.add(new Property("administration", "", "Formula administration instructions as structured data.  This repeating structure allows for changing the administration rate or volume over time for both bolus and continuous feeding.  An example of this would be an instruction to increase the rate of continuous feeding every 2 hours.", 0, java.lang.Integer.MAX_VALUE, administration));
          childrenList.add(new Property("maxVolumeToDeliver", "SimpleQuantity", "The maximum total quantity of formula that may be administered to a subject over the period of time, e.g. 1440 mL over 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxVolumeToDeliver));
          childrenList.add(new Property("administrationInstruction", "string", "Free text formula administration, feeding instructions or additional instructions or information.", 0, java.lang.Integer.MAX_VALUE, administrationInstruction));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -138930641: /*baseFormulaType*/ return this.baseFormulaType == null ? new Base[0] : new Base[] {this.baseFormulaType}; // CodeableConcept
        case -1267705979: /*baseFormulaProductName*/ return this.baseFormulaProductName == null ? new Base[0] : new Base[] {this.baseFormulaProductName}; // StringType
        case -470746842: /*additiveType*/ return this.additiveType == null ? new Base[0] : new Base[] {this.additiveType}; // CodeableConcept
        case 488079534: /*additiveProductName*/ return this.additiveProductName == null ? new Base[0] : new Base[] {this.additiveProductName}; // StringType
        case 186983261: /*caloricDensity*/ return this.caloricDensity == null ? new Base[0] : new Base[] {this.caloricDensity}; // SimpleQuantity
        case -1710107042: /*routeofAdministration*/ return this.routeofAdministration == null ? new Base[0] : new Base[] {this.routeofAdministration}; // CodeableConcept
        case 1255702622: /*administration*/ return this.administration == null ? new Base[0] : this.administration.toArray(new Base[this.administration.size()]); // NutritionOrderEnteralFormulaAdministrationComponent
        case 2017924652: /*maxVolumeToDeliver*/ return this.maxVolumeToDeliver == null ? new Base[0] : new Base[] {this.maxVolumeToDeliver}; // SimpleQuantity
        case 427085136: /*administrationInstruction*/ return this.administrationInstruction == null ? new Base[0] : new Base[] {this.administrationInstruction}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -138930641: // baseFormulaType
          this.baseFormulaType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1267705979: // baseFormulaProductName
          this.baseFormulaProductName = castToString(value); // StringType
          return value;
        case -470746842: // additiveType
          this.additiveType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 488079534: // additiveProductName
          this.additiveProductName = castToString(value); // StringType
          return value;
        case 186983261: // caloricDensity
          this.caloricDensity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -1710107042: // routeofAdministration
          this.routeofAdministration = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1255702622: // administration
          this.getAdministration().add((NutritionOrderEnteralFormulaAdministrationComponent) value); // NutritionOrderEnteralFormulaAdministrationComponent
          return value;
        case 2017924652: // maxVolumeToDeliver
          this.maxVolumeToDeliver = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case 427085136: // administrationInstruction
          this.administrationInstruction = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("baseFormulaType")) {
          this.baseFormulaType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("baseFormulaProductName")) {
          this.baseFormulaProductName = castToString(value); // StringType
        } else if (name.equals("additiveType")) {
          this.additiveType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("additiveProductName")) {
          this.additiveProductName = castToString(value); // StringType
        } else if (name.equals("caloricDensity")) {
          this.caloricDensity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("routeofAdministration")) {
          this.routeofAdministration = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("administration")) {
          this.getAdministration().add((NutritionOrderEnteralFormulaAdministrationComponent) value);
        } else if (name.equals("maxVolumeToDeliver")) {
          this.maxVolumeToDeliver = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("administrationInstruction")) {
          this.administrationInstruction = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -138930641:  return getBaseFormulaType(); 
        case -1267705979:  return getBaseFormulaProductNameElement();
        case -470746842:  return getAdditiveType(); 
        case 488079534:  return getAdditiveProductNameElement();
        case 186983261:  return getCaloricDensity(); 
        case -1710107042:  return getRouteofAdministration(); 
        case 1255702622:  return addAdministration(); 
        case 2017924652:  return getMaxVolumeToDeliver(); 
        case 427085136:  return getAdministrationInstructionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -138930641: /*baseFormulaType*/ return new String[] {"CodeableConcept"};
        case -1267705979: /*baseFormulaProductName*/ return new String[] {"string"};
        case -470746842: /*additiveType*/ return new String[] {"CodeableConcept"};
        case 488079534: /*additiveProductName*/ return new String[] {"string"};
        case 186983261: /*caloricDensity*/ return new String[] {"SimpleQuantity"};
        case -1710107042: /*routeofAdministration*/ return new String[] {"CodeableConcept"};
        case 1255702622: /*administration*/ return new String[] {};
        case 2017924652: /*maxVolumeToDeliver*/ return new String[] {"SimpleQuantity"};
        case 427085136: /*administrationInstruction*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("baseFormulaType")) {
          this.baseFormulaType = new CodeableConcept();
          return this.baseFormulaType;
        }
        else if (name.equals("baseFormulaProductName")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.baseFormulaProductName");
        }
        else if (name.equals("additiveType")) {
          this.additiveType = new CodeableConcept();
          return this.additiveType;
        }
        else if (name.equals("additiveProductName")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.additiveProductName");
        }
        else if (name.equals("caloricDensity")) {
          this.caloricDensity = new SimpleQuantity();
          return this.caloricDensity;
        }
        else if (name.equals("routeofAdministration")) {
          this.routeofAdministration = new CodeableConcept();
          return this.routeofAdministration;
        }
        else if (name.equals("administration")) {
          return addAdministration();
        }
        else if (name.equals("maxVolumeToDeliver")) {
          this.maxVolumeToDeliver = new SimpleQuantity();
          return this.maxVolumeToDeliver;
        }
        else if (name.equals("administrationInstruction")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.administrationInstruction");
        }
        else
          return super.addChild(name);
      }

      public NutritionOrderEnteralFormulaComponent copy() {
        NutritionOrderEnteralFormulaComponent dst = new NutritionOrderEnteralFormulaComponent();
        copyValues(dst);
        dst.baseFormulaType = baseFormulaType == null ? null : baseFormulaType.copy();
        dst.baseFormulaProductName = baseFormulaProductName == null ? null : baseFormulaProductName.copy();
        dst.additiveType = additiveType == null ? null : additiveType.copy();
        dst.additiveProductName = additiveProductName == null ? null : additiveProductName.copy();
        dst.caloricDensity = caloricDensity == null ? null : caloricDensity.copy();
        dst.routeofAdministration = routeofAdministration == null ? null : routeofAdministration.copy();
        if (administration != null) {
          dst.administration = new ArrayList<NutritionOrderEnteralFormulaAdministrationComponent>();
          for (NutritionOrderEnteralFormulaAdministrationComponent i : administration)
            dst.administration.add(i.copy());
        };
        dst.maxVolumeToDeliver = maxVolumeToDeliver == null ? null : maxVolumeToDeliver.copy();
        dst.administrationInstruction = administrationInstruction == null ? null : administrationInstruction.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderEnteralFormulaComponent))
          return false;
        NutritionOrderEnteralFormulaComponent o = (NutritionOrderEnteralFormulaComponent) other;
        return compareDeep(baseFormulaType, o.baseFormulaType, true) && compareDeep(baseFormulaProductName, o.baseFormulaProductName, true)
           && compareDeep(additiveType, o.additiveType, true) && compareDeep(additiveProductName, o.additiveProductName, true)
           && compareDeep(caloricDensity, o.caloricDensity, true) && compareDeep(routeofAdministration, o.routeofAdministration, true)
           && compareDeep(administration, o.administration, true) && compareDeep(maxVolumeToDeliver, o.maxVolumeToDeliver, true)
           && compareDeep(administrationInstruction, o.administrationInstruction, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderEnteralFormulaComponent))
          return false;
        NutritionOrderEnteralFormulaComponent o = (NutritionOrderEnteralFormulaComponent) other;
        return compareValues(baseFormulaProductName, o.baseFormulaProductName, true) && compareValues(additiveProductName, o.additiveProductName, true)
           && compareValues(administrationInstruction, o.administrationInstruction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(baseFormulaType, baseFormulaProductName
          , additiveType, additiveProductName, caloricDensity, routeofAdministration, administration
          , maxVolumeToDeliver, administrationInstruction);
      }

  public String fhirType() {
    return "NutritionOrder.enteralFormula";

  }

  }

    @Block()
    public static class NutritionOrderEnteralFormulaAdministrationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The time period and frequency at which the enteral formula should be delivered to the patient.
         */
        @Child(name = "schedule", type = {Timing.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Scheduled frequency of enteral feeding", formalDefinition="The time period and frequency at which the enteral formula should be delivered to the patient." )
        protected Timing schedule;

        /**
         * The volume of formula to provide to the patient per the specified administration schedule.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The volume of formula to provide", formalDefinition="The volume of formula to provide to the patient per the specified administration schedule." )
        protected SimpleQuantity quantity;

        /**
         * The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.
         */
        @Child(name = "rate", type = {SimpleQuantity.class, Ratio.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Speed with which the formula is provided per period of time", formalDefinition="The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule." )
        protected Type rate;

        private static final long serialVersionUID = 1895031997L;

    /**
     * Constructor
     */
      public NutritionOrderEnteralFormulaAdministrationComponent() {
        super();
      }

        /**
         * @return {@link #schedule} (The time period and frequency at which the enteral formula should be delivered to the patient.)
         */
        public Timing getSchedule() { 
          if (this.schedule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaAdministrationComponent.schedule");
            else if (Configuration.doAutoCreate())
              this.schedule = new Timing(); // cc
          return this.schedule;
        }

        public boolean hasSchedule() { 
          return this.schedule != null && !this.schedule.isEmpty();
        }

        /**
         * @param value {@link #schedule} (The time period and frequency at which the enteral formula should be delivered to the patient.)
         */
        public NutritionOrderEnteralFormulaAdministrationComponent setSchedule(Timing value) { 
          this.schedule = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The volume of formula to provide to the patient per the specified administration schedule.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaAdministrationComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The volume of formula to provide to the patient per the specified administration schedule.)
         */
        public NutritionOrderEnteralFormulaAdministrationComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #rate} (The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.)
         */
        public Type getRate() { 
          return this.rate;
        }

        /**
         * @return {@link #rate} (The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.)
         */
        public SimpleQuantity getRateSimpleQuantity() throws FHIRException { 
          if (!(this.rate instanceof SimpleQuantity))
            throw new FHIRException("Type mismatch: the type SimpleQuantity was expected, but "+this.rate.getClass().getName()+" was encountered");
          return (SimpleQuantity) this.rate;
        }

        public boolean hasRateSimpleQuantity() { 
          return this.rate instanceof SimpleQuantity;
        }

        /**
         * @return {@link #rate} (The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.)
         */
        public Ratio getRateRatio() throws FHIRException { 
          if (!(this.rate instanceof Ratio))
            throw new FHIRException("Type mismatch: the type Ratio was expected, but "+this.rate.getClass().getName()+" was encountered");
          return (Ratio) this.rate;
        }

        public boolean hasRateRatio() { 
          return this.rate instanceof Ratio;
        }

        public boolean hasRate() { 
          return this.rate != null && !this.rate.isEmpty();
        }

        /**
         * @param value {@link #rate} (The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.)
         */
        public NutritionOrderEnteralFormulaAdministrationComponent setRate(Type value) { 
          this.rate = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("schedule", "Timing", "The time period and frequency at which the enteral formula should be delivered to the patient.", 0, java.lang.Integer.MAX_VALUE, schedule));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The volume of formula to provide to the patient per the specified administration schedule.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("rate[x]", "SimpleQuantity|Ratio", "The rate of administration of formula via a feeding pump, e.g. 60 mL per hour, according to the specified schedule.", 0, java.lang.Integer.MAX_VALUE, rate));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -697920873: /*schedule*/ return this.schedule == null ? new Base[0] : new Base[] {this.schedule}; // Timing
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case 3493088: /*rate*/ return this.rate == null ? new Base[0] : new Base[] {this.rate}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -697920873: // schedule
          this.schedule = castToTiming(value); // Timing
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case 3493088: // rate
          this.rate = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("schedule")) {
          this.schedule = castToTiming(value); // Timing
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("rate[x]")) {
          this.rate = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -697920873:  return getSchedule(); 
        case -1285004149:  return getQuantity(); 
        case 983460768:  return getRate(); 
        case 3493088:  return getRate(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -697920873: /*schedule*/ return new String[] {"Timing"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case 3493088: /*rate*/ return new String[] {"SimpleQuantity", "Ratio"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("schedule")) {
          this.schedule = new Timing();
          return this.schedule;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("rateSimpleQuantity")) {
          this.rate = new SimpleQuantity();
          return this.rate;
        }
        else if (name.equals("rateRatio")) {
          this.rate = new Ratio();
          return this.rate;
        }
        else
          return super.addChild(name);
      }

      public NutritionOrderEnteralFormulaAdministrationComponent copy() {
        NutritionOrderEnteralFormulaAdministrationComponent dst = new NutritionOrderEnteralFormulaAdministrationComponent();
        copyValues(dst);
        dst.schedule = schedule == null ? null : schedule.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.rate = rate == null ? null : rate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderEnteralFormulaAdministrationComponent))
          return false;
        NutritionOrderEnteralFormulaAdministrationComponent o = (NutritionOrderEnteralFormulaAdministrationComponent) other;
        return compareDeep(schedule, o.schedule, true) && compareDeep(quantity, o.quantity, true) && compareDeep(rate, o.rate, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderEnteralFormulaAdministrationComponent))
          return false;
        NutritionOrderEnteralFormulaAdministrationComponent o = (NutritionOrderEnteralFormulaAdministrationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(schedule, quantity, rate
          );
      }

  public String fhirType() {
    return "NutritionOrder.enteralFormula.administration";

  }

  }

    /**
     * Identifiers assigned to this order by the order sender or by the order receiver.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Identifiers assigned to this order", formalDefinition="Identifiers assigned to this order by the order sender or by the order receiver." )
    protected List<Identifier> identifier;

    /**
     * The workflow status of the nutrition order/request.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | draft | planned | requested | active | on-hold | completed | cancelled | entered-in-error", formalDefinition="The workflow status of the nutrition order/request." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/nutrition-request-status")
    protected Enumeration<NutritionOrderStatus> status;

    /**
     * The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.
     */
    @Child(name = "patient", type = {Patient.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The person who requires the diet, formula or nutritional supplement", formalDefinition="The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    protected Patient patientTarget;

    /**
     * An encounter that provides additional information about the healthcare context in which this request is made.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The encounter associated with this nutrition order", formalDefinition="An encounter that provides additional information about the healthcare context in which this request is made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    protected Encounter encounterTarget;

    /**
     * The date and time that this nutrition order was requested.
     */
    @Child(name = "dateTime", type = {DateTimeType.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date and time the nutrition order was requested", formalDefinition="The date and time that this nutrition order was requested." )
    protected DateTimeType dateTime;

    /**
     * The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.
     */
    @Child(name = "orderer", type = {Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who ordered the diet, formula or nutritional supplement", formalDefinition="The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings." )
    protected Reference orderer;

    /**
     * The actual object that is the target of the reference (The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.)
     */
    protected Practitioner ordererTarget;

    /**
     * A link to a record of allergies or intolerances  which should be included in the nutrition order.
     */
    @Child(name = "allergyIntolerance", type = {AllergyIntolerance.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of the patient's food and nutrition-related allergies and intolerances", formalDefinition="A link to a record of allergies or intolerances  which should be included in the nutrition order." )
    protected List<Reference> allergyIntolerance;
    /**
     * The actual objects that are the target of the reference (A link to a record of allergies or intolerances  which should be included in the nutrition order.)
     */
    protected List<AllergyIntolerance> allergyIntoleranceTarget;


    /**
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     */
    @Child(name = "foodPreferenceModifier", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Order-specific modifier about the type of food that should be given", formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-diet")
    protected List<CodeableConcept> foodPreferenceModifier;

    /**
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free.  While it should not be necessary to repeat allergy or intolerance information captured in the referenced AllergyIntolerance resource in the excludeFoodModifier, this element may be used to convey additional specificity related to foods that should be eliminated from the patient’s diet for any reason.  This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     */
    @Child(name = "excludeFoodModifier", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Order-specific modifier about the type of food that should not be given", formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free.  While it should not be necessary to repeat allergy or intolerance information captured in the referenced AllergyIntolerance resource in the excludeFoodModifier, this element may be used to convey additional specificity related to foods that should be eliminated from the patient’s diet for any reason.  This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/food-type")
    protected List<CodeableConcept> excludeFoodModifier;

    /**
     * Diet given orally in contrast to enteral (tube) feeding.
     */
    @Child(name = "oralDiet", type = {}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Oral diet components", formalDefinition="Diet given orally in contrast to enteral (tube) feeding." )
    protected NutritionOrderOralDietComponent oralDiet;

    /**
     * Oral nutritional products given in order to add further nutritional value to the patient's diet.
     */
    @Child(name = "supplement", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Supplement components", formalDefinition="Oral nutritional products given in order to add further nutritional value to the patient's diet." )
    protected List<NutritionOrderSupplementComponent> supplement;

    /**
     * Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to the oral cavity.
     */
    @Child(name = "enteralFormula", type = {}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Enteral formula components", formalDefinition="Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to the oral cavity." )
    protected NutritionOrderEnteralFormulaComponent enteralFormula;

    private static final long serialVersionUID = 1429947433L;

  /**
   * Constructor
   */
    public NutritionOrder() {
      super();
    }

  /**
   * Constructor
   */
    public NutritionOrder(Reference patient, DateTimeType dateTime) {
      super();
      this.patient = patient;
      this.dateTime = dateTime;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this order by the order sender or by the order receiver.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NutritionOrder setIdentifier(List<Identifier> theIdentifier) { 
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

    public NutritionOrder addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The workflow status of the nutrition order/request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<NutritionOrderStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<NutritionOrderStatus>(new NutritionOrderStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The workflow status of the nutrition order/request.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public NutritionOrder setStatusElement(Enumeration<NutritionOrderStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The workflow status of the nutrition order/request.
     */
    public NutritionOrderStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The workflow status of the nutrition order/request.
     */
    public NutritionOrder setStatus(NutritionOrderStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<NutritionOrderStatus>(new NutritionOrderStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public NutritionOrder setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public NutritionOrder setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public NutritionOrder setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    public NutritionOrder setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #dateTime} (The date and time that this nutrition order was requested.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public DateTimeType getDateTimeElement() { 
      if (this.dateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.dateTime");
        else if (Configuration.doAutoCreate())
          this.dateTime = new DateTimeType(); // bb
      return this.dateTime;
    }

    public boolean hasDateTimeElement() { 
      return this.dateTime != null && !this.dateTime.isEmpty();
    }

    public boolean hasDateTime() { 
      return this.dateTime != null && !this.dateTime.isEmpty();
    }

    /**
     * @param value {@link #dateTime} (The date and time that this nutrition order was requested.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public NutritionOrder setDateTimeElement(DateTimeType value) { 
      this.dateTime = value;
      return this;
    }

    /**
     * @return The date and time that this nutrition order was requested.
     */
    public Date getDateTime() { 
      return this.dateTime == null ? null : this.dateTime.getValue();
    }

    /**
     * @param value The date and time that this nutrition order was requested.
     */
    public NutritionOrder setDateTime(Date value) { 
        if (this.dateTime == null)
          this.dateTime = new DateTimeType();
        this.dateTime.setValue(value);
      return this;
    }

    /**
     * @return {@link #orderer} (The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.)
     */
    public Reference getOrderer() { 
      if (this.orderer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.orderer");
        else if (Configuration.doAutoCreate())
          this.orderer = new Reference(); // cc
      return this.orderer;
    }

    public boolean hasOrderer() { 
      return this.orderer != null && !this.orderer.isEmpty();
    }

    /**
     * @param value {@link #orderer} (The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.)
     */
    public NutritionOrder setOrderer(Reference value) { 
      this.orderer = value;
      return this;
    }

    /**
     * @return {@link #orderer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.)
     */
    public Practitioner getOrdererTarget() { 
      if (this.ordererTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.orderer");
        else if (Configuration.doAutoCreate())
          this.ordererTarget = new Practitioner(); // aa
      return this.ordererTarget;
    }

    /**
     * @param value {@link #orderer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.)
     */
    public NutritionOrder setOrdererTarget(Practitioner value) { 
      this.ordererTarget = value;
      return this;
    }

    /**
     * @return {@link #allergyIntolerance} (A link to a record of allergies or intolerances  which should be included in the nutrition order.)
     */
    public List<Reference> getAllergyIntolerance() { 
      if (this.allergyIntolerance == null)
        this.allergyIntolerance = new ArrayList<Reference>();
      return this.allergyIntolerance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NutritionOrder setAllergyIntolerance(List<Reference> theAllergyIntolerance) { 
      this.allergyIntolerance = theAllergyIntolerance;
      return this;
    }

    public boolean hasAllergyIntolerance() { 
      if (this.allergyIntolerance == null)
        return false;
      for (Reference item : this.allergyIntolerance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAllergyIntolerance() { //3
      Reference t = new Reference();
      if (this.allergyIntolerance == null)
        this.allergyIntolerance = new ArrayList<Reference>();
      this.allergyIntolerance.add(t);
      return t;
    }

    public NutritionOrder addAllergyIntolerance(Reference t) { //3
      if (t == null)
        return this;
      if (this.allergyIntolerance == null)
        this.allergyIntolerance = new ArrayList<Reference>();
      this.allergyIntolerance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #allergyIntolerance}, creating it if it does not already exist
     */
    public Reference getAllergyIntoleranceFirstRep() { 
      if (getAllergyIntolerance().isEmpty()) {
        addAllergyIntolerance();
      }
      return getAllergyIntolerance().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<AllergyIntolerance> getAllergyIntoleranceTarget() { 
      if (this.allergyIntoleranceTarget == null)
        this.allergyIntoleranceTarget = new ArrayList<AllergyIntolerance>();
      return this.allergyIntoleranceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public AllergyIntolerance addAllergyIntoleranceTarget() { 
      AllergyIntolerance r = new AllergyIntolerance();
      if (this.allergyIntoleranceTarget == null)
        this.allergyIntoleranceTarget = new ArrayList<AllergyIntolerance>();
      this.allergyIntoleranceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #foodPreferenceModifier} (This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.)
     */
    public List<CodeableConcept> getFoodPreferenceModifier() { 
      if (this.foodPreferenceModifier == null)
        this.foodPreferenceModifier = new ArrayList<CodeableConcept>();
      return this.foodPreferenceModifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NutritionOrder setFoodPreferenceModifier(List<CodeableConcept> theFoodPreferenceModifier) { 
      this.foodPreferenceModifier = theFoodPreferenceModifier;
      return this;
    }

    public boolean hasFoodPreferenceModifier() { 
      if (this.foodPreferenceModifier == null)
        return false;
      for (CodeableConcept item : this.foodPreferenceModifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addFoodPreferenceModifier() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.foodPreferenceModifier == null)
        this.foodPreferenceModifier = new ArrayList<CodeableConcept>();
      this.foodPreferenceModifier.add(t);
      return t;
    }

    public NutritionOrder addFoodPreferenceModifier(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.foodPreferenceModifier == null)
        this.foodPreferenceModifier = new ArrayList<CodeableConcept>();
      this.foodPreferenceModifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #foodPreferenceModifier}, creating it if it does not already exist
     */
    public CodeableConcept getFoodPreferenceModifierFirstRep() { 
      if (getFoodPreferenceModifier().isEmpty()) {
        addFoodPreferenceModifier();
      }
      return getFoodPreferenceModifier().get(0);
    }

    /**
     * @return {@link #excludeFoodModifier} (This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free.  While it should not be necessary to repeat allergy or intolerance information captured in the referenced AllergyIntolerance resource in the excludeFoodModifier, this element may be used to convey additional specificity related to foods that should be eliminated from the patient’s diet for any reason.  This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.)
     */
    public List<CodeableConcept> getExcludeFoodModifier() { 
      if (this.excludeFoodModifier == null)
        this.excludeFoodModifier = new ArrayList<CodeableConcept>();
      return this.excludeFoodModifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NutritionOrder setExcludeFoodModifier(List<CodeableConcept> theExcludeFoodModifier) { 
      this.excludeFoodModifier = theExcludeFoodModifier;
      return this;
    }

    public boolean hasExcludeFoodModifier() { 
      if (this.excludeFoodModifier == null)
        return false;
      for (CodeableConcept item : this.excludeFoodModifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addExcludeFoodModifier() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.excludeFoodModifier == null)
        this.excludeFoodModifier = new ArrayList<CodeableConcept>();
      this.excludeFoodModifier.add(t);
      return t;
    }

    public NutritionOrder addExcludeFoodModifier(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.excludeFoodModifier == null)
        this.excludeFoodModifier = new ArrayList<CodeableConcept>();
      this.excludeFoodModifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #excludeFoodModifier}, creating it if it does not already exist
     */
    public CodeableConcept getExcludeFoodModifierFirstRep() { 
      if (getExcludeFoodModifier().isEmpty()) {
        addExcludeFoodModifier();
      }
      return getExcludeFoodModifier().get(0);
    }

    /**
     * @return {@link #oralDiet} (Diet given orally in contrast to enteral (tube) feeding.)
     */
    public NutritionOrderOralDietComponent getOralDiet() { 
      if (this.oralDiet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.oralDiet");
        else if (Configuration.doAutoCreate())
          this.oralDiet = new NutritionOrderOralDietComponent(); // cc
      return this.oralDiet;
    }

    public boolean hasOralDiet() { 
      return this.oralDiet != null && !this.oralDiet.isEmpty();
    }

    /**
     * @param value {@link #oralDiet} (Diet given orally in contrast to enteral (tube) feeding.)
     */
    public NutritionOrder setOralDiet(NutritionOrderOralDietComponent value) { 
      this.oralDiet = value;
      return this;
    }

    /**
     * @return {@link #supplement} (Oral nutritional products given in order to add further nutritional value to the patient's diet.)
     */
    public List<NutritionOrderSupplementComponent> getSupplement() { 
      if (this.supplement == null)
        this.supplement = new ArrayList<NutritionOrderSupplementComponent>();
      return this.supplement;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public NutritionOrder setSupplement(List<NutritionOrderSupplementComponent> theSupplement) { 
      this.supplement = theSupplement;
      return this;
    }

    public boolean hasSupplement() { 
      if (this.supplement == null)
        return false;
      for (NutritionOrderSupplementComponent item : this.supplement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public NutritionOrderSupplementComponent addSupplement() { //3
      NutritionOrderSupplementComponent t = new NutritionOrderSupplementComponent();
      if (this.supplement == null)
        this.supplement = new ArrayList<NutritionOrderSupplementComponent>();
      this.supplement.add(t);
      return t;
    }

    public NutritionOrder addSupplement(NutritionOrderSupplementComponent t) { //3
      if (t == null)
        return this;
      if (this.supplement == null)
        this.supplement = new ArrayList<NutritionOrderSupplementComponent>();
      this.supplement.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supplement}, creating it if it does not already exist
     */
    public NutritionOrderSupplementComponent getSupplementFirstRep() { 
      if (getSupplement().isEmpty()) {
        addSupplement();
      }
      return getSupplement().get(0);
    }

    /**
     * @return {@link #enteralFormula} (Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to the oral cavity.)
     */
    public NutritionOrderEnteralFormulaComponent getEnteralFormula() { 
      if (this.enteralFormula == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.enteralFormula");
        else if (Configuration.doAutoCreate())
          this.enteralFormula = new NutritionOrderEnteralFormulaComponent(); // cc
      return this.enteralFormula;
    }

    public boolean hasEnteralFormula() { 
      return this.enteralFormula != null && !this.enteralFormula.isEmpty();
    }

    /**
     * @param value {@link #enteralFormula} (Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to the oral cavity.)
     */
    public NutritionOrder setEnteralFormula(NutritionOrderEnteralFormulaComponent value) { 
      this.enteralFormula = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the order sender or by the order receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The workflow status of the nutrition order/request.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "An encounter that provides additional information about the healthcare context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("dateTime", "dateTime", "The date and time that this nutrition order was requested.", 0, java.lang.Integer.MAX_VALUE, dateTime));
        childrenList.add(new Property("orderer", "Reference(Practitioner)", "The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.", 0, java.lang.Integer.MAX_VALUE, orderer));
        childrenList.add(new Property("allergyIntolerance", "Reference(AllergyIntolerance)", "A link to a record of allergies or intolerances  which should be included in the nutrition order.", 0, java.lang.Integer.MAX_VALUE, allergyIntolerance));
        childrenList.add(new Property("foodPreferenceModifier", "CodeableConcept", "This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.", 0, java.lang.Integer.MAX_VALUE, foodPreferenceModifier));
        childrenList.add(new Property("excludeFoodModifier", "CodeableConcept", "This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free.  While it should not be necessary to repeat allergy or intolerance information captured in the referenced AllergyIntolerance resource in the excludeFoodModifier, this element may be used to convey additional specificity related to foods that should be eliminated from the patient’s diet for any reason.  This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.", 0, java.lang.Integer.MAX_VALUE, excludeFoodModifier));
        childrenList.add(new Property("oralDiet", "", "Diet given orally in contrast to enteral (tube) feeding.", 0, java.lang.Integer.MAX_VALUE, oralDiet));
        childrenList.add(new Property("supplement", "", "Oral nutritional products given in order to add further nutritional value to the patient's diet.", 0, java.lang.Integer.MAX_VALUE, supplement));
        childrenList.add(new Property("enteralFormula", "", "Feeding provided through the gastrointestinal tract via a tube, catheter, or stoma that delivers nutrition distal to the oral cavity.", 0, java.lang.Integer.MAX_VALUE, enteralFormula));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<NutritionOrderStatus>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case 1792749467: /*dateTime*/ return this.dateTime == null ? new Base[0] : new Base[] {this.dateTime}; // DateTimeType
        case -1207109509: /*orderer*/ return this.orderer == null ? new Base[0] : new Base[] {this.orderer}; // Reference
        case -120164120: /*allergyIntolerance*/ return this.allergyIntolerance == null ? new Base[0] : this.allergyIntolerance.toArray(new Base[this.allergyIntolerance.size()]); // Reference
        case 659473872: /*foodPreferenceModifier*/ return this.foodPreferenceModifier == null ? new Base[0] : this.foodPreferenceModifier.toArray(new Base[this.foodPreferenceModifier.size()]); // CodeableConcept
        case 1760260175: /*excludeFoodModifier*/ return this.excludeFoodModifier == null ? new Base[0] : this.excludeFoodModifier.toArray(new Base[this.excludeFoodModifier.size()]); // CodeableConcept
        case 1153521250: /*oralDiet*/ return this.oralDiet == null ? new Base[0] : new Base[] {this.oralDiet}; // NutritionOrderOralDietComponent
        case -711993159: /*supplement*/ return this.supplement == null ? new Base[0] : this.supplement.toArray(new Base[this.supplement.size()]); // NutritionOrderSupplementComponent
        case -671083805: /*enteralFormula*/ return this.enteralFormula == null ? new Base[0] : new Base[] {this.enteralFormula}; // NutritionOrderEnteralFormulaComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new NutritionOrderStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<NutritionOrderStatus>
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          return value;
        case 1792749467: // dateTime
          this.dateTime = castToDateTime(value); // DateTimeType
          return value;
        case -1207109509: // orderer
          this.orderer = castToReference(value); // Reference
          return value;
        case -120164120: // allergyIntolerance
          this.getAllergyIntolerance().add(castToReference(value)); // Reference
          return value;
        case 659473872: // foodPreferenceModifier
          this.getFoodPreferenceModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1760260175: // excludeFoodModifier
          this.getExcludeFoodModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1153521250: // oralDiet
          this.oralDiet = (NutritionOrderOralDietComponent) value; // NutritionOrderOralDietComponent
          return value;
        case -711993159: // supplement
          this.getSupplement().add((NutritionOrderSupplementComponent) value); // NutritionOrderSupplementComponent
          return value;
        case -671083805: // enteralFormula
          this.enteralFormula = (NutritionOrderEnteralFormulaComponent) value; // NutritionOrderEnteralFormulaComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new NutritionOrderStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<NutritionOrderStatus>
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("encounter")) {
          this.encounter = castToReference(value); // Reference
        } else if (name.equals("dateTime")) {
          this.dateTime = castToDateTime(value); // DateTimeType
        } else if (name.equals("orderer")) {
          this.orderer = castToReference(value); // Reference
        } else if (name.equals("allergyIntolerance")) {
          this.getAllergyIntolerance().add(castToReference(value));
        } else if (name.equals("foodPreferenceModifier")) {
          this.getFoodPreferenceModifier().add(castToCodeableConcept(value));
        } else if (name.equals("excludeFoodModifier")) {
          this.getExcludeFoodModifier().add(castToCodeableConcept(value));
        } else if (name.equals("oralDiet")) {
          this.oralDiet = (NutritionOrderOralDietComponent) value; // NutritionOrderOralDietComponent
        } else if (name.equals("supplement")) {
          this.getSupplement().add((NutritionOrderSupplementComponent) value);
        } else if (name.equals("enteralFormula")) {
          this.enteralFormula = (NutritionOrderEnteralFormulaComponent) value; // NutritionOrderEnteralFormulaComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -791418107:  return getPatient(); 
        case 1524132147:  return getEncounter(); 
        case 1792749467:  return getDateTimeElement();
        case -1207109509:  return getOrderer(); 
        case -120164120:  return addAllergyIntolerance(); 
        case 659473872:  return addFoodPreferenceModifier(); 
        case 1760260175:  return addExcludeFoodModifier(); 
        case 1153521250:  return getOralDiet(); 
        case -711993159:  return addSupplement(); 
        case -671083805:  return getEnteralFormula(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 1524132147: /*encounter*/ return new String[] {"Reference"};
        case 1792749467: /*dateTime*/ return new String[] {"dateTime"};
        case -1207109509: /*orderer*/ return new String[] {"Reference"};
        case -120164120: /*allergyIntolerance*/ return new String[] {"Reference"};
        case 659473872: /*foodPreferenceModifier*/ return new String[] {"CodeableConcept"};
        case 1760260175: /*excludeFoodModifier*/ return new String[] {"CodeableConcept"};
        case 1153521250: /*oralDiet*/ return new String[] {};
        case -711993159: /*supplement*/ return new String[] {};
        case -671083805: /*enteralFormula*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.status");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("dateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type NutritionOrder.dateTime");
        }
        else if (name.equals("orderer")) {
          this.orderer = new Reference();
          return this.orderer;
        }
        else if (name.equals("allergyIntolerance")) {
          return addAllergyIntolerance();
        }
        else if (name.equals("foodPreferenceModifier")) {
          return addFoodPreferenceModifier();
        }
        else if (name.equals("excludeFoodModifier")) {
          return addExcludeFoodModifier();
        }
        else if (name.equals("oralDiet")) {
          this.oralDiet = new NutritionOrderOralDietComponent();
          return this.oralDiet;
        }
        else if (name.equals("supplement")) {
          return addSupplement();
        }
        else if (name.equals("enteralFormula")) {
          this.enteralFormula = new NutritionOrderEnteralFormulaComponent();
          return this.enteralFormula;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "NutritionOrder";

  }

      public NutritionOrder copy() {
        NutritionOrder dst = new NutritionOrder();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        dst.orderer = orderer == null ? null : orderer.copy();
        if (allergyIntolerance != null) {
          dst.allergyIntolerance = new ArrayList<Reference>();
          for (Reference i : allergyIntolerance)
            dst.allergyIntolerance.add(i.copy());
        };
        if (foodPreferenceModifier != null) {
          dst.foodPreferenceModifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : foodPreferenceModifier)
            dst.foodPreferenceModifier.add(i.copy());
        };
        if (excludeFoodModifier != null) {
          dst.excludeFoodModifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : excludeFoodModifier)
            dst.excludeFoodModifier.add(i.copy());
        };
        dst.oralDiet = oralDiet == null ? null : oralDiet.copy();
        if (supplement != null) {
          dst.supplement = new ArrayList<NutritionOrderSupplementComponent>();
          for (NutritionOrderSupplementComponent i : supplement)
            dst.supplement.add(i.copy());
        };
        dst.enteralFormula = enteralFormula == null ? null : enteralFormula.copy();
        return dst;
      }

      protected NutritionOrder typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrder))
          return false;
        NutritionOrder o = (NutritionOrder) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(patient, o.patient, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(dateTime, o.dateTime, true) && compareDeep(orderer, o.orderer, true)
           && compareDeep(allergyIntolerance, o.allergyIntolerance, true) && compareDeep(foodPreferenceModifier, o.foodPreferenceModifier, true)
           && compareDeep(excludeFoodModifier, o.excludeFoodModifier, true) && compareDeep(oralDiet, o.oralDiet, true)
           && compareDeep(supplement, o.supplement, true) && compareDeep(enteralFormula, o.enteralFormula, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrder))
          return false;
        NutritionOrder o = (NutritionOrder) other;
        return compareValues(status, o.status, true) && compareValues(dateTime, o.dateTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, patient
          , encounter, dateTime, orderer, allergyIntolerance, foodPreferenceModifier, excludeFoodModifier
          , oralDiet, supplement, enteralFormula);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.NutritionOrder;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Return nutrition orders with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="NutritionOrder.identifier", description="Return nutrition orders with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Return nutrition orders with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>datetime</b>
   * <p>
   * Description: <b>Return nutrition orders requested on this date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>NutritionOrder.dateTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="datetime", path="NutritionOrder.dateTime", description="Return nutrition orders requested on this date", type="date" )
  public static final String SP_DATETIME = "datetime";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>datetime</b>
   * <p>
   * Description: <b>Return nutrition orders requested on this date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>NutritionOrder.dateTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATETIME = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATETIME);

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>The identify of the provider who placed the nutrition order</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NutritionOrder.orderer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="NutritionOrder.orderer", description="The identify of the provider who placed the nutrition order", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>The identify of the provider who placed the nutrition order</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NutritionOrder.orderer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>NutritionOrder:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("NutritionOrder:provider").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of the person who requires the diet, formula or nutritional supplement</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NutritionOrder.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="NutritionOrder.patient", description="The identity of the person who requires the diet, formula or nutritional supplement", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of the person who requires the diet, formula or nutritional supplement</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NutritionOrder.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>NutritionOrder:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("NutritionOrder:patient").toLocked();

 /**
   * Search parameter: <b>supplement</b>
   * <p>
   * Description: <b>Type of supplement product requested</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.supplement.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="supplement", path="NutritionOrder.supplement.type", description="Type of supplement product requested", type="token" )
  public static final String SP_SUPPLEMENT = "supplement";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>supplement</b>
   * <p>
   * Description: <b>Type of supplement product requested</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.supplement.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SUPPLEMENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SUPPLEMENT);

 /**
   * Search parameter: <b>formula</b>
   * <p>
   * Description: <b>Type of enteral or infant formula</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.enteralFormula.baseFormulaType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="formula", path="NutritionOrder.enteralFormula.baseFormulaType", description="Type of enteral or infant formula", type="token" )
  public static final String SP_FORMULA = "formula";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>formula</b>
   * <p>
   * Description: <b>Type of enteral or infant formula</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.enteralFormula.baseFormulaType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FORMULA = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FORMULA);

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Return nutrition orders with this encounter identifier</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NutritionOrder.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="NutritionOrder.encounter", description="Return nutrition orders with this encounter identifier", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Return nutrition orders with this encounter identifier</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>NutritionOrder.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>NutritionOrder:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("NutritionOrder:encounter").toLocked();

 /**
   * Search parameter: <b>oraldiet</b>
   * <p>
   * Description: <b>Type of diet that can be consumed orally (i.e., take via the mouth).</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.oralDiet.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="oraldiet", path="NutritionOrder.oralDiet.type", description="Type of diet that can be consumed orally (i.e., take via the mouth).", type="token" )
  public static final String SP_ORALDIET = "oraldiet";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>oraldiet</b>
   * <p>
   * Description: <b>Type of diet that can be consumed orally (i.e., take via the mouth).</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.oralDiet.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORALDIET = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORALDIET);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the nutrition order.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="NutritionOrder.status", description="Status of the nutrition order.", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the nutrition order.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>additive</b>
   * <p>
   * Description: <b>Type of module component to add to the feeding</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.enteralFormula.additiveType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="additive", path="NutritionOrder.enteralFormula.additiveType", description="Type of module component to add to the feeding", type="token" )
  public static final String SP_ADDITIVE = "additive";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>additive</b>
   * <p>
   * Description: <b>Type of module component to add to the feeding</b><br>
   * Type: <b>token</b><br>
   * Path: <b>NutritionOrder.enteralFormula.additiveType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ADDITIVE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ADDITIVE);


}

