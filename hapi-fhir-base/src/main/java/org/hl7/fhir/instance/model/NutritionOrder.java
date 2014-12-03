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

// Generated on Tue, Dec 2, 2014 21:09+1100 for FHIR v0.3.0

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
         * TODO.
         */
        REQUESTED, 
        /**
         * TODO.
         */
        ACTIVE, 
        /**
         * TODO.
         */
        INACTIVE, 
        /**
         * TODO.
         */
        HELD, 
        /**
         * TODO.
         */
        CANCELLED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static NutritionOrderStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
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
            case REQUESTED: return "TODO.";
            case ACTIVE: return "TODO.";
            case INACTIVE: return "TODO.";
            case HELD: return "TODO.";
            case CANCELLED: return "TODO.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REQUESTED: return "Requested";
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case HELD: return "Held";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
        }
    }

  public static class NutritionOrderStatusEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
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
        throw new Exception("Unknown NutritionOrderStatus code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
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
    public static class NutritionOrderItemComponent extends BackboneElement {
        /**
         * The frequency at which the diet, oral supplement or enteral formula should be given.
         */
        @Child(name="scheduled", type={Timing.class, Period.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Frequency to offer nutrition item", formalDefinition="The frequency at which the diet, oral supplement or enteral formula should be given." )
        protected Type scheduled;

        /**
         * Indicates whether the nutrition item is  currently in effect for the patient.
         */
        @Child(name="isInEffect", type={BooleanType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Indicates whether the nutrition item is  currently in effect", formalDefinition="Indicates whether the nutrition item is  currently in effect for the patient." )
        protected BooleanType isInEffect;

        /**
         * Class that defines the components of an oral diet order for the patient.
         */
        @Child(name="oralDiet", type={}, order=3, min=0, max=1)
        @Description(shortDefinition="Oral diet components", formalDefinition="Class that defines the components of an oral diet order for the patient." )
        protected NutritionOrderItemOralDietComponent oralDiet;

        /**
         * Class that defines the components of a supplement order for the patient.
         */
        @Child(name="supplement", type={}, order=4, min=0, max=1)
        @Description(shortDefinition="Supplement components", formalDefinition="Class that defines the components of a supplement order for the patient." )
        protected NutritionOrderItemSupplementComponent supplement;

        /**
         * Class that defines the components of an enteral formula order for the patient.
         */
        @Child(name="enteralFormula", type={}, order=5, min=0, max=1)
        @Description(shortDefinition="Enteral formula components", formalDefinition="Class that defines the components of an enteral formula order for the patient." )
        protected NutritionOrderItemEnteralFormulaComponent enteralFormula;

        private static final long serialVersionUID = 2064921337L;

      public NutritionOrderItemComponent() {
        super();
      }

      public NutritionOrderItemComponent(BooleanType isInEffect) {
        super();
        this.isInEffect = isInEffect;
      }

        /**
         * @return {@link #scheduled} (The frequency at which the diet, oral supplement or enteral formula should be given.)
         */
        public Type getScheduled() { 
          return this.scheduled;
        }

        /**
         * @return {@link #scheduled} (The frequency at which the diet, oral supplement or enteral formula should be given.)
         */
        public Timing getScheduledTiming() throws Exception { 
          if (!(this.scheduled instanceof Timing))
            throw new Exception("Type mismatch: the type Timing was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Timing) this.scheduled;
        }

        /**
         * @return {@link #scheduled} (The frequency at which the diet, oral supplement or enteral formula should be given.)
         */
        public Period getScheduledPeriod() throws Exception { 
          if (!(this.scheduled instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.scheduled.getClass().getName()+" was encountered");
          return (Period) this.scheduled;
        }

        public boolean hasScheduled() { 
          return this.scheduled != null && !this.scheduled.isEmpty();
        }

        /**
         * @param value {@link #scheduled} (The frequency at which the diet, oral supplement or enteral formula should be given.)
         */
        public NutritionOrderItemComponent setScheduled(Type value) { 
          this.scheduled = value;
          return this;
        }

        /**
         * @return {@link #isInEffect} (Indicates whether the nutrition item is  currently in effect for the patient.). This is the underlying object with id, value and extensions. The accessor "getIsInEffect" gives direct access to the value
         */
        public BooleanType getIsInEffectElement() { 
          if (this.isInEffect == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemComponent.isInEffect");
            else if (Configuration.doAutoCreate())
              this.isInEffect = new BooleanType();
          return this.isInEffect;
        }

        public boolean hasIsInEffectElement() { 
          return this.isInEffect != null && !this.isInEffect.isEmpty();
        }

        public boolean hasIsInEffect() { 
          return this.isInEffect != null && !this.isInEffect.isEmpty();
        }

        /**
         * @param value {@link #isInEffect} (Indicates whether the nutrition item is  currently in effect for the patient.). This is the underlying object with id, value and extensions. The accessor "getIsInEffect" gives direct access to the value
         */
        public NutritionOrderItemComponent setIsInEffectElement(BooleanType value) { 
          this.isInEffect = value;
          return this;
        }

        /**
         * @return Indicates whether the nutrition item is  currently in effect for the patient.
         */
        public boolean getIsInEffect() { 
          return this.isInEffect == null ? false : this.isInEffect.getValue();
        }

        /**
         * @param value Indicates whether the nutrition item is  currently in effect for the patient.
         */
        public NutritionOrderItemComponent setIsInEffect(boolean value) { 
            if (this.isInEffect == null)
              this.isInEffect = new BooleanType();
            this.isInEffect.setValue(value);
          return this;
        }

        /**
         * @return {@link #oralDiet} (Class that defines the components of an oral diet order for the patient.)
         */
        public NutritionOrderItemOralDietComponent getOralDiet() { 
          if (this.oralDiet == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemComponent.oralDiet");
            else if (Configuration.doAutoCreate())
              this.oralDiet = new NutritionOrderItemOralDietComponent();
          return this.oralDiet;
        }

        public boolean hasOralDiet() { 
          return this.oralDiet != null && !this.oralDiet.isEmpty();
        }

        /**
         * @param value {@link #oralDiet} (Class that defines the components of an oral diet order for the patient.)
         */
        public NutritionOrderItemComponent setOralDiet(NutritionOrderItemOralDietComponent value) { 
          this.oralDiet = value;
          return this;
        }

        /**
         * @return {@link #supplement} (Class that defines the components of a supplement order for the patient.)
         */
        public NutritionOrderItemSupplementComponent getSupplement() { 
          if (this.supplement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemComponent.supplement");
            else if (Configuration.doAutoCreate())
              this.supplement = new NutritionOrderItemSupplementComponent();
          return this.supplement;
        }

        public boolean hasSupplement() { 
          return this.supplement != null && !this.supplement.isEmpty();
        }

        /**
         * @param value {@link #supplement} (Class that defines the components of a supplement order for the patient.)
         */
        public NutritionOrderItemComponent setSupplement(NutritionOrderItemSupplementComponent value) { 
          this.supplement = value;
          return this;
        }

        /**
         * @return {@link #enteralFormula} (Class that defines the components of an enteral formula order for the patient.)
         */
        public NutritionOrderItemEnteralFormulaComponent getEnteralFormula() { 
          if (this.enteralFormula == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemComponent.enteralFormula");
            else if (Configuration.doAutoCreate())
              this.enteralFormula = new NutritionOrderItemEnteralFormulaComponent();
          return this.enteralFormula;
        }

        public boolean hasEnteralFormula() { 
          return this.enteralFormula != null && !this.enteralFormula.isEmpty();
        }

        /**
         * @param value {@link #enteralFormula} (Class that defines the components of an enteral formula order for the patient.)
         */
        public NutritionOrderItemComponent setEnteralFormula(NutritionOrderItemEnteralFormulaComponent value) { 
          this.enteralFormula = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("scheduled[x]", "Timing|Period", "The frequency at which the diet, oral supplement or enteral formula should be given.", 0, java.lang.Integer.MAX_VALUE, scheduled));
          childrenList.add(new Property("isInEffect", "boolean", "Indicates whether the nutrition item is  currently in effect for the patient.", 0, java.lang.Integer.MAX_VALUE, isInEffect));
          childrenList.add(new Property("oralDiet", "", "Class that defines the components of an oral diet order for the patient.", 0, java.lang.Integer.MAX_VALUE, oralDiet));
          childrenList.add(new Property("supplement", "", "Class that defines the components of a supplement order for the patient.", 0, java.lang.Integer.MAX_VALUE, supplement));
          childrenList.add(new Property("enteralFormula", "", "Class that defines the components of an enteral formula order for the patient.", 0, java.lang.Integer.MAX_VALUE, enteralFormula));
        }

      public NutritionOrderItemComponent copy() {
        NutritionOrderItemComponent dst = new NutritionOrderItemComponent();
        copyValues(dst);
        dst.scheduled = scheduled == null ? null : scheduled.copy();
        dst.isInEffect = isInEffect == null ? null : isInEffect.copy();
        dst.oralDiet = oralDiet == null ? null : oralDiet.copy();
        dst.supplement = supplement == null ? null : supplement.copy();
        dst.enteralFormula = enteralFormula == null ? null : enteralFormula.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (scheduled == null || scheduled.isEmpty()) && (isInEffect == null || isInEffect.isEmpty())
           && (oralDiet == null || oralDiet.isEmpty()) && (supplement == null || supplement.isEmpty())
           && (enteralFormula == null || enteralFormula.isEmpty());
      }

  }

    @Block()
    public static class NutritionOrderItemOralDietComponent extends BackboneElement {
        /**
         * A set of one or more codes representing diets that describe what can be consumed orally (i.e., take via the mouth).
         */
        @Child(name="code", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="A set of one or more codes representing diets that describe what can be consumed orally (i.e., take via the mouth)", formalDefinition="A set of one or more codes representing diets that describe what can be consumed orally (i.e., take via the mouth)." )
        protected List<CodeableConcept> code;

        /**
         * Class that defines the details of any nutrient modifications required for the oral diet.
         */
        @Child(name="nutrients", type={}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Required  nutrient modifications", formalDefinition="Class that defines the details of any nutrient modifications required for the oral diet." )
        protected List<NutritionOrderItemOralDietNutrientsComponent> nutrients;

        /**
         * Class that describes any texture modifications required for the patient to safely consume various types of solid foods.
         */
        @Child(name="texture", type={}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Required  texture modifications", formalDefinition="Class that describes any texture modifications required for the patient to safely consume various types of solid foods." )
        protected List<NutritionOrderItemOralDietTextureComponent> texture;

        /**
         * Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.
         */
        @Child(name="fluidConsistencyType", type={CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="The required consistency of fluids and liquids provided to the patient", formalDefinition="Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient." )
        protected List<CodeableConcept> fluidConsistencyType;

        /**
         * A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth).
         */
        @Child(name="description", type={StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth)", formalDefinition="A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth)." )
        protected StringType description;

        private static final long serialVersionUID = 551143196L;

      public NutritionOrderItemOralDietComponent() {
        super();
      }

        /**
         * @return {@link #code} (A set of one or more codes representing diets that describe what can be consumed orally (i.e., take via the mouth).)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (CodeableConcept item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #code} (A set of one or more codes representing diets that describe what can be consumed orally (i.e., take via the mouth).)
         */
    // syntactic sugar
        public CodeableConcept addCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return t;
        }

        /**
         * @return {@link #nutrients} (Class that defines the details of any nutrient modifications required for the oral diet.)
         */
        public List<NutritionOrderItemOralDietNutrientsComponent> getNutrients() { 
          if (this.nutrients == null)
            this.nutrients = new ArrayList<NutritionOrderItemOralDietNutrientsComponent>();
          return this.nutrients;
        }

        public boolean hasNutrients() { 
          if (this.nutrients == null)
            return false;
          for (NutritionOrderItemOralDietNutrientsComponent item : this.nutrients)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #nutrients} (Class that defines the details of any nutrient modifications required for the oral diet.)
         */
    // syntactic sugar
        public NutritionOrderItemOralDietNutrientsComponent addNutrients() { //3
          NutritionOrderItemOralDietNutrientsComponent t = new NutritionOrderItemOralDietNutrientsComponent();
          if (this.nutrients == null)
            this.nutrients = new ArrayList<NutritionOrderItemOralDietNutrientsComponent>();
          this.nutrients.add(t);
          return t;
        }

        /**
         * @return {@link #texture} (Class that describes any texture modifications required for the patient to safely consume various types of solid foods.)
         */
        public List<NutritionOrderItemOralDietTextureComponent> getTexture() { 
          if (this.texture == null)
            this.texture = new ArrayList<NutritionOrderItemOralDietTextureComponent>();
          return this.texture;
        }

        public boolean hasTexture() { 
          if (this.texture == null)
            return false;
          for (NutritionOrderItemOralDietTextureComponent item : this.texture)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #texture} (Class that describes any texture modifications required for the patient to safely consume various types of solid foods.)
         */
    // syntactic sugar
        public NutritionOrderItemOralDietTextureComponent addTexture() { //3
          NutritionOrderItemOralDietTextureComponent t = new NutritionOrderItemOralDietTextureComponent();
          if (this.texture == null)
            this.texture = new ArrayList<NutritionOrderItemOralDietTextureComponent>();
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
         * @return {@link #description} (A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth).). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemOralDietComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType();
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth).). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public NutritionOrderItemOralDietComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth).
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth).
         */
        public NutritionOrderItemOralDietComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "A set of one or more codes representing diets that describe what can be consumed orally (i.e., take via the mouth).", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("nutrients", "", "Class that defines the details of any nutrient modifications required for the oral diet.", 0, java.lang.Integer.MAX_VALUE, nutrients));
          childrenList.add(new Property("texture", "", "Class that describes any texture modifications required for the patient to safely consume various types of solid foods.", 0, java.lang.Integer.MAX_VALUE, texture));
          childrenList.add(new Property("fluidConsistencyType", "CodeableConcept", "Identifies the required consistency (e.g., honey-thick, nectar-thick, thin, thickened.) of liquids or fluids served to the patient.", 0, java.lang.Integer.MAX_VALUE, fluidConsistencyType));
          childrenList.add(new Property("description", "string", "A descriptive name of the required diets that describe what can be consumed orally (i.e., take via the mouth).", 0, java.lang.Integer.MAX_VALUE, description));
        }

      public NutritionOrderItemOralDietComponent copy() {
        NutritionOrderItemOralDietComponent dst = new NutritionOrderItemOralDietComponent();
        copyValues(dst);
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        if (nutrients != null) {
          dst.nutrients = new ArrayList<NutritionOrderItemOralDietNutrientsComponent>();
          for (NutritionOrderItemOralDietNutrientsComponent i : nutrients)
            dst.nutrients.add(i.copy());
        };
        if (texture != null) {
          dst.texture = new ArrayList<NutritionOrderItemOralDietTextureComponent>();
          for (NutritionOrderItemOralDietTextureComponent i : texture)
            dst.texture.add(i.copy());
        };
        if (fluidConsistencyType != null) {
          dst.fluidConsistencyType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : fluidConsistencyType)
            dst.fluidConsistencyType.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (nutrients == null || nutrients.isEmpty())
           && (texture == null || texture.isEmpty()) && (fluidConsistencyType == null || fluidConsistencyType.isEmpty())
           && (description == null || description.isEmpty());
      }

  }

    @Block()
    public static class NutritionOrderItemOralDietNutrientsComponent extends BackboneElement {
        /**
         * Identifies the type of nutrient that is being modified such as cabohydrate or sodium.
         */
        @Child(name="modifier", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Type of nutrient that is being modified", formalDefinition="Identifies the type of nutrient that is being modified such as cabohydrate or sodium." )
        protected CodeableConcept modifier;

        /**
         * The quantity or range of the specified nutrient to supply.
         */
        @Child(name="amount", type={Quantity.class, Range.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Quantity of the specified nutrient", formalDefinition="The quantity or range of the specified nutrient to supply." )
        protected Type amount;

        private static final long serialVersionUID = -1359777156L;

      public NutritionOrderItemOralDietNutrientsComponent() {
        super();
      }

        /**
         * @return {@link #modifier} (Identifies the type of nutrient that is being modified such as cabohydrate or sodium.)
         */
        public CodeableConcept getModifier() { 
          if (this.modifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemOralDietNutrientsComponent.modifier");
            else if (Configuration.doAutoCreate())
              this.modifier = new CodeableConcept();
          return this.modifier;
        }

        public boolean hasModifier() { 
          return this.modifier != null && !this.modifier.isEmpty();
        }

        /**
         * @param value {@link #modifier} (Identifies the type of nutrient that is being modified such as cabohydrate or sodium.)
         */
        public NutritionOrderItemOralDietNutrientsComponent setModifier(CodeableConcept value) { 
          this.modifier = value;
          return this;
        }

        /**
         * @return {@link #amount} (The quantity or range of the specified nutrient to supply.)
         */
        public Type getAmount() { 
          return this.amount;
        }

        /**
         * @return {@link #amount} (The quantity or range of the specified nutrient to supply.)
         */
        public Quantity getAmountQuantity() throws Exception { 
          if (!(this.amount instanceof Quantity))
            throw new Exception("Type mismatch: the type Quantity was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Quantity) this.amount;
        }

        /**
         * @return {@link #amount} (The quantity or range of the specified nutrient to supply.)
         */
        public Range getAmountRange() throws Exception { 
          if (!(this.amount instanceof Range))
            throw new Exception("Type mismatch: the type Range was expected, but "+this.amount.getClass().getName()+" was encountered");
          return (Range) this.amount;
        }

        public boolean hasAmount() { 
          return this.amount != null && !this.amount.isEmpty();
        }

        /**
         * @param value {@link #amount} (The quantity or range of the specified nutrient to supply.)
         */
        public NutritionOrderItemOralDietNutrientsComponent setAmount(Type value) { 
          this.amount = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("modifier", "CodeableConcept", "Identifies the type of nutrient that is being modified such as cabohydrate or sodium.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("amount[x]", "Quantity|Range", "The quantity or range of the specified nutrient to supply.", 0, java.lang.Integer.MAX_VALUE, amount));
        }

      public NutritionOrderItemOralDietNutrientsComponent copy() {
        NutritionOrderItemOralDietNutrientsComponent dst = new NutritionOrderItemOralDietNutrientsComponent();
        copyValues(dst);
        dst.modifier = modifier == null ? null : modifier.copy();
        dst.amount = amount == null ? null : amount.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (modifier == null || modifier.isEmpty()) && (amount == null || amount.isEmpty())
          ;
      }

  }

    @Block()
    public static class NutritionOrderItemOralDietTextureComponent extends BackboneElement {
        /**
         * Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, pureed.
         */
        @Child(name="modifier", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Code to indicate how to alter the texture of the foods, e.g., pureed", formalDefinition="Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, pureed." )
        protected CodeableConcept modifier;

        /**
         * Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.
         */
        @Child(name="foodType", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Concepts that are used to identify an entity that is ingested for nutritional purposes", formalDefinition="Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet." )
        protected CodeableConcept foodType;

        private static final long serialVersionUID = -56402817L;

      public NutritionOrderItemOralDietTextureComponent() {
        super();
      }

        /**
         * @return {@link #modifier} (Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, pureed.)
         */
        public CodeableConcept getModifier() { 
          if (this.modifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemOralDietTextureComponent.modifier");
            else if (Configuration.doAutoCreate())
              this.modifier = new CodeableConcept();
          return this.modifier;
        }

        public boolean hasModifier() { 
          return this.modifier != null && !this.modifier.isEmpty();
        }

        /**
         * @param value {@link #modifier} (Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, pureed.)
         */
        public NutritionOrderItemOralDietTextureComponent setModifier(CodeableConcept value) { 
          this.modifier = value;
          return this;
        }

        /**
         * @return {@link #foodType} (Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.)
         */
        public CodeableConcept getFoodType() { 
          if (this.foodType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemOralDietTextureComponent.foodType");
            else if (Configuration.doAutoCreate())
              this.foodType = new CodeableConcept();
          return this.foodType;
        }

        public boolean hasFoodType() { 
          return this.foodType != null && !this.foodType.isEmpty();
        }

        /**
         * @param value {@link #foodType} (Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.)
         */
        public NutritionOrderItemOralDietTextureComponent setFoodType(CodeableConcept value) { 
          this.foodType = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("modifier", "CodeableConcept", "Identifies any texture modifications (for solid foods) that should be made, e.g. easy to chew, chopped, ground, pureed.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("foodType", "CodeableConcept", "Indicates what specific type of food (e.g., meats) the texture modification applies to or may apply to all foods in the diet.", 0, java.lang.Integer.MAX_VALUE, foodType));
        }

      public NutritionOrderItemOralDietTextureComponent copy() {
        NutritionOrderItemOralDietTextureComponent dst = new NutritionOrderItemOralDietTextureComponent();
        copyValues(dst);
        dst.modifier = modifier == null ? null : modifier.copy();
        dst.foodType = foodType == null ? null : foodType.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (modifier == null || modifier.isEmpty()) && (foodType == null || foodType.isEmpty())
          ;
      }

  }

    @Block()
    public static class NutritionOrderItemSupplementComponent extends BackboneElement {
        /**
         * Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Type of supplement product requested", formalDefinition="Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement." )
        protected List<CodeableConcept> type;

        /**
         * The amount of the nutritional supplement product to provide to the patient.
         */
        @Child(name="quantity", type={Quantity.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The amount of the nutritional supplement product to provide to the patient", formalDefinition="The amount of the nutritional supplement product to provide to the patient." )
        protected Quantity quantity;

        /**
         * The name of the nutritional supplement product to be provided to the patient.
         */
        @Child(name="name", type={StringType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="The name of the nutritional supplement product to be provided to the patient", formalDefinition="The name of the nutritional supplement product to be provided to the patient." )
        protected StringType name;

        private static final long serialVersionUID = -890013157L;

      public NutritionOrderItemSupplementComponent() {
        super();
      }

        /**
         * @return {@link #type} (Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.)
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
         * @return {@link #type} (Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.)
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
         * @return {@link #quantity} (The amount of the nutritional supplement product to provide to the patient.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemSupplementComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of the nutritional supplement product to provide to the patient.)
         */
        public NutritionOrderItemSupplementComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #name} (The name of the nutritional supplement product to be provided to the patient.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemSupplementComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType();
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of the nutritional supplement product to be provided to the patient.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public NutritionOrderItemSupplementComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the nutritional supplement product to be provided to the patient.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the nutritional supplement product to be provided to the patient.
         */
        public NutritionOrderItemSupplementComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of nutritional supplement product required such as high protein or pediatric clear liquid supplement.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("quantity", "Quantity", "The amount of the nutritional supplement product to provide to the patient.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("name", "string", "The name of the nutritional supplement product to be provided to the patient.", 0, java.lang.Integer.MAX_VALUE, name));
        }

      public NutritionOrderItemSupplementComponent copy() {
        NutritionOrderItemSupplementComponent dst = new NutritionOrderItemSupplementComponent();
        copyValues(dst);
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.name = name == null ? null : name.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (name == null || name.isEmpty());
      }

  }

    @Block()
    public static class NutritionOrderItemEnteralFormulaComponent extends BackboneElement {
        /**
         * Indicates the type of enteral or infant formula requested such as pediatric elemental formula or.
         */
        @Child(name="baseFormulaType", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Type of enteral or infant formula", formalDefinition="Indicates the type of enteral or infant formula requested such as pediatric elemental formula or." )
        protected CodeableConcept baseFormulaType;

        /**
         * Indicates the type of modular component such as protein, carbohydrate or fiber to be provided in addition to or mixed with the base formula.
         */
        @Child(name="additiveType", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Type of module component to add to the feeding", formalDefinition="Indicates the type of modular component such as protein, carbohydrate or fiber to be provided in addition to or mixed with the base formula." )
        protected List<CodeableConcept> additiveType;

        /**
         * TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.
         */
        @Child(name="caloricDensity", type={Quantity.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="TODO", formalDefinition="TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***." )
        protected List<Quantity> caloricDensity;

        /**
         * ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***administration details including rate (ml per hour), route of adminstration, total volume.
         */
        @Child(name="routeofAdministration", type={CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="TODO", formalDefinition="***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***administration details including rate (ml per hour), route of adminstration, total volume." )
        protected List<CodeableConcept> routeofAdministration;

        /**
         * TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.
         */
        @Child(name="rate", type={Quantity.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="TODO", formalDefinition="TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***." )
        protected List<Quantity> rate;

        /**
         * TODO.
         */
        @Child(name="baseFormulaName", type={StringType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="TODO", formalDefinition="TODO." )
        protected StringType baseFormulaName;

        private static final long serialVersionUID = -101595704L;

      public NutritionOrderItemEnteralFormulaComponent() {
        super();
      }

        /**
         * @return {@link #baseFormulaType} (Indicates the type of enteral or infant formula requested such as pediatric elemental formula or.)
         */
        public CodeableConcept getBaseFormulaType() { 
          if (this.baseFormulaType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemEnteralFormulaComponent.baseFormulaType");
            else if (Configuration.doAutoCreate())
              this.baseFormulaType = new CodeableConcept();
          return this.baseFormulaType;
        }

        public boolean hasBaseFormulaType() { 
          return this.baseFormulaType != null && !this.baseFormulaType.isEmpty();
        }

        /**
         * @param value {@link #baseFormulaType} (Indicates the type of enteral or infant formula requested such as pediatric elemental formula or.)
         */
        public NutritionOrderItemEnteralFormulaComponent setBaseFormulaType(CodeableConcept value) { 
          this.baseFormulaType = value;
          return this;
        }

        /**
         * @return {@link #additiveType} (Indicates the type of modular component such as protein, carbohydrate or fiber to be provided in addition to or mixed with the base formula.)
         */
        public List<CodeableConcept> getAdditiveType() { 
          if (this.additiveType == null)
            this.additiveType = new ArrayList<CodeableConcept>();
          return this.additiveType;
        }

        public boolean hasAdditiveType() { 
          if (this.additiveType == null)
            return false;
          for (CodeableConcept item : this.additiveType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #additiveType} (Indicates the type of modular component such as protein, carbohydrate or fiber to be provided in addition to or mixed with the base formula.)
         */
    // syntactic sugar
        public CodeableConcept addAdditiveType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.additiveType == null)
            this.additiveType = new ArrayList<CodeableConcept>();
          this.additiveType.add(t);
          return t;
        }

        /**
         * @return {@link #caloricDensity} (TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.)
         */
        public List<Quantity> getCaloricDensity() { 
          if (this.caloricDensity == null)
            this.caloricDensity = new ArrayList<Quantity>();
          return this.caloricDensity;
        }

        public boolean hasCaloricDensity() { 
          if (this.caloricDensity == null)
            return false;
          for (Quantity item : this.caloricDensity)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #caloricDensity} (TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.)
         */
    // syntactic sugar
        public Quantity addCaloricDensity() { //3
          Quantity t = new Quantity();
          if (this.caloricDensity == null)
            this.caloricDensity = new ArrayList<Quantity>();
          this.caloricDensity.add(t);
          return t;
        }

        /**
         * @return {@link #routeofAdministration} (***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***administration details including rate (ml per hour), route of adminstration, total volume.)
         */
        public List<CodeableConcept> getRouteofAdministration() { 
          if (this.routeofAdministration == null)
            this.routeofAdministration = new ArrayList<CodeableConcept>();
          return this.routeofAdministration;
        }

        public boolean hasRouteofAdministration() { 
          if (this.routeofAdministration == null)
            return false;
          for (CodeableConcept item : this.routeofAdministration)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #routeofAdministration} (***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***administration details including rate (ml per hour), route of adminstration, total volume.)
         */
    // syntactic sugar
        public CodeableConcept addRouteofAdministration() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.routeofAdministration == null)
            this.routeofAdministration = new ArrayList<CodeableConcept>();
          this.routeofAdministration.add(t);
          return t;
        }

        /**
         * @return {@link #rate} (TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.)
         */
        public List<Quantity> getRate() { 
          if (this.rate == null)
            this.rate = new ArrayList<Quantity>();
          return this.rate;
        }

        public boolean hasRate() { 
          if (this.rate == null)
            return false;
          for (Quantity item : this.rate)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #rate} (TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.)
         */
    // syntactic sugar
        public Quantity addRate() { //3
          Quantity t = new Quantity();
          if (this.rate == null)
            this.rate = new ArrayList<Quantity>();
          this.rate.add(t);
          return t;
        }

        /**
         * @return {@link #baseFormulaName} (TODO.). This is the underlying object with id, value and extensions. The accessor "getBaseFormulaName" gives direct access to the value
         */
        public StringType getBaseFormulaNameElement() { 
          if (this.baseFormulaName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create NutritionOrderItemEnteralFormulaComponent.baseFormulaName");
            else if (Configuration.doAutoCreate())
              this.baseFormulaName = new StringType();
          return this.baseFormulaName;
        }

        public boolean hasBaseFormulaNameElement() { 
          return this.baseFormulaName != null && !this.baseFormulaName.isEmpty();
        }

        public boolean hasBaseFormulaName() { 
          return this.baseFormulaName != null && !this.baseFormulaName.isEmpty();
        }

        /**
         * @param value {@link #baseFormulaName} (TODO.). This is the underlying object with id, value and extensions. The accessor "getBaseFormulaName" gives direct access to the value
         */
        public NutritionOrderItemEnteralFormulaComponent setBaseFormulaNameElement(StringType value) { 
          this.baseFormulaName = value;
          return this;
        }

        /**
         * @return TODO.
         */
        public String getBaseFormulaName() { 
          return this.baseFormulaName == null ? null : this.baseFormulaName.getValue();
        }

        /**
         * @param value TODO.
         */
        public NutritionOrderItemEnteralFormulaComponent setBaseFormulaName(String value) { 
          if (Utilities.noString(value))
            this.baseFormulaName = null;
          else {
            if (this.baseFormulaName == null)
              this.baseFormulaName = new StringType();
            this.baseFormulaName.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("baseFormulaType", "CodeableConcept", "Indicates the type of enteral or infant formula requested such as pediatric elemental formula or.", 0, java.lang.Integer.MAX_VALUE, baseFormulaType));
          childrenList.add(new Property("additiveType", "CodeableConcept", "Indicates the type of modular component such as protein, carbohydrate or fiber to be provided in addition to or mixed with the base formula.", 0, java.lang.Integer.MAX_VALUE, additiveType));
          childrenList.add(new Property("caloricDensity", "Quantity", "TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.", 0, java.lang.Integer.MAX_VALUE, caloricDensity));
          childrenList.add(new Property("routeofAdministration", "CodeableConcept", "***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***administration details including rate (ml per hour), route of adminstration, total volume.", 0, java.lang.Integer.MAX_VALUE, routeofAdministration));
          childrenList.add(new Property("rate", "Quantity", "TODO ***CARD AND TYPE ARE PLACEHOLDERS TO COMPLETE BUILD.  Need to discuss***.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("baseFormulaName", "string", "TODO.", 0, java.lang.Integer.MAX_VALUE, baseFormulaName));
        }

      public NutritionOrderItemEnteralFormulaComponent copy() {
        NutritionOrderItemEnteralFormulaComponent dst = new NutritionOrderItemEnteralFormulaComponent();
        copyValues(dst);
        dst.baseFormulaType = baseFormulaType == null ? null : baseFormulaType.copy();
        if (additiveType != null) {
          dst.additiveType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : additiveType)
            dst.additiveType.add(i.copy());
        };
        if (caloricDensity != null) {
          dst.caloricDensity = new ArrayList<Quantity>();
          for (Quantity i : caloricDensity)
            dst.caloricDensity.add(i.copy());
        };
        if (routeofAdministration != null) {
          dst.routeofAdministration = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : routeofAdministration)
            dst.routeofAdministration.add(i.copy());
        };
        if (rate != null) {
          dst.rate = new ArrayList<Quantity>();
          for (Quantity i : rate)
            dst.rate.add(i.copy());
        };
        dst.baseFormulaName = baseFormulaName == null ? null : baseFormulaName.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (baseFormulaType == null || baseFormulaType.isEmpty()) && (additiveType == null || additiveType.isEmpty())
           && (caloricDensity == null || caloricDensity.isEmpty()) && (routeofAdministration == null || routeofAdministration.isEmpty())
           && (rate == null || rate.isEmpty()) && (baseFormulaName == null || baseFormulaName.isEmpty())
          ;
      }

  }

    /**
     * The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.
     */
    @Child(name="subject", type={Patient.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="The person who requires the diet, formula or nutritional supplement", formalDefinition="The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    protected Patient subjectTarget;

    /**
     * The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.
     */
    @Child(name="orderer", type={Practitioner.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Who ordered the diet, formula or nutritional supplement", formalDefinition="The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings." )
    protected Reference orderer;

    /**
     * The actual object that is the target of the reference (The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.)
     */
    protected Practitioner ordererTarget;

    /**
     * Identifiers assigned to this order by the order sender or by the order receiver.
     */
    @Child(name="identifier", type={Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Identifiers assigned to this order", formalDefinition="Identifiers assigned to this order by the order sender or by the order receiver." )
    protected List<Identifier> identifier;

    /**
     * An encounter that provides additional informaton about the healthcare context in which this request is made.
     */
    @Child(name="encounter", type={Encounter.class}, order=2, min=0, max=1)
    @Description(shortDefinition="The encounter associated with that this nutrition order", formalDefinition="An encounter that provides additional informaton about the healthcare context in which this request is made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (An encounter that provides additional informaton about the healthcare context in which this request is made.)
     */
    protected Encounter encounterTarget;

    /**
     * The date and time that this nutrition order was requested.
     */
    @Child(name="dateTime", type={DateTimeType.class}, order=3, min=1, max=1)
    @Description(shortDefinition="Date and time the nutrition order was requested", formalDefinition="The date and time that this nutrition order was requested." )
    protected DateTimeType dateTime;

    /**
     * The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.
     */
    @Child(name="allergyIntolerance", type={AllergyIntolerance.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of the patient's food and nutrition-related allergies and intolerances", formalDefinition="The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order." )
    protected List<Reference> allergyIntolerance;
    /**
     * The actual objects that are the target of the reference (The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.)
     */
    protected List<AllergyIntolerance> allergyIntoleranceTarget;


    /**
     * This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     */
    @Child(name="foodPreferenceModifier", type={CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Order-specific modifier about the type of food that should be given", formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings." )
    protected List<CodeableConcept> foodPreferenceModifier;

    /**
     * This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.
     */
    @Child(name="excludeFoodModifier", type={CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Order-specific modifier about the type of food that should not be given", formalDefinition="This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings." )
    protected List<CodeableConcept> excludeFoodModifier;

    /**
     * Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.
     */
    @Child(name="item", type={}, order=7, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Set of nutrition items or components that comprise the nutrition order", formalDefinition="Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order." )
    protected List<NutritionOrderItemComponent> item;

    /**
     * The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.
     */
    @Child(name="status", type={CodeType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="requested | active | inactive | held | cancelled", formalDefinition="The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended." )
    protected Enumeration<NutritionOrderStatus> status;

    private static final long serialVersionUID = 1266509935L;

    public NutritionOrder() {
      super();
    }

    public NutritionOrder(Reference subject, DateTimeType dateTime) {
      super();
      this.subject = subject;
      this.dateTime = dateTime;
    }

    /**
     * @return {@link #subject} (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public NutritionOrder setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient();
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.)
     */
    public NutritionOrder setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
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
          this.orderer = new Reference();
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
          this.ordererTarget = new Practitioner();
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
     * @return {@link #encounter} (An encounter that provides additional informaton about the healthcare context in which this request is made.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference();
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (An encounter that provides additional informaton about the healthcare context in which this request is made.)
     */
    public NutritionOrder setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An encounter that provides additional informaton about the healthcare context in which this request is made.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter();
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An encounter that provides additional informaton about the healthcare context in which this request is made.)
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
          this.dateTime = new DateTimeType();
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
    public DateAndTime getDateTime() { 
      return this.dateTime == null ? null : this.dateTime.getValue();
    }

    /**
     * @param value The date and time that this nutrition order was requested.
     */
    public NutritionOrder setDateTime(DateAndTime value) { 
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
     * @return {@link #item} (Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.)
     */
    public List<NutritionOrderItemComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<NutritionOrderItemComponent>();
      return this.item;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (NutritionOrderItemComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #item} (Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.)
     */
    // syntactic sugar
    public NutritionOrderItemComponent addItem() { //3
      NutritionOrderItemComponent t = new NutritionOrderItemComponent();
      if (this.item == null)
        this.item = new ArrayList<NutritionOrderItemComponent>();
      this.item.add(t);
      return t;
    }

    /**
     * @return {@link #status} (The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<NutritionOrderStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create NutritionOrder.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<NutritionOrderStatus>();
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
          this.status = new Enumeration<NutritionOrderStatus>();
        this.status.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("subject", "Reference(Patient)", "The person (patient) who needs the nutrition order for an oral diet, nutritional supplement and/or enteral or formula feeding.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("orderer", "Reference(Practitioner)", "The practitioner that holds legal responsibility for ordering the diet, nutritional supplement, or formula feedings.", 0, java.lang.Integer.MAX_VALUE, orderer));
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this order by the order sender or by the order receiver.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "An encounter that provides additional informaton about the healthcare context in which this request is made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("dateTime", "dateTime", "The date and time that this nutrition order was requested.", 0, java.lang.Integer.MAX_VALUE, dateTime));
        childrenList.add(new Property("allergyIntolerance", "Reference(AllergyIntolerance)", "The ability to list substances that may cause allergies or intolerances which should be included in the nutrition order.", 0, java.lang.Integer.MAX_VALUE, allergyIntolerance));
        childrenList.add(new Property("foodPreferenceModifier", "CodeableConcept", "This modifier is used to convey order-specific modifiers about the type of food that should be given. These can be derived from patient allergies, intolerances, or preferences such as Halal, Vegan or Kosher. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.", 0, java.lang.Integer.MAX_VALUE, foodPreferenceModifier));
        childrenList.add(new Property("excludeFoodModifier", "CodeableConcept", "This modifier is used to convey order-specific modifiers about the type of food that should NOT be given. These can be derived from patient allergies, intolerances, or preferences such as No Red Meat, No Soy or No Wheat or  Gluten-Free. This modifier applies to the entire nutrition order inclusive of the oral diet, nutritional supplements and enteral formula feedings.", 0, java.lang.Integer.MAX_VALUE, excludeFoodModifier));
        childrenList.add(new Property("item", "", "Different items that combine to make a complete description of the nutrition to be provided via oral diet, nutritional supplement and/or formula order.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("status", "code", "The workflow status of the nutrition order request, e.g., Active, Inactive, Pending, Held, Canceled, Suspended.", 0, java.lang.Integer.MAX_VALUE, status));
      }

      public NutritionOrder copy() {
        NutritionOrder dst = new NutritionOrder();
        copyValues(dst);
        dst.subject = subject == null ? null : subject.copy();
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
        if (item != null) {
          dst.item = new ArrayList<NutritionOrderItemComponent>();
          for (NutritionOrderItemComponent i : item)
            dst.item.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        return dst;
      }

      protected NutritionOrder typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (subject == null || subject.isEmpty()) && (orderer == null || orderer.isEmpty())
           && (identifier == null || identifier.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (dateTime == null || dateTime.isEmpty()) && (allergyIntolerance == null || allergyIntolerance.isEmpty())
           && (foodPreferenceModifier == null || foodPreferenceModifier.isEmpty()) && (excludeFoodModifier == null || excludeFoodModifier.isEmpty())
           && (item == null || item.isEmpty()) && (status == null || status.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.NutritionOrder;
   }

  @SearchParamDefinition(name="patient", path="NutritionOrder.subject", description="The identity of the person who requires the diet, formula or nutritional supplement", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="status", path="NutritionOrder.status", description="Status of the nutrition order.", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="subject", path="NutritionOrder.subject", description="The identity of the person who requires the diet, formula or nutritional supplement", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="supplement", path="NutritionOrder.item.supplement.type", description="Type of supplement product requested", type="token" )
  public static final String SP_SUPPLEMENT = "supplement";
  @SearchParamDefinition(name="oraldiet", path="NutritionOrder.item.oralDiet.code", description="Code for diet that can be consumed orally (i.e., take via the mouth).", type="token" )
  public static final String SP_ORALDIET = "oraldiet";
  @SearchParamDefinition(name="provider", path="NutritionOrder.orderer", description="The identify of the provider who placed the nutrition order", type="reference" )
  public static final String SP_PROVIDER = "provider";
  @SearchParamDefinition(name="encounter", path="NutritionOrder.encounter", description="Return nutrition orders with this encounter identity", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="datetime", path="NutritionOrder.dateTime", description="Return nutrition orders requested on this date", type="date" )
  public static final String SP_DATETIME = "datetime";
  @SearchParamDefinition(name="additive", path="NutritionOrder.item.enteralFormula.additiveType", description="Type of module component to add to the feeding", type="token" )
  public static final String SP_ADDITIVE = "additive";
  @SearchParamDefinition(name="identifier", path="NutritionOrder.identifier", description="Return nutrition orders with this external identity", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="formula", path="NutritionOrder.item.enteralFormula.baseFormulaType", description="Type of enteral or infant formula", type="token" )
  public static final String SP_FORMULA = "formula";

}

