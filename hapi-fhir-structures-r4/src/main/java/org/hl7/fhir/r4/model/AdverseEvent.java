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
 * Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.
 */
@ResourceDef(name="AdverseEvent", profile="http://hl7.org/fhir/StructureDefinition/AdverseEvent")
public class AdverseEvent extends DomainResource {

    public enum AdverseEventActuality {
        /**
         * The adverse event actually happened regardless of whether anyone was affected or harmed.
         */
        ACTUAL, 
        /**
         * A potential adverse event.
         */
        POTENTIAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AdverseEventActuality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("actual".equals(codeString))
          return ACTUAL;
        if ("potential".equals(codeString))
          return POTENTIAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AdverseEventActuality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTUAL: return "actual";
            case POTENTIAL: return "potential";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTUAL: return "http://hl7.org/fhir/adverse-event-actuality";
            case POTENTIAL: return "http://hl7.org/fhir/adverse-event-actuality";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTUAL: return "The adverse event actually happened regardless of whether anyone was affected or harmed.";
            case POTENTIAL: return "A potential adverse event.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTUAL: return "Adverse Event";
            case POTENTIAL: return "Potential Adverse Event";
            default: return "?";
          }
        }
    }

  public static class AdverseEventActualityEnumFactory implements EnumFactory<AdverseEventActuality> {
    public AdverseEventActuality fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("actual".equals(codeString))
          return AdverseEventActuality.ACTUAL;
        if ("potential".equals(codeString))
          return AdverseEventActuality.POTENTIAL;
        throw new IllegalArgumentException("Unknown AdverseEventActuality code '"+codeString+"'");
        }
        public Enumeration<AdverseEventActuality> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AdverseEventActuality>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("actual".equals(codeString))
          return new Enumeration<AdverseEventActuality>(this, AdverseEventActuality.ACTUAL);
        if ("potential".equals(codeString))
          return new Enumeration<AdverseEventActuality>(this, AdverseEventActuality.POTENTIAL);
        throw new FHIRException("Unknown AdverseEventActuality code '"+codeString+"'");
        }
    public String toCode(AdverseEventActuality code) {
      if (code == AdverseEventActuality.ACTUAL)
        return "actual";
      if (code == AdverseEventActuality.POTENTIAL)
        return "potential";
      return "?";
      }
    public String toSystem(AdverseEventActuality code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class AdverseEventSuspectEntityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.
         */
        @Child(name = "instance", type = {Immunization.class, Procedure.class, Substance.class, Medication.class, MedicationAdministration.class, MedicationStatement.class, Device.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Refers to the specific entity that caused the adverse event", formalDefinition="Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device." )
        protected Reference instance;

        /**
         * The actual object that is the target of the reference (Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.)
         */
        protected Resource instanceTarget;

        /**
         * Information on the possible cause of the event.
         */
        @Child(name = "causality", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Information on the possible cause of the event", formalDefinition="Information on the possible cause of the event." )
        protected List<AdverseEventSuspectEntityCausalityComponent> causality;

        private static final long serialVersionUID = 1245759325L;

    /**
     * Constructor
     */
      public AdverseEventSuspectEntityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AdverseEventSuspectEntityComponent(Reference instance) {
        super();
        this.instance = instance;
      }

        /**
         * @return {@link #instance} (Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.)
         */
        public Reference getInstance() { 
          if (this.instance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityComponent.instance");
            else if (Configuration.doAutoCreate())
              this.instance = new Reference(); // cc
          return this.instance;
        }

        public boolean hasInstance() { 
          return this.instance != null && !this.instance.isEmpty();
        }

        /**
         * @param value {@link #instance} (Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.)
         */
        public AdverseEventSuspectEntityComponent setInstance(Reference value) { 
          this.instance = value;
          return this;
        }

        /**
         * @return {@link #instance} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.)
         */
        public Resource getInstanceTarget() { 
          return this.instanceTarget;
        }

        /**
         * @param value {@link #instance} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.)
         */
        public AdverseEventSuspectEntityComponent setInstanceTarget(Resource value) { 
          this.instanceTarget = value;
          return this;
        }

        /**
         * @return {@link #causality} (Information on the possible cause of the event.)
         */
        public List<AdverseEventSuspectEntityCausalityComponent> getCausality() { 
          if (this.causality == null)
            this.causality = new ArrayList<AdverseEventSuspectEntityCausalityComponent>();
          return this.causality;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AdverseEventSuspectEntityComponent setCausality(List<AdverseEventSuspectEntityCausalityComponent> theCausality) { 
          this.causality = theCausality;
          return this;
        }

        public boolean hasCausality() { 
          if (this.causality == null)
            return false;
          for (AdverseEventSuspectEntityCausalityComponent item : this.causality)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AdverseEventSuspectEntityCausalityComponent addCausality() { //3
          AdverseEventSuspectEntityCausalityComponent t = new AdverseEventSuspectEntityCausalityComponent();
          if (this.causality == null)
            this.causality = new ArrayList<AdverseEventSuspectEntityCausalityComponent>();
          this.causality.add(t);
          return t;
        }

        public AdverseEventSuspectEntityComponent addCausality(AdverseEventSuspectEntityCausalityComponent t) { //3
          if (t == null)
            return this;
          if (this.causality == null)
            this.causality = new ArrayList<AdverseEventSuspectEntityCausalityComponent>();
          this.causality.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #causality}, creating it if it does not already exist
         */
        public AdverseEventSuspectEntityCausalityComponent getCausalityFirstRep() { 
          if (getCausality().isEmpty()) {
            addCausality();
          }
          return getCausality().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("instance", "Reference(Immunization|Procedure|Substance|Medication|MedicationAdministration|MedicationStatement|Device)", "Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.", 0, 1, instance));
          children.add(new Property("causality", "", "Information on the possible cause of the event.", 0, java.lang.Integer.MAX_VALUE, causality));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 555127957: /*instance*/  return new Property("instance", "Reference(Immunization|Procedure|Substance|Medication|MedicationAdministration|MedicationStatement|Device)", "Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.", 0, 1, instance);
          case -1446450521: /*causality*/  return new Property("causality", "", "Information on the possible cause of the event.", 0, java.lang.Integer.MAX_VALUE, causality);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : new Base[] {this.instance}; // Reference
        case -1446450521: /*causality*/ return this.causality == null ? new Base[0] : this.causality.toArray(new Base[this.causality.size()]); // AdverseEventSuspectEntityCausalityComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 555127957: // instance
          this.instance = castToReference(value); // Reference
          return value;
        case -1446450521: // causality
          this.getCausality().add((AdverseEventSuspectEntityCausalityComponent) value); // AdverseEventSuspectEntityCausalityComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("instance")) {
          this.instance = castToReference(value); // Reference
        } else if (name.equals("causality")) {
          this.getCausality().add((AdverseEventSuspectEntityCausalityComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 555127957:  return getInstance(); 
        case -1446450521:  return addCausality(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 555127957: /*instance*/ return new String[] {"Reference"};
        case -1446450521: /*causality*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("instance")) {
          this.instance = new Reference();
          return this.instance;
        }
        else if (name.equals("causality")) {
          return addCausality();
        }
        else
          return super.addChild(name);
      }

      public AdverseEventSuspectEntityComponent copy() {
        AdverseEventSuspectEntityComponent dst = new AdverseEventSuspectEntityComponent();
        copyValues(dst);
        dst.instance = instance == null ? null : instance.copy();
        if (causality != null) {
          dst.causality = new ArrayList<AdverseEventSuspectEntityCausalityComponent>();
          for (AdverseEventSuspectEntityCausalityComponent i : causality)
            dst.causality.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AdverseEventSuspectEntityComponent))
          return false;
        AdverseEventSuspectEntityComponent o = (AdverseEventSuspectEntityComponent) other_;
        return compareDeep(instance, o.instance, true) && compareDeep(causality, o.causality, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AdverseEventSuspectEntityComponent))
          return false;
        AdverseEventSuspectEntityComponent o = (AdverseEventSuspectEntityComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(instance, causality);
      }

  public String fhirType() {
    return "AdverseEvent.suspectEntity";

  }

  }

    @Block()
    public static class AdverseEventSuspectEntityCausalityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Assessment of if the entity caused the event.
         */
        @Child(name = "assessment", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Assessment of if the entity caused the event", formalDefinition="Assessment of if the entity caused the event." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-causality-assess")
        protected CodeableConcept assessment;

        /**
         * AdverseEvent.suspectEntity.causalityProductRelatedness.
         */
        @Child(name = "productRelatedness", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="AdverseEvent.suspectEntity.causalityProductRelatedness", formalDefinition="AdverseEvent.suspectEntity.causalityProductRelatedness." )
        protected StringType productRelatedness;

        /**
         * AdverseEvent.suspectEntity.causalityAuthor.
         */
        @Child(name = "author", type = {Practitioner.class, PractitionerRole.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="AdverseEvent.suspectEntity.causalityAuthor", formalDefinition="AdverseEvent.suspectEntity.causalityAuthor." )
        protected Reference author;

        /**
         * The actual object that is the target of the reference (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        protected Resource authorTarget;

        /**
         * ProbabilityScale | Bayesian | Checklist.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="ProbabilityScale | Bayesian | Checklist", formalDefinition="ProbabilityScale | Bayesian | Checklist." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-causality-method")
        protected CodeableConcept method;

        private static final long serialVersionUID = -1847234837L;

    /**
     * Constructor
     */
      public AdverseEventSuspectEntityCausalityComponent() {
        super();
      }

        /**
         * @return {@link #assessment} (Assessment of if the entity caused the event.)
         */
        public CodeableConcept getAssessment() { 
          if (this.assessment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityCausalityComponent.assessment");
            else if (Configuration.doAutoCreate())
              this.assessment = new CodeableConcept(); // cc
          return this.assessment;
        }

        public boolean hasAssessment() { 
          return this.assessment != null && !this.assessment.isEmpty();
        }

        /**
         * @param value {@link #assessment} (Assessment of if the entity caused the event.)
         */
        public AdverseEventSuspectEntityCausalityComponent setAssessment(CodeableConcept value) { 
          this.assessment = value;
          return this;
        }

        /**
         * @return {@link #productRelatedness} (AdverseEvent.suspectEntity.causalityProductRelatedness.). This is the underlying object with id, value and extensions. The accessor "getProductRelatedness" gives direct access to the value
         */
        public StringType getProductRelatednessElement() { 
          if (this.productRelatedness == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityCausalityComponent.productRelatedness");
            else if (Configuration.doAutoCreate())
              this.productRelatedness = new StringType(); // bb
          return this.productRelatedness;
        }

        public boolean hasProductRelatednessElement() { 
          return this.productRelatedness != null && !this.productRelatedness.isEmpty();
        }

        public boolean hasProductRelatedness() { 
          return this.productRelatedness != null && !this.productRelatedness.isEmpty();
        }

        /**
         * @param value {@link #productRelatedness} (AdverseEvent.suspectEntity.causalityProductRelatedness.). This is the underlying object with id, value and extensions. The accessor "getProductRelatedness" gives direct access to the value
         */
        public AdverseEventSuspectEntityCausalityComponent setProductRelatednessElement(StringType value) { 
          this.productRelatedness = value;
          return this;
        }

        /**
         * @return AdverseEvent.suspectEntity.causalityProductRelatedness.
         */
        public String getProductRelatedness() { 
          return this.productRelatedness == null ? null : this.productRelatedness.getValue();
        }

        /**
         * @param value AdverseEvent.suspectEntity.causalityProductRelatedness.
         */
        public AdverseEventSuspectEntityCausalityComponent setProductRelatedness(String value) { 
          if (Utilities.noString(value))
            this.productRelatedness = null;
          else {
            if (this.productRelatedness == null)
              this.productRelatedness = new StringType();
            this.productRelatedness.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #author} (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public Reference getAuthor() { 
          if (this.author == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityCausalityComponent.author");
            else if (Configuration.doAutoCreate())
              this.author = new Reference(); // cc
          return this.author;
        }

        public boolean hasAuthor() { 
          return this.author != null && !this.author.isEmpty();
        }

        /**
         * @param value {@link #author} (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public AdverseEventSuspectEntityCausalityComponent setAuthor(Reference value) { 
          this.author = value;
          return this;
        }

        /**
         * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public Resource getAuthorTarget() { 
          return this.authorTarget;
        }

        /**
         * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public AdverseEventSuspectEntityCausalityComponent setAuthorTarget(Resource value) { 
          this.authorTarget = value;
          return this;
        }

        /**
         * @return {@link #method} (ProbabilityScale | Bayesian | Checklist.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityCausalityComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (ProbabilityScale | Bayesian | Checklist.)
         */
        public AdverseEventSuspectEntityCausalityComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("assessment", "CodeableConcept", "Assessment of if the entity caused the event.", 0, 1, assessment));
          children.add(new Property("productRelatedness", "string", "AdverseEvent.suspectEntity.causalityProductRelatedness.", 0, 1, productRelatedness));
          children.add(new Property("author", "Reference(Practitioner|PractitionerRole)", "AdverseEvent.suspectEntity.causalityAuthor.", 0, 1, author));
          children.add(new Property("method", "CodeableConcept", "ProbabilityScale | Bayesian | Checklist.", 0, 1, method));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 2119382722: /*assessment*/  return new Property("assessment", "CodeableConcept", "Assessment of if the entity caused the event.", 0, 1, assessment);
          case 1824577683: /*productRelatedness*/  return new Property("productRelatedness", "string", "AdverseEvent.suspectEntity.causalityProductRelatedness.", 0, 1, productRelatedness);
          case -1406328437: /*author*/  return new Property("author", "Reference(Practitioner|PractitionerRole)", "AdverseEvent.suspectEntity.causalityAuthor.", 0, 1, author);
          case -1077554975: /*method*/  return new Property("method", "CodeableConcept", "ProbabilityScale | Bayesian | Checklist.", 0, 1, method);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 2119382722: /*assessment*/ return this.assessment == null ? new Base[0] : new Base[] {this.assessment}; // CodeableConcept
        case 1824577683: /*productRelatedness*/ return this.productRelatedness == null ? new Base[0] : new Base[] {this.productRelatedness}; // StringType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 2119382722: // assessment
          this.assessment = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1824577683: // productRelatedness
          this.productRelatedness = castToString(value); // StringType
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("assessment")) {
          this.assessment = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("productRelatedness")) {
          this.productRelatedness = castToString(value); // StringType
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else if (name.equals("method")) {
          this.method = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2119382722:  return getAssessment(); 
        case 1824577683:  return getProductRelatednessElement();
        case -1406328437:  return getAuthor(); 
        case -1077554975:  return getMethod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2119382722: /*assessment*/ return new String[] {"CodeableConcept"};
        case 1824577683: /*productRelatedness*/ return new String[] {"string"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("assessment")) {
          this.assessment = new CodeableConcept();
          return this.assessment;
        }
        else if (name.equals("productRelatedness")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.productRelatedness");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else
          return super.addChild(name);
      }

      public AdverseEventSuspectEntityCausalityComponent copy() {
        AdverseEventSuspectEntityCausalityComponent dst = new AdverseEventSuspectEntityCausalityComponent();
        copyValues(dst);
        dst.assessment = assessment == null ? null : assessment.copy();
        dst.productRelatedness = productRelatedness == null ? null : productRelatedness.copy();
        dst.author = author == null ? null : author.copy();
        dst.method = method == null ? null : method.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AdverseEventSuspectEntityCausalityComponent))
          return false;
        AdverseEventSuspectEntityCausalityComponent o = (AdverseEventSuspectEntityCausalityComponent) other_;
        return compareDeep(assessment, o.assessment, true) && compareDeep(productRelatedness, o.productRelatedness, true)
           && compareDeep(author, o.author, true) && compareDeep(method, o.method, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AdverseEventSuspectEntityCausalityComponent))
          return false;
        AdverseEventSuspectEntityCausalityComponent o = (AdverseEventSuspectEntityCausalityComponent) other_;
        return compareValues(productRelatedness, o.productRelatedness, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(assessment, productRelatedness
          , author, method);
      }

  public String fhirType() {
    return "AdverseEvent.suspectEntity.causality";

  }

  }

    /**
     * Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the resource is updated and propagates from server to server.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier for the event", formalDefinition="Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the resource is updated and propagates from server to server." )
    protected Identifier identifier;

    /**
     * Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely.
     */
    @Child(name = "actuality", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="actual | potential", formalDefinition="Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-actuality")
    protected Enumeration<AdverseEventActuality> actuality;

    /**
     * The overall type of event, intended for search and filtering purposes.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="product-problem | product-quality | product-use-error | wrong-dose | incorrect-prescribing-information | wrong-technique | wrong-route-of-administration | wrong-rate | wrong-duration | wrong-time | expired-drug | medical-device-use-error | problem-different-manufacturer | unsafe-physical-environment", formalDefinition="The overall type of event, intended for search and filtering purposes." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-category")
    protected List<CodeableConcept> category;

    /**
     * This element defines the specific type of event that occurred or that was prevented from occurring.
     */
    @Child(name = "event", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of the event itself in relation to the subject", formalDefinition="This element defines the specific type of event that occurred or that was prevented from occurring." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-type")
    protected CodeableConcept event;

    /**
     * This subject or group impacted by the event.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Practitioner.class, RelatedPerson.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Subject impacted by event", formalDefinition="This subject or group impacted by the event." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (This subject or group impacted by the event.)
     */
    protected Resource subjectTarget;

    /**
     * The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter created as part of", formalDefinition="The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.)
     */
    protected Encounter encounterTarget;

    /**
     * The date (and perhaps time) when the adverse event occurred.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the event occurred", formalDefinition="The date (and perhaps time) when the adverse event occurred." )
    protected DateTimeType date;

    /**
     * Estimated or actual date the AdverseEvent began, in the opinion of the reporter.
     */
    @Child(name = "detected", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the event was detected", formalDefinition="Estimated or actual date the AdverseEvent began, in the opinion of the reporter." )
    protected DateTimeType detected;

    /**
     * The date on which the existence of the AdverseEvent was first recorded.
     */
    @Child(name = "recordedDate", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the event was recorded", formalDefinition="The date on which the existence of the AdverseEvent was first recorded." )
    protected DateTimeType recordedDate;

    /**
     * Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).
     */
    @Child(name = "resultingCondition", type = {Condition.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Effect on the subject due to this event", formalDefinition="Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical)." )
    protected List<Reference> resultingCondition;
    /**
     * The actual objects that are the target of the reference (Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).)
     */
    protected List<Condition> resultingConditionTarget;


    /**
     * The information about where the adverse event occurred.
     */
    @Child(name = "location", type = {Location.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Location where adverse event occurred", formalDefinition="The information about where the adverse event occurred." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The information about where the adverse event occurred.)
     */
    protected Location locationTarget;

    /**
     * Assessment whether this event was of real importance.
     */
    @Child(name = "seriousness", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Seriousness of the event", formalDefinition="Assessment whether this event was of real importance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-seriousness")
    protected CodeableConcept seriousness;

    /**
     * Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a severe rash might not be serious, but a mild heart problem is.
     */
    @Child(name = "severity", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="mild | moderate | severe", formalDefinition="Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a severe rash might not be serious, but a mild heart problem is." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-severity")
    protected CodeableConcept severity;

    /**
     * Describes the type of outcome from the adverse event.
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="resolved | recovering | ongoing | resolvedWithSequelae | fatal | unknown", formalDefinition="Describes the type of outcome from the adverse event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-outcome")
    protected CodeableConcept outcome;

    /**
     * Information on who recorded the adverse event.  May be the patient or a practitioner.
     */
    @Child(name = "recorder", type = {Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who recorded the adverse event", formalDefinition="Information on who recorded the adverse event.  May be the patient or a practitioner." )
    protected Reference recorder;

    /**
     * The actual object that is the target of the reference (Information on who recorded the adverse event.  May be the patient or a practitioner.)
     */
    protected Resource recorderTarget;

    /**
     * Parties that may or should contribute or have contributed information to the adverse event, which can consist of one or more activities.  Such information includes information leading to the decision to perform the activity and how to perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of clinical history), or information about what activity was performed (e.g. informant witness).
     */
    @Child(name = "contributor", type = {Practitioner.class, PractitionerRole.class, Device.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who  was involved in the adverse event or the potential adverse event", formalDefinition="Parties that may or should contribute or have contributed information to the adverse event, which can consist of one or more activities.  Such information includes information leading to the decision to perform the activity and how to perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of clinical history), or information about what activity was performed (e.g. informant witness)." )
    protected List<Reference> contributor;
    /**
     * The actual objects that are the target of the reference (Parties that may or should contribute or have contributed information to the adverse event, which can consist of one or more activities.  Such information includes information leading to the decision to perform the activity and how to perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of clinical history), or information about what activity was performed (e.g. informant witness).)
     */
    protected List<Resource> contributorTarget;


    /**
     * Describes the entity that is suspected to have caused the adverse event.
     */
    @Child(name = "suspectEntity", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The suspected agent causing the adverse event", formalDefinition="Describes the entity that is suspected to have caused the adverse event." )
    protected List<AdverseEventSuspectEntityComponent> suspectEntity;

    /**
     * AdverseEvent.subjectMedicalHistory.
     */
    @Child(name = "subjectMedicalHistory", type = {Condition.class, Observation.class, AllergyIntolerance.class, FamilyMemberHistory.class, Immunization.class, Procedure.class, Media.class, DocumentReference.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="AdverseEvent.subjectMedicalHistory", formalDefinition="AdverseEvent.subjectMedicalHistory." )
    protected List<Reference> subjectMedicalHistory;
    /**
     * The actual objects that are the target of the reference (AdverseEvent.subjectMedicalHistory.)
     */
    protected List<Resource> subjectMedicalHistoryTarget;


    /**
     * AdverseEvent.referenceDocument.
     */
    @Child(name = "referenceDocument", type = {DocumentReference.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="AdverseEvent.referenceDocument", formalDefinition="AdverseEvent.referenceDocument." )
    protected List<Reference> referenceDocument;
    /**
     * The actual objects that are the target of the reference (AdverseEvent.referenceDocument.)
     */
    protected List<DocumentReference> referenceDocumentTarget;


    /**
     * AdverseEvent.study.
     */
    @Child(name = "study", type = {ResearchStudy.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="AdverseEvent.study", formalDefinition="AdverseEvent.study." )
    protected List<Reference> study;
    /**
     * The actual objects that are the target of the reference (AdverseEvent.study.)
     */
    protected List<ResearchStudy> studyTarget;


    private static final long serialVersionUID = -2055195281L;

  /**
   * Constructor
   */
    public AdverseEvent() {
      super();
    }

  /**
   * Constructor
   */
    public AdverseEvent(Enumeration<AdverseEventActuality> actuality, Reference subject) {
      super();
      this.actuality = actuality;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the resource is updated and propagates from server to server.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the resource is updated and propagates from server to server.)
     */
    public AdverseEvent setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #actuality} (Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely.). This is the underlying object with id, value and extensions. The accessor "getActuality" gives direct access to the value
     */
    public Enumeration<AdverseEventActuality> getActualityElement() { 
      if (this.actuality == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.actuality");
        else if (Configuration.doAutoCreate())
          this.actuality = new Enumeration<AdverseEventActuality>(new AdverseEventActualityEnumFactory()); // bb
      return this.actuality;
    }

    public boolean hasActualityElement() { 
      return this.actuality != null && !this.actuality.isEmpty();
    }

    public boolean hasActuality() { 
      return this.actuality != null && !this.actuality.isEmpty();
    }

    /**
     * @param value {@link #actuality} (Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely.). This is the underlying object with id, value and extensions. The accessor "getActuality" gives direct access to the value
     */
    public AdverseEvent setActualityElement(Enumeration<AdverseEventActuality> value) { 
      this.actuality = value;
      return this;
    }

    /**
     * @return Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely.
     */
    public AdverseEventActuality getActuality() { 
      return this.actuality == null ? null : this.actuality.getValue();
    }

    /**
     * @param value Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely.
     */
    public AdverseEvent setActuality(AdverseEventActuality value) { 
        if (this.actuality == null)
          this.actuality = new Enumeration<AdverseEventActuality>(new AdverseEventActualityEnumFactory());
        this.actuality.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (The overall type of event, intended for search and filtering purposes.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public AdverseEvent addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
    }

    /**
     * @return {@link #event} (This element defines the specific type of event that occurred or that was prevented from occurring.)
     */
    public CodeableConcept getEvent() { 
      if (this.event == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.event");
        else if (Configuration.doAutoCreate())
          this.event = new CodeableConcept(); // cc
      return this.event;
    }

    public boolean hasEvent() { 
      return this.event != null && !this.event.isEmpty();
    }

    /**
     * @param value {@link #event} (This element defines the specific type of event that occurred or that was prevented from occurring.)
     */
    public AdverseEvent setEvent(CodeableConcept value) { 
      this.event = value;
      return this;
    }

    /**
     * @return {@link #subject} (This subject or group impacted by the event.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (This subject or group impacted by the event.)
     */
    public AdverseEvent setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This subject or group impacted by the event.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This subject or group impacted by the event.)
     */
    public AdverseEvent setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.)
     */
    public AdverseEvent setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.)
     */
    public AdverseEvent setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date (and perhaps time) when the adverse event occurred.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date (and perhaps time) when the adverse event occurred.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public AdverseEvent setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date (and perhaps time) when the adverse event occurred.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date (and perhaps time) when the adverse event occurred.
     */
    public AdverseEvent setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #detected} (Estimated or actual date the AdverseEvent began, in the opinion of the reporter.). This is the underlying object with id, value and extensions. The accessor "getDetected" gives direct access to the value
     */
    public DateTimeType getDetectedElement() { 
      if (this.detected == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.detected");
        else if (Configuration.doAutoCreate())
          this.detected = new DateTimeType(); // bb
      return this.detected;
    }

    public boolean hasDetectedElement() { 
      return this.detected != null && !this.detected.isEmpty();
    }

    public boolean hasDetected() { 
      return this.detected != null && !this.detected.isEmpty();
    }

    /**
     * @param value {@link #detected} (Estimated or actual date the AdverseEvent began, in the opinion of the reporter.). This is the underlying object with id, value and extensions. The accessor "getDetected" gives direct access to the value
     */
    public AdverseEvent setDetectedElement(DateTimeType value) { 
      this.detected = value;
      return this;
    }

    /**
     * @return Estimated or actual date the AdverseEvent began, in the opinion of the reporter.
     */
    public Date getDetected() { 
      return this.detected == null ? null : this.detected.getValue();
    }

    /**
     * @param value Estimated or actual date the AdverseEvent began, in the opinion of the reporter.
     */
    public AdverseEvent setDetected(Date value) { 
      if (value == null)
        this.detected = null;
      else {
        if (this.detected == null)
          this.detected = new DateTimeType();
        this.detected.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #recordedDate} (The date on which the existence of the AdverseEvent was first recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedDate" gives direct access to the value
     */
    public DateTimeType getRecordedDateElement() { 
      if (this.recordedDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.recordedDate");
        else if (Configuration.doAutoCreate())
          this.recordedDate = new DateTimeType(); // bb
      return this.recordedDate;
    }

    public boolean hasRecordedDateElement() { 
      return this.recordedDate != null && !this.recordedDate.isEmpty();
    }

    public boolean hasRecordedDate() { 
      return this.recordedDate != null && !this.recordedDate.isEmpty();
    }

    /**
     * @param value {@link #recordedDate} (The date on which the existence of the AdverseEvent was first recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedDate" gives direct access to the value
     */
    public AdverseEvent setRecordedDateElement(DateTimeType value) { 
      this.recordedDate = value;
      return this;
    }

    /**
     * @return The date on which the existence of the AdverseEvent was first recorded.
     */
    public Date getRecordedDate() { 
      return this.recordedDate == null ? null : this.recordedDate.getValue();
    }

    /**
     * @param value The date on which the existence of the AdverseEvent was first recorded.
     */
    public AdverseEvent setRecordedDate(Date value) { 
      if (value == null)
        this.recordedDate = null;
      else {
        if (this.recordedDate == null)
          this.recordedDate = new DateTimeType();
        this.recordedDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #resultingCondition} (Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).)
     */
    public List<Reference> getResultingCondition() { 
      if (this.resultingCondition == null)
        this.resultingCondition = new ArrayList<Reference>();
      return this.resultingCondition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setResultingCondition(List<Reference> theResultingCondition) { 
      this.resultingCondition = theResultingCondition;
      return this;
    }

    public boolean hasResultingCondition() { 
      if (this.resultingCondition == null)
        return false;
      for (Reference item : this.resultingCondition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addResultingCondition() { //3
      Reference t = new Reference();
      if (this.resultingCondition == null)
        this.resultingCondition = new ArrayList<Reference>();
      this.resultingCondition.add(t);
      return t;
    }

    public AdverseEvent addResultingCondition(Reference t) { //3
      if (t == null)
        return this;
      if (this.resultingCondition == null)
        this.resultingCondition = new ArrayList<Reference>();
      this.resultingCondition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #resultingCondition}, creating it if it does not already exist
     */
    public Reference getResultingConditionFirstRep() { 
      if (getResultingCondition().isEmpty()) {
        addResultingCondition();
      }
      return getResultingCondition().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Condition> getResultingConditionTarget() { 
      if (this.resultingConditionTarget == null)
        this.resultingConditionTarget = new ArrayList<Condition>();
      return this.resultingConditionTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Condition addResultingConditionTarget() { 
      Condition r = new Condition();
      if (this.resultingConditionTarget == null)
        this.resultingConditionTarget = new ArrayList<Condition>();
      this.resultingConditionTarget.add(r);
      return r;
    }

    /**
     * @return {@link #location} (The information about where the adverse event occurred.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The information about where the adverse event occurred.)
     */
    public AdverseEvent setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The information about where the adverse event occurred.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The information about where the adverse event occurred.)
     */
    public AdverseEvent setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #seriousness} (Assessment whether this event was of real importance.)
     */
    public CodeableConcept getSeriousness() { 
      if (this.seriousness == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.seriousness");
        else if (Configuration.doAutoCreate())
          this.seriousness = new CodeableConcept(); // cc
      return this.seriousness;
    }

    public boolean hasSeriousness() { 
      return this.seriousness != null && !this.seriousness.isEmpty();
    }

    /**
     * @param value {@link #seriousness} (Assessment whether this event was of real importance.)
     */
    public AdverseEvent setSeriousness(CodeableConcept value) { 
      this.seriousness = value;
      return this;
    }

    /**
     * @return {@link #severity} (Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a severe rash might not be serious, but a mild heart problem is.)
     */
    public CodeableConcept getSeverity() { 
      if (this.severity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.severity");
        else if (Configuration.doAutoCreate())
          this.severity = new CodeableConcept(); // cc
      return this.severity;
    }

    public boolean hasSeverity() { 
      return this.severity != null && !this.severity.isEmpty();
    }

    /**
     * @param value {@link #severity} (Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a severe rash might not be serious, but a mild heart problem is.)
     */
    public AdverseEvent setSeverity(CodeableConcept value) { 
      this.severity = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Describes the type of outcome from the adverse event.)
     */
    public CodeableConcept getOutcome() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new CodeableConcept(); // cc
      return this.outcome;
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Describes the type of outcome from the adverse event.)
     */
    public AdverseEvent setOutcome(CodeableConcept value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return {@link #recorder} (Information on who recorded the adverse event.  May be the patient or a practitioner.)
     */
    public Reference getRecorder() { 
      if (this.recorder == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.recorder");
        else if (Configuration.doAutoCreate())
          this.recorder = new Reference(); // cc
      return this.recorder;
    }

    public boolean hasRecorder() { 
      return this.recorder != null && !this.recorder.isEmpty();
    }

    /**
     * @param value {@link #recorder} (Information on who recorded the adverse event.  May be the patient or a practitioner.)
     */
    public AdverseEvent setRecorder(Reference value) { 
      this.recorder = value;
      return this;
    }

    /**
     * @return {@link #recorder} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Information on who recorded the adverse event.  May be the patient or a practitioner.)
     */
    public Resource getRecorderTarget() { 
      return this.recorderTarget;
    }

    /**
     * @param value {@link #recorder} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Information on who recorded the adverse event.  May be the patient or a practitioner.)
     */
    public AdverseEvent setRecorderTarget(Resource value) { 
      this.recorderTarget = value;
      return this;
    }

    /**
     * @return {@link #contributor} (Parties that may or should contribute or have contributed information to the adverse event, which can consist of one or more activities.  Such information includes information leading to the decision to perform the activity and how to perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of clinical history), or information about what activity was performed (e.g. informant witness).)
     */
    public List<Reference> getContributor() { 
      if (this.contributor == null)
        this.contributor = new ArrayList<Reference>();
      return this.contributor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setContributor(List<Reference> theContributor) { 
      this.contributor = theContributor;
      return this;
    }

    public boolean hasContributor() { 
      if (this.contributor == null)
        return false;
      for (Reference item : this.contributor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addContributor() { //3
      Reference t = new Reference();
      if (this.contributor == null)
        this.contributor = new ArrayList<Reference>();
      this.contributor.add(t);
      return t;
    }

    public AdverseEvent addContributor(Reference t) { //3
      if (t == null)
        return this;
      if (this.contributor == null)
        this.contributor = new ArrayList<Reference>();
      this.contributor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contributor}, creating it if it does not already exist
     */
    public Reference getContributorFirstRep() { 
      if (getContributor().isEmpty()) {
        addContributor();
      }
      return getContributor().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getContributorTarget() { 
      if (this.contributorTarget == null)
        this.contributorTarget = new ArrayList<Resource>();
      return this.contributorTarget;
    }

    /**
     * @return {@link #suspectEntity} (Describes the entity that is suspected to have caused the adverse event.)
     */
    public List<AdverseEventSuspectEntityComponent> getSuspectEntity() { 
      if (this.suspectEntity == null)
        this.suspectEntity = new ArrayList<AdverseEventSuspectEntityComponent>();
      return this.suspectEntity;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setSuspectEntity(List<AdverseEventSuspectEntityComponent> theSuspectEntity) { 
      this.suspectEntity = theSuspectEntity;
      return this;
    }

    public boolean hasSuspectEntity() { 
      if (this.suspectEntity == null)
        return false;
      for (AdverseEventSuspectEntityComponent item : this.suspectEntity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public AdverseEventSuspectEntityComponent addSuspectEntity() { //3
      AdverseEventSuspectEntityComponent t = new AdverseEventSuspectEntityComponent();
      if (this.suspectEntity == null)
        this.suspectEntity = new ArrayList<AdverseEventSuspectEntityComponent>();
      this.suspectEntity.add(t);
      return t;
    }

    public AdverseEvent addSuspectEntity(AdverseEventSuspectEntityComponent t) { //3
      if (t == null)
        return this;
      if (this.suspectEntity == null)
        this.suspectEntity = new ArrayList<AdverseEventSuspectEntityComponent>();
      this.suspectEntity.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #suspectEntity}, creating it if it does not already exist
     */
    public AdverseEventSuspectEntityComponent getSuspectEntityFirstRep() { 
      if (getSuspectEntity().isEmpty()) {
        addSuspectEntity();
      }
      return getSuspectEntity().get(0);
    }

    /**
     * @return {@link #subjectMedicalHistory} (AdverseEvent.subjectMedicalHistory.)
     */
    public List<Reference> getSubjectMedicalHistory() { 
      if (this.subjectMedicalHistory == null)
        this.subjectMedicalHistory = new ArrayList<Reference>();
      return this.subjectMedicalHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setSubjectMedicalHistory(List<Reference> theSubjectMedicalHistory) { 
      this.subjectMedicalHistory = theSubjectMedicalHistory;
      return this;
    }

    public boolean hasSubjectMedicalHistory() { 
      if (this.subjectMedicalHistory == null)
        return false;
      for (Reference item : this.subjectMedicalHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSubjectMedicalHistory() { //3
      Reference t = new Reference();
      if (this.subjectMedicalHistory == null)
        this.subjectMedicalHistory = new ArrayList<Reference>();
      this.subjectMedicalHistory.add(t);
      return t;
    }

    public AdverseEvent addSubjectMedicalHistory(Reference t) { //3
      if (t == null)
        return this;
      if (this.subjectMedicalHistory == null)
        this.subjectMedicalHistory = new ArrayList<Reference>();
      this.subjectMedicalHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subjectMedicalHistory}, creating it if it does not already exist
     */
    public Reference getSubjectMedicalHistoryFirstRep() { 
      if (getSubjectMedicalHistory().isEmpty()) {
        addSubjectMedicalHistory();
      }
      return getSubjectMedicalHistory().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSubjectMedicalHistoryTarget() { 
      if (this.subjectMedicalHistoryTarget == null)
        this.subjectMedicalHistoryTarget = new ArrayList<Resource>();
      return this.subjectMedicalHistoryTarget;
    }

    /**
     * @return {@link #referenceDocument} (AdverseEvent.referenceDocument.)
     */
    public List<Reference> getReferenceDocument() { 
      if (this.referenceDocument == null)
        this.referenceDocument = new ArrayList<Reference>();
      return this.referenceDocument;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setReferenceDocument(List<Reference> theReferenceDocument) { 
      this.referenceDocument = theReferenceDocument;
      return this;
    }

    public boolean hasReferenceDocument() { 
      if (this.referenceDocument == null)
        return false;
      for (Reference item : this.referenceDocument)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReferenceDocument() { //3
      Reference t = new Reference();
      if (this.referenceDocument == null)
        this.referenceDocument = new ArrayList<Reference>();
      this.referenceDocument.add(t);
      return t;
    }

    public AdverseEvent addReferenceDocument(Reference t) { //3
      if (t == null)
        return this;
      if (this.referenceDocument == null)
        this.referenceDocument = new ArrayList<Reference>();
      this.referenceDocument.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #referenceDocument}, creating it if it does not already exist
     */
    public Reference getReferenceDocumentFirstRep() { 
      if (getReferenceDocument().isEmpty()) {
        addReferenceDocument();
      }
      return getReferenceDocument().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<DocumentReference> getReferenceDocumentTarget() { 
      if (this.referenceDocumentTarget == null)
        this.referenceDocumentTarget = new ArrayList<DocumentReference>();
      return this.referenceDocumentTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public DocumentReference addReferenceDocumentTarget() { 
      DocumentReference r = new DocumentReference();
      if (this.referenceDocumentTarget == null)
        this.referenceDocumentTarget = new ArrayList<DocumentReference>();
      this.referenceDocumentTarget.add(r);
      return r;
    }

    /**
     * @return {@link #study} (AdverseEvent.study.)
     */
    public List<Reference> getStudy() { 
      if (this.study == null)
        this.study = new ArrayList<Reference>();
      return this.study;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setStudy(List<Reference> theStudy) { 
      this.study = theStudy;
      return this;
    }

    public boolean hasStudy() { 
      if (this.study == null)
        return false;
      for (Reference item : this.study)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addStudy() { //3
      Reference t = new Reference();
      if (this.study == null)
        this.study = new ArrayList<Reference>();
      this.study.add(t);
      return t;
    }

    public AdverseEvent addStudy(Reference t) { //3
      if (t == null)
        return this;
      if (this.study == null)
        this.study = new ArrayList<Reference>();
      this.study.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #study}, creating it if it does not already exist
     */
    public Reference getStudyFirstRep() { 
      if (getStudy().isEmpty()) {
        addStudy();
      }
      return getStudy().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<ResearchStudy> getStudyTarget() { 
      if (this.studyTarget == null)
        this.studyTarget = new ArrayList<ResearchStudy>();
      return this.studyTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ResearchStudy addStudyTarget() { 
      ResearchStudy r = new ResearchStudy();
      if (this.studyTarget == null)
        this.studyTarget = new ArrayList<ResearchStudy>();
      this.studyTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the resource is updated and propagates from server to server.", 0, 1, identifier));
        children.add(new Property("actuality", "code", "Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely.", 0, 1, actuality));
        children.add(new Property("category", "CodeableConcept", "The overall type of event, intended for search and filtering purposes.", 0, java.lang.Integer.MAX_VALUE, category));
        children.add(new Property("event", "CodeableConcept", "This element defines the specific type of event that occurred or that was prevented from occurring.", 0, 1, event));
        children.add(new Property("subject", "Reference(Patient|Group|Practitioner|RelatedPerson)", "This subject or group impacted by the event.", 0, 1, subject));
        children.add(new Property("encounter", "Reference(Encounter)", "The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.", 0, 1, encounter));
        children.add(new Property("date", "dateTime", "The date (and perhaps time) when the adverse event occurred.", 0, 1, date));
        children.add(new Property("detected", "dateTime", "Estimated or actual date the AdverseEvent began, in the opinion of the reporter.", 0, 1, detected));
        children.add(new Property("recordedDate", "dateTime", "The date on which the existence of the AdverseEvent was first recorded.", 0, 1, recordedDate));
        children.add(new Property("resultingCondition", "Reference(Condition)", "Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).", 0, java.lang.Integer.MAX_VALUE, resultingCondition));
        children.add(new Property("location", "Reference(Location)", "The information about where the adverse event occurred.", 0, 1, location));
        children.add(new Property("seriousness", "CodeableConcept", "Assessment whether this event was of real importance.", 0, 1, seriousness));
        children.add(new Property("severity", "CodeableConcept", "Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a severe rash might not be serious, but a mild heart problem is.", 0, 1, severity));
        children.add(new Property("outcome", "CodeableConcept", "Describes the type of outcome from the adverse event.", 0, 1, outcome));
        children.add(new Property("recorder", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson)", "Information on who recorded the adverse event.  May be the patient or a practitioner.", 0, 1, recorder));
        children.add(new Property("contributor", "Reference(Practitioner|PractitionerRole|Device)", "Parties that may or should contribute or have contributed information to the adverse event, which can consist of one or more activities.  Such information includes information leading to the decision to perform the activity and how to perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of clinical history), or information about what activity was performed (e.g. informant witness).", 0, java.lang.Integer.MAX_VALUE, contributor));
        children.add(new Property("suspectEntity", "", "Describes the entity that is suspected to have caused the adverse event.", 0, java.lang.Integer.MAX_VALUE, suspectEntity));
        children.add(new Property("subjectMedicalHistory", "Reference(Condition|Observation|AllergyIntolerance|FamilyMemberHistory|Immunization|Procedure|Media|DocumentReference)", "AdverseEvent.subjectMedicalHistory.", 0, java.lang.Integer.MAX_VALUE, subjectMedicalHistory));
        children.add(new Property("referenceDocument", "Reference(DocumentReference)", "AdverseEvent.referenceDocument.", 0, java.lang.Integer.MAX_VALUE, referenceDocument));
        children.add(new Property("study", "Reference(ResearchStudy)", "AdverseEvent.study.", 0, java.lang.Integer.MAX_VALUE, study));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifiers assigned to this adverse event by the performer or other systems which remain constant as the resource is updated and propagates from server to server.", 0, 1, identifier);
        case 528866400: /*actuality*/  return new Property("actuality", "code", "Whether the event actually happened, or just had the potential to. Note that this is independent of whether anyone was affected or harmed or how severely.", 0, 1, actuality);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "The overall type of event, intended for search and filtering purposes.", 0, java.lang.Integer.MAX_VALUE, category);
        case 96891546: /*event*/  return new Property("event", "CodeableConcept", "This element defines the specific type of event that occurred or that was prevented from occurring.", 0, 1, event);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Group|Practitioner|RelatedPerson)", "This subject or group impacted by the event.", 0, 1, subject);
        case 1524132147: /*encounter*/  return new Property("encounter", "Reference(Encounter)", "The Encounter during which AdverseEvent was created or to which the creation of this record is tightly associated.", 0, 1, encounter);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date (and perhaps time) when the adverse event occurred.", 0, 1, date);
        case 1048254082: /*detected*/  return new Property("detected", "dateTime", "Estimated or actual date the AdverseEvent began, in the opinion of the reporter.", 0, 1, detected);
        case -1952893826: /*recordedDate*/  return new Property("recordedDate", "dateTime", "The date on which the existence of the AdverseEvent was first recorded.", 0, 1, recordedDate);
        case -830261258: /*resultingCondition*/  return new Property("resultingCondition", "Reference(Condition)", "Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).", 0, java.lang.Integer.MAX_VALUE, resultingCondition);
        case 1901043637: /*location*/  return new Property("location", "Reference(Location)", "The information about where the adverse event occurred.", 0, 1, location);
        case -1551003909: /*seriousness*/  return new Property("seriousness", "CodeableConcept", "Assessment whether this event was of real importance.", 0, 1, seriousness);
        case 1478300413: /*severity*/  return new Property("severity", "CodeableConcept", "Describes the severity of the adverse event, in relation to the subject. Contrast to AdverseEvent.seriousness - a severe rash might not be serious, but a mild heart problem is.", 0, 1, severity);
        case -1106507950: /*outcome*/  return new Property("outcome", "CodeableConcept", "Describes the type of outcome from the adverse event.", 0, 1, outcome);
        case -799233858: /*recorder*/  return new Property("recorder", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson)", "Information on who recorded the adverse event.  May be the patient or a practitioner.", 0, 1, recorder);
        case -1895276325: /*contributor*/  return new Property("contributor", "Reference(Practitioner|PractitionerRole|Device)", "Parties that may or should contribute or have contributed information to the adverse event, which can consist of one or more activities.  Such information includes information leading to the decision to perform the activity and how to perform the activity (e.g. consultant), information that the activity itself seeks to reveal (e.g. informant of clinical history), or information about what activity was performed (e.g. informant witness).", 0, java.lang.Integer.MAX_VALUE, contributor);
        case -1957422662: /*suspectEntity*/  return new Property("suspectEntity", "", "Describes the entity that is suspected to have caused the adverse event.", 0, java.lang.Integer.MAX_VALUE, suspectEntity);
        case -1685245681: /*subjectMedicalHistory*/  return new Property("subjectMedicalHistory", "Reference(Condition|Observation|AllergyIntolerance|FamilyMemberHistory|Immunization|Procedure|Media|DocumentReference)", "AdverseEvent.subjectMedicalHistory.", 0, java.lang.Integer.MAX_VALUE, subjectMedicalHistory);
        case 1013971334: /*referenceDocument*/  return new Property("referenceDocument", "Reference(DocumentReference)", "AdverseEvent.referenceDocument.", 0, java.lang.Integer.MAX_VALUE, referenceDocument);
        case 109776329: /*study*/  return new Property("study", "Reference(ResearchStudy)", "AdverseEvent.study.", 0, java.lang.Integer.MAX_VALUE, study);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 528866400: /*actuality*/ return this.actuality == null ? new Base[0] : new Base[] {this.actuality}; // Enumeration<AdverseEventActuality>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 96891546: /*event*/ return this.event == null ? new Base[0] : new Base[] {this.event}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1048254082: /*detected*/ return this.detected == null ? new Base[0] : new Base[] {this.detected}; // DateTimeType
        case -1952893826: /*recordedDate*/ return this.recordedDate == null ? new Base[0] : new Base[] {this.recordedDate}; // DateTimeType
        case -830261258: /*resultingCondition*/ return this.resultingCondition == null ? new Base[0] : this.resultingCondition.toArray(new Base[this.resultingCondition.size()]); // Reference
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case -1551003909: /*seriousness*/ return this.seriousness == null ? new Base[0] : new Base[] {this.seriousness}; // CodeableConcept
        case 1478300413: /*severity*/ return this.severity == null ? new Base[0] : new Base[] {this.severity}; // CodeableConcept
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case -799233858: /*recorder*/ return this.recorder == null ? new Base[0] : new Base[] {this.recorder}; // Reference
        case -1895276325: /*contributor*/ return this.contributor == null ? new Base[0] : this.contributor.toArray(new Base[this.contributor.size()]); // Reference
        case -1957422662: /*suspectEntity*/ return this.suspectEntity == null ? new Base[0] : this.suspectEntity.toArray(new Base[this.suspectEntity.size()]); // AdverseEventSuspectEntityComponent
        case -1685245681: /*subjectMedicalHistory*/ return this.subjectMedicalHistory == null ? new Base[0] : this.subjectMedicalHistory.toArray(new Base[this.subjectMedicalHistory.size()]); // Reference
        case 1013971334: /*referenceDocument*/ return this.referenceDocument == null ? new Base[0] : this.referenceDocument.toArray(new Base[this.referenceDocument.size()]); // Reference
        case 109776329: /*study*/ return this.study == null ? new Base[0] : this.study.toArray(new Base[this.study.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 528866400: // actuality
          value = new AdverseEventActualityEnumFactory().fromType(castToCode(value));
          this.actuality = (Enumeration) value; // Enumeration<AdverseEventActuality>
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 96891546: // event
          this.event = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1048254082: // detected
          this.detected = castToDateTime(value); // DateTimeType
          return value;
        case -1952893826: // recordedDate
          this.recordedDate = castToDateTime(value); // DateTimeType
          return value;
        case -830261258: // resultingCondition
          this.getResultingCondition().add(castToReference(value)); // Reference
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case -1551003909: // seriousness
          this.seriousness = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1478300413: // severity
          this.severity = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -799233858: // recorder
          this.recorder = castToReference(value); // Reference
          return value;
        case -1895276325: // contributor
          this.getContributor().add(castToReference(value)); // Reference
          return value;
        case -1957422662: // suspectEntity
          this.getSuspectEntity().add((AdverseEventSuspectEntityComponent) value); // AdverseEventSuspectEntityComponent
          return value;
        case -1685245681: // subjectMedicalHistory
          this.getSubjectMedicalHistory().add(castToReference(value)); // Reference
          return value;
        case 1013971334: // referenceDocument
          this.getReferenceDocument().add(castToReference(value)); // Reference
          return value;
        case 109776329: // study
          this.getStudy().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("actuality")) {
          value = new AdverseEventActualityEnumFactory().fromType(castToCode(value));
          this.actuality = (Enumeration) value; // Enumeration<AdverseEventActuality>
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("event")) {
          this.event = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("encounter")) {
          this.encounter = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("detected")) {
          this.detected = castToDateTime(value); // DateTimeType
        } else if (name.equals("recordedDate")) {
          this.recordedDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("resultingCondition")) {
          this.getResultingCondition().add(castToReference(value));
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("seriousness")) {
          this.seriousness = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("severity")) {
          this.severity = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("recorder")) {
          this.recorder = castToReference(value); // Reference
        } else if (name.equals("contributor")) {
          this.getContributor().add(castToReference(value));
        } else if (name.equals("suspectEntity")) {
          this.getSuspectEntity().add((AdverseEventSuspectEntityComponent) value);
        } else if (name.equals("subjectMedicalHistory")) {
          this.getSubjectMedicalHistory().add(castToReference(value));
        } else if (name.equals("referenceDocument")) {
          this.getReferenceDocument().add(castToReference(value));
        } else if (name.equals("study")) {
          this.getStudy().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 528866400:  return getActualityElement();
        case 50511102:  return addCategory(); 
        case 96891546:  return getEvent(); 
        case -1867885268:  return getSubject(); 
        case 1524132147:  return getEncounter(); 
        case 3076014:  return getDateElement();
        case 1048254082:  return getDetectedElement();
        case -1952893826:  return getRecordedDateElement();
        case -830261258:  return addResultingCondition(); 
        case 1901043637:  return getLocation(); 
        case -1551003909:  return getSeriousness(); 
        case 1478300413:  return getSeverity(); 
        case -1106507950:  return getOutcome(); 
        case -799233858:  return getRecorder(); 
        case -1895276325:  return addContributor(); 
        case -1957422662:  return addSuspectEntity(); 
        case -1685245681:  return addSubjectMedicalHistory(); 
        case 1013971334:  return addReferenceDocument(); 
        case 109776329:  return addStudy(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 528866400: /*actuality*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 96891546: /*event*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1524132147: /*encounter*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1048254082: /*detected*/ return new String[] {"dateTime"};
        case -1952893826: /*recordedDate*/ return new String[] {"dateTime"};
        case -830261258: /*resultingCondition*/ return new String[] {"Reference"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case -1551003909: /*seriousness*/ return new String[] {"CodeableConcept"};
        case 1478300413: /*severity*/ return new String[] {"CodeableConcept"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case -799233858: /*recorder*/ return new String[] {"Reference"};
        case -1895276325: /*contributor*/ return new String[] {"Reference"};
        case -1957422662: /*suspectEntity*/ return new String[] {};
        case -1685245681: /*subjectMedicalHistory*/ return new String[] {"Reference"};
        case 1013971334: /*referenceDocument*/ return new String[] {"Reference"};
        case 109776329: /*study*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("actuality")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.actuality");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("event")) {
          this.event = new CodeableConcept();
          return this.event;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.date");
        }
        else if (name.equals("detected")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.detected");
        }
        else if (name.equals("recordedDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.recordedDate");
        }
        else if (name.equals("resultingCondition")) {
          return addResultingCondition();
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("seriousness")) {
          this.seriousness = new CodeableConcept();
          return this.seriousness;
        }
        else if (name.equals("severity")) {
          this.severity = new CodeableConcept();
          return this.severity;
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("recorder")) {
          this.recorder = new Reference();
          return this.recorder;
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("suspectEntity")) {
          return addSuspectEntity();
        }
        else if (name.equals("subjectMedicalHistory")) {
          return addSubjectMedicalHistory();
        }
        else if (name.equals("referenceDocument")) {
          return addReferenceDocument();
        }
        else if (name.equals("study")) {
          return addStudy();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "AdverseEvent";

  }

      public AdverseEvent copy() {
        AdverseEvent dst = new AdverseEvent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.actuality = actuality == null ? null : actuality.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.event = event == null ? null : event.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.date = date == null ? null : date.copy();
        dst.detected = detected == null ? null : detected.copy();
        dst.recordedDate = recordedDate == null ? null : recordedDate.copy();
        if (resultingCondition != null) {
          dst.resultingCondition = new ArrayList<Reference>();
          for (Reference i : resultingCondition)
            dst.resultingCondition.add(i.copy());
        };
        dst.location = location == null ? null : location.copy();
        dst.seriousness = seriousness == null ? null : seriousness.copy();
        dst.severity = severity == null ? null : severity.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.recorder = recorder == null ? null : recorder.copy();
        if (contributor != null) {
          dst.contributor = new ArrayList<Reference>();
          for (Reference i : contributor)
            dst.contributor.add(i.copy());
        };
        if (suspectEntity != null) {
          dst.suspectEntity = new ArrayList<AdverseEventSuspectEntityComponent>();
          for (AdverseEventSuspectEntityComponent i : suspectEntity)
            dst.suspectEntity.add(i.copy());
        };
        if (subjectMedicalHistory != null) {
          dst.subjectMedicalHistory = new ArrayList<Reference>();
          for (Reference i : subjectMedicalHistory)
            dst.subjectMedicalHistory.add(i.copy());
        };
        if (referenceDocument != null) {
          dst.referenceDocument = new ArrayList<Reference>();
          for (Reference i : referenceDocument)
            dst.referenceDocument.add(i.copy());
        };
        if (study != null) {
          dst.study = new ArrayList<Reference>();
          for (Reference i : study)
            dst.study.add(i.copy());
        };
        return dst;
      }

      protected AdverseEvent typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AdverseEvent))
          return false;
        AdverseEvent o = (AdverseEvent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(actuality, o.actuality, true)
           && compareDeep(category, o.category, true) && compareDeep(event, o.event, true) && compareDeep(subject, o.subject, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(date, o.date, true) && compareDeep(detected, o.detected, true)
           && compareDeep(recordedDate, o.recordedDate, true) && compareDeep(resultingCondition, o.resultingCondition, true)
           && compareDeep(location, o.location, true) && compareDeep(seriousness, o.seriousness, true) && compareDeep(severity, o.severity, true)
           && compareDeep(outcome, o.outcome, true) && compareDeep(recorder, o.recorder, true) && compareDeep(contributor, o.contributor, true)
           && compareDeep(suspectEntity, o.suspectEntity, true) && compareDeep(subjectMedicalHistory, o.subjectMedicalHistory, true)
           && compareDeep(referenceDocument, o.referenceDocument, true) && compareDeep(study, o.study, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AdverseEvent))
          return false;
        AdverseEvent o = (AdverseEvent) other_;
        return compareValues(actuality, o.actuality, true) && compareValues(date, o.date, true) && compareValues(detected, o.detected, true)
           && compareValues(recordedDate, o.recordedDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, actuality, category
          , event, subject, encounter, date, detected, recordedDate, resultingCondition
          , location, seriousness, severity, outcome, recorder, contributor, suspectEntity
          , subjectMedicalHistory, referenceDocument, study);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.AdverseEvent;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the event occurred</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AdverseEvent.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="AdverseEvent.date", description="When the event occurred", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the event occurred</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AdverseEvent.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>severity</b>
   * <p>
   * Description: <b>mild | moderate | severe</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.severity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="severity", path="AdverseEvent.severity", description="mild | moderate | severe", type="token" )
  public static final String SP_SEVERITY = "severity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>severity</b>
   * <p>
   * Description: <b>mild | moderate | severe</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.severity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SEVERITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SEVERITY);

 /**
   * Search parameter: <b>recorder</b>
   * <p>
   * Description: <b>Who recorded the adverse event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.recorder</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recorder", path="AdverseEvent.recorder", description="Who recorded the adverse event", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_RECORDER = "recorder";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recorder</b>
   * <p>
   * Description: <b>Who recorded the adverse event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.recorder</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECORDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECORDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AdverseEvent:recorder</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECORDER = new ca.uhn.fhir.model.api.Include("AdverseEvent:recorder").toLocked();

 /**
   * Search parameter: <b>study</b>
   * <p>
   * Description: <b>AdverseEvent.study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.study</b><br>
   * </p>
   */
  @SearchParamDefinition(name="study", path="AdverseEvent.study", description="AdverseEvent.study", type="reference", target={ResearchStudy.class } )
  public static final String SP_STUDY = "study";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>study</b>
   * <p>
   * Description: <b>AdverseEvent.study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.study</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam STUDY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_STUDY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AdverseEvent:study</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_STUDY = new ca.uhn.fhir.model.api.Include("AdverseEvent:study").toLocked();

 /**
   * Search parameter: <b>actuality</b>
   * <p>
   * Description: <b>actual | potential</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.actuality</b><br>
   * </p>
   */
  @SearchParamDefinition(name="actuality", path="AdverseEvent.actuality", description="actual | potential", type="token" )
  public static final String SP_ACTUALITY = "actuality";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>actuality</b>
   * <p>
   * Description: <b>actual | potential</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.actuality</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTUALITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTUALITY);

 /**
   * Search parameter: <b>seriousness</b>
   * <p>
   * Description: <b>Seriousness of the event</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.seriousness</b><br>
   * </p>
   */
  @SearchParamDefinition(name="seriousness", path="AdverseEvent.seriousness", description="Seriousness of the event", type="token" )
  public static final String SP_SERIOUSNESS = "seriousness";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>seriousness</b>
   * <p>
   * Description: <b>Seriousness of the event</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.seriousness</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SERIOUSNESS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SERIOUSNESS);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Subject impacted by event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="AdverseEvent.subject", description="Subject impacted by event", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Subject impacted by event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AdverseEvent:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("AdverseEvent:subject").toLocked();

 /**
   * Search parameter: <b>resultingcondition</b>
   * <p>
   * Description: <b>Effect on the subject due to this event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.resultingCondition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="resultingcondition", path="AdverseEvent.resultingCondition", description="Effect on the subject due to this event", type="reference", target={Condition.class } )
  public static final String SP_RESULTINGCONDITION = "resultingcondition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>resultingcondition</b>
   * <p>
   * Description: <b>Effect on the subject due to this event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.resultingCondition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RESULTINGCONDITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RESULTINGCONDITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AdverseEvent:resultingcondition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RESULTINGCONDITION = new ca.uhn.fhir.model.api.Include("AdverseEvent:resultingcondition").toLocked();

 /**
   * Search parameter: <b>substance</b>
   * <p>
   * Description: <b>Refers to the specific entity that caused the adverse event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.suspectEntity.instance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="substance", path="AdverseEvent.suspectEntity.instance", description="Refers to the specific entity that caused the adverse event", type="reference", target={Device.class, Immunization.class, Medication.class, MedicationAdministration.class, MedicationStatement.class, Procedure.class, Substance.class } )
  public static final String SP_SUBSTANCE = "substance";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>substance</b>
   * <p>
   * Description: <b>Refers to the specific entity that caused the adverse event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.suspectEntity.instance</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBSTANCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBSTANCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AdverseEvent:substance</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBSTANCE = new ca.uhn.fhir.model.api.Include("AdverseEvent:substance").toLocked();

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>Location where adverse event occurred</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="AdverseEvent.location", description="Location where adverse event occurred", type="reference", target={Location.class } )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>Location where adverse event occurred</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AdverseEvent:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("AdverseEvent:location").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>product-problem | product-quality | product-use-error | wrong-dose | incorrect-prescribing-information | wrong-technique | wrong-route-of-administration | wrong-rate | wrong-duration | wrong-time | expired-drug | medical-device-use-error | problem-different-manufacturer | unsafe-physical-environment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="AdverseEvent.category", description="product-problem | product-quality | product-use-error | wrong-dose | incorrect-prescribing-information | wrong-technique | wrong-route-of-administration | wrong-rate | wrong-duration | wrong-time | expired-drug | medical-device-use-error | problem-different-manufacturer | unsafe-physical-environment", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>product-problem | product-quality | product-use-error | wrong-dose | incorrect-prescribing-information | wrong-technique | wrong-route-of-administration | wrong-rate | wrong-duration | wrong-time | expired-drug | medical-device-use-error | problem-different-manufacturer | unsafe-physical-environment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>event</b>
   * <p>
   * Description: <b>Type of the event itself in relation to the subject</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.event</b><br>
   * </p>
   */
  @SearchParamDefinition(name="event", path="AdverseEvent.event", description="Type of the event itself in relation to the subject", type="token" )
  public static final String SP_EVENT = "event";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>event</b>
   * <p>
   * Description: <b>Type of the event itself in relation to the subject</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.event</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EVENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EVENT);


}

