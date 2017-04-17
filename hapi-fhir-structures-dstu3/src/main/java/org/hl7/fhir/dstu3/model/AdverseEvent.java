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
 * Actual or  potential/avoided event causing unintended physical injury resulting from or contributed to by medical care, a research study or other healthcare setting factors that requires additional monitoring, treatment, or hospitalization, or that results in death.
 */
@ResourceDef(name="AdverseEvent", profile="http://hl7.org/fhir/Profile/AdverseEvent")
public class AdverseEvent extends DomainResource {

    public enum AdverseEventCategory {
        /**
         * null
         */
        AE, 
        /**
         * null
         */
        PAE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AdverseEventCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AE".equals(codeString))
          return AE;
        if ("PAE".equals(codeString))
          return PAE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AdverseEventCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AE: return "AE";
            case PAE: return "PAE";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AE: return "http://hl7.org/fhir/adverse-event-category";
            case PAE: return "http://hl7.org/fhir/adverse-event-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AE: return "";
            case PAE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AE: return "Adverse Event";
            case PAE: return "Potential Adverse Event";
            default: return "?";
          }
        }
    }

  public static class AdverseEventCategoryEnumFactory implements EnumFactory<AdverseEventCategory> {
    public AdverseEventCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AE".equals(codeString))
          return AdverseEventCategory.AE;
        if ("PAE".equals(codeString))
          return AdverseEventCategory.PAE;
        throw new IllegalArgumentException("Unknown AdverseEventCategory code '"+codeString+"'");
        }
        public Enumeration<AdverseEventCategory> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AdverseEventCategory>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("AE".equals(codeString))
          return new Enumeration<AdverseEventCategory>(this, AdverseEventCategory.AE);
        if ("PAE".equals(codeString))
          return new Enumeration<AdverseEventCategory>(this, AdverseEventCategory.PAE);
        throw new FHIRException("Unknown AdverseEventCategory code '"+codeString+"'");
        }
    public String toCode(AdverseEventCategory code) {
      if (code == AdverseEventCategory.AE)
        return "AE";
      if (code == AdverseEventCategory.PAE)
        return "PAE";
      return "?";
      }
    public String toSystem(AdverseEventCategory code) {
      return code.getSystem();
      }
    }

    public enum AdverseEventCausality {
        /**
         * null
         */
        CAUSALITY1, 
        /**
         * null
         */
        CAUSALITY2, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AdverseEventCausality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("causality1".equals(codeString))
          return CAUSALITY1;
        if ("causality2".equals(codeString))
          return CAUSALITY2;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AdverseEventCausality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CAUSALITY1: return "causality1";
            case CAUSALITY2: return "causality2";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CAUSALITY1: return "http://hl7.org/fhir/adverse-event-causality";
            case CAUSALITY2: return "http://hl7.org/fhir/adverse-event-causality";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CAUSALITY1: return "";
            case CAUSALITY2: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CAUSALITY1: return "causality1 placeholder";
            case CAUSALITY2: return "causality2 placeholder";
            default: return "?";
          }
        }
    }

  public static class AdverseEventCausalityEnumFactory implements EnumFactory<AdverseEventCausality> {
    public AdverseEventCausality fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("causality1".equals(codeString))
          return AdverseEventCausality.CAUSALITY1;
        if ("causality2".equals(codeString))
          return AdverseEventCausality.CAUSALITY2;
        throw new IllegalArgumentException("Unknown AdverseEventCausality code '"+codeString+"'");
        }
        public Enumeration<AdverseEventCausality> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AdverseEventCausality>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("causality1".equals(codeString))
          return new Enumeration<AdverseEventCausality>(this, AdverseEventCausality.CAUSALITY1);
        if ("causality2".equals(codeString))
          return new Enumeration<AdverseEventCausality>(this, AdverseEventCausality.CAUSALITY2);
        throw new FHIRException("Unknown AdverseEventCausality code '"+codeString+"'");
        }
    public String toCode(AdverseEventCausality code) {
      if (code == AdverseEventCausality.CAUSALITY1)
        return "causality1";
      if (code == AdverseEventCausality.CAUSALITY2)
        return "causality2";
      return "?";
      }
    public String toSystem(AdverseEventCausality code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class AdverseEventSuspectEntityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.
         */
        @Child(name = "instance", type = {Substance.class, Medication.class, MedicationAdministration.class, MedicationStatement.class, Device.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Refers to the specific entity that caused the adverse event", formalDefinition="Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device." )
        protected Reference instance;

        /**
         * The actual object that is the target of the reference (Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.)
         */
        protected Resource instanceTarget;

        /**
         * causality1 | causality2.
         */
        @Child(name = "causality", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="causality1 | causality2", formalDefinition="causality1 | causality2." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-causality")
        protected Enumeration<AdverseEventCausality> causality;

        /**
         * assess1 | assess2.
         */
        @Child(name = "causalityAssessment", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="assess1 | assess2", formalDefinition="assess1 | assess2." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-causality-assess")
        protected CodeableConcept causalityAssessment;

        /**
         * AdverseEvent.suspectEntity.causalityProductRelatedness.
         */
        @Child(name = "causalityProductRelatedness", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="AdverseEvent.suspectEntity.causalityProductRelatedness", formalDefinition="AdverseEvent.suspectEntity.causalityProductRelatedness." )
        protected StringType causalityProductRelatedness;

        /**
         * method1 | method2.
         */
        @Child(name = "causalityMethod", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="method1 | method2", formalDefinition="method1 | method2." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-causality-method")
        protected CodeableConcept causalityMethod;

        /**
         * AdverseEvent.suspectEntity.causalityAuthor.
         */
        @Child(name = "causalityAuthor", type = {Practitioner.class, PractitionerRole.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="AdverseEvent.suspectEntity.causalityAuthor", formalDefinition="AdverseEvent.suspectEntity.causalityAuthor." )
        protected Reference causalityAuthor;

        /**
         * The actual object that is the target of the reference (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        protected Resource causalityAuthorTarget;

        /**
         * result1 | result2.
         */
        @Child(name = "causalityResult", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="result1 | result2", formalDefinition="result1 | result2." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-causality-result")
        protected CodeableConcept causalityResult;

        private static final long serialVersionUID = -815429592L;

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
         * @return {@link #causality} (causality1 | causality2.). This is the underlying object with id, value and extensions. The accessor "getCausality" gives direct access to the value
         */
        public Enumeration<AdverseEventCausality> getCausalityElement() { 
          if (this.causality == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityComponent.causality");
            else if (Configuration.doAutoCreate())
              this.causality = new Enumeration<AdverseEventCausality>(new AdverseEventCausalityEnumFactory()); // bb
          return this.causality;
        }

        public boolean hasCausalityElement() { 
          return this.causality != null && !this.causality.isEmpty();
        }

        public boolean hasCausality() { 
          return this.causality != null && !this.causality.isEmpty();
        }

        /**
         * @param value {@link #causality} (causality1 | causality2.). This is the underlying object with id, value and extensions. The accessor "getCausality" gives direct access to the value
         */
        public AdverseEventSuspectEntityComponent setCausalityElement(Enumeration<AdverseEventCausality> value) { 
          this.causality = value;
          return this;
        }

        /**
         * @return causality1 | causality2.
         */
        public AdverseEventCausality getCausality() { 
          return this.causality == null ? null : this.causality.getValue();
        }

        /**
         * @param value causality1 | causality2.
         */
        public AdverseEventSuspectEntityComponent setCausality(AdverseEventCausality value) { 
          if (value == null)
            this.causality = null;
          else {
            if (this.causality == null)
              this.causality = new Enumeration<AdverseEventCausality>(new AdverseEventCausalityEnumFactory());
            this.causality.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #causalityAssessment} (assess1 | assess2.)
         */
        public CodeableConcept getCausalityAssessment() { 
          if (this.causalityAssessment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityComponent.causalityAssessment");
            else if (Configuration.doAutoCreate())
              this.causalityAssessment = new CodeableConcept(); // cc
          return this.causalityAssessment;
        }

        public boolean hasCausalityAssessment() { 
          return this.causalityAssessment != null && !this.causalityAssessment.isEmpty();
        }

        /**
         * @param value {@link #causalityAssessment} (assess1 | assess2.)
         */
        public AdverseEventSuspectEntityComponent setCausalityAssessment(CodeableConcept value) { 
          this.causalityAssessment = value;
          return this;
        }

        /**
         * @return {@link #causalityProductRelatedness} (AdverseEvent.suspectEntity.causalityProductRelatedness.). This is the underlying object with id, value and extensions. The accessor "getCausalityProductRelatedness" gives direct access to the value
         */
        public StringType getCausalityProductRelatednessElement() { 
          if (this.causalityProductRelatedness == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityComponent.causalityProductRelatedness");
            else if (Configuration.doAutoCreate())
              this.causalityProductRelatedness = new StringType(); // bb
          return this.causalityProductRelatedness;
        }

        public boolean hasCausalityProductRelatednessElement() { 
          return this.causalityProductRelatedness != null && !this.causalityProductRelatedness.isEmpty();
        }

        public boolean hasCausalityProductRelatedness() { 
          return this.causalityProductRelatedness != null && !this.causalityProductRelatedness.isEmpty();
        }

        /**
         * @param value {@link #causalityProductRelatedness} (AdverseEvent.suspectEntity.causalityProductRelatedness.). This is the underlying object with id, value and extensions. The accessor "getCausalityProductRelatedness" gives direct access to the value
         */
        public AdverseEventSuspectEntityComponent setCausalityProductRelatednessElement(StringType value) { 
          this.causalityProductRelatedness = value;
          return this;
        }

        /**
         * @return AdverseEvent.suspectEntity.causalityProductRelatedness.
         */
        public String getCausalityProductRelatedness() { 
          return this.causalityProductRelatedness == null ? null : this.causalityProductRelatedness.getValue();
        }

        /**
         * @param value AdverseEvent.suspectEntity.causalityProductRelatedness.
         */
        public AdverseEventSuspectEntityComponent setCausalityProductRelatedness(String value) { 
          if (Utilities.noString(value))
            this.causalityProductRelatedness = null;
          else {
            if (this.causalityProductRelatedness == null)
              this.causalityProductRelatedness = new StringType();
            this.causalityProductRelatedness.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #causalityMethod} (method1 | method2.)
         */
        public CodeableConcept getCausalityMethod() { 
          if (this.causalityMethod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityComponent.causalityMethod");
            else if (Configuration.doAutoCreate())
              this.causalityMethod = new CodeableConcept(); // cc
          return this.causalityMethod;
        }

        public boolean hasCausalityMethod() { 
          return this.causalityMethod != null && !this.causalityMethod.isEmpty();
        }

        /**
         * @param value {@link #causalityMethod} (method1 | method2.)
         */
        public AdverseEventSuspectEntityComponent setCausalityMethod(CodeableConcept value) { 
          this.causalityMethod = value;
          return this;
        }

        /**
         * @return {@link #causalityAuthor} (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public Reference getCausalityAuthor() { 
          if (this.causalityAuthor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityComponent.causalityAuthor");
            else if (Configuration.doAutoCreate())
              this.causalityAuthor = new Reference(); // cc
          return this.causalityAuthor;
        }

        public boolean hasCausalityAuthor() { 
          return this.causalityAuthor != null && !this.causalityAuthor.isEmpty();
        }

        /**
         * @param value {@link #causalityAuthor} (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public AdverseEventSuspectEntityComponent setCausalityAuthor(Reference value) { 
          this.causalityAuthor = value;
          return this;
        }

        /**
         * @return {@link #causalityAuthor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public Resource getCausalityAuthorTarget() { 
          return this.causalityAuthorTarget;
        }

        /**
         * @param value {@link #causalityAuthor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (AdverseEvent.suspectEntity.causalityAuthor.)
         */
        public AdverseEventSuspectEntityComponent setCausalityAuthorTarget(Resource value) { 
          this.causalityAuthorTarget = value;
          return this;
        }

        /**
         * @return {@link #causalityResult} (result1 | result2.)
         */
        public CodeableConcept getCausalityResult() { 
          if (this.causalityResult == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AdverseEventSuspectEntityComponent.causalityResult");
            else if (Configuration.doAutoCreate())
              this.causalityResult = new CodeableConcept(); // cc
          return this.causalityResult;
        }

        public boolean hasCausalityResult() { 
          return this.causalityResult != null && !this.causalityResult.isEmpty();
        }

        /**
         * @param value {@link #causalityResult} (result1 | result2.)
         */
        public AdverseEventSuspectEntityComponent setCausalityResult(CodeableConcept value) { 
          this.causalityResult = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("instance", "Reference(Substance|Medication|MedicationAdministration|MedicationStatement|Device)", "Identifies the actual instance of what caused the adverse event.  May be a substance, medication, medication administration, medication statement or a device.", 0, java.lang.Integer.MAX_VALUE, instance));
          childrenList.add(new Property("causality", "code", "causality1 | causality2.", 0, java.lang.Integer.MAX_VALUE, causality));
          childrenList.add(new Property("causalityAssessment", "CodeableConcept", "assess1 | assess2.", 0, java.lang.Integer.MAX_VALUE, causalityAssessment));
          childrenList.add(new Property("causalityProductRelatedness", "string", "AdverseEvent.suspectEntity.causalityProductRelatedness.", 0, java.lang.Integer.MAX_VALUE, causalityProductRelatedness));
          childrenList.add(new Property("causalityMethod", "CodeableConcept", "method1 | method2.", 0, java.lang.Integer.MAX_VALUE, causalityMethod));
          childrenList.add(new Property("causalityAuthor", "Reference(Practitioner|PractitionerRole)", "AdverseEvent.suspectEntity.causalityAuthor.", 0, java.lang.Integer.MAX_VALUE, causalityAuthor));
          childrenList.add(new Property("causalityResult", "CodeableConcept", "result1 | result2.", 0, java.lang.Integer.MAX_VALUE, causalityResult));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 555127957: /*instance*/ return this.instance == null ? new Base[0] : new Base[] {this.instance}; // Reference
        case -1446450521: /*causality*/ return this.causality == null ? new Base[0] : new Base[] {this.causality}; // Enumeration<AdverseEventCausality>
        case 830609609: /*causalityAssessment*/ return this.causalityAssessment == null ? new Base[0] : new Base[] {this.causalityAssessment}; // CodeableConcept
        case -1983069286: /*causalityProductRelatedness*/ return this.causalityProductRelatedness == null ? new Base[0] : new Base[] {this.causalityProductRelatedness}; // StringType
        case -1320366488: /*causalityMethod*/ return this.causalityMethod == null ? new Base[0] : new Base[] {this.causalityMethod}; // CodeableConcept
        case -1649139950: /*causalityAuthor*/ return this.causalityAuthor == null ? new Base[0] : new Base[] {this.causalityAuthor}; // Reference
        case -1177238108: /*causalityResult*/ return this.causalityResult == null ? new Base[0] : new Base[] {this.causalityResult}; // CodeableConcept
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
          value = new AdverseEventCausalityEnumFactory().fromType(castToCode(value));
          this.causality = (Enumeration) value; // Enumeration<AdverseEventCausality>
          return value;
        case 830609609: // causalityAssessment
          this.causalityAssessment = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1983069286: // causalityProductRelatedness
          this.causalityProductRelatedness = castToString(value); // StringType
          return value;
        case -1320366488: // causalityMethod
          this.causalityMethod = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1649139950: // causalityAuthor
          this.causalityAuthor = castToReference(value); // Reference
          return value;
        case -1177238108: // causalityResult
          this.causalityResult = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("instance")) {
          this.instance = castToReference(value); // Reference
        } else if (name.equals("causality")) {
          value = new AdverseEventCausalityEnumFactory().fromType(castToCode(value));
          this.causality = (Enumeration) value; // Enumeration<AdverseEventCausality>
        } else if (name.equals("causalityAssessment")) {
          this.causalityAssessment = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("causalityProductRelatedness")) {
          this.causalityProductRelatedness = castToString(value); // StringType
        } else if (name.equals("causalityMethod")) {
          this.causalityMethod = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("causalityAuthor")) {
          this.causalityAuthor = castToReference(value); // Reference
        } else if (name.equals("causalityResult")) {
          this.causalityResult = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 555127957:  return getInstance(); 
        case -1446450521:  return getCausalityElement();
        case 830609609:  return getCausalityAssessment(); 
        case -1983069286:  return getCausalityProductRelatednessElement();
        case -1320366488:  return getCausalityMethod(); 
        case -1649139950:  return getCausalityAuthor(); 
        case -1177238108:  return getCausalityResult(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 555127957: /*instance*/ return new String[] {"Reference"};
        case -1446450521: /*causality*/ return new String[] {"code"};
        case 830609609: /*causalityAssessment*/ return new String[] {"CodeableConcept"};
        case -1983069286: /*causalityProductRelatedness*/ return new String[] {"string"};
        case -1320366488: /*causalityMethod*/ return new String[] {"CodeableConcept"};
        case -1649139950: /*causalityAuthor*/ return new String[] {"Reference"};
        case -1177238108: /*causalityResult*/ return new String[] {"CodeableConcept"};
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
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.causality");
        }
        else if (name.equals("causalityAssessment")) {
          this.causalityAssessment = new CodeableConcept();
          return this.causalityAssessment;
        }
        else if (name.equals("causalityProductRelatedness")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.causalityProductRelatedness");
        }
        else if (name.equals("causalityMethod")) {
          this.causalityMethod = new CodeableConcept();
          return this.causalityMethod;
        }
        else if (name.equals("causalityAuthor")) {
          this.causalityAuthor = new Reference();
          return this.causalityAuthor;
        }
        else if (name.equals("causalityResult")) {
          this.causalityResult = new CodeableConcept();
          return this.causalityResult;
        }
        else
          return super.addChild(name);
      }

      public AdverseEventSuspectEntityComponent copy() {
        AdverseEventSuspectEntityComponent dst = new AdverseEventSuspectEntityComponent();
        copyValues(dst);
        dst.instance = instance == null ? null : instance.copy();
        dst.causality = causality == null ? null : causality.copy();
        dst.causalityAssessment = causalityAssessment == null ? null : causalityAssessment.copy();
        dst.causalityProductRelatedness = causalityProductRelatedness == null ? null : causalityProductRelatedness.copy();
        dst.causalityMethod = causalityMethod == null ? null : causalityMethod.copy();
        dst.causalityAuthor = causalityAuthor == null ? null : causalityAuthor.copy();
        dst.causalityResult = causalityResult == null ? null : causalityResult.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AdverseEventSuspectEntityComponent))
          return false;
        AdverseEventSuspectEntityComponent o = (AdverseEventSuspectEntityComponent) other;
        return compareDeep(instance, o.instance, true) && compareDeep(causality, o.causality, true) && compareDeep(causalityAssessment, o.causalityAssessment, true)
           && compareDeep(causalityProductRelatedness, o.causalityProductRelatedness, true) && compareDeep(causalityMethod, o.causalityMethod, true)
           && compareDeep(causalityAuthor, o.causalityAuthor, true) && compareDeep(causalityResult, o.causalityResult, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AdverseEventSuspectEntityComponent))
          return false;
        AdverseEventSuspectEntityComponent o = (AdverseEventSuspectEntityComponent) other;
        return compareValues(causality, o.causality, true) && compareValues(causalityProductRelatedness, o.causalityProductRelatedness, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(instance, causality, causalityAssessment
          , causalityProductRelatedness, causalityMethod, causalityAuthor, causalityResult);
      }

  public String fhirType() {
    return "AdverseEvent.suspectEntity";

  }

  }

    /**
     * The identifier(s) of this adverse event that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itsefl is not appropriate.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier for the event", formalDefinition="The identifier(s) of this adverse event that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itsefl is not appropriate." )
    protected Identifier identifier;

    /**
     * The type of event which is important to characterize what occurred and caused harm to the subject, or had the potential to cause harm to the subject.
     */
    @Child(name = "category", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="AE | PAE \rAn adverse event is an event that caused harm to a patient,  an adverse reaction is a something that is a subject-specific event that is a result of an exposure to a medication, food, device or environmental substance, a potential adverse event is something that occurred and that could have caused harm to a patient but did not", formalDefinition="The type of event which is important to characterize what occurred and caused harm to the subject, or had the potential to cause harm to the subject." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-category")
    protected Enumeration<AdverseEventCategory> category;

    /**
     * This element defines the specific type of event that occurred or that was prevented from occurring.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="actual | potential", formalDefinition="This element defines the specific type of event that occurred or that was prevented from occurring." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-type")
    protected CodeableConcept type;

    /**
     * This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented.
     */
    @Child(name = "subject", type = {Patient.class, ResearchSubject.class, Medication.class, Device.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Subject or group impacted by event", formalDefinition="This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented.)
     */
    protected Resource subjectTarget;

    /**
     * The date (and perhaps time) when the adverse event occurred.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the event occurred", formalDefinition="The date (and perhaps time) when the adverse event occurred." )
    protected DateTimeType date;

    /**
     * Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).
     */
    @Child(name = "reaction", type = {Condition.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Adverse Reaction Events linked to exposure to substance", formalDefinition="Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical)." )
    protected List<Reference> reaction;
    /**
     * The actual objects that are the target of the reference (Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).)
     */
    protected List<Condition> reactionTarget;


    /**
     * The information about where the adverse event occurred.
     */
    @Child(name = "location", type = {Location.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Location where adverse event occurred", formalDefinition="The information about where the adverse event occurred." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The information about where the adverse event occurred.)
     */
    protected Location locationTarget;

    /**
     * Describes the seriousness or severity of the adverse event.
     */
    @Child(name = "seriousness", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Mild | Moderate | Severe", formalDefinition="Describes the seriousness or severity of the adverse event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-seriousness")
    protected CodeableConcept seriousness;

    /**
     * Describes the type of outcome from the adverse event.
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="resolved | recovering | ongoing | resolvedWithSequelae | fatal | unknown", formalDefinition="Describes the type of outcome from the adverse event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adverse-event-outcome")
    protected CodeableConcept outcome;

    /**
     * Information on who recorded the adverse event.  May be the patient or a practitioner.
     */
    @Child(name = "recorder", type = {Patient.class, Practitioner.class, RelatedPerson.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who recorded the adverse event", formalDefinition="Information on who recorded the adverse event.  May be the patient or a practitioner." )
    protected Reference recorder;

    /**
     * The actual object that is the target of the reference (Information on who recorded the adverse event.  May be the patient or a practitioner.)
     */
    protected Resource recorderTarget;

    /**
     * Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness).
     */
    @Child(name = "eventParticipant", type = {Practitioner.class, Device.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who  was involved in the adverse event or the potential adverse event", formalDefinition="Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness)." )
    protected Reference eventParticipant;

    /**
     * The actual object that is the target of the reference (Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness).)
     */
    protected Resource eventParticipantTarget;

    /**
     * Describes the adverse event in text.
     */
    @Child(name = "description", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Description of the adverse event", formalDefinition="Describes the adverse event in text." )
    protected StringType description;

    /**
     * Describes the entity that is suspected to have caused the adverse event.
     */
    @Child(name = "suspectEntity", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The suspected agent causing the adverse event", formalDefinition="Describes the entity that is suspected to have caused the adverse event." )
    protected List<AdverseEventSuspectEntityComponent> suspectEntity;

    /**
     * AdverseEvent.subjectMedicalHistory.
     */
    @Child(name = "subjectMedicalHistory", type = {Condition.class, Observation.class, AllergyIntolerance.class, FamilyMemberHistory.class, Immunization.class, Procedure.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="AdverseEvent.subjectMedicalHistory", formalDefinition="AdverseEvent.subjectMedicalHistory." )
    protected List<Reference> subjectMedicalHistory;
    /**
     * The actual objects that are the target of the reference (AdverseEvent.subjectMedicalHistory.)
     */
    protected List<Resource> subjectMedicalHistoryTarget;


    /**
     * AdverseEvent.referenceDocument.
     */
    @Child(name = "referenceDocument", type = {DocumentReference.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="AdverseEvent.referenceDocument", formalDefinition="AdverseEvent.referenceDocument." )
    protected List<Reference> referenceDocument;
    /**
     * The actual objects that are the target of the reference (AdverseEvent.referenceDocument.)
     */
    protected List<DocumentReference> referenceDocumentTarget;


    /**
     * AdverseEvent.study.
     */
    @Child(name = "study", type = {ResearchStudy.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="AdverseEvent.study", formalDefinition="AdverseEvent.study." )
    protected List<Reference> study;
    /**
     * The actual objects that are the target of the reference (AdverseEvent.study.)
     */
    protected List<ResearchStudy> studyTarget;


    private static final long serialVersionUID = 156251238L;

  /**
   * Constructor
   */
    public AdverseEvent() {
      super();
    }

    /**
     * @return {@link #identifier} (The identifier(s) of this adverse event that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itsefl is not appropriate.)
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
     * @param value {@link #identifier} (The identifier(s) of this adverse event that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itsefl is not appropriate.)
     */
    public AdverseEvent setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #category} (The type of event which is important to characterize what occurred and caused harm to the subject, or had the potential to cause harm to the subject.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<AdverseEventCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<AdverseEventCategory>(new AdverseEventCategoryEnumFactory()); // bb
      return this.category;
    }

    public boolean hasCategoryElement() { 
      return this.category != null && !this.category.isEmpty();
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (The type of event which is important to characterize what occurred and caused harm to the subject, or had the potential to cause harm to the subject.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public AdverseEvent setCategoryElement(Enumeration<AdverseEventCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return The type of event which is important to characterize what occurred and caused harm to the subject, or had the potential to cause harm to the subject.
     */
    public AdverseEventCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value The type of event which is important to characterize what occurred and caused harm to the subject, or had the potential to cause harm to the subject.
     */
    public AdverseEvent setCategory(AdverseEventCategory value) { 
      if (value == null)
        this.category = null;
      else {
        if (this.category == null)
          this.category = new Enumeration<AdverseEventCategory>(new AdverseEventCategoryEnumFactory());
        this.category.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (This element defines the specific type of event that occurred or that was prevented from occurring.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (This element defines the specific type of event that occurred or that was prevented from occurring.)
     */
    public AdverseEvent setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subject} (This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented.)
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
     * @param value {@link #subject} (This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented.)
     */
    public AdverseEvent setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented.)
     */
    public AdverseEvent setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
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
     * @return {@link #reaction} (Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).)
     */
    public List<Reference> getReaction() { 
      if (this.reaction == null)
        this.reaction = new ArrayList<Reference>();
      return this.reaction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AdverseEvent setReaction(List<Reference> theReaction) { 
      this.reaction = theReaction;
      return this;
    }

    public boolean hasReaction() { 
      if (this.reaction == null)
        return false;
      for (Reference item : this.reaction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReaction() { //3
      Reference t = new Reference();
      if (this.reaction == null)
        this.reaction = new ArrayList<Reference>();
      this.reaction.add(t);
      return t;
    }

    public AdverseEvent addReaction(Reference t) { //3
      if (t == null)
        return this;
      if (this.reaction == null)
        this.reaction = new ArrayList<Reference>();
      this.reaction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reaction}, creating it if it does not already exist
     */
    public Reference getReactionFirstRep() { 
      if (getReaction().isEmpty()) {
        addReaction();
      }
      return getReaction().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Condition> getReactionTarget() { 
      if (this.reactionTarget == null)
        this.reactionTarget = new ArrayList<Condition>();
      return this.reactionTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Condition addReactionTarget() { 
      Condition r = new Condition();
      if (this.reactionTarget == null)
        this.reactionTarget = new ArrayList<Condition>();
      this.reactionTarget.add(r);
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
     * @return {@link #seriousness} (Describes the seriousness or severity of the adverse event.)
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
     * @param value {@link #seriousness} (Describes the seriousness or severity of the adverse event.)
     */
    public AdverseEvent setSeriousness(CodeableConcept value) { 
      this.seriousness = value;
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
     * @return {@link #eventParticipant} (Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness).)
     */
    public Reference getEventParticipant() { 
      if (this.eventParticipant == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.eventParticipant");
        else if (Configuration.doAutoCreate())
          this.eventParticipant = new Reference(); // cc
      return this.eventParticipant;
    }

    public boolean hasEventParticipant() { 
      return this.eventParticipant != null && !this.eventParticipant.isEmpty();
    }

    /**
     * @param value {@link #eventParticipant} (Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness).)
     */
    public AdverseEvent setEventParticipant(Reference value) { 
      this.eventParticipant = value;
      return this;
    }

    /**
     * @return {@link #eventParticipant} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness).)
     */
    public Resource getEventParticipantTarget() { 
      return this.eventParticipantTarget;
    }

    /**
     * @param value {@link #eventParticipant} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness).)
     */
    public AdverseEvent setEventParticipantTarget(Resource value) { 
      this.eventParticipantTarget = value;
      return this;
    }

    /**
     * @return {@link #description} (Describes the adverse event in text.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AdverseEvent.description");
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
     * @param value {@link #description} (Describes the adverse event in text.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public AdverseEvent setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Describes the adverse event in text.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Describes the adverse event in text.
     */
    public AdverseEvent setDescription(String value) { 
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The identifier(s) of this adverse event that are assigned by business processes and/or used to refer to it when a direct URL reference to the resource itsefl is not appropriate.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "code", "The type of event which is important to characterize what occurred and caused harm to the subject, or had the potential to cause harm to the subject.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("type", "CodeableConcept", "This element defines the specific type of event that occurred or that was prevented from occurring.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subject", "Reference(Patient|ResearchSubject|Medication|Device)", "This subject or group impacted by the event.  With a prospective adverse event, there will be no subject as the adverse event was prevented.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("date", "dateTime", "The date (and perhaps time) when the adverse event occurred.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("reaction", "Reference(Condition)", "Includes information about the reaction that occurred as a result of exposure to a substance (for example, a drug or a chemical).", 0, java.lang.Integer.MAX_VALUE, reaction));
        childrenList.add(new Property("location", "Reference(Location)", "The information about where the adverse event occurred.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("seriousness", "CodeableConcept", "Describes the seriousness or severity of the adverse event.", 0, java.lang.Integer.MAX_VALUE, seriousness));
        childrenList.add(new Property("outcome", "CodeableConcept", "Describes the type of outcome from the adverse event.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("recorder", "Reference(Patient|Practitioner|RelatedPerson)", "Information on who recorded the adverse event.  May be the patient or a practitioner.", 0, java.lang.Integer.MAX_VALUE, recorder));
        childrenList.add(new Property("eventParticipant", "Reference(Practitioner|Device)", "Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g. consultant), information that the Act itself seeks to reveal (e.g. informant of clinical history), or information about what Act was performed (e.g. informant witness).", 0, java.lang.Integer.MAX_VALUE, eventParticipant));
        childrenList.add(new Property("description", "string", "Describes the adverse event in text.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("suspectEntity", "", "Describes the entity that is suspected to have caused the adverse event.", 0, java.lang.Integer.MAX_VALUE, suspectEntity));
        childrenList.add(new Property("subjectMedicalHistory", "Reference(Condition|Observation|AllergyIntolerance|FamilyMemberHistory|Immunization|Procedure)", "AdverseEvent.subjectMedicalHistory.", 0, java.lang.Integer.MAX_VALUE, subjectMedicalHistory));
        childrenList.add(new Property("referenceDocument", "Reference(DocumentReference)", "AdverseEvent.referenceDocument.", 0, java.lang.Integer.MAX_VALUE, referenceDocument));
        childrenList.add(new Property("study", "Reference(ResearchStudy)", "AdverseEvent.study.", 0, java.lang.Integer.MAX_VALUE, study));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<AdverseEventCategory>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -867509719: /*reaction*/ return this.reaction == null ? new Base[0] : this.reaction.toArray(new Base[this.reaction.size()]); // Reference
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case -1551003909: /*seriousness*/ return this.seriousness == null ? new Base[0] : new Base[] {this.seriousness}; // CodeableConcept
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case -799233858: /*recorder*/ return this.recorder == null ? new Base[0] : new Base[] {this.recorder}; // Reference
        case 270753849: /*eventParticipant*/ return this.eventParticipant == null ? new Base[0] : new Base[] {this.eventParticipant}; // Reference
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
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
        case 50511102: // category
          value = new AdverseEventCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<AdverseEventCategory>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -867509719: // reaction
          this.getReaction().add(castToReference(value)); // Reference
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case -1551003909: // seriousness
          this.seriousness = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -799233858: // recorder
          this.recorder = castToReference(value); // Reference
          return value;
        case 270753849: // eventParticipant
          this.eventParticipant = castToReference(value); // Reference
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
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
        } else if (name.equals("category")) {
          value = new AdverseEventCategoryEnumFactory().fromType(castToCode(value));
          this.category = (Enumeration) value; // Enumeration<AdverseEventCategory>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("reaction")) {
          this.getReaction().add(castToReference(value));
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("seriousness")) {
          this.seriousness = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("recorder")) {
          this.recorder = castToReference(value); // Reference
        } else if (name.equals("eventParticipant")) {
          this.eventParticipant = castToReference(value); // Reference
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
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
        case 50511102:  return getCategoryElement();
        case 3575610:  return getType(); 
        case -1867885268:  return getSubject(); 
        case 3076014:  return getDateElement();
        case -867509719:  return addReaction(); 
        case 1901043637:  return getLocation(); 
        case -1551003909:  return getSeriousness(); 
        case -1106507950:  return getOutcome(); 
        case -799233858:  return getRecorder(); 
        case 270753849:  return getEventParticipant(); 
        case -1724546052:  return getDescriptionElement();
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
        case 50511102: /*category*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -867509719: /*reaction*/ return new String[] {"Reference"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case -1551003909: /*seriousness*/ return new String[] {"CodeableConcept"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case -799233858: /*recorder*/ return new String[] {"Reference"};
        case 270753849: /*eventParticipant*/ return new String[] {"Reference"};
        case -1724546052: /*description*/ return new String[] {"string"};
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
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.category");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.date");
        }
        else if (name.equals("reaction")) {
          return addReaction();
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("seriousness")) {
          this.seriousness = new CodeableConcept();
          return this.seriousness;
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("recorder")) {
          this.recorder = new Reference();
          return this.recorder;
        }
        else if (name.equals("eventParticipant")) {
          this.eventParticipant = new Reference();
          return this.eventParticipant;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type AdverseEvent.description");
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
        dst.category = category == null ? null : category.copy();
        dst.type = type == null ? null : type.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.date = date == null ? null : date.copy();
        if (reaction != null) {
          dst.reaction = new ArrayList<Reference>();
          for (Reference i : reaction)
            dst.reaction.add(i.copy());
        };
        dst.location = location == null ? null : location.copy();
        dst.seriousness = seriousness == null ? null : seriousness.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.recorder = recorder == null ? null : recorder.copy();
        dst.eventParticipant = eventParticipant == null ? null : eventParticipant.copy();
        dst.description = description == null ? null : description.copy();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AdverseEvent))
          return false;
        AdverseEvent o = (AdverseEvent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(category, o.category, true) && compareDeep(type, o.type, true)
           && compareDeep(subject, o.subject, true) && compareDeep(date, o.date, true) && compareDeep(reaction, o.reaction, true)
           && compareDeep(location, o.location, true) && compareDeep(seriousness, o.seriousness, true) && compareDeep(outcome, o.outcome, true)
           && compareDeep(recorder, o.recorder, true) && compareDeep(eventParticipant, o.eventParticipant, true)
           && compareDeep(description, o.description, true) && compareDeep(suspectEntity, o.suspectEntity, true)
           && compareDeep(subjectMedicalHistory, o.subjectMedicalHistory, true) && compareDeep(referenceDocument, o.referenceDocument, true)
           && compareDeep(study, o.study, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AdverseEvent))
          return false;
        AdverseEvent o = (AdverseEvent) other;
        return compareValues(category, o.category, true) && compareValues(date, o.date, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, category, type
          , subject, date, reaction, location, seriousness, outcome, recorder, eventParticipant
          , description, suspectEntity, subjectMedicalHistory, referenceDocument, study);
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
   * Search parameter: <b>recorder</b>
   * <p>
   * Description: <b>Who recorded the adverse event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.recorder</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recorder", path="AdverseEvent.recorder", description="Who recorded the adverse event", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Practitioner.class, RelatedPerson.class } )
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
   * Search parameter: <b>reaction</b>
   * <p>
   * Description: <b>Adverse Reaction Events linked to exposure to substance</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.reaction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reaction", path="AdverseEvent.reaction", description="Adverse Reaction Events linked to exposure to substance", type="reference", target={Condition.class } )
  public static final String SP_REACTION = "reaction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reaction</b>
   * <p>
   * Description: <b>Adverse Reaction Events linked to exposure to substance</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.reaction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REACTION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REACTION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AdverseEvent:reaction</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REACTION = new ca.uhn.fhir.model.api.Include("AdverseEvent:reaction").toLocked();

 /**
   * Search parameter: <b>seriousness</b>
   * <p>
   * Description: <b>Mild | Moderate | Severe</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.seriousness</b><br>
   * </p>
   */
  @SearchParamDefinition(name="seriousness", path="AdverseEvent.seriousness", description="Mild | Moderate | Severe", type="token" )
  public static final String SP_SERIOUSNESS = "seriousness";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>seriousness</b>
   * <p>
   * Description: <b>Mild | Moderate | Severe</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.seriousness</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SERIOUSNESS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SERIOUSNESS);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Subject or group impacted by event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="AdverseEvent.subject", description="Subject or group impacted by event", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Device.class, Medication.class, Patient.class, ResearchSubject.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Subject or group impacted by event</b><br>
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
   * Search parameter: <b>substance</b>
   * <p>
   * Description: <b>Refers to the specific entity that caused the adverse event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AdverseEvent.suspectEntity.instance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="substance", path="AdverseEvent.suspectEntity.instance", description="Refers to the specific entity that caused the adverse event", type="reference", target={Device.class, Medication.class, MedicationAdministration.class, MedicationStatement.class, Substance.class } )
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
   * Description: <b>AE | PAE An adverse event is an event that caused harm to a patient,  an adverse reaction is a something that is a subject-specific event that is a result of an exposure to a medication, food, device or environmental substance, a potential adverse event is something that occurred and that could have caused harm to a patient but did not</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="AdverseEvent.category", description="AE | PAE \rAn adverse event is an event that caused harm to a patient,  an adverse reaction is a something that is a subject-specific event that is a result of an exposure to a medication, food, device or environmental substance, a potential adverse event is something that occurred and that could have caused harm to a patient but did not", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>AE | PAE An adverse event is an event that caused harm to a patient,  an adverse reaction is a something that is a subject-specific event that is a result of an exposure to a medication, food, device or environmental substance, a potential adverse event is something that occurred and that could have caused harm to a patient but did not</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>actual | potential</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="AdverseEvent.type", description="actual | potential", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>actual | potential</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AdverseEvent.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

