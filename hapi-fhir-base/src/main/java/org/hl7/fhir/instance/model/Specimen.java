package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Sample for analysis.
 */
@ResourceDef(name="Specimen", profile="http://hl7.org/fhir/Profile/Specimen")
public class Specimen extends DomainResource {

    public enum HierarchicalRelationshipType implements FhirEnum {
        /**
         * The target resource is the parent of the focal specimen resource.
         */
        PARENT, 
        /**
         * The target resource is the child of the focal specimen resource.
         */
        CHILD, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final HierarchicalRelationshipTypeEnumFactory ENUM_FACTORY = new HierarchicalRelationshipTypeEnumFactory();

        public static HierarchicalRelationshipType fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("parent".equals(codeString))
          return PARENT;
        if ("child".equals(codeString))
          return CHILD;
        throw new IllegalArgumentException("Unknown HierarchicalRelationshipType code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case PARENT: return "parent";
            case CHILD: return "child";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PARENT: return "";
            case CHILD: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PARENT: return "The target resource is the parent of the focal specimen resource.";
            case CHILD: return "The target resource is the child of the focal specimen resource.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PARENT: return "Parent";
            case CHILD: return "Child";
            default: return "?";
          }
        }
    }

  public static class HierarchicalRelationshipTypeEnumFactory implements EnumFactory<HierarchicalRelationshipType> {
    public HierarchicalRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("parent".equals(codeString))
          return HierarchicalRelationshipType.PARENT;
        if ("child".equals(codeString))
          return HierarchicalRelationshipType.CHILD;
        throw new IllegalArgumentException("Unknown HierarchicalRelationshipType code '"+codeString+"'");
        }
    public String toCode(HierarchicalRelationshipType code) throws IllegalArgumentException {
      if (code == HierarchicalRelationshipType.PARENT)
        return "parent";
      if (code == HierarchicalRelationshipType.CHILD)
        return "child";
      return "?";
      }
    }

    @Block()
    public static class SpecimenSourceComponent extends BackboneElement {
        /**
         * Whether this relationship is to a parent or to a child.
         */
        @Child(name="relationship", type={CodeType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="parent | child", formalDefinition="Whether this relationship is to a parent or to a child." )
        protected Enumeration<HierarchicalRelationshipType> relationship;

        /**
         * The specimen resource that is the target of this relationship.
         */
        @Child(name="target", type={Specimen.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="The subject of the relationship", formalDefinition="The specimen resource that is the target of this relationship." )
        protected List<Reference> target;
        /**
         * The actual objects that are the target of the reference (The specimen resource that is the target of this relationship.)
         */
        protected List<Specimen> targetTarget;


        private static final long serialVersionUID = 1653620362L;

      public SpecimenSourceComponent() {
        super();
      }

      public SpecimenSourceComponent(Enumeration<HierarchicalRelationshipType> relationship) {
        super();
        this.relationship = relationship;
      }

        /**
         * @return {@link #relationship} (Whether this relationship is to a parent or to a child.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public Enumeration<HierarchicalRelationshipType> getRelationshipElement() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenSourceComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Enumeration<HierarchicalRelationshipType>();
          return this.relationship;
        }

        public boolean hasRelationshipElement() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (Whether this relationship is to a parent or to a child.). This is the underlying object with id, value and extensions. The accessor "getRelationship" gives direct access to the value
         */
        public SpecimenSourceComponent setRelationshipElement(Enumeration<HierarchicalRelationshipType> value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return Whether this relationship is to a parent or to a child.
         */
        public HierarchicalRelationshipType getRelationship() { 
          return this.relationship == null ? null : this.relationship.getValue();
        }

        /**
         * @param value Whether this relationship is to a parent or to a child.
         */
        public SpecimenSourceComponent setRelationship(HierarchicalRelationshipType value) { 
            if (this.relationship == null)
              this.relationship = new Enumeration<HierarchicalRelationshipType>(HierarchicalRelationshipType.ENUM_FACTORY);
            this.relationship.setValue(value);
          return this;
        }

        /**
         * @return {@link #target} (The specimen resource that is the target of this relationship.)
         */
        public List<Reference> getTarget() { 
          if (this.target == null)
            this.target = new ArrayList<Reference>();
          return this.target;
        }

        public boolean hasTarget() { 
          if (this.target == null)
            return false;
          for (Reference item : this.target)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #target} (The specimen resource that is the target of this relationship.)
         */
    // syntactic sugar
        public Reference addTarget() { //3
          Reference t = new Reference();
          if (this.target == null)
            this.target = new ArrayList<Reference>();
          this.target.add(t);
          return t;
        }

        /**
         * @return {@link #target} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The specimen resource that is the target of this relationship.)
         */
        public List<Specimen> getTargetTarget() { 
          if (this.targetTarget == null)
            this.targetTarget = new ArrayList<Specimen>();
          return this.targetTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #target} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The specimen resource that is the target of this relationship.)
         */
        public Specimen addTargetTarget() { 
          Specimen r = new Specimen();
          if (this.targetTarget == null)
            this.targetTarget = new ArrayList<Specimen>();
          this.targetTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("relationship", "code", "Whether this relationship is to a parent or to a child.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("target", "Reference(Specimen)", "The specimen resource that is the target of this relationship.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      public SpecimenSourceComponent copy() {
        SpecimenSourceComponent dst = new SpecimenSourceComponent();
        copyValues(dst);
        dst.relationship = relationship == null ? null : relationship.copy();
        if (target != null) {
          dst.target = new ArrayList<Reference>();
          for (Reference i : target)
            dst.target.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (relationship == null || relationship.isEmpty()) && (target == null || target.isEmpty())
          ;
      }

  }

    @Block()
    public static class SpecimenCollectionComponent extends BackboneElement {
        /**
         * Person who collected the specimen.
         */
        @Child(name="collector", type={Practitioner.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Who collected the specimen", formalDefinition="Person who collected the specimen." )
        protected Reference collector;

        /**
         * The actual object that is the target of the reference (Person who collected the specimen.)
         */
        protected Practitioner collectorTarget;

        /**
         * To communicate any details or issues encountered during the specimen collection procedure.
         */
        @Child(name="comment", type={StringType.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Collector comments", formalDefinition="To communicate any details or issues encountered during the specimen collection procedure." )
        protected List<StringType> comment;

        /**
         * Time when specimen was collected from subject - the physiologically relevant time.
         */
        @Child(name="collected", type={DateTimeType.class, Period.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Collection time", formalDefinition="Time when specimen was collected from subject - the physiologically relevant time." )
        protected Type collected;

        /**
         * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.
         */
        @Child(name="quantity", type={Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="The quantity of specimen collected", formalDefinition="The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample." )
        protected Quantity quantity;

        /**
         * A coded value specifying the technique that is used to perform the procedure.
         */
        @Child(name="method", type={CodeableConcept.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Technique used to perform collection", formalDefinition="A coded value specifying the technique that is used to perform the procedure." )
        protected CodeableConcept method;

        /**
         * Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens.
         */
        @Child(name="sourceSite", type={CodeableConcept.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Anatomical collection site", formalDefinition="Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens." )
        protected CodeableConcept sourceSite;

        private static final long serialVersionUID = 498103158L;

      public SpecimenCollectionComponent() {
        super();
      }

        /**
         * @return {@link #collector} (Person who collected the specimen.)
         */
        public Reference getCollector() { 
          if (this.collector == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.collector");
            else if (Configuration.doAutoCreate())
              this.collector = new Reference();
          return this.collector;
        }

        public boolean hasCollector() { 
          return this.collector != null && !this.collector.isEmpty();
        }

        /**
         * @param value {@link #collector} (Person who collected the specimen.)
         */
        public SpecimenCollectionComponent setCollector(Reference value) { 
          this.collector = value;
          return this;
        }

        /**
         * @return {@link #collector} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who collected the specimen.)
         */
        public Practitioner getCollectorTarget() { 
          if (this.collectorTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.collector");
            else if (Configuration.doAutoCreate())
              this.collectorTarget = new Practitioner();
          return this.collectorTarget;
        }

        /**
         * @param value {@link #collector} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who collected the specimen.)
         */
        public SpecimenCollectionComponent setCollectorTarget(Practitioner value) { 
          this.collectorTarget = value;
          return this;
        }

        /**
         * @return {@link #comment} (To communicate any details or issues encountered during the specimen collection procedure.)
         */
        public List<StringType> getComment() { 
          if (this.comment == null)
            this.comment = new ArrayList<StringType>();
          return this.comment;
        }

        public boolean hasComment() { 
          if (this.comment == null)
            return false;
          for (StringType item : this.comment)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #comment} (To communicate any details or issues encountered during the specimen collection procedure.)
         */
    // syntactic sugar
        public StringType addCommentElement() {//2 
          StringType t = new StringType();
          if (this.comment == null)
            this.comment = new ArrayList<StringType>();
          this.comment.add(t);
          return t;
        }

        /**
         * @param value {@link #comment} (To communicate any details or issues encountered during the specimen collection procedure.)
         */
        public SpecimenCollectionComponent addComment(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.comment == null)
            this.comment = new ArrayList<StringType>();
          this.comment.add(t);
          return this;
        }

        /**
         * @param value {@link #comment} (To communicate any details or issues encountered during the specimen collection procedure.)
         */
        public boolean hasComment(String value) { 
          if (this.comment == null)
            return false;
          for (StringType v : this.comment)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #collected} (Time when specimen was collected from subject - the physiologically relevant time.)
         */
        public Type getCollected() { 
          return this.collected;
        }

        /**
         * @return {@link #collected} (Time when specimen was collected from subject - the physiologically relevant time.)
         */
        public DateTimeType getCollectedDateTimeType() throws Exception { 
          if (!(this.collected instanceof DateTimeType))
            throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.collected.getClass().getName()+" was encountered");
          return (DateTimeType) this.collected;
        }

        /**
         * @return {@link #collected} (Time when specimen was collected from subject - the physiologically relevant time.)
         */
        public Period getCollectedPeriod() throws Exception { 
          if (!(this.collected instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.collected.getClass().getName()+" was encountered");
          return (Period) this.collected;
        }

        public boolean hasCollected() { 
          return this.collected != null && !this.collected.isEmpty();
        }

        /**
         * @param value {@link #collected} (Time when specimen was collected from subject - the physiologically relevant time.)
         */
        public SpecimenCollectionComponent setCollected(Type value) { 
          this.collected = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.)
         */
        public SpecimenCollectionComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #method} (A coded value specifying the technique that is used to perform the procedure.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept();
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (A coded value specifying the technique that is used to perform the procedure.)
         */
        public SpecimenCollectionComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #sourceSite} (Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens.)
         */
        public CodeableConcept getSourceSite() { 
          if (this.sourceSite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.sourceSite");
            else if (Configuration.doAutoCreate())
              this.sourceSite = new CodeableConcept();
          return this.sourceSite;
        }

        public boolean hasSourceSite() { 
          return this.sourceSite != null && !this.sourceSite.isEmpty();
        }

        /**
         * @param value {@link #sourceSite} (Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens.)
         */
        public SpecimenCollectionComponent setSourceSite(CodeableConcept value) { 
          this.sourceSite = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("collector", "Reference(Practitioner)", "Person who collected the specimen.", 0, java.lang.Integer.MAX_VALUE, collector));
          childrenList.add(new Property("comment", "string", "To communicate any details or issues encountered during the specimen collection procedure.", 0, java.lang.Integer.MAX_VALUE, comment));
          childrenList.add(new Property("collected[x]", "dateTime|Period", "Time when specimen was collected from subject - the physiologically relevant time.", 0, java.lang.Integer.MAX_VALUE, collected));
          childrenList.add(new Property("quantity", "Quantity", "The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value specifying the technique that is used to perform the procedure.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("sourceSite", "CodeableConcept", "Anatomical location from which the specimen should be collected (if subject is a patient). This element is not used for environmental specimens.", 0, java.lang.Integer.MAX_VALUE, sourceSite));
        }

      public SpecimenCollectionComponent copy() {
        SpecimenCollectionComponent dst = new SpecimenCollectionComponent();
        copyValues(dst);
        dst.collector = collector == null ? null : collector.copy();
        if (comment != null) {
          dst.comment = new ArrayList<StringType>();
          for (StringType i : comment)
            dst.comment.add(i.copy());
        };
        dst.collected = collected == null ? null : collected.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.method = method == null ? null : method.copy();
        dst.sourceSite = sourceSite == null ? null : sourceSite.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (collector == null || collector.isEmpty()) && (comment == null || comment.isEmpty())
           && (collected == null || collected.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (method == null || method.isEmpty()) && (sourceSite == null || sourceSite.isEmpty());
      }

  }

    @Block()
    public static class SpecimenTreatmentComponent extends BackboneElement {
        /**
         * Textual description of procedure.
         */
        @Child(name="description", type={StringType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Textual description of procedure", formalDefinition="Textual description of procedure." )
        protected StringType description;

        /**
         * A coded value specifying the procedure used to process the specimen.
         */
        @Child(name="procedure", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Indicates the treatment or processing step  applied to the specimen", formalDefinition="A coded value specifying the procedure used to process the specimen." )
        protected CodeableConcept procedure;

        /**
         * Material used in the processing step.
         */
        @Child(name="additive", type={Substance.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Material used in the processing step", formalDefinition="Material used in the processing step." )
        protected List<Reference> additive;
        /**
         * The actual objects that are the target of the reference (Material used in the processing step.)
         */
        protected List<Substance> additiveTarget;


        private static final long serialVersionUID = -373251521L;

      public SpecimenTreatmentComponent() {
        super();
      }

        /**
         * @return {@link #description} (Textual description of procedure.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenTreatmentComponent.description");
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
         * @param value {@link #description} (Textual description of procedure.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public SpecimenTreatmentComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Textual description of procedure.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Textual description of procedure.
         */
        public SpecimenTreatmentComponent setDescription(String value) { 
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
         * @return {@link #procedure} (A coded value specifying the procedure used to process the specimen.)
         */
        public CodeableConcept getProcedure() { 
          if (this.procedure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenTreatmentComponent.procedure");
            else if (Configuration.doAutoCreate())
              this.procedure = new CodeableConcept();
          return this.procedure;
        }

        public boolean hasProcedure() { 
          return this.procedure != null && !this.procedure.isEmpty();
        }

        /**
         * @param value {@link #procedure} (A coded value specifying the procedure used to process the specimen.)
         */
        public SpecimenTreatmentComponent setProcedure(CodeableConcept value) { 
          this.procedure = value;
          return this;
        }

        /**
         * @return {@link #additive} (Material used in the processing step.)
         */
        public List<Reference> getAdditive() { 
          if (this.additive == null)
            this.additive = new ArrayList<Reference>();
          return this.additive;
        }

        public boolean hasAdditive() { 
          if (this.additive == null)
            return false;
          for (Reference item : this.additive)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #additive} (Material used in the processing step.)
         */
    // syntactic sugar
        public Reference addAdditive() { //3
          Reference t = new Reference();
          if (this.additive == null)
            this.additive = new ArrayList<Reference>();
          this.additive.add(t);
          return t;
        }

        /**
         * @return {@link #additive} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Material used in the processing step.)
         */
        public List<Substance> getAdditiveTarget() { 
          if (this.additiveTarget == null)
            this.additiveTarget = new ArrayList<Substance>();
          return this.additiveTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #additive} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Material used in the processing step.)
         */
        public Substance addAdditiveTarget() { 
          Substance r = new Substance();
          if (this.additiveTarget == null)
            this.additiveTarget = new ArrayList<Substance>();
          this.additiveTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "Textual description of procedure.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("procedure", "CodeableConcept", "A coded value specifying the procedure used to process the specimen.", 0, java.lang.Integer.MAX_VALUE, procedure));
          childrenList.add(new Property("additive", "Reference(Substance)", "Material used in the processing step.", 0, java.lang.Integer.MAX_VALUE, additive));
        }

      public SpecimenTreatmentComponent copy() {
        SpecimenTreatmentComponent dst = new SpecimenTreatmentComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.procedure = procedure == null ? null : procedure.copy();
        if (additive != null) {
          dst.additive = new ArrayList<Reference>();
          for (Reference i : additive)
            dst.additive.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (description == null || description.isEmpty()) && (procedure == null || procedure.isEmpty())
           && (additive == null || additive.isEmpty());
      }

  }

    @Block()
    public static class SpecimenContainerComponent extends BackboneElement {
        /**
         * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.
         */
        @Child(name="identifier", type={Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Id for the container", formalDefinition="Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances." )
        protected List<Identifier> identifier;

        /**
         * Textual description of the container.
         */
        @Child(name="description", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Textual description of the container", formalDefinition="Textual description of the container." )
        protected StringType description;

        /**
         * The type of container associated with the specimen (e.g. slide, aliquot, etc).
         */
        @Child(name="type", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Kind of container directly associated with specimen", formalDefinition="The type of container associated with the specimen (e.g. slide, aliquot, etc)." )
        protected CodeableConcept type;

        /**
         * The capacity (volume or other measure) the container may contain.
         */
        @Child(name="capacity", type={Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Container volume or size", formalDefinition="The capacity (volume or other measure) the container may contain." )
        protected Quantity capacity;

        /**
         * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.
         */
        @Child(name="specimenQuantity", type={Quantity.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Quantity of specimen within container", formalDefinition="The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type." )
        protected Quantity specimenQuantity;

        /**
         * Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA.
         */
        @Child(name="additive", type={CodeableConcept.class, Substance.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Additive associated with container", formalDefinition="Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA." )
        protected Type additive;

        private static final long serialVersionUID = -1608132325L;

      public SpecimenContainerComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.)
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
         * @return {@link #identifier} (Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.)
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
         * @return {@link #description} (Textual description of the container.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.description");
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
         * @param value {@link #description} (Textual description of the container.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public SpecimenContainerComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Textual description of the container.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Textual description of the container.
         */
        public SpecimenContainerComponent setDescription(String value) { 
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
         * @return {@link #type} (The type of container associated with the specimen (e.g. slide, aliquot, etc).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of container associated with the specimen (e.g. slide, aliquot, etc).)
         */
        public SpecimenContainerComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #capacity} (The capacity (volume or other measure) the container may contain.)
         */
        public Quantity getCapacity() { 
          if (this.capacity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.capacity");
            else if (Configuration.doAutoCreate())
              this.capacity = new Quantity();
          return this.capacity;
        }

        public boolean hasCapacity() { 
          return this.capacity != null && !this.capacity.isEmpty();
        }

        /**
         * @param value {@link #capacity} (The capacity (volume or other measure) the container may contain.)
         */
        public SpecimenContainerComponent setCapacity(Quantity value) { 
          this.capacity = value;
          return this;
        }

        /**
         * @return {@link #specimenQuantity} (The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.)
         */
        public Quantity getSpecimenQuantity() { 
          if (this.specimenQuantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.specimenQuantity");
            else if (Configuration.doAutoCreate())
              this.specimenQuantity = new Quantity();
          return this.specimenQuantity;
        }

        public boolean hasSpecimenQuantity() { 
          return this.specimenQuantity != null && !this.specimenQuantity.isEmpty();
        }

        /**
         * @param value {@link #specimenQuantity} (The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.)
         */
        public SpecimenContainerComponent setSpecimenQuantity(Quantity value) { 
          this.specimenQuantity = value;
          return this;
        }

        /**
         * @return {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA.)
         */
        public Type getAdditive() { 
          return this.additive;
        }

        /**
         * @return {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA.)
         */
        public CodeableConcept getAdditiveCodeableConcept() throws Exception { 
          if (!(this.additive instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.additive.getClass().getName()+" was encountered");
          return (CodeableConcept) this.additive;
        }

        /**
         * @return {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA.)
         */
        public Reference getAdditiveReference() throws Exception { 
          if (!(this.additive instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.additive.getClass().getName()+" was encountered");
          return (Reference) this.additive;
        }

        public boolean hasAdditive() { 
          return this.additive != null && !this.additive.isEmpty();
        }

        /**
         * @param value {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA.)
         */
        public SpecimenContainerComponent setAdditive(Type value) { 
          this.additive = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("description", "string", "Textual description of the container.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("type", "CodeableConcept", "The type of container associated with the specimen (e.g. slide, aliquot, etc).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("capacity", "Quantity", "The capacity (volume or other measure) the container may contain.", 0, java.lang.Integer.MAX_VALUE, capacity));
          childrenList.add(new Property("specimenQuantity", "Quantity", "The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.", 0, java.lang.Integer.MAX_VALUE, specimenQuantity));
          childrenList.add(new Property("additive[x]", "CodeableConcept|Reference(Substance)", "Introduced substance to preserve, maintain or enhance the specimen. examples: Formalin, Citrate, EDTA.", 0, java.lang.Integer.MAX_VALUE, additive));
        }

      public SpecimenContainerComponent copy() {
        SpecimenContainerComponent dst = new SpecimenContainerComponent();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.type = type == null ? null : type.copy();
        dst.capacity = capacity == null ? null : capacity.copy();
        dst.specimenQuantity = specimenQuantity == null ? null : specimenQuantity.copy();
        dst.additive = additive == null ? null : additive.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (description == null || description.isEmpty())
           && (type == null || type.isEmpty()) && (capacity == null || capacity.isEmpty()) && (specimenQuantity == null || specimenQuantity.isEmpty())
           && (additive == null || additive.isEmpty());
      }

  }

    /**
     * Id for specimen.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Identifier", formalDefinition="Id for specimen." )
    protected List<Identifier> identifier;

    /**
     * Kind of material that forms the specimen.
     */
    @Child(name="type", type={CodeableConcept.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Kind of material that forms the specimen", formalDefinition="Kind of material that forms the specimen." )
    protected CodeableConcept type;

    /**
     * Parent specimen from which the focal specimen was a component.
     */
    @Child(name="source", type={}, order=1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Parent of specimen", formalDefinition="Parent specimen from which the focal specimen was a component." )
    protected List<SpecimenSourceComponent> source;

    /**
     * Where the specimen came from. This may be the patient(s) or from the environment or  a device.
     */
    @Child(name="subject", type={Patient.class, Group.class, Device.class, Substance.class}, order=2, min=1, max=1)
    @Description(shortDefinition="Where the specimen came from. This may be the patient(s) or from the environment or  a device", formalDefinition="Where the specimen came from. This may be the patient(s) or from the environment or  a device." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Where the specimen came from. This may be the patient(s) or from the environment or  a device.)
     */
    protected Resource subjectTarget;

    /**
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     */
    @Child(name="accessionIdentifier", type={Identifier.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Identifier assigned by the lab", formalDefinition="The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures." )
    protected Identifier accessionIdentifier;

    /**
     * Time when specimen was received for processing or testing.
     */
    @Child(name="receivedTime", type={DateTimeType.class}, order=4, min=0, max=1)
    @Description(shortDefinition="The time when specimen was received for processing", formalDefinition="Time when specimen was received for processing or testing." )
    protected DateTimeType receivedTime;

    /**
     * Details concerning the specimen collection.
     */
    @Child(name="collection", type={}, order=5, min=0, max=1)
    @Description(shortDefinition="Collection details", formalDefinition="Details concerning the specimen collection." )
    protected SpecimenCollectionComponent collection;

    /**
     * Details concerning treatment and processing steps for the specimen.
     */
    @Child(name="treatment", type={}, order=6, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Treatment and processing step details", formalDefinition="Details concerning treatment and processing steps for the specimen." )
    protected List<SpecimenTreatmentComponent> treatment;

    /**
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     */
    @Child(name="container", type={}, order=7, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Direct container of specimen (tube/slide, etc)", formalDefinition="The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here." )
    protected List<SpecimenContainerComponent> container;

    private static final long serialVersionUID = 862754396L;

    public Specimen() {
      super();
    }

    public Specimen(Reference subject) {
      super();
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Id for specimen.)
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
     * @return {@link #identifier} (Id for specimen.)
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
     * @return {@link #type} (Kind of material that forms the specimen.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept();
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Kind of material that forms the specimen.)
     */
    public Specimen setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #source} (Parent specimen from which the focal specimen was a component.)
     */
    public List<SpecimenSourceComponent> getSource() { 
      if (this.source == null)
        this.source = new ArrayList<SpecimenSourceComponent>();
      return this.source;
    }

    public boolean hasSource() { 
      if (this.source == null)
        return false;
      for (SpecimenSourceComponent item : this.source)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #source} (Parent specimen from which the focal specimen was a component.)
     */
    // syntactic sugar
    public SpecimenSourceComponent addSource() { //3
      SpecimenSourceComponent t = new SpecimenSourceComponent();
      if (this.source == null)
        this.source = new ArrayList<SpecimenSourceComponent>();
      this.source.add(t);
      return t;
    }

    /**
     * @return {@link #subject} (Where the specimen came from. This may be the patient(s) or from the environment or  a device.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Where the specimen came from. This may be the patient(s) or from the environment or  a device.)
     */
    public Specimen setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where the specimen came from. This may be the patient(s) or from the environment or  a device.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where the specimen came from. This may be the patient(s) or from the environment or  a device.)
     */
    public Specimen setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #accessionIdentifier} (The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.)
     */
    public Identifier getAccessionIdentifier() { 
      if (this.accessionIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.accessionIdentifier");
        else if (Configuration.doAutoCreate())
          this.accessionIdentifier = new Identifier();
      return this.accessionIdentifier;
    }

    public boolean hasAccessionIdentifier() { 
      return this.accessionIdentifier != null && !this.accessionIdentifier.isEmpty();
    }

    /**
     * @param value {@link #accessionIdentifier} (The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.)
     */
    public Specimen setAccessionIdentifier(Identifier value) { 
      this.accessionIdentifier = value;
      return this;
    }

    /**
     * @return {@link #receivedTime} (Time when specimen was received for processing or testing.). This is the underlying object with id, value and extensions. The accessor "getReceivedTime" gives direct access to the value
     */
    public DateTimeType getReceivedTimeElement() { 
      if (this.receivedTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.receivedTime");
        else if (Configuration.doAutoCreate())
          this.receivedTime = new DateTimeType();
      return this.receivedTime;
    }

    public boolean hasReceivedTimeElement() { 
      return this.receivedTime != null && !this.receivedTime.isEmpty();
    }

    public boolean hasReceivedTime() { 
      return this.receivedTime != null && !this.receivedTime.isEmpty();
    }

    /**
     * @param value {@link #receivedTime} (Time when specimen was received for processing or testing.). This is the underlying object with id, value and extensions. The accessor "getReceivedTime" gives direct access to the value
     */
    public Specimen setReceivedTimeElement(DateTimeType value) { 
      this.receivedTime = value;
      return this;
    }

    /**
     * @return Time when specimen was received for processing or testing.
     */
    public Date getReceivedTime() { 
      return this.receivedTime == null ? null : this.receivedTime.getValue();
    }

    /**
     * @param value Time when specimen was received for processing or testing.
     */
    public Specimen setReceivedTime(Date value) { 
      if (value == null)
        this.receivedTime = null;
      else {
        if (this.receivedTime == null)
          this.receivedTime = new DateTimeType();
        this.receivedTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #collection} (Details concerning the specimen collection.)
     */
    public SpecimenCollectionComponent getCollection() { 
      if (this.collection == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.collection");
        else if (Configuration.doAutoCreate())
          this.collection = new SpecimenCollectionComponent();
      return this.collection;
    }

    public boolean hasCollection() { 
      return this.collection != null && !this.collection.isEmpty();
    }

    /**
     * @param value {@link #collection} (Details concerning the specimen collection.)
     */
    public Specimen setCollection(SpecimenCollectionComponent value) { 
      this.collection = value;
      return this;
    }

    /**
     * @return {@link #treatment} (Details concerning treatment and processing steps for the specimen.)
     */
    public List<SpecimenTreatmentComponent> getTreatment() { 
      if (this.treatment == null)
        this.treatment = new ArrayList<SpecimenTreatmentComponent>();
      return this.treatment;
    }

    public boolean hasTreatment() { 
      if (this.treatment == null)
        return false;
      for (SpecimenTreatmentComponent item : this.treatment)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #treatment} (Details concerning treatment and processing steps for the specimen.)
     */
    // syntactic sugar
    public SpecimenTreatmentComponent addTreatment() { //3
      SpecimenTreatmentComponent t = new SpecimenTreatmentComponent();
      if (this.treatment == null)
        this.treatment = new ArrayList<SpecimenTreatmentComponent>();
      this.treatment.add(t);
      return t;
    }

    /**
     * @return {@link #container} (The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.)
     */
    public List<SpecimenContainerComponent> getContainer() { 
      if (this.container == null)
        this.container = new ArrayList<SpecimenContainerComponent>();
      return this.container;
    }

    public boolean hasContainer() { 
      if (this.container == null)
        return false;
      for (SpecimenContainerComponent item : this.container)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #container} (The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.)
     */
    // syntactic sugar
    public SpecimenContainerComponent addContainer() { //3
      SpecimenContainerComponent t = new SpecimenContainerComponent();
      if (this.container == null)
        this.container = new ArrayList<SpecimenContainerComponent>();
      this.container.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Id for specimen.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "CodeableConcept", "Kind of material that forms the specimen.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("source", "", "Parent specimen from which the focal specimen was a component.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Substance)", "Where the specimen came from. This may be the patient(s) or from the environment or  a device.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("accessionIdentifier", "Identifier", "The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.", 0, java.lang.Integer.MAX_VALUE, accessionIdentifier));
        childrenList.add(new Property("receivedTime", "dateTime", "Time when specimen was received for processing or testing.", 0, java.lang.Integer.MAX_VALUE, receivedTime));
        childrenList.add(new Property("collection", "", "Details concerning the specimen collection.", 0, java.lang.Integer.MAX_VALUE, collection));
        childrenList.add(new Property("treatment", "", "Details concerning treatment and processing steps for the specimen.", 0, java.lang.Integer.MAX_VALUE, treatment));
        childrenList.add(new Property("container", "", "The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.", 0, java.lang.Integer.MAX_VALUE, container));
      }

      public Specimen copy() {
        Specimen dst = new Specimen();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (source != null) {
          dst.source = new ArrayList<SpecimenSourceComponent>();
          for (SpecimenSourceComponent i : source)
            dst.source.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.accessionIdentifier = accessionIdentifier == null ? null : accessionIdentifier.copy();
        dst.receivedTime = receivedTime == null ? null : receivedTime.copy();
        dst.collection = collection == null ? null : collection.copy();
        if (treatment != null) {
          dst.treatment = new ArrayList<SpecimenTreatmentComponent>();
          for (SpecimenTreatmentComponent i : treatment)
            dst.treatment.add(i.copy());
        };
        if (container != null) {
          dst.container = new ArrayList<SpecimenContainerComponent>();
          for (SpecimenContainerComponent i : container)
            dst.container.add(i.copy());
        };
        return dst;
      }

      protected Specimen typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (type == null || type.isEmpty())
           && (source == null || source.isEmpty()) && (subject == null || subject.isEmpty()) && (accessionIdentifier == null || accessionIdentifier.isEmpty())
           && (receivedTime == null || receivedTime.isEmpty()) && (collection == null || collection.isEmpty())
           && (treatment == null || treatment.isEmpty()) && (container == null || container.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Specimen;
   }

  @SearchParamDefinition(name="patient", path="Specimen.subject", description="The patient the specimen comes from", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="subject", path="Specimen.subject", description="The subject of the specimen", type="reference" )
  public static final String SP_SUBJECT = "subject";

}

