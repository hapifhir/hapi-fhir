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
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
 */
@ResourceDef(name="ResearchStudy", profile="http://hl7.org/fhir/StructureDefinition/ResearchStudy")
public class ResearchStudy extends DomainResource {

    public enum ResearchStudyStatus {
        /**
         * Study is opened for accrual.
         */
        ACTIVE, 
        /**
         * Study is completed prematurely and will not resume; patients are no longer examined nor treated.
         */
        ADMINISTRATIVELYCOMPLETED, 
        /**
         * Protocol is approved by the review board.
         */
        APPROVED, 
        /**
         * Study is closed for accrual; patients can be examined and treated.
         */
        CLOSEDTOACCRUAL, 
        /**
         * Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment or intervention but are still being followed according to the primary objective of the study.
         */
        CLOSEDTOACCRUALANDINTERVENTION, 
        /**
         * Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment
or intervention but are still being followed according to the primary objective of the study.
         */
        COMPLETED, 
        /**
         * Protocol was disapproved by the review board.
         */
        DISAPPROVED, 
        /**
         * Protocol is submitted to the review board for approval.
         */
        INREVIEW, 
        /**
         * Study is temporarily closed for accrual; can be potentially resumed in the future; patients can be examined and treated.
         */
        TEMPORARILYCLOSEDTOACCRUAL, 
        /**
         * Study is temporarily closed for accrual and intervention and potentially can be resumed in the future.
         */
        TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION, 
        /**
         * Protocol was withdrawn by the lead organization.
         */
        WITHDRAWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ResearchStudyStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("administratively-completed".equals(codeString))
          return ADMINISTRATIVELYCOMPLETED;
        if ("approved".equals(codeString))
          return APPROVED;
        if ("closed-to-accrual".equals(codeString))
          return CLOSEDTOACCRUAL;
        if ("closed-to-accrual-and-intervention".equals(codeString))
          return CLOSEDTOACCRUALANDINTERVENTION;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("disapproved".equals(codeString))
          return DISAPPROVED;
        if ("in-review".equals(codeString))
          return INREVIEW;
        if ("temporarily-closed-to-accrual".equals(codeString))
          return TEMPORARILYCLOSEDTOACCRUAL;
        if ("temporarily-closed-to-accrual-and-intervention".equals(codeString))
          return TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION;
        if ("withdrawn".equals(codeString))
          return WITHDRAWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case ADMINISTRATIVELYCOMPLETED: return "administratively-completed";
            case APPROVED: return "approved";
            case CLOSEDTOACCRUAL: return "closed-to-accrual";
            case CLOSEDTOACCRUALANDINTERVENTION: return "closed-to-accrual-and-intervention";
            case COMPLETED: return "completed";
            case DISAPPROVED: return "disapproved";
            case INREVIEW: return "in-review";
            case TEMPORARILYCLOSEDTOACCRUAL: return "temporarily-closed-to-accrual";
            case TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION: return "temporarily-closed-to-accrual-and-intervention";
            case WITHDRAWN: return "withdrawn";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/research-study-status";
            case ADMINISTRATIVELYCOMPLETED: return "http://hl7.org/fhir/research-study-status";
            case APPROVED: return "http://hl7.org/fhir/research-study-status";
            case CLOSEDTOACCRUAL: return "http://hl7.org/fhir/research-study-status";
            case CLOSEDTOACCRUALANDINTERVENTION: return "http://hl7.org/fhir/research-study-status";
            case COMPLETED: return "http://hl7.org/fhir/research-study-status";
            case DISAPPROVED: return "http://hl7.org/fhir/research-study-status";
            case INREVIEW: return "http://hl7.org/fhir/research-study-status";
            case TEMPORARILYCLOSEDTOACCRUAL: return "http://hl7.org/fhir/research-study-status";
            case TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION: return "http://hl7.org/fhir/research-study-status";
            case WITHDRAWN: return "http://hl7.org/fhir/research-study-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "Study is opened for accrual.";
            case ADMINISTRATIVELYCOMPLETED: return "Study is completed prematurely and will not resume; patients are no longer examined nor treated.";
            case APPROVED: return "Protocol is approved by the review board.";
            case CLOSEDTOACCRUAL: return "Study is closed for accrual; patients can be examined and treated.";
            case CLOSEDTOACCRUALANDINTERVENTION: return "Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment or intervention but are still being followed according to the primary objective of the study.";
            case COMPLETED: return "Study is closed to accrual and intervention, i.e. the study is closed to enrollment, all study subjects have completed treatment\nor intervention but are still being followed according to the primary objective of the study.";
            case DISAPPROVED: return "Protocol was disapproved by the review board.";
            case INREVIEW: return "Protocol is submitted to the review board for approval.";
            case TEMPORARILYCLOSEDTOACCRUAL: return "Study is temporarily closed for accrual; can be potentially resumed in the future; patients can be examined and treated.";
            case TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION: return "Study is temporarily closed for accrual and intervention and potentially can be resumed in the future.";
            case WITHDRAWN: return "Protocol was withdrawn by the lead organization.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case ADMINISTRATIVELYCOMPLETED: return "Administratively Completed";
            case APPROVED: return "Approved";
            case CLOSEDTOACCRUAL: return "Closed to Accrual";
            case CLOSEDTOACCRUALANDINTERVENTION: return "Closed to Accrual and Intervention";
            case COMPLETED: return "Completed";
            case DISAPPROVED: return "Disapproved";
            case INREVIEW: return "In Review";
            case TEMPORARILYCLOSEDTOACCRUAL: return "Temporarily Closed to Accrual";
            case TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION: return "Temporarily Closed to Accrual and Intervention";
            case WITHDRAWN: return "Withdrawn";
            default: return "?";
          }
        }
    }

  public static class ResearchStudyStatusEnumFactory implements EnumFactory<ResearchStudyStatus> {
    public ResearchStudyStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ResearchStudyStatus.ACTIVE;
        if ("administratively-completed".equals(codeString))
          return ResearchStudyStatus.ADMINISTRATIVELYCOMPLETED;
        if ("approved".equals(codeString))
          return ResearchStudyStatus.APPROVED;
        if ("closed-to-accrual".equals(codeString))
          return ResearchStudyStatus.CLOSEDTOACCRUAL;
        if ("closed-to-accrual-and-intervention".equals(codeString))
          return ResearchStudyStatus.CLOSEDTOACCRUALANDINTERVENTION;
        if ("completed".equals(codeString))
          return ResearchStudyStatus.COMPLETED;
        if ("disapproved".equals(codeString))
          return ResearchStudyStatus.DISAPPROVED;
        if ("in-review".equals(codeString))
          return ResearchStudyStatus.INREVIEW;
        if ("temporarily-closed-to-accrual".equals(codeString))
          return ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUAL;
        if ("temporarily-closed-to-accrual-and-intervention".equals(codeString))
          return ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION;
        if ("withdrawn".equals(codeString))
          return ResearchStudyStatus.WITHDRAWN;
        throw new IllegalArgumentException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
        public Enumeration<ResearchStudyStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ResearchStudyStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.ACTIVE);
        if ("administratively-completed".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.ADMINISTRATIVELYCOMPLETED);
        if ("approved".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.APPROVED);
        if ("closed-to-accrual".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.CLOSEDTOACCRUAL);
        if ("closed-to-accrual-and-intervention".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.CLOSEDTOACCRUALANDINTERVENTION);
        if ("completed".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.COMPLETED);
        if ("disapproved".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.DISAPPROVED);
        if ("in-review".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.INREVIEW);
        if ("temporarily-closed-to-accrual".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUAL);
        if ("temporarily-closed-to-accrual-and-intervention".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION);
        if ("withdrawn".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.WITHDRAWN);
        throw new FHIRException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
    public String toCode(ResearchStudyStatus code) {
      if (code == ResearchStudyStatus.ACTIVE)
        return "active";
      if (code == ResearchStudyStatus.ADMINISTRATIVELYCOMPLETED)
        return "administratively-completed";
      if (code == ResearchStudyStatus.APPROVED)
        return "approved";
      if (code == ResearchStudyStatus.CLOSEDTOACCRUAL)
        return "closed-to-accrual";
      if (code == ResearchStudyStatus.CLOSEDTOACCRUALANDINTERVENTION)
        return "closed-to-accrual-and-intervention";
      if (code == ResearchStudyStatus.COMPLETED)
        return "completed";
      if (code == ResearchStudyStatus.DISAPPROVED)
        return "disapproved";
      if (code == ResearchStudyStatus.INREVIEW)
        return "in-review";
      if (code == ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUAL)
        return "temporarily-closed-to-accrual";
      if (code == ResearchStudyStatus.TEMPORARILYCLOSEDTOACCRUALANDINTERVENTION)
        return "temporarily-closed-to-accrual-and-intervention";
      if (code == ResearchStudyStatus.WITHDRAWN)
        return "withdrawn";
      return "?";
      }
    public String toSystem(ResearchStudyStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ResearchStudyArmComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique, human-readable label for this arm of the study.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Label for study arm", formalDefinition="Unique, human-readable label for this arm of the study." )
        protected StringType name;

        /**
         * Categorization of study arm, e.g. experimental, active comparator, placebo comparater.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Categorization of study arm", formalDefinition="Categorization of study arm, e.g. experimental, active comparator, placebo comparater." )
        protected CodeableConcept type;

        /**
         * A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short explanation of study path", formalDefinition="A succinct description of the path through the study that would be followed by a subject adhering to this arm." )
        protected StringType description;

        private static final long serialVersionUID = 311445244L;

    /**
     * Constructor
     */
      public ResearchStudyArmComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ResearchStudyArmComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Unique, human-readable label for this arm of the study.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyArmComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Unique, human-readable label for this arm of the study.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ResearchStudyArmComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Unique, human-readable label for this arm of the study.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Unique, human-readable label for this arm of the study.
         */
        public ResearchStudyArmComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (Categorization of study arm, e.g. experimental, active comparator, placebo comparater.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyArmComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Categorization of study arm, e.g. experimental, active comparator, placebo comparater.)
         */
        public ResearchStudyArmComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #description} (A succinct description of the path through the study that would be followed by a subject adhering to this arm.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyArmComponent.description");
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
         * @param value {@link #description} (A succinct description of the path through the study that would be followed by a subject adhering to this arm.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ResearchStudyArmComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         */
        public ResearchStudyArmComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "Unique, human-readable label for this arm of the study.", 0, 1, name));
          children.add(new Property("type", "CodeableConcept", "Categorization of study arm, e.g. experimental, active comparator, placebo comparater.", 0, 1, type));
          children.add(new Property("description", "string", "A succinct description of the path through the study that would be followed by a subject adhering to this arm.", 0, 1, description));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "Unique, human-readable label for this arm of the study.", 0, 1, name);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Categorization of study arm, e.g. experimental, active comparator, placebo comparater.", 0, 1, type);
          case -1724546052: /*description*/  return new Property("description", "string", "A succinct description of the path through the study that would be followed by a subject adhering to this arm.", 0, 1, description);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 3575610:  return getType(); 
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.name");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.description");
        }
        else
          return super.addChild(name);
      }

      public ResearchStudyArmComponent copy() {
        ResearchStudyArmComponent dst = new ResearchStudyArmComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ResearchStudyArmComponent))
          return false;
        ResearchStudyArmComponent o = (ResearchStudyArmComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(type, o.type, true) && compareDeep(description, o.description, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ResearchStudyArmComponent))
          return false;
        ResearchStudyArmComponent o = (ResearchStudyArmComponent) other_;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, type, description
          );
      }

  public String fhirType() {
    return "ResearchStudy.arm";

  }

  }

    @Block()
    public static class ResearchStudyObjectiveComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique, human-readable label for this objective of the study.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Label for the objective", formalDefinition="Unique, human-readable label for this objective of the study." )
        protected StringType name;

        /**
         * The kind of study objective.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="primary | secondary | exploratory", formalDefinition="The kind of study objective." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-study-objective-type")
        protected CodeableConcept type;

        private static final long serialVersionUID = -1935215997L;

    /**
     * Constructor
     */
      public ResearchStudyObjectiveComponent() {
        super();
      }

        /**
         * @return {@link #name} (Unique, human-readable label for this objective of the study.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyObjectiveComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Unique, human-readable label for this objective of the study.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ResearchStudyObjectiveComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Unique, human-readable label for this objective of the study.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Unique, human-readable label for this objective of the study.
         */
        public ResearchStudyObjectiveComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (The kind of study objective.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyObjectiveComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The kind of study objective.)
         */
        public ResearchStudyObjectiveComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("name", "string", "Unique, human-readable label for this objective of the study.", 0, 1, name));
          children.add(new Property("type", "CodeableConcept", "The kind of study objective.", 0, 1, type));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3373707: /*name*/  return new Property("name", "string", "Unique, human-readable label for this objective of the study.", 0, 1, name);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The kind of study objective.", 0, 1, type);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 3575610:  return getType(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.name");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else
          return super.addChild(name);
      }

      public ResearchStudyObjectiveComponent copy() {
        ResearchStudyObjectiveComponent dst = new ResearchStudyObjectiveComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ResearchStudyObjectiveComponent))
          return false;
        ResearchStudyObjectiveComponent o = (ResearchStudyObjectiveComponent) other_;
        return compareDeep(name, o.name, true) && compareDeep(type, o.type, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ResearchStudyObjectiveComponent))
          return false;
        ResearchStudyObjectiveComponent o = (ResearchStudyObjectiveComponent) other_;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, type);
      }

  public String fhirType() {
    return "ResearchStudy.objective";

  }

  }

    /**
     * Identifiers assigned to this research study by the sponsor or other systems.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier for study", formalDefinition="Identifiers assigned to this research study by the sponsor or other systems." )
    protected List<Identifier> identifier;

    /**
     * A short, descriptive user-friendly label for the study.
     */
    @Child(name = "title", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for this study", formalDefinition="A short, descriptive user-friendly label for the study." )
    protected StringType title;

    /**
     * The set of steps expected to be performed as part of the execution of the study.
     */
    @Child(name = "protocol", type = {PlanDefinition.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Steps followed in executing study", formalDefinition="The set of steps expected to be performed as part of the execution of the study." )
    protected List<Reference> protocol;
    /**
     * The actual objects that are the target of the reference (The set of steps expected to be performed as part of the execution of the study.)
     */
    protected List<PlanDefinition> protocolTarget;


    /**
     * A larger research study of which this particular study is a component or step.
     */
    @Child(name = "partOf", type = {ResearchStudy.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of larger study", formalDefinition="A larger research study of which this particular study is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger research study of which this particular study is a component or step.)
     */
    protected List<ResearchStudy> partOfTarget;


    /**
     * The current state of the study.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | administratively-completed | approved | closed-to-accrual | closed-to-accrual-and-intervention | completed | disapproved | in-review | temporarily-closed-to-accrual | temporarily-closed-to-accrual-and-intervention | withdrawn", formalDefinition="The current state of the study." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-study-status")
    protected Enumeration<ResearchStudyStatus> status;

    /**
     * The type of study based upon the intent of the study's activities. A classification of the intent of the study.
     */
    @Child(name = "primaryPurposeType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="treatment | prevention | diagnostic | supportive-care | screening | health-services-research | basic-science | device-feasibility", formalDefinition="The type of study based upon the intent of the study's activities. A classification of the intent of the study." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-study-prim-purp-type")
    protected CodeableConcept primaryPurposeType;

    /**
     * The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market evaluation.
     */
    @Child(name = "phase", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="n-a | early-phase-1 | phase-1 | phase-1-phase-2 | phase-2 | phase-2-phase-3 | phase-3 | phase-4", formalDefinition="The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market evaluation." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-study-phase")
    protected CodeableConcept phase;

    /**
     * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Classifications for the study", formalDefinition="Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc." )
    protected List<CodeableConcept> category;

    /**
     * The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about.
     */
    @Child(name = "focus", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Drugs, devices, etc. under study", formalDefinition="The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about." )
    protected List<CodeableConcept> focus;

    /**
     * The condition that is the focus of the study.  For example, In a study to examine risk factors for Lupus, might have as an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.
     */
    @Child(name = "condition", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Condition being studied", formalDefinition="The condition that is the focus of the study.  For example, In a study to examine risk factors for Lupus, might have as an inclusion criterion \"healthy volunteer\", but the target condition code would be a Lupus SNOMED code." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> condition;

    /**
     * Contact details to assist a user in learning more about or engaging with the study.
     */
    @Child(name = "contact", type = {ContactDetail.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details for the study", formalDefinition="Contact details to assist a user in learning more about or engaging with the study." )
    protected List<ContactDetail> contact;

    /**
     * Citations, references and other related documents.
     */
    @Child(name = "relatedArtifact", type = {RelatedArtifact.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="References and dependencies", formalDefinition="Citations, references and other related documents." )
    protected List<RelatedArtifact> relatedArtifact;

    /**
     * Key terms to aid in searching for or filtering the study.
     */
    @Child(name = "keyword", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Used to search for the study", formalDefinition="Key terms to aid in searching for or filtering the study." )
    protected List<CodeableConcept> keyword;

    /**
     * Indicates a country, state or other region where the study is taking place.
     */
    @Child(name = "location", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Geographic region(s) for study", formalDefinition="Indicates a country, state or other region where the study is taking place." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/jurisdiction")
    protected List<CodeableConcept> location;

    /**
     * A full description of how the study is being conducted.
     */
    @Child(name = "description", type = {MarkdownType.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What this is study doing", formalDefinition="A full description of how the study is being conducted." )
    protected MarkdownType description;

    /**
     * Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. " 200 female Europeans between the ages of 20 and 45 with early onset diabetes".
     */
    @Child(name = "enrollment", type = {Group.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Inclusion & exclusion criteria", formalDefinition="Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. \" 200 female Europeans between the ages of 20 and 45 with early onset diabetes\"." )
    protected List<Reference> enrollment;
    /**
     * The actual objects that are the target of the reference (Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. " 200 female Europeans between the ages of 20 and 45 with early onset diabetes".)
     */
    protected List<Group> enrollmentTarget;


    /**
     * Identifies the start date and the expected (or actual, depending on status) end date for the study.
     */
    @Child(name = "period", type = {Period.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the study began and ended", formalDefinition="Identifies the start date and the expected (or actual, depending on status) end date for the study." )
    protected Period period;

    /**
     * An organization that initiates the investigation and is legally responsible for the study.
     */
    @Child(name = "sponsor", type = {Organization.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization that initiates and is legally responsible for the study", formalDefinition="An organization that initiates the investigation and is legally responsible for the study." )
    protected Reference sponsor;

    /**
     * The actual object that is the target of the reference (An organization that initiates the investigation and is legally responsible for the study.)
     */
    protected Organization sponsorTarget;

    /**
     * A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.
     */
    @Child(name = "principalInvestigator", type = {Practitioner.class, PractitionerRole.class}, order=18, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Researcher who oversees multiple aspects of the study", formalDefinition="A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation." )
    protected Reference principalInvestigator;

    /**
     * The actual object that is the target of the reference (A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.)
     */
    protected Resource principalInvestigatorTarget;

    /**
     * A facility in which study activities are conducted.
     */
    @Child(name = "site", type = {Location.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Facility where study activities are conducted", formalDefinition="A facility in which study activities are conducted." )
    protected List<Reference> site;
    /**
     * The actual objects that are the target of the reference (A facility in which study activities are conducted.)
     */
    protected List<Location> siteTarget;


    /**
     * A description and/or code explaining the premature termination of the study.
     */
    @Child(name = "reasonStopped", type = {CodeableConcept.class}, order=20, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="accrual-goal-met | closed-due-to-toxicity | closed-due-to-lack-of-study-progress | temporarily-closed-per-study-design", formalDefinition="A description and/or code explaining the premature termination of the study." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-study-reason-stopped")
    protected CodeableConcept reasonStopped;

    /**
     * Comments made about the study by the performer, subject or other participants.
     */
    @Child(name = "note", type = {Annotation.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the study", formalDefinition="Comments made about the study by the performer, subject or other participants." )
    protected List<Annotation> note;

    /**
     * Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up.
     */
    @Child(name = "arm", type = {}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Defined path through the study for a subject", formalDefinition="Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up." )
    protected List<ResearchStudyArmComponent> arm;

    /**
     * A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data collected during the study.
     */
    @Child(name = "objective", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A goal for the study", formalDefinition="A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data collected during the study." )
    protected List<ResearchStudyObjectiveComponent> objective;

    private static final long serialVersionUID = -911538323L;

  /**
   * Constructor
   */
    public ResearchStudy() {
      super();
    }

  /**
   * Constructor
   */
    public ResearchStudy(Enumeration<ResearchStudyStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this research study by the sponsor or other systems.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setIdentifier(List<Identifier> theIdentifier) { 
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

    public ResearchStudy addIdentifier(Identifier t) { //3
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
     * @return {@link #title} (A short, descriptive user-friendly label for the study.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive user-friendly label for the study.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ResearchStudy setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive user-friendly label for the study.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive user-friendly label for the study.
     */
    public ResearchStudy setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #protocol} (The set of steps expected to be performed as part of the execution of the study.)
     */
    public List<Reference> getProtocol() { 
      if (this.protocol == null)
        this.protocol = new ArrayList<Reference>();
      return this.protocol;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setProtocol(List<Reference> theProtocol) { 
      this.protocol = theProtocol;
      return this;
    }

    public boolean hasProtocol() { 
      if (this.protocol == null)
        return false;
      for (Reference item : this.protocol)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addProtocol() { //3
      Reference t = new Reference();
      if (this.protocol == null)
        this.protocol = new ArrayList<Reference>();
      this.protocol.add(t);
      return t;
    }

    public ResearchStudy addProtocol(Reference t) { //3
      if (t == null)
        return this;
      if (this.protocol == null)
        this.protocol = new ArrayList<Reference>();
      this.protocol.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #protocol}, creating it if it does not already exist
     */
    public Reference getProtocolFirstRep() { 
      if (getProtocol().isEmpty()) {
        addProtocol();
      }
      return getProtocol().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<PlanDefinition> getProtocolTarget() { 
      if (this.protocolTarget == null)
        this.protocolTarget = new ArrayList<PlanDefinition>();
      return this.protocolTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public PlanDefinition addProtocolTarget() { 
      PlanDefinition r = new PlanDefinition();
      if (this.protocolTarget == null)
        this.protocolTarget = new ArrayList<PlanDefinition>();
      this.protocolTarget.add(r);
      return r;
    }

    /**
     * @return {@link #partOf} (A larger research study of which this particular study is a component or step.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setPartOf(List<Reference> thePartOf) { 
      this.partOf = thePartOf;
      return this;
    }

    public boolean hasPartOf() { 
      if (this.partOf == null)
        return false;
      for (Reference item : this.partOf)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPartOf() { //3
      Reference t = new Reference();
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return t;
    }

    public ResearchStudy addPartOf(Reference t) { //3
      if (t == null)
        return this;
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #partOf}, creating it if it does not already exist
     */
    public Reference getPartOfFirstRep() { 
      if (getPartOf().isEmpty()) {
        addPartOf();
      }
      return getPartOf().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<ResearchStudy> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<ResearchStudy>();
      return this.partOfTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ResearchStudy addPartOfTarget() { 
      ResearchStudy r = new ResearchStudy();
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<ResearchStudy>();
      this.partOfTarget.add(r);
      return r;
    }

    /**
     * @return {@link #status} (The current state of the study.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ResearchStudyStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ResearchStudyStatus>(new ResearchStudyStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of the study.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ResearchStudy setStatusElement(Enumeration<ResearchStudyStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of the study.
     */
    public ResearchStudyStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of the study.
     */
    public ResearchStudy setStatus(ResearchStudyStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ResearchStudyStatus>(new ResearchStudyStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #primaryPurposeType} (The type of study based upon the intent of the study's activities. A classification of the intent of the study.)
     */
    public CodeableConcept getPrimaryPurposeType() { 
      if (this.primaryPurposeType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.primaryPurposeType");
        else if (Configuration.doAutoCreate())
          this.primaryPurposeType = new CodeableConcept(); // cc
      return this.primaryPurposeType;
    }

    public boolean hasPrimaryPurposeType() { 
      return this.primaryPurposeType != null && !this.primaryPurposeType.isEmpty();
    }

    /**
     * @param value {@link #primaryPurposeType} (The type of study based upon the intent of the study's activities. A classification of the intent of the study.)
     */
    public ResearchStudy setPrimaryPurposeType(CodeableConcept value) { 
      this.primaryPurposeType = value;
      return this;
    }

    /**
     * @return {@link #phase} (The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market evaluation.)
     */
    public CodeableConcept getPhase() { 
      if (this.phase == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.phase");
        else if (Configuration.doAutoCreate())
          this.phase = new CodeableConcept(); // cc
      return this.phase;
    }

    public boolean hasPhase() { 
      return this.phase != null && !this.phase.isEmpty();
    }

    /**
     * @param value {@link #phase} (The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market evaluation.)
     */
    public ResearchStudy setPhase(CodeableConcept value) { 
      this.phase = value;
      return this;
    }

    /**
     * @return {@link #category} (Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setCategory(List<CodeableConcept> theCategory) { 
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

    public ResearchStudy addCategory(CodeableConcept t) { //3
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
     * @return {@link #focus} (The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about.)
     */
    public List<CodeableConcept> getFocus() { 
      if (this.focus == null)
        this.focus = new ArrayList<CodeableConcept>();
      return this.focus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setFocus(List<CodeableConcept> theFocus) { 
      this.focus = theFocus;
      return this;
    }

    public boolean hasFocus() { 
      if (this.focus == null)
        return false;
      for (CodeableConcept item : this.focus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addFocus() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.focus == null)
        this.focus = new ArrayList<CodeableConcept>();
      this.focus.add(t);
      return t;
    }

    public ResearchStudy addFocus(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.focus == null)
        this.focus = new ArrayList<CodeableConcept>();
      this.focus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #focus}, creating it if it does not already exist
     */
    public CodeableConcept getFocusFirstRep() { 
      if (getFocus().isEmpty()) {
        addFocus();
      }
      return getFocus().get(0);
    }

    /**
     * @return {@link #condition} (The condition that is the focus of the study.  For example, In a study to examine risk factors for Lupus, might have as an inclusion criterion "healthy volunteer", but the target condition code would be a Lupus SNOMED code.)
     */
    public List<CodeableConcept> getCondition() { 
      if (this.condition == null)
        this.condition = new ArrayList<CodeableConcept>();
      return this.condition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setCondition(List<CodeableConcept> theCondition) { 
      this.condition = theCondition;
      return this;
    }

    public boolean hasCondition() { 
      if (this.condition == null)
        return false;
      for (CodeableConcept item : this.condition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCondition() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.condition == null)
        this.condition = new ArrayList<CodeableConcept>();
      this.condition.add(t);
      return t;
    }

    public ResearchStudy addCondition(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.condition == null)
        this.condition = new ArrayList<CodeableConcept>();
      this.condition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #condition}, creating it if it does not already exist
     */
    public CodeableConcept getConditionFirstRep() { 
      if (getCondition().isEmpty()) {
        addCondition();
      }
      return getCondition().get(0);
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in learning more about or engaging with the study.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public ResearchStudy addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #relatedArtifact} (Citations, references and other related documents.)
     */
    public List<RelatedArtifact> getRelatedArtifact() { 
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      return this.relatedArtifact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setRelatedArtifact(List<RelatedArtifact> theRelatedArtifact) { 
      this.relatedArtifact = theRelatedArtifact;
      return this;
    }

    public boolean hasRelatedArtifact() { 
      if (this.relatedArtifact == null)
        return false;
      for (RelatedArtifact item : this.relatedArtifact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedArtifact addRelatedArtifact() { //3
      RelatedArtifact t = new RelatedArtifact();
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return t;
    }

    public ResearchStudy addRelatedArtifact(RelatedArtifact t) { //3
      if (t == null)
        return this;
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedArtifact}, creating it if it does not already exist
     */
    public RelatedArtifact getRelatedArtifactFirstRep() { 
      if (getRelatedArtifact().isEmpty()) {
        addRelatedArtifact();
      }
      return getRelatedArtifact().get(0);
    }

    /**
     * @return {@link #keyword} (Key terms to aid in searching for or filtering the study.)
     */
    public List<CodeableConcept> getKeyword() { 
      if (this.keyword == null)
        this.keyword = new ArrayList<CodeableConcept>();
      return this.keyword;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setKeyword(List<CodeableConcept> theKeyword) { 
      this.keyword = theKeyword;
      return this;
    }

    public boolean hasKeyword() { 
      if (this.keyword == null)
        return false;
      for (CodeableConcept item : this.keyword)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addKeyword() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.keyword == null)
        this.keyword = new ArrayList<CodeableConcept>();
      this.keyword.add(t);
      return t;
    }

    public ResearchStudy addKeyword(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.keyword == null)
        this.keyword = new ArrayList<CodeableConcept>();
      this.keyword.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #keyword}, creating it if it does not already exist
     */
    public CodeableConcept getKeywordFirstRep() { 
      if (getKeyword().isEmpty()) {
        addKeyword();
      }
      return getKeyword().get(0);
    }

    /**
     * @return {@link #location} (Indicates a country, state or other region where the study is taking place.)
     */
    public List<CodeableConcept> getLocation() { 
      if (this.location == null)
        this.location = new ArrayList<CodeableConcept>();
      return this.location;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setLocation(List<CodeableConcept> theLocation) { 
      this.location = theLocation;
      return this;
    }

    public boolean hasLocation() { 
      if (this.location == null)
        return false;
      for (CodeableConcept item : this.location)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addLocation() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.location == null)
        this.location = new ArrayList<CodeableConcept>();
      this.location.add(t);
      return t;
    }

    public ResearchStudy addLocation(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.location == null)
        this.location = new ArrayList<CodeableConcept>();
      this.location.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #location}, creating it if it does not already exist
     */
    public CodeableConcept getLocationFirstRep() { 
      if (getLocation().isEmpty()) {
        addLocation();
      }
      return getLocation().get(0);
    }

    /**
     * @return {@link #description} (A full description of how the study is being conducted.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A full description of how the study is being conducted.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ResearchStudy setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A full description of how the study is being conducted.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A full description of how the study is being conducted.
     */
    public ResearchStudy setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #enrollment} (Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. " 200 female Europeans between the ages of 20 and 45 with early onset diabetes".)
     */
    public List<Reference> getEnrollment() { 
      if (this.enrollment == null)
        this.enrollment = new ArrayList<Reference>();
      return this.enrollment;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setEnrollment(List<Reference> theEnrollment) { 
      this.enrollment = theEnrollment;
      return this;
    }

    public boolean hasEnrollment() { 
      if (this.enrollment == null)
        return false;
      for (Reference item : this.enrollment)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEnrollment() { //3
      Reference t = new Reference();
      if (this.enrollment == null)
        this.enrollment = new ArrayList<Reference>();
      this.enrollment.add(t);
      return t;
    }

    public ResearchStudy addEnrollment(Reference t) { //3
      if (t == null)
        return this;
      if (this.enrollment == null)
        this.enrollment = new ArrayList<Reference>();
      this.enrollment.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #enrollment}, creating it if it does not already exist
     */
    public Reference getEnrollmentFirstRep() { 
      if (getEnrollment().isEmpty()) {
        addEnrollment();
      }
      return getEnrollment().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Group> getEnrollmentTarget() { 
      if (this.enrollmentTarget == null)
        this.enrollmentTarget = new ArrayList<Group>();
      return this.enrollmentTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Group addEnrollmentTarget() { 
      Group r = new Group();
      if (this.enrollmentTarget == null)
        this.enrollmentTarget = new ArrayList<Group>();
      this.enrollmentTarget.add(r);
      return r;
    }

    /**
     * @return {@link #period} (Identifies the start date and the expected (or actual, depending on status) end date for the study.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Identifies the start date and the expected (or actual, depending on status) end date for the study.)
     */
    public ResearchStudy setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #sponsor} (An organization that initiates the investigation and is legally responsible for the study.)
     */
    public Reference getSponsor() { 
      if (this.sponsor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.sponsor");
        else if (Configuration.doAutoCreate())
          this.sponsor = new Reference(); // cc
      return this.sponsor;
    }

    public boolean hasSponsor() { 
      return this.sponsor != null && !this.sponsor.isEmpty();
    }

    /**
     * @param value {@link #sponsor} (An organization that initiates the investigation and is legally responsible for the study.)
     */
    public ResearchStudy setSponsor(Reference value) { 
      this.sponsor = value;
      return this;
    }

    /**
     * @return {@link #sponsor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An organization that initiates the investigation and is legally responsible for the study.)
     */
    public Organization getSponsorTarget() { 
      if (this.sponsorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.sponsor");
        else if (Configuration.doAutoCreate())
          this.sponsorTarget = new Organization(); // aa
      return this.sponsorTarget;
    }

    /**
     * @param value {@link #sponsor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An organization that initiates the investigation and is legally responsible for the study.)
     */
    public ResearchStudy setSponsorTarget(Organization value) { 
      this.sponsorTarget = value;
      return this;
    }

    /**
     * @return {@link #principalInvestigator} (A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.)
     */
    public Reference getPrincipalInvestigator() { 
      if (this.principalInvestigator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.principalInvestigator");
        else if (Configuration.doAutoCreate())
          this.principalInvestigator = new Reference(); // cc
      return this.principalInvestigator;
    }

    public boolean hasPrincipalInvestigator() { 
      return this.principalInvestigator != null && !this.principalInvestigator.isEmpty();
    }

    /**
     * @param value {@link #principalInvestigator} (A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.)
     */
    public ResearchStudy setPrincipalInvestigator(Reference value) { 
      this.principalInvestigator = value;
      return this;
    }

    /**
     * @return {@link #principalInvestigator} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.)
     */
    public Resource getPrincipalInvestigatorTarget() { 
      return this.principalInvestigatorTarget;
    }

    /**
     * @param value {@link #principalInvestigator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.)
     */
    public ResearchStudy setPrincipalInvestigatorTarget(Resource value) { 
      this.principalInvestigatorTarget = value;
      return this;
    }

    /**
     * @return {@link #site} (A facility in which study activities are conducted.)
     */
    public List<Reference> getSite() { 
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      return this.site;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setSite(List<Reference> theSite) { 
      this.site = theSite;
      return this;
    }

    public boolean hasSite() { 
      if (this.site == null)
        return false;
      for (Reference item : this.site)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSite() { //3
      Reference t = new Reference();
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      this.site.add(t);
      return t;
    }

    public ResearchStudy addSite(Reference t) { //3
      if (t == null)
        return this;
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      this.site.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #site}, creating it if it does not already exist
     */
    public Reference getSiteFirstRep() { 
      if (getSite().isEmpty()) {
        addSite();
      }
      return getSite().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getSiteTarget() { 
      if (this.siteTarget == null)
        this.siteTarget = new ArrayList<Location>();
      return this.siteTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addSiteTarget() { 
      Location r = new Location();
      if (this.siteTarget == null)
        this.siteTarget = new ArrayList<Location>();
      this.siteTarget.add(r);
      return r;
    }

    /**
     * @return {@link #reasonStopped} (A description and/or code explaining the premature termination of the study.)
     */
    public CodeableConcept getReasonStopped() { 
      if (this.reasonStopped == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.reasonStopped");
        else if (Configuration.doAutoCreate())
          this.reasonStopped = new CodeableConcept(); // cc
      return this.reasonStopped;
    }

    public boolean hasReasonStopped() { 
      return this.reasonStopped != null && !this.reasonStopped.isEmpty();
    }

    /**
     * @param value {@link #reasonStopped} (A description and/or code explaining the premature termination of the study.)
     */
    public ResearchStudy setReasonStopped(CodeableConcept value) { 
      this.reasonStopped = value;
      return this;
    }

    /**
     * @return {@link #note} (Comments made about the study by the performer, subject or other participants.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setNote(List<Annotation> theNote) { 
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

    public ResearchStudy addNote(Annotation t) { //3
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

    /**
     * @return {@link #arm} (Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up.)
     */
    public List<ResearchStudyArmComponent> getArm() { 
      if (this.arm == null)
        this.arm = new ArrayList<ResearchStudyArmComponent>();
      return this.arm;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setArm(List<ResearchStudyArmComponent> theArm) { 
      this.arm = theArm;
      return this;
    }

    public boolean hasArm() { 
      if (this.arm == null)
        return false;
      for (ResearchStudyArmComponent item : this.arm)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ResearchStudyArmComponent addArm() { //3
      ResearchStudyArmComponent t = new ResearchStudyArmComponent();
      if (this.arm == null)
        this.arm = new ArrayList<ResearchStudyArmComponent>();
      this.arm.add(t);
      return t;
    }

    public ResearchStudy addArm(ResearchStudyArmComponent t) { //3
      if (t == null)
        return this;
      if (this.arm == null)
        this.arm = new ArrayList<ResearchStudyArmComponent>();
      this.arm.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #arm}, creating it if it does not already exist
     */
    public ResearchStudyArmComponent getArmFirstRep() { 
      if (getArm().isEmpty()) {
        addArm();
      }
      return getArm().get(0);
    }

    /**
     * @return {@link #objective} (A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data collected during the study.)
     */
    public List<ResearchStudyObjectiveComponent> getObjective() { 
      if (this.objective == null)
        this.objective = new ArrayList<ResearchStudyObjectiveComponent>();
      return this.objective;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setObjective(List<ResearchStudyObjectiveComponent> theObjective) { 
      this.objective = theObjective;
      return this;
    }

    public boolean hasObjective() { 
      if (this.objective == null)
        return false;
      for (ResearchStudyObjectiveComponent item : this.objective)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ResearchStudyObjectiveComponent addObjective() { //3
      ResearchStudyObjectiveComponent t = new ResearchStudyObjectiveComponent();
      if (this.objective == null)
        this.objective = new ArrayList<ResearchStudyObjectiveComponent>();
      this.objective.add(t);
      return t;
    }

    public ResearchStudy addObjective(ResearchStudyObjectiveComponent t) { //3
      if (t == null)
        return this;
      if (this.objective == null)
        this.objective = new ArrayList<ResearchStudyObjectiveComponent>();
      this.objective.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #objective}, creating it if it does not already exist
     */
    public ResearchStudyObjectiveComponent getObjectiveFirstRep() { 
      if (getObjective().isEmpty()) {
        addObjective();
      }
      return getObjective().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Identifiers assigned to this research study by the sponsor or other systems.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("title", "string", "A short, descriptive user-friendly label for the study.", 0, 1, title));
        children.add(new Property("protocol", "Reference(PlanDefinition)", "The set of steps expected to be performed as part of the execution of the study.", 0, java.lang.Integer.MAX_VALUE, protocol));
        children.add(new Property("partOf", "Reference(ResearchStudy)", "A larger research study of which this particular study is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        children.add(new Property("status", "code", "The current state of the study.", 0, 1, status));
        children.add(new Property("primaryPurposeType", "CodeableConcept", "The type of study based upon the intent of the study's activities. A classification of the intent of the study.", 0, 1, primaryPurposeType));
        children.add(new Property("phase", "CodeableConcept", "The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market evaluation.", 0, 1, phase));
        children.add(new Property("category", "CodeableConcept", "Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc.", 0, java.lang.Integer.MAX_VALUE, category));
        children.add(new Property("focus", "CodeableConcept", "The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about.", 0, java.lang.Integer.MAX_VALUE, focus));
        children.add(new Property("condition", "CodeableConcept", "The condition that is the focus of the study.  For example, In a study to examine risk factors for Lupus, might have as an inclusion criterion \"healthy volunteer\", but the target condition code would be a Lupus SNOMED code.", 0, java.lang.Integer.MAX_VALUE, condition));
        children.add(new Property("contact", "ContactDetail", "Contact details to assist a user in learning more about or engaging with the study.", 0, java.lang.Integer.MAX_VALUE, contact));
        children.add(new Property("relatedArtifact", "RelatedArtifact", "Citations, references and other related documents.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        children.add(new Property("keyword", "CodeableConcept", "Key terms to aid in searching for or filtering the study.", 0, java.lang.Integer.MAX_VALUE, keyword));
        children.add(new Property("location", "CodeableConcept", "Indicates a country, state or other region where the study is taking place.", 0, java.lang.Integer.MAX_VALUE, location));
        children.add(new Property("description", "markdown", "A full description of how the study is being conducted.", 0, 1, description));
        children.add(new Property("enrollment", "Reference(Group)", "Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. \" 200 female Europeans between the ages of 20 and 45 with early onset diabetes\".", 0, java.lang.Integer.MAX_VALUE, enrollment));
        children.add(new Property("period", "Period", "Identifies the start date and the expected (or actual, depending on status) end date for the study.", 0, 1, period));
        children.add(new Property("sponsor", "Reference(Organization)", "An organization that initiates the investigation and is legally responsible for the study.", 0, 1, sponsor));
        children.add(new Property("principalInvestigator", "Reference(Practitioner|PractitionerRole)", "A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.", 0, 1, principalInvestigator));
        children.add(new Property("site", "Reference(Location)", "A facility in which study activities are conducted.", 0, java.lang.Integer.MAX_VALUE, site));
        children.add(new Property("reasonStopped", "CodeableConcept", "A description and/or code explaining the premature termination of the study.", 0, 1, reasonStopped));
        children.add(new Property("note", "Annotation", "Comments made about the study by the performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("arm", "", "Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up.", 0, java.lang.Integer.MAX_VALUE, arm));
        children.add(new Property("objective", "", "A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data collected during the study.", 0, java.lang.Integer.MAX_VALUE, objective));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifiers assigned to this research study by the sponsor or other systems.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 110371416: /*title*/  return new Property("title", "string", "A short, descriptive user-friendly label for the study.", 0, 1, title);
        case -989163880: /*protocol*/  return new Property("protocol", "Reference(PlanDefinition)", "The set of steps expected to be performed as part of the execution of the study.", 0, java.lang.Integer.MAX_VALUE, protocol);
        case -995410646: /*partOf*/  return new Property("partOf", "Reference(ResearchStudy)", "A larger research study of which this particular study is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf);
        case -892481550: /*status*/  return new Property("status", "code", "The current state of the study.", 0, 1, status);
        case -2132842986: /*primaryPurposeType*/  return new Property("primaryPurposeType", "CodeableConcept", "The type of study based upon the intent of the study's activities. A classification of the intent of the study.", 0, 1, primaryPurposeType);
        case 106629499: /*phase*/  return new Property("phase", "CodeableConcept", "The stage in the progression of a therapy from initial experimental use in humans in clinical trials to post-market evaluation.", 0, 1, phase);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc.", 0, java.lang.Integer.MAX_VALUE, category);
        case 97604824: /*focus*/  return new Property("focus", "CodeableConcept", "The medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about.", 0, java.lang.Integer.MAX_VALUE, focus);
        case -861311717: /*condition*/  return new Property("condition", "CodeableConcept", "The condition that is the focus of the study.  For example, In a study to examine risk factors for Lupus, might have as an inclusion criterion \"healthy volunteer\", but the target condition code would be a Lupus SNOMED code.", 0, java.lang.Integer.MAX_VALUE, condition);
        case 951526432: /*contact*/  return new Property("contact", "ContactDetail", "Contact details to assist a user in learning more about or engaging with the study.", 0, java.lang.Integer.MAX_VALUE, contact);
        case 666807069: /*relatedArtifact*/  return new Property("relatedArtifact", "RelatedArtifact", "Citations, references and other related documents.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact);
        case -814408215: /*keyword*/  return new Property("keyword", "CodeableConcept", "Key terms to aid in searching for or filtering the study.", 0, java.lang.Integer.MAX_VALUE, keyword);
        case 1901043637: /*location*/  return new Property("location", "CodeableConcept", "Indicates a country, state or other region where the study is taking place.", 0, java.lang.Integer.MAX_VALUE, location);
        case -1724546052: /*description*/  return new Property("description", "markdown", "A full description of how the study is being conducted.", 0, 1, description);
        case 116089604: /*enrollment*/  return new Property("enrollment", "Reference(Group)", "Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. \" 200 female Europeans between the ages of 20 and 45 with early onset diabetes\".", 0, java.lang.Integer.MAX_VALUE, enrollment);
        case -991726143: /*period*/  return new Property("period", "Period", "Identifies the start date and the expected (or actual, depending on status) end date for the study.", 0, 1, period);
        case -1998892262: /*sponsor*/  return new Property("sponsor", "Reference(Organization)", "An organization that initiates the investigation and is legally responsible for the study.", 0, 1, sponsor);
        case 1437117175: /*principalInvestigator*/  return new Property("principalInvestigator", "Reference(Practitioner|PractitionerRole)", "A researcher in a study who oversees multiple aspects of the study, such as concept development, protocol writing, protocol submission for IRB approval, participant recruitment, informed consent, data collection, analysis, interpretation and presentation.", 0, 1, principalInvestigator);
        case 3530567: /*site*/  return new Property("site", "Reference(Location)", "A facility in which study activities are conducted.", 0, java.lang.Integer.MAX_VALUE, site);
        case 1181369065: /*reasonStopped*/  return new Property("reasonStopped", "CodeableConcept", "A description and/or code explaining the premature termination of the study.", 0, 1, reasonStopped);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Comments made about the study by the performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note);
        case 96860: /*arm*/  return new Property("arm", "", "Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up.", 0, java.lang.Integer.MAX_VALUE, arm);
        case -1489585863: /*objective*/  return new Property("objective", "", "A goal that the study is aiming to achieve in terms of a scientific question to be answered by the analysis of data collected during the study.", 0, java.lang.Integer.MAX_VALUE, objective);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -989163880: /*protocol*/ return this.protocol == null ? new Base[0] : this.protocol.toArray(new Base[this.protocol.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ResearchStudyStatus>
        case -2132842986: /*primaryPurposeType*/ return this.primaryPurposeType == null ? new Base[0] : new Base[] {this.primaryPurposeType}; // CodeableConcept
        case 106629499: /*phase*/ return this.phase == null ? new Base[0] : new Base[] {this.phase}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : this.focus.toArray(new Base[this.focus.size()]); // CodeableConcept
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : this.condition.toArray(new Base[this.condition.size()]); // CodeableConcept
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 666807069: /*relatedArtifact*/ return this.relatedArtifact == null ? new Base[0] : this.relatedArtifact.toArray(new Base[this.relatedArtifact.size()]); // RelatedArtifact
        case -814408215: /*keyword*/ return this.keyword == null ? new Base[0] : this.keyword.toArray(new Base[this.keyword.size()]); // CodeableConcept
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : this.location.toArray(new Base[this.location.size()]); // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case 116089604: /*enrollment*/ return this.enrollment == null ? new Base[0] : this.enrollment.toArray(new Base[this.enrollment.size()]); // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1998892262: /*sponsor*/ return this.sponsor == null ? new Base[0] : new Base[] {this.sponsor}; // Reference
        case 1437117175: /*principalInvestigator*/ return this.principalInvestigator == null ? new Base[0] : new Base[] {this.principalInvestigator}; // Reference
        case 3530567: /*site*/ return this.site == null ? new Base[0] : this.site.toArray(new Base[this.site.size()]); // Reference
        case 1181369065: /*reasonStopped*/ return this.reasonStopped == null ? new Base[0] : new Base[] {this.reasonStopped}; // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 96860: /*arm*/ return this.arm == null ? new Base[0] : this.arm.toArray(new Base[this.arm.size()]); // ResearchStudyArmComponent
        case -1489585863: /*objective*/ return this.objective == null ? new Base[0] : this.objective.toArray(new Base[this.objective.size()]); // ResearchStudyObjectiveComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -989163880: // protocol
          this.getProtocol().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new ResearchStudyStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ResearchStudyStatus>
          return value;
        case -2132842986: // primaryPurposeType
          this.primaryPurposeType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 106629499: // phase
          this.phase = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 97604824: // focus
          this.getFocus().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -861311717: // condition
          this.getCondition().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 666807069: // relatedArtifact
          this.getRelatedArtifact().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case -814408215: // keyword
          this.getKeyword().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1901043637: // location
          this.getLocation().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case 116089604: // enrollment
          this.getEnrollment().add(castToReference(value)); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -1998892262: // sponsor
          this.sponsor = castToReference(value); // Reference
          return value;
        case 1437117175: // principalInvestigator
          this.principalInvestigator = castToReference(value); // Reference
          return value;
        case 3530567: // site
          this.getSite().add(castToReference(value)); // Reference
          return value;
        case 1181369065: // reasonStopped
          this.reasonStopped = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case 96860: // arm
          this.getArm().add((ResearchStudyArmComponent) value); // ResearchStudyArmComponent
          return value;
        case -1489585863: // objective
          this.getObjective().add((ResearchStudyObjectiveComponent) value); // ResearchStudyObjectiveComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("protocol")) {
          this.getProtocol().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new ResearchStudyStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ResearchStudyStatus>
        } else if (name.equals("primaryPurposeType")) {
          this.primaryPurposeType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("phase")) {
          this.phase = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("focus")) {
          this.getFocus().add(castToCodeableConcept(value));
        } else if (name.equals("condition")) {
          this.getCondition().add(castToCodeableConcept(value));
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("relatedArtifact")) {
          this.getRelatedArtifact().add(castToRelatedArtifact(value));
        } else if (name.equals("keyword")) {
          this.getKeyword().add(castToCodeableConcept(value));
        } else if (name.equals("location")) {
          this.getLocation().add(castToCodeableConcept(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("enrollment")) {
          this.getEnrollment().add(castToReference(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("sponsor")) {
          this.sponsor = castToReference(value); // Reference
        } else if (name.equals("principalInvestigator")) {
          this.principalInvestigator = castToReference(value); // Reference
        } else if (name.equals("site")) {
          this.getSite().add(castToReference(value));
        } else if (name.equals("reasonStopped")) {
          this.reasonStopped = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("arm")) {
          this.getArm().add((ResearchStudyArmComponent) value);
        } else if (name.equals("objective")) {
          this.getObjective().add((ResearchStudyObjectiveComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 110371416:  return getTitleElement();
        case -989163880:  return addProtocol(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case -2132842986:  return getPrimaryPurposeType(); 
        case 106629499:  return getPhase(); 
        case 50511102:  return addCategory(); 
        case 97604824:  return addFocus(); 
        case -861311717:  return addCondition(); 
        case 951526432:  return addContact(); 
        case 666807069:  return addRelatedArtifact(); 
        case -814408215:  return addKeyword(); 
        case 1901043637:  return addLocation(); 
        case -1724546052:  return getDescriptionElement();
        case 116089604:  return addEnrollment(); 
        case -991726143:  return getPeriod(); 
        case -1998892262:  return getSponsor(); 
        case 1437117175:  return getPrincipalInvestigator(); 
        case 3530567:  return addSite(); 
        case 1181369065:  return getReasonStopped(); 
        case 3387378:  return addNote(); 
        case 96860:  return addArm(); 
        case -1489585863:  return addObjective(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -989163880: /*protocol*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -2132842986: /*primaryPurposeType*/ return new String[] {"CodeableConcept"};
        case 106629499: /*phase*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 97604824: /*focus*/ return new String[] {"CodeableConcept"};
        case -861311717: /*condition*/ return new String[] {"CodeableConcept"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case 666807069: /*relatedArtifact*/ return new String[] {"RelatedArtifact"};
        case -814408215: /*keyword*/ return new String[] {"CodeableConcept"};
        case 1901043637: /*location*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case 116089604: /*enrollment*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -1998892262: /*sponsor*/ return new String[] {"Reference"};
        case 1437117175: /*principalInvestigator*/ return new String[] {"Reference"};
        case 3530567: /*site*/ return new String[] {"Reference"};
        case 1181369065: /*reasonStopped*/ return new String[] {"CodeableConcept"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 96860: /*arm*/ return new String[] {};
        case -1489585863: /*objective*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.title");
        }
        else if (name.equals("protocol")) {
          return addProtocol();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.status");
        }
        else if (name.equals("primaryPurposeType")) {
          this.primaryPurposeType = new CodeableConcept();
          return this.primaryPurposeType;
        }
        else if (name.equals("phase")) {
          this.phase = new CodeableConcept();
          return this.phase;
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("focus")) {
          return addFocus();
        }
        else if (name.equals("condition")) {
          return addCondition();
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("relatedArtifact")) {
          return addRelatedArtifact();
        }
        else if (name.equals("keyword")) {
          return addKeyword();
        }
        else if (name.equals("location")) {
          return addLocation();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.description");
        }
        else if (name.equals("enrollment")) {
          return addEnrollment();
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("sponsor")) {
          this.sponsor = new Reference();
          return this.sponsor;
        }
        else if (name.equals("principalInvestigator")) {
          this.principalInvestigator = new Reference();
          return this.principalInvestigator;
        }
        else if (name.equals("site")) {
          return addSite();
        }
        else if (name.equals("reasonStopped")) {
          this.reasonStopped = new CodeableConcept();
          return this.reasonStopped;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("arm")) {
          return addArm();
        }
        else if (name.equals("objective")) {
          return addObjective();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ResearchStudy";

  }

      public ResearchStudy copy() {
        ResearchStudy dst = new ResearchStudy();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.title = title == null ? null : title.copy();
        if (protocol != null) {
          dst.protocol = new ArrayList<Reference>();
          for (Reference i : protocol)
            dst.protocol.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.primaryPurposeType = primaryPurposeType == null ? null : primaryPurposeType.copy();
        dst.phase = phase == null ? null : phase.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        if (focus != null) {
          dst.focus = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : focus)
            dst.focus.add(i.copy());
        };
        if (condition != null) {
          dst.condition = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : condition)
            dst.condition.add(i.copy());
        };
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        if (relatedArtifact != null) {
          dst.relatedArtifact = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : relatedArtifact)
            dst.relatedArtifact.add(i.copy());
        };
        if (keyword != null) {
          dst.keyword = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : keyword)
            dst.keyword.add(i.copy());
        };
        if (location != null) {
          dst.location = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : location)
            dst.location.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (enrollment != null) {
          dst.enrollment = new ArrayList<Reference>();
          for (Reference i : enrollment)
            dst.enrollment.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        dst.sponsor = sponsor == null ? null : sponsor.copy();
        dst.principalInvestigator = principalInvestigator == null ? null : principalInvestigator.copy();
        if (site != null) {
          dst.site = new ArrayList<Reference>();
          for (Reference i : site)
            dst.site.add(i.copy());
        };
        dst.reasonStopped = reasonStopped == null ? null : reasonStopped.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (arm != null) {
          dst.arm = new ArrayList<ResearchStudyArmComponent>();
          for (ResearchStudyArmComponent i : arm)
            dst.arm.add(i.copy());
        };
        if (objective != null) {
          dst.objective = new ArrayList<ResearchStudyObjectiveComponent>();
          for (ResearchStudyObjectiveComponent i : objective)
            dst.objective.add(i.copy());
        };
        return dst;
      }

      protected ResearchStudy typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ResearchStudy))
          return false;
        ResearchStudy o = (ResearchStudy) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(title, o.title, true) && compareDeep(protocol, o.protocol, true)
           && compareDeep(partOf, o.partOf, true) && compareDeep(status, o.status, true) && compareDeep(primaryPurposeType, o.primaryPurposeType, true)
           && compareDeep(phase, o.phase, true) && compareDeep(category, o.category, true) && compareDeep(focus, o.focus, true)
           && compareDeep(condition, o.condition, true) && compareDeep(contact, o.contact, true) && compareDeep(relatedArtifact, o.relatedArtifact, true)
           && compareDeep(keyword, o.keyword, true) && compareDeep(location, o.location, true) && compareDeep(description, o.description, true)
           && compareDeep(enrollment, o.enrollment, true) && compareDeep(period, o.period, true) && compareDeep(sponsor, o.sponsor, true)
           && compareDeep(principalInvestigator, o.principalInvestigator, true) && compareDeep(site, o.site, true)
           && compareDeep(reasonStopped, o.reasonStopped, true) && compareDeep(note, o.note, true) && compareDeep(arm, o.arm, true)
           && compareDeep(objective, o.objective, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ResearchStudy))
          return false;
        ResearchStudy o = (ResearchStudy) other_;
        return compareValues(title, o.title, true) && compareValues(status, o.status, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, title, protocol
          , partOf, status, primaryPurposeType, phase, category, focus, condition, contact
          , relatedArtifact, keyword, location, description, enrollment, period, sponsor
          , principalInvestigator, site, reasonStopped, note, arm, objective);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ResearchStudy;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the study began and ended</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchStudy.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ResearchStudy.period", description="When the study began and ended", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the study began and ended</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchStudy.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ResearchStudy.identifier", description="Business Identifier for study", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>partof</b>
   * <p>
   * Description: <b>Part of larger study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="partof", path="ResearchStudy.partOf", description="Part of larger study", type="reference", target={ResearchStudy.class } )
  public static final String SP_PARTOF = "partof";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>partof</b>
   * <p>
   * Description: <b>Part of larger study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTOF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTOF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:partof</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTOF = new ca.uhn.fhir.model.api.Include("ResearchStudy:partof").toLocked();

 /**
   * Search parameter: <b>sponsor</b>
   * <p>
   * Description: <b>Organization that initiates and is legally responsible for the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.sponsor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="sponsor", path="ResearchStudy.sponsor", description="Organization that initiates and is legally responsible for the study", type="reference", target={Organization.class } )
  public static final String SP_SPONSOR = "sponsor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>sponsor</b>
   * <p>
   * Description: <b>Organization that initiates and is legally responsible for the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.sponsor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SPONSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SPONSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:sponsor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SPONSOR = new ca.uhn.fhir.model.api.Include("ResearchStudy:sponsor").toLocked();

 /**
   * Search parameter: <b>focus</b>
   * <p>
   * Description: <b>Drugs, devices, etc. under study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.focus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="focus", path="ResearchStudy.focus", description="Drugs, devices, etc. under study", type="token" )
  public static final String SP_FOCUS = "focus";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>focus</b>
   * <p>
   * Description: <b>Drugs, devices, etc. under study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.focus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FOCUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FOCUS);

 /**
   * Search parameter: <b>principalinvestigator</b>
   * <p>
   * Description: <b>Researcher who oversees multiple aspects of the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.principalInvestigator</b><br>
   * </p>
   */
  @SearchParamDefinition(name="principalinvestigator", path="ResearchStudy.principalInvestigator", description="Researcher who oversees multiple aspects of the study", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class, PractitionerRole.class } )
  public static final String SP_PRINCIPALINVESTIGATOR = "principalinvestigator";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>principalinvestigator</b>
   * <p>
   * Description: <b>Researcher who oversees multiple aspects of the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.principalInvestigator</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRINCIPALINVESTIGATOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRINCIPALINVESTIGATOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:principalinvestigator</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRINCIPALINVESTIGATOR = new ca.uhn.fhir.model.api.Include("ResearchStudy:principalinvestigator").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Name for this study</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchStudy.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ResearchStudy.title", description="Name for this study", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Name for this study</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchStudy.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>protocol</b>
   * <p>
   * Description: <b>Steps followed in executing study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.protocol</b><br>
   * </p>
   */
  @SearchParamDefinition(name="protocol", path="ResearchStudy.protocol", description="Steps followed in executing study", type="reference", target={PlanDefinition.class } )
  public static final String SP_PROTOCOL = "protocol";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>protocol</b>
   * <p>
   * Description: <b>Steps followed in executing study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.protocol</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROTOCOL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROTOCOL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:protocol</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROTOCOL = new ca.uhn.fhir.model.api.Include("ResearchStudy:protocol").toLocked();

 /**
   * Search parameter: <b>site</b>
   * <p>
   * Description: <b>Facility where study activities are conducted</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.site</b><br>
   * </p>
   */
  @SearchParamDefinition(name="site", path="ResearchStudy.site", description="Facility where study activities are conducted", type="reference", target={Location.class } )
  public static final String SP_SITE = "site";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>site</b>
   * <p>
   * Description: <b>Facility where study activities are conducted</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.site</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SITE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SITE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:site</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SITE = new ca.uhn.fhir.model.api.Include("ResearchStudy:site").toLocked();

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>Geographic region(s) for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="ResearchStudy.location", description="Geographic region(s) for study", type="token" )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>Geographic region(s) for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam LOCATION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_LOCATION);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Classifications for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="ResearchStudy.category", description="Classifications for the study", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Classifications for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>keyword</b>
   * <p>
   * Description: <b>Used to search for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.keyword</b><br>
   * </p>
   */
  @SearchParamDefinition(name="keyword", path="ResearchStudy.keyword", description="Used to search for the study", type="token" )
  public static final String SP_KEYWORD = "keyword";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>keyword</b>
   * <p>
   * Description: <b>Used to search for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.keyword</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam KEYWORD = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_KEYWORD);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | administratively-completed | approved | closed-to-accrual | closed-to-accrual-and-intervention | completed | disapproved | in-review | temporarily-closed-to-accrual | temporarily-closed-to-accrual-and-intervention | withdrawn</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ResearchStudy.status", description="active | administratively-completed | approved | closed-to-accrual | closed-to-accrual-and-intervention | completed | disapproved | in-review | temporarily-closed-to-accrual | temporarily-closed-to-accrual-and-intervention | withdrawn", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | administratively-completed | approved | closed-to-accrual | closed-to-accrual-and-intervention | completed | disapproved | in-review | temporarily-closed-to-accrual | temporarily-closed-to-accrual-and-intervention | withdrawn</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

