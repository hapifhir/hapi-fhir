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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

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
@ResourceDef(name="SpecimenDefinition", profile="http://hl7.org/fhir/StructureDefinition/SpecimenDefinition")
public class SpecimenDefinition extends DomainResource {

    public enum SpecimenContainedPreference {
        /**
         * This type of contained specimen is preferred to collect this kind of specimen.
         */
        PREFERRED, 
        /**
         * This type of conditioned specimen is an alternate.
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
            case PREFERRED: return "This type of contained specimen is preferred to collect this kind of specimen.";
            case ALTERNATE: return "This type of conditioned specimen is an alternate.";
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
    public static class SpecimenDefinitionTypeTestedComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Primary of secondary specimen.
         */
        @Child(name = "isDerived", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Primary or secondary specimen", formalDefinition="Primary of secondary specimen." )
        protected BooleanType isDerived;

        /**
         * The kind of specimen conditioned for testing expected by lab.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of intended specimen", formalDefinition="The kind of specimen conditioned for testing expected by lab." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v2-0487")
        protected CodeableConcept type;

        /**
         * The preference for this type of conditioned specimen.
         */
        @Child(name = "preference", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="preferred | alternate", formalDefinition="The preference for this type of conditioned specimen." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/specimen-contained-preference")
        protected Enumeration<SpecimenContainedPreference> preference;

        /**
         * The specimen's container.
         */
        @Child(name = "container", type = {}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The specimen's container", formalDefinition="The specimen's container." )
        protected SpecimenDefinitionTypeTestedContainerComponent container;

        /**
         * Requirements for delivery and special handling of this kind of conditioned specimen.
         */
        @Child(name = "requirement", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specimen requirements", formalDefinition="Requirements for delivery and special handling of this kind of conditioned specimen." )
        protected StringType requirement;

        /**
         * The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.
         */
        @Child(name = "retentionTime", type = {Duration.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specimen retention time", formalDefinition="The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing." )
        protected Duration retentionTime;

        /**
         * Criterion for rejection of the specimen in its container by the laboratory.
         */
        @Child(name = "rejectionCriterion", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Rejection criterion", formalDefinition="Criterion for rejection of the specimen in its container by the laboratory." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/rejection-criteria")
        protected List<CodeableConcept> rejectionCriterion;

        /**
         * Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing process.
         */
        @Child(name = "handling", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Specimen handling before testing", formalDefinition="Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing process." )
        protected List<SpecimenDefinitionTypeTestedHandlingComponent> handling;

        private static final long serialVersionUID = 308313920L;

    /**
     * Constructor
     */
      public SpecimenDefinitionTypeTestedComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SpecimenDefinitionTypeTestedComponent(Enumeration<SpecimenContainedPreference> preference) {
        super();
        this.preference = preference;
      }

        /**
         * @return {@link #isDerived} (Primary of secondary specimen.). This is the underlying object with id, value and extensions. The accessor "getIsDerived" gives direct access to the value
         */
        public BooleanType getIsDerivedElement() { 
          if (this.isDerived == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedComponent.isDerived");
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
        public SpecimenDefinitionTypeTestedComponent setIsDerivedElement(BooleanType value) { 
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
        public SpecimenDefinitionTypeTestedComponent setIsDerived(boolean value) { 
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
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedComponent.type");
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
        public SpecimenDefinitionTypeTestedComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #preference} (The preference for this type of conditioned specimen.). This is the underlying object with id, value and extensions. The accessor "getPreference" gives direct access to the value
         */
        public Enumeration<SpecimenContainedPreference> getPreferenceElement() { 
          if (this.preference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedComponent.preference");
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
        public SpecimenDefinitionTypeTestedComponent setPreferenceElement(Enumeration<SpecimenContainedPreference> value) { 
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
        public SpecimenDefinitionTypeTestedComponent setPreference(SpecimenContainedPreference value) { 
            if (this.preference == null)
              this.preference = new Enumeration<SpecimenContainedPreference>(new SpecimenContainedPreferenceEnumFactory());
            this.preference.setValue(value);
          return this;
        }

        /**
         * @return {@link #container} (The specimen's container.)
         */
        public SpecimenDefinitionTypeTestedContainerComponent getContainer() { 
          if (this.container == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedComponent.container");
            else if (Configuration.doAutoCreate())
              this.container = new SpecimenDefinitionTypeTestedContainerComponent(); // cc
          return this.container;
        }

        public boolean hasContainer() { 
          return this.container != null && !this.container.isEmpty();
        }

        /**
         * @param value {@link #container} (The specimen's container.)
         */
        public SpecimenDefinitionTypeTestedComponent setContainer(SpecimenDefinitionTypeTestedContainerComponent value) { 
          this.container = value;
          return this;
        }

        /**
         * @return {@link #requirement} (Requirements for delivery and special handling of this kind of conditioned specimen.). This is the underlying object with id, value and extensions. The accessor "getRequirement" gives direct access to the value
         */
        public StringType getRequirementElement() { 
          if (this.requirement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedComponent.requirement");
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
        public SpecimenDefinitionTypeTestedComponent setRequirementElement(StringType value) { 
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
        public SpecimenDefinitionTypeTestedComponent setRequirement(String value) { 
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
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedComponent.retentionTime");
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
        public SpecimenDefinitionTypeTestedComponent setRetentionTime(Duration value) { 
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
        public SpecimenDefinitionTypeTestedComponent setRejectionCriterion(List<CodeableConcept> theRejectionCriterion) { 
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

        public SpecimenDefinitionTypeTestedComponent addRejectionCriterion(CodeableConcept t) { //3
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
         * @return {@link #handling} (Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing process.)
         */
        public List<SpecimenDefinitionTypeTestedHandlingComponent> getHandling() { 
          if (this.handling == null)
            this.handling = new ArrayList<SpecimenDefinitionTypeTestedHandlingComponent>();
          return this.handling;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SpecimenDefinitionTypeTestedComponent setHandling(List<SpecimenDefinitionTypeTestedHandlingComponent> theHandling) { 
          this.handling = theHandling;
          return this;
        }

        public boolean hasHandling() { 
          if (this.handling == null)
            return false;
          for (SpecimenDefinitionTypeTestedHandlingComponent item : this.handling)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SpecimenDefinitionTypeTestedHandlingComponent addHandling() { //3
          SpecimenDefinitionTypeTestedHandlingComponent t = new SpecimenDefinitionTypeTestedHandlingComponent();
          if (this.handling == null)
            this.handling = new ArrayList<SpecimenDefinitionTypeTestedHandlingComponent>();
          this.handling.add(t);
          return t;
        }

        public SpecimenDefinitionTypeTestedComponent addHandling(SpecimenDefinitionTypeTestedHandlingComponent t) { //3
          if (t == null)
            return this;
          if (this.handling == null)
            this.handling = new ArrayList<SpecimenDefinitionTypeTestedHandlingComponent>();
          this.handling.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #handling}, creating it if it does not already exist
         */
        public SpecimenDefinitionTypeTestedHandlingComponent getHandlingFirstRep() { 
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
          children.add(new Property("container", "", "The specimen's container.", 0, 1, container));
          children.add(new Property("requirement", "string", "Requirements for delivery and special handling of this kind of conditioned specimen.", 0, 1, requirement));
          children.add(new Property("retentionTime", "Duration", "The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.", 0, 1, retentionTime));
          children.add(new Property("rejectionCriterion", "CodeableConcept", "Criterion for rejection of the specimen in its container by the laboratory.", 0, java.lang.Integer.MAX_VALUE, rejectionCriterion));
          children.add(new Property("handling", "", "Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing process.", 0, java.lang.Integer.MAX_VALUE, handling));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 976346515: /*isDerived*/  return new Property("isDerived", "boolean", "Primary of secondary specimen.", 0, 1, isDerived);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The kind of specimen conditioned for testing expected by lab.", 0, 1, type);
          case -1459831589: /*preference*/  return new Property("preference", "code", "The preference for this type of conditioned specimen.", 0, 1, preference);
          case -410956671: /*container*/  return new Property("container", "", "The specimen's container.", 0, 1, container);
          case 363387971: /*requirement*/  return new Property("requirement", "string", "Requirements for delivery and special handling of this kind of conditioned specimen.", 0, 1, requirement);
          case 1434969867: /*retentionTime*/  return new Property("retentionTime", "Duration", "The usual time that a specimen of this kind is retained after the ordered tests are completed, for the purpose of additional testing.", 0, 1, retentionTime);
          case -553706344: /*rejectionCriterion*/  return new Property("rejectionCriterion", "CodeableConcept", "Criterion for rejection of the specimen in its container by the laboratory.", 0, java.lang.Integer.MAX_VALUE, rejectionCriterion);
          case 2072805: /*handling*/  return new Property("handling", "", "Set of instructions for preservation/transport of the specimen at a defined temperature interval, prior the testing process.", 0, java.lang.Integer.MAX_VALUE, handling);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 976346515: /*isDerived*/ return this.isDerived == null ? new Base[0] : new Base[] {this.isDerived}; // BooleanType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1459831589: /*preference*/ return this.preference == null ? new Base[0] : new Base[] {this.preference}; // Enumeration<SpecimenContainedPreference>
        case -410956671: /*container*/ return this.container == null ? new Base[0] : new Base[] {this.container}; // SpecimenDefinitionTypeTestedContainerComponent
        case 363387971: /*requirement*/ return this.requirement == null ? new Base[0] : new Base[] {this.requirement}; // StringType
        case 1434969867: /*retentionTime*/ return this.retentionTime == null ? new Base[0] : new Base[] {this.retentionTime}; // Duration
        case -553706344: /*rejectionCriterion*/ return this.rejectionCriterion == null ? new Base[0] : this.rejectionCriterion.toArray(new Base[this.rejectionCriterion.size()]); // CodeableConcept
        case 2072805: /*handling*/ return this.handling == null ? new Base[0] : this.handling.toArray(new Base[this.handling.size()]); // SpecimenDefinitionTypeTestedHandlingComponent
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
        case -410956671: // container
          this.container = (SpecimenDefinitionTypeTestedContainerComponent) value; // SpecimenDefinitionTypeTestedContainerComponent
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
          this.getHandling().add((SpecimenDefinitionTypeTestedHandlingComponent) value); // SpecimenDefinitionTypeTestedHandlingComponent
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
        } else if (name.equals("container")) {
          this.container = (SpecimenDefinitionTypeTestedContainerComponent) value; // SpecimenDefinitionTypeTestedContainerComponent
        } else if (name.equals("requirement")) {
          this.requirement = castToString(value); // StringType
        } else if (name.equals("retentionTime")) {
          this.retentionTime = castToDuration(value); // Duration
        } else if (name.equals("rejectionCriterion")) {
          this.getRejectionCriterion().add(castToCodeableConcept(value));
        } else if (name.equals("handling")) {
          this.getHandling().add((SpecimenDefinitionTypeTestedHandlingComponent) value);
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
        case -410956671:  return getContainer(); 
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
        case -410956671: /*container*/ return new String[] {};
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
        else if (name.equals("container")) {
          this.container = new SpecimenDefinitionTypeTestedContainerComponent();
          return this.container;
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

      public SpecimenDefinitionTypeTestedComponent copy() {
        SpecimenDefinitionTypeTestedComponent dst = new SpecimenDefinitionTypeTestedComponent();
        copyValues(dst);
        dst.isDerived = isDerived == null ? null : isDerived.copy();
        dst.type = type == null ? null : type.copy();
        dst.preference = preference == null ? null : preference.copy();
        dst.container = container == null ? null : container.copy();
        dst.requirement = requirement == null ? null : requirement.copy();
        dst.retentionTime = retentionTime == null ? null : retentionTime.copy();
        if (rejectionCriterion != null) {
          dst.rejectionCriterion = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : rejectionCriterion)
            dst.rejectionCriterion.add(i.copy());
        };
        if (handling != null) {
          dst.handling = new ArrayList<SpecimenDefinitionTypeTestedHandlingComponent>();
          for (SpecimenDefinitionTypeTestedHandlingComponent i : handling)
            dst.handling.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedComponent))
          return false;
        SpecimenDefinitionTypeTestedComponent o = (SpecimenDefinitionTypeTestedComponent) other_;
        return compareDeep(isDerived, o.isDerived, true) && compareDeep(type, o.type, true) && compareDeep(preference, o.preference, true)
           && compareDeep(container, o.container, true) && compareDeep(requirement, o.requirement, true) && compareDeep(retentionTime, o.retentionTime, true)
           && compareDeep(rejectionCriterion, o.rejectionCriterion, true) && compareDeep(handling, o.handling, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedComponent))
          return false;
        SpecimenDefinitionTypeTestedComponent o = (SpecimenDefinitionTypeTestedComponent) other_;
        return compareValues(isDerived, o.isDerived, true) && compareValues(preference, o.preference, true)
           && compareValues(requirement, o.requirement, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(isDerived, type, preference
          , container, requirement, retentionTime, rejectionCriterion, handling);
      }

  public String fhirType() {
    return "SpecimenDefinition.typeTested";

  }

  }

    @Block()
    public static class SpecimenDefinitionTypeTestedContainerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of material of the container.
         */
        @Child(name = "material", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Container material", formalDefinition="The type of material of the container." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/container-material")
        protected CodeableConcept material;

        /**
         * The type of container used to contain this kind of specimen.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Kind of container associated with the kind of specimen", formalDefinition="The type of container used to contain this kind of specimen." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/specimen-container-type")
        protected CodeableConcept type;

        /**
         * Color of container cap.
         */
        @Child(name = "cap", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Color of container cap", formalDefinition="Color of container cap." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/container-cap")
        protected CodeableConcept cap;

        /**
         * The textual description of the kind of container.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Container description", formalDefinition="The textual description of the kind of container." )
        protected StringType description;

        /**
         * The capacity (volume or other measure) of this kind of container.
         */
        @Child(name = "capacity", type = {Quantity.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Container capacity", formalDefinition="The capacity (volume or other measure) of this kind of container." )
        protected Quantity capacity;

        /**
         * The minimum volume to be conditioned in the container.
         */
        @Child(name = "minimumVolume", type = {Quantity.class, StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Minimum volume", formalDefinition="The minimum volume to be conditioned in the container." )
        protected Type minimumVolume;

        /**
         * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.
         */
        @Child(name = "additive", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additive associated with container", formalDefinition="Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA." )
        protected List<SpecimenDefinitionTypeTestedContainerAdditiveComponent> additive;

        /**
         * Special processing that should be applied to the container for this kind of specimen.
         */
        @Child(name = "preparation", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specimen container preparation", formalDefinition="Special processing that should be applied to the container for this kind of specimen." )
        protected StringType preparation;

        private static final long serialVersionUID = 175789710L;

    /**
     * Constructor
     */
      public SpecimenDefinitionTypeTestedContainerComponent() {
        super();
      }

        /**
         * @return {@link #material} (The type of material of the container.)
         */
        public CodeableConcept getMaterial() { 
          if (this.material == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedContainerComponent.material");
            else if (Configuration.doAutoCreate())
              this.material = new CodeableConcept(); // cc
          return this.material;
        }

        public boolean hasMaterial() { 
          return this.material != null && !this.material.isEmpty();
        }

        /**
         * @param value {@link #material} (The type of material of the container.)
         */
        public SpecimenDefinitionTypeTestedContainerComponent setMaterial(CodeableConcept value) { 
          this.material = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of container used to contain this kind of specimen.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedContainerComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of container used to contain this kind of specimen.)
         */
        public SpecimenDefinitionTypeTestedContainerComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #cap} (Color of container cap.)
         */
        public CodeableConcept getCap() { 
          if (this.cap == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedContainerComponent.cap");
            else if (Configuration.doAutoCreate())
              this.cap = new CodeableConcept(); // cc
          return this.cap;
        }

        public boolean hasCap() { 
          return this.cap != null && !this.cap.isEmpty();
        }

        /**
         * @param value {@link #cap} (Color of container cap.)
         */
        public SpecimenDefinitionTypeTestedContainerComponent setCap(CodeableConcept value) { 
          this.cap = value;
          return this;
        }

        /**
         * @return {@link #description} (The textual description of the kind of container.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedContainerComponent.description");
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
         * @param value {@link #description} (The textual description of the kind of container.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public SpecimenDefinitionTypeTestedContainerComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The textual description of the kind of container.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The textual description of the kind of container.
         */
        public SpecimenDefinitionTypeTestedContainerComponent setDescription(String value) { 
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
         * @return {@link #capacity} (The capacity (volume or other measure) of this kind of container.)
         */
        public Quantity getCapacity() { 
          if (this.capacity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedContainerComponent.capacity");
            else if (Configuration.doAutoCreate())
              this.capacity = new Quantity(); // cc
          return this.capacity;
        }

        public boolean hasCapacity() { 
          return this.capacity != null && !this.capacity.isEmpty();
        }

        /**
         * @param value {@link #capacity} (The capacity (volume or other measure) of this kind of container.)
         */
        public SpecimenDefinitionTypeTestedContainerComponent setCapacity(Quantity value) { 
          this.capacity = value;
          return this;
        }

        /**
         * @return {@link #minimumVolume} (The minimum volume to be conditioned in the container.)
         */
        public Type getMinimumVolume() { 
          return this.minimumVolume;
        }

        /**
         * @return {@link #minimumVolume} (The minimum volume to be conditioned in the container.)
         */
        public Quantity getMinimumVolumeQuantity() throws FHIRException { 
          if (this.minimumVolume == null)
            this.minimumVolume = new Quantity();
          if (!(this.minimumVolume instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.minimumVolume.getClass().getName()+" was encountered");
          return (Quantity) this.minimumVolume;
        }

        public boolean hasMinimumVolumeQuantity() { 
          return this != null && this.minimumVolume instanceof Quantity;
        }

        /**
         * @return {@link #minimumVolume} (The minimum volume to be conditioned in the container.)
         */
        public StringType getMinimumVolumeStringType() throws FHIRException { 
          if (this.minimumVolume == null)
            this.minimumVolume = new StringType();
          if (!(this.minimumVolume instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.minimumVolume.getClass().getName()+" was encountered");
          return (StringType) this.minimumVolume;
        }

        public boolean hasMinimumVolumeStringType() { 
          return this != null && this.minimumVolume instanceof StringType;
        }

        public boolean hasMinimumVolume() { 
          return this.minimumVolume != null && !this.minimumVolume.isEmpty();
        }

        /**
         * @param value {@link #minimumVolume} (The minimum volume to be conditioned in the container.)
         */
        public SpecimenDefinitionTypeTestedContainerComponent setMinimumVolume(Type value) { 
          if (value != null && !(value instanceof Quantity || value instanceof StringType))
            throw new Error("Not the right type for SpecimenDefinition.typeTested.container.minimumVolume[x]: "+value.fhirType());
          this.minimumVolume = value;
          return this;
        }

        /**
         * @return {@link #additive} (Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public List<SpecimenDefinitionTypeTestedContainerAdditiveComponent> getAdditive() { 
          if (this.additive == null)
            this.additive = new ArrayList<SpecimenDefinitionTypeTestedContainerAdditiveComponent>();
          return this.additive;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SpecimenDefinitionTypeTestedContainerComponent setAdditive(List<SpecimenDefinitionTypeTestedContainerAdditiveComponent> theAdditive) { 
          this.additive = theAdditive;
          return this;
        }

        public boolean hasAdditive() { 
          if (this.additive == null)
            return false;
          for (SpecimenDefinitionTypeTestedContainerAdditiveComponent item : this.additive)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SpecimenDefinitionTypeTestedContainerAdditiveComponent addAdditive() { //3
          SpecimenDefinitionTypeTestedContainerAdditiveComponent t = new SpecimenDefinitionTypeTestedContainerAdditiveComponent();
          if (this.additive == null)
            this.additive = new ArrayList<SpecimenDefinitionTypeTestedContainerAdditiveComponent>();
          this.additive.add(t);
          return t;
        }

        public SpecimenDefinitionTypeTestedContainerComponent addAdditive(SpecimenDefinitionTypeTestedContainerAdditiveComponent t) { //3
          if (t == null)
            return this;
          if (this.additive == null)
            this.additive = new ArrayList<SpecimenDefinitionTypeTestedContainerAdditiveComponent>();
          this.additive.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #additive}, creating it if it does not already exist
         */
        public SpecimenDefinitionTypeTestedContainerAdditiveComponent getAdditiveFirstRep() { 
          if (getAdditive().isEmpty()) {
            addAdditive();
          }
          return getAdditive().get(0);
        }

        /**
         * @return {@link #preparation} (Special processing that should be applied to the container for this kind of specimen.). This is the underlying object with id, value and extensions. The accessor "getPreparation" gives direct access to the value
         */
        public StringType getPreparationElement() { 
          if (this.preparation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedContainerComponent.preparation");
            else if (Configuration.doAutoCreate())
              this.preparation = new StringType(); // bb
          return this.preparation;
        }

        public boolean hasPreparationElement() { 
          return this.preparation != null && !this.preparation.isEmpty();
        }

        public boolean hasPreparation() { 
          return this.preparation != null && !this.preparation.isEmpty();
        }

        /**
         * @param value {@link #preparation} (Special processing that should be applied to the container for this kind of specimen.). This is the underlying object with id, value and extensions. The accessor "getPreparation" gives direct access to the value
         */
        public SpecimenDefinitionTypeTestedContainerComponent setPreparationElement(StringType value) { 
          this.preparation = value;
          return this;
        }

        /**
         * @return Special processing that should be applied to the container for this kind of specimen.
         */
        public String getPreparation() { 
          return this.preparation == null ? null : this.preparation.getValue();
        }

        /**
         * @param value Special processing that should be applied to the container for this kind of specimen.
         */
        public SpecimenDefinitionTypeTestedContainerComponent setPreparation(String value) { 
          if (Utilities.noString(value))
            this.preparation = null;
          else {
            if (this.preparation == null)
              this.preparation = new StringType();
            this.preparation.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("material", "CodeableConcept", "The type of material of the container.", 0, 1, material));
          children.add(new Property("type", "CodeableConcept", "The type of container used to contain this kind of specimen.", 0, 1, type));
          children.add(new Property("cap", "CodeableConcept", "Color of container cap.", 0, 1, cap));
          children.add(new Property("description", "string", "The textual description of the kind of container.", 0, 1, description));
          children.add(new Property("capacity", "SimpleQuantity", "The capacity (volume or other measure) of this kind of container.", 0, 1, capacity));
          children.add(new Property("minimumVolume[x]", "SimpleQuantity|string", "The minimum volume to be conditioned in the container.", 0, 1, minimumVolume));
          children.add(new Property("additive", "", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, java.lang.Integer.MAX_VALUE, additive));
          children.add(new Property("preparation", "string", "Special processing that should be applied to the container for this kind of specimen.", 0, 1, preparation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 299066663: /*material*/  return new Property("material", "CodeableConcept", "The type of material of the container.", 0, 1, material);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The type of container used to contain this kind of specimen.", 0, 1, type);
          case 98258: /*cap*/  return new Property("cap", "CodeableConcept", "Color of container cap.", 0, 1, cap);
          case -1724546052: /*description*/  return new Property("description", "string", "The textual description of the kind of container.", 0, 1, description);
          case -67824454: /*capacity*/  return new Property("capacity", "SimpleQuantity", "The capacity (volume or other measure) of this kind of container.", 0, 1, capacity);
          case 371830456: /*minimumVolume[x]*/  return new Property("minimumVolume[x]", "SimpleQuantity|string", "The minimum volume to be conditioned in the container.", 0, 1, minimumVolume);
          case -1674665784: /*minimumVolume*/  return new Property("minimumVolume[x]", "SimpleQuantity|string", "The minimum volume to be conditioned in the container.", 0, 1, minimumVolume);
          case -532143757: /*minimumVolumeQuantity*/  return new Property("minimumVolume[x]", "SimpleQuantity|string", "The minimum volume to be conditioned in the container.", 0, 1, minimumVolume);
          case 248461049: /*minimumVolumeString*/  return new Property("minimumVolume[x]", "SimpleQuantity|string", "The minimum volume to be conditioned in the container.", 0, 1, minimumVolume);
          case -1226589236: /*additive*/  return new Property("additive", "", "Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, java.lang.Integer.MAX_VALUE, additive);
          case -1315428713: /*preparation*/  return new Property("preparation", "string", "Special processing that should be applied to the container for this kind of specimen.", 0, 1, preparation);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 299066663: /*material*/ return this.material == null ? new Base[0] : new Base[] {this.material}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 98258: /*cap*/ return this.cap == null ? new Base[0] : new Base[] {this.cap}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -67824454: /*capacity*/ return this.capacity == null ? new Base[0] : new Base[] {this.capacity}; // Quantity
        case -1674665784: /*minimumVolume*/ return this.minimumVolume == null ? new Base[0] : new Base[] {this.minimumVolume}; // Type
        case -1226589236: /*additive*/ return this.additive == null ? new Base[0] : this.additive.toArray(new Base[this.additive.size()]); // SpecimenDefinitionTypeTestedContainerAdditiveComponent
        case -1315428713: /*preparation*/ return this.preparation == null ? new Base[0] : new Base[] {this.preparation}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 299066663: // material
          this.material = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 98258: // cap
          this.cap = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -67824454: // capacity
          this.capacity = castToQuantity(value); // Quantity
          return value;
        case -1674665784: // minimumVolume
          this.minimumVolume = castToType(value); // Type
          return value;
        case -1226589236: // additive
          this.getAdditive().add((SpecimenDefinitionTypeTestedContainerAdditiveComponent) value); // SpecimenDefinitionTypeTestedContainerAdditiveComponent
          return value;
        case -1315428713: // preparation
          this.preparation = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("material")) {
          this.material = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("cap")) {
          this.cap = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("capacity")) {
          this.capacity = castToQuantity(value); // Quantity
        } else if (name.equals("minimumVolume[x]")) {
          this.minimumVolume = castToType(value); // Type
        } else if (name.equals("additive")) {
          this.getAdditive().add((SpecimenDefinitionTypeTestedContainerAdditiveComponent) value);
        } else if (name.equals("preparation")) {
          this.preparation = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 299066663:  return getMaterial(); 
        case 3575610:  return getType(); 
        case 98258:  return getCap(); 
        case -1724546052:  return getDescriptionElement();
        case -67824454:  return getCapacity(); 
        case 371830456:  return getMinimumVolume(); 
        case -1674665784:  return getMinimumVolume(); 
        case -1226589236:  return addAdditive(); 
        case -1315428713:  return getPreparationElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 299066663: /*material*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 98258: /*cap*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -67824454: /*capacity*/ return new String[] {"SimpleQuantity"};
        case -1674665784: /*minimumVolume*/ return new String[] {"SimpleQuantity", "string"};
        case -1226589236: /*additive*/ return new String[] {};
        case -1315428713: /*preparation*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("material")) {
          this.material = new CodeableConcept();
          return this.material;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("cap")) {
          this.cap = new CodeableConcept();
          return this.cap;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.description");
        }
        else if (name.equals("capacity")) {
          this.capacity = new Quantity();
          return this.capacity;
        }
        else if (name.equals("minimumVolumeQuantity")) {
          this.minimumVolume = new Quantity();
          return this.minimumVolume;
        }
        else if (name.equals("minimumVolumeString")) {
          this.minimumVolume = new StringType();
          return this.minimumVolume;
        }
        else if (name.equals("additive")) {
          return addAdditive();
        }
        else if (name.equals("preparation")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.preparation");
        }
        else
          return super.addChild(name);
      }

      public SpecimenDefinitionTypeTestedContainerComponent copy() {
        SpecimenDefinitionTypeTestedContainerComponent dst = new SpecimenDefinitionTypeTestedContainerComponent();
        copyValues(dst);
        dst.material = material == null ? null : material.copy();
        dst.type = type == null ? null : type.copy();
        dst.cap = cap == null ? null : cap.copy();
        dst.description = description == null ? null : description.copy();
        dst.capacity = capacity == null ? null : capacity.copy();
        dst.minimumVolume = minimumVolume == null ? null : minimumVolume.copy();
        if (additive != null) {
          dst.additive = new ArrayList<SpecimenDefinitionTypeTestedContainerAdditiveComponent>();
          for (SpecimenDefinitionTypeTestedContainerAdditiveComponent i : additive)
            dst.additive.add(i.copy());
        };
        dst.preparation = preparation == null ? null : preparation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedContainerComponent))
          return false;
        SpecimenDefinitionTypeTestedContainerComponent o = (SpecimenDefinitionTypeTestedContainerComponent) other_;
        return compareDeep(material, o.material, true) && compareDeep(type, o.type, true) && compareDeep(cap, o.cap, true)
           && compareDeep(description, o.description, true) && compareDeep(capacity, o.capacity, true) && compareDeep(minimumVolume, o.minimumVolume, true)
           && compareDeep(additive, o.additive, true) && compareDeep(preparation, o.preparation, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedContainerComponent))
          return false;
        SpecimenDefinitionTypeTestedContainerComponent o = (SpecimenDefinitionTypeTestedContainerComponent) other_;
        return compareValues(description, o.description, true) && compareValues(preparation, o.preparation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(material, type, cap, description
          , capacity, minimumVolume, additive, preparation);
      }

  public String fhirType() {
    return "SpecimenDefinition.typeTested.container";

  }

  }

    @Block()
    public static class SpecimenDefinitionTypeTestedContainerAdditiveComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.
         */
        @Child(name = "additive", type = {CodeableConcept.class, Substance.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additive associated with container", formalDefinition="Substance introduced in the kind of container to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v2-0371")
        protected Type additive;

        private static final long serialVersionUID = 1819209272L;

    /**
     * Constructor
     */
      public SpecimenDefinitionTypeTestedContainerAdditiveComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SpecimenDefinitionTypeTestedContainerAdditiveComponent(Type additive) {
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
            this.additive = new CodeableConcept();
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
            this.additive = new Reference();
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
        public SpecimenDefinitionTypeTestedContainerAdditiveComponent setAdditive(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for SpecimenDefinition.typeTested.container.additive.additive[x]: "+value.fhirType());
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

      public SpecimenDefinitionTypeTestedContainerAdditiveComponent copy() {
        SpecimenDefinitionTypeTestedContainerAdditiveComponent dst = new SpecimenDefinitionTypeTestedContainerAdditiveComponent();
        copyValues(dst);
        dst.additive = additive == null ? null : additive.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedContainerAdditiveComponent))
          return false;
        SpecimenDefinitionTypeTestedContainerAdditiveComponent o = (SpecimenDefinitionTypeTestedContainerAdditiveComponent) other_;
        return compareDeep(additive, o.additive, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedContainerAdditiveComponent))
          return false;
        SpecimenDefinitionTypeTestedContainerAdditiveComponent o = (SpecimenDefinitionTypeTestedContainerAdditiveComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(additive);
      }

  public String fhirType() {
    return "SpecimenDefinition.typeTested.container.additive";

  }

  }

    @Block()
    public static class SpecimenDefinitionTypeTestedHandlingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not related to temperature may be handled in the instruction element.
         */
        @Child(name = "temperatureQualifier", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Temperature qualifier", formalDefinition="It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not related to temperature may be handled in the instruction element." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/handling-condition")
        protected CodeableConcept temperatureQualifier;

        /**
         * The temperature interval for this set of handling instructions.
         */
        @Child(name = "temperatureRange", type = {Range.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Temperature range", formalDefinition="The temperature interval for this set of handling instructions." )
        protected Range temperatureRange;

        /**
         * The maximum time interval of preservation of the specimen with these conditions.
         */
        @Child(name = "maxDuration", type = {Duration.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Maximum preservation time", formalDefinition="The maximum time interval of preservation of the specimen with these conditions." )
        protected Duration maxDuration;

        /**
         * Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'.
         */
        @Child(name = "instruction", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Preservation instruction", formalDefinition="Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'." )
        protected StringType instruction;

        private static final long serialVersionUID = 2130906844L;

    /**
     * Constructor
     */
      public SpecimenDefinitionTypeTestedHandlingComponent() {
        super();
      }

        /**
         * @return {@link #temperatureQualifier} (It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not related to temperature may be handled in the instruction element.)
         */
        public CodeableConcept getTemperatureQualifier() { 
          if (this.temperatureQualifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedHandlingComponent.temperatureQualifier");
            else if (Configuration.doAutoCreate())
              this.temperatureQualifier = new CodeableConcept(); // cc
          return this.temperatureQualifier;
        }

        public boolean hasTemperatureQualifier() { 
          return this.temperatureQualifier != null && !this.temperatureQualifier.isEmpty();
        }

        /**
         * @param value {@link #temperatureQualifier} (It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not related to temperature may be handled in the instruction element.)
         */
        public SpecimenDefinitionTypeTestedHandlingComponent setTemperatureQualifier(CodeableConcept value) { 
          this.temperatureQualifier = value;
          return this;
        }

        /**
         * @return {@link #temperatureRange} (The temperature interval for this set of handling instructions.)
         */
        public Range getTemperatureRange() { 
          if (this.temperatureRange == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedHandlingComponent.temperatureRange");
            else if (Configuration.doAutoCreate())
              this.temperatureRange = new Range(); // cc
          return this.temperatureRange;
        }

        public boolean hasTemperatureRange() { 
          return this.temperatureRange != null && !this.temperatureRange.isEmpty();
        }

        /**
         * @param value {@link #temperatureRange} (The temperature interval for this set of handling instructions.)
         */
        public SpecimenDefinitionTypeTestedHandlingComponent setTemperatureRange(Range value) { 
          this.temperatureRange = value;
          return this;
        }

        /**
         * @return {@link #maxDuration} (The maximum time interval of preservation of the specimen with these conditions.)
         */
        public Duration getMaxDuration() { 
          if (this.maxDuration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedHandlingComponent.maxDuration");
            else if (Configuration.doAutoCreate())
              this.maxDuration = new Duration(); // cc
          return this.maxDuration;
        }

        public boolean hasMaxDuration() { 
          return this.maxDuration != null && !this.maxDuration.isEmpty();
        }

        /**
         * @param value {@link #maxDuration} (The maximum time interval of preservation of the specimen with these conditions.)
         */
        public SpecimenDefinitionTypeTestedHandlingComponent setMaxDuration(Duration value) { 
          this.maxDuration = value;
          return this;
        }

        /**
         * @return {@link #instruction} (Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public StringType getInstructionElement() { 
          if (this.instruction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenDefinitionTypeTestedHandlingComponent.instruction");
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
         * @param value {@link #instruction} (Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'.). This is the underlying object with id, value and extensions. The accessor "getInstruction" gives direct access to the value
         */
        public SpecimenDefinitionTypeTestedHandlingComponent setInstructionElement(StringType value) { 
          this.instruction = value;
          return this;
        }

        /**
         * @return Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'.
         */
        public String getInstruction() { 
          return this.instruction == null ? null : this.instruction.getValue();
        }

        /**
         * @param value Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'.
         */
        public SpecimenDefinitionTypeTestedHandlingComponent setInstruction(String value) { 
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
          children.add(new Property("temperatureQualifier", "CodeableConcept", "It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not related to temperature may be handled in the instruction element.", 0, 1, temperatureQualifier));
          children.add(new Property("temperatureRange", "Range", "The temperature interval for this set of handling instructions.", 0, 1, temperatureRange));
          children.add(new Property("maxDuration", "Duration", "The maximum time interval of preservation of the specimen with these conditions.", 0, 1, maxDuration));
          children.add(new Property("instruction", "string", "Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'.", 0, 1, instruction));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 548941206: /*temperatureQualifier*/  return new Property("temperatureQualifier", "CodeableConcept", "It qualifies the interval of temperature, which characterizes an occurrence of handling. Conditions that are not related to temperature may be handled in the instruction element.", 0, 1, temperatureQualifier);
          case -39203799: /*temperatureRange*/  return new Property("temperatureRange", "Range", "The temperature interval for this set of handling instructions.", 0, 1, temperatureRange);
          case 40284952: /*maxDuration*/  return new Property("maxDuration", "Duration", "The maximum time interval of preservation of the specimen with these conditions.", 0, 1, maxDuration);
          case 301526158: /*instruction*/  return new Property("instruction", "string", "Additional textual instructions for the preservation or transport of the specimen. For instance, 'Protect from light exposure'.", 0, 1, instruction);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 548941206: /*temperatureQualifier*/ return this.temperatureQualifier == null ? new Base[0] : new Base[] {this.temperatureQualifier}; // CodeableConcept
        case -39203799: /*temperatureRange*/ return this.temperatureRange == null ? new Base[0] : new Base[] {this.temperatureRange}; // Range
        case 40284952: /*maxDuration*/ return this.maxDuration == null ? new Base[0] : new Base[] {this.maxDuration}; // Duration
        case 301526158: /*instruction*/ return this.instruction == null ? new Base[0] : new Base[] {this.instruction}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 548941206: // temperatureQualifier
          this.temperatureQualifier = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -39203799: // temperatureRange
          this.temperatureRange = castToRange(value); // Range
          return value;
        case 40284952: // maxDuration
          this.maxDuration = castToDuration(value); // Duration
          return value;
        case 301526158: // instruction
          this.instruction = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("temperatureQualifier")) {
          this.temperatureQualifier = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("temperatureRange")) {
          this.temperatureRange = castToRange(value); // Range
        } else if (name.equals("maxDuration")) {
          this.maxDuration = castToDuration(value); // Duration
        } else if (name.equals("instruction")) {
          this.instruction = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 548941206:  return getTemperatureQualifier(); 
        case -39203799:  return getTemperatureRange(); 
        case 40284952:  return getMaxDuration(); 
        case 301526158:  return getInstructionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 548941206: /*temperatureQualifier*/ return new String[] {"CodeableConcept"};
        case -39203799: /*temperatureRange*/ return new String[] {"Range"};
        case 40284952: /*maxDuration*/ return new String[] {"Duration"};
        case 301526158: /*instruction*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("temperatureQualifier")) {
          this.temperatureQualifier = new CodeableConcept();
          return this.temperatureQualifier;
        }
        else if (name.equals("temperatureRange")) {
          this.temperatureRange = new Range();
          return this.temperatureRange;
        }
        else if (name.equals("maxDuration")) {
          this.maxDuration = new Duration();
          return this.maxDuration;
        }
        else if (name.equals("instruction")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.instruction");
        }
        else
          return super.addChild(name);
      }

      public SpecimenDefinitionTypeTestedHandlingComponent copy() {
        SpecimenDefinitionTypeTestedHandlingComponent dst = new SpecimenDefinitionTypeTestedHandlingComponent();
        copyValues(dst);
        dst.temperatureQualifier = temperatureQualifier == null ? null : temperatureQualifier.copy();
        dst.temperatureRange = temperatureRange == null ? null : temperatureRange.copy();
        dst.maxDuration = maxDuration == null ? null : maxDuration.copy();
        dst.instruction = instruction == null ? null : instruction.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedHandlingComponent))
          return false;
        SpecimenDefinitionTypeTestedHandlingComponent o = (SpecimenDefinitionTypeTestedHandlingComponent) other_;
        return compareDeep(temperatureQualifier, o.temperatureQualifier, true) && compareDeep(temperatureRange, o.temperatureRange, true)
           && compareDeep(maxDuration, o.maxDuration, true) && compareDeep(instruction, o.instruction, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinitionTypeTestedHandlingComponent))
          return false;
        SpecimenDefinitionTypeTestedHandlingComponent o = (SpecimenDefinitionTypeTestedHandlingComponent) other_;
        return compareValues(instruction, o.instruction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(temperatureQualifier, temperatureRange
          , maxDuration, instruction);
      }

  public String fhirType() {
    return "SpecimenDefinition.typeTested.handling";

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
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v2-0487")
    protected CodeableConcept typeCollected;

    /**
     * Preparation of the patient for specimen collection.
     */
    @Child(name = "patientPreparation", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Patient preparation for collection", formalDefinition="Preparation of the patient for specimen collection." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/prepare-patient-prior-specimen-collection")
    protected List<CodeableConcept> patientPreparation;

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
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/specimen-collection")
    protected List<CodeableConcept> collection;

    /**
     * Specimen conditioned in a container as expected by the testing laboratory.
     */
    @Child(name = "typeTested", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specimen in container intended for testing by lab", formalDefinition="Specimen conditioned in a container as expected by the testing laboratory." )
    protected List<SpecimenDefinitionTypeTestedComponent> typeTested;

    private static final long serialVersionUID = -330188872L;

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
     * @return {@link #patientPreparation} (Preparation of the patient for specimen collection.)
     */
    public List<CodeableConcept> getPatientPreparation() { 
      if (this.patientPreparation == null)
        this.patientPreparation = new ArrayList<CodeableConcept>();
      return this.patientPreparation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SpecimenDefinition setPatientPreparation(List<CodeableConcept> thePatientPreparation) { 
      this.patientPreparation = thePatientPreparation;
      return this;
    }

    public boolean hasPatientPreparation() { 
      if (this.patientPreparation == null)
        return false;
      for (CodeableConcept item : this.patientPreparation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addPatientPreparation() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.patientPreparation == null)
        this.patientPreparation = new ArrayList<CodeableConcept>();
      this.patientPreparation.add(t);
      return t;
    }

    public SpecimenDefinition addPatientPreparation(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.patientPreparation == null)
        this.patientPreparation = new ArrayList<CodeableConcept>();
      this.patientPreparation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #patientPreparation}, creating it if it does not already exist
     */
    public CodeableConcept getPatientPreparationFirstRep() { 
      if (getPatientPreparation().isEmpty()) {
        addPatientPreparation();
      }
      return getPatientPreparation().get(0);
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
     * @return {@link #typeTested} (Specimen conditioned in a container as expected by the testing laboratory.)
     */
    public List<SpecimenDefinitionTypeTestedComponent> getTypeTested() { 
      if (this.typeTested == null)
        this.typeTested = new ArrayList<SpecimenDefinitionTypeTestedComponent>();
      return this.typeTested;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public SpecimenDefinition setTypeTested(List<SpecimenDefinitionTypeTestedComponent> theTypeTested) { 
      this.typeTested = theTypeTested;
      return this;
    }

    public boolean hasTypeTested() { 
      if (this.typeTested == null)
        return false;
      for (SpecimenDefinitionTypeTestedComponent item : this.typeTested)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SpecimenDefinitionTypeTestedComponent addTypeTested() { //3
      SpecimenDefinitionTypeTestedComponent t = new SpecimenDefinitionTypeTestedComponent();
      if (this.typeTested == null)
        this.typeTested = new ArrayList<SpecimenDefinitionTypeTestedComponent>();
      this.typeTested.add(t);
      return t;
    }

    public SpecimenDefinition addTypeTested(SpecimenDefinitionTypeTestedComponent t) { //3
      if (t == null)
        return this;
      if (this.typeTested == null)
        this.typeTested = new ArrayList<SpecimenDefinitionTypeTestedComponent>();
      this.typeTested.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #typeTested}, creating it if it does not already exist
     */
    public SpecimenDefinitionTypeTestedComponent getTypeTestedFirstRep() { 
      if (getTypeTested().isEmpty()) {
        addTypeTested();
      }
      return getTypeTested().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A business identifier associated with the kind of specimen.", 0, 1, identifier));
        children.add(new Property("typeCollected", "CodeableConcept", "The kind of material to be collected.", 0, 1, typeCollected));
        children.add(new Property("patientPreparation", "CodeableConcept", "Preparation of the patient for specimen collection.", 0, java.lang.Integer.MAX_VALUE, patientPreparation));
        children.add(new Property("timeAspect", "string", "Time aspect of specimen collection (duration or offset).", 0, 1, timeAspect));
        children.add(new Property("collection", "CodeableConcept", "The action to be performed for collecting the specimen.", 0, java.lang.Integer.MAX_VALUE, collection));
        children.add(new Property("typeTested", "", "Specimen conditioned in a container as expected by the testing laboratory.", 0, java.lang.Integer.MAX_VALUE, typeTested));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A business identifier associated with the kind of specimen.", 0, 1, identifier);
        case 588504367: /*typeCollected*/  return new Property("typeCollected", "CodeableConcept", "The kind of material to be collected.", 0, 1, typeCollected);
        case -879411630: /*patientPreparation*/  return new Property("patientPreparation", "CodeableConcept", "Preparation of the patient for specimen collection.", 0, java.lang.Integer.MAX_VALUE, patientPreparation);
        case 276972933: /*timeAspect*/  return new Property("timeAspect", "string", "Time aspect of specimen collection (duration or offset).", 0, 1, timeAspect);
        case -1741312354: /*collection*/  return new Property("collection", "CodeableConcept", "The action to be performed for collecting the specimen.", 0, java.lang.Integer.MAX_VALUE, collection);
        case -1407902581: /*typeTested*/  return new Property("typeTested", "", "Specimen conditioned in a container as expected by the testing laboratory.", 0, java.lang.Integer.MAX_VALUE, typeTested);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 588504367: /*typeCollected*/ return this.typeCollected == null ? new Base[0] : new Base[] {this.typeCollected}; // CodeableConcept
        case -879411630: /*patientPreparation*/ return this.patientPreparation == null ? new Base[0] : this.patientPreparation.toArray(new Base[this.patientPreparation.size()]); // CodeableConcept
        case 276972933: /*timeAspect*/ return this.timeAspect == null ? new Base[0] : new Base[] {this.timeAspect}; // StringType
        case -1741312354: /*collection*/ return this.collection == null ? new Base[0] : this.collection.toArray(new Base[this.collection.size()]); // CodeableConcept
        case -1407902581: /*typeTested*/ return this.typeTested == null ? new Base[0] : this.typeTested.toArray(new Base[this.typeTested.size()]); // SpecimenDefinitionTypeTestedComponent
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
          this.getPatientPreparation().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 276972933: // timeAspect
          this.timeAspect = castToString(value); // StringType
          return value;
        case -1741312354: // collection
          this.getCollection().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1407902581: // typeTested
          this.getTypeTested().add((SpecimenDefinitionTypeTestedComponent) value); // SpecimenDefinitionTypeTestedComponent
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
          this.getPatientPreparation().add(castToCodeableConcept(value));
        } else if (name.equals("timeAspect")) {
          this.timeAspect = castToString(value); // StringType
        } else if (name.equals("collection")) {
          this.getCollection().add(castToCodeableConcept(value));
        } else if (name.equals("typeTested")) {
          this.getTypeTested().add((SpecimenDefinitionTypeTestedComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 588504367:  return getTypeCollected(); 
        case -879411630:  return addPatientPreparation(); 
        case 276972933:  return getTimeAspectElement();
        case -1741312354:  return addCollection(); 
        case -1407902581:  return addTypeTested(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 588504367: /*typeCollected*/ return new String[] {"CodeableConcept"};
        case -879411630: /*patientPreparation*/ return new String[] {"CodeableConcept"};
        case 276972933: /*timeAspect*/ return new String[] {"string"};
        case -1741312354: /*collection*/ return new String[] {"CodeableConcept"};
        case -1407902581: /*typeTested*/ return new String[] {};
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
          return addPatientPreparation();
        }
        else if (name.equals("timeAspect")) {
          throw new FHIRException("Cannot call addChild on a primitive type SpecimenDefinition.timeAspect");
        }
        else if (name.equals("collection")) {
          return addCollection();
        }
        else if (name.equals("typeTested")) {
          return addTypeTested();
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
        if (patientPreparation != null) {
          dst.patientPreparation = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : patientPreparation)
            dst.patientPreparation.add(i.copy());
        };
        dst.timeAspect = timeAspect == null ? null : timeAspect.copy();
        if (collection != null) {
          dst.collection = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : collection)
            dst.collection.add(i.copy());
        };
        if (typeTested != null) {
          dst.typeTested = new ArrayList<SpecimenDefinitionTypeTestedComponent>();
          for (SpecimenDefinitionTypeTestedComponent i : typeTested)
            dst.typeTested.add(i.copy());
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
           && compareDeep(collection, o.collection, true) && compareDeep(typeTested, o.typeTested, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SpecimenDefinition))
          return false;
        SpecimenDefinition o = (SpecimenDefinition) other_;
        return compareValues(timeAspect, o.timeAspect, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, typeCollected
          , patientPreparation, timeAspect, collection, typeTested);
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
   * Path: <b>SpecimenDefinition.typeTested.container.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="container", path="SpecimenDefinition.typeTested.container.type", description="The type of specimen conditioned in container expected by the lab", type="token" )
  public static final String SP_CONTAINER = "container";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>container</b>
   * <p>
   * Description: <b>The type of specimen conditioned in container expected by the lab</b><br>
   * Type: <b>token</b><br>
   * Path: <b>SpecimenDefinition.typeTested.container.type</b><br>
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

