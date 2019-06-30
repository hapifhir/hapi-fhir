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

import java.math.*;
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
 * Raw data describing a biological sequence.
 */
@ResourceDef(name="MolecularSequence", profile="http://hl7.org/fhir/StructureDefinition/MolecularSequence")
public class MolecularSequence extends DomainResource {

    public enum SequenceType {
        /**
         * Amino acid sequence.
         */
        AA, 
        /**
         * DNA Sequence.
         */
        DNA, 
        /**
         * RNA Sequence.
         */
        RNA, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SequenceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("aa".equals(codeString))
          return AA;
        if ("dna".equals(codeString))
          return DNA;
        if ("rna".equals(codeString))
          return RNA;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SequenceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AA: return "aa";
            case DNA: return "dna";
            case RNA: return "rna";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AA: return "http://hl7.org/fhir/sequence-type";
            case DNA: return "http://hl7.org/fhir/sequence-type";
            case RNA: return "http://hl7.org/fhir/sequence-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AA: return "Amino acid sequence.";
            case DNA: return "DNA Sequence.";
            case RNA: return "RNA Sequence.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AA: return "AA Sequence";
            case DNA: return "DNA Sequence";
            case RNA: return "RNA Sequence";
            default: return "?";
          }
        }
    }

  public static class SequenceTypeEnumFactory implements EnumFactory<SequenceType> {
    public SequenceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("aa".equals(codeString))
          return SequenceType.AA;
        if ("dna".equals(codeString))
          return SequenceType.DNA;
        if ("rna".equals(codeString))
          return SequenceType.RNA;
        throw new IllegalArgumentException("Unknown SequenceType code '"+codeString+"'");
        }
        public Enumeration<SequenceType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SequenceType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("aa".equals(codeString))
          return new Enumeration<SequenceType>(this, SequenceType.AA);
        if ("dna".equals(codeString))
          return new Enumeration<SequenceType>(this, SequenceType.DNA);
        if ("rna".equals(codeString))
          return new Enumeration<SequenceType>(this, SequenceType.RNA);
        throw new FHIRException("Unknown SequenceType code '"+codeString+"'");
        }
    public String toCode(SequenceType code) {
      if (code == SequenceType.AA)
        return "aa";
      if (code == SequenceType.DNA)
        return "dna";
      if (code == SequenceType.RNA)
        return "rna";
      return "?";
      }
    public String toSystem(SequenceType code) {
      return code.getSystem();
      }
    }

    public enum OrientationType {
        /**
         * Sense orientation of reference sequence.
         */
        SENSE, 
        /**
         * Antisense orientation of reference sequence.
         */
        ANTISENSE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static OrientationType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("sense".equals(codeString))
          return SENSE;
        if ("antisense".equals(codeString))
          return ANTISENSE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown OrientationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case SENSE: return "sense";
            case ANTISENSE: return "antisense";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case SENSE: return "http://hl7.org/fhir/orientation-type";
            case ANTISENSE: return "http://hl7.org/fhir/orientation-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case SENSE: return "Sense orientation of reference sequence.";
            case ANTISENSE: return "Antisense orientation of reference sequence.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case SENSE: return "Sense orientation of referenceSeq";
            case ANTISENSE: return "Antisense orientation of referenceSeq";
            default: return "?";
          }
        }
    }

  public static class OrientationTypeEnumFactory implements EnumFactory<OrientationType> {
    public OrientationType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("sense".equals(codeString))
          return OrientationType.SENSE;
        if ("antisense".equals(codeString))
          return OrientationType.ANTISENSE;
        throw new IllegalArgumentException("Unknown OrientationType code '"+codeString+"'");
        }
        public Enumeration<OrientationType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<OrientationType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("sense".equals(codeString))
          return new Enumeration<OrientationType>(this, OrientationType.SENSE);
        if ("antisense".equals(codeString))
          return new Enumeration<OrientationType>(this, OrientationType.ANTISENSE);
        throw new FHIRException("Unknown OrientationType code '"+codeString+"'");
        }
    public String toCode(OrientationType code) {
      if (code == OrientationType.SENSE)
        return "sense";
      if (code == OrientationType.ANTISENSE)
        return "antisense";
      return "?";
      }
    public String toSystem(OrientationType code) {
      return code.getSystem();
      }
    }

    public enum StrandType {
        /**
         * Watson strand of reference sequence.
         */
        WATSON, 
        /**
         * Crick strand of reference sequence.
         */
        CRICK, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static StrandType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("watson".equals(codeString))
          return WATSON;
        if ("crick".equals(codeString))
          return CRICK;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown StrandType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case WATSON: return "watson";
            case CRICK: return "crick";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case WATSON: return "http://hl7.org/fhir/strand-type";
            case CRICK: return "http://hl7.org/fhir/strand-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case WATSON: return "Watson strand of reference sequence.";
            case CRICK: return "Crick strand of reference sequence.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case WATSON: return "Watson strand of referenceSeq";
            case CRICK: return "Crick strand of referenceSeq";
            default: return "?";
          }
        }
    }

  public static class StrandTypeEnumFactory implements EnumFactory<StrandType> {
    public StrandType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("watson".equals(codeString))
          return StrandType.WATSON;
        if ("crick".equals(codeString))
          return StrandType.CRICK;
        throw new IllegalArgumentException("Unknown StrandType code '"+codeString+"'");
        }
        public Enumeration<StrandType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<StrandType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("watson".equals(codeString))
          return new Enumeration<StrandType>(this, StrandType.WATSON);
        if ("crick".equals(codeString))
          return new Enumeration<StrandType>(this, StrandType.CRICK);
        throw new FHIRException("Unknown StrandType code '"+codeString+"'");
        }
    public String toCode(StrandType code) {
      if (code == StrandType.WATSON)
        return "watson";
      if (code == StrandType.CRICK)
        return "crick";
      return "?";
      }
    public String toSystem(StrandType code) {
      return code.getSystem();
      }
    }

    public enum QualityType {
        /**
         * INDEL Comparison.
         */
        INDEL, 
        /**
         * SNP Comparison.
         */
        SNP, 
        /**
         * UNKNOWN Comparison.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static QualityType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("indel".equals(codeString))
          return INDEL;
        if ("snp".equals(codeString))
          return SNP;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown QualityType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INDEL: return "indel";
            case SNP: return "snp";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INDEL: return "http://hl7.org/fhir/quality-type";
            case SNP: return "http://hl7.org/fhir/quality-type";
            case UNKNOWN: return "http://hl7.org/fhir/quality-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INDEL: return "INDEL Comparison.";
            case SNP: return "SNP Comparison.";
            case UNKNOWN: return "UNKNOWN Comparison.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INDEL: return "INDEL Comparison";
            case SNP: return "SNP Comparison";
            case UNKNOWN: return "UNKNOWN Comparison";
            default: return "?";
          }
        }
    }

  public static class QualityTypeEnumFactory implements EnumFactory<QualityType> {
    public QualityType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("indel".equals(codeString))
          return QualityType.INDEL;
        if ("snp".equals(codeString))
          return QualityType.SNP;
        if ("unknown".equals(codeString))
          return QualityType.UNKNOWN;
        throw new IllegalArgumentException("Unknown QualityType code '"+codeString+"'");
        }
        public Enumeration<QualityType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<QualityType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("indel".equals(codeString))
          return new Enumeration<QualityType>(this, QualityType.INDEL);
        if ("snp".equals(codeString))
          return new Enumeration<QualityType>(this, QualityType.SNP);
        if ("unknown".equals(codeString))
          return new Enumeration<QualityType>(this, QualityType.UNKNOWN);
        throw new FHIRException("Unknown QualityType code '"+codeString+"'");
        }
    public String toCode(QualityType code) {
      if (code == QualityType.INDEL)
        return "indel";
      if (code == QualityType.SNP)
        return "snp";
      if (code == QualityType.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(QualityType code) {
      return code.getSystem();
      }
    }

    public enum RepositoryType {
        /**
         * When URL is clicked, the resource can be seen directly (by webpage or by download link format).
         */
        DIRECTLINK, 
        /**
         * When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can be seen directly (usually in JSON or XML format).
         */
        OPENAPI, 
        /**
         * When logged into the website, the resource can be seen.
         */
        LOGIN, 
        /**
         * When logged in and  follow the API in the website related with URL, the resource can be seen.
         */
        OAUTH, 
        /**
         * Some other complicated or particular way to get resource from URL.
         */
        OTHER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static RepositoryType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("directlink".equals(codeString))
          return DIRECTLINK;
        if ("openapi".equals(codeString))
          return OPENAPI;
        if ("login".equals(codeString))
          return LOGIN;
        if ("oauth".equals(codeString))
          return OAUTH;
        if ("other".equals(codeString))
          return OTHER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown RepositoryType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DIRECTLINK: return "directlink";
            case OPENAPI: return "openapi";
            case LOGIN: return "login";
            case OAUTH: return "oauth";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DIRECTLINK: return "http://hl7.org/fhir/repository-type";
            case OPENAPI: return "http://hl7.org/fhir/repository-type";
            case LOGIN: return "http://hl7.org/fhir/repository-type";
            case OAUTH: return "http://hl7.org/fhir/repository-type";
            case OTHER: return "http://hl7.org/fhir/repository-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DIRECTLINK: return "When URL is clicked, the resource can be seen directly (by webpage or by download link format).";
            case OPENAPI: return "When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can be seen directly (usually in JSON or XML format).";
            case LOGIN: return "When logged into the website, the resource can be seen.";
            case OAUTH: return "When logged in and  follow the API in the website related with URL, the resource can be seen.";
            case OTHER: return "Some other complicated or particular way to get resource from URL.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DIRECTLINK: return "Click and see";
            case OPENAPI: return "The URL is the RESTful or other kind of API that can access to the result.";
            case LOGIN: return "Result cannot be access unless an account is logged in";
            case OAUTH: return "Result need to be fetched with API and need LOGIN( or cookies are required when visiting the link of resource)";
            case OTHER: return "Some other complicated or particular way to get resource from URL.";
            default: return "?";
          }
        }
    }

  public static class RepositoryTypeEnumFactory implements EnumFactory<RepositoryType> {
    public RepositoryType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("directlink".equals(codeString))
          return RepositoryType.DIRECTLINK;
        if ("openapi".equals(codeString))
          return RepositoryType.OPENAPI;
        if ("login".equals(codeString))
          return RepositoryType.LOGIN;
        if ("oauth".equals(codeString))
          return RepositoryType.OAUTH;
        if ("other".equals(codeString))
          return RepositoryType.OTHER;
        throw new IllegalArgumentException("Unknown RepositoryType code '"+codeString+"'");
        }
        public Enumeration<RepositoryType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<RepositoryType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("directlink".equals(codeString))
          return new Enumeration<RepositoryType>(this, RepositoryType.DIRECTLINK);
        if ("openapi".equals(codeString))
          return new Enumeration<RepositoryType>(this, RepositoryType.OPENAPI);
        if ("login".equals(codeString))
          return new Enumeration<RepositoryType>(this, RepositoryType.LOGIN);
        if ("oauth".equals(codeString))
          return new Enumeration<RepositoryType>(this, RepositoryType.OAUTH);
        if ("other".equals(codeString))
          return new Enumeration<RepositoryType>(this, RepositoryType.OTHER);
        throw new FHIRException("Unknown RepositoryType code '"+codeString+"'");
        }
    public String toCode(RepositoryType code) {
      if (code == RepositoryType.DIRECTLINK)
        return "directlink";
      if (code == RepositoryType.OPENAPI)
        return "openapi";
      if (code == RepositoryType.LOGIN)
        return "login";
      if (code == RepositoryType.OAUTH)
        return "oauth";
      if (code == RepositoryType.OTHER)
        return "other";
      return "?";
      }
    public String toSystem(RepositoryType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MolecularSequenceReferenceSeqComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340)).
         */
        @Child(name = "chromosome", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Chromosome containing genetic finding", formalDefinition="Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340))." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/chromosome-human")
        protected CodeableConcept chromosome;

        /**
         * The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.
         */
        @Child(name = "genomeBuild", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'", formalDefinition="The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used." )
        protected StringType genomeBuild;

        /**
         * A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.
         */
        @Child(name = "orientation", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="sense | antisense", formalDefinition="A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the \"sense\" strand, and the opposite complementary strand is the \"antisense\" strand." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/orientation-type")
        protected Enumeration<OrientationType> orientation;

        /**
         * Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.
         */
        @Child(name = "referenceSeqId", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference identifier", formalDefinition="Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sequence-referenceSeq")
        protected CodeableConcept referenceSeqId;

        /**
         * A pointer to another MolecularSequence entity as reference sequence.
         */
        @Child(name = "referenceSeqPointer", type = {MolecularSequence.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A pointer to another MolecularSequence entity as reference sequence", formalDefinition="A pointer to another MolecularSequence entity as reference sequence." )
        protected Reference referenceSeqPointer;

        /**
         * The actual object that is the target of the reference (A pointer to another MolecularSequence entity as reference sequence.)
         */
        protected MolecularSequence referenceSeqPointerTarget;

        /**
         * A string like "ACGT".
         */
        @Child(name = "referenceSeqString", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A string to represent reference sequence", formalDefinition="A string like \"ACGT\"." )
        protected StringType referenceSeqString;

        /**
         * An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm.
         */
        @Child(name = "strand", type = {CodeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="watson | crick", formalDefinition="An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/strand-type")
        protected Enumeration<StrandType> strand;

        /**
         * Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        @Child(name = "windowStart", type = {IntegerType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position of the window on the  reference sequence", formalDefinition="Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive." )
        protected IntegerType windowStart;

        /**
         * End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "windowEnd", type = {IntegerType.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position of the window on the reference sequence", formalDefinition="End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
        protected IntegerType windowEnd;

        private static final long serialVersionUID = 307364267L;

    /**
     * Constructor
     */
      public MolecularSequenceReferenceSeqComponent() {
        super();
      }

        /**
         * @return {@link #chromosome} (Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340)).)
         */
        public CodeableConcept getChromosome() { 
          if (this.chromosome == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.chromosome");
            else if (Configuration.doAutoCreate())
              this.chromosome = new CodeableConcept(); // cc
          return this.chromosome;
        }

        public boolean hasChromosome() { 
          return this.chromosome != null && !this.chromosome.isEmpty();
        }

        /**
         * @param value {@link #chromosome} (Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340)).)
         */
        public MolecularSequenceReferenceSeqComponent setChromosome(CodeableConcept value) { 
          this.chromosome = value;
          return this;
        }

        /**
         * @return {@link #genomeBuild} (The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.). This is the underlying object with id, value and extensions. The accessor "getGenomeBuild" gives direct access to the value
         */
        public StringType getGenomeBuildElement() { 
          if (this.genomeBuild == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.genomeBuild");
            else if (Configuration.doAutoCreate())
              this.genomeBuild = new StringType(); // bb
          return this.genomeBuild;
        }

        public boolean hasGenomeBuildElement() { 
          return this.genomeBuild != null && !this.genomeBuild.isEmpty();
        }

        public boolean hasGenomeBuild() { 
          return this.genomeBuild != null && !this.genomeBuild.isEmpty();
        }

        /**
         * @param value {@link #genomeBuild} (The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.). This is the underlying object with id, value and extensions. The accessor "getGenomeBuild" gives direct access to the value
         */
        public MolecularSequenceReferenceSeqComponent setGenomeBuildElement(StringType value) { 
          this.genomeBuild = value;
          return this;
        }

        /**
         * @return The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.
         */
        public String getGenomeBuild() { 
          return this.genomeBuild == null ? null : this.genomeBuild.getValue();
        }

        /**
         * @param value The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.
         */
        public MolecularSequenceReferenceSeqComponent setGenomeBuild(String value) { 
          if (Utilities.noString(value))
            this.genomeBuild = null;
          else {
            if (this.genomeBuild == null)
              this.genomeBuild = new StringType();
            this.genomeBuild.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #orientation} (A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.). This is the underlying object with id, value and extensions. The accessor "getOrientation" gives direct access to the value
         */
        public Enumeration<OrientationType> getOrientationElement() { 
          if (this.orientation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.orientation");
            else if (Configuration.doAutoCreate())
              this.orientation = new Enumeration<OrientationType>(new OrientationTypeEnumFactory()); // bb
          return this.orientation;
        }

        public boolean hasOrientationElement() { 
          return this.orientation != null && !this.orientation.isEmpty();
        }

        public boolean hasOrientation() { 
          return this.orientation != null && !this.orientation.isEmpty();
        }

        /**
         * @param value {@link #orientation} (A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.). This is the underlying object with id, value and extensions. The accessor "getOrientation" gives direct access to the value
         */
        public MolecularSequenceReferenceSeqComponent setOrientationElement(Enumeration<OrientationType> value) { 
          this.orientation = value;
          return this;
        }

        /**
         * @return A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.
         */
        public OrientationType getOrientation() { 
          return this.orientation == null ? null : this.orientation.getValue();
        }

        /**
         * @param value A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the "sense" strand, and the opposite complementary strand is the "antisense" strand.
         */
        public MolecularSequenceReferenceSeqComponent setOrientation(OrientationType value) { 
          if (value == null)
            this.orientation = null;
          else {
            if (this.orientation == null)
              this.orientation = new Enumeration<OrientationType>(new OrientationTypeEnumFactory());
            this.orientation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #referenceSeqId} (Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.)
         */
        public CodeableConcept getReferenceSeqId() { 
          if (this.referenceSeqId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.referenceSeqId");
            else if (Configuration.doAutoCreate())
              this.referenceSeqId = new CodeableConcept(); // cc
          return this.referenceSeqId;
        }

        public boolean hasReferenceSeqId() { 
          return this.referenceSeqId != null && !this.referenceSeqId.isEmpty();
        }

        /**
         * @param value {@link #referenceSeqId} (Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.)
         */
        public MolecularSequenceReferenceSeqComponent setReferenceSeqId(CodeableConcept value) { 
          this.referenceSeqId = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqPointer} (A pointer to another MolecularSequence entity as reference sequence.)
         */
        public Reference getReferenceSeqPointer() { 
          if (this.referenceSeqPointer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.referenceSeqPointer");
            else if (Configuration.doAutoCreate())
              this.referenceSeqPointer = new Reference(); // cc
          return this.referenceSeqPointer;
        }

        public boolean hasReferenceSeqPointer() { 
          return this.referenceSeqPointer != null && !this.referenceSeqPointer.isEmpty();
        }

        /**
         * @param value {@link #referenceSeqPointer} (A pointer to another MolecularSequence entity as reference sequence.)
         */
        public MolecularSequenceReferenceSeqComponent setReferenceSeqPointer(Reference value) { 
          this.referenceSeqPointer = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqPointer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A pointer to another MolecularSequence entity as reference sequence.)
         */
        public MolecularSequence getReferenceSeqPointerTarget() { 
          if (this.referenceSeqPointerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.referenceSeqPointer");
            else if (Configuration.doAutoCreate())
              this.referenceSeqPointerTarget = new MolecularSequence(); // aa
          return this.referenceSeqPointerTarget;
        }

        /**
         * @param value {@link #referenceSeqPointer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A pointer to another MolecularSequence entity as reference sequence.)
         */
        public MolecularSequenceReferenceSeqComponent setReferenceSeqPointerTarget(MolecularSequence value) { 
          this.referenceSeqPointerTarget = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqString} (A string like "ACGT".). This is the underlying object with id, value and extensions. The accessor "getReferenceSeqString" gives direct access to the value
         */
        public StringType getReferenceSeqStringElement() { 
          if (this.referenceSeqString == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.referenceSeqString");
            else if (Configuration.doAutoCreate())
              this.referenceSeqString = new StringType(); // bb
          return this.referenceSeqString;
        }

        public boolean hasReferenceSeqStringElement() { 
          return this.referenceSeqString != null && !this.referenceSeqString.isEmpty();
        }

        public boolean hasReferenceSeqString() { 
          return this.referenceSeqString != null && !this.referenceSeqString.isEmpty();
        }

        /**
         * @param value {@link #referenceSeqString} (A string like "ACGT".). This is the underlying object with id, value and extensions. The accessor "getReferenceSeqString" gives direct access to the value
         */
        public MolecularSequenceReferenceSeqComponent setReferenceSeqStringElement(StringType value) { 
          this.referenceSeqString = value;
          return this;
        }

        /**
         * @return A string like "ACGT".
         */
        public String getReferenceSeqString() { 
          return this.referenceSeqString == null ? null : this.referenceSeqString.getValue();
        }

        /**
         * @param value A string like "ACGT".
         */
        public MolecularSequenceReferenceSeqComponent setReferenceSeqString(String value) { 
          if (Utilities.noString(value))
            this.referenceSeqString = null;
          else {
            if (this.referenceSeqString == null)
              this.referenceSeqString = new StringType();
            this.referenceSeqString.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #strand} (An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm.). This is the underlying object with id, value and extensions. The accessor "getStrand" gives direct access to the value
         */
        public Enumeration<StrandType> getStrandElement() { 
          if (this.strand == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.strand");
            else if (Configuration.doAutoCreate())
              this.strand = new Enumeration<StrandType>(new StrandTypeEnumFactory()); // bb
          return this.strand;
        }

        public boolean hasStrandElement() { 
          return this.strand != null && !this.strand.isEmpty();
        }

        public boolean hasStrand() { 
          return this.strand != null && !this.strand.isEmpty();
        }

        /**
         * @param value {@link #strand} (An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm.). This is the underlying object with id, value and extensions. The accessor "getStrand" gives direct access to the value
         */
        public MolecularSequenceReferenceSeqComponent setStrandElement(Enumeration<StrandType> value) { 
          this.strand = value;
          return this;
        }

        /**
         * @return An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm.
         */
        public StrandType getStrand() { 
          return this.strand == null ? null : this.strand.getValue();
        }

        /**
         * @param value An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm.
         */
        public MolecularSequenceReferenceSeqComponent setStrand(StrandType value) { 
          if (value == null)
            this.strand = null;
          else {
            if (this.strand == null)
              this.strand = new Enumeration<StrandType>(new StrandTypeEnumFactory());
            this.strand.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #windowStart} (Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getWindowStart" gives direct access to the value
         */
        public IntegerType getWindowStartElement() { 
          if (this.windowStart == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.windowStart");
            else if (Configuration.doAutoCreate())
              this.windowStart = new IntegerType(); // bb
          return this.windowStart;
        }

        public boolean hasWindowStartElement() { 
          return this.windowStart != null && !this.windowStart.isEmpty();
        }

        public boolean hasWindowStart() { 
          return this.windowStart != null && !this.windowStart.isEmpty();
        }

        /**
         * @param value {@link #windowStart} (Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getWindowStart" gives direct access to the value
         */
        public MolecularSequenceReferenceSeqComponent setWindowStartElement(IntegerType value) { 
          this.windowStart = value;
          return this;
        }

        /**
         * @return Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public int getWindowStart() { 
          return this.windowStart == null || this.windowStart.isEmpty() ? 0 : this.windowStart.getValue();
        }

        /**
         * @param value Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public MolecularSequenceReferenceSeqComponent setWindowStart(int value) { 
            if (this.windowStart == null)
              this.windowStart = new IntegerType();
            this.windowStart.setValue(value);
          return this;
        }

        /**
         * @return {@link #windowEnd} (End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
         */
        public IntegerType getWindowEndElement() { 
          if (this.windowEnd == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceReferenceSeqComponent.windowEnd");
            else if (Configuration.doAutoCreate())
              this.windowEnd = new IntegerType(); // bb
          return this.windowEnd;
        }

        public boolean hasWindowEndElement() { 
          return this.windowEnd != null && !this.windowEnd.isEmpty();
        }

        public boolean hasWindowEnd() { 
          return this.windowEnd != null && !this.windowEnd.isEmpty();
        }

        /**
         * @param value {@link #windowEnd} (End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
         */
        public MolecularSequenceReferenceSeqComponent setWindowEndElement(IntegerType value) { 
          this.windowEnd = value;
          return this;
        }

        /**
         * @return End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getWindowEnd() { 
          return this.windowEnd == null || this.windowEnd.isEmpty() ? 0 : this.windowEnd.getValue();
        }

        /**
         * @param value End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public MolecularSequenceReferenceSeqComponent setWindowEnd(int value) { 
            if (this.windowEnd == null)
              this.windowEnd = new IntegerType();
            this.windowEnd.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("chromosome", "CodeableConcept", "Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340)).", 0, 1, chromosome));
          children.add(new Property("genomeBuild", "string", "The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.", 0, 1, genomeBuild));
          children.add(new Property("orientation", "code", "A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the \"sense\" strand, and the opposite complementary strand is the \"antisense\" strand.", 0, 1, orientation));
          children.add(new Property("referenceSeqId", "CodeableConcept", "Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.", 0, 1, referenceSeqId));
          children.add(new Property("referenceSeqPointer", "Reference(MolecularSequence)", "A pointer to another MolecularSequence entity as reference sequence.", 0, 1, referenceSeqPointer));
          children.add(new Property("referenceSeqString", "string", "A string like \"ACGT\".", 0, 1, referenceSeqString));
          children.add(new Property("strand", "code", "An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm.", 0, 1, strand));
          children.add(new Property("windowStart", "integer", "Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, windowStart));
          children.add(new Property("windowEnd", "integer", "End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, windowEnd));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1499470472: /*chromosome*/  return new Property("chromosome", "CodeableConcept", "Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340)).", 0, 1, chromosome);
          case 1061239735: /*genomeBuild*/  return new Property("genomeBuild", "string", "The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.", 0, 1, genomeBuild);
          case -1439500848: /*orientation*/  return new Property("orientation", "code", "A relative reference to a DNA strand based on gene orientation. The strand that contains the open reading frame of the gene is the \"sense\" strand, and the opposite complementary strand is the \"antisense\" strand.", 0, 1, orientation);
          case -1911500465: /*referenceSeqId*/  return new Property("referenceSeqId", "CodeableConcept", "Reference identifier of reference sequence submitted to NCBI. It must match the type in the MolecularSequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.", 0, 1, referenceSeqId);
          case 1923414665: /*referenceSeqPointer*/  return new Property("referenceSeqPointer", "Reference(MolecularSequence)", "A pointer to another MolecularSequence entity as reference sequence.", 0, 1, referenceSeqPointer);
          case -1648301499: /*referenceSeqString*/  return new Property("referenceSeqString", "string", "A string like \"ACGT\".", 0, 1, referenceSeqString);
          case -891993594: /*strand*/  return new Property("strand", "code", "An absolute reference to a strand. The Watson strand is the strand whose 5'-end is on the short arm of the chromosome, and the Crick strand as the one whose 5'-end is on the long arm.", 0, 1, strand);
          case 1903685202: /*windowStart*/  return new Property("windowStart", "integer", "Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, windowStart);
          case -217026869: /*windowEnd*/  return new Property("windowEnd", "integer", "End position of the window on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, windowEnd);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1499470472: /*chromosome*/ return this.chromosome == null ? new Base[0] : new Base[] {this.chromosome}; // CodeableConcept
        case 1061239735: /*genomeBuild*/ return this.genomeBuild == null ? new Base[0] : new Base[] {this.genomeBuild}; // StringType
        case -1439500848: /*orientation*/ return this.orientation == null ? new Base[0] : new Base[] {this.orientation}; // Enumeration<OrientationType>
        case -1911500465: /*referenceSeqId*/ return this.referenceSeqId == null ? new Base[0] : new Base[] {this.referenceSeqId}; // CodeableConcept
        case 1923414665: /*referenceSeqPointer*/ return this.referenceSeqPointer == null ? new Base[0] : new Base[] {this.referenceSeqPointer}; // Reference
        case -1648301499: /*referenceSeqString*/ return this.referenceSeqString == null ? new Base[0] : new Base[] {this.referenceSeqString}; // StringType
        case -891993594: /*strand*/ return this.strand == null ? new Base[0] : new Base[] {this.strand}; // Enumeration<StrandType>
        case 1903685202: /*windowStart*/ return this.windowStart == null ? new Base[0] : new Base[] {this.windowStart}; // IntegerType
        case -217026869: /*windowEnd*/ return this.windowEnd == null ? new Base[0] : new Base[] {this.windowEnd}; // IntegerType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1499470472: // chromosome
          this.chromosome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1061239735: // genomeBuild
          this.genomeBuild = castToString(value); // StringType
          return value;
        case -1439500848: // orientation
          value = new OrientationTypeEnumFactory().fromType(castToCode(value));
          this.orientation = (Enumeration) value; // Enumeration<OrientationType>
          return value;
        case -1911500465: // referenceSeqId
          this.referenceSeqId = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1923414665: // referenceSeqPointer
          this.referenceSeqPointer = castToReference(value); // Reference
          return value;
        case -1648301499: // referenceSeqString
          this.referenceSeqString = castToString(value); // StringType
          return value;
        case -891993594: // strand
          value = new StrandTypeEnumFactory().fromType(castToCode(value));
          this.strand = (Enumeration) value; // Enumeration<StrandType>
          return value;
        case 1903685202: // windowStart
          this.windowStart = castToInteger(value); // IntegerType
          return value;
        case -217026869: // windowEnd
          this.windowEnd = castToInteger(value); // IntegerType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("chromosome")) {
          this.chromosome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("genomeBuild")) {
          this.genomeBuild = castToString(value); // StringType
        } else if (name.equals("orientation")) {
          value = new OrientationTypeEnumFactory().fromType(castToCode(value));
          this.orientation = (Enumeration) value; // Enumeration<OrientationType>
        } else if (name.equals("referenceSeqId")) {
          this.referenceSeqId = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("referenceSeqPointer")) {
          this.referenceSeqPointer = castToReference(value); // Reference
        } else if (name.equals("referenceSeqString")) {
          this.referenceSeqString = castToString(value); // StringType
        } else if (name.equals("strand")) {
          value = new StrandTypeEnumFactory().fromType(castToCode(value));
          this.strand = (Enumeration) value; // Enumeration<StrandType>
        } else if (name.equals("windowStart")) {
          this.windowStart = castToInteger(value); // IntegerType
        } else if (name.equals("windowEnd")) {
          this.windowEnd = castToInteger(value); // IntegerType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1499470472:  return getChromosome(); 
        case 1061239735:  return getGenomeBuildElement();
        case -1439500848:  return getOrientationElement();
        case -1911500465:  return getReferenceSeqId(); 
        case 1923414665:  return getReferenceSeqPointer(); 
        case -1648301499:  return getReferenceSeqStringElement();
        case -891993594:  return getStrandElement();
        case 1903685202:  return getWindowStartElement();
        case -217026869:  return getWindowEndElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1499470472: /*chromosome*/ return new String[] {"CodeableConcept"};
        case 1061239735: /*genomeBuild*/ return new String[] {"string"};
        case -1439500848: /*orientation*/ return new String[] {"code"};
        case -1911500465: /*referenceSeqId*/ return new String[] {"CodeableConcept"};
        case 1923414665: /*referenceSeqPointer*/ return new String[] {"Reference"};
        case -1648301499: /*referenceSeqString*/ return new String[] {"string"};
        case -891993594: /*strand*/ return new String[] {"code"};
        case 1903685202: /*windowStart*/ return new String[] {"integer"};
        case -217026869: /*windowEnd*/ return new String[] {"integer"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("chromosome")) {
          this.chromosome = new CodeableConcept();
          return this.chromosome;
        }
        else if (name.equals("genomeBuild")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.genomeBuild");
        }
        else if (name.equals("orientation")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.orientation");
        }
        else if (name.equals("referenceSeqId")) {
          this.referenceSeqId = new CodeableConcept();
          return this.referenceSeqId;
        }
        else if (name.equals("referenceSeqPointer")) {
          this.referenceSeqPointer = new Reference();
          return this.referenceSeqPointer;
        }
        else if (name.equals("referenceSeqString")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.referenceSeqString");
        }
        else if (name.equals("strand")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.strand");
        }
        else if (name.equals("windowStart")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.windowStart");
        }
        else if (name.equals("windowEnd")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.windowEnd");
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceReferenceSeqComponent copy() {
        MolecularSequenceReferenceSeqComponent dst = new MolecularSequenceReferenceSeqComponent();
        copyValues(dst);
        dst.chromosome = chromosome == null ? null : chromosome.copy();
        dst.genomeBuild = genomeBuild == null ? null : genomeBuild.copy();
        dst.orientation = orientation == null ? null : orientation.copy();
        dst.referenceSeqId = referenceSeqId == null ? null : referenceSeqId.copy();
        dst.referenceSeqPointer = referenceSeqPointer == null ? null : referenceSeqPointer.copy();
        dst.referenceSeqString = referenceSeqString == null ? null : referenceSeqString.copy();
        dst.strand = strand == null ? null : strand.copy();
        dst.windowStart = windowStart == null ? null : windowStart.copy();
        dst.windowEnd = windowEnd == null ? null : windowEnd.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceReferenceSeqComponent))
          return false;
        MolecularSequenceReferenceSeqComponent o = (MolecularSequenceReferenceSeqComponent) other_;
        return compareDeep(chromosome, o.chromosome, true) && compareDeep(genomeBuild, o.genomeBuild, true)
           && compareDeep(orientation, o.orientation, true) && compareDeep(referenceSeqId, o.referenceSeqId, true)
           && compareDeep(referenceSeqPointer, o.referenceSeqPointer, true) && compareDeep(referenceSeqString, o.referenceSeqString, true)
           && compareDeep(strand, o.strand, true) && compareDeep(windowStart, o.windowStart, true) && compareDeep(windowEnd, o.windowEnd, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceReferenceSeqComponent))
          return false;
        MolecularSequenceReferenceSeqComponent o = (MolecularSequenceReferenceSeqComponent) other_;
        return compareValues(genomeBuild, o.genomeBuild, true) && compareValues(orientation, o.orientation, true)
           && compareValues(referenceSeqString, o.referenceSeqString, true) && compareValues(strand, o.strand, true)
           && compareValues(windowStart, o.windowStart, true) && compareValues(windowEnd, o.windowEnd, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(chromosome, genomeBuild, orientation
          , referenceSeqId, referenceSeqPointer, referenceSeqString, strand, windowStart, windowEnd
          );
      }

  public String fhirType() {
    return "MolecularSequence.referenceSeq";

  }

  }

    @Block()
    public static class MolecularSequenceVariantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position of the variant on the  reference sequence", formalDefinition="Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive." )
        protected IntegerType start;

        /**
         * End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position of the variant on the reference sequence", formalDefinition="End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
        protected IntegerType end;

        /**
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         */
        @Child(name = "observedAllele", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Allele that was observed", formalDefinition="An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end." )
        protected StringType observedAllele;

        /**
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         */
        @Child(name = "referenceAllele", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Allele in the reference sequence", formalDefinition="An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end." )
        protected StringType referenceAllele;

        /**
         * Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).
         */
        @Child(name = "cigar", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Extended CIGAR string for aligning the sequence with reference bases", formalDefinition="Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm)." )
        protected StringType cigar;

        /**
         * A pointer to an Observation containing variant information.
         */
        @Child(name = "variantPointer", type = {Observation.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Pointer to observed variant information", formalDefinition="A pointer to an Observation containing variant information." )
        protected Reference variantPointer;

        /**
         * The actual object that is the target of the reference (A pointer to an Observation containing variant information.)
         */
        protected Observation variantPointerTarget;

        private static final long serialVersionUID = 105611837L;

    /**
     * Constructor
     */
      public MolecularSequenceVariantComponent() {
        super();
      }

        /**
         * @return {@link #start} (Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceVariantComponent.start");
            else if (Configuration.doAutoCreate())
              this.start = new IntegerType(); // bb
          return this.start;
        }

        public boolean hasStartElement() { 
          return this.start != null && !this.start.isEmpty();
        }

        public boolean hasStart() { 
          return this.start != null && !this.start.isEmpty();
        }

        /**
         * @param value {@link #start} (Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public MolecularSequenceVariantComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public MolecularSequenceVariantComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceVariantComponent.end");
            else if (Configuration.doAutoCreate())
              this.end = new IntegerType(); // bb
          return this.end;
        }

        public boolean hasEndElement() { 
          return this.end != null && !this.end.isEmpty();
        }

        public boolean hasEnd() { 
          return this.end != null && !this.end.isEmpty();
        }

        /**
         * @param value {@link #end} (End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public MolecularSequenceVariantComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public MolecularSequenceVariantComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        /**
         * @return {@link #observedAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.). This is the underlying object with id, value and extensions. The accessor "getObservedAllele" gives direct access to the value
         */
        public StringType getObservedAlleleElement() { 
          if (this.observedAllele == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceVariantComponent.observedAllele");
            else if (Configuration.doAutoCreate())
              this.observedAllele = new StringType(); // bb
          return this.observedAllele;
        }

        public boolean hasObservedAlleleElement() { 
          return this.observedAllele != null && !this.observedAllele.isEmpty();
        }

        public boolean hasObservedAllele() { 
          return this.observedAllele != null && !this.observedAllele.isEmpty();
        }

        /**
         * @param value {@link #observedAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.). This is the underlying object with id, value and extensions. The accessor "getObservedAllele" gives direct access to the value
         */
        public MolecularSequenceVariantComponent setObservedAlleleElement(StringType value) { 
          this.observedAllele = value;
          return this;
        }

        /**
         * @return An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         */
        public String getObservedAllele() { 
          return this.observedAllele == null ? null : this.observedAllele.getValue();
        }

        /**
         * @param value An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         */
        public MolecularSequenceVariantComponent setObservedAllele(String value) { 
          if (Utilities.noString(value))
            this.observedAllele = null;
          else {
            if (this.observedAllele == null)
              this.observedAllele = new StringType();
            this.observedAllele.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #referenceAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.). This is the underlying object with id, value and extensions. The accessor "getReferenceAllele" gives direct access to the value
         */
        public StringType getReferenceAlleleElement() { 
          if (this.referenceAllele == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceVariantComponent.referenceAllele");
            else if (Configuration.doAutoCreate())
              this.referenceAllele = new StringType(); // bb
          return this.referenceAllele;
        }

        public boolean hasReferenceAlleleElement() { 
          return this.referenceAllele != null && !this.referenceAllele.isEmpty();
        }

        public boolean hasReferenceAllele() { 
          return this.referenceAllele != null && !this.referenceAllele.isEmpty();
        }

        /**
         * @param value {@link #referenceAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.). This is the underlying object with id, value and extensions. The accessor "getReferenceAllele" gives direct access to the value
         */
        public MolecularSequenceVariantComponent setReferenceAlleleElement(StringType value) { 
          this.referenceAllele = value;
          return this;
        }

        /**
         * @return An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         */
        public String getReferenceAllele() { 
          return this.referenceAllele == null ? null : this.referenceAllele.getValue();
        }

        /**
         * @param value An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.
         */
        public MolecularSequenceVariantComponent setReferenceAllele(String value) { 
          if (Utilities.noString(value))
            this.referenceAllele = null;
          else {
            if (this.referenceAllele == null)
              this.referenceAllele = new StringType();
            this.referenceAllele.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #cigar} (Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).). This is the underlying object with id, value and extensions. The accessor "getCigar" gives direct access to the value
         */
        public StringType getCigarElement() { 
          if (this.cigar == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceVariantComponent.cigar");
            else if (Configuration.doAutoCreate())
              this.cigar = new StringType(); // bb
          return this.cigar;
        }

        public boolean hasCigarElement() { 
          return this.cigar != null && !this.cigar.isEmpty();
        }

        public boolean hasCigar() { 
          return this.cigar != null && !this.cigar.isEmpty();
        }

        /**
         * @param value {@link #cigar} (Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).). This is the underlying object with id, value and extensions. The accessor "getCigar" gives direct access to the value
         */
        public MolecularSequenceVariantComponent setCigarElement(StringType value) { 
          this.cigar = value;
          return this;
        }

        /**
         * @return Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).
         */
        public String getCigar() { 
          return this.cigar == null ? null : this.cigar.getValue();
        }

        /**
         * @param value Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).
         */
        public MolecularSequenceVariantComponent setCigar(String value) { 
          if (Utilities.noString(value))
            this.cigar = null;
          else {
            if (this.cigar == null)
              this.cigar = new StringType();
            this.cigar.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #variantPointer} (A pointer to an Observation containing variant information.)
         */
        public Reference getVariantPointer() { 
          if (this.variantPointer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceVariantComponent.variantPointer");
            else if (Configuration.doAutoCreate())
              this.variantPointer = new Reference(); // cc
          return this.variantPointer;
        }

        public boolean hasVariantPointer() { 
          return this.variantPointer != null && !this.variantPointer.isEmpty();
        }

        /**
         * @param value {@link #variantPointer} (A pointer to an Observation containing variant information.)
         */
        public MolecularSequenceVariantComponent setVariantPointer(Reference value) { 
          this.variantPointer = value;
          return this;
        }

        /**
         * @return {@link #variantPointer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A pointer to an Observation containing variant information.)
         */
        public Observation getVariantPointerTarget() { 
          if (this.variantPointerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceVariantComponent.variantPointer");
            else if (Configuration.doAutoCreate())
              this.variantPointerTarget = new Observation(); // aa
          return this.variantPointerTarget;
        }

        /**
         * @param value {@link #variantPointer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A pointer to an Observation containing variant information.)
         */
        public MolecularSequenceVariantComponent setVariantPointerTarget(Observation value) { 
          this.variantPointerTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("start", "integer", "Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start));
          children.add(new Property("end", "integer", "End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end));
          children.add(new Property("observedAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.", 0, 1, observedAllele));
          children.add(new Property("referenceAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.", 0, 1, referenceAllele));
          children.add(new Property("cigar", "string", "Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).", 0, 1, cigar));
          children.add(new Property("variantPointer", "Reference(Observation)", "A pointer to an Observation containing variant information.", 0, 1, variantPointer));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 109757538: /*start*/  return new Property("start", "integer", "Start position of the variant on the  reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start);
          case 100571: /*end*/  return new Property("end", "integer", "End position of the variant on the reference sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end);
          case -1418745787: /*observedAllele*/  return new Property("observedAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.", 0, 1, observedAllele);
          case 364045960: /*referenceAllele*/  return new Property("referenceAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.", 0, 1, referenceAllele);
          case 94658738: /*cigar*/  return new Property("cigar", "string", "Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).", 0, 1, cigar);
          case -1654319624: /*variantPointer*/  return new Property("variantPointer", "Reference(Observation)", "A pointer to an Observation containing variant information.", 0, 1, variantPointer);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // IntegerType
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // IntegerType
        case -1418745787: /*observedAllele*/ return this.observedAllele == null ? new Base[0] : new Base[] {this.observedAllele}; // StringType
        case 364045960: /*referenceAllele*/ return this.referenceAllele == null ? new Base[0] : new Base[] {this.referenceAllele}; // StringType
        case 94658738: /*cigar*/ return this.cigar == null ? new Base[0] : new Base[] {this.cigar}; // StringType
        case -1654319624: /*variantPointer*/ return this.variantPointer == null ? new Base[0] : new Base[] {this.variantPointer}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          return value;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          return value;
        case -1418745787: // observedAllele
          this.observedAllele = castToString(value); // StringType
          return value;
        case 364045960: // referenceAllele
          this.referenceAllele = castToString(value); // StringType
          return value;
        case 94658738: // cigar
          this.cigar = castToString(value); // StringType
          return value;
        case -1654319624: // variantPointer
          this.variantPointer = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start")) {
          this.start = castToInteger(value); // IntegerType
        } else if (name.equals("end")) {
          this.end = castToInteger(value); // IntegerType
        } else if (name.equals("observedAllele")) {
          this.observedAllele = castToString(value); // StringType
        } else if (name.equals("referenceAllele")) {
          this.referenceAllele = castToString(value); // StringType
        } else if (name.equals("cigar")) {
          this.cigar = castToString(value); // StringType
        } else if (name.equals("variantPointer")) {
          this.variantPointer = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538:  return getStartElement();
        case 100571:  return getEndElement();
        case -1418745787:  return getObservedAlleleElement();
        case 364045960:  return getReferenceAlleleElement();
        case 94658738:  return getCigarElement();
        case -1654319624:  return getVariantPointer(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return new String[] {"integer"};
        case 100571: /*end*/ return new String[] {"integer"};
        case -1418745787: /*observedAllele*/ return new String[] {"string"};
        case 364045960: /*referenceAllele*/ return new String[] {"string"};
        case 94658738: /*cigar*/ return new String[] {"string"};
        case -1654319624: /*variantPointer*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.end");
        }
        else if (name.equals("observedAllele")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.observedAllele");
        }
        else if (name.equals("referenceAllele")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.referenceAllele");
        }
        else if (name.equals("cigar")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.cigar");
        }
        else if (name.equals("variantPointer")) {
          this.variantPointer = new Reference();
          return this.variantPointer;
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceVariantComponent copy() {
        MolecularSequenceVariantComponent dst = new MolecularSequenceVariantComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.observedAllele = observedAllele == null ? null : observedAllele.copy();
        dst.referenceAllele = referenceAllele == null ? null : referenceAllele.copy();
        dst.cigar = cigar == null ? null : cigar.copy();
        dst.variantPointer = variantPointer == null ? null : variantPointer.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceVariantComponent))
          return false;
        MolecularSequenceVariantComponent o = (MolecularSequenceVariantComponent) other_;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true) && compareDeep(observedAllele, o.observedAllele, true)
           && compareDeep(referenceAllele, o.referenceAllele, true) && compareDeep(cigar, o.cigar, true) && compareDeep(variantPointer, o.variantPointer, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceVariantComponent))
          return false;
        MolecularSequenceVariantComponent o = (MolecularSequenceVariantComponent) other_;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(observedAllele, o.observedAllele, true)
           && compareValues(referenceAllele, o.referenceAllele, true) && compareValues(cigar, o.cigar, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(start, end, observedAllele
          , referenceAllele, cigar, variantPointer);
      }

  public String fhirType() {
    return "MolecularSequence.variant";

  }

  }

    @Block()
    public static class MolecularSequenceQualityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * INDEL / SNP / Undefined variant.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="indel | snp | unknown", formalDefinition="INDEL / SNP / Undefined variant." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/quality-type")
        protected Enumeration<QualityType> type;

        /**
         * Gold standard sequence used for comparing against.
         */
        @Child(name = "standardSequence", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Standard sequence for comparison", formalDefinition="Gold standard sequence used for comparing against." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sequence-quality-standardSequence")
        protected CodeableConcept standardSequence;

        /**
         * Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        @Child(name = "start", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position of the sequence", formalDefinition="Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive." )
        protected IntegerType start;

        /**
         * End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "end", type = {IntegerType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position of the sequence", formalDefinition="End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
        protected IntegerType end;

        /**
         * The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).
         */
        @Child(name = "score", type = {Quantity.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quality score for the comparison", formalDefinition="The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685))." )
        protected Quantity score;

        /**
         * Which method is used to get sequence quality.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Method to get quality", formalDefinition="Which method is used to get sequence quality." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sequence-quality-method")
        protected CodeableConcept method;

        /**
         * True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        @Child(name = "truthTP", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="True positives from the perspective of the truth data", formalDefinition="True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event." )
        protected DecimalType truthTP;

        /**
         * True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        @Child(name = "queryTP", type = {DecimalType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="True positives from the perspective of the query data", formalDefinition="True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event." )
        protected DecimalType queryTP;

        /**
         * False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.
         */
        @Child(name = "truthFN", type = {DecimalType.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="False negatives", formalDefinition="False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here." )
        protected DecimalType truthFN;

        /**
         * False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         */
        @Child(name = "queryFP", type = {DecimalType.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="False positives", formalDefinition="False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here." )
        protected DecimalType queryFP;

        /**
         * The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).
         */
        @Child(name = "gtFP", type = {DecimalType.class}, order=11, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="False positives where the non-REF alleles in the Truth and Query Call Sets match", formalDefinition="The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar)." )
        protected DecimalType gtFP;

        /**
         * QUERY.TP / (QUERY.TP + QUERY.FP).
         */
        @Child(name = "precision", type = {DecimalType.class}, order=12, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Precision of comparison", formalDefinition="QUERY.TP / (QUERY.TP + QUERY.FP)." )
        protected DecimalType precision;

        /**
         * TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         */
        @Child(name = "recall", type = {DecimalType.class}, order=13, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Recall of comparison", formalDefinition="TRUTH.TP / (TRUTH.TP + TRUTH.FN)." )
        protected DecimalType recall;

        /**
         * Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         */
        @Child(name = "fScore", type = {DecimalType.class}, order=14, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="F-score", formalDefinition="Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall)." )
        protected DecimalType fScore;

        /**
         * Receiver Operator Characteristic (ROC) Curve  to give sensitivity/specificity tradeoff.
         */
        @Child(name = "roc", type = {}, order=15, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Receiver Operator Characteristic (ROC) Curve", formalDefinition="Receiver Operator Characteristic (ROC) Curve  to give sensitivity/specificity tradeoff." )
        protected MolecularSequenceQualityRocComponent roc;

        private static final long serialVersionUID = -811933526L;

    /**
     * Constructor
     */
      public MolecularSequenceQualityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MolecularSequenceQualityComponent(Enumeration<QualityType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (INDEL / SNP / Undefined variant.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<QualityType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<QualityType>(new QualityTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (INDEL / SNP / Undefined variant.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setTypeElement(Enumeration<QualityType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return INDEL / SNP / Undefined variant.
         */
        public QualityType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value INDEL / SNP / Undefined variant.
         */
        public MolecularSequenceQualityComponent setType(QualityType value) { 
            if (this.type == null)
              this.type = new Enumeration<QualityType>(new QualityTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #standardSequence} (Gold standard sequence used for comparing against.)
         */
        public CodeableConcept getStandardSequence() { 
          if (this.standardSequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.standardSequence");
            else if (Configuration.doAutoCreate())
              this.standardSequence = new CodeableConcept(); // cc
          return this.standardSequence;
        }

        public boolean hasStandardSequence() { 
          return this.standardSequence != null && !this.standardSequence.isEmpty();
        }

        /**
         * @param value {@link #standardSequence} (Gold standard sequence used for comparing against.)
         */
        public MolecularSequenceQualityComponent setStandardSequence(CodeableConcept value) { 
          this.standardSequence = value;
          return this;
        }

        /**
         * @return {@link #start} (Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.start");
            else if (Configuration.doAutoCreate())
              this.start = new IntegerType(); // bb
          return this.start;
        }

        public boolean hasStartElement() { 
          return this.start != null && !this.start.isEmpty();
        }

        public boolean hasStart() { 
          return this.start != null && !this.start.isEmpty();
        }

        /**
         * @param value {@link #start} (Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public MolecularSequenceQualityComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.end");
            else if (Configuration.doAutoCreate())
              this.end = new IntegerType(); // bb
          return this.end;
        }

        public boolean hasEndElement() { 
          return this.end != null && !this.end.isEmpty();
        }

        public boolean hasEnd() { 
          return this.end != null && !this.end.isEmpty();
        }

        /**
         * @param value {@link #end} (End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public MolecularSequenceQualityComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        /**
         * @return {@link #score} (The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).)
         */
        public Quantity getScore() { 
          if (this.score == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.score");
            else if (Configuration.doAutoCreate())
              this.score = new Quantity(); // cc
          return this.score;
        }

        public boolean hasScore() { 
          return this.score != null && !this.score.isEmpty();
        }

        /**
         * @param value {@link #score} (The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).)
         */
        public MolecularSequenceQualityComponent setScore(Quantity value) { 
          this.score = value;
          return this;
        }

        /**
         * @return {@link #method} (Which method is used to get sequence quality.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (Which method is used to get sequence quality.)
         */
        public MolecularSequenceQualityComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #truthTP} (True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.). This is the underlying object with id, value and extensions. The accessor "getTruthTP" gives direct access to the value
         */
        public DecimalType getTruthTPElement() { 
          if (this.truthTP == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.truthTP");
            else if (Configuration.doAutoCreate())
              this.truthTP = new DecimalType(); // bb
          return this.truthTP;
        }

        public boolean hasTruthTPElement() { 
          return this.truthTP != null && !this.truthTP.isEmpty();
        }

        public boolean hasTruthTP() { 
          return this.truthTP != null && !this.truthTP.isEmpty();
        }

        /**
         * @param value {@link #truthTP} (True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.). This is the underlying object with id, value and extensions. The accessor "getTruthTP" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setTruthTPElement(DecimalType value) { 
          this.truthTP = value;
          return this;
        }

        /**
         * @return True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public BigDecimal getTruthTP() { 
          return this.truthTP == null ? null : this.truthTP.getValue();
        }

        /**
         * @param value True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public MolecularSequenceQualityComponent setTruthTP(BigDecimal value) { 
          if (value == null)
            this.truthTP = null;
          else {
            if (this.truthTP == null)
              this.truthTP = new DecimalType();
            this.truthTP.setValue(value);
          }
          return this;
        }

        /**
         * @param value True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public MolecularSequenceQualityComponent setTruthTP(long value) { 
              this.truthTP = new DecimalType();
            this.truthTP.setValue(value);
          return this;
        }

        /**
         * @param value True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public MolecularSequenceQualityComponent setTruthTP(double value) { 
              this.truthTP = new DecimalType();
            this.truthTP.setValue(value);
          return this;
        }

        /**
         * @return {@link #queryTP} (True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.). This is the underlying object with id, value and extensions. The accessor "getQueryTP" gives direct access to the value
         */
        public DecimalType getQueryTPElement() { 
          if (this.queryTP == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.queryTP");
            else if (Configuration.doAutoCreate())
              this.queryTP = new DecimalType(); // bb
          return this.queryTP;
        }

        public boolean hasQueryTPElement() { 
          return this.queryTP != null && !this.queryTP.isEmpty();
        }

        public boolean hasQueryTP() { 
          return this.queryTP != null && !this.queryTP.isEmpty();
        }

        /**
         * @param value {@link #queryTP} (True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.). This is the underlying object with id, value and extensions. The accessor "getQueryTP" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setQueryTPElement(DecimalType value) { 
          this.queryTP = value;
          return this;
        }

        /**
         * @return True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public BigDecimal getQueryTP() { 
          return this.queryTP == null ? null : this.queryTP.getValue();
        }

        /**
         * @param value True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public MolecularSequenceQualityComponent setQueryTP(BigDecimal value) { 
          if (value == null)
            this.queryTP = null;
          else {
            if (this.queryTP == null)
              this.queryTP = new DecimalType();
            this.queryTP.setValue(value);
          }
          return this;
        }

        /**
         * @param value True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public MolecularSequenceQualityComponent setQueryTP(long value) { 
              this.queryTP = new DecimalType();
            this.queryTP.setValue(value);
          return this;
        }

        /**
         * @param value True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public MolecularSequenceQualityComponent setQueryTP(double value) { 
              this.queryTP = new DecimalType();
            this.queryTP.setValue(value);
          return this;
        }

        /**
         * @return {@link #truthFN} (False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.). This is the underlying object with id, value and extensions. The accessor "getTruthFN" gives direct access to the value
         */
        public DecimalType getTruthFNElement() { 
          if (this.truthFN == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.truthFN");
            else if (Configuration.doAutoCreate())
              this.truthFN = new DecimalType(); // bb
          return this.truthFN;
        }

        public boolean hasTruthFNElement() { 
          return this.truthFN != null && !this.truthFN.isEmpty();
        }

        public boolean hasTruthFN() { 
          return this.truthFN != null && !this.truthFN.isEmpty();
        }

        /**
         * @param value {@link #truthFN} (False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.). This is the underlying object with id, value and extensions. The accessor "getTruthFN" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setTruthFNElement(DecimalType value) { 
          this.truthFN = value;
          return this;
        }

        /**
         * @return False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.
         */
        public BigDecimal getTruthFN() { 
          return this.truthFN == null ? null : this.truthFN.getValue();
        }

        /**
         * @param value False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.
         */
        public MolecularSequenceQualityComponent setTruthFN(BigDecimal value) { 
          if (value == null)
            this.truthFN = null;
          else {
            if (this.truthFN == null)
              this.truthFN = new DecimalType();
            this.truthFN.setValue(value);
          }
          return this;
        }

        /**
         * @param value False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.
         */
        public MolecularSequenceQualityComponent setTruthFN(long value) { 
              this.truthFN = new DecimalType();
            this.truthFN.setValue(value);
          return this;
        }

        /**
         * @param value False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.
         */
        public MolecularSequenceQualityComponent setTruthFN(double value) { 
              this.truthFN = new DecimalType();
            this.truthFN.setValue(value);
          return this;
        }

        /**
         * @return {@link #queryFP} (False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.). This is the underlying object with id, value and extensions. The accessor "getQueryFP" gives direct access to the value
         */
        public DecimalType getQueryFPElement() { 
          if (this.queryFP == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.queryFP");
            else if (Configuration.doAutoCreate())
              this.queryFP = new DecimalType(); // bb
          return this.queryFP;
        }

        public boolean hasQueryFPElement() { 
          return this.queryFP != null && !this.queryFP.isEmpty();
        }

        public boolean hasQueryFP() { 
          return this.queryFP != null && !this.queryFP.isEmpty();
        }

        /**
         * @param value {@link #queryFP} (False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.). This is the underlying object with id, value and extensions. The accessor "getQueryFP" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setQueryFPElement(DecimalType value) { 
          this.queryFP = value;
          return this;
        }

        /**
         * @return False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         */
        public BigDecimal getQueryFP() { 
          return this.queryFP == null ? null : this.queryFP.getValue();
        }

        /**
         * @param value False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         */
        public MolecularSequenceQualityComponent setQueryFP(BigDecimal value) { 
          if (value == null)
            this.queryFP = null;
          else {
            if (this.queryFP == null)
              this.queryFP = new DecimalType();
            this.queryFP.setValue(value);
          }
          return this;
        }

        /**
         * @param value False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         */
        public MolecularSequenceQualityComponent setQueryFP(long value) { 
              this.queryFP = new DecimalType();
            this.queryFP.setValue(value);
          return this;
        }

        /**
         * @param value False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         */
        public MolecularSequenceQualityComponent setQueryFP(double value) { 
              this.queryFP = new DecimalType();
            this.queryFP.setValue(value);
          return this;
        }

        /**
         * @return {@link #gtFP} (The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).). This is the underlying object with id, value and extensions. The accessor "getGtFP" gives direct access to the value
         */
        public DecimalType getGtFPElement() { 
          if (this.gtFP == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.gtFP");
            else if (Configuration.doAutoCreate())
              this.gtFP = new DecimalType(); // bb
          return this.gtFP;
        }

        public boolean hasGtFPElement() { 
          return this.gtFP != null && !this.gtFP.isEmpty();
        }

        public boolean hasGtFP() { 
          return this.gtFP != null && !this.gtFP.isEmpty();
        }

        /**
         * @param value {@link #gtFP} (The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).). This is the underlying object with id, value and extensions. The accessor "getGtFP" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setGtFPElement(DecimalType value) { 
          this.gtFP = value;
          return this;
        }

        /**
         * @return The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).
         */
        public BigDecimal getGtFP() { 
          return this.gtFP == null ? null : this.gtFP.getValue();
        }

        /**
         * @param value The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).
         */
        public MolecularSequenceQualityComponent setGtFP(BigDecimal value) { 
          if (value == null)
            this.gtFP = null;
          else {
            if (this.gtFP == null)
              this.gtFP = new DecimalType();
            this.gtFP.setValue(value);
          }
          return this;
        }

        /**
         * @param value The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).
         */
        public MolecularSequenceQualityComponent setGtFP(long value) { 
              this.gtFP = new DecimalType();
            this.gtFP.setValue(value);
          return this;
        }

        /**
         * @param value The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).
         */
        public MolecularSequenceQualityComponent setGtFP(double value) { 
              this.gtFP = new DecimalType();
            this.gtFP.setValue(value);
          return this;
        }

        /**
         * @return {@link #precision} (QUERY.TP / (QUERY.TP + QUERY.FP).). This is the underlying object with id, value and extensions. The accessor "getPrecision" gives direct access to the value
         */
        public DecimalType getPrecisionElement() { 
          if (this.precision == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.precision");
            else if (Configuration.doAutoCreate())
              this.precision = new DecimalType(); // bb
          return this.precision;
        }

        public boolean hasPrecisionElement() { 
          return this.precision != null && !this.precision.isEmpty();
        }

        public boolean hasPrecision() { 
          return this.precision != null && !this.precision.isEmpty();
        }

        /**
         * @param value {@link #precision} (QUERY.TP / (QUERY.TP + QUERY.FP).). This is the underlying object with id, value and extensions. The accessor "getPrecision" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setPrecisionElement(DecimalType value) { 
          this.precision = value;
          return this;
        }

        /**
         * @return QUERY.TP / (QUERY.TP + QUERY.FP).
         */
        public BigDecimal getPrecision() { 
          return this.precision == null ? null : this.precision.getValue();
        }

        /**
         * @param value QUERY.TP / (QUERY.TP + QUERY.FP).
         */
        public MolecularSequenceQualityComponent setPrecision(BigDecimal value) { 
          if (value == null)
            this.precision = null;
          else {
            if (this.precision == null)
              this.precision = new DecimalType();
            this.precision.setValue(value);
          }
          return this;
        }

        /**
         * @param value QUERY.TP / (QUERY.TP + QUERY.FP).
         */
        public MolecularSequenceQualityComponent setPrecision(long value) { 
              this.precision = new DecimalType();
            this.precision.setValue(value);
          return this;
        }

        /**
         * @param value QUERY.TP / (QUERY.TP + QUERY.FP).
         */
        public MolecularSequenceQualityComponent setPrecision(double value) { 
              this.precision = new DecimalType();
            this.precision.setValue(value);
          return this;
        }

        /**
         * @return {@link #recall} (TRUTH.TP / (TRUTH.TP + TRUTH.FN).). This is the underlying object with id, value and extensions. The accessor "getRecall" gives direct access to the value
         */
        public DecimalType getRecallElement() { 
          if (this.recall == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.recall");
            else if (Configuration.doAutoCreate())
              this.recall = new DecimalType(); // bb
          return this.recall;
        }

        public boolean hasRecallElement() { 
          return this.recall != null && !this.recall.isEmpty();
        }

        public boolean hasRecall() { 
          return this.recall != null && !this.recall.isEmpty();
        }

        /**
         * @param value {@link #recall} (TRUTH.TP / (TRUTH.TP + TRUTH.FN).). This is the underlying object with id, value and extensions. The accessor "getRecall" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setRecallElement(DecimalType value) { 
          this.recall = value;
          return this;
        }

        /**
         * @return TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         */
        public BigDecimal getRecall() { 
          return this.recall == null ? null : this.recall.getValue();
        }

        /**
         * @param value TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         */
        public MolecularSequenceQualityComponent setRecall(BigDecimal value) { 
          if (value == null)
            this.recall = null;
          else {
            if (this.recall == null)
              this.recall = new DecimalType();
            this.recall.setValue(value);
          }
          return this;
        }

        /**
         * @param value TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         */
        public MolecularSequenceQualityComponent setRecall(long value) { 
              this.recall = new DecimalType();
            this.recall.setValue(value);
          return this;
        }

        /**
         * @param value TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         */
        public MolecularSequenceQualityComponent setRecall(double value) { 
              this.recall = new DecimalType();
            this.recall.setValue(value);
          return this;
        }

        /**
         * @return {@link #fScore} (Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).). This is the underlying object with id, value and extensions. The accessor "getFScore" gives direct access to the value
         */
        public DecimalType getFScoreElement() { 
          if (this.fScore == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.fScore");
            else if (Configuration.doAutoCreate())
              this.fScore = new DecimalType(); // bb
          return this.fScore;
        }

        public boolean hasFScoreElement() { 
          return this.fScore != null && !this.fScore.isEmpty();
        }

        public boolean hasFScore() { 
          return this.fScore != null && !this.fScore.isEmpty();
        }

        /**
         * @param value {@link #fScore} (Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).). This is the underlying object with id, value and extensions. The accessor "getFScore" gives direct access to the value
         */
        public MolecularSequenceQualityComponent setFScoreElement(DecimalType value) { 
          this.fScore = value;
          return this;
        }

        /**
         * @return Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         */
        public BigDecimal getFScore() { 
          return this.fScore == null ? null : this.fScore.getValue();
        }

        /**
         * @param value Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         */
        public MolecularSequenceQualityComponent setFScore(BigDecimal value) { 
          if (value == null)
            this.fScore = null;
          else {
            if (this.fScore == null)
              this.fScore = new DecimalType();
            this.fScore.setValue(value);
          }
          return this;
        }

        /**
         * @param value Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         */
        public MolecularSequenceQualityComponent setFScore(long value) { 
              this.fScore = new DecimalType();
            this.fScore.setValue(value);
          return this;
        }

        /**
         * @param value Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         */
        public MolecularSequenceQualityComponent setFScore(double value) { 
              this.fScore = new DecimalType();
            this.fScore.setValue(value);
          return this;
        }

        /**
         * @return {@link #roc} (Receiver Operator Characteristic (ROC) Curve  to give sensitivity/specificity tradeoff.)
         */
        public MolecularSequenceQualityRocComponent getRoc() { 
          if (this.roc == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceQualityComponent.roc");
            else if (Configuration.doAutoCreate())
              this.roc = new MolecularSequenceQualityRocComponent(); // cc
          return this.roc;
        }

        public boolean hasRoc() { 
          return this.roc != null && !this.roc.isEmpty();
        }

        /**
         * @param value {@link #roc} (Receiver Operator Characteristic (ROC) Curve  to give sensitivity/specificity tradeoff.)
         */
        public MolecularSequenceQualityComponent setRoc(MolecularSequenceQualityRocComponent value) { 
          this.roc = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "INDEL / SNP / Undefined variant.", 0, 1, type));
          children.add(new Property("standardSequence", "CodeableConcept", "Gold standard sequence used for comparing against.", 0, 1, standardSequence));
          children.add(new Property("start", "integer", "Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start));
          children.add(new Property("end", "integer", "End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end));
          children.add(new Property("score", "Quantity", "The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).", 0, 1, score));
          children.add(new Property("method", "CodeableConcept", "Which method is used to get sequence quality.", 0, 1, method));
          children.add(new Property("truthTP", "decimal", "True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.", 0, 1, truthTP));
          children.add(new Property("queryTP", "decimal", "True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.", 0, 1, queryTP));
          children.add(new Property("truthFN", "decimal", "False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.", 0, 1, truthFN));
          children.add(new Property("queryFP", "decimal", "False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.", 0, 1, queryFP));
          children.add(new Property("gtFP", "decimal", "The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).", 0, 1, gtFP));
          children.add(new Property("precision", "decimal", "QUERY.TP / (QUERY.TP + QUERY.FP).", 0, 1, precision));
          children.add(new Property("recall", "decimal", "TRUTH.TP / (TRUTH.TP + TRUTH.FN).", 0, 1, recall));
          children.add(new Property("fScore", "decimal", "Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).", 0, 1, fScore));
          children.add(new Property("roc", "", "Receiver Operator Characteristic (ROC) Curve  to give sensitivity/specificity tradeoff.", 0, 1, roc));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "INDEL / SNP / Undefined variant.", 0, 1, type);
          case -1861227106: /*standardSequence*/  return new Property("standardSequence", "CodeableConcept", "Gold standard sequence used for comparing against.", 0, 1, standardSequence);
          case 109757538: /*start*/  return new Property("start", "integer", "Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start);
          case 100571: /*end*/  return new Property("end", "integer", "End position of the sequence. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end);
          case 109264530: /*score*/  return new Property("score", "Quantity", "The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).", 0, 1, score);
          case -1077554975: /*method*/  return new Property("method", "CodeableConcept", "Which method is used to get sequence quality.", 0, 1, method);
          case -1048421849: /*truthTP*/  return new Property("truthTP", "decimal", "True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.", 0, 1, truthTP);
          case 655102276: /*queryTP*/  return new Property("queryTP", "decimal", "True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.", 0, 1, queryTP);
          case -1048422285: /*truthFN*/  return new Property("truthFN", "decimal", "False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.", 0, 1, truthFN);
          case 655101842: /*queryFP*/  return new Property("queryFP", "decimal", "False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.", 0, 1, queryFP);
          case 3182199: /*gtFP*/  return new Property("gtFP", "decimal", "The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).", 0, 1, gtFP);
          case -1376177026: /*precision*/  return new Property("precision", "decimal", "QUERY.TP / (QUERY.TP + QUERY.FP).", 0, 1, precision);
          case -934922479: /*recall*/  return new Property("recall", "decimal", "TRUTH.TP / (TRUTH.TP + TRUTH.FN).", 0, 1, recall);
          case -1295082036: /*fScore*/  return new Property("fScore", "decimal", "Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).", 0, 1, fScore);
          case 113094: /*roc*/  return new Property("roc", "", "Receiver Operator Characteristic (ROC) Curve  to give sensitivity/specificity tradeoff.", 0, 1, roc);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<QualityType>
        case -1861227106: /*standardSequence*/ return this.standardSequence == null ? new Base[0] : new Base[] {this.standardSequence}; // CodeableConcept
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // IntegerType
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // IntegerType
        case 109264530: /*score*/ return this.score == null ? new Base[0] : new Base[] {this.score}; // Quantity
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case -1048421849: /*truthTP*/ return this.truthTP == null ? new Base[0] : new Base[] {this.truthTP}; // DecimalType
        case 655102276: /*queryTP*/ return this.queryTP == null ? new Base[0] : new Base[] {this.queryTP}; // DecimalType
        case -1048422285: /*truthFN*/ return this.truthFN == null ? new Base[0] : new Base[] {this.truthFN}; // DecimalType
        case 655101842: /*queryFP*/ return this.queryFP == null ? new Base[0] : new Base[] {this.queryFP}; // DecimalType
        case 3182199: /*gtFP*/ return this.gtFP == null ? new Base[0] : new Base[] {this.gtFP}; // DecimalType
        case -1376177026: /*precision*/ return this.precision == null ? new Base[0] : new Base[] {this.precision}; // DecimalType
        case -934922479: /*recall*/ return this.recall == null ? new Base[0] : new Base[] {this.recall}; // DecimalType
        case -1295082036: /*fScore*/ return this.fScore == null ? new Base[0] : new Base[] {this.fScore}; // DecimalType
        case 113094: /*roc*/ return this.roc == null ? new Base[0] : new Base[] {this.roc}; // MolecularSequenceQualityRocComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new QualityTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<QualityType>
          return value;
        case -1861227106: // standardSequence
          this.standardSequence = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          return value;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          return value;
        case 109264530: // score
          this.score = castToQuantity(value); // Quantity
          return value;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1048421849: // truthTP
          this.truthTP = castToDecimal(value); // DecimalType
          return value;
        case 655102276: // queryTP
          this.queryTP = castToDecimal(value); // DecimalType
          return value;
        case -1048422285: // truthFN
          this.truthFN = castToDecimal(value); // DecimalType
          return value;
        case 655101842: // queryFP
          this.queryFP = castToDecimal(value); // DecimalType
          return value;
        case 3182199: // gtFP
          this.gtFP = castToDecimal(value); // DecimalType
          return value;
        case -1376177026: // precision
          this.precision = castToDecimal(value); // DecimalType
          return value;
        case -934922479: // recall
          this.recall = castToDecimal(value); // DecimalType
          return value;
        case -1295082036: // fScore
          this.fScore = castToDecimal(value); // DecimalType
          return value;
        case 113094: // roc
          this.roc = (MolecularSequenceQualityRocComponent) value; // MolecularSequenceQualityRocComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new QualityTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<QualityType>
        } else if (name.equals("standardSequence")) {
          this.standardSequence = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("start")) {
          this.start = castToInteger(value); // IntegerType
        } else if (name.equals("end")) {
          this.end = castToInteger(value); // IntegerType
        } else if (name.equals("score")) {
          this.score = castToQuantity(value); // Quantity
        } else if (name.equals("method")) {
          this.method = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("truthTP")) {
          this.truthTP = castToDecimal(value); // DecimalType
        } else if (name.equals("queryTP")) {
          this.queryTP = castToDecimal(value); // DecimalType
        } else if (name.equals("truthFN")) {
          this.truthFN = castToDecimal(value); // DecimalType
        } else if (name.equals("queryFP")) {
          this.queryFP = castToDecimal(value); // DecimalType
        } else if (name.equals("gtFP")) {
          this.gtFP = castToDecimal(value); // DecimalType
        } else if (name.equals("precision")) {
          this.precision = castToDecimal(value); // DecimalType
        } else if (name.equals("recall")) {
          this.recall = castToDecimal(value); // DecimalType
        } else if (name.equals("fScore")) {
          this.fScore = castToDecimal(value); // DecimalType
        } else if (name.equals("roc")) {
          this.roc = (MolecularSequenceQualityRocComponent) value; // MolecularSequenceQualityRocComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -1861227106:  return getStandardSequence(); 
        case 109757538:  return getStartElement();
        case 100571:  return getEndElement();
        case 109264530:  return getScore(); 
        case -1077554975:  return getMethod(); 
        case -1048421849:  return getTruthTPElement();
        case 655102276:  return getQueryTPElement();
        case -1048422285:  return getTruthFNElement();
        case 655101842:  return getQueryFPElement();
        case 3182199:  return getGtFPElement();
        case -1376177026:  return getPrecisionElement();
        case -934922479:  return getRecallElement();
        case -1295082036:  return getFScoreElement();
        case 113094:  return getRoc(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -1861227106: /*standardSequence*/ return new String[] {"CodeableConcept"};
        case 109757538: /*start*/ return new String[] {"integer"};
        case 100571: /*end*/ return new String[] {"integer"};
        case 109264530: /*score*/ return new String[] {"Quantity"};
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case -1048421849: /*truthTP*/ return new String[] {"decimal"};
        case 655102276: /*queryTP*/ return new String[] {"decimal"};
        case -1048422285: /*truthFN*/ return new String[] {"decimal"};
        case 655101842: /*queryFP*/ return new String[] {"decimal"};
        case 3182199: /*gtFP*/ return new String[] {"decimal"};
        case -1376177026: /*precision*/ return new String[] {"decimal"};
        case -934922479: /*recall*/ return new String[] {"decimal"};
        case -1295082036: /*fScore*/ return new String[] {"decimal"};
        case 113094: /*roc*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.type");
        }
        else if (name.equals("standardSequence")) {
          this.standardSequence = new CodeableConcept();
          return this.standardSequence;
        }
        else if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.end");
        }
        else if (name.equals("score")) {
          this.score = new Quantity();
          return this.score;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("truthTP")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.truthTP");
        }
        else if (name.equals("queryTP")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.queryTP");
        }
        else if (name.equals("truthFN")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.truthFN");
        }
        else if (name.equals("queryFP")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.queryFP");
        }
        else if (name.equals("gtFP")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.gtFP");
        }
        else if (name.equals("precision")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.precision");
        }
        else if (name.equals("recall")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.recall");
        }
        else if (name.equals("fScore")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.fScore");
        }
        else if (name.equals("roc")) {
          this.roc = new MolecularSequenceQualityRocComponent();
          return this.roc;
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceQualityComponent copy() {
        MolecularSequenceQualityComponent dst = new MolecularSequenceQualityComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.standardSequence = standardSequence == null ? null : standardSequence.copy();
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.score = score == null ? null : score.copy();
        dst.method = method == null ? null : method.copy();
        dst.truthTP = truthTP == null ? null : truthTP.copy();
        dst.queryTP = queryTP == null ? null : queryTP.copy();
        dst.truthFN = truthFN == null ? null : truthFN.copy();
        dst.queryFP = queryFP == null ? null : queryFP.copy();
        dst.gtFP = gtFP == null ? null : gtFP.copy();
        dst.precision = precision == null ? null : precision.copy();
        dst.recall = recall == null ? null : recall.copy();
        dst.fScore = fScore == null ? null : fScore.copy();
        dst.roc = roc == null ? null : roc.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceQualityComponent))
          return false;
        MolecularSequenceQualityComponent o = (MolecularSequenceQualityComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(standardSequence, o.standardSequence, true)
           && compareDeep(start, o.start, true) && compareDeep(end, o.end, true) && compareDeep(score, o.score, true)
           && compareDeep(method, o.method, true) && compareDeep(truthTP, o.truthTP, true) && compareDeep(queryTP, o.queryTP, true)
           && compareDeep(truthFN, o.truthFN, true) && compareDeep(queryFP, o.queryFP, true) && compareDeep(gtFP, o.gtFP, true)
           && compareDeep(precision, o.precision, true) && compareDeep(recall, o.recall, true) && compareDeep(fScore, o.fScore, true)
           && compareDeep(roc, o.roc, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceQualityComponent))
          return false;
        MolecularSequenceQualityComponent o = (MolecularSequenceQualityComponent) other_;
        return compareValues(type, o.type, true) && compareValues(start, o.start, true) && compareValues(end, o.end, true)
           && compareValues(truthTP, o.truthTP, true) && compareValues(queryTP, o.queryTP, true) && compareValues(truthFN, o.truthFN, true)
           && compareValues(queryFP, o.queryFP, true) && compareValues(gtFP, o.gtFP, true) && compareValues(precision, o.precision, true)
           && compareValues(recall, o.recall, true) && compareValues(fScore, o.fScore, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, standardSequence, start
          , end, score, method, truthTP, queryTP, truthFN, queryFP, gtFP, precision
          , recall, fScore, roc);
      }

  public String fhirType() {
    return "MolecularSequence.quality";

  }

  }

    @Block()
    public static class MolecularSequenceQualityRocComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Invidual data point representing the GQ (genotype quality) score threshold.
         */
        @Child(name = "score", type = {IntegerType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Genotype quality score", formalDefinition="Invidual data point representing the GQ (genotype quality) score threshold." )
        protected List<IntegerType> score;

        /**
         * The number of true positives if the GQ score threshold was set to "score" field value.
         */
        @Child(name = "numTP", type = {IntegerType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Roc score true positive numbers", formalDefinition="The number of true positives if the GQ score threshold was set to \"score\" field value." )
        protected List<IntegerType> numTP;

        /**
         * The number of false positives if the GQ score threshold was set to "score" field value.
         */
        @Child(name = "numFP", type = {IntegerType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Roc score false positive numbers", formalDefinition="The number of false positives if the GQ score threshold was set to \"score\" field value." )
        protected List<IntegerType> numFP;

        /**
         * The number of false negatives if the GQ score threshold was set to "score" field value.
         */
        @Child(name = "numFN", type = {IntegerType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Roc score false negative numbers", formalDefinition="The number of false negatives if the GQ score threshold was set to \"score\" field value." )
        protected List<IntegerType> numFN;

        /**
         * Calculated precision if the GQ score threshold was set to "score" field value.
         */
        @Child(name = "precision", type = {DecimalType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Precision of the GQ score", formalDefinition="Calculated precision if the GQ score threshold was set to \"score\" field value." )
        protected List<DecimalType> precision;

        /**
         * Calculated sensitivity if the GQ score threshold was set to "score" field value.
         */
        @Child(name = "sensitivity", type = {DecimalType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Sensitivity of the GQ score", formalDefinition="Calculated sensitivity if the GQ score threshold was set to \"score\" field value." )
        protected List<DecimalType> sensitivity;

        /**
         * Calculated fScore if the GQ score threshold was set to "score" field value.
         */
        @Child(name = "fMeasure", type = {DecimalType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="FScore of the GQ score", formalDefinition="Calculated fScore if the GQ score threshold was set to \"score\" field value." )
        protected List<DecimalType> fMeasure;

        private static final long serialVersionUID = 1923392132L;

    /**
     * Constructor
     */
      public MolecularSequenceQualityRocComponent() {
        super();
      }

        /**
         * @return {@link #score} (Invidual data point representing the GQ (genotype quality) score threshold.)
         */
        public List<IntegerType> getScore() { 
          if (this.score == null)
            this.score = new ArrayList<IntegerType>();
          return this.score;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MolecularSequenceQualityRocComponent setScore(List<IntegerType> theScore) { 
          this.score = theScore;
          return this;
        }

        public boolean hasScore() { 
          if (this.score == null)
            return false;
          for (IntegerType item : this.score)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #score} (Invidual data point representing the GQ (genotype quality) score threshold.)
         */
        public IntegerType addScoreElement() {//2 
          IntegerType t = new IntegerType();
          if (this.score == null)
            this.score = new ArrayList<IntegerType>();
          this.score.add(t);
          return t;
        }

        /**
         * @param value {@link #score} (Invidual data point representing the GQ (genotype quality) score threshold.)
         */
        public MolecularSequenceQualityRocComponent addScore(int value) { //1
          IntegerType t = new IntegerType();
          t.setValue(value);
          if (this.score == null)
            this.score = new ArrayList<IntegerType>();
          this.score.add(t);
          return this;
        }

        /**
         * @param value {@link #score} (Invidual data point representing the GQ (genotype quality) score threshold.)
         */
        public boolean hasScore(int value) { 
          if (this.score == null)
            return false;
          for (IntegerType v : this.score)
            if (v.getValue().equals(value)) // integer
              return true;
          return false;
        }

        /**
         * @return {@link #numTP} (The number of true positives if the GQ score threshold was set to "score" field value.)
         */
        public List<IntegerType> getNumTP() { 
          if (this.numTP == null)
            this.numTP = new ArrayList<IntegerType>();
          return this.numTP;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MolecularSequenceQualityRocComponent setNumTP(List<IntegerType> theNumTP) { 
          this.numTP = theNumTP;
          return this;
        }

        public boolean hasNumTP() { 
          if (this.numTP == null)
            return false;
          for (IntegerType item : this.numTP)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #numTP} (The number of true positives if the GQ score threshold was set to "score" field value.)
         */
        public IntegerType addNumTPElement() {//2 
          IntegerType t = new IntegerType();
          if (this.numTP == null)
            this.numTP = new ArrayList<IntegerType>();
          this.numTP.add(t);
          return t;
        }

        /**
         * @param value {@link #numTP} (The number of true positives if the GQ score threshold was set to "score" field value.)
         */
        public MolecularSequenceQualityRocComponent addNumTP(int value) { //1
          IntegerType t = new IntegerType();
          t.setValue(value);
          if (this.numTP == null)
            this.numTP = new ArrayList<IntegerType>();
          this.numTP.add(t);
          return this;
        }

        /**
         * @param value {@link #numTP} (The number of true positives if the GQ score threshold was set to "score" field value.)
         */
        public boolean hasNumTP(int value) { 
          if (this.numTP == null)
            return false;
          for (IntegerType v : this.numTP)
            if (v.getValue().equals(value)) // integer
              return true;
          return false;
        }

        /**
         * @return {@link #numFP} (The number of false positives if the GQ score threshold was set to "score" field value.)
         */
        public List<IntegerType> getNumFP() { 
          if (this.numFP == null)
            this.numFP = new ArrayList<IntegerType>();
          return this.numFP;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MolecularSequenceQualityRocComponent setNumFP(List<IntegerType> theNumFP) { 
          this.numFP = theNumFP;
          return this;
        }

        public boolean hasNumFP() { 
          if (this.numFP == null)
            return false;
          for (IntegerType item : this.numFP)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #numFP} (The number of false positives if the GQ score threshold was set to "score" field value.)
         */
        public IntegerType addNumFPElement() {//2 
          IntegerType t = new IntegerType();
          if (this.numFP == null)
            this.numFP = new ArrayList<IntegerType>();
          this.numFP.add(t);
          return t;
        }

        /**
         * @param value {@link #numFP} (The number of false positives if the GQ score threshold was set to "score" field value.)
         */
        public MolecularSequenceQualityRocComponent addNumFP(int value) { //1
          IntegerType t = new IntegerType();
          t.setValue(value);
          if (this.numFP == null)
            this.numFP = new ArrayList<IntegerType>();
          this.numFP.add(t);
          return this;
        }

        /**
         * @param value {@link #numFP} (The number of false positives if the GQ score threshold was set to "score" field value.)
         */
        public boolean hasNumFP(int value) { 
          if (this.numFP == null)
            return false;
          for (IntegerType v : this.numFP)
            if (v.getValue().equals(value)) // integer
              return true;
          return false;
        }

        /**
         * @return {@link #numFN} (The number of false negatives if the GQ score threshold was set to "score" field value.)
         */
        public List<IntegerType> getNumFN() { 
          if (this.numFN == null)
            this.numFN = new ArrayList<IntegerType>();
          return this.numFN;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MolecularSequenceQualityRocComponent setNumFN(List<IntegerType> theNumFN) { 
          this.numFN = theNumFN;
          return this;
        }

        public boolean hasNumFN() { 
          if (this.numFN == null)
            return false;
          for (IntegerType item : this.numFN)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #numFN} (The number of false negatives if the GQ score threshold was set to "score" field value.)
         */
        public IntegerType addNumFNElement() {//2 
          IntegerType t = new IntegerType();
          if (this.numFN == null)
            this.numFN = new ArrayList<IntegerType>();
          this.numFN.add(t);
          return t;
        }

        /**
         * @param value {@link #numFN} (The number of false negatives if the GQ score threshold was set to "score" field value.)
         */
        public MolecularSequenceQualityRocComponent addNumFN(int value) { //1
          IntegerType t = new IntegerType();
          t.setValue(value);
          if (this.numFN == null)
            this.numFN = new ArrayList<IntegerType>();
          this.numFN.add(t);
          return this;
        }

        /**
         * @param value {@link #numFN} (The number of false negatives if the GQ score threshold was set to "score" field value.)
         */
        public boolean hasNumFN(int value) { 
          if (this.numFN == null)
            return false;
          for (IntegerType v : this.numFN)
            if (v.getValue().equals(value)) // integer
              return true;
          return false;
        }

        /**
         * @return {@link #precision} (Calculated precision if the GQ score threshold was set to "score" field value.)
         */
        public List<DecimalType> getPrecision() { 
          if (this.precision == null)
            this.precision = new ArrayList<DecimalType>();
          return this.precision;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MolecularSequenceQualityRocComponent setPrecision(List<DecimalType> thePrecision) { 
          this.precision = thePrecision;
          return this;
        }

        public boolean hasPrecision() { 
          if (this.precision == null)
            return false;
          for (DecimalType item : this.precision)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #precision} (Calculated precision if the GQ score threshold was set to "score" field value.)
         */
        public DecimalType addPrecisionElement() {//2 
          DecimalType t = new DecimalType();
          if (this.precision == null)
            this.precision = new ArrayList<DecimalType>();
          this.precision.add(t);
          return t;
        }

        /**
         * @param value {@link #precision} (Calculated precision if the GQ score threshold was set to "score" field value.)
         */
        public MolecularSequenceQualityRocComponent addPrecision(BigDecimal value) { //1
          DecimalType t = new DecimalType();
          t.setValue(value);
          if (this.precision == null)
            this.precision = new ArrayList<DecimalType>();
          this.precision.add(t);
          return this;
        }

        /**
         * @param value {@link #precision} (Calculated precision if the GQ score threshold was set to "score" field value.)
         */
        public boolean hasPrecision(BigDecimal value) { 
          if (this.precision == null)
            return false;
          for (DecimalType v : this.precision)
            if (v.getValue().equals(value)) // decimal
              return true;
          return false;
        }

        /**
         * @return {@link #sensitivity} (Calculated sensitivity if the GQ score threshold was set to "score" field value.)
         */
        public List<DecimalType> getSensitivity() { 
          if (this.sensitivity == null)
            this.sensitivity = new ArrayList<DecimalType>();
          return this.sensitivity;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MolecularSequenceQualityRocComponent setSensitivity(List<DecimalType> theSensitivity) { 
          this.sensitivity = theSensitivity;
          return this;
        }

        public boolean hasSensitivity() { 
          if (this.sensitivity == null)
            return false;
          for (DecimalType item : this.sensitivity)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #sensitivity} (Calculated sensitivity if the GQ score threshold was set to "score" field value.)
         */
        public DecimalType addSensitivityElement() {//2 
          DecimalType t = new DecimalType();
          if (this.sensitivity == null)
            this.sensitivity = new ArrayList<DecimalType>();
          this.sensitivity.add(t);
          return t;
        }

        /**
         * @param value {@link #sensitivity} (Calculated sensitivity if the GQ score threshold was set to "score" field value.)
         */
        public MolecularSequenceQualityRocComponent addSensitivity(BigDecimal value) { //1
          DecimalType t = new DecimalType();
          t.setValue(value);
          if (this.sensitivity == null)
            this.sensitivity = new ArrayList<DecimalType>();
          this.sensitivity.add(t);
          return this;
        }

        /**
         * @param value {@link #sensitivity} (Calculated sensitivity if the GQ score threshold was set to "score" field value.)
         */
        public boolean hasSensitivity(BigDecimal value) { 
          if (this.sensitivity == null)
            return false;
          for (DecimalType v : this.sensitivity)
            if (v.getValue().equals(value)) // decimal
              return true;
          return false;
        }

        /**
         * @return {@link #fMeasure} (Calculated fScore if the GQ score threshold was set to "score" field value.)
         */
        public List<DecimalType> getFMeasure() { 
          if (this.fMeasure == null)
            this.fMeasure = new ArrayList<DecimalType>();
          return this.fMeasure;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public MolecularSequenceQualityRocComponent setFMeasure(List<DecimalType> theFMeasure) { 
          this.fMeasure = theFMeasure;
          return this;
        }

        public boolean hasFMeasure() { 
          if (this.fMeasure == null)
            return false;
          for (DecimalType item : this.fMeasure)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #fMeasure} (Calculated fScore if the GQ score threshold was set to "score" field value.)
         */
        public DecimalType addFMeasureElement() {//2 
          DecimalType t = new DecimalType();
          if (this.fMeasure == null)
            this.fMeasure = new ArrayList<DecimalType>();
          this.fMeasure.add(t);
          return t;
        }

        /**
         * @param value {@link #fMeasure} (Calculated fScore if the GQ score threshold was set to "score" field value.)
         */
        public MolecularSequenceQualityRocComponent addFMeasure(BigDecimal value) { //1
          DecimalType t = new DecimalType();
          t.setValue(value);
          if (this.fMeasure == null)
            this.fMeasure = new ArrayList<DecimalType>();
          this.fMeasure.add(t);
          return this;
        }

        /**
         * @param value {@link #fMeasure} (Calculated fScore if the GQ score threshold was set to "score" field value.)
         */
        public boolean hasFMeasure(BigDecimal value) { 
          if (this.fMeasure == null)
            return false;
          for (DecimalType v : this.fMeasure)
            if (v.getValue().equals(value)) // decimal
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("score", "integer", "Invidual data point representing the GQ (genotype quality) score threshold.", 0, java.lang.Integer.MAX_VALUE, score));
          children.add(new Property("numTP", "integer", "The number of true positives if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, numTP));
          children.add(new Property("numFP", "integer", "The number of false positives if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, numFP));
          children.add(new Property("numFN", "integer", "The number of false negatives if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, numFN));
          children.add(new Property("precision", "decimal", "Calculated precision if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, precision));
          children.add(new Property("sensitivity", "decimal", "Calculated sensitivity if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, sensitivity));
          children.add(new Property("fMeasure", "decimal", "Calculated fScore if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, fMeasure));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 109264530: /*score*/  return new Property("score", "integer", "Invidual data point representing the GQ (genotype quality) score threshold.", 0, java.lang.Integer.MAX_VALUE, score);
          case 105180290: /*numTP*/  return new Property("numTP", "integer", "The number of true positives if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, numTP);
          case 105179856: /*numFP*/  return new Property("numFP", "integer", "The number of false positives if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, numFP);
          case 105179854: /*numFN*/  return new Property("numFN", "integer", "The number of false negatives if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, numFN);
          case -1376177026: /*precision*/  return new Property("precision", "decimal", "Calculated precision if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, precision);
          case 564403871: /*sensitivity*/  return new Property("sensitivity", "decimal", "Calculated sensitivity if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, sensitivity);
          case -18997736: /*fMeasure*/  return new Property("fMeasure", "decimal", "Calculated fScore if the GQ score threshold was set to \"score\" field value.", 0, java.lang.Integer.MAX_VALUE, fMeasure);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109264530: /*score*/ return this.score == null ? new Base[0] : this.score.toArray(new Base[this.score.size()]); // IntegerType
        case 105180290: /*numTP*/ return this.numTP == null ? new Base[0] : this.numTP.toArray(new Base[this.numTP.size()]); // IntegerType
        case 105179856: /*numFP*/ return this.numFP == null ? new Base[0] : this.numFP.toArray(new Base[this.numFP.size()]); // IntegerType
        case 105179854: /*numFN*/ return this.numFN == null ? new Base[0] : this.numFN.toArray(new Base[this.numFN.size()]); // IntegerType
        case -1376177026: /*precision*/ return this.precision == null ? new Base[0] : this.precision.toArray(new Base[this.precision.size()]); // DecimalType
        case 564403871: /*sensitivity*/ return this.sensitivity == null ? new Base[0] : this.sensitivity.toArray(new Base[this.sensitivity.size()]); // DecimalType
        case -18997736: /*fMeasure*/ return this.fMeasure == null ? new Base[0] : this.fMeasure.toArray(new Base[this.fMeasure.size()]); // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109264530: // score
          this.getScore().add(castToInteger(value)); // IntegerType
          return value;
        case 105180290: // numTP
          this.getNumTP().add(castToInteger(value)); // IntegerType
          return value;
        case 105179856: // numFP
          this.getNumFP().add(castToInteger(value)); // IntegerType
          return value;
        case 105179854: // numFN
          this.getNumFN().add(castToInteger(value)); // IntegerType
          return value;
        case -1376177026: // precision
          this.getPrecision().add(castToDecimal(value)); // DecimalType
          return value;
        case 564403871: // sensitivity
          this.getSensitivity().add(castToDecimal(value)); // DecimalType
          return value;
        case -18997736: // fMeasure
          this.getFMeasure().add(castToDecimal(value)); // DecimalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("score")) {
          this.getScore().add(castToInteger(value));
        } else if (name.equals("numTP")) {
          this.getNumTP().add(castToInteger(value));
        } else if (name.equals("numFP")) {
          this.getNumFP().add(castToInteger(value));
        } else if (name.equals("numFN")) {
          this.getNumFN().add(castToInteger(value));
        } else if (name.equals("precision")) {
          this.getPrecision().add(castToDecimal(value));
        } else if (name.equals("sensitivity")) {
          this.getSensitivity().add(castToDecimal(value));
        } else if (name.equals("fMeasure")) {
          this.getFMeasure().add(castToDecimal(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109264530:  return addScoreElement();
        case 105180290:  return addNumTPElement();
        case 105179856:  return addNumFPElement();
        case 105179854:  return addNumFNElement();
        case -1376177026:  return addPrecisionElement();
        case 564403871:  return addSensitivityElement();
        case -18997736:  return addFMeasureElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109264530: /*score*/ return new String[] {"integer"};
        case 105180290: /*numTP*/ return new String[] {"integer"};
        case 105179856: /*numFP*/ return new String[] {"integer"};
        case 105179854: /*numFN*/ return new String[] {"integer"};
        case -1376177026: /*precision*/ return new String[] {"decimal"};
        case 564403871: /*sensitivity*/ return new String[] {"decimal"};
        case -18997736: /*fMeasure*/ return new String[] {"decimal"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("score")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.score");
        }
        else if (name.equals("numTP")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.numTP");
        }
        else if (name.equals("numFP")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.numFP");
        }
        else if (name.equals("numFN")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.numFN");
        }
        else if (name.equals("precision")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.precision");
        }
        else if (name.equals("sensitivity")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.sensitivity");
        }
        else if (name.equals("fMeasure")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.fMeasure");
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceQualityRocComponent copy() {
        MolecularSequenceQualityRocComponent dst = new MolecularSequenceQualityRocComponent();
        copyValues(dst);
        if (score != null) {
          dst.score = new ArrayList<IntegerType>();
          for (IntegerType i : score)
            dst.score.add(i.copy());
        };
        if (numTP != null) {
          dst.numTP = new ArrayList<IntegerType>();
          for (IntegerType i : numTP)
            dst.numTP.add(i.copy());
        };
        if (numFP != null) {
          dst.numFP = new ArrayList<IntegerType>();
          for (IntegerType i : numFP)
            dst.numFP.add(i.copy());
        };
        if (numFN != null) {
          dst.numFN = new ArrayList<IntegerType>();
          for (IntegerType i : numFN)
            dst.numFN.add(i.copy());
        };
        if (precision != null) {
          dst.precision = new ArrayList<DecimalType>();
          for (DecimalType i : precision)
            dst.precision.add(i.copy());
        };
        if (sensitivity != null) {
          dst.sensitivity = new ArrayList<DecimalType>();
          for (DecimalType i : sensitivity)
            dst.sensitivity.add(i.copy());
        };
        if (fMeasure != null) {
          dst.fMeasure = new ArrayList<DecimalType>();
          for (DecimalType i : fMeasure)
            dst.fMeasure.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceQualityRocComponent))
          return false;
        MolecularSequenceQualityRocComponent o = (MolecularSequenceQualityRocComponent) other_;
        return compareDeep(score, o.score, true) && compareDeep(numTP, o.numTP, true) && compareDeep(numFP, o.numFP, true)
           && compareDeep(numFN, o.numFN, true) && compareDeep(precision, o.precision, true) && compareDeep(sensitivity, o.sensitivity, true)
           && compareDeep(fMeasure, o.fMeasure, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceQualityRocComponent))
          return false;
        MolecularSequenceQualityRocComponent o = (MolecularSequenceQualityRocComponent) other_;
        return compareValues(score, o.score, true) && compareValues(numTP, o.numTP, true) && compareValues(numFP, o.numFP, true)
           && compareValues(numFN, o.numFN, true) && compareValues(precision, o.precision, true) && compareValues(sensitivity, o.sensitivity, true)
           && compareValues(fMeasure, o.fMeasure, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(score, numTP, numFP, numFN
          , precision, sensitivity, fMeasure);
      }

  public String fhirType() {
    return "MolecularSequence.quality.roc";

  }

  }

    @Block()
    public static class MolecularSequenceRepositoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="directlink | openapi | login | oauth | other", formalDefinition="Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/repository-type")
        protected Enumeration<RepositoryType> type;

        /**
         * URI of an external repository which contains further details about the genetics data.
         */
        @Child(name = "url", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="URI of the repository", formalDefinition="URI of an external repository which contains further details about the genetics data." )
        protected UriType url;

        /**
         * URI of an external repository which contains further details about the genetics data.
         */
        @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Repository's name", formalDefinition="URI of an external repository which contains further details about the genetics data." )
        protected StringType name;

        /**
         * Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.
         */
        @Child(name = "datasetId", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of the dataset that used to call for dataset in repository", formalDefinition="Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository." )
        protected StringType datasetId;

        /**
         * Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.
         */
        @Child(name = "variantsetId", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of the variantset that used to call for variantset in repository", formalDefinition="Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository." )
        protected StringType variantsetId;

        /**
         * Id of the read in this external repository.
         */
        @Child(name = "readsetId", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of the read", formalDefinition="Id of the read in this external repository." )
        protected StringType readsetId;

        private static final long serialVersionUID = -899243265L;

    /**
     * Constructor
     */
      public MolecularSequenceRepositoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MolecularSequenceRepositoryComponent(Enumeration<RepositoryType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<RepositoryType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceRepositoryComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<RepositoryType>(new RepositoryTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public MolecularSequenceRepositoryComponent setTypeElement(Enumeration<RepositoryType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.
         */
        public RepositoryType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.
         */
        public MolecularSequenceRepositoryComponent setType(RepositoryType value) { 
            if (this.type == null)
              this.type = new Enumeration<RepositoryType>(new RepositoryTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #url} (URI of an external repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public UriType getUrlElement() { 
          if (this.url == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceRepositoryComponent.url");
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
         * @param value {@link #url} (URI of an external repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public MolecularSequenceRepositoryComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return URI of an external repository which contains further details about the genetics data.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value URI of an external repository which contains further details about the genetics data.
         */
        public MolecularSequenceRepositoryComponent setUrl(String value) { 
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
         * @return {@link #name} (URI of an external repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceRepositoryComponent.name");
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
         * @param value {@link #name} (URI of an external repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public MolecularSequenceRepositoryComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return URI of an external repository which contains further details about the genetics data.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value URI of an external repository which contains further details about the genetics data.
         */
        public MolecularSequenceRepositoryComponent setName(String value) { 
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
         * @return {@link #datasetId} (Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.). This is the underlying object with id, value and extensions. The accessor "getDatasetId" gives direct access to the value
         */
        public StringType getDatasetIdElement() { 
          if (this.datasetId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceRepositoryComponent.datasetId");
            else if (Configuration.doAutoCreate())
              this.datasetId = new StringType(); // bb
          return this.datasetId;
        }

        public boolean hasDatasetIdElement() { 
          return this.datasetId != null && !this.datasetId.isEmpty();
        }

        public boolean hasDatasetId() { 
          return this.datasetId != null && !this.datasetId.isEmpty();
        }

        /**
         * @param value {@link #datasetId} (Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.). This is the underlying object with id, value and extensions. The accessor "getDatasetId" gives direct access to the value
         */
        public MolecularSequenceRepositoryComponent setDatasetIdElement(StringType value) { 
          this.datasetId = value;
          return this;
        }

        /**
         * @return Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.
         */
        public String getDatasetId() { 
          return this.datasetId == null ? null : this.datasetId.getValue();
        }

        /**
         * @param value Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.
         */
        public MolecularSequenceRepositoryComponent setDatasetId(String value) { 
          if (Utilities.noString(value))
            this.datasetId = null;
          else {
            if (this.datasetId == null)
              this.datasetId = new StringType();
            this.datasetId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #variantsetId} (Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.). This is the underlying object with id, value and extensions. The accessor "getVariantsetId" gives direct access to the value
         */
        public StringType getVariantsetIdElement() { 
          if (this.variantsetId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceRepositoryComponent.variantsetId");
            else if (Configuration.doAutoCreate())
              this.variantsetId = new StringType(); // bb
          return this.variantsetId;
        }

        public boolean hasVariantsetIdElement() { 
          return this.variantsetId != null && !this.variantsetId.isEmpty();
        }

        public boolean hasVariantsetId() { 
          return this.variantsetId != null && !this.variantsetId.isEmpty();
        }

        /**
         * @param value {@link #variantsetId} (Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.). This is the underlying object with id, value and extensions. The accessor "getVariantsetId" gives direct access to the value
         */
        public MolecularSequenceRepositoryComponent setVariantsetIdElement(StringType value) { 
          this.variantsetId = value;
          return this;
        }

        /**
         * @return Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.
         */
        public String getVariantsetId() { 
          return this.variantsetId == null ? null : this.variantsetId.getValue();
        }

        /**
         * @param value Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.
         */
        public MolecularSequenceRepositoryComponent setVariantsetId(String value) { 
          if (Utilities.noString(value))
            this.variantsetId = null;
          else {
            if (this.variantsetId == null)
              this.variantsetId = new StringType();
            this.variantsetId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #readsetId} (Id of the read in this external repository.). This is the underlying object with id, value and extensions. The accessor "getReadsetId" gives direct access to the value
         */
        public StringType getReadsetIdElement() { 
          if (this.readsetId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceRepositoryComponent.readsetId");
            else if (Configuration.doAutoCreate())
              this.readsetId = new StringType(); // bb
          return this.readsetId;
        }

        public boolean hasReadsetIdElement() { 
          return this.readsetId != null && !this.readsetId.isEmpty();
        }

        public boolean hasReadsetId() { 
          return this.readsetId != null && !this.readsetId.isEmpty();
        }

        /**
         * @param value {@link #readsetId} (Id of the read in this external repository.). This is the underlying object with id, value and extensions. The accessor "getReadsetId" gives direct access to the value
         */
        public MolecularSequenceRepositoryComponent setReadsetIdElement(StringType value) { 
          this.readsetId = value;
          return this;
        }

        /**
         * @return Id of the read in this external repository.
         */
        public String getReadsetId() { 
          return this.readsetId == null ? null : this.readsetId.getValue();
        }

        /**
         * @param value Id of the read in this external repository.
         */
        public MolecularSequenceRepositoryComponent setReadsetId(String value) { 
          if (Utilities.noString(value))
            this.readsetId = null;
          else {
            if (this.readsetId == null)
              this.readsetId = new StringType();
            this.readsetId.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "code", "Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.", 0, 1, type));
          children.add(new Property("url", "uri", "URI of an external repository which contains further details about the genetics data.", 0, 1, url));
          children.add(new Property("name", "string", "URI of an external repository which contains further details about the genetics data.", 0, 1, name));
          children.add(new Property("datasetId", "string", "Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.", 0, 1, datasetId));
          children.add(new Property("variantsetId", "string", "Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.", 0, 1, variantsetId));
          children.add(new Property("readsetId", "string", "Id of the read in this external repository.", 0, 1, readsetId));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "code", "Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.", 0, 1, type);
          case 116079: /*url*/  return new Property("url", "uri", "URI of an external repository which contains further details about the genetics data.", 0, 1, url);
          case 3373707: /*name*/  return new Property("name", "string", "URI of an external repository which contains further details about the genetics data.", 0, 1, name);
          case -345342029: /*datasetId*/  return new Property("datasetId", "string", "Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.", 0, 1, datasetId);
          case 1929752504: /*variantsetId*/  return new Property("variantsetId", "string", "Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.", 0, 1, variantsetId);
          case -1095407289: /*readsetId*/  return new Property("readsetId", "string", "Id of the read in this external repository.", 0, 1, readsetId);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<RepositoryType>
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -345342029: /*datasetId*/ return this.datasetId == null ? new Base[0] : new Base[] {this.datasetId}; // StringType
        case 1929752504: /*variantsetId*/ return this.variantsetId == null ? new Base[0] : new Base[] {this.variantsetId}; // StringType
        case -1095407289: /*readsetId*/ return this.readsetId == null ? new Base[0] : new Base[] {this.readsetId}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new RepositoryTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<RepositoryType>
          return value;
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -345342029: // datasetId
          this.datasetId = castToString(value); // StringType
          return value;
        case 1929752504: // variantsetId
          this.variantsetId = castToString(value); // StringType
          return value;
        case -1095407289: // readsetId
          this.readsetId = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new RepositoryTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<RepositoryType>
        } else if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("datasetId")) {
          this.datasetId = castToString(value); // StringType
        } else if (name.equals("variantsetId")) {
          this.variantsetId = castToString(value); // StringType
        } else if (name.equals("readsetId")) {
          this.readsetId = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 116079:  return getUrlElement();
        case 3373707:  return getNameElement();
        case -345342029:  return getDatasetIdElement();
        case 1929752504:  return getVariantsetIdElement();
        case -1095407289:  return getReadsetIdElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 116079: /*url*/ return new String[] {"uri"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -345342029: /*datasetId*/ return new String[] {"string"};
        case 1929752504: /*variantsetId*/ return new String[] {"string"};
        case -1095407289: /*readsetId*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.type");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.url");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.name");
        }
        else if (name.equals("datasetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.datasetId");
        }
        else if (name.equals("variantsetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.variantsetId");
        }
        else if (name.equals("readsetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.readsetId");
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceRepositoryComponent copy() {
        MolecularSequenceRepositoryComponent dst = new MolecularSequenceRepositoryComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.url = url == null ? null : url.copy();
        dst.name = name == null ? null : name.copy();
        dst.datasetId = datasetId == null ? null : datasetId.copy();
        dst.variantsetId = variantsetId == null ? null : variantsetId.copy();
        dst.readsetId = readsetId == null ? null : readsetId.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceRepositoryComponent))
          return false;
        MolecularSequenceRepositoryComponent o = (MolecularSequenceRepositoryComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(url, o.url, true) && compareDeep(name, o.name, true)
           && compareDeep(datasetId, o.datasetId, true) && compareDeep(variantsetId, o.variantsetId, true)
           && compareDeep(readsetId, o.readsetId, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceRepositoryComponent))
          return false;
        MolecularSequenceRepositoryComponent o = (MolecularSequenceRepositoryComponent) other_;
        return compareValues(type, o.type, true) && compareValues(url, o.url, true) && compareValues(name, o.name, true)
           && compareValues(datasetId, o.datasetId, true) && compareValues(variantsetId, o.variantsetId, true)
           && compareValues(readsetId, o.readsetId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, url, name, datasetId
          , variantsetId, readsetId);
      }

  public String fhirType() {
    return "MolecularSequence.repository";

  }

  }

    @Block()
    public static class MolecularSequenceStructureVariantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Information about chromosome structure variation DNA change type.
         */
        @Child(name = "variantType", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant change type", formalDefinition="Information about chromosome structure variation DNA change type." )
        protected CodeableConcept variantType;

        /**
         * Used to indicate if the outer and inner start-end values have the same meaning.
         */
        @Child(name = "exact", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Does the structural variant have base pair resolution breakpoints?", formalDefinition="Used to indicate if the outer and inner start-end values have the same meaning." )
        protected BooleanType exact;

        /**
         * Length of the variant chromosome.
         */
        @Child(name = "length", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant length", formalDefinition="Length of the variant chromosome." )
        protected IntegerType length;

        /**
         * Structural variant outer.
         */
        @Child(name = "outer", type = {}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant outer", formalDefinition="Structural variant outer." )
        protected MolecularSequenceStructureVariantOuterComponent outer;

        /**
         * Structural variant inner.
         */
        @Child(name = "inner", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant inner", formalDefinition="Structural variant inner." )
        protected MolecularSequenceStructureVariantInnerComponent inner;

        private static final long serialVersionUID = -1943515207L;

    /**
     * Constructor
     */
      public MolecularSequenceStructureVariantComponent() {
        super();
      }

        /**
         * @return {@link #variantType} (Information about chromosome structure variation DNA change type.)
         */
        public CodeableConcept getVariantType() { 
          if (this.variantType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantComponent.variantType");
            else if (Configuration.doAutoCreate())
              this.variantType = new CodeableConcept(); // cc
          return this.variantType;
        }

        public boolean hasVariantType() { 
          return this.variantType != null && !this.variantType.isEmpty();
        }

        /**
         * @param value {@link #variantType} (Information about chromosome structure variation DNA change type.)
         */
        public MolecularSequenceStructureVariantComponent setVariantType(CodeableConcept value) { 
          this.variantType = value;
          return this;
        }

        /**
         * @return {@link #exact} (Used to indicate if the outer and inner start-end values have the same meaning.). This is the underlying object with id, value and extensions. The accessor "getExact" gives direct access to the value
         */
        public BooleanType getExactElement() { 
          if (this.exact == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantComponent.exact");
            else if (Configuration.doAutoCreate())
              this.exact = new BooleanType(); // bb
          return this.exact;
        }

        public boolean hasExactElement() { 
          return this.exact != null && !this.exact.isEmpty();
        }

        public boolean hasExact() { 
          return this.exact != null && !this.exact.isEmpty();
        }

        /**
         * @param value {@link #exact} (Used to indicate if the outer and inner start-end values have the same meaning.). This is the underlying object with id, value and extensions. The accessor "getExact" gives direct access to the value
         */
        public MolecularSequenceStructureVariantComponent setExactElement(BooleanType value) { 
          this.exact = value;
          return this;
        }

        /**
         * @return Used to indicate if the outer and inner start-end values have the same meaning.
         */
        public boolean getExact() { 
          return this.exact == null || this.exact.isEmpty() ? false : this.exact.getValue();
        }

        /**
         * @param value Used to indicate if the outer and inner start-end values have the same meaning.
         */
        public MolecularSequenceStructureVariantComponent setExact(boolean value) { 
            if (this.exact == null)
              this.exact = new BooleanType();
            this.exact.setValue(value);
          return this;
        }

        /**
         * @return {@link #length} (Length of the variant chromosome.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public IntegerType getLengthElement() { 
          if (this.length == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantComponent.length");
            else if (Configuration.doAutoCreate())
              this.length = new IntegerType(); // bb
          return this.length;
        }

        public boolean hasLengthElement() { 
          return this.length != null && !this.length.isEmpty();
        }

        public boolean hasLength() { 
          return this.length != null && !this.length.isEmpty();
        }

        /**
         * @param value {@link #length} (Length of the variant chromosome.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public MolecularSequenceStructureVariantComponent setLengthElement(IntegerType value) { 
          this.length = value;
          return this;
        }

        /**
         * @return Length of the variant chromosome.
         */
        public int getLength() { 
          return this.length == null || this.length.isEmpty() ? 0 : this.length.getValue();
        }

        /**
         * @param value Length of the variant chromosome.
         */
        public MolecularSequenceStructureVariantComponent setLength(int value) { 
            if (this.length == null)
              this.length = new IntegerType();
            this.length.setValue(value);
          return this;
        }

        /**
         * @return {@link #outer} (Structural variant outer.)
         */
        public MolecularSequenceStructureVariantOuterComponent getOuter() { 
          if (this.outer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantComponent.outer");
            else if (Configuration.doAutoCreate())
              this.outer = new MolecularSequenceStructureVariantOuterComponent(); // cc
          return this.outer;
        }

        public boolean hasOuter() { 
          return this.outer != null && !this.outer.isEmpty();
        }

        /**
         * @param value {@link #outer} (Structural variant outer.)
         */
        public MolecularSequenceStructureVariantComponent setOuter(MolecularSequenceStructureVariantOuterComponent value) { 
          this.outer = value;
          return this;
        }

        /**
         * @return {@link #inner} (Structural variant inner.)
         */
        public MolecularSequenceStructureVariantInnerComponent getInner() { 
          if (this.inner == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantComponent.inner");
            else if (Configuration.doAutoCreate())
              this.inner = new MolecularSequenceStructureVariantInnerComponent(); // cc
          return this.inner;
        }

        public boolean hasInner() { 
          return this.inner != null && !this.inner.isEmpty();
        }

        /**
         * @param value {@link #inner} (Structural variant inner.)
         */
        public MolecularSequenceStructureVariantComponent setInner(MolecularSequenceStructureVariantInnerComponent value) { 
          this.inner = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("variantType", "CodeableConcept", "Information about chromosome structure variation DNA change type.", 0, 1, variantType));
          children.add(new Property("exact", "boolean", "Used to indicate if the outer and inner start-end values have the same meaning.", 0, 1, exact));
          children.add(new Property("length", "integer", "Length of the variant chromosome.", 0, 1, length));
          children.add(new Property("outer", "", "Structural variant outer.", 0, 1, outer));
          children.add(new Property("inner", "", "Structural variant inner.", 0, 1, inner));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1601222305: /*variantType*/  return new Property("variantType", "CodeableConcept", "Information about chromosome structure variation DNA change type.", 0, 1, variantType);
          case 96946943: /*exact*/  return new Property("exact", "boolean", "Used to indicate if the outer and inner start-end values have the same meaning.", 0, 1, exact);
          case -1106363674: /*length*/  return new Property("length", "integer", "Length of the variant chromosome.", 0, 1, length);
          case 106111099: /*outer*/  return new Property("outer", "", "Structural variant outer.", 0, 1, outer);
          case 100355670: /*inner*/  return new Property("inner", "", "Structural variant inner.", 0, 1, inner);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1601222305: /*variantType*/ return this.variantType == null ? new Base[0] : new Base[] {this.variantType}; // CodeableConcept
        case 96946943: /*exact*/ return this.exact == null ? new Base[0] : new Base[] {this.exact}; // BooleanType
        case -1106363674: /*length*/ return this.length == null ? new Base[0] : new Base[] {this.length}; // IntegerType
        case 106111099: /*outer*/ return this.outer == null ? new Base[0] : new Base[] {this.outer}; // MolecularSequenceStructureVariantOuterComponent
        case 100355670: /*inner*/ return this.inner == null ? new Base[0] : new Base[] {this.inner}; // MolecularSequenceStructureVariantInnerComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1601222305: // variantType
          this.variantType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 96946943: // exact
          this.exact = castToBoolean(value); // BooleanType
          return value;
        case -1106363674: // length
          this.length = castToInteger(value); // IntegerType
          return value;
        case 106111099: // outer
          this.outer = (MolecularSequenceStructureVariantOuterComponent) value; // MolecularSequenceStructureVariantOuterComponent
          return value;
        case 100355670: // inner
          this.inner = (MolecularSequenceStructureVariantInnerComponent) value; // MolecularSequenceStructureVariantInnerComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("variantType")) {
          this.variantType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("exact")) {
          this.exact = castToBoolean(value); // BooleanType
        } else if (name.equals("length")) {
          this.length = castToInteger(value); // IntegerType
        } else if (name.equals("outer")) {
          this.outer = (MolecularSequenceStructureVariantOuterComponent) value; // MolecularSequenceStructureVariantOuterComponent
        } else if (name.equals("inner")) {
          this.inner = (MolecularSequenceStructureVariantInnerComponent) value; // MolecularSequenceStructureVariantInnerComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1601222305:  return getVariantType(); 
        case 96946943:  return getExactElement();
        case -1106363674:  return getLengthElement();
        case 106111099:  return getOuter(); 
        case 100355670:  return getInner(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1601222305: /*variantType*/ return new String[] {"CodeableConcept"};
        case 96946943: /*exact*/ return new String[] {"boolean"};
        case -1106363674: /*length*/ return new String[] {"integer"};
        case 106111099: /*outer*/ return new String[] {};
        case 100355670: /*inner*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("variantType")) {
          this.variantType = new CodeableConcept();
          return this.variantType;
        }
        else if (name.equals("exact")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.exact");
        }
        else if (name.equals("length")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.length");
        }
        else if (name.equals("outer")) {
          this.outer = new MolecularSequenceStructureVariantOuterComponent();
          return this.outer;
        }
        else if (name.equals("inner")) {
          this.inner = new MolecularSequenceStructureVariantInnerComponent();
          return this.inner;
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceStructureVariantComponent copy() {
        MolecularSequenceStructureVariantComponent dst = new MolecularSequenceStructureVariantComponent();
        copyValues(dst);
        dst.variantType = variantType == null ? null : variantType.copy();
        dst.exact = exact == null ? null : exact.copy();
        dst.length = length == null ? null : length.copy();
        dst.outer = outer == null ? null : outer.copy();
        dst.inner = inner == null ? null : inner.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceStructureVariantComponent))
          return false;
        MolecularSequenceStructureVariantComponent o = (MolecularSequenceStructureVariantComponent) other_;
        return compareDeep(variantType, o.variantType, true) && compareDeep(exact, o.exact, true) && compareDeep(length, o.length, true)
           && compareDeep(outer, o.outer, true) && compareDeep(inner, o.inner, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceStructureVariantComponent))
          return false;
        MolecularSequenceStructureVariantComponent o = (MolecularSequenceStructureVariantComponent) other_;
        return compareValues(exact, o.exact, true) && compareValues(length, o.length, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(variantType, exact, length
          , outer, inner);
      }

  public String fhirType() {
    return "MolecularSequence.structureVariant";

  }

  }

    @Block()
    public static class MolecularSequenceStructureVariantOuterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant outer start", formalDefinition="Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive." )
        protected IntegerType start;

        /**
         * Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant outer end", formalDefinition="Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
        protected IntegerType end;

        private static final long serialVersionUID = -1798864889L;

    /**
     * Constructor
     */
      public MolecularSequenceStructureVariantOuterComponent() {
        super();
      }

        /**
         * @return {@link #start} (Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantOuterComponent.start");
            else if (Configuration.doAutoCreate())
              this.start = new IntegerType(); // bb
          return this.start;
        }

        public boolean hasStartElement() { 
          return this.start != null && !this.start.isEmpty();
        }

        public boolean hasStart() { 
          return this.start != null && !this.start.isEmpty();
        }

        /**
         * @param value {@link #start} (Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public MolecularSequenceStructureVariantOuterComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public MolecularSequenceStructureVariantOuterComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantOuterComponent.end");
            else if (Configuration.doAutoCreate())
              this.end = new IntegerType(); // bb
          return this.end;
        }

        public boolean hasEndElement() { 
          return this.end != null && !this.end.isEmpty();
        }

        public boolean hasEnd() { 
          return this.end != null && !this.end.isEmpty();
        }

        /**
         * @param value {@link #end} (Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public MolecularSequenceStructureVariantOuterComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public MolecularSequenceStructureVariantOuterComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("start", "integer", "Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start));
          children.add(new Property("end", "integer", "Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 109757538: /*start*/  return new Property("start", "integer", "Structural variant outer start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start);
          case 100571: /*end*/  return new Property("end", "integer", "Structural variant outer end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // IntegerType
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // IntegerType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          return value;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start")) {
          this.start = castToInteger(value); // IntegerType
        } else if (name.equals("end")) {
          this.end = castToInteger(value); // IntegerType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538:  return getStartElement();
        case 100571:  return getEndElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return new String[] {"integer"};
        case 100571: /*end*/ return new String[] {"integer"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.end");
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceStructureVariantOuterComponent copy() {
        MolecularSequenceStructureVariantOuterComponent dst = new MolecularSequenceStructureVariantOuterComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceStructureVariantOuterComponent))
          return false;
        MolecularSequenceStructureVariantOuterComponent o = (MolecularSequenceStructureVariantOuterComponent) other_;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceStructureVariantOuterComponent))
          return false;
        MolecularSequenceStructureVariantOuterComponent o = (MolecularSequenceStructureVariantOuterComponent) other_;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(start, end);
      }

  public String fhirType() {
    return "MolecularSequence.structureVariant.outer";

  }

  }

    @Block()
    public static class MolecularSequenceStructureVariantInnerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant inner start", formalDefinition="Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive." )
        protected IntegerType start;

        /**
         * Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural variant inner end", formalDefinition="Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
        protected IntegerType end;

        private static final long serialVersionUID = -1798864889L;

    /**
     * Constructor
     */
      public MolecularSequenceStructureVariantInnerComponent() {
        super();
      }

        /**
         * @return {@link #start} (Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantInnerComponent.start");
            else if (Configuration.doAutoCreate())
              this.start = new IntegerType(); // bb
          return this.start;
        }

        public boolean hasStartElement() { 
          return this.start != null && !this.start.isEmpty();
        }

        public boolean hasStart() { 
          return this.start != null && !this.start.isEmpty();
        }

        /**
         * @param value {@link #start} (Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public MolecularSequenceStructureVariantInnerComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public MolecularSequenceStructureVariantInnerComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MolecularSequenceStructureVariantInnerComponent.end");
            else if (Configuration.doAutoCreate())
              this.end = new IntegerType(); // bb
          return this.end;
        }

        public boolean hasEndElement() { 
          return this.end != null && !this.end.isEmpty();
        }

        public boolean hasEnd() { 
          return this.end != null && !this.end.isEmpty();
        }

        /**
         * @param value {@link #end} (Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public MolecularSequenceStructureVariantInnerComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public MolecularSequenceStructureVariantInnerComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("start", "integer", "Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start));
          children.add(new Property("end", "integer", "Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 109757538: /*start*/  return new Property("start", "integer", "Structural variant inner start. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, 1, start);
          case 100571: /*end*/  return new Property("end", "integer", "Structural variant inner end. If the coordinate system is 0-based then end is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, 1, end);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // IntegerType
        case 100571: /*end*/ return this.end == null ? new Base[0] : new Base[] {this.end}; // IntegerType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          return value;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start")) {
          this.start = castToInteger(value); // IntegerType
        } else if (name.equals("end")) {
          this.end = castToInteger(value); // IntegerType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538:  return getStartElement();
        case 100571:  return getEndElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: /*start*/ return new String[] {"integer"};
        case 100571: /*end*/ return new String[] {"integer"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.end");
        }
        else
          return super.addChild(name);
      }

      public MolecularSequenceStructureVariantInnerComponent copy() {
        MolecularSequenceStructureVariantInnerComponent dst = new MolecularSequenceStructureVariantInnerComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceStructureVariantInnerComponent))
          return false;
        MolecularSequenceStructureVariantInnerComponent o = (MolecularSequenceStructureVariantInnerComponent) other_;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequenceStructureVariantInnerComponent))
          return false;
        MolecularSequenceStructureVariantInnerComponent o = (MolecularSequenceStructureVariantInnerComponent) other_;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(start, end);
      }

  public String fhirType() {
    return "MolecularSequence.structureVariant.inner";

  }

  }

    /**
     * A unique identifier for this particular sequence instance. This is a FHIR-defined id.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique ID for this particular sequence. This is a FHIR-defined id", formalDefinition="A unique identifier for this particular sequence instance. This is a FHIR-defined id." )
    protected List<Identifier> identifier;

    /**
     * Amino Acid Sequence/ DNA Sequence / RNA Sequence.
     */
    @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="aa | dna | rna", formalDefinition="Amino Acid Sequence/ DNA Sequence / RNA Sequence." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sequence-type")
    protected Enumeration<SequenceType> type;

    /**
     * Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).
     */
    @Child(name = "coordinateSystem", type = {IntegerType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Base number of coordinate system (0 for 0-based numbering or coordinates, inclusive start, exclusive end, 1 for 1-based numbering, inclusive start, inclusive end)", formalDefinition="Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end)." )
    protected IntegerType coordinateSystem;

    /**
     * The patient whose sequencing results are described by this resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what this is about", formalDefinition="The patient whose sequencing results are described by this resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient whose sequencing results are described by this resource.)
     */
    protected Patient patientTarget;

    /**
     * Specimen used for sequencing.
     */
    @Child(name = "specimen", type = {Specimen.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Specimen used for sequencing", formalDefinition="Specimen used for sequencing." )
    protected Reference specimen;

    /**
     * The actual object that is the target of the reference (Specimen used for sequencing.)
     */
    protected Specimen specimenTarget;

    /**
     * The method for sequencing, for example, chip information.
     */
    @Child(name = "device", type = {Device.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The method for sequencing", formalDefinition="The method for sequencing, for example, chip information." )
    protected Reference device;

    /**
     * The actual object that is the target of the reference (The method for sequencing, for example, chip information.)
     */
    protected Device deviceTarget;

    /**
     * The organization or lab that should be responsible for this result.
     */
    @Child(name = "performer", type = {Organization.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who should be responsible for test result", formalDefinition="The organization or lab that should be responsible for this result." )
    protected Reference performer;

    /**
     * The actual object that is the target of the reference (The organization or lab that should be responsible for this result.)
     */
    protected Organization performerTarget;

    /**
     * The number of copies of the sequence of interest. (RNASeq).
     */
    @Child(name = "quantity", type = {Quantity.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The number of copies of the sequence of interest.  (RNASeq)", formalDefinition="The number of copies of the sequence of interest. (RNASeq)." )
    protected Quantity quantity;

    /**
     * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
     */
    @Child(name = "referenceSeq", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A sequence used as reference", formalDefinition="A sequence that is used as a reference to describe variants that are present in a sequence analyzed." )
    protected MolecularSequenceReferenceSeqComponent referenceSeq;

    /**
     * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string.
     */
    @Child(name = "variant", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Variant in sequence", formalDefinition="The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string." )
    protected List<MolecularSequenceVariantComponent> variant;

    /**
     * Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     */
    @Child(name = "observedSeq", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Sequence that was observed", formalDefinition="Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd." )
    protected StringType observedSeq;

    /**
     * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
     */
    @Child(name = "quality", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An set of value as quality of sequence", formalDefinition="An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686))." )
    protected List<MolecularSequenceQualityComponent> quality;

    /**
     * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.
     */
    @Child(name = "readCoverage", type = {IntegerType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Average number of reads representing a given nucleotide in the reconstructed sequence", formalDefinition="Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence." )
    protected IntegerType readCoverage;

    /**
     * Configurations of the external repository. The repository shall store target's observedSeq or records related with target's observedSeq.
     */
    @Child(name = "repository", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External repository which contains detailed report related with observedSeq in this resource", formalDefinition="Configurations of the external repository. The repository shall store target's observedSeq or records related with target's observedSeq." )
    protected List<MolecularSequenceRepositoryComponent> repository;

    /**
     * Pointer to next atomic sequence which at most contains one variant.
     */
    @Child(name = "pointer", type = {MolecularSequence.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Pointer to next atomic sequence", formalDefinition="Pointer to next atomic sequence which at most contains one variant." )
    protected List<Reference> pointer;
    /**
     * The actual objects that are the target of the reference (Pointer to next atomic sequence which at most contains one variant.)
     */
    protected List<MolecularSequence> pointerTarget;


    /**
     * Information about chromosome structure variation.
     */
    @Child(name = "structureVariant", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Structural variant", formalDefinition="Information about chromosome structure variation." )
    protected List<MolecularSequenceStructureVariantComponent> structureVariant;

    private static final long serialVersionUID = -1541133500L;

  /**
   * Constructor
   */
    public MolecularSequence() {
      super();
    }

  /**
   * Constructor
   */
    public MolecularSequence(IntegerType coordinateSystem) {
      super();
      this.coordinateSystem = coordinateSystem;
    }

    /**
     * @return {@link #identifier} (A unique identifier for this particular sequence instance. This is a FHIR-defined id.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MolecularSequence setIdentifier(List<Identifier> theIdentifier) { 
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

    public MolecularSequence addIdentifier(Identifier t) { //3
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
     * @return {@link #type} (Amino Acid Sequence/ DNA Sequence / RNA Sequence.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<SequenceType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<SequenceType>(new SequenceTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Amino Acid Sequence/ DNA Sequence / RNA Sequence.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public MolecularSequence setTypeElement(Enumeration<SequenceType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Amino Acid Sequence/ DNA Sequence / RNA Sequence.
     */
    public SequenceType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Amino Acid Sequence/ DNA Sequence / RNA Sequence.
     */
    public MolecularSequence setType(SequenceType value) { 
      if (value == null)
        this.type = null;
      else {
        if (this.type == null)
          this.type = new Enumeration<SequenceType>(new SequenceTypeEnumFactory());
        this.type.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #coordinateSystem} (Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).). This is the underlying object with id, value and extensions. The accessor "getCoordinateSystem" gives direct access to the value
     */
    public IntegerType getCoordinateSystemElement() { 
      if (this.coordinateSystem == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.coordinateSystem");
        else if (Configuration.doAutoCreate())
          this.coordinateSystem = new IntegerType(); // bb
      return this.coordinateSystem;
    }

    public boolean hasCoordinateSystemElement() { 
      return this.coordinateSystem != null && !this.coordinateSystem.isEmpty();
    }

    public boolean hasCoordinateSystem() { 
      return this.coordinateSystem != null && !this.coordinateSystem.isEmpty();
    }

    /**
     * @param value {@link #coordinateSystem} (Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).). This is the underlying object with id, value and extensions. The accessor "getCoordinateSystem" gives direct access to the value
     */
    public MolecularSequence setCoordinateSystemElement(IntegerType value) { 
      this.coordinateSystem = value;
      return this;
    }

    /**
     * @return Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).
     */
    public int getCoordinateSystem() { 
      return this.coordinateSystem == null || this.coordinateSystem.isEmpty() ? 0 : this.coordinateSystem.getValue();
    }

    /**
     * @param value Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).
     */
    public MolecularSequence setCoordinateSystem(int value) { 
        if (this.coordinateSystem == null)
          this.coordinateSystem = new IntegerType();
        this.coordinateSystem.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (The patient whose sequencing results are described by this resource.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient whose sequencing results are described by this resource.)
     */
    public MolecularSequence setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient whose sequencing results are described by this resource.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient whose sequencing results are described by this resource.)
     */
    public MolecularSequence setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #specimen} (Specimen used for sequencing.)
     */
    public Reference getSpecimen() { 
      if (this.specimen == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.specimen");
        else if (Configuration.doAutoCreate())
          this.specimen = new Reference(); // cc
      return this.specimen;
    }

    public boolean hasSpecimen() { 
      return this.specimen != null && !this.specimen.isEmpty();
    }

    /**
     * @param value {@link #specimen} (Specimen used for sequencing.)
     */
    public MolecularSequence setSpecimen(Reference value) { 
      this.specimen = value;
      return this;
    }

    /**
     * @return {@link #specimen} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Specimen used for sequencing.)
     */
    public Specimen getSpecimenTarget() { 
      if (this.specimenTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.specimen");
        else if (Configuration.doAutoCreate())
          this.specimenTarget = new Specimen(); // aa
      return this.specimenTarget;
    }

    /**
     * @param value {@link #specimen} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Specimen used for sequencing.)
     */
    public MolecularSequence setSpecimenTarget(Specimen value) { 
      this.specimenTarget = value;
      return this;
    }

    /**
     * @return {@link #device} (The method for sequencing, for example, chip information.)
     */
    public Reference getDevice() { 
      if (this.device == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.device");
        else if (Configuration.doAutoCreate())
          this.device = new Reference(); // cc
      return this.device;
    }

    public boolean hasDevice() { 
      return this.device != null && !this.device.isEmpty();
    }

    /**
     * @param value {@link #device} (The method for sequencing, for example, chip information.)
     */
    public MolecularSequence setDevice(Reference value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #device} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The method for sequencing, for example, chip information.)
     */
    public Device getDeviceTarget() { 
      if (this.deviceTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.device");
        else if (Configuration.doAutoCreate())
          this.deviceTarget = new Device(); // aa
      return this.deviceTarget;
    }

    /**
     * @param value {@link #device} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The method for sequencing, for example, chip information.)
     */
    public MolecularSequence setDeviceTarget(Device value) { 
      this.deviceTarget = value;
      return this;
    }

    /**
     * @return {@link #performer} (The organization or lab that should be responsible for this result.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.performer");
        else if (Configuration.doAutoCreate())
          this.performer = new Reference(); // cc
      return this.performer;
    }

    public boolean hasPerformer() { 
      return this.performer != null && !this.performer.isEmpty();
    }

    /**
     * @param value {@link #performer} (The organization or lab that should be responsible for this result.)
     */
    public MolecularSequence setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization or lab that should be responsible for this result.)
     */
    public Organization getPerformerTarget() { 
      if (this.performerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.performer");
        else if (Configuration.doAutoCreate())
          this.performerTarget = new Organization(); // aa
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization or lab that should be responsible for this result.)
     */
    public MolecularSequence setPerformerTarget(Organization value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #quantity} (The number of copies of the sequence of interest. (RNASeq).)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The number of copies of the sequence of interest. (RNASeq).)
     */
    public MolecularSequence setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #referenceSeq} (A sequence that is used as a reference to describe variants that are present in a sequence analyzed.)
     */
    public MolecularSequenceReferenceSeqComponent getReferenceSeq() { 
      if (this.referenceSeq == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.referenceSeq");
        else if (Configuration.doAutoCreate())
          this.referenceSeq = new MolecularSequenceReferenceSeqComponent(); // cc
      return this.referenceSeq;
    }

    public boolean hasReferenceSeq() { 
      return this.referenceSeq != null && !this.referenceSeq.isEmpty();
    }

    /**
     * @param value {@link #referenceSeq} (A sequence that is used as a reference to describe variants that are present in a sequence analyzed.)
     */
    public MolecularSequence setReferenceSeq(MolecularSequenceReferenceSeqComponent value) { 
      this.referenceSeq = value;
      return this;
    }

    /**
     * @return {@link #variant} (The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string.)
     */
    public List<MolecularSequenceVariantComponent> getVariant() { 
      if (this.variant == null)
        this.variant = new ArrayList<MolecularSequenceVariantComponent>();
      return this.variant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MolecularSequence setVariant(List<MolecularSequenceVariantComponent> theVariant) { 
      this.variant = theVariant;
      return this;
    }

    public boolean hasVariant() { 
      if (this.variant == null)
        return false;
      for (MolecularSequenceVariantComponent item : this.variant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MolecularSequenceVariantComponent addVariant() { //3
      MolecularSequenceVariantComponent t = new MolecularSequenceVariantComponent();
      if (this.variant == null)
        this.variant = new ArrayList<MolecularSequenceVariantComponent>();
      this.variant.add(t);
      return t;
    }

    public MolecularSequence addVariant(MolecularSequenceVariantComponent t) { //3
      if (t == null)
        return this;
      if (this.variant == null)
        this.variant = new ArrayList<MolecularSequenceVariantComponent>();
      this.variant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #variant}, creating it if it does not already exist
     */
    public MolecularSequenceVariantComponent getVariantFirstRep() { 
      if (getVariant().isEmpty()) {
        addVariant();
      }
      return getVariant().get(0);
    }

    /**
     * @return {@link #observedSeq} (Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
     */
    public StringType getObservedSeqElement() { 
      if (this.observedSeq == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.observedSeq");
        else if (Configuration.doAutoCreate())
          this.observedSeq = new StringType(); // bb
      return this.observedSeq;
    }

    public boolean hasObservedSeqElement() { 
      return this.observedSeq != null && !this.observedSeq.isEmpty();
    }

    public boolean hasObservedSeq() { 
      return this.observedSeq != null && !this.observedSeq.isEmpty();
    }

    /**
     * @param value {@link #observedSeq} (Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
     */
    public MolecularSequence setObservedSeqElement(StringType value) { 
      this.observedSeq = value;
      return this;
    }

    /**
     * @return Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     */
    public String getObservedSeq() { 
      return this.observedSeq == null ? null : this.observedSeq.getValue();
    }

    /**
     * @param value Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     */
    public MolecularSequence setObservedSeq(String value) { 
      if (Utilities.noString(value))
        this.observedSeq = null;
      else {
        if (this.observedSeq == null)
          this.observedSeq = new StringType();
        this.observedSeq.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #quality} (An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).)
     */
    public List<MolecularSequenceQualityComponent> getQuality() { 
      if (this.quality == null)
        this.quality = new ArrayList<MolecularSequenceQualityComponent>();
      return this.quality;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MolecularSequence setQuality(List<MolecularSequenceQualityComponent> theQuality) { 
      this.quality = theQuality;
      return this;
    }

    public boolean hasQuality() { 
      if (this.quality == null)
        return false;
      for (MolecularSequenceQualityComponent item : this.quality)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MolecularSequenceQualityComponent addQuality() { //3
      MolecularSequenceQualityComponent t = new MolecularSequenceQualityComponent();
      if (this.quality == null)
        this.quality = new ArrayList<MolecularSequenceQualityComponent>();
      this.quality.add(t);
      return t;
    }

    public MolecularSequence addQuality(MolecularSequenceQualityComponent t) { //3
      if (t == null)
        return this;
      if (this.quality == null)
        this.quality = new ArrayList<MolecularSequenceQualityComponent>();
      this.quality.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #quality}, creating it if it does not already exist
     */
    public MolecularSequenceQualityComponent getQualityFirstRep() { 
      if (getQuality().isEmpty()) {
        addQuality();
      }
      return getQuality().get(0);
    }

    /**
     * @return {@link #readCoverage} (Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.). This is the underlying object with id, value and extensions. The accessor "getReadCoverage" gives direct access to the value
     */
    public IntegerType getReadCoverageElement() { 
      if (this.readCoverage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MolecularSequence.readCoverage");
        else if (Configuration.doAutoCreate())
          this.readCoverage = new IntegerType(); // bb
      return this.readCoverage;
    }

    public boolean hasReadCoverageElement() { 
      return this.readCoverage != null && !this.readCoverage.isEmpty();
    }

    public boolean hasReadCoverage() { 
      return this.readCoverage != null && !this.readCoverage.isEmpty();
    }

    /**
     * @param value {@link #readCoverage} (Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.). This is the underlying object with id, value and extensions. The accessor "getReadCoverage" gives direct access to the value
     */
    public MolecularSequence setReadCoverageElement(IntegerType value) { 
      this.readCoverage = value;
      return this;
    }

    /**
     * @return Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.
     */
    public int getReadCoverage() { 
      return this.readCoverage == null || this.readCoverage.isEmpty() ? 0 : this.readCoverage.getValue();
    }

    /**
     * @param value Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.
     */
    public MolecularSequence setReadCoverage(int value) { 
        if (this.readCoverage == null)
          this.readCoverage = new IntegerType();
        this.readCoverage.setValue(value);
      return this;
    }

    /**
     * @return {@link #repository} (Configurations of the external repository. The repository shall store target's observedSeq or records related with target's observedSeq.)
     */
    public List<MolecularSequenceRepositoryComponent> getRepository() { 
      if (this.repository == null)
        this.repository = new ArrayList<MolecularSequenceRepositoryComponent>();
      return this.repository;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MolecularSequence setRepository(List<MolecularSequenceRepositoryComponent> theRepository) { 
      this.repository = theRepository;
      return this;
    }

    public boolean hasRepository() { 
      if (this.repository == null)
        return false;
      for (MolecularSequenceRepositoryComponent item : this.repository)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MolecularSequenceRepositoryComponent addRepository() { //3
      MolecularSequenceRepositoryComponent t = new MolecularSequenceRepositoryComponent();
      if (this.repository == null)
        this.repository = new ArrayList<MolecularSequenceRepositoryComponent>();
      this.repository.add(t);
      return t;
    }

    public MolecularSequence addRepository(MolecularSequenceRepositoryComponent t) { //3
      if (t == null)
        return this;
      if (this.repository == null)
        this.repository = new ArrayList<MolecularSequenceRepositoryComponent>();
      this.repository.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #repository}, creating it if it does not already exist
     */
    public MolecularSequenceRepositoryComponent getRepositoryFirstRep() { 
      if (getRepository().isEmpty()) {
        addRepository();
      }
      return getRepository().get(0);
    }

    /**
     * @return {@link #pointer} (Pointer to next atomic sequence which at most contains one variant.)
     */
    public List<Reference> getPointer() { 
      if (this.pointer == null)
        this.pointer = new ArrayList<Reference>();
      return this.pointer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MolecularSequence setPointer(List<Reference> thePointer) { 
      this.pointer = thePointer;
      return this;
    }

    public boolean hasPointer() { 
      if (this.pointer == null)
        return false;
      for (Reference item : this.pointer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPointer() { //3
      Reference t = new Reference();
      if (this.pointer == null)
        this.pointer = new ArrayList<Reference>();
      this.pointer.add(t);
      return t;
    }

    public MolecularSequence addPointer(Reference t) { //3
      if (t == null)
        return this;
      if (this.pointer == null)
        this.pointer = new ArrayList<Reference>();
      this.pointer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #pointer}, creating it if it does not already exist
     */
    public Reference getPointerFirstRep() { 
      if (getPointer().isEmpty()) {
        addPointer();
      }
      return getPointer().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<MolecularSequence> getPointerTarget() { 
      if (this.pointerTarget == null)
        this.pointerTarget = new ArrayList<MolecularSequence>();
      return this.pointerTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public MolecularSequence addPointerTarget() { 
      MolecularSequence r = new MolecularSequence();
      if (this.pointerTarget == null)
        this.pointerTarget = new ArrayList<MolecularSequence>();
      this.pointerTarget.add(r);
      return r;
    }

    /**
     * @return {@link #structureVariant} (Information about chromosome structure variation.)
     */
    public List<MolecularSequenceStructureVariantComponent> getStructureVariant() { 
      if (this.structureVariant == null)
        this.structureVariant = new ArrayList<MolecularSequenceStructureVariantComponent>();
      return this.structureVariant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MolecularSequence setStructureVariant(List<MolecularSequenceStructureVariantComponent> theStructureVariant) { 
      this.structureVariant = theStructureVariant;
      return this;
    }

    public boolean hasStructureVariant() { 
      if (this.structureVariant == null)
        return false;
      for (MolecularSequenceStructureVariantComponent item : this.structureVariant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public MolecularSequenceStructureVariantComponent addStructureVariant() { //3
      MolecularSequenceStructureVariantComponent t = new MolecularSequenceStructureVariantComponent();
      if (this.structureVariant == null)
        this.structureVariant = new ArrayList<MolecularSequenceStructureVariantComponent>();
      this.structureVariant.add(t);
      return t;
    }

    public MolecularSequence addStructureVariant(MolecularSequenceStructureVariantComponent t) { //3
      if (t == null)
        return this;
      if (this.structureVariant == null)
        this.structureVariant = new ArrayList<MolecularSequenceStructureVariantComponent>();
      this.structureVariant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #structureVariant}, creating it if it does not already exist
     */
    public MolecularSequenceStructureVariantComponent getStructureVariantFirstRep() { 
      if (getStructureVariant().isEmpty()) {
        addStructureVariant();
      }
      return getStructureVariant().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A unique identifier for this particular sequence instance. This is a FHIR-defined id.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("type", "code", "Amino Acid Sequence/ DNA Sequence / RNA Sequence.", 0, 1, type));
        children.add(new Property("coordinateSystem", "integer", "Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).", 0, 1, coordinateSystem));
        children.add(new Property("patient", "Reference(Patient)", "The patient whose sequencing results are described by this resource.", 0, 1, patient));
        children.add(new Property("specimen", "Reference(Specimen)", "Specimen used for sequencing.", 0, 1, specimen));
        children.add(new Property("device", "Reference(Device)", "The method for sequencing, for example, chip information.", 0, 1, device));
        children.add(new Property("performer", "Reference(Organization)", "The organization or lab that should be responsible for this result.", 0, 1, performer));
        children.add(new Property("quantity", "Quantity", "The number of copies of the sequence of interest. (RNASeq).", 0, 1, quantity));
        children.add(new Property("referenceSeq", "", "A sequence that is used as a reference to describe variants that are present in a sequence analyzed.", 0, 1, referenceSeq));
        children.add(new Property("variant", "", "The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string.", 0, java.lang.Integer.MAX_VALUE, variant));
        children.add(new Property("observedSeq", "string", "Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.", 0, 1, observedSeq));
        children.add(new Property("quality", "", "An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).", 0, java.lang.Integer.MAX_VALUE, quality));
        children.add(new Property("readCoverage", "integer", "Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.", 0, 1, readCoverage));
        children.add(new Property("repository", "", "Configurations of the external repository. The repository shall store target's observedSeq or records related with target's observedSeq.", 0, java.lang.Integer.MAX_VALUE, repository));
        children.add(new Property("pointer", "Reference(MolecularSequence)", "Pointer to next atomic sequence which at most contains one variant.", 0, java.lang.Integer.MAX_VALUE, pointer));
        children.add(new Property("structureVariant", "", "Information about chromosome structure variation.", 0, java.lang.Integer.MAX_VALUE, structureVariant));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier for this particular sequence instance. This is a FHIR-defined id.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 3575610: /*type*/  return new Property("type", "code", "Amino Acid Sequence/ DNA Sequence / RNA Sequence.", 0, 1, type);
        case 354212295: /*coordinateSystem*/  return new Property("coordinateSystem", "integer", "Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).", 0, 1, coordinateSystem);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "The patient whose sequencing results are described by this resource.", 0, 1, patient);
        case -2132868344: /*specimen*/  return new Property("specimen", "Reference(Specimen)", "Specimen used for sequencing.", 0, 1, specimen);
        case -1335157162: /*device*/  return new Property("device", "Reference(Device)", "The method for sequencing, for example, chip information.", 0, 1, device);
        case 481140686: /*performer*/  return new Property("performer", "Reference(Organization)", "The organization or lab that should be responsible for this result.", 0, 1, performer);
        case -1285004149: /*quantity*/  return new Property("quantity", "Quantity", "The number of copies of the sequence of interest. (RNASeq).", 0, 1, quantity);
        case -502547180: /*referenceSeq*/  return new Property("referenceSeq", "", "A sequence that is used as a reference to describe variants that are present in a sequence analyzed.", 0, 1, referenceSeq);
        case 236785797: /*variant*/  return new Property("variant", "", "The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string.", 0, java.lang.Integer.MAX_VALUE, variant);
        case 125541495: /*observedSeq*/  return new Property("observedSeq", "string", "Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall start from referenceSeq.windowStart and end by referenceSeq.windowEnd.", 0, 1, observedSeq);
        case 651215103: /*quality*/  return new Property("quality", "", "An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).", 0, java.lang.Integer.MAX_VALUE, quality);
        case -1798816354: /*readCoverage*/  return new Property("readCoverage", "integer", "Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.", 0, 1, readCoverage);
        case 1950800714: /*repository*/  return new Property("repository", "", "Configurations of the external repository. The repository shall store target's observedSeq or records related with target's observedSeq.", 0, java.lang.Integer.MAX_VALUE, repository);
        case -400605635: /*pointer*/  return new Property("pointer", "Reference(MolecularSequence)", "Pointer to next atomic sequence which at most contains one variant.", 0, java.lang.Integer.MAX_VALUE, pointer);
        case 757269394: /*structureVariant*/  return new Property("structureVariant", "", "Information about chromosome structure variation.", 0, java.lang.Integer.MAX_VALUE, structureVariant);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<SequenceType>
        case 354212295: /*coordinateSystem*/ return this.coordinateSystem == null ? new Base[0] : new Base[] {this.coordinateSystem}; // IntegerType
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -2132868344: /*specimen*/ return this.specimen == null ? new Base[0] : new Base[] {this.specimen}; // Reference
        case -1335157162: /*device*/ return this.device == null ? new Base[0] : new Base[] {this.device}; // Reference
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -502547180: /*referenceSeq*/ return this.referenceSeq == null ? new Base[0] : new Base[] {this.referenceSeq}; // MolecularSequenceReferenceSeqComponent
        case 236785797: /*variant*/ return this.variant == null ? new Base[0] : this.variant.toArray(new Base[this.variant.size()]); // MolecularSequenceVariantComponent
        case 125541495: /*observedSeq*/ return this.observedSeq == null ? new Base[0] : new Base[] {this.observedSeq}; // StringType
        case 651215103: /*quality*/ return this.quality == null ? new Base[0] : this.quality.toArray(new Base[this.quality.size()]); // MolecularSequenceQualityComponent
        case -1798816354: /*readCoverage*/ return this.readCoverage == null ? new Base[0] : new Base[] {this.readCoverage}; // IntegerType
        case 1950800714: /*repository*/ return this.repository == null ? new Base[0] : this.repository.toArray(new Base[this.repository.size()]); // MolecularSequenceRepositoryComponent
        case -400605635: /*pointer*/ return this.pointer == null ? new Base[0] : this.pointer.toArray(new Base[this.pointer.size()]); // Reference
        case 757269394: /*structureVariant*/ return this.structureVariant == null ? new Base[0] : this.structureVariant.toArray(new Base[this.structureVariant.size()]); // MolecularSequenceStructureVariantComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 3575610: // type
          value = new SequenceTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<SequenceType>
          return value;
        case 354212295: // coordinateSystem
          this.coordinateSystem = castToInteger(value); // IntegerType
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case -2132868344: // specimen
          this.specimen = castToReference(value); // Reference
          return value;
        case -1335157162: // device
          this.device = castToReference(value); // Reference
          return value;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -502547180: // referenceSeq
          this.referenceSeq = (MolecularSequenceReferenceSeqComponent) value; // MolecularSequenceReferenceSeqComponent
          return value;
        case 236785797: // variant
          this.getVariant().add((MolecularSequenceVariantComponent) value); // MolecularSequenceVariantComponent
          return value;
        case 125541495: // observedSeq
          this.observedSeq = castToString(value); // StringType
          return value;
        case 651215103: // quality
          this.getQuality().add((MolecularSequenceQualityComponent) value); // MolecularSequenceQualityComponent
          return value;
        case -1798816354: // readCoverage
          this.readCoverage = castToInteger(value); // IntegerType
          return value;
        case 1950800714: // repository
          this.getRepository().add((MolecularSequenceRepositoryComponent) value); // MolecularSequenceRepositoryComponent
          return value;
        case -400605635: // pointer
          this.getPointer().add(castToReference(value)); // Reference
          return value;
        case 757269394: // structureVariant
          this.getStructureVariant().add((MolecularSequenceStructureVariantComponent) value); // MolecularSequenceStructureVariantComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("type")) {
          value = new SequenceTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<SequenceType>
        } else if (name.equals("coordinateSystem")) {
          this.coordinateSystem = castToInteger(value); // IntegerType
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("specimen")) {
          this.specimen = castToReference(value); // Reference
        } else if (name.equals("device")) {
          this.device = castToReference(value); // Reference
        } else if (name.equals("performer")) {
          this.performer = castToReference(value); // Reference
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("referenceSeq")) {
          this.referenceSeq = (MolecularSequenceReferenceSeqComponent) value; // MolecularSequenceReferenceSeqComponent
        } else if (name.equals("variant")) {
          this.getVariant().add((MolecularSequenceVariantComponent) value);
        } else if (name.equals("observedSeq")) {
          this.observedSeq = castToString(value); // StringType
        } else if (name.equals("quality")) {
          this.getQuality().add((MolecularSequenceQualityComponent) value);
        } else if (name.equals("readCoverage")) {
          this.readCoverage = castToInteger(value); // IntegerType
        } else if (name.equals("repository")) {
          this.getRepository().add((MolecularSequenceRepositoryComponent) value);
        } else if (name.equals("pointer")) {
          this.getPointer().add(castToReference(value));
        } else if (name.equals("structureVariant")) {
          this.getStructureVariant().add((MolecularSequenceStructureVariantComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 3575610:  return getTypeElement();
        case 354212295:  return getCoordinateSystemElement();
        case -791418107:  return getPatient(); 
        case -2132868344:  return getSpecimen(); 
        case -1335157162:  return getDevice(); 
        case 481140686:  return getPerformer(); 
        case -1285004149:  return getQuantity(); 
        case -502547180:  return getReferenceSeq(); 
        case 236785797:  return addVariant(); 
        case 125541495:  return getObservedSeqElement();
        case 651215103:  return addQuality(); 
        case -1798816354:  return getReadCoverageElement();
        case 1950800714:  return addRepository(); 
        case -400605635:  return addPointer(); 
        case 757269394:  return addStructureVariant(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 3575610: /*type*/ return new String[] {"code"};
        case 354212295: /*coordinateSystem*/ return new String[] {"integer"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case -2132868344: /*specimen*/ return new String[] {"Reference"};
        case -1335157162: /*device*/ return new String[] {"Reference"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case -502547180: /*referenceSeq*/ return new String[] {};
        case 236785797: /*variant*/ return new String[] {};
        case 125541495: /*observedSeq*/ return new String[] {"string"};
        case 651215103: /*quality*/ return new String[] {};
        case -1798816354: /*readCoverage*/ return new String[] {"integer"};
        case 1950800714: /*repository*/ return new String[] {};
        case -400605635: /*pointer*/ return new String[] {"Reference"};
        case 757269394: /*structureVariant*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.type");
        }
        else if (name.equals("coordinateSystem")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.coordinateSystem");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("specimen")) {
          this.specimen = new Reference();
          return this.specimen;
        }
        else if (name.equals("device")) {
          this.device = new Reference();
          return this.device;
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("referenceSeq")) {
          this.referenceSeq = new MolecularSequenceReferenceSeqComponent();
          return this.referenceSeq;
        }
        else if (name.equals("variant")) {
          return addVariant();
        }
        else if (name.equals("observedSeq")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.observedSeq");
        }
        else if (name.equals("quality")) {
          return addQuality();
        }
        else if (name.equals("readCoverage")) {
          throw new FHIRException("Cannot call addChild on a primitive type MolecularSequence.readCoverage");
        }
        else if (name.equals("repository")) {
          return addRepository();
        }
        else if (name.equals("pointer")) {
          return addPointer();
        }
        else if (name.equals("structureVariant")) {
          return addStructureVariant();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MolecularSequence";

  }

      public MolecularSequence copy() {
        MolecularSequence dst = new MolecularSequence();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        dst.coordinateSystem = coordinateSystem == null ? null : coordinateSystem.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.specimen = specimen == null ? null : specimen.copy();
        dst.device = device == null ? null : device.copy();
        dst.performer = performer == null ? null : performer.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.referenceSeq = referenceSeq == null ? null : referenceSeq.copy();
        if (variant != null) {
          dst.variant = new ArrayList<MolecularSequenceVariantComponent>();
          for (MolecularSequenceVariantComponent i : variant)
            dst.variant.add(i.copy());
        };
        dst.observedSeq = observedSeq == null ? null : observedSeq.copy();
        if (quality != null) {
          dst.quality = new ArrayList<MolecularSequenceQualityComponent>();
          for (MolecularSequenceQualityComponent i : quality)
            dst.quality.add(i.copy());
        };
        dst.readCoverage = readCoverage == null ? null : readCoverage.copy();
        if (repository != null) {
          dst.repository = new ArrayList<MolecularSequenceRepositoryComponent>();
          for (MolecularSequenceRepositoryComponent i : repository)
            dst.repository.add(i.copy());
        };
        if (pointer != null) {
          dst.pointer = new ArrayList<Reference>();
          for (Reference i : pointer)
            dst.pointer.add(i.copy());
        };
        if (structureVariant != null) {
          dst.structureVariant = new ArrayList<MolecularSequenceStructureVariantComponent>();
          for (MolecularSequenceStructureVariantComponent i : structureVariant)
            dst.structureVariant.add(i.copy());
        };
        return dst;
      }

      protected MolecularSequence typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof MolecularSequence))
          return false;
        MolecularSequence o = (MolecularSequence) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(coordinateSystem, o.coordinateSystem, true)
           && compareDeep(patient, o.patient, true) && compareDeep(specimen, o.specimen, true) && compareDeep(device, o.device, true)
           && compareDeep(performer, o.performer, true) && compareDeep(quantity, o.quantity, true) && compareDeep(referenceSeq, o.referenceSeq, true)
           && compareDeep(variant, o.variant, true) && compareDeep(observedSeq, o.observedSeq, true) && compareDeep(quality, o.quality, true)
           && compareDeep(readCoverage, o.readCoverage, true) && compareDeep(repository, o.repository, true)
           && compareDeep(pointer, o.pointer, true) && compareDeep(structureVariant, o.structureVariant, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof MolecularSequence))
          return false;
        MolecularSequence o = (MolecularSequence) other_;
        return compareValues(type, o.type, true) && compareValues(coordinateSystem, o.coordinateSystem, true)
           && compareValues(observedSeq, o.observedSeq, true) && compareValues(readCoverage, o.readCoverage, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, coordinateSystem
          , patient, specimen, device, performer, quantity, referenceSeq, variant, observedSeq
          , quality, readCoverage, repository, pointer, structureVariant);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MolecularSequence;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The unique identity for a particular sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MolecularSequence.identifier", description="The unique identity for a particular sequence", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The unique identity for a particular sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>referenceseqid-variant-coordinate</b>
   * <p>
   * Description: <b>Search parameter by reference sequence and variant coordinate. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `referenceSeqId-variant-coordinate=NC_000001.11$lt345$gt123`, this means it will search for the MolecularSequence resource with variants on NC_000001.11 and with position >123 and <345, where in 1-based system resource, all strings within region NC_000001.11:124-344 will be revealed, while in 0-based system resource, all strings within region NC_000001.11:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="referenceseqid-variant-coordinate", path="MolecularSequence.variant", description="Search parameter by reference sequence and variant coordinate. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `referenceSeqId-variant-coordinate=NC_000001.11$lt345$gt123`, this means it will search for the MolecularSequence resource with variants on NC_000001.11 and with position >123 and <345, where in 1-based system resource, all strings within region NC_000001.11:124-344 will be revealed, while in 0-based system resource, all strings within region NC_000001.11:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.", type="composite", compositeOf={"referenceseqid", "variant-start"} )
  public static final String SP_REFERENCESEQID_VARIANT_COORDINATE = "referenceseqid-variant-coordinate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>referenceseqid-variant-coordinate</b>
   * <p>
   * Description: <b>Search parameter by reference sequence and variant coordinate. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `referenceSeqId-variant-coordinate=NC_000001.11$lt345$gt123`, this means it will search for the MolecularSequence resource with variants on NC_000001.11 and with position >123 and <345, where in 1-based system resource, all strings within region NC_000001.11:124-344 will be revealed, while in 0-based system resource, all strings within region NC_000001.11:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam> REFERENCESEQID_VARIANT_COORDINATE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam>(SP_REFERENCESEQID_VARIANT_COORDINATE);

 /**
   * Search parameter: <b>chromosome</b>
   * <p>
   * Description: <b>Chromosome number of the reference sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.referenceSeq.chromosome</b><br>
   * </p>
   */
  @SearchParamDefinition(name="chromosome", path="MolecularSequence.referenceSeq.chromosome", description="Chromosome number of the reference sequence", type="token" )
  public static final String SP_CHROMOSOME = "chromosome";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>chromosome</b>
   * <p>
   * Description: <b>Chromosome number of the reference sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.referenceSeq.chromosome</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CHROMOSOME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CHROMOSOME);

 /**
   * Search parameter: <b>window-end</b>
   * <p>
   * Description: <b>End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.referenceSeq.windowEnd</b><br>
   * </p>
   */
  @SearchParamDefinition(name="window-end", path="MolecularSequence.referenceSeq.windowEnd", description="End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the reference sequence.", type="number" )
  public static final String SP_WINDOW_END = "window-end";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>window-end</b>
   * <p>
   * Description: <b>End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.referenceSeq.windowEnd</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam WINDOW_END = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_WINDOW_END);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Amino Acid Sequence/ DNA Sequence / RNA Sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="MolecularSequence.type", description="Amino Acid Sequence/ DNA Sequence / RNA Sequence", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Amino Acid Sequence/ DNA Sequence / RNA Sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>window-start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.referenceSeq.windowStart</b><br>
   * </p>
   */
  @SearchParamDefinition(name="window-start", path="MolecularSequence.referenceSeq.windowStart", description="Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the reference sequence.", type="number" )
  public static final String SP_WINDOW_START = "window-start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>window-start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.referenceSeq.windowStart</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam WINDOW_START = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_WINDOW_START);

 /**
   * Search parameter: <b>variant-end</b>
   * <p>
   * Description: <b>End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the variant.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.variant.end</b><br>
   * </p>
   */
  @SearchParamDefinition(name="variant-end", path="MolecularSequence.variant.end", description="End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the variant.", type="number" )
  public static final String SP_VARIANT_END = "variant-end";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>variant-end</b>
   * <p>
   * Description: <b>End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the variant.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.variant.end</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam VARIANT_END = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_VARIANT_END);

 /**
   * Search parameter: <b>chromosome-variant-coordinate</b>
   * <p>
   * Description: <b>Search parameter by chromosome and variant coordinate. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `chromosome-variant-coordinate=1$lt345$gt123`, this means it will search for the MolecularSequence resource with variants on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="chromosome-variant-coordinate", path="MolecularSequence.variant", description="Search parameter by chromosome and variant coordinate. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `chromosome-variant-coordinate=1$lt345$gt123`, this means it will search for the MolecularSequence resource with variants on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.", type="composite", compositeOf={"chromosome", "variant-start"} )
  public static final String SP_CHROMOSOME_VARIANT_COORDINATE = "chromosome-variant-coordinate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>chromosome-variant-coordinate</b>
   * <p>
   * Description: <b>Search parameter by chromosome and variant coordinate. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `chromosome-variant-coordinate=1$lt345$gt123`, this means it will search for the MolecularSequence resource with variants on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam> CHROMOSOME_VARIANT_COORDINATE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam>(SP_CHROMOSOME_VARIANT_COORDINATE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The subject that the observation is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MolecularSequence.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MolecularSequence.patient", description="The subject that the observation is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The subject that the observation is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MolecularSequence.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MolecularSequence:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MolecularSequence:patient").toLocked();

 /**
   * Search parameter: <b>variant-start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the variant.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.variant.start</b><br>
   * </p>
   */
  @SearchParamDefinition(name="variant-start", path="MolecularSequence.variant.start", description="Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the variant.", type="number" )
  public static final String SP_VARIANT_START = "variant-start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>variant-start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the variant.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>MolecularSequence.variant.start</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam VARIANT_START = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_VARIANT_START);

 /**
   * Search parameter: <b>chromosome-window-coordinate</b>
   * <p>
   * Description: <b>Search parameter by chromosome and window. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `chromosome-window-coordinate=1$lt345$gt123`, this means it will search for the MolecularSequence resource with a window on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="chromosome-window-coordinate", path="MolecularSequence.referenceSeq", description="Search parameter by chromosome and window. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `chromosome-window-coordinate=1$lt345$gt123`, this means it will search for the MolecularSequence resource with a window on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.", type="composite", compositeOf={"chromosome", "window-start"} )
  public static final String SP_CHROMOSOME_WINDOW_COORDINATE = "chromosome-window-coordinate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>chromosome-window-coordinate</b>
   * <p>
   * Description: <b>Search parameter by chromosome and window. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `chromosome-window-coordinate=1$lt345$gt123`, this means it will search for the MolecularSequence resource with a window on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam> CHROMOSOME_WINDOW_COORDINATE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam>(SP_CHROMOSOME_WINDOW_COORDINATE);

 /**
   * Search parameter: <b>referenceseqid-window-coordinate</b>
   * <p>
   * Description: <b>Search parameter by reference sequence and window. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `referenceSeqId-window-coordinate=NC_000001.11$lt345$gt123`, this means it will search for the MolecularSequence resource with a window on NC_000001.11 and with position >123 and <345, where in 1-based system resource, all strings within region NC_000001.11:124-344 will be revealed, while in 0-based system resource, all strings within region NC_000001.11:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="referenceseqid-window-coordinate", path="MolecularSequence.referenceSeq", description="Search parameter by reference sequence and window. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `referenceSeqId-window-coordinate=NC_000001.11$lt345$gt123`, this means it will search for the MolecularSequence resource with a window on NC_000001.11 and with position >123 and <345, where in 1-based system resource, all strings within region NC_000001.11:124-344 will be revealed, while in 0-based system resource, all strings within region NC_000001.11:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.", type="composite", compositeOf={"referenceseqid", "window-start"} )
  public static final String SP_REFERENCESEQID_WINDOW_COORDINATE = "referenceseqid-window-coordinate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>referenceseqid-window-coordinate</b>
   * <p>
   * Description: <b>Search parameter by reference sequence and window. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `referenceSeqId-window-coordinate=NC_000001.11$lt345$gt123`, this means it will search for the MolecularSequence resource with a window on NC_000001.11 and with position >123 and <345, where in 1-based system resource, all strings within region NC_000001.11:124-344 will be revealed, while in 0-based system resource, all strings within region NC_000001.11:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam> REFERENCESEQID_WINDOW_COORDINATE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam>(SP_REFERENCESEQID_WINDOW_COORDINATE);

 /**
   * Search parameter: <b>referenceseqid</b>
   * <p>
   * Description: <b>Reference Sequence of the sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.referenceSeq.referenceSeqId</b><br>
   * </p>
   */
  @SearchParamDefinition(name="referenceseqid", path="MolecularSequence.referenceSeq.referenceSeqId", description="Reference Sequence of the sequence", type="token" )
  public static final String SP_REFERENCESEQID = "referenceseqid";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>referenceseqid</b>
   * <p>
   * Description: <b>Reference Sequence of the sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MolecularSequence.referenceSeq.referenceSeqId</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REFERENCESEQID = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REFERENCESEQID);


}

