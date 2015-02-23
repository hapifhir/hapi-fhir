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
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
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
       * ???.
       */
      INACTIVE,
      /**
       * Actions implied by the request have been temporarily halted, but are expected to continue later. May also be called "suspended".
       */
      HELD,
      /**
       * ???.
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NutritionOrderStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
              return null;
          if ("proposed".equals(codeString))
            return PROPOSED;
          if ("planned".equals(codeString))
            return PLANNED;
        if ("requested".equals(codeString))
          return REQUESTED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("held".equals(codeString))
          return HELD;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new Exception("Unknown NutritionOrderStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED:
              return "proposed";
            case PLANNED:
              return "planned";
            case REQUESTED: return "requested";
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case HELD: return "held";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED:
              return "";
            case PLANNED:
              return "";
            case REQUESTED: return "";
            case ACTIVE: return "";
            case INACTIVE: return "";
            case HELD: return "";
            case CANCELLED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED:
              return "The request has been proposed.";
            case PLANNED:
              return "The request has been planned.";
            case REQUESTED:
              return "The request has been placed.";
            case ACTIVE:
              return "The request is 'actionable', but not all actions that are implied by it have occurred yet.";
            case INACTIVE:
              return "???.";
            case HELD:
              return "Actions implied by the request have been temporarily halted, but are expected to continue later. May also be called 'suspended'.";
            case CANCELLED:
              return "???.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED:
              return "proposed";
            case PLANNED:
              return "planned";
            case REQUESTED:
              return "requested";
            case ACTIVE:
              return "active";
            case INACTIVE:
              return "inactive";
            case HELD:
              return "held";
            case CANCELLED:
              return "cancelled";
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
      if ("planned".equals(codeString))
        return NutritionOrderStatus.PLANNED;
        if ("requested".equals(codeString))
          return NutritionOrderStatus.REQUESTED;
        if ("active".equals(codeString))
          return NutritionOrderStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return NutritionOrderStatus.INACTIVE;
        if ("held".equals(codeString))
          return NutritionOrderStatus.HELD;
        if ("cancelled".equals(codeString))
          return NutritionOrderStatus.CANCELLED;
        throw new IllegalArgumentException("Unknown NutritionOrderStatus code '"+codeString+"'");
        }
    public String toCode(NutritionOrderStatus code) {
      if (code == NutritionOrderStatus.PROPOSED)
        return "proposed";
      if (code == NutritionOrderStatus.PLANNED)
        return "planned";
      if (code == NutritionOrderStatus.REQUESTED)
        return "requested";
      if (code == NutritionOrderStatus.ACTIVE)
        return "active";
      if (code == NutritionOrderStatus.INACTIVE)
        return "inactive";
      if (code == NutritionOrderStatus.HELD)
        return "held";
      if (code == NutritionOrderStatus.CANCELLED)
        return "cancelled";
      return "?";
      }
    }

    @Block()
    public static class NutritionOrderOralDietComponent extends BackboneElement {
        /**
         * Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Type of oral diet or diet restrictions that describe what can be consumed orally", formalDefinition="Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth)." )
        protected List<CodeableConcept> type;

        /**
         * The time period and frequency at which the diet should be given.
         */
        @Child(name="scheduled", type={Timing.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Scheduled frequency of diet", formalDefinition="The time period and frequency at which the diet should be given." )
        protected Timing scheduled;

        /**
         * Class that defines the details of any nutrient modifications required for the oral diet.
         */
        @Child(name="nutrients", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Required  nutrient modifications", formalDefinition="Class that defines the details of any nutrient modifications required for the oral diet." )
        protected List<NutritionOrderOralDietNutrientsComponent> nutrients;

        /**
         * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
         */
        @Child(name="texture", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Required  texture modifications", formalDefinition="Class that describes any texture modifications required for the patient to safely consume various types of solid foods." )
        protected List<NutritionOrderOralDietTextureComponent> texture;

        /**
         * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
         */
        @Child(name="fluidConsistencyType", type={CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="The required consistency of fluids and liquids provided to the patient", formalDefinition="Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient." )
        protected List<CodeableConcept> fluidConsistencyType;

        /**
         * Additional instructions or information pertaining to the oral diet.
         */
        @Child(name="instruction", type={StringType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Instructions or additional information about the oral diet", formalDefinition="Additional instructions or information pertaining to the oral diet." )
        protected StringType instruction;

        private static final long serialVersionUID = -1825879074L;

      public NutritionOrderOralDietComponent() {
        super();
      }

        /**
         * @return {@link #type} (Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #type} (Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).)
         */
    // syntactic sugar
        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

        /**
         * @return {@link #scheduled} (The time period and frequency at which the diet should be given.)
         */
        public Timing getScheduled() { 
          if (this.scheduled == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietComponent.scheduled");
            else if (Configuration.doAutoCreate())
              this.scheduled = new Timing(); // cc
          return this.scheduled;
        }

        public boolean hasScheduled() { 
          return this.scheduled != null && !this.scheduled.isEmpty();
        }

        /**
         * @param value {@link #scheduled} (The time period and frequency at which the diet should be given.)
         */
        public NutritionOrderOralDietComponent setScheduled(Timing value) { 
          this.scheduled = value;
          return this;
        }

        /**
         * @return {@link #nutrients} (Class that defines the details of any nutrient modifications required for the oral diet.)
         */
        public List<NutritionOrderOralDietNutrientsComponent> getNutrients() { 
          if (this.nutrients == null)
            this.nutrients = new ArrayList<NutritionOrderOralDietNutrientsComponent>();
          return this.nutrients;
        }

        public boolean hasNutrients() { 
          if (this.nutrients == null)
            return false;
          for (NutritionOrderOralDietNutrientsComponent item : this.nutrients)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #nutrients} (Class that defines the details of any nutrient modifications required for the oral diet.)
         */
    // syntactic sugar
        public NutritionOrderOralDietNutrientsComponent addNutrients() { //3
          NutritionOrderOralDietNutrientsComponent t = new NutritionOrderOralDietNutrientsComponent();
          if (this.nutrients == null)
            this.nutrients = new ArrayList<NutritionOrderOralDietNutrientsComponent>();
          this.nutrients.add(t);
          return t;
        }

        /**
         * @return {@link #texture} (Class that describes any texture modifications required for the patient to safely consume various types of solid foods.)
         */
        public List<NutritionOrderOralDietTextureComponent> getTexture() { 
          if (this.texture == null)
            this.texture = new ArrayList<NutritionOrderOralDietTextureComponent>();
          return this.texture;
        }

        public boolean hasTexture() { 
          if (this.texture == null)
            return false;
          for (NutritionOrderOralDietTextureComponent item : this.texture)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #texture} (Class that describes any texture modifications required for the patient to safely consume various types of solid foods.)
         */
    // syntactic sugar
        public NutritionOrderOralDietTextureComponent addTexture() { //3
          NutritionOrderOralDietTextureComponent t = new NutritionOrderOralDietTextureComponent();
          if (this.texture == null)
            this.texture = new ArrayList<NutritionOrderOralDietTextureComponent>();
          this.texture.add(t);
          return t;
        }

        /**
         * @return {@link #fluidConsistencyType} (Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.)
         */
        public List<CodeableConcept> getFluidConsistencyType() { 
          if (this.fluidConsistencyType == null)
            this.fluidConsistencyType = new ArrayList<CodeableConcept>();
          return this.fluidConsistencyType;
        }

        public boolean hasFluidConsistencyType() { 
          if (this.fluidConsistencyType == null)
            return false;
          for (CodeableConcept item : this.fluidConsistencyType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #fluidConsistencyType} (Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.)
         */
    // syntactic sugar
        public CodeableConcept addFluidConsistencyType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.fluidConsistencyType == null)
            this.fluidConsistencyType = new ArrayList<CodeableConcept>();
          this.fluidConsistencyType.add(t);
          return t;
        }

        /**
         * @return {@link #instruction} (Additional instructions or information pertaining to the oral diet.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
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
         * @param value {@link #instruction} (Additional instructions or information pertaining to the oral diet.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public NutritionOrderOralDietComponent setInstructionElement(StringType value) { 
          this.instruction = value;
          return this;
        }

        /**
         * @return Additional instructions or information pertaining to the oral diet.
         */
        public String getInstruction() { 
          return this.instruction == null ? null : this.instruction.getValue();
        }

        /**
         * @param value Additional instructions or information pertaining to the oral diet.
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
          childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of oral diet or diet restrictions that describe what can be consumed orally (i.e., take via the mouth).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("scheduled", "Timing", "The time period and frequency at which the diet should be given.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("nutrients", "", "Class that defines the details of any nutrient modifications required for the oral diet.", 0, java.lang.Integer.MAX_VALUE, nutrients));
          childrenList.add(new Property("texture", "", "Class that describes any texture modifications required for the patient to safely consume various types of solid foods.", 0, java.lang.Integer.MAX_VALUE, texture));
          childrenList.add(new Property("fluidConsistencyType", "CodeableConcept", "Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.", 0, java.lang.Integer.MAX_VALUE, fluidConsistencyType));
          childrenList.add(new Property("instruction", "string", "Additional instructions or information pertaining to the oral diet.", 0, java.lang.Integer.MAX_VALUE, instruction));
        }

      public NutritionOrderOralDietComponent copy() {
        NutritionOrderOralDietComponent dst = new NutritionOrderOralDietComponent();
        copyValues(dst);
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        if (nutrients != null) {
          dst.nutrients = new ArrayList<NutritionOrderOralDietNutrientsComponent>();
          for (NutritionOrderOralDietNutrientsComponent i : nutrients)
            dst.nutrients.add(i.copy());
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
        return compareDeep(type, o.type, true) && compareDeep(scheduled, o.scheduled, true) && compareDeep(nutrients, o.nutrients, true)
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (scheduled == null || scheduled.isEmpty())
           && (nutrients == null || nutrients.isEmpty()) && (texture == null || texture.isEmpty()) && (fluidConsistencyType == null || fluidConsistencyType.isEmpty())
           && (instruction == null || instruction.isEmpty());
      }

  }

    @Block()
    public static class NutritionOrderOralDietNutrientsComponent extends BackboneElement {
        /**
         * Identifies the type of nutrient that is being modified such as carbohydrate or sodium.
         */
        @Child(name="modifier", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Type of nutrient that is being modified", formalDefinition="Identifies the type of nutrient that is being modified such as carbohydrate or sodium." )
        protected CodeableConcept modifier;

        /**
         * The quantity of the specified nutrient to include in diet.
         */
        @Child(name="amount", type={Quantity.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Quantity of the specified nutrient", formalDefinition="The quantity of the specified nutrient to include in diet." )
        protected Quantity amount;

        private static final long serialVersionUID = 1042462093L;

      public NutritionOrderOralDietNutrientsComponent() {
        super();
      }

        /**
         * @return {@link #modifier} (Identifies the type of nutrient that is being modified such as carbohydrate or sodium.)
         */
        public CodeableConcept getModifier() { 
          if (this.modifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietNutrientsComponent.modifier");
            else if (Configuration.doAutoCreate())
              this.modifier = new CodeableConcept(); // cc
          return this.modifier;
        }

        public boolean hasModifier() { 
          return this.modifier != null && !this.modifier.isEmpty();
        }

        /**
         * @param value {@link #modifier} (Identifies the type of nutrient that is being modified such as carbohydrate or sodium.)
         */
        public NutritionOrderOralDietNutrientsComponent setModifier(CodeableConcept value) { 
          this.modifier = value;
          return this;
        }

        /**
         * @return {@link #amount} (The quantity of the specified nutrient to include in diet.)
         */
        public Quantity getAmount() { 
          if (this.amount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderOralDietNutrientsComponent.amount");
            else if (Configuration.doAutoCreate())
              this.amount = new Quantity(); // cc
          return this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (The quantity of the specified nutrient to include in diet.)
         */
        public NutritionOrderOralDietNutrientsComponent setAmount(Quantity value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("modifier", "CodeableConcept", "Identifies the type of nutrient that is being modified such as carbohydrate or sodium.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("amount", "Quantity", "The quantity of the specified nutrient to include in diet.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      public NutritionOrderOralDietNutrientsComponent copy() {
        NutritionOrderOralDietNutrientsComponent dst = new NutritionOrderOralDietNutrientsComponent();
        copyValues(dst);
        dst.modifier = modifier == null ? null : modifier.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietNutrientsComponent))
          return false;
        NutritionOrderOralDietNutrientsComponent o = (NutritionOrderOralDietNutrientsComponent) other;
        return compareDeep(modifier, o.modifier, true) && compareDeep(amount, o.amount, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderOralDietNutrientsComponent))
          return false;
        NutritionOrderOralDietNutrientsComponent o = (NutritionOrderOralDietNutrientsComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (modifier == null || modifier.isEmpty()) && (amount == null || amount.isEmpty())
          ;
      }

  }

    @Block()
    public static class NutritionOrderOralDietTextureComponent extends BackboneElement {
        /**
         * Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.
         */
        @Child(name="modifier", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Code to indicate how to alter the texture of the foods, e.g., pureed", formalDefinition="Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed." )
        protected CodeableConcept modifier;

        /**
         * Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.
         */
        @Child(name="foodType", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Concepts that are used to identify an entity that is ingested for nutritional purposes", formalDefinition="Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet." )
        protected CodeableConcept foodType;

        private static final long serialVersionUID = -56402817L;

      public NutritionOrderOralDietTextureComponent() {
        super();
      }

        /**
         * @return {@link #modifier} (Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.)
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
         * @param value {@link #modifier} (Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.)
         */
        public NutritionOrderOralDietTextureComponent setModifier(CodeableConcept value) { 
          this.modifier = value;
          return this;
        }

        /**
         * @return {@link #foodType} (Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.)
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
         * @param value {@link #foodType} (Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.)
         */
        public NutritionOrderOralDietTextureComponent setFoodType(CodeableConcept value) { 
          this.foodType = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("modifier", "CodeableConcept", "Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, and pureed.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("foodType", "CodeableConcept", "Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.", 0, java.lang.Integer.MAX_VALUE, foodType));
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
        return super.isEmpty() && (modifier == null || modifier.isEmpty()) && (foodType == null || foodType.isEmpty())
          ;
      }

  }

    @Block()
    public static class NutritionOrderSupplementComponent extends BackboneElement {
        /**
         * Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Type of supplement product requested", formalDefinition="Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement." )
        protected CodeableConcept type;

        /**
         * The product or brand name of the nutritional supplement product to be provided to the patient.
         */
        @Child(name="productName", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Product or brand name of the nutritional supplement", formalDefinition="The product or brand name of the nutritional supplement product to be provided to the patient." )
        protected StringType productName;

        /**
         * The time period and frequency at which the supplement(s) should be given.
         */
        @Child(name="scheduled", type={Timing.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Scheduled frequency of supplement", formalDefinition="The time period and frequency at which the supplement(s) should be given." )
        protected Timing scheduled;

        /**
         * The amount of the nutritional supplement product to provide to the patient.
         */
        @Child(name="quantity", type={Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Amount of the nutritional supplement", formalDefinition="The amount of the nutritional supplement product to provide to the patient." )
        protected Quantity quantity;

        private static final long serialVersionUID = -608233691L;

      public NutritionOrderSupplementComponent() {
        super();
      }

        /**
         * @return {@link #type} (Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.)
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
         * @param value {@link #type} (Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.)
         */
        public NutritionOrderSupplementComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #productName} (The product or brand name of the nutritional supplement product to be provided to the patient.). This is the underlying object with id, value and extensions. The accessor "getProductName" gives direct access to the value
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
         * @param value {@link #productName} (The product or brand name of the nutritional supplement product to be provided to the patient.). This is the underlying object with id, value and extensions. The accessor "getProductName" gives direct access to the value
         */
        public NutritionOrderSupplementComponent setProductNameElement(StringType value) { 
          this.productName = value;
          return this;
        }

        /**
         * @return The product or brand name of the nutritional supplement product to be provided to the patient.
         */
        public String getProductName() { 
          return this.productName == null ? null : this.productName.getValue();
        }

        /**
         * @param value The product or brand name of the nutritional supplement product to be provided to the patient.
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
         * @return {@link #scheduled} (The time period and frequency at which the supplement(s) should be given.)
         */
        public Timing getScheduled() { 
          if (this.scheduled == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderSupplementComponent.scheduled");
            else if (Configuration.doAutoCreate())
              this.scheduled = new Timing(); // cc
          return this.scheduled;
        }

        public boolean hasScheduled() { 
          return this.scheduled != null && !this.scheduled.isEmpty();
        }

        /**
         * @param value {@link #scheduled} (The time period and frequency at which the supplement(s) should be given.)
         */
        public NutritionOrderSupplementComponent setScheduled(Timing value) { 
          this.scheduled = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of the nutritional supplement product to provide to the patient.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderSupplementComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of the nutritional supplement product to provide to the patient.)
         */
        public NutritionOrderSupplementComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("productName", "string", "The product or brand name of the nutritional supplement product to be provided to the patient.", 0, java.lang.Integer.MAX_VALUE, productName));
          childrenList.add(new Property("scheduled", "Timing", "The time period and frequency at which the supplement(s) should be given.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("quantity", "Quantity", "The amount of the nutritional supplement product to provide to the patient.", 0, java.lang.Integer.MAX_VALUE, quantity));
        }

      public NutritionOrderSupplementComponent copy() {
        NutritionOrderSupplementComponent dst = new NutritionOrderSupplementComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.productName = productName == null ? null : productName.copy();
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderSupplementComponent))
          return false;
        NutritionOrderSupplementComponent o = (NutritionOrderSupplementComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(productName, o.productName, true) && compareDeep(scheduled, o.scheduled, true)
           && compareDeep(quantity, o.quantity, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderSupplementComponent))
          return false;
        NutritionOrderSupplementComponent o = (NutritionOrderSupplementComponent) other;
        return compareValues(productName, o.productName, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (productName == null || productName.isEmpty())
           && (scheduled == null || scheduled.isEmpty()) && (quantity == null || quantity.isEmpty())
          ;
      }

  }

    @Block()
    public static class NutritionOrderEnteralFormulaComponent extends BackboneElement {
        /**
         * Free text formula administration or feeding instructions for cases where the instructions are too complex to code.
         */
        @Child(name="administrationInstructions", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Formula feeding instructions expressed as text", formalDefinition="Free text formula administration or feeding instructions for cases where the instructions are too complex to code." )
        protected StringType administrationInstructions;

        /**
         * Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula.
         */
        @Child(name="baseFormulaType", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Type of enteral or infant formula", formalDefinition="Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula." )
        protected CodeableConcept baseFormulaType;

        /**
         * The product or brand name of the enteral or infant formula product to be provided to the patient.
         */
        @Child(name="baseFormulaProductName", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Product or brand name of the enteral or infant formula", formalDefinition="The product or brand name of the enteral or infant formula product to be provided to the patient." )
        protected StringType baseFormulaProductName;

        /**
         * The time period and frequency at which the enteral formula should be delivered to the patient.
         */
        @Child(name="scheduled", type={Timing.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Scheduled frequency of enteral feeding", formalDefinition="The time period and frequency at which the enteral formula should be delivered to the patient." )
        protected Timing scheduled;

        /**
         * Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.
         */
        @Child(name="additiveType", type={CodeableConcept.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Type of modular component to add to the feeding", formalDefinition="Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula." )
        protected CodeableConcept additiveType;

        /**
         * The product or brand name of the type of modular component to be added to the formula.
         */
        @Child(name="additiveProductName", type={StringType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Product or brand name of the modular additive", formalDefinition="The product or brand name of the type of modular component to be added to the formula." )
        protected StringType additiveProductName;

        /**
         * The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL.
         */
        @Child(name="caloricDensity", type={Quantity.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Amount of energy per specified volume that is required", formalDefinition="The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL." )
        protected Quantity caloricDensity;

        /**
         * A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube.
         */
        @Child(name="routeofAdministration", type={CodeableConcept.class}, order=8, min=0, max=1)
        @Description(shortDefinition="How the formula should enter the patient's gastrointestinal tract", formalDefinition="A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube." )
        protected CodeableConcept routeofAdministration;

        /**
         * The volume of formula to provide to the patient per the specified administration schedule.
         */
        @Child(name="quantity", type={Quantity.class}, order=9, min=0, max=1)
        @Description(shortDefinition="The volume of formula to provide", formalDefinition="The volume of formula to provide to the patient per the specified administration schedule." )
        protected Quantity quantity;

        /**
         * Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule.
         */
        @Child(name="rate", type={Ratio.class}, order=10, min=0, max=1)
        @Description(shortDefinition="Speed with which the formula is provided per period of time", formalDefinition="Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule." )
        protected Ratio rate;

        /**
         * The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours.
         */
        @Child(name="rateAdjustment", type={Quantity.class}, order=11, min=0, max=1)
        @Description(shortDefinition="Change in the rate of administration over a given time", formalDefinition="The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours." )
        protected Quantity rateAdjustment;

        /**
         * The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours.
         */
        @Child(name="maxVolumeToDeliver", type={Quantity.class}, order=12, min=0, max=1)
        @Description(shortDefinition="Upper limit on formula volume per unit of time", formalDefinition="The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours." )
        protected Quantity maxVolumeToDeliver;

        private static final long serialVersionUID = -1342980650L;

      public NutritionOrderEnteralFormulaComponent() {
        super();
      }

        /**
         * @return {@link #administrationInstructions} (Free text formula administration or feeding instructions for cases where the instructions are too complex to code.). This is the underlying object with id, value and extensions. The accessor "getAdministrationInstructions" gives direct access to the value
         */
        public StringType getAdministrationInstructionsElement() { 
          if (this.administrationInstructions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.administrationInstructions");
            else if (Configuration.doAutoCreate())
              this.administrationInstructions = new StringType(); // bb
          return this.administrationInstructions;
        }

        public boolean hasAdministrationInstructionsElement() { 
          return this.administrationInstructions != null && !this.administrationInstructions.isEmpty();
        }

        public boolean hasAdministrationInstructions() { 
          return this.administrationInstructions != null && !this.administrationInstructions.isEmpty();
        }

        /**
         * @param value {@link #administrationInstructions} (Free text formula administration or feeding instructions for cases where the instructions are too complex to code.). This is the underlying object with id, value and extensions. The accessor "getAdministrationInstructions" gives direct access to the value
         */
        public NutritionOrderEnteralFormulaComponent setAdministrationInstructionsElement(StringType value) { 
          this.administrationInstructions = value;
          return this;
        }

        /**
         * @return Free text formula administration or feeding instructions for cases where the instructions are too complex to code.
         */
        public String getAdministrationInstructions() { 
          return this.administrationInstructions == null ? null : this.administrationInstructions.getValue();
        }

        /**
         * @param value Free text formula administration or feeding instructions for cases where the instructions are too complex to code.
         */
        public NutritionOrderEnteralFormulaComponent setAdministrationInstructions(String value) { 
          if (Utilities.noString(value))
            this.administrationInstructions = null;
          else {
            if (this.administrationInstructions == null)
              this.administrationInstructions = new StringType();
            this.administrationInstructions.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #baseFormulaType} (Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula.)
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
         * @param value {@link #baseFormulaType} (Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula.)
         */
        public NutritionOrderEnteralFormulaComponent setBaseFormulaType(CodeableConcept value) { 
          this.baseFormulaType = value;
          return this;
        }

        /**
         * @return {@link #baseFormulaProductName} (The product or brand name of the enteral or infant formula product to be provided to the patient.). This is the underlying object with id, value and extensions. The accessor "getBaseFormulaProductName" gives direct access to the value
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
         * @param value {@link #baseFormulaProductName} (The product or brand name of the enteral or infant formula product to be provided to the patient.). This is the underlying object with id, value and extensions. The accessor "getBaseFormulaProductName" gives direct access to the value
         */
        public NutritionOrderEnteralFormulaComponent setBaseFormulaProductNameElement(StringType value) { 
          this.baseFormulaProductName = value;
          return this;
        }

        /**
         * @return The product or brand name of the enteral or infant formula product to be provided to the patient.
         */
        public String getBaseFormulaProductName() { 
          return this.baseFormulaProductName == null ? null : this.baseFormulaProductName.getValue();
        }

        /**
         * @param value The product or brand name of the enteral or infant formula product to be provided to the patient.
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
         * @return {@link #scheduled} (The time period and frequency at which the enteral formula should be delivered to the patient.)
         */
        public Timing getScheduled() { 
          if (this.scheduled == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.scheduled");
            else if (Configuration.doAutoCreate())
              this.scheduled = new Timing(); // cc
          return this.scheduled;
        }

        public boolean hasScheduled() { 
          return this.scheduled != null && !this.scheduled.isEmpty();
        }

        /**
         * @param value {@link #scheduled} (The time period and frequency at which the enteral formula should be delivered to the patient.)
         */
        public NutritionOrderEnteralFormulaComponent setScheduled(Timing value) { 
          this.scheduled = value;
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
         * @return {@link #caloricDensity} (The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL.)
         */
        public Quantity getCaloricDensity() { 
          if (this.caloricDensity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.caloricDensity");
            else if (Configuration.doAutoCreate())
              this.caloricDensity = new Quantity(); // cc
          return this.caloricDensity;
        }

        public boolean hasCaloricDensity() { 
          return this.caloricDensity != null && !this.caloricDensity.isEmpty();
        }

        /**
         * @param value {@link #caloricDensity} (The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL.)
         */
        public NutritionOrderEnteralFormulaComponent setCaloricDensity(Quantity value) { 
          this.caloricDensity = value;
          return this;
        }

        /**
         * @return {@link #routeofAdministration} (A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube.)
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
         * @param value {@link #routeofAdministration} (A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube.)
         */
        public NutritionOrderEnteralFormulaComponent setRouteofAdministration(CodeableConcept value) { 
          this.routeofAdministration = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The volume of formula to provide to the patient per the specified administration schedule.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The volume of formula to provide to the patient per the specified administration schedule.)
         */
        public NutritionOrderEnteralFormulaComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule.)
         */
        public Ratio getRate() { 
          if (this.rate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.rate");
            else if (Configuration.doAutoCreate())
              this.rate = new Ratio(); // cc
          return this.rate;
        }

        public boolean hasRate() { 
          return this.rate != null && !this.rate.isEmpty();
        }

        /**
         * @param value {@link #rate} (Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule.)
         */
        public NutritionOrderEnteralFormulaComponent setRate(Ratio value) { 
          this.rate = value;
          return this;
        }

        /**
         * @return {@link #rateAdjustment} (The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours.)
         */
        public Quantity getRateAdjustment() { 
          if (this.rateAdjustment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.rateAdjustment");
            else if (Configuration.doAutoCreate())
              this.rateAdjustment = new Quantity(); // cc
          return this.rateAdjustment;
        }

        public boolean hasRateAdjustment() { 
          return this.rateAdjustment != null && !this.rateAdjustment.isEmpty();
        }

        /**
         * @param value {@link #rateAdjustment} (The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours.)
         */
        public NutritionOrderEnteralFormulaComponent setRateAdjustment(Quantity value) { 
          this.rateAdjustment = value;
          return this;
        }

        /**
         * @return {@link #maxVolumeToDeliver} (The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours.)
         */
        public Quantity getMaxVolumeToDeliver() { 
          if (this.maxVolumeToDeliver == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderEnteralFormulaComponent.maxVolumeToDeliver");
            else if (Configuration.doAutoCreate())
              this.maxVolumeToDeliver = new Quantity(); // cc
          return this.maxVolumeToDeliver;
        }

        public boolean hasMaxVolumeToDeliver() { 
          return this.maxVolumeToDeliver != null && !this.maxVolumeToDeliver.isEmpty();
        }

        /**
         * @param value {@link #maxVolumeToDeliver} (The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours.)
         */
        public NutritionOrderEnteralFormulaComponent setMaxVolumeToDeliver(Quantity value) { 
          this.maxVolumeToDeliver = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("administrationInstructions", "string", "Free text formula administration or feeding instructions for cases where the instructions are too complex to code.", 0, java.lang.Integer.MAX_VALUE, administrationInstructions));
          childrenList.add(new Property("baseFormulaType", "CodeableConcept", "Indicates the type of enteral or infant formula requested such as an adult standard formula with fiber or a soy-based infant formula.", 0, java.lang.Integer.MAX_VALUE, baseFormulaType));
          childrenList.add(new Property("baseFormulaProductName", "string", "The product or brand name of the enteral or infant formula product to be provided to the patient.", 0, java.lang.Integer.MAX_VALUE, baseFormulaProductName));
          childrenList.add(new Property("scheduled", "Timing", "The time period and frequency at which the enteral formula should be delivered to the patient.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("additiveType", "CodeableConcept", "Indicates the type of modular component such as protein, carbohydrate, fat or fiber to be provided in addition to or mixed with the base formula.", 0, java.lang.Integer.MAX_VALUE, additiveType));
          childrenList.add(new Property("additiveProductName", "string", "The product or brand name of the type of modular component to be added to the formula.", 0, java.lang.Integer.MAX_VALUE, additiveProductName));
          childrenList.add(new Property("caloricDensity", "Quantity", "The amount of energy (Calories) that the formula should provide per specified volume, typically per mL or fluid oz.  For example, an infant may require a formula the provides 24 Calories per fluid ounce or an adult may require an enteral formula that provides 1.5 Calorie/mL.", 0, java.lang.Integer.MAX_VALUE, caloricDensity));
          childrenList.add(new Property("routeofAdministration", "CodeableConcept", "A coded concept specifying the route or physiological path of administration into the patient 's gastrointestinal  tract for purposes of providing the formula feeding, e.g., nasogastric tube.", 0, java.lang.Integer.MAX_VALUE, routeofAdministration));
          childrenList.add(new Property("quantity", "Quantity", "The volume of formula to provide to the patient per the specified administration schedule.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("rate", "Ratio", "Identifies the speed with which the formula is introduced into the subject via a feeding pump, e.g., 60 mL per hour, according to the specified schedule.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("rateAdjustment", "Quantity", "The change in the administration rate over a given time, e.g. increase by 10 mL/hour every 4 hours.", 0, java.lang.Integer.MAX_VALUE, rateAdjustment));
          childrenList.add(new Property("maxVolumeToDeliver", "Quantity", "The maximum total quantity of formula that may be administered to a subject over the period of time, e.g., 1440 mL over 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxVolumeToDeliver));
        }

      public NutritionOrderEnteralFormulaComponent copy() {
        NutritionOrderEnteralFormulaComponent dst = new NutritionOrderEnteralFormulaComponent();
        copyValues(dst);
        dst.administrationInstructions = administrationInstructions == null ? null : administrationInstructions.copy();
        dst.baseFormulaType = baseFormulaType == null ? null : baseFormulaType.copy();
        dst.baseFormulaProductName = baseFormulaProductName == null ? null : baseFormulaProductName.copy();
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        dst.additiveType = additiveType == null ? null : additiveType.copy();
        dst.additiveProductName = additiveProductName == null ? null : additiveProductName.copy();
        dst.caloricDensity = caloricDensity == null ? null : caloricDensity.copy();
        dst.routeofAdministration = routeofAdministration == null ? null : routeofAdministration.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.rate = rate == null ? null : rate.copy();
        dst.rateAdjustment = rateAdjustment == null ? null : rateAdjustment.copy();
        dst.maxVolumeToDeliver = maxVolumeToDeliver == null ? null : maxVolumeToDeliver.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof NutritionOrderEnteralFormulaComponent))
          return false;
        NutritionOrderEnteralFormulaComponent o = (NutritionOrderEnteralFormulaComponent) other;
        return compareDeep(administrationInstructions, o.administrationInstructions, true) && compareDeep(baseFormulaType, o.baseFormulaType, true)
           && compareDeep(baseFormulaProductName, o.baseFormulaProductName, true) && compareDeep(scheduled, o.scheduled, true)
           && compareDeep(additiveType, o.additiveType, true) && compareDeep(additiveProductName, o.additiveProductName, true)
           && compareDeep(caloricDensity, o.caloricDensity, true) && compareDeep(routeofAdministration, o.routeofAdministration, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(rate, o.rate, true) && compareDeep(rateAdjustment, o.rateAdjustment, true)
           && compareDeep(maxVolumeToDeliver, o.maxVolumeToDeliver, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrderEnteralFormulaComponent))
          return false;
        NutritionOrderEnteralFormulaComponent o = (NutritionOrderEnteralFormulaComponent) other;
        return compareValues(administrationInstructions, o.administrationInstructions, true) && compareValues(baseFormulaProductName, o.baseFormulaProductName, true)
           && compareValues(additiveProductName, o.additiveProductName, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (administrationInstructions == null || administrationInstructions.isEmpty())
           && (baseFormulaType == null || baseFormulaType.isEmpty()) && (baseFormulaProductName == null || baseFormulaProductName.isEmpty())
           && (scheduled == null || scheduled.isEmpty()) && (additiveType == null || additiveType.isEmpty())
           && (additiveProductName == null || additiveProductName.isEmpty()) && (caloricDensity == null || caloricDensity.isEmpty())
           && (routeofAdministration == null || routeofAdministration.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (rate == null || rate.isEmpty()) && (rateAdjustment == null || rateAdjustment.isEmpty())
           && (maxVolumeToDeliver == null || maxVolumeToDeliver.isEmpty());
      }

  }

    /**
     * The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.
     */
    @Child(name = "patient", type = {Patient.class}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="The person who requires the diet, formula or nutritional supplement", formalDefinition="The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    protected Patient patientTarget;

    /**
     * The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.
     */
    @Child(name = "orderer", type = {Practitioner.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Who ordered the diet, formula or nutritional supplement", formalDefinition="The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings." )
    protected Reference orderer;

    /**
     * The actual object that is the target of the reference (The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.)
     */
    protected Practitioner ordererTarget;

    /**
     * Identifiers assigned to this order by the order sender or by the order receiver.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 2, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Identifiers assigned to this order", formalDefinition="Identifiers assigned to this order by the order sender or by the order receiver." )
    protected List<Identifier> identifier;

    /**
     * An encounter that provides additional information about the healthcare context in which this request is made.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="The encounter associated with that this nutrition order", formalDefinition="An encounter that provides additional information about the healthcare context in which this request is made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (An encounter that provides additional information about the healthcare context in which this request is made.)
     */
    protected Encounter encounterTarget;

    /**
     * The date and time that this nutrition order was requested.
     */
    @Child(name = "dateTime", type = {DateTimeType.class}, order = 4, min = 1, max = 1)
    @Description(shortDefinition="Date and time the nutrition order was requested", formalDefinition="The date and time that this nutrition order was requested." )
    protected DateTimeType dateTime;

    /**
     * The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.
     */
    @Child(name = "allergyIntolerance", type = {AllergyIntolerance.class}, order = 5, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of the patient's food and nutrition-related allergies and intolerances", formalDefinition="The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order." )
    protected List<Reference> allergyIntolerance;
    /**
     * The actual objects that are the target of the reference (The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.)
     */
    protected List<AllergyIntolerance> allergyIntoleranceTarget;


    /**
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     */
    @Child(name = "foodPreferenceModifier", type = {CodeableConcept.class}, order = 6, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Order-specific modifier about the type of food that should be given", formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings." )
    protected List<CodeableConcept> foodPreferenceModifier;

    /**
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     */
    @Child(name = "excludeFoodModifier", type = {CodeableConcept.class}, order = 7, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Order-specific modifier about the type of food that should not be given", formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings." )
    protected List<CodeableConcept> excludeFoodModifier;

    /**
     * Class that defines the components of an oral diet order for the patient.
     */
    @Child(name = "oralDiet", type = {}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Oral diet components", formalDefinition="Class that defines the components of an oral diet order for the patient." )
    protected NutritionOrderOralDietComponent oralDiet;

    /**
     * Class that defines the components of a supplement order for the patient.
     */
    @Child(name = "supplement", type = {}, order = 9, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Supplement components", formalDefinition="Class that defines the components of a supplement order for the patient." )
    protected List<NutritionOrderSupplementComponent> supplement;

    /**
     * Class that defines the components of an enteral formula order for the patient.
     */
    @Child(name = "enteralFormula", type = {}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Enteral formula components", formalDefinition="Class that defines the components of an enteral formula order for the patient." )
    protected NutritionOrderEnteralFormulaComponent enteralFormula;

    /**
     * The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
     */
    @Child(name = "status", type = {CodeType.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition = "proposed | planned | requested | active | inactive | held | cancelled", formalDefinition = "The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.")
    protected Enumeration<NutritionOrderStatus> status;

    private static final long serialVersionUID = -966362649L;

    public NutritionOrder() {
      super();
    }

    public NutritionOrder(Reference patient, DateTimeType dateTime) {
      super();
      this.patient = patient;
      this.dateTime = dateTime;
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
     * @return {@link #identifier} (Identifiers assigned to this order by the order sender or by the order receiver.)
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
     * @return {@link #identifier} (Identifiers assigned to this order by the order sender or by the order receiver.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
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
     * @return {@link #allergyIntolerance} (The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.)
     */
    public List<Reference> getAllergyIntolerance() { 
      if (this.allergyIntolerance == null)
        this.allergyIntolerance = new ArrayList<Reference>();
      return this.allergyIntolerance;
    }

    public boolean hasAllergyIntolerance() { 
      if (this.allergyIntolerance == null)
        return false;
      for (Reference item : this.allergyIntolerance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #allergyIntolerance} (The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.)
     */
    // syntactic sugar
    public Reference addAllergyIntolerance() { //3
      Reference t = new Reference();
      if (this.allergyIntolerance == null)
        this.allergyIntolerance = new ArrayList<Reference>();
      this.allergyIntolerance.add(t);
      return t;
    }

    /**
     * @return {@link #allergyIntolerance} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.)
     */
    public List<AllergyIntolerance> getAllergyIntoleranceTarget() { 
      if (this.allergyIntoleranceTarget == null)
        this.allergyIntoleranceTarget = new ArrayList<AllergyIntolerance>();
      return this.allergyIntoleranceTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #allergyIntolerance} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.)
     */
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

    public boolean hasFoodPreferenceModifier() { 
      if (this.foodPreferenceModifier == null)
        return false;
      for (CodeableConcept item : this.foodPreferenceModifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #foodPreferenceModifier} (This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.)
     */
    // syntactic sugar
    public CodeableConcept addFoodPreferenceModifier() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.foodPreferenceModifier == null)
        this.foodPreferenceModifier = new ArrayList<CodeableConcept>();
      this.foodPreferenceModifier.add(t);
      return t;
    }

    /**
     * @return {@link #excludeFoodModifier} (This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.)
     */
    public List<CodeableConcept> getExcludeFoodModifier() { 
      if (this.excludeFoodModifier == null)
        this.excludeFoodModifier = new ArrayList<CodeableConcept>();
      return this.excludeFoodModifier;
    }

    public boolean hasExcludeFoodModifier() { 
      if (this.excludeFoodModifier == null)
        return false;
      for (CodeableConcept item : this.excludeFoodModifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #excludeFoodModifier} (This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.)
     */
    // syntactic sugar
    public CodeableConcept addExcludeFoodModifier() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.excludeFoodModifier == null)
        this.excludeFoodModifier = new ArrayList<CodeableConcept>();
      this.excludeFoodModifier.add(t);
      return t;
    }

    /**
     * @return {@link #oralDiet} (Class that defines the components of an oral diet order for the patient.)
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
     * @param value {@link #oralDiet} (Class that defines the components of an oral diet order for the patient.)
     */
    public NutritionOrder setOralDiet(NutritionOrderOralDietComponent value) { 
      this.oralDiet = value;
      return this;
    }

    /**
     * @return {@link #supplement} (Class that defines the components of a supplement order for the patient.)
     */
    public List<NutritionOrderSupplementComponent> getSupplement() { 
      if (this.supplement == null)
        this.supplement = new ArrayList<NutritionOrderSupplementComponent>();
      return this.supplement;
    }

    public boolean hasSupplement() { 
      if (this.supplement == null)
        return false;
      for (NutritionOrderSupplementComponent item : this.supplement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #supplement} (Class that defines the components of a supplement order for the patient.)
     */
    // syntactic sugar
    public NutritionOrderSupplementComponent addSupplement() { //3
      NutritionOrderSupplementComponent t = new NutritionOrderSupplementComponent();
      if (this.supplement == null)
        this.supplement = new ArrayList<NutritionOrderSupplementComponent>();
      this.supplement.add(t);
      return t;
    }

    /**
     * @return {@link #enteralFormula} (Class that defines the components of an enteral formula order for the patient.)
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
     * @param value {@link #enteralFormula} (Class that defines the components of an enteral formula order for the patient.)
     */
    public NutritionOrder setEnteralFormula(NutritionOrderEnteralFormulaComponent value) { 
      this.enteralFormula = value;
      return this;
    }

    /**
     * @return {@link #status} (The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public NutritionOrder setStatusElement(Enumeration<NutritionOrderStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
     */
    public NutritionOrderStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("patient", "Reference(Patient)", "The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("orderer", "Reference(Practitioner)", "The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.", 0, java.lang.Integer.MAX_VALUE, orderer));
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the order sender or by the order receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "An encounter that provides additional information about the healthcare context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("dateTime", "dateTime", "The date and time that this nutrition order was requested.", 0, java.lang.Integer.MAX_VALUE, dateTime));
        childrenList.add(new Property("allergyIntolerance", "Reference(AllergyIntolerance)", "The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.", 0, java.lang.Integer.MAX_VALUE, allergyIntolerance));
        childrenList.add(new Property("foodPreferenceModifier", "CodeableConcept", "This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.", 0, java.lang.Integer.MAX_VALUE, foodPreferenceModifier));
        childrenList.add(new Property("excludeFoodModifier", "CodeableConcept", "This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.", 0, java.lang.Integer.MAX_VALUE, excludeFoodModifier));
        childrenList.add(new Property("oralDiet", "", "Class that defines the components of an oral diet order for the patient.", 0, java.lang.Integer.MAX_VALUE, oralDiet));
        childrenList.add(new Property("supplement", "", "Class that defines the components of a supplement order for the patient.", 0, java.lang.Integer.MAX_VALUE, supplement));
        childrenList.add(new Property("enteralFormula", "", "Class that defines the components of an enteral formula order for the patient.", 0, java.lang.Integer.MAX_VALUE, enteralFormula));
        childrenList.add(new Property("status", "code", "The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.", 0, java.lang.Integer.MAX_VALUE, status));
      }

      public NutritionOrder copy() {
        NutritionOrder dst = new NutritionOrder();
        copyValues(dst);
        dst.patient = patient == null ? null : patient.copy();
        dst.orderer = orderer == null ? null : orderer.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
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
        dst.status = status == null ? null : status.copy();
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
        return compareDeep(patient, o.patient, true) && compareDeep(orderer, o.orderer, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(dateTime, o.dateTime, true) && compareDeep(allergyIntolerance, o.allergyIntolerance, true)
           && compareDeep(foodPreferenceModifier, o.foodPreferenceModifier, true) && compareDeep(excludeFoodModifier, o.excludeFoodModifier, true)
           && compareDeep(oralDiet, o.oralDiet, true) && compareDeep(supplement, o.supplement, true) && compareDeep(enteralFormula, o.enteralFormula, true)
           && compareDeep(status, o.status, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof NutritionOrder))
          return false;
        NutritionOrder o = (NutritionOrder) other;
        return compareValues(dateTime, o.dateTime, true) && compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (patient == null || patient.isEmpty()) && (orderer == null || orderer.isEmpty())
           && (identifier == null || identifier.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (dateTime == null || dateTime.isEmpty()) && (allergyIntolerance == null || allergyIntolerance.isEmpty())
           && (foodPreferenceModifier == null || foodPreferenceModifier.isEmpty()) && (excludeFoodModifier == null || excludeFoodModifier.isEmpty())
           && (oralDiet == null || oralDiet.isEmpty()) && (supplement == null || supplement.isEmpty())
           && (enteralFormula == null || enteralFormula.isEmpty()) && (status == null || status.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.NutritionOrder;
  }

  @SearchParamDefinition(name = "identifier", path = "NutritionOrder.identifier", description = "Return nutrition orders with this external identity", type = "token")
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name = "datetime", path = "NutritionOrder.dateTime", description = "Return nutrition orders requested on this date", type = "date")
  public static final String SP_DATETIME = "datetime";
  @SearchParamDefinition(name = "provider", path = "NutritionOrder.orderer", description = "The identify of the provider who placed the nutrition order", type = "reference")
  public static final String SP_PROVIDER = "provider";
  @SearchParamDefinition(name="patient", path="NutritionOrder.patient", description="The identity of the person who requires the diet, formula or nutritional supplement", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="supplement", path="NutritionOrder.supplement.type", description="Type of supplement product requested", type="token" )
  public static final String SP_SUPPLEMENT = "supplement";
  @SearchParamDefinition(name = "formula", path = "NutritionOrder.enteralFormula.baseFormulaType", description = "Type of enteral or infant formula", type = "token")
  public static final String SP_FORMULA = "formula";
  @SearchParamDefinition(name="encounter", path="NutritionOrder.encounter", description="Return nutrition orders with this encounter identity", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name = "oraldiet", path = "NutritionOrder.oralDiet.type", description = "Type of diet that can be consumed orally (i.e., take via the mouth).", type = "token")
  public static final String SP_ORALDIET = "oraldiet";
  @SearchParamDefinition(name = "status", path = "NutritionOrder.status", description = "Status of the nutrition order.", type = "token")
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="additive", path="NutritionOrder.enteralFormula.additiveType", description="Type of module component to add to the feeding", type="token" )
  public static final String SP_ADDITIVE = "additive";

}

