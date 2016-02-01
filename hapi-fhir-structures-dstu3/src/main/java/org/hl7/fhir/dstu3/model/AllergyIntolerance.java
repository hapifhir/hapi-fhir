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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * Risk of harmful or undesirable, physiological response which is unique to an individual and associated with exposure to a substance.
 */
@ResourceDef(name="AllergyIntolerance", profile="http://hl7.org/fhir/Profile/AllergyIntolerance")
public class AllergyIntolerance extends DomainResource {

    public enum AllergyIntoleranceStatus {
        /**
         * An active record of a reaction to the identified Substance.
         */
        ACTIVE, 
        /**
         * A low level of certainty about the propensity for a reaction to the identified Substance.
         */
        UNCONFIRMED, 
        /**
         * A high level of certainty about the propensity for a reaction to the identified Substance, which may include clinical evidence by testing or rechallenge.
         */
        CONFIRMED, 
        /**
         * An inactive record of a reaction to the identified Substance.
         */
        INACTIVE, 
        /**
         * A reaction to the identified Substance has been clinically reassessed by testing or rechallenge and considered to be resolved.
         */
        RESOLVED, 
        /**
         * A propensity for a reaction to the identified Substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.
         */
        REFUTED, 
        /**
         * The statement was entered in error and is not valid.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AllergyIntoleranceStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("unconfirmed".equals(codeString))
          return UNCONFIRMED;
        if ("confirmed".equals(codeString))
          return CONFIRMED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("resolved".equals(codeString))
          return RESOLVED;
        if ("refuted".equals(codeString))
          return REFUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new FHIRException("Unknown AllergyIntoleranceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case UNCONFIRMED: return "unconfirmed";
            case CONFIRMED: return "confirmed";
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
            case UNCONFIRMED: return "http://hl7.org/fhir/allergy-intolerance-status";
            case CONFIRMED: return "http://hl7.org/fhir/allergy-intolerance-status";
            case INACTIVE: return "http://hl7.org/fhir/allergy-intolerance-status";
            case RESOLVED: return "http://hl7.org/fhir/allergy-intolerance-status";
            case REFUTED: return "http://hl7.org/fhir/allergy-intolerance-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/allergy-intolerance-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "An active record of a reaction to the identified Substance.";
            case UNCONFIRMED: return "A low level of certainty about the propensity for a reaction to the identified Substance.";
            case CONFIRMED: return "A high level of certainty about the propensity for a reaction to the identified Substance, which may include clinical evidence by testing or rechallenge.";
            case INACTIVE: return "An inactive record of a reaction to the identified Substance.";
            case RESOLVED: return "A reaction to the identified Substance has been clinically reassessed by testing or rechallenge and considered to be resolved.";
            case REFUTED: return "A propensity for a reaction to the identified Substance has been disproven with a high level of clinical certainty, which may include testing or rechallenge, and is refuted.";
            case ENTEREDINERROR: return "The statement was entered in error and is not valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case UNCONFIRMED: return "Unconfirmed";
            case CONFIRMED: return "Confirmed";
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
        if ("unconfirmed".equals(codeString))
          return AllergyIntoleranceStatus.UNCONFIRMED;
        if ("confirmed".equals(codeString))
          return AllergyIntoleranceStatus.CONFIRMED;
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
        if ("unconfirmed".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.UNCONFIRMED);
        if ("confirmed".equals(codeString))
          return new Enumeration<AllergyIntoleranceStatus>(this, AllergyIntoleranceStatus.CONFIRMED);
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
      if (code == AllergyIntoleranceStatus.UNCONFIRMED)
        return "unconfirmed";
      if (code == AllergyIntoleranceStatus.CONFIRMED)
        return "confirmed";
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

    public enum AllergyIntoleranceCriticality {
        /**
         * The potential clinical impact of a future reaction is estimated as low risk: exposure to substance is unlikely to result in a life threatening or organ system threatening outcome. Future exposure to the Substance is considered a relative contra-indication.
         */
        CRITL, 
        /**
         * The potential clinical impact of a future reaction is estimated as high risk: exposure to substance may result in a life threatening or organ system threatening outcome. Future exposure to the Substance may be considered an absolute contra-indication.
         */
        CRITH, 
        /**
         * Unable to assess the potential clinical impact with the information available.
         */
        CRITU, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AllergyIntoleranceCriticality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("CRITL".equals(codeString))
          return CRITL;
        if ("CRITH".equals(codeString))
          return CRITH;
        if ("CRITU".equals(codeString))
          return CRITU;
        throw new FHIRException("Unknown AllergyIntoleranceCriticality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CRITL: return "CRITL";
            case CRITH: return "CRITH";
            case CRITU: return "CRITU";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CRITL: return "http://hl7.org/fhir/allergy-intolerance-criticality";
            case CRITH: return "http://hl7.org/fhir/allergy-intolerance-criticality";
            case CRITU: return "http://hl7.org/fhir/allergy-intolerance-criticality";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CRITL: return "The potential clinical impact of a future reaction is estimated as low risk: exposure to substance is unlikely to result in a life threatening or organ system threatening outcome. Future exposure to the Substance is considered a relative contra-indication.";
            case CRITH: return "The potential clinical impact of a future reaction is estimated as high risk: exposure to substance may result in a life threatening or organ system threatening outcome. Future exposure to the Substance may be considered an absolute contra-indication.";
            case CRITU: return "Unable to assess the potential clinical impact with the information available.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CRITL: return "Low Risk";
            case CRITH: return "High Risk";
            case CRITU: return "Unable to determine";
            default: return "?";
          }
        }
    }

  public static class AllergyIntoleranceCriticalityEnumFactory implements EnumFactory<AllergyIntoleranceCriticality> {
    public AllergyIntoleranceCriticality fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("CRITL".equals(codeString))
          return AllergyIntoleranceCriticality.CRITL;
        if ("CRITH".equals(codeString))
          return AllergyIntoleranceCriticality.CRITH;
        if ("CRITU".equals(codeString))
          return AllergyIntoleranceCriticality.CRITU;
        throw new IllegalArgumentException("Unknown AllergyIntoleranceCriticality code '"+codeString+"'");
        }
        public Enumeration<AllergyIntoleranceCriticality> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("CRITL".equals(codeString))
          return new Enumeration<AllergyIntoleranceCriticality>(this, AllergyIntoleranceCriticality.CRITL);
        if ("CRITH".equals(codeString))
          return new Enumeration<AllergyIntoleranceCriticality>(this, AllergyIntoleranceCriticality.CRITH);
        if ("CRITU".equals(codeString))
          return new Enumeration<AllergyIntoleranceCriticality>(this, AllergyIntoleranceCriticality.CRITU);
        throw new FHIRException("Unknown AllergyIntoleranceCriticality code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceCriticality code) {
      if (code == AllergyIntoleranceCriticality.CRITL)
        return "CRITL";
      if (code == AllergyIntoleranceCriticality.CRITH)
        return "CRITH";
      if (code == AllergyIntoleranceCriticality.CRITU)
        return "CRITU";
      return "?";
      }
    public String toSystem(AllergyIntoleranceCriticality code) {
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
         * added to help the parsers
         */
        NULL;
        public static AllergyIntoleranceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("allergy".equals(codeString))
          return ALLERGY;
        if ("intolerance".equals(codeString))
          return INTOLERANCE;
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
         * Substances that are encountered in the environment.
         */
        ENVIRONMENT, 
        /**
         * Other substances that are not covered by any other category.
         */
        OTHER, 
        /**
         * added to help the parsers
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
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case FOOD: return "food";
            case MEDICATION: return "medication";
            case ENVIRONMENT: return "environment";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case FOOD: return "http://hl7.org/fhir/allergy-intolerance-category";
            case MEDICATION: return "http://hl7.org/fhir/allergy-intolerance-category";
            case ENVIRONMENT: return "http://hl7.org/fhir/allergy-intolerance-category";
            case OTHER: return "http://hl7.org/fhir/allergy-intolerance-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case FOOD: return "Any substance consumed to provide nutritional support for the body.";
            case MEDICATION: return "Substances administered to achieve a physiological effect.";
            case ENVIRONMENT: return "Substances that are encountered in the environment.";
            case OTHER: return "Other substances that are not covered by any other category.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case FOOD: return "Food";
            case MEDICATION: return "Medication";
            case ENVIRONMENT: return "Environment";
            case OTHER: return "Other";
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
        if ("other".equals(codeString))
          return AllergyIntoleranceCategory.OTHER;
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
        if ("environment".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.ENVIRONMENT);
        if ("other".equals(codeString))
          return new Enumeration<AllergyIntoleranceCategory>(this, AllergyIntoleranceCategory.OTHER);
        throw new FHIRException("Unknown AllergyIntoleranceCategory code '"+codeString+"'");
        }
    public String toCode(AllergyIntoleranceCategory code) {
      if (code == AllergyIntoleranceCategory.FOOD)
        return "food";
      if (code == AllergyIntoleranceCategory.MEDICATION)
        return "medication";
      if (code == AllergyIntoleranceCategory.ENVIRONMENT)
        return "environment";
      if (code == AllergyIntoleranceCategory.OTHER)
        return "other";
      return "?";
      }
    public String toSystem(AllergyIntoleranceCategory code) {
      return code.getSystem();
      }
    }

    public enum AllergyIntoleranceCertainty {
        /**
         * There is a low level of clinical certainty that the reaction was caused by the identified Substance.
         */
        UNLIKELY, 
        /**
         * There is a high level of clinical certainty that the reaction was caused by the identified Substance.
         */
        LIKELY, 
        /**
         * There is a very high level of clinical certainty that the reaction was due to the identified Substance, which may include clinical evidence by testing or rechallenge.
         */
        CONFIRMED, 
        /**
         * added to help the parsers
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
            case UNLIKELY: return "There is a low level of clinical certainty that the reaction was caused by the identified Substance.";
            case LIKELY: return "There is a high level of clinical certainty that the reaction was caused by the identified Substance.";
            case CONFIRMED: return "There is a very high level of clinical certainty that the reaction was due to the identified Substance, which may include clinical evidence by testing or rechallenge.";
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
         * added to help the parsers
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
         * Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AllergyIntolerance.substance and ignore the AllergyIntolerance.event.substance.
         */
        @Child(name = "substance", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific substance considered to be responsible for event", formalDefinition="Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AllergyIntolerance.substance and ignore the AllergyIntolerance.event.substance." )
        protected CodeableConcept substance;

        /**
         * Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.
         */
        @Child(name = "certainty", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="unlikely | likely | confirmed - clinical certainty about the specific substance", formalDefinition="Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event." )
        protected Enumeration<AllergyIntoleranceCertainty> certainty;

        /**
         * Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.
         */
        @Child(name = "manifestation", type = {CodeableConcept.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Clinical symptoms/signs associated with the Event", formalDefinition="Clinical symptoms and/or signs that are observed or associated with the adverse reaction event." )
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
        protected Enumeration<AllergyIntoleranceSeverity> severity;

        /**
         * Identification of the route by which the subject was exposed to the substance.
         */
        @Child(name = "exposureRoute", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How the subject was exposed to the substance", formalDefinition="Identification of the route by which the subject was exposed to the substance." )
        protected CodeableConcept exposureRoute;

        /**
         * Additional text about the adverse reaction event not captured in other fields.
         */
        @Child(name = "note", type = {Annotation.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Text about event not captured in other fields", formalDefinition="Additional text about the adverse reaction event not captured in other fields." )
        protected Annotation note;

        private static final long serialVersionUID = -765664367L;

    /**
     * Constructor
     */
      public AllergyIntoleranceReactionComponent() {
        super();
      }

        /**
         * @return {@link #substance} (Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AllergyIntolerance.substance and ignore the AllergyIntolerance.event.substance.)
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
         * @param value {@link #substance} (Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AllergyIntolerance.substance and ignore the AllergyIntolerance.event.substance.)
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

        public boolean hasManifestation() { 
          if (this.manifestation == null)
            return false;
          for (CodeableConcept item : this.manifestation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #manifestation} (Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.)
         */
    // syntactic sugar
        public CodeableConcept addManifestation() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.manifestation == null)
            this.manifestation = new ArrayList<CodeableConcept>();
          this.manifestation.add(t);
          return t;
        }

    // syntactic sugar
        public AllergyIntoleranceReactionComponent addManifestation(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.manifestation == null)
            this.manifestation = new ArrayList<CodeableConcept>();
          this.manifestation.add(t);
          return this;
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
        public Annotation getNote() { 
          if (this.note == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AllergyIntoleranceReactionComponent.note");
            else if (Configuration.doAutoCreate())
              this.note = new Annotation(); // cc
          return this.note;
        }

        public boolean hasNote() { 
          return this.note != null && !this.note.isEmpty();
        }

        /**
         * @param value {@link #note} (Additional text about the adverse reaction event not captured in other fields.)
         */
        public AllergyIntoleranceReactionComponent setNote(Annotation value) { 
          this.note = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("substance", "CodeableConcept", "Identification of the specific substance considered to be responsible for the Adverse Reaction event. Note: the substance for a specific reaction may be different to the substance identified as the cause of the risk, but must be consistent with it. For instance, it may be a more specific substance (e.g. a brand medication) or a composite substance that includes the identified substance. It must be clinically safe to only process the AllergyIntolerance.substance and ignore the AllergyIntolerance.event.substance.", 0, java.lang.Integer.MAX_VALUE, substance));
          childrenList.add(new Property("certainty", "code", "Statement about the degree of clinical certainty that the specific substance was the cause of the manifestation in this reaction event.", 0, java.lang.Integer.MAX_VALUE, certainty));
          childrenList.add(new Property("manifestation", "CodeableConcept", "Clinical symptoms and/or signs that are observed or associated with the adverse reaction event.", 0, java.lang.Integer.MAX_VALUE, manifestation));
          childrenList.add(new Property("description", "string", "Text description about the reaction as a whole, including details of the manifestation if required.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("onset", "dateTime", "Record of the date and/or time of the onset of the Reaction.", 0, java.lang.Integer.MAX_VALUE, onset));
          childrenList.add(new Property("severity", "code", "Clinical assessment of the severity of the reaction event as a whole, potentially considering multiple different manifestations.", 0, java.lang.Integer.MAX_VALUE, severity));
          childrenList.add(new Property("exposureRoute", "CodeableConcept", "Identification of the route by which the subject was exposed to the substance.", 0, java.lang.Integer.MAX_VALUE, exposureRoute));
          childrenList.add(new Property("note", "Annotation", "Additional text about the adverse reaction event not captured in other fields.", 0, java.lang.Integer.MAX_VALUE, note));
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
          this.note = castToAnnotation(value); // Annotation
        else
          super.setProperty(name, value);
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
          this.note = new Annotation();
          return this.note;
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
        dst.note = note == null ? null : note.copy();
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
        return super.isEmpty() && (substance == null || substance.isEmpty()) && (certainty == null || certainty.isEmpty())
           && (manifestation == null || manifestation.isEmpty()) && (description == null || description.isEmpty())
           && (onset == null || onset.isEmpty()) && (severity == null || severity.isEmpty()) && (exposureRoute == null || exposureRoute.isEmpty())
           && (note == null || note.isEmpty());
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
     * Record of the date and/or time of the onset of the Allergy or Intolerance.
     */
    @Child(name = "onset", type = {DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date(/time) when manifestations showed", formalDefinition="Record of the date and/or time of the onset of the Allergy or Intolerance." )
    protected DateTimeType onset;

    /**
     * Date when the sensitivity was recorded.
     */
    @Child(name = "recordedDate", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When recorded", formalDefinition="Date when the sensitivity was recorded." )
    protected DateTimeType recordedDate;

    /**
     * Individual who recorded the record and takes responsibility for its conten.
     */
    @Child(name = "recorder", type = {Practitioner.class, Patient.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who recorded the sensitivity", formalDefinition="Individual who recorded the record and takes responsibility for its conten." )
    protected Reference recorder;

    /**
     * The actual object that is the target of the reference (Individual who recorded the record and takes responsibility for its conten.)
     */
    protected Resource recorderTarget;

    /**
     * The patient who has the allergy or intolerance.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the sensitivity is for", formalDefinition="The patient who has the allergy or intolerance." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient who has the allergy or intolerance.)
     */
    protected Patient patientTarget;

    /**
     * The source of the information about the allergy that is recorded.
     */
    @Child(name = "reporter", type = {Patient.class, RelatedPerson.class, Practitioner.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Source of the information about the allergy", formalDefinition="The source of the information about the allergy that is recorded." )
    protected Reference reporter;

    /**
     * The actual object that is the target of the reference (The source of the information about the allergy that is recorded.)
     */
    protected Resource reporterTarget;

    /**
     * Identification of a substance, or a class of substances, that is considered to be responsible for the adverse reaction risk.
     */
    @Child(name = "substance", type = {CodeableConcept.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Substance, (or class) considered to be responsible for risk", formalDefinition="Identification of a substance, or a class of substances, that is considered to be responsible for the adverse reaction risk." )
    protected CodeableConcept substance;

    /**
     * Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance.
     */
    @Child(name = "status", type = {CodeType.class}, order=7, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | unconfirmed | confirmed | inactive | resolved | refuted | entered-in-error", formalDefinition="Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance." )
    protected Enumeration<AllergyIntoleranceStatus> status;

    /**
     * Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance.
     */
    @Child(name = "criticality", type = {CodeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="CRITL | CRITH | CRITU", formalDefinition="Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance." )
    protected Enumeration<AllergyIntoleranceCriticality> criticality;

    /**
     * Identification of the underlying physiological mechanism for the reaction risk.
     */
    @Child(name = "type", type = {CodeType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="allergy | intolerance - Underlying mechanism (if known)", formalDefinition="Identification of the underlying physiological mechanism for the reaction risk." )
    protected Enumeration<AllergyIntoleranceType> type;

    /**
     * Category of the identified Substance.
     */
    @Child(name = "category", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="food | medication | environment | other - Category of Substance", formalDefinition="Category of the identified Substance." )
    protected Enumeration<AllergyIntoleranceCategory> category;

    /**
     * Represents the date and/or time of the last known occurrence of a reaction event.
     */
    @Child(name = "lastOccurence", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date(/time) of last known occurrence of a reaction", formalDefinition="Represents the date and/or time of the last known occurrence of a reaction event." )
    protected DateTimeType lastOccurence;

    /**
     * Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.
     */
    @Child(name = "note", type = {Annotation.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Additional text not captured in other fields", formalDefinition="Additional narrative about the propensity for the Adverse Reaction, not captured in other fields." )
    protected Annotation note;

    /**
     * Details about each adverse reaction event linked to exposure to the identified Substance.
     */
    @Child(name = "reaction", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Adverse Reaction Events linked to exposure to substance", formalDefinition="Details about each adverse reaction event linked to exposure to the identified Substance." )
    protected List<AllergyIntoleranceReactionComponent> reaction;

    private static final long serialVersionUID = -1657522921L;

  /**
   * Constructor
   */
    public AllergyIntolerance() {
      super();
    }

  /**
   * Constructor
   */
    public AllergyIntolerance(Reference patient, CodeableConcept substance) {
      super();
      this.patient = patient;
      this.substance = substance;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this allergy/intolerance concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this allergy/intolerance concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
    public AllergyIntolerance addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
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
     * @return {@link #recordedDate} (Date when the sensitivity was recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedDate" gives direct access to the value
     */
    public DateTimeType getRecordedDateElement() { 
      if (this.recordedDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.recordedDate");
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
     * @param value {@link #recordedDate} (Date when the sensitivity was recorded.). This is the underlying object with id, value and extensions. The accessor "getRecordedDate" gives direct access to the value
     */
    public AllergyIntolerance setRecordedDateElement(DateTimeType value) { 
      this.recordedDate = value;
      return this;
    }

    /**
     * @return Date when the sensitivity was recorded.
     */
    public Date getRecordedDate() { 
      return this.recordedDate == null ? null : this.recordedDate.getValue();
    }

    /**
     * @param value Date when the sensitivity was recorded.
     */
    public AllergyIntolerance setRecordedDate(Date value) { 
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
     * @return {@link #recorder} (Individual who recorded the record and takes responsibility for its conten.)
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
     * @param value {@link #recorder} (Individual who recorded the record and takes responsibility for its conten.)
     */
    public AllergyIntolerance setRecorder(Reference value) { 
      this.recorder = value;
      return this;
    }

    /**
     * @return {@link #recorder} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its conten.)
     */
    public Resource getRecorderTarget() { 
      return this.recorderTarget;
    }

    /**
     * @param value {@link #recorder} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its conten.)
     */
    public AllergyIntolerance setRecorderTarget(Resource value) { 
      this.recorderTarget = value;
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
     * @return {@link #substance} (Identification of a substance, or a class of substances, that is considered to be responsible for the adverse reaction risk.)
     */
    public CodeableConcept getSubstance() { 
      if (this.substance == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.substance");
        else if (Configuration.doAutoCreate())
          this.substance = new CodeableConcept(); // cc
      return this.substance;
    }

    public boolean hasSubstance() { 
      return this.substance != null && !this.substance.isEmpty();
    }

    /**
     * @param value {@link #substance} (Identification of a substance, or a class of substances, that is considered to be responsible for the adverse reaction risk.)
     */
    public AllergyIntolerance setSubstance(CodeableConcept value) { 
      this.substance = value;
      return this;
    }

    /**
     * @return {@link #status} (Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public AllergyIntolerance setStatusElement(Enumeration<AllergyIntoleranceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance.
     */
    public AllergyIntoleranceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance.
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
     * @return {@link #criticality} (Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance.). This is the underlying object with id, value and extensions. The accessor "getCriticality" gives direct access to the value
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
     * @param value {@link #criticality} (Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance.). This is the underlying object with id, value and extensions. The accessor "getCriticality" gives direct access to the value
     */
    public AllergyIntolerance setCriticalityElement(Enumeration<AllergyIntoleranceCriticality> value) { 
      this.criticality = value;
      return this;
    }

    /**
     * @return Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance.
     */
    public AllergyIntoleranceCriticality getCriticality() { 
      return this.criticality == null ? null : this.criticality.getValue();
    }

    /**
     * @param value Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance.
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
     * @return {@link #category} (Category of the identified Substance.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
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
     * @param value {@link #category} (Category of the identified Substance.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public AllergyIntolerance setCategoryElement(Enumeration<AllergyIntoleranceCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return Category of the identified Substance.
     */
    public AllergyIntoleranceCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value Category of the identified Substance.
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
     * @return {@link #lastOccurence} (Represents the date and/or time of the last known occurrence of a reaction event.). This is the underlying object with id, value and extensions. The accessor "getLastOccurence" gives direct access to the value
     */
    public DateTimeType getLastOccurenceElement() { 
      if (this.lastOccurence == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.lastOccurence");
        else if (Configuration.doAutoCreate())
          this.lastOccurence = new DateTimeType(); // bb
      return this.lastOccurence;
    }

    public boolean hasLastOccurenceElement() { 
      return this.lastOccurence != null && !this.lastOccurence.isEmpty();
    }

    public boolean hasLastOccurence() { 
      return this.lastOccurence != null && !this.lastOccurence.isEmpty();
    }

    /**
     * @param value {@link #lastOccurence} (Represents the date and/or time of the last known occurrence of a reaction event.). This is the underlying object with id, value and extensions. The accessor "getLastOccurence" gives direct access to the value
     */
    public AllergyIntolerance setLastOccurenceElement(DateTimeType value) { 
      this.lastOccurence = value;
      return this;
    }

    /**
     * @return Represents the date and/or time of the last known occurrence of a reaction event.
     */
    public Date getLastOccurence() { 
      return this.lastOccurence == null ? null : this.lastOccurence.getValue();
    }

    /**
     * @param value Represents the date and/or time of the last known occurrence of a reaction event.
     */
    public AllergyIntolerance setLastOccurence(Date value) { 
      if (value == null)
        this.lastOccurence = null;
      else {
        if (this.lastOccurence == null)
          this.lastOccurence = new DateTimeType();
        this.lastOccurence.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #note} (Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.)
     */
    public Annotation getNote() { 
      if (this.note == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AllergyIntolerance.note");
        else if (Configuration.doAutoCreate())
          this.note = new Annotation(); // cc
      return this.note;
    }

    public boolean hasNote() { 
      return this.note != null && !this.note.isEmpty();
    }

    /**
     * @param value {@link #note} (Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.)
     */
    public AllergyIntolerance setNote(Annotation value) { 
      this.note = value;
      return this;
    }

    /**
     * @return {@link #reaction} (Details about each adverse reaction event linked to exposure to the identified Substance.)
     */
    public List<AllergyIntoleranceReactionComponent> getReaction() { 
      if (this.reaction == null)
        this.reaction = new ArrayList<AllergyIntoleranceReactionComponent>();
      return this.reaction;
    }

    public boolean hasReaction() { 
      if (this.reaction == null)
        return false;
      for (AllergyIntoleranceReactionComponent item : this.reaction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reaction} (Details about each adverse reaction event linked to exposure to the identified Substance.)
     */
    // syntactic sugar
    public AllergyIntoleranceReactionComponent addReaction() { //3
      AllergyIntoleranceReactionComponent t = new AllergyIntoleranceReactionComponent();
      if (this.reaction == null)
        this.reaction = new ArrayList<AllergyIntoleranceReactionComponent>();
      this.reaction.add(t);
      return t;
    }

    // syntactic sugar
    public AllergyIntolerance addReaction(AllergyIntoleranceReactionComponent t) { //3
      if (t == null)
        return this;
      if (this.reaction == null)
        this.reaction = new ArrayList<AllergyIntoleranceReactionComponent>();
      this.reaction.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this allergy/intolerance concern that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("onset", "dateTime", "Record of the date and/or time of the onset of the Allergy or Intolerance.", 0, java.lang.Integer.MAX_VALUE, onset));
        childrenList.add(new Property("recordedDate", "dateTime", "Date when the sensitivity was recorded.", 0, java.lang.Integer.MAX_VALUE, recordedDate));
        childrenList.add(new Property("recorder", "Reference(Practitioner|Patient)", "Individual who recorded the record and takes responsibility for its conten.", 0, java.lang.Integer.MAX_VALUE, recorder));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient who has the allergy or intolerance.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("reporter", "Reference(Patient|RelatedPerson|Practitioner)", "The source of the information about the allergy that is recorded.", 0, java.lang.Integer.MAX_VALUE, reporter));
        childrenList.add(new Property("substance", "CodeableConcept", "Identification of a substance, or a class of substances, that is considered to be responsible for the adverse reaction risk.", 0, java.lang.Integer.MAX_VALUE, substance));
        childrenList.add(new Property("status", "code", "Assertion about certainty associated with the propensity, or potential risk, of a reaction to the identified Substance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("criticality", "code", "Estimate of the potential clinical harm, or seriousness, of the reaction to the identified Substance.", 0, java.lang.Integer.MAX_VALUE, criticality));
        childrenList.add(new Property("type", "code", "Identification of the underlying physiological mechanism for the reaction risk.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("category", "code", "Category of the identified Substance.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("lastOccurence", "dateTime", "Represents the date and/or time of the last known occurrence of a reaction event.", 0, java.lang.Integer.MAX_VALUE, lastOccurence));
        childrenList.add(new Property("note", "Annotation", "Additional narrative about the propensity for the Adverse Reaction, not captured in other fields.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("reaction", "", "Details about each adverse reaction event linked to exposure to the identified Substance.", 0, java.lang.Integer.MAX_VALUE, reaction));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("onset"))
          this.onset = castToDateTime(value); // DateTimeType
        else if (name.equals("recordedDate"))
          this.recordedDate = castToDateTime(value); // DateTimeType
        else if (name.equals("recorder"))
          this.recorder = castToReference(value); // Reference
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("reporter"))
          this.reporter = castToReference(value); // Reference
        else if (name.equals("substance"))
          this.substance = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("status"))
          this.status = new AllergyIntoleranceStatusEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceStatus>
        else if (name.equals("criticality"))
          this.criticality = new AllergyIntoleranceCriticalityEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCriticality>
        else if (name.equals("type"))
          this.type = new AllergyIntoleranceTypeEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceType>
        else if (name.equals("category"))
          this.category = new AllergyIntoleranceCategoryEnumFactory().fromType(value); // Enumeration<AllergyIntoleranceCategory>
        else if (name.equals("lastOccurence"))
          this.lastOccurence = castToDateTime(value); // DateTimeType
        else if (name.equals("note"))
          this.note = castToAnnotation(value); // Annotation
        else if (name.equals("reaction"))
          this.getReaction().add((AllergyIntoleranceReactionComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("onset")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.onset");
        }
        else if (name.equals("recordedDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.recordedDate");
        }
        else if (name.equals("recorder")) {
          this.recorder = new Reference();
          return this.recorder;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("reporter")) {
          this.reporter = new Reference();
          return this.reporter;
        }
        else if (name.equals("substance")) {
          this.substance = new CodeableConcept();
          return this.substance;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.status");
        }
        else if (name.equals("criticality")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.criticality");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.type");
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.category");
        }
        else if (name.equals("lastOccurence")) {
          throw new FHIRException("Cannot call addChild on a primitive type AllergyIntolerance.lastOccurence");
        }
        else if (name.equals("note")) {
          this.note = new Annotation();
          return this.note;
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
        dst.onset = onset == null ? null : onset.copy();
        dst.recordedDate = recordedDate == null ? null : recordedDate.copy();
        dst.recorder = recorder == null ? null : recorder.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.reporter = reporter == null ? null : reporter.copy();
        dst.substance = substance == null ? null : substance.copy();
        dst.status = status == null ? null : status.copy();
        dst.criticality = criticality == null ? null : criticality.copy();
        dst.type = type == null ? null : type.copy();
        dst.category = category == null ? null : category.copy();
        dst.lastOccurence = lastOccurence == null ? null : lastOccurence.copy();
        dst.note = note == null ? null : note.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(onset, o.onset, true) && compareDeep(recordedDate, o.recordedDate, true)
           && compareDeep(recorder, o.recorder, true) && compareDeep(patient, o.patient, true) && compareDeep(reporter, o.reporter, true)
           && compareDeep(substance, o.substance, true) && compareDeep(status, o.status, true) && compareDeep(criticality, o.criticality, true)
           && compareDeep(type, o.type, true) && compareDeep(category, o.category, true) && compareDeep(lastOccurence, o.lastOccurence, true)
           && compareDeep(note, o.note, true) && compareDeep(reaction, o.reaction, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AllergyIntolerance))
          return false;
        AllergyIntolerance o = (AllergyIntolerance) other;
        return compareValues(onset, o.onset, true) && compareValues(recordedDate, o.recordedDate, true) && compareValues(status, o.status, true)
           && compareValues(criticality, o.criticality, true) && compareValues(type, o.type, true) && compareValues(category, o.category, true)
           && compareValues(lastOccurence, o.lastOccurence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (onset == null || onset.isEmpty())
           && (recordedDate == null || recordedDate.isEmpty()) && (recorder == null || recorder.isEmpty())
           && (patient == null || patient.isEmpty()) && (reporter == null || reporter.isEmpty()) && (substance == null || substance.isEmpty())
           && (status == null || status.isEmpty()) && (criticality == null || criticality.isEmpty())
           && (type == null || type.isEmpty()) && (category == null || category.isEmpty()) && (lastOccurence == null || lastOccurence.isEmpty())
           && (note == null || note.isEmpty()) && (reaction == null || reaction.isEmpty());
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
   * Description: <b>When recorded</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.recordedDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="AllergyIntolerance.recordedDate", description="When recorded", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When recorded</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.recordedDate</b><br>
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
  @SearchParamDefinition(name="recorder", path="AllergyIntolerance.recorder", description="Who recorded the sensitivity", type="reference" )
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
   * Search parameter: <b>substance</b>
   * <p>
   * Description: <b>Substance, (or class) considered to be responsible for risk</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.substance, AllergyIntolerance.reaction.substance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="substance", path="AllergyIntolerance.substance | AllergyIntolerance.reaction.substance", description="Substance, (or class) considered to be responsible for risk", type="token" )
  public static final String SP_SUBSTANCE = "substance";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>substance</b>
   * <p>
   * Description: <b>Substance, (or class) considered to be responsible for risk</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.substance, AllergyIntolerance.reaction.substance</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SUBSTANCE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SUBSTANCE);

 /**
   * Search parameter: <b>criticality</b>
   * <p>
   * Description: <b>CRITL | CRITH | CRITU</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.criticality</b><br>
   * </p>
   */
  @SearchParamDefinition(name="criticality", path="AllergyIntolerance.criticality", description="CRITL | CRITH | CRITU", type="token" )
  public static final String SP_CRITICALITY = "criticality";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>criticality</b>
   * <p>
   * Description: <b>CRITL | CRITH | CRITU</b><br>
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
  @SearchParamDefinition(name="reporter", path="AllergyIntolerance.reporter", description="Source of the information about the allergy", type="reference" )
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
  @SearchParamDefinition(name="patient", path="AllergyIntolerance.patient", description="Who the sensitivity is for", type="reference" )
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
   * Description: <b>food | medication | environment | other - Category of Substance</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="AllergyIntolerance.category", description="food | medication | environment | other - Category of Substance", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>food | medication | environment | other - Category of Substance</b><br>
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
   * Path: <b>AllergyIntolerance.lastOccurence</b><br>
   * </p>
   */
  @SearchParamDefinition(name="last-date", path="AllergyIntolerance.lastOccurence", description="Date(/time) of last known occurrence of a reaction", type="date" )
  public static final String SP_LAST_DATE = "last-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>last-date</b>
   * <p>
   * Description: <b>Date(/time) of last known occurrence of a reaction</b><br>
   * Type: <b>date</b><br>
   * Path: <b>AllergyIntolerance.lastOccurence</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam LAST_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_LAST_DATE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | unconfirmed | confirmed | inactive | resolved | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="AllergyIntolerance.status", description="active | unconfirmed | confirmed | inactive | resolved | refuted | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | unconfirmed | confirmed | inactive | resolved | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>AllergyIntolerance.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

