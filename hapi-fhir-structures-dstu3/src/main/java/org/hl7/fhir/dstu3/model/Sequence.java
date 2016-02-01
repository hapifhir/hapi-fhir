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

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * Variation and Sequence data.
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
         * added to help the parsers
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
    public static class SequenceCoordinateComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.
         */
        @Child(name = "chromosome", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The chromosome containing the genetic finding", formalDefinition="The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used." )
        protected CodeableConcept chromosome;

        /**
         * Inclusive 0-based nucleotide position for start of genomic finding on the positive (+) genomics strand.
         */
        @Child(name = "start", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based start position (inclusive) of the sequence", formalDefinition="Inclusive 0-based nucleotide position for start of genomic finding on the positive (+) genomics strand." )
        protected IntegerType start;

        /**
         * Exclusive 0-based nucleotide position for end of genomic finding on the positive (+) genomic strand.
         */
        @Child(name = "end", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based end position (exclusive) of the sequence", formalDefinition="Exclusive 0-based nucleotide position for end of genomic finding on the positive (+) genomic strand." )
        protected IntegerType end;

        /**
         * The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.
         */
        @Child(name = "genomeBuild", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'", formalDefinition="The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used." )
        protected StringType genomeBuild;

        private static final long serialVersionUID = 1604914542L;

    /**
     * Constructor
     */
      public SequenceCoordinateComponent() {
        super();
      }

        /**
         * @return {@link #chromosome} (The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.)
         */
        public CodeableConcept getChromosome() { 
          if (this.chromosome == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceCoordinateComponent.chromosome");
            else if (Configuration.doAutoCreate())
              this.chromosome = new CodeableConcept(); // cc
          return this.chromosome;
        }

        public boolean hasChromosome() { 
          return this.chromosome != null && !this.chromosome.isEmpty();
        }

        /**
         * @param value {@link #chromosome} (The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.)
         */
        public SequenceCoordinateComponent setChromosome(CodeableConcept value) { 
          this.chromosome = value;
          return this;
        }

        /**
         * @return {@link #start} (Inclusive 0-based nucleotide position for start of genomic finding on the positive (+) genomics strand.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public IntegerType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceCoordinateComponent.start");
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
         * @param value {@link #start} (Inclusive 0-based nucleotide position for start of genomic finding on the positive (+) genomics strand.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceCoordinateComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Inclusive 0-based nucleotide position for start of genomic finding on the positive (+) genomics strand.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value Inclusive 0-based nucleotide position for start of genomic finding on the positive (+) genomics strand.
         */
        public SequenceCoordinateComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (Exclusive 0-based nucleotide position for end of genomic finding on the positive (+) genomic strand.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public IntegerType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceCoordinateComponent.end");
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
         * @param value {@link #end} (Exclusive 0-based nucleotide position for end of genomic finding on the positive (+) genomic strand.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceCoordinateComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return Exclusive 0-based nucleotide position for end of genomic finding on the positive (+) genomic strand.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value Exclusive 0-based nucleotide position for end of genomic finding on the positive (+) genomic strand.
         */
        public SequenceCoordinateComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        /**
         * @return {@link #genomeBuild} (The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.). This is the underlying object with id, value and extensions. The accessor "getGenomeBuild" gives direct access to the value
         */
        public StringType getGenomeBuildElement() { 
          if (this.genomeBuild == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceCoordinateComponent.genomeBuild");
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
        public SequenceCoordinateComponent setGenomeBuildElement(StringType value) { 
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
        public SequenceCoordinateComponent setGenomeBuild(String value) { 
          if (Utilities.noString(value))
            this.genomeBuild = null;
          else {
            if (this.genomeBuild == null)
              this.genomeBuild = new StringType();
            this.genomeBuild.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("chromosome", "CodeableConcept", "The chromosome containing the genetic finding. The value set will be 1-22, X, Y when the species is human without chromosome abnormality. Otherwise,  NCBI-Gene code system should be used.", 0, java.lang.Integer.MAX_VALUE, chromosome));
          childrenList.add(new Property("start", "integer", "Inclusive 0-based nucleotide position for start of genomic finding on the positive (+) genomics strand.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "Exclusive 0-based nucleotide position for end of genomic finding on the positive (+) genomic strand.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("genomeBuild", "string", "The Genome Build used for reference, following GRCh build versions e.g. 'GRCh 37'.  Version number must be included if a versioned release of a primary build was used.", 0, java.lang.Integer.MAX_VALUE, genomeBuild));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("chromosome"))
          this.chromosome = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("start"))
          this.start = castToInteger(value); // IntegerType
        else if (name.equals("end"))
          this.end = castToInteger(value); // IntegerType
        else if (name.equals("genomeBuild"))
          this.genomeBuild = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("chromosome")) {
          this.chromosome = new CodeableConcept();
          return this.chromosome;
        }
        else if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.end");
        }
        else if (name.equals("genomeBuild")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.genomeBuild");
        }
        else
          return super.addChild(name);
      }

      public SequenceCoordinateComponent copy() {
        SequenceCoordinateComponent dst = new SequenceCoordinateComponent();
        copyValues(dst);
        dst.chromosome = chromosome == null ? null : chromosome.copy();
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.genomeBuild = genomeBuild == null ? null : genomeBuild.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceCoordinateComponent))
          return false;
        SequenceCoordinateComponent o = (SequenceCoordinateComponent) other;
        return compareDeep(chromosome, o.chromosome, true) && compareDeep(start, o.start, true) && compareDeep(end, o.end, true)
           && compareDeep(genomeBuild, o.genomeBuild, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceCoordinateComponent))
          return false;
        SequenceCoordinateComponent o = (SequenceCoordinateComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(genomeBuild, o.genomeBuild, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (chromosome == null || chromosome.isEmpty()) && (start == null || start.isEmpty())
           && (end == null || end.isEmpty()) && (genomeBuild == null || genomeBuild.isEmpty());
      }

  public String fhirType() {
    return "Sequence.coordinate";

  }

  }

    @Block()
    public static class SequenceQualityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * 0-based start position (inclusive) of the sequence.
         */
        @Child(name = "start", type = {IntegerType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based start position (inclusive) of the sequence", formalDefinition="0-based start position (inclusive) of the sequence." )
        protected IntegerType start;

        /**
         * 0-based end position (exclusive) of the sequence.
         */
        @Child(name = "end", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="0-based end position (exclusive) of the sequence", formalDefinition="0-based end position (exclusive) of the sequence." )
        protected IntegerType end;

        /**
         * Quality score.
         */
        @Child(name = "score", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Quality score", formalDefinition="Quality score." )
        protected Quantity score;

        /**
         * Platform.
         */
        @Child(name = "platform", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Platform", formalDefinition="Platform." )
        protected StringType platform;

        private static final long serialVersionUID = 1391681700L;

    /**
     * Constructor
     */
      public SequenceQualityComponent() {
        super();
      }

        /**
         * @return {@link #start} (0-based start position (inclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
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
         * @param value {@link #start} (0-based start position (inclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public SequenceQualityComponent setStartElement(IntegerType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return 0-based start position (inclusive) of the sequence.
         */
        public int getStart() { 
          return this.start == null || this.start.isEmpty() ? 0 : this.start.getValue();
        }

        /**
         * @param value 0-based start position (inclusive) of the sequence.
         */
        public SequenceQualityComponent setStart(int value) { 
            if (this.start == null)
              this.start = new IntegerType();
            this.start.setValue(value);
          return this;
        }

        /**
         * @return {@link #end} (0-based end position (exclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
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
         * @param value {@link #end} (0-based end position (exclusive) of the sequence.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public SequenceQualityComponent setEndElement(IntegerType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return 0-based end position (exclusive) of the sequence.
         */
        public int getEnd() { 
          return this.end == null || this.end.isEmpty() ? 0 : this.end.getValue();
        }

        /**
         * @param value 0-based end position (exclusive) of the sequence.
         */
        public SequenceQualityComponent setEnd(int value) { 
            if (this.end == null)
              this.end = new IntegerType();
            this.end.setValue(value);
          return this;
        }

        /**
         * @return {@link #score} (Quality score.)
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
         * @param value {@link #score} (Quality score.)
         */
        public SequenceQualityComponent setScore(Quantity value) { 
          this.score = value;
          return this;
        }

        /**
         * @return {@link #platform} (Platform.). This is the underlying object with id, value and extensions. The accessor "getPlatform" gives direct access to the value
         */
        public StringType getPlatformElement() { 
          if (this.platform == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceQualityComponent.platform");
            else if (Configuration.doAutoCreate())
              this.platform = new StringType(); // bb
          return this.platform;
        }

        public boolean hasPlatformElement() { 
          return this.platform != null && !this.platform.isEmpty();
        }

        public boolean hasPlatform() { 
          return this.platform != null && !this.platform.isEmpty();
        }

        /**
         * @param value {@link #platform} (Platform.). This is the underlying object with id, value and extensions. The accessor "getPlatform" gives direct access to the value
         */
        public SequenceQualityComponent setPlatformElement(StringType value) { 
          this.platform = value;
          return this;
        }

        /**
         * @return Platform.
         */
        public String getPlatform() { 
          return this.platform == null ? null : this.platform.getValue();
        }

        /**
         * @param value Platform.
         */
        public SequenceQualityComponent setPlatform(String value) { 
          if (Utilities.noString(value))
            this.platform = null;
          else {
            if (this.platform == null)
              this.platform = new StringType();
            this.platform.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("start", "integer", "0-based start position (inclusive) of the sequence.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("end", "integer", "0-based end position (exclusive) of the sequence.", 0, java.lang.Integer.MAX_VALUE, end));
          childrenList.add(new Property("score", "Quantity", "Quality score.", 0, java.lang.Integer.MAX_VALUE, score));
          childrenList.add(new Property("platform", "string", "Platform.", 0, java.lang.Integer.MAX_VALUE, platform));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("start"))
          this.start = castToInteger(value); // IntegerType
        else if (name.equals("end"))
          this.end = castToInteger(value); // IntegerType
        else if (name.equals("score"))
          this.score = castToQuantity(value); // Quantity
        else if (name.equals("platform"))
          this.platform = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.start");
        }
        else if (name.equals("end")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.end");
        }
        else if (name.equals("score")) {
          this.score = new Quantity();
          return this.score;
        }
        else if (name.equals("platform")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.platform");
        }
        else
          return super.addChild(name);
      }

      public SequenceQualityComponent copy() {
        SequenceQualityComponent dst = new SequenceQualityComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.end = end == null ? null : end.copy();
        dst.score = score == null ? null : score.copy();
        dst.platform = platform == null ? null : platform.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceQualityComponent))
          return false;
        SequenceQualityComponent o = (SequenceQualityComponent) other;
        return compareDeep(start, o.start, true) && compareDeep(end, o.end, true) && compareDeep(score, o.score, true)
           && compareDeep(platform, o.platform, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceQualityComponent))
          return false;
        SequenceQualityComponent o = (SequenceQualityComponent) other;
        return compareValues(start, o.start, true) && compareValues(end, o.end, true) && compareValues(platform, o.platform, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (start == null || start.isEmpty()) && (end == null || end.isEmpty())
           && (score == null || score.isEmpty()) && (platform == null || platform.isEmpty());
      }

  public String fhirType() {
    return "Sequence.quality";

  }

  }

    @Block()
    public static class SequenceChipComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Chip id.
         */
        @Child(name = "chipId", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Chip id", formalDefinition="Chip id." )
        protected StringType chipId;

        /**
         * Chip manufacturer id.
         */
        @Child(name = "manufacturerId", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Chip manufacturer id", formalDefinition="Chip manufacturer id." )
        protected StringType manufacturerId;

        /**
         * Chip version.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Chip version", formalDefinition="Chip version." )
        protected StringType version;

        private static final long serialVersionUID = 1437362028L;

    /**
     * Constructor
     */
      public SequenceChipComponent() {
        super();
      }

        /**
         * @return {@link #chipId} (Chip id.). This is the underlying object with id, value and extensions. The accessor "getChipId" gives direct access to the value
         */
        public StringType getChipIdElement() { 
          if (this.chipId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceChipComponent.chipId");
            else if (Configuration.doAutoCreate())
              this.chipId = new StringType(); // bb
          return this.chipId;
        }

        public boolean hasChipIdElement() { 
          return this.chipId != null && !this.chipId.isEmpty();
        }

        public boolean hasChipId() { 
          return this.chipId != null && !this.chipId.isEmpty();
        }

        /**
         * @param value {@link #chipId} (Chip id.). This is the underlying object with id, value and extensions. The accessor "getChipId" gives direct access to the value
         */
        public SequenceChipComponent setChipIdElement(StringType value) { 
          this.chipId = value;
          return this;
        }

        /**
         * @return Chip id.
         */
        public String getChipId() { 
          return this.chipId == null ? null : this.chipId.getValue();
        }

        /**
         * @param value Chip id.
         */
        public SequenceChipComponent setChipId(String value) { 
          if (Utilities.noString(value))
            this.chipId = null;
          else {
            if (this.chipId == null)
              this.chipId = new StringType();
            this.chipId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #manufacturerId} (Chip manufacturer id.). This is the underlying object with id, value and extensions. The accessor "getManufacturerId" gives direct access to the value
         */
        public StringType getManufacturerIdElement() { 
          if (this.manufacturerId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceChipComponent.manufacturerId");
            else if (Configuration.doAutoCreate())
              this.manufacturerId = new StringType(); // bb
          return this.manufacturerId;
        }

        public boolean hasManufacturerIdElement() { 
          return this.manufacturerId != null && !this.manufacturerId.isEmpty();
        }

        public boolean hasManufacturerId() { 
          return this.manufacturerId != null && !this.manufacturerId.isEmpty();
        }

        /**
         * @param value {@link #manufacturerId} (Chip manufacturer id.). This is the underlying object with id, value and extensions. The accessor "getManufacturerId" gives direct access to the value
         */
        public SequenceChipComponent setManufacturerIdElement(StringType value) { 
          this.manufacturerId = value;
          return this;
        }

        /**
         * @return Chip manufacturer id.
         */
        public String getManufacturerId() { 
          return this.manufacturerId == null ? null : this.manufacturerId.getValue();
        }

        /**
         * @param value Chip manufacturer id.
         */
        public SequenceChipComponent setManufacturerId(String value) { 
          if (Utilities.noString(value))
            this.manufacturerId = null;
          else {
            if (this.manufacturerId == null)
              this.manufacturerId = new StringType();
            this.manufacturerId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #version} (Chip version.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceChipComponent.version");
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
         * @param value {@link #version} (Chip version.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public SequenceChipComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return Chip version.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value Chip version.
         */
        public SequenceChipComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("chipId", "string", "Chip id.", 0, java.lang.Integer.MAX_VALUE, chipId));
          childrenList.add(new Property("manufacturerId", "string", "Chip manufacturer id.", 0, java.lang.Integer.MAX_VALUE, manufacturerId));
          childrenList.add(new Property("version", "string", "Chip version.", 0, java.lang.Integer.MAX_VALUE, version));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("chipId"))
          this.chipId = castToString(value); // StringType
        else if (name.equals("manufacturerId"))
          this.manufacturerId = castToString(value); // StringType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("chipId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.chipId");
        }
        else if (name.equals("manufacturerId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.manufacturerId");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.version");
        }
        else
          return super.addChild(name);
      }

      public SequenceChipComponent copy() {
        SequenceChipComponent dst = new SequenceChipComponent();
        copyValues(dst);
        dst.chipId = chipId == null ? null : chipId.copy();
        dst.manufacturerId = manufacturerId == null ? null : manufacturerId.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceChipComponent))
          return false;
        SequenceChipComponent o = (SequenceChipComponent) other;
        return compareDeep(chipId, o.chipId, true) && compareDeep(manufacturerId, o.manufacturerId, true)
           && compareDeep(version, o.version, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceChipComponent))
          return false;
        SequenceChipComponent o = (SequenceChipComponent) other;
        return compareValues(chipId, o.chipId, true) && compareValues(manufacturerId, o.manufacturerId, true)
           && compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (chipId == null || chipId.isEmpty()) && (manufacturerId == null || manufacturerId.isEmpty())
           && (version == null || version.isEmpty());
      }

  public String fhirType() {
    return "Sequence.chip";

  }

  }

    @Block()
    public static class SequenceRepositoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * URI of a GA4GH repository which contains further details about the genetics data.
         */
        @Child(name = "url", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="URI of the repository", formalDefinition="URI of a GA4GH repository which contains further details about the genetics data." )
        protected UriType url;

        /**
         * URI of a GA4GH repository which contains further details about the genetics data.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of the repository", formalDefinition="URI of a GA4GH repository which contains further details about the genetics data." )
        protected StringType name;

        /**
         * URI of the page containing information about the structure of the repository.
         */
        @Child(name = "structure", type = {UriType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="URI of the page containing information about the structure of the repository", formalDefinition="URI of the page containing information about the structure of the repository." )
        protected UriType structure;

        /**
         * Id of the GA4GH call set that matches identity of patient. A CallSet in GA4GH represents an individual.
         */
        @Child(name = "variantId", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of a GA4GH variant", formalDefinition="Id of the GA4GH call set that matches identity of patient. A CallSet in GA4GH represents an individual." )
        protected StringType variantId;

        /**
         * Id of the GA4GH read group from which details about the sequence can be found. A read group in GA4GH represents a set of DNA reads processed the same way by the sequencer.
         */
        @Child(name = "readGroupSetId", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Id of a GA4GH read group", formalDefinition="Id of the GA4GH read group from which details about the sequence can be found. A read group in GA4GH represents a set of DNA reads processed the same way by the sequencer." )
        protected StringType readGroupSetId;

        private static final long serialVersionUID = -1249242292L;

    /**
     * Constructor
     */
      public SequenceRepositoryComponent() {
        super();
      }

        /**
         * @return {@link #url} (URI of a GA4GH repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
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
         * @param value {@link #url} (URI of a GA4GH repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
         */
        public SequenceRepositoryComponent setUrlElement(UriType value) { 
          this.url = value;
          return this;
        }

        /**
         * @return URI of a GA4GH repository which contains further details about the genetics data.
         */
        public String getUrl() { 
          return this.url == null ? null : this.url.getValue();
        }

        /**
         * @param value URI of a GA4GH repository which contains further details about the genetics data.
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
         * @return {@link #name} (URI of a GA4GH repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
         * @param value {@link #name} (URI of a GA4GH repository which contains further details about the genetics data.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public SequenceRepositoryComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return URI of a GA4GH repository which contains further details about the genetics data.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value URI of a GA4GH repository which contains further details about the genetics data.
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
         * @return {@link #structure} (URI of the page containing information about the structure of the repository.). This is the underlying object with id, value and extensions. The accessor "getStructure" gives direct access to the value
         */
        public UriType getStructureElement() { 
          if (this.structure == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.structure");
            else if (Configuration.doAutoCreate())
              this.structure = new UriType(); // bb
          return this.structure;
        }

        public boolean hasStructureElement() { 
          return this.structure != null && !this.structure.isEmpty();
        }

        public boolean hasStructure() { 
          return this.structure != null && !this.structure.isEmpty();
        }

        /**
         * @param value {@link #structure} (URI of the page containing information about the structure of the repository.). This is the underlying object with id, value and extensions. The accessor "getStructure" gives direct access to the value
         */
        public SequenceRepositoryComponent setStructureElement(UriType value) { 
          this.structure = value;
          return this;
        }

        /**
         * @return URI of the page containing information about the structure of the repository.
         */
        public String getStructure() { 
          return this.structure == null ? null : this.structure.getValue();
        }

        /**
         * @param value URI of the page containing information about the structure of the repository.
         */
        public SequenceRepositoryComponent setStructure(String value) { 
          if (Utilities.noString(value))
            this.structure = null;
          else {
            if (this.structure == null)
              this.structure = new UriType();
            this.structure.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #variantId} (Id of the GA4GH call set that matches identity of patient. A CallSet in GA4GH represents an individual.). This is the underlying object with id, value and extensions. The accessor "getVariantId" gives direct access to the value
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
         * @param value {@link #variantId} (Id of the GA4GH call set that matches identity of patient. A CallSet in GA4GH represents an individual.). This is the underlying object with id, value and extensions. The accessor "getVariantId" gives direct access to the value
         */
        public SequenceRepositoryComponent setVariantIdElement(StringType value) { 
          this.variantId = value;
          return this;
        }

        /**
         * @return Id of the GA4GH call set that matches identity of patient. A CallSet in GA4GH represents an individual.
         */
        public String getVariantId() { 
          return this.variantId == null ? null : this.variantId.getValue();
        }

        /**
         * @param value Id of the GA4GH call set that matches identity of patient. A CallSet in GA4GH represents an individual.
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
         * @return {@link #readGroupSetId} (Id of the GA4GH read group from which details about the sequence can be found. A read group in GA4GH represents a set of DNA reads processed the same way by the sequencer.). This is the underlying object with id, value and extensions. The accessor "getReadGroupSetId" gives direct access to the value
         */
        public StringType getReadGroupSetIdElement() { 
          if (this.readGroupSetId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SequenceRepositoryComponent.readGroupSetId");
            else if (Configuration.doAutoCreate())
              this.readGroupSetId = new StringType(); // bb
          return this.readGroupSetId;
        }

        public boolean hasReadGroupSetIdElement() { 
          return this.readGroupSetId != null && !this.readGroupSetId.isEmpty();
        }

        public boolean hasReadGroupSetId() { 
          return this.readGroupSetId != null && !this.readGroupSetId.isEmpty();
        }

        /**
         * @param value {@link #readGroupSetId} (Id of the GA4GH read group from which details about the sequence can be found. A read group in GA4GH represents a set of DNA reads processed the same way by the sequencer.). This is the underlying object with id, value and extensions. The accessor "getReadGroupSetId" gives direct access to the value
         */
        public SequenceRepositoryComponent setReadGroupSetIdElement(StringType value) { 
          this.readGroupSetId = value;
          return this;
        }

        /**
         * @return Id of the GA4GH read group from which details about the sequence can be found. A read group in GA4GH represents a set of DNA reads processed the same way by the sequencer.
         */
        public String getReadGroupSetId() { 
          return this.readGroupSetId == null ? null : this.readGroupSetId.getValue();
        }

        /**
         * @param value Id of the GA4GH read group from which details about the sequence can be found. A read group in GA4GH represents a set of DNA reads processed the same way by the sequencer.
         */
        public SequenceRepositoryComponent setReadGroupSetId(String value) { 
          if (Utilities.noString(value))
            this.readGroupSetId = null;
          else {
            if (this.readGroupSetId == null)
              this.readGroupSetId = new StringType();
            this.readGroupSetId.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("url", "uri", "URI of a GA4GH repository which contains further details about the genetics data.", 0, java.lang.Integer.MAX_VALUE, url));
          childrenList.add(new Property("name", "string", "URI of a GA4GH repository which contains further details about the genetics data.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("structure", "uri", "URI of the page containing information about the structure of the repository.", 0, java.lang.Integer.MAX_VALUE, structure));
          childrenList.add(new Property("variantId", "string", "Id of the GA4GH call set that matches identity of patient. A CallSet in GA4GH represents an individual.", 0, java.lang.Integer.MAX_VALUE, variantId));
          childrenList.add(new Property("readGroupSetId", "string", "Id of the GA4GH read group from which details about the sequence can be found. A read group in GA4GH represents a set of DNA reads processed the same way by the sequencer.", 0, java.lang.Integer.MAX_VALUE, readGroupSetId));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("structure"))
          this.structure = castToUri(value); // UriType
        else if (name.equals("variantId"))
          this.variantId = castToString(value); // StringType
        else if (name.equals("readGroupSetId"))
          this.readGroupSetId = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.url");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.name");
        }
        else if (name.equals("structure")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.structure");
        }
        else if (name.equals("variantId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.variantId");
        }
        else if (name.equals("readGroupSetId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.readGroupSetId");
        }
        else
          return super.addChild(name);
      }

      public SequenceRepositoryComponent copy() {
        SequenceRepositoryComponent dst = new SequenceRepositoryComponent();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.name = name == null ? null : name.copy();
        dst.structure = structure == null ? null : structure.copy();
        dst.variantId = variantId == null ? null : variantId.copy();
        dst.readGroupSetId = readGroupSetId == null ? null : readGroupSetId.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof SequenceRepositoryComponent))
          return false;
        SequenceRepositoryComponent o = (SequenceRepositoryComponent) other;
        return compareDeep(url, o.url, true) && compareDeep(name, o.name, true) && compareDeep(structure, o.structure, true)
           && compareDeep(variantId, o.variantId, true) && compareDeep(readGroupSetId, o.readGroupSetId, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof SequenceRepositoryComponent))
          return false;
        SequenceRepositoryComponent o = (SequenceRepositoryComponent) other;
        return compareValues(url, o.url, true) && compareValues(name, o.name, true) && compareValues(structure, o.structure, true)
           && compareValues(variantId, o.variantId, true) && compareValues(readGroupSetId, o.readGroupSetId, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (name == null || name.isEmpty())
           && (structure == null || structure.isEmpty()) && (variantId == null || variantId.isEmpty())
           && (readGroupSetId == null || readGroupSetId.isEmpty());
      }

  public String fhirType() {
    return "Sequence.repository";

  }

  }

    /**
     * Amino acid / cDNA transcript / RNA variation.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="AA | DNA | RNA", formalDefinition="Amino acid / cDNA transcript / RNA variation." )
    protected Enumeration<SequenceType> type;

    /**
     * Identifier for variant.  If a germline variant, ClinVar or dbSNP identifier should be used.  If a somatic variant, COSMIC identifier should be used, unless in ClinVar then either maybe used. Need to provide the code system used (ClinVar, dbSNP, COSMIC).
     */
    @Child(name = "variationID", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifier for variant and ClinVar, dbSNP or COSMIC identifier should be used", formalDefinition="Identifier for variant.  If a germline variant, ClinVar or dbSNP identifier should be used.  If a somatic variant, COSMIC identifier should be used, unless in ClinVar then either maybe used. Need to provide the code system used (ClinVar, dbSNP, COSMIC)." )
    protected List<CodeableConcept> variationID;

    /**
     * Reference identifier for cDNA transcript/protein, with version, from NCBI's RefSeq or ENSEMBL. This reference sequence identifier must match the type in the Sequence.type field.
     */
    @Child(name = "referenceSeq", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reference identifier.  It must match the type in the Sequence.type field", formalDefinition="Reference identifier for cDNA transcript/protein, with version, from NCBI's RefSeq or ENSEMBL. This reference sequence identifier must match the type in the Sequence.type field." )
    protected CodeableConcept referenceSeq;

    /**
     * Quantity of the sequence.
     */
    @Child(name = "quantity", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Quantity of the sequence", formalDefinition="Quantity of the sequence." )
    protected Quantity quantity;

    /**
     * The coordinate of the variant.
     */
    @Child(name = "coordinate", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The coordinate of the variant", formalDefinition="The coordinate of the variant." )
    protected List<SequenceCoordinateComponent> coordinate;

    /**
     * The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria.
     */
    @Child(name = "species", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Supporting tests of human, viruses, and bacteria", formalDefinition="The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria." )
    protected CodeableConcept species;

    /**
     * Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
     */
    @Child(name = "observedAllele", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Nucleotide(s)/amino acids from start position of sequence to stop position of observed sequence", formalDefinition="Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand." )
    protected StringType observedAllele;

    /**
     * Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
     */
    @Child(name = "referenceAllele", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Nucleotide(s)/amino acids from start position of sequence to stop position of reference sequence", formalDefinition="Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand." )
    protected StringType referenceAllele;

    /**
     * Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).
     */
    @Child(name = "cigar", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Extended CIGAR string for aligning the sequence with reference bases", formalDefinition="Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm)." )
    protected StringType cigar;

    /**
     * Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing.
     */
    @Child(name = "quality", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Sequence Quality", formalDefinition="Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing." )
    protected List<SequenceQualityComponent> quality;

    /**
     * The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA.
     */
    @Child(name = "allelicState", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The level of occurrence of a single DNA Sequence Variation within a set of chromosomes: Heteroplasmic / Homoplasmic / Homozygous / Heterozygous / Hemizygous", formalDefinition="The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA." )
    protected CodeableConcept allelicState;

    /**
     * Allele frequencies.
     */
    @Child(name = "allelicFrequency", type = {DecimalType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Allele frequencies", formalDefinition="Allele frequencies." )
    protected DecimalType allelicFrequency;

    /**
     * Values: amplificaiton / deletion / LOH.
     */
    @Child(name = "copyNumberEvent", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Copy Number Event: Values: amplificaiton / deletion / LOH", formalDefinition="Values: amplificaiton / deletion / LOH." )
    protected CodeableConcept copyNumberEvent;

    /**
     * Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.
     */
    @Child(name = "readCoverage", type = {IntegerType.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Average number of reads representing a given nucleotide in the reconstructed sequence", formalDefinition="Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence." )
    protected IntegerType readCoverage;

    /**
     * Information of chip.
     */
    @Child(name = "chip", type = {}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Information of chip", formalDefinition="Information of chip." )
    protected SequenceChipComponent chip;

    /**
     * External repository.
     */
    @Child(name = "repository", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External repository", formalDefinition="External repository." )
    protected List<SequenceRepositoryComponent> repository;

    private static final long serialVersionUID = 417569089L;

  /**
   * Constructor
   */
    public Sequence() {
      super();
    }

  /**
   * Constructor
   */
    public Sequence(Enumeration<SequenceType> type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #type} (Amino acid / cDNA transcript / RNA variation.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
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
     * @param value {@link #type} (Amino acid / cDNA transcript / RNA variation.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Sequence setTypeElement(Enumeration<SequenceType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return Amino acid / cDNA transcript / RNA variation.
     */
    public SequenceType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value Amino acid / cDNA transcript / RNA variation.
     */
    public Sequence setType(SequenceType value) { 
        if (this.type == null)
          this.type = new Enumeration<SequenceType>(new SequenceTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #variationID} (Identifier for variant.  If a germline variant, ClinVar or dbSNP identifier should be used.  If a somatic variant, COSMIC identifier should be used, unless in ClinVar then either maybe used. Need to provide the code system used (ClinVar, dbSNP, COSMIC).)
     */
    public List<CodeableConcept> getVariationID() { 
      if (this.variationID == null)
        this.variationID = new ArrayList<CodeableConcept>();
      return this.variationID;
    }

    public boolean hasVariationID() { 
      if (this.variationID == null)
        return false;
      for (CodeableConcept item : this.variationID)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #variationID} (Identifier for variant.  If a germline variant, ClinVar or dbSNP identifier should be used.  If a somatic variant, COSMIC identifier should be used, unless in ClinVar then either maybe used. Need to provide the code system used (ClinVar, dbSNP, COSMIC).)
     */
    // syntactic sugar
    public CodeableConcept addVariationID() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.variationID == null)
        this.variationID = new ArrayList<CodeableConcept>();
      this.variationID.add(t);
      return t;
    }

    // syntactic sugar
    public Sequence addVariationID(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.variationID == null)
        this.variationID = new ArrayList<CodeableConcept>();
      this.variationID.add(t);
      return this;
    }

    /**
     * @return {@link #referenceSeq} (Reference identifier for cDNA transcript/protein, with version, from NCBI's RefSeq or ENSEMBL. This reference sequence identifier must match the type in the Sequence.type field.)
     */
    public CodeableConcept getReferenceSeq() { 
      if (this.referenceSeq == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.referenceSeq");
        else if (Configuration.doAutoCreate())
          this.referenceSeq = new CodeableConcept(); // cc
      return this.referenceSeq;
    }

    public boolean hasReferenceSeq() { 
      return this.referenceSeq != null && !this.referenceSeq.isEmpty();
    }

    /**
     * @param value {@link #referenceSeq} (Reference identifier for cDNA transcript/protein, with version, from NCBI's RefSeq or ENSEMBL. This reference sequence identifier must match the type in the Sequence.type field.)
     */
    public Sequence setReferenceSeq(CodeableConcept value) { 
      this.referenceSeq = value;
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
     * @return {@link #coordinate} (The coordinate of the variant.)
     */
    public List<SequenceCoordinateComponent> getCoordinate() { 
      if (this.coordinate == null)
        this.coordinate = new ArrayList<SequenceCoordinateComponent>();
      return this.coordinate;
    }

    public boolean hasCoordinate() { 
      if (this.coordinate == null)
        return false;
      for (SequenceCoordinateComponent item : this.coordinate)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #coordinate} (The coordinate of the variant.)
     */
    // syntactic sugar
    public SequenceCoordinateComponent addCoordinate() { //3
      SequenceCoordinateComponent t = new SequenceCoordinateComponent();
      if (this.coordinate == null)
        this.coordinate = new ArrayList<SequenceCoordinateComponent>();
      this.coordinate.add(t);
      return t;
    }

    // syntactic sugar
    public Sequence addCoordinate(SequenceCoordinateComponent t) { //3
      if (t == null)
        return this;
      if (this.coordinate == null)
        this.coordinate = new ArrayList<SequenceCoordinateComponent>();
      this.coordinate.add(t);
      return this;
    }

    /**
     * @return {@link #species} (The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria.)
     */
    public CodeableConcept getSpecies() { 
      if (this.species == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.species");
        else if (Configuration.doAutoCreate())
          this.species = new CodeableConcept(); // cc
      return this.species;
    }

    public boolean hasSpecies() { 
      return this.species != null && !this.species.isEmpty();
    }

    /**
     * @param value {@link #species} (The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria.)
     */
    public Sequence setSpecies(CodeableConcept value) { 
      this.species = value;
      return this;
    }

    /**
     * @return {@link #observedAllele} (Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getObservedAllele" gives direct access to the value
     */
    public StringType getObservedAlleleElement() { 
      if (this.observedAllele == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.observedAllele");
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
     * @param value {@link #observedAllele} (Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getObservedAllele" gives direct access to the value
     */
    public Sequence setObservedAlleleElement(StringType value) { 
      this.observedAllele = value;
      return this;
    }

    /**
     * @return Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
     */
    public String getObservedAllele() { 
      return this.observedAllele == null ? null : this.observedAllele.getValue();
    }

    /**
     * @param value Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
     */
    public Sequence setObservedAllele(String value) { 
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
     * @return {@link #referenceAllele} (Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getReferenceAllele" gives direct access to the value
     */
    public StringType getReferenceAlleleElement() { 
      if (this.referenceAllele == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.referenceAllele");
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
     * @param value {@link #referenceAllele} (Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.). This is the underlying object with id, value and extensions. The accessor "getReferenceAllele" gives direct access to the value
     */
    public Sequence setReferenceAlleleElement(StringType value) { 
      this.referenceAllele = value;
      return this;
    }

    /**
     * @return Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
     */
    public String getReferenceAllele() { 
      return this.referenceAllele == null ? null : this.referenceAllele.getValue();
    }

    /**
     * @param value Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.
     */
    public Sequence setReferenceAllele(String value) { 
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
          throw new Error("Attempt to auto-create Sequence.cigar");
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
    public Sequence setCigarElement(StringType value) { 
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
    public Sequence setCigar(String value) { 
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
     * @return {@link #quality} (Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing.)
     */
    public List<SequenceQualityComponent> getQuality() { 
      if (this.quality == null)
        this.quality = new ArrayList<SequenceQualityComponent>();
      return this.quality;
    }

    public boolean hasQuality() { 
      if (this.quality == null)
        return false;
      for (SequenceQualityComponent item : this.quality)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #quality} (Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing.)
     */
    // syntactic sugar
    public SequenceQualityComponent addQuality() { //3
      SequenceQualityComponent t = new SequenceQualityComponent();
      if (this.quality == null)
        this.quality = new ArrayList<SequenceQualityComponent>();
      this.quality.add(t);
      return t;
    }

    // syntactic sugar
    public Sequence addQuality(SequenceQualityComponent t) { //3
      if (t == null)
        return this;
      if (this.quality == null)
        this.quality = new ArrayList<SequenceQualityComponent>();
      this.quality.add(t);
      return this;
    }

    /**
     * @return {@link #allelicState} (The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA.)
     */
    public CodeableConcept getAllelicState() { 
      if (this.allelicState == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.allelicState");
        else if (Configuration.doAutoCreate())
          this.allelicState = new CodeableConcept(); // cc
      return this.allelicState;
    }

    public boolean hasAllelicState() { 
      return this.allelicState != null && !this.allelicState.isEmpty();
    }

    /**
     * @param value {@link #allelicState} (The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA.)
     */
    public Sequence setAllelicState(CodeableConcept value) { 
      this.allelicState = value;
      return this;
    }

    /**
     * @return {@link #allelicFrequency} (Allele frequencies.). This is the underlying object with id, value and extensions. The accessor "getAllelicFrequency" gives direct access to the value
     */
    public DecimalType getAllelicFrequencyElement() { 
      if (this.allelicFrequency == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.allelicFrequency");
        else if (Configuration.doAutoCreate())
          this.allelicFrequency = new DecimalType(); // bb
      return this.allelicFrequency;
    }

    public boolean hasAllelicFrequencyElement() { 
      return this.allelicFrequency != null && !this.allelicFrequency.isEmpty();
    }

    public boolean hasAllelicFrequency() { 
      return this.allelicFrequency != null && !this.allelicFrequency.isEmpty();
    }

    /**
     * @param value {@link #allelicFrequency} (Allele frequencies.). This is the underlying object with id, value and extensions. The accessor "getAllelicFrequency" gives direct access to the value
     */
    public Sequence setAllelicFrequencyElement(DecimalType value) { 
      this.allelicFrequency = value;
      return this;
    }

    /**
     * @return Allele frequencies.
     */
    public BigDecimal getAllelicFrequency() { 
      return this.allelicFrequency == null ? null : this.allelicFrequency.getValue();
    }

    /**
     * @param value Allele frequencies.
     */
    public Sequence setAllelicFrequency(BigDecimal value) { 
      if (value == null)
        this.allelicFrequency = null;
      else {
        if (this.allelicFrequency == null)
          this.allelicFrequency = new DecimalType();
        this.allelicFrequency.setValue(value);
      }
      return this;
    }

    /**
     * @param value Allele frequencies.
     */
    public Sequence setAllelicFrequency(long value) { 
          this.allelicFrequency = new DecimalType();
        this.allelicFrequency.setValue(value);
      return this;
    }

    /**
     * @param value Allele frequencies.
     */
    public Sequence setAllelicFrequency(double value) { 
          this.allelicFrequency = new DecimalType();
        this.allelicFrequency.setValue(value);
      return this;
    }

    /**
     * @return {@link #copyNumberEvent} (Values: amplificaiton / deletion / LOH.)
     */
    public CodeableConcept getCopyNumberEvent() { 
      if (this.copyNumberEvent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.copyNumberEvent");
        else if (Configuration.doAutoCreate())
          this.copyNumberEvent = new CodeableConcept(); // cc
      return this.copyNumberEvent;
    }

    public boolean hasCopyNumberEvent() { 
      return this.copyNumberEvent != null && !this.copyNumberEvent.isEmpty();
    }

    /**
     * @param value {@link #copyNumberEvent} (Values: amplificaiton / deletion / LOH.)
     */
    public Sequence setCopyNumberEvent(CodeableConcept value) { 
      this.copyNumberEvent = value;
      return this;
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
     * @return {@link #chip} (Information of chip.)
     */
    public SequenceChipComponent getChip() { 
      if (this.chip == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Sequence.chip");
        else if (Configuration.doAutoCreate())
          this.chip = new SequenceChipComponent(); // cc
      return this.chip;
    }

    public boolean hasChip() { 
      return this.chip != null && !this.chip.isEmpty();
    }

    /**
     * @param value {@link #chip} (Information of chip.)
     */
    public Sequence setChip(SequenceChipComponent value) { 
      this.chip = value;
      return this;
    }

    /**
     * @return {@link #repository} (External repository.)
     */
    public List<SequenceRepositoryComponent> getRepository() { 
      if (this.repository == null)
        this.repository = new ArrayList<SequenceRepositoryComponent>();
      return this.repository;
    }

    public boolean hasRepository() { 
      if (this.repository == null)
        return false;
      for (SequenceRepositoryComponent item : this.repository)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #repository} (External repository.)
     */
    // syntactic sugar
    public SequenceRepositoryComponent addRepository() { //3
      SequenceRepositoryComponent t = new SequenceRepositoryComponent();
      if (this.repository == null)
        this.repository = new ArrayList<SequenceRepositoryComponent>();
      this.repository.add(t);
      return t;
    }

    // syntactic sugar
    public Sequence addRepository(SequenceRepositoryComponent t) { //3
      if (t == null)
        return this;
      if (this.repository == null)
        this.repository = new ArrayList<SequenceRepositoryComponent>();
      this.repository.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "Amino acid / cDNA transcript / RNA variation.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("variationID", "CodeableConcept", "Identifier for variant.  If a germline variant, ClinVar or dbSNP identifier should be used.  If a somatic variant, COSMIC identifier should be used, unless in ClinVar then either maybe used. Need to provide the code system used (ClinVar, dbSNP, COSMIC).", 0, java.lang.Integer.MAX_VALUE, variationID));
        childrenList.add(new Property("referenceSeq", "CodeableConcept", "Reference identifier for cDNA transcript/protein, with version, from NCBI's RefSeq or ENSEMBL. This reference sequence identifier must match the type in the Sequence.type field.", 0, java.lang.Integer.MAX_VALUE, referenceSeq));
        childrenList.add(new Property("quantity", "Quantity", "Quantity of the sequence.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("coordinate", "", "The coordinate of the variant.", 0, java.lang.Integer.MAX_VALUE, coordinate));
        childrenList.add(new Property("species", "CodeableConcept", "The organism from which sample of the sequence was extracted. Supporting tests of human, viruses, and bacteria.", 0, java.lang.Integer.MAX_VALUE, species));
        childrenList.add(new Property("observedAllele", "string", "Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the observed sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.", 0, java.lang.Integer.MAX_VALUE, observedAllele));
        childrenList.add(new Property("referenceAllele", "string", "Nucleotide(s)/amino acids from start position of sequence to stop position of sequence on the positive (+) strand of the reference sequence. When the sequence  type is DNA, it should be the sequence on the positive (+) strand.", 0, java.lang.Integer.MAX_VALUE, referenceAllele));
        childrenList.add(new Property("cigar", "string", "Extended CIGAR string for aligning the sequence with reference bases. See detailed documentation [here](http://support.illumina.com/help/SequencingAnalysisWorkflow/Content/Vault/Informatics/Sequencing_Analysis/CASAVA/swSEQ_mCA_ExtendedCIGARFormat.htm).", 0, java.lang.Integer.MAX_VALUE, cigar));
        childrenList.add(new Property("quality", "", "Quality for sequence quality vary by platform reflecting differences in sequencing chemistry and digital processing.", 0, java.lang.Integer.MAX_VALUE, quality));
        childrenList.add(new Property("allelicState", "CodeableConcept", "The level of occurrence of a single DNA Sequence Variation within a set of chromosomes. Heterozygous indicates the DNA Sequence Variation is only present in one of the two genes contained in homologous chromosomes. Homozygous indicates the DNA Sequence Variation is present in both genes contained in homologous chromosomes. Hemizygous indicates the DNA Sequence Variation exists in the only single copy of a gene in a non- homologous chromosome (the male X and Y chromosome are non-homologous). Hemiplasmic indicates that the DNA Sequence Variation is present in some but not all of the copies of mitochondrial DNA. Homoplasmic indicates that the DNA Sequence Variation is present in all of the copies of mitochondrial DNA.", 0, java.lang.Integer.MAX_VALUE, allelicState));
        childrenList.add(new Property("allelicFrequency", "decimal", "Allele frequencies.", 0, java.lang.Integer.MAX_VALUE, allelicFrequency));
        childrenList.add(new Property("copyNumberEvent", "CodeableConcept", "Values: amplificaiton / deletion / LOH.", 0, java.lang.Integer.MAX_VALUE, copyNumberEvent));
        childrenList.add(new Property("readCoverage", "integer", "Coverage (read depth or depth) is the average number of reads representing a given nucleotide in the reconstructed sequence.", 0, java.lang.Integer.MAX_VALUE, readCoverage));
        childrenList.add(new Property("chip", "", "Information of chip.", 0, java.lang.Integer.MAX_VALUE, chip));
        childrenList.add(new Property("repository", "", "External repository.", 0, java.lang.Integer.MAX_VALUE, repository));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new SequenceTypeEnumFactory().fromType(value); // Enumeration<SequenceType>
        else if (name.equals("variationID"))
          this.getVariationID().add(castToCodeableConcept(value));
        else if (name.equals("referenceSeq"))
          this.referenceSeq = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("quantity"))
          this.quantity = castToQuantity(value); // Quantity
        else if (name.equals("coordinate"))
          this.getCoordinate().add((SequenceCoordinateComponent) value);
        else if (name.equals("species"))
          this.species = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("observedAllele"))
          this.observedAllele = castToString(value); // StringType
        else if (name.equals("referenceAllele"))
          this.referenceAllele = castToString(value); // StringType
        else if (name.equals("cigar"))
          this.cigar = castToString(value); // StringType
        else if (name.equals("quality"))
          this.getQuality().add((SequenceQualityComponent) value);
        else if (name.equals("allelicState"))
          this.allelicState = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("allelicFrequency"))
          this.allelicFrequency = castToDecimal(value); // DecimalType
        else if (name.equals("copyNumberEvent"))
          this.copyNumberEvent = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("readCoverage"))
          this.readCoverage = castToInteger(value); // IntegerType
        else if (name.equals("chip"))
          this.chip = (SequenceChipComponent) value; // SequenceChipComponent
        else if (name.equals("repository"))
          this.getRepository().add((SequenceRepositoryComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.type");
        }
        else if (name.equals("variationID")) {
          return addVariationID();
        }
        else if (name.equals("referenceSeq")) {
          this.referenceSeq = new CodeableConcept();
          return this.referenceSeq;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("coordinate")) {
          return addCoordinate();
        }
        else if (name.equals("species")) {
          this.species = new CodeableConcept();
          return this.species;
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
        else if (name.equals("quality")) {
          return addQuality();
        }
        else if (name.equals("allelicState")) {
          this.allelicState = new CodeableConcept();
          return this.allelicState;
        }
        else if (name.equals("allelicFrequency")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.allelicFrequency");
        }
        else if (name.equals("copyNumberEvent")) {
          this.copyNumberEvent = new CodeableConcept();
          return this.copyNumberEvent;
        }
        else if (name.equals("readCoverage")) {
          throw new FHIRException("Cannot call addChild on a primitive type Sequence.readCoverage");
        }
        else if (name.equals("chip")) {
          this.chip = new SequenceChipComponent();
          return this.chip;
        }
        else if (name.equals("repository")) {
          return addRepository();
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
        dst.type = type == null ? null : type.copy();
        if (variationID != null) {
          dst.variationID = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : variationID)
            dst.variationID.add(i.copy());
        };
        dst.referenceSeq = referenceSeq == null ? null : referenceSeq.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        if (coordinate != null) {
          dst.coordinate = new ArrayList<SequenceCoordinateComponent>();
          for (SequenceCoordinateComponent i : coordinate)
            dst.coordinate.add(i.copy());
        };
        dst.species = species == null ? null : species.copy();
        dst.observedAllele = observedAllele == null ? null : observedAllele.copy();
        dst.referenceAllele = referenceAllele == null ? null : referenceAllele.copy();
        dst.cigar = cigar == null ? null : cigar.copy();
        if (quality != null) {
          dst.quality = new ArrayList<SequenceQualityComponent>();
          for (SequenceQualityComponent i : quality)
            dst.quality.add(i.copy());
        };
        dst.allelicState = allelicState == null ? null : allelicState.copy();
        dst.allelicFrequency = allelicFrequency == null ? null : allelicFrequency.copy();
        dst.copyNumberEvent = copyNumberEvent == null ? null : copyNumberEvent.copy();
        dst.readCoverage = readCoverage == null ? null : readCoverage.copy();
        dst.chip = chip == null ? null : chip.copy();
        if (repository != null) {
          dst.repository = new ArrayList<SequenceRepositoryComponent>();
          for (SequenceRepositoryComponent i : repository)
            dst.repository.add(i.copy());
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
        return compareDeep(type, o.type, true) && compareDeep(variationID, o.variationID, true) && compareDeep(referenceSeq, o.referenceSeq, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(coordinate, o.coordinate, true) && compareDeep(species, o.species, true)
           && compareDeep(observedAllele, o.observedAllele, true) && compareDeep(referenceAllele, o.referenceAllele, true)
           && compareDeep(cigar, o.cigar, true) && compareDeep(quality, o.quality, true) && compareDeep(allelicState, o.allelicState, true)
           && compareDeep(allelicFrequency, o.allelicFrequency, true) && compareDeep(copyNumberEvent, o.copyNumberEvent, true)
           && compareDeep(readCoverage, o.readCoverage, true) && compareDeep(chip, o.chip, true) && compareDeep(repository, o.repository, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Sequence))
          return false;
        Sequence o = (Sequence) other;
        return compareValues(type, o.type, true) && compareValues(observedAllele, o.observedAllele, true) && compareValues(referenceAllele, o.referenceAllele, true)
           && compareValues(cigar, o.cigar, true) && compareValues(allelicFrequency, o.allelicFrequency, true)
           && compareValues(readCoverage, o.readCoverage, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (variationID == null || variationID.isEmpty())
           && (referenceSeq == null || referenceSeq.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (coordinate == null || coordinate.isEmpty()) && (species == null || species.isEmpty())
           && (observedAllele == null || observedAllele.isEmpty()) && (referenceAllele == null || referenceAllele.isEmpty())
           && (cigar == null || cigar.isEmpty()) && (quality == null || quality.isEmpty()) && (allelicState == null || allelicState.isEmpty())
           && (allelicFrequency == null || allelicFrequency.isEmpty()) && (copyNumberEvent == null || copyNumberEvent.isEmpty())
           && (readCoverage == null || readCoverage.isEmpty()) && (chip == null || chip.isEmpty()) && (repository == null || repository.isEmpty())
          ;
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
   * Search parameter: <b>variationid</b>
   * <p>
   * Description: <b>Identifier for variant</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.variationID</b><br>
   * </p>
   */
  @SearchParamDefinition(name="variationid", path="Sequence.variationID", description="Identifier for variant", type="token" )
  public static final String SP_VARIATIONID = "variationid";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>variationid</b>
   * <p>
   * Description: <b>Identifier for variant</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.variationID</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VARIATIONID = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VARIATIONID);

 /**
   * Search parameter: <b>species</b>
   * <p>
   * Description: <b>The organism from which sample of the sequence was extracted.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.species</b><br>
   * </p>
   */
  @SearchParamDefinition(name="species", path="Sequence.species", description="The organism from which sample of the sequence was extracted.", type="token" )
  public static final String SP_SPECIES = "species";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>species</b>
   * <p>
   * Description: <b>The organism from which sample of the sequence was extracted.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.species</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SPECIES = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SPECIES);

 /**
   * Search parameter: <b>chromosome</b>
   * <p>
   * Description: <b>Chromosome of the sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.coordinate.chromosome</b><br>
   * </p>
   */
  @SearchParamDefinition(name="chromosome", path="Sequence.coordinate.chromosome", description="Chromosome of the sequence", type="token" )
  public static final String SP_CHROMOSOME = "chromosome";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>chromosome</b>
   * <p>
   * Description: <b>Chromosome of the sequence</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.coordinate.chromosome</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CHROMOSOME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CHROMOSOME);

 /**
   * Search parameter: <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.coordinate.start</b><br>
   * </p>
   */
  @SearchParamDefinition(name="start", path="Sequence.coordinate.start", description="Start position (0-based inclusive) of the sequence", type="number" )
  public static final String SP_START = "start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>start</b>
   * <p>
   * Description: <b>Start position (0-based inclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.coordinate.start</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam START = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_START);

 /**
   * Search parameter: <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.coordinate.end</b><br>
   * </p>
   */
  @SearchParamDefinition(name="end", path="Sequence.coordinate.end", description="End position (0-based exclusive) of the sequence", type="number" )
  public static final String SP_END = "end";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>end</b>
   * <p>
   * Description: <b>End position (0-based exclusive) of the sequence</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Sequence.coordinate.end</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam END = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_END);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The type of the variant: Amino acid / cDNA transcript / RNA variation.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Sequence.type", description="The type of the variant: Amino acid / cDNA transcript / RNA variation.", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The type of the variant: Amino acid / cDNA transcript / RNA variation.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Sequence.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);


}

