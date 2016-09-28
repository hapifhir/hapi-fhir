package org.hl7.fhir.dstu3.model;

import java.math.BigDecimal;

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
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.*;
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
        if ("AA".equals(codeString))
          return AA;
        if ("DNA".equals(codeString))
          return DNA;
        if ("RNA".equals(codeString))
          return RNA;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SequenceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AA: return "AA";
            case DNA: return "DNA";
            case RNA: return "RNA";
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
        if ("AA".equals(codeString))
          return SequenceType.AA;
        if ("DNA".equals(codeString))
          return SequenceType.DNA;
        if ("RNA".equals(codeString))
          return SequenceType.RNA;
        throw new IllegalArgumentException("Unknown SequenceType code '"+codeString+"'");
        }
        public Enumeration<SequenceType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("AA".equals(codeString))
          return new Enumeration<SequenceType>(this, SequenceType.AA);
        if ("DNA".equals(codeString))
          return new Enumeration<SequenceType>(this, SequenceType.DNA);
        if ("RNA".equals(codeString))
          return new Enumeration<SequenceType>(this, SequenceType.RNA);
        throw new FHIRException("Unknown SequenceType code '"+codeString+"'");
        }
    public String toCode(SequenceType code) {
      if (code == SequenceType.AA)
        return "AA";
      if (code == SequenceType.DNA)
        return "DNA";
      if (code == SequenceType.RNA)
        return "RNA";
      return "?";
      }
    public String toSystem(SequenceType code) {
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
        @Child(name = "referenceSeqId", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference identifier", formalDefinition="Reference identifier of reference sequence submitted to NCBI. It must match the type in the Sequence.type field. For example, the prefix, “NG_” identifies reference sequence for genes, “NM_” for messenger RNA transcripts, and “NP_” for amino acid sequences." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sequence-referenceSeq")
        protected CodeableConcept referenceSeqId;

        /**
         * A Pointer to another Sequence entity as refence sequence.
         */
        @Child(name = "referenceSeqPointer", type = {Sequence.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A Pointer to another Sequence entity as refence sequence", formalDefinition="A Pointer to another Sequence entity as refence sequence." )
        protected Reference referenceSeqPointer;

        /**
         * The actual object that is the target of the reference (A Pointer to another Sequence entity as refence sequence.)
         */
        protected Sequence referenceSeqPointerTarget;

        /**
         * A Reference Sequence string.
         */
        @Child(name = "referenceSeqString", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A Reference Sequence string", formalDefinition="A Reference Sequence string." )
        protected StringType referenceSeqString;

        /**
         * Strand of DNA. Available values are "1" for the plus strand and "-1" for the minus strand.
         */
        @Child(name = "strand", type = {IntegerType.class}, order=6, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Strand of DNA", formalDefinition="Strand of DNA. Available values are \"1\" for the plus strand and \"-1\" for the minus strand." )
        protected IntegerType strand;

        /**
         * Start position (inclusive) of the window on the reference sequence.
         */
        @Child(name = "windowStart", type = {IntegerType.class}, order=7, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position (inclusive) of the window on the  reference sequence", formalDefinition="Start position (inclusive) of the window on the reference sequence." )
        protected IntegerType windowStart;

        /**
         * End position (exclusive) of the window on the reference sequence.
         */
        @Child(name = "windowEnd", type = {IntegerType.class}, order=8, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position (exclusive) of the window on the reference sequence", formalDefinition="End position (exclusive) of the window on the reference sequence." )
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
      public SequenceReferenceSeqComponent(CodeableConcept referenceSeqId, IntegerType strand, IntegerType windowStart, IntegerType windowEnd) {
        super();
        this.referenceSeqId = referenceSeqId;
        this.strand = strand;
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
         * @return {@link #referenceSeqPointer} (A Pointer to another Sequence entity as refence sequence.)
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
         * @param value {@link #referenceSeqPointer} (A Pointer to another Sequence entity as refence sequence.)
         */
        public SequenceReferenceSeqComponent setReferenceSeqPointer(Reference value) { 
          this.referenceSeqPointer = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqPointer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A Pointer to another Sequence entity as refence sequence.)
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
         * @param value {@link #referenceSeqPointer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A Pointer to another Sequence entity as refence sequence.)
         */
        public SequenceReferenceSeqComponent setReferenceSeqPointerTarget(Sequence value) { 
          this.referenceSeqPointerTarget = value;
          return this;
        }

        /**
         * @return {@link #referenceSeqString} (A Reference Sequence string.). This is the underlying object with id, value and extensions. The accessor "getReferenceSeqString" gives direct access to the value
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
         * @param value {@link #referenceSeqString} (A Reference Sequence string.). This is the underlying object with id, value and extensions. The accessor "getReferenceSeqString" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setReferenceSeqStringElement(StringType value) { 
          this.referenceSeqString = value;
          return this;
        }

        /**
         * @return A Reference Sequence string.
         */
        public String getReferenceSeqString() { 
          return this.referenceSeqString == null ? null : this.referenceSeqString.getValue();
        }

        /**
         * @param value A Reference Sequence string.
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
         * @return {@link #strand} (Strand of DNA. Available values are "1" for the plus strand and "-1" for the minus strand.). This is the underlying object with id, value and extensions. The accessor "getStrand" gives direct access to the value
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
         * @param value {@link #strand} (Strand of DNA. Available values are "1" for the plus strand and "-1" for the minus strand.). This is the underlying object with id, value and extensions. The accessor "getStrand" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setStrandElement(IntegerType value) { 
          this.strand = value;
          return this;
        }

        /**
         * @return Strand of DNA. Available values are "1" for the plus strand and "-1" for the minus strand.
         */
        public int getStrand() { 
          return this.strand == null || this.strand.isEmpty() ? 0 : this.strand.getValue();
        }

        /**
         * @param value Strand of DNA. Available values are "1" for the plus strand and "-1" for the minus strand.
         */
        public SequenceReferenceSeqComponent setStrand(int value) { 
            if (this.strand == null)
              this.strand = new IntegerType();
            this.strand.setValue(value);
          return this;
        }

        /**
         * @return {@link #windowStart} (Start position (inclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowStart" gives direct access to the value
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
         * @param value {@link #windowStart} (Start position (inclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowStart" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setWindowStartElement(IntegerType value) { 
          this.windowStart = value;
          return this;
        }

        /**
         * @return Start position (inclusive) of the window on the reference sequence.
         */
        public int getWindowStart() { 
          return this.windowStart == null || this.windowStart.isEmpty() ? 0 : this.windowStart.getValue();
        }

        /**
         * @param value Start position (inclusive) of the window on the reference sequence.
         */
        public SequenceReferenceSeqComponent setWindowStart(int value) { 
            if (this.windowStart == null)
              this.windowStart = new IntegerType();
            this.windowStart.setValue(value);
          return this;
        }

        /**
         * @return {@link #windowEnd} (End position (exclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
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
         * @param value {@link #windowEnd} (End position (exclusive) of the window on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getWindowEnd" gives direct access to the value
         */
        public SequenceReferenceSeqComponent setWindowEndElement(IntegerType value) { 
          this.windowEnd = value;
          return this;
        }

        /**
         * @return End position (exclusive) of the window on the reference sequence.
         */
        public int getWindowEnd() { 
          return this.windowEnd == null || this.windowEnd.isEmpty() ? 0 : this.windowEnd.getValue();
        }

        /**
         * @param value End position (exclusive) of the window on the reference sequence.
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
          childrenList.add(new Property("referenceSeqPointer", "Reference(Sequence)", "A Pointer to another Sequence entity as refence sequence.", 0, java.lang.Integer.MAX_VALUE, referenceSeqPointer));
          childrenList.add(new Property("referenceSeqString", "string", "A Reference Sequence string.", 0, java.lang.Integer.MAX_VALUE, referenceSeqString));
          childrenList.add(new Property("strand", "integer", "Strand of DNA. Available values are \"1\" for the plus strand and \"-1\" for the minus strand.", 0, java.lang.Integer.MAX_VALUE, strand));
          childrenList.add(new Property("windowStart", "integer", "Start position (inclusive) of the window on the reference sequence.", 0, java.lang.Integer.MAX_VALUE, windowStart));
          childrenList.add(new Property("windowEnd", "integer", "End position (exclusive) of the window on the reference sequence.", 0, java.lang.Integer.MAX_VALUE, windowEnd));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1499470472: // chromosome
          this.chromosome = castToCodeableConcept(value); // CodeableConcept
          break;
        case 1061239735: // genomeBuild
          this.genomeBuild = castToString(value); // StringType
          break;
        case -1911500465: // referenceSeqId
          this.referenceSeqId = castToCodeableConcept(value); // CodeableConcept
          break;
        case 1923414665: // referenceSeqPointer
          this.referenceSeqPointer = castToReference(value); // Reference
          break;
        case -1648301499: // referenceSeqString
          this.referenceSeqString = castToString(value); // StringType
          break;
        case -891993594: // strand
          this.strand = castToInteger(value); // IntegerType
          break;
        case 1903685202: // windowStart
          this.windowStart = castToInteger(value); // IntegerType
          break;
        case -217026869: // windowEnd
          this.windowEnd = castToInteger(value); // IntegerType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("chromosome"))
          this.chromosome = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("genomeBuild"))
          this.genomeBuild = castToString(value); // StringType
        else if (name.equals("referenceSeqId"))
          this.referenceSeqId = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("referenceSeqPointer"))
          this.referenceSeqPointer = castToReference(value); // Reference
        else if (name.equals("referenceSeqString"))
          this.referenceSeqString = castToString(value); // StringType
        else if (name.equals("strand"))
          this.strand = castToInteger(value); // IntegerType
        else if (name.equals("windowStart"))
          this.windowStart = castToInteger(value); // IntegerType
        else if (name.equals("windowEnd"))
          this.windowEnd = castToInteger(value); // IntegerType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1499470472:  return getChromosome(); // CodeableConcept
        case 1061239735: throw new FHIRException("Cannot make property genomeBuild as it is not a complex type"); // StringType
        case -1911500465:  return getReferenceSeqId(); // CodeableConcept
        case 1923414665:  return getReferenceSeqPointer(); // Reference
        case -1648301499: throw new FHIRException("Cannot make property referenceSeqString as it is not a complex type"); // StringType
        case -891993594: throw new FHIRException("Cannot make property strand as it is not a complex type"); // IntegerType
        case 1903685202: throw new FHIRException("Cannot make property windowStart as it is not a complex type"); // IntegerType
        case -217026869: throw new FHIRException("Cannot make property windowEnd as it is not a complex type"); // IntegerType
        default: return super.makeProperty(hash, name);
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
         * Start position (inclusive) of the variant on the  reference sequence.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position (inclusive) of the variant on the  reference sequence", formalDefinition="Start position (inclusive) of the variant on the  reference sequence." )
        protected IntegerType start;

        /**
         * End position (exclusive) of the variant on the reference sequence.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position (exclusive) of the variant on the reference sequence", formalDefinition="End position (exclusive) of the variant on the reference sequence." )
        protected IntegerType end;

        /**
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
         */
        @Child(name = "observedAllele", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Allele that was observed", formalDefinition="An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand." )
        protected StringType observedAllele;

        /**
         * An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
         */
        @Child(name = "referenceAllele", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Allele of reference sequence", formalDefinition="An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand." )
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
         * @return {@link #start} (Start position (inclusive) of the variant on the  reference sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
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
         * @param value {@link #start} (Start position (inclusive) of the variant on the  reference sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceVariantComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Start position (inclusive) of the variant on the  reference sequence.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Start position (inclusive) of the variant on the  reference sequence.
         */
        public SequenceVariantComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (End position (exclusive) of the variant on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
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
         * @param value {@link #end} (End position (exclusive) of the variant on the reference sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceVariantComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return End position (exclusive) of the variant on the reference sequence.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value End position (exclusive) of the variant on the reference sequence.
         */
        public SequenceVariantComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        /**
         * @return {@link #observedAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getObservedAllele" gives direct access to the value
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
         * @param value {@link #observedAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getObservedAllele" gives direct access to the value
         */
        public SequenceVariantComponent setObservedAlleleElement(StringType value) { 
          this.observedAllele = value;
          return this;
        }

        /**
         * @return An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
         */
        public String getObservedAllele() { 
          return this.observedAllele == null ? null : this.observedAllele.getValue();
        }

        /**
         * @param value An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
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
         * @return {@link #referenceAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getReferenceAllele" gives direct access to the value
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
         * @param value {@link #referenceAllele} (An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getReferenceAllele" gives direct access to the value
         */
        public SequenceVariantComponent setReferenceAlleleElement(StringType value) { 
          this.referenceAllele = value;
          return this;
        }

        /**
         * @return An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
         */
        public String getReferenceAllele() { 
          return this.referenceAllele == null ? null : this.referenceAllele.getValue();
        }

        /**
         * @param value An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
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
          childrenList.add(new Property("start", "integer", "Start position (inclusive) of the variant on the  reference sequence.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "End position (exclusive) of the variant on the reference sequence.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("observedAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.", 0, java.lang.Integer.MAX_VALUE, observedAllele));
          childrenList.add(new Property("referenceAllele", "string", "An allele is one of a set of coexisting sequence variants of a gene ([SO:0001023](http://www.sequenceontology.org/browser/current_svn/term/SO:0001023)). Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.", 0, java.lang.Integer.MAX_VALUE, referenceAllele));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          break;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          break;
        case -1418745787: // observedAllele
          this.observedAllele = castToString(value); // StringType
          break;
        case 364045960: // referenceAllele
          this.referenceAllele = castToString(value); // StringType
          break;
        case 94658738: // cigar
          this.cigar = castToString(value); // StringType
          break;
        case -1654319624: // variantPointer
          this.variantPointer = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start"))
          this.start = castToInteger(value); // IntegerType
        else if (name.equals("end"))
          this.end = castToInteger(value); // IntegerType
        else if (name.equals("observedAllele"))
          this.observedAllele = castToString(value); // StringType
        else if (name.equals("referenceAllele"))
          this.referenceAllele = castToString(value); // StringType
        else if (name.equals("cigar"))
          this.cigar = castToString(value); // StringType
        else if (name.equals("variantPointer"))
          this.variantPointer = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: throw new FHIRException("Cannot make property start as it is not a complex type"); // IntegerType
        case 100571: throw new FHIRException("Cannot make property end as it is not a complex type"); // IntegerType
        case -1418745787: throw new FHIRException("Cannot make property observedAllele as it is not a complex type"); // StringType
        case 364045960: throw new FHIRException("Cannot make property referenceAllele as it is not a complex type"); // StringType
        case 94658738: throw new FHIRException("Cannot make property cigar as it is not a complex type"); // StringType
        case -1654319624:  return getVariantPointer(); // Reference
        default: return super.makeProperty(hash, name);
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
         * Gold standard sequence used for comparing against.
         */
        @Child(name = "standardSequence", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Standard sequence for comparison", formalDefinition="Gold standard sequence used for comparing against." )
        protected CodeableConcept standardSequence;

        /**
         * Start position (inclusive) of the sequence.
         */
        @Child(name = "start", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Start position (inclusive) of the sequence", formalDefinition="Start position (inclusive) of the sequence." )
        protected IntegerType start;

        /**
         * End position (exclusive) of the sequence.
         */
        @Child(name = "end", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="End position (exclusive) of the sequence", formalDefinition="End position (exclusive) of the sequence." )
        protected IntegerType end;

        /**
         * The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).
         */
        @Child(name = "score", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quality score", formalDefinition="The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685))." )
        protected Quantity score;

        /**
         * Method for quality.
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Method for quality", formalDefinition="Method for quality." )
        protected CodeableConcept method;

        /**
         * True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        @Child(name = "truthTP", type = {DecimalType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="True positives from the perspective of the truth data", formalDefinition="True positives, from the perspective of the truth data, i.e. the number of sites in the Truth Call Set for which there are paths through the Query Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event." )
        protected DecimalType truthTP;

        /**
         * True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event.
         */
        @Child(name = "queryTP", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="True positives from the perspective of the query data", formalDefinition="True positives, from the perspective of the query data, i.e. the number of sites in the Query Call Set for which there are paths through the Truth Call Set that are consistent with all of the alleles at this site, and for which there is an accurate genotype call for the event." )
        protected DecimalType queryTP;

        /**
         * False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here.
         */
        @Child(name = "truthFN", type = {DecimalType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="False negatives", formalDefinition="False negatives, i.e. the number of sites in the Truth Call Set for which there is no path through the Query Call Set that is consistent with all of the alleles at this site, or sites for which there is an inaccurate genotype call for the event. Sites with correct variant but incorrect genotype are counted here." )
        protected DecimalType truthFN;

        /**
         * False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here.
         */
        @Child(name = "queryFP", type = {DecimalType.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="False positives", formalDefinition="False positives, i.e. the number of sites in the Query Call Set for which there is no path through the Truth Call Set that is consistent with this site. Sites with correct variant but incorrect genotype are counted here." )
        protected DecimalType queryFP;

        /**
         * The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar).
         */
        @Child(name = "gtFP", type = {DecimalType.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="False positives where the non-REF alleles in the Truth and Query Call Sets match", formalDefinition="The number of false positives where the non-REF alleles in the Truth and Query Call Sets match (i.e. cases where the truth is 1/1 and the query is 0/1 or similar)." )
        protected DecimalType gtFP;

        /**
         * QUERY.TP / (QUERY.TP + QUERY.FP).
         */
        @Child(name = "precision", type = {DecimalType.class}, order=11, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Precision", formalDefinition="QUERY.TP / (QUERY.TP + QUERY.FP)." )
        protected DecimalType precision;

        /**
         * TRUTH.TP / (TRUTH.TP + TRUTH.FN).
         */
        @Child(name = "recall", type = {DecimalType.class}, order=12, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Recall", formalDefinition="TRUTH.TP / (TRUTH.TP + TRUTH.FN)." )
        protected DecimalType recall;

        /**
         * Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall).
         */
        @Child(name = "fScore", type = {DecimalType.class}, order=13, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="F-score", formalDefinition="Harmonic mean of Recall and Precision, computed as: 2 * precision * recall / (precision + recall)." )
        protected DecimalType fScore;

        private static final long serialVersionUID = 529445255L;

    /**
     * Constructor
     */
      public SequenceQualityComponent() {
        super();
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
         * @return {@link #start} (Start position (inclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
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
         * @param value {@link #start} (Start position (inclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceQualityComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Start position (inclusive) of the sequence.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Start position (inclusive) of the sequence.
         */
        public SequenceQualityComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (End position (exclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
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
         * @param value {@link #end} (End position (exclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceQualityComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return End position (exclusive) of the sequence.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value End position (exclusive) of the sequence.
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
         * @return {@link #method} (Method for quality.)
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
         * @param value {@link #method} (Method for quality.)
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
          childrenList.add(new Property("standardSequence", "CodeableConcept", "Gold standard sequence used for comparing against.", 0, java.lang.Integer.MAX_VALUE, standardSequence));
          childrenList.add(new Property("start", "integer", "Start position (inclusive) of the sequence.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "End position (exclusive) of the sequence.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("score", "Quantity", "The score of an experimentally derived feature such as a p-value ([SO:0001685](http://www.sequenceontology.org/browser/current_svn/term/SO:0001685)).", 0, java.lang.Integer.MAX_VALUE, score));
          childrenList.add(new Property("method", "CodeableConcept", "Method for quality.", 0, java.lang.Integer.MAX_VALUE, method));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1861227106: // standardSequence
          this.standardSequence = castToCodeableConcept(value); // CodeableConcept
          break;
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          break;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          break;
        case 109264530: // score
          this.score = castToQuantity(value); // Quantity
          break;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1048421849: // truthTP
          this.truthTP = castToDecimal(value); // DecimalType
          break;
        case 655102276: // queryTP
          this.queryTP = castToDecimal(value); // DecimalType
          break;
        case -1048422285: // truthFN
          this.truthFN = castToDecimal(value); // DecimalType
          break;
        case 655101842: // queryFP
          this.queryFP = castToDecimal(value); // DecimalType
          break;
        case 3182199: // gtFP
          this.gtFP = castToDecimal(value); // DecimalType
          break;
        case -1376177026: // precision
          this.precision = castToDecimal(value); // DecimalType
          break;
        case -934922479: // recall
          this.recall = castToDecimal(value); // DecimalType
          break;
        case -1295082036: // fScore
          this.fScore = castToDecimal(value); // DecimalType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("standardSequence"))
          this.standardSequence = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("start"))
          this.start = castToInteger(value); // IntegerType
        else if (name.equals("end"))
          this.end = castToInteger(value); // IntegerType
        else if (name.equals("score"))
          this.score = castToQuantity(value); // Quantity
        else if (name.equals("method"))
          this.method = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("truthTP"))
          this.truthTP = castToDecimal(value); // DecimalType
        else if (name.equals("queryTP"))
          this.queryTP = castToDecimal(value); // DecimalType
        else if (name.equals("truthFN"))
          this.truthFN = castToDecimal(value); // DecimalType
        else if (name.equals("queryFP"))
          this.queryFP = castToDecimal(value); // DecimalType
        else if (name.equals("gtFP"))
          this.gtFP = castToDecimal(value); // DecimalType
        else if (name.equals("precision"))
          this.precision = castToDecimal(value); // DecimalType
        else if (name.equals("recall"))
          this.recall = castToDecimal(value); // DecimalType
        else if (name.equals("fScore"))
          this.fScore = castToDecimal(value); // DecimalType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1861227106:  return getStandardSequence(); // CodeableConcept
        case 109757538: throw new FHIRException("Cannot make property start as it is not a complex type"); // IntegerType
        case 100571: throw new FHIRException("Cannot make property end as it is not a complex type"); // IntegerType
        case 109264530:  return getScore(); // Quantity
        case -1077554975:  return getMethod(); // CodeableConcept
        case -1048421849: throw new FHIRException("Cannot make property truthTP as it is not a complex type"); // DecimalType
        case 655102276: throw new FHIRException("Cannot make property queryTP as it is not a complex type"); // DecimalType
        case -1048422285: throw new FHIRException("Cannot make property truthFN as it is not a complex type"); // DecimalType
        case 655101842: throw new FHIRException("Cannot make property queryFP as it is not a complex type"); // DecimalType
        case 3182199: throw new FHIRException("Cannot make property gtFP as it is not a complex type"); // DecimalType
        case -1376177026: throw new FHIRException("Cannot make property precision as it is not a complex type"); // DecimalType
        case -934922479: throw new FHIRException("Cannot make property recall as it is not a complex type"); // DecimalType
        case -1295082036: throw new FHIRException("Cannot make property fScore as it is not a complex type"); // DecimalType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("standardSequence")) {
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
        return compareDeep(standardSequence, o.standardSequence, true) && compareDeep(start, o.start, true)
           && compareDeep(end, o.end, true) && compareDeep(score, o.score, true) && compareDeep(method, o.method, true)
           && compareDeep(truthTP, o.truthTP, true) && compareDeep(queryTP, o.queryTP, true) && compareDeep(truthFN, o.truthFN, true)
           && compareDeep(queryFP, o.queryFP, true) && compareDeep(gtFP, o.gtFP, true) && compareDeep(precision, o.precision, true)
           && compareDeep(recall, o.recall, true) && compareDeep(fScore, o.fScore, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceQualityComponent))
          return false;
        SequenceQualityComponent o = (SequenceQualityComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(truthTP, o.truthTP, true)
           && compareValues(queryTP, o.queryTP, true) && compareValues(truthFN, o.truthFN, true) && compareValues(queryFP, o.queryFP, true)
           && compareValues(gtFP, o.gtFP, true) && compareValues(precision, o.precision, true) && compareValues(recall, o.recall, true)
           && compareValues(fScore, o.fScore, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(standardSequence, start, end
          , score, method, truthTP, queryTP, truthFN, queryFP, gtFP, precision, recall
          , fScore);
      }

  public String fhirType() {
    return "Sequence.quality";

  }

  }

    @Block()
    public static class SequenceRepositoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * URI of an external repository which contains further details about the genetics data.
         */
        @Child(name = "url", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="URI of the repository", formalDefinition="URI of an external repository which contains further details about the genetics data." )
        protected UriType url;

        /**
         * URI of an external repository which contains further details about the genetics data.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of the repository", formalDefinition="URI of an external repository which contains further details about the genetics data." )
        protected StringType name;

        /**
         * Id of the variant in this external repository.
         */
        @Child(name = "variantId", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of the variant", formalDefinition="Id of the variant in this external repository." )
        protected StringType variantId;

        /**
         * Id of the read in this external repository.
         */
        @Child(name = "readId", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of the read", formalDefinition="Id of the read in this external repository." )
        protected StringType readId;

        private static final long serialVersionUID = 1218159360L;

    /**
     * Constructor
     */
      public SequenceRepositoryComponent() {
        super();
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
         * @return {@link #variantId} (Id of the variant in this external repository.). This is the underlying object with id, value and extensions. The accessor "getVariantId" gives direct access to the value
         */
        public StringType getVariantIdElement() { 
          if (this.variantId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.variantId");
            else if (Configuration.doAutoCreate())
              this.variantId = new StringType(); // bb
          return this.variantId;
        }

        public boolean hasVariantIdElement() { 
          return this.variantId != null && !this.variantId.isEmpty();
        }

        public boolean hasVariantId() { 
          return this.variantId != null && !this.variantId.isEmpty();
        }

        /**
         * @param value {@link #variantId} (Id of the variant in this external repository.). This is the underlying object with id, value and extensions. The accessor "getVariantId" gives direct access to the value
         */
        public SequenceRepositoryComponent setVariantIdElement(StringType value) { 
          this.variantId = value;
          return this;
        }

        /**
         * @return Id of the variant in this external repository.
         */
        public String getVariantId() { 
          return this.variantId == null ? null : this.variantId.getValue();
        }

        /**
         * @param value Id of the variant in this external repository.
         */
        public SequenceRepositoryComponent setVariantId(String value) { 
          if (Utilities.noString(value))
            this.variantId = null;
          else {
            if (this.variantId == null)
              this.variantId = new StringType();
            this.variantId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #readId} (Id of the read in this external repository.). This is the underlying object with id, value and extensions. The accessor "getReadId" gives direct access to the value
         */
        public StringType getReadIdElement() { 
          if (this.readId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.readId");
            else if (Configuration.doAutoCreate())
              this.readId = new StringType(); // bb
          return this.readId;
        }

        public boolean hasReadIdElement() { 
          return this.readId != null && !this.readId.isEmpty();
        }

        public boolean hasReadId() { 
          return this.readId != null && !this.readId.isEmpty();
        }

        /**
         * @param value {@link #readId} (Id of the read in this external repository.). This is the underlying object with id, value and extensions. The accessor "getReadId" gives direct access to the value
         */
        public SequenceRepositoryComponent setReadIdElement(StringType value) { 
          this.readId = value;
          return this;
        }

        /**
         * @return Id of the read in this external repository.
         */
        public String getReadId() { 
          return this.readId == null ? null : this.readId.getValue();
        }

        /**
         * @param value Id of the read in this external repository.
         */
        public SequenceRepositoryComponent setReadId(String value) { 
          if (Utilities.noString(value))
            this.readId = null;
          else {
            if (this.readId == null)
              this.readId = new StringType();
            this.readId.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("url", "uri", "URI of an external repository which contains further details about the genetics data.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("name", "string", "URI of an external repository which contains further details about the genetics data.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("variantId", "string", "Id of the variant in this external repository.", 0, java.lang.Integer.MAX_VALUE, variantId));
          childrenList.add(new Property("readId", "string", "Id of the read in this external repository.", 0, java.lang.Integer.MAX_VALUE, readId));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -82113408: /*variantId*/ return this.variantId == null ? new Base[0] : new Base[] {this.variantId}; // StringType
        case -934980271: /*readId*/ return this.readId == null ? new Base[0] : new Base[] {this.readId}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -82113408: // variantId
          this.variantId = castToString(value); // StringType
          break;
        case -934980271: // readId
          this.readId = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("variantId"))
          this.variantId = castToString(value); // StringType
        else if (name.equals("readId"))
          this.readId = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -82113408: throw new FHIRException("Cannot make property variantId as it is not a complex type"); // StringType
        case -934980271: throw new FHIRException("Cannot make property readId as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.url");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.name");
        }
        else if (name.equals("variantId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.variantId");
        }
        else if (name.equals("readId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.readId");
        }
        else
          return super.addChild(name);
      }

      public SequenceRepositoryComponent copy() {
        SequenceRepositoryComponent dst = new SequenceRepositoryComponent();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.name = name == null ? null : name.copy();
        dst.variantId = variantId == null ? null : variantId.copy();
        dst.readId = readId == null ? null : readId.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceRepositoryComponent))
          return false;
        SequenceRepositoryComponent o = (SequenceRepositoryComponent) other;
        return compareDeep(url, o.url, true) && compareDeep(name, o.name, true) && compareDeep(variantId, o.variantId, true)
           && compareDeep(readId, o.readId, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceRepositoryComponent))
          return false;
        SequenceRepositoryComponent o = (SequenceRepositoryComponent) other;
        return compareValues(url, o.url, true) && compareValues(name, o.name, true) && compareValues(variantId, o.variantId, true)
           && compareValues(readId, o.readId, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(url, name, variantId, readId
          );
      }

  public String fhirType() {
    return "Sequence.repository";

  }

  }

    @Block()
    public static class SequenceStructureVariantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Precision of boundaries.
         */
        @Child(name = "precisionOfBoundaries", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Precision of boundaries", formalDefinition="Precision of boundaries." )
        protected StringType precisionOfBoundaries;

        /**
         * Structural Variant reported aCGH ratio.
         */
        @Child(name = "reportedaCGHRatio", type = {DecimalType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural Variant reported aCGH ratio", formalDefinition="Structural Variant reported aCGH ratio." )
        protected DecimalType reportedaCGHRatio;

        /**
         * Structural Variant Length.
         */
        @Child(name = "length", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural Variant Length", formalDefinition="Structural Variant Length." )
        protected IntegerType length;

        /**
         * Structural variant outer.
         */
        @Child(name = "outer", type = {}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="", formalDefinition="Structural variant outer." )
        protected SequenceStructureVariantOuterComponent outer;

        /**
         * Structural variant inner.
         */
        @Child(name = "inner", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="", formalDefinition="Structural variant inner." )
        protected SequenceStructureVariantInnerComponent inner;

        private static final long serialVersionUID = 1705627760L;

    /**
     * Constructor
     */
      public SequenceStructureVariantComponent() {
        super();
      }

        /**
         * @return {@link #precisionOfBoundaries} (Precision of boundaries.). This is the underlying object with id, value and extensions. The accessor "getPrecisionOfBoundaries" gives direct access to the value
         */
        public StringType getPrecisionOfBoundariesElement() { 
          if (this.precisionOfBoundaries == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantComponent.precisionOfBoundaries");
            else if (Configuration.doAutoCreate())
              this.precisionOfBoundaries = new StringType(); // bb
          return this.precisionOfBoundaries;
        }

        public boolean hasPrecisionOfBoundariesElement() { 
          return this.precisionOfBoundaries != null && !this.precisionOfBoundaries.isEmpty();
        }

        public boolean hasPrecisionOfBoundaries() { 
          return this.precisionOfBoundaries != null && !this.precisionOfBoundaries.isEmpty();
        }

        /**
         * @param value {@link #precisionOfBoundaries} (Precision of boundaries.). This is the underlying object with id, value and extensions. The accessor "getPrecisionOfBoundaries" gives direct access to the value
         */
        public SequenceStructureVariantComponent setPrecisionOfBoundariesElement(StringType value) { 
          this.precisionOfBoundaries = value;
          return this;
        }

        /**
         * @return Precision of boundaries.
         */
        public String getPrecisionOfBoundaries() { 
          return this.precisionOfBoundaries == null ? null : this.precisionOfBoundaries.getValue();
        }

        /**
         * @param value Precision of boundaries.
         */
        public SequenceStructureVariantComponent setPrecisionOfBoundaries(String value) { 
          if (Utilities.noString(value))
            this.precisionOfBoundaries = null;
          else {
            if (this.precisionOfBoundaries == null)
              this.precisionOfBoundaries = new StringType();
            this.precisionOfBoundaries.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #reportedaCGHRatio} (Structural Variant reported aCGH ratio.). This is the underlying object with id, value and extensions. The accessor "getReportedaCGHRatio" gives direct access to the value
         */
        public DecimalType getReportedaCGHRatioElement() { 
          if (this.reportedaCGHRatio == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantComponent.reportedaCGHRatio");
            else if (Configuration.doAutoCreate())
              this.reportedaCGHRatio = new DecimalType(); // bb
          return this.reportedaCGHRatio;
        }

        public boolean hasReportedaCGHRatioElement() { 
          return this.reportedaCGHRatio != null && !this.reportedaCGHRatio.isEmpty();
        }

        public boolean hasReportedaCGHRatio() { 
          return this.reportedaCGHRatio != null && !this.reportedaCGHRatio.isEmpty();
        }

        /**
         * @param value {@link #reportedaCGHRatio} (Structural Variant reported aCGH ratio.). This is the underlying object with id, value and extensions. The accessor "getReportedaCGHRatio" gives direct access to the value
         */
        public SequenceStructureVariantComponent setReportedaCGHRatioElement(DecimalType value) { 
          this.reportedaCGHRatio = value;
          return this;
        }

        /**
         * @return Structural Variant reported aCGH ratio.
         */
        public BigDecimal getReportedaCGHRatio() { 
          return this.reportedaCGHRatio == null ? null : this.reportedaCGHRatio.getValue();
        }

        /**
         * @param value Structural Variant reported aCGH ratio.
         */
        public SequenceStructureVariantComponent setReportedaCGHRatio(BigDecimal value) { 
          if (value == null)
            this.reportedaCGHRatio = null;
          else {
            if (this.reportedaCGHRatio == null)
              this.reportedaCGHRatio = new DecimalType();
            this.reportedaCGHRatio.setValue(value);
          }
          return this;
        }

        /**
         * @param value Structural Variant reported aCGH ratio.
         */
        public SequenceStructureVariantComponent setReportedaCGHRatio(long value) { 
              this.reportedaCGHRatio = new DecimalType();
            this.reportedaCGHRatio.setValue(value);
          return this;
        }

        /**
         * @param value Structural Variant reported aCGH ratio.
         */
        public SequenceStructureVariantComponent setReportedaCGHRatio(double value) { 
              this.reportedaCGHRatio = new DecimalType();
            this.reportedaCGHRatio.setValue(value);
          return this;
        }

        /**
         * @return {@link #length} (Structural Variant Length.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public IntegerType getLengthElement() { 
          if (this.length == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantComponent.length");
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
         * @param value {@link #length} (Structural Variant Length.). This is the underlying object with id, value and extensions. The accessor "getLength" gives direct access to the value
         */
        public SequenceStructureVariantComponent setLengthElement(IntegerType value) { 
          this.length = value;
          return this;
        }

        /**
         * @return Structural Variant Length.
         */
        public int getLength() { 
          return this.length == null || this.length.isEmpty() ? 0 : this.length.getValue();
        }

        /**
         * @param value Structural Variant Length.
         */
        public SequenceStructureVariantComponent setLength(int value) { 
            if (this.length == null)
              this.length = new IntegerType();
            this.length.setValue(value);
          return this;
        }

        /**
         * @return {@link #outer} (Structural variant outer.)
         */
        public SequenceStructureVariantOuterComponent getOuter() { 
          if (this.outer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantComponent.outer");
            else if (Configuration.doAutoCreate())
              this.outer = new SequenceStructureVariantOuterComponent(); // cc
          return this.outer;
        }

        public boolean hasOuter() { 
          return this.outer != null && !this.outer.isEmpty();
        }

        /**
         * @param value {@link #outer} (Structural variant outer.)
         */
        public SequenceStructureVariantComponent setOuter(SequenceStructureVariantOuterComponent value) { 
          this.outer = value;
          return this;
        }

        /**
         * @return {@link #inner} (Structural variant inner.)
         */
        public SequenceStructureVariantInnerComponent getInner() { 
          if (this.inner == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantComponent.inner");
            else if (Configuration.doAutoCreate())
              this.inner = new SequenceStructureVariantInnerComponent(); // cc
          return this.inner;
        }

        public boolean hasInner() { 
          return this.inner != null && !this.inner.isEmpty();
        }

        /**
         * @param value {@link #inner} (Structural variant inner.)
         */
        public SequenceStructureVariantComponent setInner(SequenceStructureVariantInnerComponent value) { 
          this.inner = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("precisionOfBoundaries", "string", "Precision of boundaries.", 0, java.lang.Integer.MAX_VALUE, precisionOfBoundaries));
          childrenList.add(new Property("reportedaCGHRatio", "decimal", "Structural Variant reported aCGH ratio.", 0, java.lang.Integer.MAX_VALUE, reportedaCGHRatio));
          childrenList.add(new Property("length", "integer", "Structural Variant Length.", 0, java.lang.Integer.MAX_VALUE, length));
          childrenList.add(new Property("outer", "", "Structural variant outer.", 0, java.lang.Integer.MAX_VALUE, outer));
          childrenList.add(new Property("inner", "", "Structural variant inner.", 0, java.lang.Integer.MAX_VALUE, inner));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1591532317: /*precisionOfBoundaries*/ return this.precisionOfBoundaries == null ? new Base[0] : new Base[] {this.precisionOfBoundaries}; // StringType
        case -1872600587: /*reportedaCGHRatio*/ return this.reportedaCGHRatio == null ? new Base[0] : new Base[] {this.reportedaCGHRatio}; // DecimalType
        case -1106363674: /*length*/ return this.length == null ? new Base[0] : new Base[] {this.length}; // IntegerType
        case 106111099: /*outer*/ return this.outer == null ? new Base[0] : new Base[] {this.outer}; // SequenceStructureVariantOuterComponent
        case 100355670: /*inner*/ return this.inner == null ? new Base[0] : new Base[] {this.inner}; // SequenceStructureVariantInnerComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1591532317: // precisionOfBoundaries
          this.precisionOfBoundaries = castToString(value); // StringType
          break;
        case -1872600587: // reportedaCGHRatio
          this.reportedaCGHRatio = castToDecimal(value); // DecimalType
          break;
        case -1106363674: // length
          this.length = castToInteger(value); // IntegerType
          break;
        case 106111099: // outer
          this.outer = (SequenceStructureVariantOuterComponent) value; // SequenceStructureVariantOuterComponent
          break;
        case 100355670: // inner
          this.inner = (SequenceStructureVariantInnerComponent) value; // SequenceStructureVariantInnerComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("precisionOfBoundaries"))
          this.precisionOfBoundaries = castToString(value); // StringType
        else if (name.equals("reportedaCGHRatio"))
          this.reportedaCGHRatio = castToDecimal(value); // DecimalType
        else if (name.equals("length"))
          this.length = castToInteger(value); // IntegerType
        else if (name.equals("outer"))
          this.outer = (SequenceStructureVariantOuterComponent) value; // SequenceStructureVariantOuterComponent
        else if (name.equals("inner"))
          this.inner = (SequenceStructureVariantInnerComponent) value; // SequenceStructureVariantInnerComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1591532317: throw new FHIRException("Cannot make property precisionOfBoundaries as it is not a complex type"); // StringType
        case -1872600587: throw new FHIRException("Cannot make property reportedaCGHRatio as it is not a complex type"); // DecimalType
        case -1106363674: throw new FHIRException("Cannot make property length as it is not a complex type"); // IntegerType
        case 106111099:  return getOuter(); // SequenceStructureVariantOuterComponent
        case 100355670:  return getInner(); // SequenceStructureVariantInnerComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("precisionOfBoundaries")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.precisionOfBoundaries");
        }
        else if (name.equals("reportedaCGHRatio")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.reportedaCGHRatio");
        }
        else if (name.equals("length")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.length");
        }
        else if (name.equals("outer")) {
          this.outer = new SequenceStructureVariantOuterComponent();
          return this.outer;
        }
        else if (name.equals("inner")) {
          this.inner = new SequenceStructureVariantInnerComponent();
          return this.inner;
        }
        else
          return super.addChild(name);
      }

      public SequenceStructureVariantComponent copy() {
        SequenceStructureVariantComponent dst = new SequenceStructureVariantComponent();
        copyValues(dst);
        dst.precisionOfBoundaries = precisionOfBoundaries == null ? null : precisionOfBoundaries.copy();
        dst.reportedaCGHRatio = reportedaCGHRatio == null ? null : reportedaCGHRatio.copy();
        dst.length = length == null ? null : length.copy();
        dst.outer = outer == null ? null : outer.copy();
        dst.inner = inner == null ? null : inner.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceStructureVariantComponent))
          return false;
        SequenceStructureVariantComponent o = (SequenceStructureVariantComponent) other;
        return compareDeep(precisionOfBoundaries, o.precisionOfBoundaries, true) && compareDeep(reportedaCGHRatio, o.reportedaCGHRatio, true)
           && compareDeep(length, o.length, true) && compareDeep(outer, o.outer, true) && compareDeep(inner, o.inner, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceStructureVariantComponent))
          return false;
        SequenceStructureVariantComponent o = (SequenceStructureVariantComponent) other;
        return compareValues(precisionOfBoundaries, o.precisionOfBoundaries, true) && compareValues(reportedaCGHRatio, o.reportedaCGHRatio, true)
           && compareValues(length, o.length, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(precisionOfBoundaries, reportedaCGHRatio
          , length, outer, inner);
      }

  public String fhirType() {
    return "Sequence.structureVariant";

  }

  }

    @Block()
    public static class SequenceStructureVariantOuterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Structural Variant Outer Start-End.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural Variant Outer Start-End", formalDefinition="Structural Variant Outer Start-End." )
        protected IntegerType start;

        /**
         * Structural Variant Outer Start-End.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural Variant Outer Start-End", formalDefinition="Structural Variant Outer Start-End." )
        protected IntegerType end;

        private static final long serialVersionUID = -1798864889L;

    /**
     * Constructor
     */
      public SequenceStructureVariantOuterComponent() {
        super();
      }

        /**
         * @return {@link #start} (Structural Variant Outer Start-End.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantOuterComponent.start");
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
         * @param value {@link #start} (Structural Variant Outer Start-End.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceStructureVariantOuterComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Structural Variant Outer Start-End.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Structural Variant Outer Start-End.
         */
        public SequenceStructureVariantOuterComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (Structural Variant Outer Start-End.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantOuterComponent.end");
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
         * @param value {@link #end} (Structural Variant Outer Start-End.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceStructureVariantOuterComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return Structural Variant Outer Start-End.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value Structural Variant Outer Start-End.
         */
        public SequenceStructureVariantOuterComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("start", "integer", "Structural Variant Outer Start-End.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "Structural Variant Outer Start-End.", 0, java.lang.Integer.MAX_VALUE, end));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          break;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start"))
          this.start = castToInteger(value); // IntegerType
        else if (name.equals("end"))
          this.end = castToInteger(value); // IntegerType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: throw new FHIRException("Cannot make property start as it is not a complex type"); // IntegerType
        case 100571: throw new FHIRException("Cannot make property end as it is not a complex type"); // IntegerType
        default: return super.makeProperty(hash, name);
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
        else
          return super.addChild(name);
      }

      public SequenceStructureVariantOuterComponent copy() {
        SequenceStructureVariantOuterComponent dst = new SequenceStructureVariantOuterComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceStructureVariantOuterComponent))
          return false;
        SequenceStructureVariantOuterComponent o = (SequenceStructureVariantOuterComponent) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceStructureVariantOuterComponent))
          return false;
        SequenceStructureVariantOuterComponent o = (SequenceStructureVariantOuterComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(start, end);
      }

  public String fhirType() {
    return "Sequence.structureVariant.outer";

  }

  }

    @Block()
    public static class SequenceStructureVariantInnerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Structural Variant Inner Start-End.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural Variant Inner Start-End", formalDefinition="Structural Variant Inner Start-End." )
        protected IntegerType start;

        /**
         * Structural Variant Inner Start-End.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Structural Variant Inner Start-End", formalDefinition="Structural Variant Inner Start-End." )
        protected IntegerType end;

        private static final long serialVersionUID = -1798864889L;

    /**
     * Constructor
     */
      public SequenceStructureVariantInnerComponent() {
        super();
      }

        /**
         * @return {@link #start} (Structural Variant Inner Start-End.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantInnerComponent.start");
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
         * @param value {@link #start} (Structural Variant Inner Start-End.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceStructureVariantInnerComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Structural Variant Inner Start-End.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Structural Variant Inner Start-End.
         */
        public SequenceStructureVariantInnerComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (Structural Variant Inner Start-End.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceStructureVariantInnerComponent.end");
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
         * @param value {@link #end} (Structural Variant Inner Start-End.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceStructureVariantInnerComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return Structural Variant Inner Start-End.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value Structural Variant Inner Start-End.
         */
        public SequenceStructureVariantInnerComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("start", "integer", "Structural Variant Inner Start-End.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "Structural Variant Inner Start-End.", 0, java.lang.Integer.MAX_VALUE, end));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109757538: // start
          this.start = castToInteger(value); // IntegerType
          break;
        case 100571: // end
          this.end = castToInteger(value); // IntegerType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start"))
          this.start = castToInteger(value); // IntegerType
        else if (name.equals("end"))
          this.end = castToInteger(value); // IntegerType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109757538: throw new FHIRException("Cannot make property start as it is not a complex type"); // IntegerType
        case 100571: throw new FHIRException("Cannot make property end as it is not a complex type"); // IntegerType
        default: return super.makeProperty(hash, name);
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
        else
          return super.addChild(name);
      }

      public SequenceStructureVariantInnerComponent copy() {
        SequenceStructureVariantInnerComponent dst = new SequenceStructureVariantInnerComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceStructureVariantInnerComponent))
          return false;
        SequenceStructureVariantInnerComponent o = (SequenceStructureVariantInnerComponent) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceStructureVariantInnerComponent))
          return false;
        SequenceStructureVariantInnerComponent o = (SequenceStructureVariantInnerComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(start, end);
      }

  public String fhirType() {
    return "Sequence.structureVariant.inner";

  }

  }

    /**
     * A unique identifier for this particular sequence instance.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique ID for this particular sequence", formalDefinition="A unique identifier for this particular sequence instance." )
    protected List<Identifier> identifier;

    /**
     * Amino acid / cDNA transcript / RNA variant.
     */
    @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="AA | DNA | RNA", formalDefinition="Amino acid / cDNA transcript / RNA variant." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sequence-type")
    protected Enumeration<SequenceType> type;

    /**
     * Whether the sequence is numbered starting at 0 (0-based numbering or coordinates) or starting at 1 (1-based numbering). Values are "0" for 0-based numbering and "1" for one-based.
     */
    @Child(name = "coordinateSystem", type = {IntegerType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Numbering used for sequence (0-based or 1-based)", formalDefinition="Whether the sequence is numbered starting at 0 (0-based numbering or coordinates) or starting at 1 (1-based numbering). Values are \"0\" for 0-based numbering and \"1\" for one-based." )
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
     * Quantity of the sequence.
     */
    @Child(name = "quantity", type = {Quantity.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Quantity of the sequence", formalDefinition="Quantity of the sequence." )
    protected Quantity quantity;

    /**
     * A reference sequence is a sequence that is used to represent an allele or variant.
     */
    @Child(name = "referenceSeq", type = {}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reference sequence", formalDefinition="A reference sequence is a sequence that is used to represent an allele or variant." )
    protected SequenceReferenceSeqComponent referenceSeq;

    /**
     * A' is a variant (mutation) of A = definition every instance of A' is either an immediate mutation of some instance of A, or there is a chain of immediate mutation processes linking A' to some instance of A ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)).
     */
    @Child(name = "variant", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Sequence variant", formalDefinition="A' is a variant (mutation) of A = definition every instance of A' is either an immediate mutation of some instance of A, or there is a chain of immediate mutation processes linking A' to some instance of A ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of))." )
    protected List<SequenceVariantComponent> variant;

    /**
     * Sequence that was observed.
     */
    @Child(name = "observedSeq", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Observed sequence", formalDefinition="Sequence that was observed." )
    protected StringType observedSeq;

    /**
     * An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).
     */
    @Child(name = "quality", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Sequence quality", formalDefinition="An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686))." )
    protected List<SequenceQualityComponent> quality;

    /**
     * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.
     */
    @Child(name = "readCoverage", type = {IntegerType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Average number of reads representing a given nucleotide in the reconstructed sequence", formalDefinition="Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence." )
    protected IntegerType readCoverage;

    /**
     * Configurations of the external repository.
     */
    @Child(name = "repository", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External repository", formalDefinition="Configurations of the external repository." )
    protected List<SequenceRepositoryComponent> repository;

    /**
     * Pointer to next atomic sequence which at most contains one variant.
     */
    @Child(name = "pointer", type = {Sequence.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Pointer to next atomic sequence", formalDefinition="Pointer to next atomic sequence which at most contains one variant." )
    protected List<Reference> pointer;
    /**
     * The actual objects that are the target of the reference (Pointer to next atomic sequence which at most contains one variant.)
     */
    protected List<Sequence> pointerTarget;


    /**
     * Structural variant.
     */
    @Child(name = "structureVariant", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="", formalDefinition="Structural variant." )
    protected List<SequenceStructureVariantComponent> structureVariant;

    private static final long serialVersionUID = -1713460757L;

  /**
   * Constructor
   */
    public Sequence() {
      super();
    }

  /**
   * Constructor
   */
    public Sequence(Enumeration<SequenceType> type, IntegerType coordinateSystem) {
      super();
      this.type = type;
      this.coordinateSystem = coordinateSystem;
    }

    /**
     * @return {@link #identifier} (A unique identifier for this particular sequence instance.)
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
     * @return {@link #type} (Amino acid / cDNA transcript / RNA variant.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
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
     * @param value {@link #type} (Amino acid / cDNA transcript / RNA variant.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Sequence setTypeElement(Enumeration<SequenceType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Amino acid / cDNA transcript / RNA variant.
     */
    public SequenceType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Amino acid / cDNA transcript / RNA variant.
     */
    public Sequence setType(SequenceType value) { 
        if (this.type == null)
          this.type = new Enumeration<SequenceType>(new SequenceTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #coordinateSystem} (Whether the sequence is numbered starting at 0 (0-based numbering or coordinates) or starting at 1 (1-based numbering). Values are "0" for 0-based numbering and "1" for one-based.). This is the underlying object with id, value and extensions. The accessor "getCoordinateSystem" gives direct access to the value
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
     * @param value {@link #coordinateSystem} (Whether the sequence is numbered starting at 0 (0-based numbering or coordinates) or starting at 1 (1-based numbering). Values are "0" for 0-based numbering and "1" for one-based.). This is the underlying object with id, value and extensions. The accessor "getCoordinateSystem" gives direct access to the value
     */
    public Sequence setCoordinateSystemElement(IntegerType value) { 
      this.coordinateSystem = value;
      return this;
    }

    /**
     * @return Whether the sequence is numbered starting at 0 (0-based numbering or coordinates) or starting at 1 (1-based numbering). Values are "0" for 0-based numbering and "1" for one-based.
     */
    public int getCoordinateSystem() { 
      return this.coordinateSystem == null || this.coordinateSystem.isEmpty() ? 0 : this.coordinateSystem.getValue();
    }

    /**
     * @param value Whether the sequence is numbered starting at 0 (0-based numbering or coordinates) or starting at 1 (1-based numbering). Values are "0" for 0-based numbering and "1" for one-based.
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
     * @return {@link #quantity} (Quantity of the sequence.)
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
     * @param value {@link #quantity} (Quantity of the sequence.)
     */
    public Sequence setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #referenceSeq} (A reference sequence is a sequence that is used to represent an allele or variant.)
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
     * @param value {@link #referenceSeq} (A reference sequence is a sequence that is used to represent an allele or variant.)
     */
    public Sequence setReferenceSeq(SequenceReferenceSeqComponent value) { 
      this.referenceSeq = value;
      return this;
    }

    /**
     * @return {@link #variant} (A' is a variant (mutation) of A = definition every instance of A' is either an immediate mutation of some instance of A, or there is a chain of immediate mutation processes linking A' to some instance of A ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)).)
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
     * @return {@link #observedSeq} (Sequence that was observed.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
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
     * @param value {@link #observedSeq} (Sequence that was observed.). This is the underlying object with id, value and extensions. The accessor "getObservedSeq" gives direct access to the value
     */
    public Sequence setObservedSeqElement(StringType value) { 
      this.observedSeq = value;
      return this;
    }

    /**
     * @return Sequence that was observed.
     */
    public String getObservedSeq() { 
      return this.observedSeq == null ? null : this.observedSeq.getValue();
    }

    /**
     * @param value Sequence that was observed.
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
     * @return {@link #repository} (Configurations of the external repository.)
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

    /**
     * @return {@link #structureVariant} (Structural variant.)
     */
    public List<SequenceStructureVariantComponent> getStructureVariant() { 
      if (this.structureVariant == null)
        this.structureVariant = new ArrayList<SequenceStructureVariantComponent>();
      return this.structureVariant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Sequence setStructureVariant(List<SequenceStructureVariantComponent> theStructureVariant) { 
      this.structureVariant = theStructureVariant;
      return this;
    }

    public boolean hasStructureVariant() { 
      if (this.structureVariant == null)
        return false;
      for (SequenceStructureVariantComponent item : this.structureVariant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SequenceStructureVariantComponent addStructureVariant() { //3
      SequenceStructureVariantComponent t = new SequenceStructureVariantComponent();
      if (this.structureVariant == null)
        this.structureVariant = new ArrayList<SequenceStructureVariantComponent>();
      this.structureVariant.add(t);
      return t;
    }

    public Sequence addStructureVariant(SequenceStructureVariantComponent t) { //3
      if (t == null)
        return this;
      if (this.structureVariant == null)
        this.structureVariant = new ArrayList<SequenceStructureVariantComponent>();
      this.structureVariant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #structureVariant}, creating it if it does not already exist
     */
    public SequenceStructureVariantComponent getStructureVariantFirstRep() { 
      if (getStructureVariant().isEmpty()) {
        addStructureVariant();
      }
      return getStructureVariant().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A unique identifier for this particular sequence instance.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("type", "code", "Amino acid / cDNA transcript / RNA variant.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("coordinateSystem", "integer", "Whether the sequence is numbered starting at 0 (0-based numbering or coordinates) or starting at 1 (1-based numbering). Values are \"0\" for 0-based numbering and \"1\" for one-based.", 0, java.lang.Integer.MAX_VALUE, coordinateSystem));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient whose sequencing results are described by this resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("specimen", "Reference(Specimen)", "Specimen used for sequencing.", 0, java.lang.Integer.MAX_VALUE, specimen));
        childrenList.add(new Property("device", "Reference(Device)", "The method for sequencing, for example, chip information.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("quantity", "Quantity", "Quantity of the sequence.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("referenceSeq", "", "A reference sequence is a sequence that is used to represent an allele or variant.", 0, java.lang.Integer.MAX_VALUE, referenceSeq));
        childrenList.add(new Property("variant", "", "A' is a variant (mutation) of A = definition every instance of A' is either an immediate mutation of some instance of A, or there is a chain of immediate mutation processes linking A' to some instance of A ([variant_of](http://www.sequenceontology.org/browser/current_svn/term/variant_of)).", 0, java.lang.Integer.MAX_VALUE, variant));
        childrenList.add(new Property("observedSeq", "string", "Sequence that was observed.", 0, java.lang.Integer.MAX_VALUE, observedSeq));
        childrenList.add(new Property("quality", "", "An experimental feature attribute that defines the quality of the feature in a quantitative way, such as a phred quality score ([SO:0001686](http://www.sequenceontology.org/browser/current_svn/term/SO:0001686)).", 0, java.lang.Integer.MAX_VALUE, quality));
        childrenList.add(new Property("readCoverage", "integer", "Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.", 0, java.lang.Integer.MAX_VALUE, readCoverage));
        childrenList.add(new Property("repository", "", "Configurations of the external repository.", 0, java.lang.Integer.MAX_VALUE, repository));
        childrenList.add(new Property("pointer", "Reference(Sequence)", "Pointer to next atomic sequence which at most contains one variant.", 0, java.lang.Integer.MAX_VALUE, pointer));
        childrenList.add(new Property("structureVariant", "", "Structural variant.", 0, java.lang.Integer.MAX_VALUE, structureVariant));
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
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -502547180: /*referenceSeq*/ return this.referenceSeq == null ? new Base[0] : new Base[] {this.referenceSeq}; // SequenceReferenceSeqComponent
        case 236785797: /*variant*/ return this.variant == null ? new Base[0] : this.variant.toArray(new Base[this.variant.size()]); // SequenceVariantComponent
        case 125541495: /*observedSeq*/ return this.observedSeq == null ? new Base[0] : new Base[] {this.observedSeq}; // StringType
        case 651215103: /*quality*/ return this.quality == null ? new Base[0] : this.quality.toArray(new Base[this.quality.size()]); // SequenceQualityComponent
        case -1798816354: /*readCoverage*/ return this.readCoverage == null ? new Base[0] : new Base[] {this.readCoverage}; // IntegerType
        case 1950800714: /*repository*/ return this.repository == null ? new Base[0] : this.repository.toArray(new Base[this.repository.size()]); // SequenceRepositoryComponent
        case -400605635: /*pointer*/ return this.pointer == null ? new Base[0] : this.pointer.toArray(new Base[this.pointer.size()]); // Reference
        case 757269394: /*structureVariant*/ return this.structureVariant == null ? new Base[0] : this.structureVariant.toArray(new Base[this.structureVariant.size()]); // SequenceStructureVariantComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 3575610: // type
          this.type = new SequenceTypeEnumFactory().fromType(value); // Enumeration<SequenceType>
          break;
        case 354212295: // coordinateSystem
          this.coordinateSystem = castToInteger(value); // IntegerType
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -2132868344: // specimen
          this.specimen = castToReference(value); // Reference
          break;
        case -1335157162: // device
          this.device = castToReference(value); // Reference
          break;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          break;
        case -502547180: // referenceSeq
          this.referenceSeq = (SequenceReferenceSeqComponent) value; // SequenceReferenceSeqComponent
          break;
        case 236785797: // variant
          this.getVariant().add((SequenceVariantComponent) value); // SequenceVariantComponent
          break;
        case 125541495: // observedSeq
          this.observedSeq = castToString(value); // StringType
          break;
        case 651215103: // quality
          this.getQuality().add((SequenceQualityComponent) value); // SequenceQualityComponent
          break;
        case -1798816354: // readCoverage
          this.readCoverage = castToInteger(value); // IntegerType
          break;
        case 1950800714: // repository
          this.getRepository().add((SequenceRepositoryComponent) value); // SequenceRepositoryComponent
          break;
        case -400605635: // pointer
          this.getPointer().add(castToReference(value)); // Reference
          break;
        case 757269394: // structureVariant
          this.getStructureVariant().add((SequenceStructureVariantComponent) value); // SequenceStructureVariantComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("type"))
          this.type = new SequenceTypeEnumFactory().fromType(value); // Enumeration<SequenceType>
        else if (name.equals("coordinateSystem"))
          this.coordinateSystem = castToInteger(value); // IntegerType
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("specimen"))
          this.specimen = castToReference(value); // Reference
        else if (name.equals("device"))
          this.device = castToReference(value); // Reference
        else if (name.equals("quantity"))
          this.quantity = castToQuantity(value); // Quantity
        else if (name.equals("referenceSeq"))
          this.referenceSeq = (SequenceReferenceSeqComponent) value; // SequenceReferenceSeqComponent
        else if (name.equals("variant"))
          this.getVariant().add((SequenceVariantComponent) value);
        else if (name.equals("observedSeq"))
          this.observedSeq = castToString(value); // StringType
        else if (name.equals("quality"))
          this.getQuality().add((SequenceQualityComponent) value);
        else if (name.equals("readCoverage"))
          this.readCoverage = castToInteger(value); // IntegerType
        else if (name.equals("repository"))
          this.getRepository().add((SequenceRepositoryComponent) value);
        else if (name.equals("pointer"))
          this.getPointer().add(castToReference(value));
        else if (name.equals("structureVariant"))
          this.getStructureVariant().add((SequenceStructureVariantComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<SequenceType>
        case 354212295: throw new FHIRException("Cannot make property coordinateSystem as it is not a complex type"); // IntegerType
        case -791418107:  return getPatient(); // Reference
        case -2132868344:  return getSpecimen(); // Reference
        case -1335157162:  return getDevice(); // Reference
        case -1285004149:  return getQuantity(); // Quantity
        case -502547180:  return getReferenceSeq(); // SequenceReferenceSeqComponent
        case 236785797:  return addVariant(); // SequenceVariantComponent
        case 125541495: throw new FHIRException("Cannot make property observedSeq as it is not a complex type"); // StringType
        case 651215103:  return addQuality(); // SequenceQualityComponent
        case -1798816354: throw new FHIRException("Cannot make property readCoverage as it is not a complex type"); // IntegerType
        case 1950800714:  return addRepository(); // SequenceRepositoryComponent
        case -400605635:  return addPointer(); // Reference
        case 757269394:  return addStructureVariant(); // SequenceStructureVariantComponent
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("structureVariant")) {
          return addStructureVariant();
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
        if (structureVariant != null) {
          dst.structureVariant = new ArrayList<SequenceStructureVariantComponent>();
          for (SequenceStructureVariantComponent i : structureVariant)
            dst.structureVariant.add(i.copy());
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
           && compareDeep(quantity, o.quantity, true) && compareDeep(referenceSeq, o.referenceSeq, true) && compareDeep(variant, o.variant, true)
           && compareDeep(observedSeq, o.observedSeq, true) && compareDeep(quality, o.quality, true) && compareDeep(readCoverage, o.readCoverage, true)
           && compareDeep(repository, o.repository, true) && compareDeep(pointer, o.pointer, true) && compareDeep(structureVariant, o.structureVariant, true)
          ;
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
          , patient, specimen, device, quantity, referenceSeq, variant, observedSeq, quality
          , readCoverage, repository, pointer, structureVariant);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Sequence;
   }

 /**
   * Search parameter: <b>coordinate</b>
   * <p>
   * Description: <b>Genomic coordinate of the sequence. For example, a search for sequence in region 1:123-345 can be represented as `coordinate=1$lt345$gt123`</b><br>
   * Type: <b>composite</b><br>
   * Path: <b></b><br>
   * </p>
   */
  @SearchParamDefinition(name="coordinate", path="", description="Genomic coordinate of the sequence. For example, a search for sequence in region 1:123-345 can be represented as `coordinate=1$lt345$gt123`", type="composite", compositeOf={"chromosome", "start"} )
  public static final String SP_COORDINATE = "coordinate";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>coordinate</b>
   * <p>
   * Description: <b>Genomic coordinate of the sequence. For example, a search for sequence in region 1:123-345 can be represented as `coordinate=1$lt345$gt123`</b><br>
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
   * Description: <b>Chromosome of the sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.referenceSeq.chromosome</b><br>
   * </p>
   */
  @SearchParamDefinition(name="chromosome", path="Sequence.referenceSeq.chromosome", description="Chromosome of the sequence", type="token" )
  public static final String SP_CHROMOSOME = "chromosome";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>chromosome</b>
   * <p>
   * Description: <b>Chromosome of the sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.referenceSeq.chromosome</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CHROMOSOME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CHROMOSOME);

 /**
   * Search parameter: <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variant.start</b><br>
   * </p>
   */
  @SearchParamDefinition(name="start", path="Sequence.variant.start", description="Start position (0-based inclusive) of the sequence", type="number" )
  public static final String SP_START = "start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variant.start</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam START = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_START);

 /**
   * Search parameter: <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variant.end</b><br>
   * </p>
   */
  @SearchParamDefinition(name="end", path="Sequence.variant.end", description="End position (0-based exclusive) of the sequence", type="number" )
  public static final String SP_END = "end";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.variant.end</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam END = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_END);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The type of the variant: Amino acid / cDNA transcript / RNA variant.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Sequence.type", description="The type of the variant: Amino acid / cDNA transcript / RNA variant.", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The type of the variant: Amino acid / cDNA transcript / RNA variant.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

