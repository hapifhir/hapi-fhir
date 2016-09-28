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

// Generated on Thu, Aug 25, 2016 23:04-0400 for FHIR v1.6.0
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.*;
/**
 * This resource allows for the definition of some activity to be performed, independent of a particular patient, practitioner, or other performance context.
 */
@ResourceDef(name="ActivityDefinition", profile="http://hl7.org/fhir/Profile/ActivityDefinition")
public class ActivityDefinition extends DomainResource {

    public enum ActivityDefinitionStatus {
        /**
         * The module is in draft state
         */
        DRAFT, 
        /**
         * The module is active
         */
        ACTIVE, 
        /**
         * The module is inactive, either rejected before publication, or retired after publication
         */
        INACTIVE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActivityDefinitionStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActivityDefinitionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/module-metadata-status";
            case ACTIVE: return "http://hl7.org/fhir/module-metadata-status";
            case INACTIVE: return "http://hl7.org/fhir/module-metadata-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The module is in draft state";
            case ACTIVE: return "The module is active";
            case INACTIVE: return "The module is inactive, either rejected before publication, or retired after publication";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            default: return "?";
          }
        }
    }

  public static class ActivityDefinitionStatusEnumFactory implements EnumFactory<ActivityDefinitionStatus> {
    public ActivityDefinitionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ActivityDefinitionStatus.DRAFT;
        if ("active".equals(codeString))
          return ActivityDefinitionStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return ActivityDefinitionStatus.INACTIVE;
        throw new IllegalArgumentException("Unknown ActivityDefinitionStatus code '"+codeString+"'");
        }
        public Enumeration<ActivityDefinitionStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ActivityDefinitionStatus>(this, ActivityDefinitionStatus.DRAFT);
        if ("active".equals(codeString))
          return new Enumeration<ActivityDefinitionStatus>(this, ActivityDefinitionStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<ActivityDefinitionStatus>(this, ActivityDefinitionStatus.INACTIVE);
        throw new FHIRException("Unknown ActivityDefinitionStatus code '"+codeString+"'");
        }
    public String toCode(ActivityDefinitionStatus code) {
      if (code == ActivityDefinitionStatus.DRAFT)
        return "draft";
      if (code == ActivityDefinitionStatus.ACTIVE)
        return "active";
      if (code == ActivityDefinitionStatus.INACTIVE)
        return "inactive";
      return "?";
      }
    public String toSystem(ActivityDefinitionStatus code) {
      return code.getSystem();
      }
    }

    public enum ActivityDefinitionCategory {
        /**
         * To communicate with a participant in some way
         */
        COMMUNICATION, 
        /**
         * To use a specific device
         */
        DEVICE, 
        /**
         * To perform a particular diagnostic
         */
        DIAGNOSTIC, 
        /**
         * To consume food of a specified nature
         */
        DIET, 
        /**
         * To consume/receive a drug or other product
         */
        DRUG, 
        /**
         * To meet with the patient (in-patient, out-patient, etc.)
         */
        ENCOUNTER, 
        /**
         * To administer a particular immunization
         */
        IMMUNIZATION, 
        /**
         * To capture information about a patient (vitals, labs, etc.)
         */
        OBSERVATION, 
        /**
         * To modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)
         */
        PROCEDURE, 
        /**
         * To refer the patient to receive some service
         */
        REFERRAL, 
        /**
         * To provide something to the patient (medication, medical supply, etc.)
         */
        SUPPLY, 
        /**
         * To receive a particular vision correction device
         */
        VISION, 
        /**
         * Some other form of action
         */
        OTHER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActivityDefinitionCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("communication".equals(codeString))
          return COMMUNICATION;
        if ("device".equals(codeString))
          return DEVICE;
        if ("diagnostic".equals(codeString))
          return DIAGNOSTIC;
        if ("diet".equals(codeString))
          return DIET;
        if ("drug".equals(codeString))
          return DRUG;
        if ("encounter".equals(codeString))
          return ENCOUNTER;
        if ("immunization".equals(codeString))
          return IMMUNIZATION;
        if ("observation".equals(codeString))
          return OBSERVATION;
        if ("procedure".equals(codeString))
          return PROCEDURE;
        if ("referral".equals(codeString))
          return REFERRAL;
        if ("supply".equals(codeString))
          return SUPPLY;
        if ("vision".equals(codeString))
          return VISION;
        if ("other".equals(codeString))
          return OTHER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMMUNICATION: return "communication";
            case DEVICE: return "device";
            case DIAGNOSTIC: return "diagnostic";
            case DIET: return "diet";
            case DRUG: return "drug";
            case ENCOUNTER: return "encounter";
            case IMMUNIZATION: return "immunization";
            case OBSERVATION: return "observation";
            case PROCEDURE: return "procedure";
            case REFERRAL: return "referral";
            case SUPPLY: return "supply";
            case VISION: return "vision";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMMUNICATION: return "http://hl7.org/fhir/activity-definition-category";
            case DEVICE: return "http://hl7.org/fhir/activity-definition-category";
            case DIAGNOSTIC: return "http://hl7.org/fhir/activity-definition-category";
            case DIET: return "http://hl7.org/fhir/activity-definition-category";
            case DRUG: return "http://hl7.org/fhir/activity-definition-category";
            case ENCOUNTER: return "http://hl7.org/fhir/activity-definition-category";
            case IMMUNIZATION: return "http://hl7.org/fhir/activity-definition-category";
            case OBSERVATION: return "http://hl7.org/fhir/activity-definition-category";
            case PROCEDURE: return "http://hl7.org/fhir/activity-definition-category";
            case REFERRAL: return "http://hl7.org/fhir/activity-definition-category";
            case SUPPLY: return "http://hl7.org/fhir/activity-definition-category";
            case VISION: return "http://hl7.org/fhir/activity-definition-category";
            case OTHER: return "http://hl7.org/fhir/activity-definition-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMMUNICATION: return "To communicate with a participant in some way";
            case DEVICE: return "To use a specific device";
            case DIAGNOSTIC: return "To perform a particular diagnostic";
            case DIET: return "To consume food of a specified nature";
            case DRUG: return "To consume/receive a drug or other product";
            case ENCOUNTER: return "To meet with the patient (in-patient, out-patient, etc.)";
            case IMMUNIZATION: return "To administer a particular immunization";
            case OBSERVATION: return "To capture information about a patient (vitals, labs, etc.)";
            case PROCEDURE: return "To modify the patient in some way (surgery, physiotherapy, education, counseling, etc.)";
            case REFERRAL: return "To refer the patient to receive some service";
            case SUPPLY: return "To provide something to the patient (medication, medical supply, etc.)";
            case VISION: return "To receive a particular vision correction device";
            case OTHER: return "Some other form of action";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMMUNICATION: return "Communication";
            case DEVICE: return "Device";
            case DIAGNOSTIC: return "Diagnostic";
            case DIET: return "Diet";
            case DRUG: return "Drug";
            case ENCOUNTER: return "Encounter";
            case IMMUNIZATION: return "Immunization";
            case OBSERVATION: return "Observation";
            case PROCEDURE: return "Procedure";
            case REFERRAL: return "Referral";
            case SUPPLY: return "Supply";
            case VISION: return "Vision";
            case OTHER: return "Other";
            default: return "?";
          }
        }
    }

  public static class ActivityDefinitionCategoryEnumFactory implements EnumFactory<ActivityDefinitionCategory> {
    public ActivityDefinitionCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("communication".equals(codeString))
          return ActivityDefinitionCategory.COMMUNICATION;
        if ("device".equals(codeString))
          return ActivityDefinitionCategory.DEVICE;
        if ("diagnostic".equals(codeString))
          return ActivityDefinitionCategory.DIAGNOSTIC;
        if ("diet".equals(codeString))
          return ActivityDefinitionCategory.DIET;
        if ("drug".equals(codeString))
          return ActivityDefinitionCategory.DRUG;
        if ("encounter".equals(codeString))
          return ActivityDefinitionCategory.ENCOUNTER;
        if ("immunization".equals(codeString))
          return ActivityDefinitionCategory.IMMUNIZATION;
        if ("observation".equals(codeString))
          return ActivityDefinitionCategory.OBSERVATION;
        if ("procedure".equals(codeString))
          return ActivityDefinitionCategory.PROCEDURE;
        if ("referral".equals(codeString))
          return ActivityDefinitionCategory.REFERRAL;
        if ("supply".equals(codeString))
          return ActivityDefinitionCategory.SUPPLY;
        if ("vision".equals(codeString))
          return ActivityDefinitionCategory.VISION;
        if ("other".equals(codeString))
          return ActivityDefinitionCategory.OTHER;
        throw new IllegalArgumentException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
        }
        public Enumeration<ActivityDefinitionCategory> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("communication".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.COMMUNICATION);
        if ("device".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.DEVICE);
        if ("diagnostic".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.DIAGNOSTIC);
        if ("diet".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.DIET);
        if ("drug".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.DRUG);
        if ("encounter".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.ENCOUNTER);
        if ("immunization".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.IMMUNIZATION);
        if ("observation".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.OBSERVATION);
        if ("procedure".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.PROCEDURE);
        if ("referral".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.REFERRAL);
        if ("supply".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.SUPPLY);
        if ("vision".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.VISION);
        if ("other".equals(codeString))
          return new Enumeration<ActivityDefinitionCategory>(this, ActivityDefinitionCategory.OTHER);
        throw new FHIRException("Unknown ActivityDefinitionCategory code '"+codeString+"'");
        }
    public String toCode(ActivityDefinitionCategory code) {
      if (code == ActivityDefinitionCategory.COMMUNICATION)
        return "communication";
      if (code == ActivityDefinitionCategory.DEVICE)
        return "device";
      if (code == ActivityDefinitionCategory.DIAGNOSTIC)
        return "diagnostic";
      if (code == ActivityDefinitionCategory.DIET)
        return "diet";
      if (code == ActivityDefinitionCategory.DRUG)
        return "drug";
      if (code == ActivityDefinitionCategory.ENCOUNTER)
        return "encounter";
      if (code == ActivityDefinitionCategory.IMMUNIZATION)
        return "immunization";
      if (code == ActivityDefinitionCategory.OBSERVATION)
        return "observation";
      if (code == ActivityDefinitionCategory.PROCEDURE)
        return "procedure";
      if (code == ActivityDefinitionCategory.REFERRAL)
        return "referral";
      if (code == ActivityDefinitionCategory.SUPPLY)
        return "supply";
      if (code == ActivityDefinitionCategory.VISION)
        return "vision";
      if (code == ActivityDefinitionCategory.OTHER)
        return "other";
      return "?";
      }
    public String toSystem(ActivityDefinitionCategory code) {
      return code.getSystem();
      }
    }

    public enum ActivityParticipantType {
        /**
         * The participant is the patient under evaluation
         */
        PATIENT, 
        /**
         * The participant is a practitioner involved in the patient's care
         */
        PRACTITIONER, 
        /**
         * The participant is a person related to the patient
         */
        RELATEDPERSON, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ActivityParticipantType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return PATIENT;
        if ("practitioner".equals(codeString))
          return PRACTITIONER;
        if ("related-person".equals(codeString))
          return RELATEDPERSON;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ActivityParticipantType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "patient";
            case PRACTITIONER: return "practitioner";
            case RELATEDPERSON: return "related-person";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/action-participant-type";
            case PRACTITIONER: return "http://hl7.org/fhir/action-participant-type";
            case RELATEDPERSON: return "http://hl7.org/fhir/action-participant-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The participant is the patient under evaluation";
            case PRACTITIONER: return "The participant is a practitioner involved in the patient's care";
            case RELATEDPERSON: return "The participant is a person related to the patient";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case PRACTITIONER: return "Practitioner";
            case RELATEDPERSON: return "Related Person";
            default: return "?";
          }
        }
    }

  public static class ActivityParticipantTypeEnumFactory implements EnumFactory<ActivityParticipantType> {
    public ActivityParticipantType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient".equals(codeString))
          return ActivityParticipantType.PATIENT;
        if ("practitioner".equals(codeString))
          return ActivityParticipantType.PRACTITIONER;
        if ("related-person".equals(codeString))
          return ActivityParticipantType.RELATEDPERSON;
        throw new IllegalArgumentException("Unknown ActivityParticipantType code '"+codeString+"'");
        }
        public Enumeration<ActivityParticipantType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient".equals(codeString))
          return new Enumeration<ActivityParticipantType>(this, ActivityParticipantType.PATIENT);
        if ("practitioner".equals(codeString))
          return new Enumeration<ActivityParticipantType>(this, ActivityParticipantType.PRACTITIONER);
        if ("related-person".equals(codeString))
          return new Enumeration<ActivityParticipantType>(this, ActivityParticipantType.RELATEDPERSON);
        throw new FHIRException("Unknown ActivityParticipantType code '"+codeString+"'");
        }
    public String toCode(ActivityParticipantType code) {
      if (code == ActivityParticipantType.PATIENT)
        return "patient";
      if (code == ActivityParticipantType.PRACTITIONER)
        return "practitioner";
      if (code == ActivityParticipantType.RELATEDPERSON)
        return "related-person";
      return "?";
      }
    public String toSystem(ActivityParticipantType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ActivityDefinitionDynamicValueComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A brief, natural language description of the intended semantics of the dynamic value.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Natural language description of the dynamic value", formalDefinition="A brief, natural language description of the intended semantics of the dynamic value." )
        protected StringType description;

        /**
         * The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        @Child(name = "path", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The path to the element to be set dynamically", formalDefinition="The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression." )
        protected StringType path;

        /**
         * The media type of the language for the expression.
         */
        @Child(name = "language", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Language of the expression", formalDefinition="The media type of the language for the expression." )
        protected StringType language;

        /**
         * An expression specifying the value of the customized element.
         */
        @Child(name = "expression", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="An expression that provides the dynamic value for the customization", formalDefinition="An expression specifying the value of the customized element." )
        protected StringType expression;

        private static final long serialVersionUID = 448404361L;

    /**
     * Constructor
     */
      public ActivityDefinitionDynamicValueComponent() {
        super();
      }

        /**
         * @return {@link #description} (A brief, natural language description of the intended semantics of the dynamic value.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.description");
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
         * @param value {@link #description} (A brief, natural language description of the intended semantics of the dynamic value.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A brief, natural language description of the intended semantics of the dynamic value.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A brief, natural language description of the intended semantics of the dynamic value.
         */
        public ActivityDefinitionDynamicValueComponent setDescription(String value) { 
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
         * @return {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.
         */
        public ActivityDefinitionDynamicValueComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public StringType getLanguageElement() { 
          if (this.language == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.language");
            else if (Configuration.doAutoCreate())
              this.language = new StringType(); // bb
          return this.language;
        }

        public boolean hasLanguageElement() { 
          return this.language != null && !this.language.isEmpty();
        }

        public boolean hasLanguage() { 
          return this.language != null && !this.language.isEmpty();
        }

        /**
         * @param value {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setLanguageElement(StringType value) { 
          this.language = value;
          return this;
        }

        /**
         * @return The media type of the language for the expression.
         */
        public String getLanguage() { 
          return this.language == null ? null : this.language.getValue();
        }

        /**
         * @param value The media type of the language for the expression.
         */
        public ActivityDefinitionDynamicValueComponent setLanguage(String value) { 
          if (Utilities.noString(value))
            this.language = null;
          else {
            if (this.language == null)
              this.language = new StringType();
            this.language.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActivityDefinitionDynamicValueComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (An expression specifying the value of the customized element.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public ActivityDefinitionDynamicValueComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return An expression specifying the value of the customized element.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value An expression specifying the value of the customized element.
         */
        public ActivityDefinitionDynamicValueComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "A brief, natural language description of the intended semantics of the dynamic value.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("path", "string", "The path to the element to be customized. This is the path on the resource that will hold the result of the calculation defined by the expression.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("language", "string", "The media type of the language for the expression.", 0, java.lang.Integer.MAX_VALUE, language));
          childrenList.add(new Property("expression", "string", "An expression specifying the value of the customized element.", 0, java.lang.Integer.MAX_VALUE, expression));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // StringType
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        case -1613589672: // language
          this.language = castToString(value); // StringType
          break;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("language"))
          this.language = castToString(value); // StringType
        else if (name.equals("expression"))
          this.expression = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        case -1613589672: throw new FHIRException("Cannot make property language as it is not a complex type"); // StringType
        case -1795452264: throw new FHIRException("Cannot make property expression as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.description");
        }
        else if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.path");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.language");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.expression");
        }
        else
          return super.addChild(name);
      }

      public ActivityDefinitionDynamicValueComponent copy() {
        ActivityDefinitionDynamicValueComponent dst = new ActivityDefinitionDynamicValueComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.path = path == null ? null : path.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActivityDefinitionDynamicValueComponent))
          return false;
        ActivityDefinitionDynamicValueComponent o = (ActivityDefinitionDynamicValueComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(path, o.path, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActivityDefinitionDynamicValueComponent))
          return false;
        ActivityDefinitionDynamicValueComponent o = (ActivityDefinitionDynamicValueComponent) other;
        return compareValues(description, o.description, true) && compareValues(path, o.path, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, path, language
          , expression);
      }

  public String fhirType() {
    return "ActivityDefinition.dynamicValue";

  }

  }

    /**
     * An absolute URL that is used to identify this asset when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this asset is (or will be) published.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical URL to reference this asset", formalDefinition="An absolute URL that is used to identify this asset when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this asset is (or will be) published." )
    protected UriType url;

    /**
     * A logical identifier for the asset such as the CMS or NQF identifiers for a measure. Note that at least one identifier is required for non-experimental active assets.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier(s) for the asset", formalDefinition="A logical identifier for the asset such as the CMS or NQF identifiers for a measure. Note that at least one identifier is required for non-experimental active assets." )
    protected List<Identifier> identifier;

    /**
     * The version of the asset, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the asset, if any", formalDefinition="The version of the asset, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets." )
    protected StringType version;

    /**
     * A machine-friendly name for the asset. This name should be usable as an identifier for the asset by machine processing applications such as code generation.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A machine-friendly name for the asset", formalDefinition="A machine-friendly name for the asset. This name should be usable as an identifier for the asset by machine processing applications such as code generation." )
    protected StringType name;

    /**
     * A short, descriptive, user-friendly title for the asset.
     */
    @Child(name = "title", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A user-friendly title for the asset", formalDefinition="A short, descriptive, user-friendly title for the asset." )
    protected StringType title;

    /**
     * The status of the asset.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | inactive", formalDefinition="The status of the asset." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/module-metadata-status")
    protected Enumeration<ActivityDefinitionStatus> status;

    /**
     * Determines whether the asset was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    @Child(name = "experimental", type = {BooleanType.class}, order=6, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="If for testing purposes, not real usage", formalDefinition="Determines whether the asset was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments." )
    protected BooleanType experimental;

    /**
     * A free text natural language description of the asset from the consumer's perspective.
     */
    @Child(name = "description", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Natural language description of the asset", formalDefinition="A free text natural language description of the asset from the consumer's perspective." )
    protected StringType description;

    /**
     * A brief description of the purpose of the asset.
     */
    @Child(name = "purpose", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the purpose of the asset", formalDefinition="A brief description of the purpose of the asset." )
    protected StringType purpose;

    /**
     * A detailed description of how the asset is used from a clinical perspective.
     */
    @Child(name = "usage", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the clinical usage of the asset", formalDefinition="A detailed description of how the asset is used from a clinical perspective." )
    protected StringType usage;

    /**
     * The date on which the asset was published.
     */
    @Child(name = "publicationDate", type = {DateType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Publication date for this version of the asset", formalDefinition="The date on which the asset was published." )
    protected DateType publicationDate;

    /**
     * The date on which the asset content was last reviewed.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Last review date for the asset", formalDefinition="The date on which the asset content was last reviewed." )
    protected DateType lastReviewDate;

    /**
     * The period during which the asset content is effective.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The effective date range for the asset", formalDefinition="The period during which the asset content is effective." )
    protected Period effectivePeriod;

    /**
     * Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge asset is applicable.
     */
    @Child(name = "coverage", type = {UsageContext.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Describes the context of use for this asset", formalDefinition="Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge asset is applicable." )
    protected List<UsageContext> coverage;

    /**
     * Clinical topics related to the content of the asset.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Descriptional topics for the asset", formalDefinition="Clinical topics related to the content of the asset." )
    protected List<CodeableConcept> topic;

    /**
     * A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.
     */
    @Child(name = "contributor", type = {Contributor.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A content contributor", formalDefinition="A contributor to the content of the asset, including authors, editors, reviewers, and endorsers." )
    protected List<Contributor> contributor;

    /**
     * The name of the individual or organization that published the asset (also known as the steward for the asset). This information is required for non-experimental active artifacts.
     */
    @Child(name = "publisher", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name of the publisher (Organization or individual)", formalDefinition="The name of the individual or organization that published the asset (also known as the steward for the asset). This information is required for non-experimental active artifacts." )
    protected StringType publisher;

    /**
     * Contact details to assist a user in finding and communicating with the publisher.
     */
    @Child(name = "contact", type = {ContactDetail.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contact details of the publisher", formalDefinition="Contact details to assist a user in finding and communicating with the publisher." )
    protected List<ContactDetail> contact;

    /**
     * A copyright statement relating to the asset and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the asset.
     */
    @Child(name = "copyright", type = {StringType.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Use and/or publishing restrictions", formalDefinition="A copyright statement relating to the asset and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the asset." )
    protected StringType copyright;

    /**
     * Related resources such as additional documentation, justification, or bibliographic references.
     */
    @Child(name = "relatedResource", type = {RelatedResource.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Related resources for the asset", formalDefinition="Related resources such as additional documentation, justification, or bibliographic references." )
    protected List<RelatedResource> relatedResource;

    /**
     * A reference to a Library resource containing any formal logic used by the asset.
     */
    @Child(name = "library", type = {Library.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the asset", formalDefinition="A reference to a Library resource containing any formal logic used by the asset." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing any formal logic used by the asset.)
     */
    protected List<Library> libraryTarget;


    /**
     * High-level categorization of the type of activity.
     */
    @Child(name = "category", type = {CodeType.class}, order=21, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="communication | device | diagnostic | diet | drug | encounter | immunization | observation | procedure | referral | supply | vision | other", formalDefinition="High-level categorization of the type of activity." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/activity-definition-category")
    protected Enumeration<ActivityDefinitionCategory> category;

    /**
     * Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=22, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Detail type of activity", formalDefinition="Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter." )
    protected CodeableConcept code;

    /**
     * The period, timing or frequency upon which the described activity is to occur.
     */
    @Child(name = "timing", type = {CodeableConcept.class, Timing.class}, order=23, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When activity is to occur", formalDefinition="The period, timing or frequency upon which the described activity is to occur." )
    protected Type timing;

    /**
     * Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.
     */
    @Child(name = "location", type = {Location.class}, order=24, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Where it should happen", formalDefinition="Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    protected Location locationTarget;

    /**
     * The type of participant in the action.
     */
    @Child(name = "participantType", type = {CodeType.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="patient | practitioner | related-person", formalDefinition="The type of participant in the action." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/action-participant-type")
    protected List<Enumeration<ActivityParticipantType>> participantType;

    /**
     * Identifies the food, drug or other product being consumed or supplied in the activity.
     */
    @Child(name = "product", type = {Medication.class, Substance.class, CodeableConcept.class}, order=26, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What's administered/supplied", formalDefinition="Identifies the food, drug or other product being consumed or supplied in the activity." )
    protected Type product;

    /**
     * Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).
     */
    @Child(name = "quantity", type = {SimpleQuantity.class}, order=27, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="How much is administered/consumed/supplied", formalDefinition="Identifies the quantity expected to be consumed at once (per dose, per meal, etc.)." )
    protected SimpleQuantity quantity;

    /**
     * A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.
     */
    @Child(name = "transform", type = {StructureMap.class}, order=28, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Transform to apply the template", formalDefinition="A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input." )
    protected Reference transform;

    /**
     * The actual object that is the target of the reference (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    protected StructureMap transformTarget;

    /**
     * Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result.
     */
    @Child(name = "dynamicValue", type = {}, order=29, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Dynamic aspects of the definition", formalDefinition="Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result." )
    protected List<ActivityDefinitionDynamicValueComponent> dynamicValue;

    private static final long serialVersionUID = -606309039L;

  /**
   * Constructor
   */
    public ActivityDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public ActivityDefinition(Enumeration<ActivityDefinitionStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #url} (An absolute URL that is used to identify this asset when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this asset is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (An absolute URL that is used to identify this asset when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this asset is (or will be) published.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public ActivityDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URL that is used to identify this asset when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this asset is (or will be) published.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URL that is used to identify this asset when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this asset is (or will be) published.
     */
    public ActivityDefinition setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (A logical identifier for the asset such as the CMS or NQF identifiers for a measure. Note that at least one identifier is required for non-experimental active assets.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setIdentifier(List<Identifier> theIdentifier) { 
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

    public ActivityDefinition addIdentifier(Identifier t) { //3
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
     * @return {@link #version} (The version of the asset, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The version of the asset, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ActivityDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the asset, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the asset, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.
     */
    public ActivityDefinition setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #name} (A machine-friendly name for the asset. This name should be usable as an identifier for the asset by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.name");
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
     * @param value {@link #name} (A machine-friendly name for the asset. This name should be usable as an identifier for the asset by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public ActivityDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A machine-friendly name for the asset. This name should be usable as an identifier for the asset by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A machine-friendly name for the asset. This name should be usable as an identifier for the asset by machine processing applications such as code generation.
     */
    public ActivityDefinition setName(String value) { 
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
     * @return {@link #title} (A short, descriptive, user-friendly title for the asset.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.title");
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
     * @param value {@link #title} (A short, descriptive, user-friendly title for the asset.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ActivityDefinition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for the asset.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for the asset.
     */
    public ActivityDefinition setTitle(String value) { 
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
     * @return {@link #status} (The status of the asset.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ActivityDefinitionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ActivityDefinitionStatus>(new ActivityDefinitionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the asset.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ActivityDefinition setStatusElement(Enumeration<ActivityDefinitionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the asset.
     */
    public ActivityDefinitionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the asset.
     */
    public ActivityDefinition setStatus(ActivityDefinitionStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ActivityDefinitionStatus>(new ActivityDefinitionStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (Determines whether the asset was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (Determines whether the asset was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public ActivityDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return Determines whether the asset was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value Determines whether the asset was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.
     */
    public ActivityDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #description} (A free text natural language description of the asset from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.description");
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
     * @param value {@link #description} (A free text natural language description of the asset from the consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ActivityDefinition setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the asset from the consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the asset from the consumer's perspective.
     */
    public ActivityDefinition setDescription(String value) { 
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
     * @return {@link #purpose} (A brief description of the purpose of the asset.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public StringType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new StringType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (A brief description of the purpose of the asset.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public ActivityDefinition setPurposeElement(StringType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return A brief description of the purpose of the asset.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value A brief description of the purpose of the asset.
     */
    public ActivityDefinition setPurpose(String value) { 
      if (Utilities.noString(value))
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new StringType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #usage} (A detailed description of how the asset is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.usage");
        else if (Configuration.doAutoCreate())
          this.usage = new StringType(); // bb
      return this.usage;
    }

    public boolean hasUsageElement() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    public boolean hasUsage() { 
      return this.usage != null && !this.usage.isEmpty();
    }

    /**
     * @param value {@link #usage} (A detailed description of how the asset is used from a clinical perspective.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public ActivityDefinition setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return A detailed description of how the asset is used from a clinical perspective.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value A detailed description of how the asset is used from a clinical perspective.
     */
    public ActivityDefinition setUsage(String value) { 
      if (Utilities.noString(value))
        this.usage = null;
      else {
        if (this.usage == null)
          this.usage = new StringType();
        this.usage.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publicationDate} (The date on which the asset was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public DateType getPublicationDateElement() { 
      if (this.publicationDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.publicationDate");
        else if (Configuration.doAutoCreate())
          this.publicationDate = new DateType(); // bb
      return this.publicationDate;
    }

    public boolean hasPublicationDateElement() { 
      return this.publicationDate != null && !this.publicationDate.isEmpty();
    }

    public boolean hasPublicationDate() { 
      return this.publicationDate != null && !this.publicationDate.isEmpty();
    }

    /**
     * @param value {@link #publicationDate} (The date on which the asset was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public ActivityDefinition setPublicationDateElement(DateType value) { 
      this.publicationDate = value;
      return this;
    }

    /**
     * @return The date on which the asset was published.
     */
    public Date getPublicationDate() { 
      return this.publicationDate == null ? null : this.publicationDate.getValue();
    }

    /**
     * @param value The date on which the asset was published.
     */
    public ActivityDefinition setPublicationDate(Date value) { 
      if (value == null)
        this.publicationDate = null;
      else {
        if (this.publicationDate == null)
          this.publicationDate = new DateType();
        this.publicationDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #lastReviewDate} (The date on which the asset content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.lastReviewDate");
        else if (Configuration.doAutoCreate())
          this.lastReviewDate = new DateType(); // bb
      return this.lastReviewDate;
    }

    public boolean hasLastReviewDateElement() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    public boolean hasLastReviewDate() { 
      return this.lastReviewDate != null && !this.lastReviewDate.isEmpty();
    }

    /**
     * @param value {@link #lastReviewDate} (The date on which the asset content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public ActivityDefinition setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the asset content was last reviewed.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the asset content was last reviewed.
     */
    public ActivityDefinition setLastReviewDate(Date value) { 
      if (value == null)
        this.lastReviewDate = null;
      else {
        if (this.lastReviewDate == null)
          this.lastReviewDate = new DateType();
        this.lastReviewDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #effectivePeriod} (The period during which the asset content is effective.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the asset content is effective.)
     */
    public ActivityDefinition setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge asset is applicable.)
     */
    public List<UsageContext> getCoverage() { 
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      return this.coverage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setCoverage(List<UsageContext> theCoverage) { 
      this.coverage = theCoverage;
      return this;
    }

    public boolean hasCoverage() { 
      if (this.coverage == null)
        return false;
      for (UsageContext item : this.coverage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addCoverage() { //3
      UsageContext t = new UsageContext();
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      this.coverage.add(t);
      return t;
    }

    public ActivityDefinition addCoverage(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.coverage == null)
        this.coverage = new ArrayList<UsageContext>();
      this.coverage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #coverage}, creating it if it does not already exist
     */
    public UsageContext getCoverageFirstRep() { 
      if (getCoverage().isEmpty()) {
        addCoverage();
      }
      return getCoverage().get(0);
    }

    /**
     * @return {@link #topic} (Clinical topics related to the content of the asset.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setTopic(List<CodeableConcept> theTopic) { 
      this.topic = theTopic;
      return this;
    }

    public boolean hasTopic() { 
      if (this.topic == null)
        return false;
      for (CodeableConcept item : this.topic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addTopic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return t;
    }

    public ActivityDefinition addTopic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #topic}, creating it if it does not already exist
     */
    public CodeableConcept getTopicFirstRep() { 
      if (getTopic().isEmpty()) {
        addTopic();
      }
      return getTopic().get(0);
    }

    /**
     * @return {@link #contributor} (A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.)
     */
    public List<Contributor> getContributor() { 
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      return this.contributor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setContributor(List<Contributor> theContributor) { 
      this.contributor = theContributor;
      return this;
    }

    public boolean hasContributor() { 
      if (this.contributor == null)
        return false;
      for (Contributor item : this.contributor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Contributor addContributor() { //3
      Contributor t = new Contributor();
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return t;
    }

    public ActivityDefinition addContributor(Contributor t) { //3
      if (t == null)
        return this;
      if (this.contributor == null)
        this.contributor = new ArrayList<Contributor>();
      this.contributor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contributor}, creating it if it does not already exist
     */
    public Contributor getContributorFirstRep() { 
      if (getContributor().isEmpty()) {
        addContributor();
      }
      return getContributor().get(0);
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the asset (also known as the steward for the asset). This information is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the asset (also known as the steward for the asset). This information is required for non-experimental active artifacts.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public ActivityDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the asset (also known as the steward for the asset). This information is required for non-experimental active artifacts.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the asset (also known as the steward for the asset). This information is required for non-experimental active artifacts.
     */
    public ActivityDefinition setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setContact(List<ContactDetail> theContact) { 
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

    public ActivityDefinition addContact(ContactDetail t) { //3
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
     * @return {@link #copyright} (A copyright statement relating to the asset and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the asset.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public StringType getCopyrightElement() { 
      if (this.copyright == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.copyright");
        else if (Configuration.doAutoCreate())
          this.copyright = new StringType(); // bb
      return this.copyright;
    }

    public boolean hasCopyrightElement() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    public boolean hasCopyright() { 
      return this.copyright != null && !this.copyright.isEmpty();
    }

    /**
     * @param value {@link #copyright} (A copyright statement relating to the asset and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the asset.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
     */
    public ActivityDefinition setCopyrightElement(StringType value) { 
      this.copyright = value;
      return this;
    }

    /**
     * @return A copyright statement relating to the asset and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the asset.
     */
    public String getCopyright() { 
      return this.copyright == null ? null : this.copyright.getValue();
    }

    /**
     * @param value A copyright statement relating to the asset and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the asset.
     */
    public ActivityDefinition setCopyright(String value) { 
      if (Utilities.noString(value))
        this.copyright = null;
      else {
        if (this.copyright == null)
          this.copyright = new StringType();
        this.copyright.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relatedResource} (Related resources such as additional documentation, justification, or bibliographic references.)
     */
    public List<RelatedResource> getRelatedResource() { 
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      return this.relatedResource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setRelatedResource(List<RelatedResource> theRelatedResource) { 
      this.relatedResource = theRelatedResource;
      return this;
    }

    public boolean hasRelatedResource() { 
      if (this.relatedResource == null)
        return false;
      for (RelatedResource item : this.relatedResource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedResource addRelatedResource() { //3
      RelatedResource t = new RelatedResource();
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      this.relatedResource.add(t);
      return t;
    }

    public ActivityDefinition addRelatedResource(RelatedResource t) { //3
      if (t == null)
        return this;
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<RelatedResource>();
      this.relatedResource.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedResource}, creating it if it does not already exist
     */
    public RelatedResource getRelatedResourceFirstRep() { 
      if (getRelatedResource().isEmpty()) {
        addRelatedResource();
      }
      return getRelatedResource().get(0);
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing any formal logic used by the asset.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setLibrary(List<Reference> theLibrary) { 
      this.library = theLibrary;
      return this;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    public ActivityDefinition addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #library}, creating it if it does not already exist
     */
    public Reference getLibraryFirstRep() { 
      if (getLibrary().isEmpty()) {
        addLibrary();
      }
      return getLibrary().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #category} (High-level categorization of the type of activity.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<ActivityDefinitionCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<ActivityDefinitionCategory>(new ActivityDefinitionCategoryEnumFactory()); // bb
      return this.category;
    }

    public boolean hasCategoryElement() { 
      return this.category != null && !this.category.isEmpty();
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (High-level categorization of the type of activity.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public ActivityDefinition setCategoryElement(Enumeration<ActivityDefinitionCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return High-level categorization of the type of activity.
     */
    public ActivityDefinitionCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value High-level categorization of the type of activity.
     */
    public ActivityDefinition setCategory(ActivityDefinitionCategory value) { 
      if (value == null)
        this.category = null;
      else {
        if (this.category == null)
          this.category = new Enumeration<ActivityDefinitionCategory>(new ActivityDefinitionCategoryEnumFactory());
        this.category.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #code} (Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.)
     */
    public ActivityDefinition setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public Type getTiming() { 
      return this.timing;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public CodeableConcept getTimingCodeableConcept() throws FHIRException { 
      if (!(this.timing instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (CodeableConcept) this.timing;
    }

    public boolean hasTimingCodeableConcept() { 
      return this.timing instanceof CodeableConcept;
    }

    /**
     * @return {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public Timing getTimingTiming() throws FHIRException { 
      if (!(this.timing instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.timing.getClass().getName()+" was encountered");
      return (Timing) this.timing;
    }

    public boolean hasTimingTiming() { 
      return this.timing instanceof Timing;
    }

    public boolean hasTiming() { 
      return this.timing != null && !this.timing.isEmpty();
    }

    /**
     * @param value {@link #timing} (The period, timing or frequency upon which the described activity is to occur.)
     */
    public ActivityDefinition setTiming(Type value) { 
      this.timing = value;
      return this;
    }

    /**
     * @return {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public ActivityDefinition setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.)
     */
    public ActivityDefinition setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #participantType} (The type of participant in the action.)
     */
    public List<Enumeration<ActivityParticipantType>> getParticipantType() { 
      if (this.participantType == null)
        this.participantType = new ArrayList<Enumeration<ActivityParticipantType>>();
      return this.participantType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setParticipantType(List<Enumeration<ActivityParticipantType>> theParticipantType) { 
      this.participantType = theParticipantType;
      return this;
    }

    public boolean hasParticipantType() { 
      if (this.participantType == null)
        return false;
      for (Enumeration<ActivityParticipantType> item : this.participantType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participantType} (The type of participant in the action.)
     */
    public Enumeration<ActivityParticipantType> addParticipantTypeElement() {//2 
      Enumeration<ActivityParticipantType> t = new Enumeration<ActivityParticipantType>(new ActivityParticipantTypeEnumFactory());
      if (this.participantType == null)
        this.participantType = new ArrayList<Enumeration<ActivityParticipantType>>();
      this.participantType.add(t);
      return t;
    }

    /**
     * @param value {@link #participantType} (The type of participant in the action.)
     */
    public ActivityDefinition addParticipantType(ActivityParticipantType value) { //1
      Enumeration<ActivityParticipantType> t = new Enumeration<ActivityParticipantType>(new ActivityParticipantTypeEnumFactory());
      t.setValue(value);
      if (this.participantType == null)
        this.participantType = new ArrayList<Enumeration<ActivityParticipantType>>();
      this.participantType.add(t);
      return this;
    }

    /**
     * @param value {@link #participantType} (The type of participant in the action.)
     */
    public boolean hasParticipantType(ActivityParticipantType value) { 
      if (this.participantType == null)
        return false;
      for (Enumeration<ActivityParticipantType> v : this.participantType)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public Type getProduct() { 
      return this.product;
    }

    /**
     * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public Reference getProductReference() throws FHIRException { 
      if (!(this.product instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.product.getClass().getName()+" was encountered");
      return (Reference) this.product;
    }

    public boolean hasProductReference() { 
      return this.product instanceof Reference;
    }

    /**
     * @return {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public CodeableConcept getProductCodeableConcept() throws FHIRException { 
      if (!(this.product instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.product.getClass().getName()+" was encountered");
      return (CodeableConcept) this.product;
    }

    public boolean hasProductCodeableConcept() { 
      return this.product instanceof CodeableConcept;
    }

    public boolean hasProduct() { 
      return this.product != null && !this.product.isEmpty();
    }

    /**
     * @param value {@link #product} (Identifies the food, drug or other product being consumed or supplied in the activity.)
     */
    public ActivityDefinition setProduct(Type value) { 
      this.product = value;
      return this;
    }

    /**
     * @return {@link #quantity} (Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).)
     */
    public SimpleQuantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new SimpleQuantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).)
     */
    public ActivityDefinition setQuantity(SimpleQuantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #transform} (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public Reference getTransform() { 
      if (this.transform == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.transform");
        else if (Configuration.doAutoCreate())
          this.transform = new Reference(); // cc
      return this.transform;
    }

    public boolean hasTransform() { 
      return this.transform != null && !this.transform.isEmpty();
    }

    /**
     * @param value {@link #transform} (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public ActivityDefinition setTransform(Reference value) { 
      this.transform = value;
      return this;
    }

    /**
     * @return {@link #transform} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public StructureMap getTransformTarget() { 
      if (this.transformTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ActivityDefinition.transform");
        else if (Configuration.doAutoCreate())
          this.transformTarget = new StructureMap(); // aa
      return this.transformTarget;
    }

    /**
     * @param value {@link #transform} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.)
     */
    public ActivityDefinition setTransformTarget(StructureMap value) { 
      this.transformTarget = value;
      return this;
    }

    /**
     * @return {@link #dynamicValue} (Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result.)
     */
    public List<ActivityDefinitionDynamicValueComponent> getDynamicValue() { 
      if (this.dynamicValue == null)
        this.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
      return this.dynamicValue;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ActivityDefinition setDynamicValue(List<ActivityDefinitionDynamicValueComponent> theDynamicValue) { 
      this.dynamicValue = theDynamicValue;
      return this;
    }

    public boolean hasDynamicValue() { 
      if (this.dynamicValue == null)
        return false;
      for (ActivityDefinitionDynamicValueComponent item : this.dynamicValue)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ActivityDefinitionDynamicValueComponent addDynamicValue() { //3
      ActivityDefinitionDynamicValueComponent t = new ActivityDefinitionDynamicValueComponent();
      if (this.dynamicValue == null)
        this.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
      this.dynamicValue.add(t);
      return t;
    }

    public ActivityDefinition addDynamicValue(ActivityDefinitionDynamicValueComponent t) { //3
      if (t == null)
        return this;
      if (this.dynamicValue == null)
        this.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
      this.dynamicValue.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dynamicValue}, creating it if it does not already exist
     */
    public ActivityDefinitionDynamicValueComponent getDynamicValueFirstRep() { 
      if (getDynamicValue().isEmpty()) {
        addDynamicValue();
      }
      return getDynamicValue().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URL that is used to identify this asset when it is referenced. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this asset is (or will be) published.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the asset such as the CMS or NQF identifiers for a measure. Note that at least one identifier is required for non-experimental active assets.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the asset, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge assets, refer to the Decision Support Service specification. Note that a version is required for non-experimental active assets.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A machine-friendly name for the asset. This name should be usable as an identifier for the asset by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("title", "string", "A short, descriptive, user-friendly title for the asset.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("status", "code", "The status of the asset.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "Determines whether the asset was developed for testing purposes (or education/evaluation/marketing), and is not intended to be used in production environments.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("description", "string", "A free text natural language description of the asset from the consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "string", "A brief description of the purpose of the asset.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "A detailed description of how the asset is used from a clinical perspective.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("publicationDate", "date", "The date on which the asset was published.", 0, java.lang.Integer.MAX_VALUE, publicationDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the asset content was last reviewed.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the asset content is effective.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("coverage", "UsageContext", "Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge asset is applicable.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("topic", "CodeableConcept", "Clinical topics related to the content of the asset.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("contributor", "Contributor", "A contributor to the content of the asset, including authors, editors, reviewers, and endorsers.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the asset (also known as the steward for the asset). This information is required for non-experimental active artifacts.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("copyright", "string", "A copyright statement relating to the asset and/or its contents. Copyright statements are generally legal restrictions on the use and publishing of the asset.", 0, java.lang.Integer.MAX_VALUE, copyright));
        childrenList.add(new Property("relatedResource", "RelatedResource", "Related resources such as additional documentation, justification, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedResource));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing any formal logic used by the asset.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("category", "code", "High-level categorization of the type of activity.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "Detailed description of the type of activity; e.g. What lab test, what procedure, what kind of encounter.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("timing[x]", "CodeableConcept|Timing", "The period, timing or frequency upon which the described activity is to occur.", 0, java.lang.Integer.MAX_VALUE, timing));
        childrenList.add(new Property("location", "Reference(Location)", "Identifies the facility where the activity will occur; e.g. home, hospital, specific clinic, etc.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("participantType", "code", "The type of participant in the action.", 0, java.lang.Integer.MAX_VALUE, participantType));
        childrenList.add(new Property("product[x]", "Reference(Medication|Substance)|CodeableConcept", "Identifies the food, drug or other product being consumed or supplied in the activity.", 0, java.lang.Integer.MAX_VALUE, product));
        childrenList.add(new Property("quantity", "SimpleQuantity", "Identifies the quantity expected to be consumed at once (per dose, per meal, etc.).", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("transform", "Reference(StructureMap)", "A reference to a StructureMap resource that defines a transform that can be executed to produce the intent resource using the ActivityDefinition instance as the input.", 0, java.lang.Integer.MAX_VALUE, transform));
        childrenList.add(new Property("dynamicValue", "", "Dynamic values that will be evaluated to produce values for elements of the resulting resource. For example, if the dosage of a medication must be computed based on the patient's weight, a dynamic value would be used to specify an expression that calculated the weight, and the path on the intent resource that would contain the result.", 0, java.lang.Integer.MAX_VALUE, dynamicValue));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ActivityDefinitionStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // StringType
        case 111574433: /*usage*/ return this.usage == null ? new Base[0] : new Base[] {this.usage}; // StringType
        case 1470566394: /*publicationDate*/ return this.publicationDate == null ? new Base[0] : new Base[] {this.publicationDate}; // DateType
        case -1687512484: /*lastReviewDate*/ return this.lastReviewDate == null ? new Base[0] : new Base[] {this.lastReviewDate}; // DateType
        case -403934648: /*effectivePeriod*/ return this.effectivePeriod == null ? new Base[0] : new Base[] {this.effectivePeriod}; // Period
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : this.coverage.toArray(new Base[this.coverage.size()]); // UsageContext
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // CodeableConcept
        case -1895276325: /*contributor*/ return this.contributor == null ? new Base[0] : this.contributor.toArray(new Base[this.contributor.size()]); // Contributor
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // StringType
        case 1554540889: /*relatedResource*/ return this.relatedResource == null ? new Base[0] : this.relatedResource.toArray(new Base[this.relatedResource.size()]); // RelatedResource
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // Reference
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<ActivityDefinitionCategory>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -873664438: /*timing*/ return this.timing == null ? new Base[0] : new Base[] {this.timing}; // Type
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case 841294093: /*participantType*/ return this.participantType == null ? new Base[0] : this.participantType.toArray(new Base[this.participantType.size()]); // Enumeration<ActivityParticipantType>
        case -309474065: /*product*/ return this.product == null ? new Base[0] : new Base[] {this.product}; // Type
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case 1052666732: /*transform*/ return this.transform == null ? new Base[0] : new Base[] {this.transform}; // Reference
        case 572625010: /*dynamicValue*/ return this.dynamicValue == null ? new Base[0] : this.dynamicValue.toArray(new Base[this.dynamicValue.size()]); // ActivityDefinitionDynamicValueComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case -892481550: // status
          this.status = new ActivityDefinitionStatusEnumFactory().fromType(value); // Enumeration<ActivityDefinitionStatus>
          break;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case -220463842: // purpose
          this.purpose = castToString(value); // StringType
          break;
        case 111574433: // usage
          this.usage = castToString(value); // StringType
          break;
        case 1470566394: // publicationDate
          this.publicationDate = castToDate(value); // DateType
          break;
        case -1687512484: // lastReviewDate
          this.lastReviewDate = castToDate(value); // DateType
          break;
        case -403934648: // effectivePeriod
          this.effectivePeriod = castToPeriod(value); // Period
          break;
        case -351767064: // coverage
          this.getCoverage().add(castToUsageContext(value)); // UsageContext
          break;
        case 110546223: // topic
          this.getTopic().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1895276325: // contributor
          this.getContributor().add(castToContributor(value)); // Contributor
          break;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          break;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          break;
        case 1522889671: // copyright
          this.copyright = castToString(value); // StringType
          break;
        case 1554540889: // relatedResource
          this.getRelatedResource().add(castToRelatedResource(value)); // RelatedResource
          break;
        case 166208699: // library
          this.getLibrary().add(castToReference(value)); // Reference
          break;
        case 50511102: // category
          this.category = new ActivityDefinitionCategoryEnumFactory().fromType(value); // Enumeration<ActivityDefinitionCategory>
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -873664438: // timing
          this.timing = (Type) value; // Type
          break;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          break;
        case 841294093: // participantType
          this.getParticipantType().add(new ActivityParticipantTypeEnumFactory().fromType(value)); // Enumeration<ActivityParticipantType>
          break;
        case -309474065: // product
          this.product = (Type) value; // Type
          break;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          break;
        case 1052666732: // transform
          this.transform = castToReference(value); // Reference
          break;
        case 572625010: // dynamicValue
          this.getDynamicValue().add((ActivityDefinitionDynamicValueComponent) value); // ActivityDefinitionDynamicValueComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("status"))
          this.status = new ActivityDefinitionStatusEnumFactory().fromType(value); // Enumeration<ActivityDefinitionStatus>
        else if (name.equals("experimental"))
          this.experimental = castToBoolean(value); // BooleanType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("purpose"))
          this.purpose = castToString(value); // StringType
        else if (name.equals("usage"))
          this.usage = castToString(value); // StringType
        else if (name.equals("publicationDate"))
          this.publicationDate = castToDate(value); // DateType
        else if (name.equals("lastReviewDate"))
          this.lastReviewDate = castToDate(value); // DateType
        else if (name.equals("effectivePeriod"))
          this.effectivePeriod = castToPeriod(value); // Period
        else if (name.equals("coverage"))
          this.getCoverage().add(castToUsageContext(value));
        else if (name.equals("topic"))
          this.getTopic().add(castToCodeableConcept(value));
        else if (name.equals("contributor"))
          this.getContributor().add(castToContributor(value));
        else if (name.equals("publisher"))
          this.publisher = castToString(value); // StringType
        else if (name.equals("contact"))
          this.getContact().add(castToContactDetail(value));
        else if (name.equals("copyright"))
          this.copyright = castToString(value); // StringType
        else if (name.equals("relatedResource"))
          this.getRelatedResource().add(castToRelatedResource(value));
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("category"))
          this.category = new ActivityDefinitionCategoryEnumFactory().fromType(value); // Enumeration<ActivityDefinitionCategory>
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("timing[x]"))
          this.timing = (Type) value; // Type
        else if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("participantType"))
          this.getParticipantType().add(new ActivityParticipantTypeEnumFactory().fromType(value));
        else if (name.equals("product[x]"))
          this.product = (Type) value; // Type
        else if (name.equals("quantity"))
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        else if (name.equals("transform"))
          this.transform = castToReference(value); // Reference
        else if (name.equals("dynamicValue"))
          this.getDynamicValue().add((ActivityDefinitionDynamicValueComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -1618432855:  return addIdentifier(); // Identifier
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ActivityDefinitionStatus>
        case -404562712: throw new FHIRException("Cannot make property experimental as it is not a complex type"); // BooleanType
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case -220463842: throw new FHIRException("Cannot make property purpose as it is not a complex type"); // StringType
        case 111574433: throw new FHIRException("Cannot make property usage as it is not a complex type"); // StringType
        case 1470566394: throw new FHIRException("Cannot make property publicationDate as it is not a complex type"); // DateType
        case -1687512484: throw new FHIRException("Cannot make property lastReviewDate as it is not a complex type"); // DateType
        case -403934648:  return getEffectivePeriod(); // Period
        case -351767064:  return addCoverage(); // UsageContext
        case 110546223:  return addTopic(); // CodeableConcept
        case -1895276325:  return addContributor(); // Contributor
        case 1447404028: throw new FHIRException("Cannot make property publisher as it is not a complex type"); // StringType
        case 951526432:  return addContact(); // ContactDetail
        case 1522889671: throw new FHIRException("Cannot make property copyright as it is not a complex type"); // StringType
        case 1554540889:  return addRelatedResource(); // RelatedResource
        case 166208699:  return addLibrary(); // Reference
        case 50511102: throw new FHIRException("Cannot make property category as it is not a complex type"); // Enumeration<ActivityDefinitionCategory>
        case 3059181:  return getCode(); // CodeableConcept
        case 164632566:  return getTiming(); // Type
        case 1901043637:  return getLocation(); // Reference
        case 841294093: throw new FHIRException("Cannot make property participantType as it is not a complex type"); // Enumeration<ActivityParticipantType>
        case 1753005361:  return getProduct(); // Type
        case -1285004149:  return getQuantity(); // SimpleQuantity
        case 1052666732:  return getTransform(); // Reference
        case 572625010:  return addDynamicValue(); // ActivityDefinitionDynamicValueComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.url");
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.title");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.experimental");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.purpose");
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.usage");
        }
        else if (name.equals("publicationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.publicationDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.lastReviewDate");
        }
        else if (name.equals("effectivePeriod")) {
          this.effectivePeriod = new Period();
          return this.effectivePeriod;
        }
        else if (name.equals("coverage")) {
          return addCoverage();
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.copyright");
        }
        else if (name.equals("relatedResource")) {
          return addRelatedResource();
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.category");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("timingCodeableConcept")) {
          this.timing = new CodeableConcept();
          return this.timing;
        }
        else if (name.equals("timingTiming")) {
          this.timing = new Timing();
          return this.timing;
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("participantType")) {
          throw new FHIRException("Cannot call addChild on a primitive type ActivityDefinition.participantType");
        }
        else if (name.equals("productReference")) {
          this.product = new Reference();
          return this.product;
        }
        else if (name.equals("productCodeableConcept")) {
          this.product = new CodeableConcept();
          return this.product;
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("transform")) {
          this.transform = new Reference();
          return this.transform;
        }
        else if (name.equals("dynamicValue")) {
          return addDynamicValue();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ActivityDefinition";

  }

      public ActivityDefinition copy() {
        ActivityDefinition dst = new ActivityDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.publicationDate = publicationDate == null ? null : publicationDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (coverage != null) {
          dst.coverage = new ArrayList<UsageContext>();
          for (UsageContext i : coverage)
            dst.coverage.add(i.copy());
        };
        if (topic != null) {
          dst.topic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : topic)
            dst.topic.add(i.copy());
        };
        if (contributor != null) {
          dst.contributor = new ArrayList<Contributor>();
          for (Contributor i : contributor)
            dst.contributor.add(i.copy());
        };
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.copyright = copyright == null ? null : copyright.copy();
        if (relatedResource != null) {
          dst.relatedResource = new ArrayList<RelatedResource>();
          for (RelatedResource i : relatedResource)
            dst.relatedResource.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.timing = timing == null ? null : timing.copy();
        dst.location = location == null ? null : location.copy();
        if (participantType != null) {
          dst.participantType = new ArrayList<Enumeration<ActivityParticipantType>>();
          for (Enumeration<ActivityParticipantType> i : participantType)
            dst.participantType.add(i.copy());
        };
        dst.product = product == null ? null : product.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.transform = transform == null ? null : transform.copy();
        if (dynamicValue != null) {
          dst.dynamicValue = new ArrayList<ActivityDefinitionDynamicValueComponent>();
          for (ActivityDefinitionDynamicValueComponent i : dynamicValue)
            dst.dynamicValue.add(i.copy());
        };
        return dst;
      }

      protected ActivityDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ActivityDefinition))
          return false;
        ActivityDefinition o = (ActivityDefinition) other;
        return compareDeep(url, o.url, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(name, o.name, true) && compareDeep(title, o.title, true) && compareDeep(status, o.status, true)
           && compareDeep(experimental, o.experimental, true) && compareDeep(description, o.description, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(usage, o.usage, true) && compareDeep(publicationDate, o.publicationDate, true)
           && compareDeep(lastReviewDate, o.lastReviewDate, true) && compareDeep(effectivePeriod, o.effectivePeriod, true)
           && compareDeep(coverage, o.coverage, true) && compareDeep(topic, o.topic, true) && compareDeep(contributor, o.contributor, true)
           && compareDeep(publisher, o.publisher, true) && compareDeep(contact, o.contact, true) && compareDeep(copyright, o.copyright, true)
           && compareDeep(relatedResource, o.relatedResource, true) && compareDeep(library, o.library, true)
           && compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(timing, o.timing, true)
           && compareDeep(location, o.location, true) && compareDeep(participantType, o.participantType, true)
           && compareDeep(product, o.product, true) && compareDeep(quantity, o.quantity, true) && compareDeep(transform, o.transform, true)
           && compareDeep(dynamicValue, o.dynamicValue, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ActivityDefinition))
          return false;
        ActivityDefinition o = (ActivityDefinition) other;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(name, o.name, true)
           && compareValues(title, o.title, true) && compareValues(status, o.status, true) && compareValues(experimental, o.experimental, true)
           && compareValues(description, o.description, true) && compareValues(purpose, o.purpose, true) && compareValues(usage, o.usage, true)
           && compareValues(publicationDate, o.publicationDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
           && compareValues(publisher, o.publisher, true) && compareValues(copyright, o.copyright, true) && compareValues(category, o.category, true)
           && compareValues(participantType, o.participantType, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(url, identifier, version
          , name, title, status, experimental, description, purpose, usage, publicationDate
          , lastReviewDate, effectivePeriod, coverage, topic, contributor, publisher, contact
          , copyright, relatedResource, library, category, code, timing, location, participantType
          , product, quantity, transform, dynamicValue);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ActivityDefinition;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ActivityDefinition.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="ActivityDefinition.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ActivityDefinition.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ActivityDefinition.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ActivityDefinition.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ActivityDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ActivityDefinition.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ActivityDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

