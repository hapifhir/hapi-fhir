package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A sample to be used for analysis.
 */
@ResourceDef(name="Specimen", profile="http://hl7.org/fhir/Profile/Specimen")
public class Specimen extends DomainResource {

    public enum SpecimenStatus {
        /**
         * The physical specimen is present and in good condition.
         */
        AVAILABLE, 
        /**
         * There is no physical specimen because it is either lost, destroyed or consumed.
         */
        UNAVAILABLE, 
        /**
         * The specimen cannot be used because of a quality issue such as a broken container, contamination, or too old.
         */
        UNSATISFACTORY, 
        /**
         * The specimen was entered in error and therefore nullified.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static SpecimenStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("available".equals(codeString))
          return AVAILABLE;
        if ("unavailable".equals(codeString))
          return UNAVAILABLE;
        if ("unsatisfactory".equals(codeString))
          return UNSATISFACTORY;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown SpecimenStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AVAILABLE: return "available";
            case UNAVAILABLE: return "unavailable";
            case UNSATISFACTORY: return "unsatisfactory";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AVAILABLE: return "http://hl7.org/fhir/specimen-status";
            case UNAVAILABLE: return "http://hl7.org/fhir/specimen-status";
            case UNSATISFACTORY: return "http://hl7.org/fhir/specimen-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/specimen-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AVAILABLE: return "The physical specimen is present and in good condition.";
            case UNAVAILABLE: return "There is no physical specimen because it is either lost, destroyed or consumed.";
            case UNSATISFACTORY: return "The specimen cannot be used because of a quality issue such as a broken container, contamination, or too old.";
            case ENTEREDINERROR: return "The specimen was entered in error and therefore nullified.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AVAILABLE: return "Available";
            case UNAVAILABLE: return "Unavailable";
            case UNSATISFACTORY: return "Unsatisfactory";
            case ENTEREDINERROR: return "Entered-in-error";
            default: return "?";
          }
        }
    }

  public static class SpecimenStatusEnumFactory implements EnumFactory<SpecimenStatus> {
    public SpecimenStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("available".equals(codeString))
          return SpecimenStatus.AVAILABLE;
        if ("unavailable".equals(codeString))
          return SpecimenStatus.UNAVAILABLE;
        if ("unsatisfactory".equals(codeString))
          return SpecimenStatus.UNSATISFACTORY;
        if ("entered-in-error".equals(codeString))
          return SpecimenStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown SpecimenStatus code '"+codeString+"'");
        }
        public Enumeration<SpecimenStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("available".equals(codeString))
          return new Enumeration<SpecimenStatus>(this, SpecimenStatus.AVAILABLE);
        if ("unavailable".equals(codeString))
          return new Enumeration<SpecimenStatus>(this, SpecimenStatus.UNAVAILABLE);
        if ("unsatisfactory".equals(codeString))
          return new Enumeration<SpecimenStatus>(this, SpecimenStatus.UNSATISFACTORY);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<SpecimenStatus>(this, SpecimenStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown SpecimenStatus code '"+codeString+"'");
        }
    public String toCode(SpecimenStatus code) {
      if (code == SpecimenStatus.AVAILABLE)
        return "available";
      if (code == SpecimenStatus.UNAVAILABLE)
        return "unavailable";
      if (code == SpecimenStatus.UNSATISFACTORY)
        return "unsatisfactory";
      if (code == SpecimenStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(SpecimenStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class SpecimenCollectionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Person who collected the specimen.
         */
        @Child(name = "collector", type = {Practitioner.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who collected the specimen", formalDefinition="Person who collected the specimen." )
        protected Reference collector;

        /**
         * The actual object that is the target of the reference (Person who collected the specimen.)
         */
        protected Practitioner collectorTarget;

        /**
         * To communicate any details or issues encountered during the specimen collection procedure.
         */
        @Child(name = "comment", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Collector comments", formalDefinition="To communicate any details or issues encountered during the specimen collection procedure." )
        protected StringType comment;

        /**
         * Time when specimen was collected from subject - the physiologically relevant time.
         */
        @Child(name = "collected", type = {DateTimeType.class, Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Collection time", formalDefinition="Time when specimen was collected from subject - the physiologically relevant time." )
        protected Type collected;

        /**
         * The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The quantity of specimen collected", formalDefinition="The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample." )
        protected SimpleQuantity quantity;

        /**
         * A coded value specifying the technique that is used to perform the procedure.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Technique used to perform collection", formalDefinition="A coded value specifying the technique that is used to perform the procedure." )
        protected CodeableConcept method;

        /**
         * Anatomical location from which the specimen was collected (if subject is a patient). This is the target site.  This element is not used for environmental specimens.
         */
        @Child(name = "bodySite", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Anatomical collection site", formalDefinition="Anatomical location from which the specimen was collected (if subject is a patient). This is the target site.  This element is not used for environmental specimens." )
        protected CodeableConcept bodySite;

        private static final long serialVersionUID = 2083688215L;

    /**
     * Constructor
     */
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
              this.collector = new Reference(); // cc
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
              this.collectorTarget = new Practitioner(); // aa
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
         * @return {@link #comment} (To communicate any details or issues encountered during the specimen collection procedure.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public StringType getCommentElement() { 
          if (this.comment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.comment");
            else if (Configuration.doAutoCreate())
              this.comment = new StringType(); // bb
          return this.comment;
        }

        public boolean hasCommentElement() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        public boolean hasComment() { 
          return this.comment != null && !this.comment.isEmpty();
        }

        /**
         * @param value {@link #comment} (To communicate any details or issues encountered during the specimen collection procedure.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
         */
        public SpecimenCollectionComponent setCommentElement(StringType value) { 
          this.comment = value;
          return this;
        }

        /**
         * @return To communicate any details or issues encountered during the specimen collection procedure.
         */
        public String getComment() { 
          return this.comment == null ? null : this.comment.getValue();
        }

        /**
         * @param value To communicate any details or issues encountered during the specimen collection procedure.
         */
        public SpecimenCollectionComponent setComment(String value) { 
          if (Utilities.noString(value))
            this.comment = null;
          else {
            if (this.comment == null)
              this.comment = new StringType();
            this.comment.setValue(value);
          }
          return this;
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
        public DateTimeType getCollectedDateTimeType() throws FHIRException { 
          if (!(this.collected instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.collected.getClass().getName()+" was encountered");
          return (DateTimeType) this.collected;
        }

        public boolean hasCollectedDateTimeType() { 
          return this.collected instanceof DateTimeType;
        }

        /**
         * @return {@link #collected} (Time when specimen was collected from subject - the physiologically relevant time.)
         */
        public Period getCollectedPeriod() throws FHIRException { 
          if (!(this.collected instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.collected.getClass().getName()+" was encountered");
          return (Period) this.collected;
        }

        public boolean hasCollectedPeriod() { 
          return this.collected instanceof Period;
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
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.)
         */
        public SpecimenCollectionComponent setQuantity(SimpleQuantity value) { 
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
              this.method = new CodeableConcept(); // cc
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
         * @return {@link #bodySite} (Anatomical location from which the specimen was collected (if subject is a patient). This is the target site.  This element is not used for environmental specimens.)
         */
        public CodeableConcept getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenCollectionComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new CodeableConcept(); // cc
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Anatomical location from which the specimen was collected (if subject is a patient). This is the target site.  This element is not used for environmental specimens.)
         */
        public SpecimenCollectionComponent setBodySite(CodeableConcept value) { 
          this.bodySite = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("collector", "Reference(Practitioner)", "Person who collected the specimen.", 0, java.lang.Integer.MAX_VALUE, collector));
          childrenList.add(new Property("comment", "string", "To communicate any details or issues encountered during the specimen collection procedure.", 0, java.lang.Integer.MAX_VALUE, comment));
          childrenList.add(new Property("collected[x]", "dateTime|Period", "Time when specimen was collected from subject - the physiologically relevant time.", 0, java.lang.Integer.MAX_VALUE, collected));
          childrenList.add(new Property("quantity", "SimpleQuantity", "The quantity of specimen collected; for instance the volume of a blood sample, or the physical measurement of an anatomic pathology sample.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value specifying the technique that is used to perform the procedure.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("bodySite", "CodeableConcept", "Anatomical location from which the specimen was collected (if subject is a patient). This is the target site.  This element is not used for environmental specimens.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1883491469: /*collector*/ return this.collector == null ? new Base[0] : new Base[] {this.collector}; // Reference
        case 950398559: /*comment*/ return this.comment == null ? new Base[0] : new Base[] {this.comment}; // StringType
        case 1883491145: /*collected*/ return this.collected == null ? new Base[0] : new Base[] {this.collected}; // Type
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : new Base[] {this.bodySite}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1883491469: // collector
          this.collector = castToReference(value); // Reference
          break;
        case 950398559: // comment
          this.comment = castToString(value); // StringType
          break;
        case 1883491145: // collected
          this.collected = (Type) value; // Type
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          break;
        case 1702620169: // bodySite
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("collector"))
          this.collector = castToReference(value); // Reference
        else if (name.equals("comment"))
          this.comment = castToString(value); // StringType
        else if (name.equals("collected[x]"))
          this.collected = (Type) value; // Type
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("method"))
          this.method = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("bodySite"))
          this.bodySite = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1883491469:  return getCollector(); // Reference
        case 950398559: throw new FHIRException("Cannot make property comment as it is not a complex type"); // StringType
        case 1632037015:  return getCollected(); // Type
        case -1285004149:  return getQuantity(); // SimpleQuantity
        case -1077554975:  return getMethod(); // CodeableConcept
        case 1702620169:  return getBodySite(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("collector")) {
          this.collector = new Reference();
          return this.collector;
        }
        else if (name.equals("comment")) {
          throw new FHIRException("Cannot call addChild on a primitive type Specimen.comment");
        }
        else if (name.equals("collectedDateTime")) {
          this.collected = new DateTimeType();
          return this.collected;
        }
        else if (name.equals("collectedPeriod")) {
          this.collected = new Period();
          return this.collected;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("bodySite")) {
          this.bodySite = new CodeableConcept();
          return this.bodySite;
        }
        else
          return super.addChild(name);
      }

      public SpecimenCollectionComponent copy() {
        SpecimenCollectionComponent dst = new SpecimenCollectionComponent();
        copyValues(dst);
        dst.collector = collector == null ? null : collector.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.collected = collected == null ? null : collected.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.method = method == null ? null : method.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SpecimenCollectionComponent))
          return false;
        SpecimenCollectionComponent o = (SpecimenCollectionComponent) other;
        return compareDeep(collector, o.collector, true) && compareDeep(comment, o.comment, true) && compareDeep(collected, o.collected, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(method, o.method, true) && compareDeep(bodySite, o.bodySite, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SpecimenCollectionComponent))
          return false;
        SpecimenCollectionComponent o = (SpecimenCollectionComponent) other;
        return compareValues(comment, o.comment, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (collector == null || collector.isEmpty()) && (comment == null || comment.isEmpty())
           && (collected == null || collected.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (method == null || method.isEmpty()) && (bodySite == null || bodySite.isEmpty());
      }

  public String fhirType() {
    return "Specimen.collection";

  }

  }

    @Block()
    public static class SpecimenTreatmentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Textual description of procedure.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Textual description of procedure", formalDefinition="Textual description of procedure." )
        protected StringType description;

        /**
         * A coded value specifying the procedure used to process the specimen.
         */
        @Child(name = "procedure", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Indicates the treatment or processing step  applied to the specimen", formalDefinition="A coded value specifying the procedure used to process the specimen." )
        protected CodeableConcept procedure;

        /**
         * Material used in the processing step.
         */
        @Child(name = "additive", type = {Substance.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Material used in the processing step", formalDefinition="Material used in the processing step." )
        protected List<Reference> additive;
        /**
         * The actual objects that are the target of the reference (Material used in the processing step.)
         */
        protected List<Substance> additiveTarget;


        private static final long serialVersionUID = -373251521L;

    /**
     * Constructor
     */
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
              this.procedure = new CodeableConcept(); // cc
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

    // syntactic sugar
        public SpecimenTreatmentComponent addAdditive(Reference t) { //3
          if (t == null)
            return this;
          if (this.additive == null)
            this.additive = new ArrayList<Reference>();
          this.additive.add(t);
          return this;
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

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1095204141: /*procedure*/ return this.procedure == null ? new Base[0] : new Base[] {this.procedure}; // CodeableConcept
        case -1226589236: /*additive*/ return this.additive == null ? new Base[0] : this.additive.toArray(new Base[this.additive.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -1095204141: // procedure
          this.procedure = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1226589236: // additive
          this.getAdditive().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("procedure"))
          this.procedure = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("additive"))
          this.getAdditive().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -1095204141:  return getProcedure(); // CodeableConcept
        case -1226589236:  return addAdditive(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Specimen.description");
        }
        else if (name.equals("procedure")) {
          this.procedure = new CodeableConcept();
          return this.procedure;
        }
        else if (name.equals("additive")) {
          return addAdditive();
        }
        else
          return super.addChild(name);
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SpecimenTreatmentComponent))
          return false;
        SpecimenTreatmentComponent o = (SpecimenTreatmentComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(procedure, o.procedure, true)
           && compareDeep(additive, o.additive, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SpecimenTreatmentComponent))
          return false;
        SpecimenTreatmentComponent o = (SpecimenTreatmentComponent) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (description == null || description.isEmpty()) && (procedure == null || procedure.isEmpty())
           && (additive == null || additive.isEmpty());
      }

  public String fhirType() {
    return "Specimen.treatment";

  }

  }

    @Block()
    public static class SpecimenContainerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Id for the container", formalDefinition="Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances." )
        protected List<Identifier> identifier;

        /**
         * Textual description of the container.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Textual description of the container", formalDefinition="Textual description of the container." )
        protected StringType description;

        /**
         * The type of container associated with the specimen (e.g. slide, aliquot, etc.).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Kind of container directly associated with specimen", formalDefinition="The type of container associated with the specimen (e.g. slide, aliquot, etc.)." )
        protected CodeableConcept type;

        /**
         * The capacity (volume or other measure) the container may contain.
         */
        @Child(name = "capacity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Container volume or size", formalDefinition="The capacity (volume or other measure) the container may contain." )
        protected SimpleQuantity capacity;

        /**
         * The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.
         */
        @Child(name = "specimenQuantity", type = {SimpleQuantity.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Quantity of specimen within container", formalDefinition="The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type." )
        protected SimpleQuantity specimenQuantity;

        /**
         * Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.
         */
        @Child(name = "additive", type = {CodeableConcept.class, Substance.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additive associated with container", formalDefinition="Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA." )
        protected Type additive;

        private static final long serialVersionUID = 187274879L;

    /**
     * Constructor
     */
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

    // syntactic sugar
        public SpecimenContainerComponent addIdentifier(Identifier t) { //3
          if (t == null)
            return this;
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          this.identifier.add(t);
          return this;
        }

        /**
         * @return {@link #description} (Textual description of the container.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.description");
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
         * @return {@link #type} (The type of container associated with the specimen (e.g. slide, aliquot, etc.).)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of container associated with the specimen (e.g. slide, aliquot, etc.).)
         */
        public SpecimenContainerComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #capacity} (The capacity (volume or other measure) the container may contain.)
         */
        public SimpleQuantity getCapacity() { 
          if (this.capacity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.capacity");
            else if (Configuration.doAutoCreate())
              this.capacity = new SimpleQuantity(); // cc
          return this.capacity;
        }

        public boolean hasCapacity() { 
          return this.capacity != null && !this.capacity.isEmpty();
        }

        /**
         * @param value {@link #capacity} (The capacity (volume or other measure) the container may contain.)
         */
        public SpecimenContainerComponent setCapacity(SimpleQuantity value) { 
          this.capacity = value;
          return this;
        }

        /**
         * @return {@link #specimenQuantity} (The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.)
         */
        public SimpleQuantity getSpecimenQuantity() { 
          if (this.specimenQuantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SpecimenContainerComponent.specimenQuantity");
            else if (Configuration.doAutoCreate())
              this.specimenQuantity = new SimpleQuantity(); // cc
          return this.specimenQuantity;
        }

        public boolean hasSpecimenQuantity() { 
          return this.specimenQuantity != null && !this.specimenQuantity.isEmpty();
        }

        /**
         * @param value {@link #specimenQuantity} (The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.)
         */
        public SpecimenContainerComponent setSpecimenQuantity(SimpleQuantity value) { 
          this.specimenQuantity = value;
          return this;
        }

        /**
         * @return {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public Type getAdditive() { 
          return this.additive;
        }

        /**
         * @return {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public CodeableConcept getAdditiveCodeableConcept() throws FHIRException { 
          if (!(this.additive instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.additive.getClass().getName()+" was encountered");
          return (CodeableConcept) this.additive;
        }

        public boolean hasAdditiveCodeableConcept() { 
          return this.additive instanceof CodeableConcept;
        }

        /**
         * @return {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public Reference getAdditiveReference() throws FHIRException { 
          if (!(this.additive instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.additive.getClass().getName()+" was encountered");
          return (Reference) this.additive;
        }

        public boolean hasAdditiveReference() { 
          return this.additive instanceof Reference;
        }

        public boolean hasAdditive() { 
          return this.additive != null && !this.additive.isEmpty();
        }

        /**
         * @param value {@link #additive} (Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.)
         */
        public SpecimenContainerComponent setAdditive(Type value) { 
          this.additive = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Id for container. There may be multiple; a manufacturer's bar code, lab assigned identifier, etc. The container ID may differ from the specimen id in some circumstances.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("description", "string", "Textual description of the container.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("type", "CodeableConcept", "The type of container associated with the specimen (e.g. slide, aliquot, etc.).", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("capacity", "SimpleQuantity", "The capacity (volume or other measure) the container may contain.", 0, java.lang.Integer.MAX_VALUE, capacity));
          childrenList.add(new Property("specimenQuantity", "SimpleQuantity", "The quantity of specimen in the container; may be volume, dimensions, or other appropriate measurements, depending on the specimen type.", 0, java.lang.Integer.MAX_VALUE, specimenQuantity));
          childrenList.add(new Property("additive[x]", "CodeableConcept|Reference(Substance)", "Introduced substance to preserve, maintain or enhance the specimen. Examples: Formalin, Citrate, EDTA.", 0, java.lang.Integer.MAX_VALUE, additive));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -67824454: /*capacity*/ return this.capacity == null ? new Base[0] : new Base[] {this.capacity}; // SimpleQuantity
        case 1485980595: /*specimenQuantity*/ return this.specimenQuantity == null ? new Base[0] : new Base[] {this.specimenQuantity}; // SimpleQuantity
        case -1226589236: /*additive*/ return this.additive == null ? new Base[0] : new Base[] {this.additive}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -67824454: // capacity
          this.capacity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 1485980595: // specimenQuantity
          this.specimenQuantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case -1226589236: // additive
          this.additive = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("capacity"))
          this.capacity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("specimenQuantity"))
          this.specimenQuantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("additive[x]"))
          this.additive = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 3575610:  return getType(); // CodeableConcept
        case -67824454:  return getCapacity(); // SimpleQuantity
        case 1485980595:  return getSpecimenQuantity(); // SimpleQuantity
        case 261915956:  return getAdditive(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Specimen.description");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("capacity")) {
          this.capacity = new SimpleQuantity();
          return this.capacity;
        }
        else if (name.equals("specimenQuantity")) {
          this.specimenQuantity = new SimpleQuantity();
          return this.specimenQuantity;
        }
        else if (name.equals("additiveCodeableConcept")) {
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SpecimenContainerComponent))
          return false;
        SpecimenContainerComponent o = (SpecimenContainerComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(description, o.description, true)
           && compareDeep(type, o.type, true) && compareDeep(capacity, o.capacity, true) && compareDeep(specimenQuantity, o.specimenQuantity, true)
           && compareDeep(additive, o.additive, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SpecimenContainerComponent))
          return false;
        SpecimenContainerComponent o = (SpecimenContainerComponent) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (description == null || description.isEmpty())
           && (type == null || type.isEmpty()) && (capacity == null || capacity.isEmpty()) && (specimenQuantity == null || specimenQuantity.isEmpty())
           && (additive == null || additive.isEmpty());
      }

  public String fhirType() {
    return "Specimen.container";

  }

  }

    /**
     * Id for specimen.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Identifier", formalDefinition="Id for specimen." )
    protected List<Identifier> identifier;

    /**
     * The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.
     */
    @Child(name = "accessionIdentifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifier assigned by the lab", formalDefinition="The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures." )
    protected Identifier accessionIdentifier;

    /**
     * The availability of the specimen.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="available | unavailable | unsatisfactory | entered-in-error", formalDefinition="The availability of the specimen." )
    protected Enumeration<SpecimenStatus> status;

    /**
     * The kind of material that forms the specimen.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Kind of material that forms the specimen", formalDefinition="The kind of material that forms the specimen." )
    protected CodeableConcept type;

    /**
     * Where the specimen came from. This may be from the patient(s) or from the environment or a device.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Substance.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the specimen came from. This may be from the patient(s) or from the environment or a device", formalDefinition="Where the specimen came from. This may be from the patient(s) or from the environment or a device." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Where the specimen came from. This may be from the patient(s) or from the environment or a device.)
     */
    protected Resource subjectTarget;

    /**
     * Time when specimen was received for processing or testing.
     */
    @Child(name = "receivedTime", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The time when specimen was received for processing", formalDefinition="Time when specimen was received for processing or testing." )
    protected DateTimeType receivedTime;

    /**
     * Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen.
     */
    @Child(name = "parent", type = {Specimen.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specimen from which this specimen originated", formalDefinition="Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen." )
    protected List<Reference> parent;
    /**
     * The actual objects that are the target of the reference (Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen.)
     */
    protected List<Specimen> parentTarget;


    /**
     * Details concerning the specimen collection.
     */
    @Child(name = "collection", type = {}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Collection details", formalDefinition="Details concerning the specimen collection." )
    protected SpecimenCollectionComponent collection;

    /**
     * Details concerning treatment and processing steps for the specimen.
     */
    @Child(name = "treatment", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Treatment and processing step details", formalDefinition="Details concerning treatment and processing steps for the specimen." )
    protected List<SpecimenTreatmentComponent> treatment;

    /**
     * The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.
     */
    @Child(name = "container", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Direct container of specimen (tube/slide, etc.)", formalDefinition="The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here." )
    protected List<SpecimenContainerComponent> container;

    private static final long serialVersionUID = -374913648L;

  /**
   * Constructor
   */
    public Specimen() {
      super();
    }

  /**
   * Constructor
   */
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

    // syntactic sugar
    public Specimen addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
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
          this.accessionIdentifier = new Identifier(); // cc
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
     * @return {@link #status} (The availability of the specimen.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<SpecimenStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<SpecimenStatus>(new SpecimenStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The availability of the specimen.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Specimen setStatusElement(Enumeration<SpecimenStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The availability of the specimen.
     */
    public SpecimenStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The availability of the specimen.
     */
    public Specimen setStatus(SpecimenStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<SpecimenStatus>(new SpecimenStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (The kind of material that forms the specimen.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The kind of material that forms the specimen.)
     */
    public Specimen setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subject} (Where the specimen came from. This may be from the patient(s) or from the environment or a device.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Where the specimen came from. This may be from the patient(s) or from the environment or a device.)
     */
    public Specimen setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where the specimen came from. This may be from the patient(s) or from the environment or a device.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where the specimen came from. This may be from the patient(s) or from the environment or a device.)
     */
    public Specimen setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
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
          this.receivedTime = new DateTimeType(); // bb
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
     * @return {@link #parent} (Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen.)
     */
    public List<Reference> getParent() { 
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      return this.parent;
    }

    public boolean hasParent() { 
      if (this.parent == null)
        return false;
      for (Reference item : this.parent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parent} (Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen.)
     */
    // syntactic sugar
    public Reference addParent() { //3
      Reference t = new Reference();
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return t;
    }

    // syntactic sugar
    public Specimen addParent(Reference t) { //3
      if (t == null)
        return this;
      if (this.parent == null)
        this.parent = new ArrayList<Reference>();
      this.parent.add(t);
      return this;
    }

    /**
     * @return {@link #parent} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen.)
     */
    public List<Specimen> getParentTarget() { 
      if (this.parentTarget == null)
        this.parentTarget = new ArrayList<Specimen>();
      return this.parentTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #parent} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen.)
     */
    public Specimen addParentTarget() { 
      Specimen r = new Specimen();
      if (this.parentTarget == null)
        this.parentTarget = new ArrayList<Specimen>();
      this.parentTarget.add(r);
      return r;
    }

    /**
     * @return {@link #collection} (Details concerning the specimen collection.)
     */
    public SpecimenCollectionComponent getCollection() { 
      if (this.collection == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Specimen.collection");
        else if (Configuration.doAutoCreate())
          this.collection = new SpecimenCollectionComponent(); // cc
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

    // syntactic sugar
    public Specimen addTreatment(SpecimenTreatmentComponent t) { //3
      if (t == null)
        return this;
      if (this.treatment == null)
        this.treatment = new ArrayList<SpecimenTreatmentComponent>();
      this.treatment.add(t);
      return this;
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

    // syntactic sugar
    public Specimen addContainer(SpecimenContainerComponent t) { //3
      if (t == null)
        return this;
      if (this.container == null)
        this.container = new ArrayList<SpecimenContainerComponent>();
      this.container.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Id for specimen.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("accessionIdentifier", "Identifier", "The identifier assigned by the lab when accessioning specimen(s). This is not necessarily the same as the specimen identifier, depending on local lab procedures.", 0, java.lang.Integer.MAX_VALUE, accessionIdentifier));
        childrenList.add(new Property("status", "code", "The availability of the specimen.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "The kind of material that forms the specimen.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Substance)", "Where the specimen came from. This may be from the patient(s) or from the environment or a device.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("receivedTime", "dateTime", "Time when specimen was received for processing or testing.", 0, java.lang.Integer.MAX_VALUE, receivedTime));
        childrenList.add(new Property("parent", "Reference(Specimen)", "Reference to the parent (source) specimen which is used when the specimen was either derived from or a component of another specimen.", 0, java.lang.Integer.MAX_VALUE, parent));
        childrenList.add(new Property("collection", "", "Details concerning the specimen collection.", 0, java.lang.Integer.MAX_VALUE, collection));
        childrenList.add(new Property("treatment", "", "Details concerning treatment and processing steps for the specimen.", 0, java.lang.Integer.MAX_VALUE, treatment));
        childrenList.add(new Property("container", "", "The container holding the specimen.  The recursive nature of containers; i.e. blood in tube in tray in rack is not addressed here.", 0, java.lang.Integer.MAX_VALUE, container));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 818734061: /*accessionIdentifier*/ return this.accessionIdentifier == null ? new Base[0] : new Base[] {this.accessionIdentifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<SpecimenStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case -767961010: /*receivedTime*/ return this.receivedTime == null ? new Base[0] : new Base[] {this.receivedTime}; // DateTimeType
        case -995424086: /*parent*/ return this.parent == null ? new Base[0] : this.parent.toArray(new Base[this.parent.size()]); // Reference
        case -1741312354: /*collection*/ return this.collection == null ? new Base[0] : new Base[] {this.collection}; // SpecimenCollectionComponent
        case -63342472: /*treatment*/ return this.treatment == null ? new Base[0] : this.treatment.toArray(new Base[this.treatment.size()]); // SpecimenTreatmentComponent
        case -410956671: /*container*/ return this.container == null ? new Base[0] : this.container.toArray(new Base[this.container.size()]); // SpecimenContainerComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 818734061: // accessionIdentifier
          this.accessionIdentifier = castToIdentifier(value); // Identifier
          break;
        case -892481550: // status
          this.status = new SpecimenStatusEnumFactory().fromType(value); // Enumeration<SpecimenStatus>
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case -767961010: // receivedTime
          this.receivedTime = castToDateTime(value); // DateTimeType
          break;
        case -995424086: // parent
          this.getParent().add(castToReference(value)); // Reference
          break;
        case -1741312354: // collection
          this.collection = (SpecimenCollectionComponent) value; // SpecimenCollectionComponent
          break;
        case -63342472: // treatment
          this.getTreatment().add((SpecimenTreatmentComponent) value); // SpecimenTreatmentComponent
          break;
        case -410956671: // container
          this.getContainer().add((SpecimenContainerComponent) value); // SpecimenContainerComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("accessionIdentifier"))
          this.accessionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("status"))
          this.status = new SpecimenStatusEnumFactory().fromType(value); // Enumeration<SpecimenStatus>
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("receivedTime"))
          this.receivedTime = castToDateTime(value); // DateTimeType
        else if (name.equals("parent"))
          this.getParent().add(castToReference(value));
        else if (name.equals("collection"))
          this.collection = (SpecimenCollectionComponent) value; // SpecimenCollectionComponent
        else if (name.equals("treatment"))
          this.getTreatment().add((SpecimenTreatmentComponent) value);
        else if (name.equals("container"))
          this.getContainer().add((SpecimenContainerComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 818734061:  return getAccessionIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<SpecimenStatus>
        case 3575610:  return getType(); // CodeableConcept
        case -1867885268:  return getSubject(); // Reference
        case -767961010: throw new FHIRException("Cannot make property receivedTime as it is not a complex type"); // DateTimeType
        case -995424086:  return addParent(); // Reference
        case -1741312354:  return getCollection(); // SpecimenCollectionComponent
        case -63342472:  return addTreatment(); // SpecimenTreatmentComponent
        case -410956671:  return addContainer(); // SpecimenContainerComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("accessionIdentifier")) {
          this.accessionIdentifier = new Identifier();
          return this.accessionIdentifier;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Specimen.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("receivedTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type Specimen.receivedTime");
        }
        else if (name.equals("parent")) {
          return addParent();
        }
        else if (name.equals("collection")) {
          this.collection = new SpecimenCollectionComponent();
          return this.collection;
        }
        else if (name.equals("treatment")) {
          return addTreatment();
        }
        else if (name.equals("container")) {
          return addContainer();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Specimen";

  }

      public Specimen copy() {
        Specimen dst = new Specimen();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.accessionIdentifier = accessionIdentifier == null ? null : accessionIdentifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.receivedTime = receivedTime == null ? null : receivedTime.copy();
        if (parent != null) {
          dst.parent = new ArrayList<Reference>();
          for (Reference i : parent)
            dst.parent.add(i.copy());
        };
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

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Specimen))
          return false;
        Specimen o = (Specimen) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(accessionIdentifier, o.accessionIdentifier, true)
           && compareDeep(status, o.status, true) && compareDeep(type, o.type, true) && compareDeep(subject, o.subject, true)
           && compareDeep(receivedTime, o.receivedTime, true) && compareDeep(parent, o.parent, true) && compareDeep(collection, o.collection, true)
           && compareDeep(treatment, o.treatment, true) && compareDeep(container, o.container, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Specimen))
          return false;
        Specimen o = (Specimen) other;
        return compareValues(status, o.status, true) && compareValues(receivedTime, o.receivedTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (accessionIdentifier == null || accessionIdentifier.isEmpty())
           && (status == null || status.isEmpty()) && (type == null || type.isEmpty()) && (subject == null || subject.isEmpty())
           && (receivedTime == null || receivedTime.isEmpty()) && (parent == null || parent.isEmpty())
           && (collection == null || collection.isEmpty()) && (treatment == null || treatment.isEmpty())
           && (container == null || container.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Specimen;
   }

 /**
   * Search parameter: <b>collector</b>
   * <p>
   * Description: <b>Who collected the specimen</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.collection.collector</b><br>
   * </p>
   */
  @SearchParamDefinition(name="collector", path="Specimen.collection.collector", description="Who collected the specimen", type="reference" )
  public static final String SP_COLLECTOR = "collector";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>collector</b>
   * <p>
   * Description: <b>Who collected the specimen</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.collection.collector</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam COLLECTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_COLLECTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Specimen:collector</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_COLLECTOR = new ca.uhn.fhir.model.api.Include("Specimen:collector").toLocked();

 /**
   * Search parameter: <b>container-id</b>
   * <p>
   * Description: <b>The unique identifier associated with the specimen container</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.container.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="container-id", path="Specimen.container.identifier", description="The unique identifier associated with the specimen container", type="token" )
  public static final String SP_CONTAINER_ID = "container-id";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>container-id</b>
   * <p>
   * Description: <b>The unique identifier associated with the specimen container</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.container.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTAINER_ID = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTAINER_ID);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The patient the specimen comes from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Specimen.subject", description="The patient the specimen comes from", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The patient the specimen comes from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Specimen:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Specimen:patient").toLocked();

 /**
   * Search parameter: <b>bodysite</b>
   * <p>
   * Description: <b>The code for the body site from where the specimen originated</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.collection.bodySite</b><br>
   * </p>
   */
  @SearchParamDefinition(name="bodysite", path="Specimen.collection.bodySite", description="The code for the body site from where the specimen originated", type="token" )
  public static final String SP_BODYSITE = "bodysite";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>bodysite</b>
   * <p>
   * Description: <b>The code for the body site from where the specimen originated</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.collection.bodySite</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BODYSITE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BODYSITE);

 /**
   * Search parameter: <b>container</b>
   * <p>
   * Description: <b>The kind of specimen container</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.container.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="container", path="Specimen.container.type", description="The kind of specimen container", type="token" )
  public static final String SP_CONTAINER = "container";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>container</b>
   * <p>
   * Description: <b>The kind of specimen container</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.container.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTAINER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTAINER);

 /**
   * Search parameter: <b>collected</b>
   * <p>
   * Description: <b>The date the specimen was collected</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Specimen.collection.collected[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="collected", path="Specimen.collection.collected", description="The date the specimen was collected", type="date" )
  public static final String SP_COLLECTED = "collected";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>collected</b>
   * <p>
   * Description: <b>The date the specimen was collected</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Specimen.collection.collected[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam COLLECTED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_COLLECTED);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject of the specimen</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Specimen.subject", description="The subject of the specimen", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject of the specimen</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Specimen:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Specimen:subject").toLocked();

 /**
   * Search parameter: <b>accession</b>
   * <p>
   * Description: <b>The accession number associated with the specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.accessionIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="accession", path="Specimen.accessionIdentifier", description="The accession number associated with the specimen", type="token" )
  public static final String SP_ACCESSION = "accession";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>accession</b>
   * <p>
   * Description: <b>The accession number associated with the specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.accessionIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACCESSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACCESSION);

 /**
   * Search parameter: <b>parent</b>
   * <p>
   * Description: <b>The parent of the specimen</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.parent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="parent", path="Specimen.parent", description="The parent of the specimen", type="reference" )
  public static final String SP_PARENT = "parent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>parent</b>
   * <p>
   * Description: <b>The parent of the specimen</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Specimen.parent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Specimen:parent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARENT = new ca.uhn.fhir.model.api.Include("Specimen:parent").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The specimen type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Specimen.type", description="The specimen type", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The specimen type</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The unique identifier associated with the specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Specimen.identifier", description="The unique identifier associated with the specimen", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The unique identifier associated with the specimen</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Specimen.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

