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
@ResourceDef(name="Sequence", profile="http://hl7.org/fhir/Profile/Sequence")
public class Sequence extends DomainResource {

    public enum SequenceType {
        /**
         * Amino acid sequence
         */
        AA, 
        /**
         * DNA Sequence
         */
        DNA, 
        /**
         * RNA Sequence
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
            case AA: return "Amino acid sequence";
            case DNA: return "DNA Sequence";
            case RNA: return "RNA Sequence";
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

    public enum QualityType {
        /**
         * INDEL Comparison
         */
        INDEL, 
        /**
         * SNP Comparison
         */
        SNP, 
        /**
         * UNKNOWN Comparison
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
            case INDEL: return "INDEL Comparison";
            case SNP: return "SNP Comparison";
            case UNKNOWN: return "UNKNOWN Comparison";
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
         * When URL is clicked, the resource can be seen directly (by webpage or by download link format)
         */
        DIRECTLINK, 
        /**
         * When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can be seen directly (usually in JSON or XML format)
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
            case DIRECTLINK: return "When URL is clicked, the resource can be seen directly (by webpage or by download link format)";
            case OPENAPI: return "When the API method (e.g. [base_url]/[parameter]) related with the URL of the website is executed, the resource can be seen directly (usually in JSON or XML format)";
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
    public static class SequenceReferenceSeqComponent extends BackboneElement implements IBaseBackboneElement {
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
         * Reference identifier of reference sequence submitted to NCBI. It must match the type in the Sequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.
         */
        @Child(name = "referenceSeqId", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference identifier", formalDefinition="Reference identifier of reference sequence submitted to NCBI. It must match the type in the Sequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sequence-referenceSeq")
        protected CodeableConcept referenceSeqId;

        /**
         * A Pointer to another Sequence entity as reference sequence.
         */
        @Child(name = "referenceSeqPointer", type = {Sequence.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A Pointer to another Sequence entity as reference sequence", formalDefinition="A Pointer to another Sequence entity as reference sequence." )
        protected Reference referenceSeqPointer;

        /**
         * The actual object that is the target of the reference (A Pointer to another Sequence entity as reference sequence.)
         */
        protected Sequence referenceSeqPointerTarget;

        /**
         * A string like "ACGT".
         */
        @Child(name = "referenceSeqString", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A string to represent reference sequence", formalDefinition="A string like \"ACGT\"." )
        protected StringType referenceSeqString;

        /**
         * Directionality of DNA sequence. Available values are "1" for the plus strand (5' to 3')/Watson/Sense/positive  and "-1" for the minus strand(3' to 5')/Crick/Antisense/negative.
         */
        @Child(name = "strand", type = {IntegerType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Directionality of DNA ( +1/-1)", formalDefinition="Directionality of DNA sequence. Available values are \"1\" for the plus strand (5' to 3')/Watson/Sense/positive  and \"-1\" for the minus strand(3' to 5')/Crick/Antisense/negative." )
        protected IntegerType strand;

        /**
         * Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        @Child(name = "windowStart", type = {IntegerType.class}, order=7, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position of the window on the  reference sequence", formalDefinition="Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive." )
        protected IntegerType windowStart;

        /**
         * End position of the window on the reference sequence. If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "windowEnd", type = {IntegerType.class}, order=8, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position of the window on the reference sequence", formalDefinition="End position of the window on the reference sequence. If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
        protected IntegerType windowEnd;

        private static final long serialVersionUID = -1675617731L;

    /**
     * Constructor
     */
      public SequenceReferenceSeqComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SequenceReferenceSeqComponent(IntegerType windowStart, IntegerType windowEnd) {
        super();
        this.windowStart = windowStart;
        this.windowEnd = windowEnd;
      }

        /**
         * @return {@link #chromosome} (Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340)).)
         */
        public CodeableConcept getChromosome() { 
          if (this.chromosome == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.chromosome");
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
        public SequenceReferenceSeqComponent setChromosome(CodeableConcept value) { 
          this.chromosome = value;
          return this;
        }

        /**
         * @return {@link #genomeBuild} (The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.). This is the underlying object with id, value and extensions. The accessor "getGenomeBuild" gives direct access to the value
         */
        public StringType getGenomeBuildElement() { 
          if (this.genomeBuild == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.genomeBuild");
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
        public SequenceReferenceSeqComponent setGenomeBuildElement(StringType value) { 
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
        public SequenceReferenceSeqComponent setGenomeBuild(String value) { 
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
         * @return {@link #referenceSeqId} (Reference identifier of reference sequence submitted to NCBI. It must match the type in the Sequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.)
         */
        public CodeableConcept getReferenceSeqId() { 
          if (this.referenceSeqId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.referenceSeqId");
            else if (Configuration.doAutoCreate())
              this.referenceSeqId = new CodeableConcept(); // cc
          return this.referenceSeqId;
        }

        public boolean hasReferenceSeqId() { 
          return this.referenceSeqId != null && !this.referenceSeqId.isEmpty();
        }

        /**
         * @param value {@link #referenceSeqId} (Reference identifier of reference sequence submitted to NCBI. It must match the type in the Sequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.)
         */
        public SequenceReferenceSeqComponent setReferenceSeqId(CodeableConcept value) { 
          this.referenceSeqId = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqPointer} (A Pointer to another Sequence entity as reference sequence.)
         */
        public Reference getReferenceSeqPointer() { 
          if (this.referenceSeqPointer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.referenceSeqPointer");
            else if (Configuration.doAutoCreate())
              this.referenceSeqPointer = new Reference(); // cc
          return this.referenceSeqPointer;
        }

        public boolean hasReferenceSeqPointer() { 
          return this.referenceSeqPointer != null && !this.referenceSeqPointer.isEmpty();
        }

        /**
         * @param value {@link #referenceSeqPointer} (A Pointer to another Sequence entity as reference sequence.)
         */
        public SequenceReferenceSeqComponent setReferenceSeqPointer(Reference value) { 
          this.referenceSeqPointer = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqPointer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A Pointer to another Sequence entity as reference sequence.)
         */
        public Sequence getReferenceSeqPointerTarget() { 
          if (this.referenceSeqPointerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.referenceSeqPointer");
            else if (Configuration.doAutoCreate())
              this.referenceSeqPointerTarget = new Sequence(); // aa
          return this.referenceSeqPointerTarget;
        }

        /**
         * @param value {@link #referenceSeqPointer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A Pointer to another Sequence entity as reference sequence.)
         */
        public SequenceReferenceSeqComponent setReferenceSeqPointerTarget(Sequence value) { 
          this.referenceSeqPointerTarget = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqString} (A string like "ACGT".). This is the underlying object with id, value and extensions. The accessor "getReferenceSeqString" gives direct access to the value
         */
        public StringType getReferenceSeqStringElement() { 
          if (this.referenceSeqString == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.referenceSeqString");
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
        public SequenceReferenceSeqComponent setReferenceSeqStringElement(StringType value) { 
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
        public SequenceReferenceSeqComponent setReferenceSeqString(String value) { 
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
         * @return {@link #strand} (Directionality of DNA sequence. Available values are "1" for the plus strand (5' to 3')/Watson/Sense/positive  and "-1" for the minus strand(3' to 5')/Crick/Antisense/negative.). This is the underlying object with id, value and extensions. The accessor "getStrand" gives direct access to the value
         */
        public IntegerType getStrandElement() { 
          if (this.strand == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.strand");
            else if (Configuration.doAutoCreate())
              this.strand = new IntegerType(); // bb
          return this.strand;
        }

        public boolean hasStrandElement() { 
          return this.strand != null && !this.strand.isEmpty();
        }

        public boolean hasStrand() { 
          return this.strand != null && !this.strand.isEmpty();
        }

        /**
         * @param value {@link #strand} (Directionality of DNA sequence. Available values are "1" for the plus strand (5' to 3')/Watson/Sense/positive  and "-1" for the minus strand(3' to 5')/Crick/Antisense/negative.). This is the underlying object with id, value and extensions. The accessor "getStrand" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setStrandElement(IntegerType value) { 
          this.strand = value;
          return this;
        }

        /**
         * @return Directionality of DNA sequence. Available values are "1" for the plus strand (5' to 3')/Watson/Sense/positive  and "-1" for the minus strand(3' to 5')/Crick/Antisense/negative.
         */
        public int getStrand() { 
          return this.strand == null || this.strand.isEmpty() ? 0 : this.strand.getValue();
        }

        /**
         * @param value Directionality of DNA sequence. Available values are "1" for the plus strand (5' to 3')/Watson/Sense/positive  and "-1" for the minus strand(3' to 5')/Crick/Antisense/negative.
         */
        public SequenceReferenceSeqComponent setStrand(int value) { 
            if (this.strand == null)
              this.strand = new IntegerType();
            this.strand.setValue(value);
          return this;
        }

        /**
         * @return {@link #windowStart} (Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getWindowStart" gives direct access to the value
         */
        public IntegerType getWindowStartElement() { 
          if (this.windowStart == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.windowStart");
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
        public SequenceReferenceSeqComponent setWindowStartElement(IntegerType value) { 
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
        public SequenceReferenceSeqComponent setWindowStart(int value) { 
            if (this.windowStart == null)
              this.windowStart = new IntegerType();
            this.windowStart.setValue(value);
          return this;
        }

        /**
         * @return {@link #windowEnd} (End position of the window on the reference sequence. If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
         */
        public IntegerType getWindowEndElement() { 
          if (this.windowEnd == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceReferenceSeqComponent.windowEnd");
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
         * @param value {@link #windowEnd} (End position of the window on the reference sequence. If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setWindowEndElement(IntegerType value) { 
          this.windowEnd = value;
          return this;
        }

        /**
         * @return End position of the window on the reference sequence. If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getWindowEnd() { 
          return this.windowEnd == null || this.windowEnd.isEmpty() ? 0 : this.windowEnd.getValue();
        }

        /**
         * @param value End position of the window on the reference sequence. If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public SequenceReferenceSeqComponent setWindowEnd(int value) { 
            if (this.windowEnd == null)
              this.windowEnd = new IntegerType();
            this.windowEnd.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("chromosome", "CodeableConcept", "Structural unit composed of a nucleic acid molecule which controls its own replication through the interaction of specific proteins at one or more origins of replication ([SO:0000340](http://www.sequenceontology.org/browser/current_svn/term/SO:0000340)).", 0, java.lang.Integer.MAX_VALUE, chromosome));
          childrenList.add(new Property("genomeBuild", "string", "The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.", 0, java.lang.Integer.MAX_VALUE, genomeBuild));
          childrenList.add(new Property("referenceSeqId", "CodeableConcept", "Reference identifier of reference sequence submitted to NCBI. It must match the type in the Sequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences.", 0, java.lang.Integer.MAX_VALUE, referenceSeqId));
          childrenList.add(new Property("referenceSeqPointer", "Reference(Sequence)", "A Pointer to another Sequence entity as reference sequence.", 0, java.lang.Integer.MAX_VALUE, referenceSeqPointer));
          childrenList.add(new Property("referenceSeqString", "string", "A string like \"ACGT\".", 0, java.lang.Integer.MAX_VALUE, referenceSeqString));
          childrenList.add(new Property("strand", "integer", "Directionality of DNA sequence. Available values are \"1\" for the plus strand (5' to 3')/Watson/Sense/positive  and \"-1\" for the minus strand(3' to 5')/Crick/Antisense/negative.", 0, java.lang.Integer.MAX_VALUE, strand));
          childrenList.add(new Property("windowStart", "integer", "Start position of the window on the reference sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, java.lang.Integer.MAX_VALUE, windowStart));
          childrenList.add(new Property("windowEnd", "integer", "End position of the window on the reference sequence. If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, java.lang.Integer.MAX_VALUE, windowEnd));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1499470472: /*chromosome*/ return this.chromosome == null ? new Base[0] : new Base[] {this.chromosome}; // CodeableConcept
        case 1061239735: /*genomeBuild*/ return this.genomeBuild == null ? new Base[0] : new Base[] {this.genomeBuild}; // StringType
        case -1911500465: /*referenceSeqId*/ return this.referenceSeqId == null ? new Base[0] : new Base[] {this.referenceSeqId}; // CodeableConcept
        case 1923414665: /*referenceSeqPointer*/ return this.referenceSeqPointer == null ? new Base[0] : new Base[] {this.referenceSeqPointer}; // Reference
        case -1648301499: /*referenceSeqString*/ return this.referenceSeqString == null ? new Base[0] : new Base[] {this.referenceSeqString}; // StringType
        case -891993594: /*strand*/ return this.strand == null ? new Base[0] : new Base[] {this.strand}; // IntegerType
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
          this.strand = castToInteger(value); // IntegerType
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
        } else if (name.equals("referenceSeqId")) {
          this.referenceSeqId = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("referenceSeqPointer")) {
          this.referenceSeqPointer = castToReference(value); // Reference
        } else if (name.equals("referenceSeqString")) {
          this.referenceSeqString = castToString(value); // StringType
        } else if (name.equals("strand")) {
          this.strand = castToInteger(value); // IntegerType
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
        case -1911500465: /*referenceSeqId*/ return new String[] {"CodeableConcept"};
        case 1923414665: /*referenceSeqPointer*/ return new String[] {"Reference"};
        case -1648301499: /*referenceSeqString*/ return new String[] {"string"};
        case -891993594: /*strand*/ return new String[] {"integer"};
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
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.genomeBuild");
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
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.referenceSeqString");
        }
        else if (name.equals("strand")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.strand");
        }
        else if (name.equals("windowStart")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.windowStart");
        }
        else if (name.equals("windowEnd")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.windowEnd");
        }
        else
          return super.addChild(name);
      }

      public SequenceReferenceSeqComponent copy() {
        SequenceReferenceSeqComponent dst = new SequenceReferenceSeqComponent();
        copyValues(dst);
        dst.chromosome = chromosome == null ? null : chromosome.copy();
        dst.genomeBuild = genomeBuild == null ? null : genomeBuild.copy();
        dst.referenceSeqId = referenceSeqId == null ? null : referenceSeqId.copy();
        dst.referenceSeqPointer = referenceSeqPointer == null ? null : referenceSeqPointer.copy();
        dst.referenceSeqString = referenceSeqString == null ? null : referenceSeqString.copy();
        dst.strand = strand == null ? null : strand.copy();
        dst.windowStart = windowStart == null ? null : windowStart.copy();
        dst.windowEnd = windowEnd == null ? null : windowEnd.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceReferenceSeqComponent))
          return false;
        SequenceReferenceSeqComponent o = (SequenceReferenceSeqComponent) other;
        return compareDeep(chromosome, o.chromosome, true) && compareDeep(genomeBuild, o.genomeBuild, true)
           && compareDeep(referenceSeqId, o.referenceSeqId, true) && compareDeep(referenceSeqPointer, o.referenceSeqPointer, true)
           && compareDeep(referenceSeqString, o.referenceSeqString, true) && compareDeep(strand, o.strand, true)
           && compareDeep(windowStart, o.windowStart, true) && compareDeep(windowEnd, o.windowEnd, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceReferenceSeqComponent))
          return false;
        SequenceReferenceSeqComponent o = (SequenceReferenceSeqComponent) other;
        return compareValues(genomeBuild, o.genomeBuild, true) && compareValues(referenceSeqString, o.referenceSeqString, true)
           && compareValues(strand, o.strand, true) && compareValues(windowStart, o.windowStart, true) && compareValues(windowEnd, o.windowEnd, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(chromosome, genomeBuild, referenceSeqId
          , referenceSeqPointer, referenceSeqString, strand, windowStart, windowEnd);
      }

  public String fhirType() {
    return "Sequence.referenceSeq";

  }

  }

    @Block()
    public static class SequenceVariantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Start position of the variant on the  reference sequence.If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position of the variant on the  reference sequence", formalDefinition="Start position of the variant on the  reference sequence.If the coordinate system is either 0-based or 1-based, then start position is inclusive." )
        protected IntegerType start;

        /**
         * End position of the variant on the reference sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position of the variant on the reference sequence", formalDefinition="End position of the variant on the reference sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
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
      public SequenceVariantComponent() {
        super();
      }

        /**
         * @return {@link #start} (Start position of the variant on the  reference sequence.If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceVariantComponent.start");
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
         * @param value {@link #start} (Start position of the variant on the  reference sequence.If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceVariantComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Start position of the variant on the  reference sequence.If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Start position of the variant on the  reference sequence.If the coordinate system is either 0-based or 1-based, then start position is inclusive.
         */
        public SequenceVariantComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (End position of the variant on the reference sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceVariantComponent.end");
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
         * @param value {@link #end} (End position of the variant on the reference sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceVariantComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return End position of the variant on the reference sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value End position of the variant on the reference sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public SequenceVariantComponent setEnd(int value) { 
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
              throw new Error("Attempt to auto-create SequenceVariantComponent.observedAllele");
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
        public SequenceVariantComponent setObservedAlleleElement(StringType value) { 
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
        public SequenceVariantComponent setObservedAllele(String value) { 
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
              throw new Error("Attempt to auto-create SequenceVariantComponent.referenceAllele");
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
        public SequenceVariantComponent setReferenceAlleleElement(StringType value) { 
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
        public SequenceVariantComponent setReferenceAllele(String value) { 
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
              throw new Error("Attempt to auto-create SequenceVariantComponent.cigar");
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
        public SequenceVariantComponent setCigarElement(StringType value) { 
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
        public SequenceVariantComponent setCigar(String value) { 
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
              throw new Error("Attempt to auto-create SequenceVariantComponent.variantPointer");
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
        public SequenceVariantComponent setVariantPointer(Reference value) { 
          this.variantPointer = value;
          return this;
        }

        /**
         * @return {@link #variantPointer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A pointer to an Observation containing variant information.)
         */
        public Observation getVariantPointerTarget() { 
          if (this.variantPointerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceVariantComponent.variantPointer");
            else if (Configuration.doAutoCreate())
              this.variantPointerTarget = new Observation(); // aa
          return this.variantPointerTarget;
        }

        /**
         * @param value {@link #variantPointer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A pointer to an Observation containing variant information.)
         */
        public SequenceVariantComponent setVariantPointerTarget(Observation value) { 
          this.variantPointerTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("start", "integer", "Start position of the variant on the  reference sequence.If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "End position of the variant on the reference sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("observedAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)).  Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed  sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.", 0, java.lang.Integer.MAX_VALUE, observedAllele));
          childrenList.add(new Property("referenceAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand. This will lay in the range between variant.start and variant.end.", 0, java.lang.Integer.MAX_VALUE, referenceAllele));
          childrenList.add(new Property("cigar", "string", "Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).", 0, java.lang.Integer.MAX_VALUE, cigar));
          childrenList.add(new Property("variantPointer", "Reference(Observation)", "A pointer to an Observation containing variant information.", 0, java.lang.Integer.MAX_VALUE, variantPointer));
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
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.end");
        }
        else if (name.equals("observedAllele")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.observedAllele");
        }
        else if (name.equals("referenceAllele")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.referenceAllele");
        }
        else if (name.equals("cigar")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.cigar");
        }
        else if (name.equals("variantPointer")) {
          this.variantPointer = new Reference();
          return this.variantPointer;
        }
        else
          return super.addChild(name);
      }

      public SequenceVariantComponent copy() {
        SequenceVariantComponent dst = new SequenceVariantComponent();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceVariantComponent))
          return false;
        SequenceVariantComponent o = (SequenceVariantComponent) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true) && compareDeep(observedAllele, o.observedAllele, true)
           && compareDeep(referenceAllele, o.referenceAllele, true) && compareDeep(cigar, o.cigar, true) && compareDeep(variantPointer, o.variantPointer, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceVariantComponent))
          return false;
        SequenceVariantComponent o = (SequenceVariantComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(observedAllele, o.observedAllele, true)
           && compareValues(referenceAllele, o.referenceAllele, true) && compareValues(cigar, o.cigar, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(start, end, observedAllele
          , referenceAllele, cigar, variantPointer);
      }

  public String fhirType() {
    return "Sequence.variant";

  }

  }

    @Block()
    public static class SequenceQualityComponent extends BackboneElement implements IBaseBackboneElement {
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
         * End position of the sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        @Child(name = "end", type = {IntegerType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position of the sequence", formalDefinition="End position of the sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position." )
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

        private static final long serialVersionUID = -383644463L;

    /**
     * Constructor
     */
      public SequenceQualityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SequenceQualityComponent(Enumeration<QualityType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (INDEL / SNP / Undefined variant.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<QualityType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceQualityComponent.type");
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
        public SequenceQualityComponent setTypeElement(Enumeration<QualityType> value) { 
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
        public SequenceQualityComponent setType(QualityType value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.standardSequence");
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
        public SequenceQualityComponent setStandardSequence(CodeableConcept value) { 
          this.standardSequence = value;
          return this;
        }

        /**
         * @return {@link #start} (Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceQualityComponent.start");
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
        public SequenceQualityComponent setStartElement(IntegerType value) { 
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
        public SequenceQualityComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (End position of the sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceQualityComponent.end");
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
         * @param value {@link #end} (End position of the sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceQualityComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return End position of the sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value End position of the sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.
         */
        public SequenceQualityComponent setEnd(int value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.score");
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
        public SequenceQualityComponent setScore(Quantity value) { 
          this.score = value;
          return this;
        }

        /**
         * @return {@link #method} (Which method is used to get sequence quality.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceQualityComponent.method");
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
        public SequenceQualityComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #truthTP} (True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.). This is the underlying object with id, value and extensions. The accessor "getTruthTP" gives direct access to the value
         */
        public DecimalType getTruthTPElement() { 
          if (this.truthTP == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceQualityComponent.truthTP");
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
        public SequenceQualityComponent setTruthTPElement(DecimalType value) { 
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
        public SequenceQualityComponent setTruthTP(BigDecimal value) { 
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
        public SequenceQualityComponent setTruthTP(long value) { 
              this.truthTP = new DecimalType();
            this.truthTP.setValue(value);
          return this;
        }

        /**
         * @param value True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public SequenceQualityComponent setTruthTP(double value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.queryTP");
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
        public SequenceQualityComponent setQueryTPElement(DecimalType value) { 
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
        public SequenceQualityComponent setQueryTP(BigDecimal value) { 
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
        public SequenceQualityComponent setQueryTP(long value) { 
              this.queryTP = new DecimalType();
            this.queryTP.setValue(value);
          return this;
        }

        /**
         * @param value True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        public SequenceQualityComponent setQueryTP(double value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.truthFN");
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
        public SequenceQualityComponent setTruthFNElement(DecimalType value) { 
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
        public SequenceQualityComponent setTruthFN(BigDecimal value) { 
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
        public SequenceQualityComponent setTruthFN(long value) { 
              this.truthFN = new DecimalType();
            this.truthFN.setValue(value);
          return this;
        }

        /**
         * @param value False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.
         */
        public SequenceQualityComponent setTruthFN(double value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.queryFP");
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
        public SequenceQualityComponent setQueryFPElement(DecimalType value) { 
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
        public SequenceQualityComponent setQueryFP(BigDecimal value) { 
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
        public SequenceQualityComponent setQueryFP(long value) { 
              this.queryFP = new DecimalType();
            this.queryFP.setValue(value);
          return this;
        }

        /**
         * @param value False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         */
        public SequenceQualityComponent setQueryFP(double value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.gtFP");
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
        public SequenceQualityComponent setGtFPElement(DecimalType value) { 
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
        public SequenceQualityComponent setGtFP(BigDecimal value) { 
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
        public SequenceQualityComponent setGtFP(long value) { 
              this.gtFP = new DecimalType();
            this.gtFP.setValue(value);
          return this;
        }

        /**
         * @param value The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).
         */
        public SequenceQualityComponent setGtFP(double value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.precision");
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
        public SequenceQualityComponent setPrecisionElement(DecimalType value) { 
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
        public SequenceQualityComponent setPrecision(BigDecimal value) { 
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
        public SequenceQualityComponent setPrecision(long value) { 
              this.precision = new DecimalType();
            this.precision.setValue(value);
          return this;
        }

        /**
         * @param value QUERY.TP / (QUERY.TP + QUERY.FP).
         */
        public SequenceQualityComponent setPrecision(double value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.recall");
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
        public SequenceQualityComponent setRecallElement(DecimalType value) { 
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
        public SequenceQualityComponent setRecall(BigDecimal value) { 
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
        public SequenceQualityComponent setRecall(long value) { 
              this.recall = new DecimalType();
            this.recall.setValue(value);
          return this;
        }

        /**
         * @param value TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         */
        public SequenceQualityComponent setRecall(double value) { 
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
              throw new Error("Attempt to auto-create SequenceQualityComponent.fScore");
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
        public SequenceQualityComponent setFScoreElement(DecimalType value) { 
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
        public SequenceQualityComponent setFScore(BigDecimal value) { 
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
        public SequenceQualityComponent setFScore(long value) { 
              this.fScore = new DecimalType();
            this.fScore.setValue(value);
          return this;
        }

        /**
         * @param value Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         */
        public SequenceQualityComponent setFScore(double value) { 
              this.fScore = new DecimalType();
            this.fScore.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "INDEL / SNP / Undefined variant.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("standardSequence", "CodeableConcept", "Gold standard sequence used for comparing against.", 0, java.lang.Integer.MAX_VALUE, standardSequence));
          childrenList.add(new Property("start", "integer", "Start position of the sequence. If the coordinate system is either 0-based or 1-based, then start position is inclusive.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "End position of the sequence.If the coordinate system is 0-based then end is is exclusive and does not include the last position. If the coordinate system is 1-base, then end is inclusive and includes the last position.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("score", "Quantity", "The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).", 0, java.lang.Integer.MAX_VALUE, score));
          childrenList.add(new Property("method", "CodeableConcept", "Which method is used to get sequence quality.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("truthTP", "decimal", "True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.", 0, java.lang.Integer.MAX_VALUE, truthTP));
          childrenList.add(new Property("queryTP", "decimal", "True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.", 0, java.lang.Integer.MAX_VALUE, queryTP));
          childrenList.add(new Property("truthFN", "decimal", "False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.", 0, java.lang.Integer.MAX_VALUE, truthFN));
          childrenList.add(new Property("queryFP", "decimal", "False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.", 0, java.lang.Integer.MAX_VALUE, queryFP));
          childrenList.add(new Property("gtFP", "decimal", "The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).", 0, java.lang.Integer.MAX_VALUE, gtFP));
          childrenList.add(new Property("precision", "decimal", "QUERY.TP / (QUERY.TP + QUERY.FP).", 0, java.lang.Integer.MAX_VALUE, precision));
          childrenList.add(new Property("recall", "decimal", "TRUTH.TP / (TRUTH.TP + TRUTH.FN).", 0, java.lang.Integer.MAX_VALUE, recall));
          childrenList.add(new Property("fScore", "decimal", "Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).", 0, java.lang.Integer.MAX_VALUE, fScore));
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
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.type");
        }
        else if (name.equals("standardSequence")) {
          this.standardSequence = new CodeableConcept();
          return this.standardSequence;
        }
        else if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.end");
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
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.truthTP");
        }
        else if (name.equals("queryTP")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.queryTP");
        }
        else if (name.equals("truthFN")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.truthFN");
        }
        else if (name.equals("queryFP")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.queryFP");
        }
        else if (name.equals("gtFP")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.gtFP");
        }
        else if (name.equals("precision")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.precision");
        }
        else if (name.equals("recall")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.recall");
        }
        else if (name.equals("fScore")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.fScore");
        }
        else
          return super.addChild(name);
      }

      public SequenceQualityComponent copy() {
        SequenceQualityComponent dst = new SequenceQualityComponent();
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
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceQualityComponent))
          return false;
        SequenceQualityComponent o = (SequenceQualityComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(standardSequence, o.standardSequence, true)
           && compareDeep(start, o.start, true) && compareDeep(end, o.end, true) && compareDeep(score, o.score, true)
           && compareDeep(method, o.method, true) && compareDeep(truthTP, o.truthTP, true) && compareDeep(queryTP, o.queryTP, true)
           && compareDeep(truthFN, o.truthFN, true) && compareDeep(queryFP, o.queryFP, true) && compareDeep(gtFP, o.gtFP, true)
           && compareDeep(precision, o.precision, true) && compareDeep(recall, o.recall, true) && compareDeep(fScore, o.fScore, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceQualityComponent))
          return false;
        SequenceQualityComponent o = (SequenceQualityComponent) other;
        return compareValues(type, o.type, true) && compareValues(start, o.start, true) && compareValues(end, o.end, true)
           && compareValues(truthTP, o.truthTP, true) && compareValues(queryTP, o.queryTP, true) && compareValues(truthFN, o.truthFN, true)
           && compareValues(queryFP, o.queryFP, true) && compareValues(gtFP, o.gtFP, true) && compareValues(precision, o.precision, true)
           && compareValues(recall, o.recall, true) && compareValues(fScore, o.fScore, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, standardSequence, start
          , end, score, method, truthTP, queryTP, truthFN, queryFP, gtFP, precision
          , recall, fScore);
      }

  public String fhirType() {
    return "Sequence.quality";

  }

  }

    @Block()
    public static class SequenceRepositoryComponent extends BackboneElement implements IBaseBackboneElement {
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
      public SequenceRepositoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SequenceRepositoryComponent(Enumeration<RepositoryType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<RepositoryType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.type");
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
        public SequenceRepositoryComponent setTypeElement(Enumeration<RepositoryType> value) { 
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
        public SequenceRepositoryComponent setType(RepositoryType value) { 
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
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.url");
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
        public SequenceRepositoryComponent setUrlElement(UriType value) { 
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
        public SequenceRepositoryComponent setUrl(String value) { 
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
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.name");
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
        public SequenceRepositoryComponent setNameElement(StringType value) { 
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
        public SequenceRepositoryComponent setName(String value) { 
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
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.datasetId");
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
        public SequenceRepositoryComponent setDatasetIdElement(StringType value) { 
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
        public SequenceRepositoryComponent setDatasetId(String value) { 
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
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.variantsetId");
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
        public SequenceRepositoryComponent setVariantsetIdElement(StringType value) { 
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
        public SequenceRepositoryComponent setVariantsetId(String value) { 
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
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.readsetId");
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
        public SequenceRepositoryComponent setReadsetIdElement(StringType value) { 
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
        public SequenceRepositoryComponent setReadsetId(String value) { 
          if (Utilities.noString(value))
            this.readsetId = null;
          else {
            if (this.readsetId == null)
              this.readsetId = new StringType();
            this.readsetId.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Click and see / RESTful API / Need login to see / RESTful API with authentication / Other ways to see resource.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("url", "uri", "URI of an external repository which contains further details about the genetics data.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("name", "string", "URI of an external repository which contains further details about the genetics data.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("datasetId", "string", "Id of the variant in this external repository. The server will understand how to use this id to call for more info about datasets in external repository.", 0, java.lang.Integer.MAX_VALUE, datasetId));
          childrenList.add(new Property("variantsetId", "string", "Id of the variantset in this external repository. The server will understand how to use this id to call for more info about variantsets in external repository.", 0, java.lang.Integer.MAX_VALUE, variantsetId));
          childrenList.add(new Property("readsetId", "string", "Id of the read in this external repository.", 0, java.lang.Integer.MAX_VALUE, readsetId));
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
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.type");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.url");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.name");
        }
        else if (name.equals("datasetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.datasetId");
        }
        else if (name.equals("variantsetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.variantsetId");
        }
        else if (name.equals("readsetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.readsetId");
        }
        else
          return super.addChild(name);
      }

      public SequenceRepositoryComponent copy() {
        SequenceRepositoryComponent dst = new SequenceRepositoryComponent();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceRepositoryComponent))
          return false;
        SequenceRepositoryComponent o = (SequenceRepositoryComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(url, o.url, true) && compareDeep(name, o.name, true)
           && compareDeep(datasetId, o.datasetId, true) && compareDeep(variantsetId, o.variantsetId, true)
           && compareDeep(readsetId, o.readsetId, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceRepositoryComponent))
          return false;
        SequenceRepositoryComponent o = (SequenceRepositoryComponent) other;
        return compareValues(type, o.type, true) && compareValues(url, o.url, true) && compareValues(name, o.name, true)
           && compareValues(datasetId, o.datasetId, true) && compareValues(variantsetId, o.variantsetId, true)
           && compareValues(readsetId, o.readsetId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, url, name, datasetId
          , variantsetId, readsetId);
      }

  public String fhirType() {
    return "Sequence.repository";

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
     * The number of copies of the seqeunce of interest. (RNASeq).
     */
    @Child(name = "quantity", type = {Quantity.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The number of copies of the seqeunce of interest.  (RNASeq)", formalDefinition="The number of copies of the seqeunce of interest. (RNASeq)." )
    protected Quantity quantity;

    /**
     * A sequence that is used as a reference to describe variants that are present in a sequence analyzed.
     */
    @Child(name = "referenceSeq", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A sequence used as reference", formalDefinition="A sequence that is used as a reference to describe variants that are present in a sequence analyzed." )
    protected SequenceReferenceSeqComponent referenceSeq;

    /**
     * The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string.
     */
    @Child(name = "variant", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Variant in sequence", formalDefinition="The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string." )
    protected List<SequenceVariantComponent> variant;

    /**
     * Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall starts from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     */
    @Child(name = "observedSeq", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Sequence that was observed", formalDefinition="Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall starts from referenceSeq.windowStart and end by referenceSeq.windowEnd." )
    protected StringType observedSeq;

    /**
     * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
     */
    @Child(name = "quality", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An set of value as quality of sequence", formalDefinition="An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686))." )
    protected List<SequenceQualityComponent> quality;

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
    protected List<SequenceRepositoryComponent> repository;

    /**
     * Pointer to next atomic sequence which at most contains one variant.
     */
    @Child(name = "pointer", type = {Sequence.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Pointer to next atomic sequence", formalDefinition="Pointer to next atomic sequence which at most contains one variant." )
    protected List<Reference> pointer;
    /**
     * The actual objects that are the target of the reference (Pointer to next atomic sequence which at most contains one variant.)
     */
    protected List<Sequence> pointerTarget;


    private static final long serialVersionUID = -2101352712L;

  /**
   * Constructor
   */
    public Sequence() {
      super();
    }

  /**
   * Constructor
   */
    public Sequence(IntegerType coordinateSystem) {
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
    public Sequence setIdentifier(List<Identifier> theIdentifier) { 
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

    public Sequence addIdentifier(Identifier t) { //3
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
          throw new Error("Attempt to auto-create Sequence.type");
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
    public Sequence setTypeElement(Enumeration<SequenceType> value) { 
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
    public Sequence setType(SequenceType value) { 
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
          throw new Error("Attempt to auto-create Sequence.coordinateSystem");
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
    public Sequence setCoordinateSystemElement(IntegerType value) { 
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
    public Sequence setCoordinateSystem(int value) { 
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
          throw new Error("Attempt to auto-create Sequence.patient");
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
    public Sequence setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient whose sequencing results are described by this resource.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient whose sequencing results are described by this resource.)
     */
    public Sequence setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #specimen} (Specimen used for sequencing.)
     */
    public Reference getSpecimen() { 
      if (this.specimen == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.specimen");
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
    public Sequence setSpecimen(Reference value) { 
      this.specimen = value;
      return this;
    }

    /**
     * @return {@link #specimen} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Specimen used for sequencing.)
     */
    public Specimen getSpecimenTarget() { 
      if (this.specimenTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.specimen");
        else if (Configuration.doAutoCreate())
          this.specimenTarget = new Specimen(); // aa
      return this.specimenTarget;
    }

    /**
     * @param value {@link #specimen} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Specimen used for sequencing.)
     */
    public Sequence setSpecimenTarget(Specimen value) { 
      this.specimenTarget = value;
      return this;
    }

    /**
     * @return {@link #device} (The method for sequencing, for example, chip information.)
     */
    public Reference getDevice() { 
      if (this.device == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.device");
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
    public Sequence setDevice(Reference value) { 
      this.device = value;
      return this;
    }

    /**
     * @return {@link #device} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The method for sequencing, for example, chip information.)
     */
    public Device getDeviceTarget() { 
      if (this.deviceTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.device");
        else if (Configuration.doAutoCreate())
          this.deviceTarget = new Device(); // aa
      return this.deviceTarget;
    }

    /**
     * @param value {@link #device} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The method for sequencing, for example, chip information.)
     */
    public Sequence setDeviceTarget(Device value) { 
      this.deviceTarget = value;
      return this;
    }

    /**
     * @return {@link #performer} (The organization or lab that should be responsible for this result.)
     */
    public Reference getPerformer() { 
      if (this.performer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.performer");
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
    public Sequence setPerformer(Reference value) { 
      this.performer = value;
      return this;
    }

    /**
     * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization or lab that should be responsible for this result.)
     */
    public Organization getPerformerTarget() { 
      if (this.performerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.performer");
        else if (Configuration.doAutoCreate())
          this.performerTarget = new Organization(); // aa
      return this.performerTarget;
    }

    /**
     * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization or lab that should be responsible for this result.)
     */
    public Sequence setPerformerTarget(Organization value) { 
      this.performerTarget = value;
      return this;
    }

    /**
     * @return {@link #quantity} (The number of copies of the seqeunce of interest. (RNASeq).)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (The number of copies of the seqeunce of interest. (RNASeq).)
     */
    public Sequence setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #referenceSeq} (A sequence that is used as a reference to describe variants that are present in a sequence analyzed.)
     */
    public SequenceReferenceSeqComponent getReferenceSeq() { 
      if (this.referenceSeq == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.referenceSeq");
        else if (Configuration.doAutoCreate())
          this.referenceSeq = new SequenceReferenceSeqComponent(); // cc
      return this.referenceSeq;
    }

    public boolean hasReferenceSeq() { 
      return this.referenceSeq != null && !this.referenceSeq.isEmpty();
    }

    /**
     * @param value {@link #referenceSeq} (A sequence that is used as a reference to describe variants that are present in a sequence analyzed.)
     */
    public Sequence setReferenceSeq(SequenceReferenceSeqComponent value) { 
      this.referenceSeq = value;
      return this;
    }

    /**
     * @return {@link #variant} (The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string.)
     */
    public List<SequenceVariantComponent> getVariant() { 
      if (this.variant == null)
        this.variant = new ArrayList<SequenceVariantComponent>();
      return this.variant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Sequence setVariant(List<SequenceVariantComponent> theVariant) { 
      this.variant = theVariant;
      return this;
    }

    public boolean hasVariant() { 
      if (this.variant == null)
        return false;
      for (SequenceVariantComponent item : this.variant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SequenceVariantComponent addVariant() { //3
      SequenceVariantComponent t = new SequenceVariantComponent();
      if (this.variant == null)
        this.variant = new ArrayList<SequenceVariantComponent>();
      this.variant.add(t);
      return t;
    }

    public Sequence addVariant(SequenceVariantComponent t) { //3
      if (t == null)
        return this;
      if (this.variant == null)
        this.variant = new ArrayList<SequenceVariantComponent>();
      this.variant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #variant}, creating it if it does not already exist
     */
    public SequenceVariantComponent getVariantFirstRep() { 
      if (getVariant().isEmpty()) {
        addVariant();
      }
      return getVariant().get(0);
    }

    /**
     * @return {@link #observedSeq} (Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall starts from referenceSeq.windowStart and end by referenceSeq.windowEnd.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
     */
    public StringType getObservedSeqElement() { 
      if (this.observedSeq == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.observedSeq");
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
     * @param value {@link #observedSeq} (Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall starts from referenceSeq.windowStart and end by referenceSeq.windowEnd.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
     */
    public Sequence setObservedSeqElement(StringType value) { 
      this.observedSeq = value;
      return this;
    }

    /**
     * @return Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall starts from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     */
    public String getObservedSeq() { 
      return this.observedSeq == null ? null : this.observedSeq.getValue();
    }

    /**
     * @param value Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall starts from referenceSeq.windowStart and end by referenceSeq.windowEnd.
     */
    public Sequence setObservedSeq(String value) { 
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
    public List<SequenceQualityComponent> getQuality() { 
      if (this.quality == null)
        this.quality = new ArrayList<SequenceQualityComponent>();
      return this.quality;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Sequence setQuality(List<SequenceQualityComponent> theQuality) { 
      this.quality = theQuality;
      return this;
    }

    public boolean hasQuality() { 
      if (this.quality == null)
        return false;
      for (SequenceQualityComponent item : this.quality)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SequenceQualityComponent addQuality() { //3
      SequenceQualityComponent t = new SequenceQualityComponent();
      if (this.quality == null)
        this.quality = new ArrayList<SequenceQualityComponent>();
      this.quality.add(t);
      return t;
    }

    public Sequence addQuality(SequenceQualityComponent t) { //3
      if (t == null)
        return this;
      if (this.quality == null)
        this.quality = new ArrayList<SequenceQualityComponent>();
      this.quality.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #quality}, creating it if it does not already exist
     */
    public SequenceQualityComponent getQualityFirstRep() { 
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
          throw new Error("Attempt to auto-create Sequence.readCoverage");
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
    public Sequence setReadCoverageElement(IntegerType value) { 
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
    public Sequence setReadCoverage(int value) { 
        if (this.readCoverage == null)
          this.readCoverage = new IntegerType();
        this.readCoverage.setValue(value);
      return this;
    }

    /**
     * @return {@link #repository} (Configurations of the external repository. The repository shall store target's observedSeq or records related with target's observedSeq.)
     */
    public List<SequenceRepositoryComponent> getRepository() { 
      if (this.repository == null)
        this.repository = new ArrayList<SequenceRepositoryComponent>();
      return this.repository;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Sequence setRepository(List<SequenceRepositoryComponent> theRepository) { 
      this.repository = theRepository;
      return this;
    }

    public boolean hasRepository() { 
      if (this.repository == null)
        return false;
      for (SequenceRepositoryComponent item : this.repository)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SequenceRepositoryComponent addRepository() { //3
      SequenceRepositoryComponent t = new SequenceRepositoryComponent();
      if (this.repository == null)
        this.repository = new ArrayList<SequenceRepositoryComponent>();
      this.repository.add(t);
      return t;
    }

    public Sequence addRepository(SequenceRepositoryComponent t) { //3
      if (t == null)
        return this;
      if (this.repository == null)
        this.repository = new ArrayList<SequenceRepositoryComponent>();
      this.repository.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #repository}, creating it if it does not already exist
     */
    public SequenceRepositoryComponent getRepositoryFirstRep() { 
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
    public Sequence setPointer(List<Reference> thePointer) { 
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

    public Sequence addPointer(Reference t) { //3
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
    public List<Sequence> getPointerTarget() { 
      if (this.pointerTarget == null)
        this.pointerTarget = new ArrayList<Sequence>();
      return this.pointerTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Sequence addPointerTarget() { 
      Sequence r = new Sequence();
      if (this.pointerTarget == null)
        this.pointerTarget = new ArrayList<Sequence>();
      this.pointerTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier for this particular sequence instance. This is a FHIR-defined id.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "code", "Amino Acid Sequence/ DNA Sequence / RNA Sequence.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("coordinateSystem", "integer", "Whether the sequence is numbered starting at 0 (0-based numbering or coordinates, inclusive start, exclusive end) or starting at 1 (1-based numbering, inclusive start and inclusive end).", 0, java.lang.Integer.MAX_VALUE, coordinateSystem));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient whose sequencing results are described by this resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "Specimen used for sequencing.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("device", "Reference(Device)", "The method for sequencing, for example, chip information.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("performer", "Reference(Organization)", "The organization or lab that should be responsible for this result.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("quantity", "Quantity", "The number of copies of the seqeunce of interest. (RNASeq).", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("referenceSeq", "", "A sequence that is used as a reference to describe variants that are present in a sequence analyzed.", 0, java.lang.Integer.MAX_VALUE, referenceSeq));
        childrenList.add(new Property("variant", "", "The definition of variant here originates from Sequence ontology ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)). This element can represent amino acid or nucleic sequence change(including insertion,deletion,SNP,etc.)  It can represent some complex mutation or segment variation with the assist of CIGAR string.", 0, java.lang.Integer.MAX_VALUE, variant));
        childrenList.add(new Property("observedSeq", "string", "Sequence that was observed. It is the result marked by referenceSeq along with variant records on referenceSeq. This shall starts from referenceSeq.windowStart and end by referenceSeq.windowEnd.", 0, java.lang.Integer.MAX_VALUE, observedSeq));
        childrenList.add(new Property("quality", "", "An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).", 0, java.lang.Integer.MAX_VALUE, quality));
        childrenList.add(new Property("readCoverage", "integer", "Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.", 0, java.lang.Integer.MAX_VALUE, readCoverage));
        childrenList.add(new Property("repository", "", "Configurations of the external repository. The repository shall store target's observedSeq or records related with target's observedSeq.", 0, java.lang.Integer.MAX_VALUE, repository));
        childrenList.add(new Property("pointer", "Reference(Sequence)", "Pointer to next atomic sequence which at most contains one variant.", 0, java.lang.Integer.MAX_VALUE, pointer));
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
        case -502547180: /*referenceSeq*/ return this.referenceSeq == null ? new Base[0] : new Base[] {this.referenceSeq}; // SequenceReferenceSeqComponent
        case 236785797: /*variant*/ return this.variant == null ? new Base[0] : this.variant.toArray(new Base[this.variant.size()]); // SequenceVariantComponent
        case 125541495: /*observedSeq*/ return this.observedSeq == null ? new Base[0] : new Base[] {this.observedSeq}; // StringType
        case 651215103: /*quality*/ return this.quality == null ? new Base[0] : this.quality.toArray(new Base[this.quality.size()]); // SequenceQualityComponent
        case -1798816354: /*readCoverage*/ return this.readCoverage == null ? new Base[0] : new Base[] {this.readCoverage}; // IntegerType
        case 1950800714: /*repository*/ return this.repository == null ? new Base[0] : this.repository.toArray(new Base[this.repository.size()]); // SequenceRepositoryComponent
        case -400605635: /*pointer*/ return this.pointer == null ? new Base[0] : this.pointer.toArray(new Base[this.pointer.size()]); // Reference
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
          this.referenceSeq = (SequenceReferenceSeqComponent) value; // SequenceReferenceSeqComponent
          return value;
        case 236785797: // variant
          this.getVariant().add((SequenceVariantComponent) value); // SequenceVariantComponent
          return value;
        case 125541495: // observedSeq
          this.observedSeq = castToString(value); // StringType
          return value;
        case 651215103: // quality
          this.getQuality().add((SequenceQualityComponent) value); // SequenceQualityComponent
          return value;
        case -1798816354: // readCoverage
          this.readCoverage = castToInteger(value); // IntegerType
          return value;
        case 1950800714: // repository
          this.getRepository().add((SequenceRepositoryComponent) value); // SequenceRepositoryComponent
          return value;
        case -400605635: // pointer
          this.getPointer().add(castToReference(value)); // Reference
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
          this.referenceSeq = (SequenceReferenceSeqComponent) value; // SequenceReferenceSeqComponent
        } else if (name.equals("variant")) {
          this.getVariant().add((SequenceVariantComponent) value);
        } else if (name.equals("observedSeq")) {
          this.observedSeq = castToString(value); // StringType
        } else if (name.equals("quality")) {
          this.getQuality().add((SequenceQualityComponent) value);
        } else if (name.equals("readCoverage")) {
          this.readCoverage = castToInteger(value); // IntegerType
        } else if (name.equals("repository")) {
          this.getRepository().add((SequenceRepositoryComponent) value);
        } else if (name.equals("pointer")) {
          this.getPointer().add(castToReference(value));
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
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.type");
        }
        else if (name.equals("coordinateSystem")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.coordinateSystem");
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
          this.referenceSeq = new SequenceReferenceSeqComponent();
          return this.referenceSeq;
        }
        else if (name.equals("variant")) {
          return addVariant();
        }
        else if (name.equals("observedSeq")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.observedSeq");
        }
        else if (name.equals("quality")) {
          return addQuality();
        }
        else if (name.equals("readCoverage")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.readCoverage");
        }
        else if (name.equals("repository")) {
          return addRepository();
        }
        else if (name.equals("pointer")) {
          return addPointer();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Sequence";

  }

      public Sequence copy() {
        Sequence dst = new Sequence();
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
          dst.variant = new ArrayList<SequenceVariantComponent>();
          for (SequenceVariantComponent i : variant)
            dst.variant.add(i.copy());
        };
        dst.observedSeq = observedSeq == null ? null : observedSeq.copy();
        if (quality != null) {
          dst.quality = new ArrayList<SequenceQualityComponent>();
          for (SequenceQualityComponent i : quality)
            dst.quality.add(i.copy());
        };
        dst.readCoverage = readCoverage == null ? null : readCoverage.copy();
        if (repository != null) {
          dst.repository = new ArrayList<SequenceRepositoryComponent>();
          for (SequenceRepositoryComponent i : repository)
            dst.repository.add(i.copy());
        };
        if (pointer != null) {
          dst.pointer = new ArrayList<Reference>();
          for (Reference i : pointer)
            dst.pointer.add(i.copy());
        };
        return dst;
      }

      protected Sequence typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Sequence))
          return false;
        Sequence o = (Sequence) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true) && compareDeep(coordinateSystem, o.coordinateSystem, true)
           && compareDeep(patient, o.patient, true) && compareDeep(specimen, o.specimen, true) && compareDeep(device, o.device, true)
           && compareDeep(performer, o.performer, true) && compareDeep(quantity, o.quantity, true) && compareDeep(referenceSeq, o.referenceSeq, true)
           && compareDeep(variant, o.variant, true) && compareDeep(observedSeq, o.observedSeq, true) && compareDeep(quality, o.quality, true)
           && compareDeep(readCoverage, o.readCoverage, true) && compareDeep(repository, o.repository, true)
           && compareDeep(pointer, o.pointer, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Sequence))
          return false;
        Sequence o = (Sequence) other;
        return compareValues(type, o.type, true) && compareValues(coordinateSystem, o.coordinateSystem, true)
           && compareValues(observedSeq, o.observedSeq, true) && compareValues(readCoverage, o.readCoverage, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, type, coordinateSystem
          , patient, specimen, device, performer, quantity, referenceSeq, variant, observedSeq
          , quality, readCoverage, repository, pointer);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Sequence;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The unique identity for a particular sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Sequence.identifier", description="The unique identity for a particular sequence", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The unique identity for a particular sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>coordinate</b>
   * <p>
   * Description: <b>Search parameter for region of the reference DNA sequence string. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `coordinate=1$lt345$gt123`, this means it will search for the Sequence resource on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="coordinate", path="Sequence.variant", description="Search parameter for region of the reference DNA sequence string. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `coordinate=1$lt345$gt123`, this means it will search for the Sequence resource on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.", type="composite", compositeOf={"chromosome", "start"} )
  public static final String SP_COORDINATE = "coordinate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>coordinate</b>
   * <p>
   * Description: <b>Search parameter for region of the reference DNA sequence string. This will refer to part of a locus or part of a gene where search region will be represented in 1-based system. Since the coordinateSystem can either be 0-based or 1-based, this search query will include the result of both coordinateSystem that contains the equivalent segment of the gene or whole genome sequence. For example, a search for sequence can be represented as `coordinate=1$lt345$gt123`, this means it will search for the Sequence resource on chromosome 1 and with position >123 and <345, where in 1-based system resource, all strings within region 1:124-344 will be revealed, while in 0-based system resource, all strings within region 1:123-344 will be revealed. You may want to check detail about 0-based v.s. 1-based above.</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam> COORDINATE = new ca.uhn.fhir.rest.gclient.CompositeClientParam<ca.uhn.fhir.rest.gclient.TokenClientParam, ca.uhn.fhir.rest.gclient.NumberClientParam>(SP_COORDINATE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The subject that the observation is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Sequence.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Sequence.patient", description="The subject that the observation is about", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The subject that the observation is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Sequence.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Sequence:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Sequence:patient").toLocked();

 /**
   * Search parameter: <b>chromosome</b>
   * <p>
   * Description: <b>Chromosome number of the reference sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.referenceSeq.chromosome</b><br>
   * </p>
   */
  @SearchParamDefinition(name="chromosome", path="Sequence.referenceSeq.chromosome", description="Chromosome number of the reference sequence", type="token" )
  public static final String SP_CHROMOSOME = "chromosome";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>chromosome</b>
   * <p>
   * Description: <b>Chromosome number of the reference sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.referenceSeq.chromosome</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CHROMOSOME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CHROMOSOME);

 /**
   * Search parameter: <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.referenceSeq.windowStart</b><br>
   * </p>
   */
  @SearchParamDefinition(name="start", path="Sequence.referenceSeq.windowStart", description="Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the reference sequence.", type="number" )
  public static final String SP_START = "start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive, 1-based inclusive, that means the nucleic acid or amino acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.referenceSeq.windowStart</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam START = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_START);

 /**
   * Search parameter: <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.referenceSeq.windowEnd</b><br>
   * </p>
   */
  @SearchParamDefinition(name="end", path="Sequence.referenceSeq.windowEnd", description="End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the reference sequence.", type="number" )
  public static final String SP_END = "end";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive, which menas the acid at this position will not be included, 1-based inclusive, which means the acid at this position will be included) of the reference sequence.</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.referenceSeq.windowEnd</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam END = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_END);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Amino Acid Sequence/ DNA Sequence / RNA Sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Sequence.type", description="Amino Acid Sequence/ DNA Sequence / RNA Sequence", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Amino Acid Sequence/ DNA Sequence / RNA Sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

