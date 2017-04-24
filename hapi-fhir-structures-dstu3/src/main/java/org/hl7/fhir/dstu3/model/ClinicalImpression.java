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
 * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow. This resource is called "ClinicalImpression" rather than "ClinicalAssessment" to avoid confusion with the recording of assessment tools such as Apgar score.
 */
@ResourceDef(name="ClinicalImpression", profile="http://hl7.org/fhir/Profile/ClinicalImpression")
public class ClinicalImpression extends DomainResource {

    public enum ClinicalImpressionStatus {
        /**
         * The assessment is still on-going and results are not yet final.
         */
        DRAFT, 
        /**
         * The assessment is done and the results are final.
         */
        COMPLETED, 
        /**
         * This assessment was never actually done and the record is erroneous (e.g. Wrong patient).
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ClinicalImpressionStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ClinicalImpressionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/clinical-impression-status";
            case COMPLETED: return "http://hl7.org/fhir/clinical-impression-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/clinical-impression-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The assessment is still on-going and results are not yet final.";
            case COMPLETED: return "The assessment is done and the results are final.";
            case ENTEREDINERROR: return "This assessment was never actually done and the record is erroneous (e.g. Wrong patient).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "In progress";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class ClinicalImpressionStatusEnumFactory implements EnumFactory<ClinicalImpressionStatus> {
    public ClinicalImpressionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ClinicalImpressionStatus.DRAFT;
        if ("completed".equals(codeString))
          return ClinicalImpressionStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ClinicalImpressionStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ClinicalImpressionStatus code '"+codeString+"'");
        }
        public Enumeration<ClinicalImpressionStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ClinicalImpressionStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ClinicalImpressionStatus>(this, ClinicalImpressionStatus.DRAFT);
        if ("completed".equals(codeString))
          return new Enumeration<ClinicalImpressionStatus>(this, ClinicalImpressionStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ClinicalImpressionStatus>(this, ClinicalImpressionStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ClinicalImpressionStatus code '"+codeString+"'");
        }
    public String toCode(ClinicalImpressionStatus code) {
      if (code == ClinicalImpressionStatus.DRAFT)
        return "draft";
      if (code == ClinicalImpressionStatus.COMPLETED)
        return "completed";
      if (code == ClinicalImpressionStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ClinicalImpressionStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ClinicalImpressionInvestigationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", "clinical", "diagnostic", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A name/code for the set", formalDefinition="A name/code for the group (\"set\") of investigations. Typically, this will be something like \"signs\", \"symptoms\", \"clinical\", \"diagnostic\", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/investigation-sets")
        protected CodeableConcept code;

        /**
         * A record of a specific investigation that was undertaken.
         */
        @Child(name = "item", type = {Observation.class, QuestionnaireResponse.class, FamilyMemberHistory.class, DiagnosticReport.class, RiskAssessment.class, ImagingStudy.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Record of a specific investigation", formalDefinition="A record of a specific investigation that was undertaken." )
        protected List<Reference> item;
        /**
         * The actual objects that are the target of the reference (A record of a specific investigation that was undertaken.)
         */
        protected List<Resource> itemTarget;


        private static final long serialVersionUID = -301363326L;

    /**
     * Constructor
     */
      public ClinicalImpressionInvestigationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ClinicalImpressionInvestigationComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", "clinical", "diagnostic", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClinicalImpressionInvestigationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", "clinical", "diagnostic", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.)
         */
        public ClinicalImpressionInvestigationComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #item} (A record of a specific investigation that was undertaken.)
         */
        public List<Reference> getItem() { 
          if (this.item == null)
            this.item = new ArrayList<Reference>();
          return this.item;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ClinicalImpressionInvestigationComponent setItem(List<Reference> theItem) { 
          this.item = theItem;
          return this;
        }

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (Reference item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addItem() { //3
          Reference t = new Reference();
          if (this.item == null)
            this.item = new ArrayList<Reference>();
          this.item.add(t);
          return t;
        }

        public ClinicalImpressionInvestigationComponent addItem(Reference t) { //3
          if (t == null)
            return this;
          if (this.item == null)
            this.item = new ArrayList<Reference>();
          this.item.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
         */
        public Reference getItemFirstRep() { 
          if (getItem().isEmpty()) {
            addItem();
          }
          return getItem().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getItemTarget() { 
          if (this.itemTarget == null)
            this.itemTarget = new ArrayList<Resource>();
          return this.itemTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "A name/code for the group (\"set\") of investigations. Typically, this will be something like \"signs\", \"symptoms\", \"clinical\", \"diagnostic\", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("item", "Reference(Observation|QuestionnaireResponse|FamilyMemberHistory|DiagnosticReport|RiskAssessment|ImagingStudy)", "A record of a specific investigation that was undertaken.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3242771: // item
          this.getItem().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("item")) {
          this.getItem().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 3242771: /*item*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

      public ClinicalImpressionInvestigationComponent copy() {
        ClinicalImpressionInvestigationComponent dst = new ClinicalImpressionInvestigationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (item != null) {
          dst.item = new ArrayList<Reference>();
          for (Reference i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ClinicalImpressionInvestigationComponent))
          return false;
        ClinicalImpressionInvestigationComponent o = (ClinicalImpressionInvestigationComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ClinicalImpressionInvestigationComponent))
          return false;
        ClinicalImpressionInvestigationComponent o = (ClinicalImpressionInvestigationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, item);
      }

  public String fhirType() {
    return "ClinicalImpression.investigation";

  }

  }

    @Block()
    public static class ClinicalImpressionFindingComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specific text, code or reference for finding or diagnosis, which may include ruled-out or resolved conditions.
         */
        @Child(name = "item", type = {CodeableConcept.class, Condition.class, Observation.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What was found", formalDefinition="Specific text, code or reference for finding or diagnosis, which may include ruled-out or resolved conditions." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
        protected Type item;

        /**
         * Which investigations support finding or diagnosis.
         */
        @Child(name = "basis", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Which investigations support finding", formalDefinition="Which investigations support finding or diagnosis." )
        protected StringType basis;

        private static final long serialVersionUID = 1690728236L;

    /**
     * Constructor
     */
      public ClinicalImpressionFindingComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ClinicalImpressionFindingComponent(Type item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (Specific text, code or reference for finding or diagnosis, which may include ruled-out or resolved conditions.)
         */
        public Type getItem() { 
          return this.item;
        }

        /**
         * @return {@link #item} (Specific text, code or reference for finding or diagnosis, which may include ruled-out or resolved conditions.)
         */
        public CodeableConcept getItemCodeableConcept() throws FHIRException { 
          if (!(this.item instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.item.getClass().getName()+" was encountered");
          return (CodeableConcept) this.item;
        }

        public boolean hasItemCodeableConcept() { 
          return this.item instanceof CodeableConcept;
        }

        /**
         * @return {@link #item} (Specific text, code or reference for finding or diagnosis, which may include ruled-out or resolved conditions.)
         */
        public Reference getItemReference() throws FHIRException { 
          if (!(this.item instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.item.getClass().getName()+" was encountered");
          return (Reference) this.item;
        }

        public boolean hasItemReference() { 
          return this.item instanceof Reference;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (Specific text, code or reference for finding or diagnosis, which may include ruled-out or resolved conditions.)
         */
        public ClinicalImpressionFindingComponent setItem(Type value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #basis} (Which investigations support finding or diagnosis.). This is the underlying object with id, value and extensions. The accessor "getBasis" gives direct access to the value
         */
        public StringType getBasisElement() { 
          if (this.basis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClinicalImpressionFindingComponent.basis");
            else if (Configuration.doAutoCreate())
              this.basis = new StringType(); // bb
          return this.basis;
        }

        public boolean hasBasisElement() { 
          return this.basis != null && !this.basis.isEmpty();
        }

        public boolean hasBasis() { 
          return this.basis != null && !this.basis.isEmpty();
        }

        /**
         * @param value {@link #basis} (Which investigations support finding or diagnosis.). This is the underlying object with id, value and extensions. The accessor "getBasis" gives direct access to the value
         */
        public ClinicalImpressionFindingComponent setBasisElement(StringType value) { 
          this.basis = value;
          return this;
        }

        /**
         * @return Which investigations support finding or diagnosis.
         */
        public String getBasis() { 
          return this.basis == null ? null : this.basis.getValue();
        }

        /**
         * @param value Which investigations support finding or diagnosis.
         */
        public ClinicalImpressionFindingComponent setBasis(String value) { 
          if (Utilities.noString(value))
            this.basis = null;
          else {
            if (this.basis == null)
              this.basis = new StringType();
            this.basis.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("item[x]", "CodeableConcept|Reference(Condition|Observation)", "Specific text, code or reference for finding or diagnosis, which may include ruled-out or resolved conditions.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("basis", "string", "Which investigations support finding or diagnosis.", 0, java.lang.Integer.MAX_VALUE, basis));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return this.item == null ? new Base[0] : new Base[] {this.item}; // Type
        case 93508670: /*basis*/ return this.basis == null ? new Base[0] : new Base[] {this.basis}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3242771: // item
          this.item = castToType(value); // Type
          return value;
        case 93508670: // basis
          this.basis = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("item[x]")) {
          this.item = castToType(value); // Type
        } else if (name.equals("basis")) {
          this.basis = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 2116201613:  return getItem(); 
        case 3242771:  return getItem(); 
        case 93508670:  return getBasisElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3242771: /*item*/ return new String[] {"CodeableConcept", "Reference"};
        case 93508670: /*basis*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("itemCodeableConcept")) {
          this.item = new CodeableConcept();
          return this.item;
        }
        else if (name.equals("itemReference")) {
          this.item = new Reference();
          return this.item;
        }
        else if (name.equals("basis")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClinicalImpression.basis");
        }
        else
          return super.addChild(name);
      }

      public ClinicalImpressionFindingComponent copy() {
        ClinicalImpressionFindingComponent dst = new ClinicalImpressionFindingComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.basis = basis == null ? null : basis.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ClinicalImpressionFindingComponent))
          return false;
        ClinicalImpressionFindingComponent o = (ClinicalImpressionFindingComponent) other;
        return compareDeep(item, o.item, true) && compareDeep(basis, o.basis, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ClinicalImpressionFindingComponent))
          return false;
        ClinicalImpressionFindingComponent o = (ClinicalImpressionFindingComponent) other;
        return compareValues(basis, o.basis, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(item, basis);
      }

  public String fhirType() {
    return "ClinicalImpression.finding";

  }

  }

    /**
     * A unique identifier assigned to the clinical impression that remains consistent regardless of what server the impression is stored on.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="A unique identifier assigned to the clinical impression that remains consistent regardless of what server the impression is stored on." )
    protected List<Identifier> identifier;

    /**
     * Identifies the workflow status of the assessment.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | completed | entered-in-error", formalDefinition="Identifies the workflow status of the assessment." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-impression-status")
    protected Enumeration<ClinicalImpressionStatus> status;

    /**
     * Categorizes the type of clinical assessment performed.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Kind of assessment performed", formalDefinition="Categorizes the type of clinical assessment performed." )
    protected CodeableConcept code;

    /**
     * A summary of the context and/or cause of the assessment - why / where was it performed, and what patient events/status prompted it.
     */
    @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Why/how the assessment was performed", formalDefinition="A summary of the context and/or cause of the assessment - why / where was it performed, and what patient events/status prompted it." )
    protected StringType description;

    /**
     * The patient or group of individuals assessed as part of this record.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Patient or group assessed", formalDefinition="The patient or group of individuals assessed as part of this record." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient or group of individuals assessed as part of this record.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter or episode of care this impression was created as part of.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or Episode created from", formalDefinition="The encounter or episode of care this impression was created as part of." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter or episode of care this impression was created as part of.)
     */
    protected Resource contextTarget;

    /**
     * The point in time or period over which the subject was assessed.
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time of assessment", formalDefinition="The point in time or period over which the subject was assessed." )
    protected Type effective;

    /**
     * Indicates when the documentation of the assessment was complete.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the assessment was documented", formalDefinition="Indicates when the documentation of the assessment was complete." )
    protected DateTimeType date;

    /**
     * The clinician performing the assessment.
     */
    @Child(name = "assessor", type = {Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The clinician performing the assessment", formalDefinition="The clinician performing the assessment." )
    protected Reference assessor;

    /**
     * The actual object that is the target of the reference (The clinician performing the assessment.)
     */
    protected Practitioner assessorTarget;

    /**
     * A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.
     */
    @Child(name = "previous", type = {ClinicalImpression.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reference to last assessment", formalDefinition="A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes." )
    protected Reference previous;

    /**
     * The actual object that is the target of the reference (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    protected ClinicalImpression previousTarget;

    /**
     * This a list of the relevant problems/conditions for a patient.
     */
    @Child(name = "problem", type = {Condition.class, AllergyIntolerance.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Relevant impressions of patient state", formalDefinition="This a list of the relevant problems/conditions for a patient." )
    protected List<Reference> problem;
    /**
     * The actual objects that are the target of the reference (This a list of the relevant problems/conditions for a patient.)
     */
    protected List<Resource> problemTarget;


    /**
     * One or more sets of investigations (signs, symptions, etc.). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes.
     */
    @Child(name = "investigation", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="One or more sets of investigations (signs, symptions, etc.)", formalDefinition="One or more sets of investigations (signs, symptions, etc.). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes." )
    protected List<ClinicalImpressionInvestigationComponent> investigation;

    /**
     * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.
     */
    @Child(name = "protocol", type = {UriType.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Clinical Protocol followed", formalDefinition="Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis." )
    protected List<UriType> protocol;

    /**
     * A text summary of the investigations and the diagnosis.
     */
    @Child(name = "summary", type = {StringType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Summary of the assessment", formalDefinition="A text summary of the investigations and the diagnosis." )
    protected StringType summary;

    /**
     * Specific findings or diagnoses that was considered likely or relevant to ongoing treatment.
     */
    @Child(name = "finding", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Possible or likely findings and diagnoses", formalDefinition="Specific findings or diagnoses that was considered likely or relevant to ongoing treatment." )
    protected List<ClinicalImpressionFindingComponent> finding;

    /**
     * Estimate of likely outcome.
     */
    @Child(name = "prognosisCodeableConcept", type = {CodeableConcept.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Estimate of likely outcome", formalDefinition="Estimate of likely outcome." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinicalimpression-prognosis")
    protected List<CodeableConcept> prognosisCodeableConcept;

    /**
     * RiskAssessment expressing likely outcome.
     */
    @Child(name = "prognosisReference", type = {RiskAssessment.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="RiskAssessment expressing likely outcome", formalDefinition="RiskAssessment expressing likely outcome." )
    protected List<Reference> prognosisReference;
    /**
     * The actual objects that are the target of the reference (RiskAssessment expressing likely outcome.)
     */
    protected List<RiskAssessment> prognosisReferenceTarget;


    /**
     * Action taken as part of assessment procedure.
     */
    @Child(name = "action", type = {ReferralRequest.class, ProcedureRequest.class, Procedure.class, MedicationRequest.class, Appointment.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Action taken as part of assessment procedure", formalDefinition="Action taken as part of assessment procedure." )
    protected List<Reference> action;
    /**
     * The actual objects that are the target of the reference (Action taken as part of assessment procedure.)
     */
    protected List<Resource> actionTarget;


    /**
     * Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by the original author could also appear.
     */
    @Child(name = "note", type = {Annotation.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the ClinicalImpression", formalDefinition="Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by the original author could also appear." )
    protected List<Annotation> note;

    private static final long serialVersionUID = -1626670747L;

  /**
   * Constructor
   */
    public ClinicalImpression() {
      super();
    }

  /**
   * Constructor
   */
    public ClinicalImpression(Enumeration<ClinicalImpressionStatus> status, Reference subject) {
      super();
      this.status = status;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to the clinical impression that remains consistent regardless of what server the impression is stored on.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setIdentifier(List<Identifier> theIdentifier) { 
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

    public ClinicalImpression addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (Identifies the workflow status of the assessment.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ClinicalImpressionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ClinicalImpressionStatus>(new ClinicalImpressionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Identifies the workflow status of the assessment.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ClinicalImpression setStatusElement(Enumeration<ClinicalImpressionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Identifies the workflow status of the assessment.
     */
    public ClinicalImpressionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Identifies the workflow status of the assessment.
     */
    public ClinicalImpression setStatus(ClinicalImpressionStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ClinicalImpressionStatus>(new ClinicalImpressionStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #code} (Categorizes the type of clinical assessment performed.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Categorizes the type of clinical assessment performed.)
     */
    public ClinicalImpression setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #description} (A summary of the context and/or cause of the assessment - why / where was it performed, and what patient events/status prompted it.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.description");
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
     * @param value {@link #description} (A summary of the context and/or cause of the assessment - why / where was it performed, and what patient events/status prompted it.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ClinicalImpression setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A summary of the context and/or cause of the assessment - why / where was it performed, and what patient events/status prompted it.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A summary of the context and/or cause of the assessment - why / where was it performed, and what patient events/status prompted it.
     */
    public ClinicalImpression setDescription(String value) { 
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
     * @return {@link #subject} (The patient or group of individuals assessed as part of this record.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient or group of individuals assessed as part of this record.)
     */
    public ClinicalImpression setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient or group of individuals assessed as part of this record.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient or group of individuals assessed as part of this record.)
     */
    public ClinicalImpression setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The encounter or episode of care this impression was created as part of.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter or episode of care this impression was created as part of.)
     */
    public ClinicalImpression setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter or episode of care this impression was created as part of.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter or episode of care this impression was created as part of.)
     */
    public ClinicalImpression setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #effective} (The point in time or period over which the subject was assessed.)
     */
    public Type getEffective() { 
      return this.effective;
    }

    /**
     * @return {@link #effective} (The point in time or period over which the subject was assessed.)
     */
    public DateTimeType getEffectiveDateTimeType() throws FHIRException { 
      if (!(this.effective instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (DateTimeType) this.effective;
    }

    public boolean hasEffectiveDateTimeType() { 
      return this.effective instanceof DateTimeType;
    }

    /**
     * @return {@link #effective} (The point in time or period over which the subject was assessed.)
     */
    public Period getEffectivePeriod() throws FHIRException { 
      if (!(this.effective instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (Period) this.effective;
    }

    public boolean hasEffectivePeriod() { 
      return this.effective instanceof Period;
    }

    public boolean hasEffective() { 
      return this.effective != null && !this.effective.isEmpty();
    }

    /**
     * @param value {@link #effective} (The point in time or period over which the subject was assessed.)
     */
    public ClinicalImpression setEffective(Type value) { 
      this.effective = value;
      return this;
    }

    /**
     * @return {@link #date} (Indicates when the documentation of the assessment was complete.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.date");
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
     * @param value {@link #date} (Indicates when the documentation of the assessment was complete.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ClinicalImpression setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return Indicates when the documentation of the assessment was complete.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value Indicates when the documentation of the assessment was complete.
     */
    public ClinicalImpression setDate(Date value) { 
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
     * @return {@link #assessor} (The clinician performing the assessment.)
     */
    public Reference getAssessor() { 
      if (this.assessor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.assessor");
        else if (Configuration.doAutoCreate())
          this.assessor = new Reference(); // cc
      return this.assessor;
    }

    public boolean hasAssessor() { 
      return this.assessor != null && !this.assessor.isEmpty();
    }

    /**
     * @param value {@link #assessor} (The clinician performing the assessment.)
     */
    public ClinicalImpression setAssessor(Reference value) { 
      this.assessor = value;
      return this;
    }

    /**
     * @return {@link #assessor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The clinician performing the assessment.)
     */
    public Practitioner getAssessorTarget() { 
      if (this.assessorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.assessor");
        else if (Configuration.doAutoCreate())
          this.assessorTarget = new Practitioner(); // aa
      return this.assessorTarget;
    }

    /**
     * @param value {@link #assessor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The clinician performing the assessment.)
     */
    public ClinicalImpression setAssessorTarget(Practitioner value) { 
      this.assessorTarget = value;
      return this;
    }

    /**
     * @return {@link #previous} (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public Reference getPrevious() { 
      if (this.previous == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.previous");
        else if (Configuration.doAutoCreate())
          this.previous = new Reference(); // cc
      return this.previous;
    }

    public boolean hasPrevious() { 
      return this.previous != null && !this.previous.isEmpty();
    }

    /**
     * @param value {@link #previous} (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public ClinicalImpression setPrevious(Reference value) { 
      this.previous = value;
      return this;
    }

    /**
     * @return {@link #previous} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public ClinicalImpression getPreviousTarget() { 
      if (this.previousTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.previous");
        else if (Configuration.doAutoCreate())
          this.previousTarget = new ClinicalImpression(); // aa
      return this.previousTarget;
    }

    /**
     * @param value {@link #previous} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public ClinicalImpression setPreviousTarget(ClinicalImpression value) { 
      this.previousTarget = value;
      return this;
    }

    /**
     * @return {@link #problem} (This a list of the relevant problems/conditions for a patient.)
     */
    public List<Reference> getProblem() { 
      if (this.problem == null)
        this.problem = new ArrayList<Reference>();
      return this.problem;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setProblem(List<Reference> theProblem) { 
      this.problem = theProblem;
      return this;
    }

    public boolean hasProblem() { 
      if (this.problem == null)
        return false;
      for (Reference item : this.problem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addProblem() { //3
      Reference t = new Reference();
      if (this.problem == null)
        this.problem = new ArrayList<Reference>();
      this.problem.add(t);
      return t;
    }

    public ClinicalImpression addProblem(Reference t) { //3
      if (t == null)
        return this;
      if (this.problem == null)
        this.problem = new ArrayList<Reference>();
      this.problem.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #problem}, creating it if it does not already exist
     */
    public Reference getProblemFirstRep() { 
      if (getProblem().isEmpty()) {
        addProblem();
      }
      return getProblem().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getProblemTarget() { 
      if (this.problemTarget == null)
        this.problemTarget = new ArrayList<Resource>();
      return this.problemTarget;
    }

    /**
     * @return {@link #investigation} (One or more sets of investigations (signs, symptions, etc.). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes.)
     */
    public List<ClinicalImpressionInvestigationComponent> getInvestigation() { 
      if (this.investigation == null)
        this.investigation = new ArrayList<ClinicalImpressionInvestigationComponent>();
      return this.investigation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setInvestigation(List<ClinicalImpressionInvestigationComponent> theInvestigation) { 
      this.investigation = theInvestigation;
      return this;
    }

    public boolean hasInvestigation() { 
      if (this.investigation == null)
        return false;
      for (ClinicalImpressionInvestigationComponent item : this.investigation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ClinicalImpressionInvestigationComponent addInvestigation() { //3
      ClinicalImpressionInvestigationComponent t = new ClinicalImpressionInvestigationComponent();
      if (this.investigation == null)
        this.investigation = new ArrayList<ClinicalImpressionInvestigationComponent>();
      this.investigation.add(t);
      return t;
    }

    public ClinicalImpression addInvestigation(ClinicalImpressionInvestigationComponent t) { //3
      if (t == null)
        return this;
      if (this.investigation == null)
        this.investigation = new ArrayList<ClinicalImpressionInvestigationComponent>();
      this.investigation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #investigation}, creating it if it does not already exist
     */
    public ClinicalImpressionInvestigationComponent getInvestigationFirstRep() { 
      if (getInvestigation().isEmpty()) {
        addInvestigation();
      }
      return getInvestigation().get(0);
    }

    /**
     * @return {@link #protocol} (Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.)
     */
    public List<UriType> getProtocol() { 
      if (this.protocol == null)
        this.protocol = new ArrayList<UriType>();
      return this.protocol;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setProtocol(List<UriType> theProtocol) { 
      this.protocol = theProtocol;
      return this;
    }

    public boolean hasProtocol() { 
      if (this.protocol == null)
        return false;
      for (UriType item : this.protocol)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #protocol} (Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.)
     */
    public UriType addProtocolElement() {//2 
      UriType t = new UriType();
      if (this.protocol == null)
        this.protocol = new ArrayList<UriType>();
      this.protocol.add(t);
      return t;
    }

    /**
     * @param value {@link #protocol} (Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.)
     */
    public ClinicalImpression addProtocol(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.protocol == null)
        this.protocol = new ArrayList<UriType>();
      this.protocol.add(t);
      return this;
    }

    /**
     * @param value {@link #protocol} (Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.)
     */
    public boolean hasProtocol(String value) { 
      if (this.protocol == null)
        return false;
      for (UriType v : this.protocol)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #summary} (A text summary of the investigations and the diagnosis.). This is the underlying object with id, value and extensions. The accessor "getSummary" gives direct access to the value
     */
    public StringType getSummaryElement() { 
      if (this.summary == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalImpression.summary");
        else if (Configuration.doAutoCreate())
          this.summary = new StringType(); // bb
      return this.summary;
    }

    public boolean hasSummaryElement() { 
      return this.summary != null && !this.summary.isEmpty();
    }

    public boolean hasSummary() { 
      return this.summary != null && !this.summary.isEmpty();
    }

    /**
     * @param value {@link #summary} (A text summary of the investigations and the diagnosis.). This is the underlying object with id, value and extensions. The accessor "getSummary" gives direct access to the value
     */
    public ClinicalImpression setSummaryElement(StringType value) { 
      this.summary = value;
      return this;
    }

    /**
     * @return A text summary of the investigations and the diagnosis.
     */
    public String getSummary() { 
      return this.summary == null ? null : this.summary.getValue();
    }

    /**
     * @param value A text summary of the investigations and the diagnosis.
     */
    public ClinicalImpression setSummary(String value) { 
      if (Utilities.noString(value))
        this.summary = null;
      else {
        if (this.summary == null)
          this.summary = new StringType();
        this.summary.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #finding} (Specific findings or diagnoses that was considered likely or relevant to ongoing treatment.)
     */
    public List<ClinicalImpressionFindingComponent> getFinding() { 
      if (this.finding == null)
        this.finding = new ArrayList<ClinicalImpressionFindingComponent>();
      return this.finding;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setFinding(List<ClinicalImpressionFindingComponent> theFinding) { 
      this.finding = theFinding;
      return this;
    }

    public boolean hasFinding() { 
      if (this.finding == null)
        return false;
      for (ClinicalImpressionFindingComponent item : this.finding)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ClinicalImpressionFindingComponent addFinding() { //3
      ClinicalImpressionFindingComponent t = new ClinicalImpressionFindingComponent();
      if (this.finding == null)
        this.finding = new ArrayList<ClinicalImpressionFindingComponent>();
      this.finding.add(t);
      return t;
    }

    public ClinicalImpression addFinding(ClinicalImpressionFindingComponent t) { //3
      if (t == null)
        return this;
      if (this.finding == null)
        this.finding = new ArrayList<ClinicalImpressionFindingComponent>();
      this.finding.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #finding}, creating it if it does not already exist
     */
    public ClinicalImpressionFindingComponent getFindingFirstRep() { 
      if (getFinding().isEmpty()) {
        addFinding();
      }
      return getFinding().get(0);
    }

    /**
     * @return {@link #prognosisCodeableConcept} (Estimate of likely outcome.)
     */
    public List<CodeableConcept> getPrognosisCodeableConcept() { 
      if (this.prognosisCodeableConcept == null)
        this.prognosisCodeableConcept = new ArrayList<CodeableConcept>();
      return this.prognosisCodeableConcept;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setPrognosisCodeableConcept(List<CodeableConcept> thePrognosisCodeableConcept) { 
      this.prognosisCodeableConcept = thePrognosisCodeableConcept;
      return this;
    }

    public boolean hasPrognosisCodeableConcept() { 
      if (this.prognosisCodeableConcept == null)
        return false;
      for (CodeableConcept item : this.prognosisCodeableConcept)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addPrognosisCodeableConcept() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.prognosisCodeableConcept == null)
        this.prognosisCodeableConcept = new ArrayList<CodeableConcept>();
      this.prognosisCodeableConcept.add(t);
      return t;
    }

    public ClinicalImpression addPrognosisCodeableConcept(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.prognosisCodeableConcept == null)
        this.prognosisCodeableConcept = new ArrayList<CodeableConcept>();
      this.prognosisCodeableConcept.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #prognosisCodeableConcept}, creating it if it does not already exist
     */
    public CodeableConcept getPrognosisCodeableConceptFirstRep() { 
      if (getPrognosisCodeableConcept().isEmpty()) {
        addPrognosisCodeableConcept();
      }
      return getPrognosisCodeableConcept().get(0);
    }

    /**
     * @return {@link #prognosisReference} (RiskAssessment expressing likely outcome.)
     */
    public List<Reference> getPrognosisReference() { 
      if (this.prognosisReference == null)
        this.prognosisReference = new ArrayList<Reference>();
      return this.prognosisReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setPrognosisReference(List<Reference> thePrognosisReference) { 
      this.prognosisReference = thePrognosisReference;
      return this;
    }

    public boolean hasPrognosisReference() { 
      if (this.prognosisReference == null)
        return false;
      for (Reference item : this.prognosisReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPrognosisReference() { //3
      Reference t = new Reference();
      if (this.prognosisReference == null)
        this.prognosisReference = new ArrayList<Reference>();
      this.prognosisReference.add(t);
      return t;
    }

    public ClinicalImpression addPrognosisReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.prognosisReference == null)
        this.prognosisReference = new ArrayList<Reference>();
      this.prognosisReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #prognosisReference}, creating it if it does not already exist
     */
    public Reference getPrognosisReferenceFirstRep() { 
      if (getPrognosisReference().isEmpty()) {
        addPrognosisReference();
      }
      return getPrognosisReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<RiskAssessment> getPrognosisReferenceTarget() { 
      if (this.prognosisReferenceTarget == null)
        this.prognosisReferenceTarget = new ArrayList<RiskAssessment>();
      return this.prognosisReferenceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public RiskAssessment addPrognosisReferenceTarget() { 
      RiskAssessment r = new RiskAssessment();
      if (this.prognosisReferenceTarget == null)
        this.prognosisReferenceTarget = new ArrayList<RiskAssessment>();
      this.prognosisReferenceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #action} (Action taken as part of assessment procedure.)
     */
    public List<Reference> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<Reference>();
      return this.action;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setAction(List<Reference> theAction) { 
      this.action = theAction;
      return this;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (Reference item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAction() { //3
      Reference t = new Reference();
      if (this.action == null)
        this.action = new ArrayList<Reference>();
      this.action.add(t);
      return t;
    }

    public ClinicalImpression addAction(Reference t) { //3
      if (t == null)
        return this;
      if (this.action == null)
        this.action = new ArrayList<Reference>();
      this.action.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
     */
    public Reference getActionFirstRep() { 
      if (getAction().isEmpty()) {
        addAction();
      }
      return getAction().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getActionTarget() { 
      if (this.actionTarget == null)
        this.actionTarget = new ArrayList<Resource>();
      return this.actionTarget;
    }

    /**
     * @return {@link #note} (Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by the original author could also appear.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ClinicalImpression setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public ClinicalImpression addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier assigned to the clinical impression that remains consistent regardless of what server the impression is stored on.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Identifies the workflow status of the assessment.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("code", "CodeableConcept", "Categorizes the type of clinical assessment performed.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("description", "string", "A summary of the context and/or cause of the assessment - why / where was it performed, and what patient events/status prompted it.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The patient or group of individuals assessed as part of this record.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care this impression was created as part of.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "The point in time or period over which the subject was assessed.", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("date", "dateTime", "Indicates when the documentation of the assessment was complete.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("assessor", "Reference(Practitioner)", "The clinician performing the assessment.", 0, java.lang.Integer.MAX_VALUE, assessor));
        childrenList.add(new Property("previous", "Reference(ClinicalImpression)", "A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.", 0, java.lang.Integer.MAX_VALUE, previous));
        childrenList.add(new Property("problem", "Reference(Condition|AllergyIntolerance)", "This a list of the relevant problems/conditions for a patient.", 0, java.lang.Integer.MAX_VALUE, problem));
        childrenList.add(new Property("investigation", "", "One or more sets of investigations (signs, symptions, etc.). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes.", 0, java.lang.Integer.MAX_VALUE, investigation));
        childrenList.add(new Property("protocol", "uri", "Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.", 0, java.lang.Integer.MAX_VALUE, protocol));
        childrenList.add(new Property("summary", "string", "A text summary of the investigations and the diagnosis.", 0, java.lang.Integer.MAX_VALUE, summary));
        childrenList.add(new Property("finding", "", "Specific findings or diagnoses that was considered likely or relevant to ongoing treatment.", 0, java.lang.Integer.MAX_VALUE, finding));
        childrenList.add(new Property("prognosisCodeableConcept", "CodeableConcept", "Estimate of likely outcome.", 0, java.lang.Integer.MAX_VALUE, prognosisCodeableConcept));
        childrenList.add(new Property("prognosisReference", "Reference(RiskAssessment)", "RiskAssessment expressing likely outcome.", 0, java.lang.Integer.MAX_VALUE, prognosisReference));
        childrenList.add(new Property("action", "Reference(ReferralRequest|ProcedureRequest|Procedure|MedicationRequest|Appointment)", "Action taken as part of assessment procedure.", 0, java.lang.Integer.MAX_VALUE, action));
        childrenList.add(new Property("note", "Annotation", "Commentary about the impression, typically recorded after the impression itself was made, though supplemental notes by the original author could also appear.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ClinicalImpressionStatus>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -373213113: /*assessor*/ return this.assessor == null ? new Base[0] : new Base[] {this.assessor}; // Reference
        case -1273775369: /*previous*/ return this.previous == null ? new Base[0] : new Base[] {this.previous}; // Reference
        case -309542241: /*problem*/ return this.problem == null ? new Base[0] : this.problem.toArray(new Base[this.problem.size()]); // Reference
        case 956015362: /*investigation*/ return this.investigation == null ? new Base[0] : this.investigation.toArray(new Base[this.investigation.size()]); // ClinicalImpressionInvestigationComponent
        case -989163880: /*protocol*/ return this.protocol == null ? new Base[0] : this.protocol.toArray(new Base[this.protocol.size()]); // UriType
        case -1857640538: /*summary*/ return this.summary == null ? new Base[0] : new Base[] {this.summary}; // StringType
        case -853173367: /*finding*/ return this.finding == null ? new Base[0] : this.finding.toArray(new Base[this.finding.size()]); // ClinicalImpressionFindingComponent
        case -676337953: /*prognosisCodeableConcept*/ return this.prognosisCodeableConcept == null ? new Base[0] : this.prognosisCodeableConcept.toArray(new Base[this.prognosisCodeableConcept.size()]); // CodeableConcept
        case -587137783: /*prognosisReference*/ return this.prognosisReference == null ? new Base[0] : this.prognosisReference.toArray(new Base[this.prognosisReference.size()]); // Reference
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
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
          value = new ClinicalImpressionStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ClinicalImpressionStatus>
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -373213113: // assessor
          this.assessor = castToReference(value); // Reference
          return value;
        case -1273775369: // previous
          this.previous = castToReference(value); // Reference
          return value;
        case -309542241: // problem
          this.getProblem().add(castToReference(value)); // Reference
          return value;
        case 956015362: // investigation
          this.getInvestigation().add((ClinicalImpressionInvestigationComponent) value); // ClinicalImpressionInvestigationComponent
          return value;
        case -989163880: // protocol
          this.getProtocol().add(castToUri(value)); // UriType
          return value;
        case -1857640538: // summary
          this.summary = castToString(value); // StringType
          return value;
        case -853173367: // finding
          this.getFinding().add((ClinicalImpressionFindingComponent) value); // ClinicalImpressionFindingComponent
          return value;
        case -676337953: // prognosisCodeableConcept
          this.getPrognosisCodeableConcept().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -587137783: // prognosisReference
          this.getPrognosisReference().add(castToReference(value)); // Reference
          return value;
        case -1422950858: // action
          this.getAction().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ClinicalImpressionStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ClinicalImpressionStatus>
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("effective[x]")) {
          this.effective = castToType(value); // Type
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("assessor")) {
          this.assessor = castToReference(value); // Reference
        } else if (name.equals("previous")) {
          this.previous = castToReference(value); // Reference
        } else if (name.equals("problem")) {
          this.getProblem().add(castToReference(value));
        } else if (name.equals("investigation")) {
          this.getInvestigation().add((ClinicalImpressionInvestigationComponent) value);
        } else if (name.equals("protocol")) {
          this.getProtocol().add(castToUri(value));
        } else if (name.equals("summary")) {
          this.summary = castToString(value); // StringType
        } else if (name.equals("finding")) {
          this.getFinding().add((ClinicalImpressionFindingComponent) value);
        } else if (name.equals("prognosisCodeableConcept")) {
          this.getPrognosisCodeableConcept().add(castToCodeableConcept(value));
        } else if (name.equals("prognosisReference")) {
          this.getPrognosisReference().add(castToReference(value));
        } else if (name.equals("action")) {
          this.getAction().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 3059181:  return getCode(); 
        case -1724546052:  return getDescriptionElement();
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case 247104889:  return getEffective(); 
        case -1468651097:  return getEffective(); 
        case 3076014:  return getDateElement();
        case -373213113:  return getAssessor(); 
        case -1273775369:  return getPrevious(); 
        case -309542241:  return addProblem(); 
        case 956015362:  return addInvestigation(); 
        case -989163880:  return addProtocolElement();
        case -1857640538:  return getSummaryElement();
        case -853173367:  return addFinding(); 
        case -676337953:  return addPrognosisCodeableConcept(); 
        case -587137783:  return addPrognosisReference(); 
        case -1422950858:  return addAction(); 
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -1468651097: /*effective*/ return new String[] {"dateTime", "Period"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -373213113: /*assessor*/ return new String[] {"Reference"};
        case -1273775369: /*previous*/ return new String[] {"Reference"};
        case -309542241: /*problem*/ return new String[] {"Reference"};
        case 956015362: /*investigation*/ return new String[] {};
        case -989163880: /*protocol*/ return new String[] {"uri"};
        case -1857640538: /*summary*/ return new String[] {"string"};
        case -853173367: /*finding*/ return new String[] {};
        case -676337953: /*prognosisCodeableConcept*/ return new String[] {"CodeableConcept"};
        case -587137783: /*prognosisReference*/ return new String[] {"Reference"};
        case -1422950858: /*action*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClinicalImpression.status");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClinicalImpression.description");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClinicalImpression.date");
        }
        else if (name.equals("assessor")) {
          this.assessor = new Reference();
          return this.assessor;
        }
        else if (name.equals("previous")) {
          this.previous = new Reference();
          return this.previous;
        }
        else if (name.equals("problem")) {
          return addProblem();
        }
        else if (name.equals("investigation")) {
          return addInvestigation();
        }
        else if (name.equals("protocol")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClinicalImpression.protocol");
        }
        else if (name.equals("summary")) {
          throw new FHIRException("Cannot call addChild on a primitive type ClinicalImpression.summary");
        }
        else if (name.equals("finding")) {
          return addFinding();
        }
        else if (name.equals("prognosisCodeableConcept")) {
          return addPrognosisCodeableConcept();
        }
        else if (name.equals("prognosisReference")) {
          return addPrognosisReference();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ClinicalImpression";

  }

      public ClinicalImpression copy() {
        ClinicalImpression dst = new ClinicalImpression();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.code = code == null ? null : code.copy();
        dst.description = description == null ? null : description.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.date = date == null ? null : date.copy();
        dst.assessor = assessor == null ? null : assessor.copy();
        dst.previous = previous == null ? null : previous.copy();
        if (problem != null) {
          dst.problem = new ArrayList<Reference>();
          for (Reference i : problem)
            dst.problem.add(i.copy());
        };
        if (investigation != null) {
          dst.investigation = new ArrayList<ClinicalImpressionInvestigationComponent>();
          for (ClinicalImpressionInvestigationComponent i : investigation)
            dst.investigation.add(i.copy());
        };
        if (protocol != null) {
          dst.protocol = new ArrayList<UriType>();
          for (UriType i : protocol)
            dst.protocol.add(i.copy());
        };
        dst.summary = summary == null ? null : summary.copy();
        if (finding != null) {
          dst.finding = new ArrayList<ClinicalImpressionFindingComponent>();
          for (ClinicalImpressionFindingComponent i : finding)
            dst.finding.add(i.copy());
        };
        if (prognosisCodeableConcept != null) {
          dst.prognosisCodeableConcept = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : prognosisCodeableConcept)
            dst.prognosisCodeableConcept.add(i.copy());
        };
        if (prognosisReference != null) {
          dst.prognosisReference = new ArrayList<Reference>();
          for (Reference i : prognosisReference)
            dst.prognosisReference.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<Reference>();
          for (Reference i : action)
            dst.action.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      protected ClinicalImpression typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ClinicalImpression))
          return false;
        ClinicalImpression o = (ClinicalImpression) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(code, o.code, true)
           && compareDeep(description, o.description, true) && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true)
           && compareDeep(effective, o.effective, true) && compareDeep(date, o.date, true) && compareDeep(assessor, o.assessor, true)
           && compareDeep(previous, o.previous, true) && compareDeep(problem, o.problem, true) && compareDeep(investigation, o.investigation, true)
           && compareDeep(protocol, o.protocol, true) && compareDeep(summary, o.summary, true) && compareDeep(finding, o.finding, true)
           && compareDeep(prognosisCodeableConcept, o.prognosisCodeableConcept, true) && compareDeep(prognosisReference, o.prognosisReference, true)
           && compareDeep(action, o.action, true) && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ClinicalImpression))
          return false;
        ClinicalImpression o = (ClinicalImpression) other;
        return compareValues(status, o.status, true) && compareValues(description, o.description, true) && compareValues(date, o.date, true)
           && compareValues(protocol, o.protocol, true) && compareValues(summary, o.summary, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, code
          , description, subject, context, effective, date, assessor, previous, problem
          , investigation, protocol, summary, finding, prognosisCodeableConcept, prognosisReference
          , action, note);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ClinicalImpression;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the assessment was documented</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClinicalImpression.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ClinicalImpression.date", description="When the assessment was documented", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the assessment was documented</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ClinicalImpression.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClinicalImpression.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ClinicalImpression.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClinicalImpression.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>previous</b>
   * <p>
   * Description: <b>Reference to last assessment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.previous</b><br>
   * </p>
   */
  @SearchParamDefinition(name="previous", path="ClinicalImpression.previous", description="Reference to last assessment", type="reference", target={ClinicalImpression.class } )
  public static final String SP_PREVIOUS = "previous";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>previous</b>
   * <p>
   * Description: <b>Reference to last assessment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.previous</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PREVIOUS = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PREVIOUS);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:previous</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PREVIOUS = new ca.uhn.fhir.model.api.Include("ClinicalImpression:previous").toLocked();

 /**
   * Search parameter: <b>finding-code</b>
   * <p>
   * Description: <b>What was found</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClinicalImpression.finding.item[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="finding-code", path="ClinicalImpression.finding.item.as(CodeableConcept)", description="What was found", type="token" )
  public static final String SP_FINDING_CODE = "finding-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>finding-code</b>
   * <p>
   * Description: <b>What was found</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClinicalImpression.finding.item[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FINDING_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FINDING_CODE);

 /**
   * Search parameter: <b>assessor</b>
   * <p>
   * Description: <b>The clinician performing the assessment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.assessor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="assessor", path="ClinicalImpression.assessor", description="The clinician performing the assessment", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_ASSESSOR = "assessor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>assessor</b>
   * <p>
   * Description: <b>The clinician performing the assessment</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.assessor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ASSESSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ASSESSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:assessor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ASSESSOR = new ca.uhn.fhir.model.api.Include("ClinicalImpression:assessor").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Patient or group assessed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="ClinicalImpression.subject", description="Patient or group assessed", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Patient or group assessed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ClinicalImpression:subject").toLocked();

 /**
   * Search parameter: <b>finding-ref</b>
   * <p>
   * Description: <b>What was found</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.finding.item[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="finding-ref", path="ClinicalImpression.finding.item.as(Reference)", description="What was found", type="reference", target={Condition.class, Observation.class } )
  public static final String SP_FINDING_REF = "finding-ref";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>finding-ref</b>
   * <p>
   * Description: <b>What was found</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.finding.item[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FINDING_REF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FINDING_REF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:finding-ref</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FINDING_REF = new ca.uhn.fhir.model.api.Include("ClinicalImpression:finding-ref").toLocked();

 /**
   * Search parameter: <b>problem</b>
   * <p>
   * Description: <b>Relevant impressions of patient state</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.problem</b><br>
   * </p>
   */
  @SearchParamDefinition(name="problem", path="ClinicalImpression.problem", description="Relevant impressions of patient state", type="reference", target={AllergyIntolerance.class, Condition.class } )
  public static final String SP_PROBLEM = "problem";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>problem</b>
   * <p>
   * Description: <b>Relevant impressions of patient state</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.problem</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROBLEM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROBLEM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:problem</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROBLEM = new ca.uhn.fhir.model.api.Include("ClinicalImpression:problem").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Patient or group assessed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ClinicalImpression.subject", description="Patient or group assessed", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Patient or group assessed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ClinicalImpression:patient").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter or Episode created from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ClinicalImpression.context", description="Encounter or Episode created from", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter or Episode created from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("ClinicalImpression:context").toLocked();

 /**
   * Search parameter: <b>investigation</b>
   * <p>
   * Description: <b>Record of a specific investigation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.investigation.item</b><br>
   * </p>
   */
  @SearchParamDefinition(name="investigation", path="ClinicalImpression.investigation.item", description="Record of a specific investigation", type="reference", target={DiagnosticReport.class, FamilyMemberHistory.class, ImagingStudy.class, Observation.class, QuestionnaireResponse.class, RiskAssessment.class } )
  public static final String SP_INVESTIGATION = "investigation";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>investigation</b>
   * <p>
   * Description: <b>Record of a specific investigation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.investigation.item</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INVESTIGATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INVESTIGATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:investigation</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INVESTIGATION = new ca.uhn.fhir.model.api.Include("ClinicalImpression:investigation").toLocked();

 /**
   * Search parameter: <b>action</b>
   * <p>
   * Description: <b>Action taken as part of assessment procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.action</b><br>
   * </p>
   */
  @SearchParamDefinition(name="action", path="ClinicalImpression.action", description="Action taken as part of assessment procedure", type="reference", target={Appointment.class, MedicationRequest.class, Procedure.class, ProcedureRequest.class, ReferralRequest.class } )
  public static final String SP_ACTION = "action";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>action</b>
   * <p>
   * Description: <b>Action taken as part of assessment procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ClinicalImpression.action</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACTION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACTION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ClinicalImpression:action</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACTION = new ca.uhn.fhir.model.api.Include("ClinicalImpression:action").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | completed | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClinicalImpression.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ClinicalImpression.status", description="draft | completed | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | completed | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ClinicalImpression.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

