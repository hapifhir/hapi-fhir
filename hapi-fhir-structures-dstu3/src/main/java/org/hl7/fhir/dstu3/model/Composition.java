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
 * A set of healthcare-related information that is assembled together into a single logical document that provides a single coherent statement of meaning, establishes its own context and that has clinical attestation with regard to who is making the statement. While a Composition defines the structure, it does not actually contain the content: rather the full content of a document is contained in a Bundle, of which the Composition is the first resource contained.
 */
@ResourceDef(name="Composition", profile="http://hl7.org/fhir/Profile/Composition")
public class Composition extends DomainResource {

    public enum CompositionStatus {
        /**
         * This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or unverified.
         */
        PRELIMINARY, 
        /**
         * This version of the composition is complete and verified by an appropriate person and no further work is planned. Any subsequent updates would be on a new version of the composition.
         */
        FINAL, 
        /**
         * The composition content or the referenced resources have been modified (edited or added to) subsequent to being released as "final" and the composition is complete and verified by an authorized person.
         */
        AMENDED, 
        /**
         * The composition or document was originally created/issued in error, and this is an amendment that marks that the entire series should not be considered as valid.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CompositionStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preliminary".equals(codeString))
          return PRELIMINARY;
        if ("final".equals(codeString))
          return FINAL;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CompositionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case AMENDED: return "amended";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PRELIMINARY: return "http://hl7.org/fhir/composition-status";
            case FINAL: return "http://hl7.org/fhir/composition-status";
            case AMENDED: return "http://hl7.org/fhir/composition-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/composition-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PRELIMINARY: return "This is a preliminary composition or document (also known as initial or interim). The content may be incomplete or unverified.";
            case FINAL: return "This version of the composition is complete and verified by an appropriate person and no further work is planned. Any subsequent updates would be on a new version of the composition.";
            case AMENDED: return "The composition content or the referenced resources have been modified (edited or added to) subsequent to being released as \"final\" and the composition is complete and verified by an authorized person.";
            case ENTEREDINERROR: return "The composition or document was originally created/issued in error, and this is an amendment that marks that the entire series should not be considered as valid.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PRELIMINARY: return "Preliminary";
            case FINAL: return "Final";
            case AMENDED: return "Amended";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class CompositionStatusEnumFactory implements EnumFactory<CompositionStatus> {
    public CompositionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preliminary".equals(codeString))
          return CompositionStatus.PRELIMINARY;
        if ("final".equals(codeString))
          return CompositionStatus.FINAL;
        if ("amended".equals(codeString))
          return CompositionStatus.AMENDED;
        if ("entered-in-error".equals(codeString))
          return CompositionStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown CompositionStatus code '"+codeString+"'");
        }
        public Enumeration<CompositionStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CompositionStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("preliminary".equals(codeString))
          return new Enumeration<CompositionStatus>(this, CompositionStatus.PRELIMINARY);
        if ("final".equals(codeString))
          return new Enumeration<CompositionStatus>(this, CompositionStatus.FINAL);
        if ("amended".equals(codeString))
          return new Enumeration<CompositionStatus>(this, CompositionStatus.AMENDED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<CompositionStatus>(this, CompositionStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown CompositionStatus code '"+codeString+"'");
        }
    public String toCode(CompositionStatus code) {
      if (code == CompositionStatus.PRELIMINARY)
        return "preliminary";
      if (code == CompositionStatus.FINAL)
        return "final";
      if (code == CompositionStatus.AMENDED)
        return "amended";
      if (code == CompositionStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(CompositionStatus code) {
      return code.getSystem();
      }
    }

    public enum DocumentConfidentiality {
        /**
         * null
         */
        U, 
        /**
         * null
         */
        L, 
        /**
         * null
         */
        M, 
        /**
         * null
         */
        N, 
        /**
         * null
         */
        R, 
        /**
         * null
         */
        V, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DocumentConfidentiality fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("U".equals(codeString))
          return U;
        if ("L".equals(codeString))
          return L;
        if ("M".equals(codeString))
          return M;
        if ("N".equals(codeString))
          return N;
        if ("R".equals(codeString))
          return R;
        if ("V".equals(codeString))
          return V;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DocumentConfidentiality code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case U: return "U";
            case L: return "L";
            case M: return "M";
            case N: return "N";
            case R: return "R";
            case V: return "V";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case U: return "http://hl7.org/fhir/v3/Confidentiality";
            case L: return "http://hl7.org/fhir/v3/Confidentiality";
            case M: return "http://hl7.org/fhir/v3/Confidentiality";
            case N: return "http://hl7.org/fhir/v3/Confidentiality";
            case R: return "http://hl7.org/fhir/v3/Confidentiality";
            case V: return "http://hl7.org/fhir/v3/Confidentiality";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case U: return "";
            case L: return "";
            case M: return "";
            case N: return "";
            case R: return "";
            case V: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case U: return "U";
            case L: return "L";
            case M: return "M";
            case N: return "N";
            case R: return "R";
            case V: return "V";
            default: return "?";
          }
        }
    }

  public static class DocumentConfidentialityEnumFactory implements EnumFactory<DocumentConfidentiality> {
    public DocumentConfidentiality fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("U".equals(codeString))
          return DocumentConfidentiality.U;
        if ("L".equals(codeString))
          return DocumentConfidentiality.L;
        if ("M".equals(codeString))
          return DocumentConfidentiality.M;
        if ("N".equals(codeString))
          return DocumentConfidentiality.N;
        if ("R".equals(codeString))
          return DocumentConfidentiality.R;
        if ("V".equals(codeString))
          return DocumentConfidentiality.V;
        throw new IllegalArgumentException("Unknown DocumentConfidentiality code '"+codeString+"'");
        }
        public Enumeration<DocumentConfidentiality> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DocumentConfidentiality>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("U".equals(codeString))
          return new Enumeration<DocumentConfidentiality>(this, DocumentConfidentiality.U);
        if ("L".equals(codeString))
          return new Enumeration<DocumentConfidentiality>(this, DocumentConfidentiality.L);
        if ("M".equals(codeString))
          return new Enumeration<DocumentConfidentiality>(this, DocumentConfidentiality.M);
        if ("N".equals(codeString))
          return new Enumeration<DocumentConfidentiality>(this, DocumentConfidentiality.N);
        if ("R".equals(codeString))
          return new Enumeration<DocumentConfidentiality>(this, DocumentConfidentiality.R);
        if ("V".equals(codeString))
          return new Enumeration<DocumentConfidentiality>(this, DocumentConfidentiality.V);
        throw new FHIRException("Unknown DocumentConfidentiality code '"+codeString+"'");
        }
    public String toCode(DocumentConfidentiality code) {
      if (code == DocumentConfidentiality.U)
        return "U";
      if (code == DocumentConfidentiality.L)
        return "L";
      if (code == DocumentConfidentiality.M)
        return "M";
      if (code == DocumentConfidentiality.N)
        return "N";
      if (code == DocumentConfidentiality.R)
        return "R";
      if (code == DocumentConfidentiality.V)
        return "V";
      return "?";
      }
    public String toSystem(DocumentConfidentiality code) {
      return code.getSystem();
      }
    }

    public enum CompositionAttestationMode {
        /**
         * The person authenticated the content in their personal capacity.
         */
        PERSONAL, 
        /**
         * The person authenticated the content in their professional capacity.
         */
        PROFESSIONAL, 
        /**
         * The person authenticated the content and accepted legal responsibility for its content.
         */
        LEGAL, 
        /**
         * The organization authenticated the content as consistent with their policies and procedures.
         */
        OFFICIAL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CompositionAttestationMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("personal".equals(codeString))
          return PERSONAL;
        if ("professional".equals(codeString))
          return PROFESSIONAL;
        if ("legal".equals(codeString))
          return LEGAL;
        if ("official".equals(codeString))
          return OFFICIAL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CompositionAttestationMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PERSONAL: return "personal";
            case PROFESSIONAL: return "professional";
            case LEGAL: return "legal";
            case OFFICIAL: return "official";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PERSONAL: return "http://hl7.org/fhir/composition-attestation-mode";
            case PROFESSIONAL: return "http://hl7.org/fhir/composition-attestation-mode";
            case LEGAL: return "http://hl7.org/fhir/composition-attestation-mode";
            case OFFICIAL: return "http://hl7.org/fhir/composition-attestation-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PERSONAL: return "The person authenticated the content in their personal capacity.";
            case PROFESSIONAL: return "The person authenticated the content in their professional capacity.";
            case LEGAL: return "The person authenticated the content and accepted legal responsibility for its content.";
            case OFFICIAL: return "The organization authenticated the content as consistent with their policies and procedures.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PERSONAL: return "Personal";
            case PROFESSIONAL: return "Professional";
            case LEGAL: return "Legal";
            case OFFICIAL: return "Official";
            default: return "?";
          }
        }
    }

  public static class CompositionAttestationModeEnumFactory implements EnumFactory<CompositionAttestationMode> {
    public CompositionAttestationMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("personal".equals(codeString))
          return CompositionAttestationMode.PERSONAL;
        if ("professional".equals(codeString))
          return CompositionAttestationMode.PROFESSIONAL;
        if ("legal".equals(codeString))
          return CompositionAttestationMode.LEGAL;
        if ("official".equals(codeString))
          return CompositionAttestationMode.OFFICIAL;
        throw new IllegalArgumentException("Unknown CompositionAttestationMode code '"+codeString+"'");
        }
        public Enumeration<CompositionAttestationMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CompositionAttestationMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("personal".equals(codeString))
          return new Enumeration<CompositionAttestationMode>(this, CompositionAttestationMode.PERSONAL);
        if ("professional".equals(codeString))
          return new Enumeration<CompositionAttestationMode>(this, CompositionAttestationMode.PROFESSIONAL);
        if ("legal".equals(codeString))
          return new Enumeration<CompositionAttestationMode>(this, CompositionAttestationMode.LEGAL);
        if ("official".equals(codeString))
          return new Enumeration<CompositionAttestationMode>(this, CompositionAttestationMode.OFFICIAL);
        throw new FHIRException("Unknown CompositionAttestationMode code '"+codeString+"'");
        }
    public String toCode(CompositionAttestationMode code) {
      if (code == CompositionAttestationMode.PERSONAL)
        return "personal";
      if (code == CompositionAttestationMode.PROFESSIONAL)
        return "professional";
      if (code == CompositionAttestationMode.LEGAL)
        return "legal";
      if (code == CompositionAttestationMode.OFFICIAL)
        return "official";
      return "?";
      }
    public String toSystem(CompositionAttestationMode code) {
      return code.getSystem();
      }
    }

    public enum DocumentRelationshipType {
        /**
         * This document logically replaces or supersedes the target document.
         */
        REPLACES, 
        /**
         * This document was generated by transforming the target document (e.g. format or language conversion).
         */
        TRANSFORMS, 
        /**
         * This document is a signature of the target document.
         */
        SIGNS, 
        /**
         * This document adds additional information to the target document.
         */
        APPENDS, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DocumentRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replaces".equals(codeString))
          return REPLACES;
        if ("transforms".equals(codeString))
          return TRANSFORMS;
        if ("signs".equals(codeString))
          return SIGNS;
        if ("appends".equals(codeString))
          return APPENDS;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DocumentRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REPLACES: return "replaces";
            case TRANSFORMS: return "transforms";
            case SIGNS: return "signs";
            case APPENDS: return "appends";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REPLACES: return "http://hl7.org/fhir/document-relationship-type";
            case TRANSFORMS: return "http://hl7.org/fhir/document-relationship-type";
            case SIGNS: return "http://hl7.org/fhir/document-relationship-type";
            case APPENDS: return "http://hl7.org/fhir/document-relationship-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REPLACES: return "This document logically replaces or supersedes the target document.";
            case TRANSFORMS: return "This document was generated by transforming the target document (e.g. format or language conversion).";
            case SIGNS: return "This document is a signature of the target document.";
            case APPENDS: return "This document adds additional information to the target document.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REPLACES: return "Replaces";
            case TRANSFORMS: return "Transforms";
            case SIGNS: return "Signs";
            case APPENDS: return "Appends";
            default: return "?";
          }
        }
    }

  public static class DocumentRelationshipTypeEnumFactory implements EnumFactory<DocumentRelationshipType> {
    public DocumentRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("replaces".equals(codeString))
          return DocumentRelationshipType.REPLACES;
        if ("transforms".equals(codeString))
          return DocumentRelationshipType.TRANSFORMS;
        if ("signs".equals(codeString))
          return DocumentRelationshipType.SIGNS;
        if ("appends".equals(codeString))
          return DocumentRelationshipType.APPENDS;
        throw new IllegalArgumentException("Unknown DocumentRelationshipType code '"+codeString+"'");
        }
        public Enumeration<DocumentRelationshipType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DocumentRelationshipType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("replaces".equals(codeString))
          return new Enumeration<DocumentRelationshipType>(this, DocumentRelationshipType.REPLACES);
        if ("transforms".equals(codeString))
          return new Enumeration<DocumentRelationshipType>(this, DocumentRelationshipType.TRANSFORMS);
        if ("signs".equals(codeString))
          return new Enumeration<DocumentRelationshipType>(this, DocumentRelationshipType.SIGNS);
        if ("appends".equals(codeString))
          return new Enumeration<DocumentRelationshipType>(this, DocumentRelationshipType.APPENDS);
        throw new FHIRException("Unknown DocumentRelationshipType code '"+codeString+"'");
        }
    public String toCode(DocumentRelationshipType code) {
      if (code == DocumentRelationshipType.REPLACES)
        return "replaces";
      if (code == DocumentRelationshipType.TRANSFORMS)
        return "transforms";
      if (code == DocumentRelationshipType.SIGNS)
        return "signs";
      if (code == DocumentRelationshipType.APPENDS)
        return "appends";
      return "?";
      }
    public String toSystem(DocumentRelationshipType code) {
      return code.getSystem();
      }
    }

    public enum SectionMode {
        /**
         * This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes
         */
        WORKING, 
        /**
         * This list was prepared as a snapshot. It should not be assumed to be current
         */
        SNAPSHOT, 
        /**
         * A list that indicates where changes have been made or recommended
         */
        CHANGES, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SectionMode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("working".equals(codeString))
          return WORKING;
        if ("snapshot".equals(codeString))
          return SNAPSHOT;
        if ("changes".equals(codeString))
          return CHANGES;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SectionMode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case WORKING: return "working";
            case SNAPSHOT: return "snapshot";
            case CHANGES: return "changes";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case WORKING: return "http://hl7.org/fhir/list-mode";
            case SNAPSHOT: return "http://hl7.org/fhir/list-mode";
            case CHANGES: return "http://hl7.org/fhir/list-mode";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case WORKING: return "This list is the master list, maintained in an ongoing fashion with regular updates as the real world list it is tracking changes";
            case SNAPSHOT: return "This list was prepared as a snapshot. It should not be assumed to be current";
            case CHANGES: return "A list that indicates where changes have been made or recommended";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WORKING: return "Working List";
            case SNAPSHOT: return "Snapshot List";
            case CHANGES: return "Change List";
            default: return "?";
          }
        }
    }

  public static class SectionModeEnumFactory implements EnumFactory<SectionMode> {
    public SectionMode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("working".equals(codeString))
          return SectionMode.WORKING;
        if ("snapshot".equals(codeString))
          return SectionMode.SNAPSHOT;
        if ("changes".equals(codeString))
          return SectionMode.CHANGES;
        throw new IllegalArgumentException("Unknown SectionMode code '"+codeString+"'");
        }
        public Enumeration<SectionMode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SectionMode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("working".equals(codeString))
          return new Enumeration<SectionMode>(this, SectionMode.WORKING);
        if ("snapshot".equals(codeString))
          return new Enumeration<SectionMode>(this, SectionMode.SNAPSHOT);
        if ("changes".equals(codeString))
          return new Enumeration<SectionMode>(this, SectionMode.CHANGES);
        throw new FHIRException("Unknown SectionMode code '"+codeString+"'");
        }
    public String toCode(SectionMode code) {
      if (code == SectionMode.WORKING)
        return "working";
      if (code == SectionMode.SNAPSHOT)
        return "snapshot";
      if (code == SectionMode.CHANGES)
        return "changes";
      return "?";
      }
    public String toSystem(SectionMode code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CompositionAttesterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of attestation the authenticator offers.
         */
        @Child(name = "mode", type = {CodeType.class}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="personal | professional | legal | official", formalDefinition="The type of attestation the authenticator offers." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/composition-attestation-mode")
        protected List<Enumeration<CompositionAttestationMode>> mode;

        /**
         * When the composition was attested by the party.
         */
        @Child(name = "time", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="When the composition was attested", formalDefinition="When the composition was attested by the party." )
        protected DateTimeType time;

        /**
         * Who attested the composition in the specified way.
         */
        @Child(name = "party", type = {Patient.class, Practitioner.class, Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who attested the composition", formalDefinition="Who attested the composition in the specified way." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (Who attested the composition in the specified way.)
         */
        protected Resource partyTarget;

        private static final long serialVersionUID = -436604745L;

    /**
     * Constructor
     */
      public CompositionAttesterComponent() {
        super();
      }

        /**
         * @return {@link #mode} (The type of attestation the authenticator offers.)
         */
        public List<Enumeration<CompositionAttestationMode>> getMode() { 
          if (this.mode == null)
            this.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          return this.mode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CompositionAttesterComponent setMode(List<Enumeration<CompositionAttestationMode>> theMode) { 
          this.mode = theMode;
          return this;
        }

        public boolean hasMode() { 
          if (this.mode == null)
            return false;
          for (Enumeration<CompositionAttestationMode> item : this.mode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #mode} (The type of attestation the authenticator offers.)
         */
        public Enumeration<CompositionAttestationMode> addModeElement() {//2 
          Enumeration<CompositionAttestationMode> t = new Enumeration<CompositionAttestationMode>(new CompositionAttestationModeEnumFactory());
          if (this.mode == null)
            this.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          this.mode.add(t);
          return t;
        }

        /**
         * @param value {@link #mode} (The type of attestation the authenticator offers.)
         */
        public CompositionAttesterComponent addMode(CompositionAttestationMode value) { //1
          Enumeration<CompositionAttestationMode> t = new Enumeration<CompositionAttestationMode>(new CompositionAttestationModeEnumFactory());
          t.setValue(value);
          if (this.mode == null)
            this.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          this.mode.add(t);
          return this;
        }

        /**
         * @param value {@link #mode} (The type of attestation the authenticator offers.)
         */
        public boolean hasMode(CompositionAttestationMode value) { 
          if (this.mode == null)
            return false;
          for (Enumeration<CompositionAttestationMode> v : this.mode)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #time} (When the composition was attested by the party.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
         */
        public DateTimeType getTimeElement() { 
          if (this.time == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompositionAttesterComponent.time");
            else if (Configuration.doAutoCreate())
              this.time = new DateTimeType(); // bb
          return this.time;
        }

        public boolean hasTimeElement() { 
          return this.time != null && !this.time.isEmpty();
        }

        public boolean hasTime() { 
          return this.time != null && !this.time.isEmpty();
        }

        /**
         * @param value {@link #time} (When the composition was attested by the party.). This is the underlying object with id, value and extensions. The accessor "getTime" gives direct access to the value
         */
        public CompositionAttesterComponent setTimeElement(DateTimeType value) { 
          this.time = value;
          return this;
        }

        /**
         * @return When the composition was attested by the party.
         */
        public Date getTime() { 
          return this.time == null ? null : this.time.getValue();
        }

        /**
         * @param value When the composition was attested by the party.
         */
        public CompositionAttesterComponent setTime(Date value) { 
          if (value == null)
            this.time = null;
          else {
            if (this.time == null)
              this.time = new DateTimeType();
            this.time.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #party} (Who attested the composition in the specified way.)
         */
        public Reference getParty() { 
          if (this.party == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompositionAttesterComponent.party");
            else if (Configuration.doAutoCreate())
              this.party = new Reference(); // cc
          return this.party;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (Who attested the composition in the specified way.)
         */
        public CompositionAttesterComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who attested the composition in the specified way.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who attested the composition in the specified way.)
         */
        public CompositionAttesterComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("mode", "code", "The type of attestation the authenticator offers.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("time", "dateTime", "When the composition was attested by the party.", 0, java.lang.Integer.MAX_VALUE, time));
          childrenList.add(new Property("party", "Reference(Patient|Practitioner|Organization)", "Who attested the composition in the specified way.", 0, java.lang.Integer.MAX_VALUE, party));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : this.mode.toArray(new Base[this.mode.size()]); // Enumeration<CompositionAttestationMode>
        case 3560141: /*time*/ return this.time == null ? new Base[0] : new Base[] {this.time}; // DateTimeType
        case 106437350: /*party*/ return this.party == null ? new Base[0] : new Base[] {this.party}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3357091: // mode
          value = new CompositionAttestationModeEnumFactory().fromType(castToCode(value));
          this.getMode().add((Enumeration) value); // Enumeration<CompositionAttestationMode>
          return value;
        case 3560141: // time
          this.time = castToDateTime(value); // DateTimeType
          return value;
        case 106437350: // party
          this.party = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("mode")) {
          value = new CompositionAttestationModeEnumFactory().fromType(castToCode(value));
          this.getMode().add((Enumeration) value);
        } else if (name.equals("time")) {
          this.time = castToDateTime(value); // DateTimeType
        } else if (name.equals("party")) {
          this.party = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091:  return addModeElement();
        case 3560141:  return getTimeElement();
        case 106437350:  return getParty(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3357091: /*mode*/ return new String[] {"code"};
        case 3560141: /*time*/ return new String[] {"dateTime"};
        case 106437350: /*party*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.mode");
        }
        else if (name.equals("time")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.time");
        }
        else if (name.equals("party")) {
          this.party = new Reference();
          return this.party;
        }
        else
          return super.addChild(name);
      }

      public CompositionAttesterComponent copy() {
        CompositionAttesterComponent dst = new CompositionAttesterComponent();
        copyValues(dst);
        if (mode != null) {
          dst.mode = new ArrayList<Enumeration<CompositionAttestationMode>>();
          for (Enumeration<CompositionAttestationMode> i : mode)
            dst.mode.add(i.copy());
        };
        dst.time = time == null ? null : time.copy();
        dst.party = party == null ? null : party.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompositionAttesterComponent))
          return false;
        CompositionAttesterComponent o = (CompositionAttesterComponent) other;
        return compareDeep(mode, o.mode, true) && compareDeep(time, o.time, true) && compareDeep(party, o.party, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompositionAttesterComponent))
          return false;
        CompositionAttesterComponent o = (CompositionAttesterComponent) other;
        return compareValues(mode, o.mode, true) && compareValues(time, o.time, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(mode, time, party);
      }

  public String fhirType() {
    return "Composition.attester";

  }

  }

    @Block()
    public static class CompositionRelatesToComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of relationship that this composition has with anther composition or document.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="replaces | transforms | signs | appends", formalDefinition="The type of relationship that this composition has with anther composition or document." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/document-relationship-type")
        protected Enumeration<DocumentRelationshipType> code;

        /**
         * The target composition/document of this relationship.
         */
        @Child(name = "target", type = {Identifier.class, Composition.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Target of the relationship", formalDefinition="The target composition/document of this relationship." )
        protected Type target;

        private static final long serialVersionUID = 1536930280L;

    /**
     * Constructor
     */
      public CompositionRelatesToComponent() {
        super();
      }

    /**
     * Constructor
     */
      public CompositionRelatesToComponent(Enumeration<DocumentRelationshipType> code, Type target) {
        super();
        this.code = code;
        this.target = target;
      }

        /**
         * @return {@link #code} (The type of relationship that this composition has with anther composition or document.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<DocumentRelationshipType> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompositionRelatesToComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<DocumentRelationshipType>(new DocumentRelationshipTypeEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (The type of relationship that this composition has with anther composition or document.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public CompositionRelatesToComponent setCodeElement(Enumeration<DocumentRelationshipType> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return The type of relationship that this composition has with anther composition or document.
         */
        public DocumentRelationshipType getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value The type of relationship that this composition has with anther composition or document.
         */
        public CompositionRelatesToComponent setCode(DocumentRelationshipType value) { 
            if (this.code == null)
              this.code = new Enumeration<DocumentRelationshipType>(new DocumentRelationshipTypeEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #target} (The target composition/document of this relationship.)
         */
        public Type getTarget() { 
          return this.target;
        }

        /**
         * @return {@link #target} (The target composition/document of this relationship.)
         */
        public Identifier getTargetIdentifier() throws FHIRException { 
          if (!(this.target instanceof Identifier))
            throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.target.getClass().getName()+" was encountered");
          return (Identifier) this.target;
        }

        public boolean hasTargetIdentifier() { 
          return this.target instanceof Identifier;
        }

        /**
         * @return {@link #target} (The target composition/document of this relationship.)
         */
        public Reference getTargetReference() throws FHIRException { 
          if (!(this.target instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.target.getClass().getName()+" was encountered");
          return (Reference) this.target;
        }

        public boolean hasTargetReference() { 
          return this.target instanceof Reference;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (The target composition/document of this relationship.)
         */
        public CompositionRelatesToComponent setTarget(Type value) { 
          this.target = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "The type of relationship that this composition has with anther composition or document.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("target[x]", "Identifier|Reference(Composition)", "The target composition/document of this relationship.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<DocumentRelationshipType>
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          value = new DocumentRelationshipTypeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<DocumentRelationshipType>
          return value;
        case -880905839: // target
          this.target = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          value = new DocumentRelationshipTypeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<DocumentRelationshipType>
        } else if (name.equals("target[x]")) {
          this.target = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case -815579825:  return getTarget(); 
        case -880905839:  return getTarget(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case -880905839: /*target*/ return new String[] {"Identifier", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.code");
        }
        else if (name.equals("targetIdentifier")) {
          this.target = new Identifier();
          return this.target;
        }
        else if (name.equals("targetReference")) {
          this.target = new Reference();
          return this.target;
        }
        else
          return super.addChild(name);
      }

      public CompositionRelatesToComponent copy() {
        CompositionRelatesToComponent dst = new CompositionRelatesToComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompositionRelatesToComponent))
          return false;
        CompositionRelatesToComponent o = (CompositionRelatesToComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompositionRelatesToComponent))
          return false;
        CompositionRelatesToComponent o = (CompositionRelatesToComponent) other;
        return compareValues(code, o.code, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, target);
      }

  public String fhirType() {
    return "Composition.relatesTo";

  }

  }

    @Block()
    public static class CompositionEventComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Code(s) that apply to the event being documented", formalDefinition="This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActCode")
        protected List<CodeableConcept> code;

        /**
         * The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The period covered by the documentation", formalDefinition="The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time." )
        protected Period period;

        /**
         * The description and/or reference of the event(s) being documented. For example, this could be used to document such a colonoscopy or an appendectomy.
         */
        @Child(name = "detail", type = {Reference.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The event(s) being documented", formalDefinition="The description and/or reference of the event(s) being documented. For example, this could be used to document such a colonoscopy or an appendectomy." )
        protected List<Reference> detail;
        /**
         * The actual objects that are the target of the reference (The description and/or reference of the event(s) being documented. For example, this could be used to document such a colonoscopy or an appendectomy.)
         */
        protected List<Resource> detailTarget;


        private static final long serialVersionUID = -1581379774L;

    /**
     * Constructor
     */
      public CompositionEventComponent() {
        super();
      }

        /**
         * @return {@link #code} (This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a "History and Physical Report" in which the procedure being documented is necessarily a "History and Physical" act.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CompositionEventComponent setCode(List<CodeableConcept> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (CodeableConcept item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return t;
        }

        public CompositionEventComponent addCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public CodeableConcept getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        /**
         * @return {@link #period} (The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CompositionEventComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.)
         */
        public CompositionEventComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #detail} (The description and/or reference of the event(s) being documented. For example, this could be used to document such a colonoscopy or an appendectomy.)
         */
        public List<Reference> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public CompositionEventComponent setDetail(List<Reference> theDetail) { 
          this.detail = theDetail;
          return this;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (Reference item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addDetail() { //3
          Reference t = new Reference();
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return t;
        }

        public CompositionEventComponent addDetail(Reference t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
         */
        public Reference getDetailFirstRep() { 
          if (getDetail().isEmpty()) {
            addDetail();
          }
          return getDetail().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getDetailTarget() { 
          if (this.detailTarget == null)
            this.detailTarget = new ArrayList<Resource>();
          return this.detailTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "This list of codes represents the main clinical acts, such as a colonoscopy or an appendectomy, being documented. In some cases, the event is inherent in the typeCode, such as a \"History and Physical Report\" in which the procedure being documented is necessarily a \"History and Physical\" act.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("period", "Period", "The period of time covered by the documentation. There is no assertion that the documentation is a complete representation for this period, only that it documents events during this time.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("detail", "Reference(Any)", "The description and/or reference of the event(s) being documented. For example, this could be used to document such a colonoscopy or an appendectomy.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -1335224239: // detail
          this.getDetail().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("detail")) {
          this.getDetail().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return addCode(); 
        case -991726143:  return getPeriod(); 
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -1335224239: /*detail*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("detail")) {
          return addDetail();
        }
        else
          return super.addChild(name);
      }

      public CompositionEventComponent copy() {
        CompositionEventComponent dst = new CompositionEventComponent();
        copyValues(dst);
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        if (detail != null) {
          dst.detail = new ArrayList<Reference>();
          for (Reference i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CompositionEventComponent))
          return false;
        CompositionEventComponent o = (CompositionEventComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(period, o.period, true) && compareDeep(detail, o.detail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CompositionEventComponent))
          return false;
        CompositionEventComponent o = (CompositionEventComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, period, detail);
      }

  public String fhirType() {
    return "Composition.event";

  }

  }

    @Block()
    public static class SectionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.
         */
        @Child(name = "title", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Label for section (e.g. for ToC)", formalDefinition="The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents." )
        protected StringType title;

        /**
         * A code identifying the kind of content contained within the section. This must be consistent with the section title.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Classification of section (recommended)", formalDefinition="A code identifying the kind of content contained within the section. This must be consistent with the section title." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/doc-section-codes")
        protected CodeableConcept code;

        /**
         * A human-readable narrative that contains the attested content of the section, used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it "clinically safe" for a human to just read the narrative.
         */
        @Child(name = "text", type = {Narrative.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Text summary of the section, for human interpretation", formalDefinition="A human-readable narrative that contains the attested content of the section, used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it \"clinically safe\" for a human to just read the narrative." )
        protected Narrative text;

        /**
         * How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
         */
        @Child(name = "mode", type = {CodeType.class}, order=4, min=0, max=1, modifier=true, summary=true)
        @Description(shortDefinition="working | snapshot | changes", formalDefinition="How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/list-mode")
        protected Enumeration<SectionMode> mode;

        /**
         * Specifies the order applied to the items in the section entries.
         */
        @Child(name = "orderedBy", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Order of section entries", formalDefinition="Specifies the order applied to the items in the section entries." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/list-order")
        protected CodeableConcept orderedBy;

        /**
         * A reference to the actual resource from which the narrative in the section is derived.
         */
        @Child(name = "entry", type = {Reference.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="A reference to data that supports this section", formalDefinition="A reference to the actual resource from which the narrative in the section is derived." )
        protected List<Reference> entry;
        /**
         * The actual objects that are the target of the reference (A reference to the actual resource from which the narrative in the section is derived.)
         */
        protected List<Resource> entryTarget;


        /**
         * If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.
         */
        @Child(name = "emptyReason", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Why the section is empty", formalDefinition="If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/list-empty-reason")
        protected CodeableConcept emptyReason;

        /**
         * A nested sub-section within this section.
         */
        @Child(name = "section", type = {SectionComponent.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Section", formalDefinition="A nested sub-section within this section." )
        protected List<SectionComponent> section;

        private static final long serialVersionUID = -128426142L;

    /**
     * Constructor
     */
      public SectionComponent() {
        super();
      }

        /**
         * @return {@link #title} (The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public StringType getTitleElement() { 
          if (this.title == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.title");
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
         * @param value {@link #title} (The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
         */
        public SectionComponent setTitleElement(StringType value) { 
          this.title = value;
          return this;
        }

        /**
         * @return The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.
         */
        public String getTitle() { 
          return this.title == null ? null : this.title.getValue();
        }

        /**
         * @param value The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.
         */
        public SectionComponent setTitle(String value) { 
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
         * @return {@link #code} (A code identifying the kind of content contained within the section. This must be consistent with the section title.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code identifying the kind of content contained within the section. This must be consistent with the section title.)
         */
        public SectionComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #text} (A human-readable narrative that contains the attested content of the section, used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it "clinically safe" for a human to just read the narrative.)
         */
        public Narrative getText() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new Narrative(); // cc
          return this.text;
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (A human-readable narrative that contains the attested content of the section, used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it "clinically safe" for a human to just read the narrative.)
         */
        public SectionComponent setText(Narrative value) { 
          this.text = value;
          return this;
        }

        /**
         * @return {@link #mode} (How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public Enumeration<SectionMode> getModeElement() { 
          if (this.mode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.mode");
            else if (Configuration.doAutoCreate())
              this.mode = new Enumeration<SectionMode>(new SectionModeEnumFactory()); // bb
          return this.mode;
        }

        public boolean hasModeElement() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        public boolean hasMode() { 
          return this.mode != null && !this.mode.isEmpty();
        }

        /**
         * @param value {@link #mode} (How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.). This is the underlying object with id, value and extensions. The accessor "getMode" gives direct access to the value
         */
        public SectionComponent setModeElement(Enumeration<SectionMode> value) { 
          this.mode = value;
          return this;
        }

        /**
         * @return How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
         */
        public SectionMode getMode() { 
          return this.mode == null ? null : this.mode.getValue();
        }

        /**
         * @param value How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.
         */
        public SectionComponent setMode(SectionMode value) { 
          if (value == null)
            this.mode = null;
          else {
            if (this.mode == null)
              this.mode = new Enumeration<SectionMode>(new SectionModeEnumFactory());
            this.mode.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #orderedBy} (Specifies the order applied to the items in the section entries.)
         */
        public CodeableConcept getOrderedBy() { 
          if (this.orderedBy == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.orderedBy");
            else if (Configuration.doAutoCreate())
              this.orderedBy = new CodeableConcept(); // cc
          return this.orderedBy;
        }

        public boolean hasOrderedBy() { 
          return this.orderedBy != null && !this.orderedBy.isEmpty();
        }

        /**
         * @param value {@link #orderedBy} (Specifies the order applied to the items in the section entries.)
         */
        public SectionComponent setOrderedBy(CodeableConcept value) { 
          this.orderedBy = value;
          return this;
        }

        /**
         * @return {@link #entry} (A reference to the actual resource from which the narrative in the section is derived.)
         */
        public List<Reference> getEntry() { 
          if (this.entry == null)
            this.entry = new ArrayList<Reference>();
          return this.entry;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SectionComponent setEntry(List<Reference> theEntry) { 
          this.entry = theEntry;
          return this;
        }

        public boolean hasEntry() { 
          if (this.entry == null)
            return false;
          for (Reference item : this.entry)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addEntry() { //3
          Reference t = new Reference();
          if (this.entry == null)
            this.entry = new ArrayList<Reference>();
          this.entry.add(t);
          return t;
        }

        public SectionComponent addEntry(Reference t) { //3
          if (t == null)
            return this;
          if (this.entry == null)
            this.entry = new ArrayList<Reference>();
          this.entry.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #entry}, creating it if it does not already exist
         */
        public Reference getEntryFirstRep() { 
          if (getEntry().isEmpty()) {
            addEntry();
          }
          return getEntry().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getEntryTarget() { 
          if (this.entryTarget == null)
            this.entryTarget = new ArrayList<Resource>();
          return this.entryTarget;
        }

        /**
         * @return {@link #emptyReason} (If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.)
         */
        public CodeableConcept getEmptyReason() { 
          if (this.emptyReason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SectionComponent.emptyReason");
            else if (Configuration.doAutoCreate())
              this.emptyReason = new CodeableConcept(); // cc
          return this.emptyReason;
        }

        public boolean hasEmptyReason() { 
          return this.emptyReason != null && !this.emptyReason.isEmpty();
        }

        /**
         * @param value {@link #emptyReason} (If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.)
         */
        public SectionComponent setEmptyReason(CodeableConcept value) { 
          this.emptyReason = value;
          return this;
        }

        /**
         * @return {@link #section} (A nested sub-section within this section.)
         */
        public List<SectionComponent> getSection() { 
          if (this.section == null)
            this.section = new ArrayList<SectionComponent>();
          return this.section;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SectionComponent setSection(List<SectionComponent> theSection) { 
          this.section = theSection;
          return this;
        }

        public boolean hasSection() { 
          if (this.section == null)
            return false;
          for (SectionComponent item : this.section)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SectionComponent addSection() { //3
          SectionComponent t = new SectionComponent();
          if (this.section == null)
            this.section = new ArrayList<SectionComponent>();
          this.section.add(t);
          return t;
        }

        public SectionComponent addSection(SectionComponent t) { //3
          if (t == null)
            return this;
          if (this.section == null)
            this.section = new ArrayList<SectionComponent>();
          this.section.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #section}, creating it if it does not already exist
         */
        public SectionComponent getSectionFirstRep() { 
          if (getSection().isEmpty()) {
            addSection();
          }
          return getSection().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("title", "string", "The label for this particular section.  This will be part of the rendered content for the document, and is often used to build a table of contents.", 0, java.lang.Integer.MAX_VALUE, title));
          childrenList.add(new Property("code", "CodeableConcept", "A code identifying the kind of content contained within the section. This must be consistent with the section title.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("text", "Narrative", "A human-readable narrative that contains the attested content of the section, used to represent the content of the resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient detail to make it \"clinically safe\" for a human to just read the narrative.", 0, java.lang.Integer.MAX_VALUE, text));
          childrenList.add(new Property("mode", "code", "How the entry list was prepared - whether it is a working list that is suitable for being maintained on an ongoing basis, or if it represents a snapshot of a list of items from another source, or whether it is a prepared list where items may be marked as added, modified or deleted.", 0, java.lang.Integer.MAX_VALUE, mode));
          childrenList.add(new Property("orderedBy", "CodeableConcept", "Specifies the order applied to the items in the section entries.", 0, java.lang.Integer.MAX_VALUE, orderedBy));
          childrenList.add(new Property("entry", "Reference(Any)", "A reference to the actual resource from which the narrative in the section is derived.", 0, java.lang.Integer.MAX_VALUE, entry));
          childrenList.add(new Property("emptyReason", "CodeableConcept", "If the section is empty, why the list is empty. An empty section typically has some text explaining the empty reason.", 0, java.lang.Integer.MAX_VALUE, emptyReason));
          childrenList.add(new Property("section", "@Composition.section", "A nested sub-section within this section.", 0, java.lang.Integer.MAX_VALUE, section));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // Narrative
        case 3357091: /*mode*/ return this.mode == null ? new Base[0] : new Base[] {this.mode}; // Enumeration<SectionMode>
        case -391079516: /*orderedBy*/ return this.orderedBy == null ? new Base[0] : new Base[] {this.orderedBy}; // CodeableConcept
        case 96667762: /*entry*/ return this.entry == null ? new Base[0] : this.entry.toArray(new Base[this.entry.size()]); // Reference
        case 1140135409: /*emptyReason*/ return this.emptyReason == null ? new Base[0] : new Base[] {this.emptyReason}; // CodeableConcept
        case 1970241253: /*section*/ return this.section == null ? new Base[0] : this.section.toArray(new Base[this.section.size()]); // SectionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3556653: // text
          this.text = castToNarrative(value); // Narrative
          return value;
        case 3357091: // mode
          value = new SectionModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<SectionMode>
          return value;
        case -391079516: // orderedBy
          this.orderedBy = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 96667762: // entry
          this.getEntry().add(castToReference(value)); // Reference
          return value;
        case 1140135409: // emptyReason
          this.emptyReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1970241253: // section
          this.getSection().add((SectionComponent) value); // SectionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("text")) {
          this.text = castToNarrative(value); // Narrative
        } else if (name.equals("mode")) {
          value = new SectionModeEnumFactory().fromType(castToCode(value));
          this.mode = (Enumeration) value; // Enumeration<SectionMode>
        } else if (name.equals("orderedBy")) {
          this.orderedBy = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("entry")) {
          this.getEntry().add(castToReference(value));
        } else if (name.equals("emptyReason")) {
          this.emptyReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("section")) {
          this.getSection().add((SectionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 110371416:  return getTitleElement();
        case 3059181:  return getCode(); 
        case 3556653:  return getText(); 
        case 3357091:  return getModeElement();
        case -391079516:  return getOrderedBy(); 
        case 96667762:  return addEntry(); 
        case 1140135409:  return getEmptyReason(); 
        case 1970241253:  return addSection(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 110371416: /*title*/ return new String[] {"string"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 3556653: /*text*/ return new String[] {"Narrative"};
        case 3357091: /*mode*/ return new String[] {"code"};
        case -391079516: /*orderedBy*/ return new String[] {"CodeableConcept"};
        case 96667762: /*entry*/ return new String[] {"Reference"};
        case 1140135409: /*emptyReason*/ return new String[] {"CodeableConcept"};
        case 1970241253: /*section*/ return new String[] {"@Composition.section"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.title");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("text")) {
          this.text = new Narrative();
          return this.text;
        }
        else if (name.equals("mode")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.mode");
        }
        else if (name.equals("orderedBy")) {
          this.orderedBy = new CodeableConcept();
          return this.orderedBy;
        }
        else if (name.equals("entry")) {
          return addEntry();
        }
        else if (name.equals("emptyReason")) {
          this.emptyReason = new CodeableConcept();
          return this.emptyReason;
        }
        else if (name.equals("section")) {
          return addSection();
        }
        else
          return super.addChild(name);
      }

      public SectionComponent copy() {
        SectionComponent dst = new SectionComponent();
        copyValues(dst);
        dst.title = title == null ? null : title.copy();
        dst.code = code == null ? null : code.copy();
        dst.text = text == null ? null : text.copy();
        dst.mode = mode == null ? null : mode.copy();
        dst.orderedBy = orderedBy == null ? null : orderedBy.copy();
        if (entry != null) {
          dst.entry = new ArrayList<Reference>();
          for (Reference i : entry)
            dst.entry.add(i.copy());
        };
        dst.emptyReason = emptyReason == null ? null : emptyReason.copy();
        if (section != null) {
          dst.section = new ArrayList<SectionComponent>();
          for (SectionComponent i : section)
            dst.section.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SectionComponent))
          return false;
        SectionComponent o = (SectionComponent) other;
        return compareDeep(title, o.title, true) && compareDeep(code, o.code, true) && compareDeep(text, o.text, true)
           && compareDeep(mode, o.mode, true) && compareDeep(orderedBy, o.orderedBy, true) && compareDeep(entry, o.entry, true)
           && compareDeep(emptyReason, o.emptyReason, true) && compareDeep(section, o.section, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SectionComponent))
          return false;
        SectionComponent o = (SectionComponent) other;
        return compareValues(title, o.title, true) && compareValues(mode, o.mode, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(title, code, text, mode
          , orderedBy, entry, emptyReason, section);
      }

  public String fhirType() {
    return "Composition.section";

  }

  }

    /**
     * Logical identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier of composition (version-independent)", formalDefinition="Logical identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time." )
    protected Identifier identifier;

    /**
     * The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="preliminary | final | amended | entered-in-error", formalDefinition="The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/composition-status")
    protected Enumeration<CompositionStatus> status;

    /**
     * Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Kind of composition (LOINC if possible)", formalDefinition="Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/doc-typecodes")
    protected CodeableConcept type;

    /**
     * A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived from the code specified in the Composition Type.
     */
    @Child(name = "class", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Categorization of Composition", formalDefinition="A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived from the code specified in the Composition Type." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/doc-classcodes")
    protected CodeableConcept class_;

    /**
     * Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).
     */
    @Child(name = "subject", type = {Reference.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what the composition is about", formalDefinition="Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    protected Resource subjectTarget;

    /**
     * Describes the clinical encounter or type of care this documentation is associated with.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Context of the Composition", formalDefinition="Describes the clinical encounter or type of care this documentation is associated with." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    protected Encounter encounterTarget;

    /**
     * The composition editing time, when the composition was last logically changed by the author.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Composition editing time", formalDefinition="The composition editing time, when the composition was last logically changed by the author." )
    protected DateTimeType date;

    /**
     * Identifies who is responsible for the information in the composition, not necessarily who typed it in.
     */
    @Child(name = "author", type = {Practitioner.class, Device.class, Patient.class, RelatedPerson.class}, order=7, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what authored the composition", formalDefinition="Identifies who is responsible for the information in the composition, not necessarily who typed it in." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies who is responsible for the information in the composition, not necessarily who typed it in.)
     */
    protected List<Resource> authorTarget;


    /**
     * Official human-readable label for the composition.
     */
    @Child(name = "title", type = {StringType.class}, order=8, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human Readable name/title", formalDefinition="Official human-readable label for the composition." )
    protected StringType title;

    /**
     * The code specifying the level of confidentiality of the Composition.
     */
    @Child(name = "confidentiality", type = {CodeType.class}, order=9, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="As defined by affinity domain", formalDefinition="The code specifying the level of confidentiality of the Composition." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ConfidentialityClassification")
    protected Enumeration<DocumentConfidentiality> confidentiality;

    /**
     * A participant who has attested to the accuracy of the composition/document.
     */
    @Child(name = "attester", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Attests to accuracy of composition", formalDefinition="A participant who has attested to the accuracy of the composition/document." )
    protected List<CompositionAttesterComponent> attester;

    /**
     * Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.
     */
    @Child(name = "custodian", type = {Organization.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization which maintains the composition", formalDefinition="Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information." )
    protected Reference custodian;

    /**
     * The actual object that is the target of the reference (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    protected Organization custodianTarget;

    /**
     * Relationships that this composition has with other compositions or documents that already exist.
     */
    @Child(name = "relatesTo", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Relationships to other compositions/documents", formalDefinition="Relationships that this composition has with other compositions or documents that already exist." )
    protected List<CompositionRelatesToComponent> relatesTo;

    /**
     * The clinical service, such as a colonoscopy or an appendectomy, being documented.
     */
    @Child(name = "event", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The clinical service(s) being documented", formalDefinition="The clinical service, such as a colonoscopy or an appendectomy, being documented." )
    protected List<CompositionEventComponent> event;

    /**
     * The root of the sections that make up the composition.
     */
    @Child(name = "section", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Composition is broken into sections", formalDefinition="The root of the sections that make up the composition." )
    protected List<SectionComponent> section;

    private static final long serialVersionUID = -1422555114L;

  /**
   * Constructor
   */
    public Composition() {
      super();
    }

  /**
   * Constructor
   */
    public Composition(Enumeration<CompositionStatus> status, CodeableConcept type, Reference subject, DateTimeType date, StringType title) {
      super();
      this.status = status;
      this.type = type;
      this.subject = subject;
      this.date = date;
      this.title = title;
    }

    /**
     * @return {@link #identifier} (Logical identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Logical identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.)
     */
    public Composition setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CompositionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CompositionStatus>(new CompositionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Composition setStatusElement(Enumeration<CompositionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     */
    public CompositionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.
     */
    public Composition setStatus(CompositionStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<CompositionStatus>(new CompositionStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.)
     */
    public Composition setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #class_} (A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived from the code specified in the Composition Type.)
     */
    public CodeableConcept getClass_() { 
      if (this.class_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.class_");
        else if (Configuration.doAutoCreate())
          this.class_ = new CodeableConcept(); // cc
      return this.class_;
    }

    public boolean hasClass_() { 
      return this.class_ != null && !this.class_.isEmpty();
    }

    /**
     * @param value {@link #class_} (A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived from the code specified in the Composition Type.)
     */
    public Composition setClass_(CodeableConcept value) { 
      this.class_ = value;
      return this;
    }

    /**
     * @return {@link #subject} (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Composition setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).)
     */
    public Composition setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Composition setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Describes the clinical encounter or type of care this documentation is associated with.)
     */
    public Composition setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The composition editing time, when the composition was last logically changed by the author.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.date");
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
     * @param value {@link #date} (The composition editing time, when the composition was last logically changed by the author.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Composition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The composition editing time, when the composition was last logically changed by the author.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The composition editing time, when the composition was last logically changed by the author.
     */
    public Composition setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      return this;
    }

    /**
     * @return {@link #author} (Identifies who is responsible for the information in the composition, not necessarily who typed it in.)
     */
    public List<Reference> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      return this.author;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Composition setAuthor(List<Reference> theAuthor) { 
      this.author = theAuthor;
      return this;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    public Composition addAuthor(Reference t) { //3
      if (t == null)
        return this;
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #author}, creating it if it does not already exist
     */
    public Reference getAuthorFirstRep() { 
      if (getAuthor().isEmpty()) {
        addAuthor();
      }
      return getAuthor().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #title} (Official human-readable label for the composition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.title");
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
     * @param value {@link #title} (Official human-readable label for the composition.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Composition setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return Official human-readable label for the composition.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value Official human-readable label for the composition.
     */
    public Composition setTitle(String value) { 
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      return this;
    }

    /**
     * @return {@link #confidentiality} (The code specifying the level of confidentiality of the Composition.). This is the underlying object with id, value and extensions. The accessor "getConfidentiality" gives direct access to the value
     */
    public Enumeration<DocumentConfidentiality> getConfidentialityElement() { 
      if (this.confidentiality == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.confidentiality");
        else if (Configuration.doAutoCreate())
          this.confidentiality = new Enumeration<DocumentConfidentiality>(new DocumentConfidentialityEnumFactory()); // bb
      return this.confidentiality;
    }

    public boolean hasConfidentialityElement() { 
      return this.confidentiality != null && !this.confidentiality.isEmpty();
    }

    public boolean hasConfidentiality() { 
      return this.confidentiality != null && !this.confidentiality.isEmpty();
    }

    /**
     * @param value {@link #confidentiality} (The code specifying the level of confidentiality of the Composition.). This is the underlying object with id, value and extensions. The accessor "getConfidentiality" gives direct access to the value
     */
    public Composition setConfidentialityElement(Enumeration<DocumentConfidentiality> value) { 
      this.confidentiality = value;
      return this;
    }

    /**
     * @return The code specifying the level of confidentiality of the Composition.
     */
    public DocumentConfidentiality getConfidentiality() { 
      return this.confidentiality == null ? null : this.confidentiality.getValue();
    }

    /**
     * @param value The code specifying the level of confidentiality of the Composition.
     */
    public Composition setConfidentiality(DocumentConfidentiality value) { 
      if (value == null)
        this.confidentiality = null;
      else {
        if (this.confidentiality == null)
          this.confidentiality = new Enumeration<DocumentConfidentiality>(new DocumentConfidentialityEnumFactory());
        this.confidentiality.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #attester} (A participant who has attested to the accuracy of the composition/document.)
     */
    public List<CompositionAttesterComponent> getAttester() { 
      if (this.attester == null)
        this.attester = new ArrayList<CompositionAttesterComponent>();
      return this.attester;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Composition setAttester(List<CompositionAttesterComponent> theAttester) { 
      this.attester = theAttester;
      return this;
    }

    public boolean hasAttester() { 
      if (this.attester == null)
        return false;
      for (CompositionAttesterComponent item : this.attester)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CompositionAttesterComponent addAttester() { //3
      CompositionAttesterComponent t = new CompositionAttesterComponent();
      if (this.attester == null)
        this.attester = new ArrayList<CompositionAttesterComponent>();
      this.attester.add(t);
      return t;
    }

    public Composition addAttester(CompositionAttesterComponent t) { //3
      if (t == null)
        return this;
      if (this.attester == null)
        this.attester = new ArrayList<CompositionAttesterComponent>();
      this.attester.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #attester}, creating it if it does not already exist
     */
    public CompositionAttesterComponent getAttesterFirstRep() { 
      if (getAttester().isEmpty()) {
        addAttester();
      }
      return getAttester().get(0);
    }

    /**
     * @return {@link #custodian} (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Reference getCustodian() { 
      if (this.custodian == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.custodian");
        else if (Configuration.doAutoCreate())
          this.custodian = new Reference(); // cc
      return this.custodian;
    }

    public boolean hasCustodian() { 
      return this.custodian != null && !this.custodian.isEmpty();
    }

    /**
     * @param value {@link #custodian} (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Composition setCustodian(Reference value) { 
      this.custodian = value;
      return this;
    }

    /**
     * @return {@link #custodian} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Organization getCustodianTarget() { 
      if (this.custodianTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Composition.custodian");
        else if (Configuration.doAutoCreate())
          this.custodianTarget = new Organization(); // aa
      return this.custodianTarget;
    }

    /**
     * @param value {@link #custodian} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.)
     */
    public Composition setCustodianTarget(Organization value) { 
      this.custodianTarget = value;
      return this;
    }

    /**
     * @return {@link #relatesTo} (Relationships that this composition has with other compositions or documents that already exist.)
     */
    public List<CompositionRelatesToComponent> getRelatesTo() { 
      if (this.relatesTo == null)
        this.relatesTo = new ArrayList<CompositionRelatesToComponent>();
      return this.relatesTo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Composition setRelatesTo(List<CompositionRelatesToComponent> theRelatesTo) { 
      this.relatesTo = theRelatesTo;
      return this;
    }

    public boolean hasRelatesTo() { 
      if (this.relatesTo == null)
        return false;
      for (CompositionRelatesToComponent item : this.relatesTo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CompositionRelatesToComponent addRelatesTo() { //3
      CompositionRelatesToComponent t = new CompositionRelatesToComponent();
      if (this.relatesTo == null)
        this.relatesTo = new ArrayList<CompositionRelatesToComponent>();
      this.relatesTo.add(t);
      return t;
    }

    public Composition addRelatesTo(CompositionRelatesToComponent t) { //3
      if (t == null)
        return this;
      if (this.relatesTo == null)
        this.relatesTo = new ArrayList<CompositionRelatesToComponent>();
      this.relatesTo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatesTo}, creating it if it does not already exist
     */
    public CompositionRelatesToComponent getRelatesToFirstRep() { 
      if (getRelatesTo().isEmpty()) {
        addRelatesTo();
      }
      return getRelatesTo().get(0);
    }

    /**
     * @return {@link #event} (The clinical service, such as a colonoscopy or an appendectomy, being documented.)
     */
    public List<CompositionEventComponent> getEvent() { 
      if (this.event == null)
        this.event = new ArrayList<CompositionEventComponent>();
      return this.event;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Composition setEvent(List<CompositionEventComponent> theEvent) { 
      this.event = theEvent;
      return this;
    }

    public boolean hasEvent() { 
      if (this.event == null)
        return false;
      for (CompositionEventComponent item : this.event)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CompositionEventComponent addEvent() { //3
      CompositionEventComponent t = new CompositionEventComponent();
      if (this.event == null)
        this.event = new ArrayList<CompositionEventComponent>();
      this.event.add(t);
      return t;
    }

    public Composition addEvent(CompositionEventComponent t) { //3
      if (t == null)
        return this;
      if (this.event == null)
        this.event = new ArrayList<CompositionEventComponent>();
      this.event.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #event}, creating it if it does not already exist
     */
    public CompositionEventComponent getEventFirstRep() { 
      if (getEvent().isEmpty()) {
        addEvent();
      }
      return getEvent().get(0);
    }

    /**
     * @return {@link #section} (The root of the sections that make up the composition.)
     */
    public List<SectionComponent> getSection() { 
      if (this.section == null)
        this.section = new ArrayList<SectionComponent>();
      return this.section;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Composition setSection(List<SectionComponent> theSection) { 
      this.section = theSection;
      return this;
    }

    public boolean hasSection() { 
      if (this.section == null)
        return false;
      for (SectionComponent item : this.section)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SectionComponent addSection() { //3
      SectionComponent t = new SectionComponent();
      if (this.section == null)
        this.section = new ArrayList<SectionComponent>();
      this.section.add(t);
      return t;
    }

    public Composition addSection(SectionComponent t) { //3
      if (t == null)
        return this;
      if (this.section == null)
        this.section = new ArrayList<SectionComponent>();
      this.section.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #section}, creating it if it does not already exist
     */
    public SectionComponent getSectionFirstRep() { 
      if (getSection().isEmpty()) {
        addSection();
      }
      return getSection().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Logical identifier for the composition, assigned when created. This identifier stays constant as the composition is changed over time.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The workflow/clinical status of this composition. The status is a marker for the clinical standing of the document.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "Specifies the particular kind of composition (e.g. History and Physical, Discharge Summary, Progress Note). This usually equates to the purpose of making the composition.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("class", "CodeableConcept", "A categorization for the type of the composition - helps for indexing and searching. This may be implied by or derived from the code specified in the Composition Type.", 0, java.lang.Integer.MAX_VALUE, class_));
        childrenList.add(new Property("subject", "Reference(Any)", "Who or what the composition is about. The composition can be about a person, (patient or healthcare practitioner), a device (e.g. a machine) or even a group of subjects (such as a document about a herd of livestock, or a set of patients that share a common exposure).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "Describes the clinical encounter or type of care this documentation is associated with.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("date", "dateTime", "The composition editing time, when the composition was last logically changed by the author.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("author", "Reference(Practitioner|Device|Patient|RelatedPerson)", "Identifies who is responsible for the information in the composition, not necessarily who typed it in.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("title", "string", "Official human-readable label for the composition.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("confidentiality", "code", "The code specifying the level of confidentiality of the Composition.", 0, java.lang.Integer.MAX_VALUE, confidentiality));
        childrenList.add(new Property("attester", "", "A participant who has attested to the accuracy of the composition/document.", 0, java.lang.Integer.MAX_VALUE, attester));
        childrenList.add(new Property("custodian", "Reference(Organization)", "Identifies the organization or group who is responsible for ongoing maintenance of and access to the composition/document information.", 0, java.lang.Integer.MAX_VALUE, custodian));
        childrenList.add(new Property("relatesTo", "", "Relationships that this composition has with other compositions or documents that already exist.", 0, java.lang.Integer.MAX_VALUE, relatesTo));
        childrenList.add(new Property("event", "", "The clinical service, such as a colonoscopy or an appendectomy, being documented.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("section", "", "The root of the sections that make up the composition.", 0, java.lang.Integer.MAX_VALUE, section));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<CompositionStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : new Base[] {this.class_}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : this.author.toArray(new Base[this.author.size()]); // Reference
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -1923018202: /*confidentiality*/ return this.confidentiality == null ? new Base[0] : new Base[] {this.confidentiality}; // Enumeration<DocumentConfidentiality>
        case 542920370: /*attester*/ return this.attester == null ? new Base[0] : this.attester.toArray(new Base[this.attester.size()]); // CompositionAttesterComponent
        case 1611297262: /*custodian*/ return this.custodian == null ? new Base[0] : new Base[] {this.custodian}; // Reference
        case -7765931: /*relatesTo*/ return this.relatesTo == null ? new Base[0] : this.relatesTo.toArray(new Base[this.relatesTo.size()]); // CompositionRelatesToComponent
        case 96891546: /*event*/ return this.event == null ? new Base[0] : this.event.toArray(new Base[this.event.size()]); // CompositionEventComponent
        case 1970241253: /*section*/ return this.section == null ? new Base[0] : this.section.toArray(new Base[this.section.size()]); // SectionComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -892481550: // status
          value = new CompositionStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CompositionStatus>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 94742904: // class
          this.class_ = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -1406328437: // author
          this.getAuthor().add(castToReference(value)); // Reference
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -1923018202: // confidentiality
          value = new DocumentConfidentialityEnumFactory().fromType(castToCode(value));
          this.confidentiality = (Enumeration) value; // Enumeration<DocumentConfidentiality>
          return value;
        case 542920370: // attester
          this.getAttester().add((CompositionAttesterComponent) value); // CompositionAttesterComponent
          return value;
        case 1611297262: // custodian
          this.custodian = castToReference(value); // Reference
          return value;
        case -7765931: // relatesTo
          this.getRelatesTo().add((CompositionRelatesToComponent) value); // CompositionRelatesToComponent
          return value;
        case 96891546: // event
          this.getEvent().add((CompositionEventComponent) value); // CompositionEventComponent
          return value;
        case 1970241253: // section
          this.getSection().add((SectionComponent) value); // SectionComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("status")) {
          value = new CompositionStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CompositionStatus>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("class")) {
          this.class_ = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("encounter")) {
          this.encounter = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("author")) {
          this.getAuthor().add(castToReference(value));
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("confidentiality")) {
          value = new DocumentConfidentialityEnumFactory().fromType(castToCode(value));
          this.confidentiality = (Enumeration) value; // Enumeration<DocumentConfidentiality>
        } else if (name.equals("attester")) {
          this.getAttester().add((CompositionAttesterComponent) value);
        } else if (name.equals("custodian")) {
          this.custodian = castToReference(value); // Reference
        } else if (name.equals("relatesTo")) {
          this.getRelatesTo().add((CompositionRelatesToComponent) value);
        } else if (name.equals("event")) {
          this.getEvent().add((CompositionEventComponent) value);
        } else if (name.equals("section")) {
          this.getSection().add((SectionComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -892481550:  return getStatusElement();
        case 3575610:  return getType(); 
        case 94742904:  return getClass_(); 
        case -1867885268:  return getSubject(); 
        case 1524132147:  return getEncounter(); 
        case 3076014:  return getDateElement();
        case -1406328437:  return addAuthor(); 
        case 110371416:  return getTitleElement();
        case -1923018202:  return getConfidentialityElement();
        case 542920370:  return addAttester(); 
        case 1611297262:  return getCustodian(); 
        case -7765931:  return addRelatesTo(); 
        case 96891546:  return addEvent(); 
        case 1970241253:  return addSection(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 94742904: /*class*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1524132147: /*encounter*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -1923018202: /*confidentiality*/ return new String[] {"code"};
        case 542920370: /*attester*/ return new String[] {};
        case 1611297262: /*custodian*/ return new String[] {"Reference"};
        case -7765931: /*relatesTo*/ return new String[] {};
        case 96891546: /*event*/ return new String[] {};
        case 1970241253: /*section*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("class")) {
          this.class_ = new CodeableConcept();
          return this.class_;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.date");
        }
        else if (name.equals("author")) {
          return addAuthor();
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.title");
        }
        else if (name.equals("confidentiality")) {
          throw new FHIRException("Cannot call addChild on a primitive type Composition.confidentiality");
        }
        else if (name.equals("attester")) {
          return addAttester();
        }
        else if (name.equals("custodian")) {
          this.custodian = new Reference();
          return this.custodian;
        }
        else if (name.equals("relatesTo")) {
          return addRelatesTo();
        }
        else if (name.equals("event")) {
          return addEvent();
        }
        else if (name.equals("section")) {
          return addSection();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Composition";

  }

      public Composition copy() {
        Composition dst = new Composition();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.class_ = class_ == null ? null : class_.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.date = date == null ? null : date.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        dst.title = title == null ? null : title.copy();
        dst.confidentiality = confidentiality == null ? null : confidentiality.copy();
        if (attester != null) {
          dst.attester = new ArrayList<CompositionAttesterComponent>();
          for (CompositionAttesterComponent i : attester)
            dst.attester.add(i.copy());
        };
        dst.custodian = custodian == null ? null : custodian.copy();
        if (relatesTo != null) {
          dst.relatesTo = new ArrayList<CompositionRelatesToComponent>();
          for (CompositionRelatesToComponent i : relatesTo)
            dst.relatesTo.add(i.copy());
        };
        if (event != null) {
          dst.event = new ArrayList<CompositionEventComponent>();
          for (CompositionEventComponent i : event)
            dst.event.add(i.copy());
        };
        if (section != null) {
          dst.section = new ArrayList<SectionComponent>();
          for (SectionComponent i : section)
            dst.section.add(i.copy());
        };
        return dst;
      }

      protected Composition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Composition))
          return false;
        Composition o = (Composition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(class_, o.class_, true) && compareDeep(subject, o.subject, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(date, o.date, true) && compareDeep(author, o.author, true) && compareDeep(title, o.title, true)
           && compareDeep(confidentiality, o.confidentiality, true) && compareDeep(attester, o.attester, true)
           && compareDeep(custodian, o.custodian, true) && compareDeep(relatesTo, o.relatesTo, true) && compareDeep(event, o.event, true)
           && compareDeep(section, o.section, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Composition))
          return false;
        Composition o = (Composition) other;
        return compareValues(status, o.status, true) && compareValues(date, o.date, true) && compareValues(title, o.title, true)
           && compareValues(confidentiality, o.confidentiality, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , class_, subject, encounter, date, author, title, confidentiality, attester
          , custodian, relatesTo, event, section);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Composition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Composition editing time</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Composition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Composition.date", description="Composition editing time", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Composition editing time</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Composition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier of composition (version-independent)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Composition.identifier", description="Logical identifier of composition (version-independent)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier of composition (version-independent)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>period</b>
   * <p>
   * Description: <b>The period covered by the documentation</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Composition.event.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="period", path="Composition.event.period", description="The period covered by the documentation", type="date" )
  public static final String SP_PERIOD = "period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>period</b>
   * <p>
   * Description: <b>The period covered by the documentation</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Composition.event.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PERIOD);

 /**
   * Search parameter: <b>related-id</b>
   * <p>
   * Description: <b>Target of the relationship</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.relatesTo.targetIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="related-id", path="Composition.relatesTo.target.as(Identifier)", description="Target of the relationship", type="token" )
  public static final String SP_RELATED_ID = "related-id";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related-id</b>
   * <p>
   * Description: <b>Target of the relationship</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.relatesTo.targetIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RELATED_ID = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RELATED_ID);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who and/or what the composition is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Composition.subject", description="Who and/or what the composition is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who and/or what the composition is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Composition:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Composition:subject").toLocked();

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>Who and/or what authored the composition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="Composition.author", description="Who and/or what authored the composition", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>Who and/or what authored the composition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Composition:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("Composition:author").toLocked();

 /**
   * Search parameter: <b>confidentiality</b>
   * <p>
   * Description: <b>As defined by affinity domain</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.confidentiality</b><br>
   * </p>
   */
  @SearchParamDefinition(name="confidentiality", path="Composition.confidentiality", description="As defined by affinity domain", type="token" )
  public static final String SP_CONFIDENTIALITY = "confidentiality";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>confidentiality</b>
   * <p>
   * Description: <b>As defined by affinity domain</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.confidentiality</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONFIDENTIALITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONFIDENTIALITY);

 /**
   * Search parameter: <b>section</b>
   * <p>
   * Description: <b>Classification of section (recommended)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.section.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="section", path="Composition.section.code", description="Classification of section (recommended)", type="token" )
  public static final String SP_SECTION = "section";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>section</b>
   * <p>
   * Description: <b>Classification of section (recommended)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.section.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SECTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SECTION);

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Context of the Composition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Composition.encounter", description="Context of the Composition", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Context of the Composition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Composition:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Composition:encounter").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Kind of composition (LOINC if possible)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Composition.type", description="Kind of composition (LOINC if possible)", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Kind of composition (LOINC if possible)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Human Readable name/title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Composition.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="Composition.title", description="Human Readable name/title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Human Readable name/title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Composition.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>attester</b>
   * <p>
   * Description: <b>Who attested the composition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.attester.party</b><br>
   * </p>
   */
  @SearchParamDefinition(name="attester", path="Composition.attester.party", description="Who attested the composition", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Patient.class, Practitioner.class } )
  public static final String SP_ATTESTER = "attester";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>attester</b>
   * <p>
   * Description: <b>Who attested the composition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.attester.party</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ATTESTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ATTESTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Composition:attester</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ATTESTER = new ca.uhn.fhir.model.api.Include("Composition:attester").toLocked();

 /**
   * Search parameter: <b>entry</b>
   * <p>
   * Description: <b>A reference to data that supports this section</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.section.entry</b><br>
   * </p>
   */
  @SearchParamDefinition(name="entry", path="Composition.section.entry", description="A reference to data that supports this section", type="reference" )
  public static final String SP_ENTRY = "entry";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>entry</b>
   * <p>
   * Description: <b>A reference to data that supports this section</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.section.entry</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTRY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTRY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Composition:entry</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTRY = new ca.uhn.fhir.model.api.Include("Composition:entry").toLocked();

 /**
   * Search parameter: <b>related-ref</b>
   * <p>
   * Description: <b>Target of the relationship</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.relatesTo.targetReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="related-ref", path="Composition.relatesTo.target.as(Reference)", description="Target of the relationship", type="reference", target={Composition.class } )
  public static final String SP_RELATED_REF = "related-ref";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related-ref</b>
   * <p>
   * Description: <b>Target of the relationship</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.relatesTo.targetReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RELATED_REF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RELATED_REF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Composition:related-ref</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RELATED_REF = new ca.uhn.fhir.model.api.Include("Composition:related-ref").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who and/or what the composition is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Composition.subject", description="Who and/or what the composition is about", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who and/or what the composition is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Composition.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Composition:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Composition:patient").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Code(s) that apply to the event being documented</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.event.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="Composition.event.code", description="Code(s) that apply to the event being documented", type="token" )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Code(s) that apply to the event being documented</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.event.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTEXT);

 /**
   * Search parameter: <b>class</b>
   * <p>
   * Description: <b>Categorization of Composition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.class</b><br>
   * </p>
   */
  @SearchParamDefinition(name="class", path="Composition.class", description="Categorization of Composition", type="token" )
  public static final String SP_CLASS = "class";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>class</b>
   * <p>
   * Description: <b>Categorization of Composition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.class</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CLASS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CLASS);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>preliminary | final | amended | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Composition.status", description="preliminary | final | amended | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>preliminary | final | amended | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Composition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

