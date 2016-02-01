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
 * The ModuleMetadata resource defines the common metadata elements used by quality improvement artifacts. This information includes descriptive and topical metadata to enable repository searches, as well as governance and evidentiary support information.
 */
@ResourceDef(name="ModuleMetadata", profile="http://hl7.org/fhir/Profile/ModuleMetadata")
public class ModuleMetadata extends DomainResource {

    public enum ModuleMetadataType {
        /**
         * The resource is a description of a knowledge module
         */
        MODULE, 
        /**
         * The resource is a shareable library of formalized knowledge
         */
        LIBRARY, 
        /**
         * An Event-Condition-Action Rule Artifact
         */
        DECISIONSUPPORTRULE, 
        /**
         * A Documentation Template Artifact
         */
        DOCUMENTATIONTEMPLATE, 
        /**
         * An Order Set Artifact
         */
        ORDERSET, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ModuleMetadataType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("module".equals(codeString))
          return MODULE;
        if ("library".equals(codeString))
          return LIBRARY;
        if ("decision-support-rule".equals(codeString))
          return DECISIONSUPPORTRULE;
        if ("documentation-template".equals(codeString))
          return DOCUMENTATIONTEMPLATE;
        if ("order-set".equals(codeString))
          return ORDERSET;
        throw new FHIRException("Unknown ModuleMetadataType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MODULE: return "module";
            case LIBRARY: return "library";
            case DECISIONSUPPORTRULE: return "decision-support-rule";
            case DOCUMENTATIONTEMPLATE: return "documentation-template";
            case ORDERSET: return "order-set";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MODULE: return "http://hl7.org/fhir/module-metadata-type";
            case LIBRARY: return "http://hl7.org/fhir/module-metadata-type";
            case DECISIONSUPPORTRULE: return "http://hl7.org/fhir/module-metadata-type";
            case DOCUMENTATIONTEMPLATE: return "http://hl7.org/fhir/module-metadata-type";
            case ORDERSET: return "http://hl7.org/fhir/module-metadata-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MODULE: return "The resource is a description of a knowledge module";
            case LIBRARY: return "The resource is a shareable library of formalized knowledge";
            case DECISIONSUPPORTRULE: return "An Event-Condition-Action Rule Artifact";
            case DOCUMENTATIONTEMPLATE: return "A Documentation Template Artifact";
            case ORDERSET: return "An Order Set Artifact";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MODULE: return "Module";
            case LIBRARY: return "Library";
            case DECISIONSUPPORTRULE: return "Decision Support Rule";
            case DOCUMENTATIONTEMPLATE: return "Documentation Template";
            case ORDERSET: return "Order Set";
            default: return "?";
          }
        }
    }

  public static class ModuleMetadataTypeEnumFactory implements EnumFactory<ModuleMetadataType> {
    public ModuleMetadataType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("module".equals(codeString))
          return ModuleMetadataType.MODULE;
        if ("library".equals(codeString))
          return ModuleMetadataType.LIBRARY;
        if ("decision-support-rule".equals(codeString))
          return ModuleMetadataType.DECISIONSUPPORTRULE;
        if ("documentation-template".equals(codeString))
          return ModuleMetadataType.DOCUMENTATIONTEMPLATE;
        if ("order-set".equals(codeString))
          return ModuleMetadataType.ORDERSET;
        throw new IllegalArgumentException("Unknown ModuleMetadataType code '"+codeString+"'");
        }
        public Enumeration<ModuleMetadataType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("module".equals(codeString))
          return new Enumeration<ModuleMetadataType>(this, ModuleMetadataType.MODULE);
        if ("library".equals(codeString))
          return new Enumeration<ModuleMetadataType>(this, ModuleMetadataType.LIBRARY);
        if ("decision-support-rule".equals(codeString))
          return new Enumeration<ModuleMetadataType>(this, ModuleMetadataType.DECISIONSUPPORTRULE);
        if ("documentation-template".equals(codeString))
          return new Enumeration<ModuleMetadataType>(this, ModuleMetadataType.DOCUMENTATIONTEMPLATE);
        if ("order-set".equals(codeString))
          return new Enumeration<ModuleMetadataType>(this, ModuleMetadataType.ORDERSET);
        throw new FHIRException("Unknown ModuleMetadataType code '"+codeString+"'");
        }
    public String toCode(ModuleMetadataType code) {
      if (code == ModuleMetadataType.MODULE)
        return "module";
      if (code == ModuleMetadataType.LIBRARY)
        return "library";
      if (code == ModuleMetadataType.DECISIONSUPPORTRULE)
        return "decision-support-rule";
      if (code == ModuleMetadataType.DOCUMENTATIONTEMPLATE)
        return "documentation-template";
      if (code == ModuleMetadataType.ORDERSET)
        return "order-set";
      return "?";
      }
    public String toSystem(ModuleMetadataType code) {
      return code.getSystem();
      }
    }

    public enum ModuleMetadataStatus {
        /**
         * The module is in draft state
         */
        DRAFT, 
        /**
         * The module is in test state
         */
        TEST, 
        /**
         * The module is active
         */
        ACTIVE, 
        /**
         * The module is inactive, either rejected before publication, or retired after publication
         */
        INACTIVE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ModuleMetadataStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("test".equals(codeString))
          return TEST;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        throw new FHIRException("Unknown ModuleMetadataStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case TEST: return "test";
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/module-metadata-status";
            case TEST: return "http://hl7.org/fhir/module-metadata-status";
            case ACTIVE: return "http://hl7.org/fhir/module-metadata-status";
            case INACTIVE: return "http://hl7.org/fhir/module-metadata-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The module is in draft state";
            case TEST: return "The module is in test state";
            case ACTIVE: return "The module is active";
            case INACTIVE: return "The module is inactive, either rejected before publication, or retired after publication";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case TEST: return "Test";
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            default: return "?";
          }
        }
    }

  public static class ModuleMetadataStatusEnumFactory implements EnumFactory<ModuleMetadataStatus> {
    public ModuleMetadataStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ModuleMetadataStatus.DRAFT;
        if ("test".equals(codeString))
          return ModuleMetadataStatus.TEST;
        if ("active".equals(codeString))
          return ModuleMetadataStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return ModuleMetadataStatus.INACTIVE;
        throw new IllegalArgumentException("Unknown ModuleMetadataStatus code '"+codeString+"'");
        }
        public Enumeration<ModuleMetadataStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ModuleMetadataStatus>(this, ModuleMetadataStatus.DRAFT);
        if ("test".equals(codeString))
          return new Enumeration<ModuleMetadataStatus>(this, ModuleMetadataStatus.TEST);
        if ("active".equals(codeString))
          return new Enumeration<ModuleMetadataStatus>(this, ModuleMetadataStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<ModuleMetadataStatus>(this, ModuleMetadataStatus.INACTIVE);
        throw new FHIRException("Unknown ModuleMetadataStatus code '"+codeString+"'");
        }
    public String toCode(ModuleMetadataStatus code) {
      if (code == ModuleMetadataStatus.DRAFT)
        return "draft";
      if (code == ModuleMetadataStatus.TEST)
        return "test";
      if (code == ModuleMetadataStatus.ACTIVE)
        return "active";
      if (code == ModuleMetadataStatus.INACTIVE)
        return "inactive";
      return "?";
      }
    public String toSystem(ModuleMetadataStatus code) {
      return code.getSystem();
      }
    }

    public enum ModuleMetadataFocusType {
        /**
         * The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1)
         */
        PATIENTGENDER, 
        /**
         * A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75]
         */
        PATIENTAGEGROUP, 
        /**
         * The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use
         */
        CLINICALFOCUS, 
        /**
         * The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101)
         */
        TARGETUSER, 
        /**
         * The settings in which the artifact is intended for use.  For example, admission, pre-op, etc
         */
        WORKFLOWSETTING, 
        /**
         * The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review
         */
        WORKFLOWTASK, 
        /**
         * The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465
         */
        CLINICALVENUE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ModuleMetadataFocusType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient-gender".equals(codeString))
          return PATIENTGENDER;
        if ("patient-age-group".equals(codeString))
          return PATIENTAGEGROUP;
        if ("clinical-focus".equals(codeString))
          return CLINICALFOCUS;
        if ("target-user".equals(codeString))
          return TARGETUSER;
        if ("workflow-setting".equals(codeString))
          return WORKFLOWSETTING;
        if ("workflow-task".equals(codeString))
          return WORKFLOWTASK;
        if ("clinical-venue".equals(codeString))
          return CLINICALVENUE;
        throw new FHIRException("Unknown ModuleMetadataFocusType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENTGENDER: return "patient-gender";
            case PATIENTAGEGROUP: return "patient-age-group";
            case CLINICALFOCUS: return "clinical-focus";
            case TARGETUSER: return "target-user";
            case WORKFLOWSETTING: return "workflow-setting";
            case WORKFLOWTASK: return "workflow-task";
            case CLINICALVENUE: return "clinical-venue";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENTGENDER: return "http://hl7.org/fhir/module-metadata-focus-type";
            case PATIENTAGEGROUP: return "http://hl7.org/fhir/module-metadata-focus-type";
            case CLINICALFOCUS: return "http://hl7.org/fhir/module-metadata-focus-type";
            case TARGETUSER: return "http://hl7.org/fhir/module-metadata-focus-type";
            case WORKFLOWSETTING: return "http://hl7.org/fhir/module-metadata-focus-type";
            case WORKFLOWTASK: return "http://hl7.org/fhir/module-metadata-focus-type";
            case CLINICALVENUE: return "http://hl7.org/fhir/module-metadata-focus-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENTGENDER: return "The gender of the patient. For this item type, use HL7 administrative gender codes (OID: 2.16.840.1.113883.1.11.1)";
            case PATIENTAGEGROUP: return "A patient demographic category for which this artifact is applicable. Allows specification of age groups using coded values originating from the MeSH Code system (OID: 2.16.840.1.113883.6.177). More specifically, only codes from the AgeGroupObservationValue value set are valid for this field  [2.16.840.1.113883.11.75]";
            case CLINICALFOCUS: return "The clinical concept(s) addressed by the artifact.  For example, disease, diagnostic test interpretation, medication ordering. Please refer to the implementation guide on which code system and codes to use";
            case TARGETUSER: return "The user types to which an artifact is targeted.  For example, PCP, Patient, Cardiologist, Behavioral Professional, Oral Health Professional, Prescriber, etc... taken from the NUCC Health Care provider taxonomyCode system (OID: 2.16.840.1.113883.6.101)";
            case WORKFLOWSETTING: return "The settings in which the artifact is intended for use.  For example, admission, pre-op, etc";
            case WORKFLOWTASK: return "The context for the clinical task(s) represented by this artifact. Can be any task context represented by the HL7 ActTaskCode value set (OID: 2.16.840.1.113883.1.11.19846). General categories include: order entry, patient documentation and patient information review";
            case CLINICALVENUE: return "The venue in which an artifact could be used.  For example, Outpatient, Inpatient, Home, Nursing home. The code value may originate from either the HL7 ActEncounter (OID: 2.16.840.1.113883.1.11.13955) or NUCC non-individual provider codes OID: 2.16.840.1.113883.1.11.19465";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENTGENDER: return "Patient Gender";
            case PATIENTAGEGROUP: return "Patient Age Group";
            case CLINICALFOCUS: return "Clinical Focus";
            case TARGETUSER: return "Target User";
            case WORKFLOWSETTING: return "Workflow Setting";
            case WORKFLOWTASK: return "Workflow Task";
            case CLINICALVENUE: return "Clinical Venue";
            default: return "?";
          }
        }
    }

  public static class ModuleMetadataFocusTypeEnumFactory implements EnumFactory<ModuleMetadataFocusType> {
    public ModuleMetadataFocusType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("patient-gender".equals(codeString))
          return ModuleMetadataFocusType.PATIENTGENDER;
        if ("patient-age-group".equals(codeString))
          return ModuleMetadataFocusType.PATIENTAGEGROUP;
        if ("clinical-focus".equals(codeString))
          return ModuleMetadataFocusType.CLINICALFOCUS;
        if ("target-user".equals(codeString))
          return ModuleMetadataFocusType.TARGETUSER;
        if ("workflow-setting".equals(codeString))
          return ModuleMetadataFocusType.WORKFLOWSETTING;
        if ("workflow-task".equals(codeString))
          return ModuleMetadataFocusType.WORKFLOWTASK;
        if ("clinical-venue".equals(codeString))
          return ModuleMetadataFocusType.CLINICALVENUE;
        throw new IllegalArgumentException("Unknown ModuleMetadataFocusType code '"+codeString+"'");
        }
        public Enumeration<ModuleMetadataFocusType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("patient-gender".equals(codeString))
          return new Enumeration<ModuleMetadataFocusType>(this, ModuleMetadataFocusType.PATIENTGENDER);
        if ("patient-age-group".equals(codeString))
          return new Enumeration<ModuleMetadataFocusType>(this, ModuleMetadataFocusType.PATIENTAGEGROUP);
        if ("clinical-focus".equals(codeString))
          return new Enumeration<ModuleMetadataFocusType>(this, ModuleMetadataFocusType.CLINICALFOCUS);
        if ("target-user".equals(codeString))
          return new Enumeration<ModuleMetadataFocusType>(this, ModuleMetadataFocusType.TARGETUSER);
        if ("workflow-setting".equals(codeString))
          return new Enumeration<ModuleMetadataFocusType>(this, ModuleMetadataFocusType.WORKFLOWSETTING);
        if ("workflow-task".equals(codeString))
          return new Enumeration<ModuleMetadataFocusType>(this, ModuleMetadataFocusType.WORKFLOWTASK);
        if ("clinical-venue".equals(codeString))
          return new Enumeration<ModuleMetadataFocusType>(this, ModuleMetadataFocusType.CLINICALVENUE);
        throw new FHIRException("Unknown ModuleMetadataFocusType code '"+codeString+"'");
        }
    public String toCode(ModuleMetadataFocusType code) {
      if (code == ModuleMetadataFocusType.PATIENTGENDER)
        return "patient-gender";
      if (code == ModuleMetadataFocusType.PATIENTAGEGROUP)
        return "patient-age-group";
      if (code == ModuleMetadataFocusType.CLINICALFOCUS)
        return "clinical-focus";
      if (code == ModuleMetadataFocusType.TARGETUSER)
        return "target-user";
      if (code == ModuleMetadataFocusType.WORKFLOWSETTING)
        return "workflow-setting";
      if (code == ModuleMetadataFocusType.WORKFLOWTASK)
        return "workflow-task";
      if (code == ModuleMetadataFocusType.CLINICALVENUE)
        return "clinical-venue";
      return "?";
      }
    public String toSystem(ModuleMetadataFocusType code) {
      return code.getSystem();
      }
    }

    public enum ModuleMetadataContributorType {
        /**
         * An author of the content of the module
         */
        AUTHOR, 
        /**
         * An editor of the content of the module
         */
        EDITOR, 
        /**
         * A reviewer of the content of the module
         */
        REVIEWER, 
        /**
         * An endorser of the content of the module
         */
        ENDORSER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ModuleMetadataContributorType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("author".equals(codeString))
          return AUTHOR;
        if ("editor".equals(codeString))
          return EDITOR;
        if ("reviewer".equals(codeString))
          return REVIEWER;
        if ("endorser".equals(codeString))
          return ENDORSER;
        throw new FHIRException("Unknown ModuleMetadataContributorType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AUTHOR: return "author";
            case EDITOR: return "editor";
            case REVIEWER: return "reviewer";
            case ENDORSER: return "endorser";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AUTHOR: return "http://hl7.org/fhir/module-metadata-contributor";
            case EDITOR: return "http://hl7.org/fhir/module-metadata-contributor";
            case REVIEWER: return "http://hl7.org/fhir/module-metadata-contributor";
            case ENDORSER: return "http://hl7.org/fhir/module-metadata-contributor";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AUTHOR: return "An author of the content of the module";
            case EDITOR: return "An editor of the content of the module";
            case REVIEWER: return "A reviewer of the content of the module";
            case ENDORSER: return "An endorser of the content of the module";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AUTHOR: return "Author";
            case EDITOR: return "Editor";
            case REVIEWER: return "Reviewer";
            case ENDORSER: return "Endorser";
            default: return "?";
          }
        }
    }

  public static class ModuleMetadataContributorTypeEnumFactory implements EnumFactory<ModuleMetadataContributorType> {
    public ModuleMetadataContributorType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("author".equals(codeString))
          return ModuleMetadataContributorType.AUTHOR;
        if ("editor".equals(codeString))
          return ModuleMetadataContributorType.EDITOR;
        if ("reviewer".equals(codeString))
          return ModuleMetadataContributorType.REVIEWER;
        if ("endorser".equals(codeString))
          return ModuleMetadataContributorType.ENDORSER;
        throw new IllegalArgumentException("Unknown ModuleMetadataContributorType code '"+codeString+"'");
        }
        public Enumeration<ModuleMetadataContributorType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("author".equals(codeString))
          return new Enumeration<ModuleMetadataContributorType>(this, ModuleMetadataContributorType.AUTHOR);
        if ("editor".equals(codeString))
          return new Enumeration<ModuleMetadataContributorType>(this, ModuleMetadataContributorType.EDITOR);
        if ("reviewer".equals(codeString))
          return new Enumeration<ModuleMetadataContributorType>(this, ModuleMetadataContributorType.REVIEWER);
        if ("endorser".equals(codeString))
          return new Enumeration<ModuleMetadataContributorType>(this, ModuleMetadataContributorType.ENDORSER);
        throw new FHIRException("Unknown ModuleMetadataContributorType code '"+codeString+"'");
        }
    public String toCode(ModuleMetadataContributorType code) {
      if (code == ModuleMetadataContributorType.AUTHOR)
        return "author";
      if (code == ModuleMetadataContributorType.EDITOR)
        return "editor";
      if (code == ModuleMetadataContributorType.REVIEWER)
        return "reviewer";
      if (code == ModuleMetadataContributorType.ENDORSER)
        return "endorser";
      return "?";
      }
    public String toSystem(ModuleMetadataContributorType code) {
      return code.getSystem();
      }
    }

    public enum ModuleMetadataResourceType {
        /**
         * Additional documentation for the module
         */
        DOCUMENTATION, 
        /**
         * Supporting evidence for the module
         */
        EVIDENCE, 
        /**
         * Bibliographic citation for the module
         */
        CITATION, 
        /**
         * The previous version of the module
         */
        PREDECESSOR, 
        /**
         * The next version of the module
         */
        SUCCESSOR, 
        /**
         * The module is derived from the resource
         */
        DERIVEDFROM, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ModuleMetadataResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("documentation".equals(codeString))
          return DOCUMENTATION;
        if ("evidence".equals(codeString))
          return EVIDENCE;
        if ("citation".equals(codeString))
          return CITATION;
        if ("predecessor".equals(codeString))
          return PREDECESSOR;
        if ("successor".equals(codeString))
          return SUCCESSOR;
        if ("derived-from".equals(codeString))
          return DERIVEDFROM;
        throw new FHIRException("Unknown ModuleMetadataResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCUMENTATION: return "documentation";
            case EVIDENCE: return "evidence";
            case CITATION: return "citation";
            case PREDECESSOR: return "predecessor";
            case SUCCESSOR: return "successor";
            case DERIVEDFROM: return "derived-from";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DOCUMENTATION: return "http://hl7.org/fhir/module-metadata-resource-type";
            case EVIDENCE: return "http://hl7.org/fhir/module-metadata-resource-type";
            case CITATION: return "http://hl7.org/fhir/module-metadata-resource-type";
            case PREDECESSOR: return "http://hl7.org/fhir/module-metadata-resource-type";
            case SUCCESSOR: return "http://hl7.org/fhir/module-metadata-resource-type";
            case DERIVEDFROM: return "http://hl7.org/fhir/module-metadata-resource-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENTATION: return "Additional documentation for the module";
            case EVIDENCE: return "Supporting evidence for the module";
            case CITATION: return "Bibliographic citation for the module";
            case PREDECESSOR: return "The previous version of the module";
            case SUCCESSOR: return "The next version of the module";
            case DERIVEDFROM: return "The module is derived from the resource";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DOCUMENTATION: return "Documentation";
            case EVIDENCE: return "Evidence";
            case CITATION: return "Citation";
            case PREDECESSOR: return "Predecessor";
            case SUCCESSOR: return "Successor";
            case DERIVEDFROM: return "Derived From";
            default: return "?";
          }
        }
    }

  public static class ModuleMetadataResourceTypeEnumFactory implements EnumFactory<ModuleMetadataResourceType> {
    public ModuleMetadataResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("documentation".equals(codeString))
          return ModuleMetadataResourceType.DOCUMENTATION;
        if ("evidence".equals(codeString))
          return ModuleMetadataResourceType.EVIDENCE;
        if ("citation".equals(codeString))
          return ModuleMetadataResourceType.CITATION;
        if ("predecessor".equals(codeString))
          return ModuleMetadataResourceType.PREDECESSOR;
        if ("successor".equals(codeString))
          return ModuleMetadataResourceType.SUCCESSOR;
        if ("derived-from".equals(codeString))
          return ModuleMetadataResourceType.DERIVEDFROM;
        throw new IllegalArgumentException("Unknown ModuleMetadataResourceType code '"+codeString+"'");
        }
        public Enumeration<ModuleMetadataResourceType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("documentation".equals(codeString))
          return new Enumeration<ModuleMetadataResourceType>(this, ModuleMetadataResourceType.DOCUMENTATION);
        if ("evidence".equals(codeString))
          return new Enumeration<ModuleMetadataResourceType>(this, ModuleMetadataResourceType.EVIDENCE);
        if ("citation".equals(codeString))
          return new Enumeration<ModuleMetadataResourceType>(this, ModuleMetadataResourceType.CITATION);
        if ("predecessor".equals(codeString))
          return new Enumeration<ModuleMetadataResourceType>(this, ModuleMetadataResourceType.PREDECESSOR);
        if ("successor".equals(codeString))
          return new Enumeration<ModuleMetadataResourceType>(this, ModuleMetadataResourceType.SUCCESSOR);
        if ("derived-from".equals(codeString))
          return new Enumeration<ModuleMetadataResourceType>(this, ModuleMetadataResourceType.DERIVEDFROM);
        throw new FHIRException("Unknown ModuleMetadataResourceType code '"+codeString+"'");
        }
    public String toCode(ModuleMetadataResourceType code) {
      if (code == ModuleMetadataResourceType.DOCUMENTATION)
        return "documentation";
      if (code == ModuleMetadataResourceType.EVIDENCE)
        return "evidence";
      if (code == ModuleMetadataResourceType.CITATION)
        return "citation";
      if (code == ModuleMetadataResourceType.PREDECESSOR)
        return "predecessor";
      if (code == ModuleMetadataResourceType.SUCCESSOR)
        return "successor";
      if (code == ModuleMetadataResourceType.DERIVEDFROM)
        return "derived-from";
      return "?";
      }
    public String toSystem(ModuleMetadataResourceType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ModuleMetadataCoverageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specifies the focus of the coverage attribute.
         */
        @Child(name = "focus", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="patient-gender | patient-age-group | clinical-focus | target-user | workflow-setting | workflow-task | clinical-venue", formalDefinition="Specifies the focus of the coverage attribute." )
        protected Enumeration<ModuleMetadataFocusType> focus;

        /**
         * Provides an optional description of the coverage attribute.
         */
        @Child(name = "description", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Provides an optional description of the coverage attribute." )
        protected StringType description;

        /**
         * Provides a value for the coverage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.
         */
        @Child(name = "value", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Provides a value for the coverage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus." )
        protected CodeableConcept value;

        private static final long serialVersionUID = 701599845L;

    /**
     * Constructor
     */
      public ModuleMetadataCoverageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleMetadataCoverageComponent(Enumeration<ModuleMetadataFocusType> focus) {
        super();
        this.focus = focus;
      }

        /**
         * @return {@link #focus} (Specifies the focus of the coverage attribute.). This is the underlying object with id, value and extensions. The accessor "getFocus" gives direct access to the value
         */
        public Enumeration<ModuleMetadataFocusType> getFocusElement() { 
          if (this.focus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataCoverageComponent.focus");
            else if (Configuration.doAutoCreate())
              this.focus = new Enumeration<ModuleMetadataFocusType>(new ModuleMetadataFocusTypeEnumFactory()); // bb
          return this.focus;
        }

        public boolean hasFocusElement() { 
          return this.focus != null && !this.focus.isEmpty();
        }

        public boolean hasFocus() { 
          return this.focus != null && !this.focus.isEmpty();
        }

        /**
         * @param value {@link #focus} (Specifies the focus of the coverage attribute.). This is the underlying object with id, value and extensions. The accessor "getFocus" gives direct access to the value
         */
        public ModuleMetadataCoverageComponent setFocusElement(Enumeration<ModuleMetadataFocusType> value) { 
          this.focus = value;
          return this;
        }

        /**
         * @return Specifies the focus of the coverage attribute.
         */
        public ModuleMetadataFocusType getFocus() { 
          return this.focus == null ? null : this.focus.getValue();
        }

        /**
         * @param value Specifies the focus of the coverage attribute.
         */
        public ModuleMetadataCoverageComponent setFocus(ModuleMetadataFocusType value) { 
            if (this.focus == null)
              this.focus = new Enumeration<ModuleMetadataFocusType>(new ModuleMetadataFocusTypeEnumFactory());
            this.focus.setValue(value);
          return this;
        }

        /**
         * @return {@link #description} (Provides an optional description of the coverage attribute.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataCoverageComponent.description");
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
         * @param value {@link #description} (Provides an optional description of the coverage attribute.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ModuleMetadataCoverageComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Provides an optional description of the coverage attribute.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Provides an optional description of the coverage attribute.
         */
        public ModuleMetadataCoverageComponent setDescription(String value) { 
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
         * @return {@link #value} (Provides a value for the coverage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.)
         */
        public CodeableConcept getValue() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataCoverageComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new CodeableConcept(); // cc
          return this.value;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Provides a value for the coverage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.)
         */
        public ModuleMetadataCoverageComponent setValue(CodeableConcept value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("focus", "code", "Specifies the focus of the coverage attribute.", 0, java.lang.Integer.MAX_VALUE, focus));
          childrenList.add(new Property("description", "string", "Provides an optional description of the coverage attribute.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("value", "CodeableConcept", "Provides a value for the coverage attribute. Different values are appropriate in different focus areas, as specified in the description of values for focus.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("focus"))
          this.focus = new ModuleMetadataFocusTypeEnumFactory().fromType(value); // Enumeration<ModuleMetadataFocusType>
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("value"))
          this.value = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("focus")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.focus");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.description");
        }
        else if (name.equals("value")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public ModuleMetadataCoverageComponent copy() {
        ModuleMetadataCoverageComponent dst = new ModuleMetadataCoverageComponent();
        copyValues(dst);
        dst.focus = focus == null ? null : focus.copy();
        dst.description = description == null ? null : description.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleMetadataCoverageComponent))
          return false;
        ModuleMetadataCoverageComponent o = (ModuleMetadataCoverageComponent) other;
        return compareDeep(focus, o.focus, true) && compareDeep(description, o.description, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleMetadataCoverageComponent))
          return false;
        ModuleMetadataCoverageComponent o = (ModuleMetadataCoverageComponent) other;
        return compareValues(focus, o.focus, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (focus == null || focus.isEmpty()) && (description == null || description.isEmpty())
           && (value == null || value.isEmpty());
      }

  public String fhirType() {
    return "ModuleMetadata.coverage";

  }

  }

    @Block()
    public static class ModuleMetadataContributorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of contributor.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="author | editor | reviewer | endorser", formalDefinition="The type of contributor." )
        protected Enumeration<ModuleMetadataContributorType> type;

        /**
         * The contributor.
         */
        @Child(name = "party", type = {Person.class, Organization.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The contributor." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (The contributor.)
         */
        protected Resource partyTarget;

        private static final long serialVersionUID = -652221921L;

    /**
     * Constructor
     */
      public ModuleMetadataContributorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleMetadataContributorComponent(Enumeration<ModuleMetadataContributorType> type, Reference party) {
        super();
        this.type = type;
        this.party = party;
      }

        /**
         * @return {@link #type} (The type of contributor.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ModuleMetadataContributorType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataContributorComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ModuleMetadataContributorType>(new ModuleMetadataContributorTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of contributor.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ModuleMetadataContributorComponent setTypeElement(Enumeration<ModuleMetadataContributorType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of contributor.
         */
        public ModuleMetadataContributorType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of contributor.
         */
        public ModuleMetadataContributorComponent setType(ModuleMetadataContributorType value) { 
            if (this.type == null)
              this.type = new Enumeration<ModuleMetadataContributorType>(new ModuleMetadataContributorTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #party} (The contributor.)
         */
        public Reference getParty() { 
          if (this.party == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataContributorComponent.party");
            else if (Configuration.doAutoCreate())
              this.party = new Reference(); // cc
          return this.party;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (The contributor.)
         */
        public ModuleMetadataContributorComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The contributor.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The contributor.)
         */
        public ModuleMetadataContributorComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of contributor.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("party", "Reference(Person|Organization)", "The contributor.", 0, java.lang.Integer.MAX_VALUE, party));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new ModuleMetadataContributorTypeEnumFactory().fromType(value); // Enumeration<ModuleMetadataContributorType>
        else if (name.equals("party"))
          this.party = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.type");
        }
        else if (name.equals("party")) {
          this.party = new Reference();
          return this.party;
        }
        else
          return super.addChild(name);
      }

      public ModuleMetadataContributorComponent copy() {
        ModuleMetadataContributorComponent dst = new ModuleMetadataContributorComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.party = party == null ? null : party.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleMetadataContributorComponent))
          return false;
        ModuleMetadataContributorComponent o = (ModuleMetadataContributorComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(party, o.party, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleMetadataContributorComponent))
          return false;
        ModuleMetadataContributorComponent o = (ModuleMetadataContributorComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (party == null || party.isEmpty())
          ;
      }

  public String fhirType() {
    return "ModuleMetadata.contributor";

  }

  }

    @Block()
    public static class ModuleMetadataRelatedResourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of related resource.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="documentation | evidence | citation | predecessor | successor | derived-from", formalDefinition="The type of related resource." )
        protected Enumeration<ModuleMetadataResourceType> type;

        /**
         * The uri of the related resource.
         */
        @Child(name = "uri", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The uri of the related resource." )
        protected UriType uri;

        /**
         * A brief description of the related resource.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A brief description of the related resource." )
        protected StringType description;

        /**
         * The document being referenced.
         */
        @Child(name = "document", type = {Attachment.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The document being referenced." )
        protected Attachment document;

        private static final long serialVersionUID = -1094264249L;

    /**
     * Constructor
     */
      public ModuleMetadataRelatedResourceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleMetadataRelatedResourceComponent(Enumeration<ModuleMetadataResourceType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of related resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ModuleMetadataResourceType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataRelatedResourceComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ModuleMetadataResourceType>(new ModuleMetadataResourceTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of related resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ModuleMetadataRelatedResourceComponent setTypeElement(Enumeration<ModuleMetadataResourceType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of related resource.
         */
        public ModuleMetadataResourceType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of related resource.
         */
        public ModuleMetadataRelatedResourceComponent setType(ModuleMetadataResourceType value) { 
            if (this.type == null)
              this.type = new Enumeration<ModuleMetadataResourceType>(new ModuleMetadataResourceTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #uri} (The uri of the related resource.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public UriType getUriElement() { 
          if (this.uri == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataRelatedResourceComponent.uri");
            else if (Configuration.doAutoCreate())
              this.uri = new UriType(); // bb
          return this.uri;
        }

        public boolean hasUriElement() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        public boolean hasUri() { 
          return this.uri != null && !this.uri.isEmpty();
        }

        /**
         * @param value {@link #uri} (The uri of the related resource.). This is the underlying object with id, value and extensions. The accessor "getUri" gives direct access to the value
         */
        public ModuleMetadataRelatedResourceComponent setUriElement(UriType value) { 
          this.uri = value;
          return this;
        }

        /**
         * @return The uri of the related resource.
         */
        public String getUri() { 
          return this.uri == null ? null : this.uri.getValue();
        }

        /**
         * @param value The uri of the related resource.
         */
        public ModuleMetadataRelatedResourceComponent setUri(String value) { 
          if (Utilities.noString(value))
            this.uri = null;
          else {
            if (this.uri == null)
              this.uri = new UriType();
            this.uri.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (A brief description of the related resource.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataRelatedResourceComponent.description");
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
         * @param value {@link #description} (A brief description of the related resource.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ModuleMetadataRelatedResourceComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A brief description of the related resource.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A brief description of the related resource.
         */
        public ModuleMetadataRelatedResourceComponent setDescription(String value) { 
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
         * @return {@link #document} (The document being referenced.)
         */
        public Attachment getDocument() { 
          if (this.document == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleMetadataRelatedResourceComponent.document");
            else if (Configuration.doAutoCreate())
              this.document = new Attachment(); // cc
          return this.document;
        }

        public boolean hasDocument() { 
          return this.document != null && !this.document.isEmpty();
        }

        /**
         * @param value {@link #document} (The document being referenced.)
         */
        public ModuleMetadataRelatedResourceComponent setDocument(Attachment value) { 
          this.document = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of related resource.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("uri", "uri", "The uri of the related resource.", 0, java.lang.Integer.MAX_VALUE, uri));
          childrenList.add(new Property("description", "string", "A brief description of the related resource.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("document", "Attachment", "The document being referenced.", 0, java.lang.Integer.MAX_VALUE, document));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new ModuleMetadataResourceTypeEnumFactory().fromType(value); // Enumeration<ModuleMetadataResourceType>
        else if (name.equals("uri"))
          this.uri = castToUri(value); // UriType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("document"))
          this.document = castToAttachment(value); // Attachment
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.type");
        }
        else if (name.equals("uri")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.uri");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.description");
        }
        else if (name.equals("document")) {
          this.document = new Attachment();
          return this.document;
        }
        else
          return super.addChild(name);
      }

      public ModuleMetadataRelatedResourceComponent copy() {
        ModuleMetadataRelatedResourceComponent dst = new ModuleMetadataRelatedResourceComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.uri = uri == null ? null : uri.copy();
        dst.description = description == null ? null : description.copy();
        dst.document = document == null ? null : document.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleMetadataRelatedResourceComponent))
          return false;
        ModuleMetadataRelatedResourceComponent o = (ModuleMetadataRelatedResourceComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(uri, o.uri, true) && compareDeep(description, o.description, true)
           && compareDeep(document, o.document, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleMetadataRelatedResourceComponent))
          return false;
        ModuleMetadataRelatedResourceComponent o = (ModuleMetadataRelatedResourceComponent) other;
        return compareValues(type, o.type, true) && compareValues(uri, o.uri, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (uri == null || uri.isEmpty())
           && (description == null || description.isEmpty()) && (document == null || document.isEmpty())
          ;
      }

  public String fhirType() {
    return "ModuleMetadata.relatedResource";

  }

  }

    /**
     * A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier", formalDefinition="A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact." )
    protected List<Identifier> identifier;

    /**
     * The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the module, if any", formalDefinition="The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification." )
    protected StringType version;

    /**
     * A short, descriptive title for the module.
     */
    @Child(name = "title", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="A short, descriptive title for the module." )
    protected StringType title;

    /**
     * Identifies the type of knowledge module, such as a rule, library, documentation template, or measure.
     */
    @Child(name = "type", type = {CodeType.class}, order=3, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="module | library | decision-support-rule | documentation-template | order-set", formalDefinition="Identifies the type of knowledge module, such as a rule, library, documentation template, or measure." )
    protected Enumeration<ModuleMetadataType> type;

    /**
     * The status of the module.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | test | active | inactive", formalDefinition="The status of the module." )
    protected Enumeration<ModuleMetadataStatus> status;

    /**
     * A description of the module from the consumer perspective.
     */
    @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="A description of the module from the consumer perspective." )
    protected StringType description;

    /**
     * A brief description of the purpose of the module.
     */
    @Child(name = "purpose", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="A brief description of the purpose of the module." )
    protected StringType purpose;

    /**
     * Notes about usage of the module.
     */
    @Child(name = "usage", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Notes about usage of the module." )
    protected StringType usage;

    /**
     * The date on which the module was published.
     */
    @Child(name = "publicationDate", type = {DateType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The date on which the module was published." )
    protected DateType publicationDate;

    /**
     * The date on which the module content was last reviewed.
     */
    @Child(name = "lastReviewDate", type = {DateType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The date on which the module content was last reviewed." )
    protected DateType lastReviewDate;

    /**
     * The period during which the module content is effective.
     */
    @Child(name = "effectivePeriod", type = {Period.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The period during which the module content is effective." )
    protected Period effectivePeriod;

    /**
     * Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable.
     */
    @Child(name = "coverage", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable." )
    protected List<ModuleMetadataCoverageComponent> coverage;

    /**
     * Clinical topics related to the content of the module.
     */
    @Child(name = "topic", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Clinical topics related to the content of the module." )
    protected List<CodeableConcept> topic;

    /**
     * Keywords associated with the module.
     */
    @Child(name = "keyword", type = {StringType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Keywords associated with the module." )
    protected List<StringType> keyword;

    /**
     * A contributor to the content of the module.
     */
    @Child(name = "contributor", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="A contributor to the content of the module." )
    protected List<ModuleMetadataContributorComponent> contributor;

    /**
     * The organization responsible for publishing the module.
     */
    @Child(name = "publisher", type = {Organization.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The organization responsible for publishing the module." )
    protected Reference publisher;

    /**
     * The actual object that is the target of the reference (The organization responsible for publishing the module.)
     */
    protected Organization publisherTarget;

    /**
     * The organization responsible for stewardship of the module content.
     */
    @Child(name = "steward", type = {Organization.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The organization responsible for stewardship of the module content." )
    protected Reference steward;

    /**
     * The actual object that is the target of the reference (The organization responsible for stewardship of the module content.)
     */
    protected Organization stewardTarget;

    /**
     * The legal rights declaration for the module.
     */
    @Child(name = "rightsDeclaration", type = {StringType.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The legal rights declaration for the module." )
    protected StringType rightsDeclaration;

    /**
     * Related resources such as additional documentation, supporting evidence, or bibliographic references.
     */
    @Child(name = "relatedResource", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Related resources such as additional documentation, supporting evidence, or bibliographic references." )
    protected List<ModuleMetadataRelatedResourceComponent> relatedResource;

    private static final long serialVersionUID = -1125568771L;

  /**
   * Constructor
   */
    public ModuleMetadata() {
      super();
    }

  /**
   * Constructor
   */
    public ModuleMetadata(Enumeration<ModuleMetadataType> type, Enumeration<ModuleMetadataStatus> status) {
      super();
      this.type = type;
      this.status = status;
    }

    /**
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
    public ModuleMetadata addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.version");
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
     * @param value {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ModuleMetadata setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public ModuleMetadata setVersion(String value) { 
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
     * @return {@link #title} (A short, descriptive title for the module.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.title");
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
     * @param value {@link #title} (A short, descriptive title for the module.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ModuleMetadata setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive title for the module.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive title for the module.
     */
    public ModuleMetadata setTitle(String value) { 
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
     * @return {@link #type} (Identifies the type of knowledge module, such as a rule, library, documentation template, or measure.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<ModuleMetadataType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<ModuleMetadataType>(new ModuleMetadataTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Identifies the type of knowledge module, such as a rule, library, documentation template, or measure.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public ModuleMetadata setTypeElement(Enumeration<ModuleMetadataType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Identifies the type of knowledge module, such as a rule, library, documentation template, or measure.
     */
    public ModuleMetadataType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Identifies the type of knowledge module, such as a rule, library, documentation template, or measure.
     */
    public ModuleMetadata setType(ModuleMetadataType value) { 
        if (this.type == null)
          this.type = new Enumeration<ModuleMetadataType>(new ModuleMetadataTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of the module.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ModuleMetadataStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ModuleMetadataStatus>(new ModuleMetadataStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the module.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ModuleMetadata setStatusElement(Enumeration<ModuleMetadataStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the module.
     */
    public ModuleMetadataStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the module.
     */
    public ModuleMetadata setStatus(ModuleMetadataStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ModuleMetadataStatus>(new ModuleMetadataStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #description} (A description of the module from the consumer perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.description");
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
     * @param value {@link #description} (A description of the module from the consumer perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ModuleMetadata setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A description of the module from the consumer perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A description of the module from the consumer perspective.
     */
    public ModuleMetadata setDescription(String value) { 
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
     * @return {@link #purpose} (A brief description of the purpose of the module.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public StringType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.purpose");
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
     * @param value {@link #purpose} (A brief description of the purpose of the module.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public ModuleMetadata setPurposeElement(StringType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return A brief description of the purpose of the module.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value A brief description of the purpose of the module.
     */
    public ModuleMetadata setPurpose(String value) { 
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
     * @return {@link #usage} (Notes about usage of the module.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public StringType getUsageElement() { 
      if (this.usage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.usage");
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
     * @param value {@link #usage} (Notes about usage of the module.). This is the underlying object with id, value and extensions. The accessor "getUsage" gives direct access to the value
     */
    public ModuleMetadata setUsageElement(StringType value) { 
      this.usage = value;
      return this;
    }

    /**
     * @return Notes about usage of the module.
     */
    public String getUsage() { 
      return this.usage == null ? null : this.usage.getValue();
    }

    /**
     * @param value Notes about usage of the module.
     */
    public ModuleMetadata setUsage(String value) { 
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
     * @return {@link #publicationDate} (The date on which the module was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public DateType getPublicationDateElement() { 
      if (this.publicationDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.publicationDate");
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
     * @param value {@link #publicationDate} (The date on which the module was published.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
     */
    public ModuleMetadata setPublicationDateElement(DateType value) { 
      this.publicationDate = value;
      return this;
    }

    /**
     * @return The date on which the module was published.
     */
    public Date getPublicationDate() { 
      return this.publicationDate == null ? null : this.publicationDate.getValue();
    }

    /**
     * @param value The date on which the module was published.
     */
    public ModuleMetadata setPublicationDate(Date value) { 
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
     * @return {@link #lastReviewDate} (The date on which the module content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public DateType getLastReviewDateElement() { 
      if (this.lastReviewDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.lastReviewDate");
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
     * @param value {@link #lastReviewDate} (The date on which the module content was last reviewed.). This is the underlying object with id, value and extensions. The accessor "getLastReviewDate" gives direct access to the value
     */
    public ModuleMetadata setLastReviewDateElement(DateType value) { 
      this.lastReviewDate = value;
      return this;
    }

    /**
     * @return The date on which the module content was last reviewed.
     */
    public Date getLastReviewDate() { 
      return this.lastReviewDate == null ? null : this.lastReviewDate.getValue();
    }

    /**
     * @param value The date on which the module content was last reviewed.
     */
    public ModuleMetadata setLastReviewDate(Date value) { 
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
     * @return {@link #effectivePeriod} (The period during which the module content is effective.)
     */
    public Period getEffectivePeriod() { 
      if (this.effectivePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.effectivePeriod");
        else if (Configuration.doAutoCreate())
          this.effectivePeriod = new Period(); // cc
      return this.effectivePeriod;
    }

    public boolean hasEffectivePeriod() { 
      return this.effectivePeriod != null && !this.effectivePeriod.isEmpty();
    }

    /**
     * @param value {@link #effectivePeriod} (The period during which the module content is effective.)
     */
    public ModuleMetadata setEffectivePeriod(Period value) { 
      this.effectivePeriod = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable.)
     */
    public List<ModuleMetadataCoverageComponent> getCoverage() { 
      if (this.coverage == null)
        this.coverage = new ArrayList<ModuleMetadataCoverageComponent>();
      return this.coverage;
    }

    public boolean hasCoverage() { 
      if (this.coverage == null)
        return false;
      for (ModuleMetadataCoverageComponent item : this.coverage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #coverage} (Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable.)
     */
    // syntactic sugar
    public ModuleMetadataCoverageComponent addCoverage() { //3
      ModuleMetadataCoverageComponent t = new ModuleMetadataCoverageComponent();
      if (this.coverage == null)
        this.coverage = new ArrayList<ModuleMetadataCoverageComponent>();
      this.coverage.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleMetadata addCoverage(ModuleMetadataCoverageComponent t) { //3
      if (t == null)
        return this;
      if (this.coverage == null)
        this.coverage = new ArrayList<ModuleMetadataCoverageComponent>();
      this.coverage.add(t);
      return this;
    }

    /**
     * @return {@link #topic} (Clinical topics related to the content of the module.)
     */
    public List<CodeableConcept> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      return this.topic;
    }

    public boolean hasTopic() { 
      if (this.topic == null)
        return false;
      for (CodeableConcept item : this.topic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #topic} (Clinical topics related to the content of the module.)
     */
    // syntactic sugar
    public CodeableConcept addTopic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleMetadata addTopic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.topic == null)
        this.topic = new ArrayList<CodeableConcept>();
      this.topic.add(t);
      return this;
    }

    /**
     * @return {@link #keyword} (Keywords associated with the module.)
     */
    public List<StringType> getKeyword() { 
      if (this.keyword == null)
        this.keyword = new ArrayList<StringType>();
      return this.keyword;
    }

    public boolean hasKeyword() { 
      if (this.keyword == null)
        return false;
      for (StringType item : this.keyword)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #keyword} (Keywords associated with the module.)
     */
    // syntactic sugar
    public StringType addKeywordElement() {//2 
      StringType t = new StringType();
      if (this.keyword == null)
        this.keyword = new ArrayList<StringType>();
      this.keyword.add(t);
      return t;
    }

    /**
     * @param value {@link #keyword} (Keywords associated with the module.)
     */
    public ModuleMetadata addKeyword(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.keyword == null)
        this.keyword = new ArrayList<StringType>();
      this.keyword.add(t);
      return this;
    }

    /**
     * @param value {@link #keyword} (Keywords associated with the module.)
     */
    public boolean hasKeyword(String value) { 
      if (this.keyword == null)
        return false;
      for (StringType v : this.keyword)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #contributor} (A contributor to the content of the module.)
     */
    public List<ModuleMetadataContributorComponent> getContributor() { 
      if (this.contributor == null)
        this.contributor = new ArrayList<ModuleMetadataContributorComponent>();
      return this.contributor;
    }

    public boolean hasContributor() { 
      if (this.contributor == null)
        return false;
      for (ModuleMetadataContributorComponent item : this.contributor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contributor} (A contributor to the content of the module.)
     */
    // syntactic sugar
    public ModuleMetadataContributorComponent addContributor() { //3
      ModuleMetadataContributorComponent t = new ModuleMetadataContributorComponent();
      if (this.contributor == null)
        this.contributor = new ArrayList<ModuleMetadataContributorComponent>();
      this.contributor.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleMetadata addContributor(ModuleMetadataContributorComponent t) { //3
      if (t == null)
        return this;
      if (this.contributor == null)
        this.contributor = new ArrayList<ModuleMetadataContributorComponent>();
      this.contributor.add(t);
      return this;
    }

    /**
     * @return {@link #publisher} (The organization responsible for publishing the module.)
     */
    public Reference getPublisher() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new Reference(); // cc
      return this.publisher;
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The organization responsible for publishing the module.)
     */
    public ModuleMetadata setPublisher(Reference value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return {@link #publisher} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization responsible for publishing the module.)
     */
    public Organization getPublisherTarget() { 
      if (this.publisherTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.publisher");
        else if (Configuration.doAutoCreate())
          this.publisherTarget = new Organization(); // aa
      return this.publisherTarget;
    }

    /**
     * @param value {@link #publisher} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization responsible for publishing the module.)
     */
    public ModuleMetadata setPublisherTarget(Organization value) { 
      this.publisherTarget = value;
      return this;
    }

    /**
     * @return {@link #steward} (The organization responsible for stewardship of the module content.)
     */
    public Reference getSteward() { 
      if (this.steward == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.steward");
        else if (Configuration.doAutoCreate())
          this.steward = new Reference(); // cc
      return this.steward;
    }

    public boolean hasSteward() { 
      return this.steward != null && !this.steward.isEmpty();
    }

    /**
     * @param value {@link #steward} (The organization responsible for stewardship of the module content.)
     */
    public ModuleMetadata setSteward(Reference value) { 
      this.steward = value;
      return this;
    }

    /**
     * @return {@link #steward} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization responsible for stewardship of the module content.)
     */
    public Organization getStewardTarget() { 
      if (this.stewardTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.steward");
        else if (Configuration.doAutoCreate())
          this.stewardTarget = new Organization(); // aa
      return this.stewardTarget;
    }

    /**
     * @param value {@link #steward} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization responsible for stewardship of the module content.)
     */
    public ModuleMetadata setStewardTarget(Organization value) { 
      this.stewardTarget = value;
      return this;
    }

    /**
     * @return {@link #rightsDeclaration} (The legal rights declaration for the module.). This is the underlying object with id, value and extensions. The accessor "getRightsDeclaration" gives direct access to the value
     */
    public StringType getRightsDeclarationElement() { 
      if (this.rightsDeclaration == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleMetadata.rightsDeclaration");
        else if (Configuration.doAutoCreate())
          this.rightsDeclaration = new StringType(); // bb
      return this.rightsDeclaration;
    }

    public boolean hasRightsDeclarationElement() { 
      return this.rightsDeclaration != null && !this.rightsDeclaration.isEmpty();
    }

    public boolean hasRightsDeclaration() { 
      return this.rightsDeclaration != null && !this.rightsDeclaration.isEmpty();
    }

    /**
     * @param value {@link #rightsDeclaration} (The legal rights declaration for the module.). This is the underlying object with id, value and extensions. The accessor "getRightsDeclaration" gives direct access to the value
     */
    public ModuleMetadata setRightsDeclarationElement(StringType value) { 
      this.rightsDeclaration = value;
      return this;
    }

    /**
     * @return The legal rights declaration for the module.
     */
    public String getRightsDeclaration() { 
      return this.rightsDeclaration == null ? null : this.rightsDeclaration.getValue();
    }

    /**
     * @param value The legal rights declaration for the module.
     */
    public ModuleMetadata setRightsDeclaration(String value) { 
      if (Utilities.noString(value))
        this.rightsDeclaration = null;
      else {
        if (this.rightsDeclaration == null)
          this.rightsDeclaration = new StringType();
        this.rightsDeclaration.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relatedResource} (Related resources such as additional documentation, supporting evidence, or bibliographic references.)
     */
    public List<ModuleMetadataRelatedResourceComponent> getRelatedResource() { 
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<ModuleMetadataRelatedResourceComponent>();
      return this.relatedResource;
    }

    public boolean hasRelatedResource() { 
      if (this.relatedResource == null)
        return false;
      for (ModuleMetadataRelatedResourceComponent item : this.relatedResource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #relatedResource} (Related resources such as additional documentation, supporting evidence, or bibliographic references.)
     */
    // syntactic sugar
    public ModuleMetadataRelatedResourceComponent addRelatedResource() { //3
      ModuleMetadataRelatedResourceComponent t = new ModuleMetadataRelatedResourceComponent();
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<ModuleMetadataRelatedResourceComponent>();
      this.relatedResource.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleMetadata addRelatedResource(ModuleMetadataRelatedResourceComponent t) { //3
      if (t == null)
        return this;
      if (this.relatedResource == null)
        this.relatedResource = new ArrayList<ModuleMetadataRelatedResourceComponent>();
      this.relatedResource.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("title", "string", "A short, descriptive title for the module.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("type", "code", "Identifies the type of knowledge module, such as a rule, library, documentation template, or measure.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("status", "code", "The status of the module.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("description", "string", "A description of the module from the consumer perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("purpose", "string", "A brief description of the purpose of the module.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("usage", "string", "Notes about usage of the module.", 0, java.lang.Integer.MAX_VALUE, usage));
        childrenList.add(new Property("publicationDate", "date", "The date on which the module was published.", 0, java.lang.Integer.MAX_VALUE, publicationDate));
        childrenList.add(new Property("lastReviewDate", "date", "The date on which the module content was last reviewed.", 0, java.lang.Integer.MAX_VALUE, lastReviewDate));
        childrenList.add(new Property("effectivePeriod", "Period", "The period during which the module content is effective.", 0, java.lang.Integer.MAX_VALUE, effectivePeriod));
        childrenList.add(new Property("coverage", "", "Specifies various attributes of the patient population for whom and/or environment of care in which, the knowledge module is applicable.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("topic", "CodeableConcept", "Clinical topics related to the content of the module.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("keyword", "string", "Keywords associated with the module.", 0, java.lang.Integer.MAX_VALUE, keyword));
        childrenList.add(new Property("contributor", "", "A contributor to the content of the module.", 0, java.lang.Integer.MAX_VALUE, contributor));
        childrenList.add(new Property("publisher", "Reference(Organization)", "The organization responsible for publishing the module.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("steward", "Reference(Organization)", "The organization responsible for stewardship of the module content.", 0, java.lang.Integer.MAX_VALUE, steward));
        childrenList.add(new Property("rightsDeclaration", "string", "The legal rights declaration for the module.", 0, java.lang.Integer.MAX_VALUE, rightsDeclaration));
        childrenList.add(new Property("relatedResource", "", "Related resources such as additional documentation, supporting evidence, or bibliographic references.", 0, java.lang.Integer.MAX_VALUE, relatedResource));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = new ModuleMetadataTypeEnumFactory().fromType(value); // Enumeration<ModuleMetadataType>
        else if (name.equals("status"))
          this.status = new ModuleMetadataStatusEnumFactory().fromType(value); // Enumeration<ModuleMetadataStatus>
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
          this.getCoverage().add((ModuleMetadataCoverageComponent) value);
        else if (name.equals("topic"))
          this.getTopic().add(castToCodeableConcept(value));
        else if (name.equals("keyword"))
          this.getKeyword().add(castToString(value));
        else if (name.equals("contributor"))
          this.getContributor().add((ModuleMetadataContributorComponent) value);
        else if (name.equals("publisher"))
          this.publisher = castToReference(value); // Reference
        else if (name.equals("steward"))
          this.steward = castToReference(value); // Reference
        else if (name.equals("rightsDeclaration"))
          this.rightsDeclaration = castToString(value); // StringType
        else if (name.equals("relatedResource"))
          this.getRelatedResource().add((ModuleMetadataRelatedResourceComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.version");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.title");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.type");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.status");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.description");
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.purpose");
        }
        else if (name.equals("usage")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.usage");
        }
        else if (name.equals("publicationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.publicationDate");
        }
        else if (name.equals("lastReviewDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.lastReviewDate");
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
        else if (name.equals("keyword")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.keyword");
        }
        else if (name.equals("contributor")) {
          return addContributor();
        }
        else if (name.equals("publisher")) {
          this.publisher = new Reference();
          return this.publisher;
        }
        else if (name.equals("steward")) {
          this.steward = new Reference();
          return this.steward;
        }
        else if (name.equals("rightsDeclaration")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleMetadata.rightsDeclaration");
        }
        else if (name.equals("relatedResource")) {
          return addRelatedResource();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ModuleMetadata";

  }

      public ModuleMetadata copy() {
        ModuleMetadata dst = new ModuleMetadata();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.title = title == null ? null : title.copy();
        dst.type = type == null ? null : type.copy();
        dst.status = status == null ? null : status.copy();
        dst.description = description == null ? null : description.copy();
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.usage = usage == null ? null : usage.copy();
        dst.publicationDate = publicationDate == null ? null : publicationDate.copy();
        dst.lastReviewDate = lastReviewDate == null ? null : lastReviewDate.copy();
        dst.effectivePeriod = effectivePeriod == null ? null : effectivePeriod.copy();
        if (coverage != null) {
          dst.coverage = new ArrayList<ModuleMetadataCoverageComponent>();
          for (ModuleMetadataCoverageComponent i : coverage)
            dst.coverage.add(i.copy());
        };
        if (topic != null) {
          dst.topic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : topic)
            dst.topic.add(i.copy());
        };
        if (keyword != null) {
          dst.keyword = new ArrayList<StringType>();
          for (StringType i : keyword)
            dst.keyword.add(i.copy());
        };
        if (contributor != null) {
          dst.contributor = new ArrayList<ModuleMetadataContributorComponent>();
          for (ModuleMetadataContributorComponent i : contributor)
            dst.contributor.add(i.copy());
        };
        dst.publisher = publisher == null ? null : publisher.copy();
        dst.steward = steward == null ? null : steward.copy();
        dst.rightsDeclaration = rightsDeclaration == null ? null : rightsDeclaration.copy();
        if (relatedResource != null) {
          dst.relatedResource = new ArrayList<ModuleMetadataRelatedResourceComponent>();
          for (ModuleMetadataRelatedResourceComponent i : relatedResource)
            dst.relatedResource.add(i.copy());
        };
        return dst;
      }

      protected ModuleMetadata typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleMetadata))
          return false;
        ModuleMetadata o = (ModuleMetadata) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(title, o.title, true)
           && compareDeep(type, o.type, true) && compareDeep(status, o.status, true) && compareDeep(description, o.description, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(usage, o.usage, true) && compareDeep(publicationDate, o.publicationDate, true)
           && compareDeep(lastReviewDate, o.lastReviewDate, true) && compareDeep(effectivePeriod, o.effectivePeriod, true)
           && compareDeep(coverage, o.coverage, true) && compareDeep(topic, o.topic, true) && compareDeep(keyword, o.keyword, true)
           && compareDeep(contributor, o.contributor, true) && compareDeep(publisher, o.publisher, true) && compareDeep(steward, o.steward, true)
           && compareDeep(rightsDeclaration, o.rightsDeclaration, true) && compareDeep(relatedResource, o.relatedResource, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleMetadata))
          return false;
        ModuleMetadata o = (ModuleMetadata) other;
        return compareValues(version, o.version, true) && compareValues(title, o.title, true) && compareValues(type, o.type, true)
           && compareValues(status, o.status, true) && compareValues(description, o.description, true) && compareValues(purpose, o.purpose, true)
           && compareValues(usage, o.usage, true) && compareValues(publicationDate, o.publicationDate, true) && compareValues(lastReviewDate, o.lastReviewDate, true)
           && compareValues(keyword, o.keyword, true) && compareValues(rightsDeclaration, o.rightsDeclaration, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (title == null || title.isEmpty()) && (type == null || type.isEmpty()) && (status == null || status.isEmpty())
           && (description == null || description.isEmpty()) && (purpose == null || purpose.isEmpty())
           && (usage == null || usage.isEmpty()) && (publicationDate == null || publicationDate.isEmpty())
           && (lastReviewDate == null || lastReviewDate.isEmpty()) && (effectivePeriod == null || effectivePeriod.isEmpty())
           && (coverage == null || coverage.isEmpty()) && (topic == null || topic.isEmpty()) && (keyword == null || keyword.isEmpty())
           && (contributor == null || contributor.isEmpty()) && (publisher == null || publisher.isEmpty())
           && (steward == null || steward.isEmpty()) && (rightsDeclaration == null || rightsDeclaration.isEmpty())
           && (relatedResource == null || relatedResource.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ModuleMetadata;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ModuleMetadata.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ModuleMetadata.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ModuleMetadata.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ModuleMetadata.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="ModuleMetadata.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ModuleMetadata.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="ModuleMetadata.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>keyword</b>
   * <p>
   * Description: <b>Keywords associated with the module</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.keyword</b><br>
   * </p>
   */
  @SearchParamDefinition(name="keyword", path="ModuleMetadata.keyword", description="Keywords associated with the module", type="string" )
  public static final String SP_KEYWORD = "keyword";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>keyword</b>
   * <p>
   * Description: <b>Keywords associated with the module</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.keyword</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam KEYWORD = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_KEYWORD);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ModuleMetadata.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="ModuleMetadata.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ModuleMetadata.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ModuleMetadata.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ModuleMetadata.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ModuleMetadata.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

