package org.hl7.fhir.instance.model;

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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Measurements and simple assertions made about a patient, device or other subject.
 */
@ResourceDef(name="Observation", profile="http://hl7.org/fhir/Profile/Observation")
public class Observation extends DomainResource {

    public enum DataAbsentReason {
        /**
         * The value is not known
         */
        UNKNOWN, 
        /**
         * The source human does not know the value
         */
        ASKED, 
        /**
         * There is reason to expect (from the workflow) that the value may become known
         */
        TEMP, 
        /**
         * The workflow didn't lead to this value being known
         */
        NOTASKED, 
        /**
         * The information is not available due to security, privacy or related reasons
         */
        MASKED, 
        /**
         * The source system wasn't capable of supporting this element
         */
        UNSUPPORTED, 
        /**
         * The content of the data is represented in the resource narrative
         */
        ASTEXT, 
        /**
         * Some system or workflow process error means that the information is not available
         */
        ERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataAbsentReason fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if ("asked".equals(codeString))
          return ASKED;
        if ("temp".equals(codeString))
          return TEMP;
        if ("notasked".equals(codeString))
          return NOTASKED;
        if ("masked".equals(codeString))
          return MASKED;
        if ("unsupported".equals(codeString))
          return UNSUPPORTED;
        if ("astext".equals(codeString))
          return ASTEXT;
        if ("error".equals(codeString))
          return ERROR;
        throw new Exception("Unknown DataAbsentReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case ASKED: return "asked";
            case TEMP: return "temp";
            case NOTASKED: return "notasked";
            case MASKED: return "masked";
            case UNSUPPORTED: return "unsupported";
            case ASTEXT: return "astext";
            case ERROR: return "error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case UNKNOWN: return "";
            case ASKED: return "";
            case TEMP: return "";
            case NOTASKED: return "";
            case MASKED: return "";
            case UNSUPPORTED: return "";
            case ASTEXT: return "";
            case ERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case UNKNOWN: return "The value is not known";
            case ASKED: return "The source human does not know the value";
            case TEMP: return "There is reason to expect (from the workflow) that the value may become known";
            case NOTASKED: return "The workflow didn't lead to this value being known";
            case MASKED: return "The information is not available due to security, privacy or related reasons";
            case UNSUPPORTED: return "The source system wasn't capable of supporting this element";
            case ASTEXT: return "The content of the data is represented in the resource narrative";
            case ERROR: return "Some system or workflow process error means that the information is not available";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case UNKNOWN: return "unknown";
            case ASKED: return "asked";
            case TEMP: return "temp";
            case NOTASKED: return "notasked";
            case MASKED: return "masked";
            case UNSUPPORTED: return "unsupported";
            case ASTEXT: return "astext";
            case ERROR: return "error";
            default: return "?";
          }
        }
    }

  public static class DataAbsentReasonEnumFactory implements EnumFactory<DataAbsentReason> {
    public DataAbsentReason fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("unknown".equals(codeString))
          return DataAbsentReason.UNKNOWN;
        if ("asked".equals(codeString))
          return DataAbsentReason.ASKED;
        if ("temp".equals(codeString))
          return DataAbsentReason.TEMP;
        if ("notasked".equals(codeString))
          return DataAbsentReason.NOTASKED;
        if ("masked".equals(codeString))
          return DataAbsentReason.MASKED;
        if ("unsupported".equals(codeString))
          return DataAbsentReason.UNSUPPORTED;
        if ("astext".equals(codeString))
          return DataAbsentReason.ASTEXT;
        if ("error".equals(codeString))
          return DataAbsentReason.ERROR;
        throw new IllegalArgumentException("Unknown DataAbsentReason code '"+codeString+"'");
        }
    public String toCode(DataAbsentReason code) {
      if (code == DataAbsentReason.UNKNOWN)
        return "unknown";
      if (code == DataAbsentReason.ASKED)
        return "asked";
      if (code == DataAbsentReason.TEMP)
        return "temp";
      if (code == DataAbsentReason.NOTASKED)
        return "notasked";
      if (code == DataAbsentReason.MASKED)
        return "masked";
      if (code == DataAbsentReason.UNSUPPORTED)
        return "unsupported";
      if (code == DataAbsentReason.ASTEXT)
        return "astext";
      if (code == DataAbsentReason.ERROR)
        return "error";
      return "?";
      }
    }

    public enum ObservationStatus {
        /**
         * The existence of the observation is registered, but there is no result yet available.
         */
        REGISTERED, 
        /**
         * This is an initial or interim observation: data may be incomplete or unverified.
         */
        PRELIMINARY, 
        /**
         * The observation is complete and verified by an authorized person.
         */
        FINAL, 
        /**
         * The observation has been modified subsequent to being Final, and is complete and verified by an authorized person.
         */
        AMENDED, 
        /**
         * The observation is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
         */
        CANCELLED, 
        /**
         * The observation has been withdrawn following previous Final release.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObservationStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return REGISTERED;
        if ("preliminary".equals(codeString))
          return PRELIMINARY;
        if ("final".equals(codeString))
          return FINAL;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown ObservationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTERED: return "registered";
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case AMENDED: return "amended";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REGISTERED: return "";
            case PRELIMINARY: return "";
            case FINAL: return "";
            case AMENDED: return "";
            case CANCELLED: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REGISTERED: return "The existence of the observation is registered, but there is no result yet available.";
            case PRELIMINARY: return "This is an initial or interim observation: data may be incomplete or unverified.";
            case FINAL: return "The observation is complete and verified by an authorized person.";
            case AMENDED: return "The observation has been modified subsequent to being Final, and is complete and verified by an authorized person.";
            case CANCELLED: return "The observation is unavailable because the measurement was not started or not completed (also sometimes called 'aborted').";
            case ENTEREDINERROR: return "The observation has been withdrawn following previous Final release.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTERED: return "Registered";
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case AMENDED: return "amended";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
    }

  public static class ObservationStatusEnumFactory implements EnumFactory<ObservationStatus> {
    public ObservationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return ObservationStatus.REGISTERED;
        if ("preliminary".equals(codeString))
          return ObservationStatus.PRELIMINARY;
        if ("final".equals(codeString))
          return ObservationStatus.FINAL;
        if ("amended".equals(codeString))
          return ObservationStatus.AMENDED;
        if ("cancelled".equals(codeString))
          return ObservationStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ObservationStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ObservationStatus code '"+codeString+"'");
        }
    public String toCode(ObservationStatus code) {
      if (code == ObservationStatus.REGISTERED)
        return "registered";
      if (code == ObservationStatus.PRELIMINARY)
        return "preliminary";
      if (code == ObservationStatus.FINAL)
        return "final";
      if (code == ObservationStatus.AMENDED)
        return "amended";
      if (code == ObservationStatus.CANCELLED)
        return "cancelled";
      if (code == ObservationStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    public enum ObservationReliability {
        /**
         * The result has no reliability concerns.
         */
        OK, 
        /**
         * An early estimate of value; measurement is still occurring.
         */
        ONGOING, 
        /**
         * An early estimate of value; processing is still occurring.
         */
        EARLY, 
        /**
         * The observation value should be treated with care.
         */
        QUESTIONABLE, 
        /**
         * The result has been generated while calibration is occurring.
         */
        CALIBRATING, 
        /**
         * The observation could not be completed because of an error.
         */
        ERROR, 
        /**
         * No observation  reliability value was available.
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObservationReliability fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ok".equals(codeString))
          return OK;
        if ("ongoing".equals(codeString))
          return ONGOING;
        if ("early".equals(codeString))
          return EARLY;
        if ("questionable".equals(codeString))
          return QUESTIONABLE;
        if ("calibrating".equals(codeString))
          return CALIBRATING;
        if ("error".equals(codeString))
          return ERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new Exception("Unknown ObservationReliability code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OK: return "ok";
            case ONGOING: return "ongoing";
            case EARLY: return "early";
            case QUESTIONABLE: return "questionable";
            case CALIBRATING: return "calibrating";
            case ERROR: return "error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case OK: return "";
            case ONGOING: return "";
            case EARLY: return "";
            case QUESTIONABLE: return "";
            case CALIBRATING: return "";
            case ERROR: return "";
            case UNKNOWN: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case OK: return "The result has no reliability concerns.";
            case ONGOING: return "An early estimate of value; measurement is still occurring.";
            case EARLY: return "An early estimate of value; processing is still occurring.";
            case QUESTIONABLE: return "The observation value should be treated with care.";
            case CALIBRATING: return "The result has been generated while calibration is occurring.";
            case ERROR: return "The observation could not be completed because of an error.";
            case UNKNOWN: return "No observation  reliability value was available.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OK: return "ok";
            case ONGOING: return "ongoing";
            case EARLY: return "early";
            case QUESTIONABLE: return "questionable";
            case CALIBRATING: return "calibrating";
            case ERROR: return "error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
    }

  public static class ObservationReliabilityEnumFactory implements EnumFactory<ObservationReliability> {
    public ObservationReliability fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ok".equals(codeString))
          return ObservationReliability.OK;
        if ("ongoing".equals(codeString))
          return ObservationReliability.ONGOING;
        if ("early".equals(codeString))
          return ObservationReliability.EARLY;
        if ("questionable".equals(codeString))
          return ObservationReliability.QUESTIONABLE;
        if ("calibrating".equals(codeString))
          return ObservationReliability.CALIBRATING;
        if ("error".equals(codeString))
          return ObservationReliability.ERROR;
        if ("unknown".equals(codeString))
          return ObservationReliability.UNKNOWN;
        throw new IllegalArgumentException("Unknown ObservationReliability code '"+codeString+"'");
        }
    public String toCode(ObservationReliability code) {
      if (code == ObservationReliability.OK)
        return "ok";
      if (code == ObservationReliability.ONGOING)
        return "ongoing";
      if (code == ObservationReliability.EARLY)
        return "early";
      if (code == ObservationReliability.QUESTIONABLE)
        return "questionable";
      if (code == ObservationReliability.CALIBRATING)
        return "calibrating";
      if (code == ObservationReliability.ERROR)
        return "error";
      if (code == ObservationReliability.UNKNOWN)
        return "unknown";
      return "?";
      }
    }

    public enum ObservationRelationshiptypes {
        /**
         * The target observation is a component of this observation (e.g. Systolic and Diastolic Blood Pressure).
         */
        HASCOMPONENT, 
        /**
         * This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.
         */
        HASMEMBER, 
        /**
         * The target observation is part of the information from which this observation value is derived (e.g. calculated anion gap, Apgar score).
         */
        DERIVEDFROM, 
        /**
         * This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).
         */
        SEQUELTO, 
        /**
         * This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.
         */
        REPLACES, 
        /**
         * The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipaemia measure target from a plasma measure).
         */
        QUALIFIEDBY, 
        /**
         * The value of the target observation interferes (degardes quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure which has no value).
         */
        INTERFEREDBY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ObservationRelationshiptypes fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("has-component".equals(codeString))
          return HASCOMPONENT;
        if ("has-member".equals(codeString))
          return HASMEMBER;
        if ("derived-from".equals(codeString))
          return DERIVEDFROM;
        if ("sequel-to".equals(codeString))
          return SEQUELTO;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("qualified-by".equals(codeString))
          return QUALIFIEDBY;
        if ("interfered-by".equals(codeString))
          return INTERFEREDBY;
        throw new Exception("Unknown ObservationRelationshiptypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HASCOMPONENT: return "has-component";
            case HASMEMBER: return "has-member";
            case DERIVEDFROM: return "derived-from";
            case SEQUELTO: return "sequel-to";
            case REPLACES: return "replaces";
            case QUALIFIEDBY: return "qualified-by";
            case INTERFEREDBY: return "interfered-by";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HASCOMPONENT: return "";
            case HASMEMBER: return "";
            case DERIVEDFROM: return "";
            case SEQUELTO: return "";
            case REPLACES: return "";
            case QUALIFIEDBY: return "";
            case INTERFEREDBY: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HASCOMPONENT: return "The target observation is a component of this observation (e.g. Systolic and Diastolic Blood Pressure).";
            case HASMEMBER: return "This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.";
            case DERIVEDFROM: return "The target observation is part of the information from which this observation value is derived (e.g. calculated anion gap, Apgar score).";
            case SEQUELTO: return "This observation follows the target observation (e.g. timed tests such as Glucose Tolerance Test).";
            case REPLACES: return "This observation replaces a previous observation (i.e. a revised value). The target observation is now obsolete.";
            case QUALIFIEDBY: return "The value of the target observation qualifies (refines) the semantics of the source observation (e.g. a lipaemia measure target from a plasma measure).";
            case INTERFEREDBY: return "The value of the target observation interferes (degardes quality, or prevents valid observation) with the semantics of the source observation (e.g. a hemolysis measure target from a plasma potassium measure which has no value).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HASCOMPONENT: return "has-component";
            case HASMEMBER: return "has-member";
            case DERIVEDFROM: return "derived-from";
            case SEQUELTO: return "sequel-to";
            case REPLACES: return "replaces";
            case QUALIFIEDBY: return "qualified-by";
            case INTERFEREDBY: return "interfered-by";
            default: return "?";
          }
        }
    }

  public static class ObservationRelationshiptypesEnumFactory implements EnumFactory<ObservationRelationshiptypes> {
    public ObservationRelationshiptypes fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("has-component".equals(codeString))
          return ObservationRelationshiptypes.HASCOMPONENT;
        if ("has-member".equals(codeString))
          return ObservationRelationshiptypes.HASMEMBER;
        if ("derived-from".equals(codeString))
          return ObservationRelationshiptypes.DERIVEDFROM;
        if ("sequel-to".equals(codeString))
          return ObservationRelationshiptypes.SEQUELTO;
        if ("replaces".equals(codeString))
          return ObservationRelationshiptypes.REPLACES;
        if ("qualified-by".equals(codeString))
          return ObservationRelationshiptypes.QUALIFIEDBY;
        if ("interfered-by".equals(codeString))
          return ObservationRelationshiptypes.INTERFEREDBY;
        throw new IllegalArgumentException("Unknown ObservationRelationshiptypes code '"+codeString+"'");
        }
    public String toCode(ObservationRelationshiptypes code) {
      if (code == ObservationRelationshiptypes.HASCOMPONENT)
        return "has-component";
      if (code == ObservationRelationshiptypes.HASMEMBER)
        return "has-member";
      if (code == ObservationRelationshiptypes.DERIVEDFROM)
        return "derived-from";
      if (code == ObservationRelationshiptypes.SEQUELTO)
        return "sequel-to";
      if (code == ObservationRelationshiptypes.REPLACES)
        return "replaces";
      if (code == ObservationRelationshiptypes.QUALIFIEDBY)
        return "qualified-by";
      if (code == ObservationRelationshiptypes.INTERFEREDBY)
        return "interfered-by";
      return "?";
      }
    }

    @Block()
    public static class ObservationReferenceRangeComponent extends BackboneElement {
        /**
         * The value of the low bound of the reference range. If this element is omitted, the low bound of the reference range is assumed to be meaningless. (e.g. reference range is <2.3)  If the low.comparator element is missing, it is assumed to be '>'.
         */
        @Child(name="low", type={Quantity.class}, order=1, min=0, max=1)
        @Description(shortDefinition = "Low Range, if relevant", formalDefinition = "The value of the low bound of the reference range. If this element is omitted, the low bound of the reference range is assumed to be meaningless. (e.g. reference range is <2.3)  If the low.comparator element is missing, it is assumed to be '>'.")
        protected Quantity low;

        /**
         * The value of the high bound of the reference range. If this element is omitted, the high bound of the reference range is assumed to be meaningless. (e.g. reference range is > 5)  If the low.comparator element is missing , it is assumed to be '<'.
         */
        @Child(name="high", type={Quantity.class}, order=2, min=0, max=1)
        @Description(shortDefinition = "High Range, if relevant", formalDefinition = "The value of the high bound of the reference range. If this element is omitted, the high bound of the reference range is assumed to be meaningless. (e.g. reference range is > 5)  If the low.comparator element is missing , it is assumed to be '<'.")
        protected Quantity high;

        /**
         * Code for the meaning of the reference range.
         */
        @Child(name="meaning", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Indicates the meaning/use of this range of this range", formalDefinition="Code for the meaning of the reference range." )
        protected CodeableConcept meaning;

        /**
         * The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.
         */
        @Child(name="age", type={Range.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Applicable age range, if relevant", formalDefinition="The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so." )
        protected Range age;

        /**
         * Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.
         */
        @Child(name="text", type={StringType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Text based reference range in an observation", formalDefinition="Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of 'Negative' or a list or table of 'normals'." )
        protected StringType text;

        private static final long serialVersionUID = 230621180L;

      public ObservationReferenceRangeComponent() {
        super();
      }

        /**
         * @return {@link #low} (The value of the low bound of the reference range. If this element is omitted, the low bound of the reference range is assumed to be meaningless. (e.g. reference range is <2.3)  If the low.comparator element is missing, it is assumed to be '>'.)
         */
        public Quantity getLow() { 
          if (this.low == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.low");
            else if (Configuration.doAutoCreate())
              this.low = new Quantity(); // cc
          return this.low;
        }

        public boolean hasLow() { 
          return this.low != null && !this.low.isEmpty();
        }

        /**
         * @param value {@link #low} (The value of the low bound of the reference range. If this element is omitted, the low bound of the reference range is assumed to be meaningless. (e.g. reference range is <2.3)  If the low.comparator element is missing, it is assumed to be '>'.)
         */
        public ObservationReferenceRangeComponent setLow(Quantity value) { 
          this.low = value;
          return this;
        }

        /**
         * @return {@link #high} (The value of the high bound of the reference range. If this element is omitted, the high bound of the reference range is assumed to be meaningless. (e.g. reference range is > 5)  If the low.comparator element is missing , it is assumed to be '<'.)
         */
        public Quantity getHigh() { 
          if (this.high == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.high");
            else if (Configuration.doAutoCreate())
              this.high = new Quantity(); // cc
          return this.high;
        }

        public boolean hasHigh() { 
          return this.high != null && !this.high.isEmpty();
        }

        /**
         * @param value {@link #high} (The value of the high bound of the reference range. If this element is omitted, the high bound of the reference range is assumed to be meaningless. (e.g. reference range is > 5)  If the low.comparator element is missing , it is assumed to be '<'.)
         */
        public ObservationReferenceRangeComponent setHigh(Quantity value) { 
          this.high = value;
          return this;
        }

        /**
         * @return {@link #meaning} (Code for the meaning of the reference range.)
         */
        public CodeableConcept getMeaning() { 
          if (this.meaning == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.meaning");
            else if (Configuration.doAutoCreate())
              this.meaning = new CodeableConcept(); // cc
          return this.meaning;
        }

        public boolean hasMeaning() { 
          return this.meaning != null && !this.meaning.isEmpty();
        }

        /**
         * @param value {@link #meaning} (Code for the meaning of the reference range.)
         */
        public ObservationReferenceRangeComponent setMeaning(CodeableConcept value) { 
          this.meaning = value;
          return this;
        }

        /**
         * @return {@link #age} (The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.)
         */
        public Range getAge() { 
          if (this.age == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.age");
            else if (Configuration.doAutoCreate())
              this.age = new Range(); // cc
          return this.age;
        }

        public boolean hasAge() { 
          return this.age != null && !this.age.isEmpty();
        }

        /**
         * @param value {@link #age} (The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.)
         */
        public ObservationReferenceRangeComponent setAge(Range value) { 
          this.age = value;
          return this;
        }

        /**
         * @return {@link #text} (Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationReferenceRangeComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ObservationReferenceRangeComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of "Negative" or a list or table of 'normals'.
         */
        public ObservationReferenceRangeComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
            childrenList.add(new Property("low", "Quantity", "The value of the low bound of the reference range. If this element is omitted, the low bound of the reference range is assumed to be meaningless. (e.g. reference range is <2.3)  If the low.comparator element is missing, it is assumed to be '>'.", 0, java.lang.Integer.MAX_VALUE, low));
            childrenList.add(new Property("high", "Quantity", "The value of the high bound of the reference range. If this element is omitted, the high bound of the reference range is assumed to be meaningless. (e.g. reference range is > 5)  If the low.comparator element is missing , it is assumed to be '<'.", 0, java.lang.Integer.MAX_VALUE, high));
          childrenList.add(new Property("meaning", "CodeableConcept", "Code for the meaning of the reference range.", 0, java.lang.Integer.MAX_VALUE, meaning));
          childrenList.add(new Property("age", "Range", "The age at which this reference range is applicable. This is a neonatal age (e.g. number of weeks at term) if the meaning says so.", 0, java.lang.Integer.MAX_VALUE, age));
          childrenList.add(new Property("text", "string", "Text based reference range in an observation which may be used when a quantitative range is not appropriate for an observation.  An example would be a reference value of 'Negative' or a list or table of 'normals'.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      public ObservationReferenceRangeComponent copy() {
        ObservationReferenceRangeComponent dst = new ObservationReferenceRangeComponent();
        copyValues(dst);
        dst.low = low == null ? null : low.copy();
        dst.high = high == null ? null : high.copy();
        dst.meaning = meaning == null ? null : meaning.copy();
        dst.age = age == null ? null : age.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ObservationReferenceRangeComponent))
          return false;
        ObservationReferenceRangeComponent o = (ObservationReferenceRangeComponent) other;
        return compareDeep(low, o.low, true) && compareDeep(high, o.high, true) && compareDeep(meaning, o.meaning, true)
           && compareDeep(age, o.age, true) && compareDeep(text, o.text, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ObservationReferenceRangeComponent))
          return false;
        ObservationReferenceRangeComponent o = (ObservationReferenceRangeComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (low == null || low.isEmpty()) && (high == null || high.isEmpty())
           && (meaning == null || meaning.isEmpty()) && (age == null || age.isEmpty()) && (text == null || text.isEmpty())
          ;
      }

  }

    @Block()
    public static class ObservationRelatedComponent extends BackboneElement {
        /**
         * A code specifying the kind of relationship that exists with the target observation.
         */
        @Child(name="type", type={CodeType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="has-component | has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by", formalDefinition="A code specifying the kind of relationship that exists with the target observation." )
        protected Enumeration<ObservationRelationshiptypes> type;

        /**
         * A reference to the observation that is related to this observation.
         */
        @Child(name="target", type={Observation.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Observation that is related to this one", formalDefinition="A reference to the observation that is related to this observation." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (A reference to the observation that is related to this observation.)
         */
        protected Observation targetTarget;

        private static final long serialVersionUID = 1078793488L;

      public ObservationRelatedComponent() {
        super();
      }

      public ObservationRelatedComponent(Reference target) {
        super();
        this.target = target;
      }

        /**
         * @return {@link #type} (A code specifying the kind of relationship that exists with the target observation.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ObservationRelationshiptypes> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationRelatedComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ObservationRelationshiptypes>(new ObservationRelationshiptypesEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code specifying the kind of relationship that exists with the target observation.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ObservationRelatedComponent setTypeElement(Enumeration<ObservationRelationshiptypes> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return A code specifying the kind of relationship that exists with the target observation.
         */
        public ObservationRelationshiptypes getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value A code specifying the kind of relationship that exists with the target observation.
         */
        public ObservationRelatedComponent setType(ObservationRelationshiptypes value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<ObservationRelationshiptypes>(new ObservationRelationshiptypesEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #target} (A reference to the observation that is related to this observation.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationRelatedComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (A reference to the observation that is related to this observation.)
         */
        public ObservationRelatedComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the observation that is related to this observation.)
         */
        public Observation getTargetTarget() { 
          if (this.targetTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ObservationRelatedComponent.target");
            else if (Configuration.doAutoCreate())
              this.targetTarget = new Observation(); // aa
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the observation that is related to this observation.)
         */
        public ObservationRelatedComponent setTargetTarget(Observation value) { 
          this.targetTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "A code specifying the kind of relationship that exists with the target observation.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("target", "Reference(Observation)", "A reference to the observation that is related to this observation.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      public ObservationRelatedComponent copy() {
        ObservationRelatedComponent dst = new ObservationRelatedComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ObservationRelatedComponent))
          return false;
        ObservationRelatedComponent o = (ObservationRelatedComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ObservationRelatedComponent))
          return false;
        ObservationRelatedComponent o = (ObservationRelatedComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (target == null || target.isEmpty())
          ;
      }

  }

    /**
     * Describes what was observed. Sometimes this is called the observation "code".
     */
    @Child(name = "code", type = {CodeableConcept.class}, order = 0, min = 1, max = 1)
    @Description(shortDefinition="Type of observation (code / type)", formalDefinition="Describes what was observed. Sometimes this is called the observation 'code'." )
    protected CodeableConcept code;

    /**
     * The information determined as a result of making the observation, if the information has a simple value.
     */
    @Child(name = "value", type = {Quantity.class, CodeableConcept.class, Attachment.class, Ratio.class, DateTimeType.class, Period.class, SampledData.class, StringType.class, TimeType.class}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Actual result", formalDefinition="The information determined as a result of making the observation, if the information has a simple value." )
    protected Type value;

    /**
     * Provides a reason why the expected value in the element Observation.value[x] is missing.
     */
    @Child(name = "dataAbsentReason", type = {CodeType.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="unknown | asked | temp | notasked +", formalDefinition="Provides a reason why the expected value in the element Observation.value[x] is missing." )
    protected Enumeration<DataAbsentReason> dataAbsentReason;

    /**
     * The assessment made based on the result of the observation.
     */
    @Child(name = "interpretation", type = {CodeableConcept.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="High, low, normal, etc.", formalDefinition="The assessment made based on the result of the observation." )
    protected CodeableConcept interpretation;

    /**
     * May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     */
    @Child(name = "comments", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Comments about result", formalDefinition="May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result." )
    protected StringType comments;

    /**
     * The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.
     */
    @Child(name = "applies", type = {DateTimeType.class, Period.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Physiologically Relevant time/time-period for observation", formalDefinition="The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the 'physiologically relevant time'. This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself." )
    protected Type applies;

    /**
     * The date and time this observation was made available.
     */
    @Child(name = "issued", type = {InstantType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Date/Time this was made available", formalDefinition="The date and time this observation was made available." )
    protected InstantType issued;

    /**
     * The status of the result value.
     */
    @Child(name = "status", type = {CodeType.class}, order = 7, min = 1, max = 1)
    @Description(shortDefinition="registered | preliminary | final | amended +", formalDefinition="The status of the result value." )
    protected Enumeration<ObservationStatus> status;

    /**
     * An estimate of the degree to which quality issues have impacted on the value reported.
     */
    @Child(name = "reliability", type = {CodeType.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="ok | ongoing | early | questionable | calibrating | error +", formalDefinition="An estimate of the degree to which quality issues have impacted on the value reported." )
    protected Enumeration<ObservationReliability> reliability;

    /**
     * Indicates the site on the subject's body where the observation was made ( i.e. the target site).
     */
    @Child(name = "bodySite", type = {CodeableConcept.class, BodySite.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Observed body part", formalDefinition="Indicates the site on the subject's body where the observation was made ( i.e. the target site)." )
    protected Type bodySite;

    /**
     * Indicates the mechanism used to perform the observation.
     */
    @Child(name = "method", type = {CodeableConcept.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="How it was done", formalDefinition="Indicates the mechanism used to perform the observation." )
    protected CodeableConcept method;

    /**
     * A unique identifier for the simple observation.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="Unique Id for this particular observation", formalDefinition="A unique identifier for the simple observation." )
    protected Identifier identifier;

    /**
     * The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,
other observer (for example a relative or EMT), or any observation made about the subject.
     */
    @Child(name = "subject", type = {Patient.class, Group.class, Device.class, Location.class}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="Who and/or what this is about", formalDefinition="The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,\nother observer (for example a relative or EMT), or any observation made about the subject." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,
other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    protected Resource subjectTarget;

    /**
     * The specimen that was used when this observation was made.
     */
    @Child(name = "specimen", type = {Specimen.class}, order = 13, min = 0, max = 1)
    @Description(shortDefinition="Specimen used for this observation", formalDefinition="The specimen that was used when this observation was made." )
    protected Reference specimen;

    /**
     * The actual object that is the target of the reference (The specimen that was used when this observation was made.)
     */
    protected Specimen specimenTarget;

    /**
     * Who was responsible for asserting the observed value as "true".
     */
    @Child(name = "performer", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class}, order = 14, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who did the observation", formalDefinition="Who was responsible for asserting the observed value as 'true'." )
    protected List<Reference> performer;
    /**
     * The actual objects that are the target of the reference (Who was responsible for asserting the observed value as "true".)
     */
    protected List<Resource> performerTarget;


    /**
     * The device used to generate the observation data.
     */
    @Child(name = "device", type = {Device.class, DeviceMetric.class}, order = 15, min = 0, max = 1)
    @Description(shortDefinition="(Measurement) Device", formalDefinition="The device used to generate the observation data." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The device used to generate the observation data.)
     */
    protected Resource deviceTarget;

    /**
     * The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 16, min = 0, max = 1)
    @Description(shortDefinition="Healthcare event during which this observation is made", formalDefinition="The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made.)
     */
    protected Encounter encounterTarget;

    /**
     * Guidance on how to interpret the value by comparison to a normal or recommended range.
     */
    @Child(name = "referenceRange", type = {}, order = 17, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Provides guide for interpretation", formalDefinition="Guidance on how to interpret the value by comparison to a normal or recommended range." )
    protected List<ObservationReferenceRangeComponent> referenceRange;

    /**
     * Related observations - either components, or previous observations, or statements of derivation.
     */
    @Child(name = "related", type = {}, order = 18, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Observations related to this observation", formalDefinition="Related observations - either components, or previous observations, or statements of derivation." )
    protected List<ObservationRelatedComponent> related;

    private static final long serialVersionUID = 1185478098L;

    public Observation() {
      super();
    }

    public Observation(CodeableConcept code, Enumeration<ObservationStatus> status) {
      super();
        this.code = code;
      this.status = status;
    }

    /**
     * @return {@link #code} (Describes what was observed. Sometimes this is called the observation "code".)
     */
    public CodeableConcept getCode() {
        if (this.code == null)
        if (Configuration.errorOnAutoCreate())
            throw new Error("Attempt to auto-create Observation.code");
        else if (Configuration.doAutoCreate())
            this.code = new CodeableConcept(); // cc
        return this.code;
    }

    public boolean hasCode() {
        return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Describes what was observed. Sometimes this is called the observation "code".)
     */
    public Observation setCode(CodeableConcept value) {
        this.code = value;
      return this;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Type getValue() { 
      return this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Quantity getValueQuantity() throws Exception { 
      if (!(this.value instanceof Quantity))
        throw new Exception("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Quantity) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public CodeableConcept getValueCodeableConcept() throws Exception { 
      if (!(this.value instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.value.getClass().getName()+" was encountered");
      return (CodeableConcept) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Attachment getValueAttachment() throws Exception { 
      if (!(this.value instanceof Attachment))
        throw new Exception("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Attachment) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Ratio getValueRatio() throws Exception { 
      if (!(this.value instanceof Ratio))
        throw new Exception("Type mismatch: the type Ratio was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Ratio) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public DateTimeType getValueDateTimeType() throws Exception { 
      if (!(this.value instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
      return (DateTimeType) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Period getValuePeriod() throws Exception { 
      if (!(this.value instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.value.getClass().getName()+" was encountered");
      return (Period) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public SampledData getValueSampledData() throws Exception { 
      if (!(this.value instanceof SampledData))
        throw new Exception("Type mismatch: the type SampledData was expected, but "+this.value.getClass().getName()+" was encountered");
      return (SampledData) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public StringType getValueStringType() throws Exception { 
      if (!(this.value instanceof StringType))
        throw new Exception("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
      return (StringType) this.value;
    }

    /**
     * @return {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public TimeType getValueTimeType() throws Exception { 
      if (!(this.value instanceof TimeType))
        throw new Exception("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
      return (TimeType) this.value;
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (The information determined as a result of making the observation, if the information has a simple value.)
     */
    public Observation setValue(Type value) { 
      this.value = value;
      return this;
    }

    /**
     * @return {@link #dataAbsentReason} (Provides a reason why the expected value in the element Observation.value[x] is missing.). This is the underlying object with id, value and extensions. The accessor "getDataAbsentReason" gives direct access to the value
     */
    public Enumeration<DataAbsentReason> getDataAbsentReasonElement() { 
      if (this.dataAbsentReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.dataAbsentReason");
        else if (Configuration.doAutoCreate())
          this.dataAbsentReason = new Enumeration<DataAbsentReason>(new DataAbsentReasonEnumFactory()); // bb
      return this.dataAbsentReason;
    }

    public boolean hasDataAbsentReasonElement() { 
      return this.dataAbsentReason != null && !this.dataAbsentReason.isEmpty();
    }

    public boolean hasDataAbsentReason() { 
      return this.dataAbsentReason != null && !this.dataAbsentReason.isEmpty();
    }

    /**
     * @param value {@link #dataAbsentReason} (Provides a reason why the expected value in the element Observation.value[x] is missing.). This is the underlying object with id, value and extensions. The accessor "getDataAbsentReason" gives direct access to the value
     */
    public Observation setDataAbsentReasonElement(Enumeration<DataAbsentReason> value) { 
      this.dataAbsentReason = value;
      return this;
    }

    /**
     * @return Provides a reason why the expected value in the element Observation.value[x] is missing.
     */
    public DataAbsentReason getDataAbsentReason() { 
      return this.dataAbsentReason == null ? null : this.dataAbsentReason.getValue();
    }

    /**
     * @param value Provides a reason why the expected value in the element Observation.value[x] is missing.
     */
    public Observation setDataAbsentReason(DataAbsentReason value) { 
      if (value == null)
        this.dataAbsentReason = null;
      else {
        if (this.dataAbsentReason == null)
          this.dataAbsentReason = new Enumeration<DataAbsentReason>(new DataAbsentReasonEnumFactory());
        this.dataAbsentReason.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #interpretation} (The assessment made based on the result of the observation.)
     */
    public CodeableConcept getInterpretation() { 
      if (this.interpretation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.interpretation");
        else if (Configuration.doAutoCreate())
          this.interpretation = new CodeableConcept(); // cc
      return this.interpretation;
    }

    public boolean hasInterpretation() { 
      return this.interpretation != null && !this.interpretation.isEmpty();
    }

    /**
     * @param value {@link #interpretation} (The assessment made based on the result of the observation.)
     */
    public Observation setInterpretation(CodeableConcept value) { 
      this.interpretation = value;
      return this;
    }

    /**
     * @return {@link #comments} (May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
     */
    public StringType getCommentsElement() { 
      if (this.comments == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.comments");
        else if (Configuration.doAutoCreate())
          this.comments = new StringType(); // bb
      return this.comments;
    }

    public boolean hasCommentsElement() { 
      return this.comments != null && !this.comments.isEmpty();
    }

    public boolean hasComments() { 
      return this.comments != null && !this.comments.isEmpty();
    }

    /**
     * @param value {@link #comments} (May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.). This is the underlying object with id, value and extensions. The accessor "getComments" gives direct access to the value
     */
    public Observation setCommentsElement(StringType value) { 
      this.comments = value;
      return this;
    }

    /**
     * @return May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     */
    public String getComments() { 
      return this.comments == null ? null : this.comments.getValue();
    }

    /**
     * @param value May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.
     */
    public Observation setComments(String value) { 
      if (Utilities.noString(value))
        this.comments = null;
      else {
        if (this.comments == null)
          this.comments = new StringType();
        this.comments.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #applies} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Type getApplies() { 
      return this.applies;
    }

    /**
     * @return {@link #applies} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public DateTimeType getAppliesDateTimeType() throws Exception { 
      if (!(this.applies instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.applies.getClass().getName()+" was encountered");
      return (DateTimeType) this.applies;
    }

    /**
     * @return {@link #applies} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Period getAppliesPeriod() throws Exception { 
      if (!(this.applies instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.applies.getClass().getName()+" was encountered");
      return (Period) this.applies;
    }

    public boolean hasApplies() { 
      return this.applies != null && !this.applies.isEmpty();
    }

    /**
     * @param value {@link #applies} (The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the "physiologically relevant time". This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.)
     */
    public Observation setApplies(Type value) { 
      this.applies = value;
      return this;
    }

    /**
     * @return {@link #issued} (The date and time this observation was made available.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public InstantType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new InstantType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (The date and time this observation was made available.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Observation setIssuedElement(InstantType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return The date and time this observation was made available.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value The date and time this observation was made available.
     */
    public Observation setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new InstantType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of the result value.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ObservationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ObservationStatus>(new ObservationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the result value.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Observation setStatusElement(Enumeration<ObservationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the result value.
     */
    public ObservationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the result value.
     */
    public Observation setStatus(ObservationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ObservationStatus>(new ObservationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #reliability} (An estimate of the degree to which quality issues have impacted on the value reported.). This is the underlying object with id, value and extensions. The accessor "getReliability" gives direct access to the value
     */
    public Enumeration<ObservationReliability> getReliabilityElement() { 
      if (this.reliability == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.reliability");
        else if (Configuration.doAutoCreate())
          this.reliability = new Enumeration<ObservationReliability>(new ObservationReliabilityEnumFactory()); // bb
      return this.reliability;
    }

    public boolean hasReliabilityElement() { 
      return this.reliability != null && !this.reliability.isEmpty();
    }

    public boolean hasReliability() { 
      return this.reliability != null && !this.reliability.isEmpty();
    }

    /**
     * @param value {@link #reliability} (An estimate of the degree to which quality issues have impacted on the value reported.). This is the underlying object with id, value and extensions. The accessor "getReliability" gives direct access to the value
     */
    public Observation setReliabilityElement(Enumeration<ObservationReliability> value) { 
      this.reliability = value;
      return this;
    }

    /**
     * @return An estimate of the degree to which quality issues have impacted on the value reported.
     */
    public ObservationReliability getReliability() { 
      return this.reliability == null ? null : this.reliability.getValue();
    }

    /**
     * @param value An estimate of the degree to which quality issues have impacted on the value reported.
     */
    public Observation setReliability(ObservationReliability value) { 
      if (value == null)
        this.reliability = null;
      else {
        if (this.reliability == null)
          this.reliability = new Enumeration<ObservationReliability>(new ObservationReliabilityEnumFactory());
        this.reliability.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the observation was made ( i.e. the target site).)
     */
    public Type getBodySite() { 
      return this.bodySite;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the observation was made ( i.e. the target site).)
     */
    public CodeableConcept getBodySiteCodeableConcept() throws Exception { 
      if (!(this.bodySite instanceof CodeableConcept))
        throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.bodySite.getClass().getName()+" was encountered");
      return (CodeableConcept) this.bodySite;
    }

    /**
     * @return {@link #bodySite} (Indicates the site on the subject's body where the observation was made ( i.e. the target site).)
     */
    public Reference getBodySiteReference() throws Exception { 
      if (!(this.bodySite instanceof Reference))
        throw new Exception("Type mismatch: the type Reference was expected, but "+this.bodySite.getClass().getName()+" was encountered");
      return (Reference) this.bodySite;
    }

    public boolean hasBodySite() { 
      return this.bodySite != null && !this.bodySite.isEmpty();
    }

    /**
     * @param value {@link #bodySite} (Indicates the site on the subject's body where the observation was made ( i.e. the target site).)
     */
    public Observation setBodySite(Type value) { 
      this.bodySite = value;
      return this;
    }

    /**
     * @return {@link #method} (Indicates the mechanism used to perform the observation.)
     */
    public CodeableConcept getMethod() { 
      if (this.method == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.method");
        else if (Configuration.doAutoCreate())
          this.method = new CodeableConcept(); // cc
      return this.method;
    }

    public boolean hasMethod() { 
      return this.method != null && !this.method.isEmpty();
    }

    /**
     * @param value {@link #method} (Indicates the mechanism used to perform the observation.)
     */
    public Observation setMethod(CodeableConcept value) { 
      this.method = value;
      return this;
    }

    /**
     * @return {@link #identifier} (A unique identifier for the simple observation.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (A unique identifier for the simple observation.)
     */
    public Observation setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #subject} (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,
other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,
other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Observation setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,
other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,
other observer (for example a relative or EMT), or any observation made about the subject.)
     */
    public Observation setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #specimen} (The specimen that was used when this observation was made.)
     */
    public Reference getSpecimen() { 
      if (this.specimen == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.specimen");
        else if (Configuration.doAutoCreate())
          this.specimen = new Reference(); // cc
      return this.specimen;
    }

    public boolean hasSpecimen() { 
      return this.specimen != null && !this.specimen.isEmpty();
    }

    /**
     * @param value {@link #specimen} (The specimen that was used when this observation was made.)
     */
    public Observation setSpecimen(Reference value) { 
      this.specimen = value;
      return this;
    }

    /**
     * @return {@link #specimen} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The specimen that was used when this observation was made.)
     */
    public Specimen getSpecimenTarget() { 
      if (this.specimenTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.specimen");
        else if (Configuration.doAutoCreate())
          this.specimenTarget = new Specimen(); // aa
      return this.specimenTarget;
    }

    /**
     * @param value {@link #specimen} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The specimen that was used when this observation was made.)
     */
    public Observation setSpecimenTarget(Specimen value) { 
      this.specimenTarget = value;
      return this;
    }

    /**
     * @return {@link #performer} (Who was responsible for asserting the observed value as "true".)
     */
    public List<Reference> getPerformer() { 
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      return this.performer;
    }

    public boolean hasPerformer() { 
      if (this.performer == null)
        return false;
      for (Reference item : this.performer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #performer} (Who was responsible for asserting the observed value as "true".)
     */
    // syntactic sugar
    public Reference addPerformer() { //3
      Reference t = new Reference();
      if (this.performer == null)
        this.performer = new ArrayList<Reference>();
      this.performer.add(t);
      return t;
    }

    /**
     * @return {@link #performer} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Who was responsible for asserting the observed value as "true".)
     */
    public List<Resource> getPerformerTarget() { 
      if (this.performerTarget == null)
        this.performerTarget = new ArrayList<Resource>();
      return this.performerTarget;
    }

    /**
     * @return {@link #device} (The device used to generate the observation data.)
     */
    public Reference getDevice() { 
      if (this.device == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.device");
        else if (Configuration.doAutoCreate())
          this.device = new Reference(); // cc
      return this.device;
    }

    public boolean hasDevice() { 
      return this.device != null && !this.device.isEmpty();
    }

    /**
     * @param value {@link #device} (The device used to generate the observation data.)
     */
    public Observation setDevice(Reference value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #device} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device used to generate the observation data.)
     */
    public Resource getDeviceTarget() { 
      return this.deviceTarget;
    }

    /**
     * @param value {@link #device} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device used to generate the observation data.)
     */
    public Observation setDeviceTarget(Resource value) { 
      this.deviceTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made.)
     */
    public Observation setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Observation.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made.)
     */
    public Observation setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #referenceRange} (Guidance on how to interpret the value by comparison to a normal or recommended range.)
     */
    public List<ObservationReferenceRangeComponent> getReferenceRange() { 
      if (this.referenceRange == null)
        this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
      return this.referenceRange;
    }

    public boolean hasReferenceRange() { 
      if (this.referenceRange == null)
        return false;
      for (ObservationReferenceRangeComponent item : this.referenceRange)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #referenceRange} (Guidance on how to interpret the value by comparison to a normal or recommended range.)
     */
    // syntactic sugar
    public ObservationReferenceRangeComponent addReferenceRange() { //3
      ObservationReferenceRangeComponent t = new ObservationReferenceRangeComponent();
      if (this.referenceRange == null)
        this.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
      this.referenceRange.add(t);
      return t;
    }

    /**
     * @return {@link #related} (Related observations - either components, or previous observations, or statements of derivation.)
     */
    public List<ObservationRelatedComponent> getRelated() { 
      if (this.related == null)
        this.related = new ArrayList<ObservationRelatedComponent>();
      return this.related;
    }

    public boolean hasRelated() { 
      if (this.related == null)
        return false;
      for (ObservationRelatedComponent item : this.related)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #related} (Related observations - either components, or previous observations, or statements of derivation.)
     */
    // syntactic sugar
    public ObservationRelatedComponent addRelated() { //3
      ObservationRelatedComponent t = new ObservationRelatedComponent();
      if (this.related == null)
        this.related = new ArrayList<ObservationRelatedComponent>();
      this.related.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "Describes what was observed. Sometimes this is called the observation 'code'.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("value[x]", "Quantity|CodeableConcept|Attachment|Ratio|dateTime|Period|SampledData|string|time", "The information determined as a result of making the observation, if the information has a simple value.", 0, java.lang.Integer.MAX_VALUE, value));
        childrenList.add(new Property("dataAbsentReason", "code", "Provides a reason why the expected value in the element Observation.value[x] is missing.", 0, java.lang.Integer.MAX_VALUE, dataAbsentReason));
        childrenList.add(new Property("interpretation", "CodeableConcept", "The assessment made based on the result of the observation.", 0, java.lang.Integer.MAX_VALUE, interpretation));
        childrenList.add(new Property("comments", "string", "May include statements about significant, unexpected or unreliable values, or information about the source of the value where this may be relevant to the interpretation of the result.", 0, java.lang.Integer.MAX_VALUE, comments));
        childrenList.add(new Property("applies[x]", "dateTime|Period", "The time or time-period the observed value is asserted as being true. For biological subjects - e.g. human patients - this is usually called the 'physiologically relevant time'. This is usually either the time of the procedure or of specimen collection, but very often the source of the date/time is not known, only the date/time itself.", 0, java.lang.Integer.MAX_VALUE, applies));
        childrenList.add(new Property("issued", "instant", "The date and time this observation was made available.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("status", "code", "The status of the result value.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("reliability", "code", "An estimate of the degree to which quality issues have impacted on the value reported.", 0, java.lang.Integer.MAX_VALUE, reliability));
        childrenList.add(new Property("bodySite[x]", "CodeableConcept|Reference(BodySite)", "Indicates the site on the subject's body where the observation was made ( i.e. the target site).", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("method", "CodeableConcept", "Indicates the mechanism used to perform the observation.", 0, java.lang.Integer.MAX_VALUE, method));
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier for the simple observation.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Group|Device|Location)", "The patient, or group of patients, location, or device whose characteristics (direct or indirect) are described by the observation and into whose record the observation is placed.  Comments: Indirect characteristics may be those of a specimen, fetus,\nother observer (for example a relative or EMT), or any observation made about the subject.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "The specimen that was used when this observation was made.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("performer", "Reference(Practitioner|Organization|Patient|RelatedPerson)", "Who was responsible for asserting the observed value as 'true'.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("device", "Reference(Device|DeviceMetric)", "The device used to generate the observation data.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The healthcare event  ( e.g. a patient and healthcare provider interaction ) during which this observation is made.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("referenceRange", "", "Guidance on how to interpret the value by comparison to a normal or recommended range.", 0, java.lang.Integer.MAX_VALUE, referenceRange));
        childrenList.add(new Property("related", "", "Related observations - either components, or previous observations, or statements of derivation.", 0, java.lang.Integer.MAX_VALUE, related));
      }

      public Observation copy() {
        Observation dst = new Observation();
        copyValues(dst);
          dst.code = code == null ? null : code.copy();
        dst.value = value == null ? null : value.copy();
        dst.dataAbsentReason = dataAbsentReason == null ? null : dataAbsentReason.copy();
        dst.interpretation = interpretation == null ? null : interpretation.copy();
        dst.comments = comments == null ? null : comments.copy();
        dst.applies = applies == null ? null : applies.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.status = status == null ? null : status.copy();
        dst.reliability = reliability == null ? null : reliability.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        dst.method = method == null ? null : method.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.specimen = specimen == null ? null : specimen.copy();
        if (performer != null) {
          dst.performer = new ArrayList<Reference>();
          for (Reference i : performer)
            dst.performer.add(i.copy());
        };
        dst.device = device == null ? null : device.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        if (referenceRange != null) {
          dst.referenceRange = new ArrayList<ObservationReferenceRangeComponent>();
          for (ObservationReferenceRangeComponent i : referenceRange)
            dst.referenceRange.add(i.copy());
        };
        if (related != null) {
          dst.related = new ArrayList<ObservationRelatedComponent>();
          for (ObservationRelatedComponent i : related)
            dst.related.add(i.copy());
        };
        return dst;
      }

      protected Observation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Observation))
          return false;
        Observation o = (Observation) other;
          return compareDeep(code, o.code, true) && compareDeep(value, o.value, true) && compareDeep(dataAbsentReason, o.dataAbsentReason, true)
           && compareDeep(interpretation, o.interpretation, true) && compareDeep(comments, o.comments, true)
           && compareDeep(applies, o.applies, true) && compareDeep(issued, o.issued, true) && compareDeep(status, o.status, true)
           && compareDeep(reliability, o.reliability, true) && compareDeep(bodySite, o.bodySite, true) && compareDeep(method, o.method, true)
           && compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(specimen, o.specimen, true)
           && compareDeep(performer, o.performer, true) && compareDeep(device, o.device, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(referenceRange, o.referenceRange, true) && compareDeep(related, o.related, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Observation))
          return false;
        Observation o = (Observation) other;
        return compareValues(dataAbsentReason, o.dataAbsentReason, true) && compareValues(comments, o.comments, true)
           && compareValues(issued, o.issued, true) && compareValues(status, o.status, true) && compareValues(reliability, o.reliability, true)
          ;
      }

      public boolean isEmpty() {
          return super.isEmpty() && (code == null || code.isEmpty()) && (value == null || value.isEmpty())
           && (dataAbsentReason == null || dataAbsentReason.isEmpty()) && (interpretation == null || interpretation.isEmpty())
           && (comments == null || comments.isEmpty()) && (applies == null || applies.isEmpty()) && (issued == null || issued.isEmpty())
           && (status == null || status.isEmpty()) && (reliability == null || reliability.isEmpty())
           && (bodySite == null || bodySite.isEmpty()) && (method == null || method.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (subject == null || subject.isEmpty()) && (specimen == null || specimen.isEmpty()) && (performer == null || performer.isEmpty())
           && (device == null || device.isEmpty()) && (encounter == null || encounter.isEmpty()) && (referenceRange == null || referenceRange.isEmpty())
           && (related == null || related.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Observation;
   }

    @SearchParamDefinition(name = "date", path = "Observation.applies[x]", description = "Obtained date/time. If the obtained element is a period, a date that falls in the period", type = "date")
    public static final String SP_DATE = "date";
    @SearchParamDefinition(name = "identifier", path = "Observation.identifier", description = "The unique Id for a particular observation", type = "token")
    public static final String SP_IDENTIFIER = "identifier";
    @SearchParamDefinition(name = "code", path = "Observation.code", description = "The code of the observation type", type = "token")
    public static final String SP_CODE = "code";
    @SearchParamDefinition(name = "code-value-[x]", path = "", description = "Both code and one of the value parameters", type = "composite")
    public static final String SP_CODEVALUEX = "code-value-[x]";
    @SearchParamDefinition(name = "performer", path = "Observation.performer", description = "Who performed the observation", type = "reference")
    public static final String SP_PERFORMER = "performer";
    @SearchParamDefinition(name = "value-quantity", path = "Observation.valueQuantity", description = "The value of the observation, if the value is a Quantity, or a SampledData (just search on the bounds of the values in sampled data)", type = "quantity")
    public static final String SP_VALUEQUANTITY = "value-quantity";
  @SearchParamDefinition(name="subject", path="Observation.subject", description="The subject that the observation is about", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="reliability", path="Observation.reliability", description="The reliability of the observation", type="token" )
  public static final String SP_RELIABILITY = "reliability";
    @SearchParamDefinition(name = "value-concept", path = "Observation.valueCodeableConcept", description = "The value of the observation, if the value is a CodeableConcept", type = "token")
    public static final String SP_VALUECONCEPT = "value-concept";
    @SearchParamDefinition(name = "value-date", path = "Observation.valueDateTime|Observation.valuePeriod", description = "The value of the observation, if the value is a Period", type = "date")
    public static final String SP_VALUEDATE = "value-date";
    @SearchParamDefinition(name = "data-absent-reason", path = "Observation.dataAbsentReason", description = "The reason why the expected value in the element Observation.value[x] is missing.", type = "token")
    public static final String SP_DATAABSENTREASON = "data-absent-reason";
  @SearchParamDefinition(name="encounter", path="Observation.encounter", description="Healthcare event related to the observation", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
    @SearchParamDefinition(name = "related-type", path = "Observation.related.type", description = "has-component | has-member | derived-from | sequel-to | replaces | qualified-by | interfered-by", type = "token")
    public static final String SP_RELATEDTYPE = "related-type";
  @SearchParamDefinition(name="related-target", path="Observation.related.target", description="Observation that is related to this one", type="reference" )
  public static final String SP_RELATEDTARGET = "related-target";
  @SearchParamDefinition(name="related", path="", description="Related Observations - search on related-type and related-target together", type="composite" )
  public static final String SP_RELATED = "related";
  @SearchParamDefinition(name="patient", path="Observation.subject", description="The subject that the observation is about (if patient)", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="specimen", path="Observation.specimen", description="Specimen used for this observation", type="reference" )
  public static final String SP_SPECIMEN = "specimen";
    @SearchParamDefinition(name = "value-string", path = "Observation.valueString", description = "The value of the observation, if the value is a string, and also searches in CodeableConcept.text", type = "string")
    public static final String SP_VALUESTRING = "value-string";
  @SearchParamDefinition(name="device", path="Observation.device", description="The Device that generated the observation data.", type="reference" )
  public static final String SP_DEVICE = "device";
    @SearchParamDefinition(name = "status", path = "Observation.status", description = "The status of the observation", type = "token")
    public static final String SP_STATUS = "status";

}

