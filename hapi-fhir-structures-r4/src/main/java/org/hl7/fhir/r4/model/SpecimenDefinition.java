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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

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
 * A kind of specimen with associated set of requirements.
 */
@ResourceDef(name="SpecimenDefinition", profile="http://hl7.org/fhir/Profile/SpecimenDefinition")
public class SpecimenDefinition extends DomainResource {

    public enum SpecimenContainedPreference {
        /**
         * This type of contained specimen is preferred to collect this kind of specimen
         */
        PREFERRED, 
        /**
         * This type of conditioned specimen is an alternate
         */
        ALTERNATE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SpecimenContainedPreference fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preferred".equals(codeString))
          return PREFERRED;
        if ("alternate".equals(codeString))
          return ALTERNATE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SpecimenContainedPreference code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PREFERRED: return "preferred";
            case ALTERNATE: return "alternate";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PREFERRED: return "http://hl7.org/fhir/specimen-contained-preference";
            case ALTERNATE: return "http://hl7.org/fhir/specimen-contained-preference";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PREFERRED: return "This type of contained specimen is preferred to collect this kind of specimen";
            case ALTERNATE: return "This type of conditioned specimen is an alternate";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PREFERRED: return "Preferred";
            case ALTERNATE: return "Alternate";
            default: return "?";
          }
        }
    }

  public static class SpecimenContainedPreferenceEnumFactory implements EnumFactory<SpecimenContainedPreference> {
    public SpecimenContainedPreference fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preferred".equals(codeString))
          return SpecimenContainedPreference.PREFERRED;
        if ("alternate".equals(codeString))
          return SpecimenContainedPreference.ALTERNATE;
        throw new IllegalArgumentException("Unknown SpecimenContainedPreference code '"+codeString+"'");
        }
        public Enumeration<SpecimenContainedPreference> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SpecimenContainedPreference>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("preferred".equals(codeString))
          return new Enumeration<SpecimenContainedPreference>(this, SpecimenContainedPreference.PREFERRED);
        if ("alternate".equals(codeString))
          return new Enumeration<SpecimenContainedPreference>(this, SpecimenContainedPreference.ALTERNATE);
        throw new FHIRException("Unknown SpecimenContainedPreference code '"+codeString+"'");
        }
    public String toCode(SpecimenContainedPreference code) {
      if (code == SpecimenContainedPreference.PREFERRED)
        return "preferred";
      if (code == SpecimenContainedPreference.ALTERNATE)
        return "alternate";
      return "?";
      }
    public String toSystem(SpecimenContainedPreference code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class SpecimenDefinitionSpecimenToLabComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Primary of secondary specimen.
         */
        @Child(name = "isDerived", type = {BooleanType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Primary or secondary specimen", formalDefinition="Primary of secondary specimen." )
        protected BooleanType isDerived;

        /**
         * The kind of specimen conditioned for testing expected by lab.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of intended specimen", formalDefinition="The kind of specimen conditioned for testing expected by lab." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v2-0487")
        protected CodeableConcept type;

        /**
         * The preference for this type of conditioned specimen.
         */
        @Child(name = "preference", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="preferred | alternate", formalDefinition="The preference for this type of conditioned specimen." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/specimen-contained-preference")
        protected Enumeration<SpecimenContainedPreference> preference;

        /**
         * The type of material of the container.
         */
        @Child(name = "containerMaterial", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Container material", formalDefinition="The type of material of the container." )
        protected CodeableConcept containerMaterial;

        /**
         * The type of container used to contain this kind of specimen.
         */
        @Child(name = "containerType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Kind of container associated with the kind of specimen", formalDefinition="The type of container used to contain this kind of specimen." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/specimen-container-type")
        protected CodeableConcept containerType;

        /**
         * Color of container cap.
         */
        @Child(name = "containerCap", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Color of container cap", formalDefinition="Color of container cap." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/container-cap")
        protected CodeableConcept containerCap;

        /**
         * The textual description of the kind of container.
         */
        @Child(name = "containerDescription", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Container description", formalDefinition="The textual description of the kind of container." )
        protected StringType containerDescription;

        /**
         * The capacity (volume or other measure) of this kind of container.
         */
        @Child(name = "containerCapacity", type = {SimpleQuantity.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Container capacity", formalDefinition="The capacity (volume or other measure) of this kind of container." )
        protected SimpleQuantity containerCapacity;

        /**
         * The minimum volume to be conditioned in the container.
         */
        @Child(name = "containerMinimumVolume", type = {SimpleQuantity.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Minimum volume", formalDefinition="The minimum volume to be conditioned in the container." )
        protected SimpleQuantity containerMinimumVolume;

        /**
         * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.
         */
        @Child(name = "containerAdditive", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additive associated with container", formalDefinition="Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA." )
        protected List<SpecimenDefinitionSpecimenToLabContainerAdditiveComponent> containerAdditive;

        /**
         * Special processing that should be applied to the container for this kind of specimen.
         */
        @Child(name = "containerPreparation", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specimen container preparation", formalDefinition="Special processing that should be applied to the container for this kind of specimen." )
        protected StringType containerPreparation;

        /**
         * Requirements for delivery and special handling of this kind of conditioned specimen.
         */
        @Child(name = "requirement", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specimen requirements", formalDefinition="Requirements for delivery and special handling of this kind of conditioned specimen." )
        protected StringType requirement;

        /**
         * The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.
         */
        @Child(name = "retentionTime", type = {Duration.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specimen retention time", formalDefinition="The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing." )
        protected Duration retentionTime;

        /**
         * Criterion for rejection of the specimen in its container by the laboratory.
         */
        @Child(name = "rejectionCriterion", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Rejection criterion", formalDefinition="Criterion for rejection of the specimen in its container by the laboratory." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/rejection-criteria")
        protected List<CodeableConcept> rejectionCriterion;

        /**
         * Set of instructions for conservation/transport of the specimen at a defined temperature interval, prior the testing process.
         */
        @Child(name = "handling", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Specimen handling before testing", formalDefinition="Set of instructions for conservation/transport of the specimen at a defined temperature interval, prior the testing process." )
        protected List<SpecimenDefinitionSpecimenToLabHandlingComponent> handling;

        private static final long serialVersionUID = -239351274L;

    /**
     * Constructor
     */
      public SpecimenDefinitionSpecimenToLabComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SpecimenDefinitionSpecimenToLabComponent(BooleanType isDerived, Enumeration<SpecimenContainedPreference> preference) {
        super();
        this.isDerived = isDerived;
        this.preference = preference;
      }

        /**
         * @return {@link #isDerived} (Primary of secondary specimen.). This is the underlying object with id, value and extensions. The accessor "getIsDerived" gives direct access to the value
         */
        public BooleanType getIsDerivedElement() { 
          if (this.isDerived == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.isDerived");
            else if (Configuration.doAutoCreate())
              this.isDerived = new BooleanType(); // bb
          return this.isDerived;
        }

        public boolean hasIsDerivedElement() { 
          return this.isDerived != null && !this.isDerived.isEmpty();
        }

        public boolean hasIsDerived() { 
          return this.isDerived != null && !this.isDerived.isEmpty();
        }

        /**
         * @param value {@link #isDerived} (Primary of secondary specimen.). This is the underlying object with id, value and extensions. The accessor "getIsDerived" gives direct access to the value
         */
        public SpecimenDefinitionSpecimenToLabComponent setIsDerivedElement(BooleanType value) { 
          this.isDerived = value;
          return this;
        }

        /**
         * @return Primary of secondary specimen.
         */
        public boolean getIsDerived() { 
          return this.isDerived == null || this.isDerived.isEmpty() ? false : this.isDerived.getValue();
        }

        /**
         * @param value Primary of secondary specimen.
         */
        public SpecimenDefinitionSpecimenToLabComponent setIsDerived(boolean value) { 
            if (this.isDerived == null)
              this.isDerived = new BooleanType();
            this.isDerived.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The kind of specimen conditioned for testing expected by lab.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The kind of specimen conditioned for testing expected by lab.)
         */
        public SpecimenDefinitionSpecimenToLabComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #preference} (The preference for this type of conditioned specimen.). This is the underlying object with id, value and extensions. The accessor "getPreference" gives direct access to the value
         */
        public Enumeration<SpecimenContainedPreference> getPreferenceElement() { 
          if (this.preference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.preference");
            else if (Configuration.doAutoCreate())
              this.preference = new Enumeration<SpecimenContainedPreference>(new SpecimenContainedPreferenceEnumFactory()); // bb
          return this.preference;
        }

        public boolean hasPreferenceElement() { 
          return this.preference != null && !this.preference.isEmpty();
        }

        public boolean hasPreference() { 
          return this.preference != null && !this.preference.isEmpty();
        }

        /**
         * @param value {@link #preference} (The preference for this type of conditioned specimen.). This is the underlying object with id, value and extensions. The accessor "getPreference" gives direct access to the value
         */
        public SpecimenDefinitionSpecimenToLabComponent setPreferenceElement(Enumeration<SpecimenContainedPreference> value) { 
          this.preference = value;
          return this;
        }

        /**
         * @return The preference for this type of conditioned specimen.
         */
        public SpecimenContainedPreference getPreference() { 
          return this.preference == null ? null : this.preference.getValue();
        }

        /**
         * @param value The preference for this type of conditioned specimen.
         */
        public SpecimenDefinitionSpecimenToLabComponent setPreference(SpecimenContainedPreference value) { 
            if (this.preference == null)
              this.preference = new Enumeration<SpecimenContainedPreference>(new SpecimenContainedPreferenceEnumFactory());
            this.preference.setValue(value);
          return this;
        }

        /**
         * @return {@link #containerMaterial} (The type of material of the container.)
         */
        public CodeableConcept getContainerMaterial() { 
          if (this.containerMaterial == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.containerMaterial");
            else if (Configuration.doAutoCreate())
              this.containerMaterial = new CodeableConcept(); // cc
          return this.containerMaterial;
        }

        public boolean hasContainerMaterial() { 
          return this.containerMaterial != null && !this.containerMaterial.isEmpty();
        }

        /**
         * @param value {@link #containerMaterial} (The type of material of the container.)
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerMaterial(CodeableConcept value) { 
          this.containerMaterial = value;
          return this;
        }

        /**
         * @return {@link #containerType} (The type of container used to contain this kind of specimen.)
         */
        public CodeableConcept getContainerType() { 
          if (this.containerType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.containerType");
            else if (Configuration.doAutoCreate())
              this.containerType = new CodeableConcept(); // cc
          return this.containerType;
        }

        public boolean hasContainerType() { 
          return this.containerType != null && !this.containerType.isEmpty();
        }

        /**
         * @param value {@link #containerType} (The type of container used to contain this kind of specimen.)
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerType(CodeableConcept value) { 
          this.containerType = value;
          return this;
        }

        /**
         * @return {@link #containerCap} (Color of container cap.)
         */
        public CodeableConcept getContainerCap() { 
          if (this.containerCap == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.containerCap");
            else if (Configuration.doAutoCreate())
              this.containerCap = new CodeableConcept(); // cc
          return this.containerCap;
        }

        public boolean hasContainerCap() { 
          return this.containerCap != null && !this.containerCap.isEmpty();
        }

        /**
         * @param value {@link #containerCap} (Color of container cap.)
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerCap(CodeableConcept value) { 
          this.containerCap = value;
          return this;
        }

        /**
         * @return {@link #containerDescription} (The textual description of the kind of container.). This is the underlying object with id, value and extensions. The accessor "getContainerDescription" gives direct access to the value
         */
        public StringType getContainerDescriptionElement() { 
          if (this.containerDescription == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.containerDescription");
            else if (Configuration.doAutoCreate())
              this.containerDescription = new StringType(); // bb
          return this.containerDescription;
        }

        public boolean hasContainerDescriptionElement() { 
          return this.containerDescription != null && !this.containerDescription.isEmpty();
        }

        public boolean hasContainerDescription() { 
          return this.containerDescription != null && !this.containerDescription.isEmpty();
        }

        /**
         * @param value {@link #containerDescription} (The textual description of the kind of container.). This is the underlying object with id, value and extensions. The accessor "getContainerDescription" gives direct access to the value
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerDescriptionElement(StringType value) { 
          this.containerDescription = value;
          return this;
        }

        /**
         * @return The textual description of the kind of container.
         */
        public String getContainerDescription() { 
          return this.containerDescription == null ? null : this.containerDescription.getValue();
        }

        /**
         * @param value The textual description of the kind of container.
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerDescription(String value) { 
          if (Utilities.noString(value))
            this.containerDescription = null;
          else {
            if (this.containerDescription == null)
              this.containerDescription = new StringType();
            this.containerDescription.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #containerCapacity} (The capacity (volume or other measure) of this kind of container.)
         */
        public SimpleQuantity getContainerCapacity() { 
          if (this.containerCapacity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.containerCapacity");
            else if (Configuration.doAutoCreate())
              this.containerCapacity = new SimpleQuantity(); // cc
          return this.containerCapacity;
        }

        public boolean hasContainerCapacity() { 
          return this.containerCapacity != null && !this.containerCapacity.isEmpty();
        }

        /**
         * @param value {@link #containerCapacity} (The capacity (volume or other measure) of this kind of container.)
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerCapacity(SimpleQuantity value) { 
          this.containerCapacity = value;
          return this;
        }

        /**
         * @return {@link #containerMinimumVolume} (The minimum volume to be conditioned in the container.)
         */
        public SimpleQuantity getContainerMinimumVolume() { 
          if (this.containerMinimumVolume == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.containerMinimumVolume");
            else if (Configuration.doAutoCreate())
              this.containerMinimumVolume = new SimpleQuantity(); // cc
          return this.containerMinimumVolume;
        }

        public boolean hasContainerMinimumVolume() { 
          return this.containerMinimumVolume != null && !this.containerMinimumVolume.isEmpty();
        }

        /**
         * @param value {@link #containerMinimumVolume} (The minimum volume to be conditioned in the container.)
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerMinimumVolume(SimpleQuantity value) { 
          this.containerMinimumVolume = value;
          return this;
        }

        /**
         * @return {@link #containerAdditive} (Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public List<SpecimenDefinitionSpecimenToLabContainerAdditiveComponent> getContainerAdditive() { 
          if (this.containerAdditive == null)
            this.containerAdditive = new ArrayList<SpecimenDefinitionSpecimenToLabContainerAdditiveComponent>();
          return this.containerAdditive;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerAdditive(List<SpecimenDefinitionSpecimenToLabContainerAdditiveComponent> theContainerAdditive) { 
          this.containerAdditive = theContainerAdditive;
          return this;
        }

        public boolean hasContainerAdditive() { 
          if (this.containerAdditive == null)
            return false;
          for (SpecimenDefinitionSpecimenToLabContainerAdditiveComponent item : this.containerAdditive)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SpecimenDefinitionSpecimenToLabContainerAdditiveComponent addContainerAdditive() { //3
          SpecimenDefinitionSpecimenToLabContainerAdditiveComponent t = new SpecimenDefinitionSpecimenToLabContainerAdditiveComponent();
          if (this.containerAdditive == null)
            this.containerAdditive = new ArrayList<SpecimenDefinitionSpecimenToLabContainerAdditiveComponent>();
          this.containerAdditive.add(t);
          return t;
        }

        public SpecimenDefinitionSpecimenToLabComponent addContainerAdditive(SpecimenDefinitionSpecimenToLabContainerAdditiveComponent t) { //3
          if (t == null)
            return this;
          if (this.containerAdditive == null)
            this.containerAdditive = new ArrayList<SpecimenDefinitionSpecimenToLabContainerAdditiveComponent>();
          this.containerAdditive.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #containerAdditive}, creating it if it does not already exist
         */
        public SpecimenDefinitionSpecimenToLabContainerAdditiveComponent getContainerAdditiveFirstRep() { 
          if (getContainerAdditive().isEmpty()) {
            addContainerAdditive();
          }
          return getContainerAdditive().get(0);
        }

        /**
         * @return {@link #containerPreparation} (Special processing that should be applied to the container for this kind of specimen.). This is the underlying object with id, value and extensions. The accessor "getContainerPreparation" gives direct access to the value
         */
        public StringType getContainerPreparationElement() { 
          if (this.containerPreparation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.containerPreparation");
            else if (Configuration.doAutoCreate())
              this.containerPreparation = new StringType(); // bb
          return this.containerPreparation;
        }

        public boolean hasContainerPreparationElement() { 
          return this.containerPreparation != null && !this.containerPreparation.isEmpty();
        }

        public boolean hasContainerPreparation() { 
          return this.containerPreparation != null && !this.containerPreparation.isEmpty();
        }

        /**
         * @param value {@link #containerPreparation} (Special processing that should be applied to the container for this kind of specimen.). This is the underlying object with id, value and extensions. The accessor "getContainerPreparation" gives direct access to the value
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerPreparationElement(StringType value) { 
          this.containerPreparation = value;
          return this;
        }

        /**
         * @return Special processing that should be applied to the container for this kind of specimen.
         */
        public String getContainerPreparation() { 
          return this.containerPreparation == null ? null : this.containerPreparation.getValue();
        }

        /**
         * @param value Special processing that should be applied to the container for this kind of specimen.
         */
        public SpecimenDefinitionSpecimenToLabComponent setContainerPreparation(String value) { 
          if (Utilities.noString(value))
            this.containerPreparation = null;
          else {
            if (this.containerPreparation == null)
              this.containerPreparation = new StringType();
            this.containerPreparation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #requirement} (Requirements for delivery and special handling of this kind of conditioned specimen.). This is the underlying object with id, value and extensions. The accessor "getRequirement" gives direct access to the value
         */
        public StringType getRequirementElement() { 
          if (this.requirement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.requirement");
            else if (Configuration.doAutoCreate())
              this.requirement = new StringType(); // bb
          return this.requirement;
        }

        public boolean hasRequirementElement() { 
          return this.requirement != null && !this.requirement.isEmpty();
        }

        public boolean hasRequirement() { 
          return this.requirement != null && !this.requirement.isEmpty();
        }

        /**
         * @param value {@link #requirement} (Requirements for delivery and special handling of this kind of conditioned specimen.). This is the underlying object with id, value and extensions. The accessor "getRequirement" gives direct access to the value
         */
        public SpecimenDefinitionSpecimenToLabComponent setRequirementElement(StringType value) { 
          this.requirement = value;
          return this;
        }

        /**
         * @return Requirements for delivery and special handling of this kind of conditioned specimen.
         */
        public String getRequirement() { 
          return this.requirement == null ? null : this.requirement.getValue();
        }

        /**
         * @param value Requirements for delivery and special handling of this kind of conditioned specimen.
         */
        public SpecimenDefinitionSpecimenToLabComponent setRequirement(String value) { 
          if (Utilities.noString(value))
            this.requirement = null;
          else {
            if (this.requirement == null)
              this.requirement = new StringType();
            this.requirement.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #retentionTime} (The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.)
         */
        public Duration getRetentionTime() { 
          if (this.retentionTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabComponent.retentionTime");
            else if (Configuration.doAutoCreate())
              this.retentionTime = new Duration(); // cc
          return this.retentionTime;
        }

        public boolean hasRetentionTime() { 
          return this.retentionTime != null && !this.retentionTime.isEmpty();
        }

        /**
         * @param value {@link #retentionTime} (The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.)
         */
        public SpecimenDefinitionSpecimenToLabComponent setRetentionTime(Duration value) { 
          this.retentionTime = value;
          return this;
        }

        /**
         * @return {@link #rejectionCriterion} (Criterion for rejection of the specimen in its container by the laboratory.)
         */
        public List<CodeableConcept> getRejectionCriterion() { 
          if (this.rejectionCriterion == null)
            this.rejectionCriterion = new ArrayList<CodeableConcept>();
          return this.rejectionCriterion;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SpecimenDefinitionSpecimenToLabComponent setRejectionCriterion(List<CodeableConcept> theRejectionCriterion) { 
          this.rejectionCriterion = theRejectionCriterion;
          return this;
        }

        public boolean hasRejectionCriterion() { 
          if (this.rejectionCriterion == null)
            return false;
          for (CodeableConcept item : this.rejectionCriterion)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addRejectionCriterion() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.rejectionCriterion == null)
            this.rejectionCriterion = new ArrayList<CodeableConcept>();
          this.rejectionCriterion.add(t);
          return t;
        }

        public SpecimenDefinitionSpecimenToLabComponent addRejectionCriterion(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.rejectionCriterion == null)
            this.rejectionCriterion = new ArrayList<CodeableConcept>();
          this.rejectionCriterion.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #rejectionCriterion}, creating it if it does not already exist
         */
        public CodeableConcept getRejectionCriterionFirstRep() { 
          if (getRejectionCriterion().isEmpty()) {
            addRejectionCriterion();
          }
          return getRejectionCriterion().get(0);
        }

        /**
         * @return {@link #handling} (Set of instructions for conservation/transport of the specimen at a defined temperature interval, prior the testing process.)
         */
        public List<SpecimenDefinitionSpecimenToLabHandlingComponent> getHandling() { 
          if (this.handling == null)
            this.handling = new ArrayList<SpecimenDefinitionSpecimenToLabHandlingComponent>();
          return this.handling;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SpecimenDefinitionSpecimenToLabComponent setHandling(List<SpecimenDefinitionSpecimenToLabHandlingComponent> theHandling) { 
          this.handling = theHandling;
          return this;
        }

        public boolean hasHandling() { 
          if (this.handling == null)
            return false;
          for (SpecimenDefinitionSpecimenToLabHandlingComponent item : this.handling)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SpecimenDefinitionSpecimenToLabHandlingComponent addHandling() { //3
          SpecimenDefinitionSpecimenToLabHandlingComponent t = new SpecimenDefinitionSpecimenToLabHandlingComponent();
          if (this.handling == null)
            this.handling = new ArrayList<SpecimenDefinitionSpecimenToLabHandlingComponent>();
          this.handling.add(t);
          return t;
        }

        public SpecimenDefinitionSpecimenToLabComponent addHandling(SpecimenDefinitionSpecimenToLabHandlingComponent t) { //3
          if (t == null)
            return this;
          if (this.handling == null)
            this.handling = new ArrayList<SpecimenDefinitionSpecimenToLabHandlingComponent>();
          this.handling.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #handling}, creating it if it does not already exist
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent getHandlingFirstRep() { 
          if (getHandling().isEmpty()) {
            addHandling();
          }
          return getHandling().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("isDerived", "boolean", "Primary of secondary specimen.", 0, 1, isDerived));
          children.add(new Property("type", "CodeableConcept", "The kind of specimen conditioned for testing expected by lab.", 0, 1, type));
          children.add(new Property("preference", "code", "The preference for this type of conditioned specimen.", 0, 1, preference));
          children.add(new Property("containerMaterial", "CodeableConcept", "The type of material of the container.", 0, 1, containerMaterial));
          children.add(new Property("containerType", "CodeableConcept", "The type of container used to contain this kind of specimen.", 0, 1, containerType));
          children.add(new Property("containerCap", "CodeableConcept", "Color of container cap.", 0, 1, containerCap));
          children.add(new Property("containerDescription", "string", "The textual description of the kind of container.", 0, 1, containerDescription));
          children.add(new Property("containerCapacity", "SimpleQuantity", "The capacity (volume or other measure) of this kind of container.", 0, 1, containerCapacity));
          children.add(new Property("containerMinimumVolume", "SimpleQuantity", "The minimum volume to be conditioned in the container.", 0, 1, containerMinimumVolume));
          children.add(new Property("containerAdditive", "", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, java.lang.Integer.MAX_VALUE, containerAdditive));
          children.add(new Property("containerPreparation", "string", "Special processing that should be applied to the container for this kind of specimen.", 0, 1, containerPreparation));
          children.add(new Property("requirement", "string", "Requirements for delivery and special handling of this kind of conditioned specimen.", 0, 1, requirement));
          children.add(new Property("retentionTime", "Duration", "The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.", 0, 1, retentionTime));
          children.add(new Property("rejectionCriterion", "CodeableConcept", "Criterion for rejection of the specimen in its container by the laboratory.", 0, java.lang.Integer.MAX_VALUE, rejectionCriterion));
          children.add(new Property("handling", "", "Set of instructions for conservation/transport of the specimen at a defined temperature interval, prior the testing process.", 0, java.lang.Integer.MAX_VALUE, handling));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 976346515: /*isDerived*/  return new Property("isDerived", "boolean", "Primary of secondary specimen.", 0, 1, isDerived);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The kind of specimen conditioned for testing expected by lab.", 0, 1, type);
          case -1459831589: /*preference*/  return new Property("preference", "code", "The preference for this type of conditioned specimen.", 0, 1, preference);
          case -207682360: /*containerMaterial*/  return new Property("containerMaterial", "CodeableConcept", "The type of material of the container.", 0, 1, containerMaterial);
          case 1966942043: /*containerType*/  return new Property("containerType", "CodeableConcept", "The type of container used to contain this kind of specimen.", 0, 1, containerType);
          case 2141642641: /*containerCap*/  return new Property("containerCap", "CodeableConcept", "Color of container cap.", 0, 1, containerCap);
          case -1474644805: /*containerDescription*/  return new Property("containerDescription", "string", "The textual description of the kind of container.", 0, 1, containerDescription);
          case -574573477: /*containerCapacity*/  return new Property("containerCapacity", "SimpleQuantity", "The capacity (volume or other measure) of this kind of container.", 0, 1, containerCapacity);
          case -2037735993: /*containerMinimumVolume*/  return new Property("containerMinimumVolume", "SimpleQuantity", "The minimum volume to be conditioned in the container.", 0, 1, containerMinimumVolume);
          case -1733338259: /*containerAdditive*/  return new Property("containerAdditive", "", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, java.lang.Integer.MAX_VALUE, containerAdditive);
          case -1065527466: /*containerPreparation*/  return new Property("containerPreparation", "string", "Special processing that should be applied to the container for this kind of specimen.", 0, 1, containerPreparation);
          case 363387971: /*requirement*/  return new Property("requirement", "string", "Requirements for delivery and special handling of this kind of conditioned specimen.", 0, 1, requirement);
          case 1434969867: /*retentionTime*/  return new Property("retentionTime", "Duration", "The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.", 0, 1, retentionTime);
          case -553706344: /*rejectionCriterion*/  return new Property("rejectionCriterion", "CodeableConcept", "Criterion for rejection of the specimen in its container by the laboratory.", 0, java.lang.Integer.MAX_VALUE, rejectionCriterion);
          case 2072805: /*handling*/  return new Property("handling", "", "Set of instructions for conservation/transport of the specimen at a defined temperature interval, prior the testing process.", 0, java.lang.Integer.MAX_VALUE, handling);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 976346515: /*isDerived*/ return this.isDerived == null ? new Base[0] : new Base[] {this.isDerived}; // BooleanType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1459831589: /*preference*/ return this.preference == null ? new Base[0] : new Base[] {this.preference}; // Enumeration<SpecimenContainedPreference>
        case -207682360: /*containerMaterial*/ return this.containerMaterial == null ? new Base[0] : new Base[] {this.containerMaterial}; // CodeableConcept
        case 1966942043: /*containerType*/ return this.containerType == null ? new Base[0] : new Base[] {this.containerType}; // CodeableConcept
        case 2141642641: /*containerCap*/ return this.containerCap == null ? new Base[0] : new Base[] {this.containerCap}; // CodeableConcept
        case -1474644805: /*containerDescription*/ return this.containerDescription == null ? new Base[0] : new Base[] {this.containerDescription}; // StringType
        case -574573477: /*containerCapacity*/ return this.containerCapacity == null ? new Base[0] : new Base[] {this.containerCapacity}; // SimpleQuantity
        case -2037735993: /*containerMinimumVolume*/ return this.containerMinimumVolume == null ? new Base[0] : new Base[] {this.containerMinimumVolume}; // SimpleQuantity
        case -1733338259: /*containerAdditive*/ return this.containerAdditive == null ? new Base[0] : this.containerAdditive.toArray(new Base[this.containerAdditive.size()]); // SpecimenDefinitionSpecimenToLabContainerAdditiveComponent
        case -1065527466: /*containerPreparation*/ return this.containerPreparation == null ? new Base[0] : new Base[] {this.containerPreparation}; // StringType
        case 363387971: /*requirement*/ return this.requirement == null ? new Base[0] : new Base[] {this.requirement}; // StringType
        case 1434969867: /*retentionTime*/ return this.retentionTime == null ? new Base[0] : new Base[] {this.retentionTime}; // Duration
        case -553706344: /*rejectionCriterion*/ return this.rejectionCriterion == null ? new Base[0] : this.rejectionCriterion.toArray(new Base[this.rejectionCriterion.size()]); // CodeableConcept
        case 2072805: /*handling*/ return this.handling == null ? new Base[0] : this.handling.toArray(new Base[this.handling.size()]); // SpecimenDefinitionSpecimenToLabHandlingComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 976346515: // isDerived
          this.isDerived = castToBoolean(value); // BooleanType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1459831589: // preference
          value = new SpecimenContainedPreferenceEnumFactory().fromType(castToCode(value));
          this.preference = (Enumeration) value; // Enumeration<SpecimenContainedPreference>
          return value;
        case -207682360: // containerMaterial
          this.containerMaterial = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1966942043: // containerType
          this.containerType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 2141642641: // containerCap
          this.containerCap = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1474644805: // containerDescription
          this.containerDescription = castToString(value); // StringType
          return value;
        case -574573477: // containerCapacity
          this.containerCapacity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -2037735993: // containerMinimumVolume
          this.containerMinimumVolume = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -1733338259: // containerAdditive
          this.getContainerAdditive().add((SpecimenDefinitionSpecimenToLabContainerAdditiveComponent) value); // SpecimenDefinitionSpecimenToLabContainerAdditiveComponent
          return value;
        case -1065527466: // containerPreparation
          this.containerPreparation = castToString(value); // StringType
          return value;
        case 363387971: // requirement
          this.requirement = castToString(value); // StringType
          return value;
        case 1434969867: // retentionTime
          this.retentionTime = castToDuration(value); // Duration
          return value;
        case -553706344: // rejectionCriterion
          this.getRejectionCriterion().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 2072805: // handling
          this.getHandling().add((SpecimenDefinitionSpecimenToLabHandlingComponent) value); // SpecimenDefinitionSpecimenToLabHandlingComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("isDerived")) {
          this.isDerived = castToBoolean(value); // BooleanType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("preference")) {
          value = new SpecimenContainedPreferenceEnumFactory().fromType(castToCode(value));
          this.preference = (Enumeration) value; // Enumeration<SpecimenContainedPreference>
        } else if (name.equals("containerMaterial")) {
          this.containerMaterial = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("containerType")) {
          this.containerType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("containerCap")) {
          this.containerCap = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("containerDescription")) {
          this.containerDescription = castToString(value); // StringType
        } else if (name.equals("containerCapacity")) {
          this.containerCapacity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("containerMinimumVolume")) {
          this.containerMinimumVolume = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("containerAdditive")) {
          this.getContainerAdditive().add((SpecimenDefinitionSpecimenToLabContainerAdditiveComponent) value);
        } else if (name.equals("containerPreparation")) {
          this.containerPreparation = castToString(value); // StringType
        } else if (name.equals("requirement")) {
          this.requirement = castToString(value); // StringType
        } else if (name.equals("retentionTime")) {
          this.retentionTime = castToDuration(value); // Duration
        } else if (name.equals("rejectionCriterion")) {
          this.getRejectionCriterion().add(castToCodeableConcept(value));
        } else if (name.equals("handling")) {
          this.getHandling().add((SpecimenDefinitionSpecimenToLabHandlingComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 976346515:  return getIsDerivedElement();
        case 3575610:  return getType(); 
        case -1459831589:  return getPreferenceElement();
        case -207682360:  return getContainerMaterial(); 
        case 1966942043:  return getContainerType(); 
        case 2141642641:  return getContainerCap(); 
        case -1474644805:  return getContainerDescriptionElement();
        case -574573477:  return getContainerCapacity(); 
        case -2037735993:  return getContainerMinimumVolume(); 
        case -1733338259:  return addContainerAdditive(); 
        case -1065527466:  return getContainerPreparationElement();
        case 363387971:  return getRequirementElement();
        case 1434969867:  return getRetentionTime(); 
        case -553706344:  return addRejectionCriterion(); 
        case 2072805:  return addHandling(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 976346515: /*isDerived*/ return new String[] {"boolean"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1459831589: /*preference*/ return new String[] {"code"};
        case -207682360: /*containerMaterial*/ return new String[] {"CodeableConcept"};
        case 1966942043: /*containerType*/ return new String[] {"CodeableConcept"};
        case 2141642641: /*containerCap*/ return new String[] {"CodeableConcept"};
        case -1474644805: /*containerDescription*/ return new String[] {"string"};
        case -574573477: /*containerCapacity*/ return new String[] {"SimpleQuantity"};
        case -2037735993: /*containerMinimumVolume*/ return new String[] {"SimpleQuantity"};
        case -1733338259: /*containerAdditive*/ return new String[] {};
        case -1065527466: /*containerPreparation*/ return new String[] {"string"};
        case 363387971: /*requirement*/ return new String[] {"string"};
        case 1434969867: /*retentionTime*/ return new String[] {"Duration"};
        case -553706344: /*rejectionCriterion*/ return new String[] {"CodeableConcept"};
        case 2072805: /*handling*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("isDerived")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.isDerived");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("preference")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.preference");
        }
        else if (name.equals("containerMaterial")) {
          this.containerMaterial = new CodeableConcept();
          return this.containerMaterial;
        }
        else if (name.equals("containerType")) {
          this.containerType = new CodeableConcept();
          return this.containerType;
        }
        else if (name.equals("containerCap")) {
          this.containerCap = new CodeableConcept();
          return this.containerCap;
        }
        else if (name.equals("containerDescription")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.containerDescription");
        }
        else if (name.equals("containerCapacity")) {
          this.containerCapacity = new SimpleQuantity();
          return this.containerCapacity;
        }
        else if (name.equals("containerMinimumVolume")) {
          this.containerMinimumVolume = new SimpleQuantity();
          return this.containerMinimumVolume;
        }
        else if (name.equals("containerAdditive")) {
          return addContainerAdditive();
        }
        else if (name.equals("containerPreparation")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.containerPreparation");
        }
        else if (name.equals("requirement")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.requirement");
        }
        else if (name.equals("retentionTime")) {
          this.retentionTime = new Duration();
          return this.retentionTime;
        }
        else if (name.equals("rejectionCriterion")) {
          return addRejectionCriterion();
        }
        else if (name.equals("handling")) {
          return addHandling();
        }
        else
          return super.addChild(name);
      }

      public SpecimenDefinitionSpecimenToLabComponent copy() {
        SpecimenDefinitionSpecimenToLabComponent dst = new SpecimenDefinitionSpecimenToLabComponent();
        copyValues(dst);
        dst.isDerived = isDerived == null ? null : isDerived.copy();
        dst.type = type == null ? null : type.copy();
        dst.preference = preference == null ? null : preference.copy();
        dst.containerMaterial = containerMaterial == null ? null : containerMaterial.copy();
        dst.containerType = containerType == null ? null : containerType.copy();
        dst.containerCap = containerCap == null ? null : containerCap.copy();
        dst.containerDescription = containerDescription == null ? null : containerDescription.copy();
        dst.containerCapacity = containerCapacity == null ? null : containerCapacity.copy();
        dst.containerMinimumVolume = containerMinimumVolume == null ? null : containerMinimumVolume.copy();
        if (containerAdditive != null) {
          dst.containerAdditive = new ArrayList<SpecimenDefinitionSpecimenToLabContainerAdditiveComponent>();
          for (SpecimenDefinitionSpecimenToLabContainerAdditiveComponent i : containerAdditive)
            dst.containerAdditive.add(i.copy());
        };
        dst.containerPreparation = containerPreparation == null ? null : containerPreparation.copy();
        dst.requirement = requirement == null ? null : requirement.copy();
        dst.retentionTime = retentionTime == null ? null : retentionTime.copy();
        if (rejectionCriterion != null) {
          dst.rejectionCriterion = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : rejectionCriterion)
            dst.rejectionCriterion.add(i.copy());
        };
        if (handling != null) {
          dst.handling = new ArrayList<SpecimenDefinitionSpecimenToLabHandlingComponent>();
          for (SpecimenDefinitionSpecimenToLabHandlingComponent i : handling)
            dst.handling.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionSpecimenToLabComponent))
          return false;
        SpecimenDefinitionSpecimenToLabComponent o = (SpecimenDefinitionSpecimenToLabComponent) other_;
        return compareDeep(isDerived, o.isDerived, true) && compareDeep(type, o.type, true) && compareDeep(preference, o.preference, true)
           && compareDeep(containerMaterial, o.containerMaterial, true) && compareDeep(containerType, o.containerType, true)
           && compareDeep(containerCap, o.containerCap, true) && compareDeep(containerDescription, o.containerDescription, true)
           && compareDeep(containerCapacity, o.containerCapacity, true) && compareDeep(containerMinimumVolume, o.containerMinimumVolume, true)
           && compareDeep(containerAdditive, o.containerAdditive, true) && compareDeep(containerPreparation, o.containerPreparation, true)
           && compareDeep(requirement, o.requirement, true) && compareDeep(retentionTime, o.retentionTime, true)
           && compareDeep(rejectionCriterion, o.rejectionCriterion, true) && compareDeep(handling, o.handling, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionSpecimenToLabComponent))
          return false;
        SpecimenDefinitionSpecimenToLabComponent o = (SpecimenDefinitionSpecimenToLabComponent) other_;
        return compareValues(isDerived, o.isDerived, true) && compareValues(preference, o.preference, true)
           && compareValues(containerDescription, o.containerDescription, true) && compareValues(containerPreparation, o.containerPreparation, true)
           && compareValues(requirement, o.requirement, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(isDerived, type, preference
          , containerMaterial, containerType, containerCap, containerDescription, containerCapacity
          , containerMinimumVolume, containerAdditive, containerPreparation, requirement, retentionTime
          , rejectionCriterion, handling);
      }

  public String fhirType() {
    return "SpecimenDefinition.specimenToLab";

  }

  }

    @Block()
    public static class SpecimenDefinitionSpecimenToLabContainerAdditiveComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.
         */
        @Child(name = "additive", type = {CodeableConcept.class, Substance.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additive associated with container", formalDefinition="Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v2-0371")
        protected Type additive;

        private static final long serialVersionUID = 1819209272L;

    /**
     * Constructor
     */
      public SpecimenDefinitionSpecimenToLabContainerAdditiveComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SpecimenDefinitionSpecimenToLabContainerAdditiveComponent(Type additive) {
        super();
        this.additive = additive;
      }

        /**
         * @return {@link #additive} (Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public Type getAdditive() { 
          return this.additive;
        }

        /**
         * @return {@link #additive} (Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public CodeableConcept getAdditiveCodeableConcept() throws FHIRException { 
          if (this.additive == null)
            return null;
          if (!(this.additive instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.additive.getClass().getName()+" was encountered");
          return (CodeableConcept) this.additive;
        }

        public boolean hasAdditiveCodeableConcept() { 
          return this != null && this.additive instanceof CodeableConcept;
        }

        /**
         * @return {@link #additive} (Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public Reference getAdditiveReference() throws FHIRException { 
          if (this.additive == null)
            return null;
          if (!(this.additive instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.additive.getClass().getName()+" was encountered");
          return (Reference) this.additive;
        }

        public boolean hasAdditiveReference() { 
          return this != null && this.additive instanceof Reference;
        }

        public boolean hasAdditive() { 
          return this.additive != null && !this.additive.isEmpty();
        }

        /**
         * @param value {@link #additive} (Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public SpecimenDefinitionSpecimenToLabContainerAdditiveComponent setAdditive(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for SpecimenDefinition.specimenToLab.containerAdditive.additive[x]: "+value.fhirType());
          this.additive = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("additive[x]", "CodeableConcept|Reference(Substance)", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, 1, additive));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 261915956: /*additive[x]*/  return new Property("additive[x]", "CodeableConcept|Reference(Substance)", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, 1, additive);
          case -1226589236: /*additive*/  return new Property("additive[x]", "CodeableConcept|Reference(Substance)", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, 1, additive);
          case 1330272821: /*additiveCodeableConcept*/  return new Property("additive[x]", "CodeableConcept|Reference(Substance)", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, 1, additive);
          case -386783009: /*additiveReference*/  return new Property("additive[x]", "CodeableConcept|Reference(Substance)", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, 1, additive);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1226589236: /*additive*/ return this.additive == null ? new Base[0] : new Base[] {this.additive}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1226589236: // additive
          this.additive = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("additive[x]")) {
          this.additive = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 261915956:  return getAdditive(); 
        case -1226589236:  return getAdditive(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1226589236: /*additive*/ return new String[] {"CodeableConcept", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("additiveCodeableConcept")) {
          this.additive = new CodeableConcept();
          return this.additive;
        }
        else if (name.equals("additiveReference")) {
          this.additive = new Reference();
          return this.additive;
        }
        else
          return super.addChild(name);
      }

      public SpecimenDefinitionSpecimenToLabContainerAdditiveComponent copy() {
        SpecimenDefinitionSpecimenToLabContainerAdditiveComponent dst = new SpecimenDefinitionSpecimenToLabContainerAdditiveComponent();
        copyValues(dst);
        dst.additive = additive == null ? null : additive.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionSpecimenToLabContainerAdditiveComponent))
          return false;
        SpecimenDefinitionSpecimenToLabContainerAdditiveComponent o = (SpecimenDefinitionSpecimenToLabContainerAdditiveComponent) other_;
        return compareDeep(additive, o.additive, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionSpecimenToLabContainerAdditiveComponent))
          return false;
        SpecimenDefinitionSpecimenToLabContainerAdditiveComponent o = (SpecimenDefinitionSpecimenToLabContainerAdditiveComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(additive);
      }

  public String fhirType() {
    return "SpecimenDefinition.specimenToLab.containerAdditive";

  }

  }

    @Block()
    public static class SpecimenDefinitionSpecimenToLabHandlingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Code representing the set of handling instructions.
         */
        @Child(name = "conditionSet", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Conservation condition set", formalDefinition="Code representing the set of handling instructions." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/handling-condition")
        protected CodeableConcept conditionSet;

        /**
         * The temperature interval for this set of handling instructions.
         */
        @Child(name = "tempRange", type = {Range.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Temperature range", formalDefinition="The temperature interval for this set of handling instructions." )
        protected Range tempRange;

        /**
         * The maximum time interval of conservation of the specimen with these conditions.
         */
        @Child(name = "maxDuration", type = {Duration.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Maximum conservation time", formalDefinition="The maximum time interval of conservation of the specimen with these conditions." )
        protected Duration maxDuration;

        /**
         * Textual instructions regarding the light exposure of the specimen prior testing.
         */
        @Child(name = "lightExposure", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Light exposure", formalDefinition="Textual instructions regarding the light exposure of the specimen prior testing." )
        protected StringType lightExposure;

        /**
         * Additional textual instructions for the conservation or transport of the specimen.
         */
        @Child(name = "instruction", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Conservation instruction", formalDefinition="Additional textual instructions for the conservation or transport of the specimen." )
        protected StringType instruction;

        private static final long serialVersionUID = 1577777957L;

    /**
     * Constructor
     */
      public SpecimenDefinitionSpecimenToLabHandlingComponent() {
        super();
      }

        /**
         * @return {@link #conditionSet} (Code representing the set of handling instructions.)
         */
        public CodeableConcept getConditionSet() { 
          if (this.conditionSet == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabHandlingComponent.conditionSet");
            else if (Configuration.doAutoCreate())
              this.conditionSet = new CodeableConcept(); // cc
          return this.conditionSet;
        }

        public boolean hasConditionSet() { 
          return this.conditionSet != null && !this.conditionSet.isEmpty();
        }

        /**
         * @param value {@link #conditionSet} (Code representing the set of handling instructions.)
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent setConditionSet(CodeableConcept value) { 
          this.conditionSet = value;
          return this;
        }

        /**
         * @return {@link #tempRange} (The temperature interval for this set of handling instructions.)
         */
        public Range getTempRange() { 
          if (this.tempRange == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabHandlingComponent.tempRange");
            else if (Configuration.doAutoCreate())
              this.tempRange = new Range(); // cc
          return this.tempRange;
        }

        public boolean hasTempRange() { 
          return this.tempRange != null && !this.tempRange.isEmpty();
        }

        /**
         * @param value {@link #tempRange} (The temperature interval for this set of handling instructions.)
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent setTempRange(Range value) { 
          this.tempRange = value;
          return this;
        }

        /**
         * @return {@link #maxDuration} (The maximum time interval of conservation of the specimen with these conditions.)
         */
        public Duration getMaxDuration() { 
          if (this.maxDuration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabHandlingComponent.maxDuration");
            else if (Configuration.doAutoCreate())
              this.maxDuration = new Duration(); // cc
          return this.maxDuration;
        }

        public boolean hasMaxDuration() { 
          return this.maxDuration != null && !this.maxDuration.isEmpty();
        }

        /**
         * @param value {@link #maxDuration} (The maximum time interval of conservation of the specimen with these conditions.)
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent setMaxDuration(Duration value) { 
          this.maxDuration = value;
          return this;
        }

        /**
         * @return {@link #lightExposure} (Textual instructions regarding the light exposure of the specimen prior testing.). This is the underlying object with id, value and extensions. The accessor "getLightExposure" gives direct access to the value
         */
        public StringType getLightExposureElement() { 
          if (this.lightExposure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabHandlingComponent.lightExposure");
            else if (Configuration.doAutoCreate())
              this.lightExposure = new StringType(); // bb
          return this.lightExposure;
        }

        public boolean hasLightExposureElement() { 
          return this.lightExposure != null && !this.lightExposure.isEmpty();
        }

        public boolean hasLightExposure() { 
          return this.lightExposure != null && !this.lightExposure.isEmpty();
        }

        /**
         * @param value {@link #lightExposure} (Textual instructions regarding the light exposure of the specimen prior testing.). This is the underlying object with id, value and extensions. The accessor "getLightExposure" gives direct access to the value
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent setLightExposureElement(StringType value) { 
          this.lightExposure = value;
          return this;
        }

        /**
         * @return Textual instructions regarding the light exposure of the specimen prior testing.
         */
        public String getLightExposure() { 
          return this.lightExposure == null ? null : this.lightExposure.getValue();
        }

        /**
         * @param value Textual instructions regarding the light exposure of the specimen prior testing.
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent setLightExposure(String value) { 
          if (Utilities.noString(value))
            this.lightExposure = null;
          else {
            if (this.lightExposure == null)
              this.lightExposure = new StringType();
            this.lightExposure.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #instruction} (Additional textual instructions for the conservation or transport of the specimen.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public StringType getInstructionElement() { 
          if (this.instruction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionSpecimenToLabHandlingComponent.instruction");
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
         * @param value {@link #instruction} (Additional textual instructions for the conservation or transport of the specimen.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent setInstructionElement(StringType value) { 
          this.instruction = value;
          return this;
        }

        /**
         * @return Additional textual instructions for the conservation or transport of the specimen.
         */
        public String getInstruction() { 
          return this.instruction == null ? null : this.instruction.getValue();
        }

        /**
         * @param value Additional textual instructions for the conservation or transport of the specimen.
         */
        public SpecimenDefinitionSpecimenToLabHandlingComponent setInstruction(String value) { 
          if (Utilities.noString(value))
            this.instruction = null;
          else {
            if (this.instruction == null)
              this.instruction = new StringType();
            this.instruction.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("conditionSet", "CodeableConcept", "Code representing the set of handling instructions.", 0, 1, conditionSet));
          children.add(new Property("tempRange", "Range", "The temperature interval for this set of handling instructions.", 0, 1, tempRange));
          children.add(new Property("maxDuration", "Duration", "The maximum time interval of conservation of the specimen with these conditions.", 0, 1, maxDuration));
          children.add(new Property("lightExposure", "string", "Textual instructions regarding the light exposure of the specimen prior testing.", 0, 1, lightExposure));
          children.add(new Property("instruction", "string", "Additional textual instructions for the conservation or transport of the specimen.", 0, 1, instruction));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1202651833: /*conditionSet*/  return new Property("conditionSet", "CodeableConcept", "Code representing the set of handling instructions.", 0, 1, conditionSet);
          case 1957710281: /*tempRange*/  return new Property("tempRange", "Range", "The temperature interval for this set of handling instructions.", 0, 1, tempRange);
          case 40284952: /*maxDuration*/  return new Property("maxDuration", "Duration", "The maximum time interval of conservation of the specimen with these conditions.", 0, 1, maxDuration);
          case -1391615939: /*lightExposure*/  return new Property("lightExposure", "string", "Textual instructions regarding the light exposure of the specimen prior testing.", 0, 1, lightExposure);
          case 301526158: /*instruction*/  return new Property("instruction", "string", "Additional textual instructions for the conservation or transport of the specimen.", 0, 1, instruction);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1202651833: /*conditionSet*/ return this.conditionSet == null ? new Base[0] : new Base[] {this.conditionSet}; // CodeableConcept
        case 1957710281: /*tempRange*/ return this.tempRange == null ? new Base[0] : new Base[] {this.tempRange}; // Range
        case 40284952: /*maxDuration*/ return this.maxDuration == null ? new Base[0] : new Base[] {this.maxDuration}; // Duration
        case -1391615939: /*lightExposure*/ return this.lightExposure == null ? new Base[0] : new Base[] {this.lightExposure}; // StringType
        case 301526158: /*instruction*/ return this.instruction == null ? new Base[0] : new Base[] {this.instruction}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1202651833: // conditionSet
          this.conditionSet = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1957710281: // tempRange
          this.tempRange = castToRange(value); // Range
          return value;
        case 40284952: // maxDuration
          this.maxDuration = castToDuration(value); // Duration
          return value;
        case -1391615939: // lightExposure
          this.lightExposure = castToString(value); // StringType
          return value;
        case 301526158: // instruction
          this.instruction = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("conditionSet")) {
          this.conditionSet = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("tempRange")) {
          this.tempRange = castToRange(value); // Range
        } else if (name.equals("maxDuration")) {
          this.maxDuration = castToDuration(value); // Duration
        } else if (name.equals("lightExposure")) {
          this.lightExposure = castToString(value); // StringType
        } else if (name.equals("instruction")) {
          this.instruction = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1202651833:  return getConditionSet(); 
        case 1957710281:  return getTempRange(); 
        case 40284952:  return getMaxDuration(); 
        case -1391615939:  return getLightExposureElement();
        case 301526158:  return getInstructionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1202651833: /*conditionSet*/ return new String[] {"CodeableConcept"};
        case 1957710281: /*tempRange*/ return new String[] {"Range"};
        case 40284952: /*maxDuration*/ return new String[] {"Duration"};
        case -1391615939: /*lightExposure*/ return new String[] {"string"};
        case 301526158: /*instruction*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("conditionSet")) {
          this.conditionSet = new CodeableConcept();
          return this.conditionSet;
        }
        else if (name.equals("tempRange")) {
          this.tempRange = new Range();
          return this.tempRange;
        }
        else if (name.equals("maxDuration")) {
          this.maxDuration = new Duration();
          return this.maxDuration;
        }
        else if (name.equals("lightExposure")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.lightExposure");
        }
        else if (name.equals("instruction")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.instruction");
        }
        else
          return super.addChild(name);
      }

      public SpecimenDefinitionSpecimenToLabHandlingComponent copy() {
        SpecimenDefinitionSpecimenToLabHandlingComponent dst = new SpecimenDefinitionSpecimenToLabHandlingComponent();
        copyValues(dst);
        dst.conditionSet = conditionSet == null ? null : conditionSet.copy();
        dst.tempRange = tempRange == null ? null : tempRange.copy();
        dst.maxDuration = maxDuration == null ? null : maxDuration.copy();
        dst.lightExposure = lightExposure == null ? null : lightExposure.copy();
        dst.instruction = instruction == null ? null : instruction.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionSpecimenToLabHandlingComponent))
          return false;
        SpecimenDefinitionSpecimenToLabHandlingComponent o = (SpecimenDefinitionSpecimenToLabHandlingComponent) other_;
        return compareDeep(conditionSet, o.conditionSet, true) && compareDeep(tempRange, o.tempRange, true)
           && compareDeep(maxDuration, o.maxDuration, true) && compareDeep(lightExposure, o.lightExposure, true)
           && compareDeep(instruction, o.instruction, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionSpecimenToLabHandlingComponent))
          return false;
        SpecimenDefinitionSpecimenToLabHandlingComponent o = (SpecimenDefinitionSpecimenToLabHandlingComponent) other_;
        return compareValues(lightExposure, o.lightExposure, true) && compareValues(instruction, o.instruction, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(conditionSet, tempRange, maxDuration
          , lightExposure, instruction);
      }

  public String fhirType() {
    return "SpecimenDefinition.specimenToLab.handling";

  }

  }

    /**
     * A business identifier associated with the kind of specimen.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier of a kind of specimen", formalDefinition="A business identifier associated with the kind of specimen." )
    protected Identifier identifier;

    /**
     * The kind of material to be collected.
     */
    @Child(name = "typeCollected", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Kind of material to collect", formalDefinition="The kind of material to be collected." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v2-0487")
    protected CodeableConcept typeCollected;

    /**
     * Preparation of the patient for specimen collection.
     */
    @Child(name = "patientPreparation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Patient preparation for collection", formalDefinition="Preparation of the patient for specimen collection." )
    protected StringType patientPreparation;

    /**
     * Time aspect of specimen collection (duration or offset).
     */
    @Child(name = "timeAspect", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time aspect for collection", formalDefinition="Time aspect of specimen collection (duration or offset)." )
    protected StringType timeAspect;

    /**
     * The action to be performed for collecting the specimen.
     */
    @Child(name = "collection", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Specimen collection procedure", formalDefinition="The action to be performed for collecting the specimen." )
    protected List<CodeableConcept> collection;

    /**
     * Specimen conditioned in a container as expected by the testing laboratory.
     */
    @Child(name = "specimenToLab", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specimen in container intended for testing by lab", formalDefinition="Specimen conditioned in a container as expected by the testing laboratory." )
    protected List<SpecimenDefinitionSpecimenToLabComponent> specimenToLab;

    private static final long serialVersionUID = -2007444482L;

  /**
   * Constructor
   */
    public SpecimenDefinition() {
      super();
    }

    /**
     * @return {@link #identifier} (A business identifier associated with the kind of specimen.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SpecimenDefinition.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (A business identifier associated with the kind of specimen.)
     */
    public SpecimenDefinition setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #typeCollected} (The kind of material to be collected.)
     */
    public CodeableConcept getTypeCollected() { 
      if (this.typeCollected == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SpecimenDefinition.typeCollected");
        else if (Configuration.doAutoCreate())
          this.typeCollected = new CodeableConcept(); // cc
      return this.typeCollected;
    }

    public boolean hasTypeCollected() { 
      return this.typeCollected != null && !this.typeCollected.isEmpty();
    }

    /**
     * @param value {@link #typeCollected} (The kind of material to be collected.)
     */
    public SpecimenDefinition setTypeCollected(CodeableConcept value) { 
      this.typeCollected = value;
      return this;
    }

    /**
     * @return {@link #patientPreparation} (Preparation of the patient for specimen collection.). This is the underlying object with id, value and extensions. The accessor "getPatientPreparation" gives direct access to the value
     */
    public StringType getPatientPreparationElement() { 
      if (this.patientPreparation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SpecimenDefinition.patientPreparation");
        else if (Configuration.doAutoCreate())
          this.patientPreparation = new StringType(); // bb
      return this.patientPreparation;
    }

    public boolean hasPatientPreparationElement() { 
      return this.patientPreparation != null && !this.patientPreparation.isEmpty();
    }

    public boolean hasPatientPreparation() { 
      return this.patientPreparation != null && !this.patientPreparation.isEmpty();
    }

    /**
     * @param value {@link #patientPreparation} (Preparation of the patient for specimen collection.). This is the underlying object with id, value and extensions. The accessor "getPatientPreparation" gives direct access to the value
     */
    public SpecimenDefinition setPatientPreparationElement(StringType value) { 
      this.patientPreparation = value;
      return this;
    }

    /**
     * @return Preparation of the patient for specimen collection.
     */
    public String getPatientPreparation() { 
      return this.patientPreparation == null ? null : this.patientPreparation.getValue();
    }

    /**
     * @param value Preparation of the patient for specimen collection.
     */
    public SpecimenDefinition setPatientPreparation(String value) { 
      if (Utilities.noString(value))
        this.patientPreparation = null;
      else {
        if (this.patientPreparation == null)
          this.patientPreparation = new StringType();
        this.patientPreparation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #timeAspect} (Time aspect of specimen collection (duration or offset).). This is the underlying object with id, value and extensions. The accessor "getTimeAspect" gives direct access to the value
     */
    public StringType getTimeAspectElement() { 
      if (this.timeAspect == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create SpecimenDefinition.timeAspect");
        else if (Configuration.doAutoCreate())
          this.timeAspect = new StringType(); // bb
      return this.timeAspect;
    }

    public boolean hasTimeAspectElement() { 
      return this.timeAspect != null && !this.timeAspect.isEmpty();
    }

    public boolean hasTimeAspect() { 
      return this.timeAspect != null && !this.timeAspect.isEmpty();
    }

    /**
     * @param value {@link #timeAspect} (Time aspect of specimen collection (duration or offset).). This is the underlying object with id, value and extensions. The accessor "getTimeAspect" gives direct access to the value
     */
    public SpecimenDefinition setTimeAspectElement(StringType value) { 
      this.timeAspect = value;
      return this;
    }

    /**
     * @return Time aspect of specimen collection (duration or offset).
     */
    public String getTimeAspect() { 
      return this.timeAspect == null ? null : this.timeAspect.getValue();
    }

    /**
     * @param value Time aspect of specimen collection (duration or offset).
     */
    public SpecimenDefinition setTimeAspect(String value) { 
      if (Utilities.noString(value))
        this.timeAspect = null;
      else {
        if (this.timeAspect == null)
          this.timeAspect = new StringType();
        this.timeAspect.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #collection} (The action to be performed for collecting the specimen.)
     */
    public List<CodeableConcept> getCollection() { 
      if (this.collection == null)
        this.collection = new ArrayList<CodeableConcept>();
      return this.collection;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SpecimenDefinition setCollection(List<CodeableConcept> theCollection) { 
      this.collection = theCollection;
      return this;
    }

    public boolean hasCollection() { 
      if (this.collection == null)
        return false;
      for (CodeableConcept item : this.collection)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCollection() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.collection == null)
        this.collection = new ArrayList<CodeableConcept>();
      this.collection.add(t);
      return t;
    }

    public SpecimenDefinition addCollection(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.collection == null)
        this.collection = new ArrayList<CodeableConcept>();
      this.collection.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #collection}, creating it if it does not already exist
     */
    public CodeableConcept getCollectionFirstRep() { 
      if (getCollection().isEmpty()) {
        addCollection();
      }
      return getCollection().get(0);
    }

    /**
     * @return {@link #specimenToLab} (Specimen conditioned in a container as expected by the testing laboratory.)
     */
    public List<SpecimenDefinitionSpecimenToLabComponent> getSpecimenToLab() { 
      if (this.specimenToLab == null)
        this.specimenToLab = new ArrayList<SpecimenDefinitionSpecimenToLabComponent>();
      return this.specimenToLab;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SpecimenDefinition setSpecimenToLab(List<SpecimenDefinitionSpecimenToLabComponent> theSpecimenToLab) { 
      this.specimenToLab = theSpecimenToLab;
      return this;
    }

    public boolean hasSpecimenToLab() { 
      if (this.specimenToLab == null)
        return false;
      for (SpecimenDefinitionSpecimenToLabComponent item : this.specimenToLab)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SpecimenDefinitionSpecimenToLabComponent addSpecimenToLab() { //3
      SpecimenDefinitionSpecimenToLabComponent t = new SpecimenDefinitionSpecimenToLabComponent();
      if (this.specimenToLab == null)
        this.specimenToLab = new ArrayList<SpecimenDefinitionSpecimenToLabComponent>();
      this.specimenToLab.add(t);
      return t;
    }

    public SpecimenDefinition addSpecimenToLab(SpecimenDefinitionSpecimenToLabComponent t) { //3
      if (t == null)
        return this;
      if (this.specimenToLab == null)
        this.specimenToLab = new ArrayList<SpecimenDefinitionSpecimenToLabComponent>();
      this.specimenToLab.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #specimenToLab}, creating it if it does not already exist
     */
    public SpecimenDefinitionSpecimenToLabComponent getSpecimenToLabFirstRep() { 
      if (getSpecimenToLab().isEmpty()) {
        addSpecimenToLab();
      }
      return getSpecimenToLab().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A business identifier associated with the kind of specimen.", 0, 1, identifier));
        children.add(new Property("typeCollected", "CodeableConcept", "The kind of material to be collected.", 0, 1, typeCollected));
        children.add(new Property("patientPreparation", "string", "Preparation of the patient for specimen collection.", 0, 1, patientPreparation));
        children.add(new Property("timeAspect", "string", "Time aspect of specimen collection (duration or offset).", 0, 1, timeAspect));
        children.add(new Property("collection", "CodeableConcept", "The action to be performed for collecting the specimen.", 0, java.lang.Integer.MAX_VALUE, collection));
        children.add(new Property("specimenToLab", "", "Specimen conditioned in a container as expected by the testing laboratory.", 0, java.lang.Integer.MAX_VALUE, specimenToLab));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A business identifier associated with the kind of specimen.", 0, 1, identifier);
        case 588504367: /*typeCollected*/  return new Property("typeCollected", "CodeableConcept", "The kind of material to be collected.", 0, 1, typeCollected);
        case -879411630: /*patientPreparation*/  return new Property("patientPreparation", "string", "Preparation of the patient for specimen collection.", 0, 1, patientPreparation);
        case 276972933: /*timeAspect*/  return new Property("timeAspect", "string", "Time aspect of specimen collection (duration or offset).", 0, 1, timeAspect);
        case -1741312354: /*collection*/  return new Property("collection", "CodeableConcept", "The action to be performed for collecting the specimen.", 0, java.lang.Integer.MAX_VALUE, collection);
        case 1669658346: /*specimenToLab*/  return new Property("specimenToLab", "", "Specimen conditioned in a container as expected by the testing laboratory.", 0, java.lang.Integer.MAX_VALUE, specimenToLab);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 588504367: /*typeCollected*/ return this.typeCollected == null ? new Base[0] : new Base[] {this.typeCollected}; // CodeableConcept
        case -879411630: /*patientPreparation*/ return this.patientPreparation == null ? new Base[0] : new Base[] {this.patientPreparation}; // StringType
        case 276972933: /*timeAspect*/ return this.timeAspect == null ? new Base[0] : new Base[] {this.timeAspect}; // StringType
        case -1741312354: /*collection*/ return this.collection == null ? new Base[0] : this.collection.toArray(new Base[this.collection.size()]); // CodeableConcept
        case 1669658346: /*specimenToLab*/ return this.specimenToLab == null ? new Base[0] : this.specimenToLab.toArray(new Base[this.specimenToLab.size()]); // SpecimenDefinitionSpecimenToLabComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 588504367: // typeCollected
          this.typeCollected = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -879411630: // patientPreparation
          this.patientPreparation = castToString(value); // StringType
          return value;
        case 276972933: // timeAspect
          this.timeAspect = castToString(value); // StringType
          return value;
        case -1741312354: // collection
          this.getCollection().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1669658346: // specimenToLab
          this.getSpecimenToLab().add((SpecimenDefinitionSpecimenToLabComponent) value); // SpecimenDefinitionSpecimenToLabComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("typeCollected")) {
          this.typeCollected = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("patientPreparation")) {
          this.patientPreparation = castToString(value); // StringType
        } else if (name.equals("timeAspect")) {
          this.timeAspect = castToString(value); // StringType
        } else if (name.equals("collection")) {
          this.getCollection().add(castToCodeableConcept(value));
        } else if (name.equals("specimenToLab")) {
          this.getSpecimenToLab().add((SpecimenDefinitionSpecimenToLabComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 588504367:  return getTypeCollected(); 
        case -879411630:  return getPatientPreparationElement();
        case 276972933:  return getTimeAspectElement();
        case -1741312354:  return addCollection(); 
        case 1669658346:  return addSpecimenToLab(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 588504367: /*typeCollected*/ return new String[] {"CodeableConcept"};
        case -879411630: /*patientPreparation*/ return new String[] {"string"};
        case 276972933: /*timeAspect*/ return new String[] {"string"};
        case -1741312354: /*collection*/ return new String[] {"CodeableConcept"};
        case 1669658346: /*specimenToLab*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("typeCollected")) {
          this.typeCollected = new CodeableConcept();
          return this.typeCollected;
        }
        else if (name.equals("patientPreparation")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.patientPreparation");
        }
        else if (name.equals("timeAspect")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.timeAspect");
        }
        else if (name.equals("collection")) {
          return addCollection();
        }
        else if (name.equals("specimenToLab")) {
          return addSpecimenToLab();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "SpecimenDefinition";

  }

      public SpecimenDefinition copy() {
        SpecimenDefinition dst = new SpecimenDefinition();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.typeCollected = typeCollected == null ? null : typeCollected.copy();
        dst.patientPreparation = patientPreparation == null ? null : patientPreparation.copy();
        dst.timeAspect = timeAspect == null ? null : timeAspect.copy();
        if (collection != null) {
          dst.collection = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : collection)
            dst.collection.add(i.copy());
        };
        if (specimenToLab != null) {
          dst.specimenToLab = new ArrayList<SpecimenDefinitionSpecimenToLabComponent>();
          for (SpecimenDefinitionSpecimenToLabComponent i : specimenToLab)
            dst.specimenToLab.add(i.copy());
        };
        return dst;
      }

      protected SpecimenDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinition))
          return false;
        SpecimenDefinition o = (SpecimenDefinition) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(typeCollected, o.typeCollected, true)
           && compareDeep(patientPreparation, o.patientPreparation, true) && compareDeep(timeAspect, o.timeAspect, true)
           && compareDeep(collection, o.collection, true) && compareDeep(specimenToLab, o.specimenToLab, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinition))
          return false;
        SpecimenDefinition o = (SpecimenDefinition) other_;
        return compareValues(patientPreparation, o.patientPreparation, true) && compareValues(timeAspect, o.timeAspect, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, typeCollected
          , patientPreparation, timeAspect, collection, specimenToLab);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.SpecimenDefinition;
   }

 /**
   * Search parameter: <b>container</b>
   * <p>
   * Description: <b>The type of specimen conditioned in container expected by the lab</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SpecimenDefinition.specimenToLab.containerType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="container", path="SpecimenDefinition.specimenToLab.containerType", description="The type of specimen conditioned in container expected by the lab", type="token" )
  public static final String SP_CONTAINER = "container";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>container</b>
   * <p>
   * Description: <b>The type of specimen conditioned in container expected by the lab</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SpecimenDefinition.specimenToLab.containerType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTAINER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTAINER);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The unique identifier associated with the specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SpecimenDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="SpecimenDefinition.identifier", description="The unique identifier associated with the specimen", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The unique identifier associated with the specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SpecimenDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The type of collected specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SpecimenDefinition.typeCollected</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="SpecimenDefinition.typeCollected", description="The type of collected specimen", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The type of collected specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SpecimenDefinition.typeCollected</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

