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
 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
 */
@ResourceDef(name="AllergyIntolerance", profile="http://hl7.org/fhir/Profile/AllergyIntolerance")
public class AllergyIntolerance extends DomainResource {

    public enum AllergyIntoleranceStatus {
        /**
         * An active record of a risk of a reaction to the identified substance.
         */
        ACTIVE, 
        /**
         * A high level of certainty about the propensity for a reaction to the identified substance, which may include clinical evidence by testing or rechallenge.
         */
        ACTIVECONFIRMED, 
        /**
         * An inactivated record of a risk of a reaction to the identified substance
         */
        INACTIVE, 
        /**
         * A reaction to the identified substance has been clinically reassessed by testing or rechallenge re-exposure and considered to be resolved e.g. change the word rechallenged to re-exposure
         */
        RESOLVED, 
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
        public static AllergyIntoleranceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("active-confirmed".equals(codeString))
          return ACTIVECONFIRMED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("resolved".equals(codeString))
          return RESOLVED;
        if ("refuted".equals(codeString))
          return REFUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case ACTIVECONFIRMED: return "active-confirmed";
            case INACTIVE: return "inactive";
            case RESOLVED: return "resolved";
            case REFUTED: return "refuted";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/allergy-intolerance-status";
            case ACTIVECONFIRMED: return "http://hl7.org/fhir/allergy-intolerance-status";
            case INACTIVE: return "http://hl7.org/fhir/allergy-intolerance-status";
            case RESOLVED: return "http://hl7.org/fhir/allergy-intolerance-status";
            case REFUTED: return "http://hl7.org/fhir/allergy-intolerance-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/allergy-intolerance-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "An active record of a risk of a reaction to the identified substance.";
            case ACTIVECONFIRMED: return "A high level of certainty about the propensity for a reaction to the identified substance, which may include clinical evidence by testing or rechallenge.";
            case INACTIVE: return "An inactivated record of a risk of a reaction to the identified substance";
            case RESOLVED: return "A reaction to the identified substance has been clinically reassessed by testing or rechallenge re-exposure and considered to be resolved e.g. change the word rechallenged to re-exposure";
            case REFUTED: return "A propensity for a reaction to the identified substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.";
            case ENTEREDINERROR: return "The statement was entered in error and is not valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case ACTIVECONFIRMED: return "Active Confirmed";
            case INACTIVE: return "Inactive";
            case RESOLVED: return "Resolved";
            case REFUTED: return "Refuted";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceStatusEnumFactory implements EnumFactory<AllergyIntoleranceStatus> {
    public AllergyIntoleranceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return AllergyIntoleranceStatus.ACTIVE;
        if ("active-confirmed".equals(codeString))
          return AllergyIntoleranceStatus.ACTIVECONFIRMED;
        if ("inactive".equals(codeString))
          return AllergyIntoleranceStatus.INACTIVE;
        if ("resolved".equals(codeString))
          return AllergyIntoleranceStatus.RESOLVED;
        if ("refuted".equals(codeString))
          return AllergyIntoleranceStatus.REFUTED;
        if ("entered-in-error".equals(codeString))
          return AllergyIntoleranceStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceStatus code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.ACTIVE);
        if ("active-confirmed".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.ACTIVECONFIRMED);
        if ("inactive".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.INACTIVE);
        if ("resolved".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.RESOLVED);
        if ("refuted".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.REFUTED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown AllergyIntoleranceStatus code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceStatus code) {
      if (code == AllergyIntoleranceStatus.ACTIVE)
        return "active";
      if (code == AllergyIntoleranceStatus.ACTIVECONFIRMED)
        return "active-confirmed";
      if (code == AllergyIntoleranceStatus.INACTIVE)
        return "inactive";
      if (code == AllergyIntoleranceStatus.RESOLVED)
        return "resolved";
      if (code == AllergyIntoleranceStatus.REFUTED)
        return "refuted";
      if (code == AllergyIntoleranceStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(AllergyIntoleranceStatus code) {
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
          if (code == null || code.isEmpty())
            return null;
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
         * A preparation that is synthesized from living organisms or their products, especially a human or animal protein, such as a hormone or antitoxin, that is used as a diagnostic, preventive, or therapeutic agent. Also called biological drug.  Examples of biological products include: vaccines; blood and blood products for transfusion and/or manufacturing into other products; allergenic extracts, which are used for both diagnosis and treatment (for example, allergy shots); human cells and tissues used for transplantation (for example, tendons, ligaments and bone); gene therapies; cellular therapies; tests to screen potential blood donors for infectious agents such as HIV.
         */
        BIOLOGIC, 
        /**
         * Substances that are encountered in the environment.
         */
        ENVIRONMENT, 
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
        if ("biologic".equals(codeString))
          return BIOLOGIC;
        if ("environment".equals(codeString))
          return ENVIRONMENT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FOOD: return "food";
            case MEDICATION: return "medication";
            case BIOLOGIC: return "biologic";
            case ENVIRONMENT: return "environment";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case FOOD: return "http://hl7.org/fhir/allergy-intolerance-category";
            case MEDICATION: return "http://hl7.org/fhir/allergy-intolerance-category";
            case BIOLOGIC: return "http://hl7.org/fhir/allergy-intolerance-category";
            case ENVIRONMENT: return "http://hl7.org/fhir/allergy-intolerance-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case FOOD: return "Any substance consumed to provide nutritional support for the body.";
            case MEDICATION: return "Substances administered to achieve a physiological effect.";
            case BIOLOGIC: return "A preparation that is synthesized from living organisms or their products, especially a human or animal protein, such as a hormone or antitoxin, that is used as a diagnostic, preventive, or therapeutic agent. Also called biological drug.  Examples of biological products include: vaccines; blood and blood products for transfusion and/or manufacturing into other products; allergenic extracts, which are used for both diagnosis and treatment (for example, allergy shots); human cells and tissues used for transplantation (for example, tendons, ligaments and bone); gene therapies; cellular therapies; tests to screen potential blood donors for infectious agents such as HIV.";
            case ENVIRONMENT: return "Substances that are encountered in the environment.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FOOD: return "Food";
            case MEDICATION: return "Medication";
            case BIOLOGIC: return "Biologic";
            case ENVIRONMENT: return "Environment";
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
        if ("biologic".equals(codeString))
          return AllergyIntoleranceCategory.BIOLOGIC;
        if ("environment".equals(codeString))
          return AllergyIntoleranceCategory.ENVIRONMENT;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceCategory> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("food".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.FOOD);
        if ("medication".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.MEDICATION);
        if ("biologic".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.BIOLOGIC);
        if ("environment".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.ENVIRONMENT);
        throw new FHIRException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceCategory code) {
      if (code == AllergyIntoleranceCategory.FOOD)
        return "food";
      if (code == AllergyIntoleranceCategory.MEDICATION)
        return "medication";
      if (code == AllergyIntoleranceCategory.BIOLOGIC)
        return "biologic";
      if (code == AllergyIntoleranceCategory.ENVIRONMENT)
        return "environment";
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
          if (code == null || code.isEmpty())
            return null;
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

    public enum AllergyIntoleranceCertainty {
        /**
         * There is a low level of clinical certainty that the reaction was caused by the identified substance.
         */
        UNLIKELY, 
        /**
         * There is a high level of clinical certainty that the reaction was caused by the identified substance.
         */
        LIKELY, 
        /**
         * There is a very high level of clinical certainty that the reaction was due to the identified substance, which may include clinical evidence by testing or rechallenge.
         */
        CONFIRMED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static AllergyIntoleranceCertainty fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unlikely".equals(codeString))
          return UNLIKELY;
        if ("likely".equals(codeString))
          return LIKELY;
        if ("confirmed".equals(codeString))
          return CONFIRMED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown AllergyIntoleranceCertainty code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNLIKELY: return "unlikely";
            case LIKELY: return "likely";
            case CONFIRMED: return "confirmed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UNLIKELY: return "http://hl7.org/fhir/reaction-event-certainty";
            case LIKELY: return "http://hl7.org/fhir/reaction-event-certainty";
            case CONFIRMED: return "http://hl7.org/fhir/reaction-event-certainty";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNLIKELY: return "There is a low level of clinical certainty that the reaction was caused by the identified substance.";
            case LIKELY: return "There is a high level of clinical certainty that the reaction was caused by the identified substance.";
            case CONFIRMED: return "There is a very high level of clinical certainty that the reaction was due to the identified substance, which may include clinical evidence by testing or rechallenge.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNLIKELY: return "Unlikely";
            case LIKELY: return "Likely";
            case CONFIRMED: return "Confirmed";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceCertaintyEnumFactory implements EnumFactory<AllergyIntoleranceCertainty> {
    public AllergyIntoleranceCertainty fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unlikely".equals(codeString))
          return AllergyIntoleranceCertainty.UNLIKELY;
        if ("likely".equals(codeString))
          return AllergyIntoleranceCertainty.LIKELY;
        if ("confirmed".equals(codeString))
          return AllergyIntoleranceCertainty.CONFIRMED;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceCertainty code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceCertainty> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("unlikely".equals(codeString))
          return new Enumeration<AllergyIntoleranceCertainty>(this, AllergyIntoleranceCertainty.UNLIKELY);
        if ("likely".equals(codeString))
          return new Enumeration<AllergyIntoleranceCertainty>(this, AllergyIntoleranceCertainty.LIKELY);
        if ("confirmed".equals(codeString))
          return new Enumeration<AllergyIntoleranceCertainty>(this, AllergyIntoleranceCertainty.CONFIRMED);
        throw new FHIRException("Unknown AllergyIntoleranceCertainty code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceCertainty code) {
      if (code == AllergyIntoleranceCertainty.UNLIKELY)
        return "unlikely";
      if (code == AllergyIntoleranceCertainty.LIKELY)
        return "likely";
      if (code == AllergyIntoleranceCertainty.CONFIRMED)
        return "confirmed";
      return "?";
      }
    public String toSystem(AllergyIntoleranceCertainty code) {
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
          if (code == null || code.isEmpty())
            return null;
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
        @Child(name = "substance", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific substance or pharmaceutical product considered to be responsible for event", formalDefinition="Identification of the specific substance (or pharmaceutical product) considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different from the substance identified as the cause of the risk, but it must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite product that includes the identified substance. It must be clinically safe to only process the 'code' and ignore the 'reaction.substance'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/substance-code")
        protected CodeableConcept substance;

        /**
         * Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.
         */
        @Child(name = "certainty", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="unlikely | likely | confirmed - clinical certainty about the specific substance", formalDefinition="Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reaction-event-certainty")
        protected Enumeration<AllergyIntoleranceCertainty> certainty;

        /**
         * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
         */
        @Child(name = "manifestation", type = {CodeableConcept.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Clinical symptoms/signs associated with the Event", formalDefinition="Clinical symptoms and/or signs that are observed or associated with the adverse reaction event." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/manifestation-codes")
        protected List<CodeableConcept> manifestation;

        /**
         * Text description about the reaction as a whole, including details of the manifestation if required.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the event as a whole", formalDefinition="Text description about the reaction as a whole, including details of the manifestation if required." )
        protected StringType description;

        /**
         * Record of the date and/or time of the onset of the Reaction.
         */
        @Child(name = "onset", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Date(/time) when manifestations showed", formalDefinition="Record of the date and/or time of the onset of the Reaction." )
        protected DateTimeType onset;

        /**
         * Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.
         */
        @Child(name = "severity", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="mild | moderate | severe (of event as a whole)", formalDefinition="Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reaction-event-severity")
        protected Enumeration<AllergyIntoleranceSeverity> severity;

        /**
         * Identification of the route by which the subject was exposed to the substance.
         */
        @Child(name = "exposureRoute", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How the subject was exposed to the substance", formalDefinition="Identification of the route by which the subject was exposed to the substance." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/route-codes")
        protected CodeableConcept exposureRoute;

        /**
         * Additional text about the adverse reaction event not captured in other fields.
         */
        @Child(name = "note", type = {Annotation.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Text about event not captured in other fields", formalDefinition="Additional text about the adverse reaction event not captured in other fields." )
        protected List<Annotation> note;

        private static final long serialVersionUID = -31700461L;

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
         * @return {@link #certainty} (Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.). This is the underlying object with id, value and extensions. The accessor "getCertainty" gives direct access to the value
         */
        public Enumeration<AllergyIntoleranceCertainty> getCertaintyElement() { 
          if (this.certainty == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AllergyIntoleranceReactionComponent.certainty");
            else if (Configuration.doAutoCreate())
              this.certainty = new Enumeration<AllergyIntoleranceCertainty>(new AllergyIntoleranceCertaintyEnumFactory()); // bb
          return this.certainty;
        }

        public boolean hasCertaintyElement() { 
          return this.certainty != null && !this.certainty.isEmpty();
        }

        public boolean hasCertainty() { 
          return this.certainty != null && !this.certainty.isEmpty();
        }

        /**
         * @param value {@link #certainty} (Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.). This is the underlying object with id, value and extensions. The accessor "getCertainty" gives direct access to the value
         */
        public AllergyIntoleranceReactionComponent setCertaintyElement(Enumeration<AllergyIntoleranceCertainty> value) { 
          this.certainty = value;
          return this;
        }

        /**
         * @return Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.
         */
        public AllergyIntoleranceCertainty getCertainty() { 
          return this.certainty == null ? null : this.certainty.getValue();
        }

        /**
         * @param value Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.
         */
        public AllergyIntoleranceReactionComponent setCertainty(AllergyIntoleranceCertainty value) { 
          if (value == null)
            this.certainty = null;
          else {
            if (this.certainty == null)
              this.certainty = new Enumeration<AllergyIntoleranceCertainty>(new AllergyIntoleranceCertaintyEnumFactory());
            this.certainty.setValue(value);
          }
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
          childrenList.add(new Property("certainty", "code", "Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.", 0, java.lang.Integer.MAX_VALUE, certainty));
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
        case -1404142937: /*certainty*/ return this.certainty == null ? new Base[0] : new Base[] {this.certainty}; // Enumeration<AllergyIntoleranceCertainty>
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 530040176: // substance
          this.substance = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1404142937: // certainty
          this.certainty = new AllergyIntoleranceCertaintyEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCertainty>
          break;
        case 1115984422: // manifestation
          this.getManifestation().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        case 105901603: // onset
          this.onset = castToDateTime(value); // DateTimeType
          break;
        case 1478300413: // severity
          this.severity = new AllergyIntoleranceSeverityEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceSeverity>
          break;
        case 421286274: // exposureRoute
          this.exposureRoute = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("substance"))
          this.substance = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("certainty"))
          this.certainty = new AllergyIntoleranceCertaintyEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCertainty>
        else if (name.equals("manifestation"))
          this.getManifestation().add(castToCodeableConcept(value));
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("onset"))
          this.onset = castToDateTime(value); // DateTimeType
        else if (name.equals("severity"))
          this.severity = new AllergyIntoleranceSeverityEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceSeverity>
        else if (name.equals("exposureRoute"))
          this.exposureRoute = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 530040176:  return getSubstance(); // CodeableConcept
        case -1404142937: throw new FHIRException("Cannot make property certainty as it is not a complex type"); // Enumeration<AllergyIntoleranceCertainty>
        case 1115984422:  return addManifestation(); // CodeableConcept
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        case 105901603: throw new FHIRException("Cannot make property onset as it is not a complex type"); // DateTimeType
        case 1478300413: throw new FHIRException("Cannot make property severity as it is not a complex type"); // Enumeration<AllergyIntoleranceSeverity>
        case 421286274:  return getExposureRoute(); // CodeableConcept
        case 3387378:  return addNote(); // Annotation
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("substance")) {
          this.substance = new CodeableConcept();
          return this.substance;
        }
        else if (name.equals("certainty")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.certainty");
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
        dst.certainty = certainty == null ? null : certainty.copy();
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
        return compareDeep(substance, o.substance, true) && compareDeep(certainty, o.certainty, true) && compareDeep(manifestation, o.manifestation, true)
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
        return compareValues(certainty, o.certainty, true) && compareValues(description, o.description, true)
           && compareValues(onset, o.onset, true) && compareValues(severity, o.severity, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(substance, certainty, manifestation
          , description, onset, severity, exposureRoute, note);
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
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | active-confirmed | inactive | resolved | refuted | entered-in-error", formalDefinition="Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-intolerance-status")
    protected Enumeration<AllergyIntoleranceStatus> status;

    /**
     * Identification of the underlying physiological mechanism for the reaction risk.
     */
    @Child(name = "type", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="allergy | intolerance - Underlying mechanism (if known)", formalDefinition="Identification of the underlying physiological mechanism for the reaction risk." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-intolerance-type")
    protected Enumeration<AllergyIntoleranceType> type;

    /**
     * Category of the identified substance.
     */
    @Child(name = "category", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="food | medication | biologic | environment", formalDefinition="Category of the identified substance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-intolerance-category")
    protected Enumeration<AllergyIntoleranceCategory> category;

    /**
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.
     */
    @Child(name = "criticality", type = {CodeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="low | high | unable-to-assess", formalDefinition="Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergy-intolerance-criticality")
    protected Enumeration<AllergyIntoleranceCriticality> criticality;

    /**
     * Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., "Latex"), an allergy or intolerance condition (e.g., "Latex allergy"), or a negated/excluded code for a specific substance or class (e.g., "No latex allergy") or a general or categorical negated statement (e.g.,  "No known allergy", "No known drug allergies").
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Allergy or intolerance code", formalDefinition="Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., \"Latex\"), an allergy or intolerance condition (e.g., \"Latex allergy\"), or a negated/excluded code for a specific substance or class (e.g., \"No latex allergy\") or a general or categorical negated statement (e.g.,  \"No known allergy\", \"No known drug allergies\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/allergyintolerance-code")
    protected CodeableConcept code;

    /**
     * The patient who has the allergy or intolerance.
     */
    @Child(name = "patient", type = {Patient.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the sensitivity is for", formalDefinition="The patient who has the allergy or intolerance." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient who has the allergy or intolerance.)
     */
    protected Patient patientTarget;

    /**
     * Indicates the most recent date on which the recorder has asserted that the information represented by this AllergyIntolerance record is accurate.
     */
    @Child(name = "attestedDate", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date record was believed accurate", formalDefinition="Indicates the most recent date on which the recorder has asserted that the information represented by this AllergyIntolerance record is accurate." )
    protected DateTimeType attestedDate;

    /**
     * Individual who recorded the record and takes responsibility for its content.
     */
    @Child(name = "recorder", type = {Practitioner.class, Patient.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who recorded the sensitivity", formalDefinition="Individual who recorded the record and takes responsibility for its content." )
    protected Reference recorder;

    /**
     * The actual object that is the target of the reference (Individual who recorded the record and takes responsibility for its content.)
     */
    protected Resource recorderTarget;

    /**
     * The source of the information about the allergy that is recorded.
     */
    @Child(name = "reporter", type = {Patient.class, RelatedPerson.class, Practitioner.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Source of the information about the allergy", formalDefinition="The source of the information about the allergy that is recorded." )
    protected Reference reporter;

    /**
     * The actual object that is the target of the reference (The source of the information about the allergy that is recorded.)
     */
    protected Resource reporterTarget;

    /**
     * Record of the date and/or time of the onset of the Allergy or Intolerance.
     */
    @Child(name = "onset", type = {DateTimeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date(/time) when manifestations showed", formalDefinition="Record of the date and/or time of the onset of the Allergy or Intolerance." )
    protected DateTimeType onset;

    /**
     * Represents the date and/or time of the last known occurrence of a reaction event.
     */
    @Child(name = "lastOccurrence", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date(/time) of last known occurrence of a reaction", formalDefinition="Represents the date and/or time of the last known occurrence of a reaction event." )
    protected DateTimeType lastOccurrence;

    /**
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     */
    @Child(name = "note", type = {Annotation.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional text not captured in other fields", formalDefinition="Additional narrative about the propensity for the Adverse Reaction, not captured in other fields." )
    protected List<Annotation> note;

    /**
     * Details about each adverse reaction event linked to exposure to the identified substance.
     */
    @Child(name = "reaction", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Adverse Reaction Events linked to exposure to substance", formalDefinition="Details about each adverse reaction event linked to exposure to the identified substance." )
    protected List<AllergyIntoleranceReactionComponent> reaction;

    private static final long serialVersionUID = 2033085856L;

  /**
   * Constructor
   */
    public AllergyIntolerance() {
      super();
    }

  /**
   * Constructor
   */
    public AllergyIntolerance(Reference patient) {
      super();
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
     * @return {@link #status} (Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<AllergyIntoleranceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<AllergyIntoleranceStatus>(new AllergyIntoleranceStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public AllergyIntolerance setStatusElement(Enumeration<AllergyIntoleranceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).
     */
    public AllergyIntoleranceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).
     */
    public AllergyIntolerance setStatus(AllergyIntoleranceStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<AllergyIntoleranceStatus>(new AllergyIntoleranceStatusEnumFactory());
        this.status.setValue(value);
      }
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
     * @return {@link #category} (Category of the identified substance.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<AllergyIntoleranceCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory()); // bb
      return this.category;
    }

    public boolean hasCategoryElement() { 
      return this.category != null && !this.category.isEmpty();
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Category of the identified substance.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public AllergyIntolerance setCategoryElement(Enumeration<AllergyIntoleranceCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return Category of the identified substance.
     */
    public AllergyIntoleranceCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value Category of the identified substance.
     */
    public AllergyIntolerance setCategory(AllergyIntoleranceCategory value) { 
      if (value == null)
        this.category = null;
      else {
        if (this.category == null)
          this.category = new Enumeration<AllergyIntoleranceCategory>(new AllergyIntoleranceCategoryEnumFactory());
        this.category.setValue(value);
      }
      return this;
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
     * @return {@link #attestedDate} (Indicates the most recent date on which the recorder has asserted that the information represented by this AllergyIntolerance record is accurate.). This is the underlying object with id, value and extensions. The accessor "getAttestedDate" gives direct access to the value
     */
    public DateTimeType getAttestedDateElement() { 
      if (this.attestedDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.attestedDate");
        else if (Configuration.doAutoCreate())
          this.attestedDate = new DateTimeType(); // bb
      return this.attestedDate;
    }

    public boolean hasAttestedDateElement() { 
      return this.attestedDate != null && !this.attestedDate.isEmpty();
    }

    public boolean hasAttestedDate() { 
      return this.attestedDate != null && !this.attestedDate.isEmpty();
    }

    /**
     * @param value {@link #attestedDate} (Indicates the most recent date on which the recorder has asserted that the information represented by this AllergyIntolerance record is accurate.). This is the underlying object with id, value and extensions. The accessor "getAttestedDate" gives direct access to the value
     */
    public AllergyIntolerance setAttestedDateElement(DateTimeType value) { 
      this.attestedDate = value;
      return this;
    }

    /**
     * @return Indicates the most recent date on which the recorder has asserted that the information represented by this AllergyIntolerance record is accurate.
     */
    public Date getAttestedDate() { 
      return this.attestedDate == null ? null : this.attestedDate.getValue();
    }

    /**
     * @param value Indicates the most recent date on which the recorder has asserted that the information represented by this AllergyIntolerance record is accurate.
     */
    public AllergyIntolerance setAttestedDate(Date value) { 
      if (value == null)
        this.attestedDate = null;
      else {
        if (this.attestedDate == null)
          this.attestedDate = new DateTimeType();
        this.attestedDate.setValue(value);
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
     * @return {@link #reporter} (The source of the information about the allergy that is recorded.)
     */
    public Reference getReporter() { 
      if (this.reporter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.reporter");
        else if (Configuration.doAutoCreate())
          this.reporter = new Reference(); // cc
      return this.reporter;
    }

    public boolean hasReporter() { 
      return this.reporter != null && !this.reporter.isEmpty();
    }

    /**
     * @param value {@link #reporter} (The source of the information about the allergy that is recorded.)
     */
    public AllergyIntolerance setReporter(Reference value) { 
      this.reporter = value;
      return this;
    }

    /**
     * @return {@link #reporter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The source of the information about the allergy that is recorded.)
     */
    public Resource getReporterTarget() { 
      return this.reporterTarget;
    }

    /**
     * @param value {@link #reporter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The source of the information about the allergy that is recorded.)
     */
    public AllergyIntolerance setReporterTarget(Resource value) { 
      this.reporterTarget = value;
      return this;
    }

    /**
     * @return {@link #onset} (Record of the date and/or time of the onset of the Allergy or Intolerance.). This is the underlying object with id, value and extensions. The accessor "getOnset" gives direct access to the value
     */
    public DateTimeType getOnsetElement() { 
      if (this.onset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.onset");
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
     * @param value {@link #onset} (Record of the date and/or time of the onset of the Allergy or Intolerance.). This is the underlying object with id, value and extensions. The accessor "getOnset" gives direct access to the value
     */
    public AllergyIntolerance setOnsetElement(DateTimeType value) { 
      this.onset = value;
      return this;
    }

    /**
     * @return Record of the date and/or time of the onset of the Allergy or Intolerance.
     */
    public Date getOnset() { 
      return this.onset == null ? null : this.onset.getValue();
    }

    /**
     * @param value Record of the date and/or time of the onset of the Allergy or Intolerance.
     */
    public AllergyIntolerance setOnset(Date value) { 
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
        childrenList.add(new Property("status", "code", "Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified substance (including pharmaceutical product).", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "code", "Identification of the underlying physiological mechanism for the reaction risk.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("category", "code", "Category of the identified substance.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("criticality", "code", "Estimate of the potential clinical harm, or seriousness, of the reaction to the identified substance.", 0, java.lang.Integer.MAX_VALUE, criticality));
        childrenList.add(new Property("code", "CodeableConcept", "Code for an allergy or intolerance statement (either a positive or a negated/excluded statement).  This may be a code for a substance or pharmaceutical product that is considered to be responsible for the adverse reaction risk (e.g., \"Latex\"), an allergy or intolerance condition (e.g., \"Latex allergy\"), or a negated/excluded code for a specific substance or class (e.g., \"No latex allergy\") or a general or categorical negated statement (e.g.,  \"No known allergy\", \"No known drug allergies\").", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient who has the allergy or intolerance.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("attestedDate", "dateTime", "Indicates the most recent date on which the recorder has asserted that the information represented by this AllergyIntolerance record is accurate.", 0, java.lang.Integer.MAX_VALUE, attestedDate));
        childrenList.add(new Property("recorder", "Reference(Practitioner|Patient)", "Individual who recorded the record and takes responsibility for its content.", 0, java.lang.Integer.MAX_VALUE, recorder));
        childrenList.add(new Property("reporter", "Reference(Patient|RelatedPerson|Practitioner)", "The source of the information about the allergy that is recorded.", 0, java.lang.Integer.MAX_VALUE, reporter));
        childrenList.add(new Property("onset", "dateTime", "Record of the date and/or time of the onset of the Allergy or Intolerance.", 0, java.lang.Integer.MAX_VALUE, onset));
        childrenList.add(new Property("lastOccurrence", "dateTime", "Represents the date and/or time of the last known occurrence of a reaction event.", 0, java.lang.Integer.MAX_VALUE, lastOccurrence));
        childrenList.add(new Property("note", "Annotation", "Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("reaction", "", "Details about each adverse reaction event linked to exposure to the identified substance.", 0, java.lang.Integer.MAX_VALUE, reaction));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<AllergyIntoleranceStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<AllergyIntoleranceType>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<AllergyIntoleranceCategory>
        case -1608054609: /*criticality*/ return this.criticality == null ? new Base[0] : new Base[] {this.criticality}; // Enumeration<AllergyIntoleranceCriticality>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -424886158: /*attestedDate*/ return this.attestedDate == null ? new Base[0] : new Base[] {this.attestedDate}; // DateTimeType
        case -799233858: /*recorder*/ return this.recorder == null ? new Base[0] : new Base[] {this.recorder}; // Reference
        case -427039519: /*reporter*/ return this.reporter == null ? new Base[0] : new Base[] {this.reporter}; // Reference
        case 105901603: /*onset*/ return this.onset == null ? new Base[0] : new Base[] {this.onset}; // DateTimeType
        case 1896977671: /*lastOccurrence*/ return this.lastOccurrence == null ? new Base[0] : new Base[] {this.lastOccurrence}; // DateTimeType
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -867509719: /*reaction*/ return this.reaction == null ? new Base[0] : this.reaction.toArray(new Base[this.reaction.size()]); // AllergyIntoleranceReactionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -892481550: // status
          this.status = new AllergyIntoleranceStatusEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceStatus>
          break;
        case 3575610: // type
          this.type = new AllergyIntoleranceTypeEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceType>
          break;
        case 50511102: // category
          this.category = new AllergyIntoleranceCategoryEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCategory>
          break;
        case -1608054609: // criticality
          this.criticality = new AllergyIntoleranceCriticalityEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCriticality>
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -424886158: // attestedDate
          this.attestedDate = castToDateTime(value); // DateTimeType
          break;
        case -799233858: // recorder
          this.recorder = castToReference(value); // Reference
          break;
        case -427039519: // reporter
          this.reporter = castToReference(value); // Reference
          break;
        case 105901603: // onset
          this.onset = castToDateTime(value); // DateTimeType
          break;
        case 1896977671: // lastOccurrence
          this.lastOccurrence = castToDateTime(value); // DateTimeType
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case -867509719: // reaction
          this.getReaction().add((AllergyIntoleranceReactionComponent) value); // AllergyIntoleranceReactionComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new AllergyIntoleranceStatusEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceStatus>
        else if (name.equals("type"))
          this.type = new AllergyIntoleranceTypeEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceType>
        else if (name.equals("category"))
          this.category = new AllergyIntoleranceCategoryEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCategory>
        else if (name.equals("criticality"))
          this.criticality = new AllergyIntoleranceCriticalityEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCriticality>
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("attestedDate"))
          this.attestedDate = castToDateTime(value); // DateTimeType
        else if (name.equals("recorder"))
          this.recorder = castToReference(value); // Reference
        else if (name.equals("reporter"))
          this.reporter = castToReference(value); // Reference
        else if (name.equals("onset"))
          this.onset = castToDateTime(value); // DateTimeType
        else if (name.equals("lastOccurrence"))
          this.lastOccurrence = castToDateTime(value); // DateTimeType
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("reaction"))
          this.getReaction().add((AllergyIntoleranceReactionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<AllergyIntoleranceStatus>
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<AllergyIntoleranceType>
        case 50511102: throw new FHIRException("Cannot make property category as it is not a complex type"); // Enumeration<AllergyIntoleranceCategory>
        case -1608054609: throw new FHIRException("Cannot make property criticality as it is not a complex type"); // Enumeration<AllergyIntoleranceCriticality>
        case 3059181:  return getCode(); // CodeableConcept
        case -791418107:  return getPatient(); // Reference
        case -424886158: throw new FHIRException("Cannot make property attestedDate as it is not a complex type"); // DateTimeType
        case -799233858:  return getRecorder(); // Reference
        case -427039519:  return getReporter(); // Reference
        case 105901603: throw new FHIRException("Cannot make property onset as it is not a complex type"); // DateTimeType
        case 1896977671: throw new FHIRException("Cannot make property lastOccurrence as it is not a complex type"); // DateTimeType
        case 3387378:  return addNote(); // Annotation
        case -867509719:  return addReaction(); // AllergyIntoleranceReactionComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.status");
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
        else if (name.equals("attestedDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.attestedDate");
        }
        else if (name.equals("recorder")) {
          this.recorder = new Reference();
          return this.recorder;
        }
        else if (name.equals("reporter")) {
          this.reporter = new Reference();
          return this.reporter;
        }
        else if (name.equals("onset")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.onset");
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
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.category = category == null ? null : category.copy();
        dst.criticality = criticality == null ? null : criticality.copy();
        dst.code = code == null ? null : code.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.attestedDate = attestedDate == null ? null : attestedDate.copy();
        dst.recorder = recorder == null ? null : recorder.copy();
        dst.reporter = reporter == null ? null : reporter.copy();
        dst.onset = onset == null ? null : onset.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(category, o.category, true) && compareDeep(criticality, o.criticality, true) && compareDeep(code, o.code, true)
           && compareDeep(patient, o.patient, true) && compareDeep(attestedDate, o.attestedDate, true) && compareDeep(recorder, o.recorder, true)
           && compareDeep(reporter, o.reporter, true) && compareDeep(onset, o.onset, true) && compareDeep(lastOccurrence, o.lastOccurrence, true)
           && compareDeep(note, o.note, true) && compareDeep(reaction, o.reaction, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AllergyIntolerance))
          return false;
        AllergyIntolerance o = (AllergyIntolerance) other;
        return compareValues(status, o.status, true) && compareValues(type, o.type, true) && compareValues(category, o.category, true)
           && compareValues(criticality, o.criticality, true) && compareValues(attestedDate, o.attestedDate, true)
           && compareValues(onset, o.onset, true) && compareValues(lastOccurrence, o.lastOccurrence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , category, criticality, code, patient, attestedDate, recorder, reporter, onset
          , lastOccurrence, note, reaction);
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
   * Path: <b>AllergyIntolerance.attestedDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="AllergyIntolerance.attestedDate", description="Date record was believed accurate", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Date record was believed accurate</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.attestedDate</b><br>
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
   * Description: <b>Allergy or intolerance code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.code, AllergyIntolerance.reaction.substance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="AllergyIntolerance.code | AllergyIntolerance.reaction.substance", description="Allergy or intolerance code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Allergy or intolerance code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.code, AllergyIntolerance.reaction.substance</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

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
   * Search parameter: <b>reporter</b>
   * <p>
   * Description: <b>Source of the information about the allergy</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.reporter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reporter", path="AllergyIntolerance.reporter", description="Source of the information about the allergy", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_REPORTER = "reporter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reporter</b>
   * <p>
   * Description: <b>Source of the information about the allergy</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>AllergyIntolerance.reporter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REPORTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REPORTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>AllergyIntolerance:reporter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REPORTER = new ca.uhn.fhir.model.api.Include("AllergyIntolerance:reporter").toLocked();

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
   * Description: <b>food | medication | biologic | environment</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="AllergyIntolerance.category", description="food | medication | biologic | environment", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>food | medication | biologic | environment</b><br>
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

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | active-confirmed | inactive | resolved | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="AllergyIntolerance.status", description="active | active-confirmed | inactive | resolved | refuted | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | active-confirmed | inactive | resolved | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

