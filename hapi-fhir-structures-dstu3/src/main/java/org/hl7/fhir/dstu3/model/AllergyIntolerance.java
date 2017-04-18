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
 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
 */
@ResourceDef(name="AllergyIntolerance", profile="http://hl7.org/fhir/Profile/AllergyIntolerance")
public class AllergyIntolerance extends DomainResource {

    public enum AllergyIntoleranceClinicalStatus {
        /**
         * An active record of a risk of a reaction to the identified substance.
         */
        ACTIVE, 
        /**
         * An inactivated record of a risk of a reaction to the identified substance.
         */
        INACTIVE, 
        /**
         * A reaction to the identified substance has been clinically reassessed by testing or re-exposure and considered to be resolved.
         */
        RESOLVED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AllergyIntoleranceClinicalStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("resolved".equals(codeString))
          return RESOLVED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceClinicalStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case RESOLVED: return "resolved";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/allergy-clinical-status";
            case INACTIVE: return "http://hl7.org/fhir/allergy-clinical-status";
            case RESOLVED: return "http://hl7.org/fhir/allergy-clinical-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "An active record of a risk of a reaction to the identified substance.";
            case INACTIVE: return "An inactivated record of a risk of a reaction to the identified substance.";
            case RESOLVED: return "A reaction to the identified substance has been clinically reassessed by testing or re-exposure and considered to be resolved.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case RESOLVED: return "Resolved";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceClinicalStatusEnumFactory implements EnumFactory<AllergyIntoleranceClinicalStatus> {
    public AllergyIntoleranceClinicalStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return AllergyIntoleranceClinicalStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return AllergyIntoleranceClinicalStatus.INACTIVE;
        if ("resolved".equals(codeString))
          return AllergyIntoleranceClinicalStatus.RESOLVED;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceClinicalStatus code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceClinicalStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AllergyIntoleranceClinicalStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<AllergyIntoleranceClinicalStatus>(this, AllergyIntoleranceClinicalStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<AllergyIntoleranceClinicalStatus>(this, AllergyIntoleranceClinicalStatus.INACTIVE);
        if ("resolved".equals(codeString))
          return new Enumeration<AllergyIntoleranceClinicalStatus>(this, AllergyIntoleranceClinicalStatus.RESOLVED);
        throw new FHIRException("Unknown AllergyIntoleranceClinicalStatus code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceClinicalStatus code) {
      if (code == AllergyIntoleranceClinicalStatus.ACTIVE)
        return "active";
      if (code == AllergyIntoleranceClinicalStatus.INACTIVE)
        return "inactive";
      if (code == AllergyIntoleranceClinicalStatus.RESOLVED)
        return "resolved";
      return "?";
      }
    public String toSystem(AllergyIntoleranceClinicalStatus code) {
      return code.getSystem();
      }
    }

    public enum AllergyIntoleranceVerificationStatus {
        /**
         * A low level of certainty about the propensity for a reaction to the identified substance.
         */
        UNCONFIRMED, 
        /**
         * A high level of certainty about the propensity for a reaction to the identified substance, which may include clinical evidence by testing or rechallenge.
         */
        CONFIRMED, 
        /**
         * A propensity for a reaction to the identified substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.
         */
        REFUTED, 
        /**
         * The statement was entered in error and is not valid.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AllergyIntoleranceVerificationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unconfirmed".equals(codeString))
          return UNCONFIRMED;
        if ("confirmed".equals(codeString))
          return CONFIRMED;
        if ("refuted".equals(codeString))
          return REFUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceVerificationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNCONFIRMED: return "unconfirmed";
            case CONFIRMED: return "confirmed";
            case REFUTED: return "refuted";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UNCONFIRMED: return "http://hl7.org/fhir/allergy-verification-status";
            case CONFIRMED: return "http://hl7.org/fhir/allergy-verification-status";
            case REFUTED: return "http://hl7.org/fhir/allergy-verification-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/allergy-verification-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNCONFIRMED: return "A low level of certainty about the propensity for a reaction to the identified substance.";
            case CONFIRMED: return "A high level of certainty about the propensity for a reaction to the identified substance, which may include clinical evidence by testing or rechallenge.";
            case REFUTED: return "A propensity for a reaction to the identified substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.";
            case ENTEREDINERROR: return "The statement was entered in error and is not valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNCONFIRMED: return "Unconfirmed";
            case CONFIRMED: return "Confirmed";
            case REFUTED: return "Refuted";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceVerificationStatusEnumFactory implements EnumFactory<AllergyIntoleranceVerificationStatus> {
    public AllergyIntoleranceVerificationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unconfirmed".equals(codeString))
          return AllergyIntoleranceVerificationStatus.UNCONFIRMED;
        if ("confirmed".equals(codeString))
          return AllergyIntoleranceVerificationStatus.CONFIRMED;
        if ("refuted".equals(codeString))
          return AllergyIntoleranceVerificationStatus.REFUTED;
        if ("entered-in-error".equals(codeString))
          return AllergyIntoleranceVerificationStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceVerificationStatus code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceVerificationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AllergyIntoleranceVerificationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("unconfirmed".equals(codeString))
          return new Enumeration<AllergyIntoleranceVerificationStatus>(this, AllergyIntoleranceVerificationStatus.UNCONFIRMED);
        if ("confirmed".equals(codeString))
          return new Enumeration<AllergyIntoleranceVerificationStatus>(this, AllergyIntoleranceVerificationStatus.CONFIRMED);
        if ("refuted".equals(codeString))
          return new Enumeration<AllergyIntoleranceVerificationStatus>(this, AllergyIntoleranceVerificationStatus.REFUTED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<AllergyIntoleranceVerificationStatus>(this, AllergyIntoleranceVerificationStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown AllergyIntoleranceVerificationStatus code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceVerificationStatus code) {
      if (code == AllergyIntoleranceVerificationStatus.UNCONFIRMED)
        return "unconfirmed";
      if (code == AllergyIntoleranceVerificationStatus.CONFIRMED)
        return "confirmed";
      if (code == AllergyIntoleranceVerificationStatus.REFUTED)
        return "refuted";
      if (code == AllergyIntoleranceVerificationStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(AllergyIntoleranceVerificationStatus code) {
      return code.getSystem();
      }
    }

    public enum AllergyIntoleranceType {
        /**
         * A propensity for hypersensitivity reaction(s) to a substance.  These reactions are most typically type I hypersensitivity, plus other "allergy-like" reactions, including pseudoallergy.
         */
        ALLERGY, 
        /**
         * A propensity for adverse reactions to a substance that is not judged to be allergic or "allergy-like".  These reactions are typically (but not necessarily) non-immune.  They are to some degree idiosyncratic and/or individually specific (i.e. are not a reaction that is expected to occur with most or all patients given similar circumstances).
         */
        INTOLERANCE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AllergyIntoleranceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("allergy".equals(codeString))
          return ALLERGY;
        if ("intolerance".equals(codeString))
          return INTOLERANCE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ALLERGY: return "allergy";
            case INTOLERANCE: return "intolerance";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ALLERGY: return "http://hl7.org/fhir/allergy-intolerance-type";
            case INTOLERANCE: return "http://hl7.org/fhir/allergy-intolerance-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ALLERGY: return "A propensity for hypersensitivity reaction(s) to a substance.  These reactions are most typically type I hypersensitivity, plus other \"allergy-like\" reactions, including pseudoallergy.";
            case INTOLERANCE: return "A propensity for adverse reactions to a substance that is not judged to be allergic or \"allergy-like\".  These reactions are typically (but not necessarily) non-immune.  They are to some degree idiosyncratic and/or individually specific (i.e. are not a reaction that is expected to occur with most or all patients given similar circumstances).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ALLERGY: return "Allergy";
            case INTOLERANCE: return "Intolerance";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceTypeEnumFactory implements EnumFactory<AllergyIntoleranceType> {
    public AllergyIntoleranceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("allergy".equals(codeString))
          return AllergyIntoleranceType.ALLERGY;
        if ("intolerance".equals(codeString))
          return AllergyIntoleranceType.INTOLERANCE;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceType code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AllergyIntoleranceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("allergy".equals(codeString))
          return new Enumeration<AllergyIntoleranceType>(this, AllergyIntoleranceType.ALLERGY);
        if ("intolerance".equals(codeString))
          return new Enumeration<AllergyIntoleranceType>(this, AllergyIntoleranceType.INTOLERANCE);
        throw new FHIRException("Unknown AllergyIntoleranceType code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceType code) {
      if (code == AllergyIntoleranceType.ALLERGY)
        return "allergy";
      if (code == AllergyIntoleranceType.INTOLERANCE)
        return "intolerance";
      return "?";
      }
    public String toSystem(AllergyIntoleranceType code) {
      return code.getSystem();
      }
    }

    public enum AllergyIntoleranceCategory {
        /**
         * Any substance consumed to provide nutritional support for the body.
         */
        FOOD, 
        /**
         * Substances administered to achieve a physiological effect.
         */
        MEDICATION, 
        /**
         * Any substances that are encountered in the environment, including any substance not already classified as food, medication, or biologic.
         */
        ENVIRONMENT, 
        /**
         * A preparation that is synthesized from living organisms or their products, especially a human or animal protein, such as a hormone or antitoxin, that is used as a diagnostic, preventive, or therapeutic agent. Examples of biologic medications include: vaccines; allergenic extracts, which are used for both diagnosis and treatment (for example, allergy shots); gene therapies; cellular therapies.  There are other biologic products, such as tissues, that are not typically associated with allergies.
         */
        BIOLOGIC, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AllergyIntoleranceCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("food".equals(codeString))
          return FOOD;
        if ("medication".equals(codeString))
          return MEDICATION;
        if ("environment".equals(codeString))
          return ENVIRONMENT;
        if ("biologic".equals(codeString))
          return BIOLOGIC;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FOOD: return "food";
            case MEDICATION: return "medication";
            case ENVIRONMENT: return "environment";
            case BIOLOGIC: return "biologic";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case FOOD: return "http://hl7.org/fhir/allergy-intolerance-category";
            case MEDICATION: return "http://hl7.org/fhir/allergy-intolerance-category";
            case ENVIRONMENT: return "http://hl7.org/fhir/allergy-intolerance-category";
            case BIOLOGIC: return "http://hl7.org/fhir/allergy-intolerance-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case FOOD: return "Any substance consumed to provide nutritional support for the body.";
            case MEDICATION: return "Substances administered to achieve a physiological effect.";
            case ENVIRONMENT: return "Any substances that are encountered in the environment, including any substance not already classified as food, medication, or biologic.";
            case BIOLOGIC: return "A preparation that is synthesized from living organisms or their products, especially a human or animal protein, such as a hormone or antitoxin, that is used as a diagnostic, preventive, or therapeutic agent. Examples of biologic medications include: vaccines; allergenic extracts, which are used for both diagnosis and treatment (for example, allergy shots); gene therapies; cellular therapies.  There are other biologic products, such as tissues, that are not typically associated with allergies.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FOOD: return "Food";
            case MEDICATION: return "Medication";
            case ENVIRONMENT: return "Environment";
            case BIOLOGIC: return "Biologic";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceCategoryEnumFactory implements EnumFactory<AllergyIntoleranceCategory> {
    public AllergyIntoleranceCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("food".equals(codeString))
          return AllergyIntoleranceCategory.FOOD;
        if ("medication".equals(codeString))
          return AllergyIntoleranceCategory.MEDICATION;
        if ("environment".equals(codeString))
          return AllergyIntoleranceCategory.ENVIRONMENT;
        if ("biologic".equals(codeString))
          return AllergyIntoleranceCategory.BIOLOGIC;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceCategory> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AllergyIntoleranceCategory>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("food".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.FOOD);
        if ("medication".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.MEDICATION);
        if ("environment".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.ENVIRONMENT);
        if ("biologic".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.BIOLOGIC);
        throw new FHIRException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceCategory code) {
      if (code == AllergyIntoleranceCategory.FOOD)
        return "food";
      if (code == AllergyIntoleranceCategory.MEDICATION)
        return "medication";
      if (code == AllergyIntoleranceCategory.ENVIRONMENT)
        return "environment";
      if (code == AllergyIntoleranceCategory.BIOLOGIC)
        return "biologic";
      return "?";
      }
    public String toSystem(AllergyIntoleranceCategory code) {
      return code.getSystem();
      }
    }

    public enum AllergyIntoleranceCriticality {
        /**
         * Worst case result of a future exposure is not assessed to be life-threatening or having high potential for organ system failure.
         */
        LOW, 
        /**
         * Worst case result of a future exposure is assessed to be life-threatening or having high potential for organ system failure.
         */
        HIGH, 
        /**
         * Unable to assess the worst case result of a future exposure.
         */
        UNABLETOASSESS, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AllergyIntoleranceCriticality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("low".equals(codeString))
          return LOW;
        if ("high".equals(codeString))
          return HIGH;
        if ("unable-to-assess".equals(codeString))
          return UNABLETOASSESS;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceCriticality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case LOW: return "low";
            case HIGH: return "high";
            case UNABLETOASSESS: return "unable-to-assess";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case LOW: return "http://hl7.org/fhir/allergy-intolerance-criticality";
            case HIGH: return "http://hl7.org/fhir/allergy-intolerance-criticality";
            case UNABLETOASSESS: return "http://hl7.org/fhir/allergy-intolerance-criticality";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case LOW: return "Worst case result of a future exposure is not assessed to be life-threatening or having high potential for organ system failure.";
            case HIGH: return "Worst case result of a future exposure is assessed to be life-threatening or having high potential for organ system failure.";
            case UNABLETOASSESS: return "Unable to assess the worst case result of a future exposure.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case LOW: return "Low Risk";
            case HIGH: return "High Risk";
            case UNABLETOASSESS: return "Unable to Assess Risk";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceCriticalityEnumFactory implements EnumFactory<AllergyIntoleranceCriticality> {
    public AllergyIntoleranceCriticality fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("low".equals(codeString))
          return AllergyIntoleranceCriticality.LOW;
        if ("high".equals(codeString))
          return AllergyIntoleranceCriticality.HIGH;
        if ("unable-to-assess".equals(codeString))
          return AllergyIntoleranceCriticality.UNABLETOASSESS;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceCriticality code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceCriticality> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AllergyIntoleranceCriticality>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("low".equals(codeString))
          return new Enumeration<AllergyIntoleranceCriticality>(this, AllergyIntoleranceCriticality.LOW);
        if ("high".equals(codeString))
          return new Enumeration<AllergyIntoleranceCriticality>(this, AllergyIntoleranceCriticality.HIGH);
        if ("unable-to-assess".equals(codeString))
          return new Enumeration<AllergyIntoleranceCriticality>(this, AllergyIntoleranceCriticality.UNABLETOASSESS);
        throw new FHIRException("Unknown AllergyIntoleranceCriticality code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceCriticality code) {
      if (code == AllergyIntoleranceCriticality.LOW)
        return "low";
      if (code == AllergyIntoleranceCriticality.HIGH)
        return "high";
      if (code == AllergyIntoleranceCriticality.UNABLETOASSESS)
        return "unable-to-assess";
      return "?";
      }
    public String toSystem(AllergyIntoleranceCriticality code) {
      return code.getSystem();
      }
    }

    public enum AllergyIntoleranceSeverity {
        /**
         * Causes mild physiological effects.
         */
        MILD, 
        /**
         * Causes moderate physiological effects.
         */
        MODERATE, 
        /**
         * Causes severe physiological effects.
         */
        SEVERE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AllergyIntoleranceSeverity fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mild".equals(codeString))
          return MILD;
        if ("moderate".equals(codeString))
          return MODERATE;
        if ("severe".equals(codeString))
          return SEVERE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceSeverity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MILD: return "mild";
            case MODERATE: return "moderate";
            case SEVERE: return "severe";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MILD: return "http://hl7.org/fhir/reaction-event-severity";
            case MODERATE: return "http://hl7.org/fhir/reaction-event-severity";
            case SEVERE: return "http://hl7.org/fhir/reaction-event-severity";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MILD: return "Causes mild physiological effects.";
            case MODERATE: return "Causes moderate physiological effects.";
            case SEVERE: return "Causes severe physiological effects.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MILD: return "Mild";
            case MODERATE: return "Moderate";
            case SEVERE: return "Severe";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceSeverityEnumFactory implements EnumFactory<AllergyIntoleranceSeverity> {
    public AllergyIntoleranceSeverity fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mild".equals(codeString))
          return AllergyIntoleranceSeverity.MILD;
        if ("moderate".equals(codeString))
          return AllergyIntoleranceSeverity.MODERATE;
        if ("severe".equals(codeString))
          return AllergyIntoleranceSeverity.SEVERE;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceSeverity code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceSeverity> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<AllergyIntoleranceSeverity>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("mild".equals(codeString))
          return new Enumeration<AllergyIntoleranceSeverity>(this, AllergyIntoleranceSeverity.MILD);
        if ("moderate".equals(codeString))
          return new Enumeration<AllergyIntoleranceSeverity>(this, AllergyIntoleranceSeverity.MODERATE);
        if ("severe".equals(codeString))
          return new Enumeration<AllergyIntoleranceSeverity>(this, AllergyIntoleranceSeverity.SEVERE);
        throw new FHIRException("Unknown AllergyIntoleranceSeverity code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceSeverity code) {
      if (code == AllergyIntoleranceSeverity.MILD)
        return "mild";
      if (code == AllergyIntoleranceSeverity.MODERATE)
        return "moderate";
      if (code == AllergyIntoleranceSeverity.SEVERE)
        return "severe";
      return "?";
      }
    public String toSystem(AllergyIntoleranceSeverity code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class AllergyIntoleranceReactionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite product that includes the identified substance. It must be clinically safe to only process the 'code' and ignore the 'reaction.substance'.
         */
        @Child(name = "substance", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Specific substance or pharmaceutical product considered to be responsible for event", formalDefinition="Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite product that includes the identified substance. It must be clinically safe to only process the 'code' and ignore the 'reaction.substance'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/substance-code")
        protected CodeableConcept substance;

        /**
         * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
         */
        @Child(name = "manifestation", type = {CodeableConcept.class}, order=2, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Clinical symptoms/signs associated with the Event", formalDefinition="Clinical symptoms and/or signs that are observed or associated with the adverse reaction event." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
        protected List<CodeableConcept> manifestation;

        /**
         * Text description about the reaction as a whole, including details of the manifestation if required.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the event as a whole", formalDefinition="Text description about the reaction as a whole, including details of the manifestation if required." )
        protected StringType description;

        /**
         * Record of the date and/or time of the onset of the Reaction.
         */
        @Child(name = "onset", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Date(/time) when manifestations showed", formalDefinition="Record of the date and/or time of the onset of the Reaction." )
        protected DateTimeType onset;

        /**
         * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.
         */
        @Child(name = "severity", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="mild | moderate | severe (of event as a whole)", formalDefinition="Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reaction-event-severity")
        protected Enumeration<AllergyIntoleranceSeverity> severity;

        /**
         * Identification of the route by which the subject was exposed to the substance.
         */
        @Child(name = "exposureRoute", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How the subject was exposed to the substance", formalDefinition="Identification of the route by which the subject was exposed to the substance." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/route-codes")
        protected CodeableConcept exposureRoute;

        /**
         * Additional text about the adverse reaction event not captured in other fields.
         */
        @Child(name = "note", type = {Annotation.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Text about event not captured in other fields", formalDefinition="Additional text about the adverse reaction event not captured in other fields." )
        protected List<Annotation> note;

        private static final long serialVersionUID = -752118516L;

    /**
     * Constructor
     */
      public AllergyIntoleranceReactionComponent() {
        super();
      }

        /**
         * @return {@link #substance} (Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite product that includes the identified substance. It must be clinically safe to only process the 'code' and ignore the 'reaction.substance'.)
         */
        public CodeableConcept getSubstance() { 
          if (this.substance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AllergyIntoleranceReactionComponent.substance");
            else if (Configuration.doAutoCreate())
              this.substance = new CodeableConcept(); // cc
          return this.substance;
        }

        public boolean hasSubstance() { 
          return this.substance != null && !this.substance.isEmpty();
        }

        /**
         * @param value {@link #substance} (Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite product that includes the identified substance. It must be clinically safe to only process the 'code' and ignore the 'reaction.substance'.)
         */
        public AllergyIntoleranceReactionComponent setSubstance(CodeableConcept value) { 
          this.substance = value;
          return this;
        }

        /**
         * @return {@link #manifestation} (Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.)
         */
        public List<CodeableConcept> getManifestation() { 
          if (this.manifestation == null)
            this.manifestation = new ArrayList<CodeableConcept>();
          return this.manifestation;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AllergyIntoleranceReactionComponent setManifestation(List<CodeableConcept> theManifestation) { 
          this.manifestation = theManifestation;
          return this;
        }

        public boolean hasManifestation() { 
          if (this.manifestation == null)
            return false;
          for (CodeableConcept item : this.manifestation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addManifestation() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.manifestation == null)
            this.manifestation = new ArrayList<CodeableConcept>();
          this.manifestation.add(t);
          return t;
        }

        public AllergyIntoleranceReactionComponent addManifestation(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.manifestation == null)
            this.manifestation = new ArrayList<CodeableConcept>();
          this.manifestation.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #manifestation}, creating it if it does not already exist
         */
        public CodeableConcept getManifestationFirstRep() { 
          if (getManifestation().isEmpty()) {
            addManifestation();
          }
          return getManifestation().get(0);
        }

        /**
         * @return {@link #description} (Text description about the reaction as a whole, including details of the manifestation if required.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AllergyIntoleranceReactionComponent.description");
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
         * @param value {@link #description} (Text description about the reaction as a whole, including details of the manifestation if required.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public AllergyIntoleranceReactionComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Text description about the reaction as a whole, including details of the manifestation if required.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Text description about the reaction as a whole, including details of the manifestation if required.
         */
        public AllergyIntoleranceReactionComponent setDescription(String value) { 
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
         * @return {@link #onset} (Record of the date and/or time of the onset of the Reaction.). This is the underlying object with id, value and extensions. The accessor "getOnset" gives direct access to the value
         */
        public DateTimeType getOnsetElement() { 
          if (this.onset == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AllergyIntoleranceReactionComponent.onset");
            else if (Configuration.doAutoCreate())
              this.onset = new DateTimeType(); // bb
          return this.onset;
        }

        public boolean hasOnsetElement() { 
          return this.onset != null && !this.onset.isEmpty();
        }

        public boolean hasOnset() { 
          return this.onset != null && !this.onset.isEmpty();
        }

        /**
         * @param value {@link #onset} (Record of the date and/or time of the onset of the Reaction.). This is the underlying object with id, value and extensions. The accessor "getOnset" gives direct access to the value
         */
        public AllergyIntoleranceReactionComponent setOnsetElement(DateTimeType value) { 
          this.onset = value;
          return this;
        }

        /**
         * @return Record of the date and/or time of the onset of the Reaction.
         */
        public Date getOnset() { 
          return this.onset == null ? null : this.onset.getValue();
        }

        /**
         * @param value Record of the date and/or time of the onset of the Reaction.
         */
        public AllergyIntoleranceReactionComponent setOnset(Date value) { 
          if (value == null)
            this.onset = null;
          else {
            if (this.onset == null)
              this.onset = new DateTimeType();
            this.onset.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #severity} (Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public Enumeration<AllergyIntoleranceSeverity> getSeverityElement() { 
          if (this.severity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AllergyIntoleranceReactionComponent.severity");
            else if (Configuration.doAutoCreate())
              this.severity = new Enumeration<AllergyIntoleranceSeverity>(new AllergyIntoleranceSeverityEnumFactory()); // bb
          return this.severity;
        }

        public boolean hasSeverityElement() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        public boolean hasSeverity() { 
          return this.severity != null && !this.severity.isEmpty();
        }

        /**
         * @param value {@link #severity} (Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
         */
        public AllergyIntoleranceReactionComponent setSeverityElement(Enumeration<AllergyIntoleranceSeverity> value) { 
          this.severity = value;
          return this;
        }

        /**
         * @return Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.
         */
        public AllergyIntoleranceSeverity getSeverity() { 
          return this.severity == null ? null : this.severity.getValue();
        }

        /**
         * @param value Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.
         */
        public AllergyIntoleranceReactionComponent setSeverity(AllergyIntoleranceSeverity value) { 
          if (value == null)
            this.severity = null;
          else {
            if (this.severity == null)
              this.severity = new Enumeration<AllergyIntoleranceSeverity>(new AllergyIntoleranceSeverityEnumFactory());
            this.severity.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #exposureRoute} (Identification of the route by which the subject was exposed to the substance.)
         */
        public CodeableConcept getExposureRoute() { 
          if (this.exposureRoute == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AllergyIntoleranceReactionComponent.exposureRoute");
            else if (Configuration.doAutoCreate())
              this.exposureRoute = new CodeableConcept(); // cc
          return this.exposureRoute;
        }

        public boolean hasExposureRoute() { 
          return this.exposureRoute != null && !this.exposureRoute.isEmpty();
        }

        /**
         * @param value {@link #exposureRoute} (Identification of the route by which the subject was exposed to the substance.)
         */
        public AllergyIntoleranceReactionComponent setExposureRoute(CodeableConcept value) { 
          this.exposureRoute = value;
          return this;
        }

        /**
         * @return {@link #note} (Additional text about the adverse reaction event not captured in other fields.)
         */
        public List<Annotation> getNote() { 
          if (this.note == null)
            this.note = new ArrayList<Annotation>();
          return this.note;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AllergyIntoleranceReactionComponent setNote(List<Annotation> theNote) { 
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

        public AllergyIntoleranceReactionComponent addNote(Annotation t) { //3
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
          childrenList.add(new Property("substance", "CodeableConcept", "Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite product that includes the identified substance. It must be clinically safe to only process the 'code' and ignore the 'reaction.substance'.", 0, java.lang.Integer.MAX_VALUE, substance));
          childrenList.add(new Property("manifestation", "CodeableConcept", "Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.", 0, java.lang.Integer.MAX_VALUE, manifestation));
          childrenList.add(new Property("description", "string", "Text description about the reaction as a whole, including details of the manifestation if required.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("onset", "dateTime", "Record of the date and/or time of the onset of the Reaction.", 0, java.lang.Integer.MAX_VALUE, onset));
          childrenList.add(new Property("severity", "code", "Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.", 0, java.lang.Integer.MAX_VALUE, severity));
          childrenList.add(new Property("exposureRoute", "CodeableConcept", "Identification of the route by which the subject was exposed to the substance.", 0, java.lang.Integer.MAX_VALUE, exposureRoute));
          childrenList.add(new Property("note", "Annotation", "Additional text about the adverse reaction event not captured in other fields.", 0, java.lang.Integer.MAX_VALUE, note));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return this.substance == null ? new Base[0] : new Base[] {this.substance}; // CodeableConcept
        case 1115984422: /*manifestation*/ return this.manifestation == null ? new Base[0] : this.manifestation.toArray(new Base[this.manifestation.size()]); // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 105901603: /*onset*/ return this.onset == null ? new Base[0] : new Base[] {this.onset}; // DateTimeType
        case 1478300413: /*severity*/ return this.severity == null ? new Base[0] : new Base[] {this.severity}; // Enumeration<AllergyIntoleranceSeverity>
        case 421286274: /*exposureRoute*/ return this.exposureRoute == null ? new Base[0] : new Base[] {this.exposureRoute}; // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 530040176: // substance
          this.substance = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1115984422: // manifestation
          this.getManifestation().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 105901603: // onset
          this.onset = castToDateTime(value); // DateTimeType
          return value;
        case 1478300413: // severity
          value = new AllergyIntoleranceSeverityEnumFactory().fromType(castToCode(value));
          this.severity = (Enumeration) value; // Enumeration<AllergyIntoleranceSeverity>
          return value;
        case 421286274: // exposureRoute
          this.exposureRoute = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("manifestation")) {
          this.getManifestation().add(castToCodeableConcept(value));
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("onset")) {
          this.onset = castToDateTime(value); // DateTimeType
        } else if (name.equals("severity")) {
          value = new AllergyIntoleranceSeverityEnumFactory().fromType(castToCode(value));
          this.severity = (Enumeration) value; // Enumeration<AllergyIntoleranceSeverity>
        } else if (name.equals("exposureRoute")) {
          this.exposureRoute = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176:  return getSubstance(); 
        case 1115984422:  return addManifestation(); 
        case -1724546052:  return getDescriptionElement();
        case 105901603:  return getOnsetElement();
        case 1478300413:  return getSeverityElement();
        case 421286274:  return getExposureRoute(); 
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176: /*substance*/ return new String[] {"CodeableConcept"};
        case 1115984422: /*manifestation*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 105901603: /*onset*/ return new String[] {"dateTime"};
        case 1478300413: /*severity*/ return new String[] {"code"};
        case 421286274: /*exposureRoute*/ return new String[] {"CodeableConcept"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = new CodeableConcept();
          return this.substance;
        }
        else if (name.equals("manifestation")) {
          return addManifestation();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.description");
        }
        else if (name.equals("onset")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.onset");
        }
        else if (name.equals("severity")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.severity");
        }
        else if (name.equals("exposureRoute")) {
          this.exposureRoute = new CodeableConcept();
          return this.exposureRoute;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

      public AllergyIntoleranceReactionComponent copy() {
        AllergyIntoleranceReactionComponent dst = new AllergyIntoleranceReactionComponent();
        copyValues(dst);
        dst.substance = substance == null ? null : substance.copy();
        if (manifestation != null) {
          dst.manifestation = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : manifestation)
            dst.manifestation.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.onset = onset == null ? null : onset.copy();
        dst.severity = severity == null ? null : severity.copy();
        dst.exposureRoute = exposureRoute == null ? null : exposureRoute.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AllergyIntoleranceReactionComponent))
          return false;
        AllergyIntoleranceReactionComponent o = (AllergyIntoleranceReactionComponent) other;
        return compareDeep(substance, o.substance, true) && compareDeep(manifestation, o.manifestation, true)
           && compareDeep(description, o.description, true) && compareDeep(onset, o.onset, true) && compareDeep(severity, o.severity, true)
           && compareDeep(exposureRoute, o.exposureRoute, true) && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AllergyIntoleranceReactionComponent))
          return false;
        AllergyIntoleranceReactionComponent o = (AllergyIntoleranceReactionComponent) other;
        return compareValues(description, o.description, true) && compareValues(onset, o.onset, true) && compareValues(severity, o.severity, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(substance, manifestation, description
          , onset, severity, exposureRoute, note);
      }

  public String fhirType() {
    return "AllergyIntolerance.reaction";

  }

  }

    /**
     * This records identifiers associated with this allergy/intolerance concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External ids for this item", formalDefinition="This records identifiers associated with this allergy/intolerance concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The clinical status of the allergy or intolerance.
     */
    @Child(name = "clinicalStatus", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | inactive | resolved", formalDefinition="The clinical status of the allergy or intolerance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-clinical-status")
    protected Enumeration<AllergyIntoleranceClinicalStatus> clinicalStatus;

    /**
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).
     */
    @Child(name = "verificationStatus", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="unconfirmed | confirmed | refuted | entered-in-error", formalDefinition="Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-verification-status")
    protected Enumeration<AllergyIntoleranceVerificationStatus> verificationStatus;

    /**
     * Identification of the underlying physiological mechanism for the reaction risk.
     */
    @Child(name = "type", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="allergy | intolerance - Underlying mechanism (if known)", formalDefinition="Identification of the underlying physiological mechanism for the reaction risk." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-intolerance-type")
    protected Enumeration<AllergyIntoleranceType> type;

    /**
     * Category of the identified substance.
     */
    @Child(name = "category", type = {CodeType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="food | medication | environment | biologic", formalDefinition="Category of the identified substance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-intolerance-category")
    protected List<Enumeration<AllergyIntoleranceCategory>> category;

    /**
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
     */
    @Child(name = "criticality", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="low | high | unable-to-assess", formalDefinition="Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-intolerance-criticality")
    protected Enumeration<AllergyIntoleranceCriticality> criticality;

    /**
     * Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., "Latex"), an allergy or intolerance condition (e.g., "Latex allergy"), or a negated/excluded code for a specific substance or class (e.g., "No latex allergy") or a general or categorical negated statement (e.g.,  "No known allergy", "No known drug allergies").
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Code that identifies the allergy or intolerance", formalDefinition="Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., \"Latex\"), an allergy or intolerance condition (e.g., \"Latex allergy\"), or a negated/excluded code for a specific substance or class (e.g., \"No latex allergy\") or a general or categorical negated statement (e.g.,  \"No known allergy\", \"No known drug allergies\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergyintolerance-code")
    protected CodeableConcept code;

    /**
     * The patient who has the allergy or intolerance.
     */
    @Child(name = "patient", type = {Patient.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the sensitivity is for", formalDefinition="The patient who has the allergy or intolerance." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient who has the allergy or intolerance.)
     */
    protected Patient patientTarget;

    /**
     * Estimated or actual date,  date-time, or age when allergy or intolerance was identified.
     */
    @Child(name = "onset", type = {DateTimeType.class, Age.class, Period.class, Range.class, StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When allergy or intolerance was identified", formalDefinition="Estimated or actual date,  date-time, or age when allergy or intolerance was identified." )
    protected Type onset;

    /**
     * The date on which the existance of the AllergyIntolerance was first asserted or acknowledged.
     */
    @Child(name = "assertedDate", type = {DateTimeType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date record was believed accurate", formalDefinition="The date on which the existance of the AllergyIntolerance was first asserted or acknowledged." )
    protected DateTimeType assertedDate;

    /**
     * Individual who recorded the record and takes responsibility for its content.
     */
    @Child(name = "recorder", type = {Practitioner.class, Patient.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who recorded the sensitivity", formalDefinition="Individual who recorded the record and takes responsibility for its content." )
    protected Reference recorder;

    /**
     * The actual object that is the target of the reference (Individual who recorded the record and takes responsibility for its content.)
     */
    protected Resource recorderTarget;

    /**
     * The source of the information about the allergy that is recorded.
     */
    @Child(name = "asserter", type = {Patient.class, RelatedPerson.class, Practitioner.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Source of the information about the allergy", formalDefinition="The source of the information about the allergy that is recorded." )
    protected Reference asserter;

    /**
     * The actual object that is the target of the reference (The source of the information about the allergy that is recorded.)
     */
    protected Resource asserterTarget;

    /**
     * Represents the date and/or time of the last known occurrence of a reaction event.
     */
    @Child(name = "lastOccurrence", type = {DateTimeType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date(/time) of last known occurrence of a reaction", formalDefinition="Represents the date and/or time of the last known occurrence of a reaction event." )
    protected DateTimeType lastOccurrence;

    /**
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     */
    @Child(name = "note", type = {Annotation.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional text not captured in other fields", formalDefinition="Additional narrative about the propensity for the Adverse Reaction, not captured in other fields." )
    protected List<Annotation> note;

    /**
     * Details about each adverse reaction event linked to exposure to the identified substance.
     */
    @Child(name = "reaction", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Adverse Reaction Events linked to exposure to substance", formalDefinition="Details about each adverse reaction event linked to exposure to the identified substance." )
    protected List<AllergyIntoleranceReactionComponent> reaction;

    private static final long serialVersionUID = 948924623L;

  /**
   * Constructor
   */
    public AllergyIntolerance() {
      super();
    }

  /**
   * Constructor
   */
    public AllergyIntolerance(Enumeration<AllergyIntoleranceVerificationStatus> verificationStatus, Reference patient) {
      super();
      this.verificationStatus = verificationStatus;
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this allergy/intolerance concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AllergyIntolerance setIdentifier(List<Identifier> theIdentifier) { 
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

    public AllergyIntolerance addIdentifier(Identifier t) { //3
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
     * @return {@link #clinicalStatus} (The clinical status of the allergy or intolerance.). This is the underlying object with id, value and extensions. The accessor "getClinicalStatus" gives direct access to the value
     */
    public Enumeration<AllergyIntoleranceClinicalStatus> getClinicalStatusElement() { 
      if (this.clinicalStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.clinicalStatus");
        else if (Configuration.doAutoCreate())
          this.clinicalStatus = new Enumeration<AllergyIntoleranceClinicalStatus>(new AllergyIntoleranceClinicalStatusEnumFactory()); // bb
      return this.clinicalStatus;
    }

    public boolean hasClinicalStatusElement() { 
      return this.clinicalStatus != null && !this.clinicalStatus.isEmpty();
    }

    public boolean hasClinicalStatus() { 
      return this.clinicalStatus != null && !this.clinicalStatus.isEmpty();
    }

    /**
     * @param value {@link #clinicalStatus} (The clinical status of the allergy or intolerance.). This is the underlying object with id, value and extensions. The accessor "getClinicalStatus" gives direct access to the value
     */
    public AllergyIntolerance setClinicalStatusElement(Enumeration<AllergyIntoleranceClinicalStatus> value) { 
      this.clinicalStatus = value;
      return this;
    }

    /**
     * @return The clinical status of the allergy or intolerance.
     */
    public AllergyIntoleranceClinicalStatus getClinicalStatus() { 
      return this.clinicalStatus == null ? null : this.clinicalStatus.getValue();
    }

    /**
     * @param value The clinical status of the allergy or intolerance.
     */
    public AllergyIntolerance setClinicalStatus(AllergyIntoleranceClinicalStatus value) { 
      if (value == null)
        this.clinicalStatus = null;
      else {
        if (this.clinicalStatus == null)
          this.clinicalStatus = new Enumeration<AllergyIntoleranceClinicalStatus>(new AllergyIntoleranceClinicalStatusEnumFactory());
        this.clinicalStatus.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #verificationStatus} (Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).). This is the underlying object with id, value and extensions. The accessor "getVerificationStatus" gives direct access to the value
     */
    public Enumeration<AllergyIntoleranceVerificationStatus> getVerificationStatusElement() { 
      if (this.verificationStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.verificationStatus");
        else if (Configuration.doAutoCreate())
          this.verificationStatus = new Enumeration<AllergyIntoleranceVerificationStatus>(new AllergyIntoleranceVerificationStatusEnumFactory()); // bb
      return this.verificationStatus;
    }

    public boolean hasVerificationStatusElement() { 
      return this.verificationStatus != null && !this.verificationStatus.isEmpty();
    }

    public boolean hasVerificationStatus() { 
      return this.verificationStatus != null && !this.verificationStatus.isEmpty();
    }

    /**
     * @param value {@link #verificationStatus} (Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).). This is the underlying object with id, value and extensions. The accessor "getVerificationStatus" gives direct access to the value
     */
    public AllergyIntolerance setVerificationStatusElement(Enumeration<AllergyIntoleranceVerificationStatus> value) { 
      this.verificationStatus = value;
      return this;
    }

    /**
     * @return Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).
     */
    public AllergyIntoleranceVerificationStatus getVerificationStatus() { 
      return this.verificationStatus == null ? null : this.verificationStatus.getValue();
    }

    /**
     * @param value Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).
     */
    public AllergyIntolerance setVerificationStatus(AllergyIntoleranceVerificationStatus value) { 
        if (this.verificationStatus == null)
          this.verificationStatus = new Enumeration<AllergyIntoleranceVerificationStatus>(new AllergyIntoleranceVerificationStatusEnumFactory());
        this.verificationStatus.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (Identification of the underlying physiological mechanism for the reaction risk.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<AllergyIntoleranceType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<AllergyIntoleranceType>(new AllergyIntoleranceTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Identification of the underlying physiological mechanism for the reaction risk.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public AllergyIntolerance setTypeElement(Enumeration<AllergyIntoleranceType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Identification of the underlying physiological mechanism for the reaction risk.
     */
    public AllergyIntoleranceType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Identification of the underlying physiological mechanism for the reaction risk.
     */
    public AllergyIntolerance setType(AllergyIntoleranceType value) { 
      if (value == null)
        this.type = null;
      else {
        if (this.type == null)
          this.type = new Enumeration<AllergyIntoleranceType>(new AllergyIntoleranceTypeEnumFactory());
        this.type.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #category} (Category of the identified substance.)
     */
    public List<Enumeration<AllergyIntoleranceCategory>> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<Enumeration<AllergyIntoleranceCategory>>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AllergyIntolerance setCategory(List<Enumeration<AllergyIntoleranceCategory>> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (Enumeration<AllergyIntoleranceCategory> item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #category} (Category of the identified substance.)
     */
    public Enumeration<AllergyIntoleranceCategory> addCategoryElement() {//2 
      Enumeration<AllergyIntoleranceCategory> t = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
      if (this.category == null)
        this.category = new ArrayList<Enumeration<AllergyIntoleranceCategory>>();
      this.category.add(t);
      return t;
    }

    /**
     * @param value {@link #category} (Category of the identified substance.)
     */
    public AllergyIntolerance addCategory(AllergyIntoleranceCategory value) { //1
      Enumeration<AllergyIntoleranceCategory> t = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
      t.setValue(value);
      if (this.category == null)
        this.category = new ArrayList<Enumeration<AllergyIntoleranceCategory>>();
      this.category.add(t);
      return this;
    }

    /**
     * @param value {@link #category} (Category of the identified substance.)
     */
    public boolean hasCategory(AllergyIntoleranceCategory value) { 
      if (this.category == null)
        return false;
      for (Enumeration<AllergyIntoleranceCategory> v : this.category)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #criticality} (Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.). This is the underlying object with id, value and extensions. The accessor "getCriticality" gives direct access to the value
     */
    public Enumeration<AllergyIntoleranceCriticality> getCriticalityElement() { 
      if (this.criticality == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.criticality");
        else if (Configuration.doAutoCreate())
          this.criticality = new Enumeration<AllergyIntoleranceCriticality>(new AllergyIntoleranceCriticalityEnumFactory()); // bb
      return this.criticality;
    }

    public boolean hasCriticalityElement() { 
      return this.criticality != null && !this.criticality.isEmpty();
    }

    public boolean hasCriticality() { 
      return this.criticality != null && !this.criticality.isEmpty();
    }

    /**
     * @param value {@link #criticality} (Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.). This is the underlying object with id, value and extensions. The accessor "getCriticality" gives direct access to the value
     */
    public AllergyIntolerance setCriticalityElement(Enumeration<AllergyIntoleranceCriticality> value) { 
      this.criticality = value;
      return this;
    }

    /**
     * @return Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
     */
    public AllergyIntoleranceCriticality getCriticality() { 
      return this.criticality == null ? null : this.criticality.getValue();
    }

    /**
     * @param value Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
     */
    public AllergyIntolerance setCriticality(AllergyIntoleranceCriticality value) { 
      if (value == null)
        this.criticality = null;
      else {
        if (this.criticality == null)
          this.criticality = new Enumeration<AllergyIntoleranceCriticality>(new AllergyIntoleranceCriticalityEnumFactory());
        this.criticality.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #code} (Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., "Latex"), an allergy or intolerance condition (e.g., "Latex allergy"), or a negated/excluded code for a specific substance or class (e.g., "No latex allergy") or a general or categorical negated statement (e.g.,  "No known allergy", "No known drug allergies").)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., "Latex"), an allergy or intolerance condition (e.g., "Latex allergy"), or a negated/excluded code for a specific substance or class (e.g., "No latex allergy") or a general or categorical negated statement (e.g.,  "No known allergy", "No known drug allergies").)
     */
    public AllergyIntolerance setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #patient} (The patient who has the allergy or intolerance.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient who has the allergy or intolerance.)
     */
    public AllergyIntolerance setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient who has the allergy or intolerance.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient who has the allergy or intolerance.)
     */
    public AllergyIntolerance setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #onset} (Estimated or actual date,  date-time, or age when allergy or intolerance was identified.)
     */
    public Type getOnset() { 
      return this.onset;
    }

    /**
     * @return {@link #onset} (Estimated or actual date,  date-time, or age when allergy or intolerance was identified.)
     */
    public DateTimeType getOnsetDateTimeType() throws FHIRException { 
      if (!(this.onset instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (DateTimeType) this.onset;
    }

    public boolean hasOnsetDateTimeType() { 
      return this.onset instanceof DateTimeType;
    }

    /**
     * @return {@link #onset} (Estimated or actual date,  date-time, or age when allergy or intolerance was identified.)
     */
    public Age getOnsetAge() throws FHIRException { 
      if (!(this.onset instanceof Age))
        throw new FHIRException("Type mismatch: the type Age was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Age) this.onset;
    }

    public boolean hasOnsetAge() { 
      return this.onset instanceof Age;
    }

    /**
     * @return {@link #onset} (Estimated or actual date,  date-time, or age when allergy or intolerance was identified.)
     */
    public Period getOnsetPeriod() throws FHIRException { 
      if (!(this.onset instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Period) this.onset;
    }

    public boolean hasOnsetPeriod() { 
      return this.onset instanceof Period;
    }

    /**
     * @return {@link #onset} (Estimated or actual date,  date-time, or age when allergy or intolerance was identified.)
     */
    public Range getOnsetRange() throws FHIRException { 
      if (!(this.onset instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Range) this.onset;
    }

    public boolean hasOnsetRange() { 
      return this.onset instanceof Range;
    }

    /**
     * @return {@link #onset} (Estimated or actual date,  date-time, or age when allergy or intolerance was identified.)
     */
    public StringType getOnsetStringType() throws FHIRException { 
      if (!(this.onset instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (StringType) this.onset;
    }

    public boolean hasOnsetStringType() { 
      return this.onset instanceof StringType;
    }

    public boolean hasOnset() { 
      return this.onset != null && !this.onset.isEmpty();
    }

    /**
     * @param value {@link #onset} (Estimated or actual date,  date-time, or age when allergy or intolerance was identified.)
     */
    public AllergyIntolerance setOnset(Type value) { 
      this.onset = value;
      return this;
    }

    /**
     * @return {@link #assertedDate} (The date on which the existance of the AllergyIntolerance was first asserted or acknowledged.). This is the underlying object with id, value and extensions. The accessor "getAssertedDate" gives direct access to the value
     */
    public DateTimeType getAssertedDateElement() { 
      if (this.assertedDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.assertedDate");
        else if (Configuration.doAutoCreate())
          this.assertedDate = new DateTimeType(); // bb
      return this.assertedDate;
    }

    public boolean hasAssertedDateElement() { 
      return this.assertedDate != null && !this.assertedDate.isEmpty();
    }

    public boolean hasAssertedDate() { 
      return this.assertedDate != null && !this.assertedDate.isEmpty();
    }

    /**
     * @param value {@link #assertedDate} (The date on which the existance of the AllergyIntolerance was first asserted or acknowledged.). This is the underlying object with id, value and extensions. The accessor "getAssertedDate" gives direct access to the value
     */
    public AllergyIntolerance setAssertedDateElement(DateTimeType value) { 
      this.assertedDate = value;
      return this;
    }

    /**
     * @return The date on which the existance of the AllergyIntolerance was first asserted or acknowledged.
     */
    public Date getAssertedDate() { 
      return this.assertedDate == null ? null : this.assertedDate.getValue();
    }

    /**
     * @param value The date on which the existance of the AllergyIntolerance was first asserted or acknowledged.
     */
    public AllergyIntolerance setAssertedDate(Date value) { 
      if (value == null)
        this.assertedDate = null;
      else {
        if (this.assertedDate == null)
          this.assertedDate = new DateTimeType();
        this.assertedDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #recorder} (Individual who recorded the record and takes responsibility for its content.)
     */
    public Reference getRecorder() { 
      if (this.recorder == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.recorder");
        else if (Configuration.doAutoCreate())
          this.recorder = new Reference(); // cc
      return this.recorder;
    }

    public boolean hasRecorder() { 
      return this.recorder != null && !this.recorder.isEmpty();
    }

    /**
     * @param value {@link #recorder} (Individual who recorded the record and takes responsibility for its content.)
     */
    public AllergyIntolerance setRecorder(Reference value) { 
      this.recorder = value;
      return this;
    }

    /**
     * @return {@link #recorder} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its content.)
     */
    public Resource getRecorderTarget() { 
      return this.recorderTarget;
    }

    /**
     * @param value {@link #recorder} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its content.)
     */
    public AllergyIntolerance setRecorderTarget(Resource value) { 
      this.recorderTarget = value;
      return this;
    }

    /**
     * @return {@link #asserter} (The source of the information about the allergy that is recorded.)
     */
    public Reference getAsserter() { 
      if (this.asserter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.asserter");
        else if (Configuration.doAutoCreate())
          this.asserter = new Reference(); // cc
      return this.asserter;
    }

    public boolean hasAsserter() { 
      return this.asserter != null && !this.asserter.isEmpty();
    }

    /**
     * @param value {@link #asserter} (The source of the information about the allergy that is recorded.)
     */
    public AllergyIntolerance setAsserter(Reference value) { 
      this.asserter = value;
      return this;
    }

    /**
     * @return {@link #asserter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The source of the information about the allergy that is recorded.)
     */
    public Resource getAsserterTarget() { 
      return this.asserterTarget;
    }

    /**
     * @param value {@link #asserter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The source of the information about the allergy that is recorded.)
     */
    public AllergyIntolerance setAsserterTarget(Resource value) { 
      this.asserterTarget = value;
      return this;
    }

    /**
     * @return {@link #lastOccurrence} (Represents the date and/or time of the last known occurrence of a reaction event.). This is the underlying object with id, value and extensions. The accessor "getLastOccurrence" gives direct access to the value
     */
    public DateTimeType getLastOccurrenceElement() { 
      if (this.lastOccurrence == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.lastOccurrence");
        else if (Configuration.doAutoCreate())
          this.lastOccurrence = new DateTimeType(); // bb
      return this.lastOccurrence;
    }

    public boolean hasLastOccurrenceElement() { 
      return this.lastOccurrence != null && !this.lastOccurrence.isEmpty();
    }

    public boolean hasLastOccurrence() { 
      return this.lastOccurrence != null && !this.lastOccurrence.isEmpty();
    }

    /**
     * @param value {@link #lastOccurrence} (Represents the date and/or time of the last known occurrence of a reaction event.). This is the underlying object with id, value and extensions. The accessor "getLastOccurrence" gives direct access to the value
     */
    public AllergyIntolerance setLastOccurrenceElement(DateTimeType value) { 
      this.lastOccurrence = value;
      return this;
    }

    /**
     * @return Represents the date and/or time of the last known occurrence of a reaction event.
     */
    public Date getLastOccurrence() { 
      return this.lastOccurrence == null ? null : this.lastOccurrence.getValue();
    }

    /**
     * @param value Represents the date and/or time of the last known occurrence of a reaction event.
     */
    public AllergyIntolerance setLastOccurrence(Date value) { 
      if (value == null)
        this.lastOccurrence = null;
      else {
        if (this.lastOccurrence == null)
          this.lastOccurrence = new DateTimeType();
        this.lastOccurrence.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #note} (Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AllergyIntolerance setNote(List<Annotation> theNote) { 
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

    public AllergyIntolerance addNote(Annotation t) { //3
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
     * @return {@link #reaction} (Details about each adverse reaction event linked to exposure to the identified substance.)
     */
    public List<AllergyIntoleranceReactionComponent> getReaction() { 
      if (this.reaction == null)
        this.reaction = new ArrayList<AllergyIntoleranceReactionComponent>();
      return this.reaction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public AllergyIntolerance setReaction(List<AllergyIntoleranceReactionComponent> theReaction) { 
      this.reaction = theReaction;
      return this;
    }

    public boolean hasReaction() { 
      if (this.reaction == null)
        return false;
      for (AllergyIntoleranceReactionComponent item : this.reaction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public AllergyIntoleranceReactionComponent addReaction() { //3
      AllergyIntoleranceReactionComponent t = new AllergyIntoleranceReactionComponent();
      if (this.reaction == null)
        this.reaction = new ArrayList<AllergyIntoleranceReactionComponent>();
      this.reaction.add(t);
      return t;
    }

    public AllergyIntolerance addReaction(AllergyIntoleranceReactionComponent t) { //3
      if (t == null)
        return this;
      if (this.reaction == null)
        this.reaction = new ArrayList<AllergyIntoleranceReactionComponent>();
      this.reaction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reaction}, creating it if it does not already exist
     */
    public AllergyIntoleranceReactionComponent getReactionFirstRep() { 
      if (getReaction().isEmpty()) {
        addReaction();
      }
      return getReaction().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this allergy/intolerance concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("clinicalStatus", "code", "The clinical status of the allergy or intolerance.", 0, java.lang.Integer.MAX_VALUE, clinicalStatus));
        childrenList.add(new Property("verificationStatus", "code", "Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).", 0, java.lang.Integer.MAX_VALUE, verificationStatus));
        childrenList.add(new Property("type", "code", "Identification of the underlying physiological mechanism for the reaction risk.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("category", "code", "Category of the identified substance.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("criticality", "code", "Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.", 0, java.lang.Integer.MAX_VALUE, criticality));
        childrenList.add(new Property("code", "CodeableConcept", "Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., \"Latex\"), an allergy or intolerance condition (e.g., \"Latex allergy\"), or a negated/excluded code for a specific substance or class (e.g., \"No latex allergy\") or a general or categorical negated statement (e.g.,  \"No known allergy\", \"No known drug allergies\").", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient who has the allergy or intolerance.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date,  date-time, or age when allergy or intolerance was identified.", 0, java.lang.Integer.MAX_VALUE, onset));
        childrenList.add(new Property("assertedDate", "dateTime", "The date on which the existance of the AllergyIntolerance was first asserted or acknowledged.", 0, java.lang.Integer.MAX_VALUE, assertedDate));
        childrenList.add(new Property("recorder", "Reference(Practitioner|Patient)", "Individual who recorded the record and takes responsibility for its content.", 0, java.lang.Integer.MAX_VALUE, recorder));
        childrenList.add(new Property("asserter", "Reference(Patient|RelatedPerson|Practitioner)", "The source of the information about the allergy that is recorded.", 0, java.lang.Integer.MAX_VALUE, asserter));
        childrenList.add(new Property("lastOccurrence", "dateTime", "Represents the date and/or time of the last known occurrence of a reaction event.", 0, java.lang.Integer.MAX_VALUE, lastOccurrence));
        childrenList.add(new Property("note", "Annotation", "Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("reaction", "", "Details about each adverse reaction event linked to exposure to the identified substance.", 0, java.lang.Integer.MAX_VALUE, reaction));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -462853915: /*clinicalStatus*/ return this.clinicalStatus == null ? new Base[0] : new Base[] {this.clinicalStatus}; // Enumeration<AllergyIntoleranceClinicalStatus>
        case -842509843: /*verificationStatus*/ return this.verificationStatus == null ? new Base[0] : new Base[] {this.verificationStatus}; // Enumeration<AllergyIntoleranceVerificationStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<AllergyIntoleranceType>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // Enumeration<AllergyIntoleranceCategory>
        case -1608054609: /*criticality*/ return this.criticality == null ? new Base[0] : new Base[] {this.criticality}; // Enumeration<AllergyIntoleranceCriticality>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 105901603: /*onset*/ return this.onset == null ? new Base[0] : new Base[] {this.onset}; // Type
        case -174231629: /*assertedDate*/ return this.assertedDate == null ? new Base[0] : new Base[] {this.assertedDate}; // DateTimeType
        case -799233858: /*recorder*/ return this.recorder == null ? new Base[0] : new Base[] {this.recorder}; // Reference
        case -373242253: /*asserter*/ return this.asserter == null ? new Base[0] : new Base[] {this.asserter}; // Reference
        case 1896977671: /*lastOccurrence*/ return this.lastOccurrence == null ? new Base[0] : new Base[] {this.lastOccurrence}; // DateTimeType
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -867509719: /*reaction*/ return this.reaction == null ? new Base[0] : this.reaction.toArray(new Base[this.reaction.size()]); // AllergyIntoleranceReactionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -462853915: // clinicalStatus
          value = new AllergyIntoleranceClinicalStatusEnumFactory().fromType(castToCode(value));
          this.clinicalStatus = (Enumeration) value; // Enumeration<AllergyIntoleranceClinicalStatus>
          return value;
        case -842509843: // verificationStatus
          value = new AllergyIntoleranceVerificationStatusEnumFactory().fromType(castToCode(value));
          this.verificationStatus = (Enumeration) value; // Enumeration<AllergyIntoleranceVerificationStatus>
          return value;
        case 3575610: // type
          value = new AllergyIntoleranceTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<AllergyIntoleranceType>
          return value;
        case 50511102: // category
          value = new AllergyIntoleranceCategoryEnumFactory().fromType(castToCode(value));
          this.getCategory().add((Enumeration) value); // Enumeration<AllergyIntoleranceCategory>
          return value;
        case -1608054609: // criticality
          value = new AllergyIntoleranceCriticalityEnumFactory().fromType(castToCode(value));
          this.criticality = (Enumeration) value; // Enumeration<AllergyIntoleranceCriticality>
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 105901603: // onset
          this.onset = castToType(value); // Type
          return value;
        case -174231629: // assertedDate
          this.assertedDate = castToDateTime(value); // DateTimeType
          return value;
        case -799233858: // recorder
          this.recorder = castToReference(value); // Reference
          return value;
        case -373242253: // asserter
          this.asserter = castToReference(value); // Reference
          return value;
        case 1896977671: // lastOccurrence
          this.lastOccurrence = castToDateTime(value); // DateTimeType
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -867509719: // reaction
          this.getReaction().add((AllergyIntoleranceReactionComponent) value); // AllergyIntoleranceReactionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("clinicalStatus")) {
          value = new AllergyIntoleranceClinicalStatusEnumFactory().fromType(castToCode(value));
          this.clinicalStatus = (Enumeration) value; // Enumeration<AllergyIntoleranceClinicalStatus>
        } else if (name.equals("verificationStatus")) {
          value = new AllergyIntoleranceVerificationStatusEnumFactory().fromType(castToCode(value));
          this.verificationStatus = (Enumeration) value; // Enumeration<AllergyIntoleranceVerificationStatus>
        } else if (name.equals("type")) {
          value = new AllergyIntoleranceTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<AllergyIntoleranceType>
        } else if (name.equals("category")) {
          value = new AllergyIntoleranceCategoryEnumFactory().fromType(castToCode(value));
          this.getCategory().add((Enumeration) value);
        } else if (name.equals("criticality")) {
          value = new AllergyIntoleranceCriticalityEnumFactory().fromType(castToCode(value));
          this.criticality = (Enumeration) value; // Enumeration<AllergyIntoleranceCriticality>
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("onset[x]")) {
          this.onset = castToType(value); // Type
        } else if (name.equals("assertedDate")) {
          this.assertedDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("recorder")) {
          this.recorder = castToReference(value); // Reference
        } else if (name.equals("asserter")) {
          this.asserter = castToReference(value); // Reference
        } else if (name.equals("lastOccurrence")) {
          this.lastOccurrence = castToDateTime(value); // DateTimeType
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("reaction")) {
          this.getReaction().add((AllergyIntoleranceReactionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -462853915:  return getClinicalStatusElement();
        case -842509843:  return getVerificationStatusElement();
        case 3575610:  return getTypeElement();
        case 50511102:  return addCategoryElement();
        case -1608054609:  return getCriticalityElement();
        case 3059181:  return getCode(); 
        case -791418107:  return getPatient(); 
        case -1886216323:  return getOnset(); 
        case 105901603:  return getOnset(); 
        case -174231629:  return getAssertedDateElement();
        case -799233858:  return getRecorder(); 
        case -373242253:  return getAsserter(); 
        case 1896977671:  return getLastOccurrenceElement();
        case 3387378:  return addNote(); 
        case -867509719:  return addReaction(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -462853915: /*clinicalStatus*/ return new String[] {"code"};
        case -842509843: /*verificationStatus*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"code"};
        case -1608054609: /*criticality*/ return new String[] {"code"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 105901603: /*onset*/ return new String[] {"dateTime", "Age", "Period", "Range", "string"};
        case -174231629: /*assertedDate*/ return new String[] {"dateTime"};
        case -799233858: /*recorder*/ return new String[] {"Reference"};
        case -373242253: /*asserter*/ return new String[] {"Reference"};
        case 1896977671: /*lastOccurrence*/ return new String[] {"dateTime"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -867509719: /*reaction*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("clinicalStatus")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.clinicalStatus");
        }
        else if (name.equals("verificationStatus")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.verificationStatus");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.type");
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.category");
        }
        else if (name.equals("criticality")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.criticality");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("onsetDateTime")) {
          this.onset = new DateTimeType();
          return this.onset;
        }
        else if (name.equals("onsetAge")) {
          this.onset = new Age();
          return this.onset;
        }
        else if (name.equals("onsetPeriod")) {
          this.onset = new Period();
          return this.onset;
        }
        else if (name.equals("onsetRange")) {
          this.onset = new Range();
          return this.onset;
        }
        else if (name.equals("onsetString")) {
          this.onset = new StringType();
          return this.onset;
        }
        else if (name.equals("assertedDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.assertedDate");
        }
        else if (name.equals("recorder")) {
          this.recorder = new Reference();
          return this.recorder;
        }
        else if (name.equals("asserter")) {
          this.asserter = new Reference();
          return this.asserter;
        }
        else if (name.equals("lastOccurrence")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.lastOccurrence");
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("reaction")) {
          return addReaction();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "AllergyIntolerance";

  }

      public AllergyIntolerance copy() {
        AllergyIntolerance dst = new AllergyIntolerance();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.clinicalStatus = clinicalStatus == null ? null : clinicalStatus.copy();
        dst.verificationStatus = verificationStatus == null ? null : verificationStatus.copy();
        dst.type = type == null ? null : type.copy();
        if (category != null) {
          dst.category = new ArrayList<Enumeration<AllergyIntoleranceCategory>>();
          for (Enumeration<AllergyIntoleranceCategory> i : category)
            dst.category.add(i.copy());
        };
        dst.criticality = criticality == null ? null : criticality.copy();
        dst.code = code == null ? null : code.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.onset = onset == null ? null : onset.copy();
        dst.assertedDate = assertedDate == null ? null : assertedDate.copy();
        dst.recorder = recorder == null ? null : recorder.copy();
        dst.asserter = asserter == null ? null : asserter.copy();
        dst.lastOccurrence = lastOccurrence == null ? null : lastOccurrence.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (reaction != null) {
          dst.reaction = new ArrayList<AllergyIntoleranceReactionComponent>();
          for (AllergyIntoleranceReactionComponent i : reaction)
            dst.reaction.add(i.copy());
        };
        return dst;
      }

      protected AllergyIntolerance typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AllergyIntolerance))
          return false;
        AllergyIntolerance o = (AllergyIntolerance) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(clinicalStatus, o.clinicalStatus, true)
           && compareDeep(verificationStatus, o.verificationStatus, true) && compareDeep(type, o.type, true)
           && compareDeep(category, o.category, true) && compareDeep(criticality, o.criticality, true) && compareDeep(code, o.code, true)
           && compareDeep(patient, o.patient, true) && compareDeep(onset, o.onset, true) && compareDeep(assertedDate, o.assertedDate, true)
           && compareDeep(recorder, o.recorder, true) && compareDeep(asserter, o.asserter, true) && compareDeep(lastOccurrence, o.lastOccurrence, true)
           && compareDeep(note, o.note, true) && compareDeep(reaction, o.reaction, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AllergyIntolerance))
          return false;
        AllergyIntolerance o = (AllergyIntolerance) other;
        return compareValues(clinicalStatus, o.clinicalStatus, true) && compareValues(verificationStatus, o.verificationStatus, true)
           && compareValues(type, o.type, true) && compareValues(category, o.category, true) && compareValues(criticality, o.criticality, true)
           && compareValues(assertedDate, o.assertedDate, true) && compareValues(lastOccurrence, o.lastOccurrence, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, clinicalStatus
          , verificationStatus, type, category, criticality, code, patient, onset, assertedDate
          , recorder, asserter, lastOccurrence, note, reaction);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.AllergyIntolerance;
   }

 /**
   * Search parameter: <b>severity</b>
   * <p>
   * Description: <b>mild | moderate | severe (of event as a whole)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.reaction.severity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="severity", path="AllergyIntolerance.reaction.severity", description="mild | moderate | severe (of event as a whole)", type="token" )
  public static final String SP_SEVERITY = "severity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>severity</b>
   * <p>
   * Description: <b>mild | moderate | severe (of event as a whole)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.reaction.severity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SEVERITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SEVERITY);

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Date record was believed accurate</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.assertedDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="AllergyIntolerance.assertedDate", description="Date record was believed accurate", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Date record was believed accurate</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.assertedDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External ids for this item</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="AllergyIntolerance.identifier", description="External ids for this item", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External ids for this item</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>manifestation</b>
   * <p>
   * Description: <b>Clinical symptoms/signs associated with the Event</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.reaction.manifestation</b><br>
   * </p>
   */
  @SearchParamDefinition(name="manifestation", path="AllergyIntolerance.reaction.manifestation", description="Clinical symptoms/signs associated with the Event", type="token" )
  public static final String SP_MANIFESTATION = "manifestation";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>manifestation</b>
   * <p>
   * Description: <b>Clinical symptoms/signs associated with the Event</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.reaction.manifestation</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam MANIFESTATION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_MANIFESTATION);

 /**
   * Search parameter: <b>recorder</b>
   * <p>
   * Description: <b>Who recorded the sensitivity</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.recorder</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recorder", path="AllergyIntolerance.recorder", description="Who recorded the sensitivity", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Patient.class, Practitioner.class } )
  public static final String SP_RECORDER = "recorder";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recorder</b>
   * <p>
   * Description: <b>Who recorded the sensitivity</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.recorder</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECORDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECORDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AllergyIntolerance:recorder</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECORDER = new ca.uhn.fhir.model.api.Include("AllergyIntolerance:recorder").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Code that identifies the allergy or intolerance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.code, AllergyIntolerance.reaction.substance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="AllergyIntolerance.code | AllergyIntolerance.reaction.substance", description="Code that identifies the allergy or intolerance", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Code that identifies the allergy or intolerance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.code, AllergyIntolerance.reaction.substance</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>verification-status</b>
   * <p>
   * Description: <b>unconfirmed | confirmed | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.verificationStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="verification-status", path="AllergyIntolerance.verificationStatus", description="unconfirmed | confirmed | refuted | entered-in-error", type="token" )
  public static final String SP_VERIFICATION_STATUS = "verification-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>verification-status</b>
   * <p>
   * Description: <b>unconfirmed | confirmed | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.verificationStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERIFICATION_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERIFICATION_STATUS);

 /**
   * Search parameter: <b>criticality</b>
   * <p>
   * Description: <b>low | high | unable-to-assess</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.criticality</b><br>
   * </p>
   */
  @SearchParamDefinition(name="criticality", path="AllergyIntolerance.criticality", description="low | high | unable-to-assess", type="token" )
  public static final String SP_CRITICALITY = "criticality";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>criticality</b>
   * <p>
   * Description: <b>low | high | unable-to-assess</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.criticality</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CRITICALITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CRITICALITY);

 /**
   * Search parameter: <b>clinical-status</b>
   * <p>
   * Description: <b>active | inactive | resolved</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.clinicalStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="clinical-status", path="AllergyIntolerance.clinicalStatus", description="active | inactive | resolved", type="token" )
  public static final String SP_CLINICAL_STATUS = "clinical-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>clinical-status</b>
   * <p>
   * Description: <b>active | inactive | resolved</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.clinicalStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CLINICAL_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CLINICAL_STATUS);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>allergy | intolerance - Underlying mechanism (if known)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="AllergyIntolerance.type", description="allergy | intolerance - Underlying mechanism (if known)", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>allergy | intolerance - Underlying mechanism (if known)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>onset</b>
   * <p>
   * Description: <b>Date(/time) when manifestations showed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.reaction.onset</b><br>
   * </p>
   */
  @SearchParamDefinition(name="onset", path="AllergyIntolerance.reaction.onset", description="Date(/time) when manifestations showed", type="date" )
  public static final String SP_ONSET = "onset";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>onset</b>
   * <p>
   * Description: <b>Date(/time) when manifestations showed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.reaction.onset</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ONSET = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ONSET);

 /**
   * Search parameter: <b>route</b>
   * <p>
   * Description: <b>How the subject was exposed to the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.reaction.exposureRoute</b><br>
   * </p>
   */
  @SearchParamDefinition(name="route", path="AllergyIntolerance.reaction.exposureRoute", description="How the subject was exposed to the substance", type="token" )
  public static final String SP_ROUTE = "route";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>route</b>
   * <p>
   * Description: <b>How the subject was exposed to the substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.reaction.exposureRoute</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ROUTE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ROUTE);

 /**
   * Search parameter: <b>asserter</b>
   * <p>
   * Description: <b>Source of the information about the allergy</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.asserter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="asserter", path="AllergyIntolerance.asserter", description="Source of the information about the allergy", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_ASSERTER = "asserter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>asserter</b>
   * <p>
   * Description: <b>Source of the information about the allergy</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.asserter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ASSERTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ASSERTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AllergyIntolerance:asserter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ASSERTER = new ca.uhn.fhir.model.api.Include("AllergyIntolerance:asserter").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who the sensitivity is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="AllergyIntolerance.patient", description="Who the sensitivity is for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who the sensitivity is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AllergyIntolerance:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("AllergyIntolerance:patient").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>food | medication | environment | biologic</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="AllergyIntolerance.category", description="food | medication | environment | biologic", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>food | medication | environment | biologic</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>last-date</b>
   * <p>
   * Description: <b>Date(/time) of last known occurrence of a reaction</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.lastOccurrence</b><br>
   * </p>
   */
  @SearchParamDefinition(name="last-date", path="AllergyIntolerance.lastOccurrence", description="Date(/time) of last known occurrence of a reaction", type="date" )
  public static final String SP_LAST_DATE = "last-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>last-date</b>
   * <p>
   * Description: <b>Date(/time) of last known occurrence of a reaction</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.lastOccurrence</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam LAST_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_LAST_DATE);


}

